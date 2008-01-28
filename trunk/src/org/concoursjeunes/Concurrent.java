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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Objet de Base de stockage des Information sur un concurrent:
 *  Nom Categorie Licence Cible Nombre de volée points
 *  
 * @author  Aurelien Jeoffray
 * @version  3.0
 */
public class Concurrent extends Archer implements Cloneable {
	/**
	 * Statut de l'archer: réservé
	 */
	public static final int RESERVEE    = 0;
	/**
	 * Statut de l'archer: Payée
	 */
	public static final int PAYEE       = 1;
	/**
	 * Statut de l'archer: non initialisé
	 */
	public static final int UNINIT      = 2;

	private CriteriaSet criteriaSet;
	
	private int depart                  = 0;
	private TargetPosition targetPosition = new TargetPosition();

	private List<Integer> points	= new ArrayList<Integer>();
	private int neuf                    = 0;
	private int dix                     = 0;
	private int manque                  = 0;

	private int inscription             = UNINIT;

	/**
	 * Constructeur vide obligatoire pour java beans
	 * 
	 */
	public Concurrent() { }

	/**
	 * Retourne les critères distinguant l'archer
	 * 
	 * @return criteriaSet le jeux de critères distinguant l'archer
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * Définit le jeux de critère d'istinguant l'archer
	 * 
	 * @param criteriaSet le jeux de critères de distinction
	 */
	public void setCriteriaSet(CriteriaSet criteriaSet) {
		this.criteriaSet = criteriaSet;
	}
	
	/**
	 * Affectation des scores pour le concurrent
	 * 
	 * @param points - la grille des scores du concurrent
	 */
	public void setScore(ArrayList<Integer> points) {
		this.points = points;
	}

	/**
	 * Donne la grille des scores du concurrent
	 * 
	 * @return la grille des scores
	 */
	public List<Integer> getScore() {
		return points;
	}

	/**
	 * Donne le total des points du concurrent
	 * pour classement
	 * 
	 * @return int - le total des points
	 */
	public int getTotalScore() {
		int total = 0;
		if(points != null) {
			for(int point : points) {
				total += point;
			}
		}
		return total;
	}
	/**
	 * Affecte le nombre de dix total du concurrent
	 * @param  dix
	 */
	public void setNeuf(int dix) {
		this.neuf = dix;
	}

	/**
	 * Donne le nombre de dix du concurrent
	 * @return  int
	 */
	public int getNeuf() {
		return this.neuf;
	}

	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * @param  dixPlus
	 */
	public void setDix(int dixPlus) {
		this.dix = dixPlus;
	}

	/**
	 * Donne le nombre de 10+ du concurrent
	 * @return  int
	 */
	public int getDix() {
		return this.dix;
	}

	/**
	 * Affecte le nombre de fleche manquee total du concurrent
	 * @param  manque
	 */
	public void setManque(int manque) {
		this.manque = manque;
	}

	/**
	 * Donne le nombre de fleche manquee du concurrent
	 * @return  int
	 */
	public int getManque() {
		return this.manque;
	}

	/**
	 * Retourne le numero de départ de l'archer
	 * @return  Returns the depart.
	 */
	public int getDepart() {
		return depart;
	}

	/**
	 * Définit le n° de départ de l'archer
	 * @param depart  The depart to set.
	 */
	public void setDepart(int depart) {
		this.depart = depart;
	}

	/**
	 * Donne le numero de cible du concurrent
	 * @return  int
	 */
	public int getCible() {
		return this.targetPosition.getTarget();
	}

	/**
	 * Affecte le numero de cible du concurrent
	 * @param  cible
	 */
	public void setCible(int cible) {
		this.targetPosition.setTarget(cible);
	}

	/**
	 * Donne la position sur cible du concurrent
	 * @return  int
	 */
	public int getPosition() {
		return this.targetPosition.getPosition();
	}

	/**
	 * Affecte la position sur cible du concurrent
	 * @param  position
	 */
	public void setPosition(int position) {
		this.targetPosition.setPosition(position);
	}

	/**
	 * Donne l'etat d'inscription du concurennt, réservé/payée
	 * @return  int
	 */
	public int getInscription() {
		return this.inscription;
	}

	/**
	 * Affecte l'etat d'inscription du concurennt, réservé/payée
	 * @param  inscription
	 */
	public void setInscription(int inscription) {
		this.inscription = inscription;
	}

	/**
	 * Libelle court du concurrent
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		if(this.targetPosition.getTarget() == 0)
			return "<html><font color=red>" + //$NON-NLS-1$
			getNomArcher() + " " + //$NON-NLS-1$
			getPrenomArcher() + " (" + //$NON-NLS-1$
			getClub() +
			")</font></html>"; //$NON-NLS-1$
		return targetPosition.toString() + ": " + //$NON-NLS-1$
		getNomArcher() + " " + //$NON-NLS-1$
		getPrenomArcher() + " (" + //$NON-NLS-1$
		getClub() + ")"; //$NON-NLS-1$
	}
	
	/**
	 * Test si l'archer possede dans la base des homonymes (même nom et prenom)
	 * 
	 * @return true su l'archer possede des homonyme, false sinon
	 */
	public boolean haveHomonyme() {
		Archer aComparant = new Archer();
		aComparant.setNomArcher(getNomArcher());
		aComparant.setPrenomArcher(getPrenomArcher());

		List<Concurrent> homonyme = getArchersInDatabase(aComparant, null, ""); //$NON-NLS-1$

		return (homonyme.size() > 1);
	}
	
	/**
	 * Sauvegarde le jeux de critère associé à l'archer
	 * 
	 * @param reglement le réglement pour lequel s'applique le jeux de critère
	 */
	public void saveCriteriaSet(Reglement reglement) {
		if(!getNumLicenceArcher().equals("")) { //$NON-NLS-1$
			criteriaSet.save();
			try {
				String sql = "select * from ARCHERS where NUMLICENCEARCHER=?"; //$NON-NLS-1$
				PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
				pstmt.setString(1, getNumLicenceArcher());
				
				ResultSet rs = pstmt.executeQuery();
				if(rs.first()) {
					pstmt.close();
					
					sql = "merge into distinguer (NUMLICENCEARCHER, NUMREGLEMENT, " + //$NON-NLS-1$
							"NUMCRITERIASET) KEY (NUMLICENCEARCHER, NUMREGLEMENT)" + //$NON-NLS-1$
							"VALUES (?, ?, ?)"; //$NON-NLS-1$
					//NUMREGLEMENT
					pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
					
					pstmt.setString(1, getNumLicenceArcher());
					pstmt.setInt(2, reglement.hashCode());
					pstmt.setInt(3, criteriaSet.hashCode());
	
					pstmt.executeUpdate();
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	public static ArrayList<Concurrent> getArchersInDatabase(Archer aGeneric, Reglement reglement, String orderfield, int nbmaxenreg) {
		ArrayList<Concurrent> concurrents = new ArrayList<Concurrent>();
		Statement stmt = null;
		try {
			stmt = ConcoursJeunes.dbConnection.createStatement();

			String sql = "select * from archers "; //$NON-NLS-1$

			if(aGeneric != null) {
				sql += "where "; //$NON-NLS-1$
				ArrayList<String> filters = new ArrayList<String>();

				if(aGeneric.getNumLicenceArcher().length() > 0) {
					filters.add("NUMLICENCEARCHER like '" + aGeneric.getNumLicenceArcher() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(aGeneric.getNomArcher().length() > 0) {
					filters.add("NOMARCHER like '" + aGeneric.getNomArcher().replaceAll("'", "''") + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
				if(aGeneric.getPrenomArcher().length() > 0) {
					filters.add("UPPER(PRENOMARCHER) like '" + aGeneric.getPrenomArcher().toUpperCase().replaceAll("'", "''") + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
				if(aGeneric.getClub().getAgrement().length() > 0) {
					filters.add("AGREMENTENTITE like '" + aGeneric.getClub().getAgrement() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}

				for(String filter : filters) {
					sql += " and " + filter; //$NON-NLS-1$
				}
			}
			sql = sql.replaceFirst(" and ", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if(orderfield.length() > 0)
				sql += " order by " + orderfield; //$NON-NLS-1$

			ResultSet rs = stmt.executeQuery(sql);

			int iEnreg = 0;
			while(rs.next() && (nbmaxenreg == -1 || iEnreg < nbmaxenreg)) {
				
				Concurrent concurrent = ConcurrentBuilder.getConcurrent(rs, reglement);
				if(concurrent != null) {
					concurrents.add(concurrent);
					iEnreg++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(Exception e) { }
		}
		return concurrents;
	}
	
	@Override
	public Concurrent clone() {
		try {
			return (Concurrent)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
}