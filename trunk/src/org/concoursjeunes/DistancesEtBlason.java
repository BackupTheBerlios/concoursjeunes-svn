/*
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
package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.concoursjeunes.builders.BlasonBuilder;

/**
 * parametre de distances et blason pour une cible et un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
public class DistancesEtBlason {
	private int[] distances = new int[] { 18, 18 };
	private int blason = 80;
	private Blason targetFace = Blason.NULL;

	private CriteriaSet criteriaSet = new CriteriaSet();

	private Reglement reglement = new Reglement();
	private int numdistancesblason = 0;

	/**
	 * cree un d&b avec les options par défaut (pour sérialisation XML)
	 * 
	 */
	public DistancesEtBlason() {
	}

	/**
	 * Cree un distance et blason avec les bon param
	 * 
	 * @param distances
	 * @param blason
	 */
	public DistancesEtBlason(int[] distances, Blason targetFace) {
		this.distances = distances;
		this.targetFace = targetFace;
	}

	/**
	 * @return Returns the blason.
	 */
	@Deprecated
	public int getBlason() {
		return this.blason;
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
	 * @param blason
	 *            The blason to set.
	 */
	@Deprecated
	public void setBlason(int blason) {
		//this.blason = blason;
		if(targetFace.equals(Blason.NULL)) {
			if(numdistancesblason > 0)
				targetFace = BlasonBuilder.getBlasons(numdistancesblason, reglement.hashCode());
			else {
				try {
	                targetFace = BlasonManager.findBlasonInDatabase(blason + "cm"); //$NON-NLS-1$
                } catch (SQLException e) {
	                e.printStackTrace();
                }
				if(targetFace == null) {
					double hRatio = 1;
					double vRatio = 1;
					if(blason >= 60)
						hRatio = 0.5;
					if(blason >= 40)
						vRatio = 0.5;
					targetFace = new Blason(blason + "cm", hRatio, vRatio); //$NON-NLS-1$
				}
			}
		}
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
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * Définit le réglement associé au couple distances/blasons
	 * 
	 * @param reglement
	 *            reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}

	/**
	 * Sauvegarde le couple distances/blasons en base
	 * 
	 * @throws SQLException
	 */
	public void save() throws SQLException {
		Statement stmt = ConcoursJeunes.dbConnection.createStatement();

		if (numdistancesblason == 0) {
			stmt.executeUpdate("insert into DISTANCESBLASONS (NUMREGLEMENT, NUMBLASON) VALUES (" + //$NON-NLS-1$
					reglement.hashCode() + ", " + targetFace.getNumblason() + ")", Statement.RETURN_GENERATED_KEYS); //$NON-NLS-1$ //$NON-NLS-2$
			ResultSet clefs = stmt.getGeneratedKeys();
			if (clefs.first()) {
				numdistancesblason = (Integer) clefs.getObject(1);
			}
		} else {
			stmt.executeUpdate("update DISTANCESBLASONS set NUMBLASON=" + //$NON-NLS-1$
					targetFace.getNumblason() + " where NUMREGLEMENT=" + reglement.hashCode() + " and NUMDISTANCESBLASONS=" + numdistancesblason); //$NON-NLS-1$ //$NON-NLS-2$
		}

		criteriaSet.save();

		String sql = "merge into ASSOCIER (NUMDISTANCESBLASONS, " + //$NON-NLS-1$
				"NUMREGLEMENT, NUMCRITERIASET) " + //$NON-NLS-1$
				"VALUES (" + numdistancesblason + ", " + //$NON-NLS-1$ //$NON-NLS-2$
				reglement.hashCode() + "," + //$NON-NLS-1$
				criteriaSet.hashCode() + ")"; //$NON-NLS-1$
		stmt.executeUpdate(sql); 
		
		stmt.executeUpdate("delete from DISTANCES where NUMDISTANCESBLASONS=" + numdistancesblason + " and NUMREGLEMENT=" + reglement.hashCode()); //$NON-NLS-1$ //$NON-NLS-2$
		for(int distance : distances) {
			stmt.executeUpdate("insert into DISTANCES (NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) " + //$NON-NLS-1$
					"VALUES (" + numdistancesblason + ", " + reglement.hashCode() +", " + distance + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
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
		return reglement.getDistancesEtBlasonFor(concurrent.getCriteriaSet().getFilteredCriteriaSet(reglement.getPlacementFilter()));
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
		if (criteriaSet == null) {
			if (other.criteriaSet != null)
				return false;
		} else if (!criteriaSet.equals(other.criteriaSet))
			return false;
		if (!Arrays.equals(distances, other.distances))
			return false;
		if (targetFace == null) {
			if (other.targetFace != null)
				return false;
		} else if (!targetFace.equals(other.targetFace))
			return false;
		return true;
	}

	

}