/**
 * Paremtrage commun à Configuration et Parametre
 */

package ajinteractive.concours;


public class DefaultParameters {
    private String nomClub          = "";
    private String numAgrement      = "";
    private String intituleConcours = "";
    
    private int nbCible             = 10;
    private int nbTireur            = 4;
    private int nbDepart            = 1;
    
    public DefaultParameters() {
        
    }

    /**
     * @return Renvoie intituleConcours.
     */
    public String getIntituleConcours() {
        return intituleConcours;
    }

    /**
     * @param intituleConcours intituleConcours à définir.
     */
    public void setIntituleConcours(String intituleConcours) {
        this.intituleConcours = intituleConcours;
    }

    /**
     * @return Renvoie nbCible.
     */
    public int getNbCible() {
        return nbCible;
    }

    /**
     * @param nbCible nbCible à définir.
     */
    public void setNbCible(int nbCible) {
        this.nbCible = nbCible;
    }

    /**
     * @return Renvoie nbDepart.
     */
    public int getNbDepart() {
        return nbDepart;
    }

    /**
     * @param nbDepart nbDepart à définir.
     */
    public void setNbDepart(int nbDepart) {
        this.nbDepart = nbDepart;
    }

    /**
     * @return Renvoie nbTireur.
     */
    public int getNbTireur() {
        return nbTireur;
    }

    /**
     * @param nbTireur nbTireur à définir.
     */
    public void setNbTireur(int nbTireur) {
        this.nbTireur = nbTireur;
    }

    /**
     * @return Renvoie nomClub.
     */
    public String getNomClub() {
        return nomClub;
    }

    /**
     * @param nomClub nomClub à définir.
     */
    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    /**
     * @return Renvoie numAgrement.
     */
    public String getNumAgrement() {
        return numAgrement;
    }

    /**
     * @param numAgrement numAgrement à définir.
     */
    public void setNumAgrement(String numAgrement) {
        this.numAgrement = numAgrement;
    }
}
