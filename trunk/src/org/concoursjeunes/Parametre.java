/**
 * Parametre d'un concours
 * 
 * @author Aurelien Jeoffray
 * @version 3.0
 */

package org.concoursjeunes;

import java.util.*;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.configuration;

/**
 * @author  aurelien
 */
public class Parametre extends DefaultParameters {
	private Date dDateConcours		= new Date(); //$NON-NLS-1$
	private ArrayList<String> vArbitres = new ArrayList<String>();

	private String saveName         = System.currentTimeMillis()
			+ ajrParametreAppli.getResourceString("extention.concours"); //$NON-NLS-1$

	public Parametre() {
		setClub(configuration.getClub());
		setIntituleConcours(configuration.getIntituleConcours());
		setNbCible(configuration.getNbCible());
		setNbTireur(configuration.getNbTireur());
		setNbDepart(configuration.getNbDepart());
		setReglement(configuration.getReglement());
	}
	/**
	 * Donne la date du concours
	 * 
	 * @return Date - la date du concours;
	 */
	public Date getDate() {
		return dDateConcours;
	}

	/**
	 * Donne la liste des arbitres
	 * 
	 * @return la liste des arbitres
	 */
	public ArrayList<String> getArbitres() {
		return vArbitres;
	}

	/**
	 * Donne le chemin du fichier de sauvegarde du concours
	 * @return  String
	 * @uml.property  name="saveName"
	 */
	public String getSaveName() {
		return saveName;
	}

	/**
	 * specifie la date du concours
	 * 
	 * @param sDateConcours
	 */
	public void setDate(Date dDateConcours) {
		this.dDateConcours = dDateConcours;
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
	 * @param  saveName
	 * @uml.property  name="saveName"
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
}