/*
 * Créer le 5 sept. 2008 à 12:12:53 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
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
package org.concoursjeunes.state;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.DateFormat;

import org.ajdeveloppement.commons.AJTemplate;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.AppInfos;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.TargetPosition;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.XmlParser;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@SuppressWarnings("nls")
public class IndividualScoreSheetState {
	private AjResourcesReader localeReader;
	private int depart;
	//private int serie;
	
	public IndividualScoreSheetState(AjResourcesReader localeReader, int depart, int serie) {
		this.localeReader = localeReader;
		this.depart = depart;
		//this.serie = serie;
	}
	
	public void printState(FicheConcours ficheConcours, URL template, Document document, PdfWriter writer)
			throws IOException {
		AJTemplate templateXML = new AJTemplate();
		templateXML.setLocalisationReader(localeReader);
		templateXML.loadTemplate(template);
	
		try {
			templateXML.parse("producer", AppInfos.NOM + " " + AppInfos.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			templateXML.parse("author", ApplicationCore.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			
			Concurrent[] concurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS);
			for(int i = 0; i < concurrents.length; i++) {
				templateXML.parse("scoresheet.LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$
				templateXML.parse("scoresheet.INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$
				templateXML.parse("scoresheet.INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours()); //$NON-NLS-1$
				templateXML.parse("scoresheet.VILLE_CLUB", ficheConcours.getParametre().getLieuConcours()); //$NON-NLS-1$
				templateXML.parse("scoresheet.DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDateDebutConcours())); //$NON-NLS-1$
				
				templateXML.parse("scoresheet.cid", concurrents[i].getNomArcher() + " " + concurrents[i].getPrenomArcher()); //$NON-NLS-1$
				templateXML.parse("scoresheet.cclub", concurrents[i].getClub().getNom()); //$NON-NLS-1$
				templateXML.parse("scoresheet.clicence", concurrents[i].getNumLicenceArcher()); //$NON-NLS-1$
				templateXML.parse("scoresheet.emplacement", new TargetPosition(concurrents[i].getCible(), concurrents[i].getPosition()).toString()); //$NON-NLS-1$
				
				int nbSerie = ficheConcours.getParametre().getReglement().getNbSerie();
				String colsSeriesSize = ""; //$NON-NLS-1$
				for(int j = 0; j < nbSerie; j++)
					colsSeriesSize += ";" + ((100.0 / nbSerie)-1) + ";1"; //$NON-NLS-1$ //$NON-NLS-2$
				colsSeriesSize = colsSeriesSize.substring(1);
				
				templateXML.parse("scoresheet.NB_SERIE", Integer.toString(nbSerie)); //$NON-NLS-1$
				templateXML.parse("scoresheet.PERCENT_SERIES", colsSeriesSize); //$NON-NLS-1$
				
				int nbFlecheParVolee = ficheConcours.getParametre().getReglement().getNbFlecheParVolee();
				for(int j = 0; j < nbSerie; j++) {
					String strDistance = getPosition(j+1) + " distance, " + ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(concurrents[i].getCriteriaSet().getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).getDistance()[j]+"m";
					templateXML.parse("scoresheet.series.SERIE_NB_COL", Integer.toString(5 + nbFlecheParVolee));
					templateXML.parse("scoresheet.series.INTITULE_SERIE", strDistance);
					String colsSize = "";
					for(int k = 0; k < 5 + nbFlecheParVolee; k++)
						colsSize += ";" + (100.0 / (5 + nbFlecheParVolee));
					colsSize = colsSize.substring(1);
					templateXML.parse("scoresheet.series.COLS_SIZE", colsSize);
					templateXML.parse("scoresheet.series.NB_FLECHE_PAR_VOLEE", Integer.toString(nbFlecheParVolee));
					for(int k = 1; k <= nbFlecheParVolee; k++) {
						templateXML.parse("scoresheet.series.fleches.NUM_FLECHE", Integer.toString(k));
						
						templateXML.loopBloc("scoresheet.series.fleches");
					}
					
					for(int k = 1; k <= ficheConcours.getParametre().getReglement().getNbVoleeParSerie(); k++) {
						templateXML.parse("scoresheet.series.volees.NUM_VOLEE", Integer.toString(k));
						
						for(int l = 0; l < ficheConcours.getParametre().getReglement().getNbFlecheParVolee(); l++) {
							templateXML.loopBloc("scoresheet.series.volees.pointsparfleche");
						}
						
						templateXML.loopBloc("scoresheet.series.volees");
					}
					templateXML.parse("scoresheet.series.NB_COL_TOTAL", Integer.toString(2 + nbFlecheParVolee));
					templateXML.parse("scoresheet.series.NUM_DISTANCE", getPosition(j+1));

					templateXML.loopBloc("scoresheet.series");
				}
				
				double colSize = 100.0 / (5 + nbFlecheParVolee);
				templateXML.parse("scoresheet.COLS_SIZE", (colSize * (nbFlecheParVolee + 2)) + ";" + colSize + ";" + colSize + ";" + colSize);
				
				for(int j = 0; j < nbSerie; j++) {
					templateXML.parse("scoresheet.distances.NB_COL_TOTAL", Integer.toString(2 + nbFlecheParVolee));
					templateXML.parse("scoresheet.distances.NUM_DISTANCE", getPosition(j+1));
					
					templateXML.loopBloc("scoresheet.distances");
				}
				templateXML.parse("scoresheet.NB_COL_TOTAL", Integer.toString(2 + nbFlecheParVolee));
				templateXML.parse("scoresheet.SIGNATURE_SIZE", Integer.toString(nbSerie + 2));
				
				templateXML.loopBloc("scoresheet");
			}
			
			//print(templateXML.output());
			XmlParser.parse(document, new StringReader(templateXML.output()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getPosition(int num) {
		switch(num) {
			case 1:
				return localeReader.getResourceString("template.first");
			case 2:
				return localeReader.getResourceString("template.second");
			case 3:
				return localeReader.getResourceString("template.third");
			case 4:
				return localeReader.getResourceString("template.forth");
			default:
				return num + localeReader.getResourceString("template.xth");
		}
	}
}
