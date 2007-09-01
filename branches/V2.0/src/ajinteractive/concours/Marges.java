/**
 * Marges d'impression d'une page
 */
package ajinteractive.concours;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Marges extends org.concoursjeunes.Marges {

    /**
     * 
     */
    public Marges() {
        super();
    }

    /**
     * @return Renvoie bas.
     */
    public double getBas() {
        return getBottom();
    }
    /**
     * @param bas bas à définir.
     */
    public void setBas(double bas) {
        setBottom(bas);
    }
    /**
     * @return Renvoie droite.
     */
    public double getDroite() {
        return getRight();
    }
    /**
     * @param droite droite à définir.
     */
    public void setDroite(double droite) {
        setRight(droite);
    }
    /**
     * @return Renvoie gauche.
     */
    public double getGauche() {
        return getLeft();
    }
    /**
     * @param gauche gauche à définir.
     */
    public void setGauche(double gauche) {
        setLeft(gauche);
    }
    /**
     * @return Renvoie haut.
     */
    public double getHaut() {
        return getTop();
    }
    /**
     * @param haut haut à définir.
     */
    public void setHaut(double haut) {
        setTop(haut);
    }
}
