/*
 * Created on 20 janv. 2005
 *
 */
package ajinteractive.concours.dialog;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

import com.lowagie.text.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * Ecran de configuration de ConcoursJeunes
 * 
 * @author Aurelien Jeoffray
 * @version 2.1
 *
 */
public class ConfigurationDialog extends JDialog implements ActionListener, ChangeListener, MouseListener {
	
    private static String CONFIG_PROFILE = "configuration_";
    private static String EXT_XML = ".xml";
    
    private boolean runInitDialog = true;
    
	private ConcoursJeunes concoursJeunes;
    private Configuration workConfiguration;
	
    private JTabbedPane tabbedpane      = new JTabbedPane();
    
    private TitledBorder tbProfil       = new TitledBorder("");
    private JLabel jlNomProfil          = new JLabel();
    //TODO Deplacer le listConfiguration()
    private JComboBox  jcbProfil        = new JComboBox(listConfiguration());
    private JButton jbAjouterProfil     = new JButton();
    
    //Ecran general personnalisation
    private TitledBorder tbParamGeneral = new TitledBorder("");
    private JLabel jlNomClub            = new JLabel();
    private JLabel jlAgremClub          = new JLabel();
    private JLabel jlIntituleConcours   = new JLabel();
    private JLabel jlLangue             = new JLabel();
    private JLabel jlPathPdf            = new JLabel();
    private JLabel jlLogoPath           = new JLabel();
    private JTextField jtfNomClub       = new JTextField(20);
    private JTextField jtfAgrClub       = new JTextField(new NumberDocument(false, false), "", 5);
    private JButton jbDetail            = new JButton();
    private JTextField jtfIntConc       = new JTextField(20);
	private JComboBox jcbLangue         = new JComboBox();
    private JComboBox jcbPathPdf        = new JComboBox();
    private JButton jbParcourirPdf      = new JButton("...");
    private JButton jbLogoPath          = new JButton();
    
    //Ecran concours/pas de tir
    JTabbedPane tabbedpaneConcours      = new JTabbedPane(JTabbedPane.LEFT);
    private JLabel jlNbCible            = new JLabel();
    private JLabel jlNbTireur           = new JLabel();
    private JLabel jlNbSerie            = new JLabel();
    private JLabel jlNbVoleeParSerie    = new JLabel();
    private JLabel jlNbFlecheParVolee   = new JLabel();
    private JLabel jlNbDepart           = new JLabel();
    
    private JLabel jlNbMembresEquipe    = new JLabel();
    private JLabel jlNbMembresRetenu    = new JLabel();
    
    private JLabel jlNbDB               = new JLabel();
    private JButton jbAddCriteria       = new JButton();
    private JButton jbAddCriteriaMember = new JButton();
    private JButton jbUpElement         = new JButton();
    private JButton jbDownElement       = new JButton();
    private JButton jbRemoveElement     = new JButton();
    private DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("criteres");
    private DefaultTreeModel treeModel  = new DefaultTreeModel(treeRoot);
    private DnDJTree treeCriteria       = new DnDJTree(treeModel);

    private JTextField jtfNbCible       = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbTireur      = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbSerie       = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbVoleeParSerie = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbFlecheParVolee = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbDepart      = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbMembresEquipe = new JTextField(new NumberDocument(false, false), "", 3);
    private JTextField jtfNbMembresRetenu = new JTextField(new NumberDocument(false, false), "", 3);
    
    private JTable jtDistanceBlason         = new JTable();
    private JScrollPane jspDistanceBlason   = new JScrollPane();
    
    //Ecran etiquette
    private JLabel jlFormatPapier           = new JLabel();
    private JLabel jlNbEtiquettes           = new JLabel();
    private JLabel jlColonnes               = new JLabel("x");
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
    private JTextField jtfLignes            = new JTextField(new NumberDocument(false, false), "", 5);
    private JTextField jtfColonnes          = new JTextField(new NumberDocument(false, false), "", 5);
    private JTextField jtfMargesH           = new JTextField(new NumberDocument(true, false), "", 5);
    private JTextField jtfMargesB           = new JTextField(new NumberDocument(true, false), "", 5);
    private JTextField jtfMargesG           = new JTextField(new NumberDocument(true, false), "", 5);
    private JTextField jtfMargesD           = new JTextField(new NumberDocument(true, false), "", 5);
    private JTextField jtfEspacementsH      = new JTextField(new NumberDocument(true, false), "", 5);
    private JTextField jtfEspacementsV      = new JTextField(new NumberDocument(true, false), "", 5);
    
    //Ecran avancé
    private JLabel jlPathRessources         = new JLabel();
    private JLabel jlURLImport              = new JLabel();
    private JLabel jlURLExport              = new JLabel();
    private JLabel jlResultats              = new JLabel();
    private JLabel jlAffResultats           = new JLabel();
    private TitledBorder tbPath             = new TitledBorder("");
    private JCheckBox jcbAvanceResultatCumul= new JCheckBox();
    private JCheckBox jcbAvanceResultatSupl = new JCheckBox();
    private JCheckBox jcbAvanceAffResultatExEquo = new JCheckBox();
    //Ecran avancé chemin d'accès
    private JCheckBox jcbExpert             = new JCheckBox("", false);
    private JTextField jtfPathRessources    = new JTextField(20);
    private JTextField jtfURLExport         = new JTextField(20);
    private JTextField jtfURLImport         = new JTextField(20);
    private JButton jbParcourirRessources   = new JButton("...");
    //Ecran avancé option debug
    private JCheckBox jcbOfficialProfile    = new JCheckBox();
    private JCheckBox jcbFirstBoot    = new JCheckBox();
    
	private JButton jbValider = new JButton();
	private JButton jbAnnuler = new JButton();
    
    private DifferentiationCriteria[] differentiationCriteria;
    
    private String[] strLstLangue;
    
    private boolean reboot = false;

    public ConfigurationDialog(ConcoursJeunes concoursJeunes) {
        this(concoursJeunes, true);
    }
    
	public ConfigurationDialog(ConcoursJeunes concoursJeunes, boolean reboot) {
		super(concoursJeunes, true);
        
        this.reboot = reboot;
		
		this.concoursJeunes = concoursJeunes;
		
		JPanel jpBouton = new JPanel();
		
		jpBouton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
		
		jpBouton.add(jbValider);
		jpBouton.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpBouton, BorderLayout.SOUTH);

        tabbedpane.addTab("configuration.onglet.genral", null, initEcranGeneral());
        tabbedpane.addTab("configuration.onglet.concours", null, initEcranConcours());
        tabbedpane.addTab("configuration.onglet.etiquettes", null, initEcranEtiquette());
        tabbedpane.addTab("configuration.onglet.interface", null, initEcranInterface());
        
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
        GridBagLayout gridbag   = new GridBagLayout();
        GridBagConstraints c    = new GridBagConstraints();
        
		JPanel jpEcranGeneral   = new JPanel();
        JPanel jpProfil         = new JPanel();
        JPanel jpParamGeneral   = new JPanel();
        
        jbAjouterProfil.addActionListener(this);
        jcbProfil.addActionListener(this);
        
        jbDetail.addActionListener(this);
        if(ConcoursJeunes.listeEntite == null)
            jbDetail.setEnabled(false);
        
        jcbLangue.addActionListener(this);
        
        jcbPathPdf.setEditable(true);
        jcbPathPdf.setPrototypeDisplayValue("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
        
        jbParcourirPdf.setMargin(new Insets(0,3,0,3));
        jbParcourirPdf.addActionListener(this);
        
        jbLogoPath.setPreferredSize(new java.awt.Dimension(100,100));
        jbLogoPath.setMargin(new Insets(0, 0, 0, 0));
        jbLogoPath.addActionListener(this);
        
        jpProfil.setBorder(tbProfil);
        jpParamGeneral.setBorder(tbParamGeneral);
        jpParamGeneral.setLayout(gridbag);
        
        jpProfil.add(jlNomProfil);
        jpProfil.add(jcbProfil);
        jpProfil.add(jbAjouterProfil);
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlNomClub, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jtfNomClub, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlAgremClub, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jtfAgrClub, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jbDetail, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, new JPanel(), gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlIntituleConcours, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jtfIntConc, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlLangue, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jcbLangue, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlPathPdf, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jcbPathPdf, gridbag, c);
        c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jbParcourirPdf, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jlLogoPath, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpParamGeneral, jbLogoPath, gridbag, c);
        
        jpEcranGeneral.setLayout(gridbag);
        c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
        AJToolKit.addComponentIntoGrid(jpEcranGeneral, jpProfil, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpEcranGeneral, jpParamGeneral, gridbag, c);
		
		return jpEcranGeneral;
	}
	
    /**
     * Genere l'onglet concours
     * 
     * @return JPanel - le panneau de l'ecran concours 
     */
	private JPanel initEcranConcours() {
        //Layout Manager
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
		JPanel jpEcranConcours = new JPanel();
		JPanel jpPasDeTir = new JPanel();
        JPanel jpDifCriteria = new JPanel();
        JPanel jpConcours = new JPanel();
        
        JPanel jpOperations = new JPanel();
        JScrollPane jspCriteres = new JScrollPane();
        
        jpEcranConcours.setLayout(new BorderLayout());
        jpPasDeTir.setLayout(gridbag);
        jpDifCriteria.setLayout(new BorderLayout());
        jpConcours.setLayout(gridbag);

        //TODO Le nombre de départ n'est pas géré
        jtfNbDepart.setEnabled(false);
        
        tabbedpaneConcours.addChangeListener(this);
        
        jbAddCriteria.setMargin(new Insets(0, 0, 0, 0));
        jbAddCriteriaMember.setMargin(new Insets(0, 0, 0, 0));
        jbUpElement.setMargin(new Insets(0, 0, 0, 0));
        jbDownElement.setMargin(new Insets(0, 0, 0, 0));
        jbRemoveElement.setMargin(new Insets(0, 0, 0, 0));
        jbAddCriteria.addActionListener(this);
        jbAddCriteriaMember.addActionListener(this);
        jbUpElement.addActionListener(this);
        jbDownElement.addActionListener(this);
        jbRemoveElement.addActionListener(this);
        treeCriteria.addMouseListener(this);
        treeCriteria.setToggleClickCount(3);
        
        jspDistanceBlason.setPreferredSize(new Dimension(300, 250));
        jtDistanceBlason.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtDistanceBlason.setPreferredScrollableViewportSize(new Dimension(350, 200));
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbCible, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbCible, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbDepart, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbDepart, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, new JPanel(), gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbTireur, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbTireur, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbSerie, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbSerie, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbVoleeParSerie, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbVoleeParSerie, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbFlecheParVolee, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbFlecheParVolee, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, new JPanel(), gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbMembresEquipe, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbMembresEquipe, gridbag, c);
        c.gridy ++;
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jlNbMembresRetenu, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPasDeTir, jtfNbMembresRetenu, gridbag, c);

        jpOperations.setLayout(new FlowLayout(FlowLayout.LEFT));
        jpOperations.add(jbAddCriteria);
        jpOperations.add(jbAddCriteriaMember);
        jpOperations.add(jbUpElement);
        jpOperations.add(jbDownElement);
        jpOperations.add(jbRemoveElement);
        jspCriteres.setViewportView(treeCriteria);
        jspCriteres.setPreferredSize(new Dimension(300, 270));
        jpDifCriteria.add(jpOperations, BorderLayout.NORTH);
        jpDifCriteria.add(jspCriteres, BorderLayout.CENTER);
        
        c.gridy = 0; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpConcours, jlNbDB, gridbag, c);
        c.gridy++; c.fill = GridBagConstraints.BOTH;
        AJToolKit.addComponentIntoGrid(jpConcours, jspDistanceBlason, gridbag, c);

        tabbedpaneConcours.addTab("configuration.ecran.concours.onglet.pasdetir", jpPasDeTir);
        tabbedpaneConcours.addTab("configuration.ecran.concours.onglet.criteres", jpDifCriteria);
        tabbedpaneConcours.addTab("configuration.ecran.concours.onglet.categorie", jpConcours);
		
        jpEcranConcours.add(tabbedpaneConcours, BorderLayout.CENTER);
        
		return jpEcranConcours;
	}
	
    /**
     * Genere l'onglet des etiquettes
     * 
     * @return JPanel - ecran etiquette
     */
	private JPanel initEcranEtiquette() {
		JPanel jpEcranEtiquette = new JPanel();
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
        
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
        
        jpEcranEtiquette.setLayout(gridbag);
		
		c.gridy = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
		AJToolKit.addComponentIntoGrid(jpEcranEtiquette, jlFormatPapier, gridbag, c);
        c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, this.jcbFormatPapier, gridbag, c);
        c.gridwidth = 1; c.anchor = GridBagConstraints.WEST;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, this.jcbOrientation, gridbag, c);
		c.gridy++; c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, jlNbEtiquettes, gridbag, c);
        c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, this.jtfLignes, gridbag, c);
        c.anchor = GridBagConstraints.CENTER;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, jlColonnes, gridbag, c);
        c.anchor = GridBagConstraints.WEST;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, this.jtfColonnes, gridbag, c);
		c.gridy++; c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, jlMarges, gridbag, c);
		c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, new JPanel(), gridbag, c);
        c.gridwidth = 4;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, margeHB, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, new JPanel(), gridbag, c);
        c.gridwidth = 4;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, margeGD, gridbag, c);
        c.gridy++; c.gridwidth = 2;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, jlEspacements, gridbag, c);
        c.gridy++; c.gridwidth = 1;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, new JPanel(), gridbag, c);
        c.gridwidth = 4;
        AJToolKit.addComponentIntoGrid(jpEcranEtiquette, espacement, gridbag, c);
		
		return jpEcranEtiquette;
	}
	
	private JPanel initEcranInterface() {
		JPanel jpEcranInterface   = new JPanel();
		
		GridBagLayout gridbag     = new GridBagLayout();
		GridBagConstraints c      = new GridBagConstraints();
        
		//Chemin
        JPanel jpPathGeneral    = new JPanel();
		
		jpEcranInterface.setLayout(gridbag);
        
        jpPathGeneral.setBorder(tbPath);
        jpPathGeneral.setLayout(gridbag);

        jcbExpert.addActionListener(this);
        jtfPathRessources.setEnabled(false);
        jtfURLExport.setEnabled(false);
        jtfURLImport.setEnabled(false);
        
        jbParcourirRessources.setMargin(new Insets(0,3,0,3));
        jbParcourirRessources.addActionListener(this);
        jbParcourirRessources.setEnabled(false);
		
        c.gridy = 0; c.gridwidth = 3; c.anchor = GridBagConstraints.WEST;
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jcbExpert, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jlPathRessources, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jtfPathRessources, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jbParcourirRessources, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jlURLImport, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jtfURLImport, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jlURLExport, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpPathGeneral, jtfURLExport, gridbag, c);
        
        c.gridy = 0; 
        AJToolKit.addComponentIntoGrid(jpEcranInterface, jlResultats, gridbag, c);
        c.gridy++;
		AJToolKit.addComponentIntoGrid(jpEcranInterface, jcbAvanceResultatCumul, gridbag, c);
		c.gridy++;
		AJToolKit.addComponentIntoGrid(jpEcranInterface, jcbAvanceResultatSupl, gridbag, c);
		c.gridy++;
		AJToolKit.addComponentIntoGrid(jpEcranInterface, jlAffResultats, gridbag, c);
		c.gridy++;
		AJToolKit.addComponentIntoGrid(jpEcranInterface, jcbAvanceAffResultatExEquo, gridbag, c);
        c.gridy++; c.fill = GridBagConstraints.BOTH;
        AJToolKit.addComponentIntoGrid(jpEcranInterface, jpPathGeneral, gridbag, c);
        if(ConcoursJeunes.ajrParametreAppli.getResourceInteger("debug.mode") == 1) {
            c.gridy++;
            AJToolKit.addComponentIntoGrid(jpEcranInterface, jcbOfficialProfile, gridbag, c);
            c.gridy++;
            AJToolKit.addComponentIntoGrid(jpEcranInterface, jcbFirstBoot, gridbag, c);
        }
        
        
        JPanel jpEcranInterface2 = new JPanel();
        jpEcranInterface2.setLayout(new BorderLayout());
        jpEcranInterface2.add(jpEcranInterface, BorderLayout.NORTH);
        
		return jpEcranInterface2;
	}
    
    private Vector<String> getPdfPath(Configuration configuration) {
        Vector<String> pdfPath  = new Vector<String>();
        
        if(!configuration.getPdfReaderPath().equals("")) {
            pdfPath.add(configuration.getPdfReaderPath());
        }
        
        //Recherche d'un lecteur pdf en fonction du syteme
        if(System.getProperty("os.name").startsWith("Windows")) {
            
            String base_pdfPath = ConcoursJeunes.ajrParametreAppli.getResourceString("path.windows.acrobat");
            //tente l'ouverture de acrobat reader
            File f = new File(base_pdfPath);

            String[] fList = f.list();
            if(fList != null) {
                String reader ="";
                for(int i = 0; i < fList.length; i++) {
                    if(fList[i].startsWith("Acrobat")) {
                        reader = base_pdfPath + File.separator + fList[i] + "\\Reader\\AcroRd32.exe";
                        pdfPath.add(reader);
                    }
                }
            }
            
        } else if(System.getProperty("os.name").contains("Linux")) {
            
            String[] pdfReader = AJToolKit.tokenize(ConcoursJeunes.ajrParametreAppli.getResourceString("path.unix.pdf"), ",");
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
    
    private DefaultTableModel createTableModel() {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if(col == 0 || workConfiguration.isOfficialProfile())
                    return false;
                return true;
            }
        };
        dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.scna"));
        for(int i = 0; i < workConfiguration.getNbSerie(); i++)
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + (i + 1));
        dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason"));

        differentiationCriteria = DifferentiationCriteria.listeDifferentiationCriteria(workConfiguration.getPlacementFilter());
        
        for(int i = 0; i < differentiationCriteria.length; i++) {
            Object[] row = new String[workConfiguration.getNbSerie() + 2];
            DifferentiationCriteriaLibelle scnaLib = new DifferentiationCriteriaLibelle(differentiationCriteria[i]);
            row[0] = scnaLib.toString();
            
            for(int j = 1; j < workConfiguration.getNbSerie() + 1; j++) {
                if(workConfiguration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]) != null)
                    row[j] = "" + workConfiguration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]).getDistance()[j-1];
                else
                    row[j] = "0";
            }
            if(workConfiguration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]) != null)
                row[workConfiguration.getNbSerie() + 1] = "" + workConfiguration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]).getBlason();
            else
                row[workConfiguration.getNbSerie() + 1] = "0";
            dtm.addRow(row);
        }
        
        return dtm;
    }
    
    private void affectLibelle() {
        jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
        jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        
        if(tabbedpane.getTabCount() > 0) {
            tabbedpane.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.genral"));
            tabbedpane.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.concours"));
            tabbedpane.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.etiquettes"));
            tabbedpane.setTitleAt(3, ConcoursJeunes.ajrLibelle.getResourceString("configuration.onglet.interface"));
        }
        
        affectLibelleGeneral();
        affectLibelleConcours();
        affectLibelleEtiquette();
        affectLibelleInterface();
    }
    
    private void affectLibelleGeneral() {
        jlNomProfil.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.profil"));
        jlNomClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.nom"));
        jlAgremClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.agrement"));
        jlIntituleConcours.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.intituleconcours"));
        jlLangue.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.langue"));
        jlPathPdf.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.pdf"));
        jlLogoPath.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.logo"));
        
        tbProfil.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.titre0"));
        tbParamGeneral.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.titre1"));
        
        jbAjouterProfil.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.addprofile"));
        jbDetail.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.detail"));
        if(jbLogoPath.getText().equals(""))
            jbLogoPath.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.logo"));
    }
    
    private void affectLibelleConcours() {
        jlNbCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.cible"));
        jlNbTireur.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.tireur"));
        jlNbSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.serie"));
        jlNbVoleeParSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.voleeparserie"));
        jlNbFlecheParVolee.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.flecheparvolee"));
        jlNbDepart.setText( ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.depart"));
        jlNbMembresEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.membresmax"));
        jlNbMembresRetenu.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.selectionmax"));

        jbAddCriteria.setIcon(new ImageIcon(workConfiguration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteria")));
        jbAddCriteria.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.addcriteria"));
        jbAddCriteriaMember.setIcon(new ImageIcon(workConfiguration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteriamember")));
        jbAddCriteriaMember.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.addcriteriamember"));
        jbUpElement.setIcon(new ImageIcon(workConfiguration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.upelement")));
        jbUpElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.upelement"));
        jbDownElement.setIcon(new ImageIcon(workConfiguration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.downelement")));
        jbDownElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.downelement"));
        jbRemoveElement.setIcon(new ImageIcon(workConfiguration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.removeelement")));
        jbRemoveElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.removeelement"));
        
        jlNbDB.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.db"));
        
        if(tabbedpaneConcours.getTabCount() > 0) {
            tabbedpaneConcours.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.onglet.pasdetir"));
            tabbedpaneConcours.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.onglet.criteres"));
            tabbedpaneConcours.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.onglet.categorie"));
        }
    }
    
    private void affectLibelleEtiquette() {
        jlFormatPapier.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.formatpapier"));
        jlNbEtiquettes.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.nbetiquettes"));
        jlMarges.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.marges"));
        jlMargesH.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.haut"));
        jlMargesB.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.bas"));
        jlMargesG.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.gauche"));
        jlMargesD.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.droite"));
        jlEspacements.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.espacement"));
        jlEspacementsH.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.horizontal"));
        jlEspacementsV.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.etiquettes.vertical"));
    }
    
    private void affectLibelleInterface() {
        jlResultats.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultat"));
        jlAffResultats.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.affresultat"));
        jlPathRessources.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.ressources"));
        jlURLImport.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.urlimport"));
        jlURLExport.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.urlexport"));
        jcbOfficialProfile.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.officialprofile"));
        jcbFirstBoot.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.firstboot"));
        
        tbPath.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.titre1"));
        
        jcbAvanceResultatCumul.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultatcumul"));
        jcbAvanceResultatSupl.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultatdnm"));
        jcbAvanceAffResultatExEquo.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.resultataffexequo"));
        
        jcbExpert.setText(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.interface.expert"));
    }
    
    private void completePanel(Configuration configuration) {
        completeGeneralPanel(configuration);
        completeConcoursPanel(configuration);
        completeEtiquettePanel(configuration);
        completeInterfacePanel(configuration);
    }
    
    private void completeGeneralPanel(Configuration configuration) {
        String[] libelleLangues = getPossibleLanguages();
        
        jtfNomClub.setText(configuration.getNomClub());
        jtfAgrClub.setText(configuration.getNumAgrement());
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
        
        jbLogoPath.setText("<html><img src=\"file:" + configuration.getLogoPath() + "\" width=90 height=100></html>");
    }
    
    private void completeConcoursPanel(Configuration configuration) {
        jtfNbCible.setText("" + configuration.getNbCible());
        jtfNbTireur.setText("" + configuration.getNbTireur());
        jtfNbSerie.setText("" + configuration.getNbSerie());
        jtfNbVoleeParSerie.setText("" + configuration.getNbVoleeParSerie());
        jtfNbFlecheParVolee.setText("" + configuration.getNbFlecheParVolee());
        jtfNbDepart.setText("" + configuration.getNbDepart());
        jtfNbMembresEquipe.setText("" +  configuration.getNbMembresEquipe());
        jtfNbMembresRetenu.setText("" + configuration.getNbMembresRetenu());
        
        jtfNbSerie.setEditable(!workConfiguration.isOfficialProfile());
        jtfNbVoleeParSerie.setEditable(!workConfiguration.isOfficialProfile());
        jtfNbFlecheParVolee.setEditable(!workConfiguration.isOfficialProfile());
        jtfNbDepart.setEditable(!workConfiguration.isOfficialProfile());
        jtfNbMembresEquipe.setEditable(!workConfiguration.isOfficialProfile());
        jtfNbMembresRetenu.setEditable(!workConfiguration.isOfficialProfile());
        
        jbAddCriteria.setEnabled(!workConfiguration.isOfficialProfile());
        jbAddCriteriaMember.setEnabled(!workConfiguration.isOfficialProfile());
        jbUpElement.setEnabled(!workConfiguration.isOfficialProfile());
        jbDownElement.setEnabled(!workConfiguration.isOfficialProfile());
        jbRemoveElement.setEnabled(!workConfiguration.isOfficialProfile());
  
        treeRoot.removeAllChildren();
        for(Criterion critere : workConfiguration.getListCriteria()) {
            if(critere != null) {
                DefaultMutableTreeNode dmtnCriteria = new DefaultMutableTreeNode(critere);
                treeRoot.add(dmtnCriteria);
                
                for(CriterionElement criterionIndividu : workConfiguration.getCriteriaPopulation().get(critere)) {
                    DefaultMutableTreeNode dmtnCriteriaIndiv = new DefaultMutableTreeNode(criterionIndividu);
                    dmtnCriteria.add(dmtnCriteriaIndiv);
                }
            }
        }
        treeModel.reload();
        jspDistanceBlason.setViewportView(jtDistanceBlason);
        
        jtDistanceBlason.setModel(createTableModel());
    }
    
    private void completeEtiquettePanel(Configuration configuration) {
        jcbFormatPapier.removeAllItems();
        for(Field champs : PageSize.class.getFields()) {
            jcbFormatPapier.addItem(champs.getName());
        }
        jcbFormatPapier.setSelectedItem(configuration.getFormatPapier());
        for(String orientation : new String[] {"portrait", "landscape"}) {
            jcbOrientation.addItem(orientation);
        }
        jcbOrientation.setSelectedItem(configuration.getOrientation());
        
        jtfLignes.setText("" + configuration.getColonneAndLigne()[0]);
        jtfColonnes.setText("" + configuration.getColonneAndLigne()[1]);
        jtfMargesH.setText("" + configuration.getMarges().getHaut());
        jtfMargesB.setText("" +configuration.getMarges().getBas());
        jtfMargesG.setText("" + configuration.getMarges().getGauche());
        jtfMargesD.setText("" +configuration.getMarges().getDroite());
        jtfEspacementsH.setText("" + configuration.getEspacements()[0]);
        jtfEspacementsV.setText("" + configuration.getEspacements()[1]);
    }
    
    private void completeInterfacePanel(Configuration configuration) {
        jcbAvanceResultatCumul.setSelected(configuration.isInterfaceResultatCumul());
        jcbAvanceResultatSupl.setSelected(configuration.isInterfaceResultatSupl());
        jcbAvanceAffResultatExEquo.setSelected(configuration.isInterfaceAffResultatExEquo());
        
        jtfPathRessources.setText(configuration.getRessourcesPath());
        jtfURLExport.setText(configuration.getExportURL());
        jtfURLImport.setText(configuration.getImportURL());
        
        jcbOfficialProfile.setSelected(configuration.isOfficialProfile());
    }
    
    /**
     * Recherche du logo club
     *
     */
    private void changeLogoPath() {
        File f;
        JFileChooser fileDialog = new JFileChooser(workConfiguration.getLogoPath());
        AJFileFilter filtreimg = new AJFileFilter(new String[] {"jpg","gif"},"Image jpeg ou gif");
        fileDialog.addChoosableFileFilter(filtreimg);
        fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        fileDialog.showOpenDialog(this);
        f = fileDialog.getSelectedFile();
        if (f == null) {
            return;
        }
        jbLogoPath.setText("<html><img src=\"file:" + f.getPath() + "\" width=90 height=100></html>");
        workConfiguration.setLogoPath(f.getPath());
    }
	
    /**
     * @return Renvoie workConfiguration.
     */
    public Configuration getWorkConfiguration() {
        return workConfiguration;
    }

    /**
     * @param workConfiguration workConfiguration à définir.
     */
    public void setWorkConfiguration(Configuration workConfiguration) {
        this.workConfiguration = workConfiguration;
    }

    /**
     * generere les ligne de distance et blason par code scna
     *
     */
    private void generateSCNA_DBRow(Configuration configuration) {
        DefaultTableModel dtm = (DefaultTableModel)this.jtDistanceBlason.getModel();
        
        //supprime toutes les lignes
        while(dtm.getRowCount() > 0)
            dtm.removeRow(0);
        
        if(dtm.getColumnCount() < configuration.getNbSerie() + 2) {
            jtDistanceBlason.removeColumn(jtDistanceBlason.getColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason")));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + configuration.getNbSerie());
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason"));
        }
        
        differentiationCriteria = DifferentiationCriteria.listeDifferentiationCriteria(workConfiguration.getPlacementFilter());
        
        for(int i = 0; i < differentiationCriteria.length; i++) {
            Object[] row = new String[configuration.getNbSerie() + 2];
            DifferentiationCriteriaLibelle libelle = new DifferentiationCriteriaLibelle(differentiationCriteria[i]);
            row[0] = libelle.toString();
            for(int j = 1; j < configuration.getNbSerie() + 1; j++) {
                if(configuration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]) != null)
                    row[j] = "" + configuration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]).getDistance()[j-1];
                else
                    row[j] = "0";
            }
            if(configuration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]) != null)
                row[configuration.getNbSerie() + 1] = "" + configuration.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i]).getBlason();
            else
                row[configuration.getNbSerie() + 1] = "0";
            dtm.addRow(row);
        }
    }
    
    /**
     * renvois la liste des profils de configuration
     * 
     * @return String[] - la liste des profils de configuration
     */
    private String[] listConfiguration() {
        String[] strConfig = new File(ConcoursJeunes.userRessources.getConfigPathForUser()).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if(name.startsWith(CONFIG_PROFILE) && name.endsWith(EXT_XML))
                    return true;
                return false;
            }
        });
      
        for(int i = 0; i < strConfig.length; i++)
            strConfig[i] = strConfig[i].substring(14, strConfig[i].length() - 4);
        return strConfig;
    }
    
    /**
     * Renvoie la liste des langues disponibles
     * 
     * @return String[] - retourne la liste des langues disponible
     */
    private String[] listLangue() {
        if(strLstLangue == null) {
            String[] strLng = new File("lang").list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if(name.startsWith("libelle_") && name.endsWith(".properties"))
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
    
    /**
     * sauvegarde la configuration général du programme
     *
     */
    private void saveConfig() {
        try {
            File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + 
                    File.separator + 
                    ConcoursJeunes.ajrParametreAppli.getResourceString("file.configuration"));
            AJToolKit.saveXMLStructure(f, workConfiguration, false);
        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.config"),
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
            npe.printStackTrace();
        }
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

                generateSCNA_DBRow(configuration);
                
                workConfiguration.setCurProfil((String)jcbProfil.getSelectedItem());

                d.close();
            }
        } catch (IOException io) {
            JOptionPane.showMessageDialog(this,
                    "<html>" + io.getMessage() + "</html>",
                    "IOException",JOptionPane.ERROR_MESSAGE);
            io.printStackTrace();
        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur.restore.config"),
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
            npe.printStackTrace();
        }
    }
    
    private void saveProfile() {
        try {
            File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + (String)jcbProfil.getSelectedItem() + EXT_XML);
            AJToolKit.saveXMLStructure(f, workConfiguration, false);
        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.config"),
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
            npe.printStackTrace();
        }
    }
    
    private void registerConfig() {
        if(listLangue()[jcbLangue.getSelectedIndex()].equals(ConcoursJeunes.configuration.getLangue()) &&
                jtfPathRessources.getText().equals(ConcoursJeunes.configuration.getRessourcesPath())) {
            reboot = false;
        }
        
        workConfiguration.setNomClub(jtfNomClub.getText());
        workConfiguration.setNumAgrement(jtfAgrClub.getText());
        workConfiguration.setIntituleConcours(jtfIntConc.getText());
        workConfiguration.setLangue(listLangue()[jcbLangue.getSelectedIndex()]);
        workConfiguration.setRessourcesPath(jtfPathRessources.getText());
        workConfiguration.setPdfReaderPath(jcbPathPdf.getSelectedItem().toString());
        workConfiguration.setExportURL(jtfURLExport.getText());
        workConfiguration.setImportURL(jtfURLImport.getText());
        
        workConfiguration.setNbCible(Integer.parseInt(jtfNbCible.getText()));
        workConfiguration.setNbTireur(Integer.parseInt(jtfNbTireur.getText()));
        workConfiguration.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));
        workConfiguration.setNbDepart(Integer.parseInt(jtfNbDepart.getText()));
        workConfiguration.setNbVoleeParSerie(Integer.parseInt(jtfNbVoleeParSerie.getText()));
        workConfiguration.setNbFlecheParVolee(Integer.parseInt(jtfNbFlecheParVolee.getText()));
        workConfiguration.setNbMembresEquipe(Integer.parseInt(jtfNbMembresEquipe.getText()));
        workConfiguration.setNbMembresRetenu(Integer.parseInt(jtfNbMembresRetenu.getText()));
        workConfiguration.setFirstboot(jcbFirstBoot.isSelected());
        workConfiguration.setOfficialProfile(jcbOfficialProfile.isSelected());
        
        workConfiguration.setCorrespondanceDifferentiationCriteria_DB(new Hashtable<DifferentiationCriteria, DistancesEtBlason>());
        for(int i = 0; i < jtDistanceBlason.getRowCount(); i++) {
            int[] distances = new int[workConfiguration.getNbSerie()];
            for(int j = 0; j < distances.length; j++) {
                if(j < jtDistanceBlason.getModel().getColumnCount() - 2)
                    distances[j] = Integer.parseInt((String)this.jtDistanceBlason.getModel().getValueAt(i, j+1));
                else
                    distances[j] = 0;
            }
            workConfiguration.putCorrespondanceDifferentiationCriteria_DB(differentiationCriteria[i],
                    new DistancesEtBlason(distances, 
                            Integer.parseInt((String)this.jtDistanceBlason.getModel().getValueAt(i, jtDistanceBlason.getModel().getColumnCount() - 1))));
        }
        workConfiguration.setFormatPapier((String)this.jcbFormatPapier.getSelectedItem());
        workConfiguration.setOrientation((String)this.jcbOrientation.getSelectedItem());
        workConfiguration.setColonneAndLigne(new int[] {Integer.parseInt(this.jtfLignes.getText()), Integer.parseInt(this.jtfColonnes.getText())});
        workConfiguration.setMarges(new Marges(Double.parseDouble(this.jtfMargesH.getText()), Double.parseDouble(this.jtfMargesB.getText()), Double.parseDouble(this.jtfMargesG.getText()), Double.parseDouble(this.jtfMargesD.getText())));
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
            saveConfig();
            
            ConcoursJeunes.configuration = workConfiguration;
            
			setVisible(false);
            
            if(reboot && JOptionPane.showConfirmDialog(this,
                    ConcoursJeunes.ajrLibelle.getResourceString("configuration.fermeture.message"),
                    ConcoursJeunes.ajrLibelle.getResourceString("configuration.fermeture.titre"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==  JOptionPane.YES_OPTION) {
                concoursJeunes.fireReboot();
            }

        } else if(source == jbAnnuler) {
			setVisible(false);
		} else if(source == jbLogoPath) {
            changeLogoPath();
        } else if(source == this.jbParcourirRessources) {
            try {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileDialog.setCurrentDirectory(new File(this.jtfPathRessources.getText()));

                if(fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    this.jtfPathRessources.setText(fileDialog.getSelectedFile().getCanonicalPath());
                }
            } catch (IOException io) {
                System.err.println("IOException: " + io.getMessage());
            } catch(NullPointerException npe) {
                System.err.println("Aucune sauvegarde possible. Action annulé");
            }
        } else if(source == this.jbParcourirPdf) {
			try {
				JFileChooser fileDialog = new JFileChooser();
				fileDialog.setCurrentDirectory(new File((String)this.jcbPathPdf.getSelectedItem()));

				if(fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    this.jcbPathPdf.addItem(fileDialog.getSelectedFile().getCanonicalPath());
                    this.jcbPathPdf.setSelectedItem(fileDialog.getSelectedFile().getCanonicalPath());
				}
			} catch (IOException io) {
				System.err.println("IOException: " + io.getMessage());
			} catch(NullPointerException npe) {
				System.err.println("Aucune sauvegarde possible. Action annulé");
			}
		} else if(source == this.jbAjouterProfil) {
            String strP = JOptionPane.showInputDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.general.newprofile"));
            if(strP != null && !strP.equals("")) {
                jcbProfil.addItem(strP);
                jcbProfil.setSelectedItem(strP);
                workConfiguration.setCurProfil(strP);
                workConfiguration.setOfficialProfile(false);
                jcbOfficialProfile.setSelected(false);
                completePanel(workConfiguration);
            }
        } else if(source == this.jbDetail) {
            EntiteDialog ed = new EntiteDialog(concoursJeunes);
            ed.showEntite(ConcoursJeunes.listeEntite.get(jtfAgrClub.getText()));
        } else if(source == jbAddCriteria) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
            if(dmtn != null) {
                CriterionDialog cd = new CriterionDialog(this);

                if(cd.getCriterion() != null) {
                    DefaultMutableTreeNode dmtnCrit = new DefaultMutableTreeNode(cd.getCriterion());
                    
                    dmtn.add(dmtnCrit);
                    
                    treeModel.reload();
                }
            }
        } else if(source == jbAddCriteriaMember) { 
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
            if(dmtn != null) {
                Object dmtnObj = dmtn.getUserObject();
                if(dmtnObj instanceof Criterion) {
                    TreePath selectedPath = treeCriteria.getSelectionPath();
                    CriterionElementDialog cpd = new CriterionElementDialog(this, (Criterion)dmtnObj);
                    
                    if(cpd.getCriterionIndividu() != null) {
                        DefaultMutableTreeNode dmtnIndiv = new DefaultMutableTreeNode(cpd.getCriterionIndividu());
                        
                        dmtn.add(dmtnIndiv);
                        
                        treeModel.reload();
                        treeCriteria.setSelectionPath(selectedPath);
                        //treeCriteria.expandPath(selectedPath);
                    
                        generateSCNA_DBRow(workConfiguration);
                    }
                }
            }
        } else if(source == jbUpElement) { 
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
            if(dmtn != null) {
                Object dmtnObj = dmtn.getUserObject();
                if(dmtnObj instanceof Criterion) {
                    int curIndex = workConfiguration.getListCriteria().indexOf(dmtnObj);
                    if(curIndex > 0) {
                        workConfiguration.getListCriteria().set(curIndex, workConfiguration.getListCriteria().get(curIndex-1));
                        workConfiguration.getListCriteria().set(curIndex-1, (Criterion)dmtnObj);
                    }
                    treeModel.removeNodeFromParent(dmtn);
                    
                    DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
                    dmtnRoot.insert(dmtn, curIndex - 1);
                    
                    treeModel.reload();
                    
                    generateSCNA_DBRow(workConfiguration);
                } else if(dmtnObj instanceof CriterionElement) {
                    TreePath selectedPath = treeCriteria.getSelectionPath();
                    DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
                            selectedPath.getParentPath().getLastPathComponent();
                    
                    int curIndex = workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).indexOf(dmtnObj);
                    if(curIndex > 0) {
                        workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).set(curIndex, workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).get(curIndex-1));
                        workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).set(curIndex-1, (CriterionElement)dmtnObj);
                    }
                    treeModel.removeNodeFromParent(dmtn);
                    
                    dmtnParent.insert(dmtn, curIndex - 1);
                    
                    treeModel.reload();
                    
                    generateSCNA_DBRow(workConfiguration);
                }
            }
        } else if(source == jbDownElement) { 
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
            if(dmtn != null) {
                Object dmtnObj = dmtn.getUserObject();
                if(dmtnObj instanceof Criterion) {
                    int curIndex = workConfiguration.getListCriteria().indexOf(dmtnObj);
                    if(curIndex < workConfiguration.getListCriteria().size() - 1) {
                        workConfiguration.getListCriteria().set(curIndex, workConfiguration.getListCriteria().get(curIndex+1));
                        workConfiguration.getListCriteria().set(curIndex+1, (Criterion)dmtnObj);
                    }
                    treeModel.removeNodeFromParent(dmtn);
                    
                    DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
                    dmtnRoot.insert(dmtn, curIndex + 1);
                    
                    treeModel.reload();
                    
                    generateSCNA_DBRow(workConfiguration);
                } else if(dmtnObj instanceof CriterionElement) {
                    TreePath selectedPath = treeCriteria.getSelectionPath();
                    DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
                            selectedPath.getParentPath().getLastPathComponent();
                    
                    int curIndex = workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).indexOf(dmtnObj);
                    if(curIndex < workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).size() - 1) {
                        workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).set(curIndex, workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).get(curIndex+1));
                        workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).set(curIndex+1, (CriterionElement)dmtnObj);
                    }
                    treeModel.removeNodeFromParent(dmtn);
                    
                    dmtnParent.insert(dmtn, curIndex + 1);
                    
                    treeModel.reload();
                    
                    generateSCNA_DBRow(workConfiguration);
                }
            }
        } else if(source == jbRemoveElement) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
            if(dmtn != null) {
                Object dmtnObj = dmtn.getUserObject();
                if(dmtnObj instanceof Criterion) {
                    workConfiguration.getCriteriaPopulation().remove(dmtnObj);
                    workConfiguration.getListCriteria().remove(dmtnObj);
                    
                    treeModel.removeNodeFromParent(dmtn);
                    
                    generateSCNA_DBRow(workConfiguration);
                } else if(dmtnObj instanceof CriterionElement) {
                    TreePath selectedPath = treeCriteria.getSelectionPath();
                    DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
                            selectedPath.getParentPath().getLastPathComponent();
                    
                    workConfiguration.getCriteriaPopulation().get(dmtnParent.getUserObject()).remove(dmtnObj);
                    
                    treeModel.removeNodeFromParent(dmtn);
                    
                    generateSCNA_DBRow(workConfiguration);
                }
            }
        } else if(source == jcbProfil) {
            if(!runInitDialog && jcbProfil.getSelectedIndex() > -1)
                loadProfile();
        } else if(source == jcbLangue) {
            if(!runInitDialog && jcbLangue.getSelectedIndex() > -1) {
                ConcoursJeunes.ajrLibelle = new AjResourcesReader("libelle", listLangue()[jcbLangue.getSelectedIndex()]);
                affectLibelle();
            }
        } else if(source == this.jcbExpert) {
            jbParcourirRessources.setEnabled(this.jcbExpert.isSelected());
            jtfPathRessources.setEnabled(this.jcbExpert.isSelected());
            jtfURLExport.setEnabled(this.jcbExpert.isSelected());
            jtfURLImport.setEnabled(this.jcbExpert.isSelected());
        }
	}

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
            
            Object dmtnObj = dmtn.getUserObject();
            if(dmtnObj instanceof Criterion) {
                TreePath selectedPath = treeCriteria.getSelectionPath();
                new CriterionDialog(this, (Criterion)dmtnObj);

                treeModel.reload(treeCriteria.getSelectedNode());
                treeCriteria.setSelectionPath(selectedPath);
                
                generateSCNA_DBRow(workConfiguration);
            } else if(dmtnObj instanceof CriterionElement) {
                TreePath selectedPath = treeCriteria.getSelectionPath();
                DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
                        selectedPath.getParentPath().getLastPathComponent();
                new CriterionElementDialog(this, (Criterion)dmtnParent.getUserObject(), 
                        (CriterionElement)dmtnObj);

                treeModel.reload(treeCriteria.getSelectedNode());
                treeCriteria.setSelectionPath(selectedPath);
                
                generateSCNA_DBRow(workConfiguration);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        if(workConfiguration != null) {
            registerConfig();
            jtDistanceBlason = new JTable(createTableModel());
            jspDistanceBlason.setViewportView(jtDistanceBlason);
        }
    }
}