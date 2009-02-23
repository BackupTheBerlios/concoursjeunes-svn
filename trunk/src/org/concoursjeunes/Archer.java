/*
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Class représentant un archer independament d'un concours
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class Archer implements Cloneable {

	private String nomArcher        = ""; //$NON-NLS-1$
	private String prenomArcher     = ""; //$NON-NLS-1$
	private String numLicenceArcher = ""; //$NON-NLS-1$
	private Entite club             = new Entite();
	private boolean certificat      = false;
	private boolean handicape		= false;
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Constructeur vide nécessaire à l'initialisation correct de l'objet
	 * dans le cas d'une deserialisation XML
	 *
	 */
	public Archer() { }
	
	/**
	 * permet d'écouter les modifications des propriété de l'archer
	 * 
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	/**
	 * 
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	/**
	 * Retourne le numero de licence de l'archer
	 * 
	 * @return le numero de licence (pour la ffta, 6 chiffres + 1 lettre)
	 */
	public String getNumLicenceArcher() {
		return numLicenceArcher;
	}

	/**
	 * Définit le numero de licence de l'archer
	 * 
	 * @param numLicenceArcher le numero de licence de l'archer
	 */
	public void setNumLicenceArcher(String numLicenceArcher) {
		String oldValue = this.numLicenceArcher;
		
		this.numLicenceArcher = numLicenceArcher;
		
		pcs.firePropertyChange("numLicenceArcher", oldValue, numLicenceArcher); //$NON-NLS-1$
	}

	/**
	 * Retourne le nom de l'archer
	 * 
	 * @return le nom de l'archer
	 */
	public String getNomArcher() {
		return nomArcher;
	}

	/**
	 * Définit le nom de l'archer
	 * 
	 * @param nomArcher le nom de l'archer
	 */
	public void setNomArcher(String nomArcher) {
		String oldValue = this.nomArcher;
		
		this.nomArcher = nomArcher;
		
		pcs.firePropertyChange("nomArcher", oldValue, nomArcher); //$NON-NLS-1$
	}

	/**
	 * Retourne le prénom de l'archer
	 * 
	 * @return le prenom de l'archer
	 */
	public String getPrenomArcher() {
		return prenomArcher;
	}

	/**
	 * Définit le prénom de l'archer
	 * 
	 * @param prenomArcher le prenom de l'archer
	 */
	public void setPrenomArcher(String prenomArcher) {
		String oldValue = this.prenomArcher;
		
		this.prenomArcher = prenomArcher;
		
		pcs.firePropertyChange("prenomArcher", oldValue, prenomArcher); //$NON-NLS-1$
	}

	/**
	 * Retourne le club (Compagnie) auquel appartient l'archer
	 * 
	 * @return club le club de l'archer
	 */
	public Entite getClub() {
		return club;
	}

	/**
	 * Définit le club (Entite legal) de l'archer
	 * 
	 * @param club l'objet Entite representant le club de l'archer
	 */
	public void setClub(Entite club) {
		Entite oldValue = this.club;
		
		this.club = club;
		
		pcs.firePropertyChange("club", oldValue, club); //$NON-NLS-1$
	}
	/**
	 * Indique si l'archer possede ou non un certificat medical de non contre indiquation
	 * à la pratique du tir à l'arc en competition
	 * 
	 * @return true si l'archer possede un certificat, false sinon
	 */
	public boolean isCertificat() {
		return certificat;
	}

	/**
	 * Définit si l'archer possede (true) ou non (false) un certificat
	 * 
	 * @param certificat true si l'archer possede un certificat, false sinon
	 */
	public void setCertificat(boolean certificat) {
		boolean oldValue = this.certificat;
		
		this.certificat = certificat;
		
		pcs.firePropertyChange("certificat", oldValue, certificat); //$NON-NLS-1$
	}

	/**
	 * Est ce que l'archer est handicapé ou non?
	 * 
	 * @return true si l'archer est handicapé, false sinon
	 */
	public boolean isHandicape() {
		return handicape;
	}

	/**
	 * Définit si l'archer est handicapé ou non
	 * 
	 * @param handicape true si l'archer est handicapé, false sinon
	 */
	public void setHandicape(boolean handicape) {
		boolean oldValue = this.handicape;
		
		this.handicape = handicape;
		
		pcs.firePropertyChange("handicape", oldValue, handicape); //$NON-NLS-1$
	}

	/**
	 * Retourne l'identifiant de l'archer (Concatenation du nom et du prenom)
	 * 
	 * @return l'identifiant de l'archer
	 */
	public String getID() {
		return nomArcher + " " + prenomArcher; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		
		if(!numLicenceArcher.isEmpty()) {
			result = PRIME * result + ((numLicenceArcher == null) ? 0 : numLicenceArcher.hashCode());
		} else {
			result = PRIME * result + ((nomArcher == null) ? 0 : nomArcher.hashCode());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Archer other = (Archer) obj;
		if(numLicenceArcher == null || numLicenceArcher.isEmpty()) {
			if (nomArcher == null) {
				if (other.getNomArcher() != null)
					return false;
			} else if (!nomArcher.equals(other.getNomArcher()))
				return false;
		} else {
			if (!numLicenceArcher.equals(other.getNumLicenceArcher()))
				return false;
		}
		return true;
	}
	
	@Override
	protected Archer clone() throws CloneNotSupportedException {
		Archer clone = (Archer)super.clone();
		clone.pcs = new PropertyChangeSupport(clone);
		
		return clone;
	}
}
