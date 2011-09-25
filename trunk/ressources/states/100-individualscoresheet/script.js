function checkPrintable(ficheConcours, options) {
	if(ficheConcours.getConcurrentList().countArcher(options.getDepart()))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer, options) {
	var localeReader = options.getLangReader();
	var serie = options.getSerie();
	var depart = options.getDepart();
	var profile = options.getProfile();
	
	//En raison des médiocres performance de javascript, le traitement à été
	//réécrit en java (8 fois plus rapide)
	var isss = new org.ajdeveloppement.concours.state.IndividualScoreSheetState(localeReader, profile, depart, serie);
	isss.printState(ficheConcours, template, document, writer);
}