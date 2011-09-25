/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Informations générale sur une fiche concours. Permet de disposer d'information
 * sur un concours sans avoir à charger intégralement sa fiche.
 * 
 * @author Aurélien JEOFFRAY
 */
public class MetaDataFicheConcours implements PropertyChangeListener {
	private Date dateConcours;
	private String intituleConcours;
	private String filenameConcours;
	
	public MetaDataFicheConcours() { }
	
	/**
	 * Construit un fichier d'information sur une fiche concours avec les informations fournit en paramètre.
	 * 
	 * @param dateConcours la date du concours
	 * @param intituleConcours l'intitulé du concours
	 * @param filenameConcours le nom du fichier physique stockant la sérialisation d'un concours 
	 */
	public MetaDataFicheConcours(Date dateConcours, String intituleConcours, String filenameConcours) {
		this.dateConcours = dateConcours;
		this.intituleConcours = intituleConcours;
		this.filenameConcours = filenameConcours;
	}

	/**
	 * Retourne la date de début du concours
	 * 
	 * @return la date de début du concours
	 */
	public Date getDateConcours() {
		return dateConcours;
	}

	/**
	 * Définit la date de début du concours
	 * 
	 * @param dateConcours la date de début du concours
	 */
	public void setDateConcours(Date dateConcours) {
		this.dateConcours = dateConcours;
	}

	/**
	 * Retourne l'intitulé du concours
	 * 
	 * @return l'intitulé du concours
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * Définit l'intitulé du concours
	 * 
	 * @param intituleConcours l'intitulé du concours
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}

	/**
	 * Retourne le nom du fichier sérialisé de la fiche concours
	 * 
	 * @return le nom du fichier physique de la fiche concours
	 */
	public String getFilenameConcours() {
		return filenameConcours;
	}

	/**
	 * Définit le nom du fichier sérialisé de la fiche concours
	 * 
	 * @param filenameConcours le nom du fichier physique de la fiche concours
	 */
	public void setFilenameConcours(String filenameConcours) {
		this.filenameConcours = filenameConcours;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof Parametre) {
			Parametre param = (Parametre)evt.getSource();
			if(evt.getPropertyName().equals("date")) { //$NON-NLS-1$
				setDateConcours(param.getDateDebutConcours());
			} else if(evt.getPropertyName().equals("intituleConcours")) { //$NON-NLS-1$
				setIntituleConcours(param.getIntituleConcours());
			} else if(evt.getPropertyName().equals("saveName")) { //$NON-NLS-1$
				setFilenameConcours(param.getSaveName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dateConcours == null) ? 0 : dateConcours.hashCode());
		result = PRIME * result + ((filenameConcours == null) ? 0 : filenameConcours.hashCode());
		result = PRIME * result + ((intituleConcours == null) ? 0 : intituleConcours.hashCode());
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
		final MetaDataFicheConcours other = (MetaDataFicheConcours) obj;
		if (dateConcours == null) {
			if (other.dateConcours != null)
				return false;
		} else if (!dateConcours.equals(other.dateConcours))
			return false;
		if (filenameConcours == null) {
			if (other.filenameConcours != null)
				return false;
		} else if (!filenameConcours.equals(other.filenameConcours))
			return false;
		if (intituleConcours == null) {
			if (other.intituleConcours != null)
				return false;
		} else if (!intituleConcours.equals(other.intituleConcours))
			return false;
		return true;
	}
}