package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.concoursjeunes.*;

/**
 * La liste des personnes présente daans la base des archers
 * @author  Aurelien Jeoffray
 * @version  2.0
 */

public class ConcurrentListDialog extends JDialog implements ActionListener, MouseListener, CaretListener {
	private ArchersTableModel dtm	= null;
	private TableRowSorter<ArchersTableModel> sorter;
	
	private Reglement reglement;

	private JLabel jlFilterLicence     = null;
	private JTextField jtfFilterLicence= null;
	private JLabel jlFilterNom     = null;
	private JTextField jtfFilterNom= null;
	private JLabel jlFilterClub     = null;
	private JTextField jtfFilterClub= null;
	private JButton moreResult		= new JButton("+");
	private JTable jTable          = null;
	private JScrollPane jScrollPane= null;
	private JPanel jPanel          = null;
	private JButton jbValider      = null;
	private JButton jbAnnuler      = null;

	private boolean isValider      = false;

	/**
	 * This is the default constructor
	 * 
	 * @param parentframe - la fenetre principal de l'application (pour le point modal)
	 */
	public ConcurrentListDialog(Window parentframe, Reglement reglement, Archer filter) {
		super(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.titre"), ModalityType.APPLICATION_MODAL);
		this.reglement = reglement;
		
		if(filter == null && ConcoursJeunes.configuration.getClub().getAgrement().length() > 0) {
			filter = new Archer();
			Entite entite = new Entite();
			entite.setAgrement(ConcoursJeunes.configuration.getClub().getAgrement().substring(0, 2) + "%");
			filter.setClub(entite);
		}
		dtm = new ArchersTableModel(filter);

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
		moreResult.setToolTipText("Plus de résultats");
		
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
		jlFilterLicence.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.licence")); //$NON-NLS-1$
		jlFilterNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.nom")); //$NON-NLS-1$
		jlFilterClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.club")); //$NON-NLS-1$
	}

	/**
	 * This method initializes jtfFilterLicence	
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfFilterLicence"
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
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfFilterNom"
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
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfFilterClub"
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
	 * @return  javax.swing.JTable
	 * @uml.property  name="jTable"
	 */    
	private JTable getJTable() {
		if (this.jTable == null) {

			this.sorter = new TableRowSorter<ArchersTableModel>(dtm);
			this.jTable = new JTable(dtm);
			this.jTable.setAutoCreateRowSorter(true);
			//this.sorter.setTableHeader(this.jTable.getTableHeader());
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
			column = jTable.getColumnModel().getColumn(5);
			column.setPreferredWidth(50);
		}
		return this.jTable;
	}

	/**
	 * This method initializes jScrollPane	
	 * @return  javax.swing.JScrollPane
	 * @uml.property  name="jScrollPane"
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
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jPanel"
	 */    
	private JPanel getJPanel() {
		if (this.jPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			this.jPanel = new JPanel();
			this.jPanel.setLayout(flowLayout1);
			flowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
			this.jPanel.add(getJButton(), null);
			this.jPanel.add(getJButton1(), null);
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
			this.jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
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
			this.jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
			this.jbAnnuler.setActionCommand("bouton.annuler"); //$NON-NLS-1$
			this.jbAnnuler.addActionListener(this);
		}
		return this.jbAnnuler;
	}
	
	/**
	 * initialise un nouveau concurrent
	 * 
	 * @param concurrent - l'objet concurrent à initialiser
	 */
	public void initConcurrent(Concurrent concurrent) {

		if(dtm != null) {
			int rowIndex = jTable.convertRowIndexToModel(jTable.getSelectedRow());
	
			concurrent.setNumLicenceArcher(dtm.getConcurrentAtRow(rowIndex).getNumLicenceArcher());
			concurrent.setNomArcher(dtm.getConcurrentAtRow(rowIndex).getNomArcher());
			concurrent.setPrenomArcher(dtm.getConcurrentAtRow(rowIndex).getPrenomArcher());
			concurrent.setClub(dtm.getConcurrentAtRow(rowIndex).getClub());
			concurrent.setCriteriaSet(dtm.getConcurrentAtRow(rowIndex).getCriteriaSet());
		}
	}

	/**
	 * renvoi si il y a eu validation
	 * @return  boolean
	 * @uml.property  name="isValider"
	 */
	public boolean isValider() {
		return this.isValider;
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("bouton.valider")) { //$NON-NLS-1$
			this.isValider = jTable.getSelectedRow() > -1;
			this.setVisible(false);
		} else if(ae.getActionCommand().equals("bouton.annuler")) { //$NON-NLS-1$
			this.isValider = false;
			this.setVisible(false);
		} else if(ae.getSource() == moreResult) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			    	dtm = new ArchersTableModel();
					sorter = new TableRowSorter<ArchersTableModel>(dtm);
					jTable.setModel(dtm);
					
					Thread.yield();
					
					if(jtfFilterLicence.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterLicence.getText().toUpperCase(), 0));
					else if(jtfFilterNom.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterNom.getText().toUpperCase(), 1));
					else if(jtfFilterClub.getText().length() > 0)
						sorter.setRowFilter(RowFilter.regexFilter(jtfFilterClub.getText().toUpperCase(), 3));
					jTable.setRowSorter(sorter);
			    }
			});
		}
	}
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == jtfFilterLicence) {
			sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterLicence.getText().toUpperCase(), 0));
			jTable.setRowSorter(sorter);
			jtfFilterNom.setText("");
			jtfFilterClub.setText("");
		} else if(e.getSource() == jtfFilterNom) {
			sorter.setRowFilter(RowFilter.regexFilter("^" + jtfFilterNom.getText().toUpperCase(), 1));
			jTable.setRowSorter(sorter);
			jtfFilterLicence.setText("");
			jtfFilterClub.setText("");
		} else if(e.getSource() == jtfFilterClub) {
			sorter.setRowFilter(RowFilter.regexFilter(jtfFilterClub.getText().toUpperCase(), 3));
			jTable.setRowSorter(sorter);
			jtfFilterNom.setText("");
			jtfFilterLicence.setText("");
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			this.isValider = true;
			this.setVisible(false);
		}
	}

	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }

	/**
	 * @author  aurelien
	 */
	private class ArchersTableModel implements TableModel {

		private ArrayList<TableModelListener> tmListeners = new ArrayList<TableModelListener>();
		private ArrayList<String> columnsName = new ArrayList<String>();
		private ArrayList<Concurrent> rows = new ArrayList<Concurrent>();

		private Concurrent curConcurrent = null;

		public ArchersTableModel() {
			this(null);
		}
		
		public ArchersTableModel(Archer filter) {
			
			rows = Concurrent.getArchersInDatabase(filter, reglement, "NOMARCHER");

			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.numlicence")); //$NON-NLS-1$
			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.nom")); //$NON-NLS-1$
			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.prenom")); //$NON-NLS-1$
			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.club")); //$NON-NLS-1$
			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.categorie")); //$NON-NLS-1$
			columnsName.add(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.niveau")); //$NON-NLS-1$
		}
		/**
		 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
		 */
		public void addTableModelListener(TableModelListener l) {
			tmListeners.add(l);
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
			return columnsName.size();
		}

		/**
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
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
			curConcurrent = rows.get(rowIndex);

			switch(columnIndex) {
			case 0:
				return curConcurrent.getNumLicenceArcher();
			case 1:
				return curConcurrent.getNomArcher();
			case 2:
				return curConcurrent.getPrenomArcher();
			case 3:
				return curConcurrent.getClub().getNom();
			case 4:
				String noplacementcritere = "";
				for(Criterion key : reglement.getListCriteria()) {
					if(!key.isPlacement()) {
						CriterionElement criterionElement = curConcurrent.getCriteriaSet().getCriterionElement(key);
						if(criterionElement != null)
							noplacementcritere += criterionElement.getCode();
					}
				}
				return noplacementcritere;
			case 5:
				String placementcritere = "";
				for(Criterion key : reglement.getListCriteria()) {
					if(key.isPlacement()) {
						placementcritere += curConcurrent.getCriteriaSet().getCriterionElement(key).getCode();
					}
				}
				return placementcritere;
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
			tmListeners.remove(l);
		}

		/**
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}

		public Concurrent getConcurrentAtRow(int rowIndex) {
			return rows.get(rowIndex);
		}
	}
}