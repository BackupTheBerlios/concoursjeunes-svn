/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import ajinteractive.standard.common.ArraysUtils;

/**
 * Collection des concurrents présent sur le concours
 * 
 * @author  Aurelien Jeoffray
 * @version  3.3
 */
@XmlRootElement
public class ConcurrentList {
	
	public enum SortCriteria {
		/**
		 * Trie les archers par nom
		 */
		SORT_BY_NAME,
		/**
		 * Trie les archers par score
		 */
		SORT_BY_POINTS,
		/**
		 * Trie les archers par cible
		 */
		SORT_BY_TARGETS,
		/**
		 * Trie les archers par club
		 */
		SORT_BY_CLUBS
	}

	private ArrayList<Concurrent> archList  = new ArrayList<Concurrent>();
	private Parametre parametre;

	//Constructeur Obligatoire pour la sérialisation XML
	public ConcurrentList() { }

	/**
	 * Construit la liste d'archer sur les parametre donnée
	 * 
	 * @param parametre - les parametres de réference de la liste
	 */
	public ConcurrentList(Parametre parametre) {
		this.parametre = parametre;
	}

	/** Ajoute un archer à la liste des archers
	 *
	 * @param concurrent - le concurrent à ajouter
	 * 
	 * @return true si ajouté avec succés, false sinon
	 */
	public boolean add(Concurrent concurrent) {
		if(concurrent != null)
			return archList.add(concurrent);
		return false;
	}

	/**
	 * suppression d'un concurrent par son index
	 * 
	 * @param index - l'index du concurrent à supprimer
	 * 
	 * @return le concurrent supprimé ou null si inexistant
	 */
	public Concurrent remove(int index) {
		if(index > 0 && index < archList.size()) {
			return remove(archList.get(index));
		}
		return null;
	}

	/**
	 * suppression d'un concurrent par sa reference objet
	 * 
	 * @param concurrent - le concurrent à supprimer
	 * 
	 * @return le concurrent supprimé ou null si inexistant
	 */
	public Concurrent remove(Concurrent concurrent) {
		if(concurrent != null) {
			
			for(int i = 0; i < archList.size(); i++) {
				if(archList.get(i).equals(concurrent) && archList.get(i).getDepart() == concurrent.getDepart()) {
					return archList.remove(i);
				}
			}
		}
		return null;
	}

	/**
	 * supprime tout les concurrent de la liste
	 *
	 */
	@Deprecated
	public void removeAll() {
		archList.clear();
	}
	
	/**
	 * supprime tout les concurrent de la liste
	 *
	 */
	public void clear() {
		archList.clear();
	}

	/**
	 * Extrait la liste complete des concurrents pour un depart donnée
	 * 
	 * @param depart - le départ pour lequelle lister tous les archers ou -1 si tous
	 * 
	 * @return tableaux des concurrents du départ choisi
	 */
	public Concurrent[] list(int depart) {
		//buffer de selection
		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		//recherche
		for(Concurrent concurrent : archList) {
			if(depart == -1 || concurrent.getDepart() == depart)
				sel.add(concurrent);
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait tous les concurrents appartenant à un club donné
	 * 
	 * @param compagnie - le club dont on veut la liste
	 * @param criteriaSet - le filtre de selection ou null pour tout prendre
	 * @param criteriaFilter filtre à appliquer sur le jeux de critère criteriaSet ou null
	 * 
	 * @return la liste des concurrents appartenant au meme club pour un jeux de critère donné
	 */
	public Concurrent[] list(Entite compagnie, CriteriaSet criteriaSet, Hashtable<Criterion, Boolean> criteriaFilter) {
		return list(compagnie, criteriaSet, -1, criteriaFilter);
	}

	/**
	 * Extrait tous les concurrents appartenant à un club donné
	 * 
	 * @param compagnie - le club dont on veut la liste
	 * @param criteriaSet - le filtre se selection
	 * @param depart - le depart concerne
	 * @param criteriaFilter filtre à appliquer sur le jeux de critère criteriaSet ou null
	 * 
	 * @return la liste des concurrents appartenant au meme club pour un jeux de critère donné et pour un départ donné
	 */
	private Concurrent[] list(Entite compagnie, CriteriaSet criteriaSet, int depart, Hashtable<Criterion, Boolean> criteriaFilter) {

		assert compagnie != null;

		//buffer de selection
		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		//recherche
		for(Concurrent concurrent : archList) {
			if(concurrent.getClub().equals(compagnie)
					&& (criteriaSet == null || 
							criteriaSet.equals(concurrent.getCriteriaSet().getFilteredCriteriaSet(criteriaFilter)))
							&& (depart == -1 || concurrent.getDepart() == depart))
				sel.add(concurrent);
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait la liste des concurrents pour une categorie donnée
	 * 
	 * @param criteriaSet - points commun des archers à récuperer
	 * @param depart - le depart concerné
	 * @param criteriaFilter filtre à appliquer sur le jeux de critère criteriaSet ou null
	 * 
	 * @return la liste des concurrents correspondant aux critere de recherche
	 */
	public Concurrent[] list(CriteriaSet criteriaSet, int depart, Hashtable<Criterion, Boolean> criteriaFilter) {

		assert criteriaSet != null;

		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(criteriaSet.equals(concurrent.getCriteriaSet().getFilteredCriteriaSet(criteriaFilter)) && 
					(depart == -1 || concurrent.getDepart() == depart))
				sel.add(concurrent);
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait la liste des concurrents pour un distance/blason donnée et un depart donné
	 * 
	 * @param distancesEtBlason - le D/B concerné
	 * @param depart - le départ concerné ou -1 si tous
	 * 
	 * @return la liste des concurrents correspondant aux criteres de recherche
	 */
	public Concurrent[] list(Reglement reglement, DistancesEtBlason distancesEtBlason, int depart, boolean handicap) {

		assert distancesEtBlason != null;

		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent);

				if(distancesEtBlason == null || db.equals(distancesEtBlason)) {
					sel.add(concurrent);
					if(handicap && concurrent.isHandicape())
						sel.add(concurrent);
				}
			}
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait les archers present sur la même cible pour un départ donné
	 * 
	 * @param cible - la cible concerné
	 * @param depart - le depart concerné
	 * 
	 * @return la liste des archers présent sur la meme cible
	 */
	public Concurrent[] list(int cible, int depart) {
		ArrayList<Concurrent> tmp = new ArrayList<Concurrent>();
		for(Concurrent concurrent : archList) {
			if(concurrent.getCible() == cible && concurrent.getDepart() == depart) {
				tmp.add(concurrent);
			}
		}

		return tmp.toArray(new Concurrent[tmp.size()]);
	}

	/**
	 * trie une liste de concurrent en fonction des critere de trie
	 * <ul>
	 * <li>SORT_BY_NAME - tri par ordre alphabetique</li>
	 * <li>SORT_BY_POINTS - classement par points</li>
	 * <li>SORT_BY_CIBLES - tri par position sur cible</li>
	 * <li>SORT_BY_CLUBS - tri par clubs</li>
	 * <ul>
	 * 
	 * @param no_sort_list - la liste à trier
	 * @param sortCritere - les critère de tri (SORT_BY_NAME, SORT_BY_POINTS, SORT_BY_CIBLES, SORT_BY_CLUBS)
	 * 
	 * @return la liste trié
	 */
	public static Concurrent[] sort(Concurrent[] no_sort_list, SortCriteria sortCritere) {

		assert no_sort_list != null;

		int nbconcurrents = no_sort_list.length;
		Concurrent[] sort_list = no_sort_list;

		switch(sortCritere) {
			case SORT_BY_NAME:
				for(int i = 0; i < nbconcurrents-1;i++) {
					for(int j = i+1; j < nbconcurrents;j++) {
						String namei = sort_list[i].getID();
						String namej = sort_list[j].getID();
						if(namej.compareToIgnoreCase(namei) < 0) {
							ArraysUtils.swap(sort_list, i, j);
						}	
					}	
				}
	
				break;
			case SORT_BY_POINTS:
				for(int i = 0; i < nbconcurrents-1;i++) {
					for(int j = i+1; j < nbconcurrents;j++) {
						int scorei = sort_list[i].getTotalScore();
						int scorej = sort_list[j].getTotalScore();
						if(scorej > scorei ||
								(scorej == scorei && (sort_list[j].getManque() < sort_list[i].getManque() ||
										(sort_list[j].getManque() == sort_list[i].getManque() && sort_list[j].getDix() > sort_list[i].getDix()) ||
										(sort_list[j].getManque() == sort_list[i].getManque() && sort_list[j].getDix() == sort_list[i].getDix() &&
												sort_list[j].getNeuf() > sort_list[i].getNeuf())))) {
							ArraysUtils.swap(sort_list, i, j);
						}
					}	
				}
	
				break;
			case SORT_BY_TARGETS:
				for(int i = 0; i < nbconcurrents-1;i++) {
					for(int j = i+1; j < nbconcurrents;j++) {
						int ciblei = sort_list[i].getCible() * 10 + sort_list[i].getPosition();
						int ciblej = sort_list[j].getCible() * 10 + sort_list[j].getPosition();
						if(ciblej < ciblei) {
							ArraysUtils.swap(sort_list, i, j);
						}	
					}	
				}
	
				break;
			case SORT_BY_CLUBS:
				for(int i = 0; i < nbconcurrents-1;i++) {
					for(int j = i+1; j < nbconcurrents;j++) {
						Entite namei = sort_list[i].getClub();
						Entite namej = sort_list[j].getClub();
						if(namej.getNom().compareToIgnoreCase(namei.getNom()) < 0) {
							ArraysUtils.swap(sort_list, i, j);
						}	
					}	
				}
	
				break;
		}
		return sort_list;
	}
	
	/**
	 * Passe au concurrent suivant par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * 
	 * @return le concurrent suivant
	 */
	public Concurrent nextConcurrent(Concurrent curConcurrent) {
		int depart = curConcurrent.getDepart();
		int cible = curConcurrent.getCible(); 
		int position = curConcurrent.getPosition();

		do {
			position++;
			if(position == parametre.getNbTireur()) {
				position = 0;
				cible++;
			}
		} while(getConcurrentAt(depart, cible, position) == null && cible <= parametre.getNbCible());

		return getConcurrentAt(depart, cible, position);
	}

	/**
	 * Passe au concurrent précédent par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * 
	 * @return le concurrent précedent
	 */
	public Concurrent previousConcurrent(Concurrent curConcurrent) {
		int depart = curConcurrent.getDepart();
		int cible = curConcurrent.getCible(); 
		int position = curConcurrent.getPosition();

		do {
			position--;
			if(position == -1) {
				position = parametre.getNbTireur() - 1;
				cible--;
			}
		} while(getConcurrentAt(depart, cible, position) == null && cible > 0);

		return getConcurrentAt(depart, cible, position);
	}

	/**
	 * Donne la liste des clubs représenté sur le concours
	 * 
	 * @return la liste des clubs représenté sur le concours
	 */
	public Entite[] listCompagnie() {
		return listCompagnie(-1);
	}

	/**
	 * Donne la liste des clubs représenté sur le concours pour un départ donné
	 * 
	 * @param depart - le départ concerné
	 * @return la liste des clubs représenté sur le concours
	 */
	public Entite[] listCompagnie(int depart) {
		ArrayList<Entite> alCie = new ArrayList<Entite>();

		for(Concurrent concurrent: archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				boolean add = true;
				for(Entite dbtemp : alCie) {
					if(dbtemp.equals(concurrent.getClub())) {
						add = false;
						break;
					}
				}
				if(add) {
					alCie.add(concurrent.getClub());
				}
			}
		}

		int number_of_element = alCie.size();

		return alCie.toArray(new Entite[number_of_element]);
	}

	/**
	 * Donne la liste des distances/blasons utilisé pour un départ donnée en fonction du réglement donnée
	 * 
	 * @param reglement le réglement permettant de détérminer les distances/blasons
	 * @param depart le départ concerné
	 * 
	 * @return la liste des distances/blasons du déprt
	 */
	public List<DistancesEtBlason> listDistancesEtBlason(Reglement reglement, int depart) {
		return listDistancesEtBlason(reglement, false, depart);
	}

	/**
	 * Donne la liste des distance blason utilise trie ou non par db pour un départ donné
	 * 
	 * @param reglement le reglement determinant les D/B
	 * @param depart - le départ concerné
	 * @param sort - est ce que la liste doit être trié?
	 * 
	 * @return DistancesEtBlason
	 */
	public List<DistancesEtBlason> listDistancesEtBlason(Reglement reglement, boolean sort, int depart) {
		List<DistancesEtBlason> alDB = new ArrayList<DistancesEtBlason>();

		for(Concurrent concurrent: archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				boolean add = true;
				for(DistancesEtBlason dbtemp : alDB) {
					DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent);
					if(dbtemp.equals(db)) {
						add = false;
						break;
					}
				}
				if(add) {
					alDB.add(DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent));
				}
			}
		}

		if(sort) {
			for(int i = 0; i < alDB.size() - 1; i++) {
				for(int j = i + 1; j < alDB.size(); j++) {
					if(alDB.get(i).getDistance()[0] < alDB.get(j).getDistance()[0]) {
						Collections.swap(alDB, i, j);
					}
				}
			}
			for(int i = 0; i < alDB.size() - 1; i++) {
				for(int j = i + 1; j < alDB.size(); j++) {
					if(alDB.get(i).getTargetFace().getNumordre() > alDB.get(j).getTargetFace().getNumordre() && alDB.get(i).getDistance()[0] == alDB.get(j).getDistance()[0]) {
						Collections.swap(alDB, i, j);
					}
				}
			}
		}

		return alDB;
	}

	/**
	 * test si la ConcurrentList contient le concurrent donnée en parametre
	 * 
	 * @param concurrent - le concurrent à tester
	 * @return true si concurrent est présent
	 */
	public boolean contains(Concurrent concurrent) {
		if(concurrent != null)
			return this.archList.contains(concurrent);
		return false;
	}
	
	/**
	 * test si la ConcurrentList contient le concurrent donnée sur le départ donnée
	 * 
	 * @param concurrent le concurrent à tester
	 * @param depart le depart donnée
	 * 
	 * @return true si le concurrent est présent sur le départ, false sinon
	 */
	public boolean contains(Concurrent concurrent, int depart) {
		if(concurrent != null) {
			ArrayList<Concurrent> conc = new ArrayList<Concurrent>();
			for(Concurrent aConc : archList) {
				if(aConc.getDepart() == depart)
					conc.add(aConc);
			}
			return conc.contains(concurrent);
		}
		return false;
	}

	/**
	 * Donne le nombre de concurrent sur le concours
	 * 
	 * @return le nombre d'archer inscrit sur le concours
	 */
	public int countArcher() {
		return this.archList.size();
	}

	/**
	 * Donne le nombre de concurrent sur le concours pour un départ donné
	 * 
	 * @param depart - le départ concerné
	 * @return le nombre d'archer sur le départ concerné
	 */
	public int countArcher(int depart) {
		return list(depart).length;
	}

	/**
	 * Donne le nombre de concurrent pour le distance/blason donné
	 * 
	 * @param reglement le reglement permettant de calculer les distances/blasons
	 * @param distancesEtBlason le pas de tir pour lequel retourné le nombre d'archer
	 * @param depart le numero du depart pour lequel retourner le nombre d'archer ou -1 si tous les départs
	 * @return le nombre d'archer sur une distance donné
	 */
	public int countArcher(Reglement reglement, DistancesEtBlason distancesEtBlason, int depart) {
		return list(reglement, distancesEtBlason, depart, false).length;
	}
	
	public int countArcher(Reglement reglement, DistancesEtBlason distancesEtBlason, int depart, boolean handicap) {
		return list(reglement, distancesEtBlason, depart, handicap).length;
	}

	/**
	 * Donne le nombre de club sur le concours
	 * 
	 * @return le nombre de club représenté sur le concours
	 */
	public int countCompagnie() {
		return listCompagnie(-1).length;
	}


	/**
	 * Donne le nombre de club sur le départ
	 * 
	 * @param depart le depart pour lequel retourner le nombre de club
	 * @return le nombre de club sur le départ
	 */
	public int countCompagnie(int depart) {
		return listCompagnie(depart).length;
	}

	/**
	 * Donne le concurrent à l'index donné
	 * 
	 * @param index
	 * @return Concurrent
	 */
	public Concurrent get(int index) {
		return this.archList.get(index);
	}

	/**
	 * Donne le concurrent à la position cible,position sur le pas de tir
	 * 
	 * @param depart
	 * @param cible - la cible ou se trouve le concurrent à recuperer
	 * @param position - la position ou se trouve le concurrent
	 * @return Concurrent - le concurrent à la position ou null si aucun
	 */
	public Concurrent getConcurrentAt(int depart, int cible, int position) {
		for(Concurrent concurrent: archList) {
			if(concurrent.getDepart() == depart && 
					concurrent.getCible() == cible && 
					concurrent.getPosition() == position)
				return concurrent;
		}
		return null;
	}

	/**
	 * <i>Methode necessaire à la deserialisation</i> Donne la liste des archers à l'objet
	 * @return  Returns the archList.
	 */
	public ArrayList<Concurrent> getArchList() {
		return this.archList;
	}

	/**
	 * <i>Methode necessaire à la deserialisation</i> Affecte la liste des archers à l'objet
	 * 
	 * @param archList  The archList to set.
	 */
	public void setArchList(ArrayList<Concurrent> archList) {
		this.archList = archList;
	}

	/**
	 * <i>Methode necessaire à la serialisation</i>
	 * @return  parametre
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * <i>Methode necessaire à la deserialisation</i>
	 * 
	 * @param parametre  parametre à définir
	 */
	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}
}