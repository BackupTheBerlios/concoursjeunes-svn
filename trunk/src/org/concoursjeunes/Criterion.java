/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 *  
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
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlForeignKey;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.concours.cache.CriterionCache;

/**
 * Caractéristique d'un critère de distinction
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CRITERE")
@SqlPrimaryKey(fields={"CODECRITERE","NUMREGLEMENT"})
public class Criterion implements ObjectPersistence, Cloneable {
	/**
	 * Tri des éléments du critères croissant
	 */
    public static final int SORT_ASC = 1;
    
    /**
     * Tri des éléments du critères décroissant
     */
    public static final int SORT_DESC = -1;
    
    public static final String[] CRITERES_TABLE_ARCHERS = {
    	"SEXE", //$NON-NLS-1$
    	"CATEGORIE", //$NON-NLS-1$
    	"NIVEAU", //$NON-NLS-1$
    	"ARC" //$NON-NLS-1$
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
    @SqlForeignKey(mappedTo="NUMREGLEMENT")
    private Reglement reglement;
    
    private static StoreHelper<Criterion> helper = null;
	static {
		try {
			helper = new StoreHelper<Criterion>(new SqlStoreHandler<Criterion>(ApplicationCore.dbConnection, Criterion.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public Criterion() {
        
    }
    
    /**
     * Initialise un nouveau critère avec le code "code"
     * 
     * @param code le code du critère
     */
    public Criterion(String code) {
        this.code = code;
    }

    /**
	 * Renvoi le code du critère
	 * @return le code du critère
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Définit le code du critère
	 * 
	 * @param code le code du critère
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retourne le règlement associé au critère
     * 
	 * @return le règlement associé au critère
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * Associe un règlement au critère
	 * 
	 * @param reglement le règlement associé au critère
	 */
	public void setReglement(Reglement reglement) {
		if(this.reglement != null && this.reglement != reglement)
			this.reglement.removeCriterion(this);
		
		this.reglement = reglement;
	}
	
	/**
	 * Associe un règlement au critère
	 * 
	 * @deprecated Remplacé par {@link #setReglement(Reglement)}
	 * 
	 * @param reglement le règlement associé au critère
	 */
	@Deprecated
	public void setReglementParent(Reglement reglement) {
		setReglement(reglement);
	}

	/**
	 * Renvoie le libellé du critère
	 * 
	 * @return le libellé du critère
	 */
    public String getLibelle() {
        return libelle;
    }

    /**
	 * Définit le libellé du critère
	 * 
	 * @param libelle le libellé du critère
	 */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    /**
	 * Renvoie l'ordre de tri du critère
	 * 
	 * @return l'ordre de tri du critère
	 */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
	 * Définit l'ordre de tri du critère
	 * 
	 * @param sortOrder  Ordre de tri à appliquer pour le critère.
	 */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Retourne le numéro d'ordre d'affichage du critère
     * 
     * @return le numéro d'ordre d'affichage du critère
     */
    public int getNumordre() {
    	return numordre;
    }

    /**
     * Définit le numéro d'ordre d'affichage du critère
     * 
     * @param numordre le numéro d'ordre d'affichage du critère
     */
	public void setNumordre(int numordre) {
    	this.numordre = numordre;
    }

    /**
     * Test si deux critères sont équivalent
     * 
     * @param criterion
     * @return boolean - le résultats de la comparaison de critères
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
	 * 
	 * @return <code>true</code> si c'est un critère de classement, <code>false</code> sinon
	 */
    public boolean isClassement() {
        return classement;
    }

    /**
	 * Définit si c'est un critère de classement
	 * 
	 * @param classement <code>true</code> si c'est un critère de classement, <code>false</code> sinon
	 */
    public void setClassement(boolean classement) {
        this.classement = classement;
    }

    /**
     * Donne si le critère est utilisé ou non à des fins de classement
     * par équipe
     * 
     * @return <i>true</i> si utilisé pour le classement par équipe, <i>false</i> sinon
     */
    public boolean isClassementEquipe() {
		return classementEquipe;
	}

    /**
     * Définit si le critère est utilisé ou non à des fins de classement
     * 
     * @param classementEquipe <i>true</i> si utilisé pour le classement par équipe, <i>false</i> sinon
     */
	public void setClassementEquipe(boolean classementEquipe) {
		this.classementEquipe = classementEquipe;
	}

	/**
	 * Est ce que c'est un critère de placement?
	 * 
	 * @return <code>true</code> si c'est un critère de placement, <code>false</code> sinon
	 */
    public boolean isPlacement() {
        return placement;
    }

    /**
	 * Définit si c'est un critère de placement
	 * 
	 * @param placement <code>true</code> si c'est un critère de placement, <code>false</code> sinon
	 */
    public void setPlacement(boolean placement) {
        this.placement = placement;
    }

    /**
     * Remplacé par {@link #getChampsTableArchers()}
     * 
     * @return le champs de la table archer correspondant au critère
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
    		champTableArchers = "SEXE";
    	else if(champTableArchers.equals("arme"))
    		champTableArchers = "ARC";
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
    	if(champTableArchers.equals("arme"))
    		champTableArchers = "ARC";
        this.champsTableArchers = champTableArchers;
    }

	/**
	 * Retourne la liste des éléments lié au critère
	 * 
	 * @return la liste des éléments du critère
	 */
	public List<CriterionElement> getCriterionElements() {
		return criterionElements;
	}

	/**
	 * Définit la liste des éléments lié au critère
	 * 
	 * @param criterionElements la liste des éléments du critère
	 */
	public void setCriterionElements(List<CriterionElement> criterionElements) {
		this.criterionElements = criterionElements;
		
		for(CriterionElement element : criterionElements)
			element.setCriterion(this);
	}
	
	public void addCriterionElement(CriterionElement criterionElement) {
		criterionElements.add(criterionElement);
		
		criterionElement.setCriterion(this);
	}
	
	public void removeCriterionElement(CriterionElement criterionElement) {
		criterionElements.remove(criterionElement);
	}

	/**
	 * Sauvegarde le critère en base.
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 */
	@SuppressWarnings("nls")
	@Override
	public void save() throws ObjectPersistenceException {
		helper.save(this); //$NON-NLS-1$

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
			throw new ObjectPersistenceException(e);
		}
		
		int numordre = 1;
		for(CriterionElement criterionElement : criterionElements) {
			criterionElement.setNumordre(numordre++);
			criterionElement.save();
		}
		
		if(!CriterionCache.getInstance().containsKey(new CriterionCache.CriterionPK(code, reglement)))
			CriterionCache.getInstance().add(this);
	}
	
	/** 
	 * Supprime le critère de la base.
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#delete()
	 */
	@Override
	public void delete() throws ObjectPersistenceException {
		helper.delete(this);
		CriterionCache.getInstance().remove(new CriterionCache.CriterionPK(code, reglement));
	}
	
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Reglement)
			reglement = (Reglement)parent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
}
