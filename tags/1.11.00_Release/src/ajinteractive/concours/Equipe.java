package ajinteractive.concours;

import java.util.*;

/**
 * Class represantant une equipe d'archer pour le concours
 * 
 * @author Aurélien Jeoffray
 * @version 2.0
 *
 */

public class Equipe {
    
    public static final long serialVersionUID = 2l;
	
	private ArrayList<Concurrent> equipe = new ArrayList<Concurrent>();
	private String nomEquipe = "";
	private ArrayList<Concurrent> selection = new ArrayList<Concurrent>();
    private int nbRetenu = 3;
    private DifferentiationCriteria differentiationCriteria;
	
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
     * @return Returns the equipe.
     */
    public ArrayList<Concurrent> getEquipe() {
        return this.equipe;
    }
    
    /**
     * @return Retourne les archers retenu pour le comptage des points
     */
    public ArrayList<Concurrent> getSelection() {
        sort();
        return this.selection;
    }

    /**
     * @return Returns the nbRetenu.
     */
    public int getNbRetenu() {
        return this.nbRetenu;
    }
    

    /**
     * @param nbRetenu The nbRetenu to set.
     */
    public void setNbRetenu(int nbRetenu) {
        this.nbRetenu = nbRetenu;
    }
    

    /**
     * @return Renvoie scna.
     */
    public DifferentiationCriteria getDifferentiationCriteria() {
        if(differentiationCriteria == null)
            differentiationCriteria = new DifferentiationCriteria();
        
        return differentiationCriteria;
    }

    /**
     * Définit le critère de distinction représentant l'équipe
     * 
     * @param differentiationCriteria le critere de distinction
     */
    public void setDifferentiationCriteria(DifferentiationCriteria differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
    }

    /**
     * @param equipe The equipe to set.
     */
    public void setEquipe(ArrayList<Concurrent> equipe) {
        this.equipe = equipe;
    }
    

    /**
     * @param selection The selection to set.
     */
    public void setSelection(ArrayList<Concurrent> selection) {
        this.selection = selection;
    }
    
    /**
     * Ajoute un concurrent à l'equipe
     * 
     * @param concurrent
     */
    public void addConcurrent(Concurrent concurrent) {
        if(concurrent != null && !this.equipe.contains(concurrent))
            this.equipe.add(concurrent);
    }
    
    /**
     * retire un concurrent de l'equipe
     * 
     * @param concurrent
     */
    public void removeConcurrent(Concurrent concurrent) {
        if(concurrent != null && this.equipe.contains(concurrent))
            this.equipe.remove(concurrent);
    }
    
    /**
     * renvoie la liste des membres de l'equipe
     * 
     * @return ArrayList<Concurrent>
     */
    public ArrayList<Concurrent> getMembresEquipe() {
        return this.equipe;
    }
    
    public boolean contains(Concurrent concurrent) {
        return equipe.contains(concurrent);
    }
    
    /**
     * renvoie le nom de l'equipe
     * 
     * @return String
     */
    public String getNomEquipe() {
        return this.nomEquipe;
    }
    
    /**
     * defini le nom de l'equipe
     * 
     * @param nomEquipe
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
        sort();
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
        sort();
        int scoreTotal = 0;
        for(int i = 0; i < selection.size(); i++) {
            scoreTotal += this.selection.get(i).getTotalScore();
        }
        return scoreTotal;
    }

    /**
     * tri les differents scores obtenu par les membre de l'equipe et
     * selectionne les meilleurs en fct du nombre qui doivent etre
     * retenu
     *
     */
    private void sort() {
		//effectue le classement des archers de la compagnie
		for(int i = 0; i<this.equipe.size()-1;i++) {
			for(int j = i+1; j <this.equipe.size();j++) {
				int scorei = this.equipe.get(i).getTotalScore();
				int scorej = this.equipe.get(j).getTotalScore();
				if(scorej>scorei) {
					Concurrent tempConcurrent = this.equipe.get(i);
                    this.equipe.set(i, this.equipe.get(j));
                    this.equipe.set(j, tempConcurrent);
				}	
			}	
		}
        this.selection.clear();
		//selectionne les 3 meilleurs score
		if(this.equipe.size() >= nbRetenu) {
			for(int i = 0; i < nbRetenu; i++)
                this.selection.add(this.equipe.get(i));
		}
	}
    
    @Override
    public String toString() {
    	return nomEquipe;
    }
}