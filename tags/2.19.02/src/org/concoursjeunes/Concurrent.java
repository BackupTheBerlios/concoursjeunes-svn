/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 *  
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ajdeveloppement.commons.sql.SqlPersistanceException;

/**
 * Objet de Base de stockage des Information sur un concurrent:
 *  Nom Categorie Licence Cible Nombre de volée points
 *  
 * @author  Aurélien Jeoffray
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
	private Blason alternativeTargetFace = null;

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
		Object oldValue = this.departages;
		
		this.departages = departages;
		
		pcs.firePropertyChange("departages", oldValue, departages); //$NON-NLS-1$
	}

	/**
	 * Affecte le nombre de dix total du concurrent
	 * 
	 * @param  neuf
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
	 * @param  dix
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
		Object oldValue = this.depart;
		
		this.depart = depart;
		
		pcs.firePropertyChange("depart", oldValue, depart); //$NON-NLS-1$
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
		Object oldValue = this.targetPosition.getTarget();
		
		this.targetPosition.setTarget(cible);
		
		pcs.firePropertyChange("cible", oldValue, cible); //$NON-NLS-1$
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
		Object oldValue = this.targetPosition.getPosition();
		
		this.targetPosition.setPosition(position);
		
		pcs.firePropertyChange("position", oldValue, position); //$NON-NLS-1$
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
	 * Indique si l'archer utilise ou non le blason alternatif
	 * (par exemple tri-spot) permis par sa categorie
	 * 
	 * @return useAlternativeTargetFace true si l'on doit utiliser le
	 * blason alternatif
	 */
	public boolean isUseAlternativeTargetFace() {
		return alternativeTargetFace != null && !alternativeTargetFace.getName().isEmpty();
	}
	
	/**
	 * Retourne le blason alternatif de l'archer si permis par sa categorie
	 * 
	 * @return le blason alternatif de l'archer
	 */
	public Blason getAlternativeTargetFace() {
		return alternativeTargetFace;
	}

	/**
	 * Définit le blason alternatif de l'archer si permis par sa categorie
	 * 
	 * @param alternativeTargetFace le blason alternatif de l'archer
	 */
	public void setAlternativeTargetFace(Blason alternativeTargetFace) {
		this.alternativeTargetFace = alternativeTargetFace;
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
	 */
	public void saveCriteriaSet() throws SqlPersistanceException {
		if(!getNumLicenceArcher().equals("")) { //$NON-NLS-1$
			try {
				
				String sql = "select NUMREGLEMENT from REGLEMENT where NOMREGLEMENT='" + criteriaSet.getReglement().getName().replace("'", "''") + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				Statement stmt = ApplicationCore.dbConnection.createStatement();
				try {
					ResultSet rs = stmt.executeQuery(sql);
					try {
						if(!rs.first())
							return;
						
						int numreglement = rs.getInt(1);
						
						if(criteriaSet.getReglement().getNumReglement() != numreglement)
							criteriaSet.getReglement().setNumReglement(numreglement);
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}
			
				criteriaSet.save();
			
				sql = "select * from ARCHERS where NUMLICENCEARCHER=?"; //$NON-NLS-1$
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
					pstmt.setInt(2, criteriaSet.getReglement().getNumReglement());
					pstmt.setInt(3, criteriaSet.getNumCriteriaSet());
	
					pstmt.executeUpdate();
					pstmt.close();
				}
			} catch (SQLException e) {
				throw new SqlPersistanceException(e);
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