/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
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
package org.concoursjeunes;

import static org.concoursjeunes.ApplicationCore.userRessources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.ConfigurationException;
import javax.swing.event.EventListenerList;
import javax.xml.bind.JAXBException;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.builders.FicheConcoursBuilder;
import org.concoursjeunes.event.ProfileEvent;
import org.concoursjeunes.event.ProfileListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.concoursjeunes.manager.ConfigurationManager;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class Profile {
	
	private String name = "defaut"; //$NON-NLS-1$
	
	private AjResourcesReader localisation = new AjResourcesReader("libelle"); //$NON-NLS-1$
	private Configuration configuration = new Configuration();
	private List<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();
	private EventListenerList listeners = new EventListenerList();
	
	public Profile() {
		this((String)null);
	}
	
	public Profile(String name){
		this.name = name;
		loadConfiguration();
	}
	
	public Profile(File profilepath) {
		loadConfiguration(profilepath);
	}
	
	/**
	 * tente de recuperer la configuration générale du programme
	 */
	private void loadConfiguration() {
		if(this.name == null) {
			configuration = ConfigurationManager.loadCurrentConfiguration();
			this.name = configuration.getCurProfil();
		} else {
			configuration = ConfigurationManager.loadConfiguration(name);
		}
		localisation.setLocale(new Locale(configuration.getLangue()));
	}
	
	private void loadConfiguration(File profilepath) {
		configuration = ConfigurationManager.loadConfiguration(profilepath);
		this.name = configuration.getCurProfil();
		localisation.setLocale(new Locale(configuration.getLangue()));
	}
	
	/**
	 * Ajoute un auditeur aux evenements du Singleton ConcoursJeunes
	 * 
	 * @param profileListener l'auditeur qui s'enregistre à la class
	 */
	public void addProfileListener(ProfileListener profileListener) {
		listeners.add(ProfileListener.class, profileListener);
	}

	/**
	 * Retire un auditeur aux evenements du Singleton ConcoursJeunes
	 * @param profileListener l'auditeur qui résilie à la class
	 */
	public void removeProfileListener(ProfileListener profileListener) {
		listeners.remove(ProfileListener.class, profileListener);
	}
	
	/**
	 * Retourne l'objet de localisation du profil. Permet de retourner l'
	 * ensemble des libellé localisé utile à l'application.
	 * 
	 * @return localisation
	 */
	public AjResourcesReader getLocalisation() {
		return localisation;
	}

	/**
	 * Retourne la configuration courante de l'application
	 * 
	 * @return la configuration de l'application
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * Définit la configuration de l'application
	 * 
	 * @param configuration la configuration de l'application
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Renomme un profil. Si l'opération réussi, retourne <code>true</code> si une erreur
	 * dans le renommage des fichiers survient, si c'est le profil par défaut ou si un profil
	 * porte déjà le nouveau nom, retourne <code>false</code>.
	 * 
	 * @param newName le nouveau nom du profil
	 * @return retourne <code>true</code> en cas de succès du renommage
	 */
	public boolean renameProfile(String newName) {
		
		boolean success = false;
		
		//on interdit de renommer le profil par défaut
		if(this.name.equals("defaut")) //$NON-NLS-1$
			return false;
		
		//renome le dossier du profil
		File f = ApplicationCore.userRessources.getProfilePath(this);
		File fNew = new File(ApplicationCore.userRessources.getProfilePath(this).getParentFile(), newName);
		
		if(fNew.exists())
			return false;
		
		
		if(f.exists() && f.renameTo(fNew)) {
			//si l'opération de renomage réussi, on supprime le fichier de config
			f = new File(ApplicationCore.userRessources.getConfigPathForUser(),
					Configuration.CONFIG_PROFILE + name + Configuration.EXT_XML);
			if(!f.delete()) {
				//si on arrive pas à supprimer le fichier on reviens en arrière
				fNew.renameTo(ApplicationCore.userRessources.getProfilePath(this));
				
				return false;
			}

			name = newName;
			configuration.setCurProfil(newName);
			
			try {
				configuration.save();
				
				success = true;
			} catch (JAXBException e) {
				success = false;
				e.printStackTrace();
			} catch (IOException e) {
				success = false;
				e.printStackTrace();
			}
			
			if(!success) {
				fNew.renameTo(ApplicationCore.userRessources.getProfilePath(this));
			}
		}
		
		return success;
	}
	
	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 * @throws ConfigurationException
	 */
	public void createFicheConcours()
			throws NullConfigurationException, IOException, JAXBException {
		createFicheConcours(null);
	}
	
	/**
	 * Création d'une nouvelle fiche concours ayant les parametres fournit
	 * 
	 * @param parametre les parametres du concours
	 * @throws ConfigurationException
	 */
	public void createFicheConcours(Parametre parametre)
			throws NullConfigurationException, IOException, JAXBException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = new FicheConcours(this, parametre);
		fichesConcours.add(ficheConcours);
		configuration.getMetaDataFichesConcours().add(ficheConcours.getMetaDataFicheConcours());

		configuration.save();
		ficheConcours.save();

		fireFicheConcoursCreated(ficheConcours);
	}

	/**
	 * Supprime une fiche concours du système
	 * 
	 * @param metaDataFicheConcours le fichier de metadonné contenant les
	 * informations sur le concours à supprimer
	 * 
	 * @throws ConfigurationException
	 */
	public void deleteFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws NullConfigurationException, IOException, JAXBException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		configuration.getMetaDataFichesConcours().remove(metaDataFicheConcours);

		if (new File(userRessources.getConcoursPathForProfile(this) + File.separator + metaDataFicheConcours.getFilenameConcours()).delete()) {
			configuration.save();

			fireFicheConcoursDeleted(null);
		}
	}

	/**
	 * Referme une fiche de concours
	 * 
	 * @param ficheConcours la fiche concours à décharger de la méméoire
	 * 
	 * @throws ConfigurationException
	 */
	public void closeFicheConcours(FicheConcours ficheConcours)
			throws NullConfigurationException, IOException, JAXBException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		ficheConcours.save();
		configuration.save();
		if (fichesConcours.remove(ficheConcours)) {
			fireFicheConcoursClosed(ficheConcours);
		}
	}

	/**
	 * Décharge de la mémoire l'ensemble des fiches ouvertes
	 * 
	 * @throws ConfigurationException
	 */
	public void closeAllFichesConcours()
			throws NullConfigurationException, IOException, JAXBException {
		saveAllFichesConcours();

		ArrayList<FicheConcours> tmpList = new ArrayList<FicheConcours>();
		tmpList.addAll(fichesConcours);
		fichesConcours.clear();
		for (FicheConcours fiche : tmpList) {
			fireFicheConcoursClosed(fiche);
		}
		tmpList.clear();
		
		configuration.save();
	}

	/**
	 * Restaure le coucours dont l'objet de metadonnée est fournit en parametre
	 * 
	 * @param metaDataFicheConcours -
	 *            l'objet metadonnée du concours à restaurer
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	public void restoreFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws NullConfigurationException, IOException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = FicheConcoursBuilder.getFicheConcours(metaDataFicheConcours, this);

		if (ficheConcours != null) {
			fichesConcours.add(ficheConcours);
			fireFicheConcoursRestored(ficheConcours);
		}
	}

	/**
	 * Sauvegarde l'ensemble des fiches de concours actuellement ouverte
	 * 
	 * @exception ConfigurationException
	 */
	public void saveAllFichesConcours()
			throws NullConfigurationException, IOException, JAXBException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		for (FicheConcours fiche : fichesConcours) {
			fiche.save();
		}
		configuration.save();
	}
	
	/**
	 * Retourne la liste des fiches concours actuellement ouvertent
	 * 
	 * @return la liste des fiches concours actuellement ouvertent
	 */
	public List<FicheConcours> getFichesConcours() {
		return fichesConcours;
	}
	
	/**
	 * Test si une fiche est déjà ouverte ou non
	 * 
	 * @param metaDataFicheConcours - le fichier de metadonnées du concours à tester
	 * @return true si ouvert, false sinon
	 */
	public boolean isOpenFicheConcours(MetaDataFicheConcours metaDataFicheConcours) {
		for(FicheConcours ficheConcours : fichesConcours) {
			if(ficheConcours.getMetaDataFicheConcours().equals(metaDataFicheConcours))
				return true;
		}
		return false;
	}
	
	private void fireFicheConcoursCreated(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursCreated(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.CREATE_CONCOURS));
		}
	}

	private void fireFicheConcoursDeleted(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursDeleted(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.DELETE_CONCOURS));
		}
	}

	private void fireFicheConcoursClosed(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursClosed(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.CLOSE_CONCOURS));
		}
	}

	private void fireFicheConcoursRestored(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursRestored(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.OPEN_CONCOURS));
		}
	}
}
