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
 * @author  Aurélien Jeoffray
 * @version  1.0
 */
public class DistancesEtBlason {
	private int[] distances = new int[] {18, 18};
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
	 * @return  Returns the blason.
	 * @uml.property  name="blason"
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
	 * @param blason  The blason to set.
	 * @uml.property  name="blason"
	 */
	public void setBlason(int blason) {
		this.blason = blason;
	}


	/**
	 * @param distances The distance to set.
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
	 * @param criteriaSet criteriaSet à définir
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
	 * @param numdistancesblason numdistancesblason à définir
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
	 * @param reglement reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}

	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			if(numdistancesblason == 0) {
				stmt.executeUpdate("insert into DISTANCESBLASONS (NUMREGLEMENT, BLASONS) VALUES (" +
						reglement.getIdReglement() + ", " + blason + ")", Statement.RETURN_GENERATED_KEYS);
				ResultSet clefs = stmt.getGeneratedKeys();
				if(clefs.first()){
					numdistancesblason = (Integer)clefs.getObject(1);  
				}
			} else {
				stmt.executeUpdate("update DISTANCESBLASONS set BLASONS=" +
						blason + " where NUMREGLEMENT=" + reglement.getIdReglement());
			}
			
			criteriaSet.save();
			
			stmt.executeUpdate("merge into ASSOCIER (NUMDISTANCESBLASONS, " +
					"NUMCRITERIASET) VALUES (" + numdistancesblason + ", " + criteriaSet.getNumCriteriaSet() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Retourne l'objet DistancesEtBlason associé à un concurrent
	 * 
	 * @param concurrent - le concurrent pour lequel retourné l'objet
	 * @return l'objet DistancesEtBlason correspondant au concurrent
	 */
	public static DistancesEtBlason getDistancesEtBlasonForConcurrent(Reglement reglement, Concurrent concurrent) {
		return reglement.getDistancesEtBlasonFor(concurrent.getCriteriaSet());
	}

	/**
	 * Test si 2 objet d&b sont identique
	 * 
	 * @param db
	 * @return boolean
	 */
	public boolean equals(DistancesEtBlason db) {
		return (Arrays.equals(this.distances, db.distances) && this.blason == db.blason);
	}

	@Override
	public boolean equals(Object db) {
		if(db instanceof DistancesEtBlason)
			return equals((DistancesEtBlason)db);

		return false;
	}

	@Override
	public int hashCode() {
		String strhash = blason + "";
		for(int distance : distances)
			strhash += "." + distance;
		return strhash.hashCode();
	}
}