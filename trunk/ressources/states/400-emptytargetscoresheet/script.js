//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours) {
	return true;
}

function printState(ficheConcours, template, document, writer) {
	var contexte = JavaImporter(
						Packages.org.concoursjeunes,
						Packages.org.ajdeveloppement.commons,
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
			templateXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
			templateXML.parse("producer", ApplicationCore.NOM + " " + ApplicationCore.VERSION);
			templateXML.parse("author", ApplicationCore.getConfiguration().getClub().getNom());

			templateXML.parse("scoresheet.LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
			templateXML.parse("scoresheet.INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom());
			templateXML.parse("scoresheet.INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours());
			templateXML.parse("scoresheet.VILLE_CLUB", ficheConcours.getParametre().getLieuConcours());
			templateXML.parse("scoresheet.DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDate()));
			
			var nbPositions = ficheConcours.getParametre().getNbTireur();
			var percentPositions = "";
			templateXML.parse("scoresheet.NB_POSITIONS", nbPositions);
			for(var j = 0; j < nbPositions; j++) {
				percentPositions += ";" + ((100.0/nbPositions)-1) + ";1";
			}
			percentPositions = percentPositions.substring(1);
			templateXML.parse("scoresheet.PERCENT_POSITIONS", percentPositions);
			
			for(var j = 0; j < nbPositions; j++) {
				
				templateXML.parse("scoresheet.positions.cid", "");
				templateXML.parse("scoresheet.positions.cclub", "");
				templateXML.parse("scoresheet.positions.clicence", "");
				templateXML.parse("scoresheet.positions.emplacement", "");
				
				var nbFlecheParVolee = ficheConcours.getParametre().getReglement().getNbFlecheParVolee();
				//var strDistance = getPosition(k+1) + " distance, " + ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(concurrent.getCriteriaSet().getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).getDistance()[k]+"m";
				templateXML.parse("scoresheet.positions.SERIE_NB_COL", 3 + nbFlecheParVolee);
				//templateXML.parse("scoresheet.positions.INTITULE_SERIE", strDistance);
				var colsSize = "";
				for(var k = 0; k < 3 + nbFlecheParVolee; k++)
					colsSize += ";" + (100.0 / (3 + nbFlecheParVolee));
				colsSize = colsSize.substring(1);
				templateXML.parse("scoresheet.positions.COLS_SIZE", colsSize);
				templateXML.parse("scoresheet.positions.NB_FLECHE_PAR_VOLEE", nbFlecheParVolee);
				for(var k = 1; k <= nbFlecheParVolee; k++) {
					templateXML.parse("scoresheet.positions.fleches.NUM_FLECHE", k);
					
					templateXML.loopBloc("scoresheet.positions.fleches");
				}
				
				for(var k = 1; k <= ficheConcours.getParametre().getReglement().getNbVoleeParSerie(); k++) {
					templateXML.parse("scoresheet.positions.volees.NUM_VOLEE", k);
					
					for(var l = 0; l < ficheConcours.getParametre().getReglement().getNbFlecheParVolee(); l++) {
						templateXML.loopBloc("scoresheet.positions.volees.pointsparfleche");
					}
					
					templateXML.loopBloc("scoresheet.positions.volees");
				}
				templateXML.parse("scoresheet.positions.NB_COL_TOTAL", 2 + nbFlecheParVolee);
				//templateXML.parse("scoresheet.positions.NUM_DISTANCE", getPosition(j+1));

				templateXML.loopBloc("scoresheet.positions");
				templateXML.loopBloc("scoresheet.complement");
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