//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours) {
	return true;
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
		var strClassement = ""; //$NON-NLS-1$
		if (ficheConcours.getConcurrentList() != null && ficheConcours.getConcurrentList().countArcher() > 0) {
			var concurrentsClasse = ficheConcours.classement();

			var tplClassement = new AJTemplate();
			var strArbitreResp = ""; //$NON-NLS-1$
			var strArbitresAss = ""; //$NON-NLS-1$

			tplClassement.setLocalisationReader(localeReader);
			tplClassement.loadTemplate(template);

			tplClassement.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassement.parse("LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassement.parse("INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$
			tplClassement.parse("INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours()); //$NON-NLS-1$
			tplClassement.parse("VILLE_CLUB", ficheConcours.getParametre().getLieuConcours()); //$NON-NLS-1$
			tplClassement.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(ficheConcours.getParametre().getDate())); //$NON-NLS-1$
			tplClassement.parse("author", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$

			var arbitres = ficheConcours.getParametre().getArbitres();
			for (var i = 0; i < arbitres.size(); i++) {
				if (arbitres.get(i).startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitres.get(i).substring(1);
				else {
					if (!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitres.get(i);
				}
			}
			tplClassement.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
			tplClassement.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
			tplClassement.parse("NB_CLUB", "" + ficheConcours.getConcurrentList().countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("NB_TIREURS", "" + ficheConcours.getConcurrentList().countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			
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
					var strTailleChampsDistance = ""; //$NON-NLS-1$
					for (var j = 0; j < ficheConcours.getParametre().getReglement().getNbSerie(); j++) {
						strTailleChampsDistance += tailleChampDistance + ";"; //$NON-NLS-1$
					}

					strSCNA = new CriteriaSetLibelle(scnaUse[i]).toString();

					tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance); //$NON-NLS-1$
					tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
					tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + ficheConcours.getParametre().getReglement().getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.NB_TIREURS", "" + sortList.length); //$NON-NLS-1$ //$NON-NLS-2$

					for (var j = 0; j < ficheConcours.getParametre().getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
								ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(scnaUse[i].getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances"); //$NON-NLS-1$
					}

					if (sortList.length > 0) {
						var row_exist = false;
						for (var j = 0; j < sortList.length; j++) {
							if (sortList[j].getTotalScore() > 0) {
								row_exist = true;
								// test d'ex-Eaquo
								if ((j < sortList.length - 1 && sortList[j].getTotalScore() == sortList[j + 1].getTotalScore() && ApplicationCore.getConfiguration().isInterfaceAffResultatExEquo())
										|| (j > 0 && sortList[j].getTotalScore() == sortList[j - 1].getTotalScore() && ApplicationCore.getConfiguration().isInterfaceAffResultatExEquo())) {

									if ((sortList[j].getManque() == 0 && sortList[j].getDix() == 0 && sortList[j].getNeuf() == 0)
											|| (j < sortList.length - 2 && sortList[j].getManque() == sortList[j + 1].getManque() && sortList[j].getDix() == sortList[j + 1].getDix() && sortList[j]
													.getNeuf() == sortList[j + 1].getNeuf())
											|| (j > 0 && sortList[j].getManque() == sortList[j - 1].getManque() && sortList[j].getDix() == sortList[j - 1].getDix() && sortList[j].getNeuf() == sortList[j - 1]
													.getNeuf())) {

										tplClassement.parse("categories.classement.COULEUR", //$NON-NLS-1$
												"bgcolor=\"#ff0000\""); //$NON-NLS-1$
									}
								} else {
									tplClassement.parse("categories.classement.COULEUR", "bgcolor=\"#ffffff\""); //$NON-NLS-1$ //$NON-NLS-2$
								}

								tplClassement.parse("categories.classement.PLACE", "" + (j + 1)); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.POSITION", "" + sortList[j].getPosition() + sortList[j].getCible()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.IDENTITEE", sortList[j].getID()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.CLUB", sortList[j].getClub().getNom()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.NUM_LICENCE", sortList[j].getNumLicenceArcher()); //$NON-NLS-1$

								var keys = ficheConcours.getParametre().getReglement().getListCriteria();
								for (var k = 0; k < keys.size(); k++)
									tplClassement.parse("categories.classement." //$NON-NLS-1$
											+ keys.get(k).getCode(), sortList[j].getCriteriaSet().getCriterionElement(keys.get(k)).getCode());

								for (var k = 0; k < ficheConcours.getParametre().getReglement().getNbSerie(); k++) {
									if (sortList[j].getScore() != null)
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList[j].getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
									else
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

									tplClassement.loopBloc("categories.classement.scores"); //$NON-NLS-1$
								}
								tplClassement.parse("categories.classement.TOTAL", "" + sortList[j].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.0_10_9", sortList[j].getManque() //$NON-NLS-1$
										+ "-" + sortList[j].getDix() + "-" + sortList[j].getNeuf()); //$NON-NLS-1$ //$NON-NLS-2$

								tplClassement.loopBloc("categories.classement"); //$NON-NLS-1$
							}
							if (!row_exist)
								tplClassement.parseBloc("categories.classement", ""); //$NON-NLS-1$ //$NON-NLS-2$
						}
					} else {
						tplClassement.parseBloc("categories.classement", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}

					tplClassement.loopBloc("categories"); //$NON-NLS-1$
				}
			}
			XmlParser.parse(document, new StringReader(tplClassement.output()));
		}
	}
}