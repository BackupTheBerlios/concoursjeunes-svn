/**
 * Paremtrage commun à Configuration et Parametre
 */

package ajinteractive.concours;

import java.util.ArrayList;
import java.util.Hashtable;

public class DefaultParameters {
    private String nomClub          = "";
    private String numAgrement      = "";
    private String intituleConcours = "";
    
    private int nbCible             = 10;
    private int nbTireur            = 4;
    private int nbSerie             = 2;
    private int nbVoleeParSerie     = 6;
    private int nbFlecheParVolee    = 3;
    private int nbDepart            = 1;
    private int nbMembresEquipe     = 4;
    private int nbMembresRetenu     = 3;

    private ArrayList<Criterion> listCriteria = new ArrayList<Criterion>();
    private Hashtable<Criterion, ArrayList<CriterionElement>> criteriaPopulation = new Hashtable<Criterion, ArrayList<CriterionElement>>();
    private Hashtable<DifferentiationCriteria, DistancesEtBlason> correspondanceDifferentiationCriteria_DB = new Hashtable<DifferentiationCriteria, DistancesEtBlason>();
    
    public DefaultParameters() {
        
    }
    
    /**
     * Renvoi la politique de placement
     * 
     * @return Hashtable<String, Boolean> - renvoi les filtres en place
     */
    public Hashtable<String, Boolean> getPlacementFilter() {
        Hashtable<String, Boolean> filterCriteria = new Hashtable<String, Boolean>();
        for(Criterion criterion : listCriteria) {
            filterCriteria.put(criterion.getCode(), criterion.isPlacement());
        }
        
        return filterCriteria;
    }
    
    /**
     * retourne la correspondance critère <-> Position de cible en place
     * 
     * @param differentiationCriteria - les critères à évaluer
     * @return Returns the correspondanceSCNA_DB.
     */
    public DistancesEtBlason getCorrespondanceDifferentiationCriteria_DB(DifferentiationCriteria differentiationCriteria) {
        return correspondanceDifferentiationCriteria_DB.get(differentiationCriteria);
    }

    /**
     * @return Renvoie correspondanceDifferentiationCriteria_DB.
     */
    public Hashtable<DifferentiationCriteria, DistancesEtBlason> getCorrespondanceDifferentiationCriteria_DB() {
        return correspondanceDifferentiationCriteria_DB;
    }

    /**
     * @param correspondanceDifferentiationCriteria_DB correspondanceDifferentiationCriteria_DB à définir.
     */
    public void setCorrespondanceDifferentiationCriteria_DB(
            Hashtable<DifferentiationCriteria, DistancesEtBlason> correspondanceDifferentiationCriteria_DB) {
        this.correspondanceDifferentiationCriteria_DB = correspondanceDifferentiationCriteria_DB;
    }
    
    /**
     * Ajout d'une correspondance entre critere de distinction et un couple distance/blason
     * 
     * @param differentiationCriteria - le critere de distinction
     * @param db - le couple distance/blason
     */
    public void putCorrespondanceDifferentiationCriteria_DB(DifferentiationCriteria differentiationCriteria, DistancesEtBlason db) {
        this.correspondanceDifferentiationCriteria_DB.put(differentiationCriteria, db);
    }

    /**
     * @return Renvoie criteriaPopulation.
     */
    public Hashtable<Criterion, ArrayList<CriterionElement>> getCriteriaPopulation() {
        return criteriaPopulation;
    }

    /**
     * @param criteriaPopulation criteriaPopulation à définir.
     */
    public void setCriteriaPopulation(
            Hashtable<Criterion, ArrayList<CriterionElement>> criteriaPopulation) {
        this.criteriaPopulation = criteriaPopulation;
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
     * @return Renvoie listCriteria.
     */
    public ArrayList<Criterion> getListCriteria() {
        return listCriteria;
    }

    /**
     * @param listCriteria listCriteria à définir.
     */
    public void setListCriteria(ArrayList<Criterion> listCriteria) {
        this.listCriteria = listCriteria;
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
     * @return Renvoie nbFlecheParVolee.
     */
    public int getNbFlecheParVolee() {
        return nbFlecheParVolee;
    }

    /**
     * @param nbFlecheParVolee nbFlecheParVolee à définir.
     */
    public void setNbFlecheParVolee(int nbFlecheParVolee) {
        this.nbFlecheParVolee = nbFlecheParVolee;
    }

    /**
     * @return Renvoie nbMembresEquipe.
     */
    public int getNbMembresEquipe() {
        return nbMembresEquipe;
    }

    /**
     * @param nbMembresEquipe nbMembresEquipe à définir.
     */
    public void setNbMembresEquipe(int nbMembresEquipe) {
        this.nbMembresEquipe = nbMembresEquipe;
    }

    /**
     * @return Renvoie nbMembresRetenu.
     */
    public int getNbMembresRetenu() {
        return nbMembresRetenu;
    }

    /**
     * @param nbMembresRetenu nbMembresRetenu à définir.
     */
    public void setNbMembresRetenu(int nbMembresRetenu) {
        this.nbMembresRetenu = nbMembresRetenu;
    }

    /**
     * @return Renvoie nbSerie.
     */
    public int getNbSerie() {
        return nbSerie;
    }

    /**
     * @param nbSerie nbSerie à définir.
     */
    public void setNbSerie(int nbSerie) {
        this.nbSerie = nbSerie;
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
     * @return Renvoie nbVoleeParSerie.
     */
    public int getNbVoleeParSerie() {
        return nbVoleeParSerie;
    }

    /**
     * @param nbVoleeParSerie nbVoleeParSerie à définir.
     */
    public void setNbVoleeParSerie(int nbVoleeParSerie) {
        this.nbVoleeParSerie = nbVoleeParSerie;
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
