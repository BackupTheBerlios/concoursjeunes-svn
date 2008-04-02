/*
 * Créé le 21/02/2006 à 14:01 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import ajinteractive.standard.utilities.app.AppRessources;
import ajinteractive.standard.utilities.io.FileUtils;

/**
 * Crée et donne le chemin des ressources utilisateur pour le programme
 * 
 * @author Aurélien Jeoffray
 */
public class CJAppRessources extends AppRessources {
	private static String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static String EXT_XML = ".xml"; //$NON-NLS-1$

	/**
	 * Construit le répertoire utilisateur selon le systeme
	 * 
	 * @param progname
	 *            nom du programme
	 */
	public CJAppRessources(String progname) {
		super(progname);
	}

	/**
	 * Retourne le chemin absolu du profile
	 * 
	 * @param profile le nom du profil pour lequelle récuperer le chemin
	 * @return le chemin absolu du profile
	 */
	private File getProfilePath(String profile) {
		File profilePath = new File(getUserPath(), "Profile" + //$NON-NLS-1$
				File.separator + profile);

		return profilePath;
	}

	/**
	 * Copie les fichiers de configuration du repertoire de base vers le
	 * repertoire utilisateur
	 * 
	 */
	private void copyDefaultConfigForUser() {
		File[] fileForCopy = new File("config").listFiles(new java.io.FileFilter() { //$NON-NLS-1$
					public boolean accept(File pathname) {
						return pathname.isFile() && pathname.getName().endsWith(".xml"); //$NON-NLS-1$
					}
				});
		if(fileForCopy != null) {
			for (File file : fileForCopy) {
				File configPath = new File(getUserPath());
				if (!new File(configPath.getPath(), file.getName()).exists()) {
					try {
						FileUtils.copyFile(file, configPath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Copie les fichiers de mise à jour fournit à l'installation dans
	 * le répertoire de mise à jour de l'appli 
	 */
	public void copyDefaultUpdateFile() {
		File[] fileForCopy = new File(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") , "update").listFiles(new java.io.FileFilter() { //$NON-NLS-1$ //$NON-NLS-2$
					public boolean accept(File pathname) {
						return pathname.isFile();
					}
				});

		new File(getAllusersDataPath(), "update").mkdirs(); //$NON-NLS-1$

		for (File file : fileForCopy) {
			try {
				FileUtils.copyFile(file, new File(getAllusersDataPath(), "update")); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Donne le chemin des fichiers de configuration
	 * 
	 * @return le chemin des fichiers de configuration
	 */
	public File getConfigPathForUser() {
		copyDefaultConfigForUser();

		return new File(getUserPath());
	}

	/**
	 * Donne le chemin de la base de donnée
	 * 
	 * @return le chemin du répertoire contenant la base de donnée
	 */
	public File getBasePath() {
		File basePath = new File(getAllusersDataPath(), "base"); //$NON-NLS-1$

		basePath.mkdirs();

		return basePath;
	}

	/**
	 * Donne le répertoire ou sont stocké les concours pour le profil donné en
	 * parametre
	 * 
	 * @param profile
	 *            le nom du profile pour lequel récuperer le chemin des concours
	 * @return le chemin des concours
	 */
	public File getConcoursPathForProfile(String profile) {
		File concoursPath = new File(getProfilePath(profile), "concours"); //$NON-NLS-1$

		concoursPath.mkdirs();

		return concoursPath;
	}

	/**
	 * Retourne le chemin des logs en fonction du profil
	 * 
	 * @param profile -
	 *            le profil pour lequelle renvouyé le chemin des logs
	 * @return le chemin des logs du profil
	 */
	public File getLogPathForProfile(String profile) {
		File concoursPath = new File(getProfilePath(profile), "log"); //$NON-NLS-1$

		concoursPath.mkdirs();

		return concoursPath;
	}

	/**
	 * Retourne la liste des configuration disponaible
	 * 
	 * @return la liste des configurations disponibles
	 */
	public String[] listAvailableConfigurations() {
		String[] strConfig = getConfigPathForUser().list(new FilenameFilter() {
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
}
