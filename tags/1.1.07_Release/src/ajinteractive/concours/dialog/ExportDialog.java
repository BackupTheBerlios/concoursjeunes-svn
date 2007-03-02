package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

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

    public ExportDialog(ConcoursJeunes concoursJeunes) {
        super(concoursJeunes);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("export.titre"));
        setModal(true);
        
        init();
    }
    
    public void init() {
        //Layout Manager
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jrbInternet = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("export.radio.internet"), true);
        jrbFichier = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("export.radio.fichier"));
        
        jbExporter = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.exporter"));
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        
        jrbInternet.addActionListener(this);
        jrbFichier.addActionListener(this);
        jbExporter.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        jrbTypeExport = new ButtonGroup();
        jrbTypeExport.add(jrbInternet);
        jrbTypeExport.add(jrbFichier);
        
        exportPane.setLayout(gridbag);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbExporter);
        boutonPane.add(jbAnnuler);
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        AJToolKit.addComponentIntoGrid(exportPane, jrbInternet, gridbag, c);
        c.gridy++;                        
        AJToolKit.addComponentIntoGrid(exportPane, jrbFichier, gridbag, c);
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