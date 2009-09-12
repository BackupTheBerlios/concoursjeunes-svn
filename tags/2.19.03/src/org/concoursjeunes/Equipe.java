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
import java.util.List;

/**
 * Class représentant une équipe d'archer pour le concours
 * 
 * @author  Aurélien Jeoffray
 * @version  2.0
 */

public class Equipe implements Cloneable, Comparable<Equipe> {

	private ArrayList<Concurrent> membresEquipe = new ArrayList<Concurrent>();
	private String nomEquipe = ""; //$NON-NLS-1$
	
    private int nbRetenu = 3;
    private CriteriaSet differentiationCriteria;
	
    /**
     * Constructeur vide pour la sérialisation XML
     */
    public Equipe() { 
    }
	
    /**
     * construit une équipe portant le nom nomEquipe
     * 
     * @param nomEquipe le nom de l'équipe à construire
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
     * Retourne les concurrents ayant été sélectionné pour pouvoir comptabiliser le score
     * 
	 * @return les archers retenu pour le comptage des points
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

		//sélectionne les 3 meilleurs score
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
     * Définit le nombre d'archer retenu dans la comptabilité des scores
     * 
	 * @param nbRetenu  The nbRetenu to set.
	 */
    public void setNbRetenu(int nbRetenu) {
        this.nbRetenu = nbRetenu;
    }
    

    /**
     * Renvoie Le critère de distinction de l'équipe.
     *  
	 * @return Le critère de distinction de l'équipe.
	 */
    public CriteriaSet getDifferentiationCriteria() {
        if(differentiationCriteria == null)
            differentiationCriteria = new CriteriaSet();
        
        return differentiationCriteria;
    }

    /**
     * Définit le critère de distinction de l'équipe.
     * 
	 * @param differentiationCriteria  le critère de distinction de l'équipe.
	 */
    public void setDifferentiationCriteria(CriteriaSet differentiationCriteria) {
        this.differentiationCriteria = differentiationCriteria;
    }

    /**
     * Définit la liste des membres de l'equipe
     * 
	 * @param equipe la liste des membres de l'equipe
	 */
    public void setMembresEquipe(ArrayList<Concurrent> equipe) {
        this.membresEquipe = equipe;
    }
    
    /**
     * Ajoute un concurrent à l'Equipe
     * 
     * @param concurrent le concurrent à ajouter
     */
    public void addConcurrent(Concurrent concurrent) {
        if(concurrent != null && !this.membresEquipe.contains(concurrent))
            this.membresEquipe.add(concurrent);
    }
    
    /**
     * retire un concurrent de l'Equipe
     * 
     * @param concurrent le concurrent à retirer
     */
    public void removeConcurrent(Concurrent concurrent) {
        if(concurrent != null && this.membresEquipe.contains(concurrent))
            this.membresEquipe.remove(concurrent);
    }
    
    /**
     * Vérifie si l'équipe contient ou non le concurrent donné en paramètre
     * 
     * @param concurrent le concurrent à vérifier
     * @return true si present, false sinon
     */
    public boolean contains(Concurrent concurrent) {
        return membresEquipe.contains(concurrent);
    }
    
    /**
	 * renvoie le nom de l'équipe
	 * 
	 * @return le nom de l'équipe
	 */
    public String getNomEquipe() {
        return this.nomEquipe;
    }
    
    /**
	 * Défini le nom de l'Equipe
	 * 
	 * @param nomEquipe le nom de l'Equipe
	 */
    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }
    
    /**
     * renvoie le score total obtenu par la sélection
     * 
     * @return le score total de la sélection de l'équipe
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