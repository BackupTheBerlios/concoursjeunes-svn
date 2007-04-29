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

/**
 * Element de critère
 * 
 * @author Aurélien JEOFFRAY
 */
public class CriterionElement {
    private String code = ""; //$NON-NLS-1$
    private String libelle = ""; //$NON-NLS-1$
    private boolean active = true;
    
    public CriterionElement() {
        
    }

    /**
	 * Renvoie le code de l'élément
	 * @return  Renvoie code.
	 * @uml.property  name="code"
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Définit le code de l'élément
	 * @param code  code à définir.
	 * @uml.property  name="code"
	 */
    public void setCode(String code) {
        this.code = code;
    }

	/**
	 * Est-ce que l'élément est utilisé?
	 * @return  Renvoie isactive.
	 * @uml.property  name="active"
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 * Détermine si l'élément doit être utilisé
	 * @param active  - Etat de l'élément (actif ou non).
	 * @uml.property  name="active"
	 */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
	 * Renvoi le libellé de l'élément
	 * @return  Renvoie libelle.
	 * @uml.property  name="libelle"
	 */
    public String getLibelle() {
        return libelle;
    }

    /**
	 * Définit le libelle de l'élément
	 * @param libelle  libelle à définir.
	 * @uml.property  name="libelle"
	 */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * retourne le libelle de l'élément
     */
    @Override
    public String toString() {
        return libelle;
    }
    
    /**
     * Test si deux élements de critères sont équivalent
     * 
     * @param criterionElement - l'objet à comaré
     * @return boolean - le résulats de la comparaison de critères
     */
    public boolean equals(CriterionElement criterionElement) {
        return code.equals(criterionElement.getCode());
    }
    
    /**
     * Test si deux critères sont équivalent en se basant sur la comparaison d'objet
     * 
     * @param criterionElement - l'objet à comaré
     * @return boolean - le résulats de la comparaison de critères
     */
    @Override
    public boolean equals(Object criterionElement) {
        if(criterionElement instanceof CriterionElement)
            return equals((CriterionElement)criterionElement);
        return false;
    }
    
    /**
     * donne le hash de l'objet en se basant sur celui de son code
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
