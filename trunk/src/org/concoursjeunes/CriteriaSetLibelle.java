/**
 * 
 */
package org.concoursjeunes;

import java.util.*;

/**
 * @author Aurelien JEOFFRAY
 */
public class CriteriaSetLibelle {
    
    private CriteriaSet criteriaSet;
    private String libelle = ""; //$NON-NLS-1$
    
    public CriteriaSetLibelle(CriteriaSet differentiationCriteria, String libelle) {
        this.criteriaSet = differentiationCriteria;
        this.libelle = libelle;
    }
    
    public CriteriaSetLibelle(CriteriaSet criteriaSet) {
        this.criteriaSet = criteriaSet;
        
        String strSCNA = ""; //$NON-NLS-1$
        
        Hashtable<Criterion, CriterionElement> criteria = criteriaSet.getCriteria();
        for(Criterion keyCriterion : criteria.keySet()) {
            
            if(criteriaSet.getCriterionElement(keyCriterion) != null) {
                strSCNA += criteria.get(keyCriterion) + " "; //$NON-NLS-1$
            }
        }
        
        strSCNA = strSCNA.trim();
        
        //si la chaine est vide (filtre *false) afficher tous le monde
        if(strSCNA.equals("")) strSCNA = ConcoursJeunes.ajrLibelle.getResourceString("equipe.categorie.tous"); //$NON-NLS-1$ //$NON-NLS-2$
        
        this.libelle = strSCNA.trim();
    }
     
    
    /**
	 * @return
	 * @uml.property  name="criteriaSet"
	 */
    public CriteriaSet getCriteriaSet() {
        return criteriaSet;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
