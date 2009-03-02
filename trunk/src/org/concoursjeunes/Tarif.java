package org.concoursjeunes;

import java.util.ArrayList;
import java.util.List;

import org.ajdeveloppement.commons.sql.SqlPersistanceBean;

public class Tarif extends SqlPersistanceBean {
	private String intituleTarif = ""; //$NON-NLS-1$
	private double tarif = 0.0;
	private List<CriteriaSet> categoriesTarif = new ArrayList<CriteriaSet>();
	
	public Tarif() {
		
	}
	
	public Tarif(String intituleTarif, double tarif) {
		super();
		this.intituleTarif = intituleTarif;
		this.tarif = tarif;
	}

	/**
	 * @return intituleTarif
	 */
	public String getIntituleTarif() {
		return intituleTarif;
	}

	/**
	 * @param intituleTarif intituleTarif à définir
	 */
	public void setIntituleTarif(String intituleTarif) {
		this.intituleTarif = intituleTarif;
	}

	/**
	 * @return tarif
	 */
	public double getTarif() {
		return tarif;
	}

	/**
	 * @param tarif tarif à définir
	 */
	public void setTarif(double tarif) {
		this.tarif = tarif;
	}

	/**
	 * @return categoriesTarif
	 */
	public List<CriteriaSet> getCategoriesTarif() {
		return categoriesTarif;
	}

	/**
	 * @param categoriesTarif categoriesTarif à définir
	 */
	public void setCategoriesTarif(List<CriteriaSet> categoriesTarif) {
		this.categoriesTarif = categoriesTarif;
	}
}
