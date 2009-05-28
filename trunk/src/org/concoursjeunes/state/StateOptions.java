package org.concoursjeunes.state;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.Profile;

public class StateOptions {
	private int depart;
	private int serie;
	private AjResourcesReader langReader;
	private Profile profile;
	
	public StateOptions() {
	}
	
	public StateOptions(int depart, int serie, AjResourcesReader langReader,
			Profile profile) {
		super();
		this.depart = depart;
		this.serie = serie;
		this.langReader = langReader;
		this.profile = profile;
	}

	/**
	 * @return depart
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * @param depart depart à définir
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * @return serie
	 */
	public int getSerie() {
		return serie;
	}

	/**
	 * @param serie serie à définir
	 */
	public void setSerie(int serie) {
		this.serie = serie;
	}

	/**
	 * @return langReader
	 */
	public AjResourcesReader getLangReader() {
		return langReader;
	}

	/**
	 * @param langReader langReader à définir
	 */
	public void setLangReader(AjResourcesReader langReader) {
		this.langReader = langReader;
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
