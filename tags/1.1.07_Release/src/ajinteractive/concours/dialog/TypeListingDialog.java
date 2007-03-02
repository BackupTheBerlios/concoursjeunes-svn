/**
 * 
 */
package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ajinteractive.concours.*;
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
    
    public int returnType = ALPHA;

    public TypeListingDialog(ConcoursJeunes concoursJeunes) {
        super(concoursJeunes);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.titre"));
        setModal(true);
        
        init();
    }
    public void init() {
        //Layout Manager
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbAlpha = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.alpha"), true);
        jrbGreffe = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.greffe"));
        
        jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("typelisting.imprimer"));
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        
        jrbAlpha.addActionListener(this);
        jrbGreffe.addActionListener(this);
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbAlpha);
        jrbTypeExport.add(jrbGreffe);
        
        exportPane.setLayout(gridbag);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbValider);
        boutonPane.add(jbAnnuler);
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        AJToolKit.addComponentIntoGrid(exportPane, jrbAlpha, gridbag, c);
        c.gridy++;                        
        AJToolKit.addComponentIntoGrid(exportPane, jrbGreffe, gridbag, c);
        c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;                 
        AJToolKit.addComponentIntoGrid(exportPane, boutonPane, gridbag, c);
        
        getContentPane().add(exportPane);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
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
