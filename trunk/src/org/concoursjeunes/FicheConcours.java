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

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.event.EventListenerList;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ajdeveloppement.commons.AJTemplate;
import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.XmlUtils;
import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.concours.Contact;
import org.ajdeveloppement.concours.exceptions.FicheConcoursException;
import org.ajdeveloppement.concours.exceptions.FicheConcoursException.Nature;
import org.concoursjeunes.builders.EquipeListBuilder;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.event.PasDeTirListener;
import org.concoursjeunes.localisable.CriteriaSetLibelle;
import org.concoursjeunes.manager.EntiteManager;

/**
 * Représente la fiche concours, regroupe l'ensemble des informations commune à un concours donné
 * 
 * @author Aurélien Jeoffray
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FicheConcours implements PasDeTirListener, PropertyChangeListener {

	@XmlAttribute(name="dbuuid")
	private UUID dbUUID = null; 
	
	@XmlElementWrapper(name="entitesConcours")
	@XmlElement(name="entite")
	private List<Entite> entitesConcours = null;
	
	@XmlTransient
	private Profile profile;
	private Parametre parametre;

	@XmlElement(name="concurrents")
	private ConcurrentList concurrentList;
	@XmlElement(name="equipes")
	private EquipeList equipes;
	
	private transient Map<Integer, ShootingLine> pasDeTir = new HashMap<Integer, ShootingLine>();

	private transient EventListenerList ficheConcoursListeners = new EventListenerList();

	private transient int currentDepart = 0;

	private transient AJTemplate templateClassementHTML;
	private transient AJTemplate templateClassementEquipeHTML;

	public FicheConcours() { }
	
	/**
	 * Initialise une nouvelle fiche concours
	 */
	public FicheConcours(Profile profile) 
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		this(profile, null);
	}
	
	/**
	 * Initialise une nouvelle fiche concours avec les paramètres fournit
	 * 
	 * @param parametre les paramètres du concours ou null si laisser ceux par défaut
	 */
	public FicheConcours(Profile profile, Parametre parametre)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
		if(profile != null) {
			if(parametre != null)
				this.parametre = parametre;
			
			setProfile(profile);
		}
	}

	/**
	 * Enregistre un auditeur aux évènements d'un concours
	 * 
	 * @param ficheConcoursListener l'auditeur à abonner
	 */
	public void addFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.add(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Supprime un auditeur des évènements du concours
	 * 
	 * @param ficheConcoursListener l'auditeur à résilier
	 */
	public void removeFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.remove(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * @param dbUUID dbUUID à définir
	 */
	public void setDbUUID(UUID dbUUID) {
		this.dbUUID = dbUUID;
	}

	/**
	 * @return dbUUID
	 */
	public UUID getDbUUID() {
		return dbUUID;
	}

	/**
	 * @return profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile profile à définir
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public void setProfile(Profile profile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		this.profile = profile;
		
		if(profile != null) {
			if(parametre == null)
				parametre = new Parametre(profile.getConfiguration());
			
			if(concurrentList == null)
				concurrentList = new ConcurrentList(parametre);
			
			if(equipes == null)
				equipes = new EquipeList(parametre.getReglement().getNbMembresRetenu());
			
			parametre.removePropertyChangeListener(this);
			parametre.addPropertyChangeListener(this);
			
			templateClassementHTML = new AJTemplate(this.profile.getLocalisation());
			templateClassementEquipeHTML = new AJTemplate(this.profile.getLocalisation());
			loadTemplates();
			
			makePasDeTir();
		}
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
	 * Donne la liste des équipes enregistré sur le concours
	 * 
	 * @return la liste des équipes
	 */
	public EquipeList getEquipes() {
		return equipes;
	}

	/**
	 * Définit la liste des équipes inscrite sur le concours
	 * 
	 * @param equipeList la liste des équipes
	 */
	public void setEquipes(EquipeList equipeList) {
		this.equipes = equipeList;
	}

	/**
	 * Retourne les paramètres propre au concours
	 * 
	 * @return les paramètres du concours
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * Ajoute un concurrent au concours
	 * 
	 * TODO ajouter une verification que le jeux de critère du concurrent est valide sur le concours
	 * 
	 * @param concurrent le concurrent à ajouter au concours
	 * 
	 * @throws IOException
	 * @throws FicheConcoursException
	 */
	public void addConcurrent(Concurrent concurrent, int depart) throws FicheConcoursException {
		concurrent.setDepart(depart);
		
		if(concurrentList == null)
			concurrentList = new ConcurrentList(parametre);

		if (concurrentList.contains(concurrent, depart))
			throw new FicheConcoursException(Nature.ALREADY_EXISTS, "Le concurrent est déjà présent");

		if (pasDeTir.size() != 0 && !pasDeTir.get(depart).havePlaceForConcurrent(concurrent))
			throw new FicheConcoursException(Nature.NO_SLOT_AVAILABLE, "Il n'y a pas de place pour le concurrent");

		concurrentList.add(concurrent);

		fireConcurrentAdded(concurrent);
		try {
			save();
		} catch (IOException e) {
			throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e);
		} catch (JAXBException e) {
			throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e);
		}
	}

	/**
	 * retire un concurrent du concours
	 * 
	 * @param removedConcurrent le concurrent à supprimer
	 *            
	 * @throws FicheConcoursException
	 */
	public void removeConcurrent(Concurrent removedConcurrent) throws FicheConcoursException {
		if(equipes != null) {
			// suppression dans l'equipe si presence dans equipes
			equipes.removeConcurrent(removedConcurrent);
		}
		
		if(concurrentList != null) {
			// suppression dans la liste
			if (concurrentList.remove(removedConcurrent) != null)
				fireConcurrentRemoved(removedConcurrent);
	
			try {
				save();
			} catch (IOException e) {
				throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e);
			} catch (JAXBException e) {
				throw new FicheConcoursException(Nature.SAVE_IO_ERROR, "Une erreur est survenue au moment de la sauvegarde", e);
			}
		}
	}

	/**
	 * Retourne le numéro du départ courant (actuellement actif)
	 * 
	 * @return le numéro du départ
	 */
	public int getCurrentDepart() {
		return currentDepart;
	}

	/**
	 * Définit le numéro du départ courant
	 * 
	 * @param currentDepart le numéro du départ
	 */
	public void setCurrentDepart(int currentDepart) {
		this.currentDepart = currentDepart;
	}

	/**
	 * Retourne le pas de tir du départ donné en paramètre
	 * 
	 * @return pasDeTir le pas de tir du départ en paramètre
	 */
	public ShootingLine getPasDeTir(int depart) {
		if(pasDeTir != null)
			return pasDeTir.get(depart);
		return null;
	}

	/**
	 * Retourne les métadonnées associé à un concours
	 * 
	 * @return l'objet de méta-données du concours
	 */
	public MetaDataFicheConcours getMetaDataFicheConcours() {
		if(parametre != null) {
			MetaDataFicheConcours metaDataFicheConcours = new MetaDataFicheConcours(parametre.getDateDebutConcours(), parametre.getIntituleConcours(), parametre.getSaveName());
			parametre.addPropertyChangeListener(metaDataFicheConcours);
	
			return metaDataFicheConcours;
		}
		return null;
	}

	/**
	 * sauvegarde de la fiche concours
	 * 
	 */
	public void save() throws IOException,JAXBException {
		if(profile != null) {
			File f = new File(
					ApplicationCore.userRessources.getConcoursPathForProfile(this.profile),
					parametre.getSaveName());
			
			XMLSerializer.saveMarshallStructure(new File(f.getPath()), this, true);
		}
	}
	
	/**
	 * 
	 * @param marshaller
	 */
	protected void beforeMarshal(Marshaller marshaller) {
		if(parametre != null && concurrentList != null) {
			entitesConcours = new ArrayList<Entite>(Arrays.asList(concurrentList.listCompagnie()));
			for(Judge judge : parametre.getJudges()) {
				if(!entitesConcours.contains(judge.getEntite()))
					entitesConcours.add(judge.getEntite());
			}
		}
	}
	
	/**
	 * 
	 * @param marshaller
	 */
	protected void afterMarshal(Marshaller marshaller) {
		entitesConcours = null;
	}
	
	/**
	 * 
	 * @param unmarshaller
	 * @param parent
	 */
	protected void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		parametre.addPropertyChangeListener(this);
		concurrentList.setParametre(parametre);
		entitesConcours = null;
		
		//si la fiche concours est basé synchronisé avec une autre base de données,
		//on la resynchronise avec la base courante
		if(!ApplicationCore.dbUUID.equals(dbUUID)) {
			List<Entite> cacheEntite = new ArrayList<Entite>();
			
			for(Concurrent concurrent : concurrentList.list())
				resyncContactEntite(concurrent, cacheEntite);
			
			for(Judge judge : parametre.getJudges())
				resyncContactEntite(judge, cacheEntite);
			
			dbUUID = ApplicationCore.dbUUID;
		}

		makePasDeTir();
	}
	
	/**
	 * Resynchronise l'entite du contact avec la base
	 * 
	 * @param contact le contact à synchroniser
	 * @param cacheEntite cache des entités synchronisé pour épargner des réquêtes en base
	 */
	private void resyncContactEntite(Contact contact, List<Entite> cacheEntite) {
		Entite entite = contact.getEntite();
		
		if(cacheEntite.equals(entite))
			contact.setEntite(cacheEntite.get(cacheEntite.indexOf(entite)));//substitution d'instance
		else {
			try {
				List<Entite> entitesInDatabase = EntiteManager.getEntitesInDatabase(entite, "");//$NON-NLS-1$
				if(entitesInDatabase != null && entitesInDatabase.size() > 0) {
					entite = entitesInDatabase.get(0);
					contact.setEntite(entite);
					
					cacheEntite.add(entite);
				}
			} catch (ObjectPersistenceException e) {
				throw new UncheckedException(e);
			} 
			
		}
	}

	/**
	 * Construit le pas de tir
	 */
	private void makePasDeTir() {
		if(parametre != null && pasDeTir != null && concurrentList != null) {
			for (int i = 0; i < parametre.getNbDepart(); i++) {
				ShootingLine pdt = new ShootingLine(this, i);
				pdt.addPasDeTirListener(this);
				pasDeTir.put(i, pdt);
			}
	
			firePasDeTirChanged();
		}
	}

	/**
	 * Classement des candidats
	 * 
	 * @deprecated utiliser de préférence {@link ConcurrentList#classement()}
	 * 
	 * @return map contenant une liste de concurrent trié par jeux de critère de classement
	 */
	@Deprecated
	public Map<CriteriaSet, List<Concurrent>> classement() {
		if(concurrentList != null)
			return concurrentList.classement().getClassementPhaseQualificative();
		return null;
	}

	/**
	 * Chargement des template de sortie XML
	 * 
	 */
	private void loadTemplates() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if(templateClassementHTML != null)
			templateClassementHTML.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
					+ File.separator + staticParameters.getResourceString("template.classement.html")); //$NON-NLS-1$
		if(templateClassementEquipeHTML != null)
			templateClassementEquipeHTML.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
					+ File.separator + staticParameters.getResourceString("template.classement_equipe.html")); //$NON-NLS-1$
	}

	/**
	 * Donne le HTML de l'etat de classement individuel
	 * 
	 * @return le HTML de l'edition
	 */
	public String getClassement() {
		String strClassement = ""; //$NON-NLS-1$
		if (concurrentList != null && concurrentList.countArcher() > 0) {
			Map<CriteriaSet, List<Concurrent>> concurrentsClasse = concurrentList.classement().getClassementPhaseQualificative();

			AJTemplate tplClassement = templateClassementHTML;

			tplClassement.reset();

			tplClassement.parse("LOGO_CLUB_URI", this.profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassement.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$

			// Entête de catégorie
			Set<CriteriaSet> scnalst = concurrentsClasse.keySet();

			List<CriteriaSet> scnaUse = new ArrayList<CriteriaSet>(scnalst);

			CriteriaSet.sortCriteriaSet(scnaUse, parametre.getReglement().getListCriteria());

			for (CriteriaSet scna : scnaUse) {

				List<Concurrent> sortList = concurrentsClasse.get(scna);

				String strSCNA;

				if (sortList.size() > 0) {
					strSCNA = new CriteriaSetLibelle(scna, this.profile.getLocalisation()).toString();

					tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$

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
								// test d'ex-Aequo
								if ((j < sortList.size() - 1 && sortList.get(j).compareScoreWith(sortList.get(j + 1)) == 0 && this.profile.getConfiguration().isInterfaceAffResultatExEquo())
										|| (j > 0 && sortList.get(j).compareScoreWith(sortList.get(j - 1)) == 0 && this.profile.getConfiguration().isInterfaceAffResultatExEquo())) {

									tplClassement.parse("categories.classement.COULEUR", //$NON-NLS-1$
											"bgcolor=\"#ff0000\""); //$NON-NLS-1$
								} else {
									tplClassement.parse("categories.classement.COULEUR", "bgcolor=\"#ffffff\""); //$NON-NLS-1$ //$NON-NLS-2$
								}

								tplClassement.parse("categories.classement.PLACE", "" + (j + 1)); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.POSITION", "" + sortList.get(j).getDepart()+sortList.get(j).getPosition() + sortList.get(j).getCible()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.IDENTITEE", sortList.get(j).getFullName()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.CLUB", sortList.get(j).getEntite().toString()); //$NON-NLS-1$

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
	
				List<Equipe> sortEquipes = EquipeList.sort(equipes.getEquipeList(criteriaSet));
	
				for (int i = 0; i < sortEquipes.size(); i++) {
	
					tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$
	
					String idsXML = ""; //$NON-NLS-1$
					String ptsXML = ""; //$NON-NLS-1$
					for (Concurrent concurrent : sortEquipes.get(i).getMembresEquipe()) {
						idsXML += XmlUtils.sanitizeText(concurrent.getFullName()) + "<br>"; //$NON-NLS-1$
						ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
					}
					tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", XmlUtils.sanitizeText(sortEquipes.get(i).getNomEquipe())); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
					tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes.get(i).getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
	
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
	 * par équipe automatique de club 
	 * 
	 * @return l'etat de classment
	 */
	public String getClassementClub() {
		String strClassementEquipe = ""; //$NON-NLS-1$

		EquipeList clubList = null;
		if(concurrentList != null && parametre != null)
			clubList = EquipeListBuilder.getClubEquipeList(concurrentList, parametre.getReglement().getNbMembresRetenu());

		if (clubList != null && clubList.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = templateClassementEquipeHTML;

			tplClassementEquipe.reset();

			// classement sortie XML
			tplClassementEquipe.parse("LOGO_CLUB_URI", this.profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$

			tplClassementEquipe.parse("categories.CATEGORIE", this.profile.getLocalisation().getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$

			List<Equipe> sortEquipes = EquipeList.sort(clubList.getEquipeList());

			for (int i = 0; i < sortEquipes.size(); i++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				String idsXML = ""; //$NON-NLS-1$
				String ptsXML = ""; //$NON-NLS-1$
				for (Concurrent concurrent : sortEquipes.get(i).getMembresEquipe()) {
					idsXML += concurrent.getFullName() + "<br>"; //$NON-NLS-1$
					ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
				}
				tplClassementEquipe.parse("categories.classement.IDENTITEES", idsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.NOM_EQUIPE", sortEquipes.get(i).getNomEquipe()); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_INDIVIDUEL", ptsXML); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.classement.TOTAL_GENERAL", "" + sortEquipes.get(i).getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$

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