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
import java.util.List;

/**
 * Caractéristique d'un critère de distinction
 * 
 * @author Aurélien JEOFFRAY
 */
public class Criterion {
	/**
	 * Tri des éléments du critères croissant
	 */
    public static final int SORT_ASC = 1;
    
    /**
     * Tri des éléments du critères décroissant
     */
    public static final int SORT_DESC = -1;
    
    private int sortOrder = 1;
    
    private String code = ""; //$NON-NLS-1$
    private String libelle = ""; //$NON-NLS-1$
    private boolean classement = false;
    private boolean classementEquipe = false;
    private boolean placement = false;
    private String codeffta = ""; //$NON-NLS-1$
    private int numordre = 0;
    
    private List<CriterionElement> criterionElements = new ArrayList<CriterionElement>();
    
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
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Définit le code du critère
	 * @param code  code à définir.
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * Renvoie le libellé du critère
	 * @return  Renvoie libelle.
	 */
    public String getLibelle() {
        return libelle;
    }

    /**
	 * Définit le libellé du critère
	 * @param libelle  libelle à définir.
	 */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    /**
	 * Renvoie l'ordre de tri du critère
	 * @return  Renvoie sortOrder.
	 */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
	 * Définit l'ordre de tri du critère
	 * @param sortOrder  - Ordre de tri à appliquer pour le critère.
	 */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getNumordre() {
    	return numordre;
    }

	public void setNumordre(int numordre) {
    	this.numordre = numordre;
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
	 */
    public boolean isClassement() {
        return classement;
    }

    /**
	 * Définit si c'est un critère de classement
	 * @param classement  classement à définir.
	 */
    public void setClassement(boolean classement) {
        this.classement = classement;
    }

    /**
     * Donne si le critère est utilisé ou non à des fins de classement
     * par équipe
     * 
     * @return <i>true</i> si utilisé pour le classement par equipe, <i>false</i> sinon
     */
    public boolean isClassementEquipe() {
		return classementEquipe;
	}

    /**
     * Définit si le critère est utilisé ou non à des fins de classement
     * 
     * @param classementEquipe
     */
	public void setClassementEquipe(boolean classementEquipe) {
		this.classementEquipe = classementEquipe;
	}

	/**
	 * Est ce que c'est un critère de placement?
	 * @return  Renvoie placement.
	 */
    public boolean isPlacement() {
        return placement;
    }

    /**
	 * Definit si c'est un critère de placement
	 * @param placement  placement à définir.
	 */
    public void setPlacement(boolean placement) {
        this.placement = placement;
    }

    /**
     * Retourne, si associé, le code FFTA correspondant au critère
     * 
	 * @return  Renvoie le code FFTA du critère.
	 */
    public String getCodeffta() {
        return codeffta;
    }

    /**
     * Définit, si il existe une correspondance, le code FFTA associé
     * 
	 * @param codeffta le code FFTA du critère
	 */
    public void setCodeffta(String codeffta) {
        this.codeffta = codeffta;
    }

	/**
	 * Retourne la liste des éléments lié au critère
	 * 
	 * @return la liste des élements du critère
	 */
	public List<CriterionElement> getCriterionElements() {
		return criterionElements;
	}

	/**
	 * Définit la liste des éléments lié au critère
	 * 
	 * @param criterionElements la liste des élements du critère
	 */
	public void setCriterionElements(List<CriterionElement> criterionElements) {
		this.criterionElements = criterionElements;
	}
	
	/**
	 * Sauvegarde le critère en base
	 * 
	 * @throws SQLException si une erreur d'integration en base se produit
	 */
	public void save(int numReglement) throws SQLException {

		Statement stmt = ApplicationCore.dbConnection.createStatement();
		
		stmt.executeUpdate("merge into CRITERE (CODECRITERE,NUMREGLEMENT,LIBELLECRITERE,SORTORDERCRITERE," + //$NON-NLS-1$
				"CLASSEMENT,CLASSEMENTEQUIPE,PLACEMENT,CODEFFTA,NUMORDRE) VALUES ('" + code + "'," +  //$NON-NLS-1$ //$NON-NLS-2$
				numReglement + ",'" + libelle + "'," +  //$NON-NLS-1$ //$NON-NLS-2$
				sortOrder + "," + //$NON-NLS-1$
				Boolean.toString(classement).toUpperCase() + "," + //$NON-NLS-1$
				Boolean.toString(classementEquipe).toUpperCase() + "," + //$NON-NLS-1$
				Boolean.toString(placement).toUpperCase() + ",'" + //$NON-NLS-1$
				codeffta + "', " + numordre +")"); //$NON-NLS-1$ //$NON-NLS-2$
		int numordre = 1;
		for(CriterionElement criterionElement : criterionElements) {
			criterionElement.setNumordre(numordre++);
			criterionElement.save(numReglement, code);
		}
	}
	
	/**
	 * Supprime le critère de la base
	 */
	public void delete(int numReglement) {
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			
			stmt.executeUpdate("delete from CRITERE where CODECRITERE='" + code + "' and " + //$NON-NLS-1$ //$NON-NLS-2$
					"NUMREGLEMENT=" + numReglement); //$NON-NLS-1$
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
