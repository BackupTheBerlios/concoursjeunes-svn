package org.concoursjeunes.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import org.concoursjeunes.*;

import ajinteractive.standard.java2.*;

/**
 * Boite de dialogue de saisie des résultats pour une cible
 * @author  Aurelien Jeoffray
 * @version  3.0
 */

public class ResultatDialog extends JDialog implements ActionListener, KeyListener, FocusListener {
	//static
	public static final int PREVIOUS_TARGET = 0;
	public static final int NEXT_TARGET = 1;
	public static final int SAVE_AND_QUIT = 2;
	public static final int CANCEL = 3;

	//private
	//private FicheConcours ficheConcours;
	/**
	 * @uml.property  name="concurrents"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Concurrent[] concurrents;
	private Parametre parametres;

	private JLabel jlCible = new JLabel();
	private JLabel jlDistance = new JLabel();
	private JLabel[] jlDistances;

	private JLabel[] lPoints;
	private JTextField[][] oldPoints;
	private JTextField[][] pointsCum2V;
	private JTextField[][] points;
	private JTextField[] dix;
	private JTextField[] neuf;
	private JTextField[] manque;
	
	private JButton jbValider = new JButton();
	private JButton jbSuivant = new JButton();

	private int returnVal = CANCEL;

	/**
	 * Ouverture de la boite de dialogue de saisie des résultats.
	 * 
	 * @param ficheConcoursPane - la fiche concours correspondant aux resultats à exploiter
	 * @param indexCible - numero de la cible à saisir
	 */
	public ResultatDialog(JFrame parentframe, Concurrent[] concurrents, Parametre parametres) {
		super(parentframe, "", true);

		this.concurrents = concurrents;
		this.parametres = parametres;

		init();
		completePanel();
		affectLibelle();

		getRootPane().setDefaultButton(jbSuivant);
		setFocusTraversalPolicy(new ResultatDialogFocusTraversalPolicy());
		//affichage de la boite de dialogue
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	private void init() {
		int nbSerie = parametres.getReglement().getNbSerie();

		//initialise les champs
		oldPoints = new JTextField[parametres.getNbTireur()][nbSerie];
		pointsCum2V = new JTextField[parametres.getNbTireur()][nbSerie];
		points = new JTextField[parametres.getNbTireur()][nbSerie];
		dix = new JTextField[parametres.getNbTireur()];
		neuf = new JTextField[parametres.getNbTireur()];
		manque = new JTextField[parametres.getNbTireur()];

		for(int i = 0; i < parametres.getNbTireur(); i++) {
			for(int j = 0; j < nbSerie; j++) {
				oldPoints[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				oldPoints[i][j].addActionListener(this);
				oldPoints[i][j].addKeyListener(this);
				oldPoints[i][j].addFocusListener(this);
				oldPoints[i][j].setEnabled(false);

				pointsCum2V[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				pointsCum2V[i][j].addActionListener(this);
				pointsCum2V[i][j].addKeyListener(this);
				pointsCum2V[i][j].addFocusListener(this);
				pointsCum2V[i][j].setEnabled(false);

				points[i][j] = new JTextField(new NumberDocument(false, false), "0",3); //$NON-NLS-1$
				if(ConcoursJeunes.configuration.isInterfaceResultatCumul()) {
					points[i][j].setEditable(false);
					points[i][j].setFocusable(false);
				} else {
					points[i][j].addActionListener(this);
					points[i][j].addKeyListener(this);
					points[i][j].addFocusListener(this);
				}
				points[i][j].setEnabled(false);

			}
			dix[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			dix[i].addActionListener(this);
			dix[i].addKeyListener(this);
			dix[i].addFocusListener(this);
			dix[i].setEnabled(false);
			neuf[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			neuf[i].addActionListener(this);
			neuf[i].addKeyListener(this);
			neuf[i].addFocusListener(this);
			neuf[i].setEnabled(false);
			manque[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			manque[i].addActionListener(this);
			manque[i].addKeyListener(this);
			manque[i].addFocusListener(this);
			manque[i].setEnabled(false);
		}

		JPanel pane1 = new JPanel();
		JPanel jpAction = new JPanel();

		//préparation de la boite de dialogue
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		//Elements textuelle
		//pane1
		jlDistances = new JLabel[nbSerie];
		for(int i = 0; i < nbSerie; i++) {
			jlDistances[i] = new JLabel();
		}
		JLabel ldix = new JLabel("10"); //$NON-NLS-1$
		JLabel lneuf = new JLabel("9"); //$NON-NLS-1$
		JLabel lmanque = new JLabel("M"); //$NON-NLS-1$

		lPoints = new JLabel[parametres.getNbTireur()];
		for(int i = 0; i < parametres.getNbTireur(); i++) {
			char pos = (char)(('A' + i));

			String libelle = pos + ":"; //$NON-NLS-1$
			
			for(int j = 0; j < concurrents.length; j++) {
				if(concurrents[j].getPosition() == i) {
					libelle += concurrents[j].getID();
					break;
				}
			}
			lPoints[i] = new JLabel(libelle);
			lPoints[i].setEnabled(false);
		}

		jbValider.addActionListener(this);
		jbSuivant.addActionListener(this);

		gridbagComposer.setParentPanel(pane1);
		c.anchor = GridBagConstraints.WEST;
		c.gridy = 0; c.gridx = 0;
		gridbagComposer.addComponentIntoGrid(jlDistance, c);
		for(int i = 0; i < nbSerie; i++) {
			c.gridx = i+1;
			gridbagComposer.addComponentIntoGrid(jlDistances[i], c);
		}
		if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(ldix, c);
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(lneuf, c);
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(lmanque, c);
		}

		c.gridx = GridBagConstraints.RELATIVE;
		JPanel[][] ppoints = new JPanel[parametres.getNbTireur()][nbSerie];
		for(int i = 0; i < parametres.getNbTireur(); i++) {
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(lPoints[i], c);

			for(int j = 0; j < nbSerie; j++) {
				ppoints[i][j] = new JPanel();
				if(ConcoursJeunes.configuration.isInterfaceResultatCumul()) {
					ppoints[i][j].add(oldPoints[i][j]);
					ppoints[i][j].add(new JLabel("+")); //$NON-NLS-1$
					ppoints[i][j].add(pointsCum2V[i][j]);
					ppoints[i][j].add(new JLabel("=")); //$NON-NLS-1$
				}
				ppoints[i][j].add(points[i][j]);
				gridbagComposer.addComponentIntoGrid(ppoints[i][j], c);
			}
			if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
				gridbagComposer.addComponentIntoGrid(dix[i], c);
				gridbagComposer.addComponentIntoGrid(neuf[i], c);
				gridbagComposer.addComponentIntoGrid(manque[i], c);
			}
		}

		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbSuivant);

		getRootPane().setDefaultButton(jbSuivant);
		setLayout(new BorderLayout());
		add(pane1, BorderLayout.CENTER);
		add(jpAction, BorderLayout.SOUTH);
	}
	
	private void completePanel() {
		//attribue l'ancienne valeur au champ distances
		for(Concurrent concurrent : concurrents) {
			ArrayList<Integer> p = concurrent.getScore();
			
			for(int j = 0; j < parametres.getReglement().getNbSerie(); j++) {
				oldPoints[concurrent.getPosition()][j].setText(p.get(j)+""); //$NON-NLS-1$
				oldPoints[concurrent.getPosition()][j].setEnabled(true);
				pointsCum2V[concurrent.getPosition()][j].setEnabled(true);

				points[concurrent.getPosition()][j].setText(p.get(j)+""); //$NON-NLS-1$
				points[concurrent.getPosition()][j].setEnabled(true);
				lPoints[concurrent.getPosition()].setEnabled(true);
				
				if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
					dix[concurrent.getPosition()].setText(concurrent.getDix()+""); //$NON-NLS-1$
					dix[concurrent.getPosition()].setEnabled(true);
					neuf[concurrent.getPosition()].setText(concurrent.getNeuf()+""); //$NON-NLS-1$
					neuf[concurrent.getPosition()].setEnabled(true);
					manque[concurrent.getPosition()].setText(concurrent.getManque()+""); //$NON-NLS-1$
					manque[concurrent.getPosition()].setEnabled(true);
				}
			}
		}
	}
	
	private void affectLibelle() {
		setTitle(ConcoursJeunes.ajrLibelle.getResourceString("resultats.titre")); //$NON-NLS-1$
		
		jlCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("resultats.cible"));
		jlDistance.setText(ConcoursJeunes.ajrLibelle.getResourceString("resultats.distances"));
		for(int i = 0; i < jlDistances.length; i++) {
			jlDistances[i].setText((i==0) ?
					ConcoursJeunes.ajrLibelle.getResourceString("resultats.distance1") + " " //$NON-NLS-1$ //$NON-NLS-2$
					: (i+1) + ConcoursJeunes.ajrLibelle.getResourceString("resultats.distancen") + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbSuivant.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.suivant")); //$NON-NLS-1$
	}
	
	public int showResultatDialog() {
		setVisible(true);
		
		return returnVal;
	}

	//auditeur d'événement
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();

		if(source == jbValider || source == jbSuivant) {
			for(Concurrent concurrent : concurrents) {
				ArrayList<Integer> concPoints = new ArrayList<Integer>();
				for(int i = 0; i < parametres.getReglement().getNbSerie(); i++) {
					if(ConcoursJeunes.configuration.isInterfaceResultatCumul())
						points[concurrent.getPosition()][i].setText(
								Integer.parseInt(oldPoints[concurrent.getPosition()][i].getText())
								+ Integer.parseInt(pointsCum2V[concurrent.getPosition()][i].getText())
								+ ""); //$NON-NLS-1$
					
					if(concPoints.size() > i)
						concPoints.set(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
					else
						concPoints.add(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
				}
				
				if(!parametres.getReglement().isValidScore(concPoints)) {
					JOptionPane.showMessageDialog(new JDialog(),
							ConcoursJeunes.ajrLibelle.getResourceString("erreur.impscore") + "<br>" + concurrent.getNomArcher(), //$NON-NLS-1$ //$NON-NLS-2$
							ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
				concurrent.setScore(concPoints);
				if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
					concurrent.setDix(Integer.parseInt(dix[concurrent.getPosition()].getText()));
					concurrent.setNeuf(Integer.parseInt(neuf[concurrent.getPosition()].getText()));
					concurrent.setManque(Integer.parseInt(manque[concurrent.getPosition()].getText()));
				}
			}
	
			// valide les informations sur le concurrent
			if(source == jbValider) {
				returnVal = SAVE_AND_QUIT;
				setVisible(false);
			}
			//Passe au concurrent suivant
			else if(source == jbSuivant) {
				returnVal = NEXT_TARGET;
				setVisible(false);
			}
			setVisible(false);
		}
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
		if(ConcoursJeunes.configuration.isInterfaceResultatCumul()) {
			char key = e.getKeyChar();
			if(Character.isDigit(key)) {
				for(int i = 0; i < parametres.getNbTireur(); i++) {
					for(int j = 0; j < parametres.getReglement().getNbSerie(); j++) {
						points[i][j].setText(Integer.parseInt(oldPoints[i][j].getText())+Integer.parseInt(pointsCum2V[i][j].getText())+""); //$NON-NLS-1$
					}
				}
			}
		}
	}
	
	public void keyTyped(KeyEvent e) {}

	public void focusGained(FocusEvent e) {
		((JTextField)e.getSource()).setSelectionStart(0);
		((JTextField)e.getSource()).setSelectionEnd(((JTextField)e.getSource()).getText().length());
	}
	
	public void focusLost(FocusEvent e) {

	}

	public class ResultatDialogFocusTraversalPolicy extends FocusTraversalPolicy {
		@Override
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			Component nextComp = null;
			for(int i = 0; i < parametres.getNbTireur(); i++) {
				for(int j = 0; j < parametres.getReglement().getNbSerie(); j++) {
					if(aComponent == oldPoints[i][j]) {
						if(i+1 < parametres.getNbTireur() && oldPoints[i+1][j].isEnabled())
							nextComp = oldPoints[i+1][j];
						else if(j+1 < parametres.getReglement().getNbSerie())
							nextComp = oldPoints[0][j+1];
						else if(dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = oldPoints[0][0];
						break;
					} else if(aComponent == pointsCum2V[i][j]) {
						if(i+1 < parametres.getNbTireur() && pointsCum2V[i+1][j].isEnabled())
							nextComp = pointsCum2V[i+1][j];
						else if(j+1 < parametres.getReglement().getNbSerie())
							nextComp = pointsCum2V[0][j+1];
						else if(dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = pointsCum2V[0][0];
						break;
					} else if(aComponent == points[i][j]) {
						if(i+1 < parametres.getNbTireur() && points[i+1][j].isEnabled())
							nextComp = points[i+1][j];
						else if(j+1 < parametres.getReglement().getNbSerie())
							nextComp = points[0][j+1];
						else if(dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = points[0][0];
						break;
					} else if(aComponent == dix[i]) {
						nextComp = neuf[i];
						break;
					} else if(aComponent == neuf[i]) {
						nextComp = manque[i];
						break;
					} else if(aComponent == manque[i]) {
						if(i+1 < parametres.getNbTireur() && dix[i+1].isEnabled())
							nextComp = dix[i+1];
						else if(ConcoursJeunes.configuration.isInterfaceResultatCumul())
							nextComp = oldPoints[0][0];
						else
							nextComp = points[0][0];
						break;
					}
				}
				if(nextComp != null)
					break;
			}
			return nextComp;
		}

		@Override
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int nbConc = concurrents.length ;
			Component nextComp = null;
			for(int i = nbConc - 1; i >= 0; i--) {
				for(int j = parametres.getReglement().getNbSerie() - 1; j >= 0; j--) {
					if(aComponent == oldPoints[i][j]) {
						if(i-1 >= 0)
							nextComp = oldPoints[i - 1][j];
						else if(j-1 >= 0)
							nextComp = oldPoints[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = oldPoints[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if(aComponent == pointsCum2V[i][j]) {
						if(i-1 >= 0)
							nextComp = pointsCum2V[i - 1][j];
						else if(j-1 >= 0)
							nextComp = pointsCum2V[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = pointsCum2V[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if(aComponent == points[i][j]) {
						if(i-1 >= 0)
							nextComp = points[i - 1][j];
						else if(j-1 >= 0)
							nextComp = points[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = points[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if(aComponent == manque[i]) {
						nextComp = neuf[i];
						break;
					} else if(aComponent == neuf[i]) {
						nextComp = dix[i];
						break;
					} else if(aComponent == dix[i]) {
						if(i-1 >= 0)
							nextComp = manque[i-1];
						else if(ConcoursJeunes.configuration.isInterfaceResultatCumul())
							nextComp = oldPoints[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						else
							nextComp = points[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					}
				}
				if(nextComp != null)
					break;
			}
			return nextComp;
		}

		@Override
		public Component getDefaultComponent(Container focusCycleRoot) {
			return oldPoints[0][0];
		}

		@Override
		public Component getLastComponent(Container focusCycleRoot) {
			return manque[3];
		}

		@Override
		public Component getFirstComponent(Container focusCycleRoot) {
			return oldPoints[0][0];
		}
	}
}