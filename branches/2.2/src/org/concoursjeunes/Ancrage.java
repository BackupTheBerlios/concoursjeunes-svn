/*
 * Créé le 31 janv. 08 à 22:06:16 pour ConcoursJeunes
 *
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
package org.concoursjeunes;

import java.sql.SQLException;
import java.util.Collections;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.sql.SqlField;
import org.ajdeveloppement.commons.sql.SqlForeignFields;
import org.ajdeveloppement.commons.sql.SqlPersistance;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.sql.SqlStoreHelper;
import org.ajdeveloppement.commons.sql.SqlTable;


/**
 * Représente la position physique relative d'un blason
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="ANCRAGES_BLASONS")
@SqlPrimaryKey(fields={"NUMBLASON","EMPLACEMENT"})
@SqlForeignFields(fields={"NUMBLASON"})
public class Ancrage implements SqlPersistance {
	public static final int POSITION_A = 0;
	public static final int POSITION_B = 1;
	public static final int POSITION_C = 2;
	public static final int POSITION_D = 3;
	public static final int POSITION_ABCD = 4;
	public static final int POSITION_AC = 5;
	public static final int POSITION_BD = 6;
	
	@SqlField(name="EMPLACEMENT")
	private int emplacement = POSITION_A;
	
	@SqlField(name="ANCRAGEX")
	private double x = 0;
	@SqlField(name="ANCRAGEY")
	private double y = 0;
	
	@XmlTransient
	private Blason blason;
	
	private static SqlStoreHelper<Ancrage> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<Ancrage>(ApplicationCore.dbConnection, Ancrage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Ancrage() {
		
	}
	
	/**
	 * Initialise un nouveau point d'ancrage avec les coordonnées
	 * relative transmis en paramètre.
	 * 
	 * @param x valeur entre 0.0 et 1.0 correspondant à la position relative du blason sur l'axe x
	 * @param y valeur entre 0.0 et 1.0 correspondant à la position relative du blason sur l'axe y
	 */
	public Ancrage(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialise un nouveau point d'ancrage avec les coordonnées
	 * relative transmis en paramètre et correspondant à une position d'archer déterminé
	 * 
	 * @param emplacement la position d'archer correpondant au présent ancrage
	 * @param x valeur entre 0.0 et 1.0 correspondant à la position relative du blason sur l'axe x
	 * @param y valeur entre 0.0 et 1.0 correspondant à la position relative du blason sur l'axe y
	 */
	public Ancrage(int emplacement, double x, double y) {
		this.emplacement = emplacement;
		this.x = x;
		this.y = y;
	}

	/**
	 * Retourne le blason associé à l'ancrage
	 * 
	 * @return blason le blason associé à l'ancrage
	 */
	public Blason getBlason() {
		return blason;
	}

	/**
	 * Définit le blason associé à l'ancrage
	 * 
	 * @param blason le blason associé à l'ancrage
	 */
	public void setBlason(Blason blason) {
		this.blason = blason;
	}

	/**
	 * Retourne l'emplacement sur la cible de l'ancrage. Correspond aux valeur des
	 * variables static <code>POSITION_</code>
	 * 
	 * @return l'emplacement sur la cible de l'ancrage.
	 */
	public int getEmplacement() {
		return emplacement;
	}

	/**
	 * Définit l'emplacement sur cible de l'ancrage. Correspond aux valeur des
	 * variables static <code>POSITION_</code>
	 * 
	 * @param emplacement l'emplacement sur cible de l'ancrage
	 */
	public void setEmplacement(int emplacement) {
		this.emplacement = emplacement;
	}

	/**
	 * Retourne la position relative du blason sur l'axe horizontal<br>
	 * Les positions sont exprimé en fraction de la taille de la cible
	 * (valeur comprise entre 0 et 1)
	 * 
	 * @return la fraction correspondant à la position du blason sur l'axe X
	 */
	public double getX() {
    	return x;
    }

	/**
	 * Définit la position relative du blason sur l'axe horizontal<br>
	 * Les positions sont exprimé en fraction de la taille de la cible
	 * (valeur double comprise entre 0 et 1)
	 * 
	 * @param x la fraction d'ancrage sur l'axe X
	 */
	public void setX(double x) {
    	this.x = x;
    }

	/**
	 * Retourne la position relative du blason sur l'axe vertical<br>
	 * Les positions sont exprimé en fraction de la taille de la cible
	 * (valeur comprise entre 0 et 1)
	 * 
	 * @return la fraction correspondant à la position du blason sur l'axe Y
	 */
	public double getY() {
    	return y;
    }

	/**
	 * Définit la position relative du blason sur l'axe vertical<br>
	 * Les positions sont exprimé en fraction de la taille de la cible
	 * (valeur double comprise entre 0 et 1)
	 * 
	 * @param y la fraction d'ancrage sur l'axe Y
	 */
	public void setY(double y) {
    	this.y = y;
    }
	
	@Override
	public void save() throws SqlPersistanceException {
		helper.save(this, Collections.singletonMap("NUMBLASON", (Object)blason.getNumblason())); //$NON-NLS-1$
	}
	
	@Override
	public void delete() throws SqlPersistanceException {
		helper.delete(this, Collections.singletonMap("NUMBLASON", (Object)blason.getNumblason())); //$NON-NLS-1$
	}

	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if(parent instanceof Blason)
			blason = (Blason)parent;
	}
}