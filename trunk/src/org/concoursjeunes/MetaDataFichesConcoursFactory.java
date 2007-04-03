/**
 * 
 */
package org.concoursjeunes;

import java.io.File;

import ajinteractive.standard.java2.AJToolKit;
import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;
import static org.concoursjeunes.ConcoursJeunes.configuration;

/**
 * @author Aurélien JEOFFRAY - Fiducial Informatique
 *
 */
public class MetaDataFichesConcoursFactory {
	public static MetaDataFichesConcours getMetaDataFichesConcours() {
		MetaDataFichesConcours metaDataFichesConcours = (MetaDataFichesConcours)AJToolKit.loadXMLStructure(
				new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + 
						ajrParametreAppli.getResourceString("file.metadatafichesconcours")), false);
		if(metaDataFichesConcours == null)
			metaDataFichesConcours = MetaDataFichesConcours.getInstance();
		
		return metaDataFichesConcours;
	}
}
