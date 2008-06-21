//mapping Java/JS:
//	depart: le depart selectionné
function checkPrintable(ficheConcours) {
	if(ficheConcours.getConcurrentList().countArcher(depart))
		return true;
	return false;
}

function printState(ficheConcours, template, document, writer) {
	new org.concoursjeunes.state.ShootingLineState(ficheConcours.getPasDeTir(ficheConcours.getCurrentDepart()), document, writer);
}