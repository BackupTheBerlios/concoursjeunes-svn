/**
 * 
 */
package org.concoursjeunes;

/**
 * @author Aurélien JEOFFRAY - Fiducial Informatique
 *
 */
public class ConcoursJeunesEvent {
	public static final int CREATE_CONCOURS = 1;
	public static final int DELETE_CONCOURS = 2;
	public static final int CLOSE_CONCOURS = 3;
	public static final int OPEN_CONCOURS = 4;
	
	private FicheConcours ficheConcours;
	
	private int action = -1;

	public ConcoursJeunesEvent(FicheConcours ficheConcours, int action) {
		this.ficheConcours = ficheConcours;
		this.action = action;
	}

	/**
	 * @return action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @param action action à définir
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * @return ficheConcours
	 */
	public FicheConcours getFicheConcours() {
		return ficheConcours;
	}

	/**
	 * @param ficheConcours ficheConcours à définir
	 */
	public void setFicheConcours(FicheConcours ficheConcours) {
		this.ficheConcours = ficheConcours;
	}
	
	
}
