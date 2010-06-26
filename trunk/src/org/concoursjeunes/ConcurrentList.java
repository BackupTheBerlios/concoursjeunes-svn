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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Collection des concurrents présent sur le concours
 * 
 * @author  Aurélien Jeoffray
 * @version  3.3
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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

	@XmlElement(name="concurrent")
	private ArrayList<Concurrent> archList  = new ArrayList<Concurrent>();
	@XmlTransient
	private Parametre parametre;

	//Constructeur Obligatoire pour la sérialisation XML
	public ConcurrentList() { }

	/**
	 * Construit la liste d'archer sur les paramètre donnée
	 * 
	 * @param parametre - les paramètres de référence de la liste
	 */
	public ConcurrentList(Parametre parametre) {
		this.parametre = parametre;
	}

	/** Ajoute un archer à la liste des archers
	 *
	 * @param concurrent - le concurrent à ajouter
	 * 
	 * @return true si ajouté avec succès, false sinon
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
	 * @deprecated remplacé par {@link org.concoursjeunes.ConcurrentList#clear()}
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
	 * Extrait la liste complete des concurrents
	 * 
	 * @return liste des concurrents
	 */
	public List<Concurrent> list() {
		return Collections.unmodifiableList(archList);
	}

	/**
	 * Extrait la liste complete des concurrents pour un depart donnée
	 * 
	 * @param depart - le départ pour lequel lister tous les archers ou -1 si tous
	 * 
	 * @return liste des concurrents du départ choisi
	 */
	public List<Concurrent> list(int depart) {
		//buffer de selection
		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		//recherche
		for(Concurrent concurrent : archList) {
			if(depart == -1 || concurrent.getDepart() == depart)
				sel.add(concurrent);
		}

		return sel;
	}

	/**
	 * Extrait tous les concurrents appartenant à un club donné
	 * 
	 * @param compagnie - le club dont on veut la liste
	 * @param criteriaSet - le filtre de selection ou null pour tout prendre
	 * @param criteriaFilter filtre à appliquer sur le jeux de critère criteriaSet ou null
	 * 
	 * @return la liste des concurrents appartenant au même club pour un jeux de critère donné
	 */
	public List<Concurrent> list(Entite compagnie, CriteriaSet criteriaSet, Hashtable<Criterion, Boolean> criteriaFilter) {
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
	 * @return la liste des concurrents appartenant au même club pour un jeux de critère donné et pour un départ donné
	 */
	private List<Concurrent> list(Entite compagnie, CriteriaSet criteriaSet, int depart, Hashtable<Criterion, Boolean> criteriaFilter) {

		if(compagnie == null)
			return list(criteriaSet, depart, criteriaFilter);

		//buffer de selection
		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		//recherche
		for(Concurrent concurrent : archList) {
			if(concurrent.getEntite().equals(compagnie)
					&& (criteriaSet == null || 
							criteriaSet.equals(concurrent.getCriteriaSet().getFilteredCriteriaSet(criteriaFilter)))
							&& (depart == -1 || concurrent.getDepart() == depart))
				sel.add(concurrent);
		}

		return sel;
	}

	/**
	 * Extrait la liste des concurrents pour une catégorie donnée
	 * 
	 * @param criteriaSet - points commun des archers à récupérer
	 * @param depart - le depart concerné
	 * @param criteriaFilter filtre à appliquer sur le jeux de critère criteriaSet ou null
	 * 
	 * @return la liste des concurrents correspondant aux critère de recherche
	 */
	public List<Concurrent> list(CriteriaSet criteriaSet, int depart, Map<Criterion, Boolean> criteriaFilter) {

		if(criteriaSet == null)
			return list(depart);

		List<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(criteriaSet.equals(concurrent.getCriteriaSet().getFilteredCriteriaSet(criteriaFilter)) && 
					(depart == -1 || concurrent.getDepart() == depart))
				sel.add(concurrent);
		}

		return sel;
	}

	/**
	 * Extrait la liste des concurrents pour un distance/blason donnée et un depart donné
	 * 
	 * @param distancesEtBlason - le D/B concerné
	 * @param depart - le départ concerné ou -1 si tous
	 * @param handicap true si on retourne les archers handicapé 2 fois (car il consomme 2 place
	 * 
	 * @return la liste des concurrents correspondant aux critères de recherche
	 */
	public List<Concurrent> list(DistancesEtBlason distancesEtBlason, int depart, boolean handicap) {

		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(parametre.getReglement(), concurrent);

				if(distancesEtBlason == null || db.equals(distancesEtBlason)) {
					sel.add(concurrent);
					if(handicap && concurrent.isHandicape())
						sel.add(concurrent);
				}
			}
		}

		return sel;
	}

	/**
	 * Extrait les archers present sur la même cible pour un départ donné
	 * 
	 * @param cible - la cible concerné
	 * @param depart - le depart concerné
	 * 
	 * @return la liste des archers présent sur la même cible
	 */
	public List<Concurrent> list(int cible, int depart) {
		ArrayList<Concurrent> tmp = new ArrayList<Concurrent>();
		for(Concurrent concurrent : archList) {
			if(concurrent.getCible() == cible && concurrent.getDepart() == depart) {
				tmp.add(concurrent);
			}
		}

		return tmp;
	}

	/**
	 * trie une liste de concurrent en fonction des critère de trie
	 * <ul>
	 * <li>SORT_BY_NAME - tri par ordre alphabétique</li>
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
	public static List<Concurrent> sort(List<Concurrent> no_sort_list, SortCriteria sortCritere) {
		//List<Concurrent> sort_list = no_sort_list;
		if(no_sort_list != null) {
			switch(sortCritere) {
				case SORT_BY_NAME:
					return sort(no_sort_list, new NameComparator());
		
				case SORT_BY_POINTS:
					List<Concurrent> sort_list = sort(no_sort_list, new PointsComparator());
					Collections.reverse(sort_list); // pour les points on prend du plus élevé au plus faible
		
					return sort_list;
				case SORT_BY_TARGETS:
					return sort(no_sort_list, new TargetComparator());
	
				case SORT_BY_CLUBS:
					return sort(no_sort_list, new ClubComparator());
			}
		}
		return null;
	}
	
	/**
	 * Retourne la liste des concurrents trié par le comparateur fournit en paramètre
	 * 
	 * @param no_sort_list la liste à trier
	 * @param comparator le critère de tri
	 * @return la liste trié
	 */
	public static List<Concurrent> sort(List<Concurrent> no_sort_list, Comparator<Concurrent> comparator) {
		List<Concurrent> sort_list = no_sort_list;
		if(no_sort_list != null)
			Collections.sort(sort_list, comparator);
		
		return sort_list;
	}
	
	/**
	 * Classement des candidats
	 * 
	 * @return map contenant une liste de concurrent trié par jeux de critère de classement
	 */
	public Classement classement() {

		Classement classement = new Classement();
		Map<CriteriaSet, List<Concurrent>> concurrentsClasse = new HashMap<CriteriaSet, List<Concurrent>>();

		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(parametre.getReglement(), parametre.getReglement().getClassementFilter());

		// Affectation des valeurs
		for (CriteriaSet categorie : catList) {
			// sort la liste des concurrents correspondant aux critères de
			// recherche
			List<Concurrent> unsortList = list(categorie, -1, parametre.getReglement().getClassementFilter());
			if (unsortList.size() > 0) {
				List<Concurrent> sortList = sort(unsortList, ConcurrentList.SortCriteria.SORT_BY_POINTS);
			
				concurrentsClasse.put(categorie, sortList);
			}
		}
		
		classement.setClassementPhaseQualificative(concurrentsClasse);
		
		

		return classement;
	}
	
	/**
	 * Passe au concurrent suivant par ordre de cible/position
	 *
	 * @deprecated utiliser {@link #nextConcurrent(List, Concurrent, SortCriteria)} à la place
	 * @param curConcurrent - le concurrent courant
	 * @return le concurrent suivant
	 */
	@Deprecated
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
	 * Retourne le concurrent suivant dans la liste fournit en paramètre en fonction du critère de tri sélectionné
	 * 
	 * @param lstConcurrent la liste des concurrent dans lequel récupérer le suivant
	 * @param curConcurrent le concurrent courant
	 * @param sortCritere le critère de tri permettant de retrouver le concurrent suivant ou null si il faut prendre
	 * l'ordre naturel de la liste
	 * @return le concurrent suivant ou null si non trouvé
	 */
	public static Concurrent nextConcurrent(List<Concurrent> lstConcurrent, Concurrent curConcurrent, SortCriteria sortCritere) {
		
		if(sortCritere != null)
			sort(lstConcurrent, sortCritere);
		
		int index = lstConcurrent.indexOf(curConcurrent);
		if(index != -1 && index < lstConcurrent.size() -1)
			return lstConcurrent.get(index+1);
		
		return null;
	}

	/**
	 * Passe au concurrent précédent par ordre de cible/position
	 * 
	 * @deprecated utiliser {@link #previousConcurrent(List, Concurrent, SortCriteria)} à la place
	 * @param curConcurrent - le concurrent courant
	 * 
	 * @return le concurrent précèdent
	 */
	@Deprecated
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
	 * Retourne le concurrent précédent dans la liste fournit en paramètre en fonction du critère
	 * de tri sélectionné
	 * 
	 * @param lstConcurrent la liste des concurrent dans lequel récupérer le précédent
	 * @param curConcurrent le concurrent courant
	 * @param sortCritere le critère de tri permettant de retrouver le concurrent précédent ou null si il faut prendre
	 * l'ordre naturel de la liste
	 * @return le concurrent précédent ou null si non trouvé
	 */
	public static Concurrent previousConcurrent(List<Concurrent> lstConcurrent, Concurrent curConcurrent, SortCriteria sortCritere) {
		
		if(sortCritere != null)
			sort(lstConcurrent, sortCritere);
		
		int index = lstConcurrent.indexOf(curConcurrent);
		if(index != -1 && index > 0)
			return lstConcurrent.get(index-1);
		
		return null;
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
					if(dbtemp.equals(concurrent.getEntite())) {
						add = false;
						break;
					}
				}
				if(add) {
					alCie.add(concurrent.getEntite());
				}
			}
		}

		int number_of_element = alCie.size();

		return alCie.toArray(new Entite[number_of_element]);
	}
	
	public List<CriteriaSet> listCriteriaSet() {
		List<CriteriaSet> criteriaSets = new ArrayList<CriteriaSet>();
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(parametre.getReglement(), parametre.getReglement().getClassementFilter());

		// Affectation des valeurs
		for (CriteriaSet categorie : catList) {
			// sort la liste des concurrents correspondant aux critères de
			// recherche
			List<Concurrent> unsortList = list(categorie, -1, parametre.getReglement().getClassementFilter());
			if (unsortList.size() > 0) {
				criteriaSets.add(categorie);
			}
		}
		
		return criteriaSets;
	}

	/**
	 * Donne la liste des distances/blasons utilisé pour un départ donnée en fonction du règlement donnée
	 * 
	 * @param reglement le règlement permettant de déterminer les distances/blasons
	 * @param depart le départ concerné
	 * 
	 * @return la liste des distances/blasons du départ
	 */
	public List<DistancesEtBlason> listDistancesEtBlason(Reglement reglement, int depart) {
		return listDistancesEtBlason(reglement, false, depart);
	}

	/**
	 * Donne la liste des distances/blasons utilisé, trié ou non pour un départ donné.
	 * 
	 * @param reglement le règlement determinant les D/B
	 * @param depart le départ concerné ou -1 pour prendre tout les départs
	 * @param sort est ce que la liste doit être trié?
	 * 
	 * @return la liste des distances/blasons utilisé pour un départ donné
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
	 * test si la ConcurrentList contient le concurrent donnée en paramètre
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
		return list(depart).size();
	}

	/**
	 * Donne le nombre de concurrent pour le distance/blason donné
	 * 
	 * @param distancesEtBlason le pas de tir pour lequel retourné le nombre d'archer
	 * @param depart le numéro du depart pour lequel retourner le nombre d'archer ou -1 si tous les départs
	 * @return le nombre d'archer sur une distance donné
	 */
	public int countArcher(DistancesEtBlason distancesEtBlason, int depart) {
		return list(distancesEtBlason, depart, false).size();
	}
	
	/**
	 * Donne le nombre de concurrent pour le distance/blason donné en comptant ou non les
	 * places supplémentaire réservé aux archer handicapé
	 * 
	 * @param distancesEtBlason le pas de tir pour lequel retourné le nombre d'archer
	 * @param depart le numéro du depart pour lequel retourner le nombre d'archer ou -1 si tous les départs
	 * @param handicap si <i>true</i>, alors compter 2 places par archers handicapé
	 * @return le nombre d'archer sur une distance donné + le nombre de place bloqué par les archers
	 * handicapé si handicap est à <i>true</i>
	 */
	public int countArcher(DistancesEtBlason distancesEtBlason, int depart, boolean handicap) {
		return list(distancesEtBlason, depart, handicap).size();
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
	 * @param cible - la cible ou se trouve le concurrent à récupérer
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
	 * <i>Méthode nécessaire à la deserialisation</i> Donne la liste des archers à l'objet
	 * @return  Returns the archList.
	 */
	public ArrayList<Concurrent> getArchList() {
		return this.archList;
	}

	/**
	 * <i>Méthode nécessaire à la deserialisation</i> Affecte la liste des archers à l'objet
	 * 
	 * @param archList  The archList to set.
	 */
	public void setArchList(ArrayList<Concurrent> archList) {
		this.archList = archList;
	}

	/**
	 * <i>Méthode nécessaire à la sérialisation</i>
	 * @return  parametre
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * <i>Méthode nécessaire à la deserialisation</i>
	 * 
	 * @param parametre  paramètre à définir
	 */
	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}
	
	/**
	 * Compare les concurrents par leurs noms/prénoms
	 * 
	 * @author Aurélien JEOFFRAY
	 *
	 */
	public static class NameComparator implements Comparator<Concurrent> {
		@Override
		public int compare(Concurrent o1, Concurrent o2) {
			return o1.getFullName().compareToIgnoreCase(o2.getFullName());
		}
	}
	
	/**
	 * Compare les concurrents par leurs positions sur le pas de tir
	 *  
	 * @author Aurélien JEOFFRAY
	 *
	 */
	public static class TargetComparator implements Comparator<Concurrent> {
		@Override
		public int compare(Concurrent o1, Concurrent o2) {
			int ciblei = o1.getCible() * 10 + o1.getPosition();
			int ciblej = o2.getCible() * 10 + o2.getPosition();
			
			return ciblej > ciblei ? -1 : (ciblej == ciblei) ? 0 : 1;
		}
	}
	
	/**
	 * Compare les concurrents par le nom de leurs clubs d'appartenance
	 * 
	 * @author Aurélien JEOFFRAY
	 *
	 */
	public static class ClubComparator implements Comparator<Concurrent> {
		@Override
		public int compare(Concurrent o1, Concurrent o2) {
			return o1.getEntite().getNom().compareToIgnoreCase(o2.getEntite().getNom());
		}
	}
	
	/**
	 * Compare les concurrents par leurs scores en phase qualificative ou sur une phase final
	 * 
	 * @author Aurélien JEOFFRAY
	 *
	 */
	public static class PointsComparator implements Comparator<Concurrent> {
		
		private int phase = -1;
		/**
		 * compare les points en phase qualificative
		 */
		public PointsComparator() {
			
		}
		
		/**
		 * compare les points sur une phase final
		 * 
		 * @param phase la phase sur laquelle comparer les points
		 */
		public PointsComparator(int phase) {
			this.phase = phase;
		}
		
		@Override
		public int compare(Concurrent o1, Concurrent o2) {
			if(phase < 0)
				return o1.compareScoreWith(o2);
			else if(phase < 6)
				return o1.compareScorePhaseFinalWith(o2, phase);
			return 0;
		}
	}
}