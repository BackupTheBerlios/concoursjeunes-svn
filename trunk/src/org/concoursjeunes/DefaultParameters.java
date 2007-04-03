/**
 * Paremtrage commun à Configuration et Parametre
 */

package org.concoursjeunes;

/**
 * @author  aurelien
 */
public class DefaultParameters {
	private Entite club = new Entite();
	private String intituleConcours = "Concours Jeunes"; //$NON-NLS-1$

	private Reglement reglement = new Reglement();
	
	private int nbCible             = 10;
	private int nbTireur            = 4;
	private int nbDepart            = 1;

	public DefaultParameters() {

	}

	/**
	 * @return  Renvoie intituleConcours.
	 * @uml.property  name="intituleConcours"
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * @param intituleConcours  intituleConcours à définir.
	 * @uml.property  name="intituleConcours"
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}

	/**
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * @param reglement reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}
	
	/**
	 * @return  Renvoie nbCible.
	 * @uml.property  name="nbCible"
	 */
	public int getNbCible() {
		return nbCible;
	}

	/**
	 * @param nbCible  nbCible à définir.
	 * @uml.property  name="nbCible"
	 */
	public void setNbCible(int nbCible) {
		this.nbCible = nbCible;
	}

	/**
	 * @return  Renvoie nbDepart.
	 * @uml.property  name="nbDepart"
	 */
	public int getNbDepart() {
		return nbDepart;
	}

	/**
	 * @param nbDepart  nbDepart à définir.
	 * @uml.property  name="nbDepart"
	 */
	public void setNbDepart(int nbDepart) {
		this.nbDepart = nbDepart;
	}

	/**
	 * @return  Renvoie nbTireur.
	 * @uml.property  name="nbTireur"
	 */
	public int getNbTireur() {
		return nbTireur;
	}

	/**
	 * @param nbTireur  nbTireur à définir.
	 * @uml.property  name="nbTireur"
	 */
	public void setNbTireur(int nbTireur) {
		this.nbTireur = nbTireur;
	}

	/**
	 * @return  club
	 * @uml.property  name="club"
	 */
	public Entite getClub() {
		return club;
	}

	/**
	 * @param club  club à définir
	 * @uml.property  name="club"
	 */
	public void setClub(Entite club) {
		this.club = club;
	}
}
