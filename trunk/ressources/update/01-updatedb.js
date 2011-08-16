//mapping:
// int dbVersion - version de la base de donnée
// SqlManager sql - moteur de base de données

function updateReglements() {
	//liste l'ensemble des fichiers de réglements
	var updatePath = new java.io.File(org.concoursjeunes.ApplicationCore.staticParameters.getResourceString("path.ressources"), "update");
	var reglements = org.ajdeveloppement.commons.io.FileUtils.listAllFiles(updatePath, ".*\\.reglement");
	var rManager = org.concoursjeunes.manager.ReglementManager.getInstance();
	for(var i = 0; i < reglements.size(); i++) {
		rManager.importReglement(reglements.get(i));
	}
}

if(dbVersion == 0) {
	//passe l'ensemble des scripts de base
	sql.executeScript("01-create_db.sql");
	sql.executeScript("../sql/ImportClubFFTA.sql");
	sql.executeUpdate("RUNSCRIPT FROM 'ressources/sql/ImportVillesFr.sql'");
	//sql.executeScript("../sql/ImportVillesFr.sql");
	
	updateReglements();
	sql.executeScript("../sql/ImportClubLFBTA.sql");
} else {
	if(dbVersion < 20) {
		sql.executeScript("01-dropoldtable.sql");
		sql.executeScript("01-create_db.sql");
		
		sql.executeUpdate("ALTER TABLE ENTITE ADD DATEMODIF DATE DEFAULT CURRENT_DATE()");
	}
	
	if(dbVersion < 21) {
		sql.executeUpdate("update REGLEMENT set REMOVABLE=FALSE where NOMREGLEMENT like 'FFTA%'");
	}

	if(dbVersion < 30) {
		sql.executeScript("02-V21toV30.sql", true);
		sql.executeScript("../sql/ImportClubFFTA.sql");
		sql.executeUpdate("RUNSCRIPT FROM 'ressources/sql/ImportVillesFr.sql'");
		
		updateReglements();
		sql.executeScript("../sql/ImportClubLFBTA.sql");
	}
}

if(dbVersion != org.concoursjeunes.ApplicationCore.DB_RELEASE_REQUIRED) {
	//mise à jour du numero de version de la base
	sql.executeScript("99-updatedbver.sql");
}