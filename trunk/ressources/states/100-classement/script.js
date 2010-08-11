function checkPrintable(ficheConcours, options) {
	return true;
}

function printState(ficheConcours, template, document, writer, options) {
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	var strClassement = "";
	if (ficheConcours.getConcurrentList() != null && ficheConcours.getConcurrentList().countArcher() > 0) {
		var concurrentsClasse = ficheConcours.getConcurrentList().classement().getClassementPhaseQualificative();

		var tplClassement = new org.ajdeveloppement.commons.AJTemplate();
		var strArbitreResp = "";
		var strArbitresAss = "";
		
		writer.setPageEvent(new org.concoursjeunes.state.PageFooter());

		tplClassement.setLocalisationReader(localeReader);
		tplClassement.loadTemplate(template);

		tplClassement.parse("CURRENT_TIME", java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(new java.util.Date()));
		tplClassement.parse("LOGO_CLUB_URI", profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
		tplClassement.parse("INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom());
		tplClassement.parse("INTITULE_CONCOURS", ficheConcours.getParametre().getIntituleConcours());
		tplClassement.parse("VILLE_CLUB", ficheConcours.getParametre().getLieuConcours());
		tplClassement.parse("DATE_CONCOURS", java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG).format(ficheConcours.getParametre().getDateDebutConcours()));
		tplClassement.parse("author", ficheConcours.getParametre().getClub().getNom());

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
		tplClassement.parse("ARBITRE_RESPONSABLE", org.ajdeveloppement.commons.XmlUtils.sanitizeText(strArbitreResp));
		tplClassement.parse("ARBITRES_ASSISTANT", org.ajdeveloppement.commons.XmlUtils.sanitizeText(strArbitresAss));
		tplClassement.parse("NB_CLUB", "" + ficheConcours.getConcurrentList().countCompagnie());
		tplClassement.parse("NB_TIREURS", "" + ficheConcours.getConcurrentList().countArcher());
		
		// Entete de categorie
		var scnalst = concurrentsClasse.keySet().iterator();

		var scnaUse = new java.util.ArrayList();
		for (var i = 0; scnalst.hasNext(); i++) {
			scnaUse.add(scnalst.next());
		}
		
		var nbSerie = ficheConcours.getParametre().getReglement().getNbSerie();
		
		var tailleChampDistance = 10.5262 / nbSerie;
		var strTailleChampsDistance = "";
		for (var j = 0; j < nbSerie; j++) {
			strTailleChampsDistance += tailleChampDistance + ";";
		}
		
		org.concoursjeunes.CriteriaSet.sortCriteriaSet(scnaUse, ficheConcours.getParametre().getReglement().getListCriteria());

		for (var i = 0; i < scnaUse.size(); i++) {

			var sortList = concurrentsClasse.get(scnaUse.get(i));

			var strSCNA = "";

			if (sortList.size() > 0) {
				strSCNA = new org.concoursjeunes.localisable.CriteriaSetLibelle(scnaUse.get(i), localeReader).toString();

				tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance);
				tplClassement.parse("categories.CATEGORIE", strSCNA);
				tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + nbSerie)); //$NON-NLS-1$ //$NON-NLS-2$
				tplClassement.parse("categories.NB_TIREURS", "" + sortList.size());

				for (var j = 0; j < nbSerie; j++) {
					tplClassement.parse("categories.distances.DISTANCE",
							ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(scnaUse.get(i).getFilteredCriteriaSet(ficheConcours.getParametre().getReglement().getPlacementFilter())).get(0).getDistance()[j] + "m"); //$NON-NLS-1$
					tplClassement.loopBloc("categories.distances");
				}
				
				var deplst = ficheConcours.getParametre().getReglement().getTie();
				var departages = ""; //$NON-NLS-1$
				for (var j = 0; j < deplst.size(); j++) {
					if(!departages.equals(""))
						departages += "-"; //$NON-NLS-1$
					departages += deplst.get(j);
				}
				tplClassement.parse("categories.DEPARTAGE", departages); //$NON-NLS-1$

				if (sortList.size() > 0) {
					var row_exist = false;
					for (var j = 0; j < sortList.size(); j++) {
						if (sortList.get(j).getTotalScore() > 0) {
							row_exist = true;

							tplClassement.parse("categories.classement.PLACE", "" + (j + 1));
							tplClassement.parse("categories.classement.POSITION", "" + sortList.get(j).getPosition() + sortList.get(j).getCible());
							tplClassement.parse("categories.classement.IDENTITEE", sortList.get(j).getFullName());
							tplClassement.parse("categories.classement.CLUB", sortList.get(j).getClub().toString());
							tplClassement.parse("categories.classement.NUM_LICENCE", sortList.get(j).getNumLicenceArcher());

							var keys = ficheConcours.getParametre().getReglement().getListCriteria();
							var catStr = "";
							for (var k = 0; k < keys.size(); k++)
								catStr += sortList.get(j).getCriteriaSet().getCriterionElement(keys.get(k)).getCode();
							tplClassement.parse("categories.classement.categorie", catStr);

							for (var k = 0; k < nbSerie; k++) {
								if (sortList.get(j).getScore() != null)
									tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList.get(j).getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
								else
									tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

								tplClassement.loopBloc("categories.classement.scores");
							}
							tplClassement.parse("categories.classement.TOTAL", "" + sortList.get(j).getTotalScore());
							
							departages = ""; //$NON-NLS-1$
							if(sortList.get(j).getDepartages().length == deplst.size()) {
								for(var k = 0; k < deplst.size(); k++) {
									departages += sortList.get(j).getDepartages()[k];
									if(k<deplst.size()-1)
										departages += "-"; //$NON-NLS-1$
								}
							}
							tplClassement.parse("categories.classement.0_10_9", departages);

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
		com.lowagie.text.xml.XmlParser.parse(document, new java.io.StringReader(tplClassement.output()));
	}
}