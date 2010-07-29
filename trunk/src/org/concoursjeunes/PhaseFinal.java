/*
 * Créé le 16 févr. 2010 à 22:23:32 pour ConcoursJeunes
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.concours.RepartitionFinals;
import org.ajdeveloppement.concours.managers.RepartitionFinalsManager;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;


/**
 * Gestion de la phase finale
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class PhaseFinal implements PropertyChangeListener,FicheConcoursListener {
	
	private FicheConcours ficheConcours;
	private Classement classement;
	private List<RepartitionFinals> repartitionsFinals = new ArrayList<RepartitionFinals>();
	
	/**
	 * Initialise une phase final pour la fiche concours fournit en paramètre
	 * 
	 * @param ficheConcours
	 */
	public PhaseFinal(FicheConcours ficheConcours) {
		this.ficheConcours = ficheConcours;
		
		try {
			repartitionsFinals = RepartitionFinalsManager.getRepartitionFinals(RepartitionFinals.TYPE_INDIV_FRANCAIS);
		} catch (ObjectPersistenceException e) {
			throw new UncheckedException(e);
		}
		
		refreshClassement();
		for(Concurrent concurrent : ficheConcours.getConcurrentList().list(-1)) {
			concurrent.addPropertyChangeListener(this);
		}
	}
	
	/**
	 * Donne le nombre d'archer possible sur une phase
	 * 
	 * @param phase le numéro de la phase pour laquel retourner le nombre d'archer
	 * @return le nombre d'archer possible sur la phase
	 */
	public static int getNombreArcherPhase(int phase) {
		return (int)Math.pow(2, phase); 
	}
	
	/**
	 * Donne le nombre de duel sur la phase
	 * 
	 * @param phase la phase pour laquelle retourner le nombre de duel
	 * @return le nombre de duel
	 */
	public static int getNombreDuelPhase(int phase) {
		return getNombreArcherPhase(phase) / 2;
	}
	
	/**
	 * Retourne la liste des catégories utilisé sur la phase final
	 * 
	 * @return collection de jeux de critères représenté sur les phases finales
	 */
	public List<CriteriaSet> getCriteriaSetPhasesFinal() {
		List<CriteriaSet> csUse = new ArrayList<CriteriaSet>(classement.getClassementPhaseQualificative().keySet());
		
		CriteriaSet.sortCriteriaSet(csUse, ficheConcours.getParametre().getReglement().getListCriteria());
		
		return csUse;
	}
	
	/**
	 * Retourne le nombre de phase possible (max 6 pour les 32éme de final) pour une catégorie
	 * en fonction du nombre de concurrent sur celle ci
	 * 
	 * @param categorie la catégorie pour laquelle retourner le nombre de phase possible
	 * @return nombre de phase jusqu'à la phase final
	 */
	public int getNombrePhase(CriteriaSet categorie) {
		assert classement != null;
		
		int nbPhase = 0;
		
		List<Concurrent> concurrents = classement.getClassementPhaseQualificative().get(categorie);
		
		if(concurrents != null) {
			int nbConcurrentCategorie = concurrents.size();
			if(nbConcurrentCategorie >= 64) {
				nbPhase = 6;
			} else if(nbConcurrentCategorie >= 32) {
				nbPhase = 5;
			} else if(nbConcurrentCategorie >= 16) {
				nbPhase = 4;
			} else if(nbConcurrentCategorie >= 8) {
				nbPhase = 3;
			} else if(nbConcurrentCategorie >= 4) {
				nbPhase = 2;
			} else if(nbConcurrentCategorie >= 2) {
				nbPhase = 1;
			}
		}
		
		return nbPhase;
	}
	
	/**
	 * Retourne un duel
	 * 
	 * @param categorie la catégorie de classement associé au duel
	 * @param phase le numéro de la phase du duel
	 * @param numDuel le numéro de duel dans la phase
	 * @return le duel souhaité
	 */
	public Duel getDuel(CriteriaSet categorie, int phase, int numDuel) {
		//on vérifie que le numéro de du duel est possible sur la phase
		if(numDuel < 0 || numDuel >= getNombreDuelPhase(phase))
			return null;
		
		//on vérifie que la phase est possible pour cette catégorie
		if(phase > getNombrePhase(categorie))
			return null;
		
		return getDuelsPhase(categorie, phase).get(numDuel);
	}
	
	/**
	 * Retourne tous les duels d'une phase
	 * 
	 * @param categorie la catégorie des duels
	 * @param phase la phase
	 * @return les duels de la phase ou null si pas de duel sur cette phase pour cette catégorie.<br>
	 *         La liste des duels est ordonnées
	 */
	public List<Duel> getDuelsPhase(CriteriaSet categorie, int phase) {
		int nombreTotalPhase = getNombrePhase(categorie);
		//on vérifie que la phase est possible pour cette catégorie
		if(phase > nombreTotalPhase)
			return null;
		
		List<Duel> duels = new ArrayList<Duel>();
		
		List<RepartitionFinals> repartitionFinals = RepartitionFinals.getRepartitionFinalsPhase(repartitionsFinals, nombreTotalPhase);
		//Liste des concurrents en pĥase max 
		List<Concurrent> concurrents = classement.getClassementPhaseQualificative().get(categorie);
		//production des duels de phase max
		for(int i = 0; i < getNombreDuelPhase(nombreTotalPhase); i++) {
			duels.add(new Duel(
					concurrents.get(repartitionFinals.get(i*2).getNumeroOrdre()-1),
					concurrents.get(repartitionFinals.get((i*2)+1).getNumeroOrdre()-1),
					nombreTotalPhase, i+1
					));
		}
		
		if(phase < nombreTotalPhase) {
			Concurrent concurrent1 = null;
			Concurrent concurrent2 = null;
			
			for(int i = nombreTotalPhase - 1; i >= phase; i--) {
				List<Duel> duelsPhasePrecedente = duels;
				duels = new ArrayList<Duel>();
				
				int numDuel = 1;
				for(Duel duel : duelsPhasePrecedente) {
					if(concurrent1 == null)
						concurrent1 = duel.getWinner();
					else {
						concurrent2 = duel.getWinner();
						
						duels.add(new Duel(concurrent1, concurrent2, i, numDuel++));
						
						concurrent1 = null;
						concurrent2 = null;
					}
				}
			}
		}
		
		return duels;
	}

	/**
	 * Retourne le classement de phase finale de la catégorie fournit en paramètre
	 * 
	 * @param categorie la catégorie pour laquelle fournir le classement
	 * @return le classement de phase finale de la catégorie
	 */
	public List<Concurrent> getClassement(CriteriaSet categorie) {
		List<Concurrent> concurrents = new ArrayList<Concurrent>();
		
		for(int i = 0; i < getNombrePhase(categorie); i++) {
			List<Duel> duels = getDuelsPhase(categorie, i);
			if(i == 0 && duels.size() > 0) {//Finale
				concurrents.add(duels.get(0).getWinner());
				concurrents.add(duels.get(0).getLooser());
			} else {
				List<Concurrent> perdants = new ArrayList<Concurrent>();
				for(Duel duel : duels) {
					if(duel.getLooser() != null)
						perdants.add(duel.getLooser());
				}
				if(perdants.size() > 0) {
					final int phase = i;
					Collections.sort(perdants, new Comparator<Concurrent>() {
						@Override
						public int compare(Concurrent o1, Concurrent o2) {
							return o1.compareScorePhaseFinalWith(o2, phase);
						}
					});
					concurrents.addAll(perdants);
				}
			}
		}
		return concurrents;
	}
	
	/**
	 * Rafraîchit le classement pour travailler sur des données à jour
	 */
	private void refreshClassement() {
		classement = ficheConcours.getConcurrentList().classement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getPropertyName().equals("score") || e.getPropertyName().equals("criteriaSet")) //$NON-NLS-1$ //$NON-NLS-2$
			refreshClassement();
	}

	@Override
	public void listConcurrentChanged(FicheConcoursEvent e) {
		if(e.getEvent() == FicheConcoursEvent.ADD_CONCURRENT) {
			e.getConcurrent().addPropertyChangeListener(this);
		} else {
			e.getConcurrent().removePropertyChangeListener(this);
		}
	}

	@Override
	public void pasDeTirChanged(FicheConcoursEvent e) {
	}
}
