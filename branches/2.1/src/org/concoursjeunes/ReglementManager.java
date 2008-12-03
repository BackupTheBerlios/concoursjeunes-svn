/*
 * Créer le 6 août 2008 à 19:01:19 pour ConcoursJeunes
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.concoursjeunes.builders.ReglementBuilder;

import ajinteractive.standard.common.AJToolKit;

/**
 * Permet la gestion listage, séléction, ajout et suppression des
 * réglements en fonction de leurs catégories et de leurs fédération
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class ReglementManager {
	
	private List<Reglement> availableReglements = new ArrayList<Reglement>();
	private List<Federation> federation = new ArrayList<Federation>();
	private List<Integer> categorie = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	public ReglementManager() {
		listReglement();
	}
	
	private void listReglement() {
		availableReglements.clear();
		federation.clear();
		categorie.clear();
		
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("select NUMREGLEMENT from REGLEMENT where NUMREGLEMENT <> 0 order by NOMREGLEMENT"); //$NON-NLS-1$

			while (rs.next()) {
				Reglement reglement = ReglementBuilder.getReglement(rs.getInt("NUMREGLEMENT")); //$NON-NLS-1$
				
				if(!federation.contains(reglement.getFederation()))
					federation.add(reglement.getFederation());
				if(!categorie.contains(reglement.getCategory()))
					categorie.add(reglement.getCategory());
				availableReglements.add(reglement); 
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ajoute le réglement fournit en parametre à la base et au gestionnaire
	 * 
	 * @param reglement le réglement à ajouter
	 * @throws SQLException
	 */
	public void addReglement(Reglement reglement) throws SQLException {
		if(reglement == null)
			return;
		
		reglement.save();
		
		availableReglements.add(reglement);
		if(!federation.contains(reglement.getFederation()))
			federation.add(reglement.getFederation());
		if(!categorie.contains(reglement.getCategory()))
			categorie.add(reglement.getCategory());
	}
	
	/**
	 * Supprime un réglement de la base et du gestionnaire
	 * 
	 * @param reglement le réglement à supprimer
	 */
	public void removeReglement(Reglement reglement) {
		if(reglement.delete()) {
			availableReglements.remove(reglement);
			if(getReglementsForCategory(reglement.getCategory()).size() == 0)
				categorie.remove(new Integer(reglement.getCategory()));
			if(getReglementsForFederation(reglement.getFederation()).size() == 0)
				federation.remove(reglement.getFederation());
			reglement = null;
		}
		
	}
	
	/**
	 * <p>
	 * Retourne la liste des réglement disponible en base de donnée.
	 * </p>
	 * <p>
	 * Des mises à jour du programme peuvent apporter de nouveau réglement sous
	 * forme de script sql
	 * </p>
	 * 
	 * @return la liste des réglement disponible
	 */
	public List<Reglement> getAvailableReglements() {
		return availableReglements;
	}
	
	/**
	 * Retourne la liste des réglements pour la categorie nommé
	 * 
	 * @param category le nom de la catégorie des réglements à retourner
	 * @return la liste des réglements de la catégorie
	 */
	public List<Reglement> getReglementsForCategory(int category) {
		List<Reglement> reglements = new ArrayList<Reglement>();
		for(Reglement reglement : availableReglements) {
			if(reglement.getCategory() == category)
				reglements.add(reglement);
		}
		
		return reglements;
	}
	
	/**
	 * Retourne la liste des réglements pour une fédération donné
	 * 
	 * @param federation la fédération pour laquelle retourner les réglements
	 * @return la liste des réglements
	 */
	public List<Reglement> getReglementsForFederation(Federation federation) {
		List<Reglement> reglements = new ArrayList<Reglement>();
		for(Reglement reglement : availableReglements) {
			if(reglement.getFederation().equals(federation))
				reglements.add(reglement);
		}
		
		return reglements;
	}
	
	/**
	 * Retourne la liste des reglements pour une federation et une categorie
	 * 
	 * @param federation la federation des réglements à retourner
	 * @param category le numero de la categorie des réglements à retourner
	 * @return la liste des réglements
	 */
	public List<Reglement> getReglementsForFederationAndCategory(Federation federation, int category) {
		List<Reglement> reglements = new ArrayList<Reglement>();
		for(Reglement reglement : availableReglements) {
			if(reglement.getFederation().equals(federation) && reglement.getCategory() == category)
				reglements.add(reglement);
		}
		
		return reglements;
	}
	
	/**
	 * Retourne le reglement qualifié par son nom ou null si inexistant
	 * 
	 * @param name le nom du reglement à retourner
	 * @return le reglement correspondant
	 */
	public Reglement getReglementByName(String name) {
		for(Reglement reglement : availableReglements) {
			if(reglement.getName().equals(name))
				return reglement;
		}
		return null;
	}
	
	/**
	 * Retourne les fédération représenté par les réglements
	 * 
	 * @return la liste des fédération représenté
	 */
	public List<Federation> getFederations() {
		return federation;
	}
	
	/**
	 * Liste l'ensemble des fédérations disponible indépendament de l'éxistance
	 * de réglement associé
	 * 
	 * @return la liste des fédérations disponible
	 */
	public List<Federation> getAvailableFederations() {
		List<Federation> federations = new ArrayList<Federation>();
		String sql = "select * from FEDERATION order by SIGLEFEDERATION"; //$NON-NLS-1$
		
		try {
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				federations.add(
						new Federation(
								rs.getString("NOMFEDERATION"), //$NON-NLS-1$
								rs.getInt("NUMFEDERATION"), //$NON-NLS-1$
								rs.getString("SIGLEFEDERATION"))); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return federations;
	}
	
	/**
	 * Retourne la liste des catégories représenté par les réglements
	 * 
	 * @return la liste des catégories de réglement
	 */
	public List<Integer> getCategories() {
		return categorie;
	}
	
	public void exportReglement(Reglement reglement, File exportFile) throws FileNotFoundException, IOException {
		AJToolKit.saveXMLStructure(exportFile, reglement, false);
	}
	
	public Reglement importReglement(File importFile) throws IOException, SQLException {
		Reglement reglement = (Reglement)AJToolKit.loadXMLStructure(importFile, false);
		if(availableReglements.contains(reglement))
			removeReglement(reglement);
		addReglement(reglement);
		
		return reglement;
	}
}
