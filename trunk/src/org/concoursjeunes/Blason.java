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

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Blason {
	
	public static Blason NULL = new Blason("80cm",1,1,80); //$NON-NLS-1$
	
	private String name = ""; //$NON-NLS-1$
	private double horizontalRatio = 1;
	private double verticalRatio = 1;
	private int nbArcher = 4;
	private int numordre = 0;
	
	private int numblason = 0;
	
	public Blason() {
		
	}

	/**
	 * @param name
	 */
	public Blason(String name) {
		this.name = name;
	}

	/**
	 * @param name
	 * @param horizontalRatio
	 * @param verticalRation
	 */
	public Blason(String name, double horizontalRatio, double verticalRation) {
		this.name = name;
		this.horizontalRatio = horizontalRatio;
		this.verticalRatio = verticalRation;
	}
	
	/**
	 * @param name
	 * @param horizontalRatio
	 * @param verticalRatio
	 * @param numordre
	 */
	public Blason(String name, double horizontalRatio, double verticalRatio,
			int numordre) {
		this.name = name;
		this.horizontalRatio = horizontalRatio;
		this.verticalRatio = verticalRatio;
		this.numordre = numordre;
	}

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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name name à définir
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return horizontalRatio
	 */
	public double getHorizontalRatio() {
		return horizontalRatio;
	}

	/**
	 * @param horizontalRatio horizontalRatio à définir
	 */
	public void setHorizontalRatio(double horizontalRatio) {
		this.horizontalRatio = horizontalRatio;
	}

	/**
	 * @return verticalRation
	 */
	public double getVerticalRatio() {
		return verticalRatio;
	}

	/**
	 * @param verticalRation verticalRation à définir
	 */
	public void setVerticalRatio(double verticalRatio) {
		this.verticalRatio = verticalRatio;
	}
	
	/**
	 * @return nbArcher
	 */
	public int getNbArcher() {
		return nbArcher;
	}

	/**
	 * @param nbArcher nbArcher à définir
	 */
	public void setNbArcher(int nbArcher) {
		this.nbArcher = nbArcher;
	}

	/**
	 * @return numordre
	 */
	public int getNumordre() {
		return numordre;
	}

	/**
	 * @param numordre numordre à définir
	 */
	public void setNumordre(int numordre) {
		this.numordre = numordre;
	}

	/**
	 * @return numblason
	 */
	public int getNumblason() {
		return numblason;
	}

	/**
	 * @param numblason numblason à définir
	 */
	public void setNumblason(int numblason) {
		this.numblason = numblason;
	}

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
