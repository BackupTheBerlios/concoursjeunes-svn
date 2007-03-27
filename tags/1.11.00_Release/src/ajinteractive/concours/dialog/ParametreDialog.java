/*
 * Created on 1 janv. 2005
 *
 */
package ajinteractive.concours.dialog;

import java.text.ParseException;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * Boite de dialogue de gestion des parametre du concours
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */
public class ParametreDialog extends JDialog implements ActionListener {
	
//private:
	private FicheConcours ficheConcours;

	private JPanel jContentPane;
	private JPanel jpParametre;
	private JPanel jpValidation;
	
	//private JButton jbLogo;
	private JTextField jtfIntituleConcours;
	private JFormattedTextField jtfDateConcours;
	private JTextField jtfNombreCible;
	private JComboBox jcbNombreTireurParCible;
	private JTextField jtfNombreDepart;
	private JTextField jtfArbitres;
	private JButton jbAjouterArbitre;
	private JButton jbSupprimerArbitre;
	private JButton jbArbitreResponsable;
	private JList jlArbitres;
	
	private JButton jbValider;
	private JButton jbAnnuler;
	
	private boolean annulation = false;
	
	public ParametreDialog(FicheConcoursFrame ficheConcoursFrame) {
		super(ficheConcoursFrame.concoursJeunes);
		
		this.ficheConcours = ficheConcoursFrame.ficheConcours;
		
		initialize();
	}
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("parametre.titre"));
		this.setContentPane(getJContentPane());
        this.getRootPane().setDefaultButton(jbValider);
		this.setModal(true);
		this.pack();
        this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
    
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJpParametre(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
    
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJpParametre() {
		if (jpParametre == null) {
			JLabel jlIntituleConcours = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.intituleconcours"));
			JLabel jlDateConcours = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.dateconcours"));
			JLabel jlNombreCible = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombrecible"));
			JLabel jlNombreTireurParCible = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombretireurparcible"));
			JLabel jlNombreDepart = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombredepart"));
			JLabel jlArbitres = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.arbitres"));
			
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			
			jpParametre = new JPanel();
			
			jpParametre.setLayout(gridbag);
			
			//TODO Le nombre de départ n'est pas gérer
			jlNombreDepart.setEnabled(false);
			
			c.gridy = 0; c.gridheight = 1; c.anchor = GridBagConstraints.WEST; c.ipadx = 2;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlIntituleConcours, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfIntituleConcours(), gridbag, c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlDateConcours, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfDateConcours(), gridbag, c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlNombreCible, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfNombreCible(), gridbag, c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlNombreTireurParCible, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfNombreTireurParCible(), gridbag, c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlNombreDepart, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfNombreDepart(), gridbag, c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, jlArbitres, gridbag, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJtfArbitres(), gridbag, c);
			c.gridx = 3; c.fill = GridBagConstraints.HORIZONTAL;
			AJToolKit.addComponentIntoGrid(jpParametre, getJbAjouterArbitre(), gridbag, c);
			
			c.gridy++;
			c.gridx = 2; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
			AJToolKit.addComponentIntoGrid(jpParametre, new JScrollPane(getJlArbitres()), gridbag, c);
			c.gridx = 3; c.gridheight = 1;
			AJToolKit.addComponentIntoGrid(jpParametre, getJbSupprimerArbitre(), gridbag, c);
			
			c.gridy++; c.anchor = GridBagConstraints.NORTH;
			AJToolKit.addComponentIntoGrid(jpParametre, getJbArbitreResponsable(), gridbag, c);
			
			c.gridy+=2; c.gridwidth = 2;
			c.gridx = 2; c.anchor = GridBagConstraints.EAST; c.fill = GridBagConstraints.NONE; //c.anchor = GridBagConstraints.EAST;
			AJToolKit.addComponentIntoGrid(jpParametre, getJpValidation(), gridbag, c);
		}
		return jpParametre;
	}
    
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfIntituleConcours() {
		if (jtfIntituleConcours == null) {
			jtfIntituleConcours = new JTextField();
			jtfIntituleConcours.setColumns(20);
			jtfIntituleConcours.setText(ficheConcours.parametre.getIntituleConcours());
		}
		return jtfIntituleConcours;
	}
    
	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfDateConcours() {
		if (jtfDateConcours == null) {
            MaskFormatter formatter;
            try {
                formatter = new MaskFormatter("##'/##'/####");
                formatter.setPlaceholderCharacter('_');
                jtfDateConcours = new JFormattedTextField(formatter);
                jtfDateConcours.setColumns(7);
                jtfDateConcours.setValue(ficheConcours.parametre.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
		}
		return jtfDateConcours;
	}
    
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJtfNombreCible() {
		if (jtfNombreCible == null) {
			jtfNombreCible = new JTextField(new NumberDocument(false, false), ""+ficheConcours.parametre.getNbCible(), 3);
		}
		return jtfNombreCible;
	}
    
	private JComboBox getJtfNombreTireurParCible() {
		if (jcbNombreTireurParCible == null) {
			jcbNombreTireurParCible = new JComboBox();
			for(int i = 2; i <= 6; i+=2)
				jcbNombreTireurParCible.addItem(i);
			jcbNombreTireurParCible.setSelectedIndex((ficheConcours.parametre.getNbTireur() / 2) - 1);
		}
		return jcbNombreTireurParCible;
	}
    
	private JTextField getJtfNombreDepart() {
		if (jtfNombreDepart == null) {
			jtfNombreDepart = new JTextField(new NumberDocument(false, false), ""+ficheConcours.parametre.getNbDepart(), 3);
			jtfNombreDepart.setEnabled(false);
		}
		return jtfNombreDepart;
	}
    
	private JTextField getJtfArbitres() {
		if (jtfArbitres == null) {
			jtfArbitres = new JTextField();
			jtfArbitres.setColumns(20);
			jtfArbitres.addActionListener(this);
		}
		return jtfArbitres;
	}
    
	private JList getJlArbitres() {
		if (jlArbitres == null) {
			jlArbitres = new JList(ficheConcours.parametre.getArbitres().toArray());
		}
		return jlArbitres;
	}

	private JButton getJbAjouterArbitre() {
		if (jbAjouterArbitre == null) {
			jbAjouterArbitre = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter"));
			jbAjouterArbitre.addActionListener(this);
		}
		return jbAjouterArbitre;
	}
    
	private JButton getJbSupprimerArbitre() {
		if (jbSupprimerArbitre == null) {
			jbSupprimerArbitre = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer"));
			jbSupprimerArbitre.addActionListener(this);
		}
		return jbSupprimerArbitre;
	}
    
	private JButton getJbArbitreResponsable() {
		if (jbArbitreResponsable == null) {
			jbArbitreResponsable = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.arbitreresponsable"));
			jbArbitreResponsable.addActionListener(this);
		}
		return jbArbitreResponsable;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJpValidation() {
		if (jpValidation == null) {
			jpValidation = new JPanel();
			jpValidation.add(getJbValider(), null);
			jpValidation.add(getJbAnnuler(), null);
		}
		return jpValidation;
	}
    
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJbValider() {
		if (jbValider == null) {
			jbValider = new JButton();
			jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
			jbValider.addActionListener(this);
		}
		return jbValider;
	}
    
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJbAnnuler() {
		if (jbAnnuler == null) {
			jbAnnuler = new JButton();
			jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
			jbAnnuler.addActionListener(this);
		}
		return jbAnnuler;
	}

    /**
     * Donne si l'action est annule ou non
     * 
     * @return boolean
     */
	public boolean isAnnule() {
		return annulation;
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == jbValider) {
			ficheConcours.parametre.setIntituleConcours(jtfIntituleConcours.getText());
			ficheConcours.parametre.setDate(jtfDateConcours.getText());
			ficheConcours.parametre.setNbCible(Integer.parseInt(jtfNombreCible.getText()));
			ficheConcours.parametre.setNbTireur((Integer)jcbNombreTireurParCible.getSelectedItem());
			ficheConcours.parametre.setNbDepart(Integer.parseInt(jtfNombreDepart.getText()));
			
			//sauvegarde en tache de fond
			ficheConcours.silentSave();
			
			setVisible(false);
		} else if(ae.getSource() == jbAnnuler) {
			annulation = true;
			setVisible(false);
		} else if(ae.getSource() == jbAjouterArbitre || ae.getSource() == jtfArbitres) {
			ficheConcours.parametre.getArbitres().add(jtfArbitres.getText());
			jtfArbitres.setText("");
			jlArbitres.setListData(ficheConcours.parametre.getArbitres().toArray());
		} else if(ae.getSource() == jbSupprimerArbitre) {
			ficheConcours.parametre.getArbitres().remove(jlArbitres.getSelectedIndex());
			jlArbitres.setListData(ficheConcours.parametre.getArbitres().toArray());
		} else if(ae.getSource() == jbArbitreResponsable && jlArbitres.getSelectedIndex() > -1) {
			//cherche si il existe un arbitre responsable
			boolean resp = false;
			for(int i = 0; i < ficheConcours.parametre.getArbitres().size(); i++) {
				if(ficheConcours.parametre.getArbitres().get(i).startsWith("*")) {
					resp = true;
					
					//si il en existe 1 et qu'il est different de celui que l'on veut
					if(i != jlArbitres.getSelectedIndex()) {
						//affecter à ce statut alors retirer l'* de la selection precedente
						String strArbitreResponsable = ficheConcours.parametre.getArbitres().get(i);
						ficheConcours.parametre.getArbitres().set(i, strArbitreResponsable.substring(1));
						
						//et l'ajouter sur la nouvelle selection
						strArbitreResponsable = ficheConcours.parametre.getArbitres().get(jlArbitres.getSelectedIndex());
						ficheConcours.parametre.getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable);
						
						jlArbitres.setListData(ficheConcours.parametre.getArbitres().toArray());
					}
					
					break;
				}
			}
			
			if(!resp) {
				String strArbitreResponsable = ficheConcours.parametre.getArbitres().get(jlArbitres.getSelectedIndex());
				ficheConcours.parametre.getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable);
				
				jlArbitres.setListData(ficheConcours.parametre.getArbitres().toArray());
			}
		}
	}
}