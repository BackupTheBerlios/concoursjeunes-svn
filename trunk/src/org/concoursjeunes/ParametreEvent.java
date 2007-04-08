/**
 * 
 */
package org.concoursjeunes;

import java.util.Date;

/**
 * @author aurelien
 *
 */
public class ParametreEvent {
	private Date dateConcours;
	private String intituleConcours;
	private String filenameConcours;
	/**
	 * @param dateConcours
	 * @param intituleConcours
	 * @param filenameConcours
	 */
	public ParametreEvent(Date dateConcours, String intituleConcours, String filenameConcours) {
		this.dateConcours = dateConcours;
		this.intituleConcours = intituleConcours;
		this.filenameConcours = filenameConcours;
	}
	/**
	 * @return the dateConcours
	 */
	public Date getDateConcours() {
		return dateConcours;
	}
	/**
	 * @param dateConcours the dateConcours to set
	 */
	public void setDateConcours(Date dateConcours) {
		this.dateConcours = dateConcours;
	}
	/**
	 * @return the filenameConcours
	 */
	public String getFilenameConcours() {
		return filenameConcours;
	}
	/**
	 * @param filenameConcours the filenameConcours to set
	 */
	public void setFilenameConcours(String filenameConcours) {
		this.filenameConcours = filenameConcours;
	}
	/**
	 * @return the intituleConcours
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}
	/**
	 * @param intituleConcours the intituleConcours to set
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}
	
	
}
