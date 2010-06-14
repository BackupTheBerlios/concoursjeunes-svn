/*
 * Créé le 13 mars 2010 à 11:42:38 pour ConcoursJeunes
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
import java.util.UUID;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

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
import org.concoursjeunes.ApplicationCore;

/**
 * Represent a contact coordinate.<br>
 * A coordinate can be an phone number or mail address
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="COORDINATE")
@SqlPrimaryKey(fields="ID_COORDINATE")
public class Coordinate implements ObjectPersistence, Cloneable {
	
	/**
	 * Type off coordinate (different type of phone number or mail address)
	 */
	public enum Type {
		/**
		 * Personal phone number
		 */
		HOME_PHONE("HOME_PHONE"), //$NON-NLS-1$
		
		/**
		 * Professional phone number
		 */
		WORK_PHONE("WORK_PHONE"), //$NON-NLS-1$
		
		/**
		 * mobile phone number
		 */
		MOBILE_PHONE("MOBILE_PHONE"), //$NON-NLS-1$
		
		/**
		 * mail address
		 */
		MAIL("MAIL"); //$NON-NLS-1$
		
		private final String value;
		
		private Type(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}

	}
	
	@XmlAttribute(name="id",required=true)
	@SqlField(name="ID_COORDINATE")
	private UUID idCoordinate = null;
	
	@SqlField(name="CODE_COORDINATE_TYPE")
	private Type coordinateType = Type.HOME_PHONE;
	
	@SqlField(name="VALUE")
	private String value;
	
	@XmlTransient
	@SqlForeignKey(mappedTo="ID_CONTACT")
	private Contact contact;
	
	private static StoreHelper<Coordinate> helper = null;
	static {
		try {
			helper = new StoreHelper<Coordinate>(new SqlStoreHandler<Coordinate>(
					ApplicationCore.dbConnection, Coordinate.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Coordinate() {
		
	}
	
	/**
	 * Init a coordinate wthe a specific type
	 * 
	 * @param coordinateType the type of coordinate
	 * @param value the coordinate value
	 */
	public Coordinate(Type coordinateType, String value) {
		this.coordinateType = coordinateType;
		this.value = value;
	}
	
	/**
	 * returne id of coordinate. Id is an unique database identifier
	 * for coordinate
	 * 
	 * @return id of coordinate
	 */
	public UUID getIdCoordinate() {
		return idCoordinate;
	}

	/**
	 * Set the id of coordinate
	 * 
	 * @param idCoordinate id of coordinate
	 */
	public void setIdCoordinate(UUID idCoordinate) {
		this.idCoordinate = idCoordinate;
	}

	/**
	 * Get type of the coordinate see {@link Type} for more information
	 * 
	 * @return type of coordinate
	 */
	public Type getCoordinateType() {
		return coordinateType;
	}

	/**
	 * Set type of the coordinate
	 * 
	 * @param coordinateType type of coordinate
	 */
	public void setCoordinateType(Type coordinateType) {
		this.coordinateType = coordinateType;
	}

	/**
	 * Get the coordinate value
	 * 
	 * @return the coordinate value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the coordinate value
	 * 
	 * @param value the coordinate value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Get the contact associate with this coordinate
	 * 
	 * @return contact the contact associate
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Set the contact associate with this coordinate
	 * 
	 * @param contact the contact associate
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
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
	 * Save Coordinate in database
	 */
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			if(idCoordinate == null)
				idCoordinate = UUID.randomUUID();
			
			helper.save(this);
			
			if(session != null)
				session.addThreatyObject(this);
		}
	}
	
	/**
	 * Delete coordinate from database
	 */
	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(idCoordinate != null && (session == null || !session.contains(this))) {
			helper.delete(this);
			
			if(session != null)
				session.addThreatyObject(this);
		}
	}
	
	/**
	 * 
	 * @param marshaller
	 */
	protected void beforeMarshal(Marshaller marshaller) {
		if(idCoordinate == null)
			idCoordinate = UUID.randomUUID();
	}
	
	/**
	 * After an unmarshalling operation reattach coordinate with parent contact if exists
	 * 
	 * @param unmarshaller
	 * @param parent the parent contact object
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Contact)
			contact = (Contact)parent;
	}
	
	/**
	 * clone coordinate object with a new id
	 */
	@Override
	protected Coordinate clone() throws CloneNotSupportedException {
		Coordinate clone = (Coordinate)super.clone();
		clone.setIdCoordinate(null);
		
		return clone;
	}
}
