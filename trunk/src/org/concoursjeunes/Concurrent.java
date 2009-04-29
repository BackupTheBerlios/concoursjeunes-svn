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

	private ArrayList<Integer> points	= new ArrayList<Integer>();
	private int[] departages			= new int[2];

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
		this.points = (ArrayList<Integer>)points;
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
	 * Retourne le tableau des départage des scores.
	 * 
	 * @return le tableau des départage des scores.
	 */
	public int[] getDepartages() {
		return departages;
	}

	/**
	 * Définit le tableau de départage des scores
	 * 
	 * @param departages le tableau de départage des scores
	 */
	public void setDepartages(int[] departages) {
		this.departages = departages;
	}

	/**
	 * Affecte le nombre de dix total du concurrent
	 * @param  dix
	 */
	@Deprecated
	public void setNeuf(int neuf) {
		//this.neuf = neuf;
		if(neuf > 0) {
			if(departages[1] == 0)
				departages[1] = neuf;
		}
	}

	/**
	 * Déprecié, retourne toujours 0
	 * @return  int
	 */
	@Deprecated
	public int getNeuf() {
		return 0;
	}

	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * @param  dixPlus
	 */
	@Deprecated
	public void setDix(int dix) {
		//this.dix = dixPlus;
		if(dix > 0) {
			if(departages[0] == 0)
				departages[0] = dix;
		}
	}

	/**
	 * Déprecié, retourne toujours 0
	 * @return  int
	 */
	@Deprecated
	public int getDix() {
		return 0;
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
	 * informe sur la présentation ou non de l'archer au greffe
	 * 
	 * @return presence <i>true</i> si l'archer est coché comme présenté au greffe, <i><false</i> sinon
	 */
	public boolean isPresence() {
		return presence;
	}

	/**
	 * définit si l'archer s'est présenté au greffe on non
	 * 
	 * @param presence <i>true</i> si l'archer s'est présenté au greffe, <i>false</i> sonon
	 */
	public void setPresence(boolean presence) {
		this.presence = presence;
	}

	/**
	 * informe sur le surclassement éventuel de l'archer
	 * 
	 * @return surclassement <i>true</i> si le concurrent est surclassé, <i>false</i> sinon
	 */
	public boolean isSurclassement() {
		return surclassement;
	}

	/**
	 * définit si l'archer est suclassé ou non
	 * 
	 * @param surclassement <i>true</i> si l'archer est surclassé, <i>false</i> sinon
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
	 * compare le score du concurrent avec celui d'un autre
	 * 
	 * @param other le concurrent avec lequel comparer le score
	 * @return 1 si le concurrent à le score le plus élevé, 0 si ex equo, -1 si le second concurrent à le meilleur score.
	 */
	public int compareScoreWith(Concurrent other) {
		if(getTotalScore() > other.getTotalScore()) {
			return 1;
		} else if(other.getTotalScore() == getTotalScore()) {
			return compareDepartageWith(other);
		}
		return -1;
	}
	
	/**
	 * compare de maniere récursive les départages d'ex-equo
	 * 
	 * @param other le concurrent avec lequel comparer
	 * @return 1 si le concurrent à le score le plus élevé, 0 si ex equo, -1 si le second concurrent à le meilleur score.
	 */
	public int compareDepartageWith(Concurrent other) {
		return compareDepartageWith(other, 0);
	}
	
	private int compareDepartageWith(Concurrent other, int indexDepartage) {
		if(indexDepartage < departages.length) {
			if(indexDepartage >= other.getDepartages().length) {
				if(departages[indexDepartage] > 0)
					return 1;
				return 0;
			} else if(departages[indexDepartage] > other.getDepartages()[indexDepartage]) {
				return 1;
			} else if(departages[indexDepartage] == other.getDepartages()[indexDepartage]) {
				return compareDepartageWith(other, indexDepartage+1);
			}
			return -1;
		}
		return 0;
	}
	
	/**
	 * Sauvegarde le jeux de critère associé à l'archer
	 * 
	 * @param reglement le réglement pour lequel s'applique le jeux de critère
	 */
	public void saveCriteriaSet(Reglement reglement) {
		if(!getNumLicenceArcher().equals("")) { //$NON-NLS-1$
			criteriaSet.save(reglement);
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
	
	/**
	 * Clone l'objet concurrent
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Concurrent clone() {
		try {
			Concurrent clone = (Concurrent)super.clone();
			clone.points = (ArrayList<Integer>)points.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
}