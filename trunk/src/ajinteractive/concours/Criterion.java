/**
 * Caractéristique d'un critère de distinction
 */

package ajinteractive.concours;

public class Criterion {
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = -1;
    
    private int sortOrder = 1;
    private String code = "";
    private String libelle = "";
    private boolean classement = false;
    private boolean placement = false;
    
    public Criterion() {
        
    }
    
    /**
     * Initialise un nouveau critère avec le code "code"
     * 
     * @param code - le code du citère
     */
    public Criterion(String code) {
        this.code = code;
    }

    /**
     * Renvoi le code du critère
     * 
     * @return Renvoie code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit le code du critère
     * 
     * @param code code à définir.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Renvoie le libellé du critère
     * 
     * @return Renvoie libelle.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé du critère
     * 
     * @param libelle libelle à définir.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Renvoie l'ordre de tri du critère
     * 
     * @return Renvoie sortOrder.
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Définit l'ordre de tri du critère
     * 
     * @param sortOrder l'ordre de tri
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Test si deux critères sont équivalent
     * 
     * @param criterion
     * @return boolean - le résulats de la comparaison de critères
     */
    public boolean equals(Criterion criterion) {
        return code.equals(criterion.getCode());
    }
    
    /**
     * Test si deux critères sont équivalent en se basant sur la comparaison d'objet
     */
    @Override
    public boolean equals(Object criterion) {
        if(criterion instanceof Criterion)
            return equals((Criterion)criterion);
        return false;
    }
    
    /**
     * donne le hash de l'objet en se basoant sur celui de son code
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    /**
     * renvoie le libelle du critère
     */
    @Override
    public String toString() {
        return libelle;
    }

    /**
     * Est ce que c'est un critère de classement?
     * 
     * @return Renvoie classement.
     */
    public boolean isClassement() {
        return classement;
    }

    /**
     * Définit si c'est un critère de classement
     * 
     * @param classement classement à définir.
     */
    public void setClassement(boolean classement) {
        this.classement = classement;
    }

    /**
     * Est ce que c'est un critère de placement?
     * 
     * @return Renvoie placement.
     */
    public boolean isPlacement() {
        return placement;
    }

    /**
     * Definit si c'est un critère de placement
     * 
     * @param placement placement à définir.
     */
    public void setPlacement(boolean placement) {
        this.placement = placement;
    }
}
