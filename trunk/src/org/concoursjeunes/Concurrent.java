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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objet de Base de stockage des Information sur un concurrent:
 *  Nom Categorie Licence Cible Nombre de volée points
 *  
 * @author  Aurelien Jeoffray
 * @version  3.0
 */
public class Concurrent extends Archer implements Cloneable {
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

	private CriteriaSet criteriaSet;
	
	private int depart                  = 0;
	private TargetPosition targetPosition = new TargetPosition();

	private List<Integer> points	= new ArrayList<Integer>();
	private int neuf                    = 0;
	private int dix                     = 0;
	private int manque                  = 0;

	private int inscription             = UNINIT;
	private boolean	presence			= false;
	private boolean surclassement		= false;

	/**
	 * Constructeur vide obligatoire pour java beans
	 * 
	 */
	public Concurrent() { }

	/**
	 * Retourne les critères distinguant l'archer
	 * 
	 * @return criteriaSet le jeux de critères distinguant l'archer
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * Définit le jeux de critère distinguant l'archer
	 * 
	 * @param criteriaSet le jeux de critères de distinction
	 */
	public void setCriteriaSet(CriteriaSet criteriaSet) {
		this.criteriaSet = criteriaSet;
	}
	
	/**
	 * Affectation des scores pour le concurrent
	 * 
	 * @param points - la grille des scores du concurrent
	 */
	public void setScore(List<Integer> points) {
		this.points = points;
	}

	/**
	 * Donne la grille des scores du concurrent
	 * 
	 * @return la grille des scores
	 */
	public List<Integer> getScore() {
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
	 */
	public void setNeuf(int dix) {
		this.neuf = dix;
	}

	/**
	 * Donne le nombre de dix du concurrent
	 * @return  int
	 */
	public int getNeuf() {
		return this.neuf;
	}

	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * @param  dixPlus
	 */
	public void setDix(int dixPlus) {
		this.dix = dixPlus;
	}

	/**
	 * Donne le nombre de 10+ du concurrent
	 * @return  int
	 */
	public int getDix() {
		return this.dix;
	}

	/**
	 * Affecte le nombre de fleche manquee total du concurrent
	 * @param  manque
	 */
	public void setManque(int manque) {
		this.manque = manque;
	}

	/**
	 * Donne le nombre de fleche manquee du concurrent
	 * @return  int
	 */
	public int getManque() {
		return this.manque;
	}

	/**
	 * Retourne le numero de départ de l'archer
	 * @return  Returns the depart.
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * Définit le n° de départ de l'archer
	 * @param depart  The depart to set.
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * Donne le numero de cible du concurrent
	 * @return  int
	 */
	public int getCible() {
		return this.targetPosition.getTarget();
	}

	/**
	 * Affecte le numero de cible du concurrent
	 * @param  cible
	 */
	public void setCible(int cible) {
		this.targetPosition.setTarget(cible);
	}

	/**
	 * Donne la position sur cible du concurrent
	 * @return  int
	 */
	public int getPosition() {
		return this.targetPosition.getPosition();
	}

	/**
	 * Affecte la position sur cible du concurrent
	 * @param  position
	 */
	public void setPosition(int position) {
		this.targetPosition.setPosition(position);
	}

	/**
	 * Donne l'etat d'inscription du concurennt, réservé/payée
	 * @return  int
	 */
	public int getInscription() {
		return this.inscription;
	}

	/**
	 * Affecte l'etat d'inscription du concurennt, réservé/payée
	 * @param  inscription
	 */
	public void setInscription(int inscription) {
		this.inscription = inscription;
	}

	/**
	 * @return presence
	 */
	public boolean isPresence() {
		return presence;
	}

	/**
	 * @param presence presence à définir
	 */
	public void setPresence(boolean presence) {
		this.presence = presence;
	}

	/**
	 * @return surclassement
	 */
	public boolean isSurclassement() {
		return surclassement;
	}

	/**
	 * @param surclassement surclassement à définir
	 */
	public void setSurclassement(boolean surclassement) {
		this.surclassement = surclassement;
	}

	/**
	 * Libelle court du concurrent
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		if(this.targetPosition.getTarget() == 0)
			return "<html><font color=red>" + //$NON-NLS-1$
			getNomArcher() + " " + //$NON-NLS-1$
			getPrenomArcher() + " (" + //$NON-NLS-1$
			getClub() +
			")</font></html>"; //$NON-NLS-1$
		return targetPosition.toString() + ": " + //$NON-NLS-1$
		getNomArcher() + " " + //$NON-NLS-1$
		getPrenomArcher() + " (" + //$NON-NLS-1$
		getClub() + ")"; //$NON-NLS-1$
	}
	
	/**
	 * Test si l'archer possede dans la base des homonymes (même nom et prenom)
	 * 
	 * @return true su l'archer possede des homonyme, false sinon
	 */
	public boolean haveHomonyme() {
		Archer aComparant = new Archer();
		aComparant.setNomArcher(getNomArcher());
		aComparant.setPrenomArcher(getPrenomArcher());

		List<Concurrent> homonyme = ConcurrentManager.getArchersInDatabase(aComparant, null, ""); //$NON-NLS-1$

		return (homonyme.size() > 1);
	}
	
	/**
	 * Sauvegarde le jeux de critère associé à l'archer
	 * 
	 * @param reglement le réglement pour lequel s'applique le jeux de critère
	 */
	public void saveCriteriaSet(Reglement reglement) {
		if(!getNumLicenceArcher().equals("")) { //$NON-NLS-1$
			criteriaSet.save(reglement.hashCode());
			try {
				String sql = "select * from ARCHERS where NUMLICENCEARCHER=?"; //$NON-NLS-1$
				PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
				pstmt.setString(1, getNumLicenceArcher());
				
				ResultSet rs = pstmt.executeQuery();
				if(rs.first()) {
					pstmt.close();
					
					sql = "merge into distinguer (NUMLICENCEARCHER, NUMREGLEMENT, " + //$NON-NLS-1$
							"NUMCRITERIASET) KEY (NUMLICENCEARCHER, NUMREGLEMENT)" + //$NON-NLS-1$
							"VALUES (?, ?, ?)"; //$NON-NLS-1$
					//NUMREGLEMENT
					pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
					
					pstmt.setString(1, getNumLicenceArcher());
					pstmt.setInt(2, reglement.hashCode());
					pstmt.setInt(3, criteriaSet.hashCode());
	
					pstmt.executeUpdate();
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Concurrent clone() {
		try {
			return (Concurrent)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
}