//mapping Java/JS:
//	template: le chemin du fichier XML de template
//  ficheConcours: la fiche concours dont dépent l'édition

var contexte = JavaImporter(
					Packages.org.concoursjeunes,
					com.lowagie.text.PageSize,
					ajinteractive.standard.common.AJTemplate,
					ajinteractive.standard.common.AJToolKit,
					java.text.DateFormat,
					Packages.java.util,
					java.util.logging.Level,
					org.jdesktop.swingx.error.ErrorInfo,
					org.jdesktop.swingx.error.JXErrorPane);

with(contexte) {
	var templateEtiquettesXML = new AJTemplate(template);

	try {
		var marge_gauche = ApplicationCore.getConfiguration().getMarges().left; // la marge gauche
		var marge_droite = ApplicationCore.getConfiguration().getMarges().right; // la marge droite
		var marge_haut = ApplicationCore.getConfiguration().getMarges().top; // la marge haut
		var marge_bas = ApplicationCore.getConfiguration().getMarges().bottom; // la marge bas
		var espacement_cellule_h = ApplicationCore.getConfiguration().getEspacements()[0]; // l'espacement horizontal entre cellule
		var espacement_cellule_v = ApplicationCore.getConfiguration().getEspacements()[1]; // l'espacement vertical entre cellule
		var cellule_x;
		var cellule_y;
		var pageDimension = PageSize.class.getField(ApplicationCore.getConfiguration().getFormatPapier()).get(null);
		
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
		
		cellule_x = (zoneaffichable_x - (espacement_cellule_h * (nblarg - 1.0))) / zoneaffichable_x * 100 / nblarg - 7;
		cellule_y = (zoneaffichable_y - (espacement_cellule_v * (nbhaut - 1.0))) / zoneaffichable_y * 100 / nbhaut;
	
		var tailles_x = 0.1 + ""; //$NON-NLS-1$
		for (int i = 0; i < nblarg; i++) {
			tailles_x += ";" + cellule_x + ";7"; //$NON-NLS-1$ //$NON-NLS-2$
			if (i < nblarg - 1)
				tailles_x += ";" + espacement_cellule_h / zoneaffichable_x * 100; //$NON-NLS-1$
		}
	
		templateEtiquettesXML.reset();
	
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
		for (var concurrent : ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS)) {
			
			if (colonne == 0)
				if(ligne < nbhaut - 1)
					templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) + espacement_cellule_v)); //$NON-NLS-1$ //$NON-NLS-2$
				else
					templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) - 1)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrent.getID()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrent.getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrent.getNumLicenceArcher()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.emplacement", new TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString()); //$NON-NLS-1$
			if (colonne + 1 == nblarg)
				templateEtiquettesXML.parseBloc("page.ligne.colonne.interbloc", ""); //$NON-NLS-1$ //$NON-NLS-2$
	
			templateEtiquettesXML.loopBloc("page.ligne.colonne"); //$NON-NLS-1$
	
			colonne = (++colonne) % nblarg;
			if (colonne == 0) {
				templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
				ligne++;
			}
	
			if (ligne == nbhaut) {
				templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$
	
				templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
				templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$
	
				ligne = 0;
			}
		}
	
		if (colonne != 0) {
			templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
		}
		if (ligne != 0) {
			templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$
		}
	} catch (e) {
		JXErrorPane.showDialog(null, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
				e.toString(), null, null, e, Level.SEVERE, null));
		e.printStackTrace();
	}
	
	return templateEtiquettesXML.output();
}