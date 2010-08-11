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
	
	writer.setPageEvent(new org.concoursjeunes.state.PageFooter());

	var listeArcherXML = new org.ajdeveloppement.commons.AJTemplate();
	listeArcherXML.setLocalisationReader(localeReader);
	listeArcherXML.loadTemplate(template);
	
	try {
		listeArcherXML.parse("NB_PARTICIPANTS", "" + ficheConcours.getConcurrentList().countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
		listeArcherXML.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(new java.util.Date())); //$NON-NLS-1$

		var concurrents = org.concoursjeunes.ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), org.concoursjeunes.ConcurrentList.SortCriteria.SORT_BY_NAME);

		for (var i = 0; i < concurrents.size(); i++) {
			listeArcherXML.parse("lignes.IDENTITEE", concurrents.get(i).getFullName()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.CLUB", concurrents.get(i).getClub().toString()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.NUM_LICENCE", concurrents.get(i).getNumLicenceArcher()); //$NON-NLS-1$

			var listCriteria = ficheConcours.getParametre().getReglement().getListCriteria();
			var catStr = "";
			for (var j = 0; j < listCriteria.size(); j++) {
				var key = listCriteria.get(j);
				catStr += concurrents.get(i).getCriteriaSet().getCriterionElement(key).getCode();
			}
			listeArcherXML.parse("lignes.categorie", catStr);

			listeArcherXML.parse("lignes.PAYEE", org.ajdeveloppement.commons.StringUtils.tokenize(localeReader.getResourceString("state.inscription"), ",")[concurrents.get(i).getInscription()]);
			listeArcherXML.parse("lignes.CERTIFICAT", org.ajdeveloppement.commons.StringUtils.tokenize(localeReader.getResourceString("state.certificat"), ",")[concurrents.get(i).isCertificat() ? 0 : 1]);
			listeArcherXML.parse("lignes.CIBLE", new org.concoursjeunes.TargetPosition(concurrents.get(i).getCible(), concurrents.get(i).getPosition()).toString());

			listeArcherXML.loopBloc("lignes");
		}
		
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(listeArcherXML.output()));
	} catch(e) {
		print(e);
	}
}