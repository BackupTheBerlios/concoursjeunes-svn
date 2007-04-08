/*
 * Created on 1 janv. 2005
 *
 */
package org.concoursjeunes.dialog;

import java.text.DateFormat;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.concoursjeunes.*;
import org.concoursjeunes.ui.FicheConcoursPane;
import org.jdesktop.swingx.JXDatePicker;

import ajinteractive.standard.java2.*;

/**
 * Boite de dialogue de gestion des parametre du concours
 * @author  Aurelien Jeoffray
 * @version  2.0
 */
public class ParametreDialog extends JDialog implements ActionListener {
	
//private:
	private FicheConcours ficheConcours;

	private JPanel jContentPane;
	private JPanel jpParametre;
	private JPanel jpValidation;
	
	//private JButton jbLogo;
	private JTextField jtfIntituleConcours;
	//private JFormattedTextField jtfDateConcours;
	private JXDatePicker jtfDateConcours;
	private JComboBox jcbReglement;
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
	
	public ParametreDialog(FicheConcoursPane ficheConcoursFrame) {
		super(ficheConcoursFrame.getParentframe());
		
		this.ficheConcours = ficheConcoursFrame.ficheConcours;
		
		init();
	}
    
	/**
	 * This method initializes this
	 */
	private void init() {
		this.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("parametre.titre")); //$NON-NLS-1$
		this.setContentPane(getJContentPane());
        this.getRootPane().setDefaultButton(jbValider);
		this.setModal(true);
		this.pack();
        this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	public void showParametreDialog() {
		this.setVisible(true);
	}
    
	/**
	 * This method initializes jContentPane
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jContentPane"
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
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jpParametre"
	 */    
	private JPanel getJpParametre() {
		if (jpParametre == null) {
			JLabel jlIntituleConcours = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.intituleconcours")); //$NON-NLS-1$
			JLabel jlDateConcours = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.dateconcours")); //$NON-NLS-1$
			JLabel jlReglement = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.reglement")); //$NON-NLS-1$
			JLabel jlNombreCible = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombrecible")); //$NON-NLS-1$
			JLabel jlNombreTireurParCible = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombretireurparcible")); //$NON-NLS-1$
			JLabel jlNombreDepart = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombredepart")); //$NON-NLS-1$
			JLabel jlbArbitres = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("parametre.arbitres")); //$NON-NLS-1$

			GridBagConstraints c = new GridBagConstraints();
			
			GridbagComposer gridbagComposer = new GridbagComposer();
			
			jpParametre = new JPanel();

			gridbagComposer.setParentPanel(jpParametre);
			c.gridy = 0; c.gridheight = 1; c.anchor = GridBagConstraints.WEST; c.ipadx = 2;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfIntituleConcours(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlDateConcours, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfDateConcours(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlReglement, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJcbReglement(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlNombreCible, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfNombreCible(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlNombreTireurParCible, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfNombreTireurParCible(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlNombreDepart, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfNombreDepart(), c);
			
			c.gridy++;
			c.gridx = 1; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(jlbArbitres, c);
			c.gridx = 2; //c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(getJtfArbitres(), c);
			c.gridx = 3; c.fill = GridBagConstraints.HORIZONTAL;
			gridbagComposer.addComponentIntoGrid(getJbAjouterArbitre(), c);
			
			c.gridy++;
			c.gridx = 2; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
			gridbagComposer.addComponentIntoGrid(new JScrollPane(getJlArbitres()), c);
			c.gridx = 3; c.gridheight = 1;
			gridbagComposer.addComponentIntoGrid(getJbSupprimerArbitre(), c);
			
			c.gridy++; c.anchor = GridBagConstraints.NORTH;
			gridbagComposer.addComponentIntoGrid(getJbArbitreResponsable(), c);
			
			c.gridy+=2; c.gridwidth = 2;
			c.gridx = 2; c.anchor = GridBagConstraints.EAST; c.fill = GridBagConstraints.NONE; //c.anchor = GridBagConstraints.EAST;
			gridbagComposer.addComponentIntoGrid(getJpValidation(), c);
		}
		return jpParametre;
	}
    
	/**
	 * This method initializes jTextField2	
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfIntituleConcours"
	 */    
	private JTextField getJtfIntituleConcours() {
		if (jtfIntituleConcours == null) {
			jtfIntituleConcours = new JTextField();
			jtfIntituleConcours.setColumns(20);
			jtfIntituleConcours.setText(ficheConcours.getParametre().getIntituleConcours());
		}
		return jtfIntituleConcours;
	}
    
	/**
	 * This method initializes jTextField3	
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfDateConcours"
	 */    
	private JXDatePicker getJtfDateConcours() {
		if (jtfDateConcours == null) {
            jtfDateConcours = new JXDatePicker();
            jtfDateConcours.setFormats(new DateFormat[] {DateFormat.getDateInstance(DateFormat.SHORT)});
            jtfDateConcours.setDate(ficheConcours.getParametre().getDate());
		}
		return jtfDateConcours;
	}
	
	private JComboBox getJcbReglement() {
		if (jcbReglement == null) {
			jcbReglement = new JComboBox();
			for(String name : ConcoursJeunes.userRessources.listAvailableReglements()) {
				jcbReglement.addItem(name);
			}
			jcbReglement.setSelectedItem(ficheConcours.getParametre().getReglement().getName());
			jcbReglement.setEnabled(!ficheConcours.getParametre().isReglementLock());
		}
		return jcbReglement;
	}
    
	/**
	 * This method initializes jTextField	
	 * @return  javax.swing.JTextField
	 * @uml.property  name="jtfNombreCible"
	 */    
	private JTextField getJtfNombreCible() {
		if (jtfNombreCible == null) {
			jtfNombreCible = new JTextField(new NumberDocument(false, false), ""+ficheConcours.getParametre().getNbCible(), 3); //$NON-NLS-1$
		}
		return jtfNombreCible;
	}
    
	private JComboBox getJtfNombreTireurParCible() {
		if (jcbNombreTireurParCible == null) {
			jcbNombreTireurParCible = new JComboBox();
			for(int i = 2; i <= 6; i+=2)
				jcbNombreTireurParCible.addItem(i);
			jcbNombreTireurParCible.setSelectedIndex((ficheConcours.getParametre().getNbTireur() / 2) - 1);
		}
		return jcbNombreTireurParCible;
	}
    
	/**
	 * @uml.property  name="jtfNombreDepart"
	 */
	private JTextField getJtfNombreDepart() {
		if (jtfNombreDepart == null) {
			jtfNombreDepart = new JTextField(new NumberDocument(false, false), ""+ficheConcours.getParametre().getNbDepart(), 3); //$NON-NLS-1$
		}
		return jtfNombreDepart;
	}
    
	/**
	 * @uml.property  name="jtfArbitres"
	 */
	private JTextField getJtfArbitres() {
		if (jtfArbitres == null) {
			jtfArbitres = new JTextField();
			jtfArbitres.setColumns(20);
			jtfArbitres.addActionListener(this);
		}
		return jtfArbitres;
	}
    
	/**
	 * @uml.property  name="jlArbitres"
	 */
	private JList getJlArbitres() {
		if (jlArbitres == null) {
			jlArbitres = new JList(ficheConcours.getParametre().getArbitres().toArray());
		}
		return jlArbitres;
	}

	/**
	 * @uml.property  name="jbAjouterArbitre"
	 */
	private JButton getJbAjouterArbitre() {
		if (jbAjouterArbitre == null) {
			jbAjouterArbitre = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
			jbAjouterArbitre.addActionListener(this);
		}
		return jbAjouterArbitre;
	}
    
	/**
	 * @uml.property  name="jbSupprimerArbitre"
	 */
	private JButton getJbSupprimerArbitre() {
		if (jbSupprimerArbitre == null) {
			jbSupprimerArbitre = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer")); //$NON-NLS-1$
			jbSupprimerArbitre.addActionListener(this);
		}
		return jbSupprimerArbitre;
	}
    
	/**
	 * @uml.property  name="jbArbitreResponsable"
	 */
	private JButton getJbArbitreResponsable() {
		if (jbArbitreResponsable == null) {
			jbArbitreResponsable = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.arbitreresponsable")); //$NON-NLS-1$
			jbArbitreResponsable.addActionListener(this);
		}
		return jbArbitreResponsable;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jpValidation"
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
	 * @return  javax.swing.JButton
	 * @uml.property  name="jbValider"
	 */    
	private JButton getJbValider() {
		if (jbValider == null) {
			jbValider = new JButton();
			jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
			jbValider.addActionListener(this);
		}
		return jbValider;
	}
    
	/**
	 * This method initializes jButton1	
	 * @return  javax.swing.JButton
	 * @uml.property  name="jbAnnuler"
	 */    
	private JButton getJbAnnuler() {
		if (jbAnnuler == null) {
			jbAnnuler = new JButton();
			jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
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
			ficheConcours.getParametre().setIntituleConcours(jtfIntituleConcours.getText());
			ficheConcours.getParametre().setDate(jtfDateConcours.getDate());
			ficheConcours.getParametre().setNbCible(Integer.parseInt(jtfNombreCible.getText()));
			ficheConcours.getParametre().setNbTireur((Integer)jcbNombreTireurParCible.getSelectedItem());
			ficheConcours.getParametre().setNbDepart(Integer.parseInt(jtfNombreDepart.getText()));
			ficheConcours.getParametre().setReglement(ReglementFactory.getReglement((String)jcbReglement.getSelectedItem()));
			ficheConcours.getParametre().setReglementLock(true);
			
			//sauvegarde en tache de fond
			ficheConcours.save();
			
			setVisible(false);
		} else if(ae.getSource() == jbAnnuler) {
			annulation = true;
			setVisible(false);
		} else if(ae.getSource() == jbAjouterArbitre || ae.getSource() == jtfArbitres) {
			ficheConcours.getParametre().getArbitres().add(jtfArbitres.getText());
			jtfArbitres.setText(""); //$NON-NLS-1$
			jlArbitres.setListData(ficheConcours.getParametre().getArbitres().toArray());
		} else if(ae.getSource() == jbSupprimerArbitre) {
			ficheConcours.getParametre().getArbitres().remove(jlArbitres.getSelectedIndex());
			jlArbitres.setListData(ficheConcours.getParametre().getArbitres().toArray());
		} else if(ae.getSource() == jbArbitreResponsable && jlArbitres.getSelectedIndex() > -1) {
			//cherche si il existe un arbitre responsable
			boolean resp = false;
			for(int i = 0; i < ficheConcours.getParametre().getArbitres().size(); i++) {
				if(ficheConcours.getParametre().getArbitres().get(i).startsWith("*")) { //$NON-NLS-1$
					resp = true;
					
					//si il en existe 1 et qu'il est different de celui que l'on veut
					if(i != jlArbitres.getSelectedIndex()) {
						//affecter à ce statut alors retirer l'* de la selection precedente
						String strArbitreResponsable = ficheConcours.getParametre().getArbitres().get(i);
						ficheConcours.getParametre().getArbitres().set(i, strArbitreResponsable.substring(1));
						
						//et l'ajouter sur la nouvelle selection
						strArbitreResponsable = ficheConcours.getParametre().getArbitres().get(jlArbitres.getSelectedIndex());
						ficheConcours.getParametre().getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable); //$NON-NLS-1$
						
						jlArbitres.setListData(ficheConcours.getParametre().getArbitres().toArray());
					}
					
					break;
				}
			}
			
			if(!resp) {
				String strArbitreResponsable = ficheConcours.getParametre().getArbitres().get(jlArbitres.getSelectedIndex());
				ficheConcours.getParametre().getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable); //$NON-NLS-1$
				
				jlArbitres.setListData(ficheConcours.getParametre().getArbitres().toArray());
			}
		}
	}
}