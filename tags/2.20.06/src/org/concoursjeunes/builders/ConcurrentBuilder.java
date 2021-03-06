/*
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
							differentiationCriteria = new CriteriaSet(reglement);
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
