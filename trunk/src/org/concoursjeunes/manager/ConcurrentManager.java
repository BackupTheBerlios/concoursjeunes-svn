/*
 * Créé le 18 mai 08 à 11:16:33 pour ConcoursJeunes
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
package org.concoursjeunes.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Archer;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.builders.ConcurrentBuilder;

/**
 * Gére le chargement en mémoire des Concurrents
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class ConcurrentManager {
	/**
	 * Retourne une liste d'archer en provenance de la base de données en fonction
	 * des critères de recherche fournit en parametres
	 * 
	 * @param aGeneric objet Archer generique servant de filtre de recherche (la recherche se
	 * fait sur les champs renseigné, le caractères genérique (%, ?) sont accepté
	 * 
	 * @param reglement le reglement à appliqué aux objets archers retourné
	 * 
	 * @param orderfield l'ordre de trie des objets retourné. Doivent être listé dans 
	 * l'ordre les champs de la base de données (table ARCHERS) servant au trie.
	 * 
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	public static List<Concurrent> getArchersInDatabase(Archer aGeneric, Reglement reglement, String orderfield) {
		return getArchersInDatabase(aGeneric, reglement, orderfield, -1);
	}
	
	/**
	 * Retourne une liste d'archer en provenance de la base de données en fonction
	 * des critères de recherche fournit en parametres
	 * 
	 * @param aGeneric objet Archer generique servant de filtre de recherche (la recherche se
	 * fait sur les champs renseigné, le caractères genérique (%, ?) sont accepté
	 * 
	 * @param reglement le reglement à appliqué aux objets archers retourné
	 * 
	 * @param orderfield l'ordre de trie des objets retourné. Doivent être listé dans 
	 * l'ordre les champs de la base de données (table ARCHERS) servant au trie.
	 * 
	 * @param concurrentManagerProgress permet de suvre la progression du remplissage
	 * 
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	public static List<Concurrent> getArchersInDatabase(Archer aGeneric, Reglement reglement, String orderfield, ConcurrentManagerProgress concurrentManagerProgress) {
		return getArchersInDatabase(aGeneric, reglement, orderfield, -1, concurrentManagerProgress);
	}
	
	/**
	 * Retourne une liste d'archer en provenance de la base de données en fonction
	 * des critères de recherche fournit en parametres
	 * 
	 * @param aGeneric objet Archer generique servant de filtre de recherche (la recherche se
	 * fait sur les champs renseigné, le caractères genérique (%, ?) sont accepté
	 * 
	 * @param reglement le reglement à appliqué aux objets archers retourné
	 * 
	 * @param orderfield l'ordre de trie des objets retourné. Doivent être listé dans 
	 * l'ordre les champs de la base de données (table ARCHERS) servant au trie.
	 * 
	 * @param nbmaxenreg définit le nombre maximum d'enregistrement qui doit être
	 * retourné par la methode
	 * 
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	public static List<Concurrent> getArchersInDatabase(Archer aGeneric, Reglement reglement, String orderfield, int nbmaxenreg) {
		return getArchersInDatabase(aGeneric, reglement, orderfield, nbmaxenreg, null);
	}
	
	/**
	 * Retourne une liste d'archer en provenance de la base de données en fonction
	 * des critères de recherche fournit en parametres
	 * 
	 * @param aGeneric objet Archer generique servant de filtre de recherche (la recherche se
	 * fait sur les champs renseigné, le caractères genérique (%, ?) sont accepté
	 * 
	 * @param reglement le reglement à appliqué aux objets archers retourné
	 * 
	 * @param orderfield l'ordre de trie des objets retourné. Doivent être listé dans 
	 * l'ordre les champs de la base de données (table ARCHERS) servant au trie.
	 * 
	 * @param nbmaxenreg définit le nombre maximum d'enregistrement qui doit être
	 * retourné par la methode
	 * 
	 * @param concurrentManagerProgress permet de suvre la progression du remplissage
	 * 
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	@SuppressWarnings("nls")
	public static List<Concurrent> getArchersInDatabase(
			Archer aGeneric, Reglement reglement, String orderfield, int nbmaxenreg, ConcurrentManagerProgress concurrentManagerProgress) {
		List<Concurrent> concurrents = new ArrayList<Concurrent>();
		Statement stmt = null;
		try {
			stmt = ApplicationCore.dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			String sql = "select %s from archers ";

			if(aGeneric != null) {
				sql += "where ";
				List<String> filters = new ArrayList<String>();

				if(!aGeneric.getNumLicenceArcher().isEmpty()) {
					filters.add("NUMLICENCEARCHER like '" + aGeneric.getNumLicenceArcher().replaceAll("'", "''").replaceAll("%", "%%") + "'");
				}
				if(!aGeneric.getNomArcher().isEmpty()) {
					filters.add("NOMARCHER like '" + aGeneric.getNomArcher().replaceAll("'", "''").replaceAll("%", "%%") + "'");
				}
				if(!aGeneric.getPrenomArcher().isEmpty()) {
					filters.add("UPPER(PRENOMARCHER) like '" + aGeneric.getPrenomArcher().toUpperCase().replaceAll("'", "''").replaceAll("%", "%%") + "'");
				}
				if(!aGeneric.getClub().getAgrement().isEmpty()) {
					filters.add("AGREMENTENTITE like '" + aGeneric.getClub().getAgrement().replaceAll("'", "''").replaceAll("%", "%%") + "'");
				}

				for(String filter : filters) {
					sql += " and " + filter;
				}
			}
			sql = sql.replaceFirst(" and ", "");

			ResultSet rs = stmt.executeQuery(String.format(sql, "count(*)"));
			try {
				rs.next();
				if(concurrentManagerProgress != null)
					concurrentManagerProgress.setConcurrentCount(rs.getInt(1));
			} finally {
				rs.close();
			}
			
			if(!orderfield.isEmpty())
				sql += " order by " + orderfield;
			
			if(nbmaxenreg > 0)
				sql += " limit " + nbmaxenreg;
			
			rs = stmt.executeQuery(String.format(sql, "*"));
			try {
				while(rs.next()) {
					
					Concurrent concurrent = ConcurrentBuilder.getConcurrent(rs, reglement);
					if(concurrent != null) {
						concurrents.add(concurrent);
						if(concurrentManagerProgress != null)
							concurrentManagerProgress.setCurrentConcurrent(concurrent);
					}
				}
			} finally {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(Exception e) { }
		}
		return concurrents;
	}
}
