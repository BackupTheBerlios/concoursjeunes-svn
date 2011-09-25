package org.ajdeveloppement.concours.plugins.scriptext;

import org.ajdeveloppement.concours.Profile;
import org.ajdeveloppement.concours.ui.ArcCompetitionFrame;

public interface ScriptExtInterface {
	/**
	 * Execute les opérations de chargement du script
	 * 
	 * @param parentframe
	 * @param profile
	 */
	public void load(ArcCompetitionFrame parentframe, Profile profile);
	
	/**
	 *  Execute les opérations de déchargement du script
	 */
	public void unload();
}
