function checkPrintable(ficheConcours, options) {
	var clubList = org.concoursjeunes.builders.EquipeListBuilder.getClubEquipeList(ficheConcours.getConcurrentList(), ficheConcours.getParametre().getReglement().getNbMembresRetenu());

	if (clubList != null && clubList.countEquipes() > 0)
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer, options) {
	var contexte = JavaImporter(
						Packages.org.concoursjeunes,
						Packages.org.ajdeveloppement.commons,
						Packages.com.lowagie.text,
						Packages.java.util,
						org.concoursjeunes.builders,
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						java.io.StringReader);
	
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	with(contexte) {
		var strClassementEquipe = ""; //$NON-NLS-1$

		var clubList = EquipeListBuilder.getClubEquipeList(ficheConcours.getConcurrentList(), ficheConcours.getParametre().getReglement().getNbMembresRetenu());

		if (clubList != null && clubList.countEquipes() > 0) {
			writer.setPageEvent(new org.concoursjeunes.state.PageFooter());
			
			var tplClassementEquipe = new AJTemplate();
			tplClassementEquipe.loadTemplate(template);

			// classement sortie XML
			tplClassementEquipe.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassementEquipe.parse("LOGO_CLUB_URI", profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", ficheConcours.getParametre().getClub().toString()); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", ficheConcours.getParametre().getLieuConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDate())); //$NON-NLS-1$

			var strArbitreResp = ""; //$NON-NLS-1$
			var strArbitresAss = ""; //$NON-NLS-1$

			var arbitres = ficheConcours.getParametre().getJudges();
			for (var i = 0; i < arbitres.size(); i++) {
				if (arbitres.get(i).isResponsable())
					strArbitreResp = arbitres.get(i).getID();
				else {
					if (!strArbitresAss.equals(""))
						strArbitresAss += ", ";
					strArbitresAss += arbitres.get(i).getID();
				}
			}

			tplClassementEquipe.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
			tplClassementEquipe.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
			tplClassementEquipe.parse("NB_CLUB", "" + ficheConcours.getConcurrentList().countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("NB_TIREURS", "" + ficheConcours.getConcurrentList().countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("TYPE_CLASSEMENT", profile.getLocalisation().getResourceString("classement.club")); //$NON-NLS-1$ //$NON-NLS-2$

			tplClassementEquipe.parse("categories.CATEGORIE", profile.getLocalisation().getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("categories.NB_EQUIPES", "" + clubList.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$

			var sortEquipes = EquipeList.sort(clubList.list());

			for (var i = 0; i < sortEquipes.length; i++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				var idsXML = ""; //$NON-NLS-1$
				var ptsXML = ""; //$NON-NLS-1$
				var concurrents = sortEquipes[i].getMembresEquipe();
				for (var j = 0; j < concurrents.size(); j++) {
					idsXML += XmlUtils.sanitizeText(concurrents.get(j).getID()) + "<newline/>"; //$NON-NLS-1$
					ptsXML += concurrents.get(j).getTotalScore() + "<newline/>"; //$NON-NLS-1$
				}
				tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", sortEquipes[i].getNomEquipe()); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes[i].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$

				tplClassementEquipe.loopBloc("categories.classement"); //$NON-NLS-1$
			}
			XmlParser.parse(document, new StringReader(tplClassementEquipe.output()));
		}
	}
}