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
import org.concoursjeunes.ui.ConcoursJeunesFrame;
import org.jdesktop.swingx.JXDatePicker;

import ajinteractive.standard.java2.*;

/**
 * Boite de dialogue de gestion des parametre du concours
 * @author  Aurelien Jeoffray
 * @version  2.0
 */
public class ParametreDialog extends JDialog implements ActionListener {
	
	private Parametre parametre;
	private Reglement tempReglement;
	
	private FicheConcours ficheConcours;
	
	//private JButton jbLogo;
	private JTextField jtfIntituleConcours = new JTextField(20);
	//private JFormattedTextField jtfDateConcours;
	private JXDatePicker jtfDateConcours = new JXDatePicker();
	private JComboBox jcbReglement = new JComboBox();
	private JButton jbDetail = new JButton();
	private JTextField jtfNombreCible = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JComboBox jcbNombreTireurParCible = new JComboBox();
	private JTextField jtfNombreDepart = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfArbitres = new JTextField(20);
	private JButton jbAjouterArbitre = new JButton();
	private JButton jbSupprimerArbitre = new JButton();
	private JButton jbArbitreResponsable = new JButton();
	private AJList jlArbitres = new AJList();
	
	private JLabel jlIntituleConcours = new JLabel();
	private JLabel jlDateConcours = new JLabel();
	private JLabel jlReglement = new JLabel();
	private JLabel jlNombreCible = new JLabel();
	private JLabel jlNombreTireurParCible = new JLabel();
	private JLabel jlNombreDepart = new JLabel();
	private JLabel jlbArbitres = new JLabel();
	
	ReglementDialog reglementDialog;
	
	private JButton jbValider = new JButton();
	private JButton jbAnnuler = new JButton();
	
	public ParametreDialog(ConcoursJeunesFrame concoursJeunesFrame, FicheConcours ficheConcours) {
		super(concoursJeunesFrame);
		
		this.ficheConcours = ficheConcours;
		
		reglementDialog = new ReglementDialog(concoursJeunesFrame, null);
		
		init();
		affectLibelle();
		
		getRootPane().setDefaultButton(jbValider);
		setModal(true);
		
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
		for(String name : Reglement.listAvailableReglements()) {
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
	public void showParametreDialog(Parametre parametre) {
		this.parametre = parametre;
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
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
		
		jbDetail.setText("+"); //$NON-NLS-1$
		
		jbAjouterArbitre.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.ajouter")); //$NON-NLS-1$
		jbSupprimerArbitre.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.supprimer")); //$NON-NLS-1$
		jbArbitreResponsable.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.arbitreresponsable")); //$NON-NLS-1$
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	public void completePanel() {
		jtfIntituleConcours.setText(parametre.getIntituleConcours());
		jtfDateConcours.setDate(parametre.getDate());
		jcbReglement.setSelectedItem(parametre.getReglement().getName());
		jcbReglement.setEnabled(!parametre.isReglementLock());
		jtfNombreCible.setText(""+parametre.getNbCible()); //$NON-NLS-1$
		jcbNombreTireurParCible.setSelectedIndex((parametre.getNbTireur() / 2) - 1);
		jtfNombreDepart.setText(""+parametre.getNbDepart()); //$NON-NLS-1$
		jlArbitres.setListData(parametre.getArbitres().toArray());
		
		tempReglement = parametre.getReglement();
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == jbValider) {
			
			int placelibre = parametre.getNbCible();
			for(int i = 0; i < parametre.getNbDepart(); i++) {
				//ficheConcours.getPasDeTir(i).getOccupationCibles();
				int placelibre_tmp = ficheConcours.getPasDeTir(i).getNbCiblesLibre((Integer)jcbNombreTireurParCible.getSelectedItem());
				if(placelibre_tmp < placelibre)
					placelibre = placelibre_tmp;
			}
			
			if(placelibre < 0 || parametre.getNbCible() - placelibre > Integer.parseInt(jtfNombreCible.getText())) {
				JOptionPane.showMessageDialog(this,
						"Le nombre de cible avec le rythme de tir est trop faible " +
						"pour le nombre courrant de concurrent inscrit", //$NON-NLS-1$
						"Nombre de cible impossible",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}
			
			if(Integer.parseInt(jtfNombreDepart.getText()) < parametre.getNbDepart()) {
				for(int i = Integer.parseInt(jtfNombreDepart.getText()); i < parametre.getNbDepart(); i++) {
					if(ficheConcours.getConcurrentList().countArcher(i) > 0) {
						JOptionPane.showMessageDialog(this,
								"Vous ne pouvez pas réduire le nombre de départ en raison de la présence d'archers sur ceux ci", //$NON-NLS-1$
								"Nombre de départ impossible",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
			}
			
			/*for(int i = 0; i < Integer.parseInt(jtfNombreDepart.getText()); i++) {
				ArrayList<DistancesEtBlason> lDB = ficheConcours.getConcurrentList().listDistancesEtBlason(
						parametre.getReglement(), true, i);
				if(ficheConcours.getPasDeTir(i).getOptimalRythme(lDB) > (Integer)jcbNombreTireurParCible.getSelectedItem()) {
					JOptionPane.showMessageDialog(this,
							"Il y a trop d'archers pour pouvoir réduire le nombre de départ", //$NON-NLS-1$
							"Rythme de tir impossible",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			}*/
				
			
			parametre.setIntituleConcours(jtfIntituleConcours.getText());
			parametre.setDate(jtfDateConcours.getDate());
			parametre.getArbitres().clear();
			for(Object arbitre : jlArbitres.getAllList()) {
				parametre.getArbitres().add((String)arbitre);
			}
			
			parametre.setNbCible(Integer.parseInt(jtfNombreCible.getText()));
			parametre.setNbTireur((Integer)jcbNombreTireurParCible.getSelectedItem());
			parametre.setNbDepart(Integer.parseInt(jtfNombreDepart.getText()));
			if(parametre.isReglementLock())
				parametre.setReglement(tempReglement);
			else
				parametre.setReglement(ReglementBuilder.createReglement((String)jcbReglement.getSelectedItem()));
			parametre.setReglementLock(true);
			
			setVisible(false);
		} else if(ae.getSource() == jbAnnuler) {
			setVisible(false);
		} else if(ae.getSource() == jbAjouterArbitre || ae.getSource() == jtfArbitres) {

			jlArbitres.add(jtfArbitres.getText());
			jtfArbitres.setText(""); //$NON-NLS-1$
		} else if(ae.getSource() == jbSupprimerArbitre) {
			jlArbitres.remove(jlArbitres.getSelectedIndex());
		} else if(ae.getSource() == jbArbitreResponsable && jlArbitres.getSelectedIndex() > -1) {
			//cherche si il existe un arbitre responsable
			boolean resp = false;
			for(int i = 0; i < parametre.getArbitres().size(); i++) {
				if(parametre.getArbitres().get(i).startsWith("*")) { //$NON-NLS-1$
					resp = true;
					
					//si il en existe 1 et qu'il est different de celui que l'on veut
					if(i != jlArbitres.getSelectedIndex()) {
						//affecter à ce statut alors retirer l'* de la selection precedente
						String strArbitreResponsable = parametre.getArbitres().get(i);
						parametre.getArbitres().set(i, strArbitreResponsable.substring(1));
						
						//et l'ajouter sur la nouvelle selection
						strArbitreResponsable = parametre.getArbitres().get(jlArbitres.getSelectedIndex());
						parametre.getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable); //$NON-NLS-1$
						
						jlArbitres.setListData(parametre.getArbitres().toArray());
					}
					
					break;
				}
			}
			
			if(!resp) {
				String strArbitreResponsable = parametre.getArbitres().get(jlArbitres.getSelectedIndex());
				parametre.getArbitres().set(jlArbitres.getSelectedIndex(), "*" + strArbitreResponsable); //$NON-NLS-1$
				
				jlArbitres.setListData(parametre.getArbitres().toArray());
			}
		} else if(ae.getSource() == jbDetail) {
			reglementDialog.setReglement(tempReglement);
			if(parametre.isReglementLock())
				reglementDialog.setVerrou(ReglementDialog.LOCK_CHANGE_L1);
			else
				reglementDialog.setVerrou(ReglementDialog.NO_LOCK);
			Reglement reglement = reglementDialog.showReglementDialog();
			if(reglement != null)
				tempReglement = reglement;
		}
	}
}