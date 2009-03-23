/*
 * Créer le 17 févr. 08 à 15:30:37 pour ConcoursJeunes
 *
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.Localisable;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.TargetPosition;
import org.concoursjeunes.event.FicheConcoursEvent;
import org.concoursjeunes.event.FicheConcoursListener;
import org.concoursjeunes.state.State;
import org.concoursjeunes.state.StateManager;
import org.concoursjeunes.state.StateSelector;
import org.jdesktop.swingx.JXSplitButton;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class GreffePane extends JPanel implements 
		FicheConcoursListener, CaretListener, ItemListener, TableModelListener, MouseListener, ActionListener {

	private JTable jtConcurrents = new JTable() {
		//  Returning the Class of each column will allow different
		//  renderers to be used based on Class
		@Override
        public Class<?> getColumnClass(int column)	{
			return getValueAt(0, column).getClass();
		}
	};
	private TableRowSorter<DefaultTableModel> sorter;
	
	@Localisable("greffepane.start")
	private JLabel jlDepart = new JLabel();
	@Localisable("greffepane.name")
	private JLabel jlNom = new JLabel();
	@Localisable("greffepane.firstname")
	private JLabel jlPrenom = new JLabel();
	@Localisable("greffepane.club")
	private JLabel jlClub = new JLabel();
	@Localisable("greffepane.licence")
	private JLabel jlLicence = new JLabel();
	private JTextField jtfDepart = new JTextField(new NumberDocument(false, false), "", 2); //$NON-NLS-1$
	private JTextField jtfNom = new JTextField(10);
	private JTextField jtfPrenom = new JTextField(10);
	private JTextField jtfClub = new JTextField(10);
	private JTextField jtfLicence = new JTextField(7);
	private JComboBox jcbPayee = new JComboBox();
	@StateSelector(name="200-listgreffe")
	@Localisable("greffepane.print")
	private JXSplitButton jbImpression = new JXSplitButton();
	@StateSelector(name="100-listalpha")
	@Localisable("greffepane.print.alpha")
	private JMenuItem jmiPrintAlpha = new JMenuItem();
	@StateSelector(name="200-listgreffe")
	@Localisable("greffepane.print.greffe")
	private JMenuItem jmiPrintGreffe = new JMenuItem();
	@StateSelector(name="300-listtarget")
	@Localisable("greffepane.print.target")
	private JMenuItem jmiPrintTarget = new JMenuItem();
	
	private FicheConcoursPane ficheConcoursPane;
	private Concurrent[] concurrents;
	private StateManager stateManager;
	
	public GreffePane(FicheConcoursPane ficheConcoursPane) {
		this.ficheConcoursPane = ficheConcoursPane;
		ficheConcoursPane.getFicheConcours().addFicheConcoursListener(this);
		
		//charge le gestionnaire d'etat
		stateManager = new StateManager();
		
		init();
		affectLibelle();
		completePanel();
	}
	
	private void init() {
		JPanel jpFiltre = new JPanel();
		JScrollPane jspListeConcurrents = new JScrollPane(jtConcurrents);
		
		jtfDepart.addCaretListener(this);
		jtfNom.addCaretListener(this);
		jtfPrenom.addCaretListener(this);
		jtfClub.addCaretListener(this);
		jtfLicence.addCaretListener(this);
		jcbPayee.addItem(ficheConcoursPane.getLocalisation().getResourceString("greffepane.paid.all")); //$NON-NLS-1$
		jcbPayee.addItem(ficheConcoursPane.getLocalisation().getResourceString("greffepane.paid.true")); //$NON-NLS-1$
		jcbPayee.addItem(ficheConcoursPane.getLocalisation().getResourceString("greffepane.paid.false")); //$NON-NLS-1$
		jcbPayee.addItemListener(this);
		jtConcurrents.addMouseListener(this);
		jbImpression.addActionListener(this);
		jbImpression.setName("jbImpression"); //$NON-NLS-1$
		jmiPrintAlpha.addActionListener(this);
		jmiPrintAlpha.setName("jmiPrintAlpha"); //$NON-NLS-1$
		jmiPrintGreffe.addActionListener(this);
		jmiPrintGreffe.setName("jmiPrintGreffe"); //$NON-NLS-1$
		jmiPrintTarget.addActionListener(this);
		jmiPrintTarget.setName("jmiPrintTarget"); //$NON-NLS-1$
		
		JPopupMenu jpm = new JPopupMenu();
		jpm.add(jmiPrintAlpha);
		jpm.add(jmiPrintGreffe);
		jpm.add(jmiPrintTarget);
		jbImpression.setDropDownMenu(jpm);
		
		jpFiltre.add(jlDepart);
		jpFiltre.add(jtfDepart);
		jpFiltre.add(jlNom);
		jpFiltre.add(jtfNom);
		jpFiltre.add(jlPrenom);
		jpFiltre.add(jtfPrenom);
		jpFiltre.add(jlClub);
		jpFiltre.add(jtfClub);
		jpFiltre.add(jlLicence);
		jpFiltre.add(jtfLicence);
		jpFiltre.add(jcbPayee);
		jpFiltre.add(jbImpression);
		
		//jtConcurrents.getColumnModel().getColumn(4).setPreferredWidth(200);
		
		setLayout(new BorderLayout());
		add(jpFiltre, BorderLayout.NORTH);
		add(jspListeConcurrents, BorderLayout.CENTER);
	}
	
	private void affectLibelle() {
		AppUtilities.localize(this, ficheConcoursPane.getLocalisation());
	}
	
	private void completePanel() {
		DefaultTableModel dtm = createTableModel();
		
		if(ficheConcoursPane != null) {
			ConcurrentList concurrentList = ficheConcoursPane.getFicheConcours().getConcurrentList();
			
			concurrents = concurrentList.list(-1);
			
			for(Concurrent concurrent : concurrents) {
				String categorie = ""; //$NON-NLS-1$
				for (Criterion key : ficheConcoursPane.getFicheConcours().getParametre().getReglement().getListCriteria()) {
					CriterionElement criterionElement = concurrent.getCriteriaSet().getCriterionElement(key);
					if (criterionElement != null)
						categorie += criterionElement.getCode();
				}
				
				Object[] row = new Object[] {
						concurrent.getDepart() + 1,
						concurrent.getNomArcher(),
						concurrent.getPrenomArcher(),
						concurrent.getClub().getVille(),
						concurrent.getNumLicenceArcher(),
						categorie,
						new TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString(),
						concurrent.getInscription() == Concurrent.PAYEE,
						concurrent.isCertificat(),
						concurrent.isPresence()
				};
				
				dtm.addRow(row);
			}
		}
		
		List<? extends SortKey> sortKeys = null;
		if(sorter != null) {
			sortKeys = sorter.getSortKeys();
		}
		
		sorter = new TableRowSorter<DefaultTableModel>(dtm);
		if(sortKeys != null)
			sorter.setSortKeys(sortKeys);
		jtConcurrents.setModel(dtm);
		jtConcurrents.setRowSorter(sorter);
		jtConcurrents.getColumnModel().getColumn(0).setMaxWidth(40);
		//jtConcurrents.getColumnModel().getColumn(1).setPreferredWidth(400);
		//jtConcurrents.getColumnModel().getColumn(2).setPreferredWidth(400);
		jtConcurrents.getColumnModel().getColumn(4).setMaxWidth(100);
		jtConcurrents.getColumnModel().getColumn(5).setMaxWidth(70);
		jtConcurrents.getColumnModel().getColumn(6).setMaxWidth(40);
		jtConcurrents.getColumnModel().getColumn(7).setMaxWidth(40);
		jtConcurrents.getColumnModel().getColumn(8).setMaxWidth(70);
		jtConcurrents.getColumnModel().getColumn(9).setMaxWidth(70);
		
		updateTableFilter();
	}
	
	private DefaultTableModel createTableModel() {
		DefaultTableModel dtm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				if(col > 5)
					return true;
				return false;
			}
		};
		
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.start")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.name")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.firstname")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.club")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.licence")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.category")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.target")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.paid")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.medic")); //$NON-NLS-1$
		dtm.addColumn(ficheConcoursPane.getLocalisation().getResourceString("greffepane.presence")); //$NON-NLS-1$
		
		dtm.addTableModelListener(this);
		
		return dtm;
	}
	
	private void updateTableFilter() {
		if(jtConcurrents.getModel().getRowCount() > 0) {
			List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<RowFilter<DefaultTableModel, Integer>>();
			
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("(?i)" + jtfDepart.getText(), 0)); //$NON-NLS-1$
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("^(?i)" + jtfNom.getText().replace("*", ".*"), 1)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("^(?i)" + jtfPrenom.getText().replace("*", ".*"), 2)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("^(?i)" + jtfClub.getText().replace("*", ".*"), 3)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("^(?i)" + jtfLicence.getText().replace("*", ".*"), 4)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			filters.add(new RowFilter<DefaultTableModel, Integer>() {
				@Override
				public boolean include(
						javax.swing.RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
					DefaultTableModel model = entry.getModel();
					if(jcbPayee.getSelectedIndex() == 0
							|| (jcbPayee.getSelectedIndex() == 1 && (Boolean)model.getValueAt(entry.getIdentifier(), 7))
							|| (jcbPayee.getSelectedIndex() == 2 && !(Boolean)model.getValueAt(entry.getIdentifier(), 7)))
						return true;
					return false;
				}
				
			});
			
			sorter.setRowFilter(RowFilter.<DefaultTableModel, Integer>andFilter((Iterable<RowFilter<DefaultTableModel, Integer>>)filters));
			//jtConcurrents.setRowSorter(sorter);
		}
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.event.FicheConcoursListener#listConcurrentChanged(org.concoursjeunes.event.FicheConcoursEvent)
	 */
	@Override
	public void listConcurrentChanged(FicheConcoursEvent e) {
		completePanel();
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.event.FicheConcoursListener#pasDeTirChanged(org.concoursjeunes.event.FicheConcoursEvent)
	 */
	@Override
	public void pasDeTirChanged(FicheConcoursEvent e) {
		completePanel();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		updateTableFilter();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getType() != TableModelEvent.UPDATE)
			return;
		
		int changedRow = e.getFirstRow();
		DefaultTableModel model = (DefaultTableModel)jtConcurrents.getModel();
		if(changedRow > -1 && model.getRowCount() > 0 && concurrents.length > 0 && changedRow < model.getRowCount() && changedRow < concurrents.length) {
			concurrents[changedRow].setCertificat((Boolean)jtConcurrents.getModel().getValueAt(changedRow, 8));
			concurrents[changedRow].setInscription((Boolean)jtConcurrents.getModel().getValueAt(changedRow, 7) ? Concurrent.PAYEE : Concurrent.RESERVEE);
			concurrents[changedRow].setPresence((Boolean)jtConcurrents.getModel().getValueAt(changedRow, 9));
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			ficheConcoursPane.openConcurrentDialog(concurrents[jtConcurrents.convertRowIndexToModel(jtConcurrents.getSelectedRow())]);
			completePanel();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == jbImpression || source == jmiPrintAlpha || source == jmiPrintGreffe || source == jmiPrintTarget) {
			ficheConcoursPane.switchToEditPane();
			try {
				String stateName = this.getClass().getDeclaredField(((AbstractButton)source).getName()).getAnnotation(StateSelector.class).name();
				State state = stateManager.getState(stateName);
				if(state != null)
					ficheConcoursPane.prepareState(state);
			} catch (SecurityException ex) {
				ex.printStackTrace();
			} catch (NoSuchFieldException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		updateTableFilter();
	}
}
