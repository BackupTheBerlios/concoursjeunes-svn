/**
 * 
 */
package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * @author Aurelien
 *
 */
public class EntiteListDialog extends JDialog implements ActionListener, MouseListener, CaretListener, FocusListener {
	private ConcoursJeunes concoursJeunes;
	
	private DefaultTableModel dtm = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private TableSorter sorter;
    
    private JTable jTable			= null;
    private JTextField jtfNom		= null;
    private JTextField jtfAgrement	= null;
    private JTextField jtfVille		= null;
    private JScrollPane jScrollPane	= null;
    private JButton jbFermer        = null;
	
	public EntiteListDialog(ConcoursJeunes concoursjeunes) {
		super(concoursjeunes, "", true);
		this.concoursJeunes = concoursjeunes;
		
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		JPanel jpEntete = new JPanel();
        JPanel jpPied = new JPanel();
		
		JLabel jlNom = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.nom") + ":");
		JLabel jlAgrement = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.agrement") + ":");
		JLabel jlVille = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.ville") + ":");
		
		jtfNom = new JTextField(10);
		jtfAgrement = new JTextField(10);
		jtfVille = new JTextField(10);
		
		jScrollPane = new JScrollPane();
        
        jbFermer = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.fermer"));
		
		jtfNom.addFocusListener(this);
		jtfNom.addCaretListener(this);
		jtfAgrement.addFocusListener(this);
		jtfAgrement.addCaretListener(this);
		jtfVille.addFocusListener(this);
		jtfVille.addCaretListener(this);
        
        jbFermer.addActionListener(this);
		
		jpEntete.add(jlNom);
		jpEntete.add(jtfNom);
		jpEntete.add(jlAgrement);
		jpEntete.add(jtfAgrement);
		jpEntete.add(jlVille);
		jpEntete.add(jtfVille);
		
		jScrollPane.setViewportView(getJTable());
        
        jpPied.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpPied.add(jbFermer);
		
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
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getJTable() {
		if (this.jTable == null) {

            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.nom"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.agrement"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.adresse"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeentite.ville"));
            
            for(Entite entite : ConcoursJeunes.listeEntite.values()) {
            	dtm.addRow(new String[] {
            			entite.getNom(), entite.getAgrement(), entite.getAdresse(),
            			entite.getVille() });
            }

            this.sorter = new TableSorter(dtm);
            this.jTable = new JTable(this.sorter);
            this.sorter.setTableHeader(this.jTable.getTableHeader());
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
    
    private void moveTableRectView(int i) {
        this.jTable.setRowSelectionInterval(i,i);
        
        JViewport jvpTemp = this.jScrollPane.getViewport();
        Rectangle rectView = jvpTemp.getViewRect();
        rectView.y = this.jTable.getRowHeight()*i - jvpTemp.getViewPosition().y;
        
        this.jScrollPane.getViewport().scrollRectToVisible(rectView);
    }
	
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
    }
    
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == jtfNom) {
			for(int i = 0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,0)).toLowerCase().startsWith(jtfNom.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
		} else if(e.getSource() == jtfAgrement) {
	        for(int i = 0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,1)).toLowerCase().startsWith(this.jtfAgrement.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
	    } else {
	        for(int i=0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,3)).toLowerCase().startsWith(this.jtfVille.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
	    }
	}
	
	public void mouseClicked(MouseEvent e) {
	    if(e.getClickCount() == 2) {
	    	EntiteDialog ed = new EntiteDialog(concoursJeunes);
	    	ed.showEntite(ConcoursJeunes.listeEntite.get(sorter.getValueAt(this.jTable.getSelectedRow(), 1)));
	    	
            //this.isValider = true;
            //this.setVisible(false);
	    }
	}
    
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	public void focusGained(FocusEvent e) {
		if(e.getSource() == this.jtfNom) {
            this.sorter.setSortingStatus(1, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(0, TableSorter.ASCENDING);
		} else if(e.getSource() == this.jtfAgrement) {
            this.sorter.setSortingStatus(0, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(1, TableSorter.ASCENDING);
    	} else if(e.getSource() == this.jtfVille) {
            this.sorter.setSortingStatus(0, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(1, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.ASCENDING);
    	}
	}
    
    public void focusLost(FocusEvent e) { }
}
