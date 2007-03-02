/**
 * Créer le 20 déc. 06 - 17:17:57
 */
package org.concoursjeunes;

/**
 * @author  Aurelien
 */
public class AutoCompleteDocumentEvent {
	private Object source;
	private Concurrent concurrent;
	private Entite entite;
	private String sqlfilter = "";
	
	/**
	 * @param source
	 * @param concurrent
	 */
	public AutoCompleteDocumentEvent(Object source, Concurrent concurrent) {
		this.source = source;
		this.concurrent = concurrent;
	}
	
	public AutoCompleteDocumentEvent(Object source, Entite entite) {
		this.source = source;
		this.entite = entite;
	}

	/**
	 * @return  the concurrent
	 * @uml.property  name="concurrent"
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
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
	 * @return  sqlfilter
	 * @uml.property  name="sqlfilter"
	 */
	public String getSqlfilter() {
		return sqlfilter;
	}

	/**
	 * @param sqlfilter  sqlfilter à définir
	 * @uml.property  name="sqlfilter"
	 */
	public void setSqlfilter(String sqlfilter) {
		this.sqlfilter = sqlfilter;
	}
}