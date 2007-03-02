package ajinteractive.concours;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.dialog.*;

/**
 * fiche concours.
 * cette fiche correspond à la table d'inscrit et de résultats
 * 
 * @author Aurélien Jeoffray
 * @version 4.0
 * 
 * TODO passage de niveau
 * 
 */
public class FicheConcoursFrame extends JInternalFrame implements ActionListener, ChangeListener, 
            MouseListener, MouseMotionListener, DragOutListener, ListSelectionListener, KeyListener,
            HyperlinkListener {

	//static
	public ConcoursJeunes concoursJeunes;
    
    //public
    public FicheConcours ficheConcours = new FicheConcours();
	
	//private
    private JTabbedPane tabbedpane     = new JTabbedPane();
    
	//bouton d'enregistrements des archers
	private JButton jbAjouterArcher    = new JButton();
	private JButton jbSupprimerArcher  = new JButton();
	private JButton jbEquipe           = new JButton();
	private JButton jbPlacementArcher  = new JButton();
	private ButtonGroup jbgSort;
	private JRadioButton jcbSortCible  = new JRadioButton("", true);
	private JRadioButton jcbSortNom    = new JRadioButton();
	private JRadioButton jcbSortClub   = new JRadioButton();
    
    private JButton jbPrintListConc    = new JButton();
    private JButton jbPrintEtiquettes  = new JButton();
    private JButton jbPrintPasDeTir    = new JButton();
	
	private JEditorPane jepClassIndiv  = new JEditorPane();					//panneau de classement
	private JEditorPane jepClassTeam   = new JEditorPane();
	//bouton d'enregistrement
	private JButton jbResultat         = new JButton();
    private JLabel jlCritClassement    = new JLabel();
	//critere individuel
	private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();
    
    private JButton printClassementIndiv = new JButton();;
	//critere par equipe
	//private JCheckBox sexeCaseEqu;
	//private JCheckBox armeCaseEqu;
    
    private JButton printClassementEquipe = new JButton();;
	
	private JSplitPane splitpane;
	private JScrollPane scrollarcher   = new JScrollPane();
	private JScrollPane scrollcible    = new JScrollPane();
	private AJList ajlConcurrent       = new AJList();
	private DnDJTree treeTarget        = new DnDJTree();
	private JPopupMenu popup;
	private DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
	private Concurrent tmpconc;

	private ParametreDialog paramDialog;
	private ConcurrentDialog concDialog;
	
	private int index = 1;

    /**
     * Création d'une fiche concours
     *  
     * 
     * @param concoursJeunes
     * @param ficheConcours
     */
	public FicheConcoursFrame(ConcoursJeunes concoursJeunes, FicheConcours ficheConcours) {
		super("", true, true, true, true); //$NON-NLS-1$
        
		this.concoursJeunes = concoursJeunes;
        this.ficheConcours = ficheConcours;
        
		init();
	}
    
	/**
     * initialise les parametres de la fiche.
     * 
	 */
	private void init() {
        JPanel northpaneGestion = new JPanel();
		JPanel northpane = new JPanel();
		JPanel northpaneEqu = new JPanel();
		
		//met la fiche de classement au format html
		jepClassIndiv.setEditorKit(new HTMLEditorKit());
        jepClassIndiv.setEditable(false);
        jepClassIndiv.addHyperlinkListener(this);
        
        //empeche le retour automatique au début de la page à chaque update
        ((DefaultCaret)jepClassIndiv.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		jepClassTeam.setEditorKit(new HTMLEditorKit());
        jepClassTeam.setEditable(false);
        //empeche le retour automatique au début de la page à chaque update
        ((DefaultCaret)jepClassTeam.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		//enregistre les auditeurs d'évenement
        jbPrintListConc.addActionListener(this);
        jbPrintEtiquettes.addActionListener(this);
        jbPrintPasDeTir.addActionListener(this);
        
		jbResultat.addActionListener(this);

        printClassementIndiv.addActionListener(this);
        printClassementEquipe.addActionListener(this);
		
		//option d'affichage du classement
        northpaneGestion.add(jbPrintListConc);
        northpaneGestion.add(jbPrintEtiquettes);
        northpaneGestion.add(jbPrintPasDeTir);
        
		//classement individuel
        northpane.add(jbResultat);

        northpane.add(jlCritClassement);
        for(Criterion criteria : ficheConcours.parametre.getListCriteria()) {
            classmentCriteriaCB.put(criteria.getCode(), 
                    new JCheckBox(criteria.getLibelle(), criteria.isClassement()));
            
            classmentCriteriaCB.get(criteria.getCode()).addActionListener(this);
            if(ConcoursJeunes.configuration.isOfficialProfile())
                classmentCriteriaCB.get(criteria.getCode()).setEnabled(false);
            northpane.add(classmentCriteriaCB.get(criteria.getCode()));
        }
        northpane.add(printClassementIndiv);
		
		//classement par equipe
		//northpaneEqu.add(new JLabel(concoursJeunes.ajrLibelle.getResourceString("interface.critereclassement")));
		//northpaneEqu.add(sexeCaseEqu);
		//northpaneEqu.add(armeCaseEqu);
        northpaneEqu.add(printClassementEquipe);
        
        //panneau de gestion
        JPanel ficheG = new JPanel();
        ficheG.setLayout(new BorderLayout());
        ficheG.add(northpaneGestion,BorderLayout.NORTH);
        ficheG.add(listeParCible(),BorderLayout.CENTER);
		
		//paneau du classement individuel
		JPanel ficheI = new JPanel();
		ficheI.setLayout(new BorderLayout());
		ficheI.add(northpane,BorderLayout.NORTH);
		ficheI.add(new JScrollPane(jepClassIndiv),BorderLayout.CENTER);
		
		//paneau du classement par equipe
		JPanel ficheE = new JPanel();
		ficheE.setLayout(new BorderLayout());
		ficheE.add(northpaneEqu,BorderLayout.NORTH);
		ficheE.add(new JScrollPane(jepClassTeam),BorderLayout.CENTER);
		
		//creation des tables de tireurs
		scrollarcher.setViewportView(ajlConcurrent);
		
		//panneau des archers
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, listeParNom(), ficheG);
		splitpane.setOneTouchExpandable(true);
		splitpane.setResizeWeight(0.25); 
		
		//creation de l'arbre de cible
		scrollcible.setViewportView(treeTarget);
		
		//panneau global
		tabbedpane.addChangeListener(this);
		tabbedpane.addTab("onglet.gestionarcher",null,splitpane); //$NON-NLS-1$
		tabbedpane.addTab("onglet.classementindividuel",
                new ImageIcon(ConcoursJeunes.configuration.getRessourcesPath() + 
                        File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer")),
                ficheI); //$NON-NLS-1$
		tabbedpane.addTab("onglet.classementequipe",new ImageIcon(ConcoursJeunes.configuration.getRessourcesPath() + 
                File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team")),
                ficheE); //$NON-NLS-1$
		
		//integration
		setContentPane(tabbedpane);
		setSize(500,500);
		
		popup();
        
        affectLibelle();
	}
    
    /**
     * Initialisation  du panneau "liste des concurrents"
     * 
     * @return JPanel
     */
    private JPanel listeParNom() {
        //liste des tireurs par nom
        JPanel pane = new JPanel();
        
        jbAjouterArcher.setMargin(new Insets(0,0,0,0));
        jbAjouterArcher.addActionListener(this);
        
        jbSupprimerArcher.setMargin(new Insets(0,0,0,0));
        jbSupprimerArcher.setEnabled(false);
        jbSupprimerArcher.addActionListener(this);
        
        jbEquipe.setMargin(new Insets(0,0,0,0));
        jbEquipe.addActionListener(this);
        
        jbPlacementArcher.setMargin(new Insets(0,0,0,0));
        jbPlacementArcher.addActionListener(this);
        
        jbgSort = new ButtonGroup();
        
        jcbSortCible.addActionListener(this);
        jcbSortNom.addActionListener(this);
        jcbSortClub.addActionListener(this);
        
        jbgSort.add(jcbSortCible);
        jbgSort.add(jcbSortNom);
        jbgSort.add(jcbSortClub);
        
        //paneau des boutons
        Box buttonPane = Box.createHorizontalBox();
        buttonPane.add(jbAjouterArcher);
        buttonPane.add(jbSupprimerArcher);
        buttonPane.add(jbEquipe);
        buttonPane.add(jbPlacementArcher);
        
        JPanel sortPane = new JPanel();
        sortPane.add(jcbSortCible);
        sortPane.add(jcbSortNom);
        sortPane.add(jcbSortClub);
        
        JPanel headerPane = new JPanel();
        headerPane.setLayout(new BorderLayout());
        headerPane.add(buttonPane, BorderLayout.NORTH);
        headerPane.add(sortPane, BorderLayout.CENTER);

        //initialistaion du panneau des tireurs
        pane.setLayout(new BorderLayout());
        pane.add(headerPane,BorderLayout.NORTH);
        pane.add(scrollarcher,BorderLayout.CENTER);
        
        ajlConcurrent.addMouseListener(this);
        ajlConcurrent.addMouseMotionListener(this);
        ajlConcurrent.addListSelectionListener(this);
        ajlConcurrent.addKeyListener(this);
        ajlConcurrent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ajlConcurrent.setCellRenderer(new ConcoursListeRenderer(
                new ImageIcon(
                        ConcoursJeunes.configuration.getRessourcesPath() + File.separator + 
                        ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")), //$NON-NLS-1$
                new ImageIcon(
                        ConcoursJeunes.configuration.getRessourcesPath() + File.separator + 
                        ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.notarget")))); //$NON-NLS-1$
        
        return pane;
    }
    
    /**
     * Initialisation de l'arbre des cibles
     * 
     * @return JPanel
     */
    private JPanel listeParCible() {
        JPanel pane = new JPanel();
        
        pane.setLayout(new BorderLayout());
        pane.add(scrollcible,BorderLayout.CENTER);
        
        treeTarget.setModel(treeModel);
        treeTarget.addMouseListener(this);
        treeTarget.addMouseMotionListener(this);
        treeTarget.addKeyListener(this);
        treeTarget.addOut(this);
        treeTarget.setCellRenderer(new CibleRenderer(
                new ImageIcon(
                		ConcoursJeunes.configuration.getRessourcesPath() + File.separator + 
                		ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")), //$NON-NLS-1$
                new ImageIcon(
                		ConcoursJeunes.configuration.getRessourcesPath() + File.separator + 
                		ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.target")))); //$NON-NLS-1$
        treeTarget.setToggleClickCount(3);
        
        return pane;
    }
    
    /**
     * affecte les libelle localisé à l'interface
     *
     */
    private void affectLibelle() {
        jbPrintListConc.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printlistconc")); //$NON-NLS-1$
        jbPrintEtiquettes.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printetiquettes")); //$NON-NLS-1$
        jbPrintPasDeTir.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printpasdetir")); //$NON-NLS-1$
        
        jbResultat.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.saisieresultats")); //$NON-NLS-1$
        printClassementIndiv.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
        printClassementEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
        jlCritClassement.setText(ConcoursJeunes.ajrLibelle.getResourceString("interface.critereclassement")); //$NON-NLS-1$
        tabbedpane.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher")); //$NON-NLS-1$
        tabbedpane.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementindividuel")); //$NON-NLS-1$
        tabbedpane.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementequipe")); //$NON-NLS-1$
        
        ((DefaultMutableTreeNode)treeModel.getRoot()).setUserObject(ConcoursJeunes.ajrLibelle.getResourceString("treenode.racine")); //$NON-NLS-1$
        treeModel.reload();
        
        jbAjouterArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
        jbSupprimerArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer")); //$NON-NLS-1$
        jbEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.equipe")); //$NON-NLS-1$
        jbPlacementArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.placementarcher")); //$NON-NLS-1$
        jcbSortCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.cible")); //$NON-NLS-1$
        jcbSortNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.nom")); //$NON-NLS-1$
        jcbSortClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.club")); //$NON-NLS-1$
    }
    
    /**
     * affecte le contenu à la fiche
     *
     */
    public void initContent() {
        concDialog = new ConcurrentDialog(this);
        paramDialog = new ParametreDialog(this);
        createListeParNom();
        createListeParCible();
        setTitle(ficheConcours.parametre.getIntituleConcours() + " - " + ficheConcours.parametre.getDate()); //$NON-NLS-1$
    }
    
    /**
     * Affiche la boite de dialogue des parametre du concours
     */
    public void openParametreDialog() {
        if(paramDialog != null) {
            paramDialog.setVisible(true);   
        } else
            paramDialog = new ParametreDialog(this);
            
    }
    
    /**
     * menu clic droit de gestion des concurrents
     */
    public void popup() {
        popup = new JPopupMenu("Edit");
        
        JMenuItem mi3 = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.edition"));
        mi3.setActionCommand("popup.edition"); //$NON-NLS-1$
        mi3.addActionListener(this);
        popup.add(mi3);
        
        JMenuItem mi1 = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.suppression")); //$NON-NLS-1$
        mi1.setActionCommand("popup.suppression"); //$NON-NLS-1$
        mi1.addActionListener(this);
        popup.add(mi1);
        
        popup.addSeparator();
        
        JMenuItem mi2 = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.retrait")); //$NON-NLS-1$
        mi2.setActionCommand("popup.retrait"); //$NON-NLS-1$
        mi2.addActionListener(this);
        popup.add(mi2);
        
        ajlConcurrent.add(popup);
    }
    
    /**
     * genere la liste des archer trié par nom
     *
     */
	private void createListeParNom() {
        ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES));
	}
    
	/**
	 * Arborescence du pas de tir
	 * 
	 */
	private void createListeParCible() {
		//Construction de l'arborescence du pas de tir
		//Construit le Noeud racine "Cibles"
        DefaultMutableTreeNode tnRoot = (DefaultMutableTreeNode)treeModel.getRoot();
        
        createTargetNodes();
        
        Concurrent[] concurrents = ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES);
        for(Concurrent concurrent : concurrents) {
            if(concurrent.getCible() > 0) {
                DefaultMutableTreeNode tnTarget = (DefaultMutableTreeNode)treeModel.getChild(tnRoot, concurrent.getCible() - 1);
                
                ((Cible)tnTarget.getUserObject()).addConcurrent(concurrent);
                
                DefaultMutableTreeNode tnPosition = (DefaultMutableTreeNode)treeModel.getChild(tnTarget, concurrent.getPosition());
                tnPosition.setUserObject(concurrent);
            }
        }
        treeModel.reload();
	}
    
    /**
     *  Construit l'arbre du pas de tir (Vide)
     *
     */
    private void createTargetNodes() {
        Cible newTarget;
        
        DefaultMutableTreeNode tnRoot = (DefaultMutableTreeNode)treeModel.getRoot();
        tnRoot.removeAllChildren();
        
        for(int i = 1; i <= ficheConcours.parametre.getNbCible(); i++) {
            newTarget = new Cible(i, ficheConcours);
            
            //Construit les noeuds cibles
            DefaultMutableTreeNode targetNode = new DefaultMutableTreeNode(newTarget);
            
            //Boucle pour génération A,B,C,D
            for(int j = 0; j < ficheConcours.parametre.getNbTireur(); j++) {
                //Construits les positions de tir A,B,C,D,...
                
                DefaultMutableTreeNode positionNode = new DefaultMutableTreeNode(getCibleLibelle(i, j));
                
                targetNode.add(positionNode);
            }
            
            tnRoot.add(targetNode);
        }
    }
    
	/**
	 * Affiche la boite de dialogue de gestion des concurrents
	 * 
	 * @param concurrent - le concurrent à editer
	 */
	private void modifConcurrent(Concurrent concurrent) {
		if(concurrent != null) {
	        generatePlaceDisponible();
			concDialog.editConcurrent(concurrent);
	
	        if(concDialog.suite || concDialog.precedent) {
                if(concDialog.suite) {
                    modifConcurrent(nextConcurrent(concurrent));
                } else {
            	    modifConcurrent(previousConcurrent(concurrent));
                }
	        } else
                ficheConcours.silentSave();
		}
	}
	
	/**
	 * Passe au concurrent suivant par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * @return Concurrent - le concurrent suivant
	 */
	private Concurrent nextConcurrent(Concurrent curConcurrent) {
		int cible = curConcurrent.getCible(); 
        int position = curConcurrent.getPosition();
        
        do {
        	position++;
            if(position == ficheConcours.parametre.getNbTireur()) {
                position = 0;
                cible++;
            }
        } while(ficheConcours.archerlist.getConcurrentAt(cible, position) == null && cible <= ficheConcours.parametre.getNbCible());
        
		return ficheConcours.archerlist.getConcurrentAt(cible, position);
	}
	
	/**
	 * Passe au concurrent précédent par ordre de cible/position
	 * 
	 * @param curConcurrent - le concurrent courrant
	 * @return Concurrent - le concurrent précedent
	 */
	private Concurrent previousConcurrent(Concurrent curConcurrent) {
		int cible = curConcurrent.getCible(); 
        int position = curConcurrent.getPosition();
        
        do {
        	position--;
            if(position == -1) {
                position = ficheConcours.parametre.getNbTireur() - 1;
                cible--;
            }
        } while(ficheConcours.archerlist.getConcurrentAt(cible, position) == null && cible > 0);
        
		return ficheConcours.archerlist.getConcurrentAt(cible, position);
	}
	
	/**
	 * Suppression d'un archer de la liste des concurrents
	 * 
	 * @param removedConcurrent
	 */
	private void removeConcurrent(Concurrent removedConcurrent) {
        
        if(JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression"), //$NON-NLS-1$
        		ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) //$NON-NLS-1$
            return;
        
        ficheConcours.removeConcurrent(removedConcurrent);

		//supression dans l'arbre
        if(removedConcurrent.getCible() > 0) {
            DefaultMutableTreeNode tnRoot = ((DefaultMutableTreeNode)treeTarget.getPathForRow(0).getLastPathComponent());
            
    		DefaultMutableTreeNode cibleTreeNode = (DefaultMutableTreeNode)treeModel.getChild(tnRoot, removedConcurrent.getCible() - 1);
    		DefaultMutableTreeNode tn = (DefaultMutableTreeNode)treeModel.getChild(cibleTreeNode, removedConcurrent.getPosition());
    		if(tn.getUserObject() == removedConcurrent) {
    			((Cible)cibleTreeNode.getUserObject()).removeConcurrent(removedConcurrent);
    			
    			tn.setUserObject(getCibleLibelle(removedConcurrent.getCible(), removedConcurrent.getPosition()));
    			treeModel.reload(cibleTreeNode);
    		}
        }
		
		//suppression dans l'ajlist
		ajlConcurrent.remove(removedConcurrent);
	}
	
    /**
	 * routine de placement automatique des concurrents sur le pas de tir
	 * en fonction des critéres SCNA
	 *
	 */
	private void placementConcurrent() {
        ficheConcours.placementConcurrent();
		
		//recupere la racine de l'arbre
		DefaultMutableTreeNode tnRoot = ((DefaultMutableTreeNode)treeTarget.getPathForRow(0).getLastPathComponent());
        
        //réinitialise l'arbre
        for(int i = 0; i < tnRoot.getChildCount(); i++) {
            DefaultMutableTreeNode dmtnCible = (DefaultMutableTreeNode)tnRoot.getChildAt(i);
            ((Cible)dmtnCible.getUserObject()).removeAll();
   
            //retire le concurrent de l'arbre
            for(int j = 0; j < dmtnCible.getChildCount(); j++) {
                DefaultMutableTreeNode dmtnPosition = (DefaultMutableTreeNode)dmtnCible.getChildAt(j);
                dmtnPosition.setUserObject(getCibleLibelle(i, j));
            }
        }
        
        for(int i = 0; i < tnRoot.getChildCount(); i++) {
            Concurrent[] concurrents = ficheConcours.archerlist.list(i + 1);
            DefaultMutableTreeNode dmtnCible = (DefaultMutableTreeNode)tnRoot.getChildAt(i);
            Cible tmpcbl = (Cible)dmtnCible.getUserObject();
            for(int j = 0; j < concurrents.length; j++) {
                tmpcbl.addConcurrent(concurrents[j]);
                DefaultMutableTreeNode dmtnPosition = (DefaultMutableTreeNode)dmtnCible.getChildAt(concurrents[j].getPosition());
                dmtnPosition.setUserObject(concurrents[j]);
            }
        }
		
        treeModel.reload();
		ajlConcurrent.repaint();
	}
    
    /**
     * placement manuel (en drag n'drop) d'un concurrent sur le pas de tir
     * 
     * @param destPath - le chemin du noeud de destination
     */
    private void placementManuelConcurrent(TreePath destPath) {
        if(destPath == null)
            return;
        Cible cible;
        int position = -1;
		
		int oldCible = 0;
		int oldPosition = 0;
        
        DefaultMutableTreeNode cibleTreeNode;
        
        //selectionne la destination
        treeTarget.setSelectionPath(destPath);
        
        //recupere le noeud destination
        cibleTreeNode = (DefaultMutableTreeNode)destPath.getLastPathComponent();
        
		oldCible = tmpconc.getCible();
		oldPosition = tmpconc.getPosition();
        
        //si drop sur cible
        if(cibleTreeNode.getChildCount() > 0) {
            //recuperation de la cible
            cible = (Cible)cibleTreeNode.getUserObject();
            
            DifferentiationCriteria dci = tmpconc.getDifferentiationCriteria();
            dci.setFiltreCriteria(ficheConcours.parametre.getPlacementFilter());
            DistancesEtBlason distancesConcurrent = ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(dci);

            if(cible.getDistancesEtBlason() == null) {
                position = cible.insertConcurrent(tmpconc);
            } else if(cible.getDistancesEtBlason().equals(distancesConcurrent))
                position = cible.insertConcurrent(tmpconc);

        } else {
            cible = (Cible)((DefaultMutableTreeNode)cibleTreeNode.getParent()).getUserObject();
            
            if(cibleTreeNode.getUserObject() instanceof Concurrent)
                return;
            
            DifferentiationCriteria dci = tmpconc.getDifferentiationCriteria();
            dci.setFiltreCriteria(ficheConcours.parametre.getPlacementFilter());
            DistancesEtBlason distancesConcurrent = ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(dci);

            if(cible.getDistancesEtBlason() == null) {
                position = (((String)(cibleTreeNode.getUserObject())).charAt(2)) - ('A');
            } else if(cible.getDistancesEtBlason().equals(distancesConcurrent))
                position = (((String)(cibleTreeNode.getUserObject())).charAt(2)) - ('A');

            if(position != -1)
                cible.setConcurrentAt(tmpconc, position);
        }
        

        if(position != -1) {
			if(oldCible != 0) {
				//recherche l'objet cible associé
	            DefaultMutableTreeNode tnRoot = ((DefaultMutableTreeNode)treeTarget.getPathForRow(0).getLastPathComponent());
	            Cible tmpcbl = (Cible)((DefaultMutableTreeNode)tnRoot.getChildAt(oldCible - 1)).getUserObject();
	            
                if(tmpconc.getCible() != oldCible) {
                    //retire le concurrent de la cible
                    tmpcbl.removeConcurrent(tmpconc);
                } else {                
        	        //retire le concurrent de la cible
        	        tmpcbl.removeConcurrentAt(oldPosition);
                }
	            
	            //retire le concurrent de l'arbre
	            DefaultMutableTreeNode dmtnChild = (DefaultMutableTreeNode)((DefaultMutableTreeNode)tnRoot.getChildAt(oldCible - 1)).getChildAt(oldPosition);
	            dmtnChild.setUserObject(getCibleLibelle(oldCible, oldPosition));
	            
	            treeModel.reload(tnRoot.getChildAt(oldCible - 1));
			}
            
            if(cibleTreeNode.getChildCount() > 0) {
                ((DefaultMutableTreeNode)cibleTreeNode.getChildAt(position)).setUserObject(tmpconc);
                
                treeModel.reload(cibleTreeNode);
                treeModel.reload(cibleTreeNode.getChildAt(position));
            } else {
                cibleTreeNode.setUserObject(tmpconc);
                
                treeModel.reload(cibleTreeNode.getParent());
                treeModel.reload(cibleTreeNode);
            }
        } else {
            JOptionPane.showMessageDialog(new JDialog(),
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement"), //$NON-NLS-1$
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        }
        ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES));
    }
    
    /**
     * Affiche la boite de dialogue de saisie des résultats
     */
    private void modifResultats() {
        ResultatDialog resultat = new ResultatDialog(this, index);
        
        //sort la listes des concurrents
        Concurrent[] listConcurrents = ficheConcours.archerlist.list(index);
        
        //si annulation ne pas continuer
        if(resultat.getAction() == ResultatDialog.CANCEL)
        	return;
        
        int[][] p = resultat.getPoints();
        int[][] s = resultat.getSuplements();
        //pour chaque concurrent
        for(int i = 0; i < listConcurrents.length; i++) {
                listConcurrents[i].setScore(p[listConcurrents[i].getPosition()]);
                if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
	                listConcurrents[i].setDix(s[listConcurrents[i].getPosition()][0]);
	                listConcurrents[i].setNeuf(s[listConcurrents[i].getPosition()][1]);
	                listConcurrents[i].setManque(s[listConcurrents[i].getPosition()][2]);
                }
        }

        index = resultat.getIndex();
        if(resultat.getAction() == ResultatDialog.NEXT_TARGET) {
	        do {
	            index++;
	        } while(ficheConcours.archerlist.list(index).length == 0 && index <= ficheConcours.parametre.getNbCible());
        } else if(resultat.getAction() == ResultatDialog.PREVIOUS_TARGET) {
        	do {
	            index--;
	        } while(ficheConcours.archerlist.list(index).length == 0 && index > 0);
        }
        
        if(resultat.getAction() != ResultatDialog.SAVE_AND_QUIT && (index > 0 && index <= ficheConcours.parametre.getNbCible())) {
            modifResultats();
        } else {
            index = 1;
        }
        ficheConcours.silentSave();
    }

    /**
     * Donne un libelle textuel pour une position
     * 
     * @param cible - la cible de la position
     * @param position - l'index de la position
     * @return String - le libelle de la position
     */
    private String getCibleLibelle(int cible, int position) {
        return ((cible < 10) ? "0" : "") + cible + (char)('A' + position); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * calcul la place occupé et disponible sur le concours
     *
     */
    private void generatePlaceDisponible() {
        //recupere le nombre d'archer total possible
        int nbArcherTotal = 0;
        int placeLibreSurCible = 0;
        int nbCibleOccupe = 0;
        int[] nbParDistanceBlason;
        
        Hashtable<DifferentiationCriteria, DistancesEtBlason> correspSCNA_DB = ConcoursJeunes.configuration.getCorrespondanceDifferentiationCriteria_DB();
        
        Enumeration<DifferentiationCriteria> scnaKeysEnum = correspSCNA_DB.keys();
        DifferentiationCriteria[] scnaKeys = new DifferentiationCriteria[correspSCNA_DB.size()];
        for(int i = 0; scnaKeysEnum.hasMoreElements(); i++) {
            scnaKeys[i] = scnaKeysEnum.nextElement();
        }
        
        DifferentiationCriteria.sortDifferentiationCriteria(scnaKeys, ficheConcours.parametre.getListCriteria());
        
        //liste le nombre d'acher par distances/blason différents
        //pour chaque distance/blason
        nbParDistanceBlason = new int[correspSCNA_DB.size()];
        int i = 0;
        for(DifferentiationCriteria key : scnaKeys) {
            
            concDialog.addCategoriePlacement(key);
            
            nbParDistanceBlason[i] = ficheConcours.archerlist.countArcher(correspSCNA_DB.get(key));
            nbArcherTotal += nbParDistanceBlason[i];
            
            placeLibreSurCible = (ficheConcours.parametre.getNbTireur() - nbParDistanceBlason[i] % ficheConcours.parametre.getNbTireur()) % ficheConcours.parametre.getNbTireur();
            
            nbCibleOccupe += (nbParDistanceBlason[i] + placeLibreSurCible) / ficheConcours.parametre.getNbTireur();
            
            concDialog.setPlaceLibre(correspSCNA_DB.get(key), new int[] {nbParDistanceBlason[i], placeLibreSurCible});
            
            i++;
        }
        concDialog.setCibleVide(ficheConcours.parametre.getNbCible() - nbCibleOccupe);
    }
    
    /**
     * Exporte les résultats d'un concours pour un traitement ultérieur
     * sur le site internet (mise en commun et diffusion)
     * 
     * @param export - la chaine XML des réultats à exporter
     */
    public void exportConcours(String export) {
        ExportDialog exportDialog = new ExportDialog(concoursJeunes);
        try {
            File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "concours" + ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if(exportDialog.exportType == ExportDialog.EXPORT_FILE) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new AJFileFilter(ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export").substring(1), ConcoursJeunes.ajrLibelle.getResourceString("message.export.fichier"))); //$NON-NLS-1$ //$NON-NLS-2$
                int returnVal = chooser.showSaveDialog(this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    if(chooser.getSelectedFile().getAbsolutePath().endsWith(ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export"))) { //$NON-NLS-1$
                        f = chooser.getSelectedFile();
                    } else {
                        f = new File(chooser.getSelectedFile().getAbsolutePath() + ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export")); //$NON-NLS-1$
                    }
                    FileWriter fw = new FileWriter(f);
                    fw.write(export, 0, export.length());
                    fw.close();
                    
                     JOptionPane.showMessageDialog(this,
                    		ConcoursJeunes.ajrLibelle.getResourceString("message.export.fin"), //$NON-NLS-1$
                    		ConcoursJeunes.ajrLibelle.getResourceString("message.export"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
                }
            } else if(exportDialog.exportType == ExportDialog.EXPORT_INTERNET) {
                OutputStreamWriter writer = null;
                BufferedReader reader = null;

                //création de la connection
                URL url = new URL(ConcoursJeunes.configuration.getExportURL());
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(URLEncoder.encode("concours", "UTF-8")+ //$NON-NLS-1$ //$NON-NLS-2$
                        "="+URLEncoder.encode(export, "UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
                writer.flush();
                writer.close();
                
                //lecture de la réponse
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                   if(ligne.equals("OK")) //$NON-NLS-1$
                       JOptionPane.showMessageDialog(this,
                    		   ConcoursJeunes.ajrLibelle.getResourceString("message.export.fin"), //$NON-NLS-1$
                    		   ConcoursJeunes.ajrLibelle.getResourceString("message.export"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
                }
                
                
            }
        } catch (UnknownHostException uhe) {
            JOptionPane.showMessageDialog(this,
                    "<html>" + ConcoursJeunes.ajrLibelle.getResourceString("erreur.connection") + " " + uhe.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur.unknownhost"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            uhe.printStackTrace();
        } catch (IOException io) {
            JOptionPane.showMessageDialog(this,
                    "<html>" + io.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
                    "IOException",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            io.printStackTrace();
        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(this,
            		ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.export"), //$NON-NLS-1$
            		ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            npe.printStackTrace();
        }
    }

	///////////////////////////////////////
	// Auditeur d'événement
	//////////////////////////////////////
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		String cmd = ae.getActionCommand();
        
		if(source == jbAjouterArcher) {
			//Ajoute un concurrent à la liste des inscrits
			Concurrent concur;
			do {
                generatePlaceDisponible();
				
				concur = new Concurrent();
				concDialog.editConcurrent(concur);
                
				if((!concDialog.valid && !concDialog.suite) || concur.getNom().equals("")) { //$NON-NLS-1$
					concur = null;
				} else {
                    ficheConcours.addConcurrent(concur);
                    ajlConcurrent.add(concur);
				}
			} while(concDialog.suite);
		} else if(source == jbSupprimerArcher) {
			//Supprime un concurrent à la liste des inscrits
			removeConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
            //classement();
		} else if(source == jbEquipe) {
			//Creer les equipes
			EquipeDialog ed = new EquipeDialog(this);
            if(ed.isValider()) {
                ficheConcours.equipes.removeAll();
                for(Equipe equipe : ed.getEquipes())
                    ficheConcours.equipes.add(equipe);
            }
		} else if(source == jbPlacementArcher) {
			if(JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.replacement"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("confirmation.replacement.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {//$NON-NLS-1$
				placementConcurrent();
				ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES));
			}
        } else if(source == jbPrintListConc) {
            TypeListingDialog tld = new TypeListingDialog(concoursJeunes);
            if(tld.returnType == TypeListingDialog.ALPHA)
                ficheConcours.printArcherList(FicheConcours.ALPHA);
            else if(tld.returnType == TypeListingDialog.GREFFE)
                ficheConcours.printArcherList(FicheConcours.GREFFE);
        } else if(source == jbPrintEtiquettes) {
            ficheConcours.printEtiquettes();
        } else if(source == jbPrintPasDeTir) {
            ficheConcours.printPasDeTir();
        } else if(source == jbResultat) {
			index = 1;
			modifResultats();
			jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
		} else if(source == printClassementIndiv) {
            ficheConcours.printClassement();
        } else if(source == printClassementEquipe) {
            ficheConcours.printClassementEquipe();
        } else if(source instanceof JCheckBox) {
            for(Criterion criterion : ficheConcours.parametre.getListCriteria()) {
                criterion.setClassement(classmentCriteriaCB.get(criterion.getCode()).isSelected());
            }
            
            //ficheConcours.parametre.setListCriteria()
            jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
		} else if(source instanceof JRadioButton) {
			if(source == jcbSortCible)
				ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES));
			else if(source == jcbSortNom)
				ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_NAME));
			else if(source == jcbSortClub)
				ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CLUBS));
			ajlConcurrent.repaint();
		} else if(cmd.equals("popup.edition")) { //$NON-NLS-1$
            modifConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
		} else if(cmd.equals("popup.suppression")) { //$NON-NLS-1$
			//Supprime un concurrent à la liste des inscrits
			removeConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
		} else if(cmd.equals("popup.retrait")) { //$NON-NLS-1$
			//donne le concurrent à retirer
			Concurrent tmpconc = (Concurrent)ajlConcurrent.getSelectedValue();
			
			//retire le concurrent de la cible
			Cible curCible = (Cible)((DefaultMutableTreeNode)treeTarget.getSelectedNode().getParent()).getUserObject();
			curCible.removeConcurrent((Concurrent)treeTarget.getSelectedNode().getUserObject());
            
            //retire le concurrent de l'arbre
			treeTarget.getSelectedNode().setUserObject(getCibleLibelle(tmpconc.getCible(), tmpconc.getPosition()));
            
            //desaffecte son numero de cible
			tmpconc.setCible(0);
			
            //recharge l'arbre et la liste
			treeModel.reload(treeTarget.getSelectedNode());
			ajlConcurrent.setListData(ArcherList.sort(ficheConcours.archerlist.list(), ArcherList.SORT_BY_CIBLES));
			ajlConcurrent.repaint();
		}
	}
    /**
     * Gestion des changements d'onglet
     */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if(i > 0) {
				int nbconcurrents = ficheConcours.archerlist.countArcher();
				Concurrent[] listConcurrents = ficheConcours.archerlist.list();
				for(int j=0;j<nbconcurrents;j++) {
					if(listConcurrents[j].getCible() == 0) {
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						break;
					}
				}
                if(i == 1)
                    jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
                else if(i == 2)
                    jepClassTeam.setText(ficheConcours.getClassementEquipe(FicheConcours.OUT_HTML));
			}
		}
	}
    
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() instanceof JTree) {
			if(e.getModifiers()==MouseEvent.BUTTON3_MASK) {
                Point coord = e.getPoint();
                
                TreePath destinationPath = treeTarget.getPathForLocation(coord.x, coord.y);
                
                //recupere le noeud destination et son parent
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)destinationPath.getLastPathComponent();
                treeTarget.setSelectionPath(destinationPath);
                
                ajlConcurrent.setSelectedValue(node.getUserObject(), true);
                
                if(node.getUserObject() instanceof Concurrent)
                    popup.show(e.getComponent(), e.getX(), e.getY());
			} else {
				JTree tmptree = (JTree)e.getSource();
				int selRow = tmptree.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1) {
				    if(e.getClickCount() == 2) {
		        		DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode)tmptree.getSelectionPath().getLastPathComponent();
	       				
	       				if(selectedTreeNode.getUserObject() instanceof Concurrent) {
	       				    modifConcurrent((Concurrent)selectedTreeNode.getUserObject());
	       				} else if(selectedTreeNode.getUserObject() instanceof Cible) {
	       					index = ((Cible)selectedTreeNode.getUserObject()).getNumCible();
	       					modifResultats();
	       				} else {
	       					index = 1;
	       					modifResultats();
	       				}
	        	    } else {
                        Point coord = e.getPoint();
                        TreePath destinationPath = treeTarget.getPathForLocation(coord.x, coord.y);
                            
                        //recupere le noeud destination et son parent
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)destinationPath.getLastPathComponent();
                        if(node.getUserObject() instanceof Cible) {
                            //tree2.isE
                            //tree2.expandPath(destinationPath);
                        } else {
                            ajlConcurrent.setSelectedValue(node.getUserObject(), true);
                        }
                    }
		        }
			}
		} else {
			AJList tmplist = (AJList)e.getSource();
			if(e.getClickCount() == 2) {
                modifConcurrent((Concurrent)tmplist.getSelectedValue());
			} else if(e.getModifiers()==MouseEvent.BUTTON3_MASK) { //
				
				tmplist.setSelectedIndex(tmplist.locationToIndex(e.getPoint()));
				
				if(tmplist.getSelectedValue() instanceof Concurrent) {
					Concurrent tmpConcurrent = (Concurrent)tmplist.getSelectedValue();

					DefaultMutableTreeNode tnRoot = ((DefaultMutableTreeNode)treeTarget.getPathForRow(0).getLastPathComponent());
					
					for(int i = 0; i < treeModel.getChildCount(tnRoot); i++) {
						
						DefaultMutableTreeNode cibleTreeNode = (DefaultMutableTreeNode)treeModel.getChild(tnRoot, i);
						
						for(int j = 0; j < treeModel.getChildCount(cibleTreeNode); j++) {
							DefaultMutableTreeNode tn = (DefaultMutableTreeNode)treeModel.getChild(cibleTreeNode, j);
							
							if(tn.getUserObject() instanceof Concurrent && (Concurrent)tn.getUserObject() == tmpConcurrent) {
								
								TreePath childPath = new TreePath(tn.getPath());
								if(treeTarget.getSelectionPath() != null)
									treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
					      		treeTarget.setSelectionPath(childPath);

								break;
							}
						}
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {
			tmpconc = (Concurrent)ajlConcurrent.getSelectedValue();
		} 
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {
            //coordonnée de l'objet source
			Point coordSrc = ajlConcurrent.getLocationOnScreen();
            //coordonnée de l'objet destination
			Point coordDest = treeTarget.getLocationOnScreen();
		
            //test si le drop correspond bien à la bonne action
			if((coordSrc.x + e.getX() - coordDest.x) > 0) {
  
				placementManuelConcurrent(treeTarget.getPathForLocation(coordSrc.x+e.getX()-coordDest.x,
                        coordSrc.y+e.getY()-coordDest.y));
            }
		}
		//dans tous les cas remettre le curseur par defaut à la fin du drag
        ajlConcurrent.setEnabled(true);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {
			Cursor cursor = java.awt.dnd.DragSource.DefaultMoveDrop;
			this.setCursor(cursor);
            
            ajlConcurrent.setEnabled(false);
            //list.setSelectedValue(tmpconc, false);
		}	
	}
	public void mouseMoved(MouseEvent e) { }
	public void ExitDragNDrop(Point loc) {
		if(treeTarget.getSelectedNode().getUserObject() instanceof Concurrent) {
        		//recupere le concurrent à déplacer
        		tmpconc = (Concurrent)treeTarget.getSelectedNode().getUserObject();
        		
        		//recupere le chemin de la destination
            placementManuelConcurrent(treeTarget.getPathForLocation(loc.x, loc.y));
        } else if(treeTarget.getSelectedNode().getUserObject() instanceof Cible) {
            DefaultMutableTreeNode dmtn = treeTarget.getSelectedNode();
            
            Cible tmpcbl = (Cible)dmtn.getUserObject();
            
            for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
                if(tmpcbl.getConcurrentAt(i) != null) {
                    tmpconc = tmpcbl.getConcurrentAt(i);
                    
                    placementManuelConcurrent(treeTarget.getPathForLocation(loc.x, loc.y));
                }
            }
        }
	}
    
	public void valueChanged(ListSelectionEvent e) {
		jbSupprimerArcher.setEnabled(true);
        
        if(ajlConcurrent.getSelectedValue() instanceof Concurrent) {
            Concurrent tmpConcurrent = (Concurrent)ajlConcurrent.getSelectedValue();

            DefaultMutableTreeNode tnRoot = ((DefaultMutableTreeNode)treeTarget.getPathForRow(0).getLastPathComponent());
            
            for(int i = 0; i < treeModel.getChildCount(tnRoot); i++) {
                
                DefaultMutableTreeNode cibleTreeNode = (DefaultMutableTreeNode)treeModel.getChild(tnRoot, i);
                
                for(int j = 0; j < treeModel.getChildCount(cibleTreeNode); j++) {
                    DefaultMutableTreeNode tn = (DefaultMutableTreeNode)treeModel.getChild(cibleTreeNode, j);
                    
                    if(tn.getUserObject() instanceof Concurrent && (Concurrent)tn.getUserObject() == tmpConcurrent) {
                        
                        TreePath childPath = new TreePath(tn.getPath());
                        if(treeTarget.getSelectionPath() != null)
                            treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
                        treeTarget.setSelectionPath(childPath);

                        break;
                    }
                }
            }
        }
	}
    
    public void keyPressed(KeyEvent e) {
        if(e.getSource() instanceof JList) {
            if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                removeConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
            } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                modifConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
            }
        } else if(e.getSource() instanceof JTree) {
            if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                DefaultMutableTreeNode tntemp = treeTarget.getSelectedNode();
                if(tntemp.getUserObject() instanceof Concurrent) {
                    removeConcurrent((Concurrent)tntemp.getUserObject());
                }
            } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                DefaultMutableTreeNode tntemp = treeTarget.getSelectedNode();
                if(tntemp.getUserObject() instanceof Concurrent)
                    modifConcurrent((Concurrent)tntemp.getUserObject());
                if(tntemp.getUserObject() instanceof Cible) {
                    index=((Cible)tntemp.getUserObject()).getNumCible();
                    modifResultats();
                }
            }
        }
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
    
    public void hyperlinkUpdate(HyperlinkEvent e) { 
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
            if (e instanceof HTMLFrameHyperlinkEvent) { 
                ((HTMLDocument)jepClassIndiv.getDocument()).processHTMLFrameHyperlinkEvent( 
                    (HTMLFrameHyperlinkEvent)e); 
            } else {
                modifConcurrent(ficheConcours.archerlist.getConcurrentAt(Integer.parseInt(e.getURL().getRef().substring(1)), 
                        Integer.parseInt(e.getURL().getRef().substring(0,1))));

                jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
            } 
        } 
    }
}