/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
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

import org.ajdeveloppement.commons.ArraysUtils;

/**
 * Represente la liste des équipes constitué sur un concours
 * 
 * @author  Aurelien Jeoffray
 * @version 1.1
 */
public class EquipeList implements Cloneable {
   
    private List<Equipe> equipeList  = new ArrayList<Equipe>();
    private int nbMembresRetenu = 3;
    
    private boolean limitedByClub = true;
    
    public EquipeList() {
    	
    }
    public EquipeList(int nbMembresRetenu) { 
    	this.nbMembresRetenu = nbMembresRetenu;
    }

	/**
	 * Retourne le nombre de membre retenue dans le comptage des points par équipes
	 * 
	 * @return le nombre de membre retenue pour le comptage des points
	 */
	public int getNbMembresRetenu() {
		return nbMembresRetenu;
	}
	
	/**
	 * Définit le nombre de membre retenue dans le comptage des points par équipes
	 * 
	 * @param nbMembresRetenu le nombre de membre retenue pour le comptage des points
	 */
	public void setNbMembresRetenu(int nbMembresRetenu) {
		this.nbMembresRetenu = nbMembresRetenu;
	}
	/**
     * Ajoute une équipe à la liste
     * 
     * @param newEquipe l'équipe à ajouter
     */
    public void add(Equipe newEquipe) {
        equipeList.add(newEquipe);
    }
    
    /**
     * Donne l'équipe à l'index donné
     * 
     * @param index - l'index de l'équipe
     * @return Equipe l'équipe à l'index donné
     */
    public Equipe get(int index) {
        return equipeList.get(index);
    }
    
    /**
     * Détermine si l'équipe nommé transmis en paramêtre existe déjà sur le concours
     * 
     * @param teamName le nom de l'équipe à tester
     * @return true si l'équipe existe, false sinon 
     */
    public boolean contains(String teamName) {
    	for(Equipe equipe : equipeList) {
    		if(equipe.getNomEquipe().equals(teamName))
    			return true;
    	}
    	return false;
    }
    
    /**
     * Retourne l'équipe contenant le concourrent donné ou null si inexistant
     * 
     * @param concurrent - le concurrent à rechercher
     * @return l'Equipe à renvoyer
     */
    public Equipe containsConcurrent(Concurrent concurrent) {
        Equipe goodTeam = null;
        for(Equipe equipe : equipeList) {
            if(equipe.contains(concurrent)) {
                goodTeam = equipe;
                break;
            }
        }
        return goodTeam;
    }
    
    /**
     * Retire un concurrent du classement par équipe
     * 
     * @param concurrent - le concurrent à retirer
     * @return boolean true si retiré et false si non trouvé
     */
    public boolean removeConcurrent(Concurrent concurrent) {
        boolean remove = false;
        for(Equipe equipe : equipeList) {
            if(equipe.contains(concurrent)) {
                equipe.removeConcurrent(concurrent);
                
                if(equipe.getMembresEquipe().size() < nbMembresRetenu)
                    remove(equipe);
                remove = true;
                break;
            }
        }
        return remove;
    }
    
    /**
     * retire une équipe complete
     * 
     * @param equipe
     */
    public void remove(Equipe equipe) {
        equipeList.remove(equipe);
    }
    
    /**
     * Supprime les équipes crée et invalide sur le concours
     */
    public void removeInvalidTeam() {
    	ArrayList<Equipe> deleteList = new ArrayList<Equipe>();
    	for(Equipe equipe : equipeList) {
            if(equipe.getMembresEquipe().size() < nbMembresRetenu)
            	deleteList.add(equipe);
        }
    	for(Equipe equipe : deleteList) {
    		remove(equipe);
    	}
    }
    
    /**
     * retire toutes les équipes
     * @deprecated remplacé par {@link EquipeList#clear()}
     */
    @Deprecated
    public void removeAll() {
        equipeList.clear();
    }
    
    /**
	 * retire toutes les équipes
	 */
	public void clear() {
		equipeList.clear();
	}
    
    /**
     * Retourne la liste complete des équipes
     * 
     * @return Equipe[]
     */
    public Equipe[] list() {
        Equipe[] equipes = new Equipe[equipeList.size()];
        equipeList.toArray(equipes);
        
        return equipes;
    }
    
    /**
     * Retourne la liste des équipes correspondant au critère donné
     * 
     * @param scna - le filtre de tri des équipes
     * @return Equipe[]
     */
    public Equipe[] list(CriteriaSet scna) {
        ArrayList<Equipe> sel = new ArrayList<Equipe>();
        
        for(Equipe equipe : equipeList) {
        	//TODO verifier la porté
            if(equipe.getDifferentiationCriteria().equals(scna))
                sel.add(equipe);
        }

        return sel.toArray(new Equipe[sel.size()]);
    }
    
    /**
     * Tri les équipe sur la base de leurs points
     * 
     * @param no_sort_list - La liste des équipes à trier
     * @return Equipe[] - la liste des équipe fournit en parametre mais trié
     */
    public static Equipe[] sort(Equipe[] no_sort_list) {
        if(no_sort_list != null && no_sort_list.length > 0) {
        	//Arrays.sort(no_sort_list);
            for(int i = 0; i < no_sort_list.length - 1; i++) {
                for(int j = i+1; j < no_sort_list.length; j++) {
                    if(no_sort_list[i].getTotalScore() < no_sort_list[j].getTotalScore()) {
                    	ArraysUtils.swap(no_sort_list, i, j);
                    }
                }
            }
        }
        
        return no_sort_list;
    }
    
    /**
     * Liste les jeux de critères utilisé pour départager les équipes
     * 
     * @return les jeux de critères utilisé pour départager les équipes
     */
    public List<CriteriaSet> listCriteriaSet() {
    	List<CriteriaSet> listCriteriaSet = new ArrayList<CriteriaSet>();
    	for(Equipe equipe : equipeList) {
    		 CriteriaSet criteriaSet = equipe.getDifferentiationCriteria();
    		 if(!listCriteriaSet.contains(criteriaSet)) {
    			 listCriteriaSet.add(criteriaSet);
    		 }
    	}
    	 return listCriteriaSet;
    }
    
    /**
     * Donne le nombre d'équipe enregistré
     * 
     * @return int le nombre d'équipe
     */
    public int countEquipes() {
        return equipeList.size();
    }

    /**
	 * Pour la sérialisation, la table des équipes
	 * @return  Renvoie equipeList.
	 */
    public List<Equipe> getEquipeList() {
        return equipeList;
    }

    /**
	 * Pour la sérialisation, la table des équipes
	 * @param equipeList  equipeList à définir.
	 */
    public void setEquipeList(List<Equipe> equipeList) {
        this.equipeList = equipeList;
    }
    
    /**
     * Est ce que les membres de l'equipe doivent appartenir au même club?
     * 
     * @return true si ils doivent appartenir au même club, false sinon
     */
    public boolean isLimitedByClub() {
		return limitedByClub;
	}
    
    /**
     * Est ce que les membres de l'equipe doivent appartenir au même club?
     * 
     * @param limitedByClub true si ils doivent appartenir au même club, false sinon
     */
	public void setLimitedByClub(boolean limitedByClub) {
		this.limitedByClub = limitedByClub;
	}
	
	@Override
    public EquipeList clone() {
    	EquipeList clone = new EquipeList();
    	ArrayList<Equipe> equipeListClone = new ArrayList<Equipe>();
    	for(Equipe equipe : equipeList) {
    		equipeListClone.add(equipe.clone());
    	}
    	clone.setEquipeList(equipeListClone);
    	clone.setLimitedByClub(limitedByClub);
    	
    	return clone;
    }
}
