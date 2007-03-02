/**
 * Parametre d'un concours
 * 
 * @author Aurelien Jeoffray
 * @version 3.0
 */

package ajinteractive.concours;

import java.util.*;

public class Parametre extends DefaultParameters {
    private String sDateConcours       = "";
	private ArrayList<String> vArbitres = new ArrayList<String>();
	
	private String saveName            = "";
	
	/**
	 * Donne la date du concours
	 * 
	 * @return String - la date du concours;
	 */
	public String getDate() {
		return sDateConcours;
	}
	
	/**
	 * Donne la liste des arbitres
	 * 
	 * @return Vector<String> - la liste des arbitres
	 */
	public ArrayList<String> getArbitres() {
		return vArbitres;
	}
	
	/**
	 * Donne le chemin du fichier de sauvegarde du concours
	 * 
	 * @return String
	 */
	public String getSaveName() {
		return saveName;
	}
	
	/**
	 * specifie la date du concours
	 * 
	 * @param sDateConcours
	 */
	public void setDate(String sDateConcours) {
		this.sDateConcours = sDateConcours;
	}

	/**
	 * specifie la liste des arbitres
	 * 
	 * @param vArbitres
	 */
	public void setArbitres(ArrayList<String> vArbitres) {
		this.vArbitres = vArbitres;
	}
	
	/**
	 * specifie le nom de sauvegarde des infos du concours
	 * 
	 * @param saveName
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
}