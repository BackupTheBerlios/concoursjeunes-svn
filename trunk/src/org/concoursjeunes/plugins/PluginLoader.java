/**
 * 
 */
package org.concoursjeunes.plugins;

import java.io.File;

import ajinteractive.standard.java2.AjResourcesReader;

/**
 * Permet de lister et charger les plugins installer en mémoire
 * 
 * @author aurelien
 *
 */
public class PluginLoader {
	
	public PluginLoader() {
		File pluginPath = new File("./plugins");
		File[] pluginsFiles = pluginPath.listFiles();
		
		for(File pluginFile : pluginsFiles) {
			if(pluginFile.getName().endsWith("_libelle.properties")) {
				AjResourcesReader pluginLocalInfo = new AjResourcesReader(
						pluginFile.getName().substring(0, pluginFile.getName().length() - ".properties".length()));
				AjResourcesReader pluginProperties = new AjResourcesReader(
						pluginFile.getName().substring(0, pluginFile.getName().length() - "_libelle.properties".length()));
				
				PluginMetadata pluginMetadata = new PluginMetadata();
				pluginMetadata.setInfo(pluginLocalInfo.getResourceString("plugin.libelle"));
				pluginMetadata.setOptionLabel(pluginLocalInfo.getResourceString("plugin.optionlabel"));
				pluginMetadata.setPluginType(pluginProperties.getResourceInteger("plugin.type"));
				pluginMetadata.setClassName(pluginProperties.getResourceString("plugin.class"));
			}
		}
	}
	
	
}
