package ajinteractive.concours;

import java.util.Arrays;
/**
 * parametre de distances et blason pour une cible et un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 *
 */
public class DistancesEtBlason {
	private int[] distances = new int[] {18, 18};
	private int blason = 80;
	
    /**
     * cree un d&b avec les options par défaut (pour sérialisation XML)
     *
     */
    public DistancesEtBlason() {
    }
    
    /**
     * Cree un distance et blason avec les bon param
     * 
     * @param distances
     * @param blason
     */
	public DistancesEtBlason(int[] distances, int blason) {
		this.distances = distances;
		this.blason = blason;
	}
	
    /**
     * Test si 2 objet d&b sont identique
     * 
     * @param db
     * @return boolean
     */
	public boolean equals(DistancesEtBlason db) {
		return (Arrays.equals(this.distances, db.distances) && this.blason == db.blason);
	}

    /**
     * @return Returns the blason.
     */
    public int getBlason() {
        return this.blason;
    }
    

    /**
     * @return Returns the distance.
     */
    public int[] getDistance() {
        return this.distances;
    }
    

    /**
     * @param blason The blason to set.
     */
    public void setBlason(int blason) {
        this.blason = blason;
    }
    

    /**
     * @param distances The distance to set.
     */
    public void setDistance(int[] distances) {
        this.distances = distances;
    }
}