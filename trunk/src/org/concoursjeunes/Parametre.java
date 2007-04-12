/**
 * Parametre d'un concours
 * 
 * @author Aurelien Jeoffray
 * @version 3.0
 */

package org.concoursjeunes;

import java.util.*;

import javax.swing.event.EventListenerList;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.configuration;

/**
 * @author  aurelien
 */
public class Parametre extends DefaultParameters {
	private Date dDateConcours		= new Date(); //$NON-NLS-1$
	private ArrayList<String> vArbitres = new ArrayList<String>();
	private Reglement reglement		= new Reglement();

	private String saveName         = System.currentTimeMillis()
			+ ajrParametreAppli.getResourceString("extention.concours"); //$NON-NLS-1$
	
	private boolean reglementLock = false;
	
	private EventListenerList listeners = new EventListenerList();

	public Parametre() {
		
	}
	
	public Parametre(Configuration configuration) {
		setClub(configuration.getClub());
		setIntituleConcours(configuration.getIntituleConcours());
		setNbCible(configuration.getNbCible());
		setNbTireur(configuration.getNbTireur());
		setNbDepart(configuration.getNbDepart());
		setReglement(ReglementFactory.getReglement(configuration.getReglementName()));
	}
	
	public void addParametreListener(ParametreListener parametreListener) {
		listeners.add(ParametreListener.class, parametreListener);
	}
	
	public void removeParametreListener(ParametreListener parametreListener) {
		listeners.add(ParametreListener.class, parametreListener);
	}
	
	/**
	 * Donne la date du concours
	 * 
	 * @return Date - la date du concours;
	 */
	public Date getDate() {
		return dDateConcours;
	}

	/**
	 * Donne la liste des arbitres
	 * 
	 * @return la liste des arbitres
	 */
	public ArrayList<String> getArbitres() {
		return vArbitres;
	}

	/**
	 * Donne le chemin du fichier de sauvegarde du concours
	 * @return  String
	 * @uml.property  name="saveName"
	 */
	public String getSaveName() {
		return saveName;
	}

	/**
	 * specifie la date du concours
	 * 
	 * @param sDateConcours
	 */
	public void setDate(Date dDateConcours) {
		this.dDateConcours = dDateConcours;
		fireMetaDataChanged();
	}

	/**
	 * specifie la liste des arbitres
	 * 
	 * @param vArbitres
	 */
	public void setArbitres(ArrayList<String> vArbitres) {
		this.vArbitres = vArbitres;
		fireParametreChanged();
	}
	
	@Override
	public void setIntituleConcours(String intituleConcours) {
		super.setIntituleConcours(intituleConcours);
		fireMetaDataChanged();
	}

	/**
	 * specifie le nom de sauvegarde des infos du concours
	 * @param  saveName
	 * @uml.property  name="saveName"
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
		fireMetaDataChanged();
	}
	
	/**
	 * @return the reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * @param reglement the reglement to set
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
		fireParametreChanged();
	}

	/**
	 * @return the reglementLock
	 */
	public boolean isReglementLock() {
		return reglementLock;
	}

	/**
	 * @param reglementLock the reglementLock to set
	 */
	public void setReglementLock(boolean reglementLock) {
		this.reglementLock = reglementLock;
	}

	private void fireMetaDataChanged() {
		for(ParametreListener pl : listeners.getListeners(ParametreListener.class)) {
			pl.metaDataChanged(new ParametreEvent(this));
		}
	}
	
	private void fireParametreChanged() {
		for(ParametreListener pl : listeners.getListeners(ParametreListener.class)) {
			pl.parametreChanged(new ParametreEvent(this));
		}
	}
}