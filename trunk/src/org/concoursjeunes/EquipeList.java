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

import javax.xml.bind.annotation.XmlTransient;

/**
 * @author  Aurelien Jeoffray
 */
public class EquipeList implements Cloneable {
   
    private ArrayList<Equipe> equipeList  = new ArrayList<Equipe>();
    @XmlTransient
    private FicheConcours ficheConcours;
    
    private boolean limitedByClub = true;
    
    public EquipeList() {
    	
    }
    public EquipeList(FicheConcours ficheConcours) { 
    	this.ficheConcours = ficheConcours;
    }
    
    @XmlTransient
    public FicheConcours getFicheConcours() {
		return ficheConcours;
	}

	public void setFicheConcours(FicheConcours ficheConcours) {
		this.ficheConcours = ficheConcours;
	}

	/**
     * 
     * @param newEquipe
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
                
                if(equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu())
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
    	for(Equipe equipe : equipeList) {
            if(equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu())
                remove(equipe);
        }
    }
    
    /**
     * retire toutes les équipes
     *
     */
    public void removeAll() {
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
            for(int i = 0; i < no_sort_list.length - 1; i++) {
                for(int j = i+1; j < no_sort_list.length; j++) {
                    if(no_sort_list[i].getTotalScore() < no_sort_list[j].getTotalScore()) {
                        Equipe tempEquipe = no_sort_list[i];
                        no_sort_list[i] = no_sort_list[j];
                        no_sort_list[j] = tempEquipe;
                    }
                }
            }
        }
        
        return no_sort_list;
    }
    
    public ArrayList<CriteriaSet> listCriteriaSet() {
    	ArrayList<CriteriaSet> listCriteriaSet = new ArrayList<CriteriaSet>();
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
	 * @uml.property  name="equipeList"
	 */
    public ArrayList<Equipe> getEquipeList() {
        return equipeList;
    }

    /**
	 * Pour la sérialisation, la table des équipes
	 * @param equipeList  equipeList à définir.
	 * @uml.property  name="equipeList"
	 */
    public void setEquipeList(ArrayList<Equipe> equipeList) {
        this.equipeList = equipeList;
    }
    
    public boolean isLimitedByClub() {
		return limitedByClub;
	}
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
    	clone.setFicheConcours(ficheConcours);
    	clone.setLimitedByClub(limitedByClub);
    	
    	return clone;
    }
}
