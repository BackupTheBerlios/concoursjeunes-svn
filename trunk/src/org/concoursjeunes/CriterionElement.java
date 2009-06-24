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
package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.sql.SqlField;
import org.ajdeveloppement.commons.sql.SqlForeignFields;
import org.ajdeveloppement.commons.sql.SqlPersistance;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.sql.SqlStoreHelper;
import org.ajdeveloppement.commons.sql.SqlTable;
import org.concoursjeunes.builders.CriterionElementBuilder;

/**
 * Element de critère
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CRITEREELEMENT")
@SqlPrimaryKey(fields={"CODECRITEREELEMENT","CODECRITERE","NUMREGLEMENT"})
@SqlForeignFields(fields={"CODECRITERE","NUMREGLEMENT"})
public class CriterionElement implements SqlPersistance {
	@XmlID
	@XmlAttribute
	@SqlField(name="CODECRITEREELEMENT")
    private String code = ""; //$NON-NLS-1$
	@SqlField(name="LIBELLECRITEREELEMENT")
    private String libelle = ""; //$NON-NLS-1$
	@SqlField(name="ACTIF")
    private boolean active = true;
	@SqlField(name="NUMORDRE")
    private int numordre = 0;
	
	@XmlTransient
	private Criterion criterion;
	
	private static SqlStoreHelper<CriterionElement> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<CriterionElement>(ApplicationCore.dbConnection, CriterionElement.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public CriterionElement() {
        
    }
    
    public CriterionElement(String code) {
        this.code = code;
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
	 * @return criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * @param criterion criterion à définir
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
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
     * Retourne le numero d'ordre d'affichage de l'élément
     * 
     * @return le numero d'ordre d'affichage de l'élément
     */
    public int getNumordre() {
    	return numordre;
    }

    /**
     * Définit le numero d'ordre d'affichage de l'élément
     * 
     * @param numordre le numero d'ordre d'affichage de l'élément
     */
	public void setNumordre(int numordre) {
    	this.numordre = numordre;
    }
	
	/**
	 * Sauvegarde l'élement de critére dans la base.  Les arguments sont ignoré
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 */
	@SuppressWarnings("nls")
	@Override
	public void save() throws SqlPersistanceException {
		Map<String, Object> fk = new HashMap<String, Object>();
		fk.put("NUMREGLEMENT", criterion.getReglement().getNumReglement());
		fk.put("CODECRITERE", criterion.getCode());
		helper.save(this, fk);
	}
	
	/**
	 * Supprime de la base le présent élément. Les arguments sont ignoré
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#delete()
	 */
	@SuppressWarnings("nls")
	@Override
	public void delete() throws SqlPersistanceException {
		Map<String, Object> fk = new HashMap<String, Object>();
		fk.put("NUMREGLEMENT", criterion.getReglement().getNumReglement());
		fk.put("CODECRITERE", criterion.getCode());
		helper.delete(this, fk);
	}

	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Criterion)
			criterion = (Criterion)parent;
	}

	/**
     * retourne le libelle de l'élément
     */
    @Override
    public String toString() {
        return code;
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
    
    /**
     * Retourne l'ensemble des éléments de critére associé à un critére donné
     */
    public static List<CriterionElement> getAllCriterionElementsFor(Criterion criterion) {
    	List<CriterionElement> elements = new ArrayList<CriterionElement>();
    	
    	try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			
			String sql = "select CODECRITEREELEMENT from critereelement where " + //$NON-NLS-1$
					"codecritere='" + criterion.getCode() + "' " + //$NON-NLS-1$ //$NON-NLS-2$
					"and numreglement=" + criterion.getReglement().getNumReglement() + " order by NUMORDRE"; //$NON-NLS-1$ //$NON-NLS-2$
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				elements.add(CriterionElementBuilder.getCriterionElement(rs.getString("CODECRITEREELEMENT"), criterion)); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return elements;
    }
}
