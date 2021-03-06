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
	private Blason targetFace = new Blason();

	private CriteriaSet criteriaSet = new CriteriaSet();

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
	 * Sauvegarde le couple distances/blasons en base
	 * 
	 * @throws SQLException
	 */
	public void save(int numReglement) throws SQLException {
		Statement stmt = ApplicationCore.dbConnection.createStatement();

		if (numdistancesblason == 0) {
			stmt.executeUpdate("insert into DISTANCESBLASONS (NUMREGLEMENT, NUMBLASON) VALUES (" + //$NON-NLS-1$
					numReglement + ", " + targetFace.getNumblason() + ")", Statement.RETURN_GENERATED_KEYS); //$NON-NLS-1$ //$NON-NLS-2$
			ResultSet clefs = stmt.getGeneratedKeys();
			if (clefs.first()) {
				numdistancesblason = (Integer) clefs.getObject(1);
			}
		} else {
			stmt.executeUpdate("update DISTANCESBLASONS set NUMBLASON=" + //$NON-NLS-1$
					targetFace.getNumblason() + " where NUMREGLEMENT=" + numReglement + " and NUMDISTANCESBLASONS=" + numdistancesblason); //$NON-NLS-1$ //$NON-NLS-2$
		}

		criteriaSet.save(numReglement);

		String sql = "merge into ASSOCIER (NUMDISTANCESBLASONS, " + //$NON-NLS-1$
				"NUMREGLEMENT, NUMCRITERIASET) " + //$NON-NLS-1$
				"VALUES (" + numdistancesblason + ", " + //$NON-NLS-1$ //$NON-NLS-2$
				numReglement + "," + //$NON-NLS-1$
				criteriaSet.hashCode() + ")"; //$NON-NLS-1$
		stmt.executeUpdate(sql); 
		
		stmt.executeUpdate("delete from DISTANCES where NUMDISTANCESBLASONS=" + numdistancesblason + " and NUMREGLEMENT=" + numReglement); //$NON-NLS-1$ //$NON-NLS-2$
		for(int distance : distances) {
			stmt.executeUpdate("insert into DISTANCES (NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) " + //$NON-NLS-1$
					"VALUES (" + numdistancesblason + ", " + numReglement +", " + distance + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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