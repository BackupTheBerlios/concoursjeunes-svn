/*
 * Créé le 13 mars 2010 à 11:28:12 pour ConcoursJeunes
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
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.Session;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SessionHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.concours.cache.CivilityCache;
import org.concoursjeunes.ApplicationCore;

/**
 * A physical or moral civility information for a contact
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="CIVILITY")
@SqlPrimaryKey(fields="ID_CIVILITY")
public class Civility implements ObjectPersistence {
	private static StoreHelper<Civility> helper = null;
	static {
		try {
			helper = new StoreHelper<Civility>(new SqlStoreHandler<Civility>(
					ApplicationCore.dbConnection, Civility.class));
		} catch (SQLException e) {
			throw new UncheckedException(e);
		}
	}
	
	//utilisé pour donnée un identifiant unique à la sérialisation de l'objet
	@XmlID
	@XmlAttribute(name="id", required=true)
	private String xmlId;
	
	@XmlTransient
	@SqlField(name="ID_CIVILITY")
	private UUID idCivility;
	
	@SqlField(name="ABREVIATION")
	private String abreviation;
	
	@SqlField(name="LIBELLE")
	private String libelle;
	
	@SqlField(name="MORALE")
	private boolean morale = false;
	
	/**
	 * Init ne civility
	 */
	public Civility() {
		
	}
	
	/**
	 * Init the civility
	 * 
	 * @param abreviation forme abrégé de la civilité ex: M., Mme
	 * @param libelle le libellé de la civilité (forme qualifié) ex: Monsieur, Madame
	 */
	public Civility(String abreviation, String libelle) {
		this.abreviation = abreviation;
		this.libelle = libelle;
	}

	/**
	 * Get database id of civility. Can be <code>null</code>
	 * if civility is not in database and never serialised
	 * 
	 * @return id of civility
	 */
	public UUID getIdCivility() {
		return idCivility;
	}

	/**
	 * Set database id of civility
	 * 
	 * @param idCivility id of civility
	 */
	public void setIdCivility(UUID idCivility) {
		this.idCivility = idCivility;
	}

	/**
	 * Get short form of Civility (ex: M., Mr.,Mrs.)
	 * 
	 * @return short form of Civility
	 */
	public String getAbreviation() {
		return abreviation;
	}

	/**
	 * Set short form of Civility (ex: M., Mr.,Mrs.)
	 * 
	 * @param abreviation short form of Civility
	 */
	public void setAbreviation(String abreviation) {
		this.abreviation = abreviation;
	}

	/** 
	 * Get long form of civility (ex: Mister, Misses)
	 * 
	 * @return long form of civility
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * Set ong form of civility (ex: Mister, Misses)
	 * 
	 * @param libelle long form of civility
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * Get if a civility is corporate or physical
	 * 
	 * @return <code>true</code> if it's a corporate civility, <code>false</code> else
	 */
	public boolean isMorale() {
		return morale;
	}

	/**
	 * Set if a civility is corporate or physical
	 * 
	 * @param morale <code>true</code> if it's a corporate civility, <code>false</code> else
	 */
	public void setMorale(boolean morale) {
		this.morale = morale;
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
	 * Save civility in database
	 * 
	 * @param session save session
	 */
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			if(idCivility == null)
				idCivility = UUID.randomUUID();
			
			helper.save(this);
			
			if(session != null)
				session.addThreatyObject(this);
			
			if(!CivilityCache.getInstance().containsKey(idCivility))
				CivilityCache.getInstance().add(this);
		}
	}
	
	/**
	 * Delete civility in database
	 * 
	 * @param session delete session
	 */
	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(idCivility != null && (session == null || !session.contains(this))) {
			helper.delete(this);
			
			if(session != null)
				session.addThreatyObject(this);
			
			CivilityCache.getInstance().remove(idCivility);
		}
	}
	
	/**
	 * Use only by JAXB. Do not use.
	 * 
	 * @param marshaller
	 */
	protected void beforeMarshal(Marshaller marshaller) {
		if(idCivility == null)
			idCivility = UUID.randomUUID();
		xmlId = idCivility.toString();
	}
	
	/**
	 * Use only by JAXB. Do not use.
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		idCivility = UUID.fromString(xmlId);
	}
}
