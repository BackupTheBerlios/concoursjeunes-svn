/**
 * 
 */
package org.concoursjeunes;

import java.util.EventListener;

/**
 * @author aurelien
 *
 */
public interface ParametreListener extends EventListener {
	public void metaDataChanged(ParametreEvent parametreEvent);
	public void parametreChanged(ParametreEvent parametreEvent);
}
