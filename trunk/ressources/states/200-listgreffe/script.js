function checkPrintable(ficheConcours, options) {
	if(ficheConcours.getConcurrentList().countArcher(options.getDepart()))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer, options) {
	var contexte = JavaImporter(
						Packages.org.concoursjeunes,
						Packages.org.ajdeveloppement.commons,
						Packages.com.lowagie.text,
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						Packages.java.util,
						java.io.StringReader);
	
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	with(contexte) {
		//var depart = 0;
	
		var listeArcherXML = new AJTemplate();
		listeArcherXML.setLocalisationReader(localeReader);
		listeArcherXML.loadTemplate(template);
		
		try {
			listeArcherXML.parse("NB_PARTICIPANTS", "" + ficheConcours.getConcurrentList().countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
			listeArcherXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
	
			var concurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_NAME);
	
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
	
				listeArcherXML.parse("lignes.PAYEE", StringUtils.tokenize(localeReader.getResourceString("state.inscription"), ",")[concurrents.get(i).getInscription()]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				listeArcherXML.parse("lignes.CERTIFICAT", StringUtils.tokenize(localeReader.getResourceString("state.certificat"), ",")[concurrents.get(i).isCertificat() ? 0 : 1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				listeArcherXML.parse("lignes.CIBLE", new TargetPosition(concurrents.get(i).getCible(), concurrents.get(i).getPosition()).toString()); //$NON-NLS-1$
	
				listeArcherXML.loopBloc("lignes"); //$NON-NLS-1$
			}
			
			XmlParser.parse(document, new StringReader(listeArcherXML.output()));
		} catch(e) {
			print(e);
		}
	}
}