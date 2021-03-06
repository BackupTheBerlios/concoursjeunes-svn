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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.concoursjeunes.*;
import org.jdesktop.swingx.JXBusyLabel;

import ajinteractive.standard.ui.GridbagComposer;

/**
 * La liste des personnes présente daans la base des archers
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 */
public class ConcurrentListDialog extends JDialog implements ActionListener, MouseListener, CaretListener {
	private ArchersTableModel dtm = null;
	private TableRowSorter<ArchersTableModel> sorter;

	private final Reglement reglement;

	private JLabel jlFilterLicence = null;
	private JTextField jtfFilterLicence = null;
	private JLabel jlFilterNom = null;
	private JTextField jtfFilterNom = null;
	private JLabel jlFilterClub = null;
	private JTextField jtfFilterClub = null;
	private final JButton moreResult = new JButton("+"); //$NON-NLS-1$
	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JPanel jPanel = null;
	private JXBusyLabel loading = new JXBusyLabel();
	private JLabel loadingProgressLabel = new JLabel();
	private JButton jbValider = null;
	private JButton jbAnnuler = null;

	private boolean isValider = false;

	/**
	 * This is the default constructor
	 * 
	 * @param parentframe -
	 *            la fenetre principal de l'application (pour le point modal)
	 */
	public ConcurrentListDialog(Window parentframe, Reglement reglement, Archer filter) {
		super(parentframe, ApplicationCore.ajrLibelle.getResourceString("concurrent.nouveau.titre"), ModalityType.APPLICATION_MODAL); //$NON-NLS-1$
		this.reglement = reglement;

		if (filter == null && ApplicationCore.getConfiguration().getClub().getAgrement().length() > 0) {
			filter = new Archer();
			Entite entite = new Entite();
			entite.setAgrement(ApplicationCore.getConfiguration().getClub().getAgrement().substring(0, 2) + "%"); //$NON-NLS-1$
			filter.setClub(entite);
		}
		//dtm = new ArchersTableModel(filter);
		dtm = new ArchersTableModel();
		ArchersTableLoader loader = new ArchersTableLoader(filter);
		loadingProgress(loader);
		loader.execute();
		loading.setBusy(true);

		init();

		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void init() {
		JPanel jContentPane = new JPanel();
		JPanel jpEntete = new JPanel();

		jlFilterLicence = new JLabel();
		jlFilterNom = new JLabel();
		jlFilterClub = new JLabel();

		moreResult.addActionListener(this);
		moreResult.setToolTipText(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.moreresult")); //$NON-NLS-1$

		jpEntete.add(jlFilterLicence);
		jpEntete.add(getJtfFilterLicence());
		jpEntete.add(jlFilterNom);
		jpEntete.add(getJtfFilterNom());
		jpEntete.add(jlFilterClub);
		jpEntete.add(getJtfFilterClub());
		jpEntete.add(moreResult);

		jContentPane.setLayout(new BorderLayout());

		jContentPane.add(jpEntete, BorderLayout.NORTH);
		jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		jContentPane.add(getJPanel(), BorderLayout.SOUTH);

		setContentPane(jContentPane);

		getRootPane().setDefaultButton(jbValider);

		affectLibelle();
	}

	private void affectLibelle() {
		jlFilterLicence.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.nouveau.licence")); //$NON-NLS-1$
		jlFilterNom.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.nouveau.nom")); //$NON-NLS-1$
		jlFilterClub.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.nouveau.club")); //$NON-NLS-1$
	}

	/**
	 * This method initializes jtfFilterLicence
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJtfFilterLicence() {
		if (jtfFilterLicence == null) {
			jtfFilterLicence = new JTextField();
			jtfFilterLicence.setColumns(10);
			jtfFilterLicence.addCaretListener(this);
		}
		return jtfFilterLicence;
	}

	/**
	 * This method initializes jtfFilterNom
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJtfFilterNom() {
		if (jtfFilterNom == null) {
			jtfFilterNom = new JTextField();
			jtfFilterNom.setColumns(10);
			jtfFilterNom.addCaretListener(this);
		}
		return jtfFilterNom;
	}

	/**
	 * This method initializes jtfFilterClub
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJtfFilterClub() {
		if (jtfFilterClub == null) {
			jtfFilterClub = new JTextField();
			jtfFilterClub.setColumns(10);
			jtfFilterClub.addCaretListener(this);
		}
		return jtfFilterClub;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (this.jTable == null) {

			this.sorter = new TableRowSorter<ArchersTableModel>(dtm);
			this.jTable = new JTable(dtm);
			this.jTable.setAutoCreateRowSorter(true);
			// this.sorter.setTableHeader(this.jTable.getTableHeader());
			this.jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			this.jTable.setPreferredScrollableViewportSize(new Dimension(640, 480));
			this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.jTable.addMouseListener(this);
			this.jTable.getRowSorter().toggleSortOrder(1);

			TableColumn column = jTable.getColumnModel().getColumn(0);
			column.setPreferredWidth(65);
			column = jTable.getColumnModel().getColumn(3);
			column.setPreferredWidth(200);
			column = jTable.getColumnModel().getColumn(4);
			column.setPreferredWidth(60);
			column = jTable.getColumnModel().getColumn(5);
			column.setPreferredWidth(50);
		}
		return this.jTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (this.jScrollPane == null) {
			this.jScrollPane = new JScrollPane();
			this.jScrollPane.setViewportView(getJTable());
		}
		return this.jScrollPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (this.jPanel == null) {
			this.jPanel = new JPanel();
			
			GridBagConstraints c = new GridBagConstraints();
			GridbagComposer gridbagComposer = new GridbagComposer();
					
			gridbagComposer.setParentPanel(this.jPanel);
			c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(loading, c);
			c.insets = new Insets(0, 5, 0, 0);
			gridbagComposer.addComponentIntoGrid(loadingProgressLabel, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.insets = new Insets(0, 0, 0, 0);
			gridbagComposer.addComponentIntoGrid(Box.createHorizontalGlue(), c);
			c.anchor = GridBagConstraints.EAST;
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0;
			gridbagComposer.addComponentIntoGrid(getJButton(), c);
			gridbagComposer.addComponentIntoGrid(getJButton1(), c);
		}
		return this.jPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (this.jbValider == null) {
			this.jbValider = new JButton();
			this.jbValider.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
			this.jbValider.setActionCommand("bouton.valider"); //$NON-NLS-1$
			this.jbValider.addActionListener(this);
		}
		return this.jbValider;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (this.jbAnnuler == null) {
			this.jbAnnuler = new JButton();
			this.jbAnnuler.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
			this.jbAnnuler.setActionCommand("bouton.annuler"); //$NON-NLS-1$
			this.jbAnnuler.addActionListener(this);
		}
		return this.jbAnnuler;
	}
	
	private void loadingProgress(ArchersTableLoader loader) {
		loader.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals("progress")) { //$NON-NLS-1$
                    int progress = ((Integer) event.getNewValue()).intValue();
                    //progressBar.setValue(progress);
                    loadingProgressLabel.setText(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.loading", progress)); //$NON-NLS-1$

                    if (progress == 100) {
                    	loading.setBusy(false);
                    	 loadingProgressLabel.setText(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.loadingended")); //$NON-NLS-1$
                    }
                }
            }
        });
	}

	public void setFilter(Archer filter) {
		if(filter == null)
			return;
		String numLicence = filter.getNumLicenceArcher().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String nom = filter.getNomArcher().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String club = filter.getClub().getNom().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (!numLicence.isEmpty()) {
			jtfFilterLicence.setText(numLicence);
			sorter.setRowFilter(RowFilter.regexFilter("^" + numLicence.toUpperCase(), 0)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
		}
		if (!nom.isEmpty()) {
			jtfFilterNom.setText(nom);
			sorter.setRowFilter(RowFilter.regexFilter("^" + nom.toUpperCase(), 1)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
		}
		if (!club.isEmpty()) {
			jtfFilterClub.setText(club);
			sorter.setRowFilter(RowFilter.regexFilter(club.toUpperCase(), 3));
			jTable.setRowSorter(sorter);
		}
	}
	/**
	 * initialise un nouveau concurrent
	 * 
	 * @param concurrent -
	 *            l'objet concurrent à initialiser
	 */
	public void initConcurrent(Concurrent concurrent) {

		if (dtm != null) {
			int rowIndex = jTable.convertRowIndexToModel(jTable.getSelectedRow());

			concurrent.setNumLicenceArcher(dtm.getConcurrentAtRow(rowIndex).getNumLicenceArcher());
			concurrent.setNomArcher(dtm.getConcurrentAtRow(rowIndex).getNomArcher());
			concurrent.setPrenomArcher(dtm.getConcurrentAtRow(rowIndex).getPrenomArcher());
			concurrent.setClub(dtm.getConcurrentAtRow(rowIndex).getClub());
			concurrent.setCriteriaSet(dtm.getConcurrentAtRow(rowIndex).getCriteriaSet());
			concurrent.setCertificat(dtm.getConcurrentAtRow(rowIndex).isCertificat());
			concurrent.setSurclassement(dtm.getConcurrentAtRow(rowIndex).isSurclassement());
		}
	}

	/**
	 * renvoi si il y a eu validation
	 * 
	 * @return boolean
	 */
	public boolean isValider() {
		return this.isValider;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("bouton.valider")) { //$NON-NLS-1$
			this.isValider = jTable.getSelectedRow() > -1;
			this.setVisible(false);
		} else if (ae.getActionCommand().equals("bouton.annuler")) { //$NON-NLS-1$
			this.isValider = false;
			this.setVisible(false);
		} else if (ae.getSource() == moreResult) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dtm = new ArchersTableModel();
					
					ArchersTableLoader loader = new ArchersTableLoader(null);
					loadingProgress(loader);
					loader.execute();
					loading.setBusy(true);
					
					sorter = new TableRowSorter<ArchersTableModel>(dtm);
					jTable.setModel(dtm);
					sorter.toggleSortOrder(1);

					Thread.yield();

					if (jtfFilterLicence.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterLicence.getText().toUpperCase(), 0)); //$NON-NLS-1$
					else if (jtfFilterNom.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterNom.getText().toUpperCase(), 1)); //$NON-NLS-1$
					else if (jtfFilterClub.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter(jtfFilterClub.getText().toUpperCase(), 3));
					jTable.setRowSorter(sorter);
				}
			});
		}
	}

	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == jtfFilterLicence && !jtfFilterLicence.getText().isEmpty()) {
			sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterLicence.getText().toUpperCase(), 0)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
			jtfFilterNom.setText(""); //$NON-NLS-1$
			jtfFilterClub.setText(""); //$NON-NLS-1$
		} else if (e.getSource() == jtfFilterNom && !jtfFilterNom.getText().isEmpty()) {
			sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterNom.getText().toUpperCase(), 1)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
			jtfFilterLicence.setText(""); //$NON-NLS-1$
			jtfFilterClub.setText(""); //$NON-NLS-1$
		} else if (e.getSource() == jtfFilterClub && !jtfFilterClub.getText().isEmpty()) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfFilterClub.getText().toUpperCase(), 3));
			jTable.setRowSorter(sorter);
			jtfFilterNom.setText(""); //$NON-NLS-1$
			jtfFilterLicence.setText(""); //$NON-NLS-1$
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			this.isValider = true;
			this.setVisible(false);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @author aurelien
	 */
	private class ArchersTableModel extends AbstractTableModel {

		private final ArrayList<String> columnsName = new ArrayList<String>();
		private List<Concurrent> rows = new ArrayList<Concurrent>();
		// private ArrayList<SoftReference<Concurrent>> softRows = new ArrayList<SoftReference<Concurrent>>();

		// private ResultSet archersRS;

		private Concurrent curConcurrent = null;

		// private int rowCount = 0;

		public ArchersTableModel() {
			this(null);
		}

		public ArchersTableModel(Archer filter) {


			//rows = ConcurrentManager.getArchersInDatabase(filter, reglement, "NOMARCHER"); //$NON-NLS-1$
			//rows = ConcurrentManager.getArchersInDatabase(filter, reglement, ""); //$NON-NLS-1$

			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.numlicence")); //$NON-NLS-1$
			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.nom")); //$NON-NLS-1$
			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.prenom")); //$NON-NLS-1$
			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.club")); //$NON-NLS-1$
			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.categorie")); //$NON-NLS-1$
			columnsName.add(ApplicationCore.ajrLibelle.getResourceString("listeconcurrent.niveau")); //$NON-NLS-1$
		}
		
		public void add(List<Concurrent> concurrents) {
			int first = 0;
			int last = 0;
			synchronized (rows) {
				rows.addAll(concurrents);
				
				last = rows.size() -1;
				first = last - concurrents.size();
				//System.out.println(first + ">" + last);
				if(last > first)
					fireTableRowsInserted(first, last);
			}
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return columnsName.size();
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int columnIndex) {
			return columnsName.get(columnIndex);
		}

		/**
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return rows.size();
		}

		/**
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			curConcurrent = getConcurrentAtRow(rowIndex);

			switch (columnIndex) {
				case 0:
					return curConcurrent.getNumLicenceArcher();
				case 1:
					return curConcurrent.getNomArcher();
				case 2:
					return curConcurrent.getPrenomArcher();
				case 3:
					return curConcurrent.getClub().getNom();
				case 4:
					String noplacementcritere = ""; //$NON-NLS-1$
					if(curConcurrent.isSurclassement())
						noplacementcritere = "<html><font color=red>"; //$NON-NLS-1$
					for (Criterion key : reglement.getListCriteria()) {
						if (!key.isPlacement()) {
							CriterionElement criterionElement = curConcurrent.getCriteriaSet().getCriterionElement(key);
							if (criterionElement != null)
								noplacementcritere += criterionElement.getCode();
						}
					}
					if(curConcurrent.isSurclassement())
						noplacementcritere += "</font></html>"; //$NON-NLS-1$
					return noplacementcritere;
				case 5:
					String placementcritere = ""; //$NON-NLS-1$
					if(curConcurrent.isSurclassement())
						placementcritere = "<html><font color=red>"; //$NON-NLS-1$
					for (Criterion key : reglement.getListCriteria()) {
						if (key.isPlacement()) {
							placementcritere += curConcurrent.getCriteriaSet().getCriterionElement(key).getCode();
						}
					}
					if(curConcurrent.isSurclassement())
						placementcritere += "</font></html>"; //$NON-NLS-1$
					return placementcritere;
				default:
					return null;
			}

		}

		/**
		 * @see javax.swing.table.TableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		/**
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}

		public Concurrent getConcurrentAtRow(int rowIndex) {
			return rows.get(rowIndex);
		}
		
		@Override
		public void finalize() throws Throwable {
			System.out.println("ConcurrentListDialog.TableModel detruit"); //$NON-NLS-1$
			super.finalize();
		}
	}
	
	private class ArchersTableLoader extends SwingWorker<List<Concurrent>, Concurrent> {

		private Archer filter;
		private int nbConcurrent = 0;
		private List<Concurrent> concurrents = new ArrayList<Concurrent>();
		/**
		 * 
		 */
		public ArchersTableLoader(Archer filter) {
			this.filter = filter;
		}
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected List<Concurrent> doInBackground() throws Exception {			
			return ConcurrentManager.getArchersInDatabase(filter, reglement, "", new ConcurrentManagerProgress() { //$NON-NLS-1$

				@Override
				public void setConcurrentCount(int concurrentCount) {
					nbConcurrent = concurrentCount;
				}

				@Override
				public void setCurrentConcurrent(Concurrent concurrent) {
					concurrents.add(concurrent);
					publish(concurrent);
					setProgress(100 * concurrents.size() / nbConcurrent);
				}
				
			});
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#process(java.util.List)
		 */
		@Override
		protected void process(List<Concurrent> chunks) {
			dtm.add(chunks);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		protected void done() {
			setProgress(100);
			super.done();
		}
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
}