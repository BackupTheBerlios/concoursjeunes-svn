/*
 * Créé le 21/02/2006 à 14:01 pour ArcCompetition
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.ajdeveloppement.concours;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * <p>Gère et retourne le chemin des différentes ressources utilisateur pour le programme.</p>
 * <p>Les chemins retournés sont fonction du système d'exploitation de l'utilisateur, ainsi que
 * de sa session</p>
 * 
 * @author Aurélien Jeoffray
 */
public class AppRessources extends org.ajdeveloppement.apps.AppRessources {
	private static final String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static final String EXT_XML = ".xml"; //$NON-NLS-1$

	private Map<String, SoftReference<ImageIcon>> imageIconCache = new HashMap<String, SoftReference<ImageIcon>>();
	
	/**
	 * Construit le répertoire utilisateur selon le système
	 * 
	 * @param progname
	 *            nom du programme
	 */
	public AppRessources(String progname) {
		super(progname);
	}

	/**
	 * Retourne le chemin du répertoire utilisateur de l'application
	 */
	@Override
	public String getUserPath() {
		return ApplicationCore.staticParameters.getResourceString("path.user", super.getUserPath()); //$NON-NLS-1$
	}
	
	/**
	 * Retourne le chemin absolu du profile
	 * 
	 * @param profile le nom du profil pour lequel récupérer le chemin
	 * @return le chemin absolu du profile
	 */
	public File getProfilePath(Profile profile) {
		File profilePath = new File(
				ApplicationCore.staticParameters.getResourceString("path.profile", getUserPath()), //$NON-NLS-1$
				profile.getConfiguration().getCurProfil());

		return profilePath;
	}

	/**
	 * Donne le chemin des fichiers de configuration
	 * 
	 * @return le chemin des fichiers de configuration
	 */
	public File getConfigPathForUser() {
		return new File(getUserPath());
	}

	/**
	 * Donne le chemin de la base de donnée
	 * 
	 * @return le chemin du répertoire contenant la base de donnée
	 */
	public File getBasePath() {
		File basePath = new File(
				ApplicationCore.staticParameters.getResourceString("path.base", getAllusersDataPath())); //$NON-NLS-1$

		basePath.mkdirs();

		return basePath;
	}
	
	/**
	 * Donne le chemin du repertoire de stockage temporaire des mises à jours
	 * 
	 * @return le chemin du répertoire contenant la base de donnée
	 */
	public File getUpdatePath() {
		File basePath = new File(
				ApplicationCore.staticParameters.getResourceString("path.update", getAllusersDataPath())); //$NON-NLS-1$

		basePath.mkdirs();

		return basePath;
	}

	/**
	 * Donne le répertoire ou sont stocké les concours pour le profil donné en
	 * paramètre
	 * 
	 * @param profile
	 *            le nom du profile pour lequel récupérer le chemin des concours
	 * @return le chemin des concours
	 */
	public File getConcoursPathForProfile(Profile profile) {
		File concoursPath = new File(
				ApplicationCore.staticParameters.getResourceString("path.concours", getProfilePath(profile))); //$NON-NLS-1$

		concoursPath.mkdirs();

		return concoursPath;
	}

	/**
	 * Retourne le chemin des logs en fonction du profil
	 * 
	 * @param profile le profil pour lequel renvoyer le chemin des logs
	 * @return le chemin des logs du profil
	 */
	public File getLogPathForProfile(Profile profile) {
		File concoursPath = new File(
				ApplicationCore.staticParameters.getResourceString("path.log", getProfilePath(profile))); //$NON-NLS-1$

		concoursPath.mkdirs();

		return concoursPath;
	}

	/**
	 * Retourne la liste des configurations disponible
	 * 
	 * @return la liste des configurations disponibles
	 */
	public String[] listAvailableConfigurations() {
		String[] strConfig = getConfigPathForUser().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith(CONFIG_PROFILE) && name.endsWith(EXT_XML))
					return true;
				return false;
			}
		});

		for (int i = 0; i < strConfig.length; i++)
			strConfig[i] = strConfig[i].substring(CONFIG_PROFILE.length(), strConfig[i].length() - EXT_XML.length());
		return strConfig;
	}
	
	/**
	 * Retourne une ressource graphique de l'application
	 * 
	 * @param resourceKey la resource graphique à charger
	 * @return l'image chargé
	 */
	public ImageIcon getImageIcon(String resourceKey) {
		return getImageIcon(resourceKey, -1, -1);
	}
	/**
	 * Retourne une ressource graphique de l'application
	 * 
	 * @param resourceKey la resource graphique à charger
	 * @param width la longueur de l'image ou -1 si l'on souhaite conserver la dimension par défaut
	 * @param height la hauteur de l'image ou -1 si l'on souhaite conserver la dimension par défaut
	 * @return l'image chargé
	 */
	public ImageIcon getImageIcon(String resourceKey, int width, int height) {
		ImageIcon imageIcon = null;
		if(imageIconCache.containsKey(resourceKey))
			imageIcon = imageIconCache.get(resourceKey).get();
		if(imageIcon == null) {
			imageIcon = new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.staticParameters.getResourceString(resourceKey));
			imageIconCache.put(resourceKey, new SoftReference<ImageIcon>(imageIcon));
		}
		
		if(width > -1 && height > -1)
			imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
		
		return imageIcon;
	}
}
