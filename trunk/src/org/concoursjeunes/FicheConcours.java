/*
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

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import javax.swing.event.EventListenerList;
import javax.xml.bind.annotation.XmlRootElement;

import org.concoursjeunes.builders.AncragesMapBuilder;
import org.concoursjeunes.builders.BlasonBuilder;
import org.concoursjeunes.builders.EquipeListBuilder;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.event.ParametreEvent;
import org.concoursjeunes.event.ParametreListener;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.common.AJTemplate;
import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.common.XmlUtils;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * Represente la fiche concours, regroupe l'ensemble des informations commune à un concours donné
 * 
 * @author Aurélien Jeoffray
 */
@XmlRootElement
public class FicheConcours implements ParametreListener {

	/**
	 * Edition par ordre alphabetique
	 */
	public static final int ALPHA = 0; // par ordre alphabetique
	/**
	 * Edition pour le greffe
	 */
	public static final int GREFFE = 1; // pour le greffe
	/**
	 * Edition par positionnement sur cible
	 */
	public static final int TARGET = 3; // par ordre sur le pas de tir

	/**
	 * Edition au format XML iText
	 */
	public static final int OUT_XML = 0; // Sortie XML
	/**
	 * Edition au format HTML
	 */
	public static final int OUT_HTML = 1; // Sortie HTML

	private Parametre parametre = new Parametre(ConcoursJeunes.getConfiguration());

	private ConcurrentList concurrentList = new ConcurrentList(parametre);
	private EquipeList equipes = new EquipeList(this);

	private final Hashtable<Integer, PasDeTir> pasDeTir = new Hashtable<Integer, PasDeTir>();

	private final EventListenerList ficheConcoursListeners = new EventListenerList();

	private int currentDepart = 0;

	private static AJTemplate templateClassementXML = new AJTemplate();
	private static AJTemplate templateClassementEquipeXML = new AJTemplate();
	private static AJTemplate templateClassementHTML = new AJTemplate();
	private static AJTemplate templateClassementEquipeHTML = new AJTemplate();
	private static AJTemplate templateListeArcherXML = new AJTemplate();
	private static AJTemplate templateListeGreffeXML = new AJTemplate();
	private static AJTemplate templateEtiquettesXML = new AJTemplate();
	private static AJTemplate templatePasDeTirXML = new AJTemplate();

	static {
		loadTemplates();
	}

	/**
	 * Initialise une nouvelle fiche concours
	 */
	public FicheConcours() {
		this(null);
	}
	
	/**
	 * Initialise une nouvelle fiche concours avec les parametres fournit
	 * 
	 * @param parametre les parametres du concours ou null si laisser les parametres par défaut
	 */
	public FicheConcours(Parametre parametre) {
		if(parametre != null)
			this.parametre = parametre;
		
		this.parametre.addParametreListener(this);
		makePasDeTir();
	}

	/**
	 * Enregistre un auditeur aux evenements d'un concours
	 * 
	 * @param ficheConcoursListener -
	 *            l'auditeur à abonner
	 */
	public void addFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.add(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Supprime un auditeur des evenements du concours
	 * 
	 * @param ficheConcoursListener -
	 *            l'auditeur à résilier
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
	 * @param concurrent -
	 *            le concurrent à ajouter au concours
	 * @return true si ajout avec succès, false sinon
	 */
	public boolean addConcurrent(Concurrent concurrent, int depart) throws IOException {
		concurrent.setDepart(depart);

		if (concurrentList.contains(concurrent, depart))
			return false;

		if (!pasDeTir.get(depart).havePlaceForConcurrent(concurrent))
			return false;

		concurrentList.add(concurrent);

		fireConcurrentAdded(concurrent);
		save();

		return true;
	}

	/**
	 * retire un concurrent du concours
	 * 
	 * @param removedConcurrent -
	 *            Le concurrent à supprimer
	 */
	public void removeConcurrent(Concurrent removedConcurrent) throws IOException {
		// retire le concurrent du pas de tir si present
		if (removedConcurrent.getCible() > 0)
			pasDeTir.get(removedConcurrent.getDepart()).getTargets().get(removedConcurrent.getCible() - 1).removeConcurrent(removedConcurrent);
		// suppression dans la liste
		// suppression dans l'equipe si presence dans equipe
		equipes.removeConcurrent(removedConcurrent);
		if (concurrentList.remove(removedConcurrent) != null)
			fireConcurrentRemoved(removedConcurrent);

		save();
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
		equipes = (EquipeList) fiche[2];
		
		checkFiche();

		parametre.addParametreListener(metaDataFicheConcours);
		parametre.addParametreListener(this);

		makePasDeTir();
	}

	/**
	 * Retourne les métadonnées associé à un concours
	 * 
	 * @return l'objet de métédonnée du concours
	 */
	public MetaDataFicheConcours getMetaDataFicheConcours() {
		MetaDataFicheConcours metaDataFicheConcours = new MetaDataFicheConcours(parametre.getDate(), parametre.getIntituleConcours(), parametre.getSaveName());
		parametre.addParametreListener(metaDataFicheConcours);

		return metaDataFicheConcours;
	}

	/**
	 * sauvegarde "silencieuse" en arriere plan de la fiche concours
	 * 
	 */
	public void save() throws IOException {
		File f = new File(ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.getConfiguration().getCurProfil()) + File.separator + parametre.getSaveName());
		AJToolKit.saveXMLStructure(f, getFiche(), true);
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
		
		DistancesEtBlason defaultDistancesEtBlason = new DistancesEtBlason();
		for(DistancesEtBlason distancesEtBlason : reglement.getListDistancesEtBlason()) {
			
			//si le blason n'est pas initialiser 
			if(distancesEtBlason.getTargetFace().equals(Blason.NULL)) {
				if(distancesEtBlason.getNumdistancesblason() > 0) { //si le reglement est dans la base
					distancesEtBlason.setTargetFace(BlasonManager.findBlasonAssociateToDistancesEtBlason(distancesEtBlason));
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

	private void makePasDeTir() {
		for (int i = 0; i < parametre.getNbDepart(); i++) {
			pasDeTir.put(i, new PasDeTir(this, i));
		}

		firePasDeTirChanged();
	}

	/**
	 * methode pour le classement des candidats
	 * 
	 * @param depart
	 */
	private Hashtable<CriteriaSet, Concurrent[]> classement() {

		Hashtable<CriteriaSet, Concurrent[]> concurrentsClasse = new Hashtable<CriteriaSet, Concurrent[]>();

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
			Concurrent[] sortList = ConcurrentList.sort(unsortList.toArray(new Concurrent[unsortList.size()]), ConcurrentList.SortCriteria.SORT_BY_POINTS);
			if (sortList.length > 0)
				concurrentsClasse.put(catList[i], sortList);
		}

		return concurrentsClasse;
	}

	/**
	 * Chargement des template de sortie XML
	 * 
	 */
	private static void loadTemplates() {
		templateClassementXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.classement.xml")); //$NON-NLS-1$

		templateClassementHTML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.classement.html")); //$NON-NLS-1$

		templateClassementEquipeXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.classement_equipe.xml")); //$NON-NLS-1$

		templateClassementEquipeHTML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.classement_equipe.html")); //$NON-NLS-1$

		templateListeArcherXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.listarcher")); //$NON-NLS-1$

		templateListeGreffeXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.listarcher.greffe")); //$NON-NLS-1$

		templateEtiquettesXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.etiquettes")); //$NON-NLS-1$

		templatePasDeTirXML.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.pasdetir")); //$NON-NLS-1$
	}

	/**
	 * Donne le XML de l'etat de classement individuel<br>
	 * L'etat peut être retourné au format XML iText en
	 * specifiant le type OUT_XMLen parametre<br>
	 * ou au format HTML en specifiant OUT_HTML en parametre 
	 * 
	 * @param outType le type de sortie à produire.
	 * 
	 * @return le XML iText à retourner
	 */
	public String getClassement(int outType) {
		String strClassement = ""; //$NON-NLS-1$
		if (concurrentList != null && concurrentList.countArcher() > 0) {
			Hashtable<CriteriaSet, Concurrent[]> concurrentsClasse = classement();

			AJTemplate tplClassement = null;
			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			switch (outType) {
				case OUT_XML:
					tplClassement = templateClassementXML;
					break;
				case OUT_HTML:
					tplClassement = templateClassementHTML;
					break;
				default:
					return null;
			}

			tplClassement.reset();

			tplClassement.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassement.parse("LOGO_CLUB_URI", ConcoursJeunes.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassement.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassement.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassement.parse("VILLE_CLUB", parametre.getLieuConcours()); //$NON-NLS-1$
			tplClassement.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(parametre.getDate())); //$NON-NLS-1$
			tplClassement.parse("author", parametre.getClub().getNom()); //$NON-NLS-1$

			for (String arbitre : parametre.getArbitres()) {
				if (arbitre.startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitre.substring(1);
				else {
					if (!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre;
				}
			}
			tplClassement.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
			tplClassement.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
			tplClassement.parse("NB_CLUB", "" + concurrentList.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("NB_TIREURS", "" + concurrentList.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.individuel")); //$NON-NLS-1$ //$NON-NLS-2$

			// Entete de categorie
			Enumeration<CriteriaSet> scnalst = concurrentsClasse.keys();

			CriteriaSet[] scnaUse = new CriteriaSet[concurrentsClasse.size()];
			for (int i = 0; scnalst.hasMoreElements(); i++) {
				scnaUse[i] = scnalst.nextElement();
			}

			CriteriaSet.sortCriteriaSet(scnaUse, parametre.getReglement().getListCriteria());

			for (CriteriaSet scna : scnaUse) {

				Concurrent[] sortList = concurrentsClasse.get(scna);

				String strSCNA;

				if (sortList.length > 0) {

					double tailleChampDistance = 10.5262 / parametre.getReglement().getNbSerie();
					String strTailleChampsDistance = ""; //$NON-NLS-1$
					for (int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						strTailleChampsDistance += tailleChampDistance + ";"; //$NON-NLS-1$
					}

					strSCNA = new CriteriaSetLibelle(scna).toString();

					tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance); //$NON-NLS-1$
					tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
					tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + parametre.getReglement().getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.NB_TIREURS", "" + sortList.length); //$NON-NLS-1$ //$NON-NLS-2$

					for (int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
								parametre.getReglement().getDistancesEtBlasonFor(scna.getFilteredCriteriaSet(parametre.getReglement().getPlacementFilter())).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances"); //$NON-NLS-1$
					}

					if (sortList.length > 0) {
						boolean row_exist = false;
						for (int j = 0; j < sortList.length; j++) {
							if (sortList[j].getTotalScore() > 0) {
								row_exist = true;
								// test d'ex-Eaquo
								if ((j < sortList.length - 1 && sortList[j].getTotalScore() == sortList[j + 1].getTotalScore() && ConcoursJeunes.getConfiguration().isInterfaceAffResultatExEquo())
										|| (j > 0 && sortList[j].getTotalScore() == sortList[j - 1].getTotalScore() && ConcoursJeunes.getConfiguration().isInterfaceAffResultatExEquo())) {

									if ((sortList[j].getManque() == 0 && sortList[j].getDix() == 0 && sortList[j].getNeuf() == 0)
											|| (j < sortList.length - 2 && sortList[j].getManque() == sortList[j + 1].getManque() && sortList[j].getDix() == sortList[j + 1].getDix() && sortList[j]
													.getNeuf() == sortList[j + 1].getNeuf())
											|| (j > 0 && sortList[j].getManque() == sortList[j - 1].getManque() && sortList[j].getDix() == sortList[j - 1].getDix() && sortList[j].getNeuf() == sortList[j - 1]
													.getNeuf())) {

										tplClassement.parse("categories.classement.COULEUR", //$NON-NLS-1$
												"bgcolor=\"#ff0000\""); //$NON-NLS-1$
									}
								} else {
									tplClassement.parse("categories.classement.COULEUR", "bgcolor=\"#ffffff\""); //$NON-NLS-1$ //$NON-NLS-2$
								}

								tplClassement.parse("categories.classement.PLACE", "" + (j + 1)); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.POSITION", "" + sortList[j].getPosition() + sortList[j].getCible()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.IDENTITEE", sortList[j].getID()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.CLUB", sortList[j].getClub().getNom()); //$NON-NLS-1$
								tplClassement.parse("categories.classement.NUM_LICENCE", sortList[j].getNumLicenceArcher()); //$NON-NLS-1$

								for (Criterion key : parametre.getReglement().getListCriteria())
									tplClassement.parse("categories.classement." //$NON-NLS-1$
											+ key.getCode(), sortList[j].getCriteriaSet().getCriterionElement(key).getCode());

								for (int k = 0; k < parametre.getReglement().getNbSerie(); k++) {
									if (sortList[j].getScore() != null)
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList[j].getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
									else
										tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

									tplClassement.loopBloc("categories.classement.scores"); //$NON-NLS-1$
								}
								tplClassement.parse("categories.classement.TOTAL", "" + sortList[j].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
								tplClassement.parse("categories.classement.0_10_9", sortList[j].getManque() //$NON-NLS-1$
										+ "-" + sortList[j].getDix() + "-" + sortList[j].getNeuf()); //$NON-NLS-1$ //$NON-NLS-2$

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
	 * Donne le XML de l'etat de classement par équipe<br>
	 * L'etat peut être retourné au format XML iText en
	 * specifiant le type OUT_XMLen parametre<br>
	 * ou au format HTML en specifiant OUT_HTML en parametre 
	 * 
	 * @param outType -
	 *            le type de sortie à produire
	 * 
	 * @return le XML iText ou le HTML à retourner
	 */
	public String getClassementEquipe(int outType) {
		System.out.println("Sortie Equipes"); //$NON-NLS-1$

		String strClassementEquipe = ""; //$NON-NLS-1$

		if (equipes != null && equipes.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = null;
			switch (outType) {
			case OUT_XML:
				tplClassementEquipe = templateClassementEquipeXML;
				break;
			case OUT_HTML:
				tplClassementEquipe = templateClassementEquipeHTML;
				break;
			default:
				return null;
			}

			tplClassementEquipe.reset();

			// classement sortie XML
			tplClassementEquipe.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())); //$NON-NLS-1$
			tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", XmlUtils.sanitizeText(parametre.getClub().getNom())); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", XmlUtils.sanitizeText(parametre.getIntituleConcours())); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", XmlUtils.sanitizeText(parametre.getLieuConcours())); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(parametre.getDate())); //$NON-NLS-1$

			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			for (String arbitre : parametre.getArbitres()) {
				if (arbitre.startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitre.substring(1);
				else {
					if (!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre;
				}
			}

			tplClassementEquipe.parse("ARBITRE_RESPONSABLE", XmlUtils.sanitizeText(strArbitreResp)); //$NON-NLS-1$
			tplClassementEquipe.parse("ARBITRES_ASSISTANT", XmlUtils.sanitizeText(strArbitresAss)); //$NON-NLS-1$
			tplClassementEquipe.parse("NB_CLUB", "" + concurrentList.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("NB_TIREURS", "" + concurrentList.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.equipe")); //$NON-NLS-1$ //$NON-NLS-2$

			
			List<CriteriaSet> teamCriteriaSet = equipes.listCriteriaSet();
			CriteriaSet[] sortedTeamCriteriaSets = new CriteriaSet[teamCriteriaSet.size()];
			sortedTeamCriteriaSets = teamCriteriaSet.toArray(sortedTeamCriteriaSets);
			
			CriteriaSet.sortCriteriaSet(sortedTeamCriteriaSets, parametre.getReglement().getListCriteria());
			
			for(CriteriaSet criteriaSet : sortedTeamCriteriaSets) {			
				tplClassementEquipe.parse("categories.CATEGORIE", new CriteriaSetLibelle(criteriaSet).toString()); //$NON-NLS-1$
				tplClassementEquipe.parse("categories.NB_EQUIPES", "" + equipes.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$
	
				Equipe[] sortEquipes = EquipeList.sort(equipes.list(criteriaSet));
	
				for (int i = 0; i < sortEquipes.length; i++) {
	
					tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$
	
					String idsXML = ""; //$NON-NLS-1$
					String ptsXML = ""; //$NON-NLS-1$
					for (Concurrent concurrent : sortEquipes[i].getMembresEquipe()) {
						if (outType == OUT_XML) {
							idsXML += XmlUtils.sanitizeText(concurrent.getID()) + "<newline/>"; //$NON-NLS-1$
							ptsXML += concurrent.getTotalScore() + "<newline/>"; //$NON-NLS-1$
						} else {
							idsXML += XmlUtils.sanitizeText(concurrent.getID()) + "<br>"; //$NON-NLS-1$
							ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
						}
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
	 * Retourne, au format html ou xml, l'etat de classement
	 * par equipe automatique de club 
	 * 
	 * @param outType le type (HTML ou XML de sortie)
	 * 
	 * @return l'etat de classment
	 */
	public String getClassementClub(int outType) {
		System.out.println("Sortie Club"); //$NON-NLS-1$

		String strClassementEquipe = ""; //$NON-NLS-1$

		EquipeList clubList = EquipeListBuilder.getClubEquipeList(concurrentList, this);

		if (clubList != null && clubList.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = null;
			switch (outType) {
			case OUT_XML:
				tplClassementEquipe = templateClassementEquipeXML;
				break;
			case OUT_HTML:
				tplClassementEquipe = templateClassementEquipeHTML;
				break;
			default:
				return null;
			}

			tplClassementEquipe.reset();

			// classement sortie XML
			tplClassementEquipe.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", parametre.getLieuConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(parametre.getDate())); //$NON-NLS-1$

			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			for (String arbitre : parametre.getArbitres()) {
				if (arbitre.startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitre.substring(1);
				else {
					if (!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre;
				}
			}

			tplClassementEquipe.parse("ARBITRE_RESPONSABLE", strArbitreResp); //$NON-NLS-1$
			tplClassementEquipe.parse("ARBITRES_ASSISTANT", strArbitresAss); //$NON-NLS-1$
			tplClassementEquipe.parse("NB_CLUB", "" + concurrentList.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("NB_TIREURS", "" + concurrentList.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.club")); //$NON-NLS-1$ //$NON-NLS-2$

			tplClassementEquipe.parse("categories.CATEGORIE", ConcoursJeunes.ajrLibelle.getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("categories.NB_EQUIPES", "" + equipes.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$

			Equipe[] sortEquipes = EquipeList.sort(clubList.list());

			for (int i = 0; i < sortEquipes.length; i++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				String idsXML = ""; //$NON-NLS-1$
				String ptsXML = ""; //$NON-NLS-1$
				for (Concurrent concurrent : sortEquipes[i].getMembresEquipe()) {
					if (outType == OUT_XML) {
						idsXML += concurrent.getID() + "<newline/>"; //$NON-NLS-1$
						ptsXML += concurrent.getTotalScore() + "<newline/>"; //$NON-NLS-1$
					} else {
						idsXML += concurrent.getID() + "<br>"; //$NON-NLS-1$
						ptsXML += concurrent.getTotalScore() + "<br>"; //$NON-NLS-1$
					}
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

	/**
	 * Donne le XML de l'etat "Liste des archer"
	 * 
	 * @param mode -
	 *            le mode (ALPHA pour liste alpabétique et GREFFE pour liste pour le greffe)
	 * @return String - le XML iText à retourner
	 */
	private String getXMLListeArcher(int mode, int depart) {
		AJTemplate listeArcherXML;
		String strArcherListeXML = ""; //$NON-NLS-1$
		
		if(concurrentList.countArcher(depart) == 0)
			return ""; //$NON-NLS-1$

		if (mode == ALPHA || mode == TARGET)
			listeArcherXML = templateListeArcherXML;
		else
			listeArcherXML = templateListeGreffeXML;

		listeArcherXML.reset();

		listeArcherXML.parse("NB_PARTICIPANTS", "" + concurrentList.countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
		listeArcherXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$

		listeArcherXML.parse("LISTE", strArcherListeXML); //$NON-NLS-1$

		Concurrent[] concurrents = null;
		if (mode == ALPHA || mode == GREFFE)
			concurrents = ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SortCriteria.SORT_BY_NAME);
		else
			concurrents = ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS);

		for (Concurrent concurrent : concurrents) {
			listeArcherXML.parse("lignes.IDENTITEE", concurrent.getID()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.CLUB", concurrent.getClub().getNom()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.NUM_LICENCE", concurrent.getNumLicenceArcher()); //$NON-NLS-1$

			for (Criterion key : parametre.getReglement().getListCriteria()) {
				listeArcherXML.parse("lignes." + key.getCode(), //$NON-NLS-1$
						concurrent.getCriteriaSet().getCriterionElement(key).getCode());
			}

			listeArcherXML.parse("lignes.PAYEE", AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.impression.inscription"), ",")[concurrent.getInscription()]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			listeArcherXML.parse("lignes.CERTIFICAT", AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.certificat"), ",")[concurrent.isCertificat() ? 0 : 1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			listeArcherXML.parse("lignes.CIBLE", new TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString()); //$NON-NLS-1$

			listeArcherXML.loopBloc("lignes"); //$NON-NLS-1$
		}

		return listeArcherXML.output();
	}

	/**
	 * Donne le XML de l'etat d'impression des étiquettes
	 * 
	 * @param nblarg -
	 *            nombre de cellule sur la largeur
	 * @param nbhaut -
	 *            nombre de cellule sur la hauteur
	 * @param orientation -
	 *            orientation de la page
	 * @return String - le XML iText à retourner
	 */
	private String getXMLEtiquettes(int nblarg, int nbhaut, int depart) {
		if(concurrentList.countArcher(depart) == 0)
			return ""; //$NON-NLS-1$
		
		try {
			double marge_gauche = ConcoursJeunes.getConfiguration().getMarges().left; // la marge gauche
			double marge_droite = ConcoursJeunes.getConfiguration().getMarges().right; // la marge droite
			double marge_haut = ConcoursJeunes.getConfiguration().getMarges().top; // la marge haut
			double marge_bas = ConcoursJeunes.getConfiguration().getMarges().bottom; // la marge bas
			double espacement_cellule_h = ConcoursJeunes.getConfiguration().getEspacements()[0]; // l'espacement horizontal entre cellule
			double espacement_cellule_v = ConcoursJeunes.getConfiguration().getEspacements()[1]; // l'espacement vertical entre cellule
			double cellule_x;
			double cellule_y;
			Rectangle pageDimension = (Rectangle)PageSize.class.getField(ConcoursJeunes.getConfiguration().getFormatPapier()).get(null);
			if(ConcoursJeunes.getConfiguration().getOrientation().equals("landscape")) //$NON-NLS-1$
				pageDimension = pageDimension.rotate();
			
			espacement_cellule_h = AJToolKit.centimeterToDpi(espacement_cellule_h);
			espacement_cellule_v = AJToolKit.centimeterToDpi(espacement_cellule_v);
			marge_gauche = AJToolKit.centimeterToDpi(marge_gauche);
			marge_droite = AJToolKit.centimeterToDpi(marge_droite);
			marge_haut = AJToolKit.centimeterToDpi(marge_haut);
			marge_bas = AJToolKit.centimeterToDpi(marge_bas);

			double zoneaffichable_x = pageDimension.getWidth() - marge_gauche - marge_droite;
			double zoneaffichable_y = pageDimension.getHeight() - marge_haut - marge_bas;
			
			cellule_x = (zoneaffichable_x - (espacement_cellule_h * (nblarg - 1.0))) / zoneaffichable_x * 100 / nblarg - 7;
			cellule_y = (zoneaffichable_y - (espacement_cellule_v * (nbhaut - 1.0))) / zoneaffichable_y * 100 / nbhaut;
	
			String tailles_x = 0.1 + ""; //$NON-NLS-1$
			for (int i = 0; i < nblarg; i++) {
				tailles_x += ";" + cellule_x + ";7"; //$NON-NLS-1$ //$NON-NLS-2$
				if (i < nblarg - 1)
					tailles_x += ";" + espacement_cellule_h / zoneaffichable_x * 100; //$NON-NLS-1$
			}
	
			templateEtiquettesXML.reset();
	
			templateEtiquettesXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			templateEtiquettesXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("author", ConcoursJeunes.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("pagesize", ConcoursJeunes.getConfiguration().getFormatPapier()); //$NON-NLS-1$
			templateEtiquettesXML.parse("orientation", ConcoursJeunes.getConfiguration().getOrientation()); //$NON-NLS-1$
			templateEtiquettesXML.parse("top", "" + AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().top)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("bottom", "" + AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().bottom)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("left", "" + AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().left)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("right", "" + AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().right)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$
	
			int colonne = 0;
			int ligne = 0;
			for (Concurrent concurrent : ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS)) {
				
				if (colonne == 0)
					if(ligne < nbhaut - 1)
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) + espacement_cellule_v)); //$NON-NLS-1$ //$NON-NLS-2$
					else
						templateEtiquettesXML.parse("page.ligne.leading", "" + (zoneaffichable_y * (cellule_y / 100.0) - 1)); //$NON-NLS-1$ //$NON-NLS-2$
				templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrent.getID()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrent.getClub().getNom()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrent.getNumLicenceArcher()); //$NON-NLS-1$
				templateEtiquettesXML.parse("page.ligne.colonne.emplacement", new TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString()); //$NON-NLS-1$
				if (colonne + 1 == nblarg)
					templateEtiquettesXML.parseBloc("page.ligne.colonne.interbloc", ""); //$NON-NLS-1$ //$NON-NLS-2$
	
				templateEtiquettesXML.loopBloc("page.ligne.colonne"); //$NON-NLS-1$
	
				colonne = (++colonne) % nblarg;
				if (colonne == 0) {
					templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
					ligne++;
				}
	
				if (ligne == nbhaut) {
					templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$
	
					templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
					templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$
	
					ligne = 0;
				}
			}
	
			if (colonne != 0) {
				templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
			}
			if (ligne != 0) {
				templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$
			}
		} catch (SecurityException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}

		return templateEtiquettesXML.output();
	}

	/**
	 * genere la sortie XML du pas de tir
	 * 
	 * @return la sortie XML du pas de tir
	 */
	private String getXMLPasDeTir(int depart) {
		
		if(concurrentList.countArcher(depart) == 0)
			return ""; //$NON-NLS-1$

		templatePasDeTirXML.reset();

		templatePasDeTirXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$

		int colonne = 0;

		double nbseq = 10.0 * Math.ceil(parametre.getNbCible() / 10.0);
		for (int i = 1; i <= nbseq; i++) {
			// Cible cible = null;

			Concurrent[] concurrents = concurrentList.list(i, depart);
			if (concurrents != null && concurrents.length > 0) {
				CriteriaSet dci = concurrents[0].getCriteriaSet();
				DistancesEtBlason db = parametre.getReglement().getDistancesEtBlasonFor(dci.getFilteredCriteriaSet(parametre.getReglement().getPlacementFilter()));

				templatePasDeTirXML.parse("ligne.numcible.nc.numcible", "" + i); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.imgcible.ic.url_img_blason", //$NON-NLS-1$
						ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + "/cible.jpg"); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.detail.dc.distance", "" + db.getDistance()[0]); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.detail.dc.blason", "" + db.getTargetFace().getName()); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				templatePasDeTirXML.parseBloc("ligne.numcible.nc", ""); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parseBloc("ligne.imgcible.ic", ""); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parseBloc("ligne.detail.dc", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			templatePasDeTirXML.loopBloc("ligne.numcible"); //$NON-NLS-1$
			templatePasDeTirXML.loopBloc("ligne.imgcible"); //$NON-NLS-1$
			templatePasDeTirXML.loopBloc("ligne.detail"); //$NON-NLS-1$

			if (colonne == 9) {
				templatePasDeTirXML.loopBloc("ligne"); //$NON-NLS-1$
				colonne = 0;
			} else {
				colonne++;
			}
		}

		if (colonne != 0) {
			templatePasDeTirXML.loopBloc("ligne"); //$NON-NLS-1$
		}

		// System.out.println(templatePasDeTirXML.output());
		return templatePasDeTirXML.output();
	}

	/**
	 * Genere l'etat classement pour la fiche en parametre
	 * 
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printClassement() {
		// parametrage d'une page au format A4 avec marges 1cm/1cm/1cm/6.5cm
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		String classement = getClassement(OUT_XML);

		if (!classement.equals("")) { //$NON-NLS-1$
			return ConcoursJeunes.printDocument(document, classement);
		}
		return false;
	}

	/**
	 * Genere l'etat classement equipe pour la fiche en parametre
	 * 
	 * @return true si impression avec succee, false sinon
	 */
	public boolean printClassementEquipe() {
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		String classementEquipe = getClassementEquipe(OUT_XML);
		if (!classementEquipe.isEmpty())
			return ConcoursJeunes.printDocument(document, classementEquipe);
		return false;
	}
	
	/**
	 * Genere l'etat classement club pour la fiche en parametre
	 * 
	 * @return true si impression avec succé, false sinon
	 */
	public boolean printClassementClub() {
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		String classementClub = getClassementClub(OUT_XML);
		if (!classementClub.equals("")) //$NON-NLS-1$
			return ConcoursJeunes.printDocument(document, classementClub);
		return false;
	}

	/**
	 * Genere l'etat "liste des archers"
	 * 
	 * @param mode -
	 *            pour le greffe (GREFFE) ou pour affichage (ALPHA ou TARGET)
	 * 
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printArcherList(int mode) {
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		return ConcoursJeunes.printDocument(document, getXMLListeArcher(mode, currentDepart));
	}

	/**
	 * genere l'etat d'impression des etiquettes
	 * 
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printEtiquettes() {
		float marge_gauche = AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().left); // la marge gauche
		float marge_droite = AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().right); // la marge droite
		float marge_haut = AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().top); // la marge haut
		float marge_bas = AJToolKit.centimeterToDpi(ConcoursJeunes.getConfiguration().getMarges().bottom); // la marge bas
		Document document = new Document(PageSize.A4, marge_gauche, marge_droite, marge_haut, marge_bas);
		return ConcoursJeunes.printDocument(document, getXMLEtiquettes(ConcoursJeunes.getConfiguration().getColonneAndLigne()[1], ConcoursJeunes.getConfiguration().getColonneAndLigne()[0], currentDepart));
	}

	/**
	 * genere l'etat d'impression du pas de tir
	 * 
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printPasDeTir() {
		Document document = new Document(PageSize.A4.rotate(), 5, 5, 5, 5);
		return ConcoursJeunes.printDocument(document, getXMLPasDeTir(currentDepart));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ParametreListener#metaDataChanged(org.concoursjeunes.ParametreEvent)
	 */
	public void metaDataChanged(ParametreEvent parametreEvent) {

	}

	/**
	 * Invoqué par l'objet parametre du concours lors d'une modification de celui ci
	 * afin de recalculer le pas de tir en fonction
	 */
	public void parametreChanged(ParametreEvent parametreEvent) {
		assert pasDeTir.size() > 0 : "Il doit exister au moins un pas de tir"; //$NON-NLS-1$

		if (parametreEvent.getParametre().getNbCible() != pasDeTir.get(0).getTargets().size() || parametreEvent.getParametre().getNbTireur() != pasDeTir.get(0).getTargets().get(0).getNbMaxArchers()) {
			//si le nombre de cible à changé reconstruire le pas de tir
			makePasDeTir();
			if (parametreEvent.getParametre().getNbCible() < pasDeTir.get(0).getTargets().size()) {
				//si le nombre de cible est inferieur, virer les archers
				//sur les cibles supprimé
				for (int i = 0; i < parametre.getNbDepart(); i++) {
					for (int j = parametreEvent.getParametre().getNbCible(); j < pasDeTir.get(i).getTargets().size(); j++) {
						for (Concurrent concurrent : concurrentList.list(j + 1, i)) {
							pasDeTir.get(i).retraitConcurrent(concurrent);
						}
					}
				}
			}
		} else if(parametreEvent.getParametre().getNbDepart() != pasDeTir.size())
			makePasDeTir();
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