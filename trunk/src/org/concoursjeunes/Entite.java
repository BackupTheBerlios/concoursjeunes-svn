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

/**
 * Entite organisationnelle
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
    
    public Entite(String nom, int type) {
        this.nom = nom;
        this.type = type;
    }

    /**
	 * @return  Returns the adresse.
	 * @uml.property  name="adresse"
	 */
    public String getAdresse() {
        return adresse;
    }
    

    /**
	 * @return  Returns the agrement.
	 * @uml.property  name="agrement"
	 */
    public String getAgrement() {
        return agrement;
    }
    

    /**
	 * @return  Returns the codePostal.
	 * @uml.property  name="codePostal"
	 */
    public String getCodePostal() {
        return codePostal;
    }
    

    /**
	 * @return  Returns the nom.
	 * @uml.property  name="nom"
	 */
    public String getNom() {
        return nom;
    }
    

    /**
	 * @return  Returns the type.
	 * @uml.property  name="type"
	 */
    public int getType() {
        return type;
    }
    

    /**
	 * @return  Returns the ville.
	 * @uml.property  name="ville"
	 */
    public String getVille() {
        return ville;
    }
    

    /**
	 * @param adresse  The adresse to set.
	 * @uml.property  name="adresse"
	 */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    

    /**
	 * @param agrement  The agrement to set.
	 * @uml.property  name="agrement"
	 */
    public void setAgrement(String agrement) {
        this.agrement = agrement;
    }
    

    /**
	 * @param codePostal  The codePostal to set.
	 * @uml.property  name="codePostal"
	 */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    

    /**
	 * @param nom  The nom to set.
	 * @uml.property  name="nom"
	 */
    public void setNom(String nom) {
        this.nom = nom;
    }
    

    /**
	 * @param type  The type to set.
	 * @uml.property  name="type"
	 */
    public void setType(int type) {
        this.type = type;
    }
    

    /**
	 * @param ville  The ville to set.
	 * @uml.property  name="ville"
	 */
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    @Override
    public String toString() {
        return nom;
    }

	/**
	 * @return  Returns the note.
	 * @uml.property  name="note"
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note  The note to set.
	 * @uml.property  name="note"
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	public void save() {
		try {
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
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
	
	public static ArrayList<Entite> getEntitesInDatabase(Entite eGeneric, String orderfield) {
		ArrayList<Entite> entites = new ArrayList<Entite>();
		Statement stmt = null;
		
		try {
			stmt = ConcoursJeunes.dbConnection.createStatement();
			
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