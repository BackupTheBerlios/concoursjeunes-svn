/**
 * 
 */
package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.concoursjeunes.*;

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
	 * @uml.property  name="jTable"
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
	 * @return
	 * @uml.property  name="action"
	 */
	public int getAction() {
		return action;
	}

	/**
	 * @param action
	 * @uml.property  name="action"
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
			sorter.setRowFilter(RowFilter.regexFilter(".*" + jtfNom.getText().toUpperCase() + ".*", 0));
			jTable.setRowSorter(sorter);
		} else if(e.getSource() == this.jtfAgrement) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfAgrement.getText().toUpperCase() + ".*", 1));
			jTable.setRowSorter(sorter);
		} else if(e.getSource() == this.jtfVille) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfVille.getText().toUpperCase() + ".*", 3));
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

				rs = stmt.executeQuery("select * from Entite order by VilleEntite");
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

				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as NbRows from Entite");

				if(rs.next()) {
					int nb = rs.getInt("NbRows");
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
					entite.setAgrement(rs.getString("AgrementEntite"));
					entite.setNom(rs.getString("NomEntite"));
					entite.setAdresse(rs.getString("AdresseEntite"));
					entite.setCodePostal(rs.getString("CodePostalEntite"));
					entite.setVille(rs.getString("VilleEntite"));
					entite.setNote(rs.getString("NoteEntite"));
					entite.setType(rs.getInt("TypeEntite"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return entite;
		}
	}
}
