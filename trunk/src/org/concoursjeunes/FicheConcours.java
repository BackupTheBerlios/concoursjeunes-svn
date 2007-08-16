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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.event.EventListenerList;
import javax.xml.bind.annotation.XmlRootElement;

import ajinteractive.standard.common.AJTemplate;
import ajinteractive.standard.common.AJToolKit;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;

/**
 * Represente la fiche concours, regroupe l'ensemble des informations commune à un concours donné
 * 
 * @author Aurélien Jeoffray
 */
@XmlRootElement
public class FicheConcours implements ParametreListener {

	public static final int ALPHA = 0; // par ordre alphabetique
	public static final int GREFFE = 1; // pour le greffe
	public static final int TARGET = 3; // par ordre sur le pas de tir

	public static final int OUT_XML = 0; // Sortie XML
	public static final int OUT_HTML = 1; // Sortie HTML

	private Parametre parametre = new Parametre(ConcoursJeunes.configuration);

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
	 * 
	 */
	public FicheConcours() {
		parametre.addParametreListener(this);
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
	 * @uml.property name="archerlist"
	 */
	public ConcurrentList getConcurrentList() {
		return concurrentList;
	}

	/**
	 * Donne la liste des equipes enrezgistré sur le concours
	 * 
	 * @return equipes - la liste des équipes
	 * @uml.property name="equipes"
	 */
	public EquipeList getEquipes() {
		return equipes;
	}

	public void setEquipes(EquipeList equipeList) {
		this.equipes = equipeList;
	}

	/**
	 * Retourne les parametres propre au concours
	 * 
	 * @return les parametres du concours
	 * @uml.property name="parametre"
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
	public boolean addConcurrent(Concurrent concurrent, int depart) {
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
	public void removeConcurrent(Concurrent removedConcurrent) {
		// retire le concurrent du pas de tir si present
		if (removedConcurrent.getCible() > 0)
			pasDeTir.get(removedConcurrent.getDepart()).getTargets().get(removedConcurrent.getCible() - 1).removeConcurrent(removedConcurrent);
		// suppression dans la liste
		// suppression dans l'equipe si presence dans equipe
		equipes.removeConcurrent(removedConcurrent);
		if (concurrentList.remove(removedConcurrent))
			fireConcurrentRemoved(removedConcurrent);

		save();
	}

	/**
	 * Retourne le numero du départ courrant (actuellement actif)
	 * 
	 * @return le numero du départ
	 * @uml.property name="currentDepart"
	 */
	public int getCurrentDepart() {
		return currentDepart;
	}

	/**
	 * Définit le numero du départ courrant
	 * 
	 * @param currentDepart -
	 *            le numero du départ
	 * @uml.property name="currentDepart"
	 */
	public void setCurrentDepart(int currentDepart) {
		this.currentDepart = currentDepart;
	}

	/**
	 * @return pasDeTir
	 */
	public PasDeTir getPasDeTir(int depart) {
		return pasDeTir.get(depart);
	}

	/**
	 * Donne la fiche complete du concours pour sauvegarde
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

		parametre.addParametreListener(metaDataFicheConcours);
		parametre.addParametreListener(this);

		makePasDeTir();
	}

	public MetaDataFicheConcours getMetaDataFicheConcours() {
		MetaDataFicheConcours metaDataFicheConcours = new MetaDataFicheConcours(parametre.getDate(), parametre.getIntituleConcours(), parametre.getSaveName());
		parametre.addParametreListener(metaDataFicheConcours);

		return metaDataFicheConcours;
	}

	/**
	 * sauvegarde "silencieuse" en arriere plan de la fiche concours
	 * 
	 */
	public void save() {
		File f = new File(ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil()) + File.separator + parametre.getSaveName());
		AJToolKit.saveXMLStructure(f, getFiche(), true);
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
			for (Concurrent concurrent : concurrentList.list(catList[i], -1))
				unsortList.add(concurrent);
			Concurrent[] sortList = ConcurrentList.sort(unsortList.toArray(new Concurrent[unsortList.size()]), ConcurrentList.SORT_BY_POINTS);
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
	 * Donne le XML de l'etat de classement individuel
	 * 
	 * @param outType -
	 *            le type de sortie à produire
	 * @param depart
	 * 
	 * @return String - le XML iText à retourner
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
			tplClassement.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassement.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassement.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassement.parse("VILLE_CLUB", parametre.getClub().getVille()); //$NON-NLS-1$
			tplClassement.parse("DATE_CONCOURS", DateFormat.getDateInstance(DateFormat.LONG).format(new Date())); //$NON-NLS-1$
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

			tplClassement.parse("ARBITRE_RESPONSABLE", strArbitreResp); //$NON-NLS-1$
			tplClassement.parse("ARBITRES_ASSISTANT", strArbitresAss); //$NON-NLS-1$
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

					// FIXME gerer les cas ou plus d'une distance par critères
					// de classement
					for (int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
								parametre.getReglement().getDistancesEtBlasonFor(scna).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances"); //$NON-NLS-1$
					}

					if (sortList.length > 0) {
						boolean row_exist = false;
						for (int j = 0; j < sortList.length; j++) {
							if (sortList[j].getTotalScore() > 0) {
								row_exist = true;
								// test d'ex-Eaquo
								if ((j < sortList.length - 1 && sortList[j].getTotalScore() == sortList[j + 1].getTotalScore() && ConcoursJeunes.configuration.isInterfaceAffResultatExEquo())
										|| (j > 0 && sortList[j].getTotalScore() == sortList[j - 1].getTotalScore() && ConcoursJeunes.configuration.isInterfaceAffResultatExEquo())) {

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
	 * Donne le XML de l'etat de classement par équipe
	 * 
	 * @param outType -
	 *            le type de sortie à produire
	 * @param depart
	 * 
	 * @return String - le XML iText à retourner
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
			tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", parametre.getClub().getVille()); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", parametre.getDate().toString()); //$NON-NLS-1$

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
			tplClassementEquipe.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.equipe")); //$NON-NLS-1$ //$NON-NLS-2$

			tplClassementEquipe.parse("categories.CATEGORIE", ConcoursJeunes.ajrLibelle.getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("categories.NB_EQUIPES", "" + equipes.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$

			Equipe[] sortEquipes = EquipeList.sort(equipes.list());

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
			tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", parametre.getClub().getVille()); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", parametre.getDate().toString()); //$NON-NLS-1$

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
			concurrents = ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SORT_BY_NAME);
		else
			concurrents = ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SORT_BY_CIBLES);

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
			listeArcherXML.parse("lignes.CIBLE", concurrent.getCible() + "" + (char) ('A' + concurrent.getPosition())); //$NON-NLS-1$ //$NON-NLS-2$

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
		double marge_gauche = ConcoursJeunes.configuration.getMarges().left; // la
		// marge
		// gauche
		double espacement_cellule_h = ConcoursJeunes.configuration.getEspacements()[0]; // l'espacement
		// horizontal
		// entre
		// cellule
		double espacement_cellule_v = ConcoursJeunes.configuration.getEspacements()[1]; // l'espacement
		// vertical
		// entre
		// cellule
		double cellule_x;
		double cellule_y;

		espacement_cellule_h = espacement_cellule_h / 21 * 100;
		marge_gauche = marge_gauche / 21 * 100;
		cellule_x = (100 - (marge_gauche + (espacement_cellule_h + 7) * (nblarg - 1))) / nblarg;

		espacement_cellule_v = espacement_cellule_v / 29.7 * 100;
		cellule_y = (99 - (espacement_cellule_v * (nbhaut - 1))) / nbhaut;

		// System.out.println("taille:" + PageSize.A4.height());

		String tailles_x = marge_gauche + ""; //$NON-NLS-1$
		for (int i = 0; i < nblarg; i++) {
			tailles_x += ";" + cellule_x + ";7"; //$NON-NLS-1$ //$NON-NLS-2$
			if (i < nblarg - 1)
				tailles_x += ";" + espacement_cellule_h; //$NON-NLS-1$
		}

		templateEtiquettesXML.reset();

		templateEtiquettesXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
		templateEtiquettesXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("pagesize", ConcoursJeunes.configuration.getFormatPapier()); //$NON-NLS-1$
		templateEtiquettesXML.parse("orientation", ConcoursJeunes.configuration.getOrientation()); //$NON-NLS-1$
		templateEtiquettesXML.parse("top", "" + (ConcoursJeunes.configuration.getMarges().top / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("bottom", "" + (ConcoursJeunes.configuration.getMarges().bottom / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("left", "" + (ConcoursJeunes.configuration.getMarges().left / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("right", "" + (ConcoursJeunes.configuration.getMarges().right / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
		templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$

		int colonne = 0;
		int ligne = 0;
		for (Concurrent concurrent : ConcurrentList.sort(concurrentList.list(depart), ConcurrentList.SORT_BY_CIBLES)) {
			if (colonne == 0)
				templateEtiquettesXML.parse("page.ligne.leading", "" + (PageSize.A4.height() * (cellule_y / 100) + PageSize.A4.height() * (espacement_cellule_v / 100))); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrent.getID()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrent.getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrent.getNumLicenceArcher()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.emplacement", concurrent.getCible() + "" + (char) ('A' + concurrent.getPosition())); //$NON-NLS-1$ //$NON-NLS-2$
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

		return templateEtiquettesXML.output();
	}

	/**
	 * genere la sortie XML du pas de tir
	 * 
	 * @return la sortie XML du pas de tir
	 */
	private String getXMLPasDeTir(int depart) {

		templatePasDeTirXML.reset();

		templatePasDeTirXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$

		int colonne = 0;

		double nbseq = 10.0 * Math.ceil(parametre.getNbCible() / 10.0);
		for (int i = 1; i <= nbseq; i++) {
			// Cible cible = null;

			Concurrent[] concurrents = concurrentList.list(i, depart);
			if (concurrents != null && concurrents.length > 0) {
				CriteriaSet dci = concurrents[0].getCriteriaSet();
				DistancesEtBlason db = parametre.getReglement().getDistancesEtBlasonFor(dci);

				templatePasDeTirXML.parse("ligne.numcible.nc.numcible", "" + i); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.imgcible.ic.url_img_blason", //$NON-NLS-1$
						ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + "/cible.jpg"); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.detail.dc.distance", "" + db.getDistance()[0]); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.detail.dc.blason", "" + db.getBlason()); //$NON-NLS-1$ //$NON-NLS-2$
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
	 * @param depart
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
	 * Genere l'etat classement pour la fiche en parametre
	 * 
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printClassementEquipe() {
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		String classementEquipe = getClassementEquipe(OUT_XML);
		if (!classementEquipe.equals("")) //$NON-NLS-1$
			return ConcoursJeunes.printDocument(document, classementEquipe);
		return false;
	}

	/**
	 * Genere l'etat "liste des archers"
	 * 
	 * @param mode -
	 *            pour le greffe ou pour affichage
	 * @param depart
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
	 * @param depart
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printEtiquettes() {
		Document document = new Document(PageSize.A4, 0, 0, 0, 0);
		return ConcoursJeunes.printDocument(document, getXMLEtiquettes(ConcoursJeunes.configuration.getColonneAndLigne()[1], ConcoursJeunes.configuration.getColonneAndLigne()[0], currentDepart));
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

	public void parametreChanged(ParametreEvent parametreEvent) {
		assert pasDeTir.size() > 0 : "Il doit exister au moins un pas de tir"; //$NON-NLS-1$

		if (parametreEvent.getParametre().getNbCible() != pasDeTir.get(0).getTargets().size() || parametreEvent.getParametre().getNbTireur() != pasDeTir.get(0).getTargets().get(0).getNbMaxArchers()) {
			if (parametreEvent.getParametre().getNbCible() < pasDeTir.get(0).getTargets().size()) {
				for (int i = 0; i < parametre.getNbDepart(); i++) {
					for (int j = parametreEvent.getParametre().getNbCible(); j < pasDeTir.get(i).getTargets().size(); j++) {
						for (Concurrent concurrent : concurrentList.list(j + 1, i)) {
							pasDeTir.get(i).retraitConcurrent(concurrent);
						}
					}
				}
			}
			makePasDeTir();
		}
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