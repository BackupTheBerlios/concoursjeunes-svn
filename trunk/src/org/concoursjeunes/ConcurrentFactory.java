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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				CriteriaSet differentiationCriteria = new CriteriaSet();

				Statement stmt = ConcoursJeunes.dbConnection.createStatement();
				ResultSet rsCriteriaSet = stmt.executeQuery("select * from distinguer where " +
						"NUMLICENCEARCHER='" + concurrent.getNumLicenceArcher() + "' and " +
						"NUMREGLEMENT=" + reglement.getIdReglement());
				if(rsCriteriaSet.next()) {
					do {
						String codeCritere = rsCriteriaSet.getString("CODECRITERE");
						String codeElement = rsCriteriaSet.getString("CODECRITEREELEMENT");
						
						for(Criterion key : reglement.getListCriteria()) {
							if(key.getCode().equals(codeCritere)) {
								for(CriterionElement criterionElement : key.getCriterionElements()) {
									if(criterionElement.getCode().equals(codeElement)) {
										differentiationCriteria.getCriteria().put(key, criterionElement);
										break;
									}
								}
								break;
							}
						}
					} while(rsCriteriaSet.next());
				} else {
					for(Criterion key : reglement.getListCriteria()) {
						if(!key.getCodeffta().equals("")) {
							ArrayList<CriterionElement> arrayList = key.getCriterionElements();
							int valindex = rs.getInt(key.getCodeffta() + "FFTA");
							if(valindex >= arrayList.size())
								valindex = arrayList.size() - 1;
							if(valindex < 0)
								valindex = 0;
							differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
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
