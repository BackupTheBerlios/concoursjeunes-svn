/**
 * 
 */
package ajinteractive.concours;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;

import ajinteractive.standard.java2.*;

/**
 * @author xp
 *
 */
public class FicheConcours {
    
    public static final int ALPHA      = 0;    //par ordre alphabetique
    public static final int GREFFE     = 1;    //pour le greffe
    
    public static final int OUT_XML    = 0;    //Sortie XML
    public static final int OUT_HTML   = 1;    //Sortie HTML

    public Parametre parametre     = new Parametre();
    public ArcherList archerlist   = new ArcherList(this);
    public EquipeList equipes      = new EquipeList(this);
    
    private static AJTemplate templateClassementXML        = new AJTemplate();
    private static AJTemplate templateClassementEquipeXML  = new AJTemplate();
    private static AJTemplate templateClassementHTML       = new AJTemplate();
    private static AJTemplate templateClassementEquipeHTML = new AJTemplate();
    private static AJTemplate templateListeArcherXML       = new AJTemplate();
    private static AJTemplate templateListeGreffeXML       = new AJTemplate();
    private static AJTemplate templateEtiquettesXML        = new AJTemplate();
    private static AJTemplate templatePasDeTirXML          = new AJTemplate();
    
    private Hashtable<DifferentiationCriteria, Concurrent[]> concurrentsClasse;
    
    /**
     * 
     */
    public FicheConcours() {
        parametre.setIntituleConcours(ConcoursJeunes.configuration.getIntituleConcours());
        parametre.setNbTireur(ConcoursJeunes.configuration.getNbTireur());
        parametre.setSaveName(System.currentTimeMillis() + ConcoursJeunes.ajrParametreAppli.getResourceString("extention.concours")); //$NON-NLS-1$
        parametre.setNbDepart(ConcoursJeunes.configuration.getNbDepart());
        parametre.setNbCible(ConcoursJeunes.configuration.getNbCible());
        parametre.setNomClub(ConcoursJeunes.configuration.getNomClub());
        parametre.setNumAgrement(ConcoursJeunes.configuration.getNumAgrement());
        parametre.setNbSerie(ConcoursJeunes.configuration.getNbSerie());
        parametre.setNbVoleeParSerie(ConcoursJeunes.configuration.getNbVoleeParSerie());
        parametre.setNbFlecheParVolee(ConcoursJeunes.configuration.getNbFlecheParVolee());
        parametre.setNbMembresEquipe(ConcoursJeunes.configuration.getNbMembresEquipe());
        parametre.setNbMembresRetenu(ConcoursJeunes.configuration.getNbMembresRetenu());
        parametre.setListCriteria(ConcoursJeunes.configuration.getListCriteria());
        parametre.setCriteriaPopulation(ConcoursJeunes.configuration.getCriteriaPopulation());
        parametre.setCorrespondanceDifferentiationCriteria_DB(ConcoursJeunes.configuration.getCorrespondanceDifferentiationCriteria_DB());
        
        loadTemplates();
    }
    
    /**
     * Ajoute un concurrent au concours
     * 
     * @param concurrent
     */
    public void addConcurrent(Concurrent concurrent) {
        archerlist.add(concurrent);

        silentSave();
    }
    
    /**
     * retire un concurrent du concours
     * 
     * @param removedConcurrent
     * @return boolean - true si supprimer avec succés, false si non trouvé
     */
    public boolean removeConcurrent(Concurrent removedConcurrent) {
        boolean remove = false;
        
        //suppression dans la liste
        remove = archerlist.remove(removedConcurrent);
        if(remove) {
            //suppression dans l'equipe si presence dans equipe
            remove = removeConcurrentInTeam(removedConcurrent);

            silentSave();
        }
        
        return remove;
    }
    
    /**
     * retire un concurrent de l'équipe
     * 
     * @param removedConcurrent
     * @return boolean - true si supprimer avec succés, false si non trouvé
     */
    public boolean removeConcurrentInTeam(Concurrent removedConcurrent) {
        boolean remove = false;
        
        if(equipes != null) {
            remove = equipes.removeConcurrent(removedConcurrent);
        }
        
        silentSave();
        
        return remove;
    }
    
    /**
     * Place les archers sur le pas de tir
     *
     */
    public void placementConcurrent() {
        int curCible = 1;
        int curPosition = 0;
        
        //retire tous les archers déjà placé
        for(int i = 0; i < archerlist.countArcher(); i++) {
            archerlist.get(i).setCible(0);
        }
        
        //pour chaque distance/blason
        for(DistancesEtBlason distancesEtBlason : archerlist.listDistancesEtBlason(true)) {
            //liste les archers pour le distance/blason
            Concurrent[] concurrents = ArcherList.sort(archerlist.list(distancesEtBlason), ArcherList.SORT_BY_CLUBS);
            
            //defini le nombre de tireur par cible en fonction du nombre de tireurs
            //max acceptés et du nombre de tireur présent
            int nbTireurParCible = parametre.getNbTireur();
            if(nbTireurParCible > 2 && archerlist.countArcher() <= parametre.getNbCible() * 2) {
                nbTireurParCible = 2;
            } else if(nbTireurParCible > 4 && archerlist.countArcher() <= parametre.getNbCible() * 4) {
                nbTireurParCible = 4;
            }
            
            int startCible = curCible;
            int endCible = curCible + (concurrents.length / nbTireurParCible) 
                    + (((concurrents.length % nbTireurParCible) > 0) ? 0 : -1);
            
            for(int j = 0; j < concurrents.length; j++) {
                concurrents[j].setCible(curCible);
                concurrents[j].setPosition(curPosition);
                
                if(curCible < endCible)
                    curCible++;
                else {
                    curPosition++;
                    curCible = startCible;
                }
            }

            curCible = endCible + 1;
            curPosition = 0;
        }
        
        silentSave();
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
        archerlist = (ArcherList)fiche[1];
        equipes = (EquipeList)fiche[2];
    }
    
    /**
     * Donne la fiche parametre et concurrent pour export vers site web
     * 
     * @return Object[]
     */
    public String getExport() {
        String strExport = ""; //$NON-NLS-1$
        
        strExport += parametre.getNumAgrement() + ";"; //$NON-NLS-1$
        strExport += parametre.getNomClub() + ";"; //$NON-NLS-1$
        Entite entite = ConcoursJeunes.listeEntite.get(parametre.getNumAgrement());
        strExport += ((entite != null) ? entite.getAdresse() : "") + ";"; //$NON-NLS-1$
        strExport += ((entite != null) ? entite.getCodePostal() : "") + ";"; //$NON-NLS-1$
        strExport += ((entite != null) ? entite.getVille() : "") + ";"; //$NON-NLS-1$
        strExport += parametre.getIntituleConcours() + ";"; //$NON-NLS-1$
        strExport += parametre.getDate() + ";"; //$NON-NLS-1$
        for(String arbitre : parametre.getArbitres())
            strExport += arbitre + ","; //$NON-NLS-1$
        strExport = strExport.substring(0, strExport.length() - 1);
        strExport += "\n"; //$NON-NLS-1$
        
        //TODO revoir l'export vers internet
        for(Concurrent concurrent : archerlist.list()) {
            strExport += concurrent.getLicence() + ";"; //$NON-NLS-1$
            strExport += concurrent.getNom() + ";"; //$NON-NLS-1$
            strExport += concurrent.getPrenom() + ";"; //$NON-NLS-1$
            strExport += concurrent.getDifferentiationCriteria().getCriterion("genre") + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            strExport += concurrent.getDifferentiationCriteria().getCriterion("categorie") + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            strExport += concurrent.getDifferentiationCriteria().getCriterion("niveau") + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            strExport += concurrent.getDifferentiationCriteria().getCriterion("arc") + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            strExport += concurrent.getClub() + ";"; //$NON-NLS-1$
            strExport += concurrent.getAgrement() + ";"; //$NON-NLS-1$
            for(int points : concurrent.getScore())
                strExport += points + ","; //$NON-NLS-1$
            strExport = strExport.substring(0, strExport.length() - 1);
            strExport += ";" + concurrent.getNeuf() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            strExport += concurrent.getDix() + ";"; //$NON-NLS-1$
            strExport += concurrent.getManque() + "\n"; //$NON-NLS-1$
        }
        strExport = strExport.substring(0, strExport.length() - 1);
        
        return strExport;
    }
    
    /**
     * sauvegarde "silencieuse" en arriere plan de la fiche concours
     *
     */
    public void silentSave() {
        File f = new File(ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil())
                + File.separator + parametre.getSaveName());
        AJToolKit.saveXMLStructure(f, getFiche(), true);
    }
    
    /**
     * methode pour le classement des candidats
     * 
     */ 
    private void classement() {
        
        concurrentsClasse = new Hashtable<DifferentiationCriteria, Concurrent[]>();
        
        Hashtable<String, Boolean> criteriaFilter = new Hashtable<String, Boolean>();
        for(Criterion criterion : parametre.getListCriteria()) {
            criteriaFilter.put(criterion.getCode(), criterion.isClassement());
        }
        
        //Etablit le classement des concurrents en fonction du nombre de points obtenue.
        DifferentiationCriteria[] catList = DifferentiationCriteria.listeDifferentiationCriteria(
                criteriaFilter);
        
        //Affectation des valeurs
        for(int i = 0; i < catList.length; i++) {
            //sort la liste des concurrents correspondant aux critéres de recherche
            Concurrent[] sortList = ArcherList.sort(archerlist.list(catList[i]), ArcherList.SORT_BY_POINTS);
            if(sortList.length > 0)
                concurrentsClasse.put(catList[i], sortList);
        }
    }
    
    /**
     * Chargement des template de sortie XML 
     *
     */
    private static void loadTemplates() {
        templateClassementXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement.xml")); //$NON-NLS-1$
        
        templateClassementHTML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement.html")); //$NON-NLS-1$
        
        templateClassementEquipeXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement_equipe.xml")); //$NON-NLS-1$
        
        templateClassementEquipeHTML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.classement_equipe.html")); //$NON-NLS-1$

        templateListeArcherXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
            + File.separator 
            + ConcoursJeunes.ajrParametreAppli.getResourceString("template.listarcher")); //$NON-NLS-1$
    
        templateListeGreffeXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
            + File.separator 
            + ConcoursJeunes.ajrParametreAppli.getResourceString("template.listarcher.greffe")); //$NON-NLS-1$
        
        templateEtiquettesXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.etiquettes")); //$NON-NLS-1$
        
        templatePasDeTirXML.loadTemplate(ConcoursJeunes.configuration.getRessourcesPath() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("template.pasdetir")); //$NON-NLS-1$
    }
    
    /**
     * Donne le XML de l'etat de classement individuel
     * 
     * @param outType - le type de sortie qui doit être retourné
     * @return String - le XML iText à retourner
     */
    public String getClassement(int outType) {
        String strClassement = ""; //$NON-NLS-1$
        if(archerlist != null && archerlist.countArcher() > 0) {
            classement();
            
            AJTemplate tplClassement = null;
            String strArbitreResp = ""; //$NON-NLS-1$
            String strArbitresAss = ""; //$NON-NLS-1$
    
            if(outType == OUT_XML) {
                    tplClassement = templateClassementXML;
            } else if(outType == OUT_HTML) {
                    tplClassement = templateClassementHTML;
            }
            
            tplClassement.reset();
            
            tplClassement.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
            tplClassement.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            tplClassement.parse("INTITULE_CLUB", parametre.getNomClub()); //$NON-NLS-1$
            tplClassement.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
            Entite entite = ConcoursJeunes.listeEntite.get(parametre.getNumAgrement());
            tplClassement.parse("VILLE_CLUB", ((entite != null) ? entite.getVille() : "")); //$NON-NLS-1$
            tplClassement.parse("DATE_CONCOURS", parametre.getDate()); //$NON-NLS-1$
            tplClassement.parse("author", parametre.getNomClub()); //$NON-NLS-1$
            
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
            tplClassement.parse("NB_CLUB", "" + archerlist.countCompagnie()); //$NON-NLS-1$ //$NON-NLS-2$
            tplClassement.parse("NB_TIREURS", "" + archerlist.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
            tplClassement.parse("TYPE_CLASSEMENT", ConcoursJeunes.ajrLibelle.getResourceString("classement.individuel")); //$NON-NLS-1$ //$NON-NLS-2$
           
            //Entete de categorie
            Enumeration<DifferentiationCriteria> scnalst = concurrentsClasse.keys();
            
            DifferentiationCriteria[] scnaUse = new DifferentiationCriteria[concurrentsClasse.size()];
            for(int i = 0; scnalst.hasMoreElements(); i++) {
                scnaUse[i] = scnalst.nextElement();
            }
            
            DifferentiationCriteria.sortDifferentiationCriteria(scnaUse, parametre.getListCriteria());
            
    
            for(DifferentiationCriteria scna : scnaUse) {
                
                Concurrent[] sortList = concurrentsClasse.get(scna);
    
                String strSCNA;
                
                if(sortList.length > 0) {
                    
                        double tailleChampDistance = 10.5262 / parametre.getNbSerie();
                    String strTailleChampsDistance = ""; //$NON-NLS-1$
                    for(int j = 0; j < parametre.getNbSerie(); j++) {
                        strTailleChampsDistance += tailleChampDistance + ";"; //$NON-NLS-1$
                    }
                    
                    strSCNA = new DifferentiationCriteriaLibelle(scna).toString();
                    
                    tplClassement.parse("categories.TAILLE_CHAMPS_DISTANCE", strTailleChampsDistance); //$NON-NLS-1$
                    tplClassement.parse("categories.CATEGORIE", strSCNA); //$NON-NLS-1$
                    tplClassement.parse("categories.NB_TIREUR_COLS", "" + (4 + parametre.getNbSerie())); //$NON-NLS-1$ //$NON-NLS-2$
                    tplClassement.parse("categories.NB_TIREURS", "" + sortList.length); //$NON-NLS-1$ //$NON-NLS-2$
                    
                    for(int j = 0; j < parametre.getNbSerie(); j++) {
                        scna.setFiltreCriteria(parametre.getPlacementFilter());
                        
                            tplClassement.parse("categories.distances.DISTANCE", //$NON-NLS-1$
                                    parametre.getCorrespondanceDifferentiationCriteria_DB(scna).getDistance()[j] + "m"); //$NON-NLS-1$
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
                        tplClassement.parse("categories.classement.CLUB", sortList[j].getClub()); //$NON-NLS-1$
                        tplClassement.parse("categories.classement.NUM_LICENCE", sortList[j].getLicence()); //$NON-NLS-1$
    
                        for(Criterion key : parametre.getListCriteria())
                            tplClassement.parse("categories.classement." //$NON-NLS-1$
                                    + key.getCode(), 
                                    parametre.getCriteriaPopulation().get(key).get(
                                            sortList[j].getDifferentiationCriteria().getCriterion(
                                                    key.getCode())).getCode()); //$NON-NLS-1$
                        
                        for(int k = 0; k < parametre.getNbSerie(); k++) {
                            if(sortList[j].getScore() != null)
                                    tplClassement.parse("categories.classement.scores.PT_DISTANCE", "" + sortList[j].getScore()[k]); //$NON-NLS-1$ //$NON-NLS-2$
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
     * @param outType - le type de sortie qui doit être retourné
     * @return String - le XML iText à retourner
     */
    public String getClassementEquipe(int outType) {
        System.out.println("Sortie Equipes"); //$NON-NLS-1$
        
        String strClassementEquipe = ""; //$NON-NLS-1$
        
        if(equipes != null && equipes.countEquipes() > 0) {
            AJTemplate tplClassementEquipe = null;
            if(outType == OUT_XML) {
                    tplClassementEquipe = templateClassementEquipeXML;
            } else if(outType == OUT_HTML) {
                tplClassementEquipe = templateClassementEquipeHTML;
            }
            
            tplClassementEquipe.reset();

            //classement sortie XML
                tplClassementEquipe.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
                tplClassementEquipe.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                tplClassementEquipe.parse("INTITULE_CLUB", parametre.getNomClub()); //$NON-NLS-1$
                tplClassementEquipe.parse("INTITULE_CONCOURS", parametre.getIntituleConcours()); //$NON-NLS-1$
                Entite entite = ConcoursJeunes.listeEntite.get(parametre.getNumAgrement());
                tplClassementEquipe.parse("VILLE_CLUB", ((entite != null) ? entite.getVille() : "")); //$NON-NLS-1$
                tplClassementEquipe.parse("DATE_CONCOURS", parametre.getDate()); //$NON-NLS-1$
                
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
    private String getXMLListeArcher(int mode) {
        AJTemplate listeArcherXML;
        String strArcherListeXML = ""; //$NON-NLS-1$
        
        if(mode == ALPHA)
            listeArcherXML = templateListeArcherXML;
        else
            listeArcherXML = templateListeGreffeXML;
        
        listeArcherXML.parse("NB_PARTICIPANTS", "" + archerlist.countArcher()); //$NON-NLS-1$ //$NON-NLS-2$
        listeArcherXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
        
        listeArcherXML.parse("LISTE", strArcherListeXML); //$NON-NLS-1$
        
        for(Concurrent concurrent : ArcherList.sort(archerlist.list(), ArcherList.SORT_BY_NAME)) {
            listeArcherXML.parse("lignes.IDENTITEE", concurrent.getID()); //$NON-NLS-1$
            listeArcherXML.parse("lignes.CLUB", concurrent.getClub()); //$NON-NLS-1$
            listeArcherXML.parse("lignes.NUM_LICENCE", concurrent.getLicence()); //$NON-NLS-1$
            
            for(Criterion key : parametre.getListCriteria()) {
                listeArcherXML.parse("lignes." + key.getCode(), //$NON-NLS-1$
                        parametre.getCriteriaPopulation().get(key).get(concurrent.getDifferentiationCriteria().getCriterion(key.getCode())).getCode());
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
    private String getXMLEtiquettes(int nblarg, int nbhaut) {
        double marge_gauche = ConcoursJeunes.configuration.getMarges().getGauche();              //la marge gauche
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
        
        templateEtiquettesXML.parse("CURRENT_TIME", DateFormat.getDateInstance(DateFormat.FULL).format(new Date())); //$NON-NLS-1$
        templateEtiquettesXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("pagesize", ConcoursJeunes.configuration.getFormatPapier()); //$NON-NLS-1$
        templateEtiquettesXML.parse("orientation", ConcoursJeunes.configuration.getOrientation()); //$NON-NLS-1$
        templateEtiquettesXML.parse("top", "" + (ConcoursJeunes.configuration.getMarges().getHaut() / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("bottom", "" + (ConcoursJeunes.configuration.getMarges().getBas() / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("left", "" + (ConcoursJeunes.configuration.getMarges().getGauche() / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("right", "" + (ConcoursJeunes.configuration.getMarges().getDroite() / 0.03527)); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("page.columns", "" + (nblarg * 3)); //$NON-NLS-1$ //$NON-NLS-2$
        templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$ //$NON-NLS-2$
        
        int colonne = 0;
        int ligne = 0;
        for(Concurrent concurrent : ArcherList.sort(archerlist.list(), ArcherList.SORT_BY_CIBLES)) {
            if(colonne == 0)
                templateEtiquettesXML.parse("page.ligne.leading", "" + (PageSize.A4.height() * (cellule_y / 100) + PageSize.A4.height() * (espacement_cellule_v / 100))); //$NON-NLS-1$ //$NON-NLS-2$
            templateEtiquettesXML.parse("page.ligne.colonne.cid", concurrent.getID()); //$NON-NLS-1$
            templateEtiquettesXML.parse("page.ligne.colonne.cclub", concurrent.getClub()); //$NON-NLS-1$
            templateEtiquettesXML.parse("page.ligne.colonne.clicence", concurrent.getLicence()); //$NON-NLS-1$
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
                templateEtiquettesXML.parse("page.widths", tailles_x); //$NON-NLS-1$ //$NON-NLS-2$
                
                ligne = 0;
            }
        }
        
        if(colonne != 0) {
            templateEtiquettesXML.loopBloc("page.ligne"); //$NON-NLS-1$
        }
        if(ligne != 0) {
            templateEtiquettesXML.loopBloc("page"); //$NON-NLS-1$
        }
        
        System.out.println(templateEtiquettesXML.output());
        return templateEtiquettesXML.output();
    }
    
    /**
     * genere la sortie XML du pas de tir
     * 
     * @return la sortie XML du pas de tir
     */
    private String getXMLPasDeTir() {
        
        templatePasDeTirXML.parse("producer", ConcoursJeunes.NOM + " " + ConcoursJeunes.VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        
        int colonne = 0;
        
        double nbseq = 10.0 * Math.ceil(parametre.getNbCible() / 10.0);
        for(int i = 1; i <= nbseq; i++) {
            //Cible cible = null;

            Concurrent[] concurrents = archerlist.list(i);
            if(concurrents != null && concurrents.length > 0) {
                DifferentiationCriteria dci = concurrents[0].getDifferentiationCriteria();
                DistancesEtBlason db = parametre.getCorrespondanceDifferentiationCriteria_DB(dci);
            
                templatePasDeTirXML.parse("ligne.numcible.nc.numcible", "" + i); //$NON-NLS-1$ //$NON-NLS-2$
                templatePasDeTirXML.parse("ligne.imgcible.ic.url_img_blason",  //$NON-NLS-1$
                        ConcoursJeunes.configuration.getRessourcesPath() + "/cible.jpg");
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
     */
    public void printClassement() {
        //parametrage d'une page au format A4 avec marges 1cm/1cm/1cm/6.5cm
        Document document = new Document(PageSize.A4, 10, 10, 10, 65);
        String classement = getClassement(OUT_XML);
        if(!classement.equals("")) { //$NON-NLS-1$
            ConcoursJeunes.printDocument(document, classement);
        }
    }
    
    /**
     * Genere l'etat classement pour la fiche en parametre
     * 
     */
    public void printClassementEquipe() {
        Document document = new Document(PageSize.A4, 10, 10, 10, 65);
        String classementEquipe = getClassementEquipe(OUT_XML);
        if(!classementEquipe.equals("")) //$NON-NLS-1$
            ConcoursJeunes.printDocument(document, classementEquipe);
    }
    
    /**
     * Genere l'etat "liste des archers"
     * 
     * @param mode - pour le greffe ou pour affichage
     */
    public void printArcherList(int mode) {
        Document document = new Document(PageSize.A4, 10, 10, 10, 65);
        ConcoursJeunes.printDocument(document, getXMLListeArcher(mode));
    }
    
    /**
     * genere l'etat d'impression des etiquettes
     * 
     */
    public void printEtiquettes() {
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        ConcoursJeunes.printDocument(document, getXMLEtiquettes(ConcoursJeunes.configuration.getColonneAndLigne()[1], ConcoursJeunes.configuration.getColonneAndLigne()[0]));
    }
    
    /**
     * genere l'etat d'impression du pas de tir
     * 
     */
    public void printPasDeTir() {
        Document document = new Document(PageSize.A4.rotate(), 5, 5, 5, 5);
        ConcoursJeunes.printDocument(document, getXMLPasDeTir());
    }
}