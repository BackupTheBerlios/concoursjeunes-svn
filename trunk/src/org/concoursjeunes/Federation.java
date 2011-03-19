/*
 * Créé le 9 août 2008 à 14:02:05 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.Marshaller;
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
import org.ajdeveloppement.commons.persistence.Session;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SessionHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlGeneratedIdField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.ajdeveloppement.concours.cache.FederationCache;

/**
 * Représente une fédération de tir à l'arc
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="FEDERATION")
@SqlPrimaryKey(fields={"NUMFEDERATION"},generatedidField=@SqlGeneratedIdField(name="NUMFEDERATION",type=Types.INTEGER))
public class Federation implements ObjectPersistence {
	private static StoreHelper<Federation> helper = null;
	static {
		try {
			helper = new StoreHelper<Federation>(new SqlStoreHandler<Federation>(ApplicationCore.dbConnection, Federation.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@XmlID
	@XmlAttribute(name="id")
	@SuppressWarnings("unused")
	private String xmlId;
	
	@SqlField(name="NUMFEDERATION")
	@XmlTransient
	private int numFederation = 0;
	@SqlField(name="SIGLEFEDERATION")
	@XmlElement(name="sigle")
	private String sigleFederation = ""; //$NON-NLS-1$
	@SqlField(name="NOMFEDERATION")
	@XmlElement(name="nom")
	private String nomFederation = ""; //$NON-NLS-1$
	@XmlElementWrapper(name="niveaux",required=true)
	@XmlElement(name="niveau")
	private List<CompetitionLevel> competitionLevels = new ArrayList<CompetitionLevel>();
	
	private static PreparedStatement pstmtAlreadyExists = null;
	
	public Federation() {
	}
	
	/**
	 * initialise une nouvelle fédération avec sont nom et son sigle
	 * 
	 * @param nomFederation le nom de la fédération à initialiser
	 * @param sigleFederation le sigle de la fédération à initialiser
	 * @throws SQLException 
	 */
	public Federation(String nomFederation, String sigleFederation) {
		this(nomFederation, 0, sigleFederation);
	}
	
	/**
	 * initialise une nouvelle fédération avec sont nom, son sigle ainsi que 
	 * son numéro en base de données.
	 * 
	 * @param nomFederation le nom de la fédération à initialiser
	 * @param numFederation le numéro en base de la fédération
	 * @param sigleFederation le sigle de la fédération à initialiser
	 * @throws SQLException 
	 */
	public Federation(String nomFederation, int numFederation,
			String sigleFederation) {
		this.nomFederation = nomFederation;
		this.numFederation = numFederation;
		this.sigleFederation = sigleFederation;
	}

	/**
	 * Retourne le numéro en base de la fédération
	 * 
	 * @return numFederation le numéro en base de la fédération
	 */
	public int getNumFederation() {
		return numFederation;
	}

	/**
	 * Définit le numéro de la fédération en base
	 * 
	 * @param numFederation le numéro en base de la fédération
	 */
	public void setNumFederation(int numFederation) {
		this.numFederation = numFederation;
	}

	/**
	 * Retourne le sigle de la fédération par exemple <i>FITA</i>
	 * 
	 * @return le sigle de la fédération
	 */
	public String getSigleFederation() {
		return sigleFederation;
	}

	/**
	 * Définit le sigle de la fédération par exemple <i>FITA</i>
	 * 
	 * @param sigleFederation le sigle de la fédération
	 */
	public void setSigleFederation(String sigleFederation) {
		this.sigleFederation = sigleFederation;
	}

	/**
	 * Retourne le nom complet de la fédération. Par exemple
	 * <i>Fédération International de Tir à l'Arc</i>
	 *  
	 * @return le nom de la fédération
	 */
	public String getNomFederation() {
		return nomFederation;
	}

	/**
	 * Définit le nom complet de la fédération. Par exemple
	 * <i>Fédération International de Tir à l'Arc</i>
	 * 
	 * @param nomFederation le nom de la fédération
	 */
	public void setNomFederation(String nomFederation) {
		this.nomFederation = nomFederation;
	}
	
	/**
	 * <p>Retourne la liste des niveaux de compétition disponible pour cette fédération.</p>
	 * <p>Un niveau de compétition peut être retourné plusieurs fois si il est disponible
	 * dans plusieurs langues, aussi préférer l'utilisation de {@link #getCompetitionLevels(String)}
	 * en précisant la langue pour retourner la liste des niveaux.</p>
	 * 
	 * @return competitionLevels la liste des niveaux de compétition disponible
	 */
	public List<CompetitionLevel> getCompetitionLevels() {
		return competitionLevels;
	}

	/**
	 * <p>Définit la liste des niveaux de compétition disponible.</p>
	 * <p>Comme aucune vérification de la présence en base des niveaux n'est réalisé,
	 * cette méthode est uniquement présente pour une utilisation par les
	 * fonction de sérialisation XML.</p>
	 * <p>Utiliser à la place les méthodes {@link #addCompetitionLevel(CompetitionLevel)}
	 * et {@link #removeCompetitionLevel(CompetitionLevel)} qui assure les fonctions
	 * de persistance</p>
	 * 
	 * @param competitionLevels la liste des niveaux de compétition disponible
	 */
	public void setCompetitionLevels(List<CompetitionLevel> competitionLevels) {
		this.competitionLevels = competitionLevels;
		
		for(CompetitionLevel competitionLevel : competitionLevels)
			competitionLevel.setFederation(this);
	}
	
	/**
	 * Ajoute un niveau de compétition à la fédération. Lorsqu'un niveau
	 * de compétition est ajouté à la fédération, celui ci est immédiatement 
	 * enregistrer dans la base de données.
	 * 
	 * @param competitionLevel le niveau de compétition à ajouter à la fédération.
	 */
	public void addCompetitionLevel(CompetitionLevel competitionLevel) {
		this.competitionLevels.add(competitionLevel);
		
		competitionLevel.setFederation(this);
	}
	
	/**
	 * Supprime un niveau de compétition de la fédération. Lorsqu'un niveau
	 * de compétition est supprimé de la fédération, celui ci est immédiatement 
	 * supprimé dans la base de données.
	 * 
	 * @param competitionLevel le niveau de compétition à supprimer de la fédération.
	 * @throws SQLException
	 */
	public void removeCompetitionLevel(CompetitionLevel competitionLevel) {
		this.competitionLevels.remove(competitionLevel);
	}

	/**
	 * <p>Retourne la liste de tous les niveaux de compétition accessible pour la
	 * fédération en fonction de la langue fournit en paramètre.</p>
	 * <p>Si le niveau n'a pas été traduit dans la langue désiré, la valeur
	 * pour la localisation <i>fr</i> sera retourné.</p>
	 * 
	 * @param lang la langue des libellés de niveau de compétition au format ISO 639 (langue sur 2 caractères).
	 * @return la liste de tous les niveaux de compétition accessible pour la
	 * fédération
	 */
	public List<CompetitionLevel> getCompetitionLevels(String lang) {
		List<CompetitionLevel> competitionLevelList = new ArrayList<CompetitionLevel>();

		for(CompetitionLevel cl : competitionLevels) {
			if(cl.getLang().equals(lang))
				competitionLevelList.add(cl);
		}
		
		if(competitionLevelList.size() == 0 && !lang.equals("fr")) //$NON-NLS-1$
			competitionLevelList = getCompetitionLevels("fr"); //$NON-NLS-1$
		
		return competitionLevelList;
	}

	private void checkAlreadyExists() throws SQLException {
		if(pstmtAlreadyExists == null) {
			String sql = "select NUMFEDERATION from FEDERATION where SIGLEFEDERATION=? and NOMFEDERATION=?"; //$NON-NLS-1$
		
			pstmtAlreadyExists = ApplicationCore.dbConnection.prepareStatement(sql);
		}
		pstmtAlreadyExists.setString(1, sigleFederation);
		pstmtAlreadyExists.setString(2, nomFederation);
		ResultSet rs = pstmtAlreadyExists.executeQuery();
		try {
			if(rs.next())
				numFederation = rs.getInt(1);
		} finally {
			rs.close();
		}
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
	 * Sauvegarde la fédération en base de données. Les arguments sont ignoré.
	 */
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			try {
				checkAlreadyExists();
				
				helper.save(this);
				
				if(session != null)
					session.addThreatyObject(this);
				
				if(!FederationCache.getInstance().containsKey(numFederation))
					FederationCache.getInstance().add(this);
		
				Statement stmt = ApplicationCore.dbConnection.createStatement();
				String sql = "delete from NIVEAU_COMPETITION where NUMFEDERATION=" + numFederation; //$NON-NLS-1$
				stmt.executeUpdate(sql);
				
				Map<String, List<CompetitionLevel>> langFilteredCL = new HashMap<String, List<CompetitionLevel>>();
				
				for(CompetitionLevel cl : competitionLevels) {
					if(!langFilteredCL.containsKey(cl.getLang()))
						langFilteredCL.put(cl.getLang(), new ArrayList<CompetitionLevel>());
					langFilteredCL.get(cl.getLang()).add(cl);
				}
				
				for(Entry<String, List<CompetitionLevel>> entry : langFilteredCL.entrySet()) {
					int i = 1;
					for(CompetitionLevel cl : entry.getValue()) {
						cl.setNumLevel(i++);
						cl.setFederation(this);
						cl.save(session);
					}
				}
			} catch (SQLException e) {
				throw new ObjectPersistenceException(e);
			}
		}
	}

	/**
	 * Supprime la fédération de la base de données. Les arguments sont ignoré.
	 * 
	 * Tous les règlements attaché à cette fédération seront également supprimés
	 */
	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			helper.delete(this);
			
			if(session != null)
				session.addThreatyObject(this);
			
			FederationCache.getInstance().remove(numFederation);
		}
	}
	
	/**
	 * For JAXB Usage only. Do not use.
	 * 
	 * @param marshaller
	 */
	protected void beforeMarshal(Marshaller marshaller) {
		xmlId = UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for(CompetitionLevel competitionLevel : competitionLevels) {
			competitionLevel.setFederation(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + numFederation;
		result = prime * result
				+ ((sigleFederation == null) ? 0 : sigleFederation.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Federation other = (Federation) obj;
		//if (numFederation != other.numFederation)
		//	return false;
		if (sigleFederation == null) {
			if (other.sigleFederation != null)
				return false;
		} else if (!sigleFederation.equals(other.sigleFederation))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return nomFederation + " (" + sigleFederation + ")";
	}
}
