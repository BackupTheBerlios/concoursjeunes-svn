/**
 * 
 */
package org.concoursjeunes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author aurelien
 *
 */
public class DatabaseManager {

	private Connection dbConnection;

	/**
	 * 
	 *
	 */
	public DatabaseManager() {
		try {
			//chargement du driver
			Class.forName("org.hsqldb.jdbcDriver").newInstance();

			dbConnection = DriverManager.getConnection("jdbc:hsqldb:file:base/concoursjeunesdb;shutdown=true", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public Concurrent getFirstArcherWith(String sqlfilter, String orderfields, Reglement reglement) {
		Concurrent concurrent = null;

		try {
			Statement stmt = dbConnection.createStatement();

			String sql = "select * from archers where " + sqlfilter + " order by " + orderfields + " LIMIT 1";

			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()) {
				concurrent = getConcurrentInCurrentResultSet(rs, reglement);
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return concurrent;
	}

	/**
	 * 
	 * 
	 * @return ArrayList<Concurrent> - tous les archers de la base
	 */
	public ArrayList<Concurrent> getAllArchers(String filter, String orderfield, Reglement reglement) {

		ArrayList<Concurrent> archers = new ArrayList<Concurrent>();

		try {
			Statement stmt = dbConnection.createStatement();

			String sql = "select * from archers";

			if(filter != null) {
				sql += " where " + filter;
			}

			if(orderfield != null) {
				sql += " order by " + orderfield;
			}

			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				Concurrent concurrent = getConcurrentInCurrentResultSet(rs, reglement);

				archers.add(concurrent);
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return archers;
	}

	/**
	 * @param numAgrement
	 * @return
	 */
	public Entite getEntite(String numAgrement) {
		Entite entite = new Entite();
		try {
			Statement stmt = dbConnection.createStatement();

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
	
	public Entite getFirstEntiteWith(String sqlfilter, String orderfields) {
		Entite entite = null;
		try {
			Statement stmt = dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("select * from Entite where " + sqlfilter + " order by " + orderfields);

			if(rs.next()) {
				entite = new Entite();
				
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

	/**
	 * @param rs
	 * @return
	 */
	private Concurrent getConcurrentInCurrentResultSet(ResultSet rs, Reglement reglement) {
		Concurrent concurrent = new Concurrent();

		try {
			concurrent.setCertificat(rs.getBoolean("CERTIFMEDICAL"));
			concurrent.setClub(getEntite(rs.getString("AGREMENTENTITE")));

			CriteriaSet differentiationCriteria = new CriteriaSet();

			for(Criterion key : reglement.getListCriteria()) {
				if(!key.getCodeffta().equals("")) {
					ArrayList<CriterionElement> arrayList = key.getCriterionElements();
					int valindex = rs.getInt(key.getCodeffta() + "FFTA");
					if(valindex >= arrayList.size())
						valindex = arrayList.size() - 1;
					if(valindex < 0)
						valindex = 0;
					differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
				}
			}

			concurrent.setCriteriaSet(differentiationCriteria);
			concurrent.setNumLicenceArcher(rs.getString("NUMLICENCEARCHER"));
			concurrent.setNomArcher(rs.getString("NOMARCHER"));
			concurrent.setPrenomArcher(rs.getString("PRENOMARCHER"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return concurrent;
	}
}
