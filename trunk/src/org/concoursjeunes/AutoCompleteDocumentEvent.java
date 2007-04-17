/**
 * Créer le 20 déc. 06 - 17:17:57
 */
package org.concoursjeunes;

/**
 * L'événement de réponse à l'action de l'AutoCompleteDocument
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class AutoCompleteDocumentEvent {
	private Object source;
	private Concurrent concurrent;
	private Entite entite;
	private Archer genericArcher;
	private Entite genericEntite;
	
	/**
	 * Crée un événement retournant un concurrent
	 * 
	 * @param source - l'objet swing à l'origine de l'événement
	 * @param concurrent - le concurrent retourné par l'autocomplement
	 */
	public AutoCompleteDocumentEvent(Object source, Concurrent concurrent) {
		this.source = source;
		this.concurrent = concurrent;
	}
	
	/**
	 * Crée un événement retournant une entite
	 * 
	 * @param source - l'objet swing à l'origine de l'événement
	 * @param entite - l'entite retourné par l'autocomplement
	 */
	public AutoCompleteDocumentEvent(Object source, Entite entite) {
		this.source = source;
		this.entite = entite;
	}

	/**
	 * Retourne l'objet concurrent répondant à l'événement
	 * 
	 * @return le concurrent renvoyé par l'autocomplement
	 * @uml.property  name="concurrent"
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * Définit le concurrent repondant à l'événement
	 * 
	 * @param concurrent  the concurrent to set
	 * @uml.property  name="concurrent"
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
	}

	/**
	 * @return  entite
	 * @uml.property  name="entite"
	 */
	public Entite getEntite() {
		return entite;
	}

	/**
	 * @param entite  entite à définir
	 * @uml.property  name="entite"
	 */
	public void setEntite(Entite entite) {
		this.entite = entite;
	}

	/**
	 * @return  the source
	 * @uml.property  name="source"
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source  the source to set
	 * @uml.property  name="source"
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * @return  genericArcher
	 */
	public Archer getGenericArcher() {
		return genericArcher;
	}

	/**
	 * @param genericArcher genericArcher à définir
	 */
	public void setGenericArcher(Archer genericArcher) {
		this.genericArcher = genericArcher;
	}

	/**
	 * @return genericEntite
	 */
	public Entite getGenericEntite() {
		return genericEntite;
	}

	/**
	 * @param genericEntite genericEntite à définir
	 */
	public void setGenericEntite(Entite genericEntite) {
		this.genericEntite = genericEntite;
	}
}