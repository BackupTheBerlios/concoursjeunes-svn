package org.concoursjeunes.ui;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ConcoursJeunesEvent;
import org.concoursjeunes.ConcoursJeunesListener;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.ParametreEvent;
import org.concoursjeunes.ParametreListener;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.dialog.ConfigurationDialog;
import org.concoursjeunes.dialog.EntiteListDialog;
import org.concoursjeunes.dialog.ImportDialog;
import org.concoursjeunes.dialog.ReglementDialog;
import org.concoursjeunes.plugins.ImportPlugin;

import ajinteractive.standard.java2.AJTemplate;
import ajinteractive.standard.java2.GhostGlassPane;
import ajinteractive.standard.ui.AJTabbedPane;
import ajinteractive.standard.ui.AJTabbedPaneListener;
import ajinteractive.standard.ui.FrameCreator;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;
/**
 * @author  aurelien
 */
public class ConcoursJeunesFrame extends JFrame implements ActionListener, HyperlinkListener, 
		ConcoursJeunesListener, ParametreListener, AJTabbedPaneListener {

	private AJTabbedPane tabbedpane;
	private JEditorPane jepHome = new JEditorPane();

	private ConcoursJeunes concoursJeunes;
	
	private AJTemplate ajtHome = new AJTemplate();

	/**
	 * Construction de l'interface graphique
	 * 
	 * @param concoursJeunes
	 */
	public ConcoursJeunesFrame() {
		concoursJeunes = ConcoursJeunes.getInstance();
		concoursJeunes.addConcoursJeunesListener(this);
		
		//affiche la boite de dialogue si le fichier de configuration n'existe pas ou si il est
		//configurer pour un affichage
		if(ConcoursJeunes.configuration == null || ConcoursJeunes.configuration.isFirstboot())
			new ConfigurationDialog(null);

		init();
		setMinimumSize(new Dimension(750, 580));
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					//permet de s'assurer que la base de données est correctement fermé
					ConcoursJeunes.dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		enumFicheConcours();
	}
	
	/**
	 * 
	 * @param args
	 */
	private ConcoursJeunesFrame(String[] args) {
		this();
		//if(args.length > 0)
		//	concoursJeunes.restoreFicheConcours(new File(args[0]));
	}

	/**
	 * 
	 *
	 */
	private void init() {
		FrameCreator frameCreator = new FrameCreator(this);
		frameCreator.setDebugMode(ConcoursJeunes.ajrParametreAppli.getResourceInteger("debug.mode") == 0);
		
		frameCreator.formatTitle(ConcoursJeunes.NOM, ConcoursJeunes.VERSION);
		frameCreator.setLibelleAjResourcesReader(ConcoursJeunes.ajrLibelle);
		frameCreator.addActionListener(this);
		frameCreator.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApp();
			}
		});

		frameCreator.createFrame(new File(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources")
				+ "/gui/ConcoursJeunes.xml"));
		
		jepHome.setEditorKit(new HTMLEditorKit());
		jepHome.setEditable(false);
		jepHome.addHyperlinkListener(this);
		((DefaultCaret)jepHome.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		ajtHome.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator 
				+ ajrParametreAppli.getResourceString("template.accueil.html"));
		
		tabbedpane = (AJTabbedPane)frameCreator.getContentPane("tabbedpane");
		
		tabbedpane.addAJTabbedPaneListener(this);
		tabbedpane.addTab("Accueil", jepHome);
		tabbedpane.hideIconAt(tabbedpane.indexOfComponent(jepHome));
		
		GhostGlassPane glassPane = new GhostGlassPane();
		setGlassPane(glassPane);
	}

	/**
	 * Affiche une boite de dialogue avec la liste des concours existant pour le profil
	 * propose de créer une nouvelle fiche, d'ouvrir une fiche existante, de supprimer une fiche
	 * 
	 */
	private void enumFicheConcours() {
		ajtHome.reset();
		ajtHome.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\"));
		ajtHome.parse("INTITULE_CLUB", ConcoursJeunes.configuration.getClub().getNom());
		ajtHome.parse("PROFILE_NAME", ConcoursJeunes.configuration.getCurProfil());
		
		
		MetaDataFichesConcours metaDataFichesConcours = concoursJeunes.getMetaDataFichesConcours();
		if(metaDataFichesConcours.getFiches().size() > 0) {
			int i = 0;
			for(MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
				ajtHome.parse("listconcours.STATE", "");
				ajtHome.parse("listconcours.ID_CONCOURS", i+"");
				ajtHome.parse("listconcours.LIBELLE_CONCOURS", df.format(metaDataFicheConcours.getDateConcours())
						+ " - " + metaDataFicheConcours.getIntituleConcours());
				
				ajtHome.loopBloc("listconcours");	
				i++;
			}
		} else {
			ajtHome.parseBloc("listconcours", "");
		}
		
		jepHome.setText(ajtHome.output());
	}

	/**
	 * 
	 * @param ficheConcours
	 */
	private void addFicheConcours(FicheConcours ficheConcours) {
		FicheConcoursPane jif = new FicheConcoursPane(this, ficheConcours);

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		//ajoute la fiche à l'espace de travail et l'affiche
		tabbedpane.addTab(ficheConcours.getParametre().getIntituleConcours() 
				+ " - " + df.format(ficheConcours.getParametre().getDate()), jif); //$NON-NLS-1$
		tabbedpane.setSelectedComponent(jif);
		//tabbedpane.add
		
		enumFicheConcours();
	}

	/**
	 * 
	 * @param tab
	 */
	private void removeFicheConcours(Component tabComponent) {
		FicheConcoursPane jif = (FicheConcoursPane)tabComponent;
		concoursJeunes.closeFicheConcours(jif.ficheConcours);
	}

	/**
	 * Importe un base de données mise à jour des archers
	 * TODO a generaliser
	 */
	private void importArchers() {
		ImportDialog importDialog = new ImportDialog(this);     

		try {
			Class<?> cla = null;
			String importClass = importDialog.showImportDialog();
			if(importClass != null) {
				cla = Class.forName(importClass); //$NON-NLS-1$
			}

			if(cla != null) {
				ImportPlugin lt = (ImportPlugin)cla.newInstance();
				lt.setParentFrame(this);
				lt.start();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void closeApp() {
		concoursJeunes.saveAllFichesConcours();
		System.exit(0);
	}

	/**
	 * @return  concoursJeunes
	 * @uml.property  name="concoursJeunes"
	 */
	public ConcoursJeunes getConcoursJeunes() {
		return concoursJeunes;
	}

	/**
	 * @param concoursJeunes  concoursJeunes à définir
	 * @uml.property  name="concoursJeunes"
	 */
	public void setConcoursJeunes(ConcoursJeunes concoursJeunes) {
		this.concoursJeunes = concoursJeunes;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		FicheConcoursPane jif = null;

		//determine la fenetre active
		try {
			//jif = (FicheConcoursFrame)this.desktop.getSelectedFrame();
			if(tabbedpane.getSelectedIndex() > 0)
				jif = (FicheConcoursPane)tabbedpane.getSelectedComponent();
		} catch(NullPointerException npe) { }

		if (cmd.equals("menubar.fichier.exporter")) { //$NON-NLS-1$
			//TODO Revoir l'export
			//if(jif != null) jif.exportConcours(jif.ficheConcours.getExport());

			//ouvre la boite de dialogue d'importation d'un concours
		} else if (cmd.equals("menubar.fichier.importer")) { //$NON-NLS-1$
			importArchers();

			//enregistre l'ensemble des fiche et quitte l'application
		} else if (cmd.equals("menubar.fichier.quitter")) { //$NON-NLS-1$
			closeApp();

			//affiche la liste des entites (Fédération, CD, Club)
		} else if(cmd.equals("menubar.edition.entite")) { //$NON-NLS-1$
			new EntiteListDialog(this);

			//affiche la boite de dialogue des parametres
		} else if (cmd.equals("menubar.edition.parametre")) { //$NON-NLS-1$
			if(jif != null) jif.openParametreDialog();

			//affiche la boite de dialogue des reglements de concours
		} else if (cmd.equals("menubar.edition.reglement")) { //$NON-NLS-1$
			ReglementDialog reglementDialog = new ReglementDialog(this);
			Reglement reglement = reglementDialog.showReglementDialog();
			if(reglement != null) {
				reglement.save();
				
				ConcoursJeunes.configuration.saveConfig();
			}
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
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.description") + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.version") + "<br>" + ConcoursJeunes.VERSION + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					ConcoursJeunes.COPYR + " " + ConcoursJeunes.AUTEURS + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.liens") + "<br></html>", //$NON-NLS-1$ //$NON-NLS-2$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.titre"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		} else if(cmd.equals("menubar.debug.generateconcurrent")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.generateConcurrent(jif, 0);

			//debugage -> Attribution rapide de points au concurrents
		} else if(cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
			if(jif != null) {
				org.concoursjeunes.debug.Debug.attributePoints(jif.ficheConcours.getConcurrentList(), 0);
			}

			//debugage -> RAZ des points
		} else if(cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.resetPoints(jif.ficheConcours.getConcurrentList(), 0);

			//debugage -> attribution de niveau aléatoire au archers
		} else if(cmd.equals("menubar.debug.atriblevel")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.attributeLevel(jif.ficheConcours.getConcurrentList(), 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursCreated(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addParametreListener(this);
		enumFicheConcours();
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursDeleted(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent) {
		enumFicheConcours();
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursClosed(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent) {
		enumFicheConcours();
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursRestored(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addParametreListener(this);
		enumFicheConcours();
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ParametreListener#metaDataChanged(org.concoursjeunes.ParametreEvent)
	 */
	public void metaDataChanged(ParametreEvent parametreEvent) {
		enumFicheConcours();
	}
	
	public void parametreChanged(ParametreEvent parametreEvent) {
		
	}
	
	public void tabAdded(Component tabComponent) {
		
	}
	public void tabClosed(Component tabComponent) {
		removeFicheConcours(tabComponent);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
			if (e instanceof HTMLFrameHyperlinkEvent) { 
				((HTMLDocument)jepHome.getDocument()).processHTMLFrameHyperlinkEvent( 
						(HTMLFrameHyperlinkEvent)e); 
			} else {
				if(e.getURL().getHost().equals("open_concours")) {
					concoursJeunes.restoreFicheConcours(concoursJeunes.getMetaDataFichesConcours().getMetaDataFicheConcours(
									Integer.parseInt(e.getURL().getRef())));
				} else if(e.getURL().getHost().equals("delete_concours")) {
					if(JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.concours"), //$NON-NLS-1$
							ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.concours.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
						concoursJeunes.deleteFicheConcours(
								concoursJeunes.getMetaDataFichesConcours().getMetaDataFicheConcours(
										Integer.parseInt(e.getURL().getRef())));
					}
				} else if(e.getURL().getHost().equals("new_concours")) {
					concoursJeunes.createFicheConcours();
				}
			}
		}
	}

	public static void main(String[] args) {
        new ConcoursJeunesFrame(args);
    }
}