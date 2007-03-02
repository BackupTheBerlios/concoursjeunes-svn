package org.concoursjeunes;

import java.util.*;

/**
 * Class represantant une membresEquipe d'archer pour le concours
 * @author  Aurélien Jeoffray
 * @version  2.0
 */

public class Equipe {

	private ArrayList<Concurrent> membresEquipe = new ArrayList<Concurrent>();
	private String nomEquipe = ""; //$NON-NLS-1$
	
    private int nbRetenu = 3;
    private CriteriaSet differentiationCriteria;
	
    /**
     * Constructeur vide pour la serialisation XML
     */
    public Equipe() { 
    }
	
    /**
     * crer une équipe portant le nom nomEquipe
     * 
     * @param nomEquipe
     */
	public Equipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}
    
	/**
	 * Retourne l'ensemble des concurrents composant l'equipes
	 * 
	 * @return les concurrents composant l'équipe
	 * @uml.property  name="membresEquipe"
	 */
    public ArrayList<Concurrent> getMembresEquipe() {
        return this.membresEquipe;
    }
    
    /**
     * Retourne les concurrents ayant été selectionné pour pouvoir comptabiliser le score
     * 
	 * @return  Retourne les archers retenu pour le comptage des points
	 * @uml.property  name="selection"
	 */
    public ArrayList<Concurrent> getSelection() {
    	ArrayList<Concurrent> selection = new ArrayList<Concurrent>();
    	
    	//effectue le classement des archers de la compagnie
		for(int i = 0; i<this.membresEquipe.size()-1;i++) {
			for(int j = i+1; j <this.membresEquipe.size();j++) {
				int scorei = this.membresEquipe.get(i).getTotalScore();
				int scorej = this.membresEquipe.get(j).getTotalScore();
				if(scorej>scorei) {
					Concurrent tempConcurrent = this.membresEquipe.get(i);
                    this.membresEquipe.set(i, this.membresEquipe.get(j));
                    this.membresEquipe.set(j, tempConcurrent);
				}	
			}	
		}

		//selectionne les 3 meilleurs score
		if(this.membresEquipe.size() >= nbRetenu) {
			for(int i = 0; i < nbRetenu; i++)
                selection.add(this.membresEquipe.get(i));
		}
        return selection;
    }

    /**
     * Retourne le nombre de concurrent retenu pour comptabiliser le score
     * 
	 * @return  Returns the nbRetenu.
	 * @uml.property  name="nbRetenu"
	 */
    public int getNbRetenu() {
        return this.nbRetenu;
    }
    

    /**
     * Definit le nombre d'archer retenu dans la comptabilite des scores
     * 
	 * @param nbRetenu  The nbRetenu to set.
	 * @uml.property  name="nbRetenu"
	 */
    public void setNbRetenu(int nbRetenu) {
        this.nbRetenu = nbRetenu;
    }
    

    /**
	 * @return  DifferentiationCriteria - Renvoie Le critère de distinction de l'équipe.
	 * @uml.property  name="differentiationCriteria"
	 */
    public CriteriaSet getDifferentiationCriteria() {
        if(differentiationCriteria == null)
            differentiationCriteria = new CriteriaSet();
        
        return differentiationCriteria;
    }

    /**
	 * @param differentiationCriteria  - Le critère de distinction de l'équipe.
	 * @uml.property  name="differentiationCriteria"
	 */
    public void setDifferentiationCriteria(CriteriaSet differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
    }

    /**
	 * @param membresEquipe  The membresEquipe to set.
	 * @uml.property  name="membresEquipe"
	 */
    public void setMembresEquipe(ArrayList<Concurrent> equipe) {
        this.membresEquipe = equipe;
    }
    
    /**
     * Ajoute un concurrent à l'membresEquipe
     * 
     * @param concurrent
     */
    public void addConcurrent(Concurrent concurrent) {
        if(concurrent != null && !this.membresEquipe.contains(concurrent))
            this.membresEquipe.add(concurrent);
    }
    
    /**
     * retire un concurrent de l'membresEquipe
     * 
     * @param concurrent
     */
    public void removeConcurrent(Concurrent concurrent) {
        if(concurrent != null && this.membresEquipe.contains(concurrent))
            this.membresEquipe.remove(concurrent);
    }
    
    /**
     * verifie si l'membresEquipe contient ou non le concurrent donné en parametre
     * 
     * @param concurrent - le concurrent à vérifier
     * @return true si present, false sinon
     */
    public boolean contains(Concurrent concurrent) {
        return membresEquipe.contains(concurrent);
    }
    
    /**
	 * renvoie le nom de l'membresEquipe
	 * 
	 * @return  String
	 * @uml.property  name="nomEquipe"
	 */
    public String getNomEquipe() {
        return this.nomEquipe;
    }
    
    /**
	 * defini le nom de l'membresEquipe
	 * @param  nomEquipe
	 * @uml.property  name="nomEquipe"
	 */
    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }
    
    /**
     * renvoie les score individuel de chacun des membre séléctionné de l'equipes
     * 
     * @return int[]
     */
    public int[] getScore() {
    	ArrayList<Concurrent> selection = getSelection();
        int[] scoreSelection = new int[selection.size()];
        for(int i = 0; i < scoreSelection.length; i++)
            scoreSelection[i] = selection.get(i).getTotalScore();
        return scoreSelection;
    }
    
    /**
     * renvoie le score total obtenu par la selection
     * 
     * @return int
     */
    public int getTotalScore() {
    	ArrayList<Concurrent> selection = getSelection();
        int scoreTotal = 0;
        for(int i = 0; i < selection.size(); i++) {
            scoreTotal += selection.get(i).getTotalScore();
        }
        return scoreTotal;
    }
    
    @Override
    public String toString() {
    	return nomEquipe;
    }
}