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
import java.util.ArrayList;

/**
 * Fabrique d'archer en se basant sur les données en base
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class ConcurrentFactory {

	/**
	 * Construit un archer à partir d'un jeux de resultat sgbd et un reglement donnée
	 * 
	 * @param rs - le jeux de resultat sgbd
	 * @param reglement - le reglement appliqué
	 * @return l'archer construit
	 */
	public static Concurrent getConcurrent(ResultSet rs, Reglement reglement) {
		Concurrent concurrent = new Concurrent();

		try {
			concurrent.setNumLicenceArcher(rs.getString("NUMLICENCEARCHER"));
			concurrent.setNomArcher(rs.getString("NOMARCHER"));
			concurrent.setPrenomArcher(rs.getString("PRENOMARCHER"));
			concurrent.setCertificat(rs.getBoolean("CERTIFMEDICAL"));
			concurrent.setClub(EntiteFactory.getEntite(rs.getString("AGREMENTENTITE")));

			if(reglement != null) {
				CriteriaSet differentiationCriteria = null;
				String sql = "select * from distinguer where NUMLICENCEARCHER=? and NUMREGLEMENT=?";
				
				PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
				
				pstmt.setString(1, concurrent.getNumLicenceArcher());
				pstmt.setInt(2, reglement.hashCode());
				
				ResultSet rsCriteriaSet = pstmt.executeQuery();

				if(rsCriteriaSet.first()) {
					differentiationCriteria = CriteriaSetFactory
							.getCriteriaSet(rsCriteriaSet.getInt("NUMCRITERIASET"), reglement, reglement.hashCode());
				} else {
					differentiationCriteria = new CriteriaSet();
					for(Criterion key : reglement.getListCriteria()) {
						if(!key.getCodeffta().equals("")) {
							ArrayList<CriterionElement> arrayList = key.getCriterionElements();
							int valindex = rs.getInt(key.getCodeffta() + "FFTA");
							if(valindex >= arrayList.size())
								valindex = arrayList.size() - 1;
							if(valindex < 0)
								valindex = 0;
							if(key.getCriterionElements().get(valindex).isActive())
								differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
							else
								return null;
						}
					}
				}

				concurrent.setCriteriaSet(differentiationCriteria);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return concurrent;
	}
	
	/**
	 * Construit un nouveau concurrent à parametrer en se basant sur le reglement donnée
	 * 
	 * @param reglement
	 * @return L'objet concurrent produit à partir du reglement
	 */
	public static Concurrent getConcurrent(Reglement reglement) {
		Concurrent concurrent = new Concurrent();
		
		CriteriaSet differentiationCriteria = new CriteriaSet();
		for(Criterion key : reglement.getListCriteria()) {
			differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(0));
		}
		concurrent.setCriteriaSet(differentiationCriteria);
		
		return concurrent;
	}
}
