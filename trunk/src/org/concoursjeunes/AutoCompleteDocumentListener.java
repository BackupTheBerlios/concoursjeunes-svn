/**
 * Créer le 20 déc. 06 - 17:16:36
 */
package org.concoursjeunes;

import java.util.EventListener;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public interface AutoCompleteDocumentListener extends EventListener {
	public void concurrentFinded(AutoCompleteDocumentEvent e);
	public void concurrentNotFound(AutoCompleteDocumentEvent e);
	public void entiteFinded(AutoCompleteDocumentEvent e);
	public void entiteNotFound(AutoCompleteDocumentEvent e);
}
