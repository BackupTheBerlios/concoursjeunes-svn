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

/**
 * parametre de distances et blason pour une cible et un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
public class DistancesEtBlason {
	private int[] distances = new int[] { 18, 18 };
	private int blason = 80;

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
	public DistancesEtBlason(int[] distances, int blason) {
		this.distances = distances;
		this.blason = blason;
	}

	/**
	 * @return Returns the blason.
	 * @uml.property name="blason"
	 */
	public int getBlason() {
		return this.blason;
	}

	/**
	 * @return Returns the distance.
	 */
	public int[] getDistance() {
		return this.distances;
	}

	/**
	 * @param blason
	 *            The blason to set.
	 * @uml.property name="blason"
	 */
	public void setBlason(int blason) {
		this.blason = blason;
	}

	/**
	 * @param distances
	 *            The distance to set.
	 */
	public void setDistance(int[] distances) {
		this.distances = distances;
	}

	/**
	 * @return criteriaSet
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * @param criteriaSet
	 *            criteriaSet à définir
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
	 * @param reglement
	 *            reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}

	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();

			if (numdistancesblason == 0) {
				stmt.executeUpdate("insert into DISTANCESBLASONS (NUMREGLEMENT, BLASONS) VALUES (" + //$NON-NLS-1$
						reglement.hashCode() + ", " + blason + ")", Statement.RETURN_GENERATED_KEYS); //$NON-NLS-1$ //$NON-NLS-2$
				ResultSet clefs = stmt.getGeneratedKeys();
				if (clefs.first()) {
					numdistancesblason = (Integer) clefs.getObject(1);
				}
			} else {
				stmt.executeUpdate("update DISTANCESBLASONS set BLASONS=" + //$NON-NLS-1$
						blason + " where NUMREGLEMENT=" + reglement.hashCode() + " and NUMDISTANCESBLASONS=" + numdistancesblason); //$NON-NLS-1$ //$NON-NLS-2$
			}

			criteriaSet.save();

			stmt.executeUpdate("merge into ASSOCIER (NUMDISTANCESBLASONS, " + //$NON-NLS-1$
					"NUMREGLEMENT, NUMCRITERIASET) " + //$NON-NLS-1$
					"VALUES (" + numdistancesblason + ", " + //$NON-NLS-1$ //$NON-NLS-2$
					reglement.hashCode() + "," + //$NON-NLS-1$
					criteriaSet.hashCode() + ")"); //$NON-NLS-1$
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retourne l'objet DistancesEtBlason associé à un concurrent
	 * 
	 * @param concurrent -
	 *            le concurrent pour lequel retourné l'objet
	 * @return l'objet DistancesEtBlason correspondant au concurrent
	 */
	public static DistancesEtBlason getDistancesEtBlasonForConcurrent(Reglement reglement, Concurrent concurrent) {
		return reglement.getDistancesEtBlasonFor(concurrent.getCriteriaSet());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blason;
		result = prime * result + ((criteriaSet == null) ? 0 : criteriaSet.hashCode());
		result = prime * result + Arrays.hashCode(distances);
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
		final DistancesEtBlason other = (DistancesEtBlason) obj;
		if (blason != other.blason)
			return false;
		if (criteriaSet == null) {
			if (other.criteriaSet != null)
				return false;
		} else if (!criteriaSet.equals(other.criteriaSet))
			return false;
		if (!Arrays.equals(distances, other.distances))
			return false;
		return true;
	}

}