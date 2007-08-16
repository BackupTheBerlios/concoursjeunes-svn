/**
 * Created on 17 déc. 2004
 */
package ajinteractive.concours;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ajinteractive.standard.java2.AJDesktopPaneUI;
import ajinteractive.standard.java2.AJToolKit;
import ajinteractive.standard.java2.AjResourcesReader;

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
        
        //Pour le debugage donne le systeme de l'utilisateur
        System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
        
        //définition du fond d'ecran de l'application
        AJDesktopPaneUI ajdpui = new AJDesktopPaneUI(
                new ImageIcon(configuration.getRessourcesPath() 
                        + File.separator + ajrParametreAppli.getResourceString("file.image.background")).getImage()); //$NON-NLS-1$
        this.desktop.setUI(ajdpui);
		
        
        restoreConcours();
	}
	
	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 */
	public void newFiche() {
        FicheConcours newFiche = new FicheConcours();
        
        fichesConcours.add(newFiche);
        
		//FicheConcoursFrame jif = new FicheConcoursFrame(this, newFiche);
        //jif.initContent();
		
		//ajoute la fiche à l'espace de travail et l'affiche
        //desktop.add(jif);
        
		/*try { 
			jif.setMaximum(true);
		    jif.setVisible(true);
		    jif.setSelected(true); 
		} catch (java.beans.PropertyVetoException e2) {}*/
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
        /*if(configuration == null || configuration.isFirstboot())
            new ConfigurationDialog(this, false);*/

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
		/*FicheLoaderDialog fld = new FicheLoaderDialog(this, strConcList, mostRecentIndex);

		//si 1 concours est selectionné
		if(fld.getAction() == FicheLoaderDialog.OUVRIR && fld.getSelectedFiche() != -1) {
            restoreConcours(fConcList[fld.getSelectedFiche()]);
		} else if(fld.getAction() == FicheLoaderDialog.NOUVEAU) {
            newFiche();
        } else if(fld.getAction() == FicheLoaderDialog.SUPPRIMER && fld.getSelectedFiche() != -1) {
            fConcList[fld.getSelectedFiche()].delete();
            restoreConcours();
        }*/
	}
    
    /**
     * Restaure le coucours fournit en parametre
     * 
     * @param concoursFile - le chemin du concours à restaurer
     */
    public void restoreConcours(File concoursFile) {
        System.out.println("chargement d'un concours");
        
        //lecture du fichier
       /* try {
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
            
        } catch (java.beans.PropertyVetoException e2) {}*/
        
        System.out.println("Fin chargement d'un concours");
    }

	/**
     * Gestion des evenements bouton et menu
	 */
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		//FicheConcoursFrame jif = null;
        
        //determine la fenetre active
		try {
		//	jif = (FicheConcoursFrame)this.desktop.getSelectedFrame();
		} catch(NullPointerException npe) { }
		
        //ouvre le menu de restauration d'un concours
		if (cmd.equals("menubar.fichier.crearesto")) { //$NON-NLS-1$
            restoreConcours();

        //ouvre la boite de dialogue d'esportation d'un concours
		} else if (cmd.equals("menubar.fichier.exporter")) { //$NON-NLS-1$
        //    if(jif != null) jif.exportConcours(jif.ficheConcours.getExport());
            
        //ouvre la boite de dialogue d'importation d'un concours
        } else if (cmd.equals("menubar.fichier.importer")) { //$NON-NLS-1$
           // importArchers();
            
        //enregistre l'ensemble des fiche et quitte l'application
		} else if (cmd.equals("menubar.fichier.quitter")) { //$NON-NLS-1$
			System.exit(0);
            
        //affiche la liste des entites (Fédération, CD, Club)
		} else if(cmd.equals("menubar.edition.entite")) { //$NON-NLS-1$
		//	new EntiteListDialog(this);
        
        //affiche la boite de dialogue des parametres
		} else if (cmd.equals("menubar.edition.parametre")) { //$NON-NLS-1$
		//	if(jif != null) jif.openParametreDialog();
            
        //affiche la boite de dialogue de configuartion
		} else if (cmd.equals("menubar.edition.configuration")) { //$NON-NLS-1$
		//	new ConfigurationDialog(this);
            
        //imprime la liste des concurrents par ordre alphabetique
		} else if (cmd.equals("menubar.impression.listeconcurrent.ordrealpha")) { //$NON-NLS-1$
		//	if(jif != null) jif.ficheConcours.printArcherList(FicheConcours.ALPHA);
            
        //imprime la liste des concurrents par ordre alphabetique avec information greffe
		} else if (cmd.equals("menubar.impression.listeconcurrent.greffe")) { //$NON-NLS-1$
		///	if(jif != null) jif.ficheConcours.printArcherList(FicheConcours.GREFFE);
            
        //imprime les etiquettes concurrent
		} else if (cmd.equals("menubar.impression.listeconcurrent.etiquette")) { //$NON-NLS-1$
		//	if(jif != null) jif.ficheConcours.printEtiquettes();
            
        //imprime la vu du pas de tir
		} else if (cmd.equals("menubar.impression.pasdetir")) { //$NON-NLS-1$
		//	if(jif != null) jif.ficheConcours.printPasDeTir();
            
        //imprime le classement individuel
		} else if (cmd.equals("menubar.impression.classement.individuel")) { //$NON-NLS-1$
		//	if(jif != null) jif.ficheConcours.printClassement();
        
        //imprime le classement par equipe
		} else if (cmd.equals("menubar.impression.classement.equipe")) { //$NON-NLS-1$
		//	if(jif != null) jif.ficheConcours.printClassementEquipe();
            
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
        //    if(jif != null) ajinteractive.concours.debug.Debug.generateConcurrent(jif);
            
        //debugage -> Att-ribution rapide de points au concurrents
		} else if(cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
        //    if(jif != null) ajinteractive.concours.debug.Debug.attributePoints(jif.ficheConcours.archerlist);
            
        //debugage -> RAZ des points
        } else if(cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
         //   if(jif != null) ajinteractive.concours.debug.Debug.resetPoints(jif.ficheConcours.archerlist);
        
        //debugage -> attribution de niveau aléatoire au archers
        } else if(cmd.equals("menubar.debug.atriblevel")) { //$NON-NLS-1$
        //    if(jif != null) ajinteractive.concours.debug.Debug.attributeLevel(jif.ficheConcours.archerlist);
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
}