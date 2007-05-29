/*
 * Créé le 02/03/2007 à 17:36 pour ConcoursJeunes
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

import java.io.File;
import java.io.FilenameFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import ajinteractive.standard.utilities.persistance.Entity;
import ajinteractive.standard.utilities.persistance.Field;
import ajinteractive.standard.utilities.persistance.Id;
import ajinteractive.standard.utilities.persistance.ManyToOne;
import ajinteractive.standard.utilities.sql.SqlParser;

/**
 * <p>
 * Représentation d'un réglement de concours. Un réglement fixe les régles arbitral
 * appliqué à un concours. Un seur réglement peut être appliqué sur un concours, et
 * à plus forte raison à tous les archers du concours.
 * </p>
 * <p>
 * On retrouve dans un réglement les éléments essentiel afin de compter les points
 * ainsi que l'ensemble des critères de classement et de placement qui doivent être
 * appliqué sur un concours.
 * </p>
 * <p>
 * Un réglement peut être qualifié d'"officiel" ou non. Si il est qualifié d'officiel,
 * celui ci ne devrait pas être altéré par les vue/controlleur. La methode
 * <i>isOfficialReglement()</i> est utilisé pour déterminé si le réglement doit être
 * considéré ou non comme officiel. Cette qualification doit permettre d'effectuer des
 * classement inter-club, inter-concours avec l'assurance que les cirtères d'évaluation
 * sont en tout point identique.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@Entity("REGLEMENT")
public class Reglement {

	@Field("NOMREGLEMENT") private String name				= "default";
	
	@Field("NBSERIE") private int nbSerie             = 2;
	@Field("NBVOLEEPARSERIE") private int nbVoleeParSerie     = 6;
	@Field("NBFLECHEPARVOLEE") private int nbFlecheParVolee    = 3;
	@Field("NBMEMBRESEQUIPE") private int nbMembresEquipe     = 4;
	@Field("NBMEMBRESRETENU") private int nbMembresRetenu     = 3;

	@ManyToOne("CRITERE") private ArrayList<Criterion> listCriteria = new ArrayList<Criterion>();
	@ManyToOne("DISTANCESBLASONS") private ArrayList<DistancesEtBlason> listDistancesEtBlason = new ArrayList<DistancesEtBlason>();
	
	@Field("ISOFFICIAL") private boolean officialReglement = false;
	
	/**
	 * Constructeur java-beans. Initialise un réglement par défaut
	 *
	 */
	public Reglement() { }
	
	/**
	 * Initialise un réglement par défaut en le nommant
	 * @param name
	 */
	public Reglement(String name) {
		this.name = name;
	}
	
	/**
	 * Retourne le nom du réglement
	 * 
	 * @return name le nom du réglement
	 */
	public String getName() {
		return name;
	}

	/**
	 * Donne ou change le nom du réglement
	 * 
	 * @param name le nom à donner au réglement
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * Retourne la liste des critères de distinction des archers pouvant être
	 * utilisé sur les concours exploitant ce réglement.
	 * </p>
	 * <p>
	 * Les critères retournés peuvent être soit determinant pour le classement,
	 * le placement, les deux ou simplement informatif.
	 * </p>
	 * 
	 * @return la liste des critéres de distinction utilisé pour
	 * le réglement
	 */
	public ArrayList<Criterion> getListCriteria() {
		return listCriteria;
	}
	
	
	
	/**
	 * Définit la liste des critères de distinction du réglement.
	 * 
	 * <i>Methode essentielement utile à la déserialisation. Ne devrait pas
	 * être utilisé directement.</i>
	 * 
	 * @param listCriteria the listCriteria to set
	 */
	public void setListCriteria(ArrayList<Criterion> listCriteria) {
		this.listCriteria = listCriteria;
	}

	/**
	 * @return listDistancesEtBlason
	 */
	public ArrayList<DistancesEtBlason> getListDistancesEtBlason() {
		return listDistancesEtBlason;
	}

	/**
	 * @param listDistancesEtBlason listDistancesEtBlason à définir
	 */
	public void setListDistancesEtBlason(
			ArrayList<DistancesEtBlason> listDistancesEtBlason) {
		this.listDistancesEtBlason = listDistancesEtBlason;
	}
	
	public DistancesEtBlason getDistancesEtBlasonFor(CriteriaSet criteriaSet) {
		for(DistancesEtBlason db : listDistancesEtBlason) {
			if(db.getCriteriaSet().equals(criteriaSet)) {
				return db;
			}
		}
		return null;
	}
	
	public void addDistancesEtBlason(DistancesEtBlason distancesEtBlason) {
		listDistancesEtBlason.add(distancesEtBlason);
	}

	/**
	 * Renvoi la politique de placement.
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
	 * Permet d'identifié le réglement comme officiel ou non.<br>
	 * Un réglement officiel ne devrait pas être altéré au cours de sa vie.
	 * 
	 * @return true si le réglement est qualifié d'officiel, false dans le
	 * cas contraire.
	 */
	public boolean isOfficialReglement() {
		return officialReglement;
	}

	/**
	 * <p>
	 * Définit si le réglement est ou non officiel
	 * </p>
	 * <p>
	 * <i>Methode essentielement utile à la déserialisation et aux outils de débugage.
	 * Ne devrait pas être utilisé directement.</i>
	 * </p>
	 * 
	 * @param officialReglement true pour un réglement officiel, false sinon
	 */
	public void setOfficialReglement(boolean officialReglement) {
		this.officialReglement = officialReglement;
	}

	/**
	 * Détermine si un tableau de score donnée est ou non valide su le réglement
	 * 
	 * @param scores le tableau de score à validé
	 * @return true si le score est valide, false dans le cas contraire
	 */
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
	
	/**
	 * Rend l'objet persistant. Sauvegarde l'ensemble des données de l'objet
	 * dans la base de donnée de ConcoursJeunes.
	 *
	 */
	public void save() {
		try {
			String sql = "merge into REGLEMENT (NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE," +
					"NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			//Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
			pstmt.setInt(1, hashCode());
			pstmt.setString(2, name);
			pstmt.setInt(3, nbSerie);
			pstmt.setInt(4, nbVoleeParSerie);
			pstmt.setInt(5, nbFlecheParVolee);
			pstmt.setInt(6, nbMembresEquipe);
			pstmt.setInt(7, nbMembresRetenu);
			pstmt.setBoolean(8, officialReglement);

			//sauvegarde les tableaux de crières et correspondance
			saveCriteria();
			saveDistancesAndBlasons();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void saveCriteria() {
		for(Criterion criterion : listCriteria) {
			criterion.save();
		}
	}
	
	private void saveDistancesAndBlasons() {
		for(DistancesEtBlason distancesEtBlason : listDistancesEtBlason) {
			distancesEtBlason.save();
		}
	}

	/**
	 * Supprime la persistance du réglement. Cette persistance ne peut être
	 * supprimé qu'à la condition que le réglement ne soit pas officiel 
	 * 
	 * @return true si suppression effective, false sinon.
	 */
	public boolean delete() {
		boolean success = false;

		if(!officialReglement) {
			try {
				Statement stmt = ConcoursJeunes.dbConnection.createStatement();
				
				stmt.executeUpdate("delete from REGLEMENT where NUMREGLEMENT=" + hashCode());
				
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	/**
	 * <p>
	 * Retourne la liste des réglement disponible en base de donnée.<br>
	 * intégre au passage les réglement disponible sous forme de script sql
	 * dans le répertoire config/reglements n'ayant pas encore intégré.
	 * </p>
	 * <p>
	 * Des mises à jour du programme peuvent apporter de nouveau réglement
	 * sous forme de script sql
	 * </p>
	 * 
	 * @return la liste des réglement disponible
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	@Id("NUMREGLEMENT")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + name.hashCode();
		
		return result;
	}
}
