/**
 * 
 */
package ajinteractive.concours;

import java.util.*;

/**
 * @author xp
 *
 */
public class DifferentiationCriteriaLibelle {
    private DifferentiationCriteria differentiationCriteria;
    private String libelle = "";
    
    public DifferentiationCriteriaLibelle(DifferentiationCriteria differentiationCriteria, String libelle) {
        this.differentiationCriteria = differentiationCriteria;
        this.libelle = libelle;
    }
    
    public DifferentiationCriteriaLibelle(DifferentiationCriteria differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
        
        String strSCNA = "";
        
        Hashtable<String, Integer> criteria = differentiationCriteria.getCriteria();
        Enumeration<String> keyCriteria = criteria.keys();
        while(keyCriteria.hasMoreElements()) {
            String keyCriterion = keyCriteria.nextElement();
            
            if(differentiationCriteria.isEnableCriterion(keyCriterion)) {
                Criterion criterion = new Criterion();
                criterion.setCode(keyCriterion);
                strSCNA += ConcoursJeunes.configuration.getCriteriaPopulation()
                        .get(criterion).get(criteria.get(keyCriterion)) + " ";
            }
        }
        
        strSCNA = strSCNA.trim();
        
        //si la chaine est vide (filtre *false) afficher tous le monde
        if(strSCNA.equals("")) strSCNA = ConcoursJeunes.ajrLibelle.getResourceString("equipe.categorie.tous");
        
        this.libelle = strSCNA.trim();
    }
     
    
    public DifferentiationCriteria getDifferentiationCriteria() {
        return differentiationCriteria;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
