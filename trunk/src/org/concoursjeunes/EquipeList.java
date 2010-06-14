/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 *  
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.ajdeveloppement.commons.ArraysUtils;

/**
 * Représente la liste des équipes constitué sur un concours
 * 
 * @author  Aurélien Jeoffray
 * @version 1.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipeList implements Cloneable {
   
	@XmlElement(name="equipe")
    private List<Equipe> equipeList  = new ArrayList<Equipe>();
    private int nbMembresRetenu = 3;
    
    private boolean limitedByClub = true;
    
    public EquipeList() {
    	
    }
    
    /**
     * Construit une nouvelle collection d'équipe dont le nombre
     * de membre retenue pour le classement est fournit en paramètre.
     * 
     * @param nbMembresRetenu le nombre de membre retenue pour le classement pour
     * chaque équipe
     */
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
     * @param index l'index de l'équipe
     * @return l'équipe à l'index donné
     */
    public Equipe get(int index) {
        return equipeList.get(index);
    }
    
    /**
     * Détermine si l'équipe nommé transmis en paramètre existe déjà sur le concours
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
     * Retourne l'équipe contenant le concurrent donné ou null si inexistant
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
     * @param equipe l'équipe à retirer
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
     * 
     * @deprecated remplacé par {@link #clear()}
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
     * @deprecated replacé par {@link #getEquipeList()}
     * 
     * @return la liste complete des équipes
     */
	@Deprecated
    public Equipe[] list() {
        Equipe[] equipes = new Equipe[equipeList.size()];
        equipeList.toArray(equipes);
        
        return equipes;
    }
    
    /**
     * Retourne la liste des équipes correspondant au critère donné
     * 
     * @param scna le filtre de tri des équipes
     * @return la liste des équipes
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
     * @return la liste des équipe fournit en paramètre mais trié
     */
    @Deprecated
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
    
    
    public static List<Equipe> sort(List<Equipe> no_sort_list) {
    	if(no_sort_list != null && no_sort_list.size() > 0) {
    		Collections.sort(no_sort_list, new Comparator<Equipe>() {
				@Override
				public int compare(Equipe o1, Equipe o2) {
					if(o1.getTotalScore() > o2.getTotalScore())
						return 1;
					else if(o1.getTotalScore() == o2.getTotalScore())
						return 0;
					return -1;
				}
			});
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
     * @return le nombre d'équipe
     */
    public int countEquipes() {
        return equipeList.size();
    }

    /**
	 * Retourne la liste des équipes
	 * 
	 * @return la liste des équipes
	 */
    public List<Equipe> getEquipeList() {
        return equipeList;
    }

    /**
	 * Pour la sérialisation, la table des équipes
	 * 
	 * @param equipeList la liste des équipes
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
