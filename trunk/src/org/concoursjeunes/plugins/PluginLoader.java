/**
 * 
 */
package org.concoursjeunes.plugins;

import java.io.File;
import java.util.ArrayList;

import ajinteractive.standard.java2.AjResourcesReader;

/**
 * Permet de lister et charger les plugins installer en mémoire
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class PluginLoader {
	
	private ArrayList<PluginMetadata> listPlugins = new ArrayList<PluginMetadata>();
	
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
				
				listPlugins.add(pluginMetadata);
			}
		}
	}
	
	public ArrayList<PluginMetadata> getPlugins(int type) {
		ArrayList<PluginMetadata> currentList = new ArrayList<PluginMetadata>();
		for(PluginMetadata pm : listPlugins) {
			if(pm.getPluginType() == type)
				currentList.add(pm);
		}
		
		return currentList;
	}
}
