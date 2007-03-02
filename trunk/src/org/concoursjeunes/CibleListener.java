/**
 * 
 */
package org.concoursjeunes;

import java.util.EventListener;

/**
 * @author aurelien
 *
 */
public interface CibleListener extends EventListener {
	public void concurrentJoined(CibleEvent e);
	public void concurrentQuit(CibleEvent e);
}
