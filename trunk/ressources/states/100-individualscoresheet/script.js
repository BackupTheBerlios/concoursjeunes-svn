//mapping Java/JS:
//	depart: le depart selectionné
//  serie: la série séléctionné
//  localeReader: la ressource de localisation de l'état
//  profile: le profil courrant
function checkPrintable(ficheConcours) {
	if(ficheConcours.getConcurrentList().countArcher(depart))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer) {
		//En raison des médiocres performance de javascript, le traitement à été
		//réécrit en java (8 fois plus rapide)
		var isss = new org.concoursjeunes.state.IndividualScoreSheetState(localeReader, profile, depart, serie);
		isss.printState(ficheConcours, template, document, writer);
}