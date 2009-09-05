/*
 * Créé le 17 août 07 à 10:20:23 pour ConcoursJeunes
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
 *  any later version.
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
package org.concoursjeunes.manager;

import static org.concoursjeunes.ApplicationCore.staticParameters;
import static org.concoursjeunes.ApplicationCore.userRessources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.ajdeveloppement.commons.io.XMLSerializer;
import org.concoursjeunes.AppConfiguration;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.Entite;
import org.concoursjeunes.builders.ConfigurationBuilder;

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
	public static Configuration loadCurrentConfiguration() {
		File profileChoice = new File(userRessources.getConfigPathForUser(),
				"currentprofile"); //$NON-NLS-1$
		String curentProfile = ApplicationCore.getAppConfiguration().getLastProfile();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(profileChoice));
			curentProfile = reader.readLine();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if(reader != null)
				try { reader.close(); } catch(IOException e) { }
		}
		
		return loadConfiguration(curentProfile);
	}
	
	/**
	 * Charge la configuration nommé en parametre
	 * 
	 * @param profilename
	 * @return la configuration nommé
	 */
	public static Configuration loadConfiguration(String profilename) {
		return loadConfiguration(new File(ApplicationCore.userRessources.getConfigPathForUser(), "configuration_" + profilename + ".xml")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Charge le fichier de configuration transmit en parametre
	 * 
	 * @param confFile le fichier de configuration à charger
	 * @return l'objet configuration chargé
	 */
	@SuppressWarnings("deprecation")
	public static Configuration loadConfiguration(File confFile) {
		Configuration configuration = null;
		boolean oldConfigFormat = false;
		//tente de charger la configuration
		try {
			configuration = XMLSerializer.loadMarshallStructure(confFile, Configuration.class);
		//si il n'y arrive pas vérifie que ce n'est pas une config 1.1
		} catch (JAXBException e) {
			oldConfigFormat = true;
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			//ne rien faire, c'est que la configuration n'a pas été créé
		} catch (IOException e) {
			oldConfigFormat = true;
			e.printStackTrace();
		}
		if(oldConfigFormat) {
			try {
				//couche de compatibilite avec le XML de la 1.1
				ajinteractive.concours.Configuration oldConfig = XMLSerializer.loadXMLStructure(confFile, false);
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
					configuration.setInterfaceAffResultatExEquo(oldConfig.isInterfaceAffResultatExEquo());
					configuration.setCurProfil(oldConfig.getCurProfil());
					
					//ecrase les XML 1.1 du profil par la config 2.0+ 
					configuration.save();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		if(configuration == null) {
			configuration = ConfigurationBuilder.getDefaultConfiguration();
		}
		
		return configuration;
	}
	
	@SuppressWarnings("deprecation")
	public static AppConfiguration loadAppConfiguration() {
		File confFile = new File(
				userRessources.getConfigPathForUser(),
				staticParameters.getResourceString("file.configuration")); //$NON-NLS-1$
		
		AppConfiguration appConfiguration = null;
		
		try {
			appConfiguration = XMLSerializer.loadMarshallStructure(confFile, AppConfiguration.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			//ne rien faire, c'est que la configuration n'a pas été créé
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(appConfiguration == null) {
			try {
				Configuration oldConf = loadConfiguration(confFile);
				
				//on crée un fichier de configuration avec les bons paramètres 
				appConfiguration = new AppConfiguration();
				appConfiguration.setFirstboot(true);
				appConfiguration.setPdfReaderPath(oldConf.getPdfReaderPath());
				appConfiguration.setProxy(oldConf.getProxy());
				appConfiguration.setUseProxy(oldConf.isUseProxy());
				appConfiguration.save();
				
				//on reset les paramètres obsolète
				//oldConf.setFirstboot(true);
				oldConf.setPdfReaderPath(null); 
				oldConf.setProxy(null);
				oldConf.setUseProxy(false);
				oldConf.save();
			} catch (JAXBException e) {
				e.printStackTrace();
				appConfiguration = null;
			} catch(FileNotFoundException e) {
				//ne rien faire, c'est que la configuration n'a pas été créé
				appConfiguration = null;
			} catch (IOException e) {
				e.printStackTrace();
				appConfiguration = null;
			}
		}
		
		if(appConfiguration == null) {
			appConfiguration = new AppConfiguration();
		}
		
		return appConfiguration;
	}
}
