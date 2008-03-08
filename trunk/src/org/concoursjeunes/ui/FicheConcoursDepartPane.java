/*
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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
 *  (at your option) any later version.
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
package org.concoursjeunes.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.Target;
import org.concoursjeunes.TargetPosition;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.exceptions.PlacementException;
import org.concoursjeunes.ui.dialog.ConcurrentDialog;
import org.concoursjeunes.ui.dialog.EquipeDialog;
import org.concoursjeunes.ui.dialog.TypeListingDialog;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.java2.AJList;
import ajinteractive.standard.ui.GhostGlassPane;

/**
 * Panneau de la fiche concours contenant l'ensemble des éléments spécifique à un départ donné
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class FicheConcoursDepartPane extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ListSelectionListener, KeyListener, FicheConcoursListener, TreeSelectionListener {

	private final JButton jbAjouterArcher = new JButton();
	private final JButton jbSupprimerArcher = new JButton();
	private final JButton jbEquipe = new JButton();
	private final JButton jbPlacementArcher = new JButton();
	private ButtonGroup jbgSort;
	private final JRadioButton jcbSortCible = new JRadioButton("", true); //$NON-NLS-1$
	private final JRadioButton jcbSortNom = new JRadioButton();
	private final JRadioButton jcbSortClub = new JRadioButton();

	private final JButton jbPrintListConc = new JButton();
	private final JButton jbPrintEtiquettes = new JButton();
	private final JButton jbPrintPasDeTir = new JButton();

	private final AJList ajlConcurrent = new AJList();
	private final JTree treeTarget = new JTree();
	private final TargetTreeModel treeModel = new TargetTreeModel();
	private JPopupMenu popup;

	private FicheConcoursPane ficheConcoursPane = null;

	private Object dragObject = null;
	private int depart = 0;

	private boolean onDrag = false;

	private final FicheConcours ficheConcours;

	/**
	 * Construction du panneau
	 * 
	 * @param ficheConcoursPane -
	 *            le panneau parent de la fiche concours
	 * @param depart -
	 *            le numero du départ représenté par le panneau courrant
	 */
	public FicheConcoursDepartPane(FicheConcoursPane ficheConcoursPane, int depart) {
		this.ficheConcoursPane = ficheConcoursPane;
		this.depart = depart;
		this.ficheConcours = ficheConcoursPane.getFicheConcours();

		// s'enregistre comme auditeur des changement de la fiche concours
		ficheConcours.addFicheConcoursListener(this);

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

		// enregistre les auditeurs d'évenement
		jbPrintListConc.addActionListener(this);
		jbPrintListConc.setMargin(new Insets(0, 0, 0, 0));
		jbPrintEtiquettes.addActionListener(this);
		jbPrintEtiquettes.setMargin(new Insets(0, 0, 0, 0));
		jbPrintPasDeTir.addActionListener(this);
		jbPrintPasDeTir.setMargin(new Insets(0, 0, 0, 0));

		// option d'affichage du classement
		northpaneGestion.setLayout(new BorderLayout());
		northpanePrintButton.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		northpanePrintButton.add(jbPrintListConc);
		northpanePrintButton.add(jbPrintEtiquettes);
		northpanePrintButton.add(jbPrintPasDeTir);
		northpaneGestion.add(northpanePrintButton, BorderLayout.NORTH);
		JLabel jl = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("interface.aideplacement")); //$NON-NLS-1$
		jl.setHorizontalTextPosition(JLabel.CENTER);
		JPanel pos = new JPanel();
		pos.setLayout(new FlowLayout(FlowLayout.CENTER));
		pos.add(jl);
		pos.setBackground(new Color(255, 255, 225));
		pos.setOpaque(true);
		northpaneGestion.add(pos, BorderLayout.CENTER);

		ficheG.setLayout(new BorderLayout());
		ficheG.add(northpaneGestion, BorderLayout.NORTH);
		ficheG.add(listeParCible(), BorderLayout.CENTER);

		// panneau des archers
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, listeParNom(), ficheG);
		splitpane.setOneTouchExpandable(true);
		splitpane.setResizeWeight(0.25);

		setLayout(new BorderLayout());
		add(splitpane, BorderLayout.CENTER);

		popup();
	}

	/**
	 * Initialisation du panneau "liste des concurrents"
	 * 
	 * @return le panneau contenant la liste des concurrents
	 */
	private JPanel listeParNom() {
		// liste des tireurs par nom
		JPanel pane = new JPanel();

		// creation des tables de tireurs
		JScrollPane scrollarcher = new JScrollPane();
		scrollarcher.setViewportView(ajlConcurrent);

		jbAjouterArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addarcher") //$NON-NLS-1$
		));
		jbAjouterArcher.setMargin(new Insets(0, 0, 0, 0));
		jbAjouterArcher.addActionListener(this);

		jbSupprimerArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.removearcher") //$NON-NLS-1$
		));
		jbSupprimerArcher.setMargin(new Insets(0, 0, 0, 0));
		jbSupprimerArcher.setEnabled(false);
		jbSupprimerArcher.addActionListener(this);

		jbEquipe.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team"))); //$NON-NLS-1$
		jbEquipe.setMargin(new Insets(0, 0, 0, 0));
		jbEquipe.addActionListener(this);

		jbPlacementArcher.setMargin(new Insets(0, 0, 0, 0));
		jbPlacementArcher.addActionListener(this);

		jbgSort = new ButtonGroup();

		jcbSortCible.addActionListener(this);
		jcbSortNom.addActionListener(this);
		jcbSortClub.addActionListener(this);

		jbgSort.add(jcbSortCible);
		jbgSort.add(jcbSortNom);
		jbgSort.add(jcbSortClub);

		// paneau des boutons
		Box buttonPane = Box.createHorizontalBox();
		buttonPane.add(jbAjouterArcher);
		buttonPane.add(jbSupprimerArcher);
		buttonPane.add(jbEquipe);
		buttonPane.add(jbPlacementArcher);

		JPanel sortPane = new JPanel();
		sortPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		sortPane.add(jcbSortCible);
		sortPane.add(jcbSortNom);
		sortPane.add(jcbSortClub);

		JPanel headerPane = new JPanel();
		headerPane.setLayout(new BorderLayout());
		headerPane.add(buttonPane, BorderLayout.NORTH);
		headerPane.add(sortPane, BorderLayout.CENTER);

		// initialistaion du panneau des tireurs
		pane.setLayout(new BorderLayout());
		pane.add(headerPane, BorderLayout.NORTH);
		pane.add(scrollarcher, BorderLayout.CENTER);

		ajlConcurrent.addMouseListener(this);
		ajlConcurrent.addMouseMotionListener(this);
		ajlConcurrent.addListSelectionListener(this);
		ajlConcurrent.addKeyListener(this);
		ajlConcurrent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlConcurrent.setCellRenderer(new ConcoursListeRenderer());

		return pane;
	}

	/**
	 * Initialisation de l'arbre des cibles
	 * 
	 * @return JPanel
	 */
	private JPanel listeParCible() {
		JPanel pane = new JPanel();

		// creation de l'arbre de cible
		JScrollPane scrollcible = new JScrollPane();
		scrollcible.setViewportView(treeTarget);

		pane.setLayout(new BorderLayout());
		pane.add(scrollcible, BorderLayout.CENTER);

		jbPrintEtiquettes.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		jbPrintListConc.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		jbPrintPasDeTir.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));

		treeTarget.setModel(treeModel);
		treeTarget.addMouseListener(this);
		treeTarget.addMouseMotionListener(this);
		treeTarget.addKeyListener(this);
		treeTarget.addTreeSelectionListener(this);
		treeTarget.setCellRenderer(new CibleRenderer(ficheConcours.getPasDeTir(depart)));
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

		//ajlConcurrent.add(popup);
	}

	/**
	 * Affecte les libellés localisé au composant de l'interface
	 */
	private void affectLibelle() {
		jbPrintListConc.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printlistconc")); //$NON-NLS-1$
		jbPrintEtiquettes.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printetiquettes")); //$NON-NLS-1$
		jbPrintPasDeTir.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.printpasdetir")); //$NON-NLS-1$

		jbAjouterArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
		jbAjouterArcher.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
		jbSupprimerArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer")); //$NON-NLS-1$
		jbEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.equipe")); //$NON-NLS-1$
		jbPlacementArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.placementarcher")); //$NON-NLS-1$
		jcbSortCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.cible")); //$NON-NLS-1$
		jcbSortNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.nom")); //$NON-NLS-1$
		jcbSortClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("radiobutton.club")); //$NON-NLS-1$

		treeModel.setRootLabel(ConcoursJeunes.ajrLibelle.getResourceString("treenode.racine")); //$NON-NLS-1$
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
		ajlConcurrent.setListData(ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS));
	}

	/**
	 * Arborescence du pas de tir
	 * 
	 */
	private void createListeParCible() {
		treeModel.setTargetChilds(ficheConcours.getPasDeTir(depart).getTargets());
	}

	/**
	 * Affiche la boite de dialogue d'insertion de concurrent
	 * 
	 */
	private void showAddConcurrentDialog() {
		int codeRetour = 0;
		do {
			codeRetour = ficheConcoursPane.concDialog.showNewConcurrentDialog(ficheConcours.getCurrentDepart());
			if (codeRetour != ConcurrentDialog.CANCEL && !ficheConcoursPane.concDialog.getConcurrent().getNomArcher().equals("")) { //$NON-NLS-1$

				try {
					ficheConcours.addConcurrent(ficheConcoursPane.concDialog.getConcurrent(), depart);
				} catch (IOException e) {
					JXErrorPane.showDialog(ficheConcoursPane.getParentframe(), new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
							null, null, e, Level.SEVERE, null));
					e.printStackTrace();
				}
			}
		} while (codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT);
	}

	/**
	 * Suppression d'un archer de la liste des concurrents
	 */
	private void removeSelectedConcurrent() {

		Concurrent removedConcurrent = (Concurrent) ajlConcurrent.getSelectedValue();
		if(removedConcurrent != null) {
			if (JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) //$NON-NLS-1$
				return;
	
			try {
				ficheConcours.removeConcurrent(removedConcurrent);
			} catch (IOException e) {
				JXErrorPane.showDialog(ficheConcoursPane.getParentframe(), new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
						null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			}
		}
	}

	/**
	 * routine de placement automatique des concurrents sur le pas de tir en fonction des critéres SCNA
	 * 
	 */
	private void placementConcurrents() {

		if (JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.replacement"), //$NON-NLS-1$
				ConcoursJeunes.ajrLibelle.getResourceString("confirmation.replacement.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) //$NON-NLS-1$
			return;

		ficheConcours.getPasDeTir(depart).placementConcurrents();

		if (jcbSortCible.isSelected()) {
			ajlConcurrent.setListData(ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS));
		}
		treeTarget.repaint();
	}

	/**
	 * placement manuel (en drag n'drop) d'un concurrent sur le pas de tir
	 * 
	 * @param destPath -
	 *            le chemin du noeud de destination
	 */
	private void placementManuelConcurrent(Concurrent concurrent, TreePath destPath) {
		if (destPath == null || concurrent == null)
			return;

		Target cible;
		int position = -1;

		// selectionne la destination
		treeTarget.setSelectionPath(destPath);

		if (destPath.getLastPathComponent() instanceof Target) {
			cible = (Target) destPath.getLastPathComponent();
		} else if (destPath.getLastPathComponent() instanceof TargetPosition) {
			// recupere le noeud destination
			if (destPath.getParentPath() != null && destPath.getParentPath().getLastPathComponent() instanceof Target) {
				cible = (Target) destPath.getParentPath().getLastPathComponent();
				TargetPosition targetPosition = (TargetPosition) destPath.getLastPathComponent();
				position = targetPosition.getPosition();
			} else {
				return;
			}
		} else {
			return;
		}

		if (cible == null) {
			JOptionPane.showMessageDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.notarget"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		} else {
			try {
				ficheConcours.getPasDeTir(depart).placementConcurrent(concurrent, cible, position);
			} catch (PlacementException e) {
				String message;
				switch (e.getNature()) {
					case ANY_AVAILABLE_POSITION:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.anyplace"); //$NON-NLS-1$
						break;
					case BAD_DISTANCES:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.baddistance"); //$NON-NLS-1$
						break;
					case BAD_TARGETFACE:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.badtargetface"); //$NON-NLS-1$
						break;
					case NULL_CONCURRENT:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.nullconcurrent"); //$NON-NLS-1$
						break;
					case POSITION_AVAILABLE_FOR_VALID_CONCURRENT:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.handicap"); //$NON-NLS-1$
						break;
					case POSITION_RESERVED_FOR_HANDICAP:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.reserved"); //$NON-NLS-1$
						break;
					default:
						message = ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.unknown"); //$NON-NLS-1$
						break;
				}
				JOptionPane.showMessageDialog(null, message,
						ConcoursJeunes.ajrLibelle.getResourceString("erreur.noplacement.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				//e.printStackTrace();
			}
		}
	}

	/**
	 * retire un concurrent du pas de tir
	 * 
	 * @param concurrent -
	 *            le concurrent à retirer
	 */
	private void retraitPlacementConcurrent(Concurrent concurrent) {
		ficheConcours.getPasDeTir(depart).retraitConcurrent(concurrent);
	}

	/**
	 * Retourne le chemin dans l'arborescence de l'arbre du concurrent donné
	 * 
	 * @param concurrent -
	 *            le concurrent à rechercher
	 * @return la position dans l'arbre
	 */
	private TreePath getTreePathForConcurrent(Concurrent concurrent) {
		if (concurrent == null)
			return null;

		return treeModel.getTreePathForNode(concurrent);
	}

	/**
	 * selectionne le concurrent donnée dans l'arbre
	 * 
	 * @param concurrent -
	 *            le concurrent à selectionner
	 */
	private void selectConcurrentInTree(Concurrent concurrent) {
		TreePath childPath = getTreePathForConcurrent(concurrent);
		if (childPath != null) {
			if (treeTarget.getSelectionPath() != null)
				treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
			treeTarget.setSelectionPath(childPath);
		}
	}

	/**
	 * Invoqué lorsque la liste des concurents change (ajout ou suppression d'un concurrent)
	 * 
	 * @see org.concoursjeunes.event.FicheConcoursListener#listConcurrentChanged(org.concoursjeunes.event.FicheConcoursEvent)
	 */
	public void listConcurrentChanged(FicheConcoursEvent e) {
		switch (e.getEvent()) {
		case FicheConcoursEvent.ADD_CONCURRENT:
			if (e.getConcurrent().getDepart() == depart)
				ajlConcurrent.add(e.getConcurrent());
			break;
		case FicheConcoursEvent.REMOVE_CONCURRENT:
			if (e.getConcurrent().getDepart() == depart)
				ajlConcurrent.remove(e.getConcurrent());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.FicheConcoursListener#pasDeTirChanged(org.concoursjeunes.FicheConcoursEvent)
	 */
	public void pasDeTirChanged(FicheConcoursEvent e) {
		createListeParCible();
		createListeParNom();
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String cmd = e.getActionCommand();

		if (source == jbAjouterArcher) {
			showAddConcurrentDialog();
		} else if (source == jbSupprimerArcher) {
			// Supprime un concurrent à la liste des inscrits
			removeSelectedConcurrent();
		} else if (source == jbEquipe) {
			// Creer les equipes
			EquipeDialog ed = new EquipeDialog(ficheConcoursPane.getParentframe(), ficheConcours);
			if (ed.isValider()) {
				ficheConcours.setEquipes(ed.getEquipes());
			}
		} else if (source == jbPlacementArcher) {
			placementConcurrents();
		} else if (source == jbPrintListConc) {
			TypeListingDialog tld = new TypeListingDialog(ficheConcoursPane.getParentframe());
			int returnType = tld.showTypeListingDialog();
			if (returnType == TypeListingDialog.ALPHA) {
				if(!ficheConcours.printArcherList(FicheConcours.ALPHA))
					JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
			} else if (returnType == TypeListingDialog.GREFFE) {
				if(!ficheConcours.printArcherList(FicheConcours.GREFFE))
					JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
			} else if (returnType == TypeListingDialog.TARGET) {
				if(!ficheConcours.printArcherList(FicheConcours.TARGET))
					JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
			}
		} else if (source == jbPrintEtiquettes) {
			if(!ficheConcours.printEtiquettes()) {
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
			}
		} else if (source == jbPrintPasDeTir) {
			if(!ficheConcours.printPasDeTir()) {
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
			}
		} else if (source instanceof JRadioButton) {
			if (source == jcbSortCible)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_TARGETS));
			else if (source == jcbSortNom)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_NAME));
			else if (source == jcbSortClub)
				ajlConcurrent.setListData(ConcurrentList.sort(ficheConcours.getConcurrentList().list(depart), ConcurrentList.SortCriteria.SORT_BY_CLUBS));
			ajlConcurrent.repaint();
		} else if (cmd.equals("popup.edition")) { //$NON-NLS-1$
			ficheConcoursPane.openConcurrentDialog((Concurrent) ajlConcurrent.getSelectedValue());
		} else if (cmd.equals("popup.suppression")) { //$NON-NLS-1$
			// Supprime un concurrent à la liste des inscrits
			removeSelectedConcurrent();
		} else if (cmd.equals("popup.retrait")) { //$NON-NLS-1$
			// donne le concurrent à retirer
			retraitPlacementConcurrent((Concurrent) ajlConcurrent.getSelectedValue());
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == treeTarget) {
			if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
				Point coord = e.getPoint();

				TreePath destinationPath = treeTarget.getPathForLocation(coord.x, coord.y);

				if(destinationPath != null) {
					treeTarget.setSelectionPath(destinationPath);
	
					if (destinationPath.getLastPathComponent() instanceof Concurrent) {
						ajlConcurrent.setSelectedValue(destinationPath.getLastPathComponent(), true);
	
						popup.show(e.getComponent(), e.getX(), e.getY());
						ajlConcurrent.repaint();
						/*popup.setLocation(e.getX(), e.getY());
						popup.setInvoker(ajlConcurrent);
						popup.setVisible(true);*/
					}
				}
			} else {
				int selRow = treeTarget.getRowForLocation(e.getX(), e.getY());
				if (selRow != -1 && e.getClickCount() == 2) {
					Object selectedTreeNode = treeTarget.getSelectionPath().getLastPathComponent();

					if (selectedTreeNode instanceof Concurrent) {
						ficheConcoursPane.openConcurrentDialog((Concurrent) selectedTreeNode);
					} else if (selectedTreeNode instanceof Target) {
						ficheConcoursPane.index = ((Target) selectedTreeNode).getNumCible();
						if (ficheConcours.getConcurrentList().list(ficheConcoursPane.index, ficheConcours.getCurrentDepart()).length > 0) {
							ficheConcoursPane.openResultatDialog();
						}
					} else {
						ficheConcoursPane.index = 1;
						if (ficheConcours.getConcurrentList().list(ficheConcoursPane.index, ficheConcours.getCurrentDepart()).length > 0) {
							ficheConcoursPane.openResultatDialog();
						}
					}
				}
			}
		} else {
			if (e.getClickCount() == 2) {
				ficheConcoursPane.openConcurrentDialog((Concurrent) ajlConcurrent.getSelectedValue());
			} else if (e.getModifiers() == InputEvent.BUTTON3_MASK) { //
				int elemIndex = ajlConcurrent.locationToIndex(e.getPoint());
				if(elemIndex > -1) {
					ajlConcurrent.setSelectedIndex(elemIndex);
	
					Concurrent tmpConcurrent = (Concurrent) ajlConcurrent.getSelectedValue();
	
					if(tmpConcurrent != null) {
						if (tmpConcurrent.getCible() > 0) {
							if (treeTarget.getSelectionPath() != null)
								treeTarget.collapsePath(treeTarget.getSelectionPath().getParentPath());
							treeTarget.setSelectionPath(getTreePathForConcurrent(tmpConcurrent));
						}
		
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
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
		if (e.getSource() == ajlConcurrent) {
			dragObject = ajlConcurrent.getSelectedValue();
		} else if (e.getSource() == treeTarget) {
			Point p = e.getPoint();
			TreePath tp = treeTarget.getPathForLocation(p.x, p.y);
			if (tp != null) {
				dragObject = tp.getLastPathComponent();
			}
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == ajlConcurrent && onDrag) {
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, ajlConcurrent);
			SwingUtilities.convertPointFromScreen(p, treeTarget);

			// test si le drop correspond bien à la bonne action
			if (p.x > 0 & p.y > 0) {
				placementManuelConcurrent((Concurrent) dragObject, treeTarget.getPathForLocation(p.x, p.y));
			}
		} else if (e.getSource() == treeTarget && onDrag) {
			Point p = (Point) e.getPoint().clone();
			TreePath tp = treeTarget.getPathForLocation(p.x, p.y);
			if (tp != null) {
				if (tp.getLastPathComponent() instanceof TargetPosition) {
					if (dragObject instanceof Concurrent) {
						placementManuelConcurrent((Concurrent) dragObject, tp);
					}
				} else if (tp.getLastPathComponent() instanceof Target) {
					if (dragObject instanceof Target) {
						Target tmpcbl = (Target) dragObject;

						for (int i = 0; i < ficheConcours.getParametre().getNbTireur(); i++) {
							if (tmpcbl.getConcurrentAt(i) != null) {
								Concurrent concurrent = tmpcbl.getConcurrentAt(i);

								placementManuelConcurrent(concurrent, tp);
							}
						}
					} else if (dragObject instanceof Concurrent) {
						placementManuelConcurrent((Concurrent) dragObject, tp);
					}
				}
			} else {
				SwingUtilities.convertPointToScreen(p, treeTarget);
				SwingUtilities.convertPointFromScreen(p, ajlConcurrent);
				if (p.x > 0 && p.y > 0 && p.x < ajlConcurrent.getWidth() && p.y < ajlConcurrent.getHeight() && dragObject instanceof Concurrent) {
					retraitPlacementConcurrent((Concurrent) dragObject);
				}
			}
		}
		// dans tous les cas remettre le curseur par defaut à la fin du drag
		ajlConcurrent.setSelectable(true);

		ficheConcoursPane.getParentframe().getGlassPane().setVisible(false);
		dragObject = null;
		onDrag = false;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == ajlConcurrent) {
			if(ajlConcurrent.getSelectedIndex() == -1)
				return;
			GhostGlassPane glassPane = (GhostGlassPane) ficheConcoursPane.getParentframe().getGlassPane();
			if (!onDrag) {
				ajlConcurrent.setSelectable(false);
				Rectangle rect = ajlConcurrent.getCellBounds(ajlConcurrent.getSelectedIndex(), ajlConcurrent.getSelectedIndex());

				BufferedImage image = new BufferedImage(ajlConcurrent.getWidth(), ajlConcurrent.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();
				ajlConcurrent.paint(g);
				image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);

				glassPane.setVisible(true);

				glassPane.setImage(image);

				// ajlConcurrent.set

				onDrag = true;
			}

			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, ajlConcurrent);
			SwingUtilities.convertPointFromScreen(p, glassPane);

			glassPane.setPoint(p);
			glassPane.repaint();
		} else if (e.getSource() == treeTarget) {
			GhostGlassPane glassPane = (GhostGlassPane) ficheConcoursPane.getParentframe().getGlassPane();
			if (!onDrag) {
				if (dragObject instanceof Concurrent || dragObject instanceof Target) {
					// Rectangle rect = treeTarget.getPathBounds(getTreePathForConcurrent(tmpconc));
					// dragObject = tp.getLastPathComponent();

					Rectangle rect = treeTarget.getPathBounds(treeModel.getTreePathForNode(dragObject));

					BufferedImage image = new BufferedImage(treeTarget.getWidth(), treeTarget.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics g = image.getGraphics();
					treeTarget.paint(g);
					image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);

					glassPane.setVisible(true);

					glassPane.setImage(image);

					// ajlConcurrent.set

					onDrag = true;
				}
			}

			if (onDrag) {
				Point p = (Point) e.getPoint().clone();
				SwingUtilities.convertPointToScreen(p, treeTarget);
				SwingUtilities.convertPointFromScreen(p, glassPane);

				glassPane.setPoint(p);
				glassPane.repaint();
			}
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
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		jbSupprimerArcher.setEnabled(true);

		if (ajlConcurrent.getSelectedValue() instanceof Concurrent) {
			Concurrent tmpConcurrent = (Concurrent) ajlConcurrent.getSelectedValue();

			selectConcurrentInTree(tmpConcurrent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		TreePath destinationPath = e.getPath();

		// recupere le noeud destination et son parent
		Object node = destinationPath.getLastPathComponent();
		if (node instanceof Concurrent) {
			ajlConcurrent.setSelectedValue(node, true);
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getSource() instanceof JList) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				removeSelectedConcurrent();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				ficheConcoursPane.openConcurrentDialog((Concurrent) ajlConcurrent.getSelectedValue());
			}
		} else if (e.getSource() instanceof JTree) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				Object node = treeTarget.getSelectionPath().getLastPathComponent();
				if (node instanceof Concurrent) {
					removeSelectedConcurrent();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Object node = treeTarget.getSelectionPath().getLastPathComponent();
				if (node instanceof Concurrent)
					ficheConcoursPane.openConcurrentDialog((Concurrent) node);
				if (node instanceof Target) {
					ficheConcoursPane.index = ((Target) node).getNumCible();
					ficheConcoursPane.openResultatDialog();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}
	
	/*@Override
	public void dispose() {
		System.out.println("Destruction de la fenetre ConcurrentDialog");
	}*/
}