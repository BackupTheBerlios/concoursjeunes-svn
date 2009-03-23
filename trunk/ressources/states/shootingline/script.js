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
	new org.concoursjeunes.state.ShootingLineState(localeReader, profile, ficheConcours.getPasDeTir(depart), document, writer);
}