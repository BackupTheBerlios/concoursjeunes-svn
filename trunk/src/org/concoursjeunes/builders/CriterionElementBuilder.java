/*
 * Créé le 22 mai 07 à 15:14:26 pour ConcoursJeunes
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

import java.util.Map;

import org.ajdeveloppement.commons.persistence.LoadHelper;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.sql.SqlLoadHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class CriterionElementBuilder {
	
	private static LoadHelper<CriterionElement,Map<String,Object>> loadHelper;
	static {
		try {
			loadHelper = new LoadHelper<CriterionElement,Map<String,Object>>(new SqlLoadHandler<CriterionElement>(ApplicationCore.dbConnection, CriterionElement.class));
		} catch(ObjectPersistenceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Construit l'élément de critère représenté par son code et appartenant au critére et réglement transmis en parametre 
	 * 
	 * @param codeElement le code de l'élément à charger
	 * @param criterion le critère parent de l'élément
	 * 
	 * @return l'élément de critère construit
	 */
	public static CriterionElement getCriterionElement(String codeElement, Criterion criterion) {

		CriterionElement criterionElement = new CriterionElement();
		criterionElement.setCriterion(criterion);
		criterionElement.setCode(codeElement);
		
		try {
			loadHelper.load(criterionElement);
		} catch (ObjectPersistenceException e) {
			e.printStackTrace();
		}
			
//			String sql = "select * from CRITEREELEMENT where CODECRITEREELEMENT=?" + //$NON-NLS-1$
//					" and CODECRITERE=? and NUMREGLEMENT=?"; //$NON-NLS-1$
//			
//			PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
//
//			pstmt.setString(1, codeElement);
//			pstmt.setString(2, criterion.getCode());
//			pstmt.setInt(3, criterion.getReglement().getNumReglement());
//	
//			ResultSet rs = pstmt.executeQuery();
//			
//			if(rs.first()) {
//				criterionElement.setLibelle(rs.getString("LIBELLECRITEREELEMENT")); //$NON-NLS-1$
//				criterionElement.setActive(rs.getBoolean("ACTIF")); //$NON-NLS-1$
//				criterionElement.setNumordre(rs.getInt("NUMORDRE")); //$NON-NLS-1$
//				
//				return criterionElement;
//			}

		return criterionElement;
	}
	
	/**
	 * Construit un element de critère avec l'ensemble des éléments fournit en paramètre.
	 * 
	 * @param codeElement le code de l'élément
	 * @param libelle le libellé de lélément
	 * @param active element actif ou non
	 * @param numordre numero d'ordre de l'element
	 * 
	 * @return l'element de critére fabriqué
	 */
	public static CriterionElement getCriterionElement(String codeElement, String libelle, boolean active, int numordre) {
		CriterionElement element = new CriterionElement(codeElement);
		element.setLibelle(libelle);
		element.setActive(active);
		element.setNumordre(numordre);
		
		return element;
	}
}
