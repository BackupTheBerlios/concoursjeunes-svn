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

import ajinteractive.standard.java2.AJToolKit;
import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
import static org.concoursjeunes.ConcoursJeunes.userRessources;
import static org.concoursjeunes.ConcoursJeunes.configuration;

/**
 * @author Aurélien JEOFFRAY
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
