/*
 * Créé le 17/03/2007 à 11:10 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ajdeveloppement.commons.persistence.LoadHelper;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.sql.ResultSetLoadHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlLoadHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.manager.ReglementManager;

/**
 * <p>
 * Les régalements son stocké dans la base de donnée. La présente fabrique
 * permet soit de créer un nouveau règlement, soit d'extraire un règlement
 * de la base en se basant sur son nom.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 *
 */
public class ReglementBuilder {
	
	private static LoadHelper<Reglement,Map<String,Object>> loadHelper;
	private static LoadHelper<Reglement,ResultSet> resultSetLoadHelper;
	static {
		try {
			loadHelper = new LoadHelper<Reglement,Map<String,Object>>(new SqlLoadHandler<Reglement>(ApplicationCore.dbConnection, Reglement.class));
			resultSetLoadHelper = new LoadHelper<Reglement,ResultSet>(new ResultSetLoadHandler<Reglement>(Reglement.class));
		} catch(ObjectPersistenceException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Crée un nouveau règlement de concours
	 * 
	 * @return le règlement créer
	 */
	public static Reglement createReglement() {
		return getDefaultReglement(); 
	}
	
	/**
	 * <p>
	 * Retourne le règlement qualifié par son nom en recherchant l'entrée
	 * dans la base de donnée. Si aucun règlement, celui ci est initialisé par défaut
	 * (équivalent à createReglement()).
	 * </p>
	 * <p>
	 * Pour fonctionner correctement, "ConcoursJeunes.dbConnection" doit auparavant être
	 * correctement instancié.
	 * </p>
	 * 
	 * @param reglementName - le nom du règlement à retourner
	 * @return - le règlement retourné
	 */
	@Deprecated
	public static Reglement getReglement(String reglementName) {
		ReglementManager reglementManager = new ReglementManager();
		
		return reglementManager.getReglementByName(reglementName);
	}
	
	/**
	 * <p>
	 * Retourne le règlement identifié par son numéro dans la base.
	 * Si aucun régalement ne correspond au numéro, celui ci est initialisé par défaut
	 * (équivalent à createReglement()).
	 * </p>
	 * <p>
	 * Pour fonctionner correctement, "ConcoursJeunes.dbConnection" doit auparavant être
	 * correctement instancié.
	 * </p>
	 * 
	 * @param numreglement le numéro du règlement à construire
	 * 
	 * @return le régalement construit à partir du numéro
	 */
	public static Reglement getReglement(int numreglement) throws ObjectPersistenceException {
		return getReglement(numreglement, null, false);
	}
	
	/**
	 * <p>
	 * Retourne le règlement identifié par son numéro dans la base.
	 * Si aucun régalement ne correspond au numéro, celui ci est initialisé par défaut
	 * (équivalent à createReglement()).
	 * </p>
	 * <p>
	 * Pour fonctionner correctement, "ConcoursJeunes.dbConnection" doit auparavant être
	 * correctement instancié.
	 * </p>
	 * 
	 * @param numreglement le numéro du règlement à construire
	 * @param doNotUseCache	ne pas utiliser le cache pour le chargement
	 * 
	 * @return le régalement construit à partir du numéro
	 */
	public static Reglement getReglement(int numreglement, boolean doNotUseCache) throws ObjectPersistenceException {
		return getReglement(numreglement, null, doNotUseCache);
	}
	
	/**
	 * Injecte les données du resultset d'une table reglement dans
	 * un objet.
	 * 
	 * @param rs le jeux de résultat à injecter dans une instance réglement
	 * 
	 * @return le réglement construit à partir du jeux de résultat
	 * @throws ObjectPersistenceException
	 */
	public static Reglement getReglement(ResultSet rs)
			throws ObjectPersistenceException {
		return getReglement(-1, rs, false);
	}
	
	/**
	 * Injecte les données du resultset d'une table reglement dans
	 * un objet.
	 * 
	 * @param rs le jeux de résultat à injecter dans une instance réglement
	 * @param doNotUseCache	ne pas utiliser le cache pour le chargement
	 * 
	 * @return le réglement construit à partir du jeux de résultat
	 * @throws ObjectPersistenceException
	 */
	public static Reglement getReglement(ResultSet rs, boolean doNotUseCache)
			throws ObjectPersistenceException {
		return getReglement(-1, rs, doNotUseCache);
	}
	
	private static Reglement getReglement(int numreglement, ResultSet rs, boolean doNotUseCache)
			throws ObjectPersistenceException {

		Reglement reglement = new Reglement();
		reglement.setVersion(Reglement.CURRENT_VERSION);
		
		try {
			Map<Class<?>, Map<String,Object>> foreignKeys = null;
			if(rs != null) {
				foreignKeys = resultSetLoadHelper.load(reglement, rs);
				
				numreglement = reglement.getNumReglement();
			} else {
				reglement.setNumReglement(numreglement);
				
				foreignKeys = loadHelper.load(reglement);
			}
			
			reglement.setFederation(
					FederationBuilder.getFederation(
							(Integer)foreignKeys.get(Reglement.class).get("NUMFEDERATION"))); //$NON-NLS-1$
			
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			try {
				// Récupération des départages
				List<String> ties = new ArrayList<String>();
				rs = stmt.executeQuery("select * from DEPARTAGE where NUMREGLEMENT=" + numreglement + " order by NUMDEPARTAGE");  //$NON-NLS-1$//$NON-NLS-2$
				try {
					while(rs.next()) {
						ties.add(rs.getString("FIELDNAME")); //$NON-NLS-1$
					}
				} finally {
					rs.close();
				}
				reglement.setTie(ties);
				
				// Récupération des critères
				List<Criterion> criteria = new ArrayList<Criterion>();
				rs = stmt.executeQuery("select * from CRITERE where NUMREGLEMENT=" + numreglement + " order by NUMORDRE"); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					while(rs.next()) {
						criteria.add(CriterionBuilder.getCriterion(reglement, rs, doNotUseCache));
					}
				} finally {
					rs.close();
				}
				reglement.setListCriteria(criteria);
				
				// Récupération des distances blason
				List<DistancesEtBlason> listDistancesEtBlason = new ArrayList<DistancesEtBlason>();
				rs = stmt.executeQuery("select * from DISTANCESBLASONS where NUMREGLEMENT=" + numreglement); //$NON-NLS-1$
				try {
					while(rs.next()) {
						DistancesEtBlason db = DistancesEtBlasonBuilder.getDistancesEtBlason(reglement, rs, doNotUseCache);
						CriteriaSet[] criteriaSets = CriteriaSet.listCriteriaSet(reglement, reglement.getPlacementFilter());
						for(CriteriaSet criteriaSet : criteriaSets) {
							if(criteriaSet.equals(db.getCriteriaSet().getFilteredCriteriaSet(reglement.getPlacementFilter()))) {
								listDistancesEtBlason.add(db);
								break;
							}
						}
					}
				} finally {
					rs.close();
				}
				reglement.setListDistancesEtBlason(listDistancesEtBlason);
				
				// Récupération des surclassements
				Map<CriteriaSet, CriteriaSet> surclassement = new HashMap<CriteriaSet, CriteriaSet>();
				rs = stmt.executeQuery("select * from SURCLASSEMENT where NUMREGLEMENT=" + numreglement); //$NON-NLS-1$
				try {
					while (rs.next()) {
						int numCriteriaSet = rs.getInt("NUMCRITERIASET"); //$NON-NLS-1$
						int numCriteriaSetSurClasse = rs.getInt("NUMCRITERIASET_SURCLASSE"); //$NON-NLS-1$
						
						CriteriaSet criteriaSet = CriteriaSetBuilder.getCriteriaSet(numCriteriaSet, reglement, doNotUseCache);
						CriteriaSet criteriaSetSurClasse = null;
						if(!rs.wasNull()) {
							criteriaSetSurClasse = CriteriaSetBuilder.getCriteriaSet(numCriteriaSetSurClasse, reglement, doNotUseCache);
						}
						
						surclassement.put(criteriaSet, criteriaSetSurClasse);
					}
				} finally {
					rs.close();
				}
				reglement.setSurclassement(surclassement);
			} finally {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reglement;
	}
	
	private static Reglement getDefaultReglement() {
		Reglement reglement = new Reglement();
		
		reglement.setVersion(Reglement.CURRENT_VERSION);
		reglement.setName("C"+(new Date().getTime())); //$NON-NLS-1$
		reglement.setNbVoleeParSerie(10);

		return reglement;
	}
}
