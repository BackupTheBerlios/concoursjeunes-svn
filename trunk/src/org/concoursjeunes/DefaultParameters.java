/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
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
	 * Retourne l'intitulé du concours
	 * 
	 * @return Renvoie l'intitulé du concours
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * Définit l'intitulé du concours ou intitulé par défaut
	 * 
	 * @param intituleConcours l'intitulé du concours
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}
	
	/**
	 * Retourne le nombre de cible sur le concours
	 * 
	 * @return le nombre de cible
	 */
	public int getNbCible() {
		return nbCible;
	}

	/**
	 * Définit le nombre de cible sur le concours
	 * 
	 * @param nbCible le nombre de cible
	 */
	public void setNbCible(int nbCible) {
		this.nbCible = nbCible;
	}

	/**
	 * Retourne le nombre de départ sur le concours
	 * 
	 * @return le nombre de départ
	 */
	public int getNbDepart() {
		return nbDepart;
	}

	/**
	 * Définit le nombre de départ sur le concours. Ce nombre de ne peut excédé 9
	 * 
	 * @param nbDepart le nombre de départ du concours. Si le nombre fournit est supérieur à 9,
	 * 9 sera enregistré
	 */
	public void setNbDepart(int nbDepart) {
		if(nbDepart > 9)
			nbDepart = 9;
		this.nbDepart = nbDepart;
	}

	/**
	 * Retourne le nombre de tireur par cible accepté sur le concours<br>
	 * Ce nombre est de:
	 * <ul>
	 * <li>2 pour un rytme AB</li>
	 * <li>3 pour un rytme ABC (Rare)</li>
	 * <li>4 pour un rytme AB.CD</li>
	 * <li>6 pour un rytme ABC.DEF (Rare)</li>
	 * </ul>
	 * A l'heure actuel, l'interface graphique ne supporte que les modes 2 et 4,
	 * les modes 3 et 6 sont déconseillé car pouvant entrainer des cas non détérminé
	 * 
	 * @return renvoie le nombre de tireur par cible
	 */
	public int getNbTireur() {
		return nbTireur;
	}

	/**
	 * Définit le nombre de tireur par cible accepté sur le concours<br>
	 * Ce nombre est de:
	 * <ul>
	 * <li>2 pour un rytme AB</li>
	 * <li>3 pour un rytme ABC (Rare)</li>
	 * <li>4 pour un rytme AB.CD</li>
	 * <li>6 pour un rytme ABC.DEF (Rare)</li>
	 * </ul>
	 * A l'heure actuel, l'interface graphique ne supporte que les modes 2 et 4,
	 * les modes 3 et 6 sont déconseillé car pouvant entrainer des cas non détérminé
	 * 
	 * @param nbTireur le nombre de tireur par cible
	 */
	public void setNbTireur(int nbTireur) {
		this.nbTireur = nbTireur;
	}

	/**
	 * Retourne le club organisateur du concours
	 * 
	 * @return le club organisateur
	 */
	public Entite getClub() {
		return club;
	}

	/**
	 * Définit le club organisateur du concours
	 * 
	 * @param club le club organisateur
	 */
	public void setClub(Entite club) {
		this.club = club;
	}
}
