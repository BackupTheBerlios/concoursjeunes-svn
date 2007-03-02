/**
 * 
 */
package ajinteractive.concours;

import java.util.*;

/**
 * @author Aurélien Jeoffray
 *
 */
public class DifferentiationCriteria {
    //private static ArrayList<String> criteriaList = new ArrayList<String>();
    private Hashtable<String, Integer> criteria = new Hashtable<String, Integer>();
    //private static Hashtable<Integer, String> criteriaSortOrder = new Hashtable<Integer, String>();
    private Hashtable<String, Boolean> filtreCriteria = new Hashtable<String, Boolean>();
    
    public DifferentiationCriteria() {
        
    }
    
    public DifferentiationCriteria(Hashtable<String, Integer> criteria) {
        this.criteria = criteria;
        
        Enumeration<String> criterionEnum = criteria.keys();
        while(criterionEnum.hasMoreElements()) {
            String criterion = criterionEnum.nextElement();
            filtreCriteria.put(criterion, true);
        }
    }
    
    public static void sortDifferentiationCriteria(DifferentiationCriteria[] differentiationCriteriaTable, ArrayList<Criterion> listCriteria) {

        for(int i = 0; i < listCriteria.size(); i++) {
            for(int j = 0; j < differentiationCriteriaTable.length - 1; j++) {
                for(int k = j + 1; k < differentiationCriteriaTable.length; k++) {
                    int result1 = differentiationCriteriaTable[j].getCriterion(listCriteria.get(i).getCode());
                    int result2 = differentiationCriteriaTable[k].getCriterion(listCriteria.get(i).getCode());
                    
                    boolean regle;
                    
                    if(listCriteria.get(i).getSortOrder() > 0)
                        regle = result1 > result2;
                    else
                        regle = result1 < result2;
                    
                    for(int l = 0; l < i; l++) {
                        int otherresult1 = differentiationCriteriaTable[j].getCriterion(listCriteria.get(l).getCode());
                        int otherresult2 = differentiationCriteriaTable[k].getCriterion(listCriteria.get(l).getCode());
                        regle = regle && otherresult1 == otherresult2;
                    }
                    
                    if(regle) {
                        DifferentiationCriteria temp = differentiationCriteriaTable[j];
                        differentiationCriteriaTable[j] = differentiationCriteriaTable[k];
                        differentiationCriteriaTable[k] = temp;
                    }
                }
            }
        }
    }

    public static DifferentiationCriteria[] listeDifferentiationCriteria(Hashtable<String, Boolean> criteriaFilter) {       
        //cré la population complete pour l'ensemble des critéres
        //objet de référence
        DifferentiationCriteria[] referents = new DifferentiationCriteria[] { new DifferentiationCriteria() };
        
        for(Criterion key : ConcoursJeunes.configuration.getListCriteria()) {
            DifferentiationCriteria[][] children = new DifferentiationCriteria[referents.length][];
            
            if(criteriaFilter.get(key.getCode())) {
                for(int i = 0; i < referents.length; i++) {
                    ArrayList<DifferentiationCriteria> childList = getChildrenPopulation(referents[i], key.getCode(), ConcoursJeunes.configuration.getCriteriaPopulation().get(key));
                    children[i] = childList.toArray(new DifferentiationCriteria[childList.size()]);
                }
                referents = new DifferentiationCriteria[children[0].length*referents.length];
                int inc = 0;
                for(int i = 0; i < children.length; i++) {
                    for(int j = 0; j < children[i].length; j++) {
                        referents[inc++] = children[i][j];
                    }
                }
            } else {
                for(int i = 0; i < referents.length; i++) {
                    referents[i].setCriterion(key.getCode(), 0, false);
                }
            }
        }

        return referents;
    }
    
    private static ArrayList<DifferentiationCriteria> getChildrenPopulation(DifferentiationCriteria referent, String criterion, ArrayList<CriterionElement> population) {
        //cré la table des enfants
        ArrayList<DifferentiationCriteria> children = new ArrayList<DifferentiationCriteria>();
        
        for(int i = 0; i < population.size(); i++) {
            if(population.get(i).isActive()) {
                //initialise les critères
                DifferentiationCriteria tempCrit = new DifferentiationCriteria();
                
                for(Criterion curCriterion : ConcoursJeunes.configuration.getListCriteria()) {
                    if(referent.getCriterion(curCriterion.getCode()) > -1)
                        tempCrit.setCriterion(curCriterion.getCode(), referent.getCriterion(curCriterion.getCode()), referent.isEnableCriterion(curCriterion.getCode()));
                }
                tempCrit.setCriterion(criterion, i);
                children.add(tempCrit);
            }
        }
        
        return children;
    }
    
    public int getCriterion(String criterion) {
        if(!criteria.containsKey(criterion))
            return -1;
        return criteria.get(criterion);
    }
    
    public void setCriterion(String criterion, int value) {
        setCriterion(criterion, value, true);
    }
    
    public void setCriterion(String criterion, int value, boolean enable) {
        criteria.put(criterion, value);
        filtreCriteria.put(criterion, enable);
    }
    
    public void setEnableCriterion(String criterion, boolean enable) {
        filtreCriteria.put(criterion, enable);
    }
    
    public boolean isEnableCriterion(String criterion) {
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
     * @return Renvoie criteria.
     */
    public Hashtable<String, Integer> getCriteria() {
        return criteria;
    }

    /**
     * @param criteria criteria à définir.
     */
    public void setCriteria(Hashtable<String, Integer> criteria) {
        this.criteria = criteria;
    }

    /**
     * @return Renvoie filtreCriteria.
     */
    public Hashtable<String, Boolean> getFiltreCriteria() {
        return filtreCriteria;
    }

    /**
     * @param filtreCriteria filtreCriteria à définir.
     */
    public void setFiltreCriteria(Hashtable<String, Boolean> filtreCriteria) {
        this.filtreCriteria = filtreCriteria;
    }

    public boolean equals(DifferentiationCriteria differentiationCriteria) {
        boolean isEquals = true;
        
        Enumeration<String> criterionEnum = criteria.keys();
        while(criterionEnum.hasMoreElements()) {
            String criterion = criterionEnum.nextElement();
            if(filtreCriteria.get(criterion) && criteria.get(criterion) != differentiationCriteria.getCriterion(criterion)) {
                isEquals = false;
            }
        }
        
        return isEquals;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DifferentiationCriteria))
            return false;
        
        DifferentiationCriteria differentiationCriteria = (DifferentiationCriteria)o;
        return equals(differentiationCriteria);
    }
    
    @Override
    public int hashCode() {
        
        ArrayList<String> keys = new ArrayList<String>();
        String preHash = "";
        
        //enumere l'ensemble des criteres
        Enumeration<String> criterionEnum = criteria.keys();
        for(int i = 0; criterionEnum.hasMoreElements(); i++) {
            String key = criterionEnum.nextElement();
            if(filtreCriteria.get(key))
                keys.add(key); 
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
            preHash += "(" + keys.get(i) + ")" + criteria.get(keys.get(i));
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
