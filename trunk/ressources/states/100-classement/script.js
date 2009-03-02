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
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						Packages.java.util,
						java.io.StringReader);
	
	with(contexte) {
		var strClassement = "";
		if (ficheConcours.getConcurrentList() != null && ficheConcours.getConcurrentList().countArcher() > 0) {
			var concurrentsClasse = ficheConcours.classement();

			var tplClassement = new AJTemplate();
			var strArbitreResp = "";
			var strArbitresAss = "";

			tplClassement.setLocalisationReader(localeReader);
			tplClassement.loadTemplate(template);

			tplClassement.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
			tplClassement.parse("LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
			tplClassement.parse("INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom());
			tplClassement.parse("INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours());
			tplClassement.parse("VILLE_CLUB", ficheConcours.getParametre().getLieuConcours());
			tplClassement.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDate()));
			tplClassement.parse("author", ficheConcours.getParametre().getClub().getNom());

			var arbitres = ficheConcours.getParametre().getArbitres();
			for (var i = 0; i < arbitres.size(); i++) {
				if (arbitres.get(i).startsWith("*"))
					strArbitreResp = arbitres.get(i).substring(1);
				else {
					if (!strArbitresAss.equals(""))
						strArbitresAss += ", ";
					strArbitresAss += arbitres.get(i);
				}
			}
			tplClassement.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp));
			tplClassement.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss));
			tplClassement.parse("NB_CLUB", "" + ficheConcours.getConcurrentList().countCompagnie());
			tplClassement.parse("NB_TIREURS", "" + ficheConcours.getConcurrentList().countArcher());
			
			// Entete de categorie
			var scnalst = concurrentsClasse.keys();

			var scnaUse = new Array(concurrentsClasse.size());
			for (var i = 0; scnalst.hasMoreElements(); i++) {
				scnaUse[i] = scnalst.nextElement();
			}

			CriteriaSet.sortCriteriaSet(scnaUse, ficheConcours.getParametre().getReglement().getListCriteria());

			for (var i = 0; i < scnaUse.length; i++) {

				var sortList = concurrentsClasse.get(scnaUse[i]);

				var strSCNA = "";

				if (sortList.length > 0) {

					var tailleChampDistance = 10.5262 / ficheConcours.getParametre().getReglement().getNbSerie();
					var strTailleChampsDistance = "";
					for (var j = 0; j < ficheConcours.getParametre().getReglement().getNbSerie(); j++) {
						strTailleChampsDistance += tailleChampDistance + ";";
					}

					strSCNA = new CriteriaSetLibelle(scnaUse[i]).toString();

					tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance);
					tplClassement.parse("categories.CATEGORIE", strSCNA);
					tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + ficheConcours.getParametre().getReglement().getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.NB_TIREURS", "" + sortList.length);

					for (var j = 0; j < ficheConcours.getParametre().getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE",
								ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(scnaUse[i].getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances");
					}

					if (sortList.length > 0) {
						var row_exist = false;
						for (var j = 0; j < sortList.length; j++) {
							if (sortList[j].getTotalScore() > 0) {
								row_exist = true;

								tplClassement.parse("categories.classement.PLACE", "" + (j + 1));
								tplClassement.parse("categories.classement.POSITION", "" + sortList[j].getPosition() + sortList[j].getCible());
								tplClassement.parse("categories.classement.IDENTITEE", sortList[j].getID());
								tplClassement.parse("categories.classement.CLUB", sortList[j].getClub().getNom());
								tplClassement.parse("categories.classement.NUM_LICENCE", sortList[j].getNumLicenceArcher());

								var keys = ficheConcours.getParametre().getReglement().getListCriteria();
								var catStr = "";
								for (var k = 0; k < keys.size(); k++)
									catStr += sortList[j].getCriteriaSet().getCriterionElement(keys.get(k)).getCode();
								tplClassement.parse("categories.classement.categorie", catStr);

								for (var k = 0; k < ficheConcours.getParametre().getReglement().getNbSerie(); k++) {
									if (sortList[j].getScore() != null)
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList[j].getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
									else
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

									tplClassement.loopBloc("categories.classement.scores");
								}
								tplClassement.parse("categories.classement.TOTAL", "" + sortList[j].getTotalScore());
								tplClassement.parse("categories.classement.0_10_9",
										sortList[j].getDix() + "-" + sortList[j].getNeuf());

								tplClassement.loopBloc("categories.classement");
							}
							if (!row_exist)
								tplClassement.parseBloc("categories.classement", "");
						}
					} else {
						tplClassement.parseBloc("categories.classement", "");
					}

					tplClassement.loopBloc("categories");
				}
			}
			XmlParser.parse(document, new StringReader(tplClassement.output()));
		}
	}
}