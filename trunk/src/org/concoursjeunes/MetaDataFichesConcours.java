/**
 * 
 */
package org.concoursjeunes;

import java.util.ArrayList;

/**
 * @author  aurelien
 */
public class MetaDataFichesConcours {
	
	private ArrayList<MetaDataFicheConcours> fiches = new ArrayList<MetaDataFicheConcours>();
	
	public MetaDataFichesConcours() { }

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
}
