/*
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
import java.util.List;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.Reglement;

/**
 * Fabrique d'archer en se basant sur les données en base
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class ConcurrentBuilder {

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
			concurrent.setNumLicenceArcher(rs.getString("NUMLICENCEARCHER")); //$NON-NLS-1$
			concurrent.setNomArcher(rs.getString("NOMARCHER")); //$NON-NLS-1$
			concurrent.setPrenomArcher(rs.getString("PRENOMARCHER")); //$NON-NLS-1$
			concurrent.setCertificat(rs.getBoolean("CERTIFMEDICAL")); //$NON-NLS-1$
			concurrent.setClub(EntiteBuilder.getEntite(rs.getString("AGREMENTENTITE"))); //$NON-NLS-1$

			if(reglement != null) {
				CriteriaSet differentiationCriteria = null;
				String sql = "select * from distinguer where NUMLICENCEARCHER=? and NUMREGLEMENT=?"; //$NON-NLS-1$
				
				PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
				
				pstmt.setString(1, concurrent.getNumLicenceArcher());
				pstmt.setInt(2, reglement.getNumReglement());
				
				try {
					ResultSet rsCriteriaSet = pstmt.executeQuery();
	
					try {
						if(rsCriteriaSet.first()) {
							differentiationCriteria = CriteriaSetBuilder
									.getCriteriaSet(rsCriteriaSet.getInt("NUMCRITERIASET"), reglement); //$NON-NLS-1$
						} else {
							differentiationCriteria = new CriteriaSet();
							differentiationCriteria.setReglement(reglement);
							for(Criterion key : reglement.getListCriteria()) {
								boolean returnfirstval = true;
								if(!key.getChampsTableArchers().isEmpty()) {
									try {
										List<CriterionElement> arrayList = key.getCriterionElements();
										int valindex = rs.getInt(key.getChampsTableArchers()); 
										if(valindex >= arrayList.size())
											valindex = arrayList.size() - 1;
										if(valindex < 0)
											valindex = 0;
										
										if(key.getCriterionElements().get(valindex).isActive())
											differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
										else
											return null;
										
										returnfirstval = false;
									} catch (SQLException e) {
									}
								}
								
								if(returnfirstval) {
									int valindex = 0;
									while(valindex < key.getCriterionElements().size() 
											&& !key.getCriterionElements().get(valindex).isActive())
										valindex++;
									if(valindex < key.getCriterionElements().size())
										differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
									else
										return null;
								}
							}
						}
					} finally {
						rsCriteriaSet.close();
						rsCriteriaSet = null;
					}
				} finally {
					pstmt.close();
					pstmt = null;
				}
				
				//régle de surclassement de l'archer
				if(reglement.getSurclassement().containsKey(differentiationCriteria)) {
					CriteriaSet tmpCS = reglement.getSurclassement().get(differentiationCriteria);
					
					if(tmpCS == null) //si la categorie est invalide alors ne pas renvoyer l'archer
						return null;
					differentiationCriteria = tmpCS; //sinon retourner sa catégorie de surclassement
					concurrent.setSurclassement(true);
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
