/*
 * Créer le 15 déc. 07 à 17:58:51 pour ConcoursJeunes
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.concoursjeunes.builders.BlasonBuilder;

/**
 * Bean representant un blason de tir et ses caracteristiques
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 *
 */
public class Blason {
	
	/**
	 * Représente le blason par défaut
	 */
	public static Blason NULL = new Blason();
	
	private String name = ""; //$NON-NLS-1$
	private double horizontalRatio = 1;
	private double verticalRatio = 1;
	private int nbArcher = 4;
	private int numordre = 0;
	private ConcurrentMap<Integer, Ancrage> ancrages = new ConcurrentHashMap<Integer, Ancrage>();
	
	private int numblason = 0;
	
	public Blason() {
	}

	/**
	 * Construit un nouveau blason portant le nom donnée en parametre
	 * 
	 * @param name le nom du blason à construire
	 */
	public Blason(String name) {
		this.name = name;
	}

	/**
	 * <p>Construit un nouveau blason ayant le nom donnée en parametre ainsi
	 * que les caractéristiques de ratio de cible horizontal et vertical.</p>
	 * <p>le ratio correspond à une valeur entre 0 et 1 correspondant à la
	 * fraction de cible occupé par le blason dans les dimensions horizontal
	 * et vertical</p>
	 * 
	 * 
	 * @param name le nom du blason
	 * @param horizontalRatio le ratio horizontal exprimé en fraction de cible (valeur entre 0 et 1)
	 * @param verticalRatio le ratio vertical exprimé en fraction de cible (valeur entre 0 et 1)
	 */
	public Blason(String name, double horizontalRatio, double verticalRatio) {
		this.name = name;
		this.horizontalRatio = horizontalRatio;
		this.verticalRatio = verticalRatio;
	}
	
	/**
	 * <p>Construit un nouveau blason ayant le nom donnée en parametre ainsi
	 * que les caractéristiques de ratio de cible horizontal et vertical ainsi
	 * que son numero d'ordre.</p>
	 * <p>le ratio correspond à une valeur entre 0 et 1 correspondant à la
	 * fraction de cible occupé par le blason dans les dimensions horizontal
	 * et vertical</p>
	 * <p>le numero d'ordre permet de trié les blasons du plus gros (en taille) au plus petit
	 * afin de les ordonner correctement sur le pas de tir</p>
	 * 
	 * @param name le nom du blason
	 * @param horizontalRatio le ratio horizontal exprimé en fraction de cible (valeur entre 0 et 1)
	 * @param verticalRatio le ratio vertical exprimé en fraction de cible (valeur entre 0 et 1)
	 * @param numordre le numero d'ordre de disposition sur le pas de tir
	 * @param nbArcher le nombre d'archer pouvant tirer sur le blason
	 */
	public Blason(String name, double horizontalRatio, double verticalRatio,
			int numordre, int nbArcher) {
		this.name = name;
		this.horizontalRatio = horizontalRatio;
		this.verticalRatio = verticalRatio;
		this.numordre = numordre;
		this.nbArcher = nbArcher;
	}

	/**
	 * Liste l'ensemble des blasons existant dans la base
	 * 
	 * @return la liste des blasons existant
	 * @throws SQLException
	 */
	public static List<Blason> listAvailableTargetFace() throws SQLException {
		ArrayList<Blason> blasons = new ArrayList<Blason>();
		
		String sql = "select * from BLASONS order by NUMORDRE desc"; //$NON-NLS-1$
		
		Statement stmt = ConcoursJeunes.dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			blasons.add(BlasonBuilder.getBlason(rs));
		}
		
		return blasons;
	}

	/**
	 * Retourne le nom du blason
	 * 
	 * @return name le nom du blason
	 */
	public String getName() {
		return name;
	}

	/**
	 * Définit le nom attribué au blason
	 * 
	 * @param name le nom du blason
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Le ratio horizontal définissant le blason.<br>
	 * Le ratio représente une fraction de la taille de la cible (valeur comprise
	 * entre 0 et 1)
	 * 
	 * @return horizontalRatio le ratio horizontal de la cible
	 */
	public double getHorizontalRatio() {
		return horizontalRatio;
	}

	/**
	 * Définit le ratio horizontal définissant le blason.<br>
	 * Le ratio représente une fraction de la taille de la cible (valeur comprise
	 * entre 0 et 1)
	 * 
	 * @param horizontalRatio le ratio vertical de la cible
	 */
	public void setHorizontalRatio(double horizontalRatio) {
		this.horizontalRatio = horizontalRatio;
	}

	/**
	 * Le ratio vertical définissant le blason.<br>
	 * Le ratio représente une fraction de la taille de la cible (valeur comprise
	 * entre 0 et 1)
	 * 
	 * @return verticalRatio le ratio vertical de la cible
	 */
	public double getVerticalRatio() {
		return verticalRatio;
	}

	/**
	 * Definit le ratio d'occupation vertical de la cible.
	 * Le ratio est une valeur entre 0 et 1 representant la fraction de cible
	 * occupé par le blason.<br>
	 * <br>
	 * Si la valeur definit est superieur à 1 alors celle ci est considere
	 * comme egal à 1. De même, une valeur negative est consideré comme egal à 0
	 * 
	 * @param verticalRatio le ratio d'occupation cible
	 */
	public void setVerticalRatio(double verticalRatio) {
		if(verticalRatio > 1.0) verticalRatio = 1.0;
		else if(verticalRatio < 0.0) verticalRatio = 0.0;
		this.verticalRatio = verticalRatio;
	}
	
	/**
	 * Le nombre d'archer que peut supporter le blason<br>
	 * <i>par exemple un blason de 40 ne peut supporter qu'un seul archer,
	 * alors qu'un blason de 80 supporte jusqu'à 4 archers</i>
	 * 
	 * @return nbArcher le nombre d'archer pouvant tiré sur le même blason
	 */
	public int getNbArcher() {
		return nbArcher;
	}

	/**
	 * Définit le nombre d'archer que peut supporter le blason<br>
	 * <i>par exemple un blason de 40 ne peut supporter qu'un seul archer,
	 * alors qu'un blason de 80 supporte jusqu'à 4 archers</i>
	 * 
	 * @param nbArcher le nombre d'archer pouvant tiré sur le même blason
	 */
	public void setNbArcher(int nbArcher) {
		this.nbArcher = nbArcher;
	}

	/**
	 * Le numero d'ordre de la cible. Le numero d'ordre permet de classer
	 * les blasons dans l'ordre.
	 * 
	 * @return numordre le numero d'ordre du blason
	 */
	public int getNumordre() {
		return numordre;
	}

	/**
	 * Définit le numero d'ordre de la cible. Le numero d'ordre permet de classer
	 * les blasons dans l'ordre.
	 * 
	 * @param numordre le numero d'ordre du blason
	 */
	public void setNumordre(int numordre) {
		this.numordre = numordre;
	}

	/**
	 * Le numero de blason tel que définit dans la base.<br>
	 * Ce numero n'est pas destiné à être utilisé directement,
	 * mais uniquement par afin de permettre la persistance de l'objet
	 * 
	 * @return numblason le numero de reference du blason dans la base ou 0 si non définit 
	 */
	public int getNumblason() {
		return numblason;
	}

	/**
	 * Définit le numero de blason tel que définit dans la base.<br>
	 * Ce numero n'est pas destiné à être utilisé directement,
	 * mais uniquement par afin de permettre la persistance de l'objet
	 * 
	 * @param numblason le numero de reference du blason dans la base ou 0 si non définit
	 */
	public void setNumblason(int numblason) {
		this.numblason = numblason;
	}

	/**
	 * Retourne la table des points d'ancrages possible du blason
	 * 
	 * @return la table des points d'ancrages possible du blason
	 */
	public synchronized ConcurrentMap<Integer, Ancrage> getAncrages() {
    	return ancrages;
    }
	

	/**
	 * Définit la table des points d'ancarge du blason en fonction de la position
	 * 
	 * @param ancrages la table des points d'ancrages possible du blason
	 */
	public void setAncrages(ConcurrentMap<Integer, Ancrage> ancrages) {
    	this.ancrages = ancrages;
    }
	

	/**
	 * Sauvegarde l'objet dans la base en créant une nouvelle ligne si le numero de blason est à 0
	 * ou en mettant à jour la ligne existante dans la base et identifié par le numero de blason
	 * 
	 * @throws SQLException
	 */
	public void save() throws SQLException {
		String sql;
		if(numblason > 0)
			sql = "update BLASONS set NOMBLASON=?, HORIZONTAL_RATIO=?, VERTICAL_RATIO=? where NUMBLASON=" + numblason; //$NON-NLS-1$
		else
			sql = "insert into BLASONS (NOMBLASON, HORIZONTAL_RATIO, VERTICAL_RATIO) values (?, ?, ?)"; //$NON-NLS-1$
		
		PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
		
		pstmt.setString(1, name);
		pstmt.setDouble(2, horizontalRatio);
		pstmt.setDouble(3, verticalRatio);
		
		pstmt.executeUpdate();
		
		if(numblason == 0) {
			ResultSet clefs = pstmt.getGeneratedKeys();
			if (clefs.first()) {
				numblason = (Integer) clefs.getObject(1);
			}
		}
	}
	
	/**
	 * Supprime la persistance de l'objet
	 * 
	 * @throws SQLException
	 */
	public void delete() throws SQLException {
		String sql = "delete from BLASONS where numblason=" + numblason; //$NON-NLS-1$
		
		Statement stmt = ConcoursJeunes.dbConnection.createStatement();
		stmt.executeUpdate(sql);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Blason other = (Blason) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
