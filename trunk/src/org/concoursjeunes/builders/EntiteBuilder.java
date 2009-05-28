/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
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
package org.concoursjeunes.builders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Entite;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class EntiteBuilder {
	/**
	 * Construit une entite à partir des informations en base
	 * 
	 * @param numAgrement le numero d'agrement de l'entite à construire
	 * 
	 * @return l'entite construite
	 */
	public static Entite getEntite(String numAgrement) {
		Entite entite = new Entite();
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("select * from Entite where AgrementEntite = '" + numAgrement + "'"); //$NON-NLS-1$ //$NON-NLS-2$

			if(rs.next()) {
				entite.setAgrement(rs.getString("AgrementEntite")); //$NON-NLS-1$
				entite.setNom(rs.getString("NomEntite")); //$NON-NLS-1$
				entite.setAdresse(rs.getString("AdresseEntite")); //$NON-NLS-1$
				entite.setCodePostal(rs.getString("CodePostalEntite")); //$NON-NLS-1$
				entite.setVille(rs.getString("VilleEntite")); //$NON-NLS-1$
				entite.setNote(rs.getString("NoteEntite")); //$NON-NLS-1$
				entite.setType(rs.getInt("TypeEntite")); //$NON-NLS-1$
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return entite;
	}
}
