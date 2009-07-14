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

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.swing.event.EventListenerList;
import javax.xml.bind.annotation.XmlRootElement;

import org.ajdeveloppement.commons.AJTemplate;
import org.ajdeveloppement.commons.XmlUtils;
import org.ajdeveloppement.commons.io.XMLSerializer;
import org.concoursjeunes.builders.AncragesMapBuilder;
import org.concoursjeunes.builders.BlasonBuilder;
import org.concoursjeunes.builders.EquipeListBuilder;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.event.PasDeTirListener;
import org.concoursjeunes.exceptions.FicheConcoursException;
import org.concoursjeunes.exceptions.FicheConcoursException.Nature;
import org.concoursjeunes.localisable.CriteriaSetLibelle;
import org.concoursjeunes.manager.BlasonManager;

/**
 * Represente la fiche concours, regroupe l'ensemble des informations commune à un concours donné
 * 
 * @author Aurélien Jeoffray
 */
@XmlRootElement
public class FicheConcours implements PasDeTirListener, PropertyChangeListener {

	private Profile profile;
	private Parametre parametre;

	private ConcurrentList concurrentList;
	private EquipeList equipes;
	private Map<Integer, PasDeTir> pasDeTir = new HashMap<Integer, PasDeTir>();

	private EventListenerList ficheConcoursListeners = new EventListenerList();

	private int currentDepart = 0;

	private AJTemplate templateClassementHTML;
	private AJTemplate templateClassementEquipeHTML;

	/**
	 * Initialise une nouvelle fiche concours
	 */
	public FicheConcours(Profile profile) 
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		this(profile, null);
	}
	
	/**
	 * Initialise une nouvelle fiche concours avec les parametres fournit
	 * 
	 * @param parametre les parametres du concours ou null si laisser les parametres par défaut
	 */
	public FicheConcours(Profile profile, Parametre parametre)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		this.profile = profile;
		if(parametre != null) {
			this.parametre = parametre;
		} else {
			this.parametre = new Parametre(profile.getConfiguration());
		}
		concurrentList = new ConcurrentList(this.parametre);
		equipes = new EquipeList(this.parametre.getReglement().getNbMembresRetenu());
		
		templateClassementHTML = new AJTemplate(this.profile.getLocalisation());
		templateClassementEquipeHTML = new AJTemplate(this.profile.getLocalisation());
		loadTemplates();
		
		this.parametre.addPropertyChangeListener(this);
		makePasDeTir();
	}

	/**
	 * Enregistre un auditeur aux evenements d'un concours
	 * 
	 * @param ficheConcoursListener l'auditeur à abonner
	 */
	public void addFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.add(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Supprime un auditeur des evenements du concours
	 * 
	 * @param ficheConcoursListener l'auditeur à résilier
	 */
	public void removeFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.remove(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Retourne la liste des archers inscrit sur le concours
	 * 
	 * @return la liste des archers inscrit sur le concours
	 */
	public ConcurrentList getConcurrentList() {
		return concurrentList;
	}

	/**
	 * Donne la liste des equipes enregistré sur le concours
	 * 
	 * @return equipes - la liste des équipes
	 */
	public EquipeList getEquipes() {
		return equipes;
	}

	/**
	 * Définit la liste des équipes inscritent sur le concours
	 * 
	 * @param equipeList la liste des équipes
	 */
	public void setEquipes(EquipeList equipeList) {
		this.equipes = equipeList;
	}

	/**
	 * Retourne les parametres propre au concours
	 * 
	 * @return les parametres du concours
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * Ajoute un concurrent au concours
	 * 
	 * TODO ajouter une verification que le jeux de critère du concurrent est valide sur le concours
	 * 
	 * @param concurrent -
	 *            le concurrent à ajouter au concours
	 * 
	 * @throws IOException
	 * @throws FicheConcoursException
	 */
	public void addConcurrent(Concurrent concurrent, int depart) throws FicheConcoursException {
		concurrent.setDepart(depart);

		if (concurrentList.contains(concurrent, depart))
			throw new FicheConcoursException(Nature.ALREADY_EXISTS, "Le concurrent est déjà présent"); //$NON-NLS-1$

		if (!pasDeTir.get(depart).havePlaceForConcurrent(concurrent))
			throw new FicheConcoursException(Nature.NO_SLOT_AVAILABLE, "Il n'y a pas de place pour le concurrent"); //$NON-NLS-1$

		concurrentList.add(concurrent);

		fireConcurrentAdded(concurrent);
		try {
			save();
		} catch (IOException e) {
			throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e); //$NON-NLS-1$
		}
	}

	/**
	 * retire un concurrent du concours
	 * 
	 * @param removedConcurrent -
	 *            Le concurrent à supprimer
	 *            
	 * @throws FicheConcoursException
	 */
	public void removeConcurrent(Concurrent removedConcurrent) throws FicheConcoursException {
		// suppression dans l'equipe si presence dans equipe
		equipes.removeConcurrent(removedConcurrent);
		// suppression dans la liste
		if (concurrentList.remove(removedConcurrent) != null)
			fireConcurrentRemoved(removedConcurrent);

		try {
			save();
		} catch (IOException e) {
			throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e); //$NON-NLS-1$
		}
	}

	/**
	 * Retourne le numero du départ courrant (actuellement actif)
	 * 
	 * @return le numero du départ
	 */
	public int getCurrentDepart() {
		return currentDepart;
	}

	/**
	 * Définit le numero du départ courrant
	 * 
	 * @param currentDepart -
	 *            le numero du départ
	 */
	public void setCurrentDepart(int currentDepart) {
		this.currentDepart = currentDepart;
	}

	/**
	 * Retourne le pas de tir du départ donné en parametre
	 * 
	 * @return pasDeTir le pas de tir du départ en parametre
	 */
	public PasDeTir getPasDeTir(int depart) {
		return pasDeTir.get(depart);
	}

	/**
	 * Donne la fiche complete du concours pour sauvegarde
	 * le tableau retourné contient les propriétés:
	 * { parametre, concurrentList, equipes }
	 * 
	 * @return Object[]
	 */
	public Object[] getFiche() {
		Object[] fiche = { parametre, concurrentList, equipes };
		return fiche;
	}

	/**
	 * Restaure une fiche concours
	 * 
	 * @param fiche -
	 *            la fiche à restaurer
	 */
	public void setFiche(Object[] fiche, MetaDataFicheConcours metaDataFicheConcours) {
		
		parametre = (Parametre) fiche[0];
		concurrentList = (ConcurrentList) fiche[1];
		concurrentList.setParametre(parametre);
		
		equipes = (EquipeList) fiche[2];
		
		checkFiche();

		parametre.addPropertyChangeListener(metaDataFicheConcours);
		parametre.addPropertyChangeListener(this);

		makePasDeTir();
	}

	/**
	 * Retourne les métadonnées associé à un concours
	 * 
	 * @return l'objet de métédonnée du concours
	 */
	public MetaDataFicheConcours getMetaDataFicheConcours() {
		MetaDataFicheConcours metaDataFicheConcours = new MetaDataFicheConcours(parametre.getDateDebutConcours(), parametre.getIntituleConcours(), parametre.getSaveName());
		parametre.addPropertyChangeListener(metaDataFicheConcours);

		return metaDataFicheConcours;
	}

	/**
	 * sauvegarde de la fiche concours
	 * 
	 */
	public void save() throws IOException {
		File f = new File(
				ApplicationCore.userRessources.getConcoursPathForProfile(this.profile),
				parametre.getSaveName());
		XMLSerializer.saveXMLStructure(f, getFiche(), true);
	}
	
	/**
	 * <p>Controle la cohérance d'une fiche et effectue une mise
	 * à niveau si besoin.</p>
	 * <p>Permet de mettre à niveau les fiches serialisé dans des versions
	 * inférieur du programme</p>
	 */
	@SuppressWarnings("deprecation")
	private void checkFiche() {
		Reglement reglement = parametre.getReglement();
		
		if(reglement.getVersion() == 1) {
			reglement.setTie(new ArrayList<String>(Arrays.asList(new String[] { "10","9" }))); //$NON-NLS-1$ //$NON-NLS-2$
			reglement.setDisplayName(reglement.getName());
			reglement.setVersion(2);
		}
		
		//contrôle l'affectation du réglement
		for(Concurrent concurrent : concurrentList.list(-1)) {
			if(concurrent.getCriteriaSet().getReglement() == null)
				concurrent.getCriteriaSet().setReglement(reglement);
		}
		
		DistancesEtBlason defaultDistancesEtBlason = new DistancesEtBlason();
		for(DistancesEtBlason distancesEtBlason : reglement.getListDistancesEtBlason()) {
			
			if(distancesEtBlason.getCriteriaSet().getReglement() == null)
				distancesEtBlason.getCriteriaSet().setReglement(reglement);
			
			//si le blason n'est pas initialiser 
			if(distancesEtBlason.getTargetFace() == null || distancesEtBlason.getTargetFace().equals(new Blason())) {
				if(distancesEtBlason.getNumdistancesblason() > 0) { //si le reglement est dans la base
					distancesEtBlason.setTargetFace(BlasonManager.findBlasonAssociateToDistancesEtBlason(distancesEtBlason.getNumdistancesblason(), reglement.hashCode()));
				} else {
					Blason targetFace = null;
					try { //on tente de retrouver une correspondance pour le blason dans la base
		                targetFace = BlasonManager.findBlasonByName(distancesEtBlason.getBlason() + "cm"); //$NON-NLS-1$
	                } catch (SQLException e) {
		                e.printStackTrace(); //on trace l'erreur mais on ne la fait pas remonter dans l'interface
	                }
	                if(targetFace == null) { //si on a pas retrouvé de blason correspondant dans la base alors créer l'entrée
	                	targetFace = BlasonBuilder.getBlason(distancesEtBlason.getBlason());
	                }
				}
				//remet la valeur par defaut pour supprimer la section du XML de persistance à la prochaine sauvergarde
				distancesEtBlason.setBlason(defaultDistancesEtBlason.getBlason());
			
			} else {
				//si il est initialisé mais ne possede pas d'ancrage
				if(distancesEtBlason.getTargetFace().getAncrages().size() == 0) {
					//si le blason est present dans la base
					if(distancesEtBlason.getTargetFace().getNumblason() > 0) {
						ConcurrentMap<Integer, Ancrage> ancrages = null;
						try {
							ancrages = AncragesMapBuilder.getAncragesMap(distancesEtBlason.getTargetFace());
                        } catch (SQLException e) {
	                        e.printStackTrace(); //on trace l'erreur mais on ne la fait pas remonter dans l'interface
                        }
                        if(ancrages == null) {
                        	ancrages = AncragesMapBuilder.getAncragesMap(distancesEtBlason.getTargetFace().getNbArcher());
                        }
                        
                        distancesEtBlason.getTargetFace().setAncrages(ancrages);
					} else {
						ConcurrentMap<Integer, Ancrage> ancrages = AncragesMapBuilder.getAncragesMap(distancesEtBlason.getTargetFace().getNbArcher());
						distancesEtBlason.getTargetFace().setAncrages(ancrages);
					}
				}
			}
		}
	}

	/**
	 * Construit le pas de tir
	 */
	private void makePasDeTir() {
		for (int i = 0; i < parametre.getNbDepart(); i++) {
			PasDeTir pdt = new PasDeTir(this, i);
			pdt.addPasDeTirListener(this);
			pasDeTir.put(i, pdt);
		}

		firePasDeTirChanged();
	}

	/**
	 * methode pour le classement des candidats
	 */
	public Map<CriteriaSet, List<Concurrent>> classement() {

		Map<CriteriaSet, List<Concurrent>> concurrentsClasse = new HashMap<CriteriaSet, List<Concurrent>>();

		// Etablit le classement des concurrents en fonction du nombre de points
		// obtenue.
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(parametre.getReglement(), parametre.getReglement().getClassementFilter());

		// Affectation des valeurs
		for (int i = 0; i < catList.length; i++) {
			// sort la liste des concurrents correspondant aux critéres de
			// recherche
			ArrayList<Concurrent> unsortList = new ArrayList<Concurrent>();
			for (Concurrent concurrent : concurrentList.list(catList[i], -1, parametre.getReglement().getClassementFilter()))
				unsortList.add(concurrent);
			List<Concurrent> sortList = ConcurrentList.sort(unsortList, ConcurrentList.SortCriteria.SORT_BY_POINTS);
			if (sortList.size() > 0)
				concurrentsClasse.put(catList[i], sortList);
		}

		return concurrentsClasse;
	}

	/**
	 * Chargement des template de sortie XML
	 * 
	 */
	private void loadTemplates() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		templateClassementHTML.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + staticParameters.getResourceString("template.classement.html")); //$NON-NLS-1$

		templateClassementEquipeHTML.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + staticParameters.getResourceString("template.classement_equipe.html")); //$NON-NLS-1$
	}

	/**
	 * Donne le XML de l'etat de classement individuel<br>
	 * L'etat peut être retourné au format XML iText en
	 * specifiant le type OUT_XMLen parametre<br>
	 * ou au format HTML en specifiant OUT_HTML en parametre 
	 * 
	 * @return le HTML de l'edition
	 */
	public String getClassement() {
		String strClassement = ""; //$NON-NLS-1$
		if (concurrentList != null && concurrentList.countArcher() > 0) {
			Map<CriteriaSet, List<Concurrent>> concurrentsClasse = classement();

			AJTemplate tplClassement = templateClassementHTML;
			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			tplClassement.reset();

			tplClassement.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassement.parse("LOGO_CLUB_URI", this.profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassement.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassement.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassement.parse("VILLE_CLUB", parametre.getLieuConcours()); //$NON-NLS-1$
			tplClassement.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(parametre.getDateDebutConcours())); //$NON-NLS-1$
			tplClassement.parse("author", parametre.getClub().getNom()); //$NON-NLS-1$

			for (Judge arbitre : parametre.getJudges()) {
				if (arbitre.isResponsable())
					strArbitreResp = arbitre.getID();
				else {
					if (!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre.getID();
				}
			}
			tplClassement.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
			tplClassement.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
			tplClassement.parse("NB_CLUB", "" + concurrentList.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("NB_TIREURS", "" + concurrentList.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			
			// Entete de categorie
			Set<CriteriaSet> scnalst = concurrentsClasse.keySet();

			List<CriteriaSet> scnaUse = new ArrayList<CriteriaSet>(scnalst);

			CriteriaSet.sortCriteriaSet(scnaUse, parametre.getReglement().getListCriteria());

			for (CriteriaSet scna : scnaUse) {

				List<Concurrent> sortList = concurrentsClasse.get(scna);

				String strSCNA;

				if (sortList.size() > 0) {

					double tailleChampDistance = 10.5262 / parametre.getReglement().getNbSerie();
					String strTailleChampsDistance = ""; //$NON-NLS-1$
					for (int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						strTailleChampsDistance += tailleChampDistance + ";"; //$NON-NLS-1$
					}

					strSCNA = new CriteriaSetLibelle(scna, this.profile.getLocalisation()).toString();

					tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance); //$NON-NLS-1$
					tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
					tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + parametre.getReglement().getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.NB_TIREURS", "" + sortList.size()); //$NON-NLS-1$ //$NON-NLS-2$

					for (int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
								parametre.getReglement().getDistancesEtBlasonFor(scna.getFilteredCriteriaSet(parametre.getReglement().getPlacementFilter())).get(0).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances"); //$NON-NLS-1$
					}
					
					String departages = ""; //$NON-NLS-1$
					for (String departage : parametre.getReglement().getTie()) {
						if(!departages.isEmpty())
							departages += "-"; //$NON-NLS-1$
						departages += departage;
					}
					tplClassement.parse("categories.DEPARTAGE", departages); //$NON-NLS-1$

					if (sortList.size() > 0) {
						boolean row_exist = false;
						for (int j = 0; j < sortList.size(); j++) {
							if (sortList.get(j).getTotalScore() > 0) {
								row_exist = true;
								// test d'ex-Eaquo
								
								if ((j < sortList.size() - 1 && sortList.get(j).compareScoreWith(sortList.get(j + 1)) == 0 && this.profile.getConfiguration().isInterfaceAffResultatExEquo())
										|| (j > 0 && sortList.get(j).compareScoreWith(sortList.get(j - 1)) == 0 && this.profile.getConfiguration().isInterfaceAffResultatExEquo())) {

									tplClassement.parse("categories.classement.COULEUR", //$NON-NLS-1$
											"bgcolor=\"#ff0000\""); //$NON-NLS-1$
								} else {
									tplClassement.parse("categories.classement.COULEUR", "bgcolor=\"#ffffff\""); //$NON-NLS-1$ //$NON-NLS-2$
								}

								tplClassement.parse("categories.classement.PLACE", "" + (j + 1)); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.POSITION", "" + sortList.get(j).getDepart()+sortList.get(j).getPosition() + sortList.get(j).getCible()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.IDENTITEE", sortList.get(j).getID()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.CLUB", sortList.get(j).getClub().getNom()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.NUM_LICENCE", sortList.get(j).getNumLicenceArcher()); //$NON-NLS-1$

								for (Criterion key : parametre.getReglement().getListCriteria())
									tplClassement.parse("categories.classement." //$NON-NLS-1$
											+ key.getCode(), sortList.get(j).getCriteriaSet().getCriterionElement(key).getCode());

								for (int k = 0; k < parametre.getReglement().getNbSerie(); k++) {
									if (sortList.get(j).getScore() != null)
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList.get(j).getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
									else
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

									tplClassement.loopBloc("categories.classement.scores"); //$NON-NLS-1$
								}
								tplClassement.parse("categories.classement.TOTAL", "" + sortList.get(j).getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
								
								departages = ""; //$NON-NLS-1$
								if(sortList.get(j).getDepartages().length == parametre.getReglement().getTie().size()) {
									for(int k = 0; k < parametre.getReglement().getTie().size(); k++) {
										departages += sortList.get(j).getDepartages()[k];
										if(k<parametre.getReglement().getTie().size()-1)
											departages += "-"; //$NON-NLS-1$
									}
								}
								tplClassement.parse("categories.classement.0_10_9", departages); //$NON-NLS-1$

								tplClassement.loopBloc("categories.classement"); //$NON-NLS-1$
							}
							if (!row_exist)
								tplClassement.parseBloc("categories.classement", ""); //$NON-NLS-1$ //$NON-NLS-2$
						}
					} else {
						tplClassement.parseBloc("categories.classement", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}

					tplClassement.loopBloc("categories"); //$NON-NLS-1$
				}
			}
			strClassement = tplClassement.output();
		}

		return strClassement;
	}

	/**
	 * Donne le HTML de l'etat de classement par équipe
	 * 
	 * @return le HTML à retourner
	 */
	public String getClassementEquipe() {
		String strClassementEquipe = ""; //$NON-NLS-1$

		if (equipes != null && equipes.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = templateClassementEquipeHTML;

			tplClassementEquipe.reset();

			// classement sortie XML
			tplClassementEquipe.parse("LOGO_CLUB_URI", this.profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", XmlUtils.sanitizeText(parametre.getClub().getNom())); //$NON-NLS-1$

			List<CriteriaSet> teamCriteriaSet = equipes.listCriteriaSet();
			
			CriteriaSet.sortCriteriaSet(teamCriteriaSet, parametre.getReglement().getListCriteria());
			
			for(CriteriaSet criteriaSet : teamCriteriaSet) {			
				tplClassementEquipe.parse("categories.CATEGORIE", new CriteriaSetLibelle(criteriaSet, this.profile.getLocalisation()).toString()); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.NB_EQUIPES", "" + equipes.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$
	
				Equipe[] sortEquipes = EquipeList.sort(equipes.list(criteriaSet));
	
				for (int i = 0; i < sortEquipes.length; i++) {
	
					tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$
	
					String idsXML = ""; //$NON-NLS-1$
					String ptsXML = ""; //$NON-NLS-1$
					for (Concurrent concurrent : sortEquipes[i].getMembresEquipe()) {
						idsXML += XmlUtils.sanitizeText(concurrent.getID()) + "<br>"; //$NON-NLS-1$
						ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
					}
					tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", XmlUtils.sanitizeText(sortEquipes[i].getNomEquipe())); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes[i].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
	
					tplClassementEquipe.loopBloc("categories.classement"); //$NON-NLS-1$
				}
				tplClassementEquipe.loopBloc("categories"); //$NON-NLS-1$
			}
			strClassementEquipe = tplClassementEquipe.output();
		}
		return strClassementEquipe;
	}

	/**
	 * Retourne, au format html, l'etat de classement
	 * par equipe automatique de club 
	 * 
	 * @return l'etat de classment
	 */
	public String getClassementClub() {
		String strClassementEquipe = ""; //$NON-NLS-1$

		EquipeList clubList = EquipeListBuilder.getClubEquipeList(concurrentList, parametre.getReglement().getNbMembresRetenu());

		if (clubList != null && clubList.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = templateClassementEquipeHTML;

			tplClassementEquipe.reset();

			// classement sortie XML
			tplClassementEquipe.parse("LOGO_CLUB_URI", this.profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$

			tplClassementEquipe.parse("categories.CATEGORIE", this.profile.getLocalisation().getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$

			Equipe[] sortEquipes = EquipeList.sort(clubList.list());

			for (int i = 0; i < sortEquipes.length; i++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				String idsXML = ""; //$NON-NLS-1$
				String ptsXML = ""; //$NON-NLS-1$
				for (Concurrent concurrent : sortEquipes[i].getMembresEquipe()) {
					idsXML += concurrent.getID() + "<br>"; //$NON-NLS-1$
					ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
				}
				tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", sortEquipes[i].getNomEquipe()); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes[i].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$

				tplClassementEquipe.loopBloc("categories.classement"); //$NON-NLS-1$
			}
			strClassementEquipe = tplClassementEquipe.output();
		}

		return strClassementEquipe;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() == parametre) {
			if(evt.getPropertyName().equals("arbitres") //$NON-NLS-1$
					|| evt.getPropertyName().equals("reglement")) { //$NON-NLS-1$
				
				assert pasDeTir.size() > 0 : "Il doit exister au moins un pas de tir"; //$NON-NLS-1$
				
				if (parametre.getNbCible() != pasDeTir.get(0).getTargets().size()
						|| parametre.getNbTireur() != pasDeTir.get(0).getTargets().get(0).getNbMaxArchers()) {
					//si le nombre de cible à changé reconstruire le pas de tir
					makePasDeTir();
					if (parametre.getNbCible() < pasDeTir.get(0).getTargets().size()) {
						//si le nombre de cible est inferieur, virer les archers
						//sur les cibles supprimé
						for (int i = 0; i < parametre.getNbDepart(); i++) {
							for (int j = parametre.getNbCible(); j < pasDeTir.get(i).getTargets().size(); j++) {
								for (Concurrent concurrent : concurrentList.list(j + 1, i)) {
									pasDeTir.get(i).retraitConcurrent(concurrent);
								}
							}
						}
					}
				} else if(parametre.getNbDepart() != pasDeTir.size())
					makePasDeTir();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.event.PasDeTirListener#pasdetirChanged()
	 */
	@Override
	public void pasdetirChanged() {
		firePasDeTirChanged();
	}

	private void fireConcurrentAdded(Concurrent concurrent) {
		for (FicheConcoursListener ficheConcoursListener : ficheConcoursListeners.getListeners(FicheConcoursListener.class)) {

			ficheConcoursListener.listConcurrentChanged(new FicheConcoursEvent(FicheConcoursEvent.ADD_CONCURRENT, concurrent));
		}
	}

	private void fireConcurrentRemoved(Concurrent concurrent) {
		for (FicheConcoursListener ficheConcoursListener : ficheConcoursListeners.getListeners(FicheConcoursListener.class)) {

			ficheConcoursListener.listConcurrentChanged(new FicheConcoursEvent(FicheConcoursEvent.REMOVE_CONCURRENT, concurrent));
		}
	}

	private void firePasDeTirChanged() {
		for (FicheConcoursListener ficheConcoursListener : ficheConcoursListeners.getListeners(FicheConcoursListener.class)) {

			ficheConcoursListener.pasDeTirChanged(new FicheConcoursEvent(FicheConcoursEvent.PASDETIR_CHANGED, FicheConcoursEvent.ALL_START));
		}
	}
}