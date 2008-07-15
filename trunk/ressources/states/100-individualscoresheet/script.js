//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours) {
	if(ficheConcours.getConcurrentList().countArcher(depart))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer) {
	var contexte = JavaImporter(
						Packages.org.concoursjeunes,
						Packages.ajinteractive.standard.common,
						Packages.com.lowagie.text,
						Packages.java.util,
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						java.util.logging.Level,
						java.io.StringReader,
						org.jdesktop.swingx.error.ErrorInfo,
						org.jdesktop.swingx.error.JXErrorPane);
	
	with(contexte) {
		var templateXML = new AJTemplate();
		templateXML.setLocalisationReader(localeReader);
		templateXML.loadTemplate(template);
	
		try {
			templateXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			templateXML.parse("producer", ApplicationCore.NOM + " " + ApplicationCore.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			templateXML.parse("author", ApplicationCore.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			
			var concurrents = ficheConcours.getConcurrentList().list(depart);
			for(var i = 0; i < concurrents.length; i++) {
				templateXML.parse("scoresheet.LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
				templateXML.parse("scoresheet.INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$
				templateXML.parse("scoresheet.INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours()); //$NON-NLS-1$
				templateXML.parse("scoresheet.VILLE_CLUB", ficheConcours.getParametre().getLieuConcours()); //$NON-NLS-1$
				templateXML.parse("scoresheet.DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDate())); //$NON-NLS-1$
				
				templateXML.parse("scoresheet.cid", concurrents[i].getNomArcher() + " " + concurrents[i].getPrenomArcher());
				templateXML.parse("scoresheet.cclub", concurrents[i].getClub().getNom());
				templateXML.parse("scoresheet.clicence", concurrents[i].getNumLicenceArcher());
				templateXML.parse("scoresheet.emplacement", new TargetPosition(concurrents[i].getCible(), concurrents[i].getPosition()).toString());
				
				var nbSerie = ficheConcours.getParametre().getReglement().getNbSerie();
				var colsSeriesSize = "";
				for(var j = 0; j < nbSerie; j++)
					colsSeriesSize += ";" + ((100.0 / nbSerie)-1) + ";1";
				colsSeriesSize = colsSeriesSize.substring(1);
				
				templateXML.parse("scoresheet.NB_SERIE", nbSerie);
				templateXML.parse("scoresheet.PERCENT_SERIES", colsSeriesSize);
				
				var nbFlecheParVolee = ficheConcours.getParametre().getReglement().getNbFlecheParVolee();
				for(var j = 0; j < nbSerie; j++) {
					var strDistance = getPosition(j+1) + " distance, " + ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(concurrents[i].getCriteriaSet().getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).getDistance()[j]+"m";
					templateXML.parse("scoresheet.series.SERIE_NB_COL", 6 + nbFlecheParVolee);
					templateXML.parse("scoresheet.series.INTITULE_SERIE", strDistance);
					var colsSize = "";
					for(var k = 0; k < 6 + nbFlecheParVolee; k++)
						colsSize += ";" + (100.0 / (6 + nbFlecheParVolee));
					colsSize = colsSize.substring(1);
					templateXML.parse("scoresheet.series.COLS_SIZE", colsSize);
					templateXML.parse("scoresheet.series.NB_FLECHE_PAR_VOLEE", nbFlecheParVolee);
					for(var k = 1; k <= nbFlecheParVolee; k++) {
						templateXML.parse("scoresheet.series.fleches.NUM_FLECHE", k);
						
						templateXML.loopBloc("scoresheet.series.fleches");
					}
					
					for(var k = 1; k <= ficheConcours.getParametre().getReglement().getNbVoleeParSerie(); k++) {
						templateXML.parse("scoresheet.series.volees.NUM_VOLEE", k);
						
						for(var l = 0; l < ficheConcours.getParametre().getReglement().getNbFlecheParVolee(); l++) {
							templateXML.loopBloc("scoresheet.series.volees.pointsparfleche");
						}
						
						templateXML.loopBloc("scoresheet.series.volees");
					}
					templateXML.parse("scoresheet.series.NB_COL_TOTAL", 2 + nbFlecheParVolee);
					templateXML.parse("scoresheet.series.NUM_DISTANCE", getPosition(j+1));

					templateXML.loopBloc("scoresheet.series");
				}
				
				var colSize = 100.0 / (6 + nbFlecheParVolee);
				templateXML.parse("scoresheet.COLS_SIZE", (colSize * (nbFlecheParVolee + 2)) + ";" + colSize + ";" + colSize + ";" + colSize + ";" + colSize);
				
				for(var j = 0; j < nbSerie; j++) {
					templateXML.parse("scoresheet.distances.NB_COL_TOTAL", 2 + nbFlecheParVolee);
					templateXML.parse("scoresheet.distances.NUM_DISTANCE", getPosition(j+1));
					
					templateXML.loopBloc("scoresheet.distances");
				}
				templateXML.parse("scoresheet.NB_COL_TOTAL", 2 + nbFlecheParVolee);
				templateXML.parse("scoresheet.SIGNATURE_SIZE", nbSerie + 1);
				
				templateXML.loopBloc("scoresheet");
			}
			
			//print(templateXML.output());
			XmlParser.parse(document, new StringReader(templateXML.output()));
		} catch (e) {
			print(e);
		}
	}
}

function getPosition(num) {
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