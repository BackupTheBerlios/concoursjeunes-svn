package org.concoursjeunes;

import java.util.EventListener;

/**
 * @author Aurélien JEOFFRAY
 */
public interface FicheConcoursListener extends EventListener {
	public void listConcurrentChanged(FicheConcoursEvent e);
}
