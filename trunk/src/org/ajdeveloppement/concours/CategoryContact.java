/*
 * Créé le 7 mai 2010 à 18:24:09 pour ConcoursJeunes
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.ajdeveloppement.concours;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlGeneratedIdField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.concours.cache.CategoryContactCache;
import org.ajdeveloppement.concours.helpers.LibelleHelper;
import org.concoursjeunes.ApplicationCore;

/**
 * Category of contact. Use to filter contact by category
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CATEGORIE_CONTACT")
@SqlPrimaryKey(fields="NUM_CATEGORIE_CONTACT",generatedidField=@SqlGeneratedIdField(name="NUM_CATEGORIE_CONTACT",type=Types.INTEGER))
public class CategoryContact implements ObjectPersistence{
	private static StoreHelper<CategoryContact> helper = null;
	static {
		try {
			helper = new StoreHelper<CategoryContact>(new SqlStoreHandler<CategoryContact>(
					ApplicationCore.dbConnection, CategoryContact.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SqlField(name="NUM_CATEGORIE_CONTACT")
	private int numCategoryContact = 0;
	@SqlField(name="ID_LIBELLE")
	private UUID idLibelle;
	
	private transient Map<String, String> localizedLibelle = new HashMap<String, String>();
	
	/**
	 * Create a new catehory of contact
	 */
	public CategoryContact() {
	}
	
	/**
	 * Get database id of category contact or 0 if contact is not in
	 * database
	 * 
	 * @return database id of category contact
	 */
	public int getNumCategoryContact() {
		return numCategoryContact;
	}

	/**
	 * Set database id of category contact
	 * 
	 * @param numCategoryContact the database id of category contact
	 */
	public void setNumCategoryContact(int numCategoryContact) {
		this.numCategoryContact = numCategoryContact;
	}
	
	/**
	 * Get the localized label of category contact
	 * 
	 * @param lang the locale of label to return
	 * @return the localized label
	 */
	public String getLibelle(String lang) {
		
		if(localizedLibelle.containsKey(lang))
			return localizedLibelle.get(lang);
		
		String libelle = LibelleHelper.getLibelle(idLibelle, lang);
		localizedLibelle.put(lang, libelle);
	
		return libelle;
	}
	
	/**
	 * Set the localized label for a specific lang
	 * 
	 * @param libelle the localized label
	 * @param lang the language of label
	 */
	public void setLibelle(String libelle, String lang) {
		localizedLibelle.put(lang, libelle);
	}

	/**
	 * Save Category in database
	 */
	@Override
	public void save() throws ObjectPersistenceException {
		for(Entry<String,String> entry : localizedLibelle.entrySet()) {
			if(!LibelleHelper.getLibelle(idLibelle, entry.getKey()).equals(entry.getValue()))
				new Libelle(idLibelle, entry.getValue(), entry.getKey()).save();
		}
		
		helper.save(this);
		
		if(!CategoryContactCache.getInstance().containsKey(numCategoryContact))
			CategoryContactCache.getInstance().add(this);
	}
	
	/**
	 * Delete Category in database
	 */
	@Override
	public void delete() throws ObjectPersistenceException {
		helper.delete(this);
		
		CategoryContactCache.getInstance().remove(numCategoryContact);
	}
}
