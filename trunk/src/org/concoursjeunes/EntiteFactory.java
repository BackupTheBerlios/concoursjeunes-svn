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
public class EntiteFactory {
	public static Entite getEntite(String numAgrement) {
		Entite entite = new Entite();
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("select * from Entite where AgrementEntite = '" + numAgrement + "'");

			if(rs.next()) {
				entite.setAgrement(rs.getString("AgrementEntite"));
				entite.setNom(rs.getString("NomEntite"));
				entite.setAdresse(rs.getString("AdresseEntite"));
				entite.setCodePostal(rs.getString("CodePostalEntite"));
				entite.setVille(rs.getString("VilleEntite"));
				entite.setNote(rs.getString("NoteEntite"));
				entite.setType(rs.getInt("TypeEntite"));
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return entite;
	}
}
