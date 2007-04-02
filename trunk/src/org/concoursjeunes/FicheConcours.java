/**
 * 
 */
package org.concoursjeunes;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.event.EventListenerList;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;

import ajinteractive.standard.java2.*;

/**
 * Represente la fiche concours, regroupe l'ensemble des informations commune à un concours donné
 * 
 * @author  Aurélien Jeoffray
 */
public class FicheConcours {

	public static final int ALPHA		= 0;    //par ordre alphabetique
	public static final int GREFFE		= 1;    //pour le greffe

	public static final int OUT_XML		= 0;    //Sortie XML
	public static final int OUT_HTML	= 1;    //Sortie HTML

	private Parametre parametre			= new Parametre();
	private ConcurrentList archerlist   = new ConcurrentList(parametre);
	private EquipeList equipes			= new EquipeList();
	private Hashtable<Integer, ArrayList<Cible>> pasDeTir = new Hashtable<Integer, ArrayList<Cible>>();

	private EventListenerList ficheConcoursListeners = new EventListenerList();

	private int currentDepart		= 0;

	private static AJTemplate templateClassementXML        = new AJTemplate();
	private static AJTemplate templateClassementEquipeXML  = new AJTemplate();
	private static AJTemplate templateClassementHTML       = new AJTemplate();
	private static AJTemplate templateClassementEquipeHTML = new AJTemplate();
	private static AJTemplate templateListeArcherXML       = new AJTemplate();
	private static AJTemplate templateListeGreffeXML       = new AJTemplate();
	private static AJTemplate templateEtiquettesXML        = new AJTemplate();
	private static AJTemplate templatePasDeTirXML          = new AJTemplate();

	private Hashtable<CriteriaSet, Concurrent[]> concurrentsClasse;

	/**
	 * 
	 */
	public FicheConcours() {
		makePasDeTir();

		loadTemplates();
	}

	/**
	 * Enregistre un auditeur aux evenements d'un concours
	 * 
	 * @param ficheConcoursListener - l'auditeur à abonner
	 */
	public void addFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.add(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Supprime un auditeur des evenements du concours
	 * 
	 * @param ficheConcoursListener - l'auditeur à résilier
	 */
	public void removeFicheConcoursListener(FicheConcoursListener ficheConcoursListener) {
		ficheConcoursListeners.remove(FicheConcoursListener.class, ficheConcoursListener);
	}

	/**
	 * Retourne la liste des archers inscrit sur le concours
	 * 
	 * @return  archerlist - la liste des archers inscrit sur le concours
	 * @uml.property  name="archerlist"
	 */
	public ConcurrentList getArcherlist() {
		return archerlist;
	}

	/**
	 * Definit la liste des archers inscrit sur le concours
	 * 
	 * @param archerlist - la liste des archers inscrit sur le concours
	 * @uml.property  name="archerlist"
	 */
	public void setArcherlist(ConcurrentList archerlist) {
		this.archerlist = archerlist;
	}

	/**
	 * Donne la liste des equipes enrezgistré sur le concours
	 * 
	 * @return  equipes - la liste des équipes
	 * @uml.property  name="equipes"
	 */
	public EquipeList getEquipes() {
		return equipes;
	}

	/**
	 * Définit la liste des équipes enregistré sur le concours
	 * 
	 * @param equipes - la liste des équipes
	 * @uml.property  name="equipes"
	 */
	public void setEquipes(EquipeList equipes) {
		this.equipes = equipes;
	}

	/**
	 * Retourne les parametres propre au concours
	 * 
	 * @return les parametres du concours
	 * @uml.property  name="parametre"
	 */
	public Parametre getParametre() {
		return parametre;
	}

	/**
	 * Definit les parametres d'un concours
	 * 
	 * @param parametre - les parametres du concours
	 * @uml.property  name="parametre"
	 */
	public void setParametre(Parametre parametre) {
		this.parametre = parametre;
	}

	/**
	 * Détermine, pour un concurrent donnée, si une place est diponible sur le pas de tir
	 * 
	 * @param concurrent - le concuurent à tester
	 * @return true si une place est disponible pour le concurrent, false sinon
	 */
	public boolean havePlaceForConcurrent(Concurrent concurrent) {
		OccupationCibles place = null;
		if(!archerlist.contains(concurrent)) {
			place = getOccupationCibles(concurrent.getDepart()).get(
					DistancesEtBlason.getDistancesEtBlasonForConcurrent(parametre.getReglement(), concurrent));

			return place.getPlaceLibre() > 0 || getNbCiblesLibre(concurrent.getDepart()) > 0;
		}

		int index = archerlist.getArchList().indexOf(concurrent);
		Concurrent conc2 = archerlist.get(index);

		DistancesEtBlason db1 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(parametre.getReglement(), concurrent);
		DistancesEtBlason db2 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(parametre.getReglement(), conc2);

		//si on ne change pas de db pas de pb
		if(db1.equals(db2)) {
			return true;
		}

		//si il reste de la place dans la nouvelle categorie pas de pb
		place = getOccupationCibles(concurrent.getDepart()).get(db1);
		if(place.getPlaceLibre() > 0 || getNbCiblesLibre(concurrent.getDepart()) > 0) {
			return true;
		}

		//si le retrait du concurrent libere une cible ok
		place = getOccupationCibles(concurrent.getDepart()).get(db2);
		if(place.getPlaceOccupe() % parametre.getNbTireur() == 1) {
			return true;
		}

		//sinon changement impossible
		return false;
	}

	/**
	 * Ajoute un concurrent au concours
	 * 
	 * @param concurrent - le concurrent à ajouter au concours
	 * @return true si ajout avec succès, false sinon
	 */
	public boolean addConcurrent(Concurrent concurrent) {
		//TODO interdire seulement sur le même départ
		if(archerlist.contains(concurrent))
			return false;

		if(!havePlaceForConcurrent(concurrent))
			return false;

		archerlist.add(concurrent);

		fireConcurrentAdded(concurrent);
		silentSave();

		return true;
	}

	/**
	 * retire un concurrent du concours
	 * 
	 * @param removedConcurrent - Le concurrent à supprimer
	 * @return true si suppression avec succès, false sinon
	 */
	public boolean removeConcurrent(Concurrent removedConcurrent) {
		boolean remove = false;

		//retire le concurrent du pas de tir si present
		if(removedConcurrent.getCible() > 0)
			pasDeTir.get(removedConcurrent.getDepart()).get(removedConcurrent.getCible()-1).removeConcurrent(removedConcurrent);
		//suppression dans la liste
		//TODO controler le depart
		remove = archerlist.remove(removedConcurrent);
		if(remove) {
			fireConcurrentRemoved(removedConcurrent);
			//suppression dans l'equipe si presence dans equipe
			remove = removeConcurrentInTeam(removedConcurrent);
		}

		silentSave();

		return remove;
	}

	/**
	 * retire un concurrent de l'équipe
	 * 
	 * @param removedConcurrent - Le concurrent à supprimer de l'équipe
	 * @return true si suppression avec succès, false sinon
	 */
	public boolean removeConcurrentInTeam(Concurrent removedConcurrent) {
		boolean remove = false;

		if(equipes != null) {
			remove = equipes.removeConcurrent(parametre.getReglement(), removedConcurrent);
		}

		silentSave();

		return remove;
	}

	/**
	 * Retourne le numero du départ courrant (actuellement actif)
	 * 
	 * @return le numero du départ
	 * @uml.property  name="currentDepart"
	 */
	public int getCurrentDepart() {
		return currentDepart;
	}

	/**
	 * Définit le numero du départ courrant
	 * 
	 * @param currentDepart - le numero du départ 
	 * @uml.property  name="currentDepart"
	 */
	public void setCurrentDepart(int currentDepart) {
		this.currentDepart = currentDepart;
	}

	/**
	 * Place les archers sur le pas de tir
	 *
	 * @param depart - le numero du depart pour lequel placer les archers
	 */
	public void placementConcurrents(int depart) {
		int curCible = 1;

		//pour chaque distance/blason
		for(DistancesEtBlason distancesEtBlason : archerlist.listDistancesEtBlason(parametre.getReglement(), true, depart)) {
			//liste les archers pour le distance/blason
			Concurrent[] concurrents = ConcurrentList.sort(archerlist.list(parametre.getReglement(), distancesEtBlason, depart), ConcurrentList.SORT_BY_CLUBS);

			//defini le nombre de tireur par cible en fonction du nombre de tireurs
			//max acceptés et du nombre de tireur présent
			//FIXME calculer le taux d'occupation des cibles en fonction de chaque d/b afin d'eviter les effets de bord
			int nbTireurParCible = parametre.getNbTireur();
			for(int i = 2; i <= parametre.getNbTireur(); i+=2) {
				if(archerlist.countArcher(depart) <= parametre.getNbCible() * i) {
					nbTireurParCible = i;
				}
			}

			int startCible = curCible;
			int endCible = curCible + (concurrents.length / nbTireurParCible) 
			+ (((concurrents.length % nbTireurParCible) > 0) ? 0 : -1);

			for(int j = 0; j < concurrents.length; j++) {
				if(concurrents[j].getCible() > 0)
					pasDeTir.get(concurrents[j].getDepart()).get(concurrents[j].getCible()-1).removeConcurrent(concurrents[j]);
				pasDeTir.get(depart).get(curCible - 1).insertConcurrent(concurrents[j]);

				if(curCible < endCible)
					curCible++;
				else {
					curCible = startCible;
				}
			}

			curCible = endCible + 1;
		}

		silentSave();
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
			pasDeTir.get(concurrent.getDepart()).get(concurrent.getCible()-1).removeConcurrent(concurrent);

		//tenter le placement
		boolean success = cible.setConcurrentAt(concurrent, position);

		silentSave();

		return success;
	}

	/**
	 * Passe au concurrent suivant par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * @return Concurrent - le concurrent suivant
	 */
	public Concurrent nextConcurrent(Concurrent curConcurrent) {
		int depart = curConcurrent.getDepart();
		int cible = curConcurrent.getCible(); 
		int position = curConcurrent.getPosition();

		do {
			position++;
			if(position == parametre.getNbTireur()) {
				position = 0;
				cible++;
			}
		} while(archerlist.getConcurrentAt(depart, cible, position) == null && cible <= parametre.getNbCible());

		return archerlist.getConcurrentAt(depart, cible, position);
	}

	/**
	 * Passe au concurrent précédent par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * @return Concurrent - le concurrent précedent
	 */
	public Concurrent previousConcurrent(Concurrent curConcurrent) {
		int depart = curConcurrent.getDepart();
		int cible = curConcurrent.getCible(); 
		int position = curConcurrent.getPosition();

		do {
			position--;
			if(position == -1) {
				position = parametre.getNbTireur() - 1;
				cible--;
			}
		} while(archerlist.getConcurrentAt(depart, cible, position) == null && cible > 0);

		return archerlist.getConcurrentAt(depart, cible, position);
	}


	/**
	 * Retourne le nombre de cible libre sur un depart donné
	 * 
	 * @param depart - le depart pour lequelle retourné le nombre de cible libre
	 * @return le nombre de cible libre
	 */
	public int getNbCiblesLibre(int depart) {
		int nbCibleOccupe = 0;
		//deduit le nombre de cible libre de la table d'ocupation des cibles
		Hashtable<DistancesEtBlason, OccupationCibles> occupationCibles = getOccupationCibles(depart);
		//decompte les cibles occupés
		Enumeration<OccupationCibles> ocEnum = occupationCibles.elements();
		while(ocEnum.hasMoreElements()) {
			OccupationCibles oc = ocEnum.nextElement();

			nbCibleOccupe += (oc.getPlaceOccupe() + oc.getPlaceLibre()) / parametre.getNbTireur();
		}
		//nb cible total - nb cible occupe = nb cible libre
		return parametre.getNbCible() - nbCibleOccupe;
	}

	/**
	 * Retourne la table d'occupation des cibles pour un départ donné
	 * 
	 * @param depart - le depart concerné
	 * @return la table d'occupation des cibles
	 */
	public Hashtable<DistancesEtBlason, OccupationCibles> getOccupationCibles(int depart) {
		return calculatePlaceDisponible(depart);
	}

	/**
	 * calcul la place occupé et disponible sur le concours
	 *
	 * @param depart - le départ pour lequelle tester la place dispo
	 * @return la table d'occupation pour le depart
	 */
	private Hashtable<DistancesEtBlason, OccupationCibles> calculatePlaceDisponible(int depart) {
		Hashtable<DistancesEtBlason, OccupationCibles> occupationCibles = new Hashtable<DistancesEtBlason, OccupationCibles>();

		//recupere le nombre d'archer total possible
		int placeLibreSurCible = 0;
		int[] nbParDistanceBlason;

		//recupere dans la configuration la correspondance Critères de distinction/Distance-Blason
		Hashtable<CriteriaSet, DistancesEtBlason> correspSCNA_DB = parametre.getReglement().getCorrespondanceCriteriaSet_DB();

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

			nbParDistanceBlason[i] = archerlist.countArcher(parametre.getReglement(), distblas, depart);

			placeLibreSurCible = (parametre.getNbTireur() - nbParDistanceBlason[i] % parametre.getNbTireur()) 
			% parametre.getNbTireur();

			occupationCibles.put(distblas, new OccupationCibles(placeLibreSurCible, nbParDistanceBlason[i]));

			i++;
		}

		return occupationCibles;
	}

	/**
	 * Construit le pas de tir en fonction du nombre de depart et du nombre de cible<br>
	 * Sert à la regeneration d'une fiche après deserialisation
	 *
	 */
	private void makePasDeTir() {
		for(int i = 0; i < parametre.getNbDepart(); i++) {
			ArrayList<Cible> departCibles = new ArrayList<Cible>();
			for(int j = 0; j < parametre.getNbCible(); j++) {
				Cible cible = new Cible(j+1, this);
				if(archerlist.countArcher(i) > 0) {
					for(Concurrent concurrent : archerlist.list(j + 1, i))
						cible.setConcurrentAt(concurrent, concurrent.getPosition());
				}
				departCibles.add(cible);
			}
			pasDeTir.put(i, departCibles);
		}
	}

	/**
	 * @return pasDeTir
	 */
	public ArrayList<Cible> getPasDeTir(int depart) {
		return pasDeTir.get(depart);
	}

	/**
	 * Donne la fiche complete du concours pour sauvegarde
	 * 
	 * @return Object[]
	 */
	public Object[] getFiche() {
		Object[] fiche = {
				parametre,
				archerlist,
				equipes
		};
		return fiche;
	}

	/**
	 * Restaure une fiche concours
	 * 
	 * @param fiche - la fiche à restaurer
	 */
	public void setFiche(Object[] fiche) {
		parametre = (Parametre)fiche[0];
		archerlist = (ConcurrentList)fiche[1];
		equipes = (EquipeList)fiche[2];

		makePasDeTir();
	}

	/**
	 * sauvegarde "silencieuse" en arriere plan de la fiche concours
	 *
	 */
	public void silentSave() {
		File f = new File(ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil())
				+ File.separator + parametre.getSaveName());
		AJToolKit.saveXMLStructure(f, getFiche(), true);

		/*for(MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
			String filenameConcours = metaDataFicheConcours.getFilenameConcours();
			if(filenameConcours.equals(parametre.getSaveName())) {
				metaDataFicheConcours.setDateConcours(parametre.getDate());
				metaDataFicheConcours.setIntituleConcours(parametre.getIntituleConcours());
				break;
			}
		}*/
	}

	/**
	 * methode pour le classement des candidats
	 * 
	 * @param depart
	 */ 
	private void classement(int depart) {

		concurrentsClasse = new Hashtable<CriteriaSet, Concurrent[]>();

		//Etablit le classement des concurrents en fonction du nombre de points obtenue.
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(parametre.getReglement(), 
				parametre.getReglement().getClassementFilter());

		//Affectation des valeurs
		for(int i = 0; i < catList.length; i++) {
			//sort la liste des concurrents correspondant aux critéres de recherche
			ArrayList<Concurrent> unsortList = new ArrayList<Concurrent>();
			for(int j = 0; j <= depart; j++) {
				for(Concurrent concurrent : archerlist.list(catList[i], j))
					unsortList.add(concurrent);
			}
			Concurrent[] sortList = ConcurrentList.sort(unsortList.toArray(new Concurrent[unsortList.size()]), ConcurrentList.SORT_BY_POINTS);
			if(sortList.length > 0)
				concurrentsClasse.put(catList[i], sortList);
		}
	}

	/**
	 * Chargement des template de sortie XML 
	 *
	 */
	private static void loadTemplates() {
		templateClassementXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement.xml")); //$NON-NLS-1$

		templateClassementHTML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement.html")); //$NON-NLS-1$

		templateClassementEquipeXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement_equipe.xml")); //$NON-NLS-1$

		templateClassementEquipeHTML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement_equipe.html")); //$NON-NLS-1$

		templateListeArcherXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.listarcher")); //$NON-NLS-1$

		templateListeGreffeXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.listarcher.greffe")); //$NON-NLS-1$

		templateEtiquettesXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.etiquettes")); //$NON-NLS-1$

		templatePasDeTirXML.loadTemplate(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ConcoursJeunes.ajrParametreAppli.getResourceString("template.pasdetir")); //$NON-NLS-1$
	}

	/**
	 * Donne le XML de l'etat de classement individuel
	 * 
	 * @param outType - le type de sortie à produire
	 * @param depart
	 * 
	 * @return String - le XML iText à retourner
	 */
	public String getClassement(int outType, int depart) {
		String strClassement = ""; //$NON-NLS-1$
		if(archerlist != null && archerlist.countArcher() > 0) {
			classement(depart);

			AJTemplate tplClassement = null;
			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			switch(outType) {
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
			tplClassement.parse("DATE_CONCOURS", parametre.getDate().toString()); //$NON-NLS-1$
			tplClassement.parse("author", parametre.getClub().getNom()); //$NON-NLS-1$

			for(String arbitre : parametre.getArbitres()) {
				if(arbitre.startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitre.substring(1);
				else {
					if(!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre;
				}
			}

			tplClassement.parse("ARBITRE_RESPONSABLE", strArbitreResp); //$NON-NLS-1$
			tplClassement.parse("ARBITRES_ASSISTANT", strArbitresAss); //$NON-NLS-1$
			//TODO compter le nombre d'incrit selon la règle nb depart 0 à curdepart 
			tplClassement.parse("NB_CLUB", "" + archerlist.countCompagnie(depart)); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("NB_TIREURS", "" + archerlist.countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassement.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.individuel")); //$NON-NLS-1$ //$NON-NLS-2$

			//Entete de categorie
			Enumeration<CriteriaSet> scnalst = concurrentsClasse.keys();

			CriteriaSet[] scnaUse = new CriteriaSet[concurrentsClasse.size()];
			for(int i = 0; scnalst.hasMoreElements(); i++) {
				scnaUse[i] = scnalst.nextElement();
			}

			CriteriaSet.sortCriteriaSet(scnaUse, parametre.getReglement().getListCriteria());

			for(CriteriaSet scna : scnaUse) {

				Concurrent[] sortList = concurrentsClasse.get(scna);

				String strSCNA;

				if(sortList.length > 0) {

					double tailleChampDistance = 10.5262 / parametre.getReglement().getNbSerie();
					String strTailleChampsDistance = ""; //$NON-NLS-1$
					for(int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						strTailleChampsDistance += tailleChampDistance + ";"; //$NON-NLS-1$
					}

					strSCNA = new CriteriaSetLibelle(scna).toString();

					tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance); //$NON-NLS-1$
					tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
					tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + parametre.getReglement().getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
					tplClassement.parse("categories.NB_TIREURS", "" + sortList.length); //$NON-NLS-1$ //$NON-NLS-2$

					//FIXME gerer les cas ou plus d'une distance par critères de classement
					for(int j = 0; j < parametre.getReglement().getNbSerie(); j++) {
						tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
								parametre.getReglement().getCorrespondanceCriteriaSet_DB(scna).getDistance()[j] + "m"); //$NON-NLS-1$
						tplClassement.loopBloc("categories.distances"); //$NON-NLS-1$
					}

					for(int j = 0; j < sortList.length; j++) {
						//test d'ex-Eaquo
						if ((j < sortList.length - 1
								&& sortList[j].getTotalScore() > 0
								&& sortList[j].getTotalScore() == sortList[j + 1].getTotalScore()
								&& ConcoursJeunes.configuration.isInterfaceAffResultatExEquo())
								|| (j > 0
										&& sortList[j].getTotalScore() > 0
										&& sortList[j].getTotalScore() == sortList[j - 1]
										                                           .getTotalScore() && ConcoursJeunes.configuration
										                                           .isInterfaceAffResultatExEquo())) {

							if ((sortList[j].getManque() == 0
									&& sortList[j].getDix() == 0 && sortList[j]
									                                         .getNeuf() == 0)
									                                         || (j < sortList.length - 2 && sortList[j].getManque() == sortList[j + 1]
									                                                                                                            .getManque()
									                                                                                                            && sortList[j].getDix() == sortList[j + 1]
									                                                                                                                                                .getDix() && sortList[j]
									                                                                                                                                                                      .getNeuf() == sortList[j + 1].getNeuf())
									                                                                                                                                                                      || (j > 0 && sortList[j].getManque() == sortList[j - 1]
									                                                                                                                                                                                                                       .getManque()
									                                                                                                                                                                                                                       && sortList[j].getDix() == sortList[j - 1]
									                                                                                                                                                                                                                                                           .getDix() && sortList[j]
									                                                                                                                                                                                                                                                                                 .getNeuf() == sortList[j - 1].getNeuf())) {

								tplClassement.parse(
										"categories.classement.COULEUR", //$NON-NLS-1$
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

						for(Criterion key : parametre.getReglement().getListCriteria())
							tplClassement.parse("categories.classement." //$NON-NLS-1$
									+ key.getCode(), sortList[j].getCriteriaSet().getCriterionElement(
											key).getCode());

						for(int k = 0; k < parametre.getReglement().getNbSerie(); k++) {
							if(sortList[j].getScore() != null)
								tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList[j].getScore().get(k)); //$NON-NLS-1$ //$NON-NLS-2$
							else
								tplClassement.parse("categories.classement.scores.PT_DISTANCE", "0"); //$NON-NLS-1$ //$NON-NLS-2$

							tplClassement.loopBloc("categories.classement.scores"); //$NON-NLS-1$
						}
						tplClassement.parse("categories.classement.TOTAL", ""+sortList[j].getTotalScore()); //$NON-NLS-1$ //$NON-NLS-2$
						tplClassement.parse("categories.classement.0_10_9", sortList[j].getManque() //$NON-NLS-1$
								+ "-" + sortList[j].getDix() + "-" + sortList[j].getNeuf()); //$NON-NLS-1$ //$NON-NLS-2$

						tplClassement.loopBloc("categories.classement"); //$NON-NLS-1$
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
	 * @param outType - le type de sortie à produire
	 * @param depart
	 * 
	 * @return String - le XML iText à retourner
	 */
	public String getClassementEquipe(int outType) {
		System.out.println("Sortie Equipes"); //$NON-NLS-1$

		String strClassementEquipe = ""; //$NON-NLS-1$

		if(equipes != null && equipes.countEquipes() > 0) {
			AJTemplate tplClassementEquipe = null;
			switch(outType) {
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

			//classement sortie XML
			tplClassementEquipe.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
			tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			tplClassementEquipe.parse("INTITULE_CLUB", parametre.getClub().getNom()); //$NON-NLS-1$
			tplClassementEquipe.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
			tplClassementEquipe.parse("VILLE_CLUB", parametre.getClub().getVille()); //$NON-NLS-1$
			tplClassementEquipe.parse("DATE_CONCOURS", parametre.getDate().toString()); //$NON-NLS-1$

			String strArbitreResp = ""; //$NON-NLS-1$
			String strArbitresAss = ""; //$NON-NLS-1$

			for(String arbitre : parametre.getArbitres()) {
				if(arbitre.startsWith("*")) //$NON-NLS-1$
					strArbitreResp = arbitre.substring(1);
				else {
					if(!strArbitresAss.equals("")) //$NON-NLS-1$
						strArbitresAss += ", "; //$NON-NLS-1$
					strArbitresAss += arbitre;
				}
			}

			tplClassementEquipe.parse("ARBITRE_RESPONSABLE", strArbitreResp); //$NON-NLS-1$
			tplClassementEquipe.parse("ARBITRES_ASSISTANT", strArbitresAss); //$NON-NLS-1$
			tplClassementEquipe.parse("NB_CLUB", "" + archerlist.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("NB_TIREURS", "" + archerlist.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.equipe")); //$NON-NLS-1$ //$NON-NLS-2$

			tplClassementEquipe.parse("categories.CATEGORIE", ConcoursJeunes.ajrLibelle.getResourceString("equipe.composition")); //$NON-NLS-1$ //$NON-NLS-2$
			tplClassementEquipe.parse("categories.NB_EQUIPES", "" + equipes.countEquipes()); //$NON-NLS-1$ //$NON-NLS-2$


			Equipe[] sortEquipes = equipes.sort(equipes.list());

			for(int i = 0; i < sortEquipes.length; i++) {

				tplClassementEquipe.parse("categories.classement.PLACE", "" + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$

				String idsXML = ""; //$NON-NLS-1$
				String ptsXML = ""; //$NON-NLS-1$
				for(Concurrent concurrent : sortEquipes[i].getMembresEquipe()) {
					if(outType == OUT_XML) {
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
	 * @param mode - le mode (ALPHA pour liste alpabétique et GREFFE pour liste pour le greffe)
	 * @return String - le XML iText à retourner
	 */
	private String getXMLListeArcher(int mode, int depart) {
		AJTemplate listeArcherXML;
		String strArcherListeXML = ""; //$NON-NLS-1$

		if(mode == ALPHA)
			listeArcherXML = templateListeArcherXML;
		else
			listeArcherXML = templateListeGreffeXML;

		listeArcherXML.reset();

		listeArcherXML.parse("NB_PARTICIPANTS", "" + archerlist.countArcher(depart)); //$NON-NLS-1$ //$NON-NLS-2$
		listeArcherXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$

		listeArcherXML.parse("LISTE", strArcherListeXML); //$NON-NLS-1$

		for(Concurrent concurrent : ConcurrentList.sort(archerlist.list(depart), ConcurrentList.SORT_BY_NAME)) {
			listeArcherXML.parse("lignes.IDENTITEE", concurrent.getID()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.CLUB", concurrent.getClub().getNom()); //$NON-NLS-1$
			listeArcherXML.parse("lignes.NUM_LICENCE", concurrent.getNumLicenceArcher()); //$NON-NLS-1$

			for(Criterion key : parametre.getReglement().getListCriteria()) {
				listeArcherXML.parse("lignes." + key.getCode(), //$NON-NLS-1$
						concurrent.getCriteriaSet().getCriterionElement(key).getCode());
			}

			listeArcherXML.parse("lignes.PAYEE", AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.impression.inscription"), ",")[concurrent.getInscription()]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			listeArcherXML.parse("lignes.CERTIFICAT", AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.certificat"), ",")[concurrent.isCertificat() ? 0 : 1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			listeArcherXML.parse("lignes.CIBLE", concurrent.getCible() + "" + (char)('A' + concurrent.getPosition())); //$NON-NLS-1$ //$NON-NLS-2$

			listeArcherXML.loopBloc("lignes"); //$NON-NLS-1$
		}

		return listeArcherXML.output();
	}

	/**
	 * Donne le XML de l'etat d'impression des étiquettes
	 * 
	 * @param nblarg - nombre de cellule sur la largeur
	 * @param nbhaut - nombre de cellule sur la hauteur
	 * @param orientation - orientation  de la page
	 * @return String - le XML iText à retourner
	 */
	private String getXMLEtiquettes(int nblarg, int nbhaut, int depart) {
		double marge_gauche = ConcoursJeunes.configuration.getMarges().left;              //la marge gauche
		double espacement_cellule_h = ConcoursJeunes.configuration.getEspacements()[0]; //l'espacement horizontal entre cellule
		double espacement_cellule_v = ConcoursJeunes.configuration.getEspacements()[1]; //l'espacement vertical entre cellule
		double cellule_x;
		double cellule_y;

		espacement_cellule_h = espacement_cellule_h / 21 * 100;
		marge_gauche = marge_gauche / 21 * 100;
		cellule_x = (100 - (marge_gauche + (espacement_cellule_h + 7) * (nblarg - 1))) / nblarg;

		espacement_cellule_v = espacement_cellule_v / 29.7 * 100;
		cellule_y = (99 - (espacement_cellule_v * (nbhaut - 1))) / nbhaut;

		//System.out.println("taille:" + PageSize.A4.height());

		String tailles_x = marge_gauche + ""; //$NON-NLS-1$
		for(int i = 0; i < nblarg; i++) {
			tailles_x += ";" + cellule_x + ";7"; //$NON-NLS-1$ //$NON-NLS-2$
			if(i < nblarg -1)
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
		for(Concurrent concurrent : ConcurrentList.sort(archerlist.list(depart), ConcurrentList.SORT_BY_CIBLES)) {
			if(colonne == 0)
				templateEtiquettesXML.parse("page.ligne.leading", "" + (PageSize.A4.height() * (cellule_y / 100) + PageSize.A4.height() * (espacement_cellule_v / 100))); //$NON-NLS-1$ //$NON-NLS-2$
			templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrent.getID()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrent.getClub().getNom()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrent.getNumLicenceArcher()); //$NON-NLS-1$
			templateEtiquettesXML.parse("page.ligne.colonne.emplacement", concurrent.getCible() + "" + (char)('A' + concurrent.getPosition())); //$NON-NLS-1$ //$NON-NLS-2$
			if(colonne + 1 == nblarg)
				templateEtiquettesXML.parseBloc("page.ligne.colonne.interbloc", ""); //$NON-NLS-1$ //$NON-NLS-2$

			templateEtiquettesXML.loopBloc("page.ligne.colonne"); //$NON-NLS-1$

			colonne = (++colonne) % nblarg;
			if(colonne == 0) {
				templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
				ligne++;
			}

			if(ligne == nbhaut) {
				templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$

				templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
				templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$

				ligne = 0;
			}
		}

		if(colonne != 0) {
			templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
		}
		if(ligne != 0) {
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
		for(int i = 1; i <= nbseq; i++) {
			//Cible cible = null;

			Concurrent[] concurrents = archerlist.list(i, depart);
			if(concurrents != null && concurrents.length > 0) {
				CriteriaSet dci = concurrents[0].getCriteriaSet();
				DistancesEtBlason db = parametre.getReglement().getCorrespondanceCriteriaSet_DB(dci);

				templatePasDeTirXML.parse("ligne.numcible.nc.numcible", "" + i); //$NON-NLS-1$ //$NON-NLS-2$
				templatePasDeTirXML.parse("ligne.imgcible.ic.url_img_blason",  //$NON-NLS-1$
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

			if(colonne == 9) {
				templatePasDeTirXML.loopBloc("ligne"); //$NON-NLS-1$
				colonne = 0;
			} else {
				colonne++;
			}
		}

		if(colonne != 0) {
			templatePasDeTirXML.loopBloc("ligne"); //$NON-NLS-1$
		}

		//System.out.println(templatePasDeTirXML.output());
		return templatePasDeTirXML.output();
	}

	/**
	 * Genere l'etat classement pour la fiche en parametre
	 * 
	 * @param depart
	 * @return true si impression avec succe, false sinon
	 */
	public boolean printClassement() {
		//parametrage d'une page au format A4 avec marges 1cm/1cm/1cm/6.5cm
		Document document = new Document(PageSize.A4, 10, 10, 10, 65);
		String classement = getClassement(OUT_XML, currentDepart);

		if(!classement.equals("")) { //$NON-NLS-1$
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
		if(!classementEquipe.equals("")) //$NON-NLS-1$
			return ConcoursJeunes.printDocument(document, classementEquipe);
		return false;
	}

	/**
	 * Genere l'etat "liste des archers"
	 * 
	 * @param mode - pour le greffe ou pour affichage
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
		return ConcoursJeunes.printDocument(document, 
				getXMLEtiquettes(ConcoursJeunes.configuration.getColonneAndLigne()[1], 
						ConcoursJeunes.configuration.getColonneAndLigne()[0], currentDepart));
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

	private void fireConcurrentAdded(Concurrent concurrent) {
		for(FicheConcoursListener ficheConcoursListener : ficheConcoursListeners.getListeners(FicheConcoursListener.class)) {

			ficheConcoursListener.listConcurrentChanged(
					new FicheConcoursEvent(FicheConcoursEvent.ADD_CONCURRENT, concurrent));
		}
	}

	private void fireConcurrentRemoved(Concurrent concurrent) {
		for(FicheConcoursListener ficheConcoursListener : ficheConcoursListeners.getListeners(FicheConcoursListener.class)) {

			ficheConcoursListener.listConcurrentChanged(
					new FicheConcoursEvent(FicheConcoursEvent.REMOVE_CONCURRENT, concurrent));
		}
	}


}