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

import java.util.Hashtable;

/**
 * @author Aurelien JEOFFRAY
 * @version 1.0
 */
public class CriteriaSetLibelle {
    
    private CriteriaSet criteriaSet;
    private String libelle = ""; //$NON-NLS-1$
    
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
     * Retourne le jeux de critere lié au libellé
     * 
	 * @return le jeux de critere associé au libelle
	 */
    public CriteriaSet getCriteriaSet() {
        return criteriaSet;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
