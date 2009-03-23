package org.concoursjeunes.event;

import java.util.EventListener;

public interface ApplicationCoreListener extends EventListener {
	//public void databaseVersionError(ApplicationCoreEvent e);
	public void profileAdded(ApplicationCoreEvent e);
	public void profileRemoved(ApplicationCoreEvent e);
}
