/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.Marshaller;
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
	
	//utilisé pour donnée un identifiant unique à la sérialisation de l'objet
	@XmlID
	@XmlAttribute(name="id")
	@SuppressWarnings("unused")
	private String xmlId;
	
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
	
	protected void beforeMarshal(Marshaller marshaller) {
		xmlId = UUID.randomUUID().toString();
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
