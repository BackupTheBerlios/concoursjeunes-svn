/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizableString;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.AJTree;
import org.ajdeveloppement.commons.ui.GhostGlassPane;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.Entite;
import org.concoursjeunes.Equipe;
import org.concoursjeunes.EquipeList;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.localisable.CriteriaSetLibelle;

/**
 * Boite de dialogue de gestion des équipes
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 */
public class EquipeDialog extends JDialog implements ActionListener, TreeSelectionListener, MouseListener, MouseMotionListener {

	private AjResourcesReader localisation;
	private FicheConcours ficheConcours;

	private final Hashtable<Criterion, JCheckBox> classmentCriteriaCB = new Hashtable<Criterion, JCheckBox>();
	private final JTree treeConcurrents = new JTree();
	private DefaultTreeModel treeModelConcurrents;
	private AJTree treeEquipes;
	private DefaultTreeModel treeModel;
	private final DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("racine"); //$NON-NLS-1$

	@Localizable("equipe.contrainte.club")
	private final JCheckBox cbEquipeClub = new JCheckBox();
	
	@Localizable("equipe.concurrenttable")
	private final LocalizableString lsConcurrentTable = new LocalizableString();

	@Localizable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localizable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();

	private boolean validation = false;

	private EquipeList tempEquipes = new EquipeList();

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
	public EquipeDialog(JFrame parentFrame, FicheConcours ficheConcours, AjResourcesReader localisation) {
		super(parentFrame, true);
		this.localisation = localisation;
		this.ficheConcours = ficheConcours;
		tempEquipes = ficheConcours.getEquipes().clone();

		// initialisation de l'interface
		init();
		affectLibelle();
		completePanel();
		completePanelEquipes();

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
		treeEquipes = new AJTree(treeModel);

		cbEquipeClub.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		treeConcurrents.addTreeSelectionListener(this);
		treeConcurrents.addMouseListener(this);
		treeConcurrents.addMouseMotionListener(this);
		treeEquipes.setKeepExpansionState(true);
		treeEquipes.setRootVisible(false);
		treeEquipes.setShowsRootHandles(true);
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

		//popup();

		setGlassPane(new GhostGlassPane(0.5f));
	}

	private void affectLibelle() {
		Localizator.localize(this, localisation);
	}

	private void completePanel() {
		DefaultMutableTreeNode[] dmtnCategorie;
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(lsConcurrentTable); 

		treeModelConcurrents = new DefaultTreeModel(rootNode);
		treeConcurrents.setModel(treeModelConcurrents);
		
		cbEquipeClub.setSelected(tempEquipes.isLimitedByClub());
		cbEquipeClub.setEnabled(tempEquipes.countEquipes() == 0);

		Hashtable<Criterion, Boolean> criteriaFilter = new Hashtable<Criterion, Boolean>();
		for (Map.Entry<Criterion, JCheckBox> cb : classmentCriteriaCB.entrySet()) {
			cb.getValue().setSelected(cb.getKey().isClassementEquipe());
			cb.getValue().setEnabled(tempEquipes.countEquipes() == 0);
			criteriaFilter.put(cb.getKey(), cb.getKey().isClassementEquipe());
		}
		// Donne la liste des codes SCNA filtré
		CriteriaSet[] catList = CriteriaSet.listCriteriaSet(ficheConcours.getParametre().getReglement(), criteriaFilter);

		dmtnCategorie = new DefaultMutableTreeNode[catList.length];

		rootNode.removeAllChildren();

		for (int i = 0; i < catList.length; i++) {
			// test si il existe des archers dans la catégorie
			List<Concurrent> concurrents = ficheConcours.getConcurrentList().list(catList[i], -1, criteriaFilter);
			if (concurrents.size() >= ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
				dmtnCategorie[i] = new DefaultMutableTreeNode(new CriteriaSetLibelle(catList[i], localisation));

				if (cbEquipeClub.isSelected()) {
					Entite[] entites = ficheConcours.getConcurrentList().listCompagnie();
					for (Entite entite : entites) {
						List<Concurrent> clubConcurrents = ConcurrentList.sort(ficheConcours.getConcurrentList().list(entite, catList[i], criteriaFilter), ConcurrentList.SortCriteria.SORT_BY_NAME);
						if (clubConcurrents.size() >= ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
							DefaultMutableTreeNode dmtnEntite = new DefaultMutableTreeNode(entite);
							for (Concurrent concurrent : clubConcurrents) {
								if (tempEquipes.containsConcurrent(concurrent) == null)
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
					concurrents = ConcurrentList.sort(concurrents, ConcurrentList.SortCriteria.SORT_BY_NAME);
					for (Concurrent concurrent : concurrents) {
						if (tempEquipes.containsConcurrent(concurrent) == null)
							dmtnCategorie[i].add(new DefaultMutableTreeNode(concurrent));
					}

					rootNode.add(dmtnCategorie[i]);
					treeModelConcurrents.reload(rootNode);
					treeConcurrents.expandPath(new TreePath(rootNode.getPath()));
				}
			}
		}
	}
	
	private void completePanelEquipes() {
		DefaultMutableTreeNode dmtnCategorie = null;

		// supprimer tous le noeud de l'arbre
		treeRoot.removeAllChildren();

		Hashtable<CriteriaSet, ArrayList<Equipe>> equipes = new Hashtable<CriteriaSet, ArrayList<Equipe>>();

		for (Equipe equipe : tempEquipes.getEquipeList()) {
			if(equipes.get(equipe.getDifferentiationCriteria()) == null) {
				equipes.put(equipe.getDifferentiationCriteria(), new ArrayList<Equipe>());
			}
			
			equipes.get(equipe.getDifferentiationCriteria()).add(equipe);
		}

		List<CriteriaSet> activeCriteriaSet = new ArrayList<CriteriaSet>();
		for(CriteriaSet criteriaSet : equipes.keySet()) {
			activeCriteriaSet.add(criteriaSet);
		}
		CriteriaSet.sortCriteriaSet(activeCriteriaSet, ficheConcours.getParametre().getReglement().getListCriteria());
		
		for(CriteriaSet criteriaSet : activeCriteriaSet) {
			// generer le noeud correspondant
			dmtnCategorie = new DefaultMutableTreeNode(new CriteriaSetLibelle(criteriaSet, localisation));
	
			// liste les équipes pour la catégorie
			for (Equipe equipe : equipes.get(criteriaSet)) {
				DefaultMutableTreeNode dmtnEquipe = new DefaultMutableTreeNode(equipe);
				for (Concurrent concurrent : equipe.getMembresEquipe())
					dmtnEquipe.add(new DefaultMutableTreeNode(concurrent));

				dmtnCategorie.add(dmtnEquipe);
			}
	
			// integrer le noeud à l'arbre
			treeRoot.add(dmtnCategorie);
		}

		// recharger l'arbre
		treeModel.reload();

		// selectionner le 1er élément
		//expandPath(treeEquipes, new TreePath(treeRoot.getPath()), (cbEquipeClub.isSelected())?2:1);
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

		List<Equipe> equipes = new ArrayList<Equipe>();

		for (Equipe equipe : tempEquipes.getEquipeList(criteriaSet)) {
			if (club == null || equipe.getMembresEquipe().get(0).getEntite().equals(club)) {
				// injecter l'equipes dans sa catégorie de classement
				equipes.add(equipe);
			}
		}

		// generer le noeud correspondant
		dmtnCategorie = new DefaultMutableTreeNode(new CriteriaSetLibelle(criteriaSet, localisation));

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
		treeModel.reload();
		
		if (dmtnClub != null && !dmtnClub.isLeaf())
			treeEquipes.expandPath(new TreePath(dmtnClub.getPath()));
		else
			treeEquipes.expandPath(new TreePath(dmtnCategorie.getPath()));
	}

	/**
	 * renvoie la liste des équipes
	 * 
	 * @return Equipe[] - la liste des équipes
	 */
	public EquipeList getEquipes() {
		tempEquipes.removeInvalidTeam();
		
		return tempEquipes;
	}

	/**
	 * indique si la boite à été valider ou non
	 * 
	 * @return boolean - l'etat de validation
	 */
	public boolean isValider() {
		return validation;
	}

	private boolean createEquipe(TreePath destPath) {
		if (destPath == null)
			return false;
		
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
		String strEquipeName = firstConcurrent.getEntite().getNom();

		int i = 2;
		while (tempEquipes.contains(strEquipeName)) {
			strEquipeName += String.valueOf(i);
			i++;
		}

		do {
			strEquipeName = JOptionPane.showInputDialog(this, localisation.getResourceString("equipe.saisinom"), strEquipeName); //$NON-NLS-1$
			if(strEquipeName == null)
				return false;
		} while (strEquipeName.isEmpty() || tempEquipes.contains(strEquipeName));

		// on crée l'équipe
		Equipe equipe = new Equipe(strEquipeName);
		equipe.setNbRetenu(ficheConcours.getParametre().getReglement().getNbMembresRetenu());
		equipe.setDifferentiationCriteria(scna);

		tempEquipes.add(equipe);

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
		cbEquipeClub.setEnabled(false);
		for (Map.Entry<Criterion, JCheckBox> cb : classmentCriteriaCB.entrySet()) {
			cb.getValue().setEnabled(tempEquipes.countEquipes() == 0);
		}
		
		return true;
	}

	private boolean addMembresToEquipe(Equipe equipe, DefaultMutableTreeNode dmtn) {

		TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
		Concurrent[] selectionConc = new Concurrent[selectedPaths.length];
		for (int i = 0; i < selectedPaths.length; i++) {
			selectionConc[i] = (Concurrent) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject();
		}

		// test si le nombre de concurrent n'est pas trop important pour l'équipe
		if (equipe.getMembresEquipe().size() + selectionConc.length > ficheConcours.getParametre().getReglement().getNbMembresEquipe()) {
			JOptionPane.showMessageDialog(this, localisation.getResourceString("equipe.taille.max"), //$NON-NLS-1$
					localisation.getResourceString("equipe.warning"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
			return false;
		}
		
		for (Object concurrent : selectionConc) {
			if (concurrent != null) {
				// ajoute le concurrent à l'equipe
				equipe.addConcurrent((Concurrent) concurrent);
				// ajoute le concurrent à l'arborescence.
				DefaultMutableTreeNode membreTreeNode = new DefaultMutableTreeNode(concurrent);
				dmtn.add(membreTreeNode);
			}
		}
		
		return true;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			if (e.getActionCommand().equals("filter")) { //$NON-NLS-1$
				for (Map.Entry<Criterion, JCheckBox> cb : classmentCriteriaCB.entrySet()) {
					cb.getKey().setClassementEquipe(cb.getValue().isSelected());
				}
				completePanel(); // categoriesEquipes();

			} else if (e.getSource() == cbEquipeClub) {
				tempEquipes.setLimitedByClub(cbEquipeClub.isSelected());
				completePanel();
				/*
				 * DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent(); if(dmtn != null) { Object dmtnObj = dmtn.getUserObject(); if(dmtnObj instanceof
				 * CriteriaSetLibelle) { listConcurrents(((CriteriaSetLibelle)dmtnObj).getCriteriaSet(), ConcurrentList.SORT_BY_CLUBS); } }
				 */
			}

		} else if (e.getSource() == jbAnnuler) {
			validation = false;
			setVisible(false);
		} else if (e.getSource() == jbValider) {
			for(Equipe equipe : tempEquipes.getEquipeList()) {
	            if(equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
	                if(JOptionPane.showConfirmDialog(this, 
	                		localisation.getResourceString("equipe.warning.incomplete"), //$NON-NLS-1$
	                		localisation.getResourceString("equipe.warning.incomplete.title"), //$NON-NLS-1$
	                		JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
	                	return;
	                }
	                break;
	            }
	        }
			
			validation = true;
			setVisible(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
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
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == treeConcurrents && onDrag) {
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeConcurrents);
			SwingUtilities.convertPointFromScreen(p, treeEquipes);

			// test si le drop correspond bien à la bonne action
			if (p.x > 0 && p.y > 0 && p.x < treeEquipes.getWidth() && p.y < treeEquipes.getHeight()) {

				TreePath treePath = treeEquipes.getPathForLocation(p.x, p.y);
				if(treePath == null) {
					this.getGlassPane().setVisible(false);
					dragObject = null;
					onDrag = false;
					return;
				}
				DefaultMutableTreeNode teamNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				Object destObj = teamNode.getUserObject();

				if (destObj instanceof Equipe) {

					Equipe equipe = (Equipe) destObj;

					if(addMembresToEquipe(equipe, teamNode)) {

						treeModel.reload(teamNode);
	
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeConcurrents.getSelectionPath().getParentPath().getLastPathComponent();
						TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
						for (TreePath selectedPath : selectedPaths) {
							parentNode.remove((DefaultMutableTreeNode) selectedPath.getLastPathComponent());
						}
						treeModelConcurrents.reload(parentNode);
					}
				} else {
					if(createEquipe(treeEquipes.getPathForLocation(p.x, p.y))) {

						TreePath[] selectedPaths = treeConcurrents.getSelectionPaths();
						for (TreePath selectedPath : selectedPaths) {
							((DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent()).remove((DefaultMutableTreeNode) selectedPath.getLastPathComponent());
						}
	
						treeModelConcurrents.reload((DefaultMutableTreeNode) treeConcurrents.getSelectionPath().getParentPath().getLastPathComponent());
					}
				}
			}
		} else if (e.getSource() == treeEquipes && onDrag) {
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, treeEquipes);
			SwingUtilities.convertPointFromScreen(p, treeConcurrents);
			if (p.x > 0 && p.y > 0 && p.x < treeConcurrents.getWidth() && p.y < treeConcurrents.getHeight()) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeEquipes.getSelectionPath().getLastPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeEquipes.getSelectionPath().getParentPath().getLastPathComponent();

				Equipe equipe = (Equipe) parentNode.getUserObject();
				equipe.removeConcurrent((Concurrent) selectedNode.getUserObject());

				if (equipe.getMembresEquipe().size() < ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
					tempEquipes.remove(equipe);
				}

				completePanel();
				completePanelEquipes();
			}
		}
		this.getGlassPane().setVisible(false);
		dragObject = null;
		onDrag = false;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
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
	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
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
						} else if (dmtnParent.getUserObject() instanceof CriteriaSetLibelle) {
							CriteriaSetLibelle critere = (CriteriaSetLibelle) dmtnParent.getUserObject();
							
							categoriesEquipes(critere.getCriteriaSet(), null);
						}
					} else if (dmtnObj instanceof Entite) {
						Entite club = (Entite) dmtnObj;

						DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) dmtn.getParent();
						CriteriaSetLibelle critere = (CriteriaSetLibelle) dmtnParent.getUserObject();

						categoriesEquipes(critere.getCriteriaSet(), club);
					} else {
						completePanelEquipes();
					}
				}

			}
		}
	}
}