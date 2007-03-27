package ajinteractive.concours;

/**
 * Objet de Base de stockage des Information sur un concurrent
 *  Nom
 *  Categorie
 *  Licence
 *  Cible
 *  Nombre de volée
 *  points
 * 
 * @author Aurelien Jeoffray
 * @version 3.0
 *
 */
public class Concurrent {
//public:
    /**
     * Statut de l'archer: réservé
     */
    public static final int RESERVEE    = 0;
    /**
     * Statut de l'archer: Payée
     */
    public static final int PAYEE       = 1;
    /**
     * Statut de l'archer: non initialisé
     */
    public static final int UNINIT      = 2;

//private:
	//info Tireur
	private String nomConcurrent        = "";
	private String prenomConcurrent     = "";
	private String numLicenceConcurrent = "";
	private DifferentiationCriteria differentiationCriteria;
	//private DistancesEtBlason distancesEtBlason;
	private String club                 = "";
	private String agrement             = "";
    private boolean certificat          = false;
    private int depart                  = 0;
	private int cible                   = 0;	//position sur le concours
	private int position                = 0;
	public int[] points;
	private int neuf                    = 0;
	private int dix                     = 0;
	private int manque                  = 0;
	private int inscription             = UNINIT;
	 
	/**
	 * Création d'un concurrent avec NombreSerie * NombreVoleeParSerie volées
	 * 
	 */
	public Concurrent() {
	}
	
	/**
	 * Affectation des scores pour le concurrent
	 * 
	 * @param points - la grille des scores du concurrent
	 */
	public void setScore(int[] points) {
		this.points = points;
	}
	
	/**
	 * Donne la grille des scores du concurrent
	 * 
	 * @return int[] - la grille des scores
	 */
	public int[] getScore() {
		return this.points;
	}
	
	/**
	 * Donne le total des points du concurrent
	 * pour classement
	 * 
	 * @return int - le total des points
	 */
	public int getTotalScore() {
		int total = 0;
        if(points != null) {
        		for(int i = 0; i < points.length; i++) {
        			total += points[i];
        		}
        }
		return total;
	}
	/**
	 * Affecte le nombre de dix total du concurrent
	 * 
	 * @param dix
	 */
	public void setNeuf(int dix) {
		this.neuf = dix;
	}
	
	/**
	 * Donne le nombre de dix du concurrent
	 * 
	 * @return int
	 */
	public int getNeuf() {
		return this.neuf;
	}
	
	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * 
	 * @param dixPlus
	 */
	public void setDix(int dixPlus) {
		this.dix = dixPlus;
	}
	
	/**
	 * Donne le nombre de 10+ du concurrent
	 * 
	 * @return int
	 */
	public int getDix() {
		return this.dix;
	}
	
	/**
	 * Affecte le nombre de fleche manquee total du concurrent
	 * 
	 * @param manque
	 */
	public void setManque(int manque) {
		this.manque = manque;
	}
	
	/**
	 * Donne le nombre de fleche manquee du concurrent
	 * 
	 * @return int
	 */
	public int getManque() {
		return this.manque;
	}
	
	/**
	 * affecte le nom du concurrent
	 * 
	 * @param nomConcurrent
	 */
	public void setNom(String nomConcurrent) {
		this.nomConcurrent = nomConcurrent;
	}
	
	/**
	 * donne le nom du concurrent
	 * 
	 * @return String
	 */
	public String getNom() {
		return this.nomConcurrent;
	}
	
	/**
	 * affecte le prenom du concurrent
	 * 
	 * @param prenomConcurrent
	 */
	public void setPrenom(String prenomConcurrent) {
		this.prenomConcurrent = prenomConcurrent;
	}
	
	/**
	 * donne le prenom du concurrent
	 * 
	 * @return String
	 */
	public String getPrenom() {
		return this.prenomConcurrent;
	}
	
	/**
	 * Donne l'identifiant court du concurrent
	 * 
	 * @return String
	 */
	public String getID() {
		return this.nomConcurrent + " " + this.prenomConcurrent;
	}
	
	/**
	 * Affecte le numero de licence du concurrent
	 * 
	 * @param numLicenceConcurrent
	 */
	public void setLicence(String numLicenceConcurrent) {
		this.numLicenceConcurrent = numLicenceConcurrent;
	}
	
	/**
	 * Donne le numero de licence du concurrent
	 * 
	 * @return String
	 */
	public String getLicence() {
		return this.numLicenceConcurrent;
	}
	
	/**
     * @return Renvoie differentiationCriteria.
     */
    public DifferentiationCriteria getDifferentiationCriteria() {
        return differentiationCriteria;
    }

    /**
     * @param differentiationCriteria differentiationCriteria à définir.
     */
    public void setDifferentiationCriteria(
            DifferentiationCriteria differentiationCriteria) {
        if(differentiationCriteria != null)
            this.differentiationCriteria = differentiationCriteria;
    }
	
	/**
	 * Affecte le club du concurrent
	 * 
	 * @param club
	 */
	public void setClub(String club) {
		this.club = club;
	}
	
	/**
	 * Donne le club du concurrent
	 * 
	 * @return String
	 */
	public String getClub() {
		return this.club;
	}
	
	/**
	 * Affecte le numero d'agrement du club du concurrent
	 * 
	 * @param agrement
	 */
	public void setAgrement(String agrement) {
		this.agrement = agrement;
	}
	
	/**
	 * Donne le numero d'agrement du club du concurrent
	 * 
	 * @return String
	 */
	public String getAgrement() {
		return this.agrement;
	}
	
	/**
     * Retourne le numero de départ de l'archer
     * 
     * @return Returns the depart.
     */
    public int getDepart() {
        return depart;
    }

    /**
     * Définit le n° de départ de l'archer
     * 
     * @param depart The depart to set.
     */
    public void setDepart(int depart) {
        this.depart = depart;
    }

    /**
	 * Donne le numero de cible du concurrent
	 * 
	 * @return int
	 */
	public int getCible() {
		return this.cible;
	}
	
	/**
	 * Affecte le numero de cible du concurrent
	 * 
	 * @param cible
	 */
	public void setCible(int cible) {
		this.cible = cible;
	}
	
	/**
	 * Donne la position sur cible du concurrent
	 * 
	 * @return int
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Affecte la position sur cible du concurrent
	 * 
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Donne l'etat d'inscription du concurennt, réservé/payée
	 * 
	 * @return int
	 */
	public int getInscription() {
		return this.inscription;
	}
	
	/**
	 * Affecte l'etat d'inscription du concurennt, réservé/payée
	 * 
	 * @param inscription
	 */
	public void setInscription(int inscription) {
		this.inscription = inscription;
	}
	
	/**
	 * Libelle court du concurrent
	 * 
	 * @return String
	 */
    @Override
	public String toString() {
		if(this.cible == 0)
			return "<html><font color=red>" +
                this.nomConcurrent + " " +
                this.prenomConcurrent + " (" +
                this.club +
				")</font></html>";
		return ((this.cible < 10) ? "0" : "") + this.cible + "" + (char)('A' + this.position) + ": " +
            this.nomConcurrent + " " +
            this.prenomConcurrent + " (" +
            this.club + ")";
	}

    /**
     * Est ce que l'archer à un certificat médical?
     * 
     * @return Returns the certificat.
     */
    public boolean isCertificat() {
        return certificat;
    }

    /**
     * Definit si l'archer à un certificat médical
     * 
     * @param certificat The certificat to set.
     */
    public void setCertificat(boolean certificat) {
        this.certificat = certificat;
    }
}