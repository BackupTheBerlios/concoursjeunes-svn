function checkPrintable(ficheConcours, options) {
	return true;
}

function printState(ficheConcours, template, document, writer, options) {
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	var templateXML = new org.ajdeveloppement.commons.AJTemplate();
	templateXML.setLocalisationReader(localeReader);
	templateXML.loadTemplate(template);

	try {
		templateXML.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(new java.util.Date())); //$NON-NLS-1$
		templateXML.parse("producer", org.concoursjeunes.AppInfos.NOM + " " + org.concoursjeunes.AppInfos.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
		templateXML.parse("author", profile.getConfiguration().getClub().getNom()); //$NON-NLS-1$	

		templateXML.parse("scoresheet.LOGO_CLUB_URI", profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
		templateXML.parse("scoresheet.INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$
		templateXML.parse("scoresheet.INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours()); //$NON-NLS-1$
		templateXML.parse("scoresheet.VILLE_CLUB", ficheConcours.getParametre().getLieuConcours()); //$NON-NLS-1$
		templateXML.parse("scoresheet.DATE_CONCOURS", java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG).format(ficheConcours.getParametre().getDate())); //$NON-NLS-1$
		
		var nbSerie = ficheConcours.getParametre().getReglement().getNbSerie();
		var colsSeriesSize = "";
		for(var j = 0; j < nbSerie; j++)
			colsSeriesSize += ";" + ((100.0 / nbSerie)-1) + ";1";
		colsSeriesSize = colsSeriesSize.substring(1);
		
		templateXML.parse("scoresheet.NB_SERIE", nbSerie);
		templateXML.parse("scoresheet.PERCENT_SERIES", colsSeriesSize);
			
		var nbFlecheParVolee = ficheConcours.getParametre().getReglement().getNbFlecheParVolee();
		for(var j = 0; j < nbSerie; j++) {
			var strDistance = getPosition(j+1, localeReader) + " distance";
			templateXML.parse("scoresheet.series.SERIE_NB_COL", 5 + nbFlecheParVolee);
			templateXML.parse("scoresheet.series.INTITULE_SERIE", strDistance);
			var colsSize = "";
			for(var k = 0; k < 5 + nbFlecheParVolee; k++)
				colsSize += ";" + (100.0 / (5 + nbFlecheParVolee));
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
			templateXML.parse("scoresheet.series.NUM_DISTANCE", getPosition(j+1, localeReader));

			templateXML.loopBloc("scoresheet.series");
		}
		
		var colSize = 100.0 / (5 + nbFlecheParVolee);
		templateXML.parse("scoresheet.COLS_SIZE", (colSize * (nbFlecheParVolee + 2)) + ";" + colSize + ";" + colSize + ";" + colSize);
		
		for(var j = 0; j < nbSerie; j++) {
			templateXML.parse("scoresheet.distances.NB_COL_TOTAL", 2 + nbFlecheParVolee);
			templateXML.parse("scoresheet.distances.NUM_DISTANCE", getPosition(j+1, localeReader));
			
			templateXML.loopBloc("scoresheet.distances");
		}
		templateXML.parse("scoresheet.NB_COL_TOTAL", 2 + nbFlecheParVolee);
		templateXML.parse("scoresheet.SIGNATURE_SIZE", nbSerie + 2);
		
		//print(templateXML.output());
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(templateXML.output()));
	} catch (e) {
		print(e);
	}
}

function getPosition(num, localeReader) {
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