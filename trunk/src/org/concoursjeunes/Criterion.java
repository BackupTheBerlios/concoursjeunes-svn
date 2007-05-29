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

import ajinteractive.standard.utilities.persistance.Entity;
import ajinteractive.standard.utilities.persistance.Field;
import ajinteractive.standard.utilities.persistance.Id;

/**
 * Caractéristique d'un critère de distinction
 * 
 * @author Aurélien JEOFFRAY
 */
@Entity("CRITERE")
public class Criterion {
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = -1;
    
    @Field("SORTORDERCRITERE") private int sortOrder = 1;
    
    @Id("CODECRITERE") private String code = ""; //$NON-NLS-1$
    @Field("LIBELLERITERE") private String libelle = ""; //$NON-NLS-1$
    @Field("CLASSEMENT") private boolean classement = false;
    @Field("PLACEMENT") private boolean placement = false;
    @Field("CODEFFTA") private String codeffta = ""; //$NON-NLS-1$
    
    private ArrayList<CriterionElement> criterionElements = new ArrayList<CriterionElement>();
    
    private Reglement reglementParent = new Reglement();
    
    public Criterion() {
        
    }
    
    /**
     * Initialise un nouveau critère avec le code "code"
     * 
     * @param code - le code du citère
     */
    public Criterion(String code) {
        this.code = code;
    }

    /**
	 * Renvoi le code du critère
	 * @return  Renvoie code.
	 * @uml.property  name="code"
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Définit le code du critère
	 * @param code  code à définir.
	 * @uml.property  name="code"
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * Renvoie le libellé du critère
	 * @return  Renvoie libelle.
	 * @uml.property  name="libelle"
	 */
    public String getLibelle() {
        return libelle;
    }

    /**
	 * Définit le libellé du critère
	 * @param libelle  libelle à définir.
	 * @uml.property  name="libelle"
	 */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    /**
	 * Renvoie l'ordre de tri du critère
	 * @return  Renvoie sortOrder.
	 * @uml.property  name="sortOrder"
	 */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
	 * Définit l'ordre de tri du critère
	 * @param sortOrder  - Ordre de tri à appliquer pour le critère.
	 * @uml.property  name="sortOrder"
	 */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Test si deux critères sont équivalent
     * 
     * @param criterion
     * @return boolean - le résulats de la comparaison de critères
     */
    public boolean equals(Criterion criterion) {
        return code.equals(criterion.getCode());
    }
    
    /**
     * Test si deux critères sont équivalent en se basant sur la comparaison d'objet
     */
    @Override
    public boolean equals(Object criterion) {
        if(criterion instanceof Criterion)
            return equals((Criterion)criterion);
        return false;
    }
    
    /**
     * donne le hash de l'objet en se basant sur celui de son code
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    /**
     * renvoie le libelle du critère
     */
    @Override
    public String toString() {
        return libelle;
    }

    /**
	 * Est ce que c'est un critère de classement?
	 * @return  Renvoie classement.
	 * @uml.property  name="classement"
	 */
    public boolean isClassement() {
        return classement;
    }

    /**
	 * Définit si c'est un critère de classement
	 * @param classement  classement à définir.
	 * @uml.property  name="classement"
	 */
    public void setClassement(boolean classement) {
        this.classement = classement;
    }

    /**
	 * Est ce que c'est un critère de placement?
	 * @return  Renvoie placement.
	 * @uml.property  name="placement"
	 */
    public boolean isPlacement() {
        return placement;
    }

    /**
	 * Definit si c'est un critère de placement
	 * @param placement  placement à définir.
	 * @uml.property  name="placement"
	 */
    public void setPlacement(boolean placement) {
        this.placement = placement;
    }

    /**
	 * @return  Renvoie codeffta.
	 * @uml.property  name="codeffta"
	 */
    public String getCodeffta() {
        return codeffta;
    }

    /**
	 * @param codeffta  codeffta à définir.
	 * @uml.property  name="codeffta"
	 */
    public void setCodeffta(String codeffta) {
        this.codeffta = codeffta;
    }

	/**
	 * Retourne la liste des éléments lié au critère
	 * 
	 * @return la liste des élements du critère
	 * @uml.property  name="criterionElements"
	 */
	public ArrayList<CriterionElement> getCriterionElements() {
		return criterionElements;
	}

	/**
	 * @param criterionElements
	 * @uml.property  name="criterionElements"
	 */
	public void setCriterionElements(ArrayList<CriterionElement> criterionElements) {
		this.criterionElements = criterionElements;
	}

	public Reglement getReglementParent() {
		return reglementParent;
	}

	public void setReglementParent(Reglement reglementParent) {
		this.reglementParent = reglementParent;
	}
	
	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			stmt.executeUpdate("merge into CRITERE (CODECRITERE,NUMREGLEMENT,LIBELLECRITERE,SORTORDERCRITERE," +
					"CLASSEMENT,PLACEMENT,CODEFFTA) VALUES ('" + code + "'," + 
					reglementParent.hashCode() + ",'" + libelle + "'," + 
					sortOrder + "," +
					Boolean.toString(classement).toUpperCase() + "," +
					Boolean.toString(placement).toUpperCase() + ",'" +
					codeffta + "')");
			for(CriterionElement criterionElement : criterionElements) {
				criterionElement.save();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			stmt.executeUpdate("delete from CRITERE where CODECRITERE='" + code + "' and " +
					"NUMREGLEMENT=" + reglementParent.hashCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
