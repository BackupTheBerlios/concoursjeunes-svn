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
 * @author Aurelien
 *
 */
public class TypeListingDialog extends JDialog implements ActionListener {
    public static final int ALPHA = 0;
    public static final int GREFFE = 1;
    public static final int NONE = 2;
    
    private JRadioButton jrbAlpha;
    private JRadioButton jrbGreffe;
    
    private JButton jbValider;
    private JButton jbAnnuler;
    
    private ButtonGroup jrbTypeExport;
    
    private int returnType = ALPHA;

    public TypeListingDialog(JFrame parentframe) {
        super(parentframe);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.titre")); //$NON-NLS-1$
        setModal(true);
        
        init();
    }
    
    private void init() {
        //Layout Manager
        GridBagConstraints c = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbAlpha = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.alpha"), true); //$NON-NLS-1$
        jrbGreffe = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.greffe")); //$NON-NLS-1$
        
        jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.imprimer")); //$NON-NLS-1$
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
        
        jrbAlpha.addActionListener(this);
        jrbGreffe.addActionListener(this);
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbAlpha);
        jrbTypeExport.add(jrbGreffe);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbValider);
        boutonPane.add(jbAnnuler);
        
        gridbagComposer.setParentPanel(exportPane);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jrbAlpha, c);
        c.gridy++;                        
        gridbagComposer.addComponentIntoGrid(jrbGreffe, c);
        c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;                 
        gridbagComposer.addComponentIntoGrid(boutonPane, c);
        
        getContentPane().add(exportPane);
    }
    
    public int showTypeListingDialog() {
    	pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        
        return returnType;
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jrbAlpha) {
            returnType = ALPHA;
        } else if(ae.getSource() == jrbGreffe) {
            returnType = GREFFE;
        } else if(ae.getSource() == jbValider) {
            setVisible(false);
        } else if(ae.getSource() == jbAnnuler) {
            returnType = NONE;
            setVisible(false);
        }
    }
}
