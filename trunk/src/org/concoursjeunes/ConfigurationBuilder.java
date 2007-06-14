/*
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

import ajinteractive.standard.java2.AJToolKit;
import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;

/**
 * Fabrique de configuration programme de l'API ConcoursJeunes
 * 
 * @author Aurélien JEOFFRAY
 */
public class ConfigurationBuilder {
	
	/**
	 * Retourne la configuration courrante
	 * 
	 * @return l'objet de configuration courant
	 */
	public static Configuration getCurrentConfiguration() {
		Configuration configuration = (Configuration)AJToolKit.loadMarshallStructure(new File(userRessources.getConfigPathForUser() + File.separator + 
				ajrParametreAppli.getResourceString("file.configuration")), Configuration.class, false);
		if(configuration == null) {
			configuration = new Configuration();
		}
		
		return configuration;
	}
	
	/**
	 * Retourne la configuration par défaut
	 * 
	 * @return l'objet de configuration par défaut
	 */
	public static Configuration getDefaultConfiguration() {
		return new Configuration();
	}
	
	/**
	 * Retourne la configuration nommé en parametre
	 * 
	 * @param profilename
	 * @return la configuration nommé
	 */
	public static Configuration getConfiguration(String profilename) {
		Configuration configuration = (Configuration)AJToolKit.loadXMLStructure(
				new File(ConcoursJeunes.userRessources.getConfigPathForUser() 
						+ File.separator + "configuration_" + profilename + ".xml"), false);
		
		if(configuration == null) {
			configuration = new Configuration();
		}
		
		return configuration;
	}
}
