/**
 * 
 */
package org.concoursjeunes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlAttachmentRef;

/**
 * @author Aurélien JEOFFRAY - Fiducial Informatique
 *
 */
public class PasDeTir {
	private ArrayList<Cible> targets = new ArrayList<Cible>();
	private FicheConcours ficheConcours;
	private int depart = 0;
	
	public PasDeTir(FicheConcours ficheConcours, int depart) {
		this.ficheConcours = ficheConcours;
		this.depart = depart;
		
		for(int j = 0; j < ficheConcours.getParametre().getNbCible(); j++) {
			Cible cible = new Cible(j+1, ficheConcours);
			if(ficheConcours.getConcurrentList().countArcher(depart) > 0) {
				for(Concurrent concurrent : ficheConcours.getConcurrentList().list(j + 1, depart))
					cible.setConcurrentAt(concurrent, concurrent.getPosition());
			}
			targets.add(cible);
		}
	}
	
	/**
	 * calcul la place occupé et disponible sur le concours
	 *
	 * @param depart - le départ pour lequelle tester la place dispo
	 * @return la table d'occupation pour le depart
	 */
	private Hashtable<DistancesEtBlason, OccupationCibles> calculatePlaceDisponible() {
		Hashtable<DistancesEtBlason, OccupationCibles> occupationCibles = new Hashtable<DistancesEtBlason, OccupationCibles>();

		//recupere le nombre d'archer total possible
		int placeLibreSurCible = 0;
		int[] nbParDistanceBlason;

		//recupere dans la configuration la correspondance Critères de distinction/Distance-Blason
		Hashtable<CriteriaSet, DistancesEtBlason> correspSCNA_DB = ficheConcours.getParametre().getReglement().getCorrespondanceCriteriaSet_DB();

		//recuper les clés de placement
		Enumeration<DistancesEtBlason> dbEnum = correspSCNA_DB.elements();
		ArrayList<DistancesEtBlason> distancesEtBlasons = new ArrayList<DistancesEtBlason>();
		for(int i = 0; dbEnum.hasMoreElements(); i++) {
			DistancesEtBlason db = dbEnum.nextElement();
			if(!distancesEtBlasons.contains(db))
				distancesEtBlasons.add(db);
		}

		//liste le nombre d'acher par distances/blason différents
		//pour chaque distance/blason
		nbParDistanceBlason = new int[distancesEtBlasons.size()];
		int i = 0;
		for(DistancesEtBlason distblas : distancesEtBlasons) {

			nbParDistanceBlason[i] = ficheConcours.getConcurrentList().countArcher(ficheConcours.getParametre().getReglement(), distblas, depart);

			placeLibreSurCible = (ficheConcours.getParametre().getNbTireur() - nbParDistanceBlason[i] % ficheConcours.getParametre().getNbTireur()) 
			% ficheConcours.getParametre().getNbTireur();

			occupationCibles.put(distblas, new OccupationCibles(placeLibreSurCible, nbParDistanceBlason[i]));

			i++;
		}

		return occupationCibles;
	}
	
	/**
	 * Retourne la table d'occupation des cibles pour un départ donné
	 * 
	 * @param depart - le depart concerné
	 * @return la table d'occupation des cibles
	 */
	public Hashtable<DistancesEtBlason, OccupationCibles> getOccupationCibles() {
		return calculatePlaceDisponible();
	}
	
	/**
	 * Retourne le nombre de cible libre sur un depart donné
	 * 
	 * @param depart - le depart pour lequelle retourné le nombre de cible libre
	 * @return le nombre de cible libre
	 */
	public int getNbCiblesLibre() {
		int nbCibleOccupe = 0;
		//deduit le nombre de cible libre de la table d'ocupation des cibles
		Hashtable<DistancesEtBlason, OccupationCibles> occupationCibles = getOccupationCibles();
		//decompte les cibles occupés
		Enumeration<OccupationCibles> ocEnum = occupationCibles.elements();
		while(ocEnum.hasMoreElements()) {
			OccupationCibles oc = ocEnum.nextElement();

			nbCibleOccupe += (oc.getPlaceOccupe() + oc.getPlaceLibre()) / ficheConcours.getParametre().getNbTireur();
		}
		//nb cible total - nb cible occupe = nb cible libre
		return ficheConcours.getParametre().getNbCible() - nbCibleOccupe;
	}
	
	/**
	 * Détermine, pour un concurrent donnée, si une place est diponible sur le pas de tir
	 * 
	 * @param concurrent - le concuurent à tester
	 * @return true si une place est disponible pour le concurrent, false sinon
	 */
	public boolean havePlaceForConcurrent(Concurrent concurrent) {
		OccupationCibles place = null;
		if(!ficheConcours.getConcurrentList().contains(concurrent)) {
			place = getOccupationCibles().get(
					DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), concurrent));

			return place.getPlaceLibre() > 0 || getNbCiblesLibre() > 0;
		}

		int index = ficheConcours.getConcurrentList().getArchList().indexOf(concurrent);
		Concurrent conc2 = ficheConcours.getConcurrentList().get(index);

		DistancesEtBlason db1 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), concurrent);
		DistancesEtBlason db2 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), conc2);

		//si on ne change pas de db pas de pb
		if(db1.equals(db2)) {
			return true;
		}

		//si il reste de la place dans la nouvelle categorie pas de pb
		place = getOccupationCibles().get(db1);
		if(place.getPlaceLibre() > 0 || getNbCiblesLibre() > 0) {
			return true;
		}

		//si le retrait du concurrent libere une cible ok
		place = getOccupationCibles().get(db2);
		if(place.getPlaceOccupe() % ficheConcours.getParametre().getNbTireur() == 1) {
			return true;
		}

		//sinon changement impossible
		return false;
	}
	
	/**
	 * Place les archers sur le pas de tir
	 *
	 * @param depart - le numero du depart pour lequel placer les archers
	 */
	public void placementConcurrents() {
		int curCible = 1;

		//pour chaque distance/blason
		for(DistancesEtBlason distancesEtBlason : ficheConcours.getConcurrentList().listDistancesEtBlason(ficheConcours.getParametre().getReglement(), true, depart)) {
			//liste les archers pour le distance/blason
			Concurrent[] concurrents = ConcurrentList.sort( ficheConcours.getConcurrentList().list(ficheConcours.getParametre().getReglement(), distancesEtBlason, depart), ConcurrentList.SORT_BY_CLUBS);

			//defini le nombre de tireur par cible en fonction du nombre de tireurs
			//max acceptés et du nombre de tireur présent
			//FIXME calculer le taux d'occupation des cibles en fonction de chaque d/b afin d'eviter les effets de bord
			int nbTireurParCible = ficheConcours.getParametre().getNbTireur();
			for(int i = 2; i <= ficheConcours.getParametre().getNbTireur(); i+=2) {
				if( ficheConcours.getConcurrentList().countArcher(depart) <= ficheConcours.getParametre().getNbCible() * i) {
					nbTireurParCible = i;
				}
			}

			int startCible = curCible;
			int endCible = curCible + (concurrents.length / nbTireurParCible) 
					+ (((concurrents.length % nbTireurParCible) > 0) ? 0 : -1);

			for(int j = 0; j < concurrents.length; j++) {
				if(concurrents[j].getCible() > 0)
					targets.get(concurrents[j].getCible()-1).removeConcurrent(concurrents[j]);
				targets.get(curCible - 1).insertConcurrent(concurrents[j]);

				if(curCible < endCible)
					curCible++;
				else {
					curCible = startCible;
				}
			}

			curCible = endCible + 1;
		}
	}

	/**
	 * Place un concurrent sur une cible donné à une place donnée
	 * 
	 * @param concurrent - le concurrent à placer
	 * @param cible - la cible sur laquelle placer le concurrent
	 * @param position - la position du concurrent sur la cible
	 * @return true si placé avec succé, false sinon
	 */
	public boolean placementConcurrent(Concurrent concurrent, Cible cible, int position) {

		//si le concurrent était déjà placé sur le pas de tir aupparavant le retirer
		if(concurrent.getCible() > 0)
			targets.get(concurrent.getCible()-1).removeConcurrent(concurrent);

		//tenter le placement
		boolean success = cible.setConcurrentAt(concurrent, position);

		return success;
	}
	
	public void retraitConcurrent(Concurrent concurrent) {
		if(concurrent.getCible() > 0)
			targets.get(concurrent.getCible()-1).removeConcurrent(concurrent);
	}

	/**
	 * @return targets
	 */
	public ArrayList<Cible> getTargets() {
		return targets;
	}

	/**
	 * @param targets targets à définir
	 */
	public void setTargets(ArrayList<Cible> targets) {
		this.targets = targets;
	}
}
