//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours) {
	if(ficheConcours.getConcurrentList().countArcher(depart))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer) {
		//En raison des médiocres performance de javascript, le traitement à été
		//réécrit en java (8 fois plus rapide)
		var isss = new org.concoursjeunes.state.IndividualScoreSheetState(localeReader, depart, serie);
		isss.printState(ficheConcours, template, document, writer);
}