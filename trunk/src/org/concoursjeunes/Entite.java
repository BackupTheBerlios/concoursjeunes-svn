/**
 * 
 */
package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Entite organisationnelle
 * @author  Aurelien
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
	
	public static ArrayList<Entite> getEntitesInDatabase(Entite eGeneric, String orderfield) {
		ArrayList<Entite> entites = new ArrayList<Entite>();
		Statement stmt = null;
		
		try {
			stmt = ConcoursJeunes.dbConnection.createStatement();
			
			String sql = "select * from Entite ";
			if(eGeneric != null) {
				sql += "where ";
				ArrayList<String> filters = new ArrayList<String>();
				if(eGeneric.getNom().length() > 0) {
					filters.add("NOMENTITE like \"" + eGeneric.getNom().toUpperCase() + "\"");
				}
				if(eGeneric.getAgrement().length() > 0) {
					filters.add("AGREMENTENTITE like \"" + eGeneric.getAgrement().toUpperCase() + "\"");
				}
				if(eGeneric.getVille().length() > 0) {
					filters.add("VILLEENTITE like \"" + eGeneric.getVille().toUpperCase() + "\"");
				}
				
				for(String filter : filters) {
					sql += " and " + filter;
				}
			}
			sql = sql.replaceFirst(" and ", "");
			if(orderfield.length() > 0)
				sql += "order by " + orderfield;
			
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()) {
				Entite entite = new Entite();
				entite.setAgrement(rs.getString("AgrementEntite"));
				entite.setNom(rs.getString("NomEntite"));
				entite.setAdresse(rs.getString("AdresseEntite"));
				entite.setCodePostal(rs.getString("CodePostalEntite"));
				entite.setVille(rs.getString("VilleEntite"));
				entite.setNote(rs.getString("NoteEntite"));
				entite.setType(rs.getInt("TypeEntite"));
				
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