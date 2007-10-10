//mapping:
// int dbVersion
// [type à définir] sql - moteur de base de donnée

if(dbVersion == 0) {
	sql.executeScript("01-create_db.sql");
	sql.executeScript("02-federal.sql");
	sql.executeScript("02-savoie.sql");
	sql.executeScript("04-insertclub.sql");
	sql.executeScript("99-updatedbver.sql");
} else {
	var rowSet = sql.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS" 
		+ "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='CRITERE' AND COLUMN_NAME='CLASSEMENTEQUIPE';");
	if(rowSet.first()) {
		sql.executeUpdate("ALTER TABLE CRITERE ADD CLASSEMENTEQUIPE BOOLEAN DEFAULT FALSE BEFORE PLACEMENT;");
	}
}