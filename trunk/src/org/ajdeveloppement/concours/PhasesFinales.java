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
package org.ajdeveloppement.concours;

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ajdeveloppement.commons.AJTemplate;
import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.concours.managers.RepartitionFinalsManager;
import org.concoursjeunes.Classement;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.localisable.CriteriaSetLibelle;


/**
 * Gestion de la phase finale
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class PhasesFinales implements PropertyChangeListener,FicheConcoursListener {
	
	private FicheConcours ficheConcours;
	private Classement classement;
	private List<RepartitionFinals> repartitionsFinals = new ArrayList<RepartitionFinals>();
	
	private transient AJTemplate templateClassementHTML;
	
	/**
	 * Initialise une phase final pour la fiche concours fournit en paramètre
	 * 
	 * @param ficheConcours
	 */
	public PhasesFinales(FicheConcours ficheConcours) {
		this.ficheConcours = ficheConcours;
		
		templateClassementHTML = new AJTemplate(ficheConcours.getProfile().getLocalisation());
		try {
			loadTemplates();
		} catch (UnsupportedEncodingException e1) {
			throw new UncheckedException(e1);
		} catch (FileNotFoundException e1) {
			throw new UncheckedException(e1);
		} catch (IOException e1) {
			throw new UncheckedException(e1);
		}
		
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
		return (int)Math.pow(2, phase+1); 
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
		if(phase >= getNombrePhase(categorie))
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
		if(phase >= nombreTotalPhase)
			return null;
		
		List<Duel> duels = new ArrayList<Duel>();
		
		List<RepartitionFinals> repartitionFinals = RepartitionFinals.getRepartitionFinalsPhase(repartitionsFinals, nombreTotalPhase-1);
		//Liste des concurrents en pĥase max 
		List<Concurrent> concurrents = classement.getClassementPhaseQualificative().get(categorie);
		//production des duels de phase max
		for(int i = 0; i < getNombreDuelPhase(nombreTotalPhase-1); i++) {
			duels.add(new Duel(
					concurrents.get(repartitionFinals.get(i*2).getNumeroOrdre()-1),
					concurrents.get(repartitionFinals.get((i*2)+1).getNumeroOrdre()-1),
					nombreTotalPhase-1, i+1
					));
		}
		
		List<Duel> duelsPhasePrecedente = null;
		
		if(phase < nombreTotalPhase-1) {
			Concurrent concurrent1 = null;
			Concurrent concurrent2 = null;
			
			for(int i = nombreTotalPhase - 2; i >= phase; i--) {
				duelsPhasePrecedente = duels;
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
		
		//Si il y a matière à réaliser une petite finale
		//alors on l'ajoute
		if(phase == 0 && nombreTotalPhase > 1 && duelsPhasePrecedente != null && duelsPhasePrecedente.size() == 2
				&& duelsPhasePrecedente.get(0).getLooser() != null && duelsPhasePrecedente.get(1).getLooser() !=null) {
			duels.add(new Duel(
					duelsPhasePrecedente.get(0).getLooser(), //on prend les 2 perdants de la demi-finale
					duelsPhasePrecedente.get(1).getLooser(),
					phase, 2));
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
		
		int nbTotalPhases = getNombrePhase(categorie);
		
		for(int i = 0; i < nbTotalPhases; i++) {
			List<Duel> duels = getDuelsPhase(categorie, i);
			if(duels.size() == 2 && duels.get(0).getPhase() == 0) {
				Duel duel = duels.get(0);
				if(duel.getWinner() != null) {
					concurrents.add(duel.getWinner());
					concurrents.add(duel.getLooser());
				} else {
					concurrents.add(duel.getConcurrent1());
					concurrents.add(duel.getConcurrent2());
				}
				
				//Petite finale
				duel = duels.get(1);
				if(duel.getWinner() != null) {
					concurrents.add(duel.getWinner());
					concurrents.add(duel.getLooser());
				} else {
					concurrents.add(duel.getConcurrent1());
					concurrents.add(duel.getConcurrent2());
				}
			} else if(duels.size() > 2 || concurrents.size() == 0 ) {
				List<Concurrent> perdants = new ArrayList<Concurrent>();
				for(Duel duel : duels) {
					if(duel.getLooser() != null)
						perdants.add(duel.getLooser());
					else if(i < nbTotalPhases-1){
						perdants.add(duel.getConcurrent1());
						perdants.add(duel.getConcurrent2());
					}
				}
				if(perdants.size() > 0) {
					final int phase = i;
					Collections.sort(perdants, new Comparator<Concurrent>() {
						@Override
						public int compare(Concurrent o1, Concurrent o2) {
							return o1.compareScorePhaseFinalWith(o2, phase) * -1;
						}
					});
					concurrents.addAll(perdants);
				}
			}
		}
		return concurrents;
	}
	
	/**
	 * Retourne le classement générale de phase finale
	 * 
	 * @return le classement par catégorie
	 */
	public Map<CriteriaSet, List<Concurrent>> getClassement() {
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(
				ficheConcours.getParametre().getReglement(),
				ficheConcours.getParametre().getReglement().getClassementFilter());
		Map<CriteriaSet, List<Concurrent>> concurrentsClasse = new HashMap<CriteriaSet, List<Concurrent>>();
		
		for(CriteriaSet criteriaSet : catList) {
			concurrentsClasse.put(criteriaSet, getClassement(criteriaSet));
		}
		
		return concurrentsClasse;
	}
	
	/**
	 * Retourne le classement générale de phase finale au format HTML
	 * 
	 * @return le classement par catégorie au format HTML
	 */
	public String getClassementHTMLPhasesFinalesIndividuel() {
		Map<CriteriaSet, List<Concurrent>> concurrentsClasse = getClassement();
		
		if(concurrentsClasse.size() == 0)
			return ""; //$NON-NLS-1$
		
		AJTemplate tplClassement = templateClassementHTML;
		
		tplClassement.reset();
		
		tplClassement.parse("LOGO_CLUB_URI", ficheConcours.getProfile().getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tplClassement.parse("INTITULE_CLUB", ficheConcours.getParametre().getClub().getNom()); //$NON-NLS-1$
		
		Set<CriteriaSet> scnalst = concurrentsClasse.keySet();
		List<CriteriaSet> scnaUse = new ArrayList<CriteriaSet>(scnalst);
		CriteriaSet.sortCriteriaSet(scnaUse, ficheConcours.getParametre().getReglement().getListCriteria());
		
		for (CriteriaSet scna : scnaUse) {
			List<Concurrent> sortList = concurrentsClasse.get(scna);
			
			if (sortList.size() > 0) {
				String strSCNA = CriteriaSetLibelle.getLibelle(scna, ficheConcours.getProfile().getLocalisation());
				
				tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
				
				int place = 0;
				int scorePrecedent = -1;
				int phasePrecedente = -1;
				for(Concurrent concurrent : sortList) {
					if(concurrent == null)
						continue;
					
					int phase = -1;
					for(int i = 0; i < 6; i++) {
						if(concurrent.getScorePhasefinale(i) != 0) {
							phase = i;
							break;
						}
					}
					
					if(phase == -1)
						continue;
					
					if(phasePrecedente != phase || concurrent.getScorePhasefinale(phase) != scorePrecedent)
						place++;
					
					scorePrecedent = concurrent.getScorePhasefinale(phase);
					phasePrecedente = phase;

					tplClassement.parse("categories.classement.COULEUR", ""); //$NON-NLS-1$ //$NON-NLS-2$
					
					tplClassement.parse("categories.classement.POSITION", "" + concurrent.getDepart() + concurrent.getPosition() + concurrent.getCible()); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.classement.PLACE", String.valueOf(place)); //$NON-NLS-1$
					tplClassement.parse("categories.classement.IDENTITEE", concurrent.getFullName()); //$NON-NLS-1$
					tplClassement.parse("categories.classement.CLUB", concurrent.getEntite().toString()); //$NON-NLS-1$
					tplClassement.parse("categories.classement.PHASE", (phase > 0 || place < 3) ? ficheConcours.getProfile().getLocalisation().getResourceString("duel.phase."+phase) : ficheConcours.getProfile().getLocalisation().getResourceString("duel.phase.smallfinal")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					tplClassement.parse("categories.classement.SCORE", String.valueOf(concurrent.getScorePhasefinale(phase))); //$NON-NLS-1$
					
					tplClassement.loopBloc("categories.classement"); //$NON-NLS-1$
				}
				
				tplClassement.loopBloc("categories"); //$NON-NLS-1$
			}
		}
		
		return tplClassement.output();
	}
	
	/**
	 * Rafraîchit le classement pour travailler sur des données à jour
	 */
	private void refreshClassement() {
		classement = ficheConcours.getConcurrentList().classement();
	}
	
	/**
	 * Chargement des template de sortie HTML
	 * 
	 */
	private void loadTemplates() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if(templateClassementHTML != null)
			templateClassementHTML.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
					+ File.separator + staticParameters.getResourceString("template.classement_phasesfinales.html")); //$NON-NLS-1$
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getPropertyName().equals("score") || e.getPropertyName().equals("criteriaSet")) //$NON-NLS-1$ //$NON-NLS-2$
			refreshClassement();
		//|| e.getPropertyName().equals("departages") || e.getPropertyName().equals("scoresPhasesFinales")
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
