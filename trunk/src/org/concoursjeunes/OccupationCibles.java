/**
 * 
 */
package org.concoursjeunes;

/**
 * @author  aurelien
 */
public class OccupationCibles {
	private int placeLibre = 0;
	private int placeOccupe = 0;
	
	/**
	 * @param placeLibre
	 * @param placeOccupe
	 */
	public OccupationCibles(int placeLibre, int placeOccupe) {
		this.placeLibre = placeLibre;
		this.placeOccupe = placeOccupe;
	}

	/**
	 * @return  placeLibre
	 * @uml.property  name="placeLibre"
	 */
	public int getPlaceLibre() {
		return placeLibre;
	}

	/**
	 * @param placeLibre  placeLibre à définir
	 * @uml.property  name="placeLibre"
	 */
	public void setPlaceLibre(int placeLibre) {
		this.placeLibre = placeLibre;
	}


	/**
	 * @return  placeOccupe
	 * @uml.property  name="placeOccupe"
	 */
	public int getPlaceOccupe() {
		return placeOccupe;
	}

	/**
	 * @param placeOccupe  placeOccupe à définir
	 * @uml.property  name="placeOccupe"
	 */
	public void setPlaceOccupe(int placeOccupe) {
		this.placeOccupe = placeOccupe;
	}
	
	
}
