/**
 * 
 */
package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ConcurrentPersistant {
	private Concurrent concurrent = null;

	public ConcurrentPersistant(Concurrent concurrent) {
		this.concurrent = concurrent;
	}

	/**
	 * @return concurrent
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * @param concurrent concurrent à définir
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
	}
	
	public void makePersistant() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery(
					"select * from distinguer where NUMLICENCEARCHER='" + concurrent.getNumLicenceArcher() + "'");
			if(rs.first()) {
				stmt.executeUpdate("update distinguer set CODECRITERE=,CODECRITEREELEMENT=,REGLEMENT=");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
