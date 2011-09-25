/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.concours.Archer;
import org.ajdeveloppement.concours.Concurrent;
import org.ajdeveloppement.concours.Criterion;
import org.ajdeveloppement.concours.CriterionElement;
import org.ajdeveloppement.concours.Entite;
import org.ajdeveloppement.concours.Profile;
import org.ajdeveloppement.concours.Reglement;
import org.ajdeveloppement.concours.managers.ConcurrentManager;
import org.ajdeveloppement.concours.managers.ConcurrentManagerProgress;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXHyperlink;

/**
 * La liste des personnes présente daans la base des archers
 * 
 * @author Aurélien Jeoffray
 * @version 2.0
 */
@Localizable(textMethod="setTitle",value="concurrent.nouveau.titre")
public class ConcurrentListDialog extends JDialog implements ActionListener, MouseListener, CaretListener {
	private AjResourcesReader localisation;
	
	private ArchersTableModel dtm = new ArchersTableModel();
	private TableRowSorter<ArchersTableModel> sorter = new TableRowSorter<ArchersTableModel>(dtm);

	private final Reglement reglement;

	@Localizable("concurrent.nouveau.licence")
	private JLabel jlFilterLicence = null;
	private JTextField jtfFilterLicence = null;
	@Localizable("concurrent.nouveau.nom")
	private JLabel jlFilterNom = null;
	private JTextField jtfFilterNom = null;
	@Localizable("concurrent.nouveau.club")
	private JLabel jlFilterClub = null;
	private JTextField jtfFilterClub = null;
	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JPanel jPanel = null;
	private JXBusyLabel loading = new JXBusyLabel();
	private JLabel loadingProgressLabel = new JLabel();
	private JLabel databaseFilter = new JLabel();
	private JXHyperlink jxhDisableFilter = new JXHyperlink();
	private JButton jbValider = null;
	private JButton jbAnnuler = null;

	private boolean isValider = false;
	private boolean filtreligue = false;
	private boolean filtre = false;

	/**
	 * This is the default constructor
	 * 
	 * @param parentframe -
	 *            la fenetre principal de l'application (pour le point modal)
	 */
	public ConcurrentListDialog(JDialog parentframe, Profile profile, Reglement reglement, Archer filter) {
		super(parentframe, "", ModalityType.APPLICATION_MODAL); //$NON-NLS-1$
		this.reglement = reglement;
		this.localisation = profile.getLocalisation();

		if (filter == null && profile.getConfiguration().getClub().getAgrement().length() > 0) {
			if(profile.getConfiguration().getFederation().getSigleFederation().equals("FFTA"))  {//$NON-NLS-1$
				filter = new Archer();
				Entite entite = new Entite();
				entite.setAgrement(profile.getConfiguration().getClub().getAgrement().substring(0, 2) + "%"); //$NON-NLS-1$
				filter.setEntite(entite);
				filtreligue = true;
			}
		}
		if(filter != null)
			filtre = true;
		
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

		jpEntete.add(jlFilterLicence);
		jpEntete.add(getJtfFilterLicence());
		jpEntete.add(jlFilterNom);
		jpEntete.add(getJtfFilterNom());
		jpEntete.add(jlFilterClub);
		jpEntete.add(getJtfFilterClub());

		jContentPane.setLayout(new BorderLayout());

		jContentPane.add(jpEntete, BorderLayout.NORTH);
		jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		jContentPane.add(getJPanel(), BorderLayout.SOUTH);

		setContentPane(jContentPane);

		getRootPane().setDefaultButton(jbValider);

		affectLabels();
	}

	private void affectLabels() {
		Localizator.localize(this, localisation);
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

			this.jTable = new JTable(dtm);

			jTable.setRowSorter(sorter);

			this.jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			this.jTable.setPreferredScrollableViewportSize(new Dimension(640, 480));
			this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.jTable.addMouseListener(this);

			TableColumn column = jTable.getColumnModel().getColumn(0);
			column.setPreferredWidth(65);
			column = jTable.getColumnModel().getColumn(3);
			column.setPreferredWidth(200);
			column = jTable.getColumnModel().getColumn(4);
			column.setPreferredWidth(60);
			
			sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
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
			
			JPanel jpFilter = new JPanel();
			
			if(filtre) {
				if(filtreligue)
					databaseFilter.setText(localisation.getResourceString("listeconcurrent.liguefilter")); //$NON-NLS-1$
				else
					databaseFilter.setText(localisation.getResourceString("listeconcurrent.genericfilter")); //$NON-NLS-1$
				jxhDisableFilter.setText(localisation.getResourceString("listeconcurrent.disablefilter")); //$NON-NLS-1$
				jxhDisableFilter.addActionListener(this);
			}
			
			GridBagConstraints c = new GridBagConstraints();
			GridbagComposer gridbagComposer = new GridbagComposer();
			
			jpFilter.add(databaseFilter);
			jpFilter.add(jxhDisableFilter);
					
			gridbagComposer.setParentPanel(this.jPanel);
			c.gridy=0;
			c.anchor = GridBagConstraints.WEST;
			c.gridwidth = 5;
			gridbagComposer.addComponentIntoGrid(jpFilter, c);
			c.gridy++;
			c.gridwidth = 1;
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
			this.jbValider.setText(localisation.getResourceString("bouton.valider")); //$NON-NLS-1$
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
			this.jbAnnuler.setText(localisation.getResourceString("bouton.annuler")); //$NON-NLS-1$
			this.jbAnnuler.setActionCommand("bouton.annuler"); //$NON-NLS-1$
			this.jbAnnuler.addActionListener(this);
		}
		return this.jbAnnuler;
	}
	
	private void loadingProgress(ArchersTableLoader loader) {
		loader.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
			public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals("progress")) { //$NON-NLS-1$
                    int progress = ((Integer) event.getNewValue()).intValue();

                    loadingProgressLabel.setText(localisation.getResourceString("listeconcurrent.loading", progress)); //$NON-NLS-1$

                    if (progress == 100) {
                    	loading.setBusy(false);
                    	 loadingProgressLabel.setText(localisation.getResourceString("listeconcurrent.loadingended")); //$NON-NLS-1$
                    }
                }
            }
        });
	}

	/**
	 * Filtre la liste des archers afficher avec le l'archer générique fournit en paramètre
	 * 
	 * @param filter objet archer de filtrage
	 */
	public void setFilter(Archer filter) {
		if(filter == null)
			return;
		String numLicence = filter.getNumLicenceArcher().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String nom = filter.getName().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String club = filter.getEntite().getNom().replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (!numLicence.isEmpty()) {
			jtfFilterLicence.setText(numLicence);
			sorter.setRowFilter(RowFilter.regexFilter("^" + numLicence.toUpperCase(), 0)); //$NON-NLS-1$
		}
		if (!nom.isEmpty()) {
			jtfFilterNom.setText(nom);
			sorter.setRowFilter(RowFilter.regexFilter("^" + nom.toUpperCase(), 1)); //$NON-NLS-1$
		}
		if (!club.isEmpty()) {
			jtfFilterClub.setText(club);
			sorter.setRowFilter(RowFilter.regexFilter(club.toUpperCase(), 3));
		}
	}
	/**
	 * Retourne le concurrent séléctionné
	 * 
	 * @return le concurrent séléctionné 
	 */
	public Concurrent getSelectedConcurrent() {

		if (dtm != null) {
			int rowIndex = jTable.convertRowIndexToModel(jTable.getSelectedRow());

			return dtm.getConcurrentAtRow(rowIndex);
		}
		
		return null;
	}

	/**
	 * renvoi si il y a eu validation
	 * 
	 * @return boolean
	 */
	public boolean isValider() {
		return this.isValider;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("bouton.valider")) { //$NON-NLS-1$
			this.isValider = jTable.getSelectedRow() > -1;
			this.setVisible(false);
		} else if (ae.getActionCommand().equals("bouton.annuler")) { //$NON-NLS-1$
			this.isValider = false;
			this.setVisible(false);
		} else if (ae.getSource() == jxhDisableFilter) {
			databaseFilter.setText(""); //$NON-NLS-1$
			jxhDisableFilter.setText(""); //$NON-NLS-1$
			jxhDisableFilter.removeActionListener(this);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					dtm.clear();
					ArchersTableLoader loader = new ArchersTableLoader(null);
					loadingProgress(loader);
					loadingProgressLabel.setText(localisation.getResourceString("listeconcurrent.loading", 0)); //$NON-NLS-1$
					loader.execute();
					loading.setBusy(true);
				}
			});
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == jtfFilterLicence && jtfFilterLicence.hasFocus()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterLicence.getText().toUpperCase(), 0)); //$NON-NLS-1$
					jtfFilterNom.setText(""); //$NON-NLS-1$
					jtfFilterClub.setText(""); //$NON-NLS-1$
				}
			});
		} else if (e.getSource() == jtfFilterNom && jtfFilterNom.hasFocus()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					synchronized (dtm) {
						sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterNom.getText().toUpperCase(), 1)); //$NON-NLS-1$
						jtfFilterLicence.setText(""); //$NON-NLS-1$
						jtfFilterClub.setText(""); //$NON-NLS-1$
					}
				}
			});
		} else if (e.getSource() == jtfFilterClub && jtfFilterClub.hasFocus()) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfFilterClub.getText().toUpperCase(), 3));
			jtfFilterNom.setText(""); //$NON-NLS-1$
			jtfFilterLicence.setText(""); //$NON-NLS-1$
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			this.isValider = true;
			this.setVisible(false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @author aurelien
	 */
	private class ArchersTableModel extends AbstractTableModel {

		private List<Concurrent> rows = new ArrayList<Concurrent>();

		private Concurrent curConcurrent = null;

		public ArchersTableModel() {
		}
		
		public void add(List<Concurrent> concurrents) {
			int first = 0;
			int last = 0;
			synchronized (rows) {
				first = rows.size() -1;
				
				rows.addAll(concurrents);
				
				last = rows.size() -1;
				if(last > first) {
					fireTableRowsInserted(first+1, last);
				}
			}
		}
		
		public void clear() {
			rows.clear();
			
			fireTableDataChanged();
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
		@Override
		public int getColumnCount() {
			return 5;
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
				case 0:
					return localisation.getResourceString("listeconcurrent.numlicence"); //$NON-NLS-1$
				case 1:
					return localisation.getResourceString("listeconcurrent.nom"); //$NON-NLS-1$
				case 2:
					return localisation.getResourceString("listeconcurrent.prenom"); //$NON-NLS-1$
				case 3:
					return localisation.getResourceString("listeconcurrent.club"); //$NON-NLS-1$
				case 4:
					return localisation.getResourceString("listeconcurrent.categorie"); //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}

		/**
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		@Override
		public int getRowCount() {
			return rows.size();
		}

		/**
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			curConcurrent = getConcurrentAtRow(rowIndex);

			switch (columnIndex) {
				case 0:
					return curConcurrent.getNumLicenceArcher();
				case 1:
					return curConcurrent.getName();
				case 2:
					return curConcurrent.getFirstName();
				case 3:
					return curConcurrent.getEntite().getNom();
				case 4:
					String criteres = ""; //$NON-NLS-1$
					if(reglement != null) {
						if(curConcurrent.isSurclassement())
							criteres = "<html><font color=red>"; //$NON-NLS-1$
						for (Criterion key : reglement.getListCriteria()) {
							CriterionElement criterionElement = curConcurrent.getCriteriaSet().getCriterionElement(key);
							if (criterionElement != null)
								criteres += criterionElement.getCode();
						}
						if(curConcurrent.isSurclassement())
							criteres += "</font></html>"; //$NON-NLS-1$
					}
					return criteres;
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
			//ConcurrentManager.
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
		private int nbLoadedConcurrent = 0;
		//private List<Concurrent> concurrents = new ArrayList<Concurrent>();
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
					nbLoadedConcurrent++;
					publish(concurrent);
					setProgress(100 * nbLoadedConcurrent / nbConcurrent);
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