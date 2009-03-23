package org.concoursjeunes.event;

import org.concoursjeunes.Profile;

public class ApplicationCoreEvent {
	private Profile profile;
	
	public ApplicationCoreEvent(Profile profile) {
		this.profile = profile;
	}

	/**
	 * @return profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile profile à définir
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
