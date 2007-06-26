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
		File pluginPath = new File("./plugins/properties"); //$NON-NLS-1$
		File[] pluginsFiles = pluginPath.listFiles();
		
		for(File pluginFile : pluginsFiles) {
			if(pluginFile.getName().endsWith(".properties")) { //$NON-NLS-1$
				AjResourcesReader pluginProperties = new AjResourcesReader("properties." +  //$NON-NLS-1$
						pluginFile.getName().substring(0, pluginFile.getName().length() - ".properties".length())); //$NON-NLS-1$
				AjResourcesReader pluginLocalInfo = new AjResourcesReader(pluginProperties.getResourceString("plugin.libelle.file")); //$NON-NLS-1$
				
				PluginMetadata pluginMetadata = new PluginMetadata();
				pluginMetadata.setInfo(pluginLocalInfo.getResourceString("plugin.libelle")); //$NON-NLS-1$
				pluginMetadata.setOptionLabel(pluginLocalInfo.getResourceString("plugin.optionlabel")); //$NON-NLS-1$
				pluginMetadata.setPluginType(pluginProperties.getResourceInteger("plugin.type")); //$NON-NLS-1$
				pluginMetadata.setClassName(pluginProperties.getResourceString("plugin.class")); //$NON-NLS-1$
				
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
