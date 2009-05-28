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

/**
 * @author Aurélien JEOFFRAY
 */
public class TargetsOccupation {
	private int placeLibre = 0;
	private int placeOccupe = 0;
	
	/**
	 * @param placeLibre
	 * @param placeOccupe
	 */
	public TargetsOccupation(int placeLibre, int placeOccupe) {
		this.placeLibre = placeLibre;
		this.placeOccupe = placeOccupe;
	}

	/**
	 * @return  placeLibre
	 */
	public int getPlaceLibre() {
		return placeLibre;
	}

	/**
	 * @param placeLibre  placeLibre à définir
	 */
	public void setPlaceLibre(int placeLibre) {
		this.placeLibre = placeLibre;
	}


	/**
	 * @return  placeOccupe
	 */
	public int getPlaceOccupe() {
		return placeOccupe;
	}

	/**
	 * @param placeOccupe  placeOccupe à définir
	 */
	public void setPlaceOccupe(int placeOccupe) {
		this.placeOccupe = placeOccupe;
	}
	
	
}
