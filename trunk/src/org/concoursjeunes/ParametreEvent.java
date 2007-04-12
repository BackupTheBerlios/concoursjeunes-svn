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
	private Parametre parametre;
	/**
	 * @param dateConcours
	 * @param intituleConcours
	 * @param filenameConcours
	 */
	public ParametreEvent(Parametre parametre) {
		this.parametre = parametre;
	}
	
	public Parametre getParametre() {
		return parametre;
	}
	
	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}
}
