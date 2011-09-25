/*
 * Créé le 21 nov. 07 à 11:21:11 pour ArcCompetition
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.ajdeveloppement.concours;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Représente une position sur le pas de tir
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetPosition implements Cloneable {
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;
	
	private int target = 0;
	private int position = A;
	
	public TargetPosition() {
		
	}
	
	/**
	 * Initialise une nouvelle position sur cible
	 * 
	 * @param target le numéro de la cible
	 * @param position la position sur la cible (O->A,1->B,2->C,3->D)
	 */
	public TargetPosition(int target, int position) {
		super();
		this.target = target;
		this.position = position;
	}
	
	/**
	 * Retourne le numéro de cible
	 * 
	 * @return target le numéro de cible
	 */
	public int getTarget() {
		return target;
	}
	
	/**
	 * Définit le numéro de la cible
	 * 
	 * @param target le numéro de la cible
	 */
	public void setTarget(int target) {
		this.target = target;
	}
	
	/**
	 * Retourne l'index de la position sur cible
	 * 
	 * @return l'index de la position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Retourne la position sous la forme d'une lettre (A,B,C,D)
	 * 
	 * @return la position en lettre
	 */
	public String getStringPosition() {
		return String.valueOf((char) ('A' + position));
	}
	
	/**
	 * Définit la position sur la cible
	 * 
	 * @param position l'index de la position
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Clone la position de cible
	 */
	@Override
	public TargetPosition clone() {
		try {
			return (TargetPosition)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
	
	/**
	 * Retourne la position sur le pas de tir
	 * sous la forme XXY ou XX représente le numéro de cible
	 * et Y la position (A,B,C,D) sur celle ci.
	 * 
	 * @return la position au format XXY
	 */
	@Override
	public String toString() {
		return new DecimalFormat("00").format(target) + getStringPosition(); //$NON-NLS-1$
	}
	
	/**
	 * Retourne la position sur le pas de tir
	 * sous la forme XXY ou XX représente le numéro de cible
	 * et Y laposition (A,B,C,D) sur celle ci.
	 * 
	 * @param target le numéro de la cible
	 * @param position l'index de la position
	 * @return la position au format XXY
	 */
	public static String toString(int target, int position) {
		return new TargetPosition(target, position).toString();
	}
}
