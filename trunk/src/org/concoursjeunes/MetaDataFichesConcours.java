/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
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
	 * Retourne les metadonnées de la fiche spécifié en parametre par son index 
	 * 
	 * @param index - l'index de la fiche pour laquelle récuperer les metadonnées
	 * @return les metadonnées de la fiche idiqué en parametre
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
