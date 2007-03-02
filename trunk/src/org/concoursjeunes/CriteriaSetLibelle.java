/**
 * 
 */
package org.concoursjeunes;

import java.util.*;

/**
 * @author  xp
 */
public class CriteriaSetLibelle {
    private CriteriaSet differentiationCriteria;
    private String libelle = ""; //$NON-NLS-1$
    
    public CriteriaSetLibelle(CriteriaSet differentiationCriteria, String libelle) {
        this.differentiationCriteria = differentiationCriteria;
        this.libelle = libelle;
    }
    
    public CriteriaSetLibelle(CriteriaSet differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
        
        String strSCNA = ""; //$NON-NLS-1$
        
        Hashtable<Criterion, CriterionElement> criteria = differentiationCriteria.getCriteria();
        Enumeration<Criterion> keyCriteria = criteria.keys();
        while(keyCriteria.hasMoreElements()) {
        	Criterion keyCriterion = keyCriteria.nextElement();
            
            if(differentiationCriteria.isEnableCriterion(keyCriterion)) {
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
	 * @uml.property  name="differentiationCriteria"
	 */
    public CriteriaSet getDifferentiationCriteria() {
        return differentiationCriteria;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
