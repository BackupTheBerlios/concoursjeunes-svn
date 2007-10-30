/*
 * Created on 18 janv. 2005
 *
 */
package ajinteractive.concours;

/**
 * parametre de configuration de l'application
 * 
 * @author Aurelien Jeoffray
 * @version 2.1
 *
 */
public class Configuration extends DefaultParameters {
	
    private String langue           = "";               //reboot //$NON-NLS-1$
    private String logoPath         = "ressources/logo_ffta.gif";   //noreboot //$NON-NLS-1$
    
    private String pdfReaderPath    = "";               //noreboot //$NON-NLS-1$
    
    private String formatPapier     = "A4";             //noreboot //$NON-NLS-1$
    private String orientation      = "portrait";       //noreboot //$NON-NLS-1$
    private int[] colonneAndLigne   = new int[] {9, 3}; //noreboot
    private Marges marges           = new Marges();     //noreboot
    private double[] espacements    = new double[] {0.5, 0.5};//noreboot
    
    private boolean interfaceResultatCumul = false;     //noreboot
    private boolean interfaceResultatSupl = true;       //noreboot
    private boolean interfaceAffResultatExEquo = true;  //noreboot
    
    //propriete caché
    private boolean firstboot       = false;            //noreboot
    private boolean officialProfile = false;            //noreboot
    private String curProfil        = "";               //noreboot //$NON-NLS-1$

	public Configuration() {
        
	}
	
	/**
	 * Retourne la langue courante de l'IHM
	 * 
	 * @return String - le code langue
	 */
	public String getLangue() {
		return this.langue;
	}
	
	/**
	 * Retourne l'adresse du lecteur pdf
	 * 
	 * @return String - l'adresse du lecteur pdf
	 */
	public String getPdfReaderPath() {
		return this.pdfReaderPath;
	}
	
	/**
	 * defini la langue de l'IHM
	 * 
	 * @param langue - la langue de l'application
	 */
	public void setLangue(String langue) {
		this.langue = langue;
	}
	
	/**
	 * defini l'adresse du lecteur pdf
	 * 
	 * @param pdfReaderPath - l'adresse du lecteur pdf
	 */
	public void setPdfReaderPath(String pdfReaderPath) {
		this.pdfReaderPath = pdfReaderPath;
	}

    /**
     * nombre de colonne et de ligne sur une page d'étiquettes
     * 
     * @return Returns the colonneAndLigne.
     */
    public int[] getColonneAndLigne() {
        return this.colonneAndLigne;
    }

    /**
     * Espacements entre 2 cellules d'étiquettes
     * 
     * @return Returns the espacements.
     */
    public double[] getEspacements() {
        return this.espacements;
    }

    /**
     * Format du papier étiquettes
     * 
     * @return Returns the formatPapier.
     */
    public String getFormatPapier() {
        return this.formatPapier;
    }
    
    /**
     * Orientation du papier étiquettes
     * 
     * @return Returns the orientation.
     */
    public String getOrientation() {
        return this.orientation;
    }

    /**
     * Marge d'impression des étiquettes
     * 
     * @return Returns the marges.
     */
    public Marges getMarges() {
        return this.marges;
    }

    /**
     * Défini le nombre de colonne et ligne d'étiquettes
     * 
     * @param colonneAndLigne The colonneAndLigne to set.
     */
    public void setColonneAndLigne(int[] colonneAndLigne) {
        this.colonneAndLigne = colonneAndLigne;
    }

    /**
     * Définit les espacementes entres cellules d'étiquettes
     * 
     * @param espacements The espacements to set.
     */
    public void setEspacements(double[] espacements) {
        this.espacements = espacements;
    }

    /**
     * Définit le format du papier étiquettes utilisé
     * 
     * @param formatPapier The formatPapier to set.
     */
    public void setFormatPapier(String formatPapier) {
        this.formatPapier = formatPapier;
    }
    
    /**
     * Définit l'orientation de la feuille d'étiquettes
     * 
     * @param orientation The orientation to set.
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
 
    /**
     * Définit les marges d'impression du papier étiquettes
     * 
     * @param marges The marges to set.
     */
    public void setMarges(Marges marges) {
        this.marges = marges;
    }

    /**
     * Donne le nom du profil courrant
     * 
     * @return Returns the curProfil.
     */
    public String getCurProfil() {
        return curProfil;
    }
    

    /**
     * Définit le nom du profil courrant
     * 
     * @param curProfil The curProfil to set.
     */
    public void setCurProfil(String curProfil) {
        this.curProfil = curProfil;
    }

    /**
     * Détéremine si c'est le premier lancement de l'application
     * 
     * @return Returns the firstboot.
     */
    public boolean isFirstboot() {
        return firstboot;
    }

    /**
     * Place l'application sur premier lancement
     * 
     * @param firstboot The firstboot to set.
     */
    public void setFirstboot(boolean firstboot) {
        this.firstboot = firstboot;
    }

	/**
     * Est ce que les champs de cumul doivent être affiché?
     * 
	 * @return Renvoie interfaceResultatCumul.
	 */
	public boolean isInterfaceResultatCumul() {
		return interfaceResultatCumul;
	}

	/**
     * Définit si les champs de cumul doivent être affiché
     * 
	 * @param interfaceResultatCumul interfaceResultatCumul à définir.
	 */
	public void setInterfaceResultatCumul(boolean interfaceResultatCumul) {
		this.interfaceResultatCumul = interfaceResultatCumul;
	}

	/**
     * Est ce qu'on affiche la saisie des 10/9/M
     * 
	 * @return Renvoie interfaceResultatSupl.
	 */
	public boolean isInterfaceResultatSupl() {
		return interfaceResultatSupl;
	}

	/**
     * Définit si l'on affiche la saisie des 10/9/M
     * 
	 * @param interfaceResultatSupl interfaceResultatSupl à définir.
	 */
	public void setInterfaceResultatSupl(boolean interfaceResultatSupl) {
		this.interfaceResultatSupl = interfaceResultatSupl;
	}

	/**
     * Est ce qu'on met en surbriance les ex-aequo?
     * 
	 * @return Renvoie interfaceAffResultatExEquo.
	 */
	public boolean isInterfaceAffResultatExEquo() {
		return interfaceAffResultatExEquo;
	}

	/**
     * Définit si l'on met en surbriance les ex-aequo
     * 
	 * @param interfaceAffResultatExEquo interfaceAffResultatExEquo à définir.
	 */
	public void setInterfaceAffResultatExEquo(boolean interfaceAffResultatExEquo) {
		this.interfaceAffResultatExEquo = interfaceAffResultatExEquo;
	}

    /**
     * Donne le chemin du logo du club
     * 
     * @return Renvoie logoPath.
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * Définit le chemin du logo du club
     * 
     * @param logoPath logoPath à définir.
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    /**
     * Est-ce que c'est un profil officiel?
     * 
     * @return Renvoie officialProfile.
     */
    public boolean isOfficialProfile() {
        return officialProfile;
    }

    /**
     * Définit si c'est un profil officiel
     * 
     * @param officialProfile officialProfile à définir.
     */
    public void setOfficialProfile(boolean officialProfile) {
        this.officialProfile = officialProfile;
    }
}