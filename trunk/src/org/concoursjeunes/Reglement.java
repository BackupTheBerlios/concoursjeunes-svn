/*
 * Créé le 02/03/2007 à 17:36 pour ConcoursJeunes
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Représentation d'un réglement de concours. Un réglement fixe les régles
 * arbitral appliqué à un concours. Un seur réglement peut être appliqué sur un
 * concours, et à plus forte raison à tous les archers du concours.
 * </p>
 * <p>
 * On retrouve dans un réglement les éléments essentiel afin de compter les
 * points ainsi que l'ensemble des critères de classement et de placement qui
 * doivent être appliqué sur un concours.
 * </p>
 * <p>
 * Un réglement peut être qualifié d'"officiel" ou non. Si il est qualifié
 * d'officiel, celui ci ne devrait pas être altéré par les vue/controlleur. La
 * methode <i>isOfficialReglement()</i> est utilisé pour déterminé si le
 * réglement doit être considéré ou non comme officiel. Cette qualification doit
 * permettre d'effectuer des classement inter-club, inter-concours avec
 * l'assurance que les cirtères d'évaluation sont en tout point identique.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 * 
 */
@XmlRootElement
public class Reglement {

	private String name = "default"; //$NON-NLS-1$

	private int nbSerie = 2;
	private int nbVoleeParSerie = 6;
	private int nbFlecheParVolee = 3;
	private int nbMembresEquipe = 4;
	private int nbMembresRetenu = 3;

	private List<Criterion> listCriteria = new ArrayList<Criterion>();
	private Map<CriteriaSet, CriteriaSet> surclassement = new HashMap<CriteriaSet, CriteriaSet>();
	private List<DistancesEtBlason> listDistancesEtBlason = new ArrayList<DistancesEtBlason>();
	private List<String> tie = new ArrayList<String>();

	private boolean officialReglement = false;
	private Federation federation = new Federation();
	private int category = 0;
	private boolean removable = true;

	/**
	 * Constructeur java-beans. Initialise un réglement par défaut
	 * 
	 */
	public Reglement() {
	}

	/**
	 * Initialise un réglement par défaut en le nommant
	 * 
	 * @param name le nom du reglement à créer
	 */
	public Reglement(String name) {
		this.name = name;
	}

	/**
	 * Retourne le nom du réglement
	 * 
	 * @return le nom du réglement
	 */
	public String getName() {
		return name;
	}

	/**
	 * Donne ou change le nom du réglement
	 * 
	 * @param name
	 *            le nom à donner au réglement
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * Retourne la liste des critères de distinction des archers pouvant être
	 * utilisé sur les concours exploitant ce réglement.
	 * </p>
	 * <p>
	 * Les critères retournés peuvent être soit determinant pour le classement,
	 * le placement, les deux ou simplement informatif.
	 * </p>
	 * 
	 * @return la liste des critéres de distinction utilisé pour le réglement
	 */
	public List<Criterion> getListCriteria() {
		return listCriteria;
	}

	/**
	 * Définit la liste des critères de distinction du réglement.
	 * 
	 * <i>Methode essentielement utile à la déserialisation. Ne devrait pas être
	 * utilisé directement.</i>
	 * 
	 * @param listCriteria
	 *            the listCriteria to set
	 */
	public void setListCriteria(List<Criterion> listCriteria) {
		this.listCriteria = listCriteria;
	}

	/**
	 * Retourne le tableau de surclassement à appliquer sur
	 * le réglement
	 * 
	 * @return le tableau de surclassement
	 */
	public Map<CriteriaSet, CriteriaSet> getSurclassement() {
		return surclassement;
	}

	/**
	 * Définit le tableau de surclassement à appliquer sur
	 * le réglement
	 * 
	 * @param surclassement le tableau de surclassement
	 */
	public void setSurclassement(Map<CriteriaSet, CriteriaSet> surclassement) {
		this.surclassement = surclassement;
	}

	/**
	 * Retourne la liste des couples distances/blasons exploité avec le réglement
	 * 
	 * @return la liste des couples distances/blasons
	 */
	public List<DistancesEtBlason> getListDistancesEtBlason() {
		return listDistancesEtBlason;
	}

	/**
	 * Définit la liste des couples distances/blasons exploité avec le réglement
	 * 
	 * @param listDistancesEtBlason la liste des couples distances/blasons
	 */
	public void setListDistancesEtBlason(List<DistancesEtBlason> listDistancesEtBlason) {
		this.listDistancesEtBlason = listDistancesEtBlason;
	}

	/**
	 * Retourne le couple distances blason associé à un jeux de critères donnée
	 * 
	 * @param criteriaSet
	 *            le jeux de critère pour lequel récuperer le DistancesBlason
	 * @return le DistancesEtBlason associé au jeux ou null si aucun
	 */
	public DistancesEtBlason getDistancesEtBlasonFor(CriteriaSet criteriaSet) {
		for (DistancesEtBlason db : listDistancesEtBlason) {
			if (db.getCriteriaSet().equals(criteriaSet)) {
				return db;
			}
		}
		return null;
	}

	/**
	 * Ajoute un couple distances/Blasons au réglement
	 * 
	 * @param distancesEtBlason le distances/Blasons à rajouter
	 */
	public void addDistancesEtBlason(DistancesEtBlason distancesEtBlason) {
		listDistancesEtBlason.add(distancesEtBlason);
	}

	/**
	 * Renvoi la politique de placement.
	 * 
	 * @return Hashtable<String, Boolean> Renvoi le filtre de critere en place
	 *         pour le placement des archers
	 */
	public Hashtable<Criterion, Boolean> getPlacementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for (Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isPlacement());
		}

		return filterCriteria;
	}

	/**
	 * Renvoi la politique de classement
	 * 
	 * @return Hashtable<String, Boolean> Renvoi le filtre de critere en place
	 *         pour le classement des archers
	 */
	public Hashtable<Criterion, Boolean> getClassementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for (Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isClassement());
		}

		return filterCriteria;
	}
	
	/**
	 * Retourne la liste des critères de classement valide sur le réglement,
	 * sont donc exclue de la liste les jeux de critères surclassé ou interdit
	 * 
	 * @return liste des critères de classement valide sur le réglement
	 */
	public CriteriaSet[] getValidClassementCriteriaSet() {
		CriteriaSet[] lccs = CriteriaSet.listCriteriaSet(this, getClassementFilter());
		List<CriteriaSet> validCS = new ArrayList<CriteriaSet>();
		
		for(CriteriaSet cs : lccs) {
			if(!surclassement.containsKey(cs))
				validCS.add(cs);
		}
		
		return validCS.toArray(new CriteriaSet[validCS.size()]);
	}
	
	/**
	 * Retourne la liste des critères de placement valide sur le réglement,
	 * sont donc exclue de la liste les jeux de critères surclassé ou interdit
	 * 
	 * @return liste des critères de placement valide sur le réglement
	 */
	public CriteriaSet[] getValidPlacementCriteriaSet() {
		List<CriteriaSet> validCS = new ArrayList<CriteriaSet>();
		List<CriteriaSet> placementCS = new ArrayList<CriteriaSet>();
		CriteriaSet[] lccs = CriteriaSet.listCriteriaSet(this, getClassementFilter());
		CriteriaSet[] lpcs = CriteriaSet.listCriteriaSet(this, getPlacementFilter());

		for(CriteriaSet cs : lccs) {
			if(!surclassement.containsKey(cs) && !validCS.contains(cs.getFilteredCriteriaSet(getPlacementFilter())))
				validCS.add(cs.getFilteredCriteriaSet(getPlacementFilter()));
		}
		
		//permet de conserver l'ordre des critères de placements
		for(CriteriaSet cs : lpcs) {
			if(validCS.contains(cs))
				placementCS.add(cs);
		}
		
		return placementCS.toArray(new CriteriaSet[placementCS.size()]);
	}

	/**
	 * Retourne le nombre de fleche tiré par volée imposé par le réglement
	 * <p>
	 * La valeur par défaut est fixé à 3
	 * 
	 * @return le nombre de fleche tiré par voléee
	 */
	public int getNbFlecheParVolee() {
		return nbFlecheParVolee;
	}

	/**
	 * Définit le nombre de fleche tiré par volée imposé par le réglement
	 * 
	 * @param nbFlecheParVolee le nombre de fleche tiré par voléee
	 */
	public void setNbFlecheParVolee(int nbFlecheParVolee) {
		this.nbFlecheParVolee = nbFlecheParVolee;
	}

	/**
	 * Retourne le nombre maximum de concurrents que peut contenir une équipe
	 * sur un concours avec ce réglement
	 * <p>
	 * La valeur par défaut est fixé à 4
	 * 
	 * @return le nombre maximum de concurrents que peut contenir une équipe
	 */
	public int getNbMembresEquipe() {
		return nbMembresEquipe;
	}

	/**
	 * Définit le nombre maximum de concurrents que peut contenir une équipe
	 * sur un concours avec ce réglement
	 * 
	 * @param nbMembresEquipe le nombre maximum de concurrents que peut contenir une équipe
	 */
	public void setNbMembresEquipe(int nbMembresEquipe) {
		this.nbMembresEquipe = nbMembresEquipe;
	}

	/**
	 * Retourne le nombre de concurrents, membre d'une équipe dont les points seront
	 * comptablisé pour le classement par équipe
	 * <p>
	 * La valeur par défaut est fixé à 3
	 * 
	 * @return le nombre de concurrents d'une équipe dont les points seront comptablisé
	 */
	public int getNbMembresRetenu() {
		return nbMembresRetenu;
	}

	/**
	 * Définit le nombre de concurrents, membre d'une équipe dont les points seront
	 * comptablisé pour le classement par équipe
	 * 
	 * @param nbMembresRetenu le nombre de concurrents d'une équipe dont les points seront comptablisé
	 */
	public void setNbMembresRetenu(int nbMembresRetenu) {
		this.nbMembresRetenu = nbMembresRetenu;
	}

	/**
	 * Retourne le nombre de séries de volées (distances) que compte le concours
	 *   
	 * @return le nombre de séries devant être réalisé sur le concours
	 */
	public int getNbSerie() {
		return nbSerie;
	}

	/**
	 * Définit le nombre de séries de volées (distances) que compte le concours
	 * 
	 * @param nbSerie le nombre de séries devant être réalisé sur le concours
	 */
	public void setNbSerie(int nbSerie) {
		this.nbSerie = nbSerie;
	}

	/**
	 * Définit le nombre de volées que devra tirer un archer dans une série
	 * 
	 * @return le nombre de volées dans une série
	 */
	public int getNbVoleeParSerie() {
		return nbVoleeParSerie;
	}

	/**
	 * Retourne le nombre de volées que devra tirer un archer dans une série
	 * 
	 * @param nbVoleeParSerie le nombre de volées dans une série
	 */
	public void setNbVoleeParSerie(int nbVoleeParSerie) {
		this.nbVoleeParSerie = nbVoleeParSerie;
	}

	/**
	 * Retourne la liste des champs de départage
	 * 
	 * @return la liste des départage
	 */
	public List<String> getTie() {
		return tie;
	}

	/**
	 * Défini la liste des champs de départage
	 * 
	 * @param tie la liste des champs de départage
	 */
	public void setTie(List<String> tie) {
		this.tie = tie;
	}

	/**
	 * Permet d'identifié le réglement comme officiel ou non.<br>
	 * Un réglement officiel ne devrait pas être altéré au cours de sa vie.
	 * 
	 * @return true si le réglement est qualifié d'officiel, false dans le cas
	 *         contraire.
	 */
	public boolean isOfficialReglement() {
		return officialReglement;
	}

	/**
	 * <p>
	 * Définit si le réglement est ou non officiel
	 * </p>
	 * <p>
	 * <i>Methode essentielement utile à la déserialisation et aux outils de
	 * débugage. Ne devrait pas être utilisé directement.</i>
	 * </p>
	 * 
	 * @param officialReglement
	 *            true pour un réglement officiel, false sinon
	 */
	public void setOfficialReglement(boolean officialReglement) {
		this.officialReglement = officialReglement;
	}

	/**
	 * Détermine si un tableau de score donnée est ou non valide su le réglement
	 * 
	 * @param scores
	 *            le tableau de score à validé
	 * @return true si le score est valide, false dans le cas contraire
	 */
	public boolean isValidScore(List<Integer> scores) {
		boolean valid = true;
		for (int score : scores) {
			if (score > nbVoleeParSerie * nbFlecheParVolee * 10) {
				valid = false;
				break;
			}
		}
		return valid;
	}

	/**
	 * @param federation federation à définir
	 */
	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	/**
	 * @return federation
	 */
	public Federation getFederation() {
		return federation;
	}

	/**
	 * Définit le numéro de la catégorie du réglement<br>
	 * La correspondance entre les numéros de catégorie et leurs libéllé
	 * est stocké dans la table CATEGORIE_REGLEMENT   
	 * 
	 * @param category le numéro de la catégorie du réglement
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * Retourne le numéro de la catégorie du réglement<br>
	 * La correspondance entre les numéros de catégorie et leurs libéllé
	 * est stocké dans la table CATEGORIE_REGLEMENT
	 * 
	 * @return le numéro de la catégorie du réglement
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param removable removable à définir
	 */
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	/**
	 * @return removable
	 */
	public boolean isRemovable() {
		return removable;
	}

	/**
	 * Rend l'objet persistant. Sauvegarde l'ensemble des données de l'objet
	 * dans la base de donnée de ConcoursJeunes.
	 * 
	 */
	public void save() throws SQLException {
		String sql = "merge into REGLEMENT (NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE," + //$NON-NLS-1$
				"NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL, NUMFEDERATION, " + //$NON-NLS-1$
				"NUMCATEGORIE_REGLEMENT, REMOVABLE) " + //$NON-NLS-1$
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //$NON-NLS-1$

		// Statement stmt = ConcoursJeunes.dbConnection.createStatement();
		PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
		pstmt.setInt(1, hashCode());
		pstmt.setString(2, name);
		pstmt.setInt(3, nbSerie);
		pstmt.setInt(4, nbVoleeParSerie);
		pstmt.setInt(5, nbFlecheParVolee);
		pstmt.setInt(6, nbMembresEquipe);
		pstmt.setInt(7, nbMembresRetenu);
		pstmt.setBoolean(8, officialReglement);
		pstmt.setInt(9, federation.getNumFederation());
		pstmt.setInt(10, category);
		pstmt.setBoolean(11, removable);

		pstmt.executeUpdate();
		pstmt.close();

		// sauvegarde les tableaux de crières et correspondance
		saveCriteria();
		saveDistancesAndBlasons();
		saveSurclassement();
	}

	/**
	 * Sauvegarde en base les critères de distinction des archers actif pour le réglement
	 * 
	 * @throws SQLException
	 */
	private void saveCriteria() throws SQLException {
		int numordre = 1;
		for (Criterion criterion : listCriteria) {
			criterion.setNumordre(numordre++);
			criterion.save(hashCode());
		}
	}
	
	/**
	 * Sauvegarde en base le tableau de surclassement
	 * 
	 * @throws SQLException
	 */
	private void saveSurclassement() throws SQLException {
		Statement stmt = ApplicationCore.dbConnection.createStatement();
		stmt.executeUpdate("delete from SURCLASSEMENT where NUMREGLEMENT=" + hashCode()); //$NON-NLS-1$
		stmt.close();
		
		String sql = "insert into SURCLASSEMENT (NUMCRITERIASET, NUMREGLEMENT, NUMCRITERIASET_SURCLASSE) " + //$NON-NLS-1$
				"values (?, ?, ?)"; //$NON-NLS-1$
		PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
		for(Map.Entry<CriteriaSet, CriteriaSet> row : surclassement.entrySet()) {
			row.getKey().save(hashCode());
			pstmt.setInt(1, row.getKey().hashCode());
			pstmt.setInt(2, hashCode());
			if(row.getValue() != null) {
				row.getValue().save(hashCode());
				pstmt.setInt(3, row.getValue().hashCode());
			} else
				pstmt.setNull(3, Types.INTEGER);
			pstmt.executeUpdate();
		}
		pstmt.close();
	}

	/**
	 * Sauvegarde en base le tableau des distances/blasons
	 * 
	 * @throws SQLException
	 */
	private void saveDistancesAndBlasons() throws SQLException {
		for (DistancesEtBlason distancesEtBlason : listDistancesEtBlason) {
			distancesEtBlason.save(hashCode());
		}
	}

	/**
	 * Supprime la persistance du réglement. Cette persistance ne peut être
	 * supprimé qu'à la condition que le réglement ne soit pas officiel
	 * 
	 * @return true si suppression effective, false sinon.
	 */
	public boolean delete() {
		boolean success = false;

		if (!officialReglement) {
			try {
				Statement stmt = ApplicationCore.dbConnection.createStatement();

				stmt.executeUpdate("delete from REGLEMENT where NUMREGLEMENT=" + hashCode()); //$NON-NLS-1$

				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + name.hashCode();

		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reglement other = (Reglement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @return le nom du réglement
	 */
	@Override
	public String toString() {
		return name;
	}
}
