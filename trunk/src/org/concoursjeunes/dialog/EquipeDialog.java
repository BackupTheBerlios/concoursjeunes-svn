package org.concoursjeunes.dialog;

import static org.concoursjeunes.ConcoursJeunes.ajrLibelle;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.CriteriaSetLibelle;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.Entite;
import org.concoursjeunes.Equipe;
import org.concoursjeunes.FicheConcours;

import ajinteractive.standard.java2.GhostGlassPane;

/**
 * Boite de dialogue de gestion des équipes
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
public class EquipeDialog extends JDialog implements ActionListener, ListSelectionListener, TreeSelectionListener, MouseListener, MouseMotionListener {

	private final FicheConcours ficheConcours;

	private final Hashtable<Criterion, JCheckBox> classmentCriteriaCB = new Hashtable<Criterion, JCheckBox>();
	private final JTree treeConcurrents = new JTree();
	private DefaultTreeModel treeModelConcurrents;
	private JTree treeEquipes;
	private DefaultTreeModel treeModel;
	private final DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("racine"); //$NON-NLS-1$

	private final JCheckBox cbEquipeClub = new JCheckBox();

	private JPopupMenu popup;

	private final JButton jbValider = new JButton();
	private final JButton jbAnnuler = new JButton();

	private boolean validation = false;

	private final Hashtable<CriteriaSet, Hashtable<String, Equipe>> hEquipes = new Hashtable<CriteriaSet, Hashtable<String, Equipe>>();

	private boolean onDrag = false;
	private Object dragObject = null;

	/**
	 * Generation de la boite de dialogue de gestion des équipes
	 * 
	 * @param parentFrame
	 *            la fenetre parente dont dépend la boite de dialogue
	 * @param ficheConcours -
	 *            La fiche concours associé
	 */
	public EquipeDialog(JFrame parentFrame, FicheConcours ficheConcours) {
		super(parentFrame, true);
		this.ficheConcours = ficheConcours;

		// initialisation de l'interface
		init();
		affectLibelle();
		completePanel();

		// affichage de la boite de dialogue
		getRootPane().setDefaultButton(jbValider);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * initialisation de l'interface
	 * 
	 */
	private void init() {
		JPanel jpEquipes = new JPanel();
		JPanel jpSelection = new JPanel();
		JPanel jpValidAnnul = new JPanel();

		JPanel jpCriteres = new JPanel();
		JPanel jpTri = new JPanel();
		JPanel jpFiltre = new JPanel();

		JSplitPane splitpane;

		for (Criterion criteria : ficheConcours.getParametre().getReglement().getListCriteria()) {
			classmentCriteriaCB.put(criteria, new JCheckBox(criteria.getLibelle()));
			classmentCriteriaCB.get(criteria).addActionListener(this);
			classmentCriteriaCB.get(criteria).setActionCommand("filter"); //$NON-NLS-1$
			jpCriteres.add(classmentCriteriaCB.get(criteria));
		}

		treeModel = new DefaultTreeModel(treeRoot);
		treeEquipes = new JTree(treeModel);

		cbEquipeClub.setSelected(true);

		cbEquipeClub.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		treeConcurrents.addTreeSelectionListener(this);
		treeConcurrents.addMouseListener(this);
		treeConcurrents.addMouseMotionListener(this);
		treeEquipes.setRootVisible(false);
		treeEquipes.addTreeSelectionListener(this);
		treeEquipes.addMouseListener(this);
		treeEquipes.addMouseMotionListener(this);

		jpEquipes.setLayout(new BorderLayout());
		jpEquipes.add(new JScrollPane(treeEquipes), BorderLayout.CENTER);

		jpFiltre.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpFiltre.add(cbEquipeClub);

		jpTri.setLayout(new BorderLayout());
		jpTri.add(jpCriteres, BorderLayout.NORTH);
		jpTri.add(jpFiltre, BorderLayout.CENTER);

		jpSelection.setLayout(new BorderLayout());
		jpSelection.add(jpTri, BorderLayout.NORTH);
		jpSelection.add(new JScrollPane(treeConcurrents), BorderLayout.CENTER);

		jpValidAnnul.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpValidAnnul.add(jbValider);
		jpValidAnnul.add(jbAnnuler);

		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jpEquipes, jpSelection);
		splitpane.setOneTouchExpandable(true);
		splitpane.setResizeWeight(0.75);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitpane, BorderLayout.CENTER);
		getContentPane().add(jpValidAnnul, BorderLayout.SOUTH);

		popup();

		setGlassPane(new GhostGlassPane());
	}

	private void affectLibelle() {
		cbEquipeClub.setText(ajrLibelle.getResourceString("equipe.contrainte.club")); //$NON-NLS-1$
		jbValider.setText(ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}

	private void completePanel() {
		DefaultMutableTreeNode[] dmtnCategorie;
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Table des concurrents"); //$NON-NLS-1$

		treeModelConcurrents = new DefaultTreeModel(rootNode);
		treeConcurrents.setModel(treeModelConcurrents);

		Hashtable<Criterion, Boolean> criteriaFilter = new Hashtable<Criterion, Boolean>();
		for (Map.Entry<Criterion, JCheckBox> cb : classmentCriteriaCB.entrySet()) {
			criteriaFilter.put(cb.getKey(), cb.getValue().isSelected());
		}
		// Donne la liste des codes SCNA filtré
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(ficheConcours.getParametre().getReglement(), criteriaFilter);

		dmtnCategorie = new DefaultMutableTreeNode[catList.length];

		rootNode.removeAllChildren();

		for (int i = 0; i < catList.length; i++) {
			// test si il existe des archers dans la catégorie
			Concurrent[] concurrents = ficheConcours.getConcurrentList().list(catList[i], -1);
			if (concurrents.length >= ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
				dmtnCategorie[i] = new DefaultMutableTreeNode(new CriteriaSetLibelle(catList[i]));

				if (cbEquipeClub.isSelected()) {
					Entite[] entites = ficheConcours.getConcurrentList().listCompagnie();
					for (Entite entite : entites) {
						Concurrent[] clubConcurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(entite, catList[i]), ConcurrentList.SORT_BY_NAME);
						if (clubConcurrents.length >= ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
							DefaultMutableTreeNode dmtnEntite = new DefaultMutableTreeNode(entite);
							for (Concurrent concurrent : clubConcurrents) {
								if (ficheConcours.getEquipes().containsConcurrent(concurrent) == null)
									dmtnEntite.add(new DefaultMutableTreeNode(concurrent));
							}
							dmtnCategorie[i].add(dmtnEntite);
						}
					}
					if (dmtnCategorie[i].getChildCount() > 0) {
						rootNode.add(dmtnCategorie[i]);
						treeModelConcurrents.reload(rootNode);
						treeConcurrents.expandPath(new TreePath(dmtnCategorie[i].getPath()));
					}
				} else {
					concurrents = ConcurrentList.sort(concurrents, ConcurrentList.SORT_BY_NAME);
					for (Concurrent concurrent : concurrents) {
						if (ficheConcours.getEquipes().containsConcurrent(concurrent) == null)
							dmtnCategorie[i].add(new DefaultMutableTreeNode(concurrent));
					}

					rootNode.add(dmtnCategorie[i]);
					treeModelConcurrents.reload(rootNode);
					treeConcurrents.expandPath(new TreePath(rootNode.getPath()));
				}
			}
		}

		// ficheConcours.getConcurrentList().l
		// ficheConcours.getConcurrentList().list(-1)
		// for()
		// parcours toutes les équipes enregistré
		/*
		 * for(Equipe equipe : ficheConcours.getEquipes().list()) { //si la catégorie de l'équipe n'existe pas alors la créer if(!hEquipes.containsKey(equipe.getDifferentiationCriteria())) {
		 * hEquipes.put(equipe.getDifferentiationCriteria(), new Hashtable<String, Equipe>()); } //injecter l'equipes dans sa catégorie de classement
		 * hEquipes.get(equipe.getDifferentiationCriteria()).put(equipe.getNomEquipe(), equipe); }
		 * 
		 * 
		 * categoriesEquipes();
		 */
	}

	/**
	 * Enumere les catégories d'équipes valide sur le concours en fonction des critères de tri.
	 * 
	 * @param depart
	 */
	private void categoriesEquipes(CriteriaSet criteriaSet, Entite club) {

		DefaultMutableTreeNode dmtnCategorie;

		// supprimer tous le noeud de l'arbre
		treeRoot.removeAllChildren();

		ArrayList<Equipe> equipes = new ArrayList<Equipe>();

		for (Equipe equipe : ficheConcours.getEquipes().list(criteriaSet)) {
			if (club == null || equipe.getMembresEquipe().get(0).getClub().equals(club)) {
				// injecter l'equipes dans sa catégorie de classement
				equipes.add(equipe);
			}
		}

		// generer le noeud correspondant
		dmtnCategorie = new DefaultMutableTreeNode(new CriteriaSetLibelle(criteriaSet));

		DefaultMutableTreeNode dmtnClub = null;
		if (club != null)
			dmtnClub = new DefaultMutableTreeNode(club.getVille());
		// liste les équipes pour la catégorie
		for (Equipe equipe : equipes) {
			DefaultMutableTreeNode dmtnEquipe = new DefaultMutableTreeNode(equipe);
			for (Concurrent concurrent : equipe.getMembresEquipe())
				dmtnEquipe.add(new DefaultMutableTreeNode(concurrent));
			if (dmtnClub != null) {
				dmtnClub.add(dmtnEquipe);
			} else
				dmtnCategorie.add(dmtnEquipe);
		}
		if (club != null) {
			dmtnCategorie.add(dmtnClub);
		}

		// integrer le noeud à l'arbre
		treeRoot.add(dmtnCategorie);

		// recharger l'arbre
		treeModel.reload(treeRoot);

		// selectionner le 1er élément
		// treeEquipes.setSelectionRow(0);
		expandPath(treeEquipes, new TreePath(dmtnCategorie.getPath()));
	}

	private void expandPath(JTree tree, TreePath treePath) {
		tree.expandPath(treePath);

		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			expandPath(tree, new TreePath(((DefaultMutableTreeNode) treeNode.getChildAt(i)).getPath()));
		}
	}

	public void popup() {
		popup = new JPopupMenu("Edit"); //$NON-NLS-1$

		JMenuItem mi1 = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.suppression")); //$NON-NLS-1$
		mi1.setActionCommand("popup.suppression"); //$NON-NLS-1$
		mi1.addActionListener(this);
		popup.add(mi1);

		treeEquipes.add(popup);
	}

	/**
	 * renvoie la liste des équipes
	 * 
	 * @return Equipe[] - la liste des équipes
	 */
	public Equipe[] getEquipes() {
		ArrayList<Equipe> al = new ArrayList<Equipe>();

		for (Hashtable<String, Equipe> htable : hEquipes.values()) {
			for (Equipe equipe : htable.values()) {
				if (equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
					htable.remove(equipe.getNomEquipe());
				}
			}
		}

		for (Hashtable<String, Equipe> htable : hEquipes.values()) {
			for (Equipe equipe : htable.values()) {
				al.add(equipe);
			}
		}
		return al.toArray(new Equipe[al.size()]);
	}

	/**
	 * indique si la boite à été valider ou non
	 * 
	 * @return boolean - l'etat de validation
	 */
	public boolean isValider() {
		return validation;
	}

	private void createEquipe(TreePath destPath) {
		if (destPath == null)
			return;

		DefaultMutableTreeNode equipesTreeNode;
		TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();

		// selectionne la destination
		treeEquipes.setSelectionPath(destPath);

		// recupere le noeud destination
		equipesTreeNode = (DefaultMutableTreeNode) destPath.getPathComponent(1);

		// detecte la categorie du point d'arborescence
		CriteriaSet scna = ((CriteriaSetLibelle) equipesTreeNode.getUserObject()).getCriteriaSet();

		// donner à l'équipe le nom du club du premier concourrent
		Concurrent firstConcurrent = (Concurrent) ((DefaultMutableTreeNode) selectedPaths[0].getLastPathComponent()).getUserObject();
		String strEquipeName = firstConcurrent.getClub().getNom();

		int i = 2;
		while (ficheConcours.getEquipes().contains(strEquipeName)) {
			strEquipeName += String.valueOf(i);
			i++;
		}

		do {
			strEquipeName = JOptionPane.showInputDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("equipe.saisinom"), strEquipeName); //$NON-NLS-1$
		} while (strEquipeName == null || strEquipeName.equals("") || ficheConcours.getEquipes().contains(strEquipeName)); //$NON-NLS-1$

		// on crée l'équipe
		Equipe equipe = new Equipe(strEquipeName);
		equipe.setNbRetenu(ficheConcours.getParametre().getReglement().getNbMembresRetenu());
		equipe.setDifferentiationCriteria(scna);

		ficheConcours.getEquipes().add(equipe);

		// on crée le point d'arborescence dans l'arbre
		DefaultMutableTreeNode equipeTreeNode = new DefaultMutableTreeNode(equipe);

		// on ajoute les membres à l'équipe
		addMembresToEquipe(equipe, equipeTreeNode);

		// on crée le point d'arborescence dans l'arbre
		if (cbEquipeClub.isSelected()) {
			((DefaultMutableTreeNode) destPath.getLastPathComponent()).add(equipeTreeNode);
		} else {
			equipesTreeNode.add(equipeTreeNode);
		}

		treeModel.reload();
		TreePath treePath = new TreePath(equipeTreeNode.getPath());
		treeEquipes.expandPath(treePath);
	}

	private void addMembresToEquipe(Equipe equipe, DefaultMutableTreeNode dmtn) {

		TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
		Concurrent[] selectionConc = new Concurrent[selectedPaths.length];
		for (int i = 0; i < selectedPaths.length; i++) {
			selectionConc[i] = (Concurrent) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject();
		}

		// test si le nombre de concurrent n'est pas trop important pour l'équipe
		if (equipe.getMembresEquipe().size() + selectionConc.length > ficheConcours.getParametre().getReglement().getNbMembresEquipe()) {
			JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("equipe.taille.max"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("equipe.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		} else {
			for (Object concurrent : selectionConc) {
				if (concurrent != null) {
					// ajoute le concurrent à l'equipe
					equipe.addConcurrent((Concurrent) concurrent);
					// ajoute le concurrent à l'arborescence.
					DefaultMutableTreeNode membreTreeNode = new DefaultMutableTreeNode(concurrent);
					dmtn.add(membreTreeNode);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void removeMembreForEquipe(Equipe equipe, Concurrent concurrent) {

	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			if (e.getActionCommand().equals("filter")) //$NON-NLS-1$
				completePanel(); // categoriesEquipes();

			else if (e.getSource() == cbEquipeClub) {
				completePanel();
				/*
				 * DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent(); if(dmtn != null) { Object dmtnObj = dmtn.getUserObject(); if(dmtnObj instanceof
				 * CriteriaSetLibelle) { listConcurrents(((CriteriaSetLibelle)dmtnObj).getCriteriaSet(), ConcurrentList.SORT_BY_CLUBS); } }
				 */
			}
		} else if (e.getActionCommand().equals("popup.suppression")) { //$NON-NLS-1$
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeEquipes.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof CriteriaSetLibelle) {

				} else if (dmtnObj instanceof Concurrent) {
					DefaultMutableTreeNode tnEquipe = (DefaultMutableTreeNode) dmtn.getParent();

					Equipe equipe = (Equipe) tnEquipe.getUserObject();

					removeMembreForEquipe(equipe, (Concurrent) dmtnObj);
				}
			}

		} else if (e.getSource() == jbAnnuler) {
			validation = false;
			setVisible(false);
		} else if (e.getSource() == jbValider) {
			validation = true;
			setVisible(false);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		/*
		 * if(e.getSource() == ajlConcurrents) { AJList tmplist = (AJList)e.getSource(); int[] selection = tmplist.getSelectedIndices();
		 * 
		 * if(cbEquipeClub.isSelected()) { Concurrent concRef = null;
		 * 
		 * for(int index : selection) { Concurrent concurrent = (Concurrent)tmplist.getValueAt(index); //prend le 1er élément en référence if(concRef == null) concRef = concurrent;
		 * 
		 * if(!concurrent.getClub().equals(concRef.getClub())) { tmplist.removeSelectionInterval(index, index); } } }
		 * 
		 * if(selection.length > ficheConcours.getParametre().getReglement().getNbMembresEquipe()) {
		 * tmplist.removeSelectionInterval(selection[ficheConcours.getParametre().getReglement().getNbMembresEquipe()], selection[selection.length-1]); } }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == treeConcurrents && treeConcurrents.getLastSelectedPathComponent() != null) {
			if (((DefaultMutableTreeNode) treeConcurrents.getLastSelectedPathComponent()).getUserObject() instanceof Concurrent)
				dragObject = treeConcurrents.getLastSelectedPathComponent();
		} else if (e.getSource() == treeEquipes && treeEquipes.getLastSelectedPathComponent() != null) {
			if (((DefaultMutableTreeNode) treeEquipes.getLastSelectedPathComponent()).getUserObject() instanceof Concurrent)
				dragObject = treeEquipes.getLastSelectedPathComponent();
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == treeConcurrents) {
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeConcurrents);
			SwingUtilities.convertPointFromScreen(p, treeEquipes);

			// test si le drop correspond bien à la bonne action
			if (p.x > 0 && p.y > 0 && p.x < treeEquipes.getWidth() && p.y < treeEquipes.getHeight()) {

				DefaultMutableTreeNode teamNode = (DefaultMutableTreeNode) treeEquipes.getPathForLocation(p.x, p.y).getLastPathComponent();
				Object destObj = teamNode.getUserObject();

				if (destObj instanceof Equipe) {

					Equipe equipe = (Equipe) destObj;

					addMembresToEquipe(equipe, teamNode);

					treeModel.reload(teamNode);

					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeConcurrents.getSelectionPath().getParentPath().getLastPathComponent();
					TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
					for (TreePath selectedPath : selectedPaths) {
						parentNode.remove((DefaultMutableTreeNode) selectedPath.getLastPathComponent());
					}
					treeModelConcurrents.reload(parentNode);
				} else {
					createEquipe(treeEquipes.getPathForLocation(p.x, p.y));

					TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
					for (TreePath selectedPath : selectedPaths) {
						((DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent()).remove((DefaultMutableTreeNode) selectedPath.getLastPathComponent());
					}

					treeModelConcurrents.reload((DefaultMutableTreeNode) treeConcurrents.getSelectionPath().getParentPath().getLastPathComponent());
				}
			}
		} else if (e.getSource() == treeEquipes) {
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeEquipes);
			SwingUtilities.convertPointFromScreen(p, treeConcurrents);
			if (p.x > 0 && p.y > 0 && p.x < treeConcurrents.getWidth() && p.y < treeConcurrents.getHeight()) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeEquipes.getSelectionPath().getLastPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeEquipes.getSelectionPath().getParentPath().getLastPathComponent();

				Equipe equipe = (Equipe) parentNode.getUserObject();
				equipe.removeConcurrent((Concurrent) selectedNode.getUserObject());

				if (equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
					ficheConcours.getEquipes().remove(equipe);

					DefaultMutableTreeNode refNode = (DefaultMutableTreeNode) parentNode.getParent();
					refNode.removeAllChildren();
					treeModel.reload(refNode);
				} else {
					parentNode.remove(selectedNode);
					treeModel.reload(parentNode);
				}

				completePanel();
			}
		}
		this.getGlassPane().setVisible(false);
		dragObject = null;
		onDrag = false;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == treeConcurrents && dragObject != null) {
			GhostGlassPane glassPane = (GhostGlassPane) this.getGlassPane();
			if (!onDrag) {
				Rectangle rect = treeConcurrents.getPathBounds(new TreePath(((DefaultMutableTreeNode) dragObject).getPath()));

				BufferedImage image = new BufferedImage(treeConcurrents.getWidth(), treeConcurrents.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();
				treeConcurrents.paint(g);
				image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);

				glassPane.setVisible(true);

				glassPane.setImage(image);

				// ajlConcurrent.set

				onDrag = true;
			}

			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeConcurrents);
			SwingUtilities.convertPointFromScreen(p, glassPane);

			glassPane.setPoint(p);
			glassPane.repaint();
		} else if (e.getSource() == treeEquipes && dragObject != null) {
			GhostGlassPane glassPane = (GhostGlassPane) this.getGlassPane();
			if (!onDrag) {
				Rectangle rect = treeEquipes.getPathBounds(new TreePath(((DefaultMutableTreeNode) dragObject).getPath()));

				BufferedImage image = new BufferedImage(treeEquipes.getWidth(), treeEquipes.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();
				treeEquipes.paint(g);
				image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);

				glassPane.setVisible(true);

				glassPane.setImage(image);

				// ajlConcurrent.set

				onDrag = true;
			}

			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeEquipes);
			SwingUtilities.convertPointFromScreen(p, glassPane);

			glassPane.setPoint(p);
			glassPane.repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		// TreePath treePath = e.getPath();
		if (e.getOldLeadSelectionPath() != e.getNewLeadSelectionPath()) {
			if (e.getSource() == treeEquipes) {
				/*
				 * DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent(); if(dmtn != null) { Object dmtnObj = dmtn.getUserObject(); if(dmtnObj instanceof
				 * CriteriaSetLibelle) { listConcurrents(((CriteriaSetLibelle)dmtnObj).getCriteriaSet(), ConcurrentList.SORT_BY_CLUBS); } }
				 */
			} else if (e.getSource() == treeConcurrents) {
				TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
				if (selectedPaths != null) {
					if (selectedPaths.length > ficheConcours.getParametre().getReglement().getNbMembresEquipe()) {
						for (int i = ficheConcours.getParametre().getReglement().getNbMembresEquipe(); i < selectedPaths.length; i++) {
							treeConcurrents.removeSelectionPath(selectedPaths[i]);
						}
					}
				}

				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeConcurrents.getLastSelectedPathComponent();
				if (dmtn != null) {
					Object dmtnObj = dmtn.getUserObject();
					if (dmtnObj instanceof Concurrent) {
						DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) dmtn.getParent();
						if (dmtnParent.getUserObject() instanceof Entite) {
							Entite club = (Entite) dmtnParent.getUserObject();

							DefaultMutableTreeNode dmtnGrandParent = (DefaultMutableTreeNode) dmtnParent.getParent();
							CriteriaSetLibelle critere = (CriteriaSetLibelle) dmtnGrandParent.getUserObject();

							categoriesEquipes(critere.getCriteriaSet(), club);
						}
					} else if (dmtnObj instanceof Entite) {
						Entite club = (Entite) dmtnObj;

						DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) dmtn.getParent();
						CriteriaSetLibelle critere = (CriteriaSetLibelle) dmtnParent.getUserObject();

						categoriesEquipes(critere.getCriteriaSet(), club);
					}
				}

			}
		}
	}
}