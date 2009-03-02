//mapping:
// int dbVersion - version de la base de donn�e
// SqlManager sql - moteur de base de donn�es

function updateReglements() {
	var contexte = JavaImporter(
			Packages.org.concoursjeunes,
			Packages.org.ajdeveloppement.commons,
			Packages.org.ajdeveloppement.commons.io,
			Packages.java.util,
			Packages.java.io);

	with(contexte) {
		//liste l'ensemble des fichiers de r�glements
		var updatePath = new File(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources"), "update");
		var reglements = FileUtils.listAllFiles(updatePath, ".*\\.reglement");
		var rManager = new ReglementManager();
		for(var i = 0; i < reglements.size(); i++) {
			rManager.importReglement(reglements.get(i));
		}
	}
}

if(dbVersion == 0) {
	//passe l'ensemble des scripts de base
	sql.executeScript("01-create_db.sql");
	sql.executeScript("02-defaut.sql");
	sql.executeScript("04-insertclub.sql");
} else if(dbVersion < 10) {
	var rowSet;
	
	//correction de l'enregistrement des r�glements
	rowSet = sql.executeQuery("SELECT * FROM REGLEMENT where NUMREGLEMENT=-909407998;");
	if(!rowSet.first()) {
		sql.executeScript("05-patch01.sql");
	}
	
	//ajout d'une colonne pour le classement par �quipe
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS " 
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='CRITERE' AND COLUMN_NAME='CLASSEMENTEQUIPE';");
	if(!rowSet.first()) {
		sql.executeUpdate("ALTER TABLE CRITERE ADD CLASSEMENTEQUIPE BOOLEAN DEFAULT FALSE BEFORE PLACEMENT;");
	}
	
	//ajout d'un numero d'ordre de critere
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS " 
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='CRITERE' AND COLUMN_NAME='NUMORDRE';");
	if(!rowSet.first()) {
		sql.executeUpdate("ALTER TABLE CRITERE ADD NUMORDRE INTEGER NOT NULL DEFAULT 1;");
		rowSet = sql.executeQuery("SELECT * FROM CRITERE ORDER BY NUMREGLEMENT;");
		while(rowSet.next()) {
			sql.executeUpdate("UPDATE CRITERE SET NUMORDRE=(SELECT MAX(NUMORDRE) + 1 FROM CRITERE WHERE NUMREGLEMENT="
				+ rowSet.getInt("NUMREGLEMENT") + ") WHERE NUMREGLEMENT=" + rowSet.getInt("NUMREGLEMENT") 
				+ " AND CODECRITERE='" + rowSet.getString("CODECRITERE") + "';");
		}
	}
	
	//ajout d'un numero d'ordre d'element de critere
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS " 
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='CRITEREELEMENT' AND COLUMN_NAME='NUMORDRE';");
	if(!rowSet.first()) {
		sql.executeUpdate("ALTER TABLE CRITEREELEMENT ADD NUMORDRE INTEGER NOT NULL DEFAULT 1;");
		rowSet = sql.executeQuery("SELECT * FROM CRITEREELEMENT ORDER BY NUMREGLEMENT, CODECRITERE;");
		while(rowSet.next()) {
			sql.executeUpdate("UPDATE CRITEREELEMENT SET NUMORDRE=(SELECT MAX(NUMORDRE) + 1 FROM CRITEREELEMENT WHERE NUMREGLEMENT="
				+ rowSet.getInt("NUMREGLEMENT") + " AND CODECRITERE='" + rowSet.getString("CODECRITERE") + "') WHERE NUMREGLEMENT=" + rowSet.getInt("NUMREGLEMENT") 
				+ " AND CODECRITERE='" + rowSet.getString("CODECRITERE") + "' AND CODECRITEREELEMENT='" + rowSet.getString("CODECRITEREELEMENT") + "';");
		}
	}
	
	//ajout de la gestion des blasons
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES "
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='BLASONS'");
	if(!rowSet.first()) {
		sql.executeUpdate("CREATE TABLE BLASONS ("
			+ "NUMBLASON INTEGER AUTO_INCREMENT NOT NULL,"
			+ "NOMBLASON VARCHAR(32) NOT NULL,"
			+ "HORIZONTAL_RATIO DOUBLE NOT NULL,"
			+ "VERTICAL_RATIO DOUBLE NOT NULL,"
			+ "NBARCHER INTEGER NOT NULL,"
			+ "NUMORDRE INT NOT NULL,"
			+ "IMAGE VARCHAR(64) NOT NULL DEFAULT '',"
			+ "PRIMARY KEY (NUMBLASON));");
		sql.executeUpdate("ALTER TABLE DISTANCESBLASONS ADD NUMBLASON INTEGER NOT NULL DEFAULT 1;");

		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE, IMAGE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 1, 1, 4, BLASONS, CONCAT(CONCAT('targetface_fita_', BLASONS), '.png') FROM DISTANCESBLASONS WHERE BLASONS > 60 ORDER BY BLASONS DESC;");
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE, IMAGE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 0.5, 1, 2, BLASONS, CONCAT(CONCAT('targetface_fita_', BLASONS), '.png') FROM DISTANCESBLASONS WHERE BLASONS > 40 AND BLASONS <= 60 ORDER BY BLASONS DESC;");
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE, IMAGE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 0.5, 0.5, 1, BLASONS, CONCAT(CONCAT('targetface_fita_', BLASONS), '.png') FROM DISTANCESBLASONS WHERE BLASONS <= 40 ORDER BY BLASONS DESC;");
		sql.executeUpdate("UPDATE DISTANCESBLASONS A SET NUMBLASON=(SELECT DISTINCT NUMBLASON FROM BLASONS B WHERE B.NUMORDRE = A.BLASONS);");
		
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE, IMAGE) VALUES ('Tri Spot \"Vegas\"', 0.5, 0.5, 1, 39, 'targetface_fita_trispot_vegas.png');");
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE, IMAGE) VALUES ('Tri Spot Vertical', 0.25, 1, 1, 24, 'targetface_fita_trispot.png');");
		
		sql.executeUpdate("ALTER TABLE DISTANCESBLASONS ADD FOREIGN KEY (NUMBLASON) REFERENCES BLASONS (NUMBLASON) ON UPDATE CASCADE ON DELETE CASCADE;");
	}
	
	//ajout des images de blason
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS "
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='BLASONS' AND COLUMN_NAME='IMAGE';");
	if(!rowSet.first()) {
		sql.executeUpdate("ALTER TABLE BLASONS ADD COLUMN IMAGE VARCHAR(64) NOT NULL DEFAULT ''");
		sql.executeUpdate("UPDATE BLASONS SET IMAGE=CONCAT(CONCAT('targetface_fita_', NUMORDRE), '.png') where IMAGE=''");
		sql.executeUpdate("UPDATE BLASONS SET IMAGE='targetface_fita_trispot_vegas.png' where NOMBLASON='Tri Spot \"Vegas\"'");
		sql.executeUpdate("UPDATE BLASONS SET IMAGE='targetface_fita_trispot.png' where NOMBLASON='Tri Spot Vertical'");
	}
		
	//ajout des ancrages de blasons
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES "
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='ANCRAGES_BLASONS';");
	if(!rowSet.first()) {
		sql.executeScript("05-patch02.sql");
	}
	
	//Ajout des index de recherche sur la table entite
	sql.executeUpdate("CREATE INDEX IF NOT EXISTS I_NOM_ENTITE ON ENTITE (NOMENTITE ASC);");
	sql.executeUpdate("CREATE INDEX IF NOT EXISTS I_VILLE_ENTITE ON ENTITE (VILLEENTITE ASC);");
	
	//Extension des r�glements au format ConcoursJeunes 2.1
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES "
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='SURCLASSEMENT';");
	if(!rowSet.first()) {
		sql.executeScript("05-patch03.sql");
	}
	sql.executeScript("02-defaut.sql");
}

if(dbVersion < 20) {
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES "
			+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='DEPARTAGE'");
	if(!rowSet.first()) {
		sql.executeUpdate("CREATE TABLE DEPARTAGE ("
			+ "NUMDEPARTAGE INTEGER AUTO_INCREMENT NOT NULL,"
			+ "NUMREGLEMENT INTEGER,"
			+ "FIELDNAME VARCHAR(64),"
			+ "PRIMARY KEY (NUMDEPARTAGE, NUMREGLEMENT),"
			+ "FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE);");
	}
	
	updateReglements();
}

if(dbVersion != org.concoursjeunes.ApplicationCore.DB_RELEASE_REQUIRED) {
	//mise � jour du numero de version de la base
	sql.executeScript("99-updatedbver.sql");
}