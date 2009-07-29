//mapping:
// int dbVersion - version de la base de donnée
// SqlManager sql - moteur de base de données

function updateReglements() {
	//liste l'ensemble des fichiers de réglements
	var updatePath = new java.io.File(org.concoursjeunes.ApplicationCore.staticParameters.getResourceString("path.ressources"), "update");
	var reglements = org.ajdeveloppement.commons.io.FileUtils.listAllFiles(updatePath, ".*\\.reglement");
	var rManager = new org.concoursjeunes.manager.ReglementManager();
	for(var i = 0; i < reglements.size(); i++) {
		rManager.importReglement(reglements.get(i));
	}
}

if(dbVersion == 0) {
	//passe l'ensemble des scripts de base
	sql.executeScript("01-create_db.sql");
	sql.executeScript("../sql/ImportClubFFTA.sql");
	
	updateReglements();
} else if(dbVersion < 20) {
	sql.executeScript("01-dropoldtable.sql");
	sql.executeScript("01-create_db.sql");
	
	sql.executeUpdate("ALTER TABLE ENTITE ADD DATEMODIF DATE DEFAULT CURRENT_DATE()");
	
	sql.executeScript("../sql/ImportClubFFTA.sql");
	
	updateReglements();
}

if(dbVersion != org.concoursjeunes.ApplicationCore.DB_RELEASE_REQUIRED) {
	//mise à jour du numero de version de la base
	sql.executeScript("99-updatedbver.sql");
}