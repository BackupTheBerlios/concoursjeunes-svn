function checkPrintable(ficheConcours, options) {
	if(ficheConcours.getConcurrentList().countArcher(options.getDepart()))
		return true;
	return false;
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
		templateXML.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(new Date()));
		templateXML.parse("producer", org.concoursjeunes.AppInfos.NOM + " " + org.concoursjeunes.AppInfos.VERSION);
		templateXML.parse("author", profile.getConfiguration().getClub().getNom());
		
		var nbVoleeParSerie = ficheConcours.getParametre().getReglement().getNbVoleeParSerie();
		var nbFlecheParVolee = ficheConcours.getParametre().getReglement().getNbFlecheParVolee();
		var targets = ficheConcours.getPasDeTir(depart).getTargets();
		var nbPositions = targets.get(0).getNbMaxArchers();
		
		var logo = profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\");
		var nomClub = ficheConcours.getParametre().getClub().getNom();
		var intituleConcours = ficheConcours.getParametre().getIntituleConcours();
		var lieuConcours = ficheConcours.getParametre().getLieuConcours();
		var dateConcours = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG).format(ficheConcours.getParametre().getDate());
		
		for(var i = 0; i < targets.size(); i++) {
			if(targets.get(i).getNbArcher() == 0)
				continue;
			templateXML.parse("scoresheet.LOGO_CLUB_URI", logo);
			templateXML.parse("scoresheet.INTITULE_CLUB", nomClub);
			templateXML.parse("scoresheet.INTITULE_CONCOURS", intituleConcours);
			templateXML.parse("scoresheet.VILLE_CLUB", lieuConcours);
			templateXML.parse("scoresheet.DATE_CONCOURS", dateConcours);
			
			var percentPositions = "";
			templateXML.parse("scoresheet.NB_POSITIONS", nbPositions);
			for(var j = 0; j < nbPositions; j++) {
				percentPositions += ";" + ((100.0/nbPositions)-1) + ";1";
			}
			percentPositions = percentPositions.substring(1);
			templateXML.parse("scoresheet.PERCENT_POSITIONS", percentPositions);
			
			for(var j = 0; j < nbPositions; j++) {
				var concurrent = targets.get(i).getConcurrentAt(j);
				if(concurrent != null) {
					templateXML.parse("scoresheet.positions.cid", concurrent.getNomArcher() + " " + concurrent.getPrenomArcher());
					templateXML.parse("scoresheet.positions.cclub", concurrent.getClub().toString());
					templateXML.parse("scoresheet.positions.clicence", concurrent.getNumLicenceArcher());
					templateXML.parse("scoresheet.positions.emplacement", new org.concoursjeunes.TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString());
				} else {
					templateXML.parse("scoresheet.positions.cid", "");
					templateXML.parse("scoresheet.positions.cclub", "");
					templateXML.parse("scoresheet.positions.clicence", "");
					templateXML.parse("scoresheet.positions.emplacement", "");
				}
				
				templateXML.parse("scoresheet.positions.SERIE_NB_COL", 3 + nbFlecheParVolee);

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
				
				for(var k = 1; k <= nbVoleeParSerie; k++) {
					templateXML.parse("scoresheet.positions.volees.NUM_VOLEE", k);
					
					for(var l = 0; l < nbFlecheParVolee; l++) {
						templateXML.loopBloc("scoresheet.positions.volees.pointsparfleche");
					}
					
					templateXML.loopBloc("scoresheet.positions.volees");
				}
				templateXML.parse("scoresheet.positions.NB_COL_TOTAL", 2 + nbFlecheParVolee);
				//templateXML.parse("scoresheet.positions.NUM_DISTANCE", getPosition(j+1));

				templateXML.loopBloc("scoresheet.positions");
				templateXML.loopBloc("scoresheet.complement");
			}
			templateXML.loopBloc("scoresheet");
		}
		
		//print(templateXML.output());
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(templateXML.output()));
	} catch (e) {
		print(e);
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