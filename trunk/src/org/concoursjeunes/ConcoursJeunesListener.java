/**
 * 
 */
package org.concoursjeunes;

import java.util.EventListener;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public interface ConcoursJeunesListener extends EventListener {
	public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent);
	public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent);
	public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent);
	public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent);
}
