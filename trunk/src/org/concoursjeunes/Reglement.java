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

import java.io.File;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import ajinteractive.standard.utilities.sql.SqlParser;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Reglement {
	
	private int idReglement			= 0;
	
	private String name				= "default";
	
	private int nbSerie             = 2;
	private int nbVoleeParSerie     = 6;
	private int nbFlecheParVolee    = 3;
	private int nbMembresEquipe     = 4;
	private int nbMembresRetenu     = 3;

	private ArrayList<Criterion> listCriteria = new ArrayList<Criterion>();
	private Hashtable<CriteriaSet, DistancesEtBlason> correspondanceCriteriaSet_DB = new Hashtable<CriteriaSet, DistancesEtBlason>();
	
	private boolean officialReglement = false;
	
	public Reglement() {
		
	}
	
	public Reglement(String name) {
		this.name = name;
	}
	
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name name à définir
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return correspondanceCriteriaSet_DB
	 */
	public Hashtable<CriteriaSet, DistancesEtBlason> getCorrespondanceCriteriaSet_DB() {
		return correspondanceCriteriaSet_DB;
	}
	
	/**
	 * @param criteriaSet
	 * @return l'objet DistancesEtBlason correspondant au criteres en parametre
	 */
	public DistancesEtBlason getCorrespondanceCriteriaSet_DB(CriteriaSet criteriaSet) {
		for(CriteriaSet criteriaSet2 : correspondanceCriteriaSet_DB.keySet()) {
			if(criteriaSet.equals(criteriaSet2))
				return correspondanceCriteriaSet_DB.get(criteriaSet2);
		}
		return null;
	}

	/**
	 * @param correspondanceCriteriaSet_DB correspondanceCriteriaSet_DB à définir
	 */
	public void setCorrespondanceCriteriaSet_DB(
			Hashtable<CriteriaSet, DistancesEtBlason> correspondanceDifferentiationCriteria_DB) {
		this.correspondanceCriteriaSet_DB = correspondanceDifferentiationCriteria_DB;
	}
	
	/**
	 * 
	 * @param criteriaSet
	 * @param distancesEtBlason
	 */
	public void putCorrespondanceCriteriaSet_DB(CriteriaSet criteriaSet, DistancesEtBlason distancesEtBlason) {
		this.correspondanceCriteriaSet_DB.put(criteriaSet, distancesEtBlason);
	}

	/**
	 * @return listCriteria
	 */
	public ArrayList<Criterion> getListCriteria() {
		return listCriteria;
	}
	
	
	
	/**
	 * @param listCriteria the listCriteria to set
	 */
	public void setListCriteria(ArrayList<Criterion> listCriteria) {
		this.listCriteria = listCriteria;
	}

	/**
	 * Renvoi la politique de placement
	 * 
	 * @return Hashtable<String, Boolean> Renvoi le filtre de critere en place pour le placement des archers
	 */
	public Hashtable<Criterion, Boolean> getPlacementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for(Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isPlacement());
		}

		return filterCriteria;
	}
	
	/**
	 * Renvoi la politique de classement
	 * 
	 * @return Hashtable<String, Boolean> Renvoi le filtre de critere en place pour le classement des archers
	 */
	public Hashtable<Criterion, Boolean> getClassementFilter() {
		Hashtable<Criterion, Boolean> filterCriteria = new Hashtable<Criterion, Boolean>();
		for(Criterion criterion : listCriteria) {
			filterCriteria.put(criterion, criterion.isClassement());
		}

		return filterCriteria;
	}

	/**
	 * @return nbFlecheParVolee
	 */
	public int getNbFlecheParVolee() {
		return nbFlecheParVolee;
	}

	/**
	 * @param nbFlecheParVolee nbFlecheParVolee à définir
	 */
	public void setNbFlecheParVolee(int nbFlecheParVolee) {
		this.nbFlecheParVolee = nbFlecheParVolee;
	}

	/**
	 * @return nbMembresEquipe
	 */
	public int getNbMembresEquipe() {
		return nbMembresEquipe;
	}

	/**
	 * @param nbMembresEquipe nbMembresEquipe à définir
	 */
	public void setNbMembresEquipe(int nbMembresEquipe) {
		this.nbMembresEquipe = nbMembresEquipe;
	}

	/**
	 * @return nbMembresRetenu
	 */
	public int getNbMembresRetenu() {
		return nbMembresRetenu;
	}

	/**
	 * @param nbMembresRetenu nbMembresRetenu à définir
	 */
	public void setNbMembresRetenu(int nbMembresRetenu) {
		this.nbMembresRetenu = nbMembresRetenu;
	}

	/**
	 * @return nbSerie
	 */
	public int getNbSerie() {
		return nbSerie;
	}

	/**
	 * @param nbSerie nbSerie à définir
	 */
	public void setNbSerie(int nbSerie) {
		this.nbSerie = nbSerie;
	}

	/**
	 * @return nbVoleeParSerie
	 */
	public int getNbVoleeParSerie() {
		return nbVoleeParSerie;
	}

	/**
	 * @param nbVoleeParSerie nbVoleeParSerie à définir
	 */
	public void setNbVoleeParSerie(int nbVoleeParSerie) {
		this.nbVoleeParSerie = nbVoleeParSerie;
	}

	/**
	 * @return officialReglement
	 */
	public boolean isOfficialReglement() {
		return officialReglement;
	}

	/**
	 * @param officialReglement officialReglement à définir
	 */
	public void setOfficialReglement(boolean officialReglement) {
		this.officialReglement = officialReglement;
	}

	/**
	 * @return idReglement
	 */
	public int getIdReglement() {
		return idReglement;
	}

	/**
	 * @param idReglement idReglement à définir
	 */
	public void setIdReglement(int idReglement) {
		this.idReglement = idReglement;
	}

	public boolean isValidScore(ArrayList<Integer> scores) {
		boolean valid = true;
		for(int score : scores) {
			if(score > nbVoleeParSerie * nbFlecheParVolee * 10) {
				valid = false;
				break;
			}
		}
		return valid;
	}
	
	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			if(idReglement != 0) {
				stmt.executeUpdate("update Reglement set NOMREGLEMENT='" + name + "',"
						+ "NBSERIE=" + nbSerie + ",NBVOLEEPARSERIE=" + nbVoleeParSerie + ","
						+ "NBFLECHEPARVOLEE=" + nbFlecheParVolee + ", NBMEMBRESEQUIPE=" + nbMembresEquipe + ","
						+ "NBMEMBRESRETENU=" + nbMembresRetenu + ", ISOFFICIAL=" + ((officialReglement)?"TRUE":"FALSE")
						+ " WHERE NUMREGLEMENT=" + idReglement);
				stmt.executeQuery("delete from CRITERE where NUMREGLEMENT=" + idReglement);
				stmt.executeQuery("delete from DISTANCESBLASONS where NUMREGLEMENT=" + idReglement);
			} else {
				stmt.executeUpdate("insert into Reglement (NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE," +
						"NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL) " +
						"VALUES ('" + name + "'," + nbSerie + "," + nbVoleeParSerie + "," +
						nbFlecheParVolee + "," + nbMembresEquipe + "," +
						nbMembresRetenu + "," + ((officialReglement)?"TRUE":"FALSE") + ")", Statement.RETURN_GENERATED_KEYS);
				ResultSet clefs = stmt.getGeneratedKeys();
				if(clefs.first()){
				    idReglement = (Integer)clefs.getObject(1);  
				}
			}
			saveCriteria(stmt);
			saveDistancesAndBlasons(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*File fUserReglement = new File(ConcoursJeunes.userRessources.getReglementPathForUser()
				+ File.separator + "reglement_" + name + ".xml");
		AJToolKit.saveXMLStructure(fUserReglement, this, false);*/
	}
	
	private void saveCriteria(Statement stmt) throws SQLException {
		for(Criterion criterion : listCriteria) {
			stmt.executeUpdate("insert into CRITERE (CODECRITERE,NUMREGLEMENT,LIBELLECRITERE,SORTORDERCRITERE," +
					"CLASSEMENT,PLACEMENT,CODEFFTA) VALUES ('" + criterion.getCode() + "'," + 
					idReglement + ",'" + criterion.getLibelle() + "'," + 
					criterion.getSortOrder() + "," +
					((criterion.isClassement())?"TRUE":"FALSE") + "," +
					((criterion.isPlacement())?"TRUE":"FALSE") + ",'" +
					criterion.getCodeffta() + "')");
			for(CriterionElement criterionElement : criterion.getCriterionElements()) {
				stmt.executeUpdate("insert into CRITEREELEMENT (CODECRITEREELEMENT," +
						"CODECRITERE,NUMREGLEMENT,LIBELLECRITEREELEMENT,ACTIF) values (" +
						"'" + criterionElement.getCode() + "', '" + criterion.getCode() + "'," +
						"" + idReglement + ", '" + criterionElement.getLibelle() + "'," +
						((criterionElement.isActive())?"TRUE":"FALSE") + ")");
			}
		}
	}
	
	private void saveDistancesAndBlasons(Statement stmt) throws SQLException {
		
		for(Entry<CriteriaSet, DistancesEtBlason> entry : correspondanceCriteriaSet_DB.entrySet()) {
			CriteriaSet criteriaSet = entry.getKey();
			DistancesEtBlason distancesEtBlason = entry.getValue();
			int numdb = 0;
			
			stmt.executeUpdate("insert into DISTANCESBLASONS (NUMREGLEMENT, BLASONS) VALUES (" +
					idReglement + ", " + distancesEtBlason.getBlason() + ")", Statement.RETURN_GENERATED_KEYS);
			ResultSet clefs = stmt.getGeneratedKeys();
			if(clefs.first()){
				numdb = (Integer)clefs.getObject(1);  
			}
			
			for(Criterion criterion : criteriaSet.getCriteria().keySet()) {
				CriterionElement criterionElement = criteriaSet.getCriteria().get(criterion);
				stmt.executeUpdate("insert into CRITERIASET (NUMDISTANCESBLASONS, NUMREGLEMENT1, CODECRITEREELEMENT," +
						"CODECRITERE, NUMREGLEMENT2) VALUES (" +
						numdb + ", " + idReglement + ", '" + criterionElement.getCode() + "'," +
						"'" + criterion.getCode() + "', " + idReglement + ")");
			}

			for(int distance : distancesEtBlason.getDistance()) {
				stmt.executeUpdate("insert into DISTANCES (NUMDISTANCESBLASONS, NUMREGLEMENT," +
						"DISTANCE) VALUES (" + numdb + ", " + idReglement + ", " + distance + ")");
			}
		}
	}
	
	public boolean delete() {
		boolean success = false;
		/*if(!officialReglement) {
			File fUserReglement = new File(ConcoursJeunes.userRessources.getReglementPathForUser() 
					+ File.separator + "reglement_" + name + ".xml");
			success = fUserReglement.delete();
		}*/
		if(!officialReglement) {
			try {
				Statement stmt = ConcoursJeunes.dbConnection.createStatement();
				
				stmt.executeUpdate("delete from REGLEMENT where NUMREGLEMENT=" + idReglement);
				
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public static String[] listAvailableReglements() {
		ArrayList<String> availableReglements = new ArrayList<String>();
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery("select NOMREGLEMENT from REGLEMENT");

			while(rs.next()) {
				availableReglements.add(rs.getString("NOMREGLEMENT"));
			}
			rs.close();
			
			String[] newReglements = new File("config/reglements").list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if(name.endsWith(".sql"))
						return true;
					return false;
				}
			});
			for(String reglementName : newReglements) {
				String name = new File(reglementName).getName();
				name = name.substring(0, name.length() - 4);
				
				if(!availableReglements.contains(name)) {
					SqlParser.createBatch(new File("config/reglements" + File.separator + reglementName), stmt);
					
					stmt.executeBatch();
					availableReglements.add(name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return availableReglements.toArray(new String[availableReglements.size()]);
	}
}
