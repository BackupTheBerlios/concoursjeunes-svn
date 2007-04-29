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
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ReglementFactory {
	/**
	 * Crée un nouveau reglement de concours
	 * 
	 * @return le reglement créer
	 */
	public static Reglement createReglement() {
		return new Reglement();
	}
	
	/**
	 * Retourne le reglement qualifié par son nom
	 * 
	 * @param reglementName - le nom du reglement à retourné
	 * @return - le reglement retourné
	 */
	public static Reglement getReglement(String reglementName) {

		Reglement reglement = new Reglement();
		
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from REGLEMENT where NOMREGLEMENT='" + reglementName + "'");
			if(rs.first()) {
				int numreglment = rs.getInt("NUMREGLEMENT");
				
				reglement.setName(rs.getString("NOMREGLEMENT"));
				reglement.setNbSerie(rs.getInt("NBSERIE"));
				reglement.setNbVoleeParSerie(rs.getInt("NBVOLEEPARSERIE"));
				reglement.setNbFlecheParVolee(rs.getInt("NBFLECHEPARVOLEE"));
				reglement.setNbMembresEquipe(rs.getInt("NBMEMBRESEQUIPE"));
				reglement.setNbMembresRetenu(rs.getInt("NBMEMBRESRETENU"));
				reglement.setOfficialReglement(rs.getBoolean("ISOFFICIAL"));
				
				rs.close();
				
				ArrayList<Criterion> criteria = new ArrayList<Criterion>();
				rs = stmt.executeQuery("select * from CRITERE where NUMREGLEMENT=" + numreglment);
				while(rs.next()) {
					Criterion criterion = new Criterion();
					
					criterion.setCode(rs.getString("CODECRITERE"));
					criterion.setLibelle(rs.getString("LIBELLECRITERE"));
					criterion.setSortOrder(rs.getInt("SORTORDERCRITERE"));
					criterion.setClassement(rs.getBoolean("CLASSEMENT"));
					criterion.setPlacement(rs.getBoolean("PLACEMENT"));
					criterion.setCodeffta(rs.getString("CODEFFTA"));
					
					ArrayList<CriterionElement> criterionElements = new ArrayList<CriterionElement>();
					Statement stmt2 = ConcoursJeunes.dbConnection.createStatement();
					ResultSet rs2 = stmt2.executeQuery("select * from CRITEREELEMENT where " +
							"CODECRITERE='" + criterion.getCode() + "' and NUMREGLEMENT=" + numreglment);
					while(rs2.next()) {
						CriterionElement criterionElement = new CriterionElement();
						
						criterionElement.setCode(rs2.getString("CODECRITEREELEMENT"));
						criterionElement.setLibelle(rs2.getString("LIBELLECRITEREELEMENT"));
						criterionElement.setActive(rs2.getBoolean("ACTIF"));
						
						criterionElements.add(criterionElement);
					}
					stmt2.close();
					criterion.setCriterionElements(criterionElements);
					
					criteria.add(criterion);
				}
				rs.close();
				reglement.setListCriteria(criteria);
				
				Hashtable<CriteriaSet, DistancesEtBlason> correspondanceDifferentiationCriteria_DB = new Hashtable<CriteriaSet, DistancesEtBlason>();
				rs = stmt.executeQuery("select * from DISTANCESBLASONS where NUMREGLEMENT=" + numreglment);
				while(rs.next()) {
					Statement stmt2 = ConcoursJeunes.dbConnection.createStatement();
					
					int numdb = rs.getInt("NUMDISTANCESBLASONS");
					
					DistancesEtBlason distancesEtBlason = new DistancesEtBlason();
					ArrayList<Integer> distances = new ArrayList<Integer>();
					ResultSet rs2 = stmt2.executeQuery("select * from DISTANCES " +
							"where NUMDISTANCESBLASONS=" + numdb + " and NUMREGLEMENT=" + numreglment);
					while(rs2.next()) {
						distances.add(rs2.getInt("DISTANCE"));
					}
					rs2.close();
					int[] iDistances = new int[distances.size()];
					for(int i = 0; i < distances.size(); i++) {
						iDistances[i] = distances.get(i);
					}
					distancesEtBlason.setDistance(iDistances);
					distancesEtBlason.setBlason(rs.getInt("BLASONS"));
					
					CriteriaSet criteriaSet = new CriteriaSet();
					rs2 = stmt2.executeQuery("select critere.codecritere, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, " +
							"critereelement.codecritereelement, LIBELLECRITEREELEMENT, ACTIF " +
							"from CRITERIASET, CRITERE, CRITEREELEMENT where " +
							"criteriaset.codecritere = critere.codecritere and criteriaset.numreglement2 = critere.numreglement and " +
							"criteriaset.codecritere = critereelement.codecritere and criteriaset.codecritereelement = critereelement.codecritereelement and criteriaset.numreglement2 = critereelement.numreglement and " +
							"NUMDISTANCESBLASONS=" + numdb + 
							"and NUMREGLEMENT1=" + numreglment);
					while(rs2.next()) {
						Criterion criterion = new Criterion();
						
						criterion.setCode(rs2.getString("CODECRITERE"));
						criterion.setLibelle(rs2.getString("LIBELLECRITERE"));
						criterion.setSortOrder(rs2.getInt("SORTORDERCRITERE"));
						criterion.setClassement(rs2.getBoolean("CLASSEMENT"));
						criterion.setPlacement(rs2.getBoolean("PLACEMENT"));
						criterion.setCodeffta(rs2.getString("CODEFFTA"));
						
						CriterionElement criterionElement = new CriterionElement();
						
						criterionElement.setCode(rs2.getString("codecritereelement"));
						criterionElement.setLibelle(rs2.getString("LIBELLECRITEREELEMENT"));
						criterionElement.setActive(rs2.getBoolean("ACTIF"));
						
						criteriaSet.getCriteria().put(criterion, criterionElement);
					}
					rs2.close();
					correspondanceDifferentiationCriteria_DB.put(criteriaSet, distancesEtBlason);
					
					stmt2.close();
				}
				rs.close();
				reglement.setCorrespondanceCriteriaSet_DB(correspondanceDifferentiationCriteria_DB);
			}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//reglement.se
		
		/*File fReglement = null;
		
		File fOfficialReglement = new File("config" + File.separator + "reglement" 
				+ File.separator + "reglement_" + reglementName + ".xml");
		if(fOfficialReglement.exists()) {
			fReglement = fOfficialReglement;
		} else {
			File fUserReglement = new File(ConcoursJeunes.userRessources.getReglementPathForUser() 
					+ File.separator + "reglement_" + reglementName + ".xml");
			if(fUserReglement.exists())
				fReglement = fUserReglement;
		}
		
		if(fReglement != null) {
			reglement = (Reglement)AJToolKit.loadXMLStructure(fReglement, false);
		}*/
		
		return reglement;
	}
}
