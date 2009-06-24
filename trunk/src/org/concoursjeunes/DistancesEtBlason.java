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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.ajdeveloppement.commons.sql.SqlField;
import org.ajdeveloppement.commons.sql.SqlForeignFields;
import org.ajdeveloppement.commons.sql.SqlPersistance;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.sql.SqlStoreHelper;
import org.ajdeveloppement.commons.sql.SqlTable;
import org.concoursjeunes.xml.bind.BlasonAdapter;

/**
 * parametre de distances et blason pour une cible et un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
@SqlTable(name="DISTANCESBLASONS")
@SqlPrimaryKey(fields={"NUMDISTANCESBLASONS","NUMREGLEMENT"},generatedidField="NUMDISTANCESBLASONS")
@SqlForeignFields(fields={"NUMREGLEMENT","NUMBLASON","NUMCRITERIASET"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DistancesEtBlason implements SqlPersistance {
	@XmlElementWrapper(name="distances",required=true)
    @XmlElement(name="distance")
	private int[] distances = new int[] { 18, 18 };
	@XmlTransient
	private int blason = 80;
	@XmlJavaTypeAdapter(BlasonAdapter.class)
	private Blason targetFace = new Blason();
	@SqlField(name="DEFAULTTARGETFACE")
	private boolean defaultTargetFace = true;

	private CriteriaSet criteriaSet = new CriteriaSet();

	@SqlField(name="NUMDISTANCESBLASONS")
	@XmlTransient
	private int numdistancesblason = 0;
	
	private static SqlStoreHelper<DistancesEtBlason> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<DistancesEtBlason>(ApplicationCore.dbConnection, DistancesEtBlason.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * cree un d&b avec les options par défaut (pour sérialisation XML)
	 * 
	 */
	public DistancesEtBlason() {
	}

	/**
	 * Cree un distance et blason avec les bon param
	 * 
	 * @param distances tableau des distances représenté. 1 distance par série.
	 * 	Les distances sont représenté en metre
	 * @param targetFace le blason associé
	 */
	public DistancesEtBlason(int[] distances, Blason targetFace) {
		this.distances = distances;
		this.targetFace = targetFace;
	}

	/**
	 * <p>Conserver pour compatibilite avec les concours réalisé avant la création
	 * de la propriete targetFace.</p>
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
	 * <p>Conserver pour compatibilite avec les concours réalisé avant la création
	 * de la propriete targetFace.</p>
	 * 
	 * @deprecated remplacer par <b><i>{@link DistancesEtBlason#setTargetFace(Blason)}</i></b>
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
	 * Retourne le blason
	 * 
	 * @return targetFace le blason
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
	 * @param distances
	 *            The distance to set.
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
	 * @return le jeux de critére associé
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * Définit le jeux de critère que représente l'objet
	 * 
	 * @param criteriaSet
	 *            le jeux de critére associé
	 */
	public void setCriteriaSet(CriteriaSet criteriaSet) {
		this.criteriaSet = criteriaSet;
	}

	/**
	 * @return numdistancesblason
	 */
	public int getNumdistancesblason() {
		return numdistancesblason;
	}

	/**
	 * @param numdistancesblason
	 *            numdistancesblason à définir
	 */
	public void setNumdistancesblason(int numdistancesblason) {
		this.numdistancesblason = numdistancesblason;
	}
	
	/**
	 * Sauvegarde le couple distances/blasons en base.
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 * @throws SqlPersistanceException
	 */
	@SuppressWarnings("nls")
	@Override
	public void save() throws SqlPersistanceException {
		criteriaSet.save();
		
		Map<String, Object> fk = new HashMap<String, Object>();
		fk.put("NUMREGLEMENT", criteriaSet.getReglement().getNumReglement()); //$NON-NLS-1$
		fk.put("NUMBLASON", targetFace.getNumblason()); 
		fk.put("NUMCRITERIASET", criteriaSet.getNumCriteriaSet()); 
		helper.save(this, fk);
		
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
			throw new SqlPersistanceException(e);
		}
	}
	
	/**
	 * Supprime le distances/blason de la base.
	 * 
	 * @throws SQLException
	 */
	@Override
	public void delete() throws SqlPersistanceException {
		helper.delete(this, Collections.singletonMap("NUMREGLEMENT", (Object)criteriaSet.getReglement().getNumReglement()));  //$NON-NLS-1$
	}

	/**
	 * Retourne l'objet DistancesEtBlason associé à un concurrent pour
	 * un réglement donné.
	 * 
	 * @param reglement le reglement detreminant le DistancesEtBlason
	 * du concurrent
	 * @param concurrent
	 *            le concurrent pour lequel retourné l'objet
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
		return ldb.get(0);
	}
	
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Reglement)
			criteriaSet.setReglement((Reglement)parent);
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