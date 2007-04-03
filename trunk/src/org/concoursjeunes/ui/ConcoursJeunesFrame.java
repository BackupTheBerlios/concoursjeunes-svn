package org.concoursjeunes.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ConcoursJeunesEvent;
import org.concoursjeunes.ConcoursJeunesListener;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.dialog.ConfigurationDialog;
import org.concoursjeunes.dialog.EntiteListDialog;
import org.concoursjeunes.dialog.FicheLoaderDialog;
import org.concoursjeunes.dialog.ImportDialog;
import org.concoursjeunes.dialog.ReglementDialog;
import org.concoursjeunes.plugins.ImportPlugin;

import ajinteractive.standard.java2.GhostGlassPane;
import ajinteractive.standard.ui.FrameCreator;

/**
 * @author  aurelien
 */
public class ConcoursJeunesFrame extends JFrame implements ActionListener, ConcoursJeunesListener {

	private JTabbedPane tabbedpane;

	private ConcoursJeunes concoursJeunes;

	/**
	 * Construction de l'interface graphique
	 * 
	 * @param concoursJeunes
	 */
	public ConcoursJeunesFrame() {
		concoursJeunes = new ConcoursJeunes();
		concoursJeunes.addConcoursJeunesListener(this);
		
		//affiche la boite de dialogue si le fichier de configuration n'existe pas ou si il est
		//configurer pour un affichage
		if(ConcoursJeunes.configuration == null || ConcoursJeunes.configuration.isFirstboot())
			new ConfigurationDialog(null);

		init();

		enumFicheConcours();
	}
	
	private ConcoursJeunesFrame(String[] args) {
		this();
		if(args.length > 0)
			concoursJeunes.restoreFicheConcours(new File(args[0]));
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
		
		tabbedpane = (JTabbedPane)frameCreator.getContentPane("tabbedpane");
		
		GhostGlassPane glassPane = new GhostGlassPane();
		setGlassPane(glassPane);
	}

	/**
	 * 
	 * @param title
	 * @param tabnum
	 * @return
	 */
	private JPanel getTabComponent(String title, int tabnum) {
		JPanel panel = new JPanel();

		JLabel label = new JLabel(title);

		JButton bClose = new JButton("");
		bClose.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources")
				+ File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.base.close")));
		bClose.setMargin(new Insets(0,0,0,0));
		bClose.addActionListener(this);
		bClose.setName("closeTab");
		bClose.setActionCommand("" + tabnum);

		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(label, BorderLayout.CENTER);
		panel.add(bClose, BorderLayout.EAST);
		return panel;
	}

	/**
	 * Affiche une boite de dialogue avec la liste des concours existant pour le profil
	 * propose de créer une nouvelle fiche, d'ouvrir une fiche existante, de supprimer une fiche
	 * 
	 */
	private void enumFicheConcours() {
		MetaDataFichesConcours metaDataFichesConcours = concoursJeunes.getMetaDataFichesConcours();
		String[] strConcList = new String[metaDataFichesConcours.getFiches().size()];
		int i = 0;
		for(MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
			strConcList[i] = df.format(metaDataFicheConcours.getDateConcours())
					+ " - " + metaDataFicheConcours.getIntituleConcours();
			i++;
		}

		//affiche les concours diponible
		//FicheLoaderDialog fld = new FicheLoaderDialog(this, strConcList, mostRecentIndex);
		FicheLoaderDialog fld = new FicheLoaderDialog(this, strConcList, 0);

		//si 1 concours est selectionné
		if(fld.getAction() == FicheLoaderDialog.OUVRIR && fld.getSelectedFiche() != -1) {
			concoursJeunes.restoreFicheConcours(new File(ConcoursJeunes.userRessources.getConcoursPathForProfile(
					ConcoursJeunes.configuration.getCurProfil()) + File.separator + 
					metaDataFichesConcours.getMetaDataFicheConcours(fld.getSelectedFiche()).getFilenameConcours()));
		} else if(fld.getAction() == FicheLoaderDialog.NOUVEAU) {
			concoursJeunes.createFicheConcours();
		} else if(fld.getAction() == FicheLoaderDialog.SUPPRIMER && fld.getSelectedFiche() != -1) {
			concoursJeunes.deleteFicheConcours(
					metaDataFichesConcours.getMetaDataFicheConcours(fld.getSelectedFiche()).getFilenameConcours());
			enumFicheConcours();
		}
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
		tabbedpane.setTabComponentAt(tabbedpane.getTabCount() - 1, getTabComponent(ficheConcours.getParametre().getIntituleConcours() + 
				" - " + df.format(ficheConcours.getParametre().getDate()), tabbedpane.getTabCount() - 1));
		tabbedpane.setSelectedComponent(jif);
	}

	/**
	 * 
	 * @param tab
	 */
	private void removeFicheConcours(int tab) {
		FicheConcoursPane jif = (FicheConcoursPane)tabbedpane.getComponentAt(tab);
		tabbedpane.removeTabAt(tab);
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
			if(importDialog.importClass != null) {
				cla = Class.forName("org.concoursjeunes.plugins." + importDialog.importClass); //$NON-NLS-1$
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
			jif = (FicheConcoursPane)tabbedpane.getSelectedComponent();
		} catch(NullPointerException npe) { }

		//ouvre le menu de restauration d'un concours
		if (cmd.equals("menubar.fichier.crearesto")) { //$NON-NLS-1$
			enumFicheConcours();

			//ouvre la boite de dialogue d'esportation d'un concours
		} else if (cmd.equals("menubar.fichier.exporter")) { //$NON-NLS-1$
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
		} else if (e.getSource() instanceof JButton && 
				((JButton)e.getSource()).getName().equals("closeTab")) {
			removeFicheConcours(Integer.parseInt(cmd));
			//debugage -> Generation rapide d'une liste de concurrent
		} else if(cmd.equals("menubar.debug.generateconcurrent")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.generateConcurrent(jif, 0);

			//debugage -> Attribution rapide de points au concurrents
		} else if(cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
			if(jif != null) {
				org.concoursjeunes.debug.Debug.attributePoints(jif.ficheConcours.getArcherlist(), 0);
			}

			//debugage -> RAZ des points
		} else if(cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.resetPoints(jif.ficheConcours.getArcherlist(), 0);

			//debugage -> attribution de niveau aléatoire au archers
		} else if(cmd.equals("menubar.debug.atriblevel")) { //$NON-NLS-1$
			if(jif != null) org.concoursjeunes.debug.Debug.attributeLevel(jif.ficheConcours.getArcherlist(), 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursCreated(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursDeleted(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent) {
		
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursClosed(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent) {
		// TODO Raccord de méthode auto-généré
		
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursRestored(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
	}

	public static void main(String[] args) {
        new ConcoursJeunesFrame(args);
    }
}