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
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.Session;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SessionHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlForeignKey;
import org.ajdeveloppement.commons.persistence.sql.SqlGeneratedIdField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;
import org.concoursjeunes.xml.bind.BlasonAdapter;

/**
 * parametre de distances et blason pour une cible et un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
@SqlTable(name="DISTANCESBLASONS")
@SqlPrimaryKey(fields={"NUMDISTANCESBLASONS","NUMREGLEMENT"},generatedidField=@SqlGeneratedIdField(name="NUMDISTANCESBLASONS",type=Types.INTEGER))
@XmlAccessorType(XmlAccessType.FIELD)
public class DistancesEtBlason implements ObjectPersistence {
	@XmlElementWrapper(name="distances",required=true)
    @XmlElement(name="distance")
	private int[] distances = new int[] { 18, 18 };
	@XmlTransient
	private int blason = 80;
	@XmlJavaTypeAdapter(BlasonAdapter.class)
	@SqlForeignKey(mappedTo="NUMBLASON")
	private Blason targetFace = new Blason();
	@SqlField(name="DEFAULTTARGETFACE")
	private boolean defaultTargetFace = true;

	@SqlForeignKey(mappedTo="NUMCRITERIASET")
	private CriteriaSet criteriaSet = new CriteriaSet();

	@SqlField(name="NUMDISTANCESBLASONS")
	@XmlTransient
	private int numdistancesblason = 0;
	
	@XmlTransient
	@SqlForeignKey(mappedTo="NUMREGLEMENT")
	private Reglement reglement = new Reglement();
	
	private static StoreHelper<DistancesEtBlason> helper = null;
	static {
		try {
			helper = new StoreHelper<DistancesEtBlason>(new SqlStoreHandler<DistancesEtBlason>(ApplicationCore.dbConnection, DistancesEtBlason.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * construit un DistancesEtBlason avec les options par défaut (pour sérialisation XML)
	 * 
	 */
	public DistancesEtBlason() {
	}

	/**
	 * Construit un distance et blason avec les bons paramètre
	 * 
	 * @param distances tableau des distances représenté. 1 distance par série.
	 * 	Les distances sont représenté en mètre
	 * @param targetFace le blason associé
	 */
	public DistancesEtBlason(int[] distances, Blason targetFace) {
		this.distances = distances;
		this.targetFace = targetFace;
	}

	/**
	 * <p>Conserver pour compatibilité avec les concours réalisé avant la création
	 * de la propriété targetFace.</p>
	 * 
	 * @deprecated remplacer par <b><i>{@link DistancesEtBlason#getTargetFace()}</i></b>
	 * 
	 * @return Returns the blason.
	 */
	@Deprecated
	public int getBlason() {
		return this.blason;
	}
	
	/**
	 * <p>Conserver pour compatibilité avec les concours réalisé avant la création
	 * de la propriété targetFace.</p>
	 * 
	 * @deprecated remplacé par {@link DistancesEtBlason#setTargetFace(Blason)}
	 * 
	 * @param blason
	 *            The blason to set.
	 */
	@Deprecated
	public void setBlason(int blason) {
		this.blason = blason;
	}

	/**
	 * Retourne le tableau des distances
	 * 
	 * @return le tableau des distances
	 */
	public int[] getDistance() {
		return this.distances;
	}

	/**
	 * Retourne le blason de l'objet
	 * 
	 * @return le blason
	 */
	public Blason getTargetFace() {
		return targetFace;
	}

	/**
	 * Définit le blason
	 * 
	 * @param targetFace le blason
	 */
	public void setTargetFace(Blason targetFace) {
		this.targetFace = targetFace;
	}

	/**
	 * Définit le tableau des distances
	 * 
	 * @param distances le tableau des distances de l'objet
	 */
	public void setDistance(int[] distances) {
		this.distances = distances;
	}

	/**
	 * Indique si c'est le blason par défaut ou si l'objet représente
	 * représente un blason alternatif.
	 * 
	 * @return true si c'est le blason par défaut, false
	 * si c'est un blason alternatif.
	 */
	public boolean isDefaultTargetFace() {
		return defaultTargetFace;
	}

	/**
	 * Définit si c'est le blason par défaut ou si l'objet représente
	 * représente un blason alternatif.
	 * 
	 * @param defaultTargetFace true si c'est le blason par défaut, false
	 * si c'est un blason alternatif.
	 */
	public void setDefaultTargetFace(boolean defaultTargetFace) {
		this.defaultTargetFace = defaultTargetFace;
	}

	/**
	 * Le jeux de critère pour lequel l'objet est définit
	 * 
	 * @return le jeux de critère associé
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * Définit le jeux de critère que représente l'objet
	 * 
	 * @param criteriaSet le jeux de critère associé
	 */
	public void setCriteriaSet(CriteriaSet criteriaSet) {
		this.criteriaSet = criteriaSet;
		if(reglement == null)
			reglement = criteriaSet.getReglement();
	}
	
	public Reglement getReglement() {
		return reglement;
	}
	
	public void setReglement(Reglement reglement) {
		this.reglement = reglement; 
	}

	/**
	 * Retourne le numéro en base de l'objet
	 * 
	 * @return le numéro en base de l'objet
	 */
	public int getNumdistancesblason() {
		return numdistancesblason;
	}

	/**
	 * Définit le numéro en base de l'objet
	 * 
	 * @param numdistancesblason le numéro en base de l'objet
	 */
	public void setNumdistancesblason(int numdistancesblason) {
		this.numdistancesblason = numdistancesblason;
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
	 * Sauvegarde le couple distances/blasons en base.
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 * @throws SqlPersistanceException
	 */
	@SuppressWarnings("nls")
	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			criteriaSet.save(session);
			
			helper.save(this);
			
			if(session != null)
				session.addThreatyObject(this);
			
			try {
				Statement stmt = ApplicationCore.dbConnection.createStatement();
				try {
					stmt.executeUpdate("delete from DISTANCES where NUMDISTANCESBLASONS=" + numdistancesblason + " and NUMREGLEMENT=" + criteriaSet.getReglement().getNumReglement()); 
					int i = 1;
					for(int distance : distances) {
						stmt.executeUpdate("insert into DISTANCES (NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) " + //$NON-NLS-1$
								"VALUES (" + (i++) +", " + numdistancesblason + ", " + criteriaSet.getReglement().getNumReglement() +", " + distance + ")"); 
					}
				} finally {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new ObjectPersistenceException(e);
			}
		}
	}
	
	/**
	 * Supprime le distances/blason de la base.
	 * 
	 * @throws SQLException
	 */
	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			helper.delete(this);
			
			if(session != null)
				session.addThreatyObject(this);
		}
	}

	/**
	 * Retourne l'objet DistancesEtBlason associé à un concurrent pour
	 * un règlement donné.
	 * 
	 * @param reglement le règlement déterminant le DistancesEtBlason
	 * du concurrent
	 * @param concurrent le concurrent pour lequel retourné l'objet
	 * 
	 * @return l'objet DistancesEtBlason correspondant au concurrent
	 */
	public static DistancesEtBlason getDistancesEtBlasonForConcurrent(Reglement reglement, Concurrent concurrent) {
		List<DistancesEtBlason> ldb = reglement.getDistancesEtBlasonFor(concurrent.getCriteriaSet().getFilteredCriteriaSet(reglement.getPlacementFilter()));
		if(concurrent.isUseAlternativeTargetFace()) {
			for(DistancesEtBlason db : ldb) {
				 if(concurrent.getAlternativeTargetFace().equals(db.getTargetFace()))
					return db;
			}
		}
		if(ldb.size() > 0)
			return ldb.get(0);
		return null;
	}
	
	/**
	 * Used by JAXB Only
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Reglement) {
			criteriaSet.setReglement((Reglement)parent);
			reglement = (Reglement)parent;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((criteriaSet == null) ? 0 : criteriaSet.hashCode());
		result = prime * result + Arrays.hashCode(distances);
		result = prime * result
				+ ((targetFace == null) ? 0 : targetFace.hashCode());
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
		final DistancesEtBlason other = (DistancesEtBlason) obj;
		if (!Arrays.equals(distances, other.distances))
			return false;
		if (targetFace == null) {
			if (other.targetFace != null)
				return false;
		} else if (!targetFace.equals(other.targetFace))
			return false;
		return true;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {	
		return "{" + targetFace.toString() + "," + Arrays.toString(distances) + "}";
	}

}