package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import ajinteractive.standard.java2.AJToolKit;
import ajinteractive.concours.*;

/**
 * @author Aurelien
 *
 */
public class EntiteDialog extends JDialog implements ActionListener {
	//private ConcoursJeunes concoursJeunes;
	private Entite entite;
	
	private JTextField jtfNom;
	private JFormattedTextField jftfAgrement;
	private JTextField jtfAdresse;
	private JFormattedTextField jftfCodePostal;
	private JTextField jtfVille;
	private JComboBox jcbType;
	private JTextArea jtaNote;
	
	private JButton jbValider;
	private JButton jbAnnuler;
	
	public EntiteDialog(Window parent) {
	    //super(parent, "", true);
        setModal(true);
		
		initialize();
	}
	
	public void initialize() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel entitePane = new JPanel();
		JPanel buttonPane = new JPanel();
		
		JLabel jlNom = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.nom"));
		JLabel jlAgrement = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.agrement"));
		JLabel jlAdresse = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.adresse"));
		JLabel jlCodePostal = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.codepostal"));
		JLabel jlVille = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.ville"));
		JLabel jlType = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.type"));
		JLabel jlNote = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("entite.note"));
		
		jtfNom = new JTextField("", 30);
		jtfAdresse = new JTextField("", 30);
		jtfVille = new JTextField("", 10);
		jcbType = new JComboBox(new String[] {"Fédération", "Ligue", "Comité Départemental", "Compagnie"});
		jtaNote = new JTextArea(5,30);
		
		MaskFormatter formatter;
		try {
			formatter = new MaskFormatter("#######");
			formatter.setPlaceholderCharacter('_');
			jftfAgrement = new JFormattedTextField(formatter);
			jftfAgrement.setColumns(6);
			formatter = new MaskFormatter("#####");
			formatter.setPlaceholderCharacter('_');
			jftfCodePostal = new JFormattedTextField(formatter);
			jftfCodePostal.setColumns(4);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		entitePane.setLayout(gridbag);
		
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		AJToolKit.addComponentIntoGrid(entitePane, jlNom, gridbag, c);
		c.gridwidth = 3;
		AJToolKit.addComponentIntoGrid(entitePane, jtfNom, gridbag, c);
		c.gridy++; c.gridwidth = 1;
		AJToolKit.addComponentIntoGrid(entitePane, jlAgrement, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jftfAgrement, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jlType, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jcbType, gridbag, c);
		c.gridy++;
		AJToolKit.addComponentIntoGrid(entitePane, jlAdresse, gridbag, c);
		c.gridwidth = 3;
		AJToolKit.addComponentIntoGrid(entitePane, jtfAdresse, gridbag, c);
		c.gridy++; c.gridwidth = 1;
		AJToolKit.addComponentIntoGrid(entitePane, jlCodePostal, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jftfCodePostal, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jlVille, gridbag, c);
		AJToolKit.addComponentIntoGrid(entitePane, jtfVille, gridbag, c);
		c.gridy++;
		AJToolKit.addComponentIntoGrid(entitePane, jlNote, gridbag, c);
		c.gridwidth = 3;
		AJToolKit.addComponentIntoGrid(entitePane, new JScrollPane(jtaNote), gridbag, c);
		
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
		jbValider.addActionListener(this);
		jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
		jbAnnuler.addActionListener(this);
		
		buttonPane.add(jbValider);
		buttonPane.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(entitePane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
	}
	
	public void showEntite(Entite entite) {
		if(entite != null) {
			this.entite = entite;
			
			jtfNom.setText(entite.getNom());
			jtfAdresse.setText(entite.getAdresse());
			jtfVille.setText(entite.getVille());
			jcbType.setSelectedIndex(entite.getType());
			jtaNote.setText(entite.getNote());
			jftfAgrement.setValue(entite.getAgrement());
			jftfCodePostal.setValue(entite.getCodePostal());
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
			entite.setAgrement((String)jftfAgrement.getValue());
			entite.setCodePostal((String)jftfCodePostal.getValue());
			
			new Thread(new Runnable() {
				public void run() {
					try {
						File f = new File(ConcoursJeunes.userRessources.getBasePathForUser() + File.separator + "organisations.xml.gz");
                        AJToolKit.saveXMLStructure(f, ConcoursJeunes.listeEntite, true);
					} catch(NullPointerException npe) {
						JOptionPane.showMessageDialog(EntiteDialog.this,
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.entite"),
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
			            npe.printStackTrace();
					}
				}
			}).start();
			
			
			setVisible(false);
		}
	}
}
