//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours, options) {
	if(ficheConcours.getEquipes() != null && ficheConcours.getEquipes().countEquipes() > 0)
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer, options) {
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	var strClassementEquipe = ""; //$NON-NLS-1$

	if (ficheConcours.getEquipes() != null && ficheConcours.getEquipes().countEquipes() > 0) {
		
		writer.setPageEvent(new org.ajdeveloppement.concours.state.PageFooter());
		
		var tplClassementEquipe = new org.ajdeveloppement.commons.AJTemplate();
		tplClassementEquipe.loadTemplate(template);

		// classement sortie XML
		tplClassementEquipe.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(new java.util.Date())); //$NON-NLS-1$
		tplClassementEquipe.parse("LOGO_CLUB_URI", profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tplClassementEquipe.parse("INTITULE_CLUB", org.ajdeveloppement.commons.XmlUtils.sanitizeText(ficheConcours.getParametre().getClub().toString())); //$NON-NLS-1$
		tplClassementEquipe.parse("INTITULE_CONCOURS", org.ajdeveloppement.commons.XmlUtils.sanitizeText(ficheConcours.getParametre().getIntituleConcours())); //$NON-NLS-1$
		tplClassementEquipe.parse("VILLE_CLUB", org.ajdeveloppement.commons.XmlUtils.sanitizeText(ficheConcours.getParametre().getLieuConcours())); //$NON-NLS-1$
		tplClassementEquipe.parse("DATE_CONCOURS", java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG).format(ficheConcours.getParametre().getDate())); //$NON-NLS-1$

		var strArbitreResp = ""; //$NON-NLS-1$
		var strArbitresAss = ""; //$NON-NLS-1$

		var arbitres = ficheConcours.getParametre().getJudges();
		for (var i = 0; i < arbitres.size(); i++) {
			if (arbitres.get(i).isResponsable())
				strArbitreResp = arbitres.get(i).getFullName();
			else {
				if (!strArbitresAss.equals(""))
					strArbitresAss += ", ";
				strArbitresAss += arbitres.get(i).getFullName();
			}
		}

		tplClassementEquipe.parse("ARBITRE_RESPONSABLE", org.ajdeveloppement.commons.XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
		tplClassementEquipe.parse("ARBITRES_ASSISTANT", org.ajdeveloppement.commons.XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
		tplClassementEquipe.parse("NB_CLUB", "" + ficheConcours.getConcurrentList().countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
		tplClassementEquipe.parse("NB_TIREURS", "" + ficheConcours.getConcurrentList().countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
		tplClassementEquipe.parse("TYPE_CLASSEMENT", profile.getLocalisation().getResourceString("classement.equipe")); //$NON-NLS-1$ //$NON-NLS-2$

		sortedTeamCriteriaSets = ficheConcours.getEquipes().listCriteriaSet();
		
		org.ajdeveloppement.concours.CriteriaSet.sortCriteriaSet(sortedTeamCriteriaSets, ficheConcours.getParametre().getReglement().getListCriteria());
		
		for(var i = 0; i < sortedTeamCriteriaSets.size(); i++) {			
			tplClassementEquipe.parse("categories.CATEGORIE", new org.ajdeveloppement.concours.localisable.CriteriaSetLibelle(sortedTeamCriteriaSets.get(i), profile.getLocalisation()).toString()); //$NON-NLS-1$
			tplClassementEquipe.parse("categories.NB_EQUIPES", "" + ficheConcours.getEquipes().countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$

			var sortEquipes = org.ajdeveloppement.concours.EquipeList.sort(ficheConcours.getEquipes().list(sortedTeamCriteriaSets.get(i)));

			for (var j = 0; j < sortEquipes.length; j++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (j + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				var idsXML = ""; //$NON-NLS-1$
				var ptsXML = ""; //$NON-NLS-1$
				var concurrents = sortEquipes[j].getMembresEquipe();
				//print(concurrents.size() +"\n");
				for (var k = 0; k < concurrents.size(); k++) {
					//print(concurrents[k].getID() +"\n");
					idsXML += org.ajdeveloppement.commons.XmlUtils.sanitizeText(concurrents.get(k).getFullName()) + "<newline/>"; //$NON-NLS-1$
					ptsXML += concurrents.get(k).getTotalScore() + "<newline/>"; //$NON-NLS-1$
				}
				tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", org.ajdeveloppement.commons.XmlUtils.sanitizeText(sortEquipes[j].getNomEquipe())); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes[j].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$

				tplClassementEquipe.loopBloc("categories.classement"); //$NON-NLS-1$
			}
			tplClassementEquipe.loopBloc("categories"); //$NON-NLS-1$
		}
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(tplClassementEquipe.output()));
	}
}