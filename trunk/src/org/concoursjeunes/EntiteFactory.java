/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
