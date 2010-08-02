/*
 * Créé le 02/03/2007 à 17:36 pour ConcoursJeunes
 *
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.ajdeveloppement.concours.exceptions.PlacementException;
import org.concoursjeunes.Target.Repartition;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.event.PasDeTirListener;

/**
 * <p>
 * Représente le pas de tir d'un départ de concours. Un pas de tir est associé à un concours
 * et à un départ donné. Il liste l'ensemble des cibles (et leurs archers associé) présentes
 * sur celui-ci.
 * </p>
 * <p>
 * La class <i>ShootingLine</i> va fournir des méthodes permettant de placer un archer sur le pas
 * de tir du concours ainsi que des méthodes permettant de calculer l'occupation de celui-ci et
 * bloquer l'insertion en cas de dépassement.
 * </p>
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 *
 */
public class ShootingLine implements FicheConcoursListener {
	private List<Target> targets = new ArrayList<Target>();
	private List<Target> simulationTargets = new ArrayList<Target>();

	private FicheConcours ficheConcours;
	private int depart = 0;
	
	private EventListenerList listeners = new EventListenerList();
	
	/**
	 * Construit un nouveau pas de tir pour le concours et le dépar donné
	 * 
	 * @param ficheConcours le concours associé au pas de tir
	 * @param depart le depart associé au pas de tir
	 */
	public ShootingLine(FicheConcours ficheConcours, int depart) {
		this.ficheConcours = ficheConcours;
		this.depart = depart;
		
		ficheConcours.addFicheConcoursListener(this);
		
		//construit l'arbre de cible lié au pas de tir
		for(int j = 0; j < ficheConcours.getParametre().getNbCible(); j++) {
			Target cible = new Target(j+1, ficheConcours.getParametre().getReglement(), ficheConcours.getParametre().getNbTireur());
			//dans le cas d'une ouverture de concours existant, place les archers réferencé
			//sur leurs cible
			if(ficheConcours.getConcurrentList().countArcher(depart) > 0) {
				for(Concurrent concurrent : ficheConcours.getConcurrentList().list(j + 1, depart))
					try {
						cible.setConcurrentAt(concurrent, concurrent.getPosition());
					} catch (PlacementException e) {
						e.printStackTrace(); // ne devrait jamais se produire. Laisse une trace dans le cas contraire
					}
			}
			targets.add(cible);
			simulationTargets.add(new Target(j+1, ficheConcours.getParametre().getReglement(), ficheConcours.getParametre().getNbTireur()));
		}
	}
	
	public void addPasDeTirListener(PasDeTirListener listener) {
		listeners.add(PasDeTirListener.class, listener);
	}
	
	public void removePasDeTirListener(PasDeTirListener listener) {
		listeners.remove(PasDeTirListener.class, listener);
	}
	
	/**
	 * défini le nombre de tireur par cible en fonction du nombre de tireurs
	 * max acceptés et du nombre de tireur présent
	 * 
	 * @param lDB la liste des DistancesEtBlason utilisé sur le pas de tir
	 * @return le nombre de tireur par cible à utiliser
	 */
	private int getOptimalRythme(List<DistancesEtBlason> lDB) {
		for(int i = 2; i <= ficheConcours.getParametre().getNbTireur(); i+=2) {
			Map<DistancesEtBlason, TargetsOccupation> targetsOccupation = getTargetsOccupation(i);
			if(targetsOccupation != null)
				return i;
		}
		
		return 0;
	}
	
	/**
	 * Retourne la table d'occupation en fonction du nombre de tireur par cible donné
	 * 
	 * @param nbtireurparcible le nombre de tireur par cible servant de base au calcul de la table d'occupation
	 * @return la table d'occupation des cibles
	 */
	public Map<DistancesEtBlason, TargetsOccupation> getTargetsOccupation(int nbtireurparcible) {
		Map<DistancesEtBlason, TargetsOccupation> occupationCibles = new HashMap<DistancesEtBlason, TargetsOccupation>();
		List<DistancesEtBlason> distancesEtBlasons = ficheConcours.getParametre().getReglement().getListDistancesEtBlason();
		
		//effectue une simulation de placement
		boolean success = placementConcurrents(nbtireurparcible, true);
		if(!success)
			return null;
		
		//boucle sur chacune des cibles de la simulation pour en extraire le résultat
		for(Target target : simulationTargets) {
			for(DistancesEtBlason distblas : distancesEtBlasons) {
				int nbArcher = target.getNbArcherFor(distblas); //place occupé sur le db
				int placeLibre = target.getNbAvailableSlotsFor(distblas); //place libre sur le db
				
				if(occupationCibles.get(distblas) == null)
					occupationCibles.put(distblas, new TargetsOccupation(0, 0));
				occupationCibles.get(distblas).setPlaceOccupe(occupationCibles.get(distblas).getPlaceOccupe() + nbArcher);
				occupationCibles.get(distblas).setPlaceLibre(occupationCibles.get(distblas).getPlaceLibre() + placeLibre);
			}
			
		}

		return occupationCibles;
	}
	
	/**
	 * Retourne le nombre de cible libre en fonction du nombre de tireur par cible donné
	 * 
	 * @param nbtireurparcible le nombre de tireur par cible servant de base au calcul de la table d'occupation
	 * @return le nombre de cible libre
	 */
	public int getNbFreeTargets(int nbtireurparcible) {
		int nbCibleOccupe = 0;
		
		if(placementConcurrents(nbtireurparcible, true)) {
			for(Target target : simulationTargets) {
				if(target.getNbArcher() > 0)
					nbCibleOccupe++;
			}
	
			//nb cible total - nb cible occupe = nb cible libre
			return ficheConcours.getParametre().getNbCible() - nbCibleOccupe;
		}
		return 0;
	}
	
	/**
	 * Détermine, pour un concurrent donnée, si une place est disponible sur le pas de tir
	 * 
	 * @param concurrent - le concurrent à tester
	 * @return true si une place est disponible pour le concurrent, false sinon
	 */
	public boolean havePlaceForConcurrent(Concurrent concurrent) {
		TargetsOccupation place = null;
		if(!ficheConcours.getConcurrentList().contains(concurrent, concurrent.getDepart())) { // vérifie qu'il ne soit pas déjà sur le pas de tir
			DistancesEtBlason distancesEtBlason = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), concurrent);
			if(distancesEtBlason == null)
				return false;
			
			place = getTargetsOccupation(ficheConcours.getParametre().getNbTireur()).get(distancesEtBlason);

			return place.getPlaceLibre() > (concurrent.isHandicape()?1:0) || getNbFreeTargets(ficheConcours.getParametre().getNbTireur()) > 0;
		}

		int index = ficheConcours.getConcurrentList().getArchList().indexOf(concurrent);
		Concurrent conc2 = ficheConcours.getConcurrentList().get(index);

		DistancesEtBlason db1 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), concurrent);
		DistancesEtBlason db2 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), conc2);

		//si on ne change pas de db pas de pb
		//et que l'archer ne devient pas handicapé ;)
		if(db1.equals(db2) && concurrent.isHandicape() == conc2.isHandicape()) {
			return true;
		}

		//si il reste de la place dans la nouvelle catégorie pas de pb
		place = getTargetsOccupation(ficheConcours.getParametre().getNbTireur()).get(db1);
		if(place.getPlaceLibre() > (concurrent.isHandicape()?1:0) || getNbFreeTargets(ficheConcours.getParametre().getNbTireur()) > 0) {
			return true;
		}

		//si le retrait du concurrent libère une cible ok
		place = getTargetsOccupation(ficheConcours.getParametre().getNbTireur()).get(db2);
		if(place.getPlaceOccupe() % ficheConcours.getParametre().getNbTireur() == (concurrent.isHandicape()?2:1)) {
			return true;
		}

		//sinon changement impossible
		return false;
	}
	
	/**
	 * Place les archers sur le pas de tir
	 * La méthode de placement utilisé permet d'éviter, dans la mesure du possible,
	 * de placer les archers d'un même club sur la même cible
	 */
	public void placementConcurrents() {
		placementConcurrents(ficheConcours.getParametre().getNbTireur(), false);
	}
	
	/**
	 * Place les archers sur le pas de tir
	 * La méthode de placement utilisé permet d'éviter, dans la mesure du possible,
	 * de placer les archers d'un même club sur la même cible
	 * 
	 * @param nbtireurparcible le nombre de tireur par cible à considérer dans le cas
	 * s'une simulation. Si on est pas en simulation, la valeur est ignoré
	 * 
	 * @param simulationMode si <i>true</i> se contente de simulé le placement sans
	 * le réaliser concrètement en plaçant les archers dans simulationTargets en lieu
	 * et place de targets. Le but est simplement de déterminer la place disponible restante
	 * 
	 * @return true si le placement est effectué avec succès, false si le placement est
	 * impossible
	 */
	private boolean placementConcurrents(int nbtireurparcible, boolean simulationMode) {
		int curCible = 1;
		List<DistancesEtBlason> lDB = ficheConcours.getConcurrentList().listDistancesEtBlason(ficheConcours.getParametre().getReglement(), true, depart);
		
		//defini le nombre de tireur par cible en fonction du nombre de tireurs
		//max acceptés et du nombre de tireur présent
		int nbTireurParCible = nbtireurparcible;
		if(!simulationMode)
			nbTireurParCible = getOptimalRythme(lDB);
		
		if(nbTireurParCible == 0)
			return false;
		
		List<Target> currentTargetsTable;
		if(simulationMode)
			currentTargetsTable = simulationTargets;
		else
			currentTargetsTable = targets;
		
		for(Target cible : currentTargetsTable) {
			cible.removeAll(simulationMode);
		}
		
		//pour chaque distance/blason 
		for(DistancesEtBlason distancesEtBlason : lDB) {
			//liste les archers pour le distance/blason
			List<Concurrent> concurrents = ConcurrentList.sort(
					ficheConcours.getConcurrentList().list(distancesEtBlason, depart, false),
					ConcurrentList.SortCriteria.SORT_BY_CLUBS);

			//determine le nombre de concurrent pour la distance modéré avec les archers handicapé
			// (un archer handicapé compte pour 2 personnes dans le placement sur pas de tir)
			int nbConcurrent = ficheConcours.getConcurrentList().countArcher(distancesEtBlason, depart, true);
			//calcul le nombre et la position des cibles qui vont être occupé pour la distance
			int startCible = curCible;
			int endCible = curCible + (int)Math.ceil((double)nbConcurrent / (double)nbTireurParCible) - 1;
			
			//calcul le nombre de slot à occuper sur la dernière cible
			int nbArcherOnFirstTarget = (currentTargetsTable.get(startCible - 1).getNbArcher() + currentTargetsTable.get(startCible - 1).getNbHandicap());
			if(nbArcherOnFirstTarget > 0) {
				int nbArcherOnLastTarget = nbConcurrent % nbTireurParCible;
				int maxAvailableFreeTarget = nbTireurParCible - nbArcherOnFirstTarget;
				int availableFreeTarget = currentTargetsTable.get(startCible - 1).getNbAvailableSlotsFor(distancesEtBlason);
				if(availableFreeTarget > maxAvailableFreeTarget)
					availableFreeTarget = maxAvailableFreeTarget;
				nbArcherOnLastTarget = ((nbArcherOnLastTarget==0) ? nbTireurParCible : nbArcherOnLastTarget) - availableFreeTarget;
				if(nbArcherOnLastTarget > 0)
					endCible += 1;
			}
			
			if(endCible > ficheConcours.getParametre().getNbCible())
				return false;
			
			if(nbConcurrent > concurrents.size()) { //si on a des archers handicapé dans le groupe
				//extraire les archers handicapé pour les placer en premier
				//afin d'éviter d'avoir des problèmes pour les placer
				ArrayList<Concurrent> concurrentsHandicape = new ArrayList<Concurrent>();
				for(Concurrent concurrent : concurrents) {
					if(concurrent.isHandicape()) {
						concurrentsHandicape.add(concurrent);
					}
				}
				
				//place les archers handicapé
				for(Concurrent concurrent : concurrentsHandicape) {
					curCible = placementConcurrent(concurrent, startCible, curCible, endCible, nbTireurParCible, simulationMode);
				}
			}
			
			//place les archers valide
			for(Concurrent concurrent : concurrents) {
				if(!concurrent.isHandicape()) {
					curCible = placementConcurrent(concurrent, startCible, curCible, endCible, nbTireurParCible, simulationMode);
				}
			}

			//int occupation = targets.get(endCible - 1).getNbArcher() + targets.get(endCible - 1).getNbHandicap();
			
			//passe au bloc suivant
			curCible = endCible;
		}
		
		firePasDeTirChanged();
		
		return true;
	}
	
	private int placementConcurrent(Concurrent concurrent, int startTarget, int curTarget, int endTarget, int nbTireurParCible, boolean simulationMode) {
		int position = -1;
		int origCurTarget = curTarget;
		List<Target> currentTargetsTable;
		if(simulationMode)
			currentTargetsTable = simulationTargets;
		else
			currentTargetsTable = targets;
		
		do {
			if(currentTargetsTable.get(curTarget - 1).getNbArcher() + currentTargetsTable.get(curTarget - 1).getNbHandicap() < nbTireurParCible) {
				try {
					Repartition repartition;
					if(nbTireurParCible == 2)
						repartition = Repartition.ABCD;
					else
						repartition = Repartition.ACBD;
					position = currentTargetsTable.get(curTarget - 1).insertConcurrent(concurrent, repartition, simulationMode);
				} catch (PlacementException e) { }
			}
			if(curTarget < endTarget)
				curTarget++;
			else if(origCurTarget > startTarget) {
				origCurTarget = startTarget;
				curTarget = startTarget;
			} else {
				break;
			}
		} while(position == -1 && curTarget <= endTarget);
		
		return curTarget;
	}

	/**
	 * Place un concurrent sur une cible donné à une place donnée
	 * 
	 * @param concurrent - le concurrent à placer
	 * @param cible - la cible sur laquelle placer le concurrent
	 * @param position - la position du concurrent sur la cible
	 */
	public void placementConcurrent(Concurrent concurrent, Target cible, int position) 
			throws PlacementException {

		int oldTarget = -1;
		int oldPosition = -1;
		//si le concurrent était déjà placé sur le pas de tir auparavant le retirer
		if(concurrent.getCible() > 0) {
			oldTarget = concurrent.getCible()-1;
			oldPosition = concurrent.getPosition();
			targets.get(oldTarget).removeConcurrent(concurrent);
		}

		//tenter le placement
		try {
			cible.setConcurrentAt(concurrent, position);
		} catch (PlacementException e) {
			//replace le concurrent à son emplacement d'origine en cas d'échec
			if(oldTarget > -1 && oldPosition > -1)
				targets.get(oldTarget).setConcurrentAt(concurrent, oldPosition);
			throw e;
		}
		
		firePasDeTirChanged();
	}
	
	/**
	 * Recherche et retire un concurrent du pas de tir
	 * 
	 * @param concurrent le concurrent à retirer
	 */
	public void retraitConcurrent(Concurrent concurrent) {
		if(concurrent.getCible() > 0) {
			targets.get(concurrent.getCible()-1).removeConcurrent(concurrent);
			
			firePasDeTirChanged();
		}
	}

	/**
	 * Retourne la liste des cibles lié au pas de tir
	 * 
	 * @return la liste des cibles lié au pas de tir
	 */
	public List<Target> getTargets() {
		return targets;
	}

	/**
	 * Retourne la fiche concours a laquel est lié le pas de tir
	 * 
	 * @return ficheConcours la fiche concours a laquel est lié le pas de tir
	 */
	public FicheConcours getFicheConcours() {
		return ficheConcours;
	}
	
	/**
	 * Retourne le départ représenté par le pas de tir
	 * 
	 * @return depart le départ représenté par le pas de tir
	 */
	public int getDepart() {
		return depart;
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.event.FicheConcoursListener#listConcurrentChanged(org.concoursjeunes.event.FicheConcoursEvent)
	 */
	@Override
	public void listConcurrentChanged(FicheConcoursEvent e) {
		if(e.getEvent() == FicheConcoursEvent.REMOVE_CONCURRENT) {
			if(e.getConcurrent().getDepart() == depart)
				retraitConcurrent(e.getConcurrent());
		}
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.event.FicheConcoursListener#pasDeTirChanged(org.concoursjeunes.event.FicheConcoursEvent)
	 */
	@Override
	public void pasDeTirChanged(FicheConcoursEvent e) {
		
	}
	
	private void firePasDeTirChanged() {
		for(PasDeTirListener listener : listeners.getListeners(PasDeTirListener.class)) {
			listener.pasdetirChanged();
		}
	}
}
