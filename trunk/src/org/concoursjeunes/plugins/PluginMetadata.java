/**
 * 
 */
package org.concoursjeunes.plugins;

/**
 * @author  Aurélien JEOFFRAY
 */
public class PluginMetadata {
	public static final int IMPORT_PLUGIN = 1;
	public static final int EXPORT_PLUGIN = 2;
	
	private String info = ""; //$NON-NLS-1$
	private String optionLabel = ""; //$NON-NLS-1$
	private int pluginType = 0; //$NON-NLS-1$
	private String className = ""; //$NON-NLS-1$
	
	public PluginMetadata() {
		
	}

	/**
	 * @return  String
	 * @uml.property  name="info"
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 * @uml.property  name="info"
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return
	 * @uml.property  name="optionLabel"
	 */
	public String getOptionLabel() {
		return optionLabel;
	}

	/**
	 * @param optionLabel
	 * @uml.property  name="optionLabel"
	 */
	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	/**
	 * @return le type de plugin
	 * 
	 * @uml.property  name="pluginType"
	 */
	public int getPluginType() {
		return pluginType;
	}

	/**
	 * @param pluginType
	 * @uml.property  name="pluginType"
	 */
	public void setPluginType(int pluginType) {
		this.pluginType = pluginType;
	}

	/**
	 * @return
	 * @uml.property  name="className"
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 * @uml.property  name="className"
	 */
	public void setClassName(String className) {
		this.className = className;
	}

}
