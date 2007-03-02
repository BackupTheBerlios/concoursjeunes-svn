/**
 * 
 */
package org.concoursjeunes;

import java.util.*;

/**
 * @author  Aurélien Jeoffray
 */
public class CriteriaSet {

	private Hashtable<Criterion, CriterionElement> criteria = new Hashtable<Criterion, CriterionElement>();
    private Hashtable<Criterion, Boolean> filtreCriteria = new Hashtable<Criterion, Boolean>();
    
    public CriteriaSet() {
        
    }
    
    public CriteriaSet(Hashtable<Criterion, CriterionElement> criteria) {
        this.criteria = criteria;
        
        Enumeration<Criterion> criterionEnum = criteria.keys();
        while(criterionEnum.hasMoreElements()) {
        	Criterion criterion = criterionEnum.nextElement();
            filtreCriteria.put(criterion, true);
        }
    }
    
    /**
     * Tri les criteres de distinction selon l'ordre de la table listCriteria
     * 
     * @param differentiationCriteriaTable
     * @param listCriteria
     */
    public static void sortCriteriaSet(CriteriaSet[] differentiationCriteriaTable, ArrayList<Criterion> listCriteria) {

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

    public static CriteriaSet[] listeDifferentiationCriteria(Reglement reglement, Hashtable<Criterion, Boolean> criteriaFilter) {       
        //cré la population complete pour l'ensemble des critéres
        //objet de référence
        CriteriaSet[] referents = new CriteriaSet[] { new CriteriaSet() };
        
        for(Criterion key : reglement.getListCriteria()) {
            CriteriaSet[][] children = new CriteriaSet[referents.length][];
            
            if(criteriaFilter.get(key)) {
                for(int i = 0; i < referents.length; i++) {
                    ArrayList<CriteriaSet> childList = getChildrenPopulation(reglement, referents[i], key);
                    children[i] = childList.toArray(new CriteriaSet[childList.size()]);
                }
                referents = new CriteriaSet[children[0].length*referents.length];
                int inc = 0;
                for(int i = 0; i < children.length; i++) {
                    for(int j = 0; j < children[i].length; j++) {
                        referents[inc++] = children[i][j];
                    }
                }
            } else {
                for(int i = 0; i < referents.length; i++) {
                    referents[i].setCriterionElement(key, null, false);
                }
            }
        }

        return referents;
    }
    
    private static ArrayList<CriteriaSet> getChildrenPopulation(Reglement reglement, CriteriaSet referent, Criterion criterion) {
        //cré la table des enfants
        ArrayList<CriteriaSet> children = new ArrayList<CriteriaSet>();
        
        for(int i = 0; i < criterion.getCriterionElements().size(); i++) {
            if(criterion.getCriterionElements().get(i).isActive()) {
                //initialise les critères
                CriteriaSet tempCrit = new CriteriaSet();
                
                for(Criterion curCriterion : reglement.getListCriteria()) {
                    if(referent.getCriterionElement(curCriterion) != null)
                        tempCrit.setCriterionElement(curCriterion, referent.getCriterionElement(curCriterion), referent.isEnableCriterion(curCriterion));
                }
                tempCrit.setCriterionElement(criterion, criterion.getCriterionElements().get(i));
                children.add(tempCrit);
            }
        }
        
        return children;
    }
    
    public CriterionElement getCriterionElement(Criterion criterion) {
        if(!criteria.containsKey(criterion))
            return null;
        return criteria.get(criterion);
    }
    
    public void setCriterionElement(Criterion criterion, CriterionElement element) {
        setCriterionElement(criterion, element, true);
    }
    
    public void setCriterionElement(Criterion criterion, CriterionElement element, boolean enable) {
    	if(element != null) {
	        criteria.put(criterion, element);
	        filtreCriteria.put(criterion, enable);
    	}
    }
    
    public void setEnableCriterion(Criterion criterion, boolean enable) {
        filtreCriteria.put(criterion, enable);
    }
    
    public boolean isEnableCriterion(Criterion criterion) {
        return filtreCriteria.get(criterion);
    }

    /**
     * @return Renvoie criteriaSortOrder.
     */
    /*public static Hashtable<Integer, String> getCriteriaSortOrder() {
        return criteriaSortOrder;
    }*/

    /**
     * @param criteriaSortOrder criteriaSortOrder à définir.
     */
    /*public static void setCriteriaSortOrder(
            Hashtable<Integer, String> criteriaSortOrder) {
        DifferentiationCriteria.criteriaSortOrder = criteriaSortOrder;
    }*/

    /**
	 * @return  Renvoie criteriaSortOrder.
	 * @uml.property  name="criteria"
	 */
    public Hashtable<Criterion, CriterionElement> getCriteria() {
        return criteria;
    }

    /**
	 * @param criteria  criteria à définir.
	 * @uml.property  name="criteria"
	 */
    public void setCriteria(Hashtable<Criterion, CriterionElement> criteria) {
        this.criteria = criteria;
    }

    /**
	 * @return  Renvoie filtreCriteria.
	 * @uml.property  name="filtreCriteria"
	 */
    public Hashtable<Criterion, Boolean> getFiltreCriteria() {
        return filtreCriteria;
    }

    /**
	 * @param filtreCriteria  filtreCriteria à définir.
	 * @uml.property  name="filtreCriteria"
	 */
    public void setFiltreCriteria(Hashtable<Criterion, Boolean> filtreCriteria) {
        this.filtreCriteria = filtreCriteria;
    }

    public boolean equals(CriteriaSet differentiationCriteria) {
        boolean isEquals = true;
        
        Enumeration<Criterion> criterionEnum = criteria.keys();
        while(criterionEnum.hasMoreElements()) {
        	Criterion criterion = criterionEnum.nextElement();
            if(filtreCriteria.get(criterion) && !criteria.get(criterion).equals(differentiationCriteria.getCriterionElement(criterion))) {
                isEquals = false;
            }
        }
        
        return isEquals;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CriteriaSet))
            return false;
        
        CriteriaSet differentiationCriteria = (CriteriaSet)o;
        return equals(differentiationCriteria);
    }
    
    @Override
    public int hashCode() {
        
        ArrayList<String> keys = new ArrayList<String>();
        String preHash = ""; //$NON-NLS-1$
        
        //enumere l'ensemble des criteres
        Enumeration<Criterion> criterionEnum = criteria.keys();
        for(int i = 0; criterionEnum.hasMoreElements(); i++) {
        	Criterion key = criterionEnum.nextElement();
            if(filtreCriteria.get(key))
                keys.add(key.getCode()); 
        }
        
        //algorithme de tri classique pour que les critéres soit toujours 
        //dans le même ordre pour le hash
        for(int i = 0; i < keys.size() - 1; i++) {
            for(int j = i + 1; j < keys.size(); j++) {
                if(keys.get(i).compareTo(keys.get(j)) > 0) {
                    String tempKey = keys.get(j);
                    keys.set(j, keys.get(i));
                    keys.set(i, tempKey);
                }
            }
        }
        
        //compose la chaine
        for(int i = 0; i < keys.size(); i++) {
            preHash += "(" + keys.get(i) + ")" + criteria.get(keys.get(i)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return preHash.hashCode();
    }

    /**
     * @return Renvoie criteriaList.
     */
    /*public static ArrayList<String> getCriteriaList() {
        return criteriaList;
    }*/

    /**
     * @param criteriaList criteriaList à définir.
     */
    /*public static void setCriteriaList(ArrayList<String> criteriaList) {
        DifferentiationCriteria.criteriaList = criteriaList;
    }*/
}
