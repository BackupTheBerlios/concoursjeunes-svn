/*
 * Créé le 13 mars 2010 à 11:08:19 pour ConcoursJeunes
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.Session;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SessionHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlForeignKey;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.commons.sql.SqlManager;
import org.ajdeveloppement.concours.managers.CivilityManager;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Entite;
import org.concoursjeunes.manager.EntiteManager;

/**
 * Represent a contact person. This class can be serialised with JAXB.
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CONTACT")
@SqlPrimaryKey(fields="ID_CONTACT")
public class Contact implements ObjectPersistence, Cloneable {
	
	// [start] Helper persistence
	private static StoreHelper<Contact> helper = null;
	static {
		try {
			helper = new StoreHelper<Contact>(new SqlStoreHandler<Contact>(
					ApplicationCore.dbConnection, Contact.class));
		} catch (SQLException e) {
			throw new UncheckedException(e);
		}
	}
	// [end]
	
	//utilisé pour donnée un identifiant unique à la sérialisation de l'objet
	@XmlID
	@XmlAttribute(name="id")
	private String xmlId;
	
	@SqlField(name="ID_CONTACT")
	@XmlTransient
	private UUID idContact;
	
	@SqlField(name="NAME")
	private String name;
	
	@SqlField(name="FIRSTNAME")
	private String firstName;
	
	@SqlForeignKey(mappedTo="ID_CIVILITY")
	private Civility civility;
	
	@SqlField(name="ADDRESS")
	private String adress;
	
	@SqlField(name="ZIP_CODE")
	private String zipCode;
	
	@SqlField(name="CITY")
	private String city;
	
	@SqlField(name="NOTE")
	private String note;
	
	@XmlIDREF
	@SqlForeignKey(mappedTo="ID_ENTITE")
	private Entite entite = new Entite();
	
	private List<Coordinate> coordinates;
	
	private List<CategoryContact> categories;

	protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**
	 * Create a new contact
	 */
	public Contact() { }

	/**
	 * Create a new contact
	 * 
	 * @param name the name of contact
	 * @param firstName the first name of contact
	 * @param civility the civility (Mr, Ms, ...)
	 */
	public Contact(String name, String firstName, Civility civility) {
		this.name = name;
		this.firstName = firstName;
		this.civility = civility;
	}
	
	/**
	 * add a property listener to bean
	 * 
	 * @param l the bean's listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	/**
	 * remove a property listener from bean
	 * 
	 * @param l the bean's listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	/**
	 * Get the id of contact
	 * 
	 * @return idContact
	 */
	public UUID getIdContact() {
		if(idContact == null)
			idContact = UUID.randomUUID();
		return idContact;
	}

	/**
	 * Set the id of contact
	 * 
	 * @param idContact idContact à définir
	 */
	public void setIdContact(UUID idContact) {
		this.idContact = idContact;
	}

	/**
	 * Get the name of contact
	 * 
	 * @return name of contact
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of contact
	 * 
	 * @param name name of contact
	 */
	public void setName(String name) {
		String oldValue = this.name;
		
		this.name = name;
		
		pcs.firePropertyChange("name", oldValue, name); //$NON-NLS-1$
	}

	/**
	 * Get the first Name of contact 
	 * 
	 * @return the first Name of contact
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the fisrt name of contact
	 * 
	 * @param firstName the fisrt name of contact
	 */
	public void setFirstName(String firstName) {
		String oldValue = this.firstName;
		
		this.firstName = firstName;
		
		pcs.firePropertyChange("firstName", oldValue, firstName); //$NON-NLS-1$
	}

	/**
	 * Get the civility of contact
	 * 
	 * @return the civility of contact
	 */
	public Civility getCivility() {
		return civility;
	}

	/**
	 * Set the civility of contact
	 * 
	 * @param civility the civility of contact
	 */
	public void setCivility(Civility civility) {
		Civility oldValue = this.civility;
		
		this.civility = civility;
		
		pcs.firePropertyChange("civility", oldValue, civility); //$NON-NLS-1$
	}

	/**
	 * Get the post address of contact
	 * 
	 * @return the post address of contact
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * Set the post address of contact
	 * 
	 * @param adress the post address of contact
	 */
	public void setAdress(String adress) {
		Object oldValue = this.adress;
		
		this.adress = adress;
		
		pcs.firePropertyChange("adress", oldValue, adress); //$NON-NLS-1$
	}

	/**
	 * Get the post zip code of contact
	 * 
	 * @return the post zip code of contact
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Set the post zip code of contact
	 * 
	 * @param zipCode the post zip code of contact
	 */
	public void setZipCode(String zipCode) {
		Object oldValue = this.zipCode;
		
		this.zipCode = zipCode;
		
		pcs.firePropertyChange("zipCode", oldValue, zipCode); //$NON-NLS-1$
	}

	/**
	 * Get the residence city of contact
	 * 
	 * @return the residence city of contact
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Set the residence city of contact
	 * 
	 * @param city the city of contact
	 */
	public void setCity(String city) {
		Object oldValue = this.city;
		
		this.city = city;
		
		pcs.firePropertyChange("city", oldValue, city); //$NON-NLS-1$
	}

	/**
	 * Get free note about contact
	 * 
	 * @return free note about contact
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Set free note about contact
	 * 
	 * @param note free note about contact
	 */
	public void setNote(String note) {
		Object oldValue = this.note;
		
		this.note = note;
		
		pcs.firePropertyChange("note", oldValue, note); //$NON-NLS-1$
	}

	/**
	 * Get coordinates of contact (phone, fax, mail)
	 * 
	 * @return coordinates of contact
	 */
	public List<Coordinate> getCoordinates() {
		if(coordinates == null)
			coordinates = new ArrayList<Coordinate>();
		return coordinates;
	}

	/**
	 * Set coordinates of contact (phone, fax, mail)
	 * 
	 * @param coordinates coordinates of contact (phone, fax, mail)
	 */
	public void setCoordinates(List<Coordinate> coordinates) {
		Object oldValue = this.coordinates;
		
		this.coordinates = coordinates;
		
		for(Coordinate coordinate : coordinates)
			coordinate.setContact(this);
		
		pcs.firePropertyChange("coordinates", oldValue, coordinates); //$NON-NLS-1$
	}

	/**
	 * Get categories of contact
	 * 
	 * @return categories the categories of contact
	 */
	public List<CategoryContact> getCategories() {
		if(categories == null)
			categories = new ArrayList<CategoryContact>();
		return categories;
	}

	/**
	 * Set categories of contact
	 * 
	 * @param categories the categories of contact
	 */
	public void setCategories(List<CategoryContact> categories) {
		Object oldValue = categories;
		
		this.categories = categories;
		
		pcs.firePropertyChange("categories", oldValue, categories); //$NON-NLS-1$
	}

	/**
	 * Get identity of contact (firstName + name)
	 * @deprecated replaced by {@link #getFullName()}
	 * 
	 * @return the identity of contact
	 */
	@Deprecated
	public String getID() {
		return getFullName(); 
	}
	
	/**
	 * Get identity of contact (firstName + name)
	 * 
	 * @return the identity of contact
	 */
	public String getFullName() {
		return name + " " + firstName; //$NON-NLS-1$
	}
	
	/**
	 * Get identity of contact with civility(civility + firstName + name)
	 * 
	 * @return the identity of contact
	 */
	public String getFullNameWithCivility() {
		return ((civility != null && civility.getAbreviation() != null) ? civility.getAbreviation() + " " : "")  + name + " " + firstName; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * Set entity associate with contact
	 * 
	 * @param entite entity associate with contact
	 */
	public void setEntite(Entite entite) {
		Entite oldValue = this.entite;
		
		this.entite = entite;
		
		pcs.firePropertyChange("entite", oldValue, entite); //$NON-NLS-1$
	}

	/**
	 * Get entity associate with contact
	 * @return entity associate with contact
	 */
	public Entite getEntite() {
		return entite;
	}

	@Override
	public void save() throws ObjectPersistenceException {
		SessionHelper.startSaveSession(ApplicationCore.dbConnection, this);
	}
	
	@Override
	public void delete() throws ObjectPersistenceException {
		SessionHelper.startDeleteSession(ApplicationCore.dbConnection, this);
	}
	
	/**
	 * Save contact in database
	 */
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			if(idContact == null)
				idContact = UUID.randomUUID();
			
			if(entite != null) {
				if(!entite.getNom().isEmpty()) {
					entite.save(session);
					
					if(!session.contains(entite)) {
						//si l'instance n'a pas été sauvegardé c'est qu'il existe une instance concurrente en base
						//on va donc la récupérer et l'utiliser
						List<Entite> entitesInDatabase = EntiteManager.getEntitesInDatabase(entite, ""); //$NON-NLS-1$
						if(entitesInDatabase != null && entitesInDatabase.size() > 0)
							entite = entitesInDatabase.get(0);
						else //ne devrais pas ce produire compte tenu des tests précédents mais dans le doute pour éviter les plantages on stop la sauvegarde
							return;
					}
				}
			}
			
			SqlManager sqlManager = new SqlManager(ApplicationCore.dbConnection, null);
			try {
				Entite savedEntite = entite;
				if(entite != null && entite.getNom().isEmpty())
					entite = null;
				
				helper.save(this);
				
				entite = savedEntite;
			
				if(categories != null) {
				
					String savedIdCategories = ""; //$NON-NLS-1$
					
					for(CategoryContact categoryContact : categories) {
						sqlManager.executeUpdate("merge into ASSOCIER_CATEGORIE_CONTACT (ID_CONTACT, NUM_CATEGORIE_CONTACT) " //$NON-NLS-1$
								+ "VALUES ('" + idContact.toString() + "', " + categoryContact.getNumCategoryContact() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						
						savedIdCategories += categoryContact.getNumCategoryContact() + ","; //$NON-NLS-1$
					}
					String categoriesFilter = ""; //$NON-NLS-1$
					if(!savedIdCategories.isEmpty()) {
						savedIdCategories = savedIdCategories.substring(0, savedIdCategories.length() - 1);
						categoriesFilter = String.format("and NUM_CATEGORIE_CONTACT not in (%s)", savedIdCategories); //$NON-NLS-1$
					}
					sqlManager.executeUpdate(String.format("delete from ASSOCIER_CATEGORIE_CONTACT where ID_CONTACT = '%s' " //$NON-NLS-1$
							+ categoriesFilter, idContact.toString()));
				
				} else {
					sqlManager.executeUpdate(String.format("delete from ASSOCIER_CATEGORIE_CONTACT where ID_CONTACT = '%s'", idContact.toString())); //$NON-NLS-1$
				}
			
				if(coordinates != null && coordinates.size() > 0) {
					String savedIdCoordinates = ""; //$NON-NLS-1$
					
					for(Coordinate coordinate : coordinates) {
						coordinate.save(session);
						
						savedIdCoordinates += String.format("'%s',", coordinate.getIdCoordinate().toString()); //$NON-NLS-1$
					}
					
					if(!savedIdCoordinates.isEmpty()) {
						savedIdCoordinates = savedIdCoordinates.substring(0, savedIdCoordinates.length() - 1);
						
						sqlManager.executeUpdate(String.format("delete from COORDINATE where ID_CONTACT = '%s' and ID_COORDINATE not in (%s)", //$NON-NLS-1$
								idContact.toString(),
								savedIdCoordinates));
					}
				} else {
					sqlManager.executeUpdate(String.format("delete from COORDINATE where ID_CONTACT = '%s'", idContact.toString())); //$NON-NLS-1$
				}
			} catch (SQLException e) {
				throw new ObjectPersistenceException(e);
			}
			
			if(session != null)
				session.addThreatyObject(this);
		}
	}

	/**
	 * remove the contact database 
	 */
	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(idContact != null && (session == null || !session.contains(this))) {
			helper.delete(this);
			
			if(session != null)
				session.addThreatyObject(this);
		}
	}
	
	/**
	 * For JAXB Usage only. Do not use.
	 * 
	 * @param marshaller
	 */
	protected void beforeMarshal(Marshaller marshaller) {
		if(idContact == null)
			idContact = UUID.randomUUID();
		xmlId = idContact.toString();
		
		entite.beforeMarshal(marshaller);
	}
	
	/**
	 * For JAXB Usage only. Do not use.
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(xmlId != null)
			idContact = UUID.fromString(xmlId);
		
		xmlId = null;
		
		//met à null les instances vide de civilité
		if(civility != null) {
			if(civility.getAbreviation() == null || civility.getAbreviation().isEmpty())
				civility = null;
			else {
				try {
					List<Civility> civilities = CivilityManager.getAllCivilities();
					
					for(Civility c : civilities) {
						if(c.getIdCivility().equals(civility.getIdCivility()) || (c.getAbreviation().equals(civility.getAbreviation()) && c.getLibelle().equals(civility.getLibelle()))) {
							civility = c;
							break;
						}
					}
				} catch (ObjectPersistenceException e) {
					civility = null;
					e.printStackTrace();
				}
			}
		}
		
		entite.afterUnmarshal(unmarshaller, this);
	}
	
	@Override
	public String toString() {
		return getFullNameWithCivility();
	}
	
	/**
	 * clone the contact and all non-immutable properties
	 * 
	 * @return the cloned contact
	 * @throws CloneNotSupportedException
	 */
	@Override
	protected Contact clone() throws CloneNotSupportedException {
		return clone(true);
	}
	
	/**
	 * clone the contact and all non-immutable properties.
	 * 
	 * @param conserveId if true, the id is conserved else id is reset to null
	 * @return the cloned contact
	 * @throws CloneNotSupportedException
	 */
	protected Contact clone(boolean conserveId) throws CloneNotSupportedException {
		Contact clone = (Contact)super.clone();
		if(!conserveId)
			clone.setIdContact(null);
		clone.pcs = new PropertyChangeSupport(clone);
		
		List<Coordinate> clonedCoordinates = new ArrayList<Coordinate>();
		if(coordinates != null) {
			for(Coordinate coordinate : clone.getCoordinates()) {
				Coordinate clonedCoordinate = coordinate.clone();
				clonedCoordinate.setContact(clone);
				clonedCoordinates.add(clonedCoordinate);
			}
			clone.setCoordinates(clonedCoordinates);
		}
		
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((idContact == null) ? 0 : idContact.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (idContact == null) {
			if (other.idContact != null)
				return false;
		} else if (!idContact.equals(other.idContact))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}