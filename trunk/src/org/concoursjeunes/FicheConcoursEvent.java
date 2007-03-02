/**
 * 
 */
package org.concoursjeunes;

/**
 * @author  aurelien
 */
public class FicheConcoursEvent {
	public static final int ADD_CONCURRENT		= 1;
	public static final int REMOVE_CONCURRENT	= 2;
	
	public static final int PLACEMENT_CHANGED	= 3;
	
	private Concurrent concurrent;
	private int depart = -1;
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
	 * @uml.property  name="concurrent"
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * @param concurrent  concurrent à définir
	 * @uml.property  name="concurrent"
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
	}
	
	/**
	 * @return  depart
	 * @uml.property  name="depart"
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * @param depart  depart à définir
	 * @uml.property  name="depart"
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * @return  event
	 * @uml.property  name="event"
	 */
	public int getEvent() {
		return event;
	}

	/**
	 * @param event  event à définir
	 * @uml.property  name="event"
	 */
	public void setEvent(int event) {
		this.event = event;
	}
	
	
}
