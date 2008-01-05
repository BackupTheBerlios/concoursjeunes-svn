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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Element de critère
 * 
 * @author Aurélien JEOFFRAY
 */
public class CriterionElement {
    private String code = ""; //$NON-NLS-1$
    private String libelle = ""; //$NON-NLS-1$
    private boolean active = true;
    
    private Criterion criterionParent = new Criterion();
    
    public CriterionElement() {
        
    }

    /**
	 * Renvoie le code de l'élément
	 * @return  Renvoie code.
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Définit le code de l'élément
	 * @param code  code à définir.
	 */
    public void setCode(String code) {
        this.code = code;
    }

	/**
	 * Est-ce que l'élément est utilisé?
	 * @return  Renvoie isactive.
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 * Détermine si l'élément doit être utilisé
	 * @param active  - Etat de l'élément (actif ou non).
	 */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
	 * Renvoi le libellé de l'élément
	 * @return  Renvoie libelle.
	 */
    public String getLibelle() {
        return libelle;
    }

    /**
	 * Définit le libelle de l'élément
	 * @param libelle  libelle à définir.
	 */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Le critère parent de l'élément
     * 
     * @return Le critère parent
     */
    public Criterion getCriterionParent() {
		return criterionParent;
	}

    /**
     * Définit le critère parent de l'élément
     * 
     * @param criterionParent le critère parent
     */
	public void setCriterionParent(Criterion criterionParent) {
		this.criterionParent = criterionParent;
	}
	
	/**
	 * Sauvegarde l'élement de critére dans la base
	 */
	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			stmt.executeUpdate("merge into CRITEREELEMENT (CODECRITEREELEMENT," + //$NON-NLS-1$
					"CODECRITERE,NUMREGLEMENT,LIBELLECRITEREELEMENT,ACTIF) values (" + //$NON-NLS-1$
					"'" + code + "', '" + criterionParent.getCode() + "'," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"" + criterionParent.getReglementParent().hashCode() + ", '" + libelle + "'," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					Boolean.toString(active).toUpperCase() + ")"); //$NON-NLS-1$
		} catch(SQLException e) {
			
		}
	}
	
	/**
	 * Supprime de la base le présent élément
	 */
	public void delete() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			stmt.executeUpdate("delete from CRITEREELEMENT where CODECRITEREELEMENT='" + code + //$NON-NLS-1$
					"' and CODECRITERE='" + criterionParent.getCode() + "' and " + //$NON-NLS-1$ //$NON-NLS-2$
					"NUMREGLEMENT=" + criterionParent.getReglementParent().hashCode()); //$NON-NLS-1$
		} catch(SQLException e) {
			
		}
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
    
    /*public static ArrayList<CriterionElement> getAllCriterionElementsFor(Criterion criterion) {
    	return getAllCriterionElementsFor(criterion, criterion.getReglementParent().hashCode());
    }*/
    
    /**
     * Retourne l'ensemble des éléments de critére associé à un critére donné et un réglement donné
     */
    public static List<CriterionElement> getAllCriterionElementsFor(Criterion criterion, int hashReglement) {
    	List<CriterionElement> elements = new ArrayList<CriterionElement>();
    	
    	try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			String sql = "select CODECRITEREELEMENT from critereelement where " + //$NON-NLS-1$
					"codecritere='" + criterion.getCode() + "' " + //$NON-NLS-1$ //$NON-NLS-2$
					"and numreglement=" + hashReglement; //$NON-NLS-1$
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				elements.add(CriterionElementBuilder.getCriterionElement(rs.getString("CODECRITEREELEMENT"), criterion, hashReglement)); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return elements;
    }
}
