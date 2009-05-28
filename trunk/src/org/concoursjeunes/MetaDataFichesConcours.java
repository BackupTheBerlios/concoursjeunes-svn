/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.concoursjeunes;

import java.util.ArrayList;

/**
 * @author Aurélien JEOFFRAY
 */
public class MetaDataFichesConcours {
	
	private ArrayList<MetaDataFicheConcours> fiches = new ArrayList<MetaDataFicheConcours>();
	
	public MetaDataFichesConcours() { }

	/**
	 * @return  fiches
	 */
	public ArrayList<MetaDataFicheConcours> getFiches() {
		return fiches;
	}

	/**
	 * @param fiches  fiches à définir
	 */
	public void setFiches(ArrayList<MetaDataFicheConcours> fiches) {
		this.fiches = fiches;
	}
	
	/**
	 * 
	 * @param metaDataFicheConcours
	 */
	public void add(MetaDataFicheConcours metaDataFicheConcours) {
		fiches.add(metaDataFicheConcours);
	}
	
	/**
	 * 
	 * @param metaDataFicheConcours
	 */
	public void remove(MetaDataFicheConcours metaDataFicheConcours) {
		fiches.remove(metaDataFicheConcours);
	}
	
	public void removeAll() {
		fiches.clear();
	}
	
	/**
	 * Retourne les metadonnées de la fiche spécifié en parametre par son index 
	 * 
	 * @param index - l'index de la fiche pour laquelle récuperer les metadonnées
	 * @return les metadonnées de la fiche idiqué en parametre
	 */
	public MetaDataFicheConcours get(int index) {
		return fiches.get(index);
	}
	
	public boolean contains(MetaDataFicheConcours metaDataFicheConcours) {
		return fiches.contains(metaDataFicheConcours);
	}
}
