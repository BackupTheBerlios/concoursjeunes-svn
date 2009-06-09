/*
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
package org.concoursjeunes.localisable;

import java.util.Map;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;

/**
 * Permet la génération du libellé représentant un jeux de critères
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class CriteriaSetLibelle {
    
    private final CriteriaSet criteriaSet;
    private String libelle = ""; //$NON-NLS-1$
    
    /**
     * Construit le libellé associé au jeux de critères  fournit en parametre
     * 
     * @param criteriaSet le jeux de criteres pour lequelle construire le libellé
     */
    public CriteriaSetLibelle(CriteriaSet criteriaSet, AjResourcesReader localisation) {
        this.criteriaSet = criteriaSet;
        
        String strSCNA = ""; //$NON-NLS-1$
        
        if(criteriaSet != null) {
	        Map<Criterion, CriterionElement> criteria = criteriaSet.getCriteria();
	        for(Criterion keyCriterion : criteria.keySet()) {
	            
	            if(criteriaSet.getCriterionElement(keyCriterion) != null) {
	                strSCNA += criteria.get(keyCriterion).getLibelle() + " "; //$NON-NLS-1$
	            }
	        }
	        
	        strSCNA = strSCNA.trim();
	        
	        //si la chaine est vide (filtre *false) afficher tous le monde
	        if(strSCNA.equals("")) strSCNA = localisation.getResourceString("equipe.categorie.tous"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
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
    
    /**
     * Retourne le libellé construit à partir du jeux de critères
     */
    @Override
    public String toString() {
        return libelle;
    }
}
