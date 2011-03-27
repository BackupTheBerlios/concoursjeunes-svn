/*
 * Créé le 22 mai 07 à 15:43:45 pour ConcoursJeunes
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ajdeveloppement.commons.persistence.LoadHelper;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.sql.ResultSetLoadHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlLoadHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Reglement;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class DistancesEtBlasonBuilder {
	
	private static LoadHelper<DistancesEtBlason,Map<String,Object>> loadHelper;
	private static LoadHelper<DistancesEtBlason,ResultSet> resultSetLoadHelper;
	static {
		try {
			loadHelper = new LoadHelper<DistancesEtBlason,Map<String,Object>>(new SqlLoadHandler<DistancesEtBlason>(ApplicationCore.dbConnection, DistancesEtBlason.class));
			resultSetLoadHelper = new LoadHelper<DistancesEtBlason,ResultSet>(new ResultSetLoadHandler<DistancesEtBlason>(DistancesEtBlason.class));
		} catch(ObjectPersistenceException e) {
			e.printStackTrace();
		}
	}
	
	private static PreparedStatement pstmtDistances = null;
	
	/**
	 * Construit un objet DistancesEtBlason en se basan sur le numero de sa reference
	 * en base ainsi que le numero de réglement.<br>
	 * Associe à l'objet DistancesEtBlason l'objet Reglement lié
	 * 
	 * @param numdistancesblason Le numero de la ligne en base
	 * @param reglement Le réglement à lié
	 * 
	 * @return l'objet DistancesEtBlason généré
	 * @throws ObjectPersistenceException 
	 */
	public static DistancesEtBlason getDistancesEtBlason(int numdistancesblason, Reglement reglement) throws ObjectPersistenceException {
		return getDistancesEtBlason(numdistancesblason, reglement, null);
	}
	
	public static DistancesEtBlason getDistancesEtBlason(Reglement reglement, ResultSet rs) throws ObjectPersistenceException {
		return getDistancesEtBlason(-1, reglement, rs);
	}
	
	/**
	 * Construit un objet DistancesEtBlason en se basan sur le numero de sa reference
	 * en base ainsi que le numero de réglement.<br>
	 * Associe à l'objet DistancesEtBlason l'objet Reglement lié
	 * 
	 * @param numdistancesblason Le numero de la ligne en base
	 * @param reglement Le réglement à lié
	 * 
	 * @return l'objet DistancesEtBlason généré
	 * @throws ObjectPersistenceException 
	 */
	private static DistancesEtBlason getDistancesEtBlason(int numdistancesblason, Reglement reglement, ResultSet rs) throws ObjectPersistenceException {
		DistancesEtBlason distancesEtBlason = new DistancesEtBlason();
		distancesEtBlason.setReglement(reglement);
		
		try {
			if(rs == null)
				distancesEtBlason.setNumdistancesblason(numdistancesblason);
			else
				numdistancesblason = rs.getInt("DISTANCESBLASONS.NUMDISTANCESBLASONS"); //$NON-NLS-1$
			
			if(pstmtDistances == null) {
				String sql = "select DISTANCE from DISTANCES where " + //$NON-NLS-1$
					"NUMDISTANCESBLASONS=? and NUMREGLEMENT=? order by NUMDISTANCES"; //$NON-NLS-1$
				pstmtDistances = ApplicationCore.dbConnection.prepareStatement(sql);
			}
			
			pstmtDistances.setInt(1, numdistancesblason);
			pstmtDistances.setInt(2, reglement.getNumReglement());
			
			ResultSet rs2 = pstmtDistances.executeQuery();
			try {
				List<Integer> distances = new ArrayList<Integer>();
				while(rs2.next()) {
					distances.add(rs2.getInt("DISTANCE")); //$NON-NLS-1$
				}
				int[] iDist = new int[distances.size()];
				for(int i = 0; i < iDist.length; i++)
					iDist[i] = distances.get(i);
				distancesEtBlason.setDistance(iDist);
			} finally {
				rs2.close();
			}
			
			Map<Class<?>, Map<String,Object>> foreignKeys = null;
			
			if(rs == null) {
				foreignKeys = loadHelper.load(distancesEtBlason);
			} else {
				foreignKeys = resultSetLoadHelper.load(distancesEtBlason, rs);
			}
			
			if(foreignKeys != null) {
				distancesEtBlason.setCriteriaSet(CriteriaSetBuilder.getCriteriaSet((Integer)foreignKeys.get(DistancesEtBlason.class).get("NUMCRITERIASET"), reglement)); //$NON-NLS-1$
				distancesEtBlason.setTargetFace(BlasonBuilder.getBlason((Integer)foreignKeys.get(DistancesEtBlason.class).get("NUMBLASON"))); //$NON-NLS-1$
			}
		} catch (SQLException e) {
			throw new ObjectPersistenceException(e);
		}
		
		return distancesEtBlason;
	}
}
