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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.sql.SqlField;
import org.ajdeveloppement.commons.sql.SqlForeignFields;
import org.ajdeveloppement.commons.sql.SqlPersistance;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.sql.SqlStoreHelper;
import org.ajdeveloppement.commons.sql.SqlTable;

/**
 * <p>
 * Représentation d'un règlement de concours. Un règlement fixe les règles
 * arbitral appliqué à un concours. Un seul règlement peut être appliqué sur un
 * concours, et à plus forte raison à tous les archers du concours.
 * </p>
 * <p>
 * On retrouve dans un règlement les éléments essentiel afin de compter les
 * points ainsi que l'ensemble des critères de classement et de placement qui
 * doivent être appliqué sur un concours.
 * </p>
 * <p>
 * Un règlement peut être qualifié d'"officiel" ou non. Si il est qualifié
 * d'officiel, celui ci ne devrait pas être altéré par les vue/contrôleur. La
 * méthode {@link #isOfficialReglement()}</i> est utilisé pour déterminé si le
 * règlement doit être considéré ou non comme officiel. Cette qualification doit
 * permettre d'effectuer des classement inter-club, inter-concours avec
 * l'assurance que les critères d'évaluation sont en tout point identique.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="REGLEMENT")
@SqlPrimaryKey(fields="NUMREGLEMENT",generatedidField="NUMREGLEMENT")
@SqlForeignFields(fields={"NUMFEDERATION"})
public class Reglement implements SqlPersistance {
	
	public enum TypeReglement {
		/**
		 * Règlement sur cible anglaise
		 */
		TARGET,
		/**
		 * Règlement nature (3D, Campagne, Tir Nature)
		 */
		NATURE
	}

	@XmlAttribute
	private int version = 1;
	@XmlTransient
	@SqlField(name="NUMREGLEMENT")
	private int numReglement = 0;
	@XmlAttribute
	@XmlID
	@SqlField(name="NOMREGLEMENT")
	private String name = "default"; //$NON-NLS-1$
	@SqlField(name="LIBELLE")
	private String displayName = ""; //$NON-NLS-1$
	
	private TypeReglement reglementType = TypeReglement.TARGET;

	@SqlField(name="NBSERIE")
	private int nbSerie = 2;
	@SqlField(name="NBVOLEEPARSERIE")
	private int nbVoleeParSerie = 6;
	@SqlField(name="NBFLECHEPARVOLEE")
	private int nbFlecheParVolee = 3;
	@SqlField(name="NBMEMBRESEQUIPE")
	private int nbMembresEquipe = 4;
	@SqlField(name="NBMEMBRESRETENU")
	private int nbMembresRetenu = 3;

	@XmlElementWrapper(name="criteria",required=true)
    @XmlElement(name="criterion")
	private List<Criterion> listCriteria = new ArrayList<Criterion>();
	private Map<CriteriaSet, CriteriaSet> surclassement = new HashMap<CriteriaSet, CriteriaSet>();
	@XmlElementWrapper(name="listdistancesetblason",required=true)
    @XmlElement(name="distancesetblason")
	private List<DistancesEtBlason> listDistancesEtBlason = new ArrayList<DistancesEtBlason>();
	@XmlElementWrapper(name="departages",required=true)
    @XmlElement(name="departage")
	private List<String> tie = new ArrayList<String>();

	@SqlField(name="ISOFFICIAL")
	private boolean officialReglement = false;
	private Federation federation = new Federation();
	@SqlField(name="NUMCATEGORIE_REGLEMENT")
	private int category = 0;
	@SqlField(name="REMOVABLE")
	private boolean removable = true;
	
	private boolean inDatabase = true; 
	
	private static SqlStoreHelper<Reglement> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<Reglement>(ApplicationCore.dbConnection, Reglement.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@XmlTransient
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Constructeur java-beans. Initialise un règlement par défaut
	 * 
	 */
	public Reglement() {
	}

	/**
	 * Initialise un règlement par défaut en le nommant
	 * 
	 * @param name le nom du règlement à créer
	 */
	public Reglement(String name) {
		this.name = name;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	/**
	 * Retourne le numéro de version interne du règlement.
	 * 
	 * @return version le numéro de version du règlement.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Définit le numéro interne de version du règlement
	 * 
	 * @param version le numéro de version du règlement.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return numReglement
	 */
	public int getNumReglement() {
		return numReglement;
	}

	/**
	 * @param numReglement numReglement à définir
	 */
	public void setNumReglement(int numReglement) {
		this.numReglement = numReglement;
		
		//force le recalcul du hashCode des Entry
		surclassement = new HashMap<CriteriaSet, CriteriaSet>(surclassement);
	}

	/**
	 * Retourne le nom du règlement
	 * 
	 * @return le nom du règlement
	 */
	public String getName() {
		return name;
	}

	/**
	 * Donne ou change le nom du règlement
	 * 
	 * @param name le nom à donner au règlement
	 */
	public void setName(String name) {
		Object oldValue = this.name;
		
		this.name = name;
		
		pcs.firePropertyChange("name", oldValue, name); //$NON-NLS-1$
	}

	/**
	 * Retourne le nom à afficher du règlement
	 * 
	 * @return displayName le nom à afficher du règlement
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Définit le nom à afficher du règlement
	 * 
	 * @param displayName le nom à afficher du règlement
	 */
	public void setDisplayName(String displayName) {
		Object oldValue = this.displayName;
		
		this.displayName = displayName;
		
		pcs.firePropertyChange("displayName", oldValue, displayName); //$NON-NLS-1$
	}

	/**
	 * Retourne le type de règlement
	 * 
	 * @return reglementType le type de règlement
	 */
	public TypeReglement getReglementType() {
		return reglementType;
	}

	/**
	 * Définit le type de règlement
	 * 
	 * @param reglementType le type de règlement
	 */
	public void setReglementType(TypeReglement reglementType) {
		Object oldValue = this.reglementType;
		
		this.reglementType = reglementType;
		
		pcs.firePropertyChange("reglementType", oldValue, reglementType); //$NON-NLS-1$
	}

	/**
	 * <p>
	 * Retourne la liste des critères de distinction des archers pouvant être
	 * utilisé sur les concours exploitant ce règlement.
	 * </p>
	 * <p>
	 * Les critères retournés peuvent être soit determinant pour le classement,
	 * le placement, les deux ou simplement informatif.
	 * </p>
	 * 
	 * @return la liste des critères de distinction utilisé pour le règlement
	 */
	public List<Criterion> getListCriteria() {
		return listCriteria;
	}

	/**
	 * Définit la liste des critères de distinction du règlement.
	 * 
	 * <i>Méthode essentiellement utile à la déserialisation. Ne devrait pas être
	 * utilisé directement.</i>
	 * 
	 * @param listCriteria la liste des critères de distinction du règlement.
	 */
	public void setListCriteria(List<Criterion> listCriteria) {
		Object oldValue = this.listCriteria;
		
		this.listCriteria = listCriteria;
		
		for(Criterion criterion : listCriteria)
			criterion.setReglement(this);
		
		pcs.firePropertyChange("listCriteria", oldValue, listCriteria); //$NON-NLS-1$
	}

	/**
	 * Retourne le tableau de surclassement à appliquer sur
	 * le règlement
	 * 
	 * @return le tableau de surclassement
	 */
	public Map<CriteriaSet, CriteriaSet> getSurclassement() {
		return surclassement;
	}

	/**
	 * Définit le tableau de surclassement à appliquer sur
	 * le règlement
	 * 
	 * @param surclassement le tableau de surclassement
	 */
	public void setSurclassement(Map<CriteriaSet, CriteriaSet> surclassement) {
		Object oldValue = this.surclassement;
		
		this.surclassement = surclassement;
		
		for(Entry<CriteriaSet, CriteriaSet> entry : surclassement.entrySet()) {
			entry.getKey().setReglement(this);
			if(entry.getValue() != null)
				entry.getValue().setReglement(this);
		}
		
		pcs.firePropertyChange("surclassement", oldValue, surclassement); //$NON-NLS-1$
	}

	/**
	 * Retourne la liste des couples distances/blasons exploité avec le règlement
	 * 
	 * @return la liste des couples distances/blasons
	 */
	public List<DistancesEtBlason> getListDistancesEtBlason() {
		return listDistancesEtBlason;
	}

	/**
	 * Définit la liste des couples distances/blasons exploité avec le règlement
	 * 
	 * @param listDistancesEtBlason la liste des couples distances/blasons
	 */
	public void setListDistancesEtBlason(List<DistancesEtBlason> listDistancesEtBlason) {
		Object oldValue = this.listDistancesEtBlason;
		
		this.listDistancesEtBlason = listDistancesEtBlason;
		
		pcs.firePropertyChange("listDistancesEtBlason", oldValue, listDistancesEtBlason); //$NON-NLS-1$
	}

	/**
	 * Retourne le couple distances blason associé à un jeux de critères de placement donnée
	 * 
	 * @param criteriaSet
	 *            le jeux de critère pour lequel récupérer le DistancesBlason
	 * @return le DistancesEtBlason associé au jeux ou null si aucun
	 */
	public List<DistancesEtBlason> getDistancesEtBlasonFor(CriteriaSet criteriaSet) {
		List<DistancesEtBlason> csDB = new ArrayList<DistancesEtBlason>();
		for (DistancesEtBlason db : listDistancesEtBlason) {
			if (db.getCriteriaSet().equals(criteriaSet)) {
				csDB.add(db);
			}
		}
		return csDB;
	}

	/**
	 * Ajoute un couple distances/Blasons au règlement
	 * 
	 * @param distancesEtBlason le distances/Blasons à rajouter
	 */
	public void addDistancesEtBlason(DistancesEtBlason distancesEtBlason) {
		listDistancesEtBlason.add(distancesEtBlason);
	}

	/**
	 * Renvoi la politique de placement.
	 * 
	 * @return Renvoi le filtre de critère en place
	 *         pour le placement des archers
	 */
	public Map<Criterion, Boolean> getPlacementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for (Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isPlacement());
		}

		return filterCriteria;
	}

	/**
	 * Renvoi la politique de classement
	 * 
	 * @return Renvoi le filtre de critère en place
	 *         pour le classement des archers
	 */
	public Map<Criterion, Boolean> getClassementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for (Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isClassement());
		}

		return filterCriteria;
	}
	
	/**
	 * Retourne la liste des critères de classement valide sur le règlement,
	 * sont donc exclue de la liste les jeux de critères surclassé ou interdit
	 * 
	 * @return liste des critères de classement valide sur le règlement
	 */
	public List<CriteriaSet> getValidClassementCriteriaSet() {
		CriteriaSet[] lccs = CriteriaSet.listCriteriaSet(this, getClassementFilter());
		List<CriteriaSet> validCS = new ArrayList<CriteriaSet>();
		
		for(CriteriaSet cs : lccs) {
			if(!surclassement.containsKey(cs))
				validCS.add(cs);
		}
		
		return validCS;
	}
	
	/**
	 * Retourne la liste des critères de placement valide sur le règlement,
	 * sont donc exclue de la liste les jeux de critères surclassé ou interdit
	 * 
	 * @return liste des critères de placement valide sur le règlement
	 */
	public List<CriteriaSet> getValidPlacementCriteriaSet() {
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
		
		return placementCS;
	}

	/**
	 * Retourne le nombre de flèche tiré par volée imposé par le règlement
	 * <p>
	 * La valeur par défaut est fixé à 3
	 * 
	 * @return le nombre de flèches tiré par volée
	 */
	public int getNbFlecheParVolee() {
		return nbFlecheParVolee;
	}

	/**
	 * Définit le nombre de flèches tiré par volée imposé par le règlement
	 * 
	 * @param nbFlecheParVolee le nombre de flèches tiré par volée
	 */
	public void setNbFlecheParVolee(int nbFlecheParVolee) {
		Object oldValue = this.nbFlecheParVolee;
		
		this.nbFlecheParVolee = nbFlecheParVolee;
		
		pcs.firePropertyChange("nbFlecheParVolee", oldValue, nbFlecheParVolee); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre maximum de concurrents que peut contenir une équipe
	 * sur un concours avec ce règlement
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
	 * sur un concours avec ce règlement
	 * 
	 * @param nbMembresEquipe le nombre maximum de concurrents que peut contenir une équipe
	 */
	public void setNbMembresEquipe(int nbMembresEquipe) {
		Object oldValue = this.nbMembresEquipe;
		
		this.nbMembresEquipe = nbMembresEquipe;
		
		pcs.firePropertyChange("nbMembresEquipe", oldValue, nbMembresEquipe); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre de concurrents, membre d'une équipe dont les points seront
	 * comptabilisés pour le classement par équipe
	 * <p>
	 * La valeur par défaut est fixé à 3
	 * 
	 * @return le nombre de concurrents d'une équipe dont les points seront comptabilisés
	 */
	public int getNbMembresRetenu() {
		return nbMembresRetenu;
	}

	/**
	 * Définit le nombre de concurrents, membre d'une équipe dont les points seront
	 * comptabilisés pour le classement par équipe
	 * 
	 * @param nbMembresRetenu le nombre de concurrents d'une équipe dont les points seront comptabilisés
	 */
	public void setNbMembresRetenu(int nbMembresRetenu) {
		Object oldValue = this.nbMembresRetenu;
		
		this.nbMembresRetenu = nbMembresRetenu;
		
		pcs.firePropertyChange("nbMembresRetenu", oldValue, nbMembresRetenu); //$NON-NLS-1$
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
		Object oldValue = this.nbSerie;
		
		this.nbSerie = nbSerie;
		
		pcs.firePropertyChange("nbSerie", oldValue, nbSerie); //$NON-NLS-1$
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
		Object oldValue = this.nbVoleeParSerie;
		
		this.nbVoleeParSerie = nbVoleeParSerie;
		
		pcs.firePropertyChange("nbVoleeParSerie", oldValue, nbVoleeParSerie); //$NON-NLS-1$
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
		Object oldValue = this.tie;
		
		this.tie = tie;
		
		pcs.firePropertyChange("tie", oldValue, tie); //$NON-NLS-1$
	}

	/**
	 * Permet d'identifié le règlement comme officiel ou non.<br>
	 * Un règlement officiel ne devrait pas être altéré au cours de sa vie.
	 * 
	 * @return true si le règlement est qualifié d'officiel, false dans le cas
	 *         contraire.
	 */
	public boolean isOfficialReglement() {
		return officialReglement;
	}

	/**
	 * <p>
	 * Définit si le règlement est ou non officiel
	 * </p>
	 * <p>
	 * <i>Méthode essentiellement utile à la déserialisation et aux outils de
	 * débugage. Ne devrait pas être utilisé directement.</i>
	 * </p>
	 * 
	 * @param officialReglement
	 *            true pour un règlement officiel, false sinon
	 */
	public void setOfficialReglement(boolean officialReglement) {
		Object oldValue = this.officialReglement;
		
		this.officialReglement = officialReglement;
		
		pcs.firePropertyChange("officialReglement", oldValue, officialReglement); //$NON-NLS-1$
	}

	/**
	 * Détermine si un tableau de score donnée est ou non valide su le règlement
	 * 
	 * @param scores le tableau de score à validé
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
	 * Définit la fédération associé à un règlement
	 * 
	 * @param federation la fédération associé à un règlement
	 */
	public void setFederation(Federation federation) {
		Object oldValue = this.federation;
		
		this.federation = federation;
		
		pcs.firePropertyChange("federation", oldValue, federation); //$NON-NLS-1$
	}

	/**
	 * Retourne la fédération associé à un règlement
	 * 
	 * @return la fédération associé à un règlement
	 */
	public Federation getFederation() {
		return federation;
	}

	/**
	 * Définit le numéro de la catégorie du règlement<br>
	 * La correspondance entre les numéros de catégorie et leurs libellé
	 * est stocké dans la table CATEGORIE_REGLEMENT   
	 * 
	 * @param category le numéro de la catégorie du règlement
	 */
	public void setCategory(int category) {
		Object oldValue = this.category;
		
		this.category = category;
		
		pcs.firePropertyChange("category", oldValue, category); //$NON-NLS-1$
	}

	/**
	 * Retourne le numéro de la catégorie du règlement<br>
	 * La correspondance entre les numéros de catégorie et leurs libellé
	 * sont stockés dans la table CATEGORIE_REGLEMENT
	 * 
	 * @return le numéro de la catégorie du règlement
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * Définit si le règlement peut ou non être supprimé de la base de données
	 * 
	 * @param removable <code>true</code> si le règlement peut être supprimé.
	 */
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	/**
	 * Indique si le règlement peut être supprimé de la base ou non
	 * 
	 * @return removable <code>true</code> si le règlement peut être supprimé.
	 */
	public boolean isRemovable() {
		return removable;
	}

	/**
	 * Indique si le règlement est présent en base ou non. Si le règlement n'est pas
	 * présent en base, les concurrents se basant sur ce règlement ne peuvent pas
	 * voir leur catégorie rendu persistant.
	 * 
	 * @return <code>true</code> si le règlement est présent en base
	 */
	public boolean isInDatabase() {
		return inDatabase;
	}

	/**
	 * Définit si le règlement est présent en base ou non. Si le règlement n'est pas
	 * présent en base, les concurrents se basant sur ce règlement ne peuvent pas
	 * voir leur catégorie rendu persistant.
	 * 
	 * @param inDatabase <code>true</code> si le règlement est présent en base
	 */
	public void setInDatabase(boolean inDatabase) {
		this.inDatabase = inDatabase;
	}

	/**
	 * <p>Rend l'objet persistant. Sauvegarde l'ensemble des données de l'objet
	 * dans la base de donnée de ConcoursJeunes.</p>
	 * 
	 * <p>Les arguments sont ignoré.</p>
	 */
	@Override
	public void save() throws SqlPersistanceException {
		if(federation.getNumFederation() == 0)
			federation.save();
		//si le numéro de règlement est à 0, regarde si il n'existe pas malgré tous
		//une entré en base de données
		if(numReglement == 0) {
			try {
				String sql = "select NUMREGLEMENT from REGLEMENT where NOMREGLEMENT=?"; //$NON-NLS-1$
				PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
				try {
					pstmt.setString(1, name);
					ResultSet rs = pstmt.executeQuery();
					try {
						if(rs.first()) {
							setNumReglement(rs.getInt(1));
						}
					} finally {
						rs.close();
					}
				} finally {
					pstmt.close();
				}
			} catch (SQLException e) {
				throw new SqlPersistanceException(e);
			}
		}

		boolean creation = false;
		if(numReglement == 0)
			creation = true;
		helper.save(this, Collections.singletonMap("NUMFEDERATION", (Object)federation.getNumFederation())); //$NON-NLS-1$
		
		if(creation)
			setNumReglement(numReglement); //force le recalcule des hashCode des surclassements

		try {
			saveTie();
			// sauvegarde les tableaux de critères et correspondance
			saveCriteria();
			saveDistancesAndBlasons();
			saveSurclassement();
		} catch (SQLException e) {
			throw new SqlPersistanceException(e);
		}
	}
	
	private void saveTie() throws SQLException {
		Statement stmt = ApplicationCore.dbConnection.createStatement();
		try {
			stmt.executeUpdate("delete from DEPARTAGE where NUMREGLEMENT=" + numReglement); //$NON-NLS-1$
		} finally {
			stmt.close();
		}
		
		String sql = "insert into DEPARTAGE (NUMDEPARTAGE, NUMREGLEMENT, FIELDNAME) " + //$NON-NLS-1$
				"values (?, ?, ?)"; //$NON-NLS-1$
		PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
		int i = 1;
		for(String d : tie) {
			pstmt.setInt(1, i++);
			pstmt.setInt(2, numReglement);
			pstmt.setString(3, d);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Sauvegarde en base les critères de distinction des archers actif pour le réglement
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("nls")
	private void saveCriteria() throws SqlPersistanceException {
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				String codesCritere = "";
				for (Criterion criterion : listCriteria) {
					if(!codesCritere.isEmpty())
						codesCritere += ",";
					codesCritere += "'" + criterion.getCode().replace("'", "''") + "'";
				}

				stmt.executeUpdate("delete from CRITERE where NUMREGLEMENT=" + numReglement + " and CODECRITERE not in (" + codesCritere + ")");
			} finally {
				stmt.close();
			}
		
			int numordre = 1;
			for (Criterion criterion : listCriteria) {
				criterion.setNumordre(numordre++);
				criterion.save();
			}
		} catch (SQLException e) {
			throw new SqlPersistanceException(e);
		}
	}
	
	/**
	 * Sauvegarde en base le tableau de surclassement
	 * 
	 * @throws SQLException
	 */
	private void saveSurclassement() throws SqlPersistanceException {
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				stmt.executeUpdate("delete from SURCLASSEMENT where NUMREGLEMENT=" + numReglement); //$NON-NLS-1$
			} finally {
				stmt.close();
			}
			
			String sql = "insert into SURCLASSEMENT (NUMCRITERIASET, NUMREGLEMENT, NUMCRITERIASET_SURCLASSE) " + //$NON-NLS-1$
					"values (?, ?, ?)"; //$NON-NLS-1$
			PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
			try {
				for(Map.Entry<CriteriaSet, CriteriaSet> row : surclassement.entrySet()) {
					row.getKey().save();
					pstmt.setInt(1, row.getKey().getNumCriteriaSet());
					pstmt.setInt(2, numReglement);
					if(row.getValue() != null) {
						row.getValue().save();
						pstmt.setInt(3, row.getValue().getNumCriteriaSet());
					} else
						pstmt.setNull(3, Types.INTEGER);
					pstmt.executeUpdate();
				}
			} finally {
				pstmt.close();
			}
		} catch (SQLException e) {
			throw new SqlPersistanceException(e);
		}
	}

	/**
	 * Sauvegarde en base le tableau des distances/blasons
	 * 
	 * @throws SQLException
	 */
	private void saveDistancesAndBlasons() throws SqlPersistanceException {
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				stmt.executeUpdate("delete from DISTANCESBLASONS where NUMREGLEMENT=" + numReglement); //$NON-NLS-1$
			} finally {
				stmt.close();
			}
		
			int i = 1;
			for (DistancesEtBlason distancesEtBlason : listDistancesEtBlason) {
				distancesEtBlason.setNumdistancesblason(i++);
				distancesEtBlason.save();
			}
		} catch (SQLException e) {
			throw new SqlPersistanceException(e);
		}
	}

	/**
	 * Supprime la persistance du règlement. Cette persistance ne peut être
	 * supprimé qu'à la condition que le règlement ne soit pas officiel
	 * 
	 * @throws SqlPersistanceException
	 */
	@Override
	public void delete() throws SqlPersistanceException{
		if (!officialReglement) {
			helper.delete(this);
		} else
			throw new SqlPersistanceException("delete this Reglement is not authorized because there is official"); //$NON-NLS-1$
	}
	
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for(Entry<CriteriaSet, CriteriaSet> entry : surclassement.entrySet()) {
			entry.getKey().setReglement(this);
			if(entry.getValue() != null)
				entry.getValue().setReglement(this);
		}
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
	 * @return le nom du règlement
	 */
	@Override
	public String toString() {
		return displayName;
	}
}
