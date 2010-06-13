function checkPrintable(ficheConcours, options) {
	var depart = options.getDepart();
	if(ficheConcours.getConcurrentList().countArcher(depart))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer, options) {
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();

	//var depart = 0;
	var listeArcherXML = new org.ajdeveloppement.commons.AJTemplate();
	listeArcherXML.setLocalisationReader(localeReader);
	listeArcherXML.loadTemplate(template);
	
	try {
		listeArcherXML.parse("NB_PARTICIPANTS", "" + ficheConcours.getConcurrentList().countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
		listeArcherXML.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(new java.util.Date())); //$NON-NLS-1$

		var concurrents = org.concoursjeunes.ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), org.concoursjeunes.ConcurrentList.SortCriteria.SORT_BY_NAME);

		for (var i = 0; i < concurrents.size(); i++) {
			listeArcherXML.parse("lignes.IDENTITEE", concurrents.get(i).getID()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.CLUB", concurrents.get(i).getClub().toString()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.NUM_LICENCE", concurrents.get(i).getNumLicenceArcher()); //$NON-NLS-1$

			var listCriteria = ficheConcours.getParametre().getReglement().getListCriteria();
			var catStr = "";
			for (var j = 0; j < listCriteria.size(); j++) {
				var key = listCriteria.get(j);
				catStr += concurrents.get(i).getCriteriaSet().getCriterionElement(key).getCode();
			}
			listeArcherXML.parse("lignes.categorie", catStr);

			//listeArcherXML.parse("lignes.PAYEE", AJToolKit.tokenize(localeReader.getResourceString("state.inscription"), ",")[concurrents.get(i).getInscription()]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			//listeArcherXML.parse("lignes.CERTIFICAT", AJToolKit.tokenize(localeReader.getResourceString("state.certificat"), ",")[concurrents.get(i).isCertificat() ? 0 : 1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			listeArcherXML.parse("lignes.CIBLE", new org.concoursjeunes.TargetPosition(concurrents.get(i).getCible(), concurrents.get(i).getPosition()).toString()); //$NON-NLS-1$

			listeArcherXML.loopBloc("lignes"); //$NON-NLS-1$
		}
		
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(listeArcherXML.output()));
	} catch(e) {
		print(e);
	}
}