package ajinteractive.concours;

import java.util.ArrayList;

/**
 * Collection des concurrents présent sur le concours
 * 
 * @author Aurelien Jeoffray
 * @version 3.2
 */

public class ArcherList {
//static:
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
//private:
    /**
     * Liste des archers
     */
	private ArrayList<Concurrent> archList  = new ArrayList<Concurrent>();
    
    private FicheConcours ficheConcours;
	
    //Constructeur Obligatoire pour la sérialisation XML
	public ArcherList() { }
    
    public ArcherList(FicheConcours ficheConcours) {
        this.ficheConcours = ficheConcours;
    }
	
	/** Ajoute un archer à la liste des archers
	 *
	 * @param concurrent - le concurrent à ajouter
	 */
	public void add(Concurrent concurrent) {
        this.archList.add(concurrent);
	}
	
	/**
	 * suppression d'un concurrent par son index
	 * 
	 * @param index
	 * @return boolean - true si suppression avec succès, false sinon
	 */
	public boolean remove(int index) {
		Concurrent concurrent = archList.get(index);
        return remove(concurrent);
	}
    
    /**
     * suppression d'un concurrent par sa reference objet
     * 
     * @param concurrent
     * @return boolean - true si suppression avec succès, false sinon
     */
    public boolean remove(Concurrent concurrent) {
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
	 * Extrait la liste complete des concurrents
	 * 
	 * @return Concurrent[] - tableaux des concurrents
	 */
	public Concurrent[] list() {
		int number_of_element = this.archList.size();
		
		return archList.toArray(new Concurrent[number_of_element]);
	}
	
	/**
	 * Extrait tous les concurrents appartenant à un club donné
	 * 
	 * @param compagnie - le club dont on veut la liste
     * @param differentiationCriteria - le filtre se selection
	 * @return Concurrent[] - la liste des concurrents appartenant au meme club
	 */
	public Concurrent[] list(String compagnie, DifferentiationCriteria differentiationCriteria) {

        //buffer de selection
        ArrayList<Concurrent> sel = new ArrayList<Concurrent>();

        //recherche
		for(int i = 0; i < archList.size(); i++) {
			if(archList.get(i).getClub().equals(compagnie)
					&& (differentiationCriteria == null || 
                            differentiationCriteria.equals(archList.get(i).getDifferentiationCriteria())))
				sel.add(this.archList.get(i));
		}
        
		return sel.toArray(new Concurrent[sel.size()]);
	}

	/**
	 * Extrait la liste des concurrents pour une categorie donnée
	 * 
	 * @param differentiationCriteria - la categorie de filtrage
	 * 
	 * @return Concurrent[] les concourrents appartenant à la categorie de filtrage
	 */
	public Concurrent[] list(DifferentiationCriteria differentiationCriteria) {
        ArrayList<Concurrent> sel = new ArrayList<Concurrent>();
        
		for(int i=0; i < this.archList.size(); i++) {
			if(differentiationCriteria.equals(this.archList.get(i).getDifferentiationCriteria()))
				sel.add(this.archList.get(i));
		}

		return sel.toArray(new Concurrent[sel.size()]);
	}
	
	/**
	 * Extrait les archers present sur la même cible
	 * 
	 * @param cible
	 * @return Concurrent[] - la liste des archers présent sur la meme cible
	 */
	public Concurrent[] list(int cible) {
		ArrayList<Concurrent> tmp = new ArrayList<Concurrent>();
		for(int i=0; i < this.archList.size(); i++) {
			if(this.archList.get(i).getCible() == cible)
				tmp.add(this.archList.get(i));
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
						String namei = sort_list[i].getClub();
						String namej = sort_list[j].getClub();
						if(namej.compareToIgnoreCase(namei) < 0) {
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
	 * @return String[]
	 */
	public String[] listCompagnie() {
		ArrayList<String> alCie = new ArrayList<String>();
		
		for(Concurrent concurrent: archList) {
			boolean add = true;
			for(String dbtemp : alCie) {
				if(dbtemp.equals(concurrent.getClub())) {
					add = false;
					break;
				}
			}
			if(add) {
				alCie.add(concurrent.getClub());
			}
		}
		
		int number_of_element = alCie.size();

		return alCie.toArray(new String[number_of_element]);
	}
    
    /**
     * test si la ArcherList contient le concurrent donnée en parametre
     * 
     * @param concurrent - le concurrent à tester
     * @return boolean - true si concurrent est présent
     */
    public boolean contains(Concurrent concurrent) {
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
	 * Donne le nombre de concurrent pour la compagnie donne
	 * 
	 * @param compagnie
	 * @return int
	 */
	public int countArcher(String compagnie) {
		return list(compagnie, null).length;
	}
	
	/**
	 * Donne le nombre de club sur le concours
	 * 
	 * @return int
	 */
	public int countCompagnie() {
		return listCompagnie().length;
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
	 * @param cible - la cible ou se trouve le concurrent à recuperer
     * @param position - la position ou se trouve le concurrent
	 * @return Concurrent - le concurrent à la position ou null si aucun
	 */
	public Concurrent getConcurrentAt(int cible, int position) {
		for(int i = 0; i < this.archList.size(); i++)
			if(this.archList.get(i).getCible() == cible && this.archList.get(i).getPosition() == position)
				return this.archList.get(i);
		return null;
	}

    /**
     * <i>Methode necessaire à la deserialisation</i>
     * Donne la liste des archers à l'objet
     * 
     * @return Returns the archList.
     */
    public ArrayList<Concurrent> getArchList() {
        return this.archList;
    }

    /**
     * <i>Methode necessaire à la deserialisation</i>
     * Affecte la liste des archers à l'objet
     * 
     * @param archList The archList to set.
     */
    public void setArchList(ArrayList<Concurrent> archList) {
        this.archList = archList;
    }

    /**
     * @return Renvoie ficheConcours.
     */
    public FicheConcours getFicheConcours() {
        return ficheConcours;
    }

    /**
     * @param ficheConcours - ficheConcours à définir.
     */
    public void setFicheConcours(FicheConcours ficheConcours) {
        this.ficheConcours = ficheConcours;
    }
}