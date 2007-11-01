//mapping:
// int dbVersion
// SqlManager sql - moteur de base de donn�e

if(dbVersion == 0) {
	sql.executeScript("01-create_db.sql");
	sql.executeScript("02-defaut.sql");
	sql.executeScript("02-federal.sql");
	sql.executeScript("02-savoie.sql");
	sql.executeScript("04-insertclub.sql");
	sql.executeScript("99-updatedbver.sql");
} else {
	var rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS " 
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='CRITERE' AND COLUMN_NAME='CLASSEMENTEQUIPE';");
	if(!rowSet.first()) {
		sql.executeUpdate("ALTER TABLE CRITERE ADD CLASSEMENTEQUIPE BOOLEAN DEFAULT FALSE BEFORE PLACEMENT;");
	}
	
	var rowSet = sql.executeQuery("SELECT * FROM REGLEMENT WHERE NUMREGLEMENT=0;");
	if(!rowSet.first()) {
		sql.executeScript("02-defaut.sql");
	}
	
	var rowSet = sql.executeQuery("SELECT * FROM CRITEREELEMENT WHERE CODECRITEREELEMENT='AD' and CODECRITERE='arc' and NUMREGLEMENT in(-1825540830, -180676679);");
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
}