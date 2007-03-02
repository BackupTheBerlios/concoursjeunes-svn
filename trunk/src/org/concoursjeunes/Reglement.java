/**
 * 
 */
package org.concoursjeunes;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author aurelien
 *
 */
public class Reglement {
	private int nbSerie             = 2;
	private int nbVoleeParSerie     = 6;
	private int nbFlecheParVolee    = 3;
	private int nbMembresEquipe     = 4;
	private int nbMembresRetenu     = 3;

	private ArrayList<Criterion> listCriteria = new ArrayList<Criterion>();
	private Hashtable<CriteriaSet, DistancesEtBlason> correspondanceCriteriaSet_DB = new Hashtable<CriteriaSet, DistancesEtBlason>();
	
	private boolean officialReglement = false;
	
	public Reglement() {
		
	}

	/**
	 * @return correspondanceCriteriaSet_DB
	 */
	public Hashtable<CriteriaSet, DistancesEtBlason> getCorrespondanceCriteriaSet_DB() {
		return correspondanceCriteriaSet_DB;
	}
	
	/**
	 * @param criteriaSet
	 * @return
	 */
	public DistancesEtBlason getCorrespondanceCriteriaSet_DB(CriteriaSet criteriaSet) {
		return correspondanceCriteriaSet_DB.get(criteriaSet);
	}

	/**
	 * @param correspondanceCriteriaSet_DB correspondanceCriteriaSet_DB à définir
	 */
	public void setCorrespondanceCriteriaSet_DB(
			Hashtable<CriteriaSet, DistancesEtBlason> correspondanceDifferentiationCriteria_DB) {
		this.correspondanceCriteriaSet_DB = correspondanceDifferentiationCriteria_DB;
	}
	
	/**
	 * 
	 * @param criteriaSet
	 * @param distancesEtBlason
	 */
	public void putCorrespondanceCriteriaSet_DB(CriteriaSet criteriaSet, DistancesEtBlason distancesEtBlason) {
		this.correspondanceCriteriaSet_DB.put(criteriaSet, distancesEtBlason);
	}

	/**
	 * @return listCriteria
	 */
	public ArrayList<Criterion> getListCriteria() {
		return listCriteria;
	}

	/**
	 * @param listCriteria listCriteria à définir
	 */
	public void setListCriteria(ArrayList<Criterion> listCriteria) {
		this.listCriteria = listCriteria;
	}
	
	/**
	 * Renvoi la politique de placement
	 * 
	 * @return Hashtable<String, Boolean> Renvoi le filtre de critere en place pour le placement des archers
	 */
	public Hashtable<Criterion, Boolean> getPlacementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for(Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isPlacement());
		}

		return filterCriteria;
	}

	/**
	 * @return nbFlecheParVolee
	 */
	public int getNbFlecheParVolee() {
		return nbFlecheParVolee;
	}

	/**
	 * @param nbFlecheParVolee nbFlecheParVolee à définir
	 */
	public void setNbFlecheParVolee(int nbFlecheParVolee) {
		this.nbFlecheParVolee = nbFlecheParVolee;
	}

	/**
	 * @return nbMembresEquipe
	 */
	public int getNbMembresEquipe() {
		return nbMembresEquipe;
	}

	/**
	 * @param nbMembresEquipe nbMembresEquipe à définir
	 */
	public void setNbMembresEquipe(int nbMembresEquipe) {
		this.nbMembresEquipe = nbMembresEquipe;
	}

	/**
	 * @return nbMembresRetenu
	 */
	public int getNbMembresRetenu() {
		return nbMembresRetenu;
	}

	/**
	 * @param nbMembresRetenu nbMembresRetenu à définir
	 */
	public void setNbMembresRetenu(int nbMembresRetenu) {
		this.nbMembresRetenu = nbMembresRetenu;
	}

	/**
	 * @return nbSerie
	 */
	public int getNbSerie() {
		return nbSerie;
	}

	/**
	 * @param nbSerie nbSerie à définir
	 */
	public void setNbSerie(int nbSerie) {
		this.nbSerie = nbSerie;
	}

	/**
	 * @return nbVoleeParSerie
	 */
	public int getNbVoleeParSerie() {
		return nbVoleeParSerie;
	}

	/**
	 * @param nbVoleeParSerie nbVoleeParSerie à définir
	 */
	public void setNbVoleeParSerie(int nbVoleeParSerie) {
		this.nbVoleeParSerie = nbVoleeParSerie;
	}

	/**
	 * @return officialReglement
	 */
	public boolean isOfficialReglement() {
		return officialReglement;
	}

	/**
	 * @param officialReglement officialReglement à définir
	 */
	public void setOfficialReglement(boolean officialReglement) {
		this.officialReglement = officialReglement;
	}
	
	public boolean isValidScore(ArrayList<Integer> scores) {
		boolean valid = true;
		for(int score : scores) {
			if(score > nbVoleeParSerie * nbFlecheParVolee * 10) {
				valid = false;
				break;
			}
		}
		return valid;
	}
}
