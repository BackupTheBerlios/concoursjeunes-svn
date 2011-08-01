/*
 * Créé le 6 août 2008 à 19:01:19 pour ConcoursJeunes
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Federation;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.builders.ReglementBuilder;

/**
 * Permet la gestion listage, sélection, ajout et suppression des
 * règlements en fonction de leurs catégories et de leurs fédération
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class ReglementManager {
	
	private static PreparedStatement pstmAllReglementOrdered = null;
	
	private List<Reglement> availableReglements = new ArrayList<Reglement>();
	private List<Federation> federation = new ArrayList<Federation>();
	private List<Integer> categorie = new ArrayList<Integer>();
	
	private static ReglementManager instance = new ReglementManager();
	
	/**
	 * Construit une nouvelle instance du gestionnaire de règlement, listant
	 * tout les règlements présent en base.
	 */
	private ReglementManager() {
		listReglement();
	}
	
	public static ReglementManager getInstance() {
		return instance;
	}
	
	/**
	 * Liste tout les règlements présent en base
	 */
	private void listReglement() {
		availableReglements.clear();
		federation.clear();
		categorie.clear();
		
		try {
			if(pstmAllReglementOrdered == null)
				pstmAllReglementOrdered = ApplicationCore.dbConnection.prepareStatement(
						"select * from REGLEMENT where NOMREGLEMENT <> 'default' order by LIBELLE"); //$NON-NLS-1$

			ResultSet rs = pstmAllReglementOrdered.executeQuery();
			try {
				while (rs.next()) {
					Reglement reglement = ReglementBuilder.getReglement(rs);
					
					if(!federation.contains(reglement.getFederation()))
						federation.add(reglement.getFederation());
					if(!categorie.contains(reglement.getCategory()))
						categorie.add(reglement.getCategory());
					availableReglements.add(reglement); 
				}
			} finally {
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ObjectPersistenceException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Ajoute le règlement fournit en paramètre à la base et au gestionnaire
	 * 
	 * @param reglement le règlement à ajouter
	 * @throws SqlPersistanceException
	 */
	public void addReglement(Reglement reglement) throws ObjectPersistenceException {
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
	 * Supprime un règlement de la base et du gestionnaire
	 * 
	 * @param reglement le règlement à supprimer
	 */
	public void removeReglement(Reglement reglement) throws ObjectPersistenceException {
		reglement.delete();
	
		availableReglements.remove(reglement);
		if(getReglementsForCategory(reglement.getCategory()).size() == 0)
			categorie.remove(new Integer(reglement.getCategory()));
		if(getReglementsForFederation(reglement.getFederation()).size() == 0)
			removeFederation(reglement.getFederation());
		reglement = null;
	}
	
	/**
	 * Met à jour un règlement en base à partir de sa référence objet.
	 * Si le règlement n'existe pas en base, se contente de le créer
	 * 
	 * @param reglement
	 * @throws SqlPersistanceException
	 */
	public void updateReglement(Reglement reglement) throws ObjectPersistenceException {
		availableReglements.remove(reglement);
		/*
		if(getReglementsForCategory(reglement.getCategory()).size() == 0)
			categorie.remove(new Integer(reglement.getCategory()));
		if(getReglementsForFederation(reglement.getFederation()).size() == 0)
			removeFederation(reglement.getFederation());*/
		addReglement(reglement);
	}
	
	public void removeFederation(Federation f) throws ObjectPersistenceException {
		federation.remove(f);
		f.delete();
	}
	
	/**
	 * <p>
	 * Retourne la liste des règlement disponible en base de donnée.
	 * </p>
	 * <p>
	 * Des mises à jour du programme peuvent apporter de nouveau règlement sous
	 * forme de script sql
	 * </p>
	 * 
	 * @return la liste des règlement disponible
	 */
	public List<Reglement> getAvailableReglements() {
		return availableReglements;
	}
	
	/**
	 * Retourne la liste des règlements pour la categorie nommé
	 * 
	 * @param category le nom de la catégorie des règlements à retourner
	 * @return la liste des règlements de la catégorie
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
	 * Retourne la liste des règlements pour une fédération donné
	 * 
	 * @param federation la fédération pour laquelle retourner les règlements
	 * @return la liste des règlements
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
	 * Retourne la liste des règlements pour une federation et une catégorie
	 * 
	 * @param federation la federation des règlements à retourner
	 * @param category le numéro de la catégorie des règlements à retourner
	 * @return la liste des règlements
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
	 * Retourne le règlement qualifié par son nom ou null si inexistant
	 * 
	 * @param name le nom du règlement à retourner
	 * @return le règlement correspondant
	 */
	public Reglement getReglementByName(String name) {
		for(Reglement reglement : availableReglements) {
			if(reglement.getName().equals(name))
				return reglement;
		}
		return null;
	}
	
	/**
	 * Retourne les fédération représenté par les règlements
	 * 
	 * @return la liste des fédération représenté
	 */
	public List<Federation> getFederations() {
		return federation;
	}
	
	/**
	 * Retourne la liste des catégories représenté par les règlements
	 * 
	 * @return la liste des catégories de règlement
	 */
	public List<Integer> getCategories() {
		return categorie;
	}
	
	/**
	 * Exporte un règlement sous la forme d'un fichier XML
	 * 
	 * @param reglement le règlement à exporter
	 * @param exportFile le fichier dans lequel exporter le règlement
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportReglement(Reglement reglement, File exportFile) throws FileNotFoundException, IOException {
		try {
			XMLSerializer.saveMarshallStructure(exportFile, reglement);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Importe un règlement à partir d'un fichier XML
	 * 
	 * @param importFile le fichier XML contenant le règlement
	 * @return l'objet règlement résultant
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public Reglement importReglement(File importFile) throws IOException, ObjectPersistenceException {
		Reglement reglement = null;
		try {
			reglement = XMLSerializer.loadMarshallStructure(importFile, Reglement.class, false);
			updateReglement(reglement);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return reglement;
	}
}
