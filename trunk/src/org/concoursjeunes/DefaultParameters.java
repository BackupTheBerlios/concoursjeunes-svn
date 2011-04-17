/*
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
package org.concoursjeunes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Paramétrage commun à Configuration et Paramètre
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DefaultParameters {
	private Entite club				= new Entite();
	private String intituleConcours = "Spécial Jeunes"; //$NON-NLS-1$
	
	private int nbCible             = 10;
	private int nbTireur            = 4;
	private int nbDepart            = 1;
	
	protected transient final PropertyChangeSupport pcs = new PropertyChangeSupport( this );

	public DefaultParameters() {

	}
	
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.removePropertyChangeListener(propertyChangeListener);
	}

	/**
	 * Retourne l'intitulé du concours
	 * 
	 * @return Renvoie l'intitulé du concours
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * Définit l'intitulé du concours ou intitulé par défaut
	 * 
	 * @param intituleConcours l'intitulé du concours
	 */
	public void setIntituleConcours(String intituleConcours) {
		Object oldValue = this.intituleConcours;
		
		this.intituleConcours = intituleConcours;
		
		pcs.firePropertyChange("intituleConcours", oldValue, intituleConcours); //$NON-NLS-1$
	}
	
	/**
	 * Retourne le nombre de cible sur le concours
	 * 
	 * @return le nombre de cible
	 */
	public int getNbCible() {
		return nbCible;
	}

	/**
	 * Définit le nombre de cible sur le concours
	 * 
	 * @param nbCible le nombre de cible
	 */
	public void setNbCible(int nbCible) {
		Object oldValue = this.nbCible;
		
		this.nbCible = nbCible;
		
		pcs.firePropertyChange("nbCible", oldValue, nbCible); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre de départ sur le concours
	 * 
	 * @return le nombre de départ
	 */
	public int getNbDepart() {
		return nbDepart;
	}

	/**
	 * Définit le nombre de départ sur le concours.
	 * 
	 * @param nbDepart le nombre de départ du concours.
	 */
	public void setNbDepart(int nbDepart) {
		Object oldValue = this.nbDepart;
		
		this.nbDepart = nbDepart;
		
		pcs.firePropertyChange("nbDepart", oldValue, nbDepart); //$NON-NLS-1$
	}

	/**
	 * Retourne le nombre de tireur par cible accepté sur le concours<br>
	 * Ce nombre est de:
	 * <ul>
	 * <li>2 pour un rythme AB</li>
	 * <li>3 pour un rythme ABC (Rare)</li>
	 * <li>4 pour un rythme AB.CD</li>
	 * <li>6 pour un rythme ABC.DEF (Rare)</li>
	 * </ul>
	 * A l'heure actuel, l'interface graphique ne supporte que les modes 2 et 4,
	 * les modes 3 et 6 sont déconseillé car pouvant entraîner des cas non déterminé
	 * 
	 * @return renvoie le nombre de tireur par cible
	 */
	public int getNbTireur() {
		return nbTireur;
	}

	/**
	 * Définit le nombre de tireur par cible accepté sur le concours<br>
	 * Ce nombre est de:
	 * <ul>
	 * <li>2 pour un rythme AB</li>
	 * <li>3 pour un rythme ABC</li>
	 * <li>4 pour un rythme AB.CD</li>
	 * <li>6 pour un rythme ABC.DEF (Rare)</li>
	 * </ul>
	 * A l'heure actuel, l'interface graphique ne supporte que les modes 2, 3 et 4,
	 * le 6 est déconseillé car pouvant entraîner des cas non déterminé
	 * 
	 * @param nbTireur le nombre de tireur par cible
	 */
	public void setNbTireur(int nbTireur) {
		Object oldValue = this.nbTireur;
		
		this.nbTireur = nbTireur;
		
		pcs.firePropertyChange("nbTireur", oldValue, nbTireur); //$NON-NLS-1$
	}

	/**
	 * Retourne le club organisateur du concours
	 * 
	 * @return le club organisateur
	 */
	public Entite getClub() {
		return club;
	}

	/**
	 * Définit le club organisateur du concours
	 * 
	 * @param club le club organisateur
	 */
	public void setClub(Entite club) {
		Object oldValue = this.club;
		
		this.club = club;
		
		pcs.firePropertyChange("club", oldValue, club); //$NON-NLS-1$
	}
}
