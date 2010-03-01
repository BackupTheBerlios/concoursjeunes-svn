/*
 * Créé le 15 déc. 07 à 20:35:47 pour ConcoursJeunes
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

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.ajdeveloppement.commons.persistance.LoadHelper;
import org.ajdeveloppement.commons.persistance.ObjectPersistanceException;
import org.ajdeveloppement.commons.persistance.sql.SqlLoadHandler;
import org.concoursjeunes.Ancrage;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Blason;

/**
 * Construit un objet blason à partir des données en base
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 *
 */
public class BlasonBuilder {
	
	private static LoadHelper<Blason> loadHelper;
	static {
		try {
			loadHelper = new LoadHelper<Blason>(new SqlLoadHandler<Blason>(ApplicationCore.dbConnection, Blason.class));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construit un blason à partir d'un jeux de résultat transmis en parametre.<br>
	 * Le jeux de résultat doit posseder les champs de la table BLASONS.
	 * Si le jeux de résultat n'est pas valide, retourne une exception <i>SQLException</i>
	 * 
	 * @param numblason l'id du blason à charger
	 * @return le blason construit à partir du jeux de résultat
	 * @throws SQLException retourné si le jeux de résultat ne contient pas l'ensemble<br>
	 * des champs de la table BLASONS 
	 */
	public static Blason getBlason(int numblason) throws ObjectPersistanceException {
		Blason blason = new Blason();
		blason.setNumblason(numblason); 
		
		loadHelper.load(blason);
		
		blason.setAncrages(AncragesMapBuilder.getAncragesMap(blason));
		
		return blason;
	}
	
	/**
	 * Construit un blason au caracteristique standard à partir de sa taille.<br>
	 * Dans ce cas, on considère que le blason à une forme carré dans le calcul de position
	 * et de ratio.
	 * 
	 * @param size la taille du blason à créer
	 * @return le blason créer
	 */
	public static Blason getBlasonBySize(int size) {
		Blason blason = null;
		
		double hRatio = 1;
		double vRatio = 1;
		int nbArcher = 4;
		ConcurrentMap<Integer, Ancrage> ancrages = new ConcurrentHashMap<Integer, Ancrage>();
		ancrages.put(Ancrage.POSITION_ABCD, new Ancrage(Ancrage.POSITION_ABCD, 0, 0));
		if(size <= 60) {
			hRatio = 0.5;
			nbArcher = 2;
			ancrages.clear();
			ancrages.put(Ancrage.POSITION_AC, new Ancrage(Ancrage.POSITION_AC, 0, 0));
			ancrages.put(Ancrage.POSITION_BD, new Ancrage(Ancrage.POSITION_BD, 0, 0.5));
		}
		if(size <= 40) {
			vRatio = 0.5;
			nbArcher = 1;
			ancrages.clear();
			ancrages.put(Ancrage.POSITION_A, new Ancrage(Ancrage.POSITION_A, 0, 0));
			ancrages.put(Ancrage.POSITION_B, new Ancrage(Ancrage.POSITION_B, 0, 0.5));
			ancrages.put(Ancrage.POSITION_C, new Ancrage(Ancrage.POSITION_C, 0.5, 0));
			ancrages.put(Ancrage.POSITION_D, new Ancrage(Ancrage.POSITION_D, 0.5, 0.5));
		}
		
		blason = new Blason(size + "cm", hRatio, vRatio); //$NON-NLS-1$
		blason.setNbArcher(nbArcher);
		blason.setAncrages(ancrages);
		
		return blason;
	}
}
