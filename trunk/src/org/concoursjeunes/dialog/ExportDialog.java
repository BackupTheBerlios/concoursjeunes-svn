package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.*;

/**
 * Boite de dialogue d'export des résultats du concours
 * 
 * @author Aurelien Jeoffray
 * @version 1.0
 *
 */
public class ExportDialog extends JDialog implements ActionListener {
    
    public static final int EXPORT_NONE = 0;
    public static final int EXPORT_FILE = 1;
    public static final int EXPORT_INTERNET = 2;
   
    private JRadioButton jrbInternet;
    private JRadioButton jrbFichier;
    
    private JButton jbExporter;
    private JButton jbAnnuler;
    
    private ButtonGroup jrbTypeExport;
    
    public int exportType = EXPORT_INTERNET;

    public ExportDialog(JFrame parentframe) {
        super(parentframe);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("export.titre")); //$NON-NLS-1$
        setModal(true);
        
        init();
    }
    
    public void init() {
        //Layout Manager
        GridBagConstraints c = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbInternet = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("export.radio.internet"), true); //$NON-NLS-1$
        jrbFichier = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("export.radio.fichier")); //$NON-NLS-1$
        
        jbExporter = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.exporter")); //$NON-NLS-1$
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
        
        jrbInternet.addActionListener(this);
        jrbFichier.addActionListener(this);
        jbExporter.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbInternet);
        jrbTypeExport.add(jrbFichier);

        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbExporter);
        boutonPane.add(jbAnnuler);
        
        gridbagComposer.setParentPanel(exportPane);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jrbInternet, c);
        c.gridy++;                        
        gridbagComposer.addComponentIntoGrid(jrbFichier, c);
        c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;                 
        gridbagComposer.addComponentIntoGrid(boutonPane, c);
        
        getContentPane().add(exportPane);
        pack();
        this.setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jrbFichier) {
            exportType = EXPORT_FILE;
        } else if(ae.getSource() == jrbInternet) {
            exportType = EXPORT_INTERNET;
        } else if(ae.getSource() == jbExporter) {
            setVisible(false);
        } else if(ae.getSource() == jbAnnuler) {
            exportType = EXPORT_NONE;
            setVisible(false);
        }
    }
}