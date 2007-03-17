package org.concoursjeunes;

import java.io.File;

import ajinteractive.standard.java2.AJToolKit;

public class ReglementFactory {
	/**
	 * Cré un nouveau reglement de concours
	 * 
	 * @return le reglement créer
	 */
	public static Reglement createReglement() {
		return new Reglement();
	}
	
	/**
	 * Retourne le reglement qualifié par son nom
	 * 
	 * @param reglementName - le nom du reglement à retourné
	 * @return - le reglement retourné
	 */
	public static Reglement getReglement(String reglementName) {

		Reglement reglement = null;
		
		File fReglement = null;
		
		File fOfficialReglement = new File("config" + File.separator + "reglement" 
				+ File.separator + "reglement_" + reglementName + ".xml");
		if(fOfficialReglement.exists()) {
			fReglement = fOfficialReglement;
		} else {
			File fUserReglement = new File(ConcoursJeunes.userRessources.getConfigPathForUser() 
					+ File.separator + "reglement_" + reglementName + ".xml");
			if(fUserReglement.exists())
				fReglement = fUserReglement;
		}
		
		if(fReglement != null) {
			reglement = (Reglement)AJToolKit.loadXMLStructure(fReglement, true);
		}
		
		return reglement;
	}
}
