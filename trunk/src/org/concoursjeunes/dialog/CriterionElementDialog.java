/**
 * 
 */
package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.*;

/**
 * @author Aurélien JEOFFRAY
 */
public class CriterionElementDialog extends JDialog implements ActionListener {
    
    private ReglementDialog parent;
    private Criterion criterion;
    private CriterionElement criterionIndividu;
    
    private JLabel jlCode = new JLabel();
    private JLabel jlLibelle = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JCheckBox jcbActive = new JCheckBox("", true); //$NON-NLS-1$
    
    private JButton jbValider = new JButton();
    private JButton jbAnnuler = new JButton();

    public CriterionElementDialog(ReglementDialog parent, Criterion criterion) {
        this(parent, criterion, null);
    }
    public CriterionElementDialog(ReglementDialog parent, Criterion criterion, CriterionElement criterionIndividu) {
        super(parent, ConcoursJeunes.ajrLibelle.getResourceString("criterion.titre"), true); //$NON-NLS-1$
        
        this.parent = parent;
        this.criterion = criterion;
        this.criterionIndividu = criterionIndividu;
        
        init();
        affectLibelle();
        completePanel();
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
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
        gridbagComposer.addComponentIntoGrid(jlCode, c);
        gridbagComposer.addComponentIntoGrid(jtfCode, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlLibelle, c);
        gridbagComposer.addComponentIntoGrid(jtfLibelle, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbActive, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    private void affectLibelle() {
        jlCode.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.code")); //$NON-NLS-1$
        jlLibelle.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.libelle")); //$NON-NLS-1$
        jcbActive.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.active")); //$NON-NLS-1$
        
        jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
        jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
    }
    
    private void completePanel() {
        if(criterionIndividu != null) {
            jtfCode.setText(criterionIndividu.getCode());
            jtfLibelle.setText(criterionIndividu.getLibelle());
                       
            jcbActive.setSelected(criterionIndividu.isActive());
            
            jtfCode.setEditable(!parent.getReglement().isOfficialReglement()
            		&& parent.getVerrou() == ReglementDialog.NO_LOCK);
            jcbActive.setEnabled(!parent.getReglement().isOfficialReglement());
        }
    }

    /**
	 * @return  Renvoie criterionIndividu.
	 * @uml.property  name="criterionIndividu"
	 */
    public CriterionElement getCriterionIndividu() {
        return criterionIndividu;
    }

    /**
	 * @param criterionIndividu  criterionIndividu à définir.
	 * @uml.property  name="criterionIndividu"
	 */
    public void setCriterionIndividu(CriterionElement criterionIndividu) {
        this.criterionIndividu = criterionIndividu;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
            if(criterionIndividu == null) {
                criterionIndividu = new CriterionElement();
                criterionIndividu.setCriterionParent(criterion);
                
                criterion.getCriterionElements().add(criterionIndividu);
            }
            
            criterionIndividu.setCode(jtfCode.getText());
            criterionIndividu.setLibelle(jtfLibelle.getText());
            criterionIndividu.setActive(jcbActive.isSelected());
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
            setVisible(false);
        }
    }
}
