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
package org.concoursjeunes.ui.dialog;

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.awt.BorderLayout;
import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.Localisable;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.StringUtils;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.commons.ui.AJTree;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.ajdeveloppement.commons.ui.ToolTipHeader;
import org.concoursjeunes.Blason;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.localisable.CriteriaSetLibelle;

/**
 * @author Aurélien JEOFFRAY
 * 
 */
public class ReglementDialog extends JDialog implements ActionListener, MouseListener, TableModelListener {
	private JFrame parentframe;
	private Profile profile;
	private AjResourcesReader localisation;
	private Reglement reglement;

	@Localisable("reglement.name")
	private JLabel jlReglementName = new JLabel();

	@Localisable("reglement.serie")
	private JLabel jlNbSerie = new JLabel();
	@Localisable("reglement.voleeparserie")
	private JLabel jlNbVoleeParSerie = new JLabel();
	@Localisable("reglement.flecheparvolee")
	private JLabel jlNbFlecheParVolee = new JLabel();
	@Localisable("reglement.membresmax")
	private JLabel jlNbMembresEquipe = new JLabel();
	@Localisable("reglement.selectionmax")
	private JLabel jlNbMembresRetenu = new JLabel();
	@Localisable("reglement.departages")
	private JLabel jlDepartages = new JLabel();
	@Localisable("reglement.departagesinfo")
	private JLabel jlDepartagesInfo = new JLabel();
	@Localisable("reglement.official")
	private JCheckBox jcbOfficialReglement = new JCheckBox();

	private JLabel jlNbDB = new JLabel();
	@Localisable(value="",tooltip="reglement.addcriteria")
	private JButton jbAddCriteria = new JButton();
	@Localisable(value="",tooltip="reglement.addcriteriamember")
	private JButton jbAddCriteriaMember = new JButton();
	@Localisable(value="",tooltip="reglement.upelement")
	private JButton jbUpElement = new JButton();
	@Localisable(value="",tooltip="reglement.downelement")
	private JButton jbDownElement = new JButton();
	@Localisable(value="",tooltip="reglement.removeelement")
	private JButton jbRemoveElement = new JButton();
	private DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("criteres"); //$NON-NLS-1$
	private DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
	private AJTree treeCriteria = new AJTree(treeModel);

	private JTextField jtfReglementName = new JTextField(15);
	private JTextField jtfNbSerie = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbVoleeParSerie = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbFlecheParVolee = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbMembresEquipe = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbMembresRetenu = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfDepartages = new JTextField(15);
	
	private JTable jtCriteriaSet = new JTable() {
	//  Returning the Class of each column will allow different
		//  renderers to be used based on Class
		@Override
        public Class<?> getColumnClass(int column)	{
			if(column == 0)
				return Boolean.class;
			return CriteriaSet.class;
		}
	};
	private JComboBox jcbCriteriaSet = new JComboBox(); 

	private AJList<DistancesBlasonsSet> ajlDistancesBlasons = new AJList<DistancesBlasonsSet>();
	private JScrollPane jspDistanceBlason = new JScrollPane(ajlDistancesBlasons);

	@Localisable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();

	private boolean verrou = false;

	public ReglementDialog(JFrame parentframe, Reglement reglement, Profile profile) {
		super(parentframe, true);

		this.parentframe = parentframe;
		this.reglement = reglement;
		this.profile = profile;
		this.localisation = profile.getLocalisation();

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
		
		jbAddCriteria.setIcon(new ImageIcon(staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + staticParameters.getResourceString("file.icon.addcriteria"))); //$NON-NLS-1$
		jbAddCriteriaMember.setIcon(new ImageIcon(staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + staticParameters.getResourceString("file.icon.addcriteriamember"))); //$NON-NLS-1$
		jbUpElement.setIcon(new ImageIcon(staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + staticParameters.getResourceString("file.icon.upelement"))); //$NON-NLS-1$
		jbDownElement.setIcon(new ImageIcon(staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + staticParameters.getResourceString("file.icon.downelement"))); //$NON-NLS-1$
		jbRemoveElement.setIcon(new ImageIcon(staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + staticParameters.getResourceString("file.icon.removeelement"))); //$NON-NLS-1$

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(localisation.getResourceString("reglement.general.title"), initGeneral()); //$NON-NLS-1$
		tabbedPane.addTab(localisation.getResourceString("reglement.criteres.title"), initCriteria()); //$NON-NLS-1$
		tabbedPane.addTab(localisation.getResourceString("reglement.surclassement.title"), initCriteriaSet()); //$NON-NLS-1$
		tabbedPane.addTab(localisation.getResourceString("reglement.categories.title"), initDistancesEtBlasons()); //$NON-NLS-1$

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
		
		ajlDistancesBlasons.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof DistancesBlasonsSet) {
					DistancesBlasonsSet dbSet = (DistancesBlasonsSet)value;
					
					String formattedString = "<html>"; //$NON-NLS-1$
					
					DistancesEtBlason fisrtDB = dbSet.get().get(0);
					formattedString += new CriteriaSetLibelle(fisrtDB.getCriteriaSet(),localisation).toString();
					String distances = ""; //$NON-NLS-1$
					for(int dist : fisrtDB.getDistance()) {
						if(!distances.isEmpty())
							distances += ", "; //$NON-NLS-1$
						distances += dist + "m"; //$NON-NLS-1$
					}
					formattedString += "<br><span style=\"font-weight: normal;\">"; //$NON-NLS-1$
					formattedString += "&nbsp;&nbsp;&nbsp;" + localisation.getResourceString("reglement.distance") + ": " + distances + "<br>";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					formattedString += "&nbsp;&nbsp;&nbsp;" + localisation.getResourceString("reglement.blason") + ": " + fisrtDB.getTargetFace().getName() + "<br>";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					if(dbSet.get().size() > 1) {
						String blasonsalt = ""; //$NON-NLS-1$
						boolean first = true;
						for(DistancesEtBlason db : dbSet.get()) {
							if(first) {
								first = false;
								continue;
							}
							if(!blasonsalt.isEmpty())
								blasonsalt += ", "; //$NON-NLS-1$
							blasonsalt += db.getTargetFace().getName();
						}
						formattedString += "&nbsp;&nbsp;&nbsp;" + localisation.getResourceString("reglement.blasonsalt") + " " + blasonsalt; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					} else {
						formattedString += "&nbsp;&nbsp;&nbsp;" + localisation.getResourceString("reglement.blasonsaltnull"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					formattedString += "</span></html>";  //$NON-NLS-1$
					
					value = formattedString;
				}

				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		
		gridbagComposer.setParentPanel(panel);

		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		gridbagComposer.addComponentIntoGrid(jlReglementName, c);
		gridbagComposer.addComponentIntoGrid(jtfReglementName, c);
		c.gridy++;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(Box.createVerticalStrut(30), c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(new JSeparator(), c);
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlNbSerie, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfNbSerie, c);
		c.gridy++;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlNbVoleeParSerie, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfNbVoleeParSerie, c);
		c.gridy++;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlNbFlecheParVolee, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfNbFlecheParVolee, c);
		c.gridy++;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlNbMembresEquipe, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfNbMembresEquipe, c);
		c.gridy++;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlNbMembresRetenu, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfNbMembresRetenu, c);
		c.gridy++;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlDepartages, c);
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfDepartages, c);
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jlDepartagesInfo, c);
		c.gridy++;
		c.gridx = GridBagConstraints.RELATIVE;
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
		treeCriteria.setKeepExpansionState(true);
		treeCriteria.setCellRenderer(new DefaultTreeCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
			 */
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				if(value instanceof DefaultMutableTreeNode) {
					Object o = ((DefaultMutableTreeNode)value).getUserObject();
					if(o instanceof Criterion)
						value = ((Criterion)o).getLibelle();
					else if(o instanceof CriterionElement)
						value = ((CriterionElement)o).getLibelle();
				}
					
				return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
						row, hasFocus);
			}
		});

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
	
	private JPanel initCriteriaSet() {
		JPanel jpCriteriaSet = new JPanel();
		
		jtCriteriaSet.setDefaultRenderer(CriteriaSet.class, new DefaultTableCellRenderer() {
	    	@Override
	    	public Component getTableCellRendererComponent(JTable table, Object value,
	    			boolean isSelected, boolean hasFocus, int row, int column) {
	    		if(value instanceof CriteriaSet)
	    			value = new CriteriaSetLibelle((CriteriaSet)value, localisation);
	    		return super.getTableCellRendererComponent(table, 
	    				value, isSelected, hasFocus, row, column);
	    	}
	    });
		jcbCriteriaSet.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof CriteriaSet)
					value = new CriteriaSetLibelle((CriteriaSet)value, localisation);
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		
		jpCriteriaSet.setLayout(new BorderLayout());
		jpCriteriaSet.add(BorderLayout.CENTER, new JScrollPane(jtCriteriaSet));
		
		return jpCriteriaSet;
	}

	private JPanel initDistancesEtBlasons() {
		JPanel jpConcours = new JPanel();

		jspDistanceBlason.setPreferredSize(new Dimension(400, 250));
		ajlDistancesBlasons.addMouseListener(this);
		//jtDistanceBlason.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//jtDistanceBlason.setPreferredScrollableViewportSize(new Dimension(450, 200));
		/*jtDistanceBlason.setDefaultRenderer(CriteriaSet.class, new DefaultTableCellRenderer() {
	    	@Override
	    	public Component getTableCellRendererComponent(JTable table, Object value,
	    			boolean isSelected, boolean hasFocus, int row, int column) {
	    		return super.getTableCellRendererComponent(table, new CriteriaSetLibelle((CriteriaSet)value, localisation), isSelected, hasFocus, row, column);
	    	}
	    });
		jtDistanceBlason.setDefaultRenderer(Blason.class, new DefaultTableCellRenderer() {
	    	@Override
	    	public Component getTableCellRendererComponent(JTable table, Object value,
	    			boolean isSelected, boolean hasFocus, int row, int column) {
	    		if(value == null)
	    			value = " "; //$NON-NLS-1$
	    		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    	}
	    });*/

		jpConcours.setLayout(new BorderLayout());
		jpConcours.add(BorderLayout.NORTH, jlNbDB);
		jpConcours.add(BorderLayout.CENTER, jspDistanceBlason);

		return jpConcours;
	}

	private void affectLibelle() {
		setTitle(localisation.getResourceString("reglement.titre")); //$NON-NLS-1$
		
		AppUtilities.localize(this, localisation);	
	}

	private void completePanel() {
		completeGeneral();
		completeCriteria();
		completeCriteriaSet();
		completeDistancesEtBlasons();
	}

	private void completeGeneral() {
		if (reglement.isOfficialReglement())
			verrou = true;

		if(verrou) {
			jtfReglementName.setEditable(false);
			
			jtfNbSerie.setEditable(false);
			jtfNbVoleeParSerie.setEditable(false);
			jtfNbFlecheParVolee.setEditable(false);
			jtfNbMembresEquipe.setEditable(false);
			jtfNbMembresRetenu.setEditable(false);
			jtfDepartages.setEditable(false);

			jbAddCriteria.setEnabled(false);
			jbAddCriteriaMember.setEnabled(false);
			jbDownElement.setEnabled(false);
			jbUpElement.setEnabled(false);
			jbRemoveElement.setEnabled(false);
		}

		jtfReglementName.setText(reglement.getDisplayName()); 

		jtfNbSerie.setText(reglement.getNbSerie() + ""); //$NON-NLS-1$
		jtfNbVoleeParSerie.setText(reglement.getNbVoleeParSerie() + ""); //$NON-NLS-1$
		jtfNbFlecheParVolee.setText(reglement.getNbFlecheParVolee() + ""); //$NON-NLS-1$
		jtfNbMembresEquipe.setText(reglement.getNbMembresEquipe() + ""); //$NON-NLS-1$
		jtfNbMembresRetenu.setText(reglement.getNbMembresRetenu() + ""); //$NON-NLS-1$
		
		jtfDepartages.setText(""); //$NON-NLS-1$
		for(String departage : reglement.getTie()) {
			String tmp = "";  //$NON-NLS-1$
			if(!jtfDepartages.getText().isEmpty())
				tmp = jtfDepartages.getText() + ","; //$NON-NLS-1$
			jtfDepartages.setText(tmp + departage);
		}

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
	}
	
	private void completeCriteriaSet() {
		if(reglement != null) {
			jtCriteriaSet.setModel(createCriteriaSetTablemodel());
			jtCriteriaSet.getColumnModel().getColumn(0).setMaxWidth(20);
			
			ToolTipHeader header = new ToolTipHeader(jtCriteriaSet.getColumnModel());
		    header.setToolTipStrings(new String[] { 
		    		localisation.getResourceString("reglement.surclassement.enable"), "", ""} );  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		    jtCriteriaSet.setTableHeader(header);
			
		    if(!verrou) {
				TableColumn cH = jtCriteriaSet.getColumnModel().getColumn(2);
				cH.setCellEditor(new DefaultCellEditor(jcbCriteriaSet) {
					/* (non-Javadoc)
					 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
					 */
					@Override
					public Component getTableCellEditorComponent(JTable table,
							Object value, boolean isSelected, int row, int column) {
						
						jcbCriteriaSet.removeAllItems();
						jcbCriteriaSet.addItem(" "); //$NON-NLS-1$
						CriteriaSet tmpCs = (CriteriaSet)table.getValueAt(row, 1);
						
						if(!reglement.getSurclassement().containsValue(tmpCs)) {
							for(CriteriaSet cs : CriteriaSet.listCriteriaSet(reglement, reglement.getClassementFilter())) {
								if(!tmpCs.equals(cs) && !reglement.getSurclassement().containsKey(cs))
									jcbCriteriaSet.addItem(cs);
							}
						}
	
						return super.getTableCellEditorComponent(table, value, isSelected, row, column);
					}
				});
		    }
		}
	}

	private void completeDistancesEtBlasons() {
		if (reglement == null)
			return;
		
		//s'assure que le nombre de serie est correct
		reglement.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));
		
		CriteriaSet[] differentiationCriteria = reglement.getValidPlacementCriteriaSet();
		
		ajlDistancesBlasons.clear();
		for (CriteriaSet plCS : differentiationCriteria) {
			if (reglement.getDistancesEtBlasonFor(plCS).size() > 0) {
				ajlDistancesBlasons.add(new DistancesBlasonsSet(reglement.getDistancesEtBlasonFor(plCS)));
			} else {
				DistancesEtBlason newDb = new DistancesEtBlason();
				newDb.setCriteriaSet(plCS);
				newDb.setDistance(new int[reglement.getNbSerie()]);
				Blason defaultBlason = new Blason();
				try {
					List<Blason> availableTargetFace = Blason.listAvailableTargetFace();
					if(availableTargetFace.size() > 0)
						defaultBlason = availableTargetFace.get(0);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				newDb.setTargetFace(defaultBlason);
				
				ajlDistancesBlasons.add(new DistancesBlasonsSet(Collections.singletonList(newDb)));
			}
		}
	}
	
	private DefaultTableModel createCriteriaSetTablemodel() {
		DefaultTableModel dtm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				if (verrou || col == 1 || reglement.isOfficialReglement())
					return false;
				return true;
			}
		};
		
		dtm.addColumn(localisation.getResourceString("reglement.surclassement.enable")); //$NON-NLS-1$
		dtm.addColumn(localisation.getResourceString("reglement.surclassement.categories")); //$NON-NLS-1$
		dtm.addColumn(localisation.getResourceString("reglement.surclassement.surclassement")); //$NON-NLS-1$
		dtm.addTableModelListener(this);
		
		//on liste toutes les catégorie de classement
		CriteriaSet[] differentiationCriteria = CriteriaSet.listCriteriaSet(reglement, reglement.getClassementFilter());
		for (int i = 0; i < differentiationCriteria.length; i++) {
			CriteriaSet criteriaSet = reglement.getSurclassement().get(differentiationCriteria[i]);
			boolean enable = true;
			if(criteriaSet == null && reglement.getSurclassement().containsKey(differentiationCriteria[i]))
				enable = false;
			dtm.addRow(new Object[] {
					enable,
					differentiationCriteria[i],
					criteriaSet
				});
		}
		
		return dtm;
	}

	private void reloadTablesModel() {
		completeCriteriaSet();
		completeDistancesEtBlasons();
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
	public boolean isVerrou() {
		return verrou;
	}

	/**
	 * @param verrou
	 *            verrou à définir
	 */
	public void setVerrou(boolean verrou) {
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
			reglement.setDisplayName(jtfReglementName.getText());
			reglement.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));
			reglement.setNbVoleeParSerie(Integer.parseInt(jtfNbVoleeParSerie.getText()));
			reglement.setNbFlecheParVolee(Integer.parseInt(jtfNbFlecheParVolee.getText()));
			reglement.setNbMembresEquipe(Integer.parseInt(jtfNbMembresEquipe.getText()));
			reglement.setNbMembresRetenu(Integer.parseInt(jtfNbMembresRetenu.getText()));
			
			reglement.getTie().clear();
			for(String departage : StringUtils.tokenize(jtfDepartages.getText(), ",")) //$NON-NLS-1$
				reglement.getTie().add(departage);
			
			List<DistancesEtBlason> activeDB = new ArrayList<DistancesEtBlason>();
			for (DistancesBlasonsSet dbSet : ajlDistancesBlasons.getAllElements()) {
				for(DistancesEtBlason db : dbSet.get()) {
					//reglement.addDistancesEtBlason(db);
					activeDB.add(db);
				}
			}

			reglement.getListDistancesEtBlason().clear();
			reglement.getListDistancesEtBlason().addAll(activeDB);

			reglement.setOfficialReglement(jcbOfficialReglement.isSelected());
			
			setVisible(false);
		} else if (source == jbAnnuler) {
			reglement = null;
			setVisible(false);
		} else if (source == jbAddCriteria) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getPathForRow(0).getLastPathComponent();
			if (dmtn != null) {
				CriterionDialog cd = new CriterionDialog(this, localisation);
				cd.setLock(verrou);
				if (cd.showCriterionDialog() != null) {
					cd.getCriterion().setReglement(reglement);
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

					if (!(criterion.isPlacement() && verrou)) {
						CriterionElementDialog cpd = new CriterionElementDialog(this, criterion, localisation);

						if (cpd.getCriterionIndividu() != null) {
							DefaultMutableTreeNode dmtnIndiv = new DefaultMutableTreeNode(cpd.getCriterionIndividu());

							dmtn.add(dmtnIndiv);

							treeModel.reload(dmtn);

							reloadTablesModel();
						}
					} else {
						JOptionPane.showMessageDialog(this, localisation.getResourceString("reglement.message.criteria.noelement"), //$NON-NLS-1$
								localisation.getResourceString("reglement.message.criteria.noelement.title"), //$NON-NLS-1$
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
					
					treeCriteria.setSelectionPath(new TreePath(dmtn.getPath()));

					reloadTablesModel();
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
					
					treeCriteria.setSelectionPath(new TreePath(dmtn.getPath()));

					reloadTablesModel();
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
					
					treeCriteria.setSelectionPath(new TreePath(dmtn.getPath()));

					reloadTablesModel();
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
					
					treeCriteria.setSelectionPath(new TreePath(dmtn.getPath()));

					reloadTablesModel();
				}
			}
		} else if (source == jbRemoveElement) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
			if (dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					reglement.getListCriteria().remove(dmtnObj);

					treeModel.removeNodeFromParent(dmtn);

					reloadTablesModel();
				} else if (dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion) dmtnParent.getUserObject();
					curCriterion.getCriterionElements().remove(dmtnObj);

					treeModel.removeNodeFromParent(dmtn);

					reloadTablesModel();
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == treeCriteria) {
			if (e.getClickCount() == 2) {
				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeCriteria.getLastSelectedPathComponent();
				if(dmtn == null)
					return;
				Object dmtnObj = dmtn.getUserObject();
				if (dmtnObj instanceof Criterion) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					CriterionDialog criterionDialog = new CriterionDialog(this, (Criterion) dmtnObj, localisation);
					criterionDialog.setLock(verrou);
					criterionDialog.showCriterionDialog();
	
					treeModel.reload((TreeNode) treeCriteria.getSelectionPath().getLastPathComponent());
					treeCriteria.setSelectionPath(selectedPath);
	
					reloadTablesModel();
				} else if (dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
					new CriterionElementDialog(this, (Criterion) dmtnParent.getUserObject(), (CriterionElement) dmtnObj, localisation);
	
					treeModel.reload((TreeNode) treeCriteria.getSelectionPath().getLastPathComponent());
					treeCriteria.setSelectionPath(selectedPath);
	
					reloadTablesModel();
				}
			}
		} else if(e.getSource() == ajlDistancesBlasons) {
			if (e.getClickCount() == 2 && !verrou) {
				DistancesBlasonsDialog dbDialog = new DistancesBlasonsDialog(parentframe, profile);
				dbDialog.showDistancesBlasonsDialog(((DistancesBlasonsSet)ajlDistancesBlasons.getSelectedValue()).get());
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

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		DefaultTableModel dtm = (DefaultTableModel)e.getSource();
		CriteriaSet criteriaSet = (CriteriaSet)dtm.getValueAt(e.getFirstRow(), 1);
		boolean active = (Boolean)dtm.getValueAt(e.getFirstRow(), 0);
		Object oSurclasse = dtm.getValueAt(e.getFirstRow(), 2);
		
		if(active && oSurclasse instanceof CriteriaSet)
			reglement.getSurclassement().put(criteriaSet, (CriteriaSet)oSurclasse);
		else if(!active)
			reglement.getSurclassement().put(criteriaSet, null);
		else
			reglement.getSurclassement().remove(criteriaSet);
		
		completeDistancesEtBlasons();
	}

	private class DistancesBlasonsSet {
		private List<DistancesEtBlason> dbList = new ArrayList<DistancesEtBlason>();
		
		/*public DistancesBlasonsSet() {
		}*/
		
		public DistancesBlasonsSet(List<DistancesEtBlason> dbList) {
			set(dbList);
		}
		
		public List<DistancesEtBlason> get() {
			return dbList;
		}
		
		public void set(List<DistancesEtBlason> dbList) {
			if(!(dbList instanceof ArrayList<?>))
				dbList = new ArrayList<DistancesEtBlason>(dbList);
			this.dbList = dbList;
		}
		
		/*public void add(DistancesEtBlason distancesEtBlason) {
			dbList.add(distancesEtBlason);
		}*/
		
		/*public void remove(DistancesEtBlason distancesEtBlason) {
			dbList.remove(distancesEtBlason);
		}*/
	}
}