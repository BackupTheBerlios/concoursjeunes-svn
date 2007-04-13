/**
 * 
 */
package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.AJToolKit;
import ajinteractive.standard.java2.GridbagComposer;

/**
 * Boite de dialogue de gestion des critère de distinction des archers
 * @author  Aurelien
 */
public class CriterionDialog extends JDialog implements ActionListener {
	
	public static final int NO_LOCK = 0;
	public static final int PLACEMENT_LOCK = 1;
    
    private ReglementDialog parent;
    
    private Criterion criterion;
    
    private JLabel jlIndex = new JLabel();
    private JLabel jlCode = new JLabel();
    private JLabel jlLibelle = new JLabel();
    private JLabel jlSortOrder = new JLabel();
    private JLabel jlWinFFTACode = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JComboBox jcbSortOrder = new JComboBox();
    private JCheckBox jcbPlacementCriterion = new JCheckBox();
    private JCheckBox jcbClassementCriterion = new JCheckBox();
    private JComboBox jcbWinFFTACode = new JComboBox();
    
    private JButton jbValider = new JButton();
    private JButton jbAnnuler = new JButton();
    
    private int lock = NO_LOCK;
    
    /**
     * 
     * @param parent
     */
    public CriterionDialog(ReglementDialog parent) {
        this(parent, null);
    }
    
    /**
     * 
     * @param parent
     * @param criterion
     */
    public CriterionDialog(ReglementDialog parent, Criterion criterion) {
        super(parent, "", true); //$NON-NLS-1$
        
        this.parent = parent;
        this.criterion = criterion;
        
        init();
        affectLibelle();
        completePanel();
    }
    
    /**
     * 
     *
     */
    private void init() {
        //Layout Manager
        GridBagConstraints c    = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel jpGeneral = new JPanel();
        JPanel jpOperation = new JPanel();
        
        jpOperation.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        gridbagComposer.setParentPanel(jpGeneral);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jlIndex, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlCode, c);
        gridbagComposer.addComponentIntoGrid(jtfCode, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlLibelle, c);
        gridbagComposer.addComponentIntoGrid(jtfLibelle, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlSortOrder, c);
        gridbagComposer.addComponentIntoGrid(jcbSortOrder, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbPlacementCriterion, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbClassementCriterion, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlWinFFTACode, c);
        gridbagComposer.addComponentIntoGrid(jcbWinFFTACode, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    /**
     * 
     *
     */
    private void affectLibelle() {
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("criterion.titre")); //$NON-NLS-1$
        
        jlCode.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.code")); //$NON-NLS-1$
        jlLibelle.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.libelle")); //$NON-NLS-1$
        jlSortOrder.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri")); //$NON-NLS-1$
        jcbSortOrder.removeAllItems();
        jcbSortOrder.addItem(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri.asc")); //$NON-NLS-1$
        jcbSortOrder.addItem(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri.desc")); //$NON-NLS-1$
        jcbPlacementCriterion.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.placement")); //$NON-NLS-1$
        jcbClassementCriterion.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.classement")); //$NON-NLS-1$
        jlWinFFTACode.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.winfftacode.libelle")); //$NON-NLS-1$
        jcbWinFFTACode.removeAllItems();
        jcbWinFFTACode.addItem(""); //$NON-NLS-1$
        for(String critere : AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("criterion.winfftacode.code"), ",")) { //$NON-NLS-1$ //$NON-NLS-2$
            jcbWinFFTACode.addItem(critere);
        }
        
        jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
        jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
    }
    
    /**
     * 
     *
     */
    private void completePanel() {
        if(criterion != null) {
            jtfCode.setText(criterion.getCode());
            jtfCode.setEditable(false);
            jtfLibelle.setText(criterion.getLibelle());
            
            jcbSortOrder.setSelectedIndex((criterion.getSortOrder() > 0) ? 0 : 1);
            
            jcbPlacementCriterion.setSelected(criterion.isPlacement());
            jcbClassementCriterion.setSelected(criterion.isClassement());
            jcbWinFFTACode.setSelectedItem(criterion.getCodeffta());
            
            //jcbSortOrder.setEnabled(!parent.getWorkConfiguration().isOfficialProfile());
            jcbPlacementCriterion.setEnabled(!parent.getReglement().isOfficialReglement());
            jcbClassementCriterion.setEnabled(!parent.getReglement().isOfficialReglement());
            jcbWinFFTACode.setEnabled(!parent.getReglement().isOfficialReglement());
        }
        
        if(lock == PLACEMENT_LOCK) {
        	jcbPlacementCriterion.setEnabled(false);
        }
    }
    
    public Criterion showCriterionDialog() {
    	completePanel();
    	
    	pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    	
    	return criterion;
    }

    /**
	 * @return  Renvoie criterion.
	 * @uml.property  name="criterion"
	 */
    public Criterion getCriterion() {
        return criterion;
    }

    /**
	 * @param criterion  criterion à définir.
	 * @uml.property  name="criterion"
	 */
    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }
    
    /**
	 * @return lock
	 */
	public int getLock() {
		return lock;
	}

	/**
	 * @param lock lock à définir
	 */
	public void setLock(int lock) {
		this.lock = lock;
	}

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
            if(criterion == null) {
                criterion = new Criterion(jtfCode.getText());

                parent.getReglement().getListCriteria().add(criterion);
            }
            
            criterion.setSortOrder((jcbSortOrder.getSelectedIndex() == 1) ? Criterion.SORT_DESC : Criterion.SORT_ASC);
            criterion.setLibelle(jtfLibelle.getText());
            criterion.setPlacement(jcbPlacementCriterion.isSelected());
            criterion.setClassement(jcbClassementCriterion.isSelected());
            criterion.setCodeffta((String)jcbWinFFTACode.getSelectedItem());
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
        	criterion = null;
        	
            setVisible(false);
        }
    }
}
