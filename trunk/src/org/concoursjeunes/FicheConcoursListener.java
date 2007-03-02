package org.concoursjeunes;

import java.util.EventListener;

public interface FicheConcoursListener extends EventListener {
	public void listConcurrentChanged(FicheConcoursEvent e);
}
