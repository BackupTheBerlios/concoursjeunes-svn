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
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						Packages.java.util,
						java.io.StringReader);
	
	with(contexte) {
		var depart = 0;
	
		var listeArcherXML = new AJTemplate();
		listeArcherXML.setLocalisationReader(localeReader);
		listeArcherXML.loadTemplate(template);
		
		try {
			listeArcherXML.parse("NB_PARTICIPANTS", "" + ficheConcours.getConcurrentList().countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
			listeArcherXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
	
			var concurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS);
	
			for (var i = 0; i < concurrents.length; i++) {
				listeArcherXML.parse("lignes.IDENTITEE", concurrents[i].getID()); //$NON-NLS-1$
				listeArcherXML.parse("lignes.CLUB", concurrents[i].getClub().getNom()); //$NON-NLS-1$
				listeArcherXML.parse("lignes.NUM_LICENCE", concurrents[i].getNumLicenceArcher()); //$NON-NLS-1$
	
				var listCriteria = ficheConcours.getParametre().getReglement().getListCriteria();
				for (var j = 0; j < listCriteria.size(); j++) {
					var key = listCriteria.get(j);
					listeArcherXML.parse("lignes." + key.getCode(), //$NON-NLS-1$
							concurrents[i].getCriteriaSet().getCriterionElement(key).getCode());
				}
	
				listeArcherXML.parse("lignes.PAYEE", AJToolKit.tokenize(ApplicationCore.ajrLibelle.getResourceString("concurrent.impression.inscription"), ",")[concurrents[i].getInscription()]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				listeArcherXML.parse("lignes.CERTIFICAT", AJToolKit.tokenize(ApplicationCore.ajrLibelle.getResourceString("concurrent.certificat"), ",")[concurrents[i].isCertificat() ? 0 : 1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				listeArcherXML.parse("lignes.CIBLE", new TargetPosition(concurrents[i].getCible(), concurrents[i].getPosition()).toString()); //$NON-NLS-1$
	
				listeArcherXML.loopBloc("lignes"); //$NON-NLS-1$
			}
			
			XmlParser.parse(document, new StringReader(listeArcherXML.output()));
		} catch(e) {
			print(e);
		}
	}
}