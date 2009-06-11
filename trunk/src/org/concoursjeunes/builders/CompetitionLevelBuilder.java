/*
 * Créer le 3 mai 2009 à 17:10:49 pour ConcoursJeunes
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.CompetitionLevel;
import org.concoursjeunes.Federation;

/**
 * Construit un objet CompetitionLevel à partir de la base de données
 * 
 * @author Aurélien JEOFFRAY
 */
public class CompetitionLevelBuilder {
	
	/**
	 * <p>Construit un objet CompetitionLevel pour le niveaux fédéral définit à partir de la base 
	 * de données.</p>
	 * <p>Si le niveau n'est pas trouvé pour la langue en parametre, mais disponible pour le français,
	 * alors c'est le libellé français qui est utilisé.</p>
	 * <p>Si le niveau n'est pas trouvé, le génére en mettant le code comme libellé.</p>
	 * 
	 * @param codeNiveau le code du niveau à retourner
	 * @param numFederation la fédération associé au niveau
	 * @param lang la langue du libellé.
	 * @return le niveau de compétition généré.
	 * @throws SQLException
	 */
	@SuppressWarnings("nls")
	public static CompetitionLevel getCompetitionLevel(int numLevel, Federation federation, String lang) throws SQLException {
		String sql = "select * from NIVEAU_COMPETITION where CODENIVEAU=? and NUMFEDERATION=? and LANG=?";
		
		CompetitionLevel competitionLevel = null;
		
		PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
		try {
			pstmt.setInt(1, numLevel);
			pstmt.setInt(2, federation.getNumFederation());
			pstmt.setString(3, lang);
			
			ResultSet rs = pstmt.executeQuery();
			try {
				boolean enregexists = rs.next(); 
				if(!enregexists) {
					rs.close();
					
					pstmt.setString(3, "fr");
					
					rs = pstmt.executeQuery();
					enregexists = rs.next();
				}
				
				if(enregexists) {
					competitionLevel = new CompetitionLevel();
					competitionLevel.setFederation(federation);
					competitionLevel.setNumLevel(rs.getInt("CODENIVEAU"));
					competitionLevel.setLang(lang);
					competitionLevel.setLibelle(rs.getString("LIBELLE"));
					competitionLevel.setDefaut(rs.getBoolean("DEFAUT"));
				}
			} finally {
				rs.close();
			}
		} finally {
			pstmt.close();
		}
		
		if(competitionLevel == null) {
			competitionLevel = new CompetitionLevel();
			competitionLevel.setFederation(federation);
			competitionLevel.setNumLevel(numLevel);
			competitionLevel.setLang(lang);
			competitionLevel.setLibelle("Niveau " + numLevel);
		}

		return competitionLevel;
	}
}
