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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.sql.SqlField;
import org.ajdeveloppement.commons.sql.SqlForeignFields;
import org.ajdeveloppement.commons.sql.SqlPersistance;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.sql.SqlStoreHelper;
import org.ajdeveloppement.commons.sql.SqlTable;

/**
 * Caractéristique d'un critère de distinction
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CRITERE")
@SqlPrimaryKey(fields={"CODECRITERE","NUMREGLEMENT"})
@SqlForeignFields(fields="NUMREGLEMENT")
public class Criterion implements SqlPersistance {
	/**
	 * Tri des éléments du critères croissant
	 */
    public static final int SORT_ASC = 1;
    
    /**
     * Tri des éléments du critères décroissant
     */
    public static final int SORT_DESC = -1;
    
    public static final String[] CRITERES_TABLE_ARCHERS = {
    	"sexe", //$NON-NLS-1$
    	"categorie", //$NON-NLS-1$
    	"niveau", //$NON-NLS-1$
    	"arc" //$NON-NLS-1$
    };
    
    @XmlID
    @XmlAttribute
    @SqlField(name="CODECRITERE")
    private String code = ""; //$NON-NLS-1$
    @SqlField(name="LIBELLECRITERE")
    private String libelle = ""; //$NON-NLS-1$
    @SqlField(name="SORTORDERCRITERE")
    private int sortOrder = SORT_ASC;
    @SqlField(name="CLASSEMENT")
    private boolean classement = false;
    @SqlField(name="CLASSEMENTEQUIPE")
    private boolean classementEquipe = false;
    @SqlField(name="PLACEMENT")
    private boolean placement = false;
    @SqlField(name="CODEFFTA")
    private String champsTableArchers = ""; //$NON-NLS-1$
    @SqlField(name="NUMORDRE")
    private int numordre = 0;
    @XmlElementWrapper(name="criterionelements",required=true)
    @XmlElement(name="element")
    private List<CriterionElement> criterionElements = new ArrayList<CriterionElement>();
    
    @XmlTransient
    private Reglement reglement;
    
    private static SqlStoreHelper<Criterion> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<Criterion>(ApplicationCore.dbConnection, Criterion.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
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
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * @param reglement reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
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

    /**
     * Retourne le numero d'ordre d'affichage du critère
     * 
     * @return le numero d'ordre d'affichage du critère
     */
    public int getNumordre() {
    	return numordre;
    }

    /**
     * Définit le numero d'ordre d'affichage du critère
     * 
     * @param numordre le numero d'ordre d'affichage du critère
     */
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
        return code;
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
     * Remplacé par {@link #getChampsTableArchers()}
     * 
     * @return
     */
    @Deprecated
    public String getCodeffta() {
    	return getChampsTableArchers();
    }
    
    /**
     * Remplacé par {@link #setChampsTableArchers(String)}
     * 
     * @param champTableArchers
     */
    @SuppressWarnings("nls")
    @Deprecated
    public void setCodeffta(String champTableArchers) {
    	if(champTableArchers.equals("genre"))
    		champTableArchers = "sexe";
    	setChampsTableArchers(champTableArchers);
    }
    
    /**
     * Retourne, si associé, le champ de la table Archers correspondant au critère
     * 
	 * @return  Renvoie le champ de la table Archer du critère.
	 */
    public String getChampsTableArchers() {
        return champsTableArchers;
    }

    /**
     * Définit, si il existe une correspondance, le champ de la table Archer associé
     * 
	 * @param champTableArchers le champ de la table Archer du critère
	 */
    public void setChampsTableArchers(String champTableArchers) {
        this.champsTableArchers = champTableArchers;
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
		
		for(CriterionElement element : criterionElements)
			element.setCriterion(this);
	}

	/**
	 * Sauvegarde le critère en base.  Les arguments sont ignoré
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save(java.lang.Object[])
	 */
	@SuppressWarnings("nls")
	@Override
	public void save() throws SqlPersistanceException {
		helper.save(this, Collections.singletonMap("NUMREGLEMENT", (Object)reglement.getNumReglement())); //$NON-NLS-1$

		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				String codesElement = "";
				for (CriterionElement element : criterionElements) {
					if(!codesElement.isEmpty())
						codesElement += ",";
					codesElement += "'" + element.getCode().replace("'", "''") + "'";
				}
	
				stmt.executeUpdate("delete from CRITEREELEMENT where NUMREGLEMENT=" + reglement.getNumReglement() 
						+ " and CODECRITERE='" + code.replace("'", "''") +"' and CODECRITEREELEMENT not in (" + codesElement + ")");
			} finally {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new SqlPersistanceException(e);
		}
		
		int numordre = 1;
		for(CriterionElement criterionElement : criterionElements) {
			criterionElement.setNumordre(numordre++);
			criterionElement.save();
		}
	}
	
	/** 
	 * Supprime le critère de la base. Les arguments sont ignoré
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#delete(java.lang.Object[])
	 */
	@Override
	public void delete() throws SqlPersistanceException {
		helper.delete(this, Collections.singletonMap("NUMREGLEMENT", (Object)reglement.getNumReglement())); //$NON-NLS-1$
	}
	
	@SuppressWarnings("unused")
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Reglement)
			reglement = (Reglement)parent;
	}
}
