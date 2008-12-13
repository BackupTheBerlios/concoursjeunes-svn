package org.concoursjeunes.event;

import java.util.EventListener;

public interface ApplicationCoreListener extends EventListener {
	public void databaseVersionError(ApplicationCoreEvent e);
	
}
