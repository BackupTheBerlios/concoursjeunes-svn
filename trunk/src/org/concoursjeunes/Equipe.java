/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.concoursjeunes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represantant une Equipe d'archer pour le concours
 * @author  Aurélien Jeoffray
 * @version  2.0
 */

public class Equipe implements Cloneable, Comparable<Equipe> {

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
	 */
    public ArrayList<Concurrent> getMembresEquipe() {
        return this.membresEquipe;
    }
    
    /**
     * Retourne les concurrents ayant été selectionné pour pouvoir comptabiliser le score
     * 
	 * @return  Retourne les archers retenu pour le comptage des points
	 */
    public List<Concurrent> getSelection() {
    	List<Concurrent> selection = new ArrayList<Concurrent>();
    	
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
	 */
    public int getNbRetenu() {
        return this.nbRetenu;
    }
    

    /**
     * Definit le nombre d'archer retenu dans la comptabilite des scores
     * 
	 * @param nbRetenu  The nbRetenu to set.
	 */
    public void setNbRetenu(int nbRetenu) {
        this.nbRetenu = nbRetenu;
    }
    

    /**
     * Renvoie Le critère de distinction de l'équipe.
     *  
	 * @return  DifferentiationCriteria - Le critère de distinction de l'équipe.
	 */
    public CriteriaSet getDifferentiationCriteria() {
        if(differentiationCriteria == null)
            differentiationCriteria = new CriteriaSet();
        
        return differentiationCriteria;
    }

    /**
     * Définit le critère de distinction de l'équipe.
     * 
	 * @param differentiationCriteria  - Le critère de distinction de l'équipe.
	 */
    public void setDifferentiationCriteria(CriteriaSet differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
    }

    /**
     * Definit la liste des membres de l'equipe
     * 
	 * @param equipe la liste des membres de l'equipe
	 */
    public void setMembresEquipe(ArrayList<Concurrent> equipe) {
        this.membresEquipe = equipe;
    }
    
    /**
     * Ajoute un concurrent à l'Equipe
     * 
     * @param concurrent
     */
    public void addConcurrent(Concurrent concurrent) {
        if(concurrent != null && !this.membresEquipe.contains(concurrent))
            this.membresEquipe.add(concurrent);
    }
    
    /**
     * retire un concurrent de l'Equipe
     * 
     * @param concurrent
     */
    public void removeConcurrent(Concurrent concurrent) {
        if(concurrent != null && this.membresEquipe.contains(concurrent))
            this.membresEquipe.remove(concurrent);
    }
    
    /**
     * verifie si l'Equipe contient ou non le concurrent donné en parametre
     * 
     * @param concurrent - le concurrent à vérifier
     * @return true si present, false sinon
     */
    public boolean contains(Concurrent concurrent) {
        return membresEquipe.contains(concurrent);
    }
    
    /**
	 * renvoie le nom de l'Equipe
	 * 
	 * @return  String
	 */
    public String getNomEquipe() {
        return this.nomEquipe;
    }
    
    /**
	 * defini le nom de l'Equipe
	 * 
	 * @param  nomEquipe
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
    	List<Concurrent> selection = getSelection();
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
    	List<Concurrent> selection = getSelection();
        int scoreTotal = 0;
        for(int i = 0; i < selection.size(); i++) {
            scoreTotal += selection.get(i).getTotalScore();
        }
        return scoreTotal;
    }
    
    @Override
    public int compareTo(Equipe equipe) {
    	int totalScore = getTotalScore();
    	int otherTotalScore = equipe.getTotalScore();
    	
    	if(totalScore > otherTotalScore)
    		return 1;
    	else if(totalScore < otherTotalScore)
    		return -1;
    	return 0;
    }
    
    @Override
    public String toString() {
    	return nomEquipe;
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public Equipe clone() {
    	Equipe clone = new Equipe();
    	
    	clone.setMembresEquipe((ArrayList<Concurrent>)membresEquipe.clone());
    	clone.setNomEquipe(new String(nomEquipe));
    	clone.setNbRetenu(nbRetenu);
    	clone.setDifferentiationCriteria(differentiationCriteria);
    	
    	return clone;
    }
}