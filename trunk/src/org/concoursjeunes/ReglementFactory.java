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
		return getReglement(-1, reglementName);
	}
	
	public static Reglement getReglement(int numreglement) {
		return getReglement(numreglement, null);
	}
	
	private static Reglement getReglement(int numreglement, String reglementName) {

		Reglement reglement = new Reglement();
		
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			String sql = "";
			if(numreglement > -1)
				sql = "select * from REGLEMENT where NUMREGLEMENT=" + numreglement;
			else
				sql = "select * from REGLEMENT where NOMREGLEMENT='" + reglementName + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
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
				rs = stmt.executeQuery("select CODECRITERE from CRITERE where NUMREGLEMENT=" + numreglment);
				while(rs.next()) {
					criteria.add(CriterionFactory.getCriterion(rs.getString("CODECRITERE"), reglement));
				}
				rs.close();
				reglement.setListCriteria(criteria);
				
				ArrayList<DistancesEtBlason> listDistancesEtBlason = new ArrayList<DistancesEtBlason>();
				rs = stmt.executeQuery("select * from DISTANCESBLASONS where NUMREGLEMENT=" + numreglment);
				while(rs.next()) {
					int numdb = rs.getInt("NUMDISTANCESBLASONS");
					
					listDistancesEtBlason.add(DistancesEtBlasonFactory.getDistancesEtBlason(numdb, reglement));
				}
				rs.close();
				reglement.setListDistancesEtBlason(listDistancesEtBlason);
			}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reglement;
	}
}
