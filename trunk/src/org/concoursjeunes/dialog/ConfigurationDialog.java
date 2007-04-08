/*
 * Created on 20 janv. 2005
 *
 */
package org.concoursjeunes.dialog;

import static org.concoursjeunes.ConcoursJeunes.userRessources;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.concoursjeunes.*;

import com.lowagie.text.*;

import ajinteractive.standard.java2.*;

/**
 * Ecran de configuration de ConcoursJeunes
 * @author  Aurelien Jeoffray
 * @version  2.1
 */
public class ConfigurationDialog extends JDialog implements ActionListener {

	private static String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static String EXT_XML = ".xml"; //$NON-NLS-1$

	private boolean runInitDialog = true;

	private Configuration workConfiguration;

	private JTabbedPane tabbedpane      = new JTabbedPane();

	private TitledBorder tbProfil       = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlNomProfil          = new JLabel();
	private JComboBox  jcbProfil        = new JComboBox(ConcoursJeunes.userRessources.listAvailableConfigurations());
	private JButton jbAjouterProfil     = new JButton();

	//Ecran general personnalisation
	private TitledBorder tbParamGeneral = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlNomClub            = new JLabel();
	private JLabel jlAgremClub          = new JLabel();
	private JLabel jlIntituleConcours   = new JLabel();
	private JLabel jlLangue             = new JLabel();
	private JLabel jlPathPdf            = new JLabel();
	private JLabel jlLogoPath           = new JLabel();
	private JTextField jtfNomClub       = new JTextField(20);
	private JTextField jtfAgrClub       = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JButton jbDetail            = new JButton();
	private JTextField jtfIntConc       = new JTextField(20);
	private JComboBox jcbLangue         = new JComboBox();
	private JComboBox jcbPathPdf        = new JComboBox();
	private JButton jbParcourirPdf      = new JButton("..."); //$NON-NLS-1$
	private JButton jbLogoPath          = new JButton();

	//Ecran concours/pas de tir
	private JLabel jlReglement			= new JLabel();
	private JLabel jlNbCible            = new JLabel();
	private JLabel jlNbTireur           = new JLabel();
	private JLabel jlNbDepart           = new JLabel();

	private JComboBox jcbReglement		= new JComboBox();
	private JTextField jtfNbCible       = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbTireur      = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbDepart      = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$

	//Ecran etiquette
	private JLabel jlFormatPapier           = new JLabel();
	private JLabel jlNbEtiquettes           = new JLabel();
	private JLabel jlColonnes               = new JLabel("x"); //$NON-NLS-1$
	private JLabel jlMarges                 = new JLabel();
	private JLabel jlMargesH                = new JLabel();
	private JLabel jlMargesB                = new JLabel();
	private JLabel jlMargesG                = new JLabel();
	private JLabel jlMargesD                = new JLabel();
	private JLabel jlEspacements            = new JLabel();
	private JLabel jlEspacementsH           = new JLabel();
	private JLabel jlEspacementsV           = new JLabel();
	private JComboBox jcbFormatPapier       = new JComboBox();
	private JComboBox jcbOrientation        = new JComboBox();
	private JTextField jtfLignes            = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JTextField jtfColonnes          = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesH           = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesB           = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesG           = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesD           = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfEspacementsH      = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfEspacementsV      = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$

	//Ecran avancé
	private JLabel jlURLImport              = new JLabel();
	private JLabel jlURLExport              = new JLabel();
	private JLabel jlResultats              = new JLabel();
	private JLabel jlAffResultats           = new JLabel();
	private TitledBorder tbPath             = new TitledBorder(""); //$NON-NLS-1$
	private JCheckBox jcbAvanceResultatCumul= new JCheckBox();
	private JCheckBox jcbAvanceResultatSupl = new JCheckBox();
	private JCheckBox jcbAvanceAffResultatExEquo = new JCheckBox();
	//Ecran avancé chemin d'accès
	private JCheckBox jcbExpert             = new JCheckBox("", false); //$NON-NLS-1$
	private JTextField jtfURLExport         = new JTextField(20);
	private JTextField jtfURLImport         = new JTextField(20);
	//Ecran avancé option debug
	private JCheckBox jcbOfficialProfile    = new JCheckBox();
	private JCheckBox jcbFirstBoot    = new JCheckBox();

	private JButton jbValider = new JButton();
	private JButton jbAnnuler = new JButton();

	private String[] strLstLangue;

	public ConfigurationDialog(JFrame parentframe) {
		super(parentframe, true);

		JPanel jpBouton = new JPanel();

		jpBouton.setLayout(new FlowLayout(FlowLayout.RIGHT));

		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);

		jpBouton.add(jbValider);
		jpBouton.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpBouton, BorderLayout.SOUTH);

		tabbedpane.addTab("configuration.onglet.genral", null, initEcranGeneral()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.concours", null, initEcranConcours()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.etiquettes", null, initEcranEtiquette()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.interface", null, initEcranInterface()); //$NON-NLS-1$

		workConfiguration = ConcoursJeunes.configuration;

		if(workConfiguration == null) {
			workConfiguration = new Configuration();
			ConcoursJeunes.configuration = workConfiguration;
		}

		affectLibelle();
		completePanel(workConfiguration);

		runInitDialog = false;

		getContentPane().add(tabbedpane, BorderLayout.CENTER);
		getRootPane().setDefaultButton(this.jbValider);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Genere l'onglet general
	 * 
	 * @return JPanel - le panneau de l'onglet général
	 */
	private JPanel initEcranGeneral() {
		//Layout Manager
		GridBagConstraints c    = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel jpEcranGeneral   = new JPanel();
		JPanel jpProfil         = new JPanel();
		JPanel jpParamGeneral   = new JPanel();

		jbAjouterProfil.addActionListener(this);
		jcbProfil.addActionListener(this);

		jbDetail.addActionListener(this);

		jcbLangue.addActionListener(this);

		jcbPathPdf.setEditable(true);
		jcbPathPdf.setPrototypeDisplayValue("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "); //$NON-NLS-1$

		jbParcourirPdf.setMargin(new Insets(0,3,0,3));
		jbParcourirPdf.addActionListener(this);

		jbLogoPath.setPreferredSize(new java.awt.Dimension(100,100));
		jbLogoPath.setMargin(new Insets(0, 0, 0, 0));
		jbLogoPath.addActionListener(this);

		jpProfil.setBorder(tbProfil);
		jpParamGeneral.setBorder(tbParamGeneral);

		jpProfil.add(jlNomProfil);
		jpProfil.add(jcbProfil);
		jpProfil.add(jbAjouterProfil);

		gridbagComposer.setParentPanel(jpParamGeneral);
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlNomClub, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNomClub, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlAgremClub, c);
		gridbagComposer.addComponentIntoGrid(jtfAgrClub, c);
		gridbagComposer.addComponentIntoGrid(jbDetail, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfIntConc, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlLangue, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbLangue, c);
		if(!Desktop.isDesktopSupported()) {
			c.gridy++; c.gridwidth = 1;
			gridbagComposer.addComponentIntoGrid(jlPathPdf, c);
			c.gridwidth = 2;
			gridbagComposer.addComponentIntoGrid(jcbPathPdf, c);
			c.gridwidth = 1;
			gridbagComposer.addComponentIntoGrid(jbParcourirPdf, c);
		}
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlLogoPath, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jbLogoPath, c);

		gridbagComposer.setParentPanel(jpEcranGeneral);
		c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jpProfil, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jpParamGeneral, c);

		return jpEcranGeneral;
	}

	/**
	 * Genere l'onglet concours
	 * 
	 * @return JPanel - le panneau de l'ecran concours 
	 */
	private JPanel initEcranConcours() {
		//Layout Manager
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel jpEcranConcours = new JPanel();
		JPanel jpPasDeTir = new JPanel();
		JPanel jpDifCriteria = new JPanel();

		jpEcranConcours.setLayout(new BorderLayout());
		jpDifCriteria.setLayout(new BorderLayout());

		gridbagComposer.setParentPanel(jpPasDeTir);
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlReglement, c);
		gridbagComposer.addComponentIntoGrid(jcbReglement, c);
		c.gridy ++;
		gridbagComposer.addComponentIntoGrid(jlNbCible, c);
		gridbagComposer.addComponentIntoGrid(jtfNbCible, c);
		c.gridy ++;
		gridbagComposer.addComponentIntoGrid(jlNbDepart, c);
		gridbagComposer.addComponentIntoGrid(jtfNbDepart, c);
		c.gridy ++;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridy ++;
		gridbagComposer.addComponentIntoGrid(jlNbTireur, c);
		gridbagComposer.addComponentIntoGrid(jtfNbTireur, c);

		jpEcranConcours.add(jpPasDeTir, BorderLayout.CENTER);

		return jpEcranConcours;
	}

	/**
	 * Genere l'onglet des etiquettes
	 * 
	 * @return JPanel - ecran etiquette
	 */
	private JPanel initEcranEtiquette() {
		JPanel jpEcranEtiquette = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel margeHB = new JPanel();
		JPanel margeGD = new JPanel();
		JPanel espacement = new JPanel();

		margeHB.setLayout(new GridLayout(1,4));
		margeHB.add(jlMargesH);
		margeHB.add(jtfMargesH);
		margeHB.add(jlMargesB);
		margeHB.add(jtfMargesB);

		margeGD.setLayout(new GridLayout(1,4));
		margeGD.add(jlMargesD);
		margeGD.add(jtfMargesD);
		margeGD.add(jlMargesG);
		margeGD.add(jtfMargesG);

		espacement.setLayout(new GridLayout(1,4));
		espacement.add(jlEspacementsH);
		espacement.add(jtfEspacementsH);
		espacement.add(jlEspacementsV);
		espacement.add(jtfEspacementsV);

		gridbagComposer.setParentPanel(jpEcranEtiquette);
		c.gridy = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlFormatPapier, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(this.jcbFormatPapier, c);
		c.gridwidth = 1; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(this.jcbOrientation, c);
		c.gridy++; c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jlNbEtiquettes, c);
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(this.jtfLignes, c);
		c.anchor = GridBagConstraints.CENTER;
		gridbagComposer.addComponentIntoGrid(jlColonnes, c);
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(this.jtfColonnes, c);
		c.gridy++; c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jlMarges, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(margeHB, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(margeGD, c);
		c.gridy++; c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jlEspacements, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(espacement, c);

		return jpEcranEtiquette;
	}

	private JPanel initEcranInterface() {
		JPanel jpEcranInterface   = new JPanel();

		GridBagConstraints c      = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		//Chemin
		JPanel jpPathGeneral    = new JPanel();

		jpPathGeneral.setBorder(tbPath);

		jcbExpert.addActionListener(this);
		jtfURLExport.setEnabled(false);
		jtfURLImport.setEnabled(false);

		gridbagComposer.setParentPanel(jpPathGeneral);
		c.gridy = 0; c.gridwidth = 3; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jcbExpert, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlURLImport, c);
		gridbagComposer.addComponentIntoGrid(jtfURLImport, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlURLExport, c);
		gridbagComposer.addComponentIntoGrid(jtfURLExport, c);

		gridbagComposer.setParentPanel(jpEcranInterface);
		c.gridy = 0; 
		gridbagComposer.addComponentIntoGrid(jlResultats, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceResultatCumul, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceResultatSupl, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlAffResultats, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceAffResultatExEquo, c);
		c.gridy++; c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(jpPathGeneral, c);
		if(ConcoursJeunes.ajrParametreAppli.getResourceInteger("debug.mode") == 1) { //$NON-NLS-1$
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(jcbOfficialProfile, c);
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(jcbFirstBoot, c);
		}


		JPanel jpEcranInterface2 = new JPanel();
		jpEcranInterface2.setLayout(new BorderLayout());
		jpEcranInterface2.add(jpEcranInterface, BorderLayout.NORTH);

		return jpEcranInterface2;
	}

	/**
	 * 
	 * @param configuration
	 * @return les chemins d'accès à l'executable PDF
	 */
	private ArrayList<String> getPdfPath(Configuration configuration) {
		ArrayList<String> pdfPath  = new ArrayList<String>();

		if(!configuration.getPdfReaderPath().equals("")) { //$NON-NLS-1$
			pdfPath.add(configuration.getPdfReaderPath());
		}
		//Recherche d'un lecteur pdf en fonction du syteme
		if(System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$

			String base_pdfPath = ConcoursJeunes.ajrParametreAppli.getResourceString("path.windows.acrobat"); //$NON-NLS-1$
			//tente l'ouverture de acrobat reader
			File f = new File(base_pdfPath);

			String[] fList = f.list();
			if(fList != null) {
				String reader = ""; //$NON-NLS-1$
				for(int i = 0; i < fList.length; i++) {
					if(fList[i].startsWith("Acrobat")) { //$NON-NLS-1$
						reader = base_pdfPath + File.separator + fList[i] + "\\Reader\\AcroRd32.exe"; //$NON-NLS-1$
						pdfPath.add(reader);
					}
				}
			}

		} else if(System.getProperty("os.name").contains("Linux")) { //$NON-NLS-1$ //$NON-NLS-2$

			String[] pdfReader = AJToolKit.tokenize(ConcoursJeunes.ajrParametreAppli.getResourceString("path.unix.pdf"), ","); //$NON-NLS-1$ //$NON-NLS-2$
			for(String reader : pdfReader)
				pdfPath.add(reader);
		}

		return pdfPath;
	}

	private String[] getPossibleLanguages() {
		//liste les langues disponible
		String[] langues = listLangue();
		Locale[] locales = new Locale[langues.length];
		String[] libelleLangues = new String[langues.length];
		for(int i = 0; i < langues.length; i++) {
			locales[i] = new Locale(langues[i]);
			libelleLangues[i] = locales[i].getDisplayLanguage(locales[i]);
		}

		return libelleLangues;
	}

	private void affectLibelle() {
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$

		if(tabbedpane.getTabCount() > 0) {
			tabbedpane.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.genral")); //$NON-NLS-1$
			tabbedpane.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.concours")); //$NON-NLS-1$
			tabbedpane.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.etiquettes")); //$NON-NLS-1$
			tabbedpane.setTitleAt(3, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.interface")); //$NON-NLS-1$
		}

		affectLibelleGeneral();
		affectLibelleConcours();
		affectLibelleEtiquette();
		affectLibelleInterface();
	}

	private void affectLibelleGeneral() {
		jlNomProfil.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.profil")); //$NON-NLS-1$
		jlNomClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.nom")); //$NON-NLS-1$
		jlAgremClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.agrement")); //$NON-NLS-1$
		jlIntituleConcours.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.intituleconcours")); //$NON-NLS-1$
		jlLangue.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.langue")); //$NON-NLS-1$
		jlPathPdf.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.pdf")); //$NON-NLS-1$
		jlLogoPath.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.logo")); //$NON-NLS-1$

		tbProfil.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.titre0")); //$NON-NLS-1$
		tbParamGeneral.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.titre1")); //$NON-NLS-1$

		jbAjouterProfil.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.addprofile")); //$NON-NLS-1$
		jbDetail.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.detail")); //$NON-NLS-1$
		if(jbLogoPath.getText().equals("")) //$NON-NLS-1$
			jbLogoPath.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.logo")); //$NON-NLS-1$
	}

	private void affectLibelleConcours() {
		jlReglement.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.reglement"));
		jlNbCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.cible")); //$NON-NLS-1$
		jlNbTireur.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.tireur")); //$NON-NLS-1$
		jlNbDepart.setText( ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.depart")); //$NON-NLS-1$
	}

	private void affectLibelleEtiquette() {
		jlFormatPapier.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.formatpapier")); //$NON-NLS-1$
		jlNbEtiquettes.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.nbetiquettes")); //$NON-NLS-1$
		jlMarges.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.marges")); //$NON-NLS-1$
		jlMargesH.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.haut")); //$NON-NLS-1$
		jlMargesB.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.bas")); //$NON-NLS-1$
		jlMargesG.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.gauche")); //$NON-NLS-1$
		jlMargesD.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.droite")); //$NON-NLS-1$
		jlEspacements.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.espacement")); //$NON-NLS-1$
		jlEspacementsH.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.horizontal")); //$NON-NLS-1$
		jlEspacementsV.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.vertical")); //$NON-NLS-1$
	}

	private void affectLibelleInterface() {
		jlResultats.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultat")); //$NON-NLS-1$
		jlAffResultats.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.affresultat")); //$NON-NLS-1$
		jlURLImport.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.urlimport")); //$NON-NLS-1$
		jlURLExport.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.urlexport")); //$NON-NLS-1$
		jcbOfficialProfile.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.officialprofile")); //$NON-NLS-1$
		jcbFirstBoot.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.firstboot")); //$NON-NLS-1$

		tbPath.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.titre1")); //$NON-NLS-1$

		jcbAvanceResultatCumul.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultatcumul")); //$NON-NLS-1$
		jcbAvanceResultatSupl.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultatdnm")); //$NON-NLS-1$
		jcbAvanceAffResultatExEquo.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultataffexequo")); //$NON-NLS-1$

		jcbExpert.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.expert")); //$NON-NLS-1$
	}

	private void completePanel(Configuration configuration) {
		completeGeneralPanel(configuration);
		completeConcoursPanel(configuration);
		completeEtiquettePanel(configuration);
		completeInterfacePanel(configuration);
	}

	private void completeGeneralPanel(Configuration configuration) {
		String[] libelleLangues = getPossibleLanguages();

		jtfNomClub.setText(configuration.getClub().getNom());
		jtfAgrClub.setText(configuration.getClub().getAgrement());
		jtfIntConc.setText(configuration.getIntituleConcours());

		jcbProfil.setSelectedItem(configuration.getCurProfil());

		jcbLangue.removeAllItems();
		for(int i = 0; i < libelleLangues.length; i++) {
			jcbLangue.addItem(libelleLangues[i]);
		}
		jcbLangue.setSelectedItem(new Locale(configuration.getLangue()).getDisplayLanguage(
				new Locale(configuration.getLangue())));
		if(libelleLangues.length < 2) {
			jcbLangue.setEnabled(false);
		}

		jcbPathPdf.removeAllItems();
		for(String pdfpath : getPdfPath(configuration)) {
			jcbPathPdf.addItem(pdfpath);
		}

		jbLogoPath.setText("<html><img src=\"file:" + configuration.getLogoPath() + "\" width=90 height=100></html>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void completeConcoursPanel(Configuration configuration) {
		String[] availableReglements = userRessources.listAvailableReglements();
		if(availableReglements != null) {
			for(String reglementName : availableReglements)
				jcbReglement.addItem(reglementName);
		}
		jcbReglement.setSelectedItem(configuration.getReglementName());
		jtfNbCible.setText("" + configuration.getNbCible()); //$NON-NLS-1$
		jtfNbTireur.setText("" + configuration.getNbTireur()); //$NON-NLS-1$
		jtfNbDepart.setText("" + configuration.getNbDepart()); //$NON-NLS-1$
	}

	private void completeEtiquettePanel(Configuration configuration) {
		jcbFormatPapier.removeAllItems();
		for(Field champs : PageSize.class.getFields()) {
			jcbFormatPapier.addItem(champs.getName());
		}
		jcbFormatPapier.setSelectedItem(configuration.getFormatPapier());
		for(String orientation : new String[] {"portrait", "landscape"}) { //$NON-NLS-1$ //$NON-NLS-2$
			jcbOrientation.addItem(orientation);
		}
		jcbOrientation.setSelectedItem(configuration.getOrientation());

		jtfLignes.setText("" + configuration.getColonneAndLigne()[0]); //$NON-NLS-1$
		jtfColonnes.setText("" + configuration.getColonneAndLigne()[1]); //$NON-NLS-1$
		jtfMargesH.setText("" + configuration.getMarges().getTop()); //$NON-NLS-1$
		jtfMargesB.setText("" +configuration.getMarges().getBottom()); //$NON-NLS-1$
		jtfMargesG.setText("" + configuration.getMarges().getLeft()); //$NON-NLS-1$
		jtfMargesD.setText("" +configuration.getMarges().getRight()); //$NON-NLS-1$
		jtfEspacementsH.setText("" + configuration.getEspacements()[0]); //$NON-NLS-1$
		jtfEspacementsV.setText("" + configuration.getEspacements()[1]); //$NON-NLS-1$
	}

	private void completeInterfacePanel(Configuration configuration) {
		jcbAvanceResultatCumul.setSelected(configuration.isInterfaceResultatCumul());
		jcbAvanceResultatSupl.setSelected(configuration.isInterfaceResultatSupl());
		jcbAvanceAffResultatExEquo.setSelected(configuration.isInterfaceAffResultatExEquo());

		jtfURLExport.setText(configuration.getExportURL());
		jtfURLImport.setText(configuration.getImportURL());
	}

	/**
	 * Recherche du logo club
	 *
	 */
	private void changeLogoPath() {
		File f;
		JFileChooser fileDialog = new JFileChooser(workConfiguration.getLogoPath());
		AJFileFilter filtreimg = new AJFileFilter(new String[] {"jpg","gif"},"Image jpeg ou gif"); //$NON-NLS-1$ //$NON-NLS-2$
		fileDialog.addChoosableFileFilter(filtreimg);
		fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
		fileDialog.showOpenDialog(this);
		f = fileDialog.getSelectedFile();
		if (f == null) {
			return;
		}
		jbLogoPath.setText("<html><img src=\"file:" + f.getPath() + "\" width=90 height=100></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		workConfiguration.setLogoPath(f.getPath());
	}

	/**
	 * @return  Renvoie workConfiguration.
	 * @uml.property  name="workConfiguration"
	 */
	public Configuration getWorkConfiguration() {
		return workConfiguration;
	}

	/**
	 * @param workConfiguration  workConfiguration à définir.
	 * @uml.property  name="workConfiguration"
	 */
	public void setWorkConfiguration(Configuration workConfiguration) {
		this.workConfiguration = workConfiguration;
	}

	/**
	 * Renvoie la liste des langues disponibles
	 * 
	 * @return String[] - retourne la liste des langues disponible
	 */
	private String[] listLangue() {
		if(strLstLangue == null) {
			String[] strLng = new File("lang").list(new FilenameFilter() { //$NON-NLS-1$
				public boolean accept(File dir, String name) {
					if(name.startsWith("libelle_") && name.endsWith(".properties")) //$NON-NLS-1$ //$NON-NLS-2$
						return true;
					return false;
				}
			});

			for(int i = 0; i < strLng.length; i++)
				strLng[i] = strLng[i].substring(8, strLng[i].length() - 11);
			strLstLangue = strLng;
		}

		return strLstLangue;
	}

	private void loadProfile() {
		try {
			File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + (String)jcbProfil.getSelectedItem() + EXT_XML);

			if(f.exists()) {
				java.beans.XMLDecoder d = new java.beans.XMLDecoder(
						new BufferedInputStream(
								new FileInputStream(f)));
				Configuration configuration = (Configuration)d.readObject();

				workConfiguration = configuration;

				completePanel(configuration);

				workConfiguration.setCurProfil((String)jcbProfil.getSelectedItem());

				d.close();
			}
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this,
					"<html>" + io.getMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
					"IOException",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			io.printStackTrace();
		} catch(NullPointerException npe) {
			JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur.restore.config"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			npe.printStackTrace();
		}
	}

	private void saveProfile() {
		try {
			File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + (String)jcbProfil.getSelectedItem() + EXT_XML);
			AJToolKit.saveXMLStructure(f, workConfiguration, false);
		} catch(NullPointerException npe) {
			JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.config"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			npe.printStackTrace();
		}
	}

	private void registerConfig() {
		//TODO revoir saisi club
		workConfiguration.getClub().setNom(jtfNomClub.getText());
		workConfiguration.getClub().setAgrement(jtfAgrClub.getText());
		workConfiguration.setIntituleConcours(jtfIntConc.getText());
		workConfiguration.setLangue(listLangue()[jcbLangue.getSelectedIndex()]);
		workConfiguration.setPdfReaderPath(jcbPathPdf.getSelectedItem().toString());
		workConfiguration.setExportURL(jtfURLExport.getText());
		workConfiguration.setImportURL(jtfURLImport.getText());

		workConfiguration.setReglementName(jcbReglement.getSelectedItem().toString());
		workConfiguration.setNbCible(Integer.parseInt(jtfNbCible.getText()));
		workConfiguration.setNbTireur(Integer.parseInt(jtfNbTireur.getText()));
		workConfiguration.setNbDepart(Integer.parseInt(jtfNbDepart.getText()));
		workConfiguration.setFirstboot(jcbFirstBoot.isSelected());

		workConfiguration.setFormatPapier((String)this.jcbFormatPapier.getSelectedItem());
		workConfiguration.setOrientation((String)this.jcbOrientation.getSelectedItem());
		workConfiguration.setColonneAndLigne(new int[] {Integer.parseInt(this.jtfLignes.getText()), Integer.parseInt(this.jtfColonnes.getText())});
		workConfiguration.setMarges(new Marges(
				Double.parseDouble(this.jtfMargesH.getText()), 
				Double.parseDouble(this.jtfMargesB.getText()), 
				Double.parseDouble(this.jtfMargesG.getText()), 
				Double.parseDouble(this.jtfMargesD.getText())));
		workConfiguration.setEspacements(new double[] {Double.parseDouble(this.jtfEspacementsH.getText()), Double.parseDouble(this.jtfEspacementsV.getText())});
		workConfiguration.setInterfaceResultatCumul(jcbAvanceResultatCumul.isSelected());
		workConfiguration.setInterfaceResultatSupl(jcbAvanceResultatSupl.isSelected());
		workConfiguration.setInterfaceAffResultatExEquo(jcbAvanceAffResultatExEquo.isSelected());
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == this.jbValider) {

			registerConfig();

			saveProfile();
			workConfiguration.saveConfig();

			ConcoursJeunes.configuration = workConfiguration;

			setVisible(false);
		} else if(source == jbAnnuler) {
			setVisible(false);
		} else if(source == jbLogoPath) {
			changeLogoPath();
		} else if(source == this.jbParcourirPdf) {
			try {
				JFileChooser fileDialog = new JFileChooser();
				fileDialog.setCurrentDirectory(new File((String)this.jcbPathPdf.getSelectedItem()));

				if(fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					this.jcbPathPdf.addItem(fileDialog.getSelectedFile().getCanonicalPath());
					this.jcbPathPdf.setSelectedItem(fileDialog.getSelectedFile().getCanonicalPath());
				}
			} catch (IOException io) {
				System.err.println("IOException: " + io.getMessage()); //$NON-NLS-1$
			} catch(NullPointerException npe) {
				System.err.println("Aucune sauvegarde possible. Action annulé");
			}
		} else if(source == this.jbAjouterProfil) {
			String strP = JOptionPane.showInputDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.newprofile")); //$NON-NLS-1$
			if(strP != null && !strP.equals("")) { //$NON-NLS-1$
				jcbProfil.addItem(strP);
				workConfiguration.setCurProfil(strP);
				jcbOfficialProfile.setSelected(false);

				completePanel(workConfiguration);
			}
		} else if(source == this.jbDetail) {
			EntiteDialog ed = new EntiteDialog(this);
			ed.showEntite(workConfiguration.getClub());
		} else if(source == jcbProfil) {
			if(!runInitDialog && jcbProfil.getSelectedIndex() > -1)
				loadProfile();
		} else if(source == jcbLangue) {
			if(!runInitDialog && jcbLangue.getSelectedIndex() > -1) {
				AjResourcesReader.setLocale(new Locale(listLangue()[jcbLangue.getSelectedIndex()]));
				ConcoursJeunes.ajrLibelle = new AjResourcesReader("libelle"); //$NON-NLS-1$
				affectLibelle();
			}
		} else if(source == this.jcbExpert) {
			jtfURLExport.setEnabled(this.jcbExpert.isSelected());
			jtfURLImport.setEnabled(this.jcbExpert.isSelected());
		}
	}
}