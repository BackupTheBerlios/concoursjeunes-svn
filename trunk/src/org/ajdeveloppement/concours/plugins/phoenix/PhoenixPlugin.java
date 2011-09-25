/*
 * Créé le 29 déc. 07 à 16:15:22 pour ArcCompetition
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
package org.ajdeveloppement.concours.plugins.phoenix;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.concours.Ancrage;
import org.ajdeveloppement.concours.ApplicationCore;
import org.ajdeveloppement.concours.Blason;
import org.ajdeveloppement.concours.Concurrent;
import org.ajdeveloppement.concours.ConcurrentList;
import org.ajdeveloppement.concours.Configuration;
import org.ajdeveloppement.concours.CriteriaSet;
import org.ajdeveloppement.concours.Criterion;
import org.ajdeveloppement.concours.CriterionElement;
import org.ajdeveloppement.concours.DistancesEtBlason;
import org.ajdeveloppement.concours.Entite;
import org.ajdeveloppement.concours.EquipeList;
import org.ajdeveloppement.concours.FicheConcours;
import org.ajdeveloppement.concours.MetaDataFicheConcours;
import org.ajdeveloppement.concours.MetaDataFichesConcours;
import org.ajdeveloppement.concours.Parametre;
import org.ajdeveloppement.concours.Profile;
import org.ajdeveloppement.concours.Reglement;
import org.ajdeveloppement.concours.builders.AncragesMapBuilder;
import org.ajdeveloppement.concours.builders.BlasonBuilder;
import org.ajdeveloppement.concours.builders.FicheConcoursBuilder;
import org.ajdeveloppement.concours.event.ApplicationCoreEvent;
import org.ajdeveloppement.concours.event.ApplicationCoreListener;
import org.ajdeveloppement.concours.event.ProfileEvent;
import org.ajdeveloppement.concours.event.ProfileListener;
import org.ajdeveloppement.concours.managers.BlasonManager;
import org.ajdeveloppement.concours.plugins.Plugin;
import org.ajdeveloppement.concours.plugins.PluginEntry;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Plugin(type = Plugin.Type.STARTUP)
public class PhoenixPlugin implements ProfileListener, ApplicationCoreListener {
	
	public PhoenixPlugin() {
		for(Profile profile : ApplicationCore.getInstance().getProfiles())
			profile.addProfileListener(this);
		ApplicationCore.getInstance().addApplicationCoreListener(this);
	}
	
	@PluginEntry
	public void start() {
		for(Profile profile : ApplicationCore.getInstance().getProfiles())
			findConcours(profile);
	}
	
	
	private synchronized void findConcours(Profile profile) {
		Configuration configuration = profile.getConfiguration();
		
		MetaDataFichesConcours metaDataFichesConcours = configuration.getMetaDataFichesConcours();
		
		File concoursPath = ApplicationCore.userRessources.getConcoursPathForProfile(profile);
		
		convertOldFicheConcours(concoursPath, metaDataFichesConcours, profile);
		
		File[] concoursFiles = concoursPath.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".ctax"); //$NON-NLS-1$
			}
		});
		
		if(metaDataFichesConcours.getFiches().size() != concoursFiles.length) {
			metaDataFichesConcours.removeAll();
			for(File concoursFile : concoursFiles) {
				if(concoursFile.isFile() && !metaDataFichesConcours.containsFileName(concoursFile.getName())) {
					try {
						FicheConcours ficheConcours = FicheConcoursBuilder.getFicheConcours(new MetaDataFicheConcours(null, null, concoursFile.getName()), profile);
						MetaDataFicheConcours metaDataFicheConcours = ficheConcours.getMetaDataFicheConcours();
						if(!metaDataFichesConcours.contains(metaDataFicheConcours)) {
							metaDataFichesConcours.add(metaDataFicheConcours);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void convertOldFicheConcours(File concoursPath, MetaDataFichesConcours metaDataFichesConcours, Profile profile) {
		File[] concoursFiles = concoursPath.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".cta"); //$NON-NLS-1$
			}
		});
		if(concoursFiles.length > 0)
			metaDataFichesConcours.removeAll();
		for(File concoursFile : concoursFiles) {
			if(concoursFile.isFile()) {
				try {
					Object[] structure = XMLSerializer.loadXMLStructure(concoursFile, true);
					if(structure != null && structure.length == 3) {
						Parametre parametre = null;
						if(structure[0] instanceof Parametre) {
							parametre = (Parametre) structure[0];
							metaDataFichesConcours.remove(
									new MetaDataFicheConcours(
											parametre.getDateDebutConcours(), parametre.getIntituleConcours(), parametre.getSaveName()));
							parametre.setSaveName(parametre.getSaveName() + "x"); //$NON-NLS-1$
							
						}
						
						if(parametre != null) {
							checkFiche(structure);
							
							FicheConcours ficheConcours = new FicheConcours(profile, parametre);
							for(Concurrent concurrent : ((ConcurrentList)structure[1]).list())
								ficheConcours.addConcurrent(concurrent, concurrent.getDepart());
							
							ficheConcours.setEquipes((EquipeList)structure[2]);

							ficheConcours.save();
							
							metaDataFichesConcours.add(ficheConcours.getMetaDataFicheConcours());
							
							concoursFile.delete();
						}
					} else {
						concoursFile.delete(); //Fichier verolé on supprime
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	/**
	 * <p>Contrôle la cohérence d'une fiche et effectue une mise
	 * à niveau si besoin.</p>
	 * <p>Permet de mettre à niveau les fiches sérialisé dans des versions
	 * inférieur du programme</p>
	 */
	@SuppressWarnings("deprecation")
	private void checkFiche(Object[] oldSerializedFiche) {
		Parametre parametre = (Parametre)oldSerializedFiche[0];
		ConcurrentList concurrentList = (ConcurrentList)oldSerializedFiche[1];
		
		if(parametre != null) {
			Reglement reglement = parametre.getReglement();
			
			if(reglement.getVersion() == 1) {
				reglement.setTie(new ArrayList<String>(Arrays.asList(new String[] { "10","9" }))); //$NON-NLS-1$ //$NON-NLS-2$
				reglement.setDisplayName(reglement.getName());
				reglement.setVersion(Reglement.CURRENT_VERSION);
				
				for(Entry<CriteriaSet, CriteriaSet> entry : reglement.getSurclassement().entrySet()) {
					for(Entry<Criterion, CriterionElement> entry2 : entry.getKey().getCriteria().entrySet()) {
						if(entry2.getValue().getCriterion() == null)
							entry2.getValue().setCriterion(entry2.getKey());
					}
					for(Entry<Criterion, CriterionElement> entry2 : entry.getValue().getCriteria().entrySet()) {
						if(entry2.getValue() != null && entry2.getValue().getCriterion() == null)
							entry2.getValue().setCriterion(entry2.getKey());
					}
				}
			}
			
			//contrôle l'affectation du règlement et des critères
			for(Criterion criterion : reglement.getListCriteria()) {
				if(criterion.getReglement() == null)
					criterion.setReglement(reglement);
				for(CriterionElement element : criterion.getCriterionElements()) {
					if(element.getCriterion() == null)
						element.setCriterion(criterion);
				}
			}
			
			if(concurrentList != null) {
				List<Entite> entiteCache = new ArrayList<Entite>();
				for(Concurrent concurrent : concurrentList.list()) {
					if(concurrent.getCriteriaSet().getReglement() == null)
						concurrent.getCriteriaSet().setReglement(reglement);
					
					//S'assure que pour une entite donnée, il n'ai qu'une seul instance
					if(!entiteCache.contains(concurrent.getEntite()))
						entiteCache.add(concurrent.getEntite());
					else
						concurrent.setEntite(entiteCache.get(entiteCache.indexOf(concurrent.getEntite())));
					
					// En correction corruption suite manipulation Bug 57
					if(concurrent.getInscription() == Concurrent.UNINIT)
						concurrent.setInscription(Concurrent.RESERVEE);
					
					//si il manque des critères, essaye de les regénérer
					for(Criterion criterion : reglement.getListCriteria()) {
						if(!concurrent.getCriteriaSet().getCriteria().containsKey(criterion)) {
							if(criterion.getCriterionElements().size() == 0) {
								CriterionElement defElement = new CriterionElement("A"); //$NON-NLS-1$
								defElement.setLibelle("Tous"); //$NON-NLS-1$
								List<CriterionElement> lce = new ArrayList<CriterionElement>();
								lce.add(defElement);
								criterion.setCriterionElements(lce);
							}
							concurrent.getCriteriaSet().addCriterionElement(
									criterion.getCriterionElements().get(0));
						}
					}
		
					for(Entry<Criterion, CriterionElement> entry : concurrent.getCriteriaSet().getCriteria().entrySet()) {
						if(entry.getValue().getCriterion() == null)
							entry.getValue().setCriterion(entry.getKey());
					}
				}
			}
			
			DistancesEtBlason defaultDistancesEtBlason = new DistancesEtBlason();
			for(DistancesEtBlason distancesEtBlason : reglement.getListDistancesEtBlason()) {
				
				if(distancesEtBlason.getCriteriaSet().getReglement() == null)
					distancesEtBlason.getCriteriaSet().setReglement(reglement);
				
				//si le blason n'est pas initialiser
				if(distancesEtBlason.getTargetFace() == null || distancesEtBlason.getTargetFace().equals(new Blason())) {
					if(distancesEtBlason.getNumdistancesblason() > 0 && reglement.getNumReglement() > 0) { //si le règlement est dans la base
						try {
							distancesEtBlason.setTargetFace(BlasonManager.findBlasonAssociateToDistancesEtBlason(distancesEtBlason));
						} catch (ObjectPersistenceException e) {
							e.printStackTrace();
						}
					} else {
						Blason targetFace = null;
						try { //on tente de retrouver une correspondance pour le blason dans la base
			                targetFace = BlasonManager.findBlasonByName(distancesEtBlason.getBlason() + "cm"); //$NON-NLS-1$
		                } catch (ObjectPersistenceException e) {
			                e.printStackTrace(); //on trace l'erreur mais on ne la fait pas remonter dans l'interface
		                }
		                if(targetFace == null) { //si on a pas retrouvé de blason correspondant dans la base alors créer l'entrée
		                	targetFace = BlasonBuilder.getBlasonBySize(distancesEtBlason.getBlason());
		                }
					}
					//remet la valeur par défaut pour supprimer la section du XML de persistance à la prochaine sauvegarde
					distancesEtBlason.setBlason(defaultDistancesEtBlason.getBlason());
				
				} else {
					//si il est initialisé mais ne possède pas d'ancrage
					if(distancesEtBlason.getTargetFace().getAncrages() == null || distancesEtBlason.getTargetFace().getAncrages().size() == 0) {
						//si le blason est present dans la base
						if(distancesEtBlason.getTargetFace().getNumblason() > 0) {
							ConcurrentMap<Integer, Ancrage> ancrages = null;
							try {
								ancrages = AncragesMapBuilder.getAncragesMap(distancesEtBlason.getTargetFace());
	                        } catch (ObjectPersistenceException e) {
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
	}

	/* (non-Javadoc)
	 * @see org.ajdeveloppement.concours.event.ProfileListener#configurationChanged(org.ajdeveloppement.concours.profileEvent)
	 */
	@Override
	public void configurationChanged(ProfileEvent profileEvent) {
		findConcours(profileEvent.getProfile());
	}

	/* (non-Javadoc)
	 * @see org.ajdeveloppement.concours.event.ProfileListener#ficheConcoursClosed(org.ajdeveloppement.concours.profileEvent)
	 */
	@Override
	public void ficheConcoursClosed(ProfileEvent profileEvent) {
		
	}

	/* (non-Javadoc)
	 * @see org.ajdeveloppement.concours.event.ProfileListener#ficheConcoursCreated(org.ajdeveloppement.concours.profileEvent)
	 */
	@Override
	public void ficheConcoursCreated(ProfileEvent profileEvent) {
		
	}

	/* (non-Javadoc)
	 * @see org.ajdeveloppement.concours.event.ProfileListener#ficheConcoursDeleted(org.ajdeveloppement.concours.profileEvent)
	 */
	@Override
	public void ficheConcoursDeleted(ProfileEvent profileEvent) {
		
	}

	/* (non-Javadoc)
	 * @see org.ajdeveloppement.concours.event.ProfileListener#ficheConcoursRestored(org.ajdeveloppement.concours.profileEvent)
	 */
	@Override
	public void ficheConcoursRestored(ProfileEvent profileEvent) {
		
	}

	@Override
	public void profileAdded(ApplicationCoreEvent e) {
		findConcours(e.getProfile());
	}

	@Override
	public void profileRemoved(ApplicationCoreEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}
}
