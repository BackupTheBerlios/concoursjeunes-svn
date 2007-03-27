package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ajinteractive.standard.java2.AJToolKit;
import ajinteractive.concours.*;

/**
 * Boite de dialogue d'import d'une base de donné d'archer
 * 
 * @author Aurelien Jeoffray
 * @version 1.0
 * 
 */
public class ImportDialog extends JDialog implements ActionListener {
    
    public static final int IMPORT_NONE = 0;
    public static final int IMPORT_FILE = 1;
    public static final int IMPORT_INTERNET = 2;
    public static final int IMPORT_FFTA = 3;
    public static final int IMPORT_RESULTARC = 4;
    
    private JRadioButton jrbInternet;
    private JRadioButton jrbFichier;
    private JRadioButton jrbFFTA;
    private JRadioButton jrbResultArc;
    
    private JButton jbImporter;
    private JButton jbAnnuler;
    
    private ButtonGroup jrbTypeExport;
    
    public int importType = IMPORT_INTERNET;

    public ImportDialog(ConcoursJeunes concoursJeunes) {
        super(concoursJeunes);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("import.titre"));
        setModal(true);
        
        init();
    }
    public void init() {
        //Layout Manager
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbInternet = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.internet"), true);
        jrbFichier = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.fichier"));
        jrbFFTA = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.ffta"));
        jrbResultArc = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("import.radio.resultarc"));
        
        jbImporter = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.importer"));
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        
        jrbInternet.addActionListener(this);
        jrbFichier.addActionListener(this);
        jrbFFTA.addActionListener(this);
        jrbResultArc.addActionListener(this);
        jbImporter.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbInternet);
        jrbTypeExport.add(jrbFichier);
        jrbTypeExport.add(jrbFFTA);
        jrbTypeExport.add(jrbResultArc);
        
        exportPane.setLayout(gridbag);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbImporter);
        boutonPane.add(jbAnnuler);
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        AJToolKit.addComponentIntoGrid(exportPane, jrbInternet, gridbag, c);
        c.gridy++;                        
        AJToolKit.addComponentIntoGrid(exportPane, jrbFichier, gridbag, c);
        c.gridy++;                        
        AJToolKit.addComponentIntoGrid(exportPane, jrbFFTA, gridbag, c);
        c.gridy++;                        
        AJToolKit.addComponentIntoGrid(exportPane, jrbResultArc, gridbag, c);
        c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;                 
        AJToolKit.addComponentIntoGrid(exportPane, boutonPane, gridbag, c);
        
        getContentPane().add(exportPane);
        pack();
        this.setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jrbFichier) {
            importType = IMPORT_FILE;
        } else if(ae.getSource() == jrbInternet) {
            importType = IMPORT_INTERNET;
        } else if(ae.getSource() == jrbFFTA) {
            importType = IMPORT_FFTA;
        } else if(ae.getSource() == jrbResultArc) {
        	importType = IMPORT_RESULTARC;
        } else if(ae.getSource() == jbImporter) {
            setVisible(false);
        } else if(ae.getSource() == jbAnnuler) {
            importType = IMPORT_NONE;
            setVisible(false);
        }
    }
}