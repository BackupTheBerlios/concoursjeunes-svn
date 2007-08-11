/**
 * 
 */
package org.concoursjeunes.plugins;

/**
 * @author Aurélien JEOFFRAY
 */
public class PluginMetadata {
	public static final int ALL = 0;
	public static final int ONDEMAND_PLUGIN = 1;
	public static final int STARTUP_PLUGIN = 3;

	private String info = ""; //$NON-NLS-1$
	private String optionLabel = ""; //$NON-NLS-1$
	private int pluginType = 0;
	private String className = ""; //$NON-NLS-1$
	private String reposURL = ""; //$NON-NLS-1$
	private String[] menuPath;

	public PluginMetadata() {

	}

	/**
	 * @return String
	 * @uml.property name="info"
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 * @uml.property name="info"
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return le libelle de l'option
	 * @uml.property name="optionLabel"
	 */
	public String getOptionLabel() {
		return optionLabel;
	}

	/**
	 * @param optionLabel
	 * @uml.property name="optionLabel"
	 */
	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	/**
	 * @return le type de plugin
	 * 
	 * @uml.property name="pluginType"
	 */
	public int getPluginType() {
		return pluginType;
	}

	/**
	 * @param pluginType
	 * @uml.property name="pluginType"
	 */
	public void setPluginType(int pluginType) {
		this.pluginType = pluginType;
	}

	/**
	 * @return le nom de la class principal constituant le plug-in
	 * @uml.property name="className"
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 * @uml.property name="className"
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public String getReposURL() {
		return reposURL;
	}

	public void setReposURL(String reposURL) {
		this.reposURL = reposURL;
	}

	public String[] getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String[] menuPath) {
		this.menuPath = menuPath;
	}

}
