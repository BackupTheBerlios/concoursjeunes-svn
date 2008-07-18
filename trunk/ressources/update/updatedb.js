//mapping:
// int dbVersion
// SqlManager sql - moteur de base de donnï¿½es

if(dbVersion == 0) {
	//passe l'ensemble des scripts de base
	sql.executeScript("01-create_db.sql");
	sql.executeScript("02-defaut.sql");
	sql.executeScript("02-federal.sql");
	sql.executeScript("02-savoie.sql");
	sql.executeScript("02-2x18m.sql");
	sql.executeScript("02-2x25m.sql");
	sql.executeScript("04-insertclub.sql");
	sql.executeScript("99-updatedbver.sql");
} else if(dbVersion < 10) {
	var rowSet;
	
	//correction de l'enregistrement des réglements
	rowSet = sql.executeQuery("SELECT * FROM REGLEMENT where NUMREGLEMENT=-909407998;");
	if(!rowSet.first()) {
		sql.executeScript("05-patch01.sql");
	}
	
	//ajout d'une colonne pour le classement par équipe
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

		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 1, 1, 4, BLASONS FROM DISTANCESBLASONS WHERE BLASONS > 60 ORDER BY BLASONS DESC;");
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 0.5, 1, 2, BLASONS FROM DISTANCESBLASONS WHERE BLASONS > 40 AND BLASONS <= 60 ORDER BY BLASONS DESC;");
		sql.executeUpdate("INSERT INTO BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO, NBARCHER, NUMORDRE) "
			+ "SELECT DISTINCT CONCAT(BLASONS, 'cm') AS NOMBLASON, 0.5, 0.5, 1, BLASONS FROM DISTANCESBLASONS WHERE BLASONS <= 40 ORDER BY BLASONS DESC;");
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
	
	//ajout du réglement de référence
	rowSet = sql.executeQuery("SELECT * FROM REGLEMENT WHERE NUMREGLEMENT=0;");
	if(!rowSet.first()) {
		sql.executeScript("02-defaut.sql");
	}
	
	//ajout d'arcs supplï¿½mentaire sur les 2 rï¿½glements de base
	rowSet = sql.executeQuery("SELECT * FROM CRITEREELEMENT WHERE CODECRITEREELEMENT='AD' and CODECRITERE='arc' and NUMREGLEMENT in(-1825540830, -180676679);");
	if(!rowSet.first()) {
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('AD', 'arc', -1825540830, 'Arc droit', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('AC', 'arc', -1825540830, 'Arc chasse', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('BB', 'arc', -1825540830, 'Bare Bow', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('TL', 'arc', -1825540830, 'Tir Libre', TRUE);");
		
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('AD', 'arc', -180676679, 'Arc droit', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('AC', 'arc', -180676679, 'Arc chasse', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('BB', 'arc', -180676679, 'Bare Bow', TRUE);");
		sql.executeUpdate("MERGE INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('TL', 'arc', -180676679, 'Tir Libre', TRUE);");
	}
	
	//Ajout des index de recherche sur la table entite
	sql.executeUpdate("CREATE INDEX IF NOT EXISTS I_NOM_ENTITE ON ENTITE (NOMENTITE ASC);");
	sql.executeUpdate("CREATE INDEX IF NOT EXISTS I_VILLE_ENTITE ON ENTITE (VILLEENTITE ASC);");
	
	//Ajout du tableau des surclassements
	rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES "
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='SURCLASSEMENT';");
	if(!rowSet.first()) {
		sql.executeUpdate("CREATE TABLE SURCLASSEMENT ("
			+ "NUMCRITERIASET INTEGER NOT NULL,"
			+ "NUMREGLEMENT INTEGER NOT NULL,"
			+ "NUMCRITERIASET_SURCLASSE INTEGER NULL,"
			+ "PRIMARY KEY (NUMCRITERIASET, NUMREGLEMENT),"
			+ "FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE,"
			+ "FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,"
			+ "FOREIGN KEY (NUMCRITERIASET_SURCLASSE) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE);");
	}
	
	sql.executeScript("02-2x18m.sql");
	sql.executeScript("02-2x25m.sql");
}

if(dbVersion != org.concoursjeunes.ApplicationCore.DB_RELEASE_REQUIRED) {
	//mise ï¿½ jour du numero de version de la base
	sql.executeScript("99-updatedbver.sql");
}