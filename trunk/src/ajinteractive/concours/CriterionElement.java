/**
 * Element de critère
 */
package ajinteractive.concours;

/**
 * @author Aurelien
 *
 */
public class CriterionElement {
    private String code = "";
    private String libelle = "";
    private boolean active = true;
    
    public CriterionElement() {
        
    }

    /**
     * Renvoie le code de l'élément
     * 
     * @return Renvoie code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit le code de l'élément
     * 
     * @param code code à définir.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Est-ce que l'élément est utilisé?
     * 
     * @return Renvoie isactive.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Détermine si l'élément doit être utilisé
     * 
     * @param active fixe le statut d'activite de l'element.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Renvoi le libellé de l'élément
     * 
     * @return Renvoie libelle.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libelle de l'élément
     * 
     * @param libelle libelle à définir.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * retourne le libelle de l'élément
     */
    @Override
    public String toString() {
        return libelle;
    }
}
