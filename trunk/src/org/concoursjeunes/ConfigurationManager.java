/*
 * Créer le 17 août 07 à 10:20:23 pour ConcoursJeunes
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.concoursjeunes;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.naming.ConfigurationException;
import javax.xml.bind.JAXBException;

import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.utilities.io.FileUtil;

/**
 * Gére le chargement de la configuration du programme
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class ConfigurationManager {
	
	/**
	 * Charge la configuration courante de l'application
	 * 
	 * @return la configuation courante
	 */
	public static Configuration loadCurrentConfiguration() throws IOException {
		File confFile = new File(userRessources.getConfigPathForUser() + File.separator + 
				ajrParametreAppli.getResourceString("file.configuration")); //$NON-NLS-1$

		return loadConfiguration(confFile);
	}
	
	/**
	 * Charge la configuration nommé en parametre
	 * 
	 * @param profilename
	 * @return la configuration nommé
	 */
	public static Configuration loadConfiguration(String profilename) throws IOException {
		return loadConfiguration(new File(ConcoursJeunes.userRessources.getConfigPathForUser() 
				+ File.separator + "configuration_" + profilename + ".xml")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Charge le fichier de configuration transmit en parametre
	 * 
	 * @param confFile le fichier de configuration à charger
	 * @return l'objet configuration chargé
	 */
	@SuppressWarnings("deprecation")
	public static Configuration loadConfiguration(File confFile) throws IOException {
		Configuration configuration = null;
		//tente de charger la configuration
		try {
			configuration = (Configuration)AJToolKit.loadMarshallStructure(confFile, Configuration.class);
			if(configuration == null) {
				configuration = ConfigurationBuilder.getDefaultConfiguration();
			}
			
			//changement suite à la dispartion de la ligue du dauphiné
			if(configuration.getClub().getAgrement().startsWith("16")) { //$NON-NLS-1$
				configuration.getClub().setAgrement("33" + configuration.getClub().getAgrement().substring(2)); //$NON-NLS-1$
			}
		
		//si il n'y arrive pas vérifie que ce n'est pas une config 1.1
		} catch (JAXBException e) {
			//couche de compatibilite avec le XML de la 1.1
			ajinteractive.concours.Configuration oldConfig = (ajinteractive.concours.Configuration)AJToolKit.loadXMLStructure(confFile, false);
			configuration = ConfigurationBuilder.getDefaultConfiguration();
			if(oldConfig != null) {
				//Etablie la correspondance entre les methodes 2.0+ et les 1.1
				Entite entite = new Entite(oldConfig.getNomClub(), Entite.CLUB);
				entite.setAgrement(oldConfig.getNumAgrement());
				
				configuration.setClub(entite);
				configuration.setIntituleConcours(oldConfig.getIntituleConcours());
				configuration.setNbCible(oldConfig.getNbCible());
				configuration.setNbTireur(oldConfig.getNbTireur());
				configuration.setNbDepart(oldConfig.getNbDepart());
				configuration.setLangue(oldConfig.getLangue());
				configuration.setLogoPath(oldConfig.getLogoPath());
				configuration.setPdfReaderPath(oldConfig.getPdfReaderPath());
				configuration.setFormatPapier(oldConfig.getFormatPapier());
				configuration.setOrientation(oldConfig.getOrientation());
				configuration.setColonneAndLigne(oldConfig.getColonneAndLigne());
				configuration.setMarges(oldConfig.getMarges());
				configuration.setEspacements(oldConfig.getEspacements());
				configuration.setInterfaceResultatCumul(oldConfig.isInterfaceResultatCumul());
				configuration.setInterfaceResultatSupl(oldConfig.isInterfaceResultatSupl());
				configuration.setInterfaceAffResultatExEquo(oldConfig.isInterfaceAffResultatExEquo());
				configuration.setFirstboot(oldConfig.isFirstboot());
				configuration.setCurProfil(oldConfig.getCurProfil());
				
				//ecrase les XML 1.1 du profil par la config 2.0+ 
				configuration.saveAsDefault();
				configuration.save();
			}
		}
		
		return configuration;
	}
	
	/**
	 * Renome un profil
	 * 
	 * @param currentName - le nom actuel du profil
	 * @param newName - le nouveau nom a attribuer au profil
	 * @return true si le renomage a reussi, false sinon
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	public static boolean renameConfiguration(String currentName, String newName) 
			throws ConfigurationException, IOException {
		
		boolean success = false;
		
		if(currentName.equals("defaut")) //$NON-NLS-1$
			return false;
		
		ConcoursJeunes concoursJeunes = ConcoursJeunes.getInstance();
		ArrayList<MetaDataFicheConcours> openedFichesConcours = new ArrayList<MetaDataFicheConcours>();
		
		if(ConcoursJeunes.getConfiguration().getCurProfil().equals(currentName) && concoursJeunes.getFichesConcours().size() > 0) {
			for(FicheConcours ficheConcours : concoursJeunes.getFichesConcours()) {
				openedFichesConcours.add(ficheConcours.getMetaDataFicheConcours());
			}
			concoursJeunes.closeAllFichesConcours();
			ConcoursJeunes.getConfiguration().save();
		}
		
		//renome le fichier de configuration
		File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator 
				+ Configuration.CONFIG_PROFILE + currentName + Configuration.EXT_XML);
		File fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator 
				+ Configuration.CONFIG_PROFILE + newName + Configuration.EXT_XML);
		if(fNew.exists())
			return false;
		success = f.renameTo(fNew);
		
		//renome le dossier du profil
		f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + "Profile" + File.separator + currentName); //$NON-NLS-1$
		fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + "Profile" + File.separator + newName); //$NON-NLS-1$
		
		if(success && f.exists() && !f.renameTo(fNew)) {
			try {
				FileUtil.deleteFilesPath(fNew);
				fNew.delete();
				success = f.renameTo(fNew);
			} catch (IOException e1) {
				success = false;
				e1.printStackTrace();
			}
			
			if(!success) {
				//si le renomage du dossier echoue (pouvant avoir pour seul cause
				//une erreur système) revenir en arrière
				f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator 
						+ Configuration.CONFIG_PROFILE + currentName + Configuration.EXT_XML);
				fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator 
						+ Configuration.CONFIG_PROFILE + newName + Configuration.EXT_XML);
				fNew.renameTo(f);
			}
		}
		
		Configuration configuration = null;
		if(success) {
			configuration = loadConfiguration(newName);
			configuration.setCurProfil(newName);
			configuration.save();
		}
		
		if(ConcoursJeunes.getConfiguration().getCurProfil().equals(currentName) && openedFichesConcours.size() > 0) {
			if(success && configuration != null) {
				ConcoursJeunes.setConfiguration(configuration);
				configuration.saveAsDefault();
			}
			
			for(MetaDataFicheConcours metaDataFicheConcours : openedFichesConcours) {
				concoursJeunes.restoreFicheConcours(metaDataFicheConcours);
			}
		}
		
		return success;
	}
}
