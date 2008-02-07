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
package org.concoursjeunes.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.concoursjeunes.Blason;
import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.CriteriaSetLibelle;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Reglement;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.java2.GridbagComposer;
import ajinteractive.standard.java2.NumberDocument;

/**
 * @author Aurélien JEOFFRAY
 * 
 */
public class ReglementDialog extends JDialog implements ActionListener, MouseListener {

	public static final int NO_LOCK = 0;
	public static final int LOCK_CHANGE_L1 = 1;
	public static final int LOCK_CHANGE_L2 = 2;

	private Reglement reglement;

	private final JLabel jlReglementName = new JLabel();

	private final JLabel jlNbSerie = new JLabel();
	private final JLabel jlNbVoleeParSerie = new JLabel();
	private final JLabel jlNbFlecheParVolee = new JLabel();
	private final JLabel jlNbMembresEquipe = new JLabel();
	private final JLabel jlNbMembresRetenu = new JLabel();
	private final JCheckBox jcbOfficialReglement = new JCheckBox();

	private final JLabel jlNbDB = new JLabel();
	private final JButton jbAddCriteria = new JButton();
	private final JButton jbAddCriteriaMember = new JButton();
	private final JButton jbUpElement = new JButton();
	private final JButton jbDownElement = new JButton();
	private final JButton jbRemoveElement = new JButton();
	private final DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("criteres"); //$NON-NLS-1$
	private final DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
	private final JTree treeCriteria = new JTree(treeModel);

	private final JTextField jtfNbSerie = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JTextField jtfNbVoleeParSerie = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JTextField jtfNbFlecheParVolee = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JTextField jtfNbMembresEquipe = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JTextField jtfNbMembresRetenu = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$

	private final JTable jtDistanceBlason = new JTable();
	private final JScrollPane jspDistanceBlason = new JScrollPane();
	private final JComboBox jcbBlasons = new JComboBox();

	private final JButton jbValider = new JButton();
	private final JButton jbAnnuler = new JButton();

	private CriteriaSet[] differentiationCriteria;

	private int verrou = NO_LOCK;

	public ReglementDialog(JFrame parentframe, Reglement reglement) {
		super(parentframe, true);

		this.reglement = reglement;

		init();
		affectLibelle();
	}

	private void init() {
		JPanel panel = new JPanel();
		JPanel jpAction = new JPanel();

		panel.setLayout(new BorderLayout());
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));

		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.general.title"), initGeneral()); //$NON-NLS-1$
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.criteres.title"), initCriteria()); //$NON-NLS-1$
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.categories.title"), initCategories()); //$NON-NLS-1$

		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);

		panel.add(tabbedPane, BorderLayout.CENTER);
		panel.add(jpAction, BorderLayout.SOUTH);

		setContentPane(panel);
	}

	private JPanel initGeneral() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel panel = new JPanel();

		gridbagComposer.setParentPanel(panel);
		c.weightx = 1.0;
		// c.weighty = 0.0;

		c.gridy = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlReglementName, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(Box.createVerticalStrut(30), c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(new JSeparator(), c);
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlNbSerie, c);
		gridbagComposer.addComponentIntoGrid(jtfNbSerie, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbVoleeParSerie, c);
		gridbagComposer.addComponentIntoGrid(jtfNbVoleeParSerie, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbFlecheParVolee, c);
		gridbagComposer.addComponentIntoGrid(jtfNbFlecheParVolee, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbMembresEquipe, c);
		gridbagComposer.addComponentIntoGrid(jtfNbMembresEquipe, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbMembresRetenu, c);
		gridbagComposer.addComponentIntoGrid(jtfNbMembresRetenu, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbOfficialReglement, c);
		c.gridy++;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(Box.createGlue(), c);

		return panel;
	}

	private JPanel initCriteria() {
		JPanel jpDifCriteria = new JPanel();

		JPanel jpOperations = new JPanel();
		JScrollPane jspCriteres = new JScrollPane();

		treeCriteria.addMouseListener(this);
		treeCriteria.setToggleClickCount(3);

		jpDifCriteria.setLayout(new BorderLayout());

		jbAddCriteria.setMargin(new Insets(0, 0, 0, 0));
		jbAddCriteria.addActionListener(this);
		jbAddCriteriaMember.setMargin(new Insets(0, 0, 0, 0));
		jbAddCriteriaMember.addActionListener(this);
		jbUpElement.setMargin(new Insets(0, 0, 0, 0));
		jbUpElement.addActionListener(this);
		jbDownElement.setMargin(new Insets(0, 0, 0, 0));
		jbDownElement.addActionListener(this);
		jbRemoveElement.setMargin(new Insets(0, 0, 0, 0));
		jbRemoveElement.addActionListener(this);

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

		return jpDifCriteria;
	}

	private JPanel initCategories() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel jpConcours = new JPanel();

		jspDistanceBlason.setPreferredSize(new Dimension(400, 250));
		jtDistanceBlason.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jtDistanceBlason.setPreferredScrollableViewportSize(new Dimension(450, 200));

		gridbagComposer.setParentPanel(jpConcours);
		c.gridy = 0;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNbDB, c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(jspDistanceBlason, c);

		return jpConcours;
	}

	private void affectLibelle() {
		setTitle(ConcoursJeunes.ajrLibelle.getResourceString("reglement.titre")); //$NON-NLS-1$

		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$

		jlReglementName.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.name")); //$NON-NLS-1$
		jlNbSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.serie")); //$NON-NLS-1$
		jlNbVoleeParSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.voleeparserie")); //$NON-NLS-1$
		jlNbFlecheParVolee.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.flecheparvolee")); //$NON-NLS-1$
		jlNbMembresEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.membresmax")); //$NON-NLS-1$
		jlNbMembresRetenu.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.selectionmax")); //$NON-NLS-1$
		jcbOfficialReglement.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.official")); //$NON-NLS-1$

		jbAddCriteria.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteria"))); //$NON-NLS-1$
		jbAddCriteria.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.addcriteria")); //$NON-NLS-1$
		jbAddCriteriaMember.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteriamember"))); //$NON-NLS-1$
		jbAddCriteriaMember.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.addcriteriamember")); //$NON-NLS-1$
		jbUpElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.upelement"))); //$NON-NLS-1$
		jbUpElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.upelement")); //$NON-NLS-1$
		jbDownElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.downelement"))); //$NON-NLS-1$
		jbDownElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.downelement")); //$NON-NLS-1$
		jbRemoveElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.removeelement"))); //$NON-NLS-1$
		jbRemoveElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.removeelement")); //$NON-NLS-1$
	}

	private void completePanel() {
		completeGeneral();
		completeCriteria();
		completeCategories();
	}

	private void completeGeneral() {
		if (reglement.isOfficialReglement())
			verrou = LOCK_CHANGE_L2;

		switch (verrou) {
			case LOCK_CHANGE_L2:
				jtfNbMembresEquipe.setEditable(false);
				jtfNbMembresRetenu.setEditable(false);
	
				jbAddCriteria.setEnabled(false);
				jbAddCriteriaMember.setEnabled(false);
				jbDownElement.setEnabled(false);
				jbUpElement.setEnabled(false);
				jbRemoveElement.setEnabled(false);
			case LOCK_CHANGE_L1:
				jtfNbSerie.setEditable(false);
				jtfNbVoleeParSerie.setEditable(false);
				jtfNbFlecheParVolee.setEditable(false);
		}

		jlReglementName.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.name") + " " + reglement.getName()); //$NON-NLS-1$ //$NON-NLS-2$

		jtfNbSerie.setText(reglement.getNbSerie() + ""); //$NON-NLS-1$
		jtfNbVoleeParSerie.setText(reglement.getNbVoleeParSerie() + ""); //$NON-NLS-1$
		jtfNbFlecheParVolee.setText(reglement.getNbFlecheParVolee() + ""); //$NON-NLS-1$
		jtfNbMembresEquipe.setText(reglement.getNbMembresEquipe() + ""); //$NON-NLS-1$
		jtfNbMembresRetenu.setText(reglement.getNbMembresRetenu() + ""); //$NON-NLS-1$

		jcbOfficialReglement.setSelected(reglement.isOfficialReglement());
		if (System.getProperty("debug.mode") == null) { //$NON-NLS-1$
			jcbOfficialReglement.setEnabled(false);
		}
	}

	private void completeCriteria() {
		if (reglement == null)
			return;
		treeRoot.removeAllChildren();
		for (Criterion critere : reglement.getListCriteria()) {
			if (critere != null) {
				DefaultMutableTreeNode dmtnCriteria = new DefaultMutableTreeNode(critere);
				treeRoot.add(dmtnCriteria);

				for (CriterionElement criterionIndividu : critere.getCriterionElements()) {
					DefaultMutableTreeNode dmtnCriteriaIndiv = new DefaultMutableTreeNode(criterionIndividu);
					dmtnCriteria.add(dmtnCriteriaIndiv);
				}
			}
		}
		treeModel.reload();
		jspDistanceBlason.setViewportView(jtDistanceBlason);
	}

	private void completeCategories() {
		if (reglement != null)
			jtDistanceBlason.setModel(createTableModel());
		try {
			List<Blason> blasons = Blason.listAvailableTargetFace();
			for(Blason blason : blasons)
				jcbBlasons.addItem(blason);
			TableColumn cH = jtDistanceBlason.getColumnModel().getColumn(jtDistanceBlason.getColumnModel().getColumnCount() - 1);
			cH.setCellEditor(new DefaultCellEditor(jcbBlasons));
		} catch (SQLException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}

		if (verrou != NO_LOCK) {
			jtDistanceBlason.setEnabled(false);
		}
	}

	private DefaultTableModel createTableModel() {
		assert reglement != null : "ne peut creer le tableau avec un reglement null"; //$NON-NLS-1$

		DefaultTableModel dtm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || reglement.isOfficialReglement())
					return false;
				return true;
			}
		};
		
		//s'assure que le nombre de serie est corect
		reglement.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));

		dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.scna")); //$NON-NLS-1$
		for (int i = 0; i < reglement.getNbSerie(); i++)
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$
		dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason")); //$NON-NLS-1$

		differentiationCriteria = CriteriaSet.listCriteriaSet(reglement, reglement.getPlacementFilter());

		for (int i = 0; i < differentiationCriteria.length; i++) {
			Object[] row = new Object[reglement.getNbSerie() + 2];
			CriteriaSetLibelle scnaLib = new CriteriaSetLibelle(differentiationCriteria[i]);
			row[0] = scnaLib.toString();

			for (int j = 1; j < reglement.getNbSerie() + 1; j++) {
				if (reglement.getDistancesEtBlasonFor(differentiationCriteria[i]) != null)
					row[j] = "" + reglement.getDistancesEtBlasonFor(differentiationCriteria[i]).getDistance()[j - 1]; //$NON-NLS-1$
				else
					row[j] = "0"; //$NON-NLS-1$
			}
			if (reglement.getDistancesEtBlasonFor(differentiationCriteria[i]) != null)
				row[reglement.getNbSerie() + 1] = reglement.getDistancesEtBlasonFor(differentiationCriteria[i]).getTargetFace();
			else {
				Blason defaultBlason = Blason.NULL;
				try {
					List<Blason> availableTargetFace = Blason.listAvailableTargetFace();
					if(availableTargetFace.size() > 0)
						defaultBlason = availableTargetFace.get(0);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				row[reglement.getNbSerie() + 1] = defaultBlason;
			}
			dtm.addRow(row);
		}

		return dtm;
	}

	/**
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * @param reglement
	 *            reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}

	/**
	 * @return verrou
	 */
	public int getVerrou() {
		return verrou;
	}

	/**
	 * @param verrou
	 *            verrou à définir
	 */
	public void setVerrou(int verrou) {
		this.verrou = verrou;
	}

	public Reglement showReglementDialog() {
		completePanel();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		return reglement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.jbValider) {
			if (reglement == null)
				reglement = new Reglement();
			reglement.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));
			reglement.setNbVoleeParSerie(Integer.parseInt(jtfNbVoleeParSerie.getText()));
			reglement.setNbFlecheParVolee(Integer.parseInt(jtfNbFlecheParVolee.getText()));
			reglement.setNbMembresEquipe(Integer.parseInt(jtfNbMembresEquipe.getText()));
			reglement.setNbMembresRetenu(Integer.parseInt(jtfNbMembresRetenu.getText()));

			if(jtDistanceBlason.getCellEditor() != null)
				jtDistanceBlason.getCellEditor().stopCellEditing();
			for (int i = 0; i < jtDistanceBlason.getRowCount(); i++) {
				int[] distances = new int[reglement.getNbSerie()];
				for (int j = 0; j < distances.length; j++) {
					if (j < jtDistanceBlason.getModel().getColumnCount() - 2)
						distances[j] = Integer.parseInt((String) this.jtDistanceBlason.getModel().getValueAt(i, j + 1));
					else
						distances[j] = 0;
				}
				DistancesEtBlason db = reglement.getDistancesEtBlasonFor(differentiationCriteria[i]);
				if (db == null) {
					db = new DistancesEtBlason(distances, (Blason) this.jtDistanceBlason.getModel().getValueAt(i, jtDistanceBlason.getModel().getColumnCount() - 1));

					db.setCriteriaSet(differentiationCriteria[i]);
					db.setReglement(reglement);

					reglement.addDistancesEtBlason(db);
				} else {
					int updateDbIndex = reglement.getListDistancesEtBlason().indexOf(db);

					db.setDistance(distances);
					db.setTargetFace((Blason)jtDistanceBlason.getModel().getValueAt(i, jtDistanceBlason.getModel().getColumnCount() - 1));

					reglement.getListDistancesEtBlason().set(updateDbIndex, db);
				}
			}

			reglement.setOfficialReglement(jcbOfficialReglement.isSelected());

			setVisible(false);
		} else if (source == jbAnnuler) {
			reglement = null;
			setVisible(false);
		}
		if (source == jbAddCriteria) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getPathForRow(0).getLastPathComponent();
			if (dmtn != null) {
				CriterionDialog cd = new CriterionDialog(this);
				cd.setLock(verrou);
				if (cd.showCriterionDialog() != null) {
					DefaultMutableTreeNode dmtnCrit = new DefaultMutableTreeNode(cd.getCriterion());

					dmtn.add(dmtnCrit);

					treeModel.reload();
				}
			}
		} else if (source == jbAddCriteriaMember) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					Criterion criterion = (Criterion) dmtnObj;

					if (!(criterion.isPlacement() && verrou != NO_LOCK)) {
						TreePath selectedPath = treeCriteria.getSelectionPath();
						CriterionElementDialog cpd = new CriterionElementDialog(this, criterion);

						if (cpd.getCriterionIndividu() != null) {
							DefaultMutableTreeNode dmtnIndiv = new DefaultMutableTreeNode(cpd.getCriterionIndividu());

							dmtn.add(dmtnIndiv);

							treeModel.reload(dmtn);
							treeCriteria.expandPath(selectedPath);
							treeCriteria.setSelectionPath(selectedPath);
							// treeCriteria.expandPath(selectedPath);

							jtDistanceBlason.setModel(createTableModel());
							//generateSCNA_DBRow();
						}
					} else {
						JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("reglement.message.criteria.noelement"), //$NON-NLS-1$
								ConcoursJeunes.ajrLibelle.getResourceString("reglement.message.criteria.noelement.title"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		} else if (source == jbUpElement) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					int curIndex = reglement.getListCriteria().indexOf(dmtnObj);
					if (curIndex > 0) {
						reglement.getListCriteria().set(curIndex, reglement.getListCriteria().get(curIndex - 1));
						reglement.getListCriteria().set(curIndex - 1, (Criterion) dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode) treeCriteria.getPathForRow(0).getLastPathComponent();
					dmtnRoot.insert(dmtn, curIndex - 1);

					treeModel.reload();

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				} else if (dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion) dmtnParent.getUserObject();

					int curIndex = curCriterion.getCriterionElements().indexOf(dmtnObj);
					if (curIndex > 0) {
						List<CriterionElement> alce = curCriterion.getCriterionElements();

						CriterionElement precElement = alce.get(curIndex - 1);
						CriterionElement curElement = (CriterionElement) dmtnObj;
						alce.set(curIndex, precElement);
						alce.set(curIndex - 1, curElement);
					}
					treeModel.removeNodeFromParent(dmtn);

					dmtnParent.insert(dmtn, curIndex - 1);

					treeModel.reload();

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				}
			}
		} else if (source == jbDownElement) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					int curIndex = reglement.getListCriteria().indexOf(dmtnObj);
					if (curIndex < reglement.getListCriteria().size() - 1) {
						reglement.getListCriteria().set(curIndex, reglement.getListCriteria().get(curIndex + 1));
						reglement.getListCriteria().set(curIndex + 1, (Criterion) dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode) treeCriteria.getPathForRow(0).getLastPathComponent();
					dmtnRoot.insert(dmtn, curIndex + 1);

					treeModel.reload();

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				} else if (dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion) dmtnParent.getUserObject();

					int curIndex = curCriterion.getCriterionElements().indexOf(dmtnObj);
					if (curIndex < curCriterion.getCriterionElements().size() - 1) {
						curCriterion.getCriterionElements().set(curIndex, curCriterion.getCriterionElements().get(curIndex + 1));
						curCriterion.getCriterionElements().set(curIndex + 1, (CriterionElement) dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					dmtnParent.insert(dmtn, curIndex + 1);

					treeModel.reload();

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				}
			}
		} else if (source == jbRemoveElement) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					reglement.getListCriteria().remove(dmtnObj);
					((Criterion) dmtnObj).delete();

					treeModel.removeNodeFromParent(dmtn);

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				} else if (dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion) dmtnParent.getUserObject();
					curCriterion.getCriterionElements().remove(dmtnObj);
					((CriterionElement) dmtnObj).delete();

					treeModel.removeNodeFromParent(dmtn);

					jtDistanceBlason.setModel(createTableModel());
					//generateSCNA_DBRow();
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if(dmtn == null)
				return;
			Object dmtnObj = dmtn.getUserObject();
			if (dmtnObj instanceof Criterion) {
				TreePath selectedPath = treeCriteria.getSelectionPath();
				CriterionDialog criterionDialog = new CriterionDialog(this, (Criterion) dmtnObj);
				criterionDialog.setLock(verrou);
				criterionDialog.showCriterionDialog();

				treeModel.reload((TreeNode) treeCriteria.getSelectionPath().getLastPathComponent());
				treeCriteria.setSelectionPath(selectedPath);

				jtDistanceBlason.setModel(createTableModel());
				//generateSCNA_DBRow();
			} else if (dmtnObj instanceof CriterionElement) {
				TreePath selectedPath = treeCriteria.getSelectionPath();
				DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
				new CriterionElementDialog(this, (Criterion) dmtnParent.getUserObject(), (CriterionElement) dmtnObj);

				treeModel.reload((TreeNode) treeCriteria.getSelectionPath().getLastPathComponent());
				treeCriteria.setSelectionPath(selectedPath);

				jtDistanceBlason.setModel(createTableModel());
				//generateSCNA_DBRow();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}
}