/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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
package org.ajdeveloppement.concours.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.PluginClassLoader;
import org.ajdeveloppement.concours.plugins.Plugin.Type;

/**
 * Permet de lister et charger les plugins installer en mémoire
 * 
 * @author Aurélien JEOFFRAY
 * 
 */
public class PluginLoader {

	private static final List<PluginMetadata> listPlugins = new ArrayList<PluginMetadata>();
	
	static {
		File pluginPath = new File("./plugins/properties"); //$NON-NLS-1$
		File[] pluginsFiles = pluginPath.listFiles();

		if (pluginsFiles != null) {
			for (File pluginFile : pluginsFiles) {
				if (pluginFile.getName().endsWith(".properties") && !pluginFile.getName().endsWith("_custom.properties")) { //$NON-NLS-1$ //$NON-NLS-2$
					String pluginName = pluginFile.getName().substring(0, pluginFile.getName().length() - ".properties".length()); //$NON-NLS-1$
					
					AjResourcesReader pluginProperties = new AjResourcesReader("properties." + //$NON-NLS-1$
							pluginName);
					
					Class<?> cla = loadPluginClass(pluginProperties.getResourceString("plugin.class")); //$NON-NLS-1$
					if(cla != null) {
						PluginMetadata pluginMetadata = new PluginMetadata();
						pluginMetadata.setName(pluginName);
						pluginMetadata.setPluginLocalisationPropertiesPath(pluginProperties.getResourceString("plugin.libelle.file")); //$NON-NLS-1$
						pluginMetadata.setInfo("plugin.libelle"); //$NON-NLS-1$
						pluginMetadata.setOptionLabel("plugin.optionlabel"); //$NON-NLS-1$
						pluginMetadata.setPluginType(cla.getAnnotation(Plugin.class).type());
						pluginMetadata.setClassName(pluginProperties.getResourceString("plugin.class")); //$NON-NLS-1$
						pluginMetadata.setVersion(pluginProperties.getResourceString("plugin.version")); //$NON-NLS-1$
						pluginMetadata.setMenuPath(pluginProperties.getResourceString("plugin.menu").split("/")); //$NON-NLS-1$ //$NON-NLS-2$
						pluginMetadata.setPluginClass(cla);
	
						listPlugins.add(pluginMetadata);
					}
				}
			}
		}
	}
	

	public PluginLoader() {
		
	}
	
	private static Class<?> loadPluginClass(String className) {
		Class<?> cla = null;
		try {
			cla =  Class.forName(className, false, new PluginClassLoader(findParentClassLoader(), new File("plugins"))); //$NON-NLS-1$
			
			if (cla != null) {
				//annule le chargement de la class si ce n'est pas une class de plugin
				if(!cla.isAnnotationPresent(Plugin.class)) {
					cla = null;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return cla;
	}
	
	/**
	 * Locates the best class loader based on context (see class description).
	 * 
	 * @return The best parent classloader to use
	 */
	private static ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = PluginLoader.class.getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return parent;
	}

	/**
	 * Retourne les metadonnées des plugins installé et correspondant
	 * au type fournit en parametre.<br>
	 * le type peut être!
	 * <ul>
	 * 	<li>PluginMetadata.ALL</li>
	 *  <li>PluginMetadata.ONDEMAND_PLUGIN</li>
	 *  <li>PluginMetadata.STARTUP_PLUGIN</li>
	 * </ul>
	 * @param type le type des plugins à retourner
	 * @return les metafonnées des plugins retourné
	 */
	public List<PluginMetadata> getPlugins(Type type) {
		List<PluginMetadata> currentList = new ArrayList<PluginMetadata>();
		for (PluginMetadata pm : listPlugins) {
			if (type == Type.ALL || pm.getPluginType() == type)
				currentList.add(pm);
		}

		return currentList;
	}
	
	/**
	 * Determine si un plugin représenté par son nom logique est installé ou non
	 * sur le système
	 * 
	 * @param pluginName le nom du plugin à tester
	 * @return true si le plugin est installé, false sinon
	 */
	public boolean isInstalled(String pluginName) {
		return new File("./plugins/properties/" + pluginName + ".properties").exists(); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
