package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import org.concoursjeunes.*;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;

import ajinteractive.standard.java2.AJList;
import ajinteractive.standard.java2.GridbagComposer;

/**
 * Boite de dialogue d'import d'une base de donné d'archer
 * 
 * @author Aurelien Jeoffray
 * @version 1.0
 * 
 * TODO Lister dans la boite de dialogue les plugins d'import disponible
 */
public class ImportDialog extends JDialog implements ActionListener {   
    private AJList jlPlugins = new AJList();
    
    private JButton jbImporter;
    private JButton jbAnnuler;
    
    private String importClass = null;
    ArrayList<PluginMetadata> plugins = new ArrayList<PluginMetadata>();

    public ImportDialog(JFrame parentframe) {
        super(parentframe);
        
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("import.titre")); //$NON-NLS-1$
        setModal(true);
        
        init();
    }
    
    private void init() {
        //Layout Manager
        GridBagConstraints c = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel exportPane = new JPanel();
        JPanel boutonPane = new JPanel();
        
        jbImporter = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.importer")); //$NON-NLS-1$
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
        
        jbImporter.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        boutonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boutonPane.add(jbImporter);
        boutonPane.add(jbAnnuler);
        
        gridbagComposer.setParentPanel(exportPane);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;                       //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(new JScrollPane(jlPlugins), c);
        c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;                 
        gridbagComposer.addComponentIntoGrid(boutonPane, c);
        
        getContentPane().add(exportPane);
    }
    
    public String showImportDialog() {
    	PluginLoader pl = new PluginLoader();
    	plugins = pl.getPlugins(PluginMetadata.IMPORT_PLUGIN);
    	for(PluginMetadata pm : plugins) {
    		jlPlugins.add(pm.getOptionLabel());
    	}
    	
    	pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    	
    	return importClass;
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jbImporter) {
        	importClass = plugins.get(jlPlugins.getSelectedIndex()).getClassName();
            setVisible(false);
        } else if(ae.getSource() == jbAnnuler) {
            importClass = null;
            setVisible(false);
        }
    }
}