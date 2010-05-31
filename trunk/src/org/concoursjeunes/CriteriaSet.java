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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.ajdeveloppement.commons.JAXBMapRefAdapter;
import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlForeignKey;
import org.ajdeveloppement.commons.persistence.sql.SqlGeneratedIdField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.commons.persistence.sql.SqlUnmappedFields;

/**
 * Jeux de critères utilisé pour distinguer un archer a des fins
 * de classements/placements et/ou informations
 * 
 * @author  Aurélien Jeoffray
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CRITERIASET")
@SqlPrimaryKey(fields="NUMCRITERIASET",generatedidField=@SqlGeneratedIdField(name="NUMCRITERIASET",type=Types.INTEGER))
@SqlUnmappedFields(fields={"IDCRITERIASET"})
public class CriteriaSet implements ObjectPersistence {

	@XmlTransient
	@SqlField(name="NUMCRITERIASET")
	private int numCriteriaSet = 0;

	@XmlTransient
	@SqlForeignKey(mappedTo="NUMREGLEMENT")
	private Reglement reglement;
	@XmlJavaTypeAdapter(JAXBMapRefAdapter.class)
	private Map<Criterion, CriterionElement> criteria = new HashMap<Criterion, CriterionElement>(5);
	
	private static StoreHelper<CriteriaSet> helper = null;
	static {
		try {
			helper = new StoreHelper<CriteriaSet>(new SqlStoreHandler<CriteriaSet>(ApplicationCore.dbConnection, CriteriaSet.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public CriteriaSet() {
	}
	
	public CriteriaSet(Reglement reglement) {
		this(reglement, null);
	}

	public CriteriaSet(Map<Criterion, CriterionElement> criteria) {
		this(null, criteria);
	}
	
	public CriteriaSet(Reglement reglement, Map<Criterion, CriterionElement> criteria) {
		if(reglement != null)
			this.reglement = reglement;
		if(criteria != null)
			this.criteria = criteria;
	}

	/**
	 * @return numCriteriaSet
	 */
	public int getNumCriteriaSet() {
		return numCriteriaSet;
	}

	/**
	 * @param numCriteriaSet numCriteriaSet à définir
	 */
	public void setNumCriteriaSet(int numCriteriaSet) {
		this.numCriteriaSet = numCriteriaSet;
	}

	/**
	 * @param reglement reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
		
		/*for(Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
			entry.getKey().setReglement(reglement);
			entry.getValue().setCriterion(entry.getKey());
		}*/
	}

	/**
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * Retourne l'element de critères correspondant au critères données en parametre
	 * 
	 * @param criterion le critère pour lequel retourné son élément
	 * @return l'element de critère
	 */
	public CriterionElement getCriterionElement(Criterion criterion) {
		if(!criteria.containsKey(criterion))
			return null;
		return criteria.get(criterion);
	}

	/**
	 * Définit un element pour un critère donné
	 * 
	 * @param element l'element à définir
	 */
	public void addCriterionElement(CriterionElement element) {
		if(element != null) {
			criteria.put(element.getCriterion(), element);
		}
	}

	/**
	 * Renvoi la liste des critères composant le jeux
	 * 
	 * @return la liste des critères composant le jeux
	 */
	public Map<Criterion, CriterionElement> getCriteria() {
		return criteria;
	}

	/**
	 * Définit la liste des critères composant le jeux
	 * 
	 * @param criteria  criteria à définir.
	 */
	public void setCriteria(Map<Criterion, CriterionElement> criteria) {
		this.criteria = criteria;
		
		for(Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
			entry.getKey().setReglement(reglement);
			entry.getValue().setCriterion(entry.getKey());
		}
	}

	/**
	 * Retourne le jeux de critère courant, filtré en fonction de la table de filtrage
	 * fournit en paramètre
	 * 
	 * @param criteriaFilter la table de filtrage des critères inclue dans le jeux retourné
	 * @return le jeux de critères filtré.
	 */
	public CriteriaSet getFilteredCriteriaSet(Map<Criterion, Boolean> criteriaFilter) {
		if(criteriaFilter == null)
			return this;
		
		CriteriaSet criteriaSet = new CriteriaSet();
		criteriaSet.setReglement(reglement);
		for(Criterion criterion : criteria.keySet()) {
			if(criteriaFilter.get(criterion))
				criteriaSet.addCriterionElement(criteria.get(criterion));
		}
		return criteriaSet;
	}
	
	@SuppressWarnings("nls")
	private String getUID() {
		//garantie l'ordre des éléments
		List<String> l = new ArrayList<String>();
		for(Entry<Criterion, CriterionElement> entry : criteria.entrySet())
			l.add(entry.getKey().getCode()+"="+entry.getValue().getCode());
		Collections.sort(l);
		String uid = "{";
		for(String e : l) {
			if(uid.length() > 1)
				uid += ",";
			uid += e;
		}
		uid += "}";
		int numReglement = 0;
		if(reglement != null)
			numReglement = reglement.getNumReglement();
		return "R=" + numReglement + ",S=" + uid;
	}
	
	/**
	 * Sauvegarde en base le jeux de critère. Les arguments sont ignoré
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 * 
	 */
	@Override
	public void save() throws ObjectPersistenceException {
		//vérifie si le jeux n'existe pas déjà
		String uid = getUID();
		String sql = "select NUMCRITERIASET from CRITERIASET where IDCRITERIASET='" + uid.replace("'","''") + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				ResultSet rs = stmt.executeQuery(sql);
				try {
					//si le jeux existe ne pas aller plus loin
					if(rs.first()) {
						if(numCriteriaSet == 0)
							numCriteriaSet = rs.getInt(1);
						return;
					}
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new ObjectPersistenceException(e);
		}

		helper.save(this, Collections.<String, Object>singletonMap("IDCRITERIASET", uid)); //$NON-NLS-1$
		
		try {
			sql = "delete from POSSEDE where NUMCRITERIASET=" + numCriteriaSet; //$NON-NLS-1$
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			stmt.executeUpdate(sql);
			
			sql =  "insert into POSSEDE (NUMCRITERIASET, CODECRITEREELEMENT, " //$NON-NLS-1$
				+ "CODECRITERE, NUMREGLEMENT) " //$NON-NLS-1$
				+ "values (?, ?, ?, ?)"; //$NON-NLS-1$
			PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
			try {
				for(Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
					Criterion criterion = entry.getKey();
					CriterionElement criterionElement = entry.getValue();
					
					pstmt.setInt(1, numCriteriaSet); 
					pstmt.setString(2, criterionElement.getCode());
					pstmt.setString(3, criterion.getCode());
					pstmt.setInt(4, reglement.getNumReglement());
	
					pstmt.executeUpdate();
				}
			} finally {
				pstmt.close();
			}
		} catch (SQLException e) {
			throw new ObjectPersistenceException(e);
		}
	}

	@Override
	public void delete() throws ObjectPersistenceException {
		helper.delete(this);
	}
	
	protected void beforeMarshal(Marshaller marshaller) {
		/*jaxbCriteria = new HashMap<String, String>();
		for(Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
			jaxbCriteria.put(entry.getKey().getCode(), entry.getValue().getCode());
		}*/
	}
	
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		/*if(parent instanceof Reglement) {
			Reglement reglement = (Reglement)parent;
			for(Entry<String, String> entry : jaxbCriteria.entrySet()) {
				Criterion criterion = reglement.getListCriteria().get(
						reglement.getListCriteria().indexOf(new Criterion(entry.getKey())));
				CriterionElement element = criterion.getCriterionElements().get(
						criterion.getCriterionElements().indexOf(new CriterionElement(entry.getValue())));
				
				criteria.put(criterion, element);
			}
		}*/
	}
	
	/**
	 * Tri les critères de distinction selon l'ordre de la table listCriteria
	 * 
	 * @param differentiationCriteriaTable
	 * @param listCriteria
	 */
	public static void sortCriteriaSet(List<CriteriaSet> differentiationCriteriaTable, List<Criterion> listCriteria) {

		//boucle sur a liste des critères disponible dans l'ordre d'affichage
		for(int i = 0; i < listCriteria.size(); i++) {
			//boucle sur la liste de critère de distinction retourné (comparaison d'élément
			for(int j = 0; j < differentiationCriteriaTable.size() - 1; j++) {

				for(int k = j + 1; k < differentiationCriteriaTable.size(); k++) {
					Criterion testedCriterion = listCriteria.get(i);
					//récupération des valeurs de critère
					CriterionElement result1 = differentiationCriteriaTable.get(j).getCriterionElement(testedCriterion);
					CriterionElement result2 = differentiationCriteriaTable.get(k).getCriterionElement(testedCriterion);

					boolean regle;

					int index1 = testedCriterion.getCriterionElements().indexOf(result1);
					int index2 = testedCriterion.getCriterionElements().indexOf(result2);

					//ordre croissant
					if(listCriteria.get(i).getSortOrder() > 0) {

						regle = index1 > index2;
						//ordre décroissants
					} else
						regle = index1 < index2;

					//pour les critères déjà passé en revue, vérifie qu'il y ai égalité
					for(int l = 0; l < i; l++) {
						Criterion otherCriterion = listCriteria.get(l);

						CriterionElement otherresult1 = differentiationCriteriaTable.get(j).getCriterionElement(otherCriterion);
						CriterionElement otherresult2 = differentiationCriteriaTable.get(k).getCriterionElement(otherCriterion);

						int otherindex1 = otherCriterion.getCriterionElements().indexOf(otherresult1);
						int otherindex2 = otherCriterion.getCriterionElements().indexOf(otherresult2);

						regle = regle && otherindex1 == otherindex2;
					}

					if(regle) {
						Collections.swap(differentiationCriteriaTable, j, k);
					}
				}
			}
		}
	}

	/**
	 * Retourne le liste des jeux de critères possible pour un règlement donné en fonction du filtre appliqué
	 * 
	 * @param reglement - le règlement servant de base à la génération du jeux de critère
	 * @param criteriaFilter - le filtre du jeux
	 * @return la liste des jeux de critères retourné
	 */
	public static CriteriaSet[] listCriteriaSet(Reglement reglement, Map<Criterion, Boolean> criteriaFilter) {       
		//crée la population complete pour l'ensemble des critères
		//objet de référence
		CriteriaSet[] referents = new CriteriaSet[] { new CriteriaSet(reglement) };

		//boucle sur les critères du règlement
		for(Criterion key : reglement.getListCriteria()) {
			CriteriaSet[][] children = new CriteriaSet[referents.length][];

			if(criteriaFilter.get(key)) {
				for(int i = 0; i < referents.length; i++) {
					List<CriteriaSet> childList = getChildrenPopulation(reglement, referents[i], key);
					children[i] = childList.toArray(new CriteriaSet[childList.size()]);
				}
				if(children.length > 0) {
					referents = new CriteriaSet[children[0].length*referents.length];
					int inc = 0;
					for(int i = 0; i < children.length; i++) {
						for(int j = 0; j < children[i].length; j++) {
							referents[inc++] = children[i][j];
						}
					}
				}
			} /*else {
				for(int i = 0; i < referents.length; i++) {
					referents[i].setCriterionElement(key, null);
				}
			}*/
		}

		return referents;
	}

	/**
	 * Retourne une liste de jeux de critères enfant par rapport au jeux de reference
	 * 
	 * @param reglement le règlement servant de base
	 * @param referent le jeux de critère de reference
	 * @param criterion le critère de reference
	 * @return les enfants
	 */
	private static List<CriteriaSet> getChildrenPopulation(Reglement reglement, CriteriaSet referent, Criterion criterion) {
		//crée la table des enfants
		List<CriteriaSet> children = new ArrayList<CriteriaSet>();

		for(int i = 0; i < criterion.getCriterionElements().size(); i++) {
			if(criterion.getCriterionElements().get(i).isActive()) {
				//initialise les critères
				CriteriaSet tempCrit = new CriteriaSet();
				tempCrit.setReglement(reglement);

				for(Criterion curCriterion : reglement.getListCriteria()) {
					if(referent.getCriterionElement(curCriterion) != null)
						tempCrit.addCriterionElement(referent.getCriterionElement(curCriterion));
				}
				tempCrit.addCriterionElement(criterion.getCriterionElements().get(i));
				children.add(tempCrit);
			}
		}

		return children;
	}

	/**
	 * test l'equivalence de deux jeux de critères
	 * 
	 * @param criteriaSet le jeux de critère avec lequel comparer
	 * @return true si equivalent, false sinon
	 */
	public boolean equals(CriteriaSet criteriaSet) {
		boolean isEquals = true;
		
		if(criteria.size() != criteriaSet.getCriteria().size())
			return false;

		if(!getUID().equals(criteriaSet.getUID()))
			isEquals = false;

		return isEquals;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;

		final CriteriaSet other = (CriteriaSet) obj;

		return equals(other);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getUID().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getUID();
	}
}
