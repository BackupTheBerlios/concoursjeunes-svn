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
						Packages.java.util,
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						java.io.StringReader);
	
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	with(contexte) {
		var templateEtiquettesXML = new AJTemplate();
		templateEtiquettesXML.loadTemplate(template);
	
		try {
			var nblarg = profile.getConfiguration().getColonneAndLigne()[1];
			var nbhaut = profile.getConfiguration().getColonneAndLigne()[0];
			var depart = 0;
		
			var marge_gauche = profile.getConfiguration().getMarges().left; // la marge gauche
			var marge_droite = profile.getConfiguration().getMarges().right; // la marge droite
			var marge_haut = profile.getConfiguration().getMarges().top; // la marge haut
			var marge_bas = profile.getConfiguration().getMarges().bottom; // la marge bas
			var espacement_cellule_h = profile.getConfiguration().getEspacements()[0]; // l'espacement horizontal entre cellule
			var espacement_cellule_v = profile.getConfiguration().getEspacements()[1]; // l'espacement vertical entre cellule
			eval("var pageDimension = PageSize." + profile.getConfiguration().getFormatPapier());
			
			if(profile.getConfiguration().getOrientation().equals("landscape")) //$NON-NLS-1$
				pageDimension = pageDimension.rotate();
				
			espacement_cellule_h = AJToolKit.centimeterToDpi(espacement_cellule_h);
			espacement_cellule_v = AJToolKit.centimeterToDpi(espacement_cellule_v);
			marge_gauche = AJToolKit.centimeterToDpi(marge_gauche);
			marge_droite = AJToolKit.centimeterToDpi(marge_droite);
			marge_haut = AJToolKit.centimeterToDpi(marge_haut);
			marge_bas = AJToolKit.centimeterToDpi(marge_bas);
		
			var zoneaffichable_x = pageDimension.getWidth() - marge_gauche - marge_droite;
			var zoneaffichable_y = pageDimension.getHeight() - marge_haut - marge_bas;
			
			var cellule_x = (zoneaffichable_x - (espacement_cellule_h * (nblarg - 1.0))) / zoneaffichable_x * 100 / nblarg - 7;
			var cellule_y = (zoneaffichable_y - (espacement_cellule_v * (nbhaut - 1.0))) / zoneaffichable_y * 100 / nbhaut;
		
			var tailles_x = 0.1 + ""; //$NON-NLS-1$
			for (var i = 0; i < nblarg; i++) {
				tailles_x += ";" + cellule_x + ";7"; //$NON-NLS-1$ //$NON-NLS-2$
				if (i < nblarg - 1)
					tailles_x += ";" + espacement_cellule_h / zoneaffichable_x * 100; //$NON-NLS-1$
			}
		
			templateEtiquettesXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			templateEtiquettesXML.parse("producer", AppInfos.NOM + " " + AppInfos.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("author", profile.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("pagesize", profile.getConfiguration().getFormatPapier()); //$NON-NLS-1$
			templateEtiquettesXML.parse("orientation", profile.getConfiguration().getOrientation()); //$NON-NLS-1$
			templateEtiquettesXML.parse("top", "" + AJToolKit.centimeterToDpi(profile.getConfiguration().getMarges().top)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("bottom", "" + AJToolKit.centimeterToDpi(profile.getConfiguration().getMarges().bottom)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("left", "" + AJToolKit.centimeterToDpi(profile.getConfiguration().getMarges().left)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("right", "" + AJToolKit.centimeterToDpi(profile.getConfiguration().getMarges().right)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$
		
			var colonne = 0;
			var ligne = 0;
			var concurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS);
			
			for (var i = 0; i < concurrents.size(); i++) {
				
				if (colonne == 0)
					if(ligne < nbhaut - 1)
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) + espacement_cellule_v)); //$NON-NLS-1$ //$NON-NLS-2$
					else
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) - 1)); //$NON-NLS-1$ //$NON-NLS-2$
				templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrents.get(i).getID()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrents.get(i).getClub().toString()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrents.get(i).getNumLicenceArcher()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.emplacement", new TargetPosition(concurrents.get(i).getCible(), concurrents.get(i).getPosition()).toString()); //$NON-NLS-1$
				if (colonne + 1 == nblarg)
					templateEtiquettesXML.parseBloc("page.ligne.colonne.interbloc", ""); //$NON-NLS-1$ //$NON-NLS-2$
		
				templateEtiquettesXML.loopBloc("page.ligne.colonne"); //$NON-NLS-1$
		
				colonne = (++colonne) % nblarg;
				if (colonne == 0) {
					templateEtiquettesXML.loopBloc("page.ligne");
					ligne++;
				}
		
				if (ligne == nbhaut) {
					templateEtiquettesXML.loopBloc("page");
		
					templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3));
					templateEtiquettesXML.parse("page.widths", tailles_x);
		
					ligne = 0;
				}
			}
		
			if (colonne != 0) {
				templateEtiquettesXML.loopBloc("page.ligne");
			}
			if (ligne != 0) {
				templateEtiquettesXML.loopBloc("page");
			}
			
			/*var footer = new HeaderFooter(new Phrase("page "), new Phrase("."));
			footer.setBorder(0);
			footer.setAlignment(HeaderFooter.ALIGN_RIGHT);
			document.setFooter(footer);*/
			
			XmlParser.parse(document, new StringReader(templateEtiquettesXML.output()));
		} catch (e) {
			/*org.jdesktop.swingx.error.JXErrorPane.showDialog(null, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));*/
			//e.printStackTrace();
			print(e);
		}
	}
}