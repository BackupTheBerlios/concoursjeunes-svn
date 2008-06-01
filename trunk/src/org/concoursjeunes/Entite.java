/*
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
			Statement stmt = ApplicationCore.dbConnection.createStatement();
			
			stmt.executeUpdate("MERGE INTO Entite (AGREMENTENTITE, NOMENTITE, " + //$NON-NLS-1$
					"ADRESSEENTITE, CODEPOSTALENTITE, VILLEENTITE, NOTEENTITE, TYPEENTITE) " + //$NON-NLS-1$
					"VALUES ('" + agrement + "', '"  //$NON-NLS-1$ //$NON-NLS-2$
					+ nom.replaceAll("'", "''") + "', '"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ adresse.replaceAll("'", "''") + "', '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ codePostal + "', '"  //$NON-NLS-1$
					+ ville.replaceAll("'", "''") + "', '"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ note.replaceAll("'", "''") + "', "  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ type + ")"); //$NON-NLS-1$
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