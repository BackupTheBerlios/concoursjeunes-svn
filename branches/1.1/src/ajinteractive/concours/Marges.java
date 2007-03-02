/**
 * Marges d'impression d'une page
 */
package ajinteractive.concours;

/**
 * @author xp
 *
 */
public class Marges {

    private double haut = 0;
    private double bas = 0;
    private double gauche = 0;
    private double droite = 0;
    /**
     * 
     */
    public Marges() {
        
    }
    
    /**
     * Cré un objet Marges en définissant ses marges
     * 
     * @param haut
     * @param bas
     * @param gauche
     * @param droite
     */
    public Marges(double haut, double bas, double gauche, double droite) {
        this.haut = haut;
        this.bas = bas;
        this.gauche = gauche;
        this.droite = droite;
    }
    /**
     * @return Renvoie bas.
     */
    public double getBas() {
        return bas;
    }
    /**
     * @param bas bas à définir.
     */
    public void setBas(double bas) {
        this.bas = bas;
    }
    /**
     * @return Renvoie droite.
     */
    public double getDroite() {
        return droite;
    }
    /**
     * @param droite droite à définir.
     */
    public void setDroite(double droite) {
        this.droite = droite;
    }
    /**
     * @return Renvoie gauche.
     */
    public double getGauche() {
        return gauche;
    }
    /**
     * @param gauche gauche à définir.
     */
    public void setGauche(double gauche) {
        this.gauche = gauche;
    }
    /**
     * @return Renvoie haut.
     */
    public double getHaut() {
        return haut;
    }
    /**
     * @param haut haut à définir.
     */
    public void setHaut(double haut) {
        this.haut = haut;
    }
}
