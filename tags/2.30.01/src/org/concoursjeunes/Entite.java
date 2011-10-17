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
import org.ajdeveloppement.concours.Contact;
import org.ajdeveloppement.concours.cache.EntiteCache;
import org.ajdeveloppement.concours.managers.ContactManager;

/**
 * Entité organisationnelle.<br>
 * Une entité peut représenté
 * <ul>
 * <li>Une Fédération</li>
 * <li>Une ligue</li>
 * <li>Un comité départemental</li>
 * <li>Un club</li>
 * </ul>
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name = "ENTITE")
@SqlPrimaryKey(fields = { "ID_ENTITE" })
public class Entite implements ObjectPersistence {

	public static final int FEDERATION = 0;
	public static final int LIGUE = 1;
	public static final int CD = 2;
	public static final int CLUB = 3;

	// utilisé pour donnée un identifiant unique à la sérialisation de l'objet
	@XmlID
	@XmlAttribute(name = "id", required = true)
	private String xmlId;

	@XmlTransient
	@SqlField(name = "ID_ENTITE")
	private UUID idEntite;
	@SqlField(name = "NOMENTITE")
	private String nom;
	@SqlField(name = "AGREMENTENTITE")
	private String agrement;
	@XmlIDREF
	@SqlForeignKey(mappedTo="NUMFEDERATION")
	private Federation federation;
	@SqlField(name = "ADRESSEENTITE")
	private String adresse;
	@SqlField(name = "CODEPOSTALENTITE")
	private String codePostal;
	@SqlField(name = "VILLEENTITE")
	private String ville;
	@SqlField(name = "PAYS")
	private String pays = "fr"; //$NON-NLS-1$
	@SqlField(name = "NOTEENTITE")
	private String note;
	@SqlField(name = "TYPEENTITE")
	private int type = CLUB;

	@XmlAttribute
	@SqlField(name = "REMOVABLE")
	private boolean removable = true;

	// private transient List<Contact> contacts = null;

	private static StoreHelper<Entite> helper = null;
	static {
		try {
			helper = new StoreHelper<Entite>(new SqlStoreHandler<Entite>(
					ApplicationCore.dbConnection, Entite.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Entite() {

	}

	/**
	 * Construit une nouvelle entité nommé ayant le type fournit en parametre
	 * 
	 * @param nom
	 *            le nom de l'entite
	 * @param type
	 *            le type de l'entite
	 */
	public Entite(String nom, int type) {
		this.nom = nom;
		this.type = type;
	}

	/**
	 * Retourne l'identifiant de l'entite
	 * 
	 * @return l'identifiant de l'entite
	 */
	public UUID getIdEntite() {
		return idEntite;
	}

	/**
	 * Définit l'identifiant de l'entite
	 * 
	 * @param idEntite
	 *            l'identifiant de l'entite
	 */
	public void setIdEntite(UUID idEntite) {
		this.idEntite = idEntite;
	}

	/**
	 * Retourne l'adresse de l'entite
	 * 
	 * @return l'adresse de l'entite
	 */
	public String getAdresse() {
		if (adresse == null)
			return ""; //$NON-NLS-1$
		return adresse;
	}

	/**
	 * Retourne le numéro d'agrement de l'association
	 * 
	 * @return le numéro d'agrement
	 */
	public String getAgrement() {
		if (agrement == null)
			return ""; //$NON-NLS-1$
		return agrement;
	}

	/**
	 * Retourne le code postal de l'adresse de l'entite
	 * 
	 * @return le code postal
	 */
	public String getCodePostal() {
		if (codePostal == null)
			return ""; //$NON-NLS-1$
		return codePostal;
	}

	/**
	 * Retourne le nom de l'entite
	 * 
	 * @return le nom
	 */
	public String getNom() {
		if (nom == null)
			return ""; //$NON-NLS-1$
		return nom;
	}

	/**
	 * Retourne le type d'entite.<br>
	 * Les types possible sont: FEDERATION, LIGUE, CD, CLUB
	 * 
	 * @return le type de l'entite
	 */
	public int getType() {
		return type;
	}

	/**
	 * Retourne la ville de l'entite
	 * 
	 * @return la ville de l'entite
	 */
	public String getVille() {
		if (ville == null)
			return ""; //$NON-NLS-1$
		return ville;
	}

	/**
	 * Définit l'adresse de l'entite
	 * 
	 * @param adresse
	 *            l'adresse de l'entite
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * Définit le numéro d'agrement identifiant de manière unique l'entite
	 * 
	 * @param agrement
	 *            le numéro d'agrement
	 */
	public void setAgrement(String agrement) {
		this.agrement = agrement;
	}

	/**
	 * @param federation federation à définir
	 */
	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	/**
	 * @return federation
	 */
	public Federation getFederation() {
		return federation;
	}

	/**
	 * Définit le code postal de l'adresse de l'entite
	 * 
	 * @param codePostal
	 *            le code postal
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	/**
	 * Définit le nom de l'entite
	 * 
	 * @param nom
	 *            le nom
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Définit le type d'entite.<br>
	 * Les types possible sont: FEDERATION, LIGUE, CD, CLUB
	 * 
	 * @param type
	 *            le type d'entite
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Définit la ville de l'entite
	 * 
	 * @param ville
	 *            la ville de l'entite
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return pays
	 */
	public String getPays() {
		return pays;
	}

	/**
	 * @param pays pays à définir
	 */
	public void setPays(String pays) {
		this.pays = pays;
	}

	/**
	 * Retourne une note ou commentaire préalablement définit sur l'entite
	 * 
	 * @return une note sur l'entite
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Ajoute une note ou commentaire sur l'entite
	 * 
	 * @param note
	 *            une note sur l'entite
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param removable
	 *            removable à définir
	 */
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	/**
	 * @return removable
	 */
	public boolean isRemovable() {
		return removable;
	}

	// /**
	// * @param contacts contacts à définir
	// */
	// public void setContacts(List<Contact> contacts) {
	// this.contacts = contacts;
	// }

	/**
	 * @return contacts
	 */
	public List<Contact> getContacts() {
		//if (contacts == null && idEntite != null) { // la liste des contacts est
													// chargé en lazy loading
		try {
			return ContactManager.getContactsForEntity(this);
		} catch (ObjectPersistenceException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Contact>();
		//}
		//return contacts;
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
	 * Sauvegarde l'entite dans la base de donnée
	 */
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if (session == null || !session.contains(this)) {
			if (idEntite == null)
				idEntite = UUID.randomUUID();

			if (nom == null || nom.isEmpty())
				return;

			// SqlManager sqlManager = new
			// SqlManager(ApplicationCore.dbConnection, null);
			// //Avant d'enregistrer, on recherche dans la base si il n'y a pas
			// déjà un enregistrement pour cette entite avec
			// //un autre id
			// try {
			// ResultSet rs = sqlManager.executeQuery(
			//						String.format("select ID_ENTITE from ENTITE where AGREMENTENTITE='%s' and NOMENTITE='%s'", //$NON-NLS-1$
			//								(agrement != null) ? agrement.replace("'", "''") : "", nom.replace("'", "''"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			// if(rs.first()) {
			//					UUID savedIdEntite = (UUID)rs.getObject("ID_ENTITE"); //$NON-NLS-1$
			// if(!savedIdEntite.equals(idEntite))
			// return;
			// }
			// } catch (SQLException e) {
			// throw new ObjectPersistenceException(e);
			// }
			if(federation != null)
				federation.save(session);

			helper.save(this);

			if (session != null)
				session.addThreatyObject(this);

			// ajoute l'entrée au cache si nécessaire
			if (!EntiteCache.getInstance().containsKey(idEntite))
				EntiteCache.getInstance().add(this);
		}
	}

	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if (session == null || !session.contains(this)) {
			helper.delete(this);

			if (session != null)
				session.addThreatyObject(this);
			// retire l'objet du cache
			EntiteCache.getInstance().remove(idEntite);
		}
	}

	/**
	 * 
	 * @param marshaller
	 */
	public void beforeMarshal(Marshaller marshaller) {
		if (idEntite == null)
			idEntite = UUID.randomUUID();
		xmlId = idEntite.toString();
	}

	/**
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if (xmlId != null)
			idEntite = UUID.fromString(xmlId);

		xmlId = null;
	}

	@Override
	public String toString() {
		if ((nom == null || nom.isEmpty()) && ville != null && !ville.isEmpty())
			return ville;
		return (nom == null) ? "" : nom; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agrement == null) ? 0 : agrement.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + type;
		result = prime * result + ((ville == null) ? 0 : ville.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entite other = (Entite) obj;
		if (agrement == null) {
			if (other.agrement != null)
				return false;
		} else if (!agrement.equals(other.agrement))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (type != other.type)
			return false;
		if (ville == null) {
			if (other.ville != null)
				return false;
		} else if (!ville.equals(other.ville))
			return false;
		return true;
	}
}