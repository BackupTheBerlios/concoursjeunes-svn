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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Entite organisationnelle.<br>
 * Une entite peut représenté
 * <ul>
 * <li>Une Fédération</li>
 * <li>Une ligue</li>
 * <li>Un comité départemental</li>
 * <li>Un club</li>
 * </ul>
 * 
 * @author Aurélien JEOFFRAY
 */
public class Entite {
    
    private String nom        	= ""; //$NON-NLS-1$
    private String agrement		= ""; //$NON-NLS-1$
    private String adresse   	= ""; //$NON-NLS-1$
    private String codePostal	= ""; //$NON-NLS-1$
    private String ville      	= ""; //$NON-NLS-1$
    private String note		 	= ""; //$NON-NLS-1$
    private int type          	= CLUB;
    
    public static final int FEDERATION = 0;
    public static final int LIGUE = 1;
    public static final int CD = 2;
    public static final int CLUB = 3;
    
    public Entite() {
        
    }
    
    /**
     * Construit une nouvelle entité nommé ayant le type fournit en parametre
     * 
     * @param nom le nom de l'entite
     * @param type le type de l'entite
     */
    public Entite(String nom, int type) {
        this.nom = nom;
        this.type = type;
    }

    /**
     * Retourne l'adresse de l'entite
     * 
	 * @return l'adresse de l'entite
	 */
    public String getAdresse() {
        return adresse;
    }
    

    /**
     * Retourne le numero d'agrement identifiant de manière unique l'entite
     * 
	 * @return le numero d'agrement
	 */
    public String getAgrement() {
        return agrement;
    }
    

    /**
     * Retourne le code postal de l'adresse de l'entite
     * 
	 * @return le code postal
	 */
    public String getCodePostal() {
        return codePostal;
    }
    

    /**
     * Retourne le nom de l'entite
     * 
	 * @return le nom
	 */
    public String getNom() {
        return nom;
    }
    

    /**
     * Retourne le type d'entite.<br>
     * Les types possible sont:
     * FEDERATION, LIGUE, CD, CLUB
     * 
	 * @return le type de l'entite
	 */
    public int getType() {
        return type;
    }
    

    /**
     * Retourne la ville de l'entite
     * 
	 * @return la ville de l'entite
	 */
    public String getVille() {
        return ville;
    }
    

    /**
     * Définit l'adresse de l'entite
     * 
	 * @param adresse l'adresse de l'entite
	 */
    public void setAdresse(String adresse) {
    	if(adresse == null)
    		adresse = ""; //$NON-NLS-1$
        this.adresse = adresse;
    }
    

    /**
     * Définit le numero d'agrement identifiant de manière unique l'entite
     * 
	 * @param agrement le numero d'agrement
	 */
    public void setAgrement(String agrement) {
        this.agrement = agrement;
    }
    

    /**
     * Définit le code postal de l'adresse de l'entite
     * 
	 * @param codePostal le code postal
	 */
    public void setCodePostal(String codePostal) {
    	if(codePostal == null)
    		codePostal = ""; //$NON-NLS-1$
        this.codePostal = codePostal;
    }
    

    /**
     * Définit le nom de l'entite
     * 
	 * @param nom le nom
	 */
    public void setNom(String nom) {
        this.nom = nom;
    }
    

    /**
     * Définit le type d'entite.<br>
     * Les types possible sont:
     * FEDERATION, LIGUE, CD, CLUB
     * 
	 * @param type le type d'entite
	 */
    public void setType(int type) {
        this.type = type;
    }
    

    /**
     * Définit la ville de l'entite
     * 
	 * @param ville la ville de l'entite
	 */
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    @Override
    public String toString() {
        return nom;
    }

	/**
	 * Retourne une note ou commentaire préalablement
	 * définit sur l'entite
	 * 
	 * @return une note sur l'entite
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Ajoute une note ou commentaire sur l'entite
	 * 
	 * @param note une note sur l'entite
	 */
	public void setNote(String note) {
		if(note == null)
			note = ""; //$NON-NLS-1$
		this.note = note;
	}
	
	/**
	 * Sauvegarde l'entite dans la base de donnée
	 */
	public void save() {
		try {
			String sql = "MERGE INTO Entite (AGREMENTENTITE, NOMENTITE, " + //$NON-NLS-1$
					"ADRESSEENTITE, CODEPOSTALENTITE, VILLEENTITE, NOTEENTITE, TYPEENTITE) " + //$NON-NLS-1$
					"VALUES (?, ?, ?, ?, ?, ?, ?)"; //$NON-NLS-1$
			PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
			pstmt.setString(1, agrement);
			pstmt.setString(2, nom);
			pstmt.setString(3, adresse);
			pstmt.setString(4, codePostal);
			pstmt.setString(5, ville);
			pstmt.setString(6, note);
			pstmt.setInt(7, type);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Liste les entites stocké dans la base correspondant à l'entite generique
	 * transmis en parametre et ordonné par le nom du champs fournit<br>
	 * <br>
	 * Une entite générique est une entite ou seul l'une des 3 propriétés
	 * (Nom, Agrement, Ville) est renseigné. L'utilisation des wildcards SQL
	 * est possible (%, _)
	 * 
	 * @param eGeneric l'entite générique permettant de filtré les résultats
	 * @param orderfield le champs de tri de la liste
	 * 
	 * @return la liste des entite répondant aux critères de recherche
	 */
	public static List<Entite> getEntitesInDatabase(Entite eGeneric, String orderfield) {
		List<Entite> entites = new ArrayList<Entite>();
		Statement stmt = null;
		
		try {
			stmt = ApplicationCore.dbConnection.createStatement();
			
			String sql = "select * from Entite "; //$NON-NLS-1$
			if(eGeneric != null) {
				sql += "where "; //$NON-NLS-1$
				ArrayList<String> filters = new ArrayList<String>();
				if(eGeneric.getNom().length() > 0) {
					filters.add("NOMENTITE like '" + eGeneric.getNom().toUpperCase() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(eGeneric.getAgrement().length() > 0) {
					filters.add("AGREMENTENTITE like '" + eGeneric.getAgrement().toUpperCase() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(eGeneric.getVille().length() > 0) {
					filters.add("VILLEENTITE like '" + eGeneric.getVille().toUpperCase() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				for(String filter : filters) {
					sql += " and " + filter; //$NON-NLS-1$
				}
			}
			sql = sql.replaceFirst(" and ", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if(orderfield.length() > 0)
				sql += "order by " + orderfield; //$NON-NLS-1$
			
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()) {
				Entite entite = new Entite();
				entite.setAgrement(rs.getString("AgrementEntite")); //$NON-NLS-1$
				entite.setNom(rs.getString("NomEntite")); //$NON-NLS-1$
				entite.setAdresse(rs.getString("AdresseEntite")); //$NON-NLS-1$
				entite.setCodePostal(rs.getString("CodePostalEntite")); //$NON-NLS-1$
				entite.setVille(rs.getString("VilleEntite")); //$NON-NLS-1$
				entite.setNote(rs.getString("NoteEntite")); //$NON-NLS-1$
				entite.setType(rs.getInt("TypeEntite")); //$NON-NLS-1$
				
				entites.add(entite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(Exception e) { }
		}
		
		return entites;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((agrement == null) ? 0 : agrement.hashCode());
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
		final Entite other = (Entite) obj;
		if (agrement == null) {
			if (other.agrement != null)
				return false;
		} else if (!agrement.equals(other.agrement))
			return false;
		return true;
	}
	
	
}