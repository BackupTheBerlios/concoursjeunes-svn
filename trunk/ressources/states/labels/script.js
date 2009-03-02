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
						Packages.org.ajdeveloppement.commons,
						Packages.com.lowagie.text,
						Packages.java.util,
						com.lowagie.text.xml.XmlParser,
						java.text.DateFormat,
						java.util.logging.Level,
						java.io.StringReader,
						org.jdesktop.swingx.error.ErrorInfo,
						org.jdesktop.swingx.error.JXErrorPane);
	
	with(contexte) {
		var templateEtiquettesXML = new AJTemplate();
		templateEtiquettesXML.loadTemplate(template);
	
		try {
			var nblarg = ApplicationCore.getConfiguration().getColonneAndLigne()[1];
			var nbhaut = ApplicationCore.getConfiguration().getColonneAndLigne()[0];
			var depart = 0;
		
			var marge_gauche = ApplicationCore.getConfiguration().getMarges().left; // la marge gauche
			var marge_droite = ApplicationCore.getConfiguration().getMarges().right; // la marge droite
			var marge_haut = ApplicationCore.getConfiguration().getMarges().top; // la marge haut
			var marge_bas = ApplicationCore.getConfiguration().getMarges().bottom; // la marge bas
			var espacement_cellule_h = ApplicationCore.getConfiguration().getEspacements()[0]; // l'espacement horizontal entre cellule
			var espacement_cellule_v = ApplicationCore.getConfiguration().getEspacements()[1]; // l'espacement vertical entre cellule
			eval("var pageDimension = PageSize." + ApplicationCore.getConfiguration().getFormatPapier());
			
			if(ApplicationCore.getConfiguration().getOrientation().equals("landscape")) //$NON-NLS-1$
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
			templateEtiquettesXML.parse("producer", ApplicationCore.NOM + " " + ApplicationCore.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("author", ApplicationCore.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("pagesize", ApplicationCore.getConfiguration().getFormatPapier()); //$NON-NLS-1$
			templateEtiquettesXML.parse("orientation", ApplicationCore.getConfiguration().getOrientation()); //$NON-NLS-1$
			templateEtiquettesXML.parse("top", "" + AJToolKit.centimeterToDpi(ApplicationCore.getConfiguration().getMarges().top)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("bottom", "" + AJToolKit.centimeterToDpi(ApplicationCore.getConfiguration().getMarges().bottom)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("left", "" + AJToolKit.centimeterToDpi(ApplicationCore.getConfiguration().getMarges().left)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("right", "" + AJToolKit.centimeterToDpi(ApplicationCore.getConfiguration().getMarges().right)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$
		
			var colonne = 0;
			var ligne = 0;
			var concurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS);
			
			for (var i = 0; i < concurrents.length; i++) {
				
				if (colonne == 0)
					if(ligne < nbhaut - 1)
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) + espacement_cellule_v)); //$NON-NLS-1$ //$NON-NLS-2$
					else
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) - 1)); //$NON-NLS-1$ //$NON-NLS-2$
				templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrents[i].getID()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrents[i].getClub().getNom()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrents[i].getNumLicenceArcher()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.emplacement", new TargetPosition(concurrents[i].getCible(), concurrents[i].getPosition()).toString()); //$NON-NLS-1$
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