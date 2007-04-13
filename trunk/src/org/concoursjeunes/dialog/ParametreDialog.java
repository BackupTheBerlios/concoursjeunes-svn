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
	
	//private JButton jbLogo;
	private JTextField jtfIntituleConcours = new JTextField(20);
	//private JFormattedTextField jtfDateConcours;
	private JXDatePicker jtfDateConcours = new JXDatePicker();
	private JComboBox jcbReglement = new JComboBox();
	private JButton jbDetail = new JButton();
	private JTextField jtfNombreCible = new JTextField(new NumberDocument(false, false), "", 3);
	private JComboBox jcbNombreTireurParCible = new JComboBox();
	private JTextField jtfNombreDepart = new JTextField(new NumberDocument(false, false), "", 3);
	private JTextField jtfArbitres = new JTextField(20);
	private JButton jbAjouterArbitre = new JButton();
	private JButton jbSupprimerArbitre = new JButton();
	private JButton jbArbitreResponsable = new JButton();
	private JList jlArbitres = new JList();
	
	private JLabel jlIntituleConcours = new JLabel();
	private JLabel jlDateConcours = new JLabel(); //$NON-NLS-1$
	private JLabel jlReglement = new JLabel(); //$NON-NLS-1$
	private JLabel jlNombreCible = new JLabel(); //$NON-NLS-1$
	private JLabel jlNombreTireurParCible = new JLabel(); //$NON-NLS-1$
	private JLabel jlNombreDepart = new JLabel(); //$NON-NLS-1$
	private JLabel jlbArbitres = new JLabel(); //$NON-NLS-1$
	
	ReglementDialog reglementDialog;
	
	private JButton jbValider = new JButton();
	private JButton jbAnnuler = new JButton();
	
	public ParametreDialog(FicheConcoursPane ficheConcoursFrame) {
		super(ficheConcoursFrame.getParentframe());
		
		reglementDialog = new ReglementDialog(ficheConcoursFrame.getParentframe());
		
		this.ficheConcours = ficheConcoursFrame.ficheConcours;
		
		init();
		affectLibelle();
		completePanel();
		
		getRootPane().setDefaultButton(jbValider);
		setModal(true);
		pack();
		setLocationRelativeTo(null);
		//this.setResizable(false);
	}
    
	/**
	 * This method initializes this
	 */
	private void init() {
		//JPanel jContentPane = new JPanel();
		JPanel jpParametre = new JPanel();
		JPanel jpValidation = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();
		
		jbDetail.addActionListener(this);
		jtfDateConcours.setFormats(new DateFormat[] {DateFormat.getDateInstance(DateFormat.SHORT)});
		for(String name : ConcoursJeunes.userRessources.listAvailableReglements()) {
			jcbReglement.addItem(name);
		}
		for(int i = 2; i <= 6; i+=2)
			jcbNombreTireurParCible.addItem(i);
		jtfArbitres.addActionListener(this);
		jbAjouterArbitre.addActionListener(this);
		jbSupprimerArbitre.addActionListener(this);
		jbArbitreResponsable.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		jpValidation.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpValidation.add(jbValider);
		jpValidation.add(jbAnnuler);
		
		gridbagComposer.setParentPanel(jpParametre);
		c.gridy = 0; c.gridheight = 1; c.anchor = GridBagConstraints.WEST; c.ipadx = 2;
		gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfIntituleConcours, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDateConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfDateConcours, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlReglement, c);
		gridbagComposer.addComponentIntoGrid(jcbReglement, c);
		gridbagComposer.addComponentIntoGrid(jbDetail, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNombreCible, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreTireurParCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbNombreTireurParCible, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreDepart, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNombreDepart, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlbArbitres, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfArbitres, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth =1;
		gridbagComposer.addComponentIntoGrid(jbAjouterArbitre, c);
		
		c.gridy++;
		c.gridx = 1; c.gridwidth = 2; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jlArbitres), c);
		c.gridx = 3; c.gridheight = 1; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jbSupprimerArbitre, c);
		
		c.gridy++; c.anchor = GridBagConstraints.NORTH;
		gridbagComposer.addComponentIntoGrid(jbArbitreResponsable, c);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpParametre, BorderLayout.CENTER);
		getContentPane().add(jpValidation, BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 *
	 */
	public void showParametreDialog() {
		setVisible(true);
	}
	
	/**
	 * 
	 *
	 */
	public void affectLibelle() {
		setTitle(ConcoursJeunes.ajrLibelle.getResourceString("parametre.titre")); //$NON-NLS-1$
		
		jlIntituleConcours.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.intituleconcours")); //$NON-NLS-1$
		jlDateConcours.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.dateconcours")); //$NON-NLS-1$
		jlReglement.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.reglement")); //$NON-NLS-1$
		jlNombreCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombrecible")); //$NON-NLS-1$
		jlNombreTireurParCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombretireurparcible")); //$NON-NLS-1$
		jlNombreDepart.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.nombredepart")); //$NON-NLS-1$
		jlbArbitres.setText(ConcoursJeunes.ajrLibelle.getResourceString("parametre.arbitres")); //$NON-NLS-1$
		
		jbDetail.setText("+");
		
		jbAjouterArbitre.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter"));
		jbSupprimerArbitre.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer"));
		jbArbitreResponsable.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.arbitreresponsable"));
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	public void completePanel() {
		jtfIntituleConcours.setText(ficheConcours.getParametre().getIntituleConcours());
		jtfDateConcours.setDate(ficheConcours.getParametre().getDate());
		jcbReglement.setSelectedItem(ficheConcours.getParametre().getReglement().getName());
		jcbReglement.setEnabled(!ficheConcours.getParametre().isReglementLock());
		jtfNombreCible.setText(""+ficheConcours.getParametre().getNbCible());
		jcbNombreTireurParCible.setSelectedIndex((ficheConcours.getParametre().getNbTireur() / 2) - 1);
		jtfNombreDepart.setText(""+ficheConcours.getParametre().getNbDepart());
		jlArbitres.setListData(ficheConcours.getParametre().getArbitres().toArray());
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
		} else if(ae.getSource() == jbDetail) {
			reglementDialog.setReglement(ficheConcours.getParametre().getReglement());
			if(ficheConcours.getParametre().isReglementLock())
				reglementDialog.setVerrou(ReglementDialog.LOCK_CHANGE_L1);
			else
				reglementDialog.setVerrou(ReglementDialog.NO_LOCK);
			Reglement reglement = reglementDialog.showReglementDialog();
			if(reglement != null)
				ficheConcours.getParametre().setReglement(reglement);
		}
	}
}