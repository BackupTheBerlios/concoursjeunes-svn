/**
 * 
 */
package org.concoursjeunes;

import java.util.Date;

/**
 * @author  aurelien
 */
public class MetaDataFicheConcours {
	private Date dateConcours;
	private String intituleConcours;
	private String filenameConcours;
	
	public MetaDataFicheConcours() { }
	
	/**
	 * @param dateConcours
	 * @param intituleConcours
	 */
	public MetaDataFicheConcours(Date dateConcours, String intituleConcours, String filenameConcours) {
		this.dateConcours = dateConcours;
		this.intituleConcours = intituleConcours;
		this.filenameConcours = filenameConcours;
	}

	/**
	 * @return  dateConcours
	 * @uml.property  name="dateConcours"
	 */
	public Date getDateConcours() {
		return dateConcours;
	}

	/**
	 * @param dateConcours  dateConcours à définir
	 * @uml.property  name="dateConcours"
	 */
	public void setDateConcours(Date dateConcours) {
		this.dateConcours = dateConcours;
	}

	/**
	 * @return  intituleConcours
	 * @uml.property  name="intituleConcours"
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * @param intituleConcours  intituleConcours à définir
	 * @uml.property  name="intituleConcours"
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}

	/**
	 * @return  filenameConcours
	 * @uml.property  name="filenameConcours"
	 */
	public String getFilenameConcours() {
		return filenameConcours;
	}

	/**
	 * @param filenameConcours  filenameConcours à définir
	 * @uml.property  name="filenameConcours"
	 */
	public void setFilenameConcours(String filenameConcours) {
		this.filenameConcours = filenameConcours;
	}
}