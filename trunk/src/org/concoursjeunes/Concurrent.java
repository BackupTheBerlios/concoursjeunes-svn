package org.concoursjeunes;

import java.util.ArrayList;

/**
 * Objet de Base de stockage des Information sur un concurrent Nom Categorie Licence Cible Nombre de volée points
 * @author  Aurelien Jeoffray
 * @version  3.0
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
	private String nomConcurrent        = ""; //$NON-NLS-1$
	private String prenomConcurrent     = ""; //$NON-NLS-1$
	private String numLicenceConcurrent = ""; //$NON-NLS-1$
	private CriteriaSet criteriaSet;
	private Entite club                 = new Entite();
    private boolean certificat          = false;
    
    private int depart                  = 0;
	private int cible                   = 0;	//position sur le concours
	private int position                = 0;
	
	//public int[] points					= new int[ConcoursJeunes.configuration.getNbSerie()];
	public ArrayList<Integer> points	= new ArrayList<Integer>();
	private int neuf                    = 0;
	private int dix                     = 0;
	private int manque                  = 0;
	
	private int inscription             = UNINIT;
	 
	/**
	 * Constructeur vide obligatoire pour java beans
	 * 
	 */
	public Concurrent() { }
	
	/**
	 * Affectation des scores pour le concurrent
	 * 
	 * @param points - la grille des scores du concurrent
	 * @return true si le score est valide, false sinon
	 */
	public void setScore(ArrayList<Integer> points) {
		this.points = points;
	}
	
	public void setScore(int serie, int points) {
		this.points.set(serie, points);
	}
	
	/**
	 * Donne la grille des scores du concurrent
	 * 
	 * @return la grille des scores
	 */
	public ArrayList<Integer> getScore() {
		return points;
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
    		for(int point : points) {
    			total += point;
    		}
        }
		return total;
	}
	/**
	 * Affecte le nombre de dix total du concurrent
	 * @param  dix
	 * @uml.property  name="neuf"
	 */
	public void setNeuf(int dix) {
		this.neuf = dix;
	}
	
	/**
	 * Donne le nombre de dix du concurrent
	 * @return  int
	 * @uml.property  name="neuf"
	 */
	public int getNeuf() {
		return this.neuf;
	}
	
	/**
	 * Affecte le nombre de 10+ total du concurrent
	 * @param  dixPlus
	 * @uml.property  name="dix"
	 */
	public void setDix(int dixPlus) {
		this.dix = dixPlus;
	}
	
	/**
	 * Donne le nombre de 10+ du concurrent
	 * @return  int
	 * @uml.property  name="dix"
	 */
	public int getDix() {
		return this.dix;
	}
	
	/**
	 * Affecte le nombre de fleche manquee total du concurrent
	 * @param  manque
	 * @uml.property  name="manque"
	 */
	public void setManque(int manque) {
		this.manque = manque;
	}
	
	/**
	 * Donne le nombre de fleche manquee du concurrent
	 * @return  int
	 * @uml.property  name="manque"
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
		return this.nomConcurrent + " " + this.prenomConcurrent; //$NON-NLS-1$
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
	 * @return  Renvoie criteriaSet.
	 * @uml.property  name="criteriaSet"
	 */
    public CriteriaSet getCriteriaSet() {
        return criteriaSet;
    }

    /**
	 * @param criteriaSet  criteriaSet à définir.
	 * @uml.property  name="criteriaSet"
	 */
    public void setCriteriaSet(
            CriteriaSet differentiationCriteria) {
        if(differentiationCriteria != null)
            this.criteriaSet = differentiationCriteria;
    }
	
	/**
	 * Affecte le club du concurrent
	 * @param  club
	 * @uml.property  name="club"
	 */
	public void setClub(Entite club) {
		this.club = club;
	}
	
	/**
	 * Donne le club du concurrent
	 * @return  Entite
	 * @uml.property  name="club"
	 */
	public Entite getClub() {
		return this.club;
	}
	
	/**
	 * Retourne le numero de départ de l'archer
	 * @return  Returns the depart.
	 * @uml.property  name="depart"
	 */
    public int getDepart() {
        return depart;
    }

    /**
	 * Définit le n° de départ de l'archer
	 * @param depart  The depart to set.
	 * @uml.property  name="depart"
	 */
    public void setDepart(int depart) {
        this.depart = depart;
    }

    /**
	 * Donne le numero de cible du concurrent
	 * @return  int
	 * @uml.property  name="cible"
	 */
	public int getCible() {
		return this.cible;
	}
	
	/**
	 * Affecte le numero de cible du concurrent
	 * @param  cible
	 * @uml.property  name="cible"
	 */
	public void setCible(int cible) {
		this.cible = cible;
	}
	
	/**
	 * Donne la position sur cible du concurrent
	 * @return  int
	 * @uml.property  name="position"
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Affecte la position sur cible du concurrent
	 * @param  position
	 * @uml.property  name="position"
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Donne l'etat d'inscription du concurennt, réservé/payée
	 * @return  int
	 * @uml.property  name="inscription"
	 */
	public int getInscription() {
		return this.inscription;
	}
	
	/**
	 * Affecte l'etat d'inscription du concurennt, réservé/payée
	 * @param  inscription
	 * @uml.property  name="inscription"
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
			return "<html><font color=red>" + //$NON-NLS-1$
                this.nomConcurrent + " " + //$NON-NLS-1$
                this.prenomConcurrent + " (" + //$NON-NLS-1$
                this.club +
				")</font></html>"; //$NON-NLS-1$
		return ((this.cible < 10) ? "0" : "") + this.cible + "" + (char)('A' + this.position) + ": " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            this.nomConcurrent + " " + //$NON-NLS-1$
            this.prenomConcurrent + " (" + //$NON-NLS-1$
            this.club + ")"; //$NON-NLS-1$
	}

    /**
	 * Est ce que l'archer à un certificat médical?
	 * @return  Returns the certificat.
	 * @uml.property  name="certificat"
	 */
    public boolean isCertificat() {
        return certificat;
    }

    /**
	 * Definit si l'archer à un certificat médical
	 * @param certificat  The certificat to set.
	 * @uml.property  name="certificat"
	 */
    public void setCertificat(boolean certificat) {
        this.certificat = certificat;
    }
    
    public boolean haveHomonyme(Reglement reglement) {
		ArrayList<Concurrent> homonyme = ConcoursJeunes.databaseManager.getAllArchers("NomArcher='" + nomConcurrent + "'" +
				" and PrenomArcher='" + prenomConcurrent + "'", null, reglement);
		return (homonyme.size() > 1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((nomConcurrent == null) ? 0 : nomConcurrent.hashCode());
		result = PRIME * result + ((numLicenceConcurrent == null) ? 0 : numLicenceConcurrent.hashCode());
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
		final Concurrent other = (Concurrent) obj;
		if (nomConcurrent == null) {
			if (other.nomConcurrent != null)
				return false;
		} else if (!nomConcurrent.equals(other.nomConcurrent))
			return false;
		if (numLicenceConcurrent == null) {
			if (other.numLicenceConcurrent != null)
				return false;
		} else if (!numLicenceConcurrent.equals(other.numLicenceConcurrent))
			return false;
		return true;
	}
}