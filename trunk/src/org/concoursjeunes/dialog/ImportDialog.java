package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.GridbagComposer;

/**
 * Boite de dialogue d'import d'une base de donné d'archer
 * 
 * @author Aurelien Jeoffray
 * @version 1.0
 * 
 */
public class ImportDialog extends JDialog implements ActionListener {
    
    public static final String IMPORT_FILE = "FileImportPlugin"; //$NON-NLS-1$
    public static final String IMPORT_INTERNET = "InternetImportPlugin"; //$NON-NLS-1$
    public static final String IMPORT_FFTA = "FFTAImportPlugin"; //$NON-NLS-1$
    
    private JRadioButton jrbInternet;
    private JRadioButton jrbFichier;
    private JRadioButton jrbFFTA;
    
    private JButton jbImporter;
    private JButton jbAnnuler;
    
    private ButtonGroup jrbTypeExport;
    
    public String importClass = IMPORT_INTERNET;

    public ImportDialog(JFrame parentframe) {
        super(parentframe);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("import.titre")); //$NON-NLS-1$
        setModal(true);
        
        init();
    }
    
    public void init() {
        //Layout Manager
        GridBagConstraints c = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbInternet = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.internet"), true); //$NON-NLS-1$
        jrbFichier = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.fichier")); //$NON-NLS-1$
        jrbFFTA = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.ffta")); //$NON-NLS-1$
        
        jbImporter = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.importer")); //$NON-NLS-1$
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
        
        jrbInternet.addActionListener(this);
        jrbFichier.addActionListener(this);
        jrbFFTA.addActionListener(this);
        jbImporter.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbInternet);
        jrbTypeExport.add(jrbFichier);
        jrbTypeExport.add(jrbFFTA);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbImporter);
        boutonPane.add(jbAnnuler);
        
        gridbagComposer.setParentPanel(exportPane);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jrbInternet, c);
        c.gridy++;                        
        gridbagComposer.addComponentIntoGrid(jrbFichier, c);
        c.gridy++;                        
        gridbagComposer.addComponentIntoGrid(jrbFFTA, c);
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
            importClass = IMPORT_FILE;
        } else if(ae.getSource() == jrbInternet) {
            importClass = IMPORT_INTERNET;
        } else if(ae.getSource() == jrbFFTA) {
            importClass = IMPORT_FFTA;
        } else if(ae.getSource() == jbImporter) {
            setVisible(false);
        } else if(ae.getSource() == jbAnnuler) {
            importClass = null;
            setVisible(false);
        }
    }
}