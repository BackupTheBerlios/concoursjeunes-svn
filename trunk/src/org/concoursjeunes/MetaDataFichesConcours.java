/**
 * 
 */
package org.concoursjeunes;

import java.io.File;
import java.util.ArrayList;

import ajinteractive.standard.java2.AJToolKit;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;
import static org.concoursjeunes.ConcoursJeunes.configuration;

/**
 * @author Aurélien JEOFFRAY
 */
public class MetaDataFichesConcours {
	
	private ArrayList<MetaDataFicheConcours> fiches = new ArrayList<MetaDataFicheConcours>();
	
	private static MetaDataFichesConcours instance = new MetaDataFichesConcours();
	
	public MetaDataFichesConcours() { }
	
	public static MetaDataFichesConcours getInstance() { return instance; }

	/**
	 * @return  fiches
	 * @uml.property  name="fiches"
	 */
	public ArrayList<MetaDataFicheConcours> getFiches() {
		return fiches;
	}

	/**
	 * @param fiches  fiches à définir
	 * @uml.property  name="fiches"
	 */
	public void setFiches(ArrayList<MetaDataFicheConcours> fiches) {
		this.fiches = fiches;
	}
	
	/**
	 * 
	 * @param metaDataFicheConcours
	 */
	public void addMetaDataFicheConcours(MetaDataFicheConcours metaDataFicheConcours) {
		fiches.add(metaDataFicheConcours);
	}
	
	/**
	 * 
	 * @param metaDataFicheConcours
	 */
	public void removeMetaDataFicheConcours(MetaDataFicheConcours metaDataFicheConcours) {
		fiches.remove(metaDataFicheConcours);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public MetaDataFicheConcours getMetaDataFicheConcours(int index) {
		return fiches.get(index);
	}
	
	public void save() {
		AJToolKit.saveXMLStructure(
				new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + 
						ajrParametreAppli.getResourceString("file.metadatafichesconcours")), this, false);
	}
}
