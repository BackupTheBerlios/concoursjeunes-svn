/*
 * Créer le 13 avr. 2009 à 20:57:53 pour ConcoursJeunes
 *
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

import java.sql.SQLException;
import java.util.Collections;

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
 * Représente le niveau d'une compétition.
 * 
 * @author Aurélien JEOFFRAY
 */
@XmlAccessorType(XmlAccessType.FIELD)
@SqlTable(name="NIVEAU_COMPETITION")
@SqlPrimaryKey(fields={"CODENIVEAU","NUMFEDERATION","LANG"})
@SqlForeignFields(fields={"NUMFEDERATION"})
public class CompetitionLevel implements SqlPersistance {
	@XmlTransient
	@SqlField(name="CODENIVEAU")
	private int numlevel = 0;
	@SqlField(name="LANG")
	private String lang = "fr"; //$NON-NLS-1$
	@SqlField(name="LIBELLE")
	private String libelle = ""; //$NON-NLS-1$
	@SqlField(name="DEFAUT")
	private boolean defaut = false;
	
	@XmlTransient
	private Federation federation;
	
	private static SqlStoreHelper<CompetitionLevel> helper = null;
	static {
		try {
			helper = new SqlStoreHelper<CompetitionLevel>(ApplicationCore.dbConnection, CompetitionLevel.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public CompetitionLevel() {
		
	}

	/**
	 * @return code
	 */
	public int getNumLevel() {
		return numlevel;
	}

	/**
	 * @param code code à définir
	 */
	public void setNumLevel(int code) {
		this.numlevel = code;
	}
	
	/**
	 * @return federation
	 */
	public Federation getFederation() {
		return federation;
	}

	/**
	 * @param federation federation à définir
	 */
	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	/**
	 * @return lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang lang à définir
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle libelle à définir
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	/**
	 * @param defaut defaut à définir
	 */
	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}

	/**
	 * @return defaut
	 */
	public boolean isDefaut() {
		return defaut;
	}

	/** 
	 * Sauvegarde en base un niveau de compétition
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#save()
	 * 
	 * @throws SQLException
	 */
	@Override
	public void save() throws SqlPersistanceException {
		helper.save(this, Collections.singletonMap("NUMFEDERATION", (Object)federation.getNumFederation())); //$NON-NLS-1$
	}

	/** 
	 * Sauvegarde de la base le niveau de compétition
	 * 
	 * @see org.ajdeveloppement.commons.sql.SqlPersistance#delete()
	 * compte.
	 * 
	 * @throws SQLException
	 */
	@Override
	public void delete() throws SqlPersistanceException {
		helper.delete(this, Collections.singletonMap("NUMFEDERATION", (Object)federation.getNumFederation())); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numlevel;
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
		CompetitionLevel other = (CompetitionLevel) obj;
		if (numlevel != other.numlevel)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return libelle;
	}
}
