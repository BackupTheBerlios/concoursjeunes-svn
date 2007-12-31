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
package org.concoursjeunes.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Entite;

/**
 * @author Aurélien JEOFFRAY
 */
public class EntiteListDialog extends JDialog implements ActionListener, MouseListener, CaretListener {

	public static final int VALIDER = 1;
	public static final int ANNULER = 2;

	private JFrame parentframe;

	private EntiteTableModel dtm = new EntiteTableModel();
	private TableRowSorter<EntiteTableModel> sorter;

	private JTable jTable		= null;
	private JTextField jtfNom		= null;
	private JTextField jtfAgrement	= null;
	private JTextField jtfVille		= null;
	private JScrollPane jScrollPane	= null;
	private JButton jbValider       	= null;
	private JButton jbAnnuler		= null;

	private int action				= ANNULER;

	public EntiteListDialog(JFrame parentframe) {
		super(parentframe, "", true); //$NON-NLS-1$
		this.parentframe = parentframe;

		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		JPanel jpEntete = new JPanel();
		JPanel jpPied = new JPanel();

		JLabel jlNom = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.nom") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel jlAgrement = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.agrement") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel jlVille = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.ville") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

		jtfNom = new JTextField(10);
		jtfAgrement = new JTextField(10);
		jtfVille = new JTextField(10);

		jScrollPane = new JScrollPane();

		jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$

		//jtfNom.addFocusListener(this);
		jtfNom.addCaretListener(this);
		//jtfAgrement.addFocusListener(this);
		jtfAgrement.addCaretListener(this);
		//jtfVille.addFocusListener(this);
		jtfVille.addCaretListener(this);

		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);

		jpEntete.add(jlNom);
		jpEntete.add(jtfNom);
		jpEntete.add(jlAgrement);
		jpEntete.add(jtfAgrement);
		jpEntete.add(jlVille);
		jpEntete.add(jtfVille);

		jScrollPane.setViewportView(getJTable());

		jpPied.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpPied.add(jbValider);
		jpPied.add(jbAnnuler);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jpEntete, BorderLayout.NORTH);
		this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
		this.getContentPane().add(jpPied, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * This method initializes jTable	
	 * @return  javax.swing.JTable
	 */    
	private JTable getJTable() {
		if (this.jTable == null) {
			this.sorter = new TableRowSorter<EntiteTableModel>(dtm);
			this.jTable = new JTable(dtm);
			this.jTable.setAutoCreateRowSorter(true);
			this.jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			this.jTable.setPreferredScrollableViewportSize(new Dimension(640, 480));
			this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.jTable.addMouseListener(this);

			TableColumn column = jTable.getColumnModel().getColumn(0);
			column.setPreferredWidth(200);
			column = jTable.getColumnModel().getColumn(1);
			column.setPreferredWidth(30);
		}
		return this.jTable;
	}

	public Entite getSelectedEntite() {
		int row = jTable.convertRowIndexToModel(jTable.getSelectedRow());

		return dtm.getEntiteAtRow(row);
	}

	/**
	 * @return l'action réalisé sur la boite de dialogue
	 * 
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @param action
	 */
	public void setAction(int action) {
		this.action = action;
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == jbValider) {
			action = VALIDER;
			setVisible(false);
		} else if(ae.getSource() == jbAnnuler) {
			action = ANNULER;
			setVisible(false);
		}
	}

	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == this.jtfNom) {
			sorter.setRowFilter(RowFilter.regexFilter(".*" + jtfNom.getText().toUpperCase() + ".*", 0)); //$NON-NLS-1$ //$NON-NLS-2$
			jTable.setRowSorter(sorter);
		} else if(e.getSource() == this.jtfAgrement) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfAgrement.getText().toUpperCase() + ".*", 1)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
		} else if(e.getSource() == this.jtfVille) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfVille.getText().toUpperCase() + ".*", 3)); //$NON-NLS-1$
			jTable.setRowSorter(sorter);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			EntiteDialog ed = new EntiteDialog(parentframe);
			ed.showEntite(dtm.getEntiteAtRow(jTable.convertRowIndexToModel(jTable.getSelectedRow())));

			//this.isValider = true;
			//this.setVisible(false);
		}
	}

	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }

	/*public void focusGained(FocusEvent e) {
		if(e.getSource() == this.jtfNom) {
			ArrayList<SortKey> sortKeys = new ArrayList<SortKey>();
	        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
			jTable.getRowSorter().setSortKeys(sortKeys);
		} else if(e.getSource() == this.jtfAgrement) {
			ArrayList<SortKey> sortKeys = new ArrayList<SortKey>();
	        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
	        jTable.getRowSorter().setSortKeys(sortKeys);
    	} else if(e.getSource() == this.jtfVille) {
    		ArrayList<SortKey> sortKeys = new ArrayList<SortKey>();
	        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
	        jTable.getRowSorter().setSortKeys(sortKeys);
    	}
	}*/

	public void focusLost(FocusEvent e) { }

	/**
	 * @author  aurelien
	 */
	private class EntiteTableModel implements TableModel {

		private EventListenerList listenerList = new EventListenerList();

		private ArrayList<String> columnName = new ArrayList<String>();

		private ResultSet rs;
		private Entite curEntite = null;
		private int curIndex = 0;

		public EntiteTableModel() {
			try {
				
				Statement stmt = ConcoursJeunes.dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

				rs = stmt.executeQuery("select * from Entite order by VilleEntite"); //$NON-NLS-1$
			} catch (SQLException e) {
				e.printStackTrace();
			}

			columnName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.nom")); //$NON-NLS-1$
			columnName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.agrement")); //$NON-NLS-1$
			columnName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.adresse")); //$NON-NLS-1$
			columnName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.ville")); //$NON-NLS-1$
		}

		/**
		 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
		 */
		public void addTableModelListener(TableModelListener l) {
			listenerList.add(TableModelListener.class, l);
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return columnName.size();
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
		public String getColumnName(int columnIndex) {
			return columnName.get(columnIndex);
		}

		/**
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			try {
				Statement stmt = ConcoursJeunes.dbConnection.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as NbRows from Entite"); //$NON-NLS-1$

				if(rs.next()) {
					int nb = rs.getInt("NbRows"); //$NON-NLS-1$
					return nb;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return 0;
		}

		/**
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			getEntiteAtRow(rowIndex);

			switch(columnIndex) {
			case 0:
				return curEntite.getNom();
			case 1:
				return curEntite.getAgrement();
			case 2:
				return curEntite.getAdresse();
			case 3:
				return curEntite.getVille();
			default:
				return null;
			}
		}

		/**
		 * @see javax.swing.table.TableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		/**
		 * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
		 */
		public void removeTableModelListener(TableModelListener l) {
			listenerList.remove(TableModelListener.class, l);
		}

		/**
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}

		public Entite getEntiteAtRow(int rowIndex) {

			if(rowIndex != curIndex || curEntite == null) {
				curEntite = loadEntite(rowIndex);
				curIndex = rowIndex;
			}

			return curEntite;
		}

		private Entite loadEntite(int index) {
			Entite entite = new Entite();
			try {
				if(rs.absolute(index + 1)) {
					entite.setAgrement(rs.getString("AgrementEntite")); //$NON-NLS-1$
					entite.setNom(rs.getString("NomEntite")); //$NON-NLS-1$
					entite.setAdresse(rs.getString("AdresseEntite")); //$NON-NLS-1$
					entite.setCodePostal(rs.getString("CodePostalEntite")); //$NON-NLS-1$
					entite.setVille(rs.getString("VilleEntite")); //$NON-NLS-1$
					entite.setNote(rs.getString("NoteEntite")); //$NON-NLS-1$
					entite.setType(rs.getInt("TypeEntite")); //$NON-NLS-1$
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return entite;
		}
	}
}
