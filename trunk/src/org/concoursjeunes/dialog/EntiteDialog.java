package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.GridbagComposer;

/**
 * @author Aurélien JEOFFRAY
 */
public class EntiteDialog extends JDialog implements ActionListener {
    //private ConcoursJeunes concoursJeunes;
    private Entite entite;

    private JTextField jtfNom;
    private JTextField jftfAgrement;
    private JTextField jtfAdresse;
    private JFormattedTextField jftfCodePostal;
    private JTextField jtfVille;
    private JComboBox jcbType;
    private JTextArea jtaNote;

    private JButton jbValider;
    private JButton jbAnnuler;

    public EntiteDialog(JFrame parent) {
	super(parent, "", true); //$NON-NLS-1$

	initialize();
    }

    public EntiteDialog(JDialog parent) {
	super(parent, "", true); //$NON-NLS-1$

	initialize();
    }

    public void initialize() {
	GridBagConstraints c = new GridBagConstraints();

	GridbagComposer gridbagComposer = new GridbagComposer();

	JPanel entitePane = new JPanel();
	JPanel buttonPane = new JPanel();

	JLabel jlNom = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.nom")); //$NON-NLS-1$
	JLabel jlAgrement = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.agrement")); //$NON-NLS-1$
	JLabel jlAdresse = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.adresse")); //$NON-NLS-1$
	JLabel jlCodePostal = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.codepostal")); //$NON-NLS-1$
	JLabel jlVille = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.ville")); //$NON-NLS-1$
	JLabel jlType = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.type")); //$NON-NLS-1$
	JLabel jlNote = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.note")); //$NON-NLS-1$

	jtfNom = new JTextField("", 30); //$NON-NLS-1$
	jtfNom.setEditable(false);
	jftfAgrement = new JTextField("", 6); //$NON-NLS-1$
    jftfAgrement.setEditable(false);
	jtfAdresse = new JTextField("", 30); //$NON-NLS-1$
	jtfVille = new JTextField("", 10); //$NON-NLS-1$
	jtfVille.setEditable(false);
	jcbType = new JComboBox(new String[] {"Fédération", "Ligue", "Comité Départemental", "Compagnie"});
	jtaNote = new JTextArea(5,30);

	MaskFormatter formatter;
	try {
	    formatter = new MaskFormatter("#####"); //$NON-NLS-1$
	    formatter.setPlaceholderCharacter('_');
	    jftfCodePostal = new JFormattedTextField(formatter);
	    jftfCodePostal.setColumns(4);
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	gridbagComposer.setParentPanel(entitePane);
	c.gridy = 0; c.anchor = GridBagConstraints.WEST;
	gridbagComposer.addComponentIntoGrid(jlNom, c);
	c.gridwidth = 3;
	gridbagComposer.addComponentIntoGrid(jtfNom, c);
	c.gridy++; c.gridwidth = 1;
	gridbagComposer.addComponentIntoGrid(jlAgrement, c);
	gridbagComposer.addComponentIntoGrid(jftfAgrement, c);
	gridbagComposer.addComponentIntoGrid(jlType, c);
	gridbagComposer.addComponentIntoGrid(jcbType, c);
	c.gridy++;
	gridbagComposer.addComponentIntoGrid(jlAdresse, c);
	c.gridwidth = 3;
	gridbagComposer.addComponentIntoGrid(jtfAdresse, c);
	c.gridy++; c.gridwidth = 1;
	gridbagComposer.addComponentIntoGrid(jlCodePostal, c);
	gridbagComposer.addComponentIntoGrid(jftfCodePostal, c);
	gridbagComposer.addComponentIntoGrid(jlVille, c);
	gridbagComposer.addComponentIntoGrid(jtfVille, c);
	c.gridy++;
	gridbagComposer.addComponentIntoGrid(jlNote, c);
	c.gridwidth = 3;
	gridbagComposer.addComponentIntoGrid(new JScrollPane(jtaNote), c);

	buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

	jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
	jbValider.addActionListener(this);
	jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	jbAnnuler.addActionListener(this);

	buttonPane.add(jbValider);
	buttonPane.add(jbAnnuler);

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(entitePane, BorderLayout.CENTER);
	getContentPane().add(buttonPane, BorderLayout.SOUTH);
	pack();
	setLocationRelativeTo(null);
    }

    public void showEntite(Entite curEntite) {
		if(curEntite != null) {
		    this.entite = curEntite;
	
		    jtfNom.setText(curEntite.getNom());
		    jtfAdresse.setText(curEntite.getAdresse());
		    jtfVille.setText(curEntite.getVille());
		    jcbType.setSelectedIndex(curEntite.getType());
		    jtaNote.setText(curEntite.getNote());
		    jftfAgrement.setText(curEntite.getAgrement());
		    jftfCodePostal.setValue(curEntite.getCodePostal());
		    setVisible(true);
		}
    }

    public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == jbAnnuler) {
		    setVisible(false);
		} else {
		    entite.setNom(jtfNom.getText());
		    entite.setAdresse(jtfAdresse.getText());
		    entite.setVille(jtfVille.getText());
		    entite.setType(jcbType.getSelectedIndex());
		    entite.setNote(jtaNote.getText());
		    entite.setAgrement(jftfAgrement.getText());
		    entite.setCodePostal((String)jftfCodePostal.getValue());
	
		    entite.save();
	
		    setVisible(false);
		}
    }
}
