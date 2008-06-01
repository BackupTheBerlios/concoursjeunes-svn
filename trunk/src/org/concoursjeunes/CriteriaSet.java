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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Jeux de critères utilisé pour distinguer un archer a des fins
 * de classements/placements et/ou informations
 * 
 * @author  Aurélien Jeoffray
 */
public class CriteriaSet {

	private Hashtable<Criterion, CriterionElement> criteria = new Hashtable<Criterion, CriterionElement>();

	public CriteriaSet() {
	}

	public CriteriaSet(Hashtable<Criterion, CriterionElement> criteria) {
		this.criteria = criteria;
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
	 * @param criterion le critère pour lequel d"finir l'élément
	 * @param element l'element à définir
	 */
	public void setCriterionElement(Criterion criterion, CriterionElement element) {
		if(element != null) {
			criteria.put(criterion, element);
		}
	}

	/**
	 * Renvoi la liste des critères composant le jeux
	 * 
	 * @return la liste des critères composant le jeux
	 */
	public Hashtable<Criterion, CriterionElement> getCriteria() {
		return criteria;
	}

	/**
	 * Définit la liste des critères composant le jeux
	 * 
	 * @param criteria  criteria à définir.
	 */
	public void setCriteria(Hashtable<Criterion, CriterionElement> criteria) {
		this.criteria = criteria;
	}

	/**
	 * Retourne le jeux de critère courrant, filtré en fonction de la table de filtrage
	 * fournit en parametre
	 * 
	 * @param criteriaFilter la table de filtrage des critères inclue dans le jeux retourné
	 * @return le jeux de critères filtré.
	 */
	public CriteriaSet getFilteredCriteriaSet(Hashtable<Criterion, Boolean> criteriaFilter) {
		CriteriaSet criteriaSet = new CriteriaSet();
		for(Criterion criterion : criteria.keySet()) {
			if(criteriaFilter.get(criterion))
				criteriaSet.setCriterionElement(criterion, criteria.get(criterion));
			else
				criteriaSet.setCriterionElement(criterion, null);
		}
		return criteriaSet;
	}

	/**
	 * Sauvegarde en base le jeux de critère
	 */
	public void save(int numReglement) {
		String sql;
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			
			stmt.executeUpdate("merge into CRITERIASET VALUES (" + hashCode() + ")"); //$NON-NLS-1$ //$NON-NLS-2$

			
			for(Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
				Criterion criterion = entry.getKey();
				CriterionElement criterionElement = entry.getValue();

				sql =  "merge into POSSEDE (NUMCRITERIASET, CODECRITEREELEMENT, " + //$NON-NLS-1$
						"CODECRITERE, NUMREGLEMENT) KEY (NUMCRITERIASET, CODECRITERE, NUMREGLEMENT) VALUES (" + //$NON-NLS-1$
						hashCode() + ", '" +  //$NON-NLS-1$
						criterionElement.getCode() + "', '" + //$NON-NLS-1$ 
						criterion.getCode() + "', " +  //$NON-NLS-1$
						numReglement + ")"; //$NON-NLS-1$
				//System.out.println(sql);
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tri les criteres de distinction selon l'ordre de la table listCriteria
	 * 
	 * @param differentiationCriteriaTable
	 * @param listCriteria
	 */
	public static void sortCriteriaSet(CriteriaSet[] differentiationCriteriaTable, List<Criterion> listCriteria) {

		//boucle sur a liste des critères disponible dans l'ordre d'affichage
		for(int i = 0; i < listCriteria.size(); i++) {
			//boucle sur la liste de ritere de distiction retourné (comparaison d'élément
			for(int j = 0; j < differentiationCriteriaTable.length - 1; j++) {

				for(int k = j + 1; k < differentiationCriteriaTable.length; k++) {
					Criterion testedCriterion = listCriteria.get(i);
					//recuperation des valeurs de critère
					CriterionElement result1 = differentiationCriteriaTable[j].getCriterionElement(testedCriterion);
					CriterionElement result2 = differentiationCriteriaTable[k].getCriterionElement(testedCriterion);

					boolean regle;

					int index1 = testedCriterion.getCriterionElements().indexOf(result1);
					int index2 = testedCriterion.getCriterionElements().indexOf(result2);

					//ordre croissant
					if(listCriteria.get(i).getSortOrder() > 0) {

						regle = index1 > index2;
						//ordre décroissants
					} else
						regle = index1 < index2;

					//pour les critères déjà passé en revue, verifie qu'il y est egalite
					for(int l = 0; l < i; l++) {
						Criterion otherCriterion = listCriteria.get(l);

						CriterionElement otherresult1 = differentiationCriteriaTable[j].getCriterionElement(otherCriterion);
						CriterionElement otherresult2 = differentiationCriteriaTable[k].getCriterionElement(otherCriterion);

						int otherindex1 = otherCriterion.getCriterionElements().indexOf(otherresult1);
						int otherindex2 = otherCriterion.getCriterionElements().indexOf(otherresult2);

						regle = regle && otherindex1 == otherindex2;
					}

					if(regle) {
						CriteriaSet temp = differentiationCriteriaTable[j];
						differentiationCriteriaTable[j] = differentiationCriteriaTable[k];
						differentiationCriteriaTable[k] = temp;
					}
				}
			}
		}
	}

	/**
	 * Retourne le liste des jeux de critères possible pour un réglement donné en fonction du filtre appliqué
	 * 
	 * @param reglement - le reglement servant de base à la génération du jeux de critère
	 * @param criteriaFilter - le filtre du jeux
	 * @return la liste des jeux de critères retourné
	 */
	public static CriteriaSet[] listCriteriaSet(Reglement reglement, Hashtable<Criterion, Boolean> criteriaFilter) {       
		//cré la population complete pour l'ensemble des critéres
		//objet de référence
		CriteriaSet[] referents = new CriteriaSet[] { new CriteriaSet() };

		//boucle sur les critères du reglement
		for(Criterion key : reglement.getListCriteria()) {
			CriteriaSet[][] children = new CriteriaSet[referents.length][];

			if(criteriaFilter.get(key)) {
				for(int i = 0; i < referents.length; i++) {
					List<CriteriaSet> childList = getChildrenPopulation(reglement, referents[i], key);
					children[i] = childList.toArray(new CriteriaSet[childList.size()]);
				}
				referents = new CriteriaSet[children[0].length*referents.length];
				int inc = 0;
				for(int i = 0; i < children.length; i++) {
					for(int j = 0; j < children[i].length; j++) {
						referents[inc++] = children[i][j];
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
	 * @param reglement - le reglement servant de basze
	 * @param referent - le jeux de critère de reference
	 * @param criterion - le critère de reference
	 * @return les enfants
	 */
	private static List<CriteriaSet> getChildrenPopulation(Reglement reglement, CriteriaSet referent, Criterion criterion) {
		//cré la table des enfants
		List<CriteriaSet> children = new ArrayList<CriteriaSet>();

		for(int i = 0; i < criterion.getCriterionElements().size(); i++) {
			if(criterion.getCriterionElements().get(i).isActive()) {
				//initialise les critères
				CriteriaSet tempCrit = new CriteriaSet();

				for(Criterion curCriterion : reglement.getListCriteria()) {
					if(referent.getCriterionElement(curCriterion) != null)
						tempCrit.setCriterionElement(curCriterion, referent.getCriterionElement(curCriterion));
				}
				tempCrit.setCriterionElement(criterion, criterion.getCriterionElements().get(i));
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

		for(Criterion criterion : criteria.keySet()) {
			if(getCriterionElement(criterion) != null 
					&& criteriaSet.getCriterionElement(criterion) != null 
					&& !getCriterionElement(criterion).equals(criteriaSet.getCriterionElement(criterion))) {
				isEquals = false;
			}
		}

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
		final int PRIME = 31;
		int result = 1;
		for(Map.Entry<Criterion, CriterionElement> entry : criteria.entrySet()) {
			result = PRIME * result + (entry.getKey().getCode() + ":" + entry.getValue().getCode()).hashCode(); //$NON-NLS-1$
		}
		return result;
	}
	
	@Override
	public String toString() {
		return new CriteriaSetLibelle(this).toString();
	}
}
