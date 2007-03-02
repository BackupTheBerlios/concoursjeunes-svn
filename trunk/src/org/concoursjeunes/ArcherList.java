package org.concoursjeunes;

import java.util.*;

/**
 * Collection des concurrents présent sur le concours
 * @author  Aurelien Jeoffray
 * @version  3.2
 */

public class ArcherList {
//	static:
	/**
	 * Trie les archers par nom
	 */
	public static final int SORT_BY_NAME   = 0;
	/**
	 * Trie les archers par score
	 */
	public static final int SORT_BY_POINTS = 1;
	/**
	 * Trie les archers par cible
	 */
	public static final int SORT_BY_CIBLES = 2;
	/**
	 * Trie les archers par club
	 */
	public static final int SORT_BY_CLUBS  = 3;

	private ArrayList<Concurrent> archList  = new ArrayList<Concurrent>();
	private Parametre parametre;

	//Constructeur Obligatoire pour la sérialisation XML
	public ArcherList() { }

	/**
	 * Construit la liste d'archer sur les parametre donnée
	 * 
	 * @param parametre - les parametres de réference de la liste
	 */
	public ArcherList(Parametre parametre) {
		this.parametre = parametre;
	}

	/** Ajoute un archer à la liste des archers
	 *
	 * @param concurrent - le concurrent à ajouter
	 */
	public void add(Concurrent concurrent) {
		assert concurrent != null : "concurrent ne doit pas être null";

		this.archList.add(concurrent);
	}

	/**
	 * suppression d'un concurrent par son index
	 * 
	 * @param index - l'index du concurrent à supprimer
	 * 
	 * @return boolean - true si la suppression à réussi, false sinon
	 */
	public boolean remove(int index) {
		assert index > 0;
		assert index < archList.size();

		Concurrent concurrent = archList.get(index);
		return remove(concurrent);
	}

	/**
	 * suppression d'un concurrent par sa reference objet
	 * 
	 * @param concurrent - le concurrent à supprimer
	 * 
	 * @return boolean - true si la suppression à réussi, false sinon
	 */
	public boolean remove(Concurrent concurrent) {
		assert concurrent != null;

		return archList.remove(concurrent);
	}

	/**
	 * supprime tout les concurrentde la liste
	 *
	 */
	public void removeAll() {
		archList.clear();
	}

	/**
	 * Extrait la liste complete des concurrents pour un depart donnée
	 * 
	 * @param depart - le départ pour lequelle lister tous les archers ou -1 si tous
	 * @return Concurrent[] - tableaux des concurrents
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
	 * @param differentiationCriteria - le filtre se selection
	 * 
	 * @return Concurrent[] - la liste des concurrents appartenant au meme club
	 */
	public Concurrent[] list(Entite compagnie, CriteriaSet differentiationCriteria) {
		return list(compagnie, differentiationCriteria, -1);
	}

	/**
	 * Extrait tous les concurrents appartenant à un club donné
	 * 
	 * @param compagnie - le club dont on veut la liste
	 * @param differentiationCriteria - le filtre se selection
	 * @param depart - le depart concerne
	 * @return Concurrent[] - la liste des concurrents appartenant au meme club
	 */
	public Concurrent[] list(Entite compagnie, CriteriaSet differentiationCriteria, int depart) {

		assert compagnie != null;

		//buffer de selection
		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		//recherche
		for(Concurrent concurrent : archList) {
			if(concurrent.getClub().equals(compagnie)
					&& (differentiationCriteria == null || 
							differentiationCriteria.equals(concurrent.getCriteriaSet()))
							&& (depart == -1 || concurrent.getDepart() == depart))
				sel.add(concurrent);
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait la liste des concurrents pour une categorie donnée
	 * 
	 * @param differentiationCriteria - points commun des archers à récuperer
	 * @param depart - le depart concerné
	 * @return Concurrent[] - la liste des concurrents correspondant aux critere de recherche
	 */
	public Concurrent[] list(CriteriaSet differentiationCriteria, int depart) {

		assert differentiationCriteria != null;

		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(differentiationCriteria.equals(concurrent.getCriteriaSet()) && 
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
	 * @return Concurrent[] - la liste des concurrents correspondant aux criteres de recherche
	 */
	public Concurrent[] list(Reglement reglement, DistancesEtBlason distancesEtBlason, int depart) {

		assert distancesEtBlason != null;

		ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

		for(Concurrent concurrent : archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				CriteriaSet dci = concurrent.getCriteriaSet();
				dci.setFiltreCriteria(reglement.getPlacementFilter());
				DistancesEtBlason db = reglement.getCorrespondanceCriteriaSet_DB(dci);

				if(distancesEtBlason == null || db.equals(distancesEtBlason))
					sel.add(concurrent);
			}
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait les archers present sur la même cible pour un départ donné
	 * 
	 * @param cible - la cible concerné
	 * @param depart - le depart concerné
	 * @return Concurrent[] - la liste des archers présent sur la meme cible
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
	 * <ul>
	 * 
	 * @param no_sort_list - la liste à trier
	 * @param sortCritere - les critère de tri (SORT_BY_NAME, SORT_BY_POINTS, SORT_BY_CIBLES)
	 * @return la liste trié
	 */
	public static Concurrent[] sort(Concurrent[] no_sort_list, int sortCritere) {

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
						Concurrent tempConcurrent = sort_list[i];
						sort_list[i] = sort_list[j];
						sort_list[j] = tempConcurrent;
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
						Concurrent tempConcurrent = sort_list[i];
						sort_list[i] = sort_list[j];
						sort_list[j] = tempConcurrent;
					}
				}	
			}

			break;
		case SORT_BY_CIBLES:
			for(int i = 0; i < nbconcurrents-1;i++) {
				for(int j = i+1; j < nbconcurrents;j++) {
					int ciblei = sort_list[i].getCible() * 10 + sort_list[i].getPosition();
					int ciblej = sort_list[j].getCible() * 10 + sort_list[j].getPosition();
					if(ciblej < ciblei) {
						Concurrent tempConcurrent = sort_list[i];
						sort_list[i] = sort_list[j];
						sort_list[j] = tempConcurrent;
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
						Concurrent tempConcurrent = sort_list[i];
						sort_list[i] = sort_list[j];
						sort_list[j] = tempConcurrent;
					}	
				}	
			}

			break;
		}
		return sort_list;
	}

	/**
	 * Donne la liste des clubs représenté sur le concours
	 * 
	 * @return Entite[]
	 */
	public Entite[] listCompagnie() {
		return listCompagnie(-1);
	}

	/**
	 * Donne la liste des clubs représenté sur le concours pour un départ donné
	 * 
	 * @param depart - le départ concerné
	 * @return Entite[] - la liste des clubs
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
	 * Donne la liste des distance blason utilise pour un départ donnée
	 * 
	 * @param depart
	 * @return DistancesEtBlason
	 */
	public ArrayList<DistancesEtBlason> listDistancesEtBlason(Reglement reglement, int depart) {
		return listDistancesEtBlason(reglement, false, depart);
	}

	/**
	 * Donne la liste des distance blason utilise trie ou non par db pour un départ donné
	 * 
	 * @param depart - le départ concerné
	 * @param sort - est ce que la liste doit être trié?
	 * 
	 * @return DistancesEtBlason
	 */
	public ArrayList<DistancesEtBlason> listDistancesEtBlason(Reglement reglement, boolean sort, int depart) {
		ArrayList<DistancesEtBlason> alDB = new ArrayList<DistancesEtBlason>();

		for(Concurrent concurrent: archList) {
			if(depart == -1 || concurrent.getDepart() == depart) {
				boolean add = true;
				for(DistancesEtBlason dbtemp : alDB) {
					CriteriaSet dci = concurrent.getCriteriaSet();
					dci.setFiltreCriteria(reglement.getPlacementFilter());
					DistancesEtBlason db = reglement.getCorrespondanceCriteriaSet_DB(dci);
					if(dbtemp.equals(db)) {
						add = false;
						break;
					}
				}
				if(add) {
					CriteriaSet dci = concurrent.getCriteriaSet();
					dci.setFiltreCriteria(reglement.getPlacementFilter());
					DistancesEtBlason db = reglement.getCorrespondanceCriteriaSet_DB(dci);
					alDB.add(db);
				}
			}
		}

		if(sort) {
			for(int i = 0; i < alDB.size() - 1; i++) {
				for(int j = i + 1; j < alDB.size(); j++) {
					if(alDB.get(i).getDistance()[0] < alDB.get(j).getDistance()[0]) {
						DistancesEtBlason temp = alDB.get(i);
						alDB.set(i, alDB.get(j));
						alDB.set(j, temp);
					}
				}
			}
			for(int i = 0; i < alDB.size() - 1; i++) {
				for(int j = i + 1; j < alDB.size(); j++) {
					if(alDB.get(i).getBlason() > alDB.get(j).getBlason() && alDB.get(i).getDistance()[0] == alDB.get(j).getDistance()[0]) {
						DistancesEtBlason temp = alDB.get(i);
						alDB.set(i, alDB.get(j));
						alDB.set(j, temp);
					}
				}
			}
		}

		return alDB;
	}

	/**
	 * test si la ArcherList contient le concurrent donnée en parametre
	 * 
	 * @param concurrent - le concurrent à tester
	 * @return boolean - true si concurrent est présent
	 */
	public boolean contains(Concurrent concurrent) {
		assert concurrent != null;

		return this.archList.contains(concurrent);
	}

	/**
	 * Donne le nombre de concurrent sur le concours
	 * 
	 * @return int
	 */
	public int countArcher() {
		return this.archList.size();
	}

	/**
	 * Donne le nombre de concurrent sur le concours pour un départ donné
	 * 
	 * @param depart - le départ concerné
	 * @return int
	 */
	public int countArcher(int depart) {
		return list(depart).length;
	}

	/**
	 * Donne le nombre de concurrent pour la compagnie donne
	 * 
	 * @param depart
	 * @param compagnie
	 * @return int
	 */
	public int countArcher(Entite compagnie, int depart) {
		return list(compagnie, null, depart).length;
	}

	/**
	 * Donne le nombre de concurrent pour le distance/blason donné
	 * 
	 * @param depart
	 * @param distancesEtBlason
	 * @return int
	 */
	public int countArcher(Reglement reglement, DistancesEtBlason distancesEtBlason, int depart) {
		return list(reglement, distancesEtBlason, depart).length;
	}

	/**
	 * Donne le nombre de club sur le concours
	 * 
	 * @return int
	 */
	public int countCompagnie() {
		return listCompagnie(-1).length;
	}


	/**
	 * Donne le nombre de club sur le concours
	 * 
	 * @param depart
	 * @return int
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

	//public 

	/**
	 * <i>Methode necessaire à la deserialisation</i> Donne la liste des archers à l'objet
	 * @return  Returns the archList.
	 * @uml.property  name="archList"
	 */
	public ArrayList<Concurrent> getArchList() {
		return this.archList;
	}

	/**
	 * <i>Methode necessaire à la deserialisation</i> Affecte la liste des archers à l'objet
	 * @param archList  The archList to set.
	 * @uml.property  name="archList"
	 */
	public void setArchList(ArrayList<Concurrent> archList) {
		this.archList = archList;
	}

	/**
	 * @return  parametre
	 * @uml.property  name="parametre"
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * @param parametre  parametre à définir
	 * @uml.property  name="parametre"
	 */
	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}
}