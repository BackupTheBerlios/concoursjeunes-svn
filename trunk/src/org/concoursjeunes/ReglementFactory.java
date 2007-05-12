/*
 * Créé le 17/03/2007 à 11:10 pour ConcoursJeunes
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
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
 * <p>
 * Les réglements son stoqué dans la base de donnée. La présente fabrique
 * permet soit de créer un nouveau réglement, soit d'extraire un réglement
 * de la base en se basant sur son nom.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
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
	 * <p>
	 * Retourne le reglement qualifié par son nom en recherchant l'entrée
	 * dans la base de donnée. Si aucun réglement, celui ci est initialisé par défaut
	 * (équivalent à createReglement()).
	 * </p>
	 * <p>
	 * Pour fonctionner correctement, "ConcoursJeunes.dbConnection" dooit auparavent être
	 * correctement instancié.
	 * </p>
	 * 
	 * @param reglementName - le nom du reglement à retourner
	 * @return - le reglement retourné
	 */
	public static Reglement getReglement(String reglementName) {

		Reglement reglement = new Reglement();
		
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from REGLEMENT where NOMREGLEMENT='" + reglementName + "'");
			if(rs.first()) {
				int numreglment = rs.getInt("NUMREGLEMENT");
				
				reglement.setIdReglement(numreglment);
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
					
					criterion.setReglementParent(reglement);
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
						
						criterionElement.setCriterionParent(criterion);
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
		
		return reglement;
	}
}
