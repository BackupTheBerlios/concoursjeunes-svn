/**
 * Created on 17 déc. 2004
 */
package ajinteractive.concours;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.help.*;

import org.xml.sax.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.dialog.*;

/**
 * Fenetre principale de l'application ConcoursJeunes
 * Barre de menu, gestionnaire d'etat et de sauvegarde/restauration
 * des parametres et concours
 * 
 * @author Aurelien Jeoffray
 * @version 1.11.00 - 01/02/2007
 *
 */
public class ConcoursJeunes extends JFrame implements ActionListener, WindowListener {
    
    //UID: 1.Major(2).Minor(2).Correctif(2).Build(3).Type(1,Alpha,Beta,RC(1->6),Release)
    public static final long serialVersionUID          = 10111000019l;
    
    /**
     * Chaines de version de ConcoursJeunes
     */
    public static final String NOM                     = "ConcoursJeunes";      //$NON-NLS-1$
    public static final String VERSION                 = "1.11.00 - 01/02/2007";//$NON-NLS-1$
    public static final String AUTEURS                 = "Aurélien JEOFFRAY";   //$NON-NLS-1$
    public static final String COPYR                   = "© 2002-2007";         //$NON-NLS-1$
    
    /**
     * Chaine de ressources
     * 
     */
    private static final String RES_LIBELLE = "libelle";                        //$NON-NLS-1$
    private static final String RES_PARAMETRE = "parametre";                    //$NON-NLS-1$

//	variable de récupération des ressources
    /**
     * Chargement des Libelle de l'application
     */
	public static AjResourcesReader ajrLibelle         = new AjResourcesReader(RES_LIBELLE);
    
    /**
     * Chargement des parametrages statiques
     */
	public static AjResourcesReader ajrParametreAppli  = new AjResourcesReader(RES_PARAMETRE);
	
    /**
     * Gestion de la configuration
     */
	public static Configuration configuration          = new Configuration();
    
    /**
     * ressources utilisateurs
     */
    public static UserRessources userRessources        = new UserRessources();
    
    /**
     * liste des Entites
     */
    public static Hashtable<String, Entite> listeEntite = new Hashtable<String, Entite>();
	
//	private
	private JDesktopPane desktop                       = new JDesktopPane(); 
    private Luncher luncher;
    
    private ArrayList<FicheConcours> fichesConcours    = new ArrayList<FicheConcours>();
	
	/**	
	 * constructeur, création de la fenetre principale
	 */
	public ConcoursJeunes() {
        
		//tente de recuperer la configuration générale du programme
		restoreConfig();
		
		AjResourcesReader.setLocale(new Locale(configuration.getLangue()));

        //affecte la langue de l'interface
        ajrLibelle = new AjResourcesReader(RES_LIBELLE);
        
        //en debug_mode=0, log la sortie systeme
        if(ajrParametreAppli.getResourceInteger("debug.mode") == 0) { //$NON-NLS-1$
            try {
                System.setErr(new PrintStream(
                        userRessources.getLogPathForProfile(configuration.getCurProfil()) +
                        File.separator + ajrParametreAppli.getResourceString("log.error"))); //$NON-NLS-1$
                System.setOut(new PrintStream(
                        userRessources.getLogPathForProfile(configuration.getCurProfil()) +
                        File.separator + ajrParametreAppli.getResourceString("log.exec"))); //$NON-NLS-1$
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        //Affiche d'un splash screen d'attente au démarrage
        SplashScreen s = new SplashScreen(configuration.getRessourcesPath() 
                + File.separator + ajrParametreAppli.getResourceString("file.image.background")); //$NON-NLS-1$

        //Pour le debugage donne le systeme de l'utilisateur
        System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$

        //chargement des entites
        s.setProgressionText("Chargement des entitées"); //$NON-NLS-1$
        loadEntite();
        
        //chargement des archers
        s.setProgressionText("Chargement des archers"); //$NON-NLS-1$
        ConcurrentDialog.setConcurrentListDialog(new ConcurrentListDialog(this));
        
        //définition du fond d'ecran de l'application
        AJDesktopPaneUI ajdpui = new AJDesktopPaneUI(
                new ImageIcon(configuration.getRessourcesPath() 
                        + File.separator + ajrParametreAppli.getResourceString("file.image.background")).getImage()); //$NON-NLS-1$
        this.desktop.setUI(ajdpui);
		
		//parametre de la fenetre principale
        addWindowListener(this);
		//Création de la barre de menu
		setJMenuBar(createMenubar());
		
		//Ajout du Multi document 
		getContentPane().add(this.desktop);
        
        setSize(1024, 768);

        setTitle(NOM + " " + VERSION + " - " + AUTEURS); //$NON-NLS-1$ //$NON-NLS-2$
        setIconImage(
                new ImageIcon(configuration.getRessourcesPath() 
                        + File.separator + ajrParametreAppli.getResourceString("file.icon.application")).getImage()); //$NON-NLS-1$
        
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        s.dispose();
        
        restoreConcours();
	}
    
    /**
     * envoi du signal de reboot de l'application
     *
     */
    public void fireReboot() {
        if(luncher != null) {
            setVisible(false);
            luncher.reboot();
        }
    }
    
    /**
     * ajout du interface de lancement de l'application afin de permettre un reboot rapide de celle ci
     * 
     * @param luncher - l'interface de lancement de l'application
     */
    public void setLuncher(Luncher luncher) {
        this.luncher = luncher;
    }
	
	/**
	 *  création de la barre de menu
     *  
     *  @return JMenuBar - retourne la barre de menu
	 */
	private JMenuBar createMenubar() {
		JMenuBar mb = new JMenuBar();
		JMenu jmFichier = createMenuFichier();
		JMenu jmEdition = createMenuEdition();
		JMenu jmImpression = createMenuImpression();
		JMenu jmAide = createMenuAide();
		
		mb.add(jmFichier);
		mb.add(jmEdition);
		mb.add(jmImpression);
		mb.add(jmAide);
        
        //menu de debugage
        if(ajrParametreAppli.getResourceInteger("debug.mode") == 1) {
            JMenu jmDebug = createMenuDebug();
            mb.add(jmDebug);
        }
        
		return mb;
	}
	
	/**
	 * 	création du menu fichier
     * 
     *  @return JMenu - retourne le menu fichier
	 */
	private JMenu createMenuFichier() {
		JMenu menuFichier = new JMenu(ajrLibelle.getResourceString("menubar.fichier"));
		menuFichier.add(createMenuItem("menubar.fichier.crearesto", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK)));
        menuFichier.addSeparator();
        menuFichier.add(createMenuItem("menubar.fichier.exporter", null));
        menuFichier.add(createMenuItem("menubar.fichier.importer", null));
		menuFichier.addSeparator();
		menuFichier.add(createMenuItem("menubar.fichier.quitter", KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK)));

		return menuFichier;
	}
    
	/**
	 * création du menu edition
     * 
     * @return JMenu - retourne le menu edition
	 */
	private JMenu createMenuEdition() {
		JMenu menuEdition = new JMenu(ajrLibelle.getResourceString("menubar.edition"));
		menuEdition.add(createMenuItem("menubar.edition.entite", null));
		menuEdition.addSeparator();
		menuEdition.add(createMenuItem("menubar.edition.parametre", null));
		menuEdition.add(createMenuItem("menubar.edition.configuration", null));

		return menuEdition;
	}
    
	/**
	 * création du menu impression
     * 
     * @return JMenu - retourne le menu impression
	 */
	private JMenu createMenuImpression() {
		JMenu menuImpression = new JMenu(ajrLibelle.getResourceString("menubar.impression"));

		menuImpression.add(createMenuItem("menubar.impression.listeconcurrent.ordrealpha", null));
		menuImpression.add(createMenuItem("menubar.impression.listeconcurrent.greffe", null));
		menuImpression.add(createMenuItem("menubar.impression.listeconcurrent.etiquette", null));
		menuImpression.add(createMenuItem("menubar.impression.pasdetir", null));
		menuImpression.add(createMenuItem("menubar.impression.classement.individuel", null));
		menuImpression.add(createMenuItem("menubar.impression.classement.equipe", null));

		return menuImpression;
	}
	/**
	 * création du menu aide
	 * 
     * @return JMenu - retourne le menu aide
	 */
	private JMenu createMenuAide() {
		JMenu menuAide = new JMenu(ajrLibelle.getResourceString("menubar.aide"));
		menuAide.add(createMenuItem("menubar.aide.aide", null));
		menuAide.addSeparator();
		menuAide.add(createMenuItem("menubar.aide.apropos", null));

		return menuAide;
	}
    
    /**
     * création du menu de debugage
     * 
     * @return JMenu - retourne le menu de debugage
     */
    private JMenu createMenuDebug() {
        JMenu menuDebug = new JMenu(ajrLibelle.getResourceString("menubar.debug"));
        menuDebug.add(createMenuItem("menubar.debug.generateconcurrent", null));
        menuDebug.add(createMenuItem("menubar.debug.addpoints", null));
        menuDebug.add(createMenuItem("menubar.debug.resetpoints", null));
        menuDebug.add(createMenuItem("menubar.debug.atriblevel", null));

        return menuDebug;
    }
	
    /**
     * création des éléments du menu
     * 
     * @param cmd - le code de l'element à creer
     * @param keyStroke - le racourci clavier associer
     * 
     * @return JMenuItem - renvoie l'element du menu
     */
    private JMenuItem createMenuItem(String cmd, KeyStroke keyStroke) {
        return createMenuItem(cmd, keyStroke, true);
    }
	
    /**
     * création des éléments du menu
     * 
     * @param cmd - le code de l'element à creer
     * @param keyStroke - le racourci clavier associer
     * @param enable - rend actif ou non le menu
     * 
     * @return JMenuItem - renvoie l'element du menu
     */
	private JMenuItem createMenuItem(String cmd, KeyStroke keyStroke, boolean enable) {
		JMenuItem mi = new JMenuItem(ajrLibelle.getResourceString(cmd));
		mi.setAccelerator(keyStroke);
		mi.setActionCommand(cmd);
		mi.setEnabled(enable);
		
		if(cmd.equals("menubar.aide.aide")) {
			//création des objets HelpSet et HelpBroker
			HelpSet hs = getHelpSet(ajrParametreAppli.getResourceString("file.help.hs"));
			HelpBroker hb = hs.createHelpBroker();
			
			//affectation de l'aide au composant
			CSH.setHelpIDString(mi, ajrParametreAppli.getResourceString("ressource.starthelp"));
			//gestion des événements
			mi.addActionListener(new CSH.DisplayHelpFromSource(hb));
		} else {
			mi.addActionListener(this);
		}
		
		return mi;
	}
	
	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 */
	public void newFiche() {
        FicheConcours newFiche = new FicheConcours();
        
        fichesConcours.add(newFiche);
        
		FicheConcoursFrame jif = new FicheConcoursFrame(this, newFiche);
        jif.initContent();
		
		//ajoute la fiche à l'espace de travail et l'affiche
        desktop.add(jif);
        
		try { 
			jif.setMaximum(true);
		    jif.setVisible(true);
		    jif.setSelected(true); 
		} catch (java.beans.PropertyVetoException e2) {}
	}
    
	/**
	 * restaure la configuration général du programme
	 *
	 */
	private void restoreConfig() {
        System.out.println("chargement de la configuration");
        
        configuration = (Configuration)AJToolKit.loadXMLStructure(new File(userRessources.getConfigPathForUser() + File.separator + 
                ajrParametreAppli.getResourceString("file.configuration")), false);
        
        //evite la corruption du configuration.xml
        Configuration confRef = (Configuration)AJToolKit.loadXMLStructure(new File(userRessources.getConfigPathForUser() + File.separator + 
                "configuration_" + configuration.getCurProfil() + ".xml"), false); //$NON-NLS-1$ //$NON-NLS-2$
        configuration.resetOfficialInfo(confRef);
        
        //affiche la boite de dialogue si le fichier de configuration n'existe pas ou si il est
        //configurer pour un affichage
        if(configuration == null || configuration.isFirstboot())
            new ConfigurationDialog(this, false);

        System.out.println("fin chargement de la configuration");
	}
    
	/**
	 * Affiche une boite de dialogue avec la liste des concours existant pour le profil
     * propose de créer une nouvelle fiche, d'ouvrir une fiche existante, de supprimer une fiche
	 * 
	 */
	private void restoreConcours() {
        System.out.println("chargement des concours");
        
		//ouvre le dossier des fichiers concours
		File f = new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()));
		//recupere la liste des fichier concours (.cta [Concours Tir à l'Arc])
		File[] fConcList = f.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(ajrParametreAppli.getResourceString("extention.concours"));
			}
		});
		
		//charge le descriptif de chaque concours
		String[] strConcList = new String[fConcList.length];
        long mostRecent = 0;
        int mostRecentIndex = -1;
		for(int i = 0; i < fConcList.length; i++) {
            System.out.println("Listing concours");
            
            if(fConcList[i].lastModified() > mostRecent) {
                mostRecent = fConcList[i].lastModified();
                mostRecentIndex = i;
            }    
            
			//charge le .cta
			Object[] fiche = (Object[])AJToolKit.loadXMLStructure(fConcList[i], true);
			
			//extrait le descriptif
			String concours = ((Parametre)fiche[0]).getIntituleConcours();
			String date = ((Parametre)fiche[0]).getDate();
			
			//decharge le .cta
			fiche = null;
			
			strConcList[i] = date + " - " + concours;
            
            System.out.println("Fin listing concours");
		}
		
		//affiche les concours diponible
		FicheLoaderDialog fld = new FicheLoaderDialog(this, strConcList, mostRecentIndex);

		//si 1 concours est selectionné
		if(fld.getAction() == FicheLoaderDialog.OUVRIR && fld.getSelectedFiche() != -1) {
            restoreConcours(fConcList[fld.getSelectedFiche()]);
		} else if(fld.getAction() == FicheLoaderDialog.NOUVEAU) {
            newFiche();
        } else if(fld.getAction() == FicheLoaderDialog.SUPPRIMER && fld.getSelectedFiche() != -1) {
            fConcList[fld.getSelectedFiche()].delete();
            restoreConcours();
        }
	}
    
    /**
     * Restaure le coucours fournit en parametre
     * 
     * @param concoursFile - le chemin du concours à restaurer
     */
    public void restoreConcours(File concoursFile) {
        System.out.println("chargement d'un concours");
        
        //lecture du fichier
        try {
            FicheConcours ficheConcours = new FicheConcours();
            ficheConcours.setFiche((Object[])AJToolKit.loadXMLStructure(concoursFile, true));
            
            fichesConcours.add(ficheConcours);
            
            //ouvre le .cta du concours selectionné
            //charge les données dans la fiche concours
            FicheConcoursFrame jif = new FicheConcoursFrame(this, ficheConcours);
            //jif.ficheConcours.setFiche();
            jif.initContent();
            //ajoute la fiche à l'application
            desktop.add(jif);
            
            jif.setMaximum(true);
            jif.setVisible(true);
            jif.setSelected(true);
            
        } catch (java.beans.PropertyVetoException e2) {}
        
        System.out.println("Fin chargement d'un concours");
    }
    
    /**
     * Importe un base de données mise à jour des archers
     *
     */
    private void importArchers() {
        ImportDialog importDialog = new ImportDialog(this);
        
        if(importDialog.importType == ImportDialog.IMPORT_FILE) {
            LoaderThread lt = new LoaderThread(LoaderThread.FILE);
            lt.start();
        } else if(importDialog.importType == ImportDialog.IMPORT_INTERNET) {
            LoaderThread lt = new LoaderThread(LoaderThread.INTERNET);
            lt.start();
        } else if(importDialog.importType == ImportDialog.IMPORT_FFTA) {
            LoaderThread lt = new LoaderThread(LoaderThread.FFTA);
            lt.start();
        } else if(importDialog.importType == ImportDialog.IMPORT_RESULTARC) {
            LoaderThread lt = new LoaderThread(LoaderThread.RESULTARC);
            lt.start();
        }
    }
    
    /**
     * genere le pdf à partir des parametres document et du contenu xml
     * 
     * @param document - parametre du doc
     * @param xmlcontent - le contenu formater en xml iText
     */
    public static void printDocument(Document document, String xmlcontent) {
        try {
            File tmpFile = File.createTempFile("cjta", ajrParametreAppli.getResourceString("extention.pdf"));
            String filePath = tmpFile.getCanonicalPath();
            tmpFile.deleteOnExit();
            
            /*HeaderFooter footer = new HeaderFooter(new Phrase("page "), new Phrase("."));
            document.setFooter(footer);*/
            
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource is = new InputSource(new StringReader(xmlcontent));
            parser.parse(is, new com.lowagie.text.xml.SAXiTextHandler(document));
            
            String NAV =  configuration.getPdfReaderPath();

            System.out.println(NAV + " " + tmpFile.getAbsolutePath() + "");
            Runtime.getRuntime().exec(NAV + " " + tmpFile.getAbsolutePath() + "");
        }
        catch(Exception e) {
            e.printStackTrace();
            /*JOptionPane.showMessageDialog(this,
                    "<html>" + e.getMessage() + "</html>",
                    "Exception",JOptionPane.ERROR_MESSAGE);*/
        }

        document.close();
    }
	
    /**
     * renvoie le jeu d'aide de l'appli
     * 
     * @param helpsetfile - le nom du fichier de définition d'aide
     * @return HelpSet - le jeu d'aide 
     */
	private HelpSet getHelpSet(String helpsetfile) {
		HelpSet hs = null;
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
			hs = new HelpSet(null, hsURL);
		} catch(Exception ee) {
			System.err.println("HelpSet: "+ee.getMessage());
			System.err.println("HelpSet: "+ helpsetfile + " non trouvé");
		}
		return hs;
	}
    
    /**
     * Charge les entites organisationnel (fede, ligue, cd et club)
     *
     */
    @SuppressWarnings("unchecked")
    public void loadEntite() {
        System.out.println("chargement des entités");
        
        File f = new File(userRessources.getBasePathForUser() + File.separator + 
                ajrParametreAppli.getResourceString("file.base.entite"));
        
        Object structure = AJToolKit.loadXMLStructure(f, true);

        if(structure != null && structure instanceof Hashtable)
            listeEntite = (Hashtable<String, Entite>)structure;

        if(listeEntite == null)
            JOptionPane.showMessageDialog(null,
                    ajrLibelle.getResourceString("erreur.nobase"),
                    ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
        
        System.out.println("fin chargement des entités");
    }

	/**
     * Gestion des evenements bouton et menu
	 */
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		FicheConcoursFrame jif = null;
        
        //determine la fenetre active
		try {
			jif = (FicheConcoursFrame)this.desktop.getSelectedFrame();
		} catch(NullPointerException npe) { }
		
        //ouvre le menu de restauration d'un concours
		if (cmd.equals("menubar.fichier.crearesto")) { //$NON-NLS-1$
            restoreConcours();

        //ouvre la boite de dialogue d'esportation d'un concours
		} else if (cmd.equals("menubar.fichier.exporter")) { //$NON-NLS-1$
            if(jif != null) jif.exportConcours(jif.ficheConcours.getExport());
            
        //ouvre la boite de dialogue d'importation d'un concours
        } else if (cmd.equals("menubar.fichier.importer")) { //$NON-NLS-1$
            importArchers();
            
        //enregistre l'ensemble des fiche et quitte l'application
		} else if (cmd.equals("menubar.fichier.quitter")) { //$NON-NLS-1$
            for(FicheConcours fiche : fichesConcours) {
                fiche.silentSave();
            }
			System.exit(0);
            
        //affiche la liste des entites (Fédération, CD, Club)
		} else if(cmd.equals("menubar.edition.entite")) { //$NON-NLS-1$
			new EntiteListDialog(this);
        
        //affiche la boite de dialogue des parametres
		} else if (cmd.equals("menubar.edition.parametre")) { //$NON-NLS-1$
			if(jif != null) jif.openParametreDialog();
            
        //affiche la boite de dialogue de configuartion
		} else if (cmd.equals("menubar.edition.configuration")) { //$NON-NLS-1$
			new ConfigurationDialog(this);
            
        //imprime la liste des concurrents par ordre alphabetique
		} else if (cmd.equals("menubar.impression.listeconcurrent.ordrealpha")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printArcherList(FicheConcours.ALPHA);
            
        //imprime la liste des concurrents par ordre alphabetique avec information greffe
		} else if (cmd.equals("menubar.impression.listeconcurrent.greffe")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printArcherList(FicheConcours.GREFFE);
            
        //imprime les etiquettes concurrent
		} else if (cmd.equals("menubar.impression.listeconcurrent.etiquette")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printEtiquettes();
            
        //imprime la vu du pas de tir
		} else if (cmd.equals("menubar.impression.pasdetir")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printPasDeTir();
            
        //imprime le classement individuel
		} else if (cmd.equals("menubar.impression.classement.individuel")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printClassement();
        
        //imprime le classement par equipe
		} else if (cmd.equals("menubar.impression.classement.equipe")) { //$NON-NLS-1$
			if(jif != null) jif.ficheConcours.printClassementEquipe();
            
        //affiche la boite de dialogie "A propos"
		} else if (cmd.equals("menubar.aide.apropos")) { //$NON-NLS-1$
			JOptionPane.showMessageDialog(this,
					"<html>ConcoursJeunes<br>" + //$NON-NLS-1$
					ajrLibelle.getResourceString("apropos.description") + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
                    ajrLibelle.getResourceString("apropos.version") + "<br>" + VERSION + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    COPYR + " " + AUTEURS + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
                    ajrLibelle.getResourceString("apropos.liens") + "<br></html>", //$NON-NLS-1$ //$NON-NLS-2$
                    ajrLibelle.getResourceString("apropos.titre"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
        
        //debugage -> Generation rapide d'une liste de concurrent
        } else if(cmd.equals("menubar.debug.generateconcurrent")) { //$NON-NLS-1$
            if(jif != null) ajinteractive.concours.debug.Debug.generateConcurrent(jif);
            
        //debugage -> Att-ribution rapide de points au concurrents
		} else if(cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
            if(jif != null) ajinteractive.concours.debug.Debug.attributePoints(jif.ficheConcours.archerlist);
            
        //debugage -> RAZ des points
        } else if(cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
            if(jif != null) ajinteractive.concours.debug.Debug.resetPoints(jif.ficheConcours.archerlist);
        
        //debugage -> attribution de niveau aléatoire au archers
        } else if(cmd.equals("menubar.debug.atriblevel")) { //$NON-NLS-1$
            if(jif != null) ajinteractive.concours.debug.Debug.attributeLevel(jif.ficheConcours.archerlist);
        }
	}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent arg0) {
        
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent arg0) {

    }

    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent arg0) {
        for(FicheConcours fiche : fichesConcours) {
            fiche.silentSave();
        }
        System.exit(0);
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent arg0) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent arg0) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent arg0) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent arg0) {
        
    }
    
    /**
     * Thread de gestion de l'import des ressources d'utilisation
     * @author Aurelien Jeoffray
     */
    class LoaderThread extends Thread {
        /**
         * Import à partir d'un fichier
         */
        public static final int FILE = 0;
        /**
         * Import à partir d'internet
         */
        public static final int INTERNET = 1;
        /**
         * Import à partir d'un fichier csv, representation de la base WinFFTA
         */
        public static final int FFTA = 2;
        /**
         * Import à partir d'un fichier csv, representation de la base ResultArc
         */
        public static final int RESULTARC = 3;
        
        private boolean state = false;      //etat du thread
        private int type = FILE;            //type courrant utilisé
        
        LoaderThread(int type) {
            this.type = type;
        }
        
        @Override
        public void run() {
            
            switch(type) {
                case FILE:
                    fileLoader();
                    break;
                case INTERNET:
                    File destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.entite"));
                    String url = configuration.getImportURL() + ajrParametreAppli.getResourceString("http.get.entite");
                    //chargement du fichier organisationnel
                    if(internetLoader(url, destination)) {
                        //chargement des entite dans l'appli
                        loadEntite();
                        
                        //ouverture de la fenetre de selection des ligues à charger
                        LigueLoaderDialog lld = new LigueLoaderDialog(ConcoursJeunes.this);
                        //selection des ligues à chargé
                        Hashtable<String, Entite> liguesSelectionne = lld.getLiguesSelectionne();
                        
                        //initialisation de la barre de progression
                        ProgressMonitor progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                                "", 0, liguesSelectionne.size());
                        progressMonitor.setProgress(0);
                        progressMonitor.setMillisToDecideToPopup(0);
                        
                        int iLigue = 1;

                        //boucle sur chacune des ligues selectionné
                        for(Entite entite : liguesSelectionne.values()) {
                            //gestion du "groupe AUTRE"
                            if(entite.getAgrement().equals("null")) {
                                //donne le numero des categories du groupe AUTRE
                                Vector<Integer> autre = lld.getAutre();
                                
                                //recupere le archers de AUTRE
                                for(int numLigue : autre) {
                                    destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.archers", numLigue));
                                    url = configuration.getImportURL() + ajrParametreAppli.getResourceString("http.get.archers", numLigue);
                                    
                                    internetLoader(url, destination);
                                    
                                    progressMonitor.setNote(entite.getNom() + " (" + ajrParametreAppli.getResourceString("file.base.archers", numLigue) + ")");
                                    progressMonitor.setProgress(iLigue++);
                                }
                            } else {
                                //recuperation des archers appartenant à la ligue courrante
                                int numLigue = Integer.parseInt(entite.getAgrement().substring(0,2));
    
                                destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.archers", numLigue));
                                url = configuration.getImportURL() + ajrParametreAppli.getResourceString("http.get.archers", numLigue);
                                
                                internetLoader(url, destination);
                                
                                progressMonitor.setNote(entite.getNom() + " (" + ajrParametreAppli.getResourceString("file.base.archers", numLigue) + ")");
                                progressMonitor.setProgress(iLigue++);
                            }
                        }

                        JOptionPane.showMessageDialog(ConcoursJeunes.this, ajrLibelle.getResourceString("message.import.fin"),
                                ajrLibelle.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case FFTA:
                    fftaLoader();
                    break;
                case RESULTARC:
                	resultarcLoader();
            }
            
        }
        
        /**
         * Indique si le chargement s'est terminé avec succés
         * 
         * @return boolean - l'etat de fin de chargement
         */
        public boolean isSuccess() {
            return state;
        }
        
        /**
         * Chargement à partir d'une archive
         * 
         * @return boolean - true si le chargement est réussi, false sinon
         */
        @SuppressWarnings("unchecked")
        private boolean fileLoader() {
            File source;
            File destination;
            
            java.io.FileInputStream sourceFile = null;
            FileOutputStream destinationFile = null;
            
            try {
                //Boite de dialogue de selection de fichier
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new AJFileFilter("jar", "application/x-compressed"));
                int returnVal = chooser.showOpenDialog(ConcoursJeunes.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   source = chooser.getSelectedFile();
                   
                   //ouvre l'archive et recupere les entrées
                   ZipFile zipRessources = new ZipFile(source);
                   Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)zipRessources.entries();
                   
                   //barre de progression
                   ProgressMonitor progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                           "", 0, zipRessources.size());
                   progressMonitor.setProgress(0);
                   progressMonitor.setMillisToDecideToPopup(0);
                   int iLigue = 1;
                   
                   //import de chaque entrée
                   while(e.hasMoreElements()) {
                      ZipEntry ze = e.nextElement();
                      InputStream is = zipRessources.getInputStream(ze);
                      
                      if(!ze.getName().startsWith("META-INF")) {
                          destination = new File(userRessources.getBasePathForUser() + File.separator + ze.getName());
                          destination.createNewFile();
    
                          destinationFile = new java.io.FileOutputStream(destination);
                          byte[] buffer = new byte[512*1024];
                          int nbLecture;
                          while((nbLecture = is.read(buffer)) != -1 ) {
                              destinationFile.write(buffer, 0, nbLecture);
                          }
                      }
                      progressMonitor.setNote(ze.getName());
                      progressMonitor.setProgress(iLigue++);
                   }
                   
                   JOptionPane.showMessageDialog(ConcoursJeunes.this, ajrLibelle.getResourceString("message.import.fin"),
                           ajrLibelle.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE);
                }
                return true;
            } catch( java.io.FileNotFoundException f ) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        ajrLibelle.getResourceString("erreur.filenotfound"),
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                f.printStackTrace();
                return false;
            } catch( java.io.IOException e ) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + e.getMessage() + "</html>",
                        "IOException",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return false;
            } finally {
                // Quoi qu'il arrive, on ferme les flux
                try { sourceFile.close(); } catch(Exception e) { }
                try { destinationFile.close(); } catch(Exception e) { }
            }
        }
        
        /**
         * Chargement d'une ressource à partir d'internet
         * 
         * @param url - l'url source de la ressource
         * @param destination - la destination
         * @return boolean - true si la requette à été executé avec succé, false sinon
         */
        private boolean internetLoader(String url, File destination) {
            FileOutputStream destinationFile = null;
            BufferedInputStream sourceStream = null;
            try {
                URL u = new URL(url);
                URLConnection uc = u.openConnection();
                InputStream in = uc.getInputStream();

                // Chain a ProgressMonitorInputStream to the 
                // URLConnection's InputStream
                ProgressMonitorInputStream pin = new ProgressMonitorInputStream(null, u.toString(), in);
                sourceStream = new BufferedInputStream(pin);
                
                destinationFile = new java.io.FileOutputStream(destination);
                 
                // Set the maximum value of the ProgressMonitor
                ProgressMonitor pm = pin.getProgressMonitor(); 
                pm.setMaximum(uc.getContentLength());
                pm.setMillisToDecideToPopup(10);
                pm.setMillisToPopup(0);
                
                //Lecture par segment de 128Ko 
                byte[] buffer = new byte[128*1024];
                int nbLecture;
                while((nbLecture = sourceStream.read(buffer)) != -1 ) {
                        destinationFile.write(buffer, 0, nbLecture);
                }
                
                return true;
            } catch (InterruptedIOException e) {
                  //supprime le fichier si celui ci n'est pas complet
                  if(destination.exists()) {
                      destination.delete();
                  }
                  return false;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur.import")
                        + "<br>" + e.getLocalizedMessage()
                        + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return false;
            } finally {
                // Quoi qu'il arrive, on ferme les flux
                try { sourceStream.close(); } catch(Exception e) { }
                try { destinationFile.close(); } catch(Exception e) { }
          }
        }
        
        /**
         * Chargement à partir des exports FFTA
         * 
         * @return boolean - true si la requette à été executé avec succé, false sinon
         */
        private boolean fftaLoader() {
            BufferedReader sourceReader = null;
            File destination;
            
            ArrayList<String[]> listeEntite = new ArrayList<String[]>();
            ArrayList<String[]> listelicencie = new ArrayList<String[]>();
            Hashtable<String, Entite> hashEntite = new Hashtable<String, Entite>();
            Hashtable<Integer, Hashtable<String, Concurrent>> listeTireur = new Hashtable<Integer, Hashtable<String, Concurrent>>();
            
            try {
            	if(System.getProperty("os.name").startsWith("Windows")) {
	                Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir")
	                        + File.separator + ajrParametreAppli.getResourceString("winffta.export.cmd"));
	                proc.waitFor();
            	}
                
                //recuperation de la position des informations
                String separateur = ajrParametreAppli.getResourceString("winffta.separateur");
                
                int iEntiteAgrement = ajrParametreAppli.getResourceInteger("winffta.ficlub.agrement");
                int iEntiteIntitule = ajrParametreAppli.getResourceInteger("winffta.ficlub.intitule");
                int iEntiteLocalite = ajrParametreAppli.getResourceInteger("winffta.ficlub.localite");
                
                int iLicence = ajrParametreAppli.getResourceInteger("winffta.numlicence");
                int iNom = ajrParametreAppli.getResourceInteger("winffta.nom");
                int iPrenom = ajrParametreAppli.getResourceInteger("winffta.prenom");
                int iClub = ajrParametreAppli.getResourceInteger("winffta.club");
                int iSexe = ajrParametreAppli.getResourceInteger("winffta.sexe");
                int iCategorie = ajrParametreAppli.getResourceInteger("winffta.categorie");
                int iNiveau = ajrParametreAppli.getResourceInteger("winffta.niveau");
                int iArme = ajrParametreAppli.getResourceInteger("winffta.arme");
                int iAgrement = ajrParametreAppli.getResourceInteger("winffta.agrement");
                
                //chargement de chaque ligne de FICLUB
                sourceReader = new BufferedReader(new FileReader(configuration.getRessourcesPath() + File.separator + ajrParametreAppli.getResourceString("winffta.ficlub.fichier")));
                String line;
                for(int i=0;(line = sourceReader.readLine()) != null;i++) {
                    listeEntite.add(AJToolKit.tokenize(line, separateur));
                }
                
                sourceReader.close();
                
                //chargement de chaque ligne BASE
                sourceReader = new BufferedReader(new FileReader(configuration.getRessourcesPath() + File.separator + ajrParametreAppli.getResourceString("winffta.fichier")));
                for(int i=0;(line = sourceReader.readLine()) != null;i++) {
                    listelicencie.add(AJToolKit.tokenize(line, separateur));
                }
                
                sourceReader.close();
                
                //initialisationd de la barre de progression
                ProgressMonitor progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                        "", 0, listelicencie.size() + listeEntite.size());
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(0);
                
                //formatage des entites
                int progression = 0;
                for(String[] stmp : listeEntite) {
                    Entite entite = null;
                    if(ConcoursJeunes.listeEntite != null && ConcoursJeunes.listeEntite.size() > 0) {
                        entite = ConcoursJeunes.listeEntite.get(stmp[iEntiteAgrement]);
                    }
                    if(entite == null) {
                        entite = new Entite();
                        ConcoursJeunes.listeEntite.put(stmp[iEntiteAgrement], entite);
                    }
                    
                    entite.setAgrement(stmp[iEntiteAgrement]);
                    entite.setNom(stmp[iEntiteIntitule]);
                    entite.setVille(stmp[iEntiteLocalite]);
                    if(stmp[iEntiteAgrement].endsWith("00000"))
                        entite.setType(Entite.LIGUE);
                    else if(stmp[iEntiteAgrement].endsWith("000"))
                        entite.setType(Entite.CD);
                    else
                        entite.setType(Entite.CLUB);
                    hashEntite.put(stmp[iEntiteAgrement], entite);
                    
                    int ligue = Integer.parseInt(stmp[iEntiteAgrement].substring(0,2));
                    
                    if(!listeTireur.containsKey(ligue)) {
                        listeTireur.put(ligue, new Hashtable<String, Concurrent>());
                    }
                    
                    progressMonitor.setProgress(++progression);

                    Thread.yield();
                }
                
                int ligue = 0;
                
                //formatage des licencie
                for(String[] stmp : listelicencie) {
                    Concurrent tireur = new Concurrent();
                    
                    tireur.setLicence(stmp[iLicence].trim());     //Num Licence   (Str)
                    tireur.setNom(stmp[iNom].trim());         //Nom           (Str)
                    tireur.setPrenom(stmp[iPrenom].trim());      //Prenom        (Str)
                    tireur.setClub(stmp[iClub].trim());            //Club          (Str)
                    DifferentiationCriteria scna = new DifferentiationCriteria();
                    if(Integer.parseInt(stmp[iSexe].trim()) < 2)
                        scna.setCriterion("genre", Integer.parseInt(stmp[iSexe].trim()) - 1);       //Sexe          (int 1->2)
                    else
                        scna.setCriterion("genre", 1);
                    scna.setCriterion("categorie", Integer.parseInt(stmp[iCategorie].trim()) - 1);   //Categorie     (int 1->8)
                    scna.setCriterion("niveau", Integer.parseInt(stmp[iNiveau].trim()) - 1);   //Niveau        (int)
                    scna.setCriterion("arc", ((Integer.parseInt(stmp[iArme].trim()) > 0) ? Integer.parseInt(stmp[iArme].trim()) : 1) - 1);       //Arme          (int 1->?)
                    tireur.setDifferentiationCriteria(scna);
                    tireur.setAgrement(stmp[iAgrement].trim()); //Agrement      (Str)
                    
                    ligue = Integer.parseInt(tireur.getAgrement().substring(0,2));
                    
                    if(!listeTireur.containsKey(ligue)) {
                        listeTireur.put(ligue, new Hashtable<String, Concurrent>());
                    }
                    listeTireur.get(ligue).put(tireur.getLicence(), tireur);
                    
                    progressMonitor.setProgress(++progression);

                    Thread.yield();
                }
                
                destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.entite"));
                AJToolKit.saveXMLStructure(destination, hashEntite, true);
                
                progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                        "", 0, listeTireur.size());
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(0);
                int index = 1;
                
                //serialize les objets generer
                Enumeration e = listeTireur.keys();
                while(e.hasMoreElements()) {
                    Integer i = (Integer)e.nextElement();

                    destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.archers", i));
                    AJToolKit.saveXMLStructure(destination, listeTireur.get(i), true);
                    
                    progressMonitor.setNote(ajrParametreAppli.getResourceString("file.base.archers", i));
                    progressMonitor.setProgress(index++);
                    
                    Thread.yield();
                }
                
                sourceReader.close();
                JOptionPane.showMessageDialog(ConcoursJeunes.this, ajrLibelle.getResourceString("message.import.fin"),
                        ajrLibelle.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException io) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + io.getLocalizedMessage() + "</html>",
                        "IOException", JOptionPane.ERROR_MESSAGE);
                io.printStackTrace();
                return false;
            } catch(NullPointerException npe) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur.import") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                npe.printStackTrace();
                return false;
            } catch(OutOfMemoryError oome) {
                listeTireur = null;
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur.import.outofmemory") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                return false;
            } catch(InterruptedException ie) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try { sourceReader.close(); } catch(Exception e) { }
            }
        }
        
        /**
         * Chargement à partir des exports FFTA
         * 
         * @return boolean - true si la requette à été executé avec succé, false sinon
         */
        private boolean resultarcLoader() {
            BufferedReader sourceReader = null;
            File destination;
            
            ArrayList<String[]> listeEntite = new ArrayList<String[]>();
            ArrayList<String[]> listelicencie = new ArrayList<String[]>();
            Hashtable<String, Entite> hashEntite = new Hashtable<String, Entite>();
            Hashtable<Integer, Hashtable<String, Concurrent>> listeTireur = new Hashtable<Integer, Hashtable<String, Concurrent>>();
            
            try {
            	if(System.getProperty("os.name").startsWith("Windows")) {
            		System.out.println(System.getProperty("user.dir")
	                        + File.separator + ajrParametreAppli.getResourceString("resultarc.export.cmd", "C:\\resultarc\\", userRessources.getBasePathForUser()));
	                Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir")
	                        + File.separator + ajrParametreAppli.getResourceString("resultarc.export.cmd", "C:\\resultarc\\", userRessources.getBasePathForUser()));
	                proc.waitFor();
            	}
                
                //recuperation de la position des informations
                String separateur = ajrParametreAppli.getResourceString("resultarc.separateur");
                
                int iEntiteAgrement = ajrParametreAppli.getResourceInteger("resultarc.ficlub.agrement");
                int iEntiteIntitule = ajrParametreAppli.getResourceInteger("resultarc.ficlub.intitule");
                int iEntiteLocalite = ajrParametreAppli.getResourceInteger("resultarc.ficlub.localite");
                
                int iLicence = ajrParametreAppli.getResourceInteger("resultarc.numlicence");
                int iNom = ajrParametreAppli.getResourceInteger("resultarc.nom");
                int iPrenom = ajrParametreAppli.getResourceInteger("resultarc.prenom");
                int iClub = ajrParametreAppli.getResourceInteger("resultarc.club");
                int iSexe = ajrParametreAppli.getResourceInteger("resultarc.sexe");
                int iCategorie = ajrParametreAppli.getResourceInteger("resultarc.categorie");
                int iNiveau = ajrParametreAppli.getResourceInteger("resultarc.niveau");
                int iArme = ajrParametreAppli.getResourceInteger("resultarc.arme");
                int iAgrement = ajrParametreAppli.getResourceInteger("resultarc.agrement");
                
                //HACK problème retour caractère \n dans la table hyperfile FICLUB.FIC
                //charge tous le fichier en mémoire
                FileReader frficlub = new FileReader(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("resultarc.ficlub.fichier"));
                StringBuffer sbuffer = new StringBuffer();
                char[] buffer = new char[128];
                int dataSize = 0;
                while((dataSize = frficlub.read(buffer)) > -1) {
                	sbuffer.append(buffer, 0, dataSize);
                }
                frficlub.close();
                
                String sFiclub = sbuffer.toString();
                sbuffer = null;
                
                //supprime le caractère foireux
                sFiclub = sFiclub.replaceAll("\\r;", ";");
                
                //réimprime le fichier
                PrintStream psficlub = new PrintStream(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("resultarc.ficlub.fichier"));
                psficlub.print(sFiclub);
                psficlub.close();
                //FIN HACK
                
                //chargement de chaque ligne de FICLUB
                sourceReader = new BufferedReader(new FileReader(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("resultarc.ficlub.fichier")));
                String line;
                for(int i=0;(line = sourceReader.readLine()) != null;i++) {
                    listeEntite.add(AJToolKit.tokenize(line, separateur));
                }
                
                sourceReader.close();
                
                //chargement de chaque ligne BASE
                sourceReader = new BufferedReader(new FileReader(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("resultarc.fichier")));
                for(int i=0;(line = sourceReader.readLine()) != null;i++) {
                    listelicencie.add(AJToolKit.tokenize(line, separateur));
                }
                
                sourceReader.close();
                
                //initialisationd de la barre de progression
                ProgressMonitor progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                        "", 0, listelicencie.size() + listeEntite.size());
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(0);
                
                //formatage des entites
                int progression = 0;
                for(String[] stmp : listeEntite) {
                    Entite entite = null;
                    if(ConcoursJeunes.listeEntite != null && ConcoursJeunes.listeEntite.size() > 0) {
                        entite = ConcoursJeunes.listeEntite.get(stmp[iEntiteAgrement]);
                    }
                    if(entite == null) {
                        entite = new Entite();
                        ConcoursJeunes.listeEntite.put(stmp[iEntiteAgrement], entite);
                    }
                    
                    entite.setAgrement(stmp[iEntiteAgrement]);
                    entite.setNom(stmp[iEntiteIntitule]);
                    entite.setVille(stmp[iEntiteLocalite]);
                    if(stmp[iEntiteAgrement].endsWith("00000"))
                        entite.setType(Entite.LIGUE);
                    else if(stmp[iEntiteAgrement].endsWith("000"))
                        entite.setType(Entite.CD);
                    else
                        entite.setType(Entite.CLUB);
                    hashEntite.put(stmp[iEntiteAgrement], entite);
                    
                    progressMonitor.setProgress(++progression);

                    Thread.yield();
                }
                
                int ligue = 0;
                
                //formatage des licencie
                for(String[] stmp : listelicencie) {
                    Concurrent tireur = new Concurrent();
                    
                    tireur.setLicence(stmp[iLicence].trim());     //Num Licence   (Str)
                    tireur.setNom(stmp[iNom].trim());         //Nom           (Str)
                    tireur.setPrenom(stmp[iPrenom].trim());      //Prenom        (Str)
                    tireur.setClub(stmp[iClub].trim());            //Club          (Str)
                    DifferentiationCriteria scna = new DifferentiationCriteria();
                    if(Integer.parseInt(stmp[iSexe].trim()) < 2)
                        scna.setCriterion("genre", Integer.parseInt(stmp[iSexe].trim()) - 1);       //Sexe          (int 1->2)
                    else
                        scna.setCriterion("genre", 1);
                    scna.setCriterion("categorie", Integer.parseInt(stmp[iCategorie].trim()) - 1);   //Categorie     (int 1->8)
                    scna.setCriterion("niveau", Integer.parseInt(stmp[iNiveau].trim()) - 1);   //Niveau        (int)
                    if(scna.getCriterion("niveau") < 0)
                    	scna.setCriterion("niveau", 0);
                    scna.setCriterion("arc", ((Integer.parseInt(stmp[iArme].trim()) > 0) ? Integer.parseInt(stmp[iArme].trim()) : 1) - 1);       //Arme          (int 1->?)
                    tireur.setDifferentiationCriteria(scna);
                    tireur.setAgrement(stmp[iAgrement].trim()); //Agrement      (Str)
                    
                    ligue = Integer.parseInt(tireur.getAgrement().substring(0,2));
                    
                    if(!listeTireur.containsKey(ligue)) {
                        listeTireur.put(ligue, new Hashtable<String, Concurrent>());
                    }
                    listeTireur.get(ligue).put(tireur.getLicence(), tireur);
                    
                    progressMonitor.setProgress(++progression);

                    Thread.yield();
                }
                
                destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.entite"));
                AJToolKit.saveXMLStructure(destination, hashEntite, true);
                
                progressMonitor = new ProgressMonitor(ConcoursJeunes.this, ajrLibelle.getResourceString("message.telechargement"),
                        "", 0, listeTireur.size());
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(0);
                int index = 1;
                
                //serialize les objets generer
                Enumeration e = listeTireur.keys();
                while(e.hasMoreElements()) {
                    Integer i = (Integer)e.nextElement();

                    destination = new File(userRessources.getBasePathForUser() + File.separator + ajrParametreAppli.getResourceString("file.base.archers", i));
                    AJToolKit.saveXMLStructure(destination, listeTireur.get(i), true);
                    
                    progressMonitor.setNote(ajrParametreAppli.getResourceString("file.base.archers", i));
                    progressMonitor.setProgress(index++);
                    
                    Thread.yield();
                }
                
                sourceReader.close();
                JOptionPane.showMessageDialog(ConcoursJeunes.this, ajrLibelle.getResourceString("message.import.fin"),
                        ajrLibelle.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException io) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + io.getLocalizedMessage() + "</html>",
                        "IOException", JOptionPane.ERROR_MESSAGE);
                io.printStackTrace();
                return false;
            } catch(NullPointerException npe) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur.import") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                npe.printStackTrace();
                return false;
            } catch(OutOfMemoryError oome) {
                listeTireur = null;
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur.import.outofmemory") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                return false;
            } catch(InterruptedException ie) {
                JOptionPane.showMessageDialog(ConcoursJeunes.this,
                        "<html>" + ajrLibelle.getResourceString("erreur") + "</html>",
                        ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                try { sourceReader.close(); } catch(Exception e) { }
            }
        }
    }
}