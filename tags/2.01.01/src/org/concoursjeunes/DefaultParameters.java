/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.concoursjeunes;

/**
 * Paremtrage commun à Configuration et Parametre
 * 
 * @author Aurélien JEOFFRAY
 */
public class DefaultParameters {
	private Entite club = new Entite();
	private String intituleConcours = "Concours Jeunes"; //$NON-NLS-1$
	
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
