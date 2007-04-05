/**
 * 
 */
package org.concoursjeunes.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.Cible;
import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Equipe;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.FicheConcoursEvent;
import org.concoursjeunes.FicheConcoursListener;
import org.concoursjeunes.dialog.ConcurrentDialog;
import org.concoursjeunes.dialog.EquipeDialog;
import org.concoursjeunes.dialog.TypeListingDialog;

import ajinteractive.standard.java2.AJList;
import ajinteractive.standard.java2.GhostGlassPane;

/**
 * Panneau de la fiche concours contenant l'ensemble des éléments spécifique à un départ donné
 * @author  Aurélien JEOFFRAY
 * @version  1.0
 */
public class FicheConcoursDepartPane extends JPanel implements ActionListener, MouseListener, MouseMotionListener,
		ListSelectionListener, KeyListener, FicheConcoursListener {

	private JButton jbAjouterArcher    = new JButton();
	private JButton jbSupprimerArcher  = new JButton();
	private JButton jbEquipe           = new JButton();
	private JButton jbPlacementArcher  = new JButton();
	private ButtonGroup jbgSort;
	private JRadioButton jcbSortCible  = new JRadioButton("", true); //$NON-NLS-1$
	private JRadioButton jcbSortNom    = new JRadioButton();
	private JRadioButton jcbSortClub   = new JRadioButton();

	private JButton jbPrintListConc    = new JButton();
	private JButton jbPrintEtiquettes  = new JButton();
	private JButton jbPrintPasDeTir    = new JButton();

	private AJList ajlConcurrent		= new AJList();
	//private DnDJTree treeTarget        = new DnDJTree();
	private JTree treeTarget			= new JTree();
	//private DefaultTreeModel treeModel	= new DefaultTreeModel(new DefaultMutableTreeNode());
	private TargetTreeModel treeModel	= new TargetTreeModel();
	private JPopupMenu popup;

	private FicheConcoursPane ficheConcoursPane = null;
	//private Concurrent tmpconc			= null;
	private Object dragObject			= null;
	private int depart					= 0;

	private boolean onDrag				= false;

	/**
	 * Construction du panneau
	 * 
	 * @param ficheConcoursPane - le panneau parent de la fiche concours
	 * @param depart - le numero du départ représenté par le panneau courrant
	 */
	public FicheConcoursDepartPane(FicheConcoursPane ficheConcoursPane, int depart) {
		this.ficheConcoursPane = ficheConcoursPane;
		this.depart = depart;
		
		//s'enregistre comme auditeur des changement de la fiche concours
		ficheConcoursPane.ficheConcours.addFicheConcoursListener(this);

		init();
		affectLibelle();
		initContent();
	}

	/**
	 * Initialisation des composants graphique
	 */
	private void init() {
		JPanel northpaneGestion = new JPanel();
		JPanel northpanePrintButton = new JPanel();
		JPanel ficheG = new JPanel();

		//enregistre les auditeurs d'évenement
		jbPrintListConc.addActionListener(this);
		jbPrintEtiquettes.addActionListener(this);
		jbPrintPasDeTir.addActionListener(this);

		//option d'affichage du classement
		northpaneGestion.setLayout(new BorderLayout());
		northpanePrintButton.add(jbPrintListConc);
		northpanePrintButton.add(jbPrintEtiquettes);
		northpanePrintButton.add(jbPrintPasDeTir);
		northpaneGestion.add(northpanePrintButton, BorderLayout.NORTH);
		JLabel jl = new JLabel("Faites glisser un concurrent sur une cible ou une position pour l'affecter à celle ci");
		jl.setBackground(Color.ORANGE);
		jl.setOpaque(true);
		northpaneGestion.add(jl, BorderLayout.CENTER);

		ficheG.setLayout(new BorderLayout());
		ficheG.add(northpaneGestion,BorderLayout.NORTH);
		ficheG.add(listeParCible(),BorderLayout.CENTER);

		//panneau des archers
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, listeParNom(), ficheG);
		splitpane.setOneTouchExpandable(true);
		splitpane.setResizeWeight(0.25); 

		setLayout(new BorderLayout());
		add(splitpane, BorderLayout.CENTER);

		popup();
	}

	/**
	 * Initialisation  du panneau "liste des concurrents"
	 * 
	 * @return le panneau contenant la liste des concurrents
	 */
	private JPanel listeParNom() {
		//liste des tireurs par nom
		JPanel pane = new JPanel();

		//creation des tables de tireurs
		JScrollPane scrollarcher   = new JScrollPane();
		scrollarcher.setViewportView(ajlConcurrent);

		jbAjouterArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteria")
		));
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
						ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
						ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")), //$NON-NLS-1$
						new ImageIcon(
								ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
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

		//creation de l'arbre de cible
		JScrollPane scrollcible    = new JScrollPane();
		scrollcible.setViewportView(treeTarget);

		pane.setLayout(new BorderLayout());
		pane.add(scrollcible,BorderLayout.CENTER);

		treeTarget.setModel(treeModel);
		treeTarget.addMouseListener(this);
		treeTarget.addMouseMotionListener(this);
		treeTarget.addKeyListener(this);
		//treeTarget.addOut(this);
		treeTarget.setCellRenderer(new CibleRenderer(
				new ImageIcon(
						ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
						ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")), //$NON-NLS-1$
						new ImageIcon(
								ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
								ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.target")))); //$NON-NLS-1$
		treeTarget.setToggleClickCount(3);
		treeTarget.setShowsRootHandles(false);

		return pane;
	}

	/**
	 * menu clic droit de gestion des concurrents
	 */
	private void popup() {
		popup = new JPopupMenu("Edit"); //$NON-NLS-1$

		JMenuItem mi3 = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.edition")); //$NON-NLS-1$
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
	 * Affecte les libellés localisé au composant de l'interface
	 */
	private void affectLibelle() {
		jbPrintListConc.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printlistconc")); //$NON-NLS-1$
		jbPrintEtiquettes.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printetiquettes")); //$NON-NLS-1$
		jbPrintPasDeTir.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printpasdetir")); //$NON-NLS-1$

		//jbAjouterArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
		jbAjouterArcher.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
		jbSupprimerArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer")); //$NON-NLS-1$
		jbEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.equipe")); //$NON-NLS-1$
		jbPlacementArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.placementarcher")); //$NON-NLS-1$
		jcbSortCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.cible")); //$NON-NLS-1$
		jcbSortNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.nom")); //$NON-NLS-1$
		jcbSortClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.club")); //$NON-NLS-1$

		treeModel.setRootLabel(ConcoursJeunes.ajrLibelle.getResourceString("treenode.racine"));
	}

	/**
	 * affecte le contenu à la fiche
	 */
	private void initContent() {
		createListeParNom();
		createListeParCible();
	}

	/**
	 * genere la liste des archer trié par nom
	 *
	 */
	private void createListeParNom() {
		ajlConcurrent.setListData(
				ConcurrentList.sort(ficheConcoursPane.ficheConcours.getConcurrentList().list(depart), 
						ConcurrentList.SORT_BY_CIBLES));
	}

	/**
	 * Arborescence du pas de tir
	 * 
	 */
	private void createListeParCible() {
		treeModel.setTargetChilds(ficheConcoursPane.ficheConcours.getPasDeTir(depart).getTargets());
	}
	
	/**
	 * Affiche la boite de dialogue d'insertion de concurrent
	 *
	 */
	private void showAddConcurrentDialog() {
		int codeRetour = 0;
		do {
			codeRetour = ficheConcoursPane.concDialog.showNewConcurrentDialog();
			if(codeRetour != ConcurrentDialog.CANCEL 
					&& !ficheConcoursPane.concDialog.getConcurrent().getNomArcher().equals("")) { //$NON-NLS-1$
				
				ficheConcoursPane.ficheConcours.addConcurrent(ficheConcoursPane.concDialog.getConcurrent(), depart);
			}
		} while(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT);
	}

	/**
	 * Suppression d'un archer de la liste des concurrents
	 */
	private void removeSelectedConcurrent() {
		
		Concurrent removedConcurrent = (Concurrent)ajlConcurrent.getSelectedValue();

		if(JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression"), //$NON-NLS-1$
				ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) //$NON-NLS-1$
			return;

		ficheConcoursPane.ficheConcours.removeConcurrent(removedConcurrent);
	}

	/**
	 * routine de placement automatique des concurrents sur le pas de tir
	 * en fonction des critéres SCNA
	 *
	 */
	private void placementConcurrents() {

		if(JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.placement"), //$NON-NLS-1$
				ConcoursJeunes.ajrLibelle.getResourceString("confirmation.placement.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) //$NON-NLS-1$
			return;

		ficheConcoursPane.ficheConcours.getPasDeTir(depart).placementConcurrents();
		
		if(jcbSortCible.isSelected()) {
			ajlConcurrent.setListData(
					ConcurrentList.sort(ficheConcoursPane.ficheConcours.getConcurrentList().list(depart), 
							ConcurrentList.SORT_BY_CIBLES));
		}
		treeTarget.repaint();
	}

	/**
	 * placement manuel (en drag n'drop) d'un concurrent sur le pas de tir
	 * 
	 * @param destPath - le chemin du noeud de destination
	 */
	private void placementManuelConcurrent(Concurrent concurrent, TreePath destPath) {
		if(destPath == null || concurrent == null)
			return;
		
		Cible cible;
		int position = -1;
		
		//selectionne la destination
		treeTarget.setSelectionPath(destPath);

		if(destPath.getLastPathComponent() instanceof Cible) {
			cible = (Cible)destPath.getLastPathComponent();
		} else if(destPath.getLastPathComponent() instanceof String) {
			//recupere le noeud destination
			cible = (Cible)destPath.getParentPath().getLastPathComponent();
			String strPos = (String)destPath.getLastPathComponent();
			position = strPos.charAt(strPos.length() - 1) - 'A';
		} else {
			return;
		}
		
		if(cible == null || !ficheConcoursPane.ficheConcours.getPasDeTir(depart).placementConcurrent(concurrent, cible, position)) {
			JOptionPane.showMessageDialog(new JDialog(),
					ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}

	/**
	 * retire un concurrent du pas de tir
	 * 
	 * @param concurrent - le concurrent à retirer
	 */
	private void retraitPlacementConcurrent(Concurrent concurrent) {
		//FIXME à réécrire
		//recherche sa position dans l'arbre
		/*TreePath childPath = getTreePathForConcurrent(concurrent);
		DefaultMutableTreeNode concurrentTreeNode = (DefaultMutableTreeNode)childPath.getLastPathComponent();
		Cible curCible = (Cible)((DefaultMutableTreeNode)concurrentTreeNode.getParent()).getUserObject();
		//retire le concurrent de la cible
		curCible.removeConcurrent(concurrent);*/
	}

	/**
	 * Retourne le chemin dans l'arborescence de l'arbre du concurrent donné
	 * 
	 * @param concurrent - le concurrent à rechercher
	 * @return la position dans l'arbre
	 */
	private TreePath getTreePathForConcurrent(Concurrent concurrent) {
		if(concurrent == null)
			return null;
		
		return treeModel.getTreePathForNode(concurrent);
	}

	/**
	 * selectionne le concurrent donnée dans l'arbre
	 * 
	 * @param concurrent - le concurrent à selectionner
	 */
	private void selectConcurrentInTree(Concurrent concurrent) {
		TreePath childPath = getTreePathForConcurrent(concurrent);
		if(childPath != null) {
			if(treeTarget.getSelectionPath() != null)
				treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
			treeTarget.setSelectionPath(childPath);
		}
	}

	/**
	 * Invoqué lorsque la liste des concurents change (ajout ou suppression d'un concurrent)
	 * 
	 * @see org.concoursjeunes.FicheConcoursListener#listConcurrentChanged(org.concoursjeunes.FicheConcoursEvent)
	 */
	public void listConcurrentChanged(FicheConcoursEvent e) {
		switch(e.getEvent()) {
			case FicheConcoursEvent.ADD_CONCURRENT:
				ajlConcurrent.add(e.getConcurrent());
				break;
			case FicheConcoursEvent.REMOVE_CONCURRENT:
				ajlConcurrent.remove(e.getConcurrent());
				break;
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String cmd = e.getActionCommand();

		if(source == jbAjouterArcher) {
			showAddConcurrentDialog();
		} else if(source == jbSupprimerArcher) {
			//Supprime un concurrent à la liste des inscrits
			removeSelectedConcurrent();
		} else if(source == jbEquipe) {
			//Creer les equipes
			EquipeDialog ed = new EquipeDialog(ficheConcoursPane.getParentframe(), ficheConcoursPane.ficheConcours);
			if(ed.isValider()) {
				ficheConcoursPane.ficheConcours.getEquipes().removeAll();
				for(Equipe equipe : ed.getEquipes())
					ficheConcoursPane.ficheConcours.getEquipes().add(equipe);
			}
		} else if(source == jbPlacementArcher) {
			placementConcurrents();
		} else if(source == jbPrintListConc) {
			TypeListingDialog tld = new TypeListingDialog(ficheConcoursPane.getParentframe());
			if(tld.returnType == TypeListingDialog.ALPHA)
				ficheConcoursPane.ficheConcours.printArcherList(FicheConcours.ALPHA);
			else if(tld.returnType == TypeListingDialog.GREFFE)
				ficheConcoursPane.ficheConcours.printArcherList(FicheConcours.GREFFE);
		} else if(source == jbPrintEtiquettes) {
			ficheConcoursPane.ficheConcours.printEtiquettes();
		} else if(source == jbPrintPasDeTir) {
			ficheConcoursPane.ficheConcours.printPasDeTir();
		} else if(source instanceof JRadioButton) {
			if(source == jcbSortCible)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcoursPane.ficheConcours.getConcurrentList().list(depart), 
						ConcurrentList.SORT_BY_CIBLES));
			else if(source == jcbSortNom)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcoursPane.ficheConcours.getConcurrentList().list(depart), 
						ConcurrentList.SORT_BY_NAME));
			else if(source == jcbSortClub)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcoursPane.ficheConcours.getConcurrentList().list(depart), 
						ConcurrentList.SORT_BY_CLUBS));
			ajlConcurrent.repaint();
		} else if(cmd.equals("popup.edition")) { //$NON-NLS-1$
			ficheConcoursPane.openConcurrentDialog((Concurrent)ajlConcurrent.getSelectedValue());
		} else if(cmd.equals("popup.suppression")) { //$NON-NLS-1$
			//Supprime un concurrent à la liste des inscrits
			removeSelectedConcurrent();
		} else if(cmd.equals("popup.retrait")) { //$NON-NLS-1$
			//donne le concurrent à retirer
			retraitPlacementConcurrent((Concurrent)ajlConcurrent.getSelectedValue());
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == treeTarget) {
			if(e.getModifiers() == InputEvent.BUTTON3_MASK) {
				Point coord = e.getPoint();

				TreePath destinationPath = treeTarget.getPathForLocation(coord.x, coord.y);

				treeTarget.setSelectionPath(destinationPath);

				if(destinationPath.getLastPathComponent() instanceof Concurrent) {
					ajlConcurrent.setSelectedValue(destinationPath.getLastPathComponent(), true);
					
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			} else {
				int selRow = treeTarget.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						Object selectedTreeNode = treeTarget.getSelectionPath().getLastPathComponent();
	
						if(selectedTreeNode instanceof Concurrent) {
							ficheConcoursPane.openConcurrentDialog((Concurrent)selectedTreeNode);
						} else if(selectedTreeNode instanceof Cible) {
							ficheConcoursPane.index = ((Cible)selectedTreeNode).getNumCible();
							ficheConcoursPane.openResultatDialog();
						} else {
							ficheConcoursPane.index = 1;
							ficheConcoursPane.openResultatDialog();
						}
					} else {
						Point coord = e.getPoint();
						TreePath destinationPath = treeTarget.getPathForLocation(coord.x, coord.y);
	
						//recupere le noeud destination et son parent
						Object node = destinationPath.getLastPathComponent();
						if(node instanceof Concurrent) {
							ajlConcurrent.setSelectedValue(node, true);
						}
					}
				}
			}
		} else {
			if(e.getClickCount() == 2) {
				ficheConcoursPane.openConcurrentDialog((Concurrent)ajlConcurrent.getSelectedValue());
			} else if(e.getModifiers()==InputEvent.BUTTON3_MASK) { //
				ajlConcurrent.setSelectedIndex(ajlConcurrent.locationToIndex(e.getPoint()));

				Concurrent tmpConcurrent = (Concurrent)ajlConcurrent.getSelectedValue();
				
				if(tmpConcurrent.getCible() > 0) {
					if(treeTarget.getSelectionPath() != null)
						treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
					treeTarget.setSelectionPath(getTreePathForConcurrent(tmpConcurrent));
				}

				popup.show(e.getComponent(), e.getX(), e.getY());
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

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {
			dragObject = ajlConcurrent.getSelectedValue();
		} else if(e.getSource() == treeTarget) {
			Point p = e.getPoint();
			TreePath tp = treeTarget.getPathForLocation(p.x, p.y);
			if(tp != null) {
				dragObject = tp.getLastPathComponent();
			}
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {		
			Point p = (Point)e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, ajlConcurrent);
            SwingUtilities.convertPointFromScreen(p, treeTarget);

			//test si le drop correspond bien à la bonne action
			if(p.x > 0 & p.y > 0) {
				placementManuelConcurrent((Concurrent)dragObject, treeTarget.getPathForLocation(p.x, p.y));
			}
		} else if(e.getSource() == treeTarget) {
			Point p = (Point)e.getPoint().clone();
			TreePath tp = treeTarget.getPathForLocation(p.x, p.y);
			if(tp != null) {
				if(tp.getLastPathComponent() instanceof String && tp.getLastPathComponent() != treeModel.getRoot()) {
					if(dragObject instanceof Concurrent) {
						placementManuelConcurrent((Concurrent)dragObject, tp);
					}
				} else if(tp.getLastPathComponent() instanceof Cible) {
					if(dragObject instanceof Cible) {
						Cible tmpcbl = (Cible)dragObject;
	
						for(int i = 0; i < ficheConcoursPane.ficheConcours.getParametre().getNbTireur(); i++) {
							if(tmpcbl.getConcurrentAt(i) != null) {
								Concurrent concurrent = tmpcbl.getConcurrentAt(i);
	
								placementManuelConcurrent(concurrent, tp);
							}
						}
					} else if(dragObject instanceof Concurrent) {
						placementManuelConcurrent((Concurrent)dragObject, tp);
					}
				}
			}
		}
		//dans tous les cas remettre le curseur par defaut à la fin du drag
		ajlConcurrent.setSelectable(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		ficheConcoursPane.getParentframe().getGlassPane().setVisible(false);
		dragObject = null;
		onDrag = false;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if(e.getSource() == ajlConcurrent) {
			GhostGlassPane glassPane = (GhostGlassPane)ficheConcoursPane.getParentframe().getGlassPane();
			if(!onDrag) {
				ajlConcurrent.setSelectable(false);
				Rectangle rect = ajlConcurrent.getCellBounds(
						ajlConcurrent.getSelectedIndex(), ajlConcurrent.getSelectedIndex());
				
				BufferedImage image = new BufferedImage(ajlConcurrent.getWidth(), ajlConcurrent.getHeight(), BufferedImage.TYPE_INT_ARGB);
	            Graphics g = image.getGraphics();
	            ajlConcurrent.paint(g);
	            image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
	            
	            glassPane.setVisible(true);
	            
	            glassPane.setImage(image);
	            
	            //ajlConcurrent.set
	            
	            onDrag = true;
			}

            Point p = (Point) e.getPoint().clone();
            SwingUtilities.convertPointToScreen(p, ajlConcurrent);
            SwingUtilities.convertPointFromScreen(p, glassPane);
            
            glassPane.setPoint(p);
            glassPane.repaint();
		} else if(e.getSource() == treeTarget) {
			GhostGlassPane glassPane = (GhostGlassPane)ficheConcoursPane.getParentframe().getGlassPane();
			if(!onDrag) {
				if(dragObject instanceof Concurrent || dragObject instanceof Cible) {
					//Rectangle rect = treeTarget.getPathBounds(getTreePathForConcurrent(tmpconc));
					//dragObject = tp.getLastPathComponent();
					
					Rectangle rect = treeTarget.getPathBounds(treeModel.getTreePathForNode(dragObject));
					
					BufferedImage image = new BufferedImage(treeTarget.getWidth(), treeTarget.getHeight(), BufferedImage.TYPE_INT_ARGB);
		            Graphics g = image.getGraphics();
		            treeTarget.paint(g);
		            image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
		            
		            glassPane.setVisible(true);
		            
		            glassPane.setImage(image);
		            
		            //ajlConcurrent.set
		            
		            onDrag = true;
				}
			}
			
			if(onDrag) {
				Point p = (Point) e.getPoint().clone();
	            SwingUtilities.convertPointToScreen(p, treeTarget);
	            SwingUtilities.convertPointFromScreen(p, glassPane);
	            
	            glassPane.setPoint(p);
	            glassPane.repaint();
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		jbSupprimerArcher.setEnabled(true);

		if(ajlConcurrent.getSelectedValue() instanceof Concurrent) {
			Concurrent tmpConcurrent = (Concurrent)ajlConcurrent.getSelectedValue();

			selectConcurrentInTree(tmpConcurrent);
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getSource() instanceof JList) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE) {
				removeSelectedConcurrent();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				ficheConcoursPane.openConcurrentDialog((Concurrent)ajlConcurrent.getSelectedValue());
			}
		} else if(e.getSource() instanceof JTree) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE) {
				Object node = treeTarget.getSelectionPath().getLastPathComponent();
				if(node instanceof Concurrent) {
					removeSelectedConcurrent();
				}
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				Object node = treeTarget.getSelectionPath().getLastPathComponent();
				if(node instanceof Concurrent)
					ficheConcoursPane.openConcurrentDialog((Concurrent)node);
				if(node instanceof Cible) {
					ficheConcoursPane.index=((Cible)node).getNumCible();
					ficheConcoursPane.openResultatDialog();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * @see ajinteractive.standard.java2.DragOutListener#ExitDragNDrop(java.awt.Point)
	 */
	/*public void ExitDragNDrop(Point loc) {
		if(treeTarget.getSelectedNode().getUserObject() instanceof Concurrent) {
			//recupere le concurrent à déplacer
			tmpconc = (Concurrent)treeTarget.getSelectedNode().getUserObject();

			//recupere le chemin de la destination
			placementManuelConcurrent(treeTarget.getPathForLocation(loc.x, loc.y));
		} else if(treeTarget.getSelectedNode().getUserObject() instanceof Cible) {
			DefaultMutableTreeNode dmtn = treeTarget.getSelectedNode();

			Cible tmpcbl = (Cible)dmtn.getUserObject();

			for(int i = 0; i < ficheConcoursPane.ficheConcours.getParametre().getNbTireur(); i++) {
				if(tmpcbl.getConcurrentAt(i) != null) {
					tmpconc = tmpcbl.getConcurrentAt(i);

					placementManuelConcurrent(treeTarget.getPathForLocation(loc.x, loc.y));
				}
			}
		}
	}*/
}