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
 */
public class FicheConcoursEvent {
	public static final int ADD_CONCURRENT		= 1;
	public static final int REMOVE_CONCURRENT	= 2;
	
	public static final int PASDETIR_CHANGED	= 3;
	
	public static final int ALL_START			= -1;
	
	private Concurrent concurrent;
	private int depart = ALL_START;
	private int event = 0;
	
	public FicheConcoursEvent(int event, Concurrent concurrent) {
		this.event = event;
		this.concurrent = concurrent;
	}
	
	public FicheConcoursEvent(int event, int depart) {
		this.event = event;
		this.depart = depart;
	}

	/**
	 * @return  concurrent
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * @param concurrent  concurrent à définir
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
	}
	
	/**
	 * @return  depart
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * @param depart  depart à définir
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * @return  event
	 */
	public int getEvent() {
		return event;
	}

	/**
	 * @param event  event à définir
	 */
	public void setEvent(int event) {
		this.event = event;
	}
	
	
}
