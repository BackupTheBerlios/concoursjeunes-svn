package org.concoursjeunes;

import java.util.ArrayList;

/**
 * Objet de Base de stockage des Information sur un concurrent:
 *  Nom Categorie Licence Cible Nombre de volée points
 * @author  Aurelien Jeoffray
 * @version  3.0
 */
public class Concurrent extends Archer {
//	public:
	/**
	 * Statut de l'archer: réservé
	 */
	public static final int RESERVEE    = 0;
	/**
	 * Statut de l'archer: Payée
	 */
	public static final int PAYEE       = 1;
	/**
	 * Statut de l'archer: non initialisé
	 */
	public static final int UNINIT      = 2;

//	private:
	private int depart                  = 0;
	private int cible                   = 0;	//position sur le concours
	private int position                = 0;

	public ArrayList<Integer> points	= new ArrayList<Integer>();
	private int neuf                    = 0;
	private int dix                     = 0;
	private int manque                  = 0;

	private int inscription             = UNINIT;

	/**
	 * Constructeur vide obligatoire pour java beans
	 * 
	 */
	public Concurrent() { }

	/**
	 * Affectation des scores pour le concurrent
	 * 
	 * @param points - la grille des scores du concurrent
	 */
	public void setScore(ArrayList<Integer> points) {
		this.points = points;
	}

	/**
	 * Donne la grille des scores du concurrent
	 * 
	 * @return la grille des scores
	 */
	public ArrayList<Integer> getScore() {
		return points;
	}

	/**
	 * Donne le total des points du concurrent
	 * pour classement
	 * 
	 * @return int - le total des points
	 */
	public int getTotalScore() {
		int total = 0;
		if(points != null) {
			for(int point : points) {
				total += point;
			}
		}
		return total;
	}
	/**
	 * Affecte le nombre de dix total du concurrent
	 * @param  dix
	 * @uml.property  name="neuf"
	 */
	public void setNeuf(int dix) {
		this.neuf = dix;
	}

	/**
	 * Donne le nombre de dix du concurrent
	 * @return  int
	 * @uml.property  name="neuf"
	 */
	public int getNeuf() {
		return this.neuf;
	}

	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * @param  dixPlus
	 * @uml.property  name="dix"
	 */
	public void setDix(int dixPlus) {
		this.dix = dixPlus;
	}

	/**
	 * Donne le nombre de 10+ du concurrent
	 * @return  int
	 * @uml.property  name="dix"
	 */
	public int getDix() {
		return this.dix;
	}

	/**
	 * Affecte le nombre de fleche manquee total du concurrent
	 * @param  manque
	 * @uml.property  name="manque"
	 */
	public void setManque(int manque) {
		this.manque = manque;
	}

	/**
	 * Donne le nombre de fleche manquee du concurrent
	 * @return  int
	 * @uml.property  name="manque"
	 */
	public int getManque() {
		return this.manque;
	}

	/**
	 * Retourne le numero de départ de l'archer
	 * @return  Returns the depart.
	 * @uml.property  name="depart"
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * Définit le n° de départ de l'archer
	 * @param depart  The depart to set.
	 * @uml.property  name="depart"
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * Donne le numero de cible du concurrent
	 * @return  int
	 * @uml.property  name="cible"
	 */
	public int getCible() {
		return this.cible;
	}

	/**
	 * Affecte le numero de cible du concurrent
	 * @param  cible
	 * @uml.property  name="cible"
	 */
	public void setCible(int cible) {
		this.cible = cible;
	}

	/**
	 * Donne la position sur cible du concurrent
	 * @return  int
	 * @uml.property  name="position"
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Affecte la position sur cible du concurrent
	 * @param  position
	 * @uml.property  name="position"
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Donne l'etat d'inscription du concurennt, réservé/payée
	 * @return  int
	 * @uml.property  name="inscription"
	 */
	public int getInscription() {
		return this.inscription;
	}

	/**
	 * Affecte l'etat d'inscription du concurennt, réservé/payée
	 * @param  inscription
	 * @uml.property  name="inscription"
	 */
	public void setInscription(int inscription) {
		this.inscription = inscription;
	}

	/**
	 * Libelle court du concurrent
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		if(this.cible == 0)
			return "<html><font color=red>" + //$NON-NLS-1$
			getNomArcher() + " " + //$NON-NLS-1$
			getPrenomArcher() + " (" + //$NON-NLS-1$
			getClub() +
			")</font></html>"; //$NON-NLS-1$
		return ((this.cible < 10) ? "0" : "") + this.cible + "" + (char)('A' + this.position) + ": " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		getNomArcher() + " " + //$NON-NLS-1$
		getPrenomArcher() + " (" + //$NON-NLS-1$
		getClub() + ")"; //$NON-NLS-1$
	}
}