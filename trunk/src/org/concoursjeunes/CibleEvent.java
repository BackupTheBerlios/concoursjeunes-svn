/**
 * 
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
