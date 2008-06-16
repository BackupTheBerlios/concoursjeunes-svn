//mapping Java/JS:
//	template: le chemin du fichier XML de template
//  ficheConcours: la fiche concours dont dépent l'édition
//  document: le document pdf à produire
//  writer: le writer d'ecriture du pdf

new org.concoursjeunes.state.ShootingLineState(ficheConcours.getPasDeTir(ficheConcours.getCurrentDepart()), document, writer);