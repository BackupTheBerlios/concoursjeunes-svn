/*
 * Créé le 3 mai 2009 à 17:32:56 pour ConcoursJeunes
 *
 * Copyright 2002-2009 - Aurélien JEOFFRAY
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ajdeveloppement.commons.persistence.LoadHelper;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.sql.ResultSetLoadHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlLoadHandler;
import org.ajdeveloppement.concours.cache.FederationCache;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.CompetitionLevel;
import org.concoursjeunes.Federation;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class FederationBuilder {
	
	private static LoadHelper<Federation,Map<String,Object>> loadHelper;
	private static LoadHelper<Federation,ResultSet> resultSetLoadHelper;
	static {
		try {
			loadHelper = new LoadHelper<Federation,Map<String,Object>>(new SqlLoadHandler<Federation>(ApplicationCore.dbConnection, Federation.class));
			resultSetLoadHelper = new LoadHelper<Federation, ResultSet>(new ResultSetLoadHandler<Federation>(Federation.class));
		} catch(ObjectPersistenceException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retourne la fédération qualifié par son identifiant en base
	 * 
	 * @param numFederation l'id de la fédération
	 * @return la fédération a retourner
	 * @throws ObjectPersistenceException
	 */
	public static Federation getFederation(int numFederation) throws ObjectPersistenceException {
		return getFederation(numFederation, null);
	}
	
	/**
	 * Construit la fédération a partir du jeux de résultat correspondant
	 * 
	 * @param rs le resultset contenant les enregistrement en base permettant de construire la fédération
	 * @return la fédération a retourner
	 * @throws ObjectPersistenceException
	 */
	public static Federation getFederation(ResultSet rs) throws ObjectPersistenceException {
		return getFederation(0, rs);
	}
	
	/**
	 * 
	 * @param numFederation
	 * @param rs
	 * @return
	 * @throws ObjectPersistenceException
	 */
	@SuppressWarnings("nls")
	private static Federation getFederation(int numFederation, ResultSet resultSet) throws ObjectPersistenceException {
		Federation federation = null;
		
		if(resultSet != null) {
			try {
				federation = FederationCache.getInstance().get(resultSet.getInt("FEDERATION.NUMFEDERATION"));
			} catch (SQLException e) {
				throw new ObjectPersistenceException(e);
			}
		} else
			federation = FederationCache.getInstance().get(numFederation);
		
		if(federation == null) {
			federation = new Federation();
			if(resultSet == null) {
				federation.setNumFederation(numFederation);
				
				loadHelper.load(federation);
			} else {
				resultSetLoadHelper.load(federation, resultSet);
			}
		
			try {
				Statement stmt = ApplicationCore.dbConnection.createStatement();
				try {
					//ResultSet rs = stmt.executeQuery(sql);
					ResultSet rs = null;
					try {
						//retourne les langues disponible pour les niveaux de compétition
						List<String> langs = new ArrayList<String>();
						String sql = "select distinct LANG from NIVEAU_COMPETITION where NUMFEDERATION=" + numFederation;
						rs = stmt.executeQuery(sql);
						while(rs.next()) {
							langs.add(rs.getString(1));
						}
						rs.close();
						
						//construit les niveaux
						List<CompetitionLevel> competitionLevels = new ArrayList<CompetitionLevel>();
						sql = "select distinct CODENIVEAU from NIVEAU_COMPETITION where NUMFEDERATION=" + numFederation;
						rs = stmt.executeQuery(sql);
						while(rs.next()) {
							for(String lang : langs) {
								competitionLevels.add(CompetitionLevelBuilder.getCompetitionLevel(
										rs.getInt(1), federation, lang));
							}
						}
						federation.setCompetitionLevels(competitionLevels);
						
						FederationCache.getInstance().add(federation);
					} finally {
						if(rs != null)
							rs.close();
					}
				} finally {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new ObjectPersistenceException(e);
			}
		}
		
		return federation;
	}
}
