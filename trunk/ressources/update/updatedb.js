//mapping:
// int dbVersion - version de la base de donnée
// SqlManager sql - moteur de base de données

function updateReglements() {
	var contexte = JavaImporter(
			Packages.org.concoursjeunes,
			Packages.org.ajdeveloppement.commons,
			Packages.org.ajdeveloppement.commons.io,
			Packages.java.util,
			Packages.java.io);

	with(contexte) {
		//liste l'ensemble des fichiers de réglements
		var updatePath = new File(ApplicationCore.staticParameters.getResourceString("path.ressources"), "update");
		var reglements = FileUtils.listAllFiles(updatePath, ".*\\.reglement");
		var rManager = new ReglementManager();
		for(var i = 0; i < reglements.size(); i++) {
			rManager.importReglement(reglements.get(i));
		}
		sql.executeUpdate("update REGLEMENT set NUMREGLEMENT=0 where NOMREGLEMENT='defaut'")
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
	
	sql.executeUpdate("ALTER TABLE ARCHERS ADD DATEMODIF DATE DEFAULT CURRENT_DATE()");
	sql.executeUpdate("ALTER TABLE ENTITE ADD DATEMODIF DATE DEFAULT CURRENT_DATE()");
	
	sql.executeScript("../sql/ImportClubFFTA.sql");
	
	updateReglements();
}

if(dbVersion != org.concoursjeunes.ApplicationCore.DB_RELEASE_REQUIRED) {
	//mise à jour du numero de version de la base
	sql.executeScript("99-updatedbver.sql");
}