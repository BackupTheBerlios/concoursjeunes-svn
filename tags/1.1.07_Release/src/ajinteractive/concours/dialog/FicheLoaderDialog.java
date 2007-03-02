package ajinteractive.concours.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ajinteractive.concours.*;

/**
 * Boite de dialogue de gestion chargement/création des diferents concours
 * 
 * @author Aurelien Jeoffray 07/03/2005
 * @version 1.0
 * 
 */
public class FicheLoaderDialog extends JDialog implements ActionListener {
    
    public static final int ANNULER     = 0;
    public static final int NOUVEAU     = 1;
    public static final int OUVRIR      = 2;
    public static final int SUPPRIMER   = 3;

	private String[] listFicheConcours;
	private int selectedIndex;
    private int action = ANNULER;
	
	private JList jListFicheConcours;
    private JButton jbNouveau;
	private JButton jbOuvrir;
    private JButton jbSupprimer;
	private JButton jbAnnuler;
	
	/**
	 * @param concoursJeunes - la fenetre mère dont dépend la boite de dialogue
	 * @param listFicheConcours - la liste des concours disponible
	 * @param selectedIndex - le concours à selectionner par défaut
	 */
	public FicheLoaderDialog(ConcoursJeunes concoursJeunes, String[] listFicheConcours, int selectedIndex) {
		super(concoursJeunes,ConcoursJeunes.ajrLibelle.getResourceString("ficheloader.titre"), true);
		this.listFicheConcours = listFicheConcours;
        this.selectedIndex = selectedIndex;
		
		initDialog();
	}
	
	/**
	 * 
	 */
	public void initDialog() {
		jListFicheConcours = new JList(listFicheConcours);
		JPanel jpListConcoursBut = new JPanel();
		
        jbNouveau = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.nouveau"));
		jbOuvrir = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ouvrir"));
        jbSupprimer = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer"));
		jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        jbNouveau.addActionListener(this);
        jbOuvrir.addActionListener(this);
        jbSupprimer.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		jpListConcoursBut.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jListFicheConcours.setSelectedIndex(selectedIndex);
		
        jpListConcoursBut.add(jbNouveau);
		jpListConcoursBut.add(jbOuvrir);
        jpListConcoursBut.add(jbSupprimer);
		jpListConcoursBut.add(jbAnnuler);
		
		this.getContentPane().add(new JScrollPane(jListFicheConcours), BorderLayout.CENTER);
		this.getContentPane().add(jpListConcoursBut, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(jbOuvrir);
		//this.setSize(370, 250);
        pack();
        this.setResizable(false);
        setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
    /**
     * affecte la liste des fiche concours existante
     * 
     * @param listFicheConcours
     */
	public void setListFicheConcours(String[] listFicheConcours) {
		this.listFicheConcours = listFicheConcours;
	}
	
    /**
     * renvoie la fiche selectionné
     * 
     * @return int
     */
	public int getSelectedFiche() {
		return selectedIndex;
	}
    
    /**
     * renvoie l'action a effectuer
     * 
     * @return int
     */
    public int getAction()  {
        return action;
    }
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jbNouveau) {
            action = NOUVEAU;
            
            this.setVisible(false);
        } else if(ae.getSource() == jbOuvrir) {
            selectedIndex = jListFicheConcours.getSelectedIndex();
            
            action = OUVRIR;
         
            this.setVisible(false);
        } else if(ae.getSource() == jbSupprimer) {
            selectedIndex = jListFicheConcours.getSelectedIndex();
            
            action = SUPPRIMER;
            
            this.setVisible(false);
		} else {
            action = ANNULER;
            
			this.setVisible(false);
		}
	}
}