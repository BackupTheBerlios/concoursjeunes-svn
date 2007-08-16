/**
 * 
 */
package ajinteractive.concours;

/**
 * Entite organisationnelle
 * 
 * @author Aurelien
 *
 */
public class Entite {
    
    private String nom        	= "";
    private String agrement		= "";
    private String adresse   	= "";
    private String codePostal	= "";
    private String ville      	= "";
    private String note		 	= "";
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
     * @return Returns the adresse.
     */
    public String getAdresse() {
        return adresse;
    }
    

    /**
     * @return Returns the agrement.
     */
    public String getAgrement() {
        return agrement;
    }
    

    /**
     * @return Returns the codePostal.
     */
    public String getCodePostal() {
        return codePostal;
    }
    

    /**
     * @return Returns the nom.
     */
    public String getNom() {
        return nom;
    }
    

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
    

    /**
     * @return Returns the ville.
     */
    public String getVille() {
        return ville;
    }
    

    /**
     * @param adresse The adresse to set.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    

    /**
     * @param agrement The agrement to set.
     */
    public void setAgrement(String agrement) {
        this.agrement = agrement;
    }
    

    /**
     * @param codePostal The codePostal to set.
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    

    /**
     * @param nom The nom to set.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    

    /**
     * @param type The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }
    

    /**
     * @param ville The ville to set.
     */
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    @Override
    public String toString() {
        return nom;
    }

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}
}