package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * La liste des personnes présente daans la base des archers
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */

public class ConcurrentListDialog extends JDialog implements ActionListener, MouseListener, CaretListener, FocusListener {
    
    //variable de récupération des ressources
    //private FicheConcours ficheConcours;
	
    private static Hashtable<String, Concurrent> listeTireur;
    
    private DefaultTableModel dtm = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private TableSorter sorter;
    
    private JPanel jContentPane    = null;
	private JPanel jpEntete        = null;
    private JComboBox jcbLigue     = null;
	private JLabel jlLicence       = null;
	private JTextField jtfLicence  = null;
	private JLabel jlNom           = null;
	private JTextField jtfNom      = null;
	private JLabel jlClub          = null;
	private JTextField jtfClub     = null;
	private JTable jTable          = null;
	private JScrollPane jScrollPane= null;
	private JPanel jPanel          = null;
	private JButton jbValider      = null;
	private JButton jbAnnuler      = null;
	
    private int defaultligue       = 0;
	private boolean isValider      = false;
    
    private Vector<Entite> ligues = new Vector<Entite>();

	/**
	 * This is the default constructor
	 * 
	 * @param concoursJeunes - la fenetre mere dont depend la boite de dialogue
	 */
	public ConcurrentListDialog(ConcoursJeunes concoursJeunes) {
		super(concoursJeunes);
        
        if(ConcoursJeunes.configuration.getNumAgrement().length() > 0)
            defaultligue = Integer.parseInt(ConcoursJeunes.configuration.getNumAgrement().substring(0,2));

        
		InputLicence(defaultligue);
		if(listeTireur != null)
			initialize();
	}
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.titre"));
		this.setContentPane(getJContentPane());
        this.getRootPane().setDefaultButton(this.jbValider);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setModal(true);
		//this.setVisible(true);
	}
    
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(this.jContentPane == null) {
            this.jContentPane = new javax.swing.JPanel();
            this.jContentPane.setLayout(new java.awt.BorderLayout());
            this.jContentPane.add(getJpEntete(), java.awt.BorderLayout.NORTH);
            this.jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
            this.jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
		}
		return this.jContentPane;
	}
    
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJpEntete() {
		if (this.jpEntete == null) {
            this.jlLicence = new JLabel();
            this.jlNom = new JLabel();
            this.jlClub = new JLabel();
            this.jpEntete = new JPanel();
			
            this.jlLicence.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.licence"));
            this.jlNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.nom"));
            this.jlClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nouveau.club"));
			
            this.jpEntete.add(getJcbLigue(), null);
            this.jpEntete.add(this.jlNom, null);
            this.jpEntete.add(getJtfNom(), null);
            this.jpEntete.add(this.jlClub, null);
            this.jpEntete.add(getJtfClub(), null);
            this.jpEntete.add(this.jlLicence, null);
            this.jpEntete.add(getJtfLicence(), null);
		}
		return this.jpEntete;
	}
    
    
    /**
     * This method initializes jcbLigue   
     *  
     * @return javax.swing.JTextField   
     */    
    private JComboBox getJcbLigue() {
        int ligueindex = 0;
        int defaultligueindex  = 0;
        
        for(Entite entite : ConcoursJeunes.listeEntite.values()) {
            if(entite.getType() == Entite.LIGUE) {
                int i = Integer.parseInt(entite.getAgrement().substring(0,2));
                ligues.add(entite);
                if(i == defaultligue)
                    defaultligueindex = ligueindex;
                ligueindex++;
            } 
        }
        Entite autre = new Entite();
        autre.setNom("AUTRE");
        autre.setType(Entite.LIGUE);
        ligues.add(autre);
        
        if (this.jcbLigue == null) {
            this.jcbLigue = new JComboBox(ligues);
            this.jcbLigue.setSelectedIndex(defaultligueindex);
            this.jcbLigue.addActionListener(this);
        }
        return this.jcbLigue;
    }
    
	/**
	 * This method initializes jtfLicence	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfLicence() {
		if (this.jtfLicence == null) {
            this.jtfLicence = new JTextField();
            this.jtfLicence.setColumns(10);
            this.jtfLicence.addFocusListener(this);
            this.jtfLicence.addCaretListener(this);
		}
		return this.jtfLicence;
	}
    
	/**
	 * This method initializes jtfNom	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfNom() {
		if (this.jtfNom == null) {
            this.jtfNom = new JTextField();
            this.jtfNom.setColumns(10);
            this.jtfNom.addFocusListener(this);
            this.jtfNom.addCaretListener(this);
		}
		return this.jtfNom;
	}
    
	/**
	 * This method initializes jtfClub	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfClub() {
		if (this.jtfClub == null) {
            this.jtfClub = new JTextField();
            this.jtfClub.setColumns(10);
            this.jtfClub.addFocusListener(this);
            this.jtfClub.addCaretListener(this);
		}
		return this.jtfClub;
	}
    
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getJTable() {
		if (this.jTable == null) {

            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.numlicence"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.nom"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.prenom"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.club"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.categorie"));
            dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("listeconcurrent.niveau"));
            
            generateRow();

            this.sorter = new TableSorter(dtm);
            this.jTable = new JTable(this.sorter);
            this.sorter.setTableHeader(this.jTable.getTableHeader());
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
            this.jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
            this.jbValider.setActionCommand("bouton.valider");
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
            this.jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
            this.jbAnnuler.setActionCommand("bouton.annuler");
            this.jbAnnuler.addActionListener(this);
		}
		return this.jbAnnuler;
	}
    
    private void generateRow() {
        for(Concurrent tireur : listeTireur.values()) {
            
            boolean isactive = true;
            Enumeration<Criterion> criteriaEnum = ConcoursJeunes.configuration.getCriteriaPopulation().keys();
            while(criteriaEnum.hasMoreElements()) {
                Criterion criterion = criteriaEnum.nextElement();
                ArrayList<CriterionElement> cp = ConcoursJeunes.configuration.getCriteriaPopulation().get(criterion);
                int criterionIndex = tireur.getDifferentiationCriteria().getCriterion(criterion.getCode());
                if(criterionIndex >= 0)
                	isactive = isactive && cp.size() > criterionIndex && cp.get(criterionIndex).isActive();
                else
                	isactive = false;
            }
            if(isactive) {
                String[] row = new String[6];
                row[0] = tireur.getLicence();
                row[1] = tireur.getNom();
                row[2] = tireur.getPrenom();
                row[3] = tireur.getClub();
                
                row[4] = "";
                for(Criterion key : ConcoursJeunes.configuration.getListCriteria()) {
                    if(!key.isPlacement()) {
                        ArrayList<CriterionElement> cp = ConcoursJeunes.configuration.getCriteriaPopulation().get(key);
                        row[4] += cp.get(tireur.getDifferentiationCriteria().getCriterion(key.getCode())).getCode();
                    }
                }
                row[5] = "";
                for(Criterion key : ConcoursJeunes.configuration.getListCriteria()) {
                    if(key.isPlacement()) {
                        ArrayList<CriterionElement> cp = ConcoursJeunes.configuration.getCriteriaPopulation().get(key);
                        row[5] += cp.get(tireur.getDifferentiationCriteria().getCriterion(key.getCode())).getCode();
                    }
                }
            
                dtm.addRow(row);
            }
        }
    }
	
    /**
     * initialise un nouveau concurrent
     * 
     * @param concurrent - l'objet concurrent à initialiser
     */
	public void initConcurrent(Concurrent concurrent) {
	    concurrent.setLicence(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getLicence());
	    concurrent.setNom(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getNom());
	    concurrent.setPrenom(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getPrenom());
	    concurrent.setClub(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getClub());
	    concurrent.setAgrement(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getAgrement());
	    concurrent.setDifferentiationCriteria(listeTireur.get(this.sorter.getValueAt(this.jTable.getSelectedRow(), 0)).getDifferentiationCriteria());
	}
    
    /**
     * @return Renvoie listeTireur.
     */
    public static Hashtable<String, Concurrent> getListeTireur() {
        return listeTireur;
    }

    /**
     * @param listeTireur listeTireur à définir.
     */
    public static void setListeTireur(Hashtable<String, Concurrent> listeTireur) {
        ConcurrentListDialog.listeTireur = listeTireur;
    }

    /**
     * renvoi si il y a eu validation
     * 
     * @return boolean
     */
	public boolean isValider() {
		return this.isValider;
	}
	
	/**
	 *  recupere la liste des licencie
	 * 
	 */
    private static boolean InputLicence(int numligue) {
        return InputLicence(numligue, 0);
    }
    
    @SuppressWarnings("unchecked")
	private static boolean InputLicence(int numligue, int indexFichier) {
		File f = new File(ConcoursJeunes.userRessources.getBasePathForUser() 
                + File.separator 
                + ConcoursJeunes.ajrParametreAppli.getResourceString("file.base.archers", numligue));
		if(f.exists()) {
			try {
                java.beans.XMLDecoder d = new java.beans.XMLDecoder(
                        new GZIPInputStream(
                            new FileInputStream(f)));
                if(indexFichier == 0)
                    listeTireur = (Hashtable<String, Concurrent>)d.readObject();
                else
                    listeTireur.putAll((Hashtable<String, Concurrent>)d.readObject());
				d.close();
			} catch (IOException io) {
				System.err.println("IOException: " + io.getMessage());
			} catch(NullPointerException npe) {
				System.err.println("Aucune restauration de archers.xml.gz possible. Action annulé");
			}
			
			return true;
		}
		/*JOptionPane.showMessageDialog(null,
                ConcoursJeunes.ajrLibelle.getResourceString("erreur.nobase"),
                ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);*/
		System.err.println("archers.xml.gz innexistant");
        return false;
	}
    
    private void moveTableRectView(int i) {
        this.jTable.setRowSelectionInterval(i,i);
        
        JViewport jvpTemp = this.jScrollPane.getViewport();
        Rectangle rectView = jvpTemp.getViewRect();
        rectView.y = this.jTable.getRowHeight()*i - jvpTemp.getViewPosition().y;
        
        this.jScrollPane.getViewport().scrollRectToVisible(rectView);
    }
	
	public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals("bouton.valider")) {
            this.isValider = jTable.getSelectedRow() > -1;
            this.setVisible(false);
        } else if(ae.getActionCommand().equals("bouton.annuler")) {
            this.isValider = false;
            this.setVisible(false);
        } else if(ae.getSource() == jcbLigue) {
            Entite entite = (Entite)jcbLigue.getSelectedItem();
            if(entite.getNom().equals("AUTRE")) {
                while(dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                listeTireur = null;
                
                Vector<Integer> numLigues = new  Vector<Integer>();
                for(int i = 0; i < ligues.size(); i++) {
                    if(!ligues.get(i).getAgrement().equals(""))
                        numLigues.add(Integer.parseInt(ligues.get(i).getAgrement().substring(0,2)));
                }
                Vector<Integer> key = new Vector<Integer>();
                for(Entite tmpent : ConcoursJeunes.listeEntite.values()) {
                    if(!tmpent.getAgrement().equals("")) {
                        int n = Integer.parseInt(tmpent.getAgrement().substring(0,2));
                        
                        if(!numLigues.contains(n) && !key.contains(n)) {
                            key.add(n);
                        }
                    }
                }
                int i = 0;
                for(int n : key) {
                    InputLicence(n, i++); 
                }
                
                generateRow();
            } else {
                String agrement = entite.getAgrement();

                while(dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                listeTireur = null;
                //System.gc();
                if(InputLicence(Integer.parseInt(agrement.substring(0,2)))) {
                    generateRow();
                }
            }
        }
	}
	public void caretUpdate(CaretEvent e) {
		if(e.getSource() == this.jtfLicence) {
			for(int i = 0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,0)).toLowerCase().startsWith(this.jtfLicence.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
		} else if(e.getSource() == this.jtfNom) {
	        for(int i = 0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,1)).toLowerCase().startsWith(this.jtfNom.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
	    } else {
	        for(int i=0; i < this.sorter.getRowCount(); i++) {
				if(((String)this.sorter.getValueAt(i,3)).toLowerCase().startsWith(this.jtfClub.getText().toLowerCase())) {
                    moveTableRectView(i);
					break;
				}
			}
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
    
	public void focusGained(FocusEvent e) {
		if(e.getSource() == this.jtfLicence) {
            this.sorter.setSortingStatus(1, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(0, TableSorter.ASCENDING);
		} else if(e.getSource() == this.jtfNom) {
            this.sorter.setSortingStatus(0, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(1, TableSorter.ASCENDING);
    	} else if(e.getSource() == this.jtfClub) {
            this.sorter.setSortingStatus(0, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(1, TableSorter.NOT_SORTED);
            this.sorter.setSortingStatus(3, TableSorter.ASCENDING);
    	}
	}
    
    public void focusLost(FocusEvent e) { }
}