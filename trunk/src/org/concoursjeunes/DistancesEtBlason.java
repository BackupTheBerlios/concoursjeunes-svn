package org.concoursjeunes;

import java.util.Arrays;
/**
 * parametre de distances et blason pour une cible et un concurrent
 * @author  Aurélien Jeoffray
 * @version  1.0
 */
public class DistancesEtBlason {
	private int[] distances = new int[] {18, 18};
	private int blason = 80;

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
	 * Retourne l'objet DistancesEtBlason associé à un concurrent
	 * 
	 * @param concurrent - le concurrent pour lequel retourné l'objet
	 * @return l'objet DistancesEtBlason correspondant au concurrent
	 */
	public static DistancesEtBlason getDistancesEtBlasonForConcurrent(Reglement reglement, Concurrent concurrent) {
		return reglement.getCorrespondanceCriteriaSet_DB(concurrent.getCriteriaSet());
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