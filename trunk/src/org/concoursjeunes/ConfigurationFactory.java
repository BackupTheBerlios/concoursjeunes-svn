/**
 * 
 */
package org.concoursjeunes;

import java.io.File;

import ajinteractive.standard.java2.AJToolKit;
import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;

/**
 * @author Aurélien JEOFFRAY - Fiducial Informatique
 *
 */
public class ConfigurationFactory {
	
	/**
	 * Retourne la configuration courrante
	 * 
	 * @return
	 */
	public static Configuration getCurrentConfiguration() {
		Configuration configuration = (Configuration)AJToolKit.loadXMLStructure(new File(userRessources.getConfigPathForUser() + File.separator + 
				ajrParametreAppli.getResourceString("file.configuration")), false);
		if(configuration == null) {
			configuration = new Configuration();
		}
		
		return configuration;
	}
	
	public static Configuration getDefaultConfiguration() {
		return null;
	}
	public static Configuration getConfiguration(String profilename) {
		return null;
	}
}
