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

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class CibleEvent {
	private Concurrent concurrent;
	private Cible cible;
	
	/**
	 * @param concurrent
	 * @param cible
	 */
	public CibleEvent(Concurrent concurrent, Cible cible) {
		this.concurrent = concurrent;
		this.cible = cible;
	}

	/**
	 * @return concurrent
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * @param concurrent concurrent à définir
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
	}

	/**
	 * @return cible
	 */
	public Cible getCible() {
		return cible;
	}

	/**
	 * @param cible cible à définir
	 */
	public void setCible(Cible cible) {
		this.cible = cible;
	}
}
