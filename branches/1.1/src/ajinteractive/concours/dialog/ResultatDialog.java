package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * Boite de dialogue de saisie des résultats pour une cible
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */

public class ResultatDialog extends JDialog implements ActionListener, KeyListener, ChangeListener, FocusListener {
	//static
	public static final int PREVIOUS_TARGET = 0;
	public static final int NEXT_TARGET = 1;
	public static final int SAVE_AND_QUIT = 2;
	public static final int CANCEL = 3;
	
	//private
	private FicheConcours ficheConcours;
	
	private JButton jbValider;
	private JButton jbSuivant;
	
	private JLabel[] lPoints;
	private JTextField[][] oldPoints;
	private JTextField[][] pointsCum2V;
	private JTextField[][] points;
	private JTextField[] dix;
	private JTextField[] neuf;
	private JTextField[] manque;
	
	private int action = CANCEL;
	private int index = 1;

	public JSpinner cible; 
	
	/**
	 * Ouverture de la boite de dialogue de saisie des résultats.
	 * 
	 * @param ficheConcoursFrame - la fiche concours correspondant aux resultats à exploiter
	 * @param indexCible - numero de la cible à saisir
	 */
	public ResultatDialog(FicheConcoursFrame ficheConcoursFrame, int indexCible) {
		super(ficheConcoursFrame.concoursJeunes);
        
        this.ficheConcours = ficheConcoursFrame.ficheConcours;
        this.index = indexCible;
        
		this.setTitle(ConcoursJeunes.ajrLibelle.getResourceString("resultats.titre"));
		this.setModal(true);
        
		Concurrent[] concurrents = ficheConcours.archerlist.list(indexCible);
		int nbSerie = ficheConcours.parametre.getNbSerie();
		
		cible = new JSpinner(new SpinnerNumberModel(indexCible, 1, ficheConcours.parametre.getNbCible(), 1));
		
		jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
		jbSuivant = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.suivant"));
		
		//initialise les champs
		oldPoints = new JTextField[ficheConcours.parametre.getNbTireur()][nbSerie];
		pointsCum2V = new JTextField[ficheConcours.parametre.getNbTireur()][nbSerie];
		points = new JTextField[ficheConcours.parametre.getNbTireur()][nbSerie];
		dix = new JTextField[ficheConcours.parametre.getNbTireur()];
		neuf = new JTextField[ficheConcours.parametre.getNbTireur()];
		manque = new JTextField[ficheConcours.parametre.getNbTireur()];
		
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
			for(int j = 0; j < nbSerie; j++) {
				oldPoints[i][j] = new JTextField(new NumberDocument(false, false),"0",3);
				oldPoints[i][j].addActionListener(this);
				oldPoints[i][j].addKeyListener(this);
				oldPoints[i][j].addFocusListener(this);
				oldPoints[i][j].setEnabled(false);

				pointsCum2V[i][j] = new JTextField(new NumberDocument(false, false),"0",3);
				pointsCum2V[i][j].addActionListener(this);
				pointsCum2V[i][j].addKeyListener(this);
				pointsCum2V[i][j].addFocusListener(this);
				pointsCum2V[i][j].setEnabled(false);
				
				points[i][j] = new JTextField(new NumberDocument(false, false), "0",3);
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
			dix[i] = new JTextField(new NumberDocument(false, false),"0",3);
			dix[i].addActionListener(this);
			dix[i].addKeyListener(this);
			dix[i].addFocusListener(this);
			dix[i].setEnabled(false);
			neuf[i] = new JTextField(new NumberDocument(false, false),"0",3);
			neuf[i].addActionListener(this);
			neuf[i].addKeyListener(this);
			neuf[i].addFocusListener(this);
			neuf[i].setEnabled(false);
			manque[i] = new JTextField(new NumberDocument(false, false),"0",3);
			manque[i].addActionListener(this);
			manque[i].addKeyListener(this);
			manque[i].addFocusListener(this);
			manque[i].setEnabled(false);
		}
		
		////
		JPanel pane1 = new JPanel();
        JPanel jpAction = new JPanel();
		////
		
		//préparation de la boite de dialogue
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		pane1.setLayout(gridbag);
		//
		
		//Elements textuelle
		//pane1
		JLabel cib = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("resultats.cible"));
        JLabel lDistance = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("resultats.distances"));
		JLabel lDistances[] = new JLabel[nbSerie];
		for(int i = 0; i < nbSerie; i++) {
			
			lDistances[i] = new JLabel((i==0) ?
                    ConcoursJeunes.ajrLibelle.getResourceString("resultats.distance1") + " "
					: (i+1) + ConcoursJeunes.ajrLibelle.getResourceString("resultats.distancen") + " ");
		}
		JLabel ldix = new JLabel("10");
		JLabel lneuf = new JLabel("9");
		JLabel lmanque = new JLabel("M");
		
		lPoints = new JLabel[ficheConcours.parametre.getNbTireur()];
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
			char pos = (char)(('A' + i));
			
			String libelle = pos + ":";
			for(int j = 0; j < concurrents.length; j++) {
				if(concurrents[j].getPosition() == i) {
					libelle += concurrents[j].getID();
					break;
				}
			}
			lPoints[i] = new JLabel(libelle);
			lPoints[i].setEnabled(false);
		}
		
		cible.addChangeListener(this);
		
		jbValider.setActionCommand("bouton.valider");
		jbSuivant.setActionCommand("bouton.suivant");
		jbValider.addActionListener(this);
		jbSuivant.addActionListener(this);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridy = 0;
		AJToolKit.addComponentIntoGrid(pane1, cib, gridbag, c);
		AJToolKit.addComponentIntoGrid(pane1, cible, gridbag, c);
		
		c.gridy++; c.gridx = 0;
        AJToolKit.addComponentIntoGrid(pane1, lDistance, gridbag, c);
		for(int i = 0; i < nbSerie; i++) {
			c.gridx = i+1;
			AJToolKit.addComponentIntoGrid(pane1, lDistances[i], gridbag, c);
		}
		if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
			c.gridx++;
			AJToolKit.addComponentIntoGrid(pane1, ldix, gridbag, c);
			c.gridx++;
			AJToolKit.addComponentIntoGrid(pane1, lneuf, gridbag, c);
			c.gridx++;
			AJToolKit.addComponentIntoGrid(pane1, lmanque, gridbag, c);
		}
		
		c.gridx = GridBagConstraints.RELATIVE;
		JPanel[][] ppoints = new JPanel[ficheConcours.parametre.getNbTireur()][nbSerie];
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
			c.gridy++;
			AJToolKit.addComponentIntoGrid(pane1, lPoints[i], gridbag, c);
			
			for(int j = 0; j < nbSerie; j++) {
				ppoints[i][j] = new JPanel();
				if(ConcoursJeunes.configuration.isInterfaceResultatCumul()) {
					ppoints[i][j].add(oldPoints[i][j]);
					ppoints[i][j].add(new JLabel("+"));
					ppoints[i][j].add(pointsCum2V[i][j]);
					ppoints[i][j].add(new JLabel("="));
				}
				ppoints[i][j].add(points[i][j]);
				AJToolKit.addComponentIntoGrid(pane1, ppoints[i][j], gridbag, c);
			}
			if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
				AJToolKit.addComponentIntoGrid(pane1, dix[i], gridbag, c);
				AJToolKit.addComponentIntoGrid(pane1, neuf[i], gridbag, c);
				AJToolKit.addComponentIntoGrid(pane1, manque[i], gridbag, c);
			}
		}
		
        jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpAction.add(jbValider);
        jpAction.add(jbSuivant);
        
        this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pane1, BorderLayout.CENTER);
        this.getContentPane().add(jpAction, BorderLayout.SOUTH);
		
		//attribue l'ancienne valeur au champ distances
		for(int i = 0; i < concurrents.length; i++) {
			int[] p = concurrents[i].getScore();
			if(concurrents[i].getCible() == indexCible) {
				for(int k = 0; k < nbSerie; k++) {
                    if(p != null)
                        oldPoints[concurrents[i].getPosition()][k].setText(p[k]+"");
                    else
                        oldPoints[concurrents[i].getPosition()][k].setText("0");
					oldPoints[concurrents[i].getPosition()][k].setEnabled(true);
					pointsCum2V[concurrents[i].getPosition()][k].setEnabled(true);
                    
                    if(p != null)
                        points[concurrents[i].getPosition()][k].setText(p[k]+"");
                    else
                        points[concurrents[i].getPosition()][k].setText("0");
                    
					points[concurrents[i].getPosition()][k].setEnabled(true);
					lPoints[concurrents[i].getPosition()].setEnabled(true);
					if(ConcoursJeunes.configuration.isInterfaceResultatSupl()) {
						dix[concurrents[i].getPosition()].setText(concurrents[i].getDix()+"");
						dix[concurrents[i].getPosition()].setEnabled(true);
						neuf[concurrents[i].getPosition()].setText(concurrents[i].getNeuf()+"");
						neuf[concurrents[i].getPosition()].setEnabled(true);
						manque[concurrents[i].getPosition()].setText(concurrents[i].getManque()+"");
						manque[concurrents[i].getPosition()].setEnabled(true);
					}
				}
			}
		}
		
        getRootPane().setDefaultButton(jbSuivant);
        this.setFocusTraversalPolicy(new ResultatDialogFocusTraversalPolicy());
		//affichage de la boite de dialogue
		pack();
        setResizable(false);
        setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//auditeur d'événement
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
			for(int j = 0; j < ficheConcours.parametre.getNbSerie(); j++) {
				if(ConcoursJeunes.configuration.isInterfaceResultatCumul())
					points[i][j].setText(Integer.parseInt(oldPoints[i][j].getText())+Integer.parseInt(pointsCum2V[i][j].getText())+"");
				
				if(Integer.parseInt(points[i][j].getText()) > (this.ficheConcours.parametre.getNbVoleeParSerie()
                        * this.ficheConcours.parametre.getNbFlecheParVolee() * 10)) {
                    JOptionPane.showMessageDialog(new JDialog(),
                            ConcoursJeunes.ajrLibelle.getResourceString("erreur.impscore") + "<br>" + lPoints[i].getText(),
                            ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE);
                    return;
                }
			}
		}
		
		// valide les informations sur le concurrent
		if(source == jbValider) {
			action = SAVE_AND_QUIT;
			setVisible(false);
		}
		//Passe au concurrent suivant
		else if(source == jbSuivant) {
			action = NEXT_TARGET;
			setVisible(false);
		}
	}
	public void keyPressed(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
		if(ConcoursJeunes.configuration.isInterfaceResultatCumul()) {
			char key = e.getKeyChar();
			if(Character.isDigit(key)) {
				for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
					for(int j = 0; j < ficheConcours.parametre.getNbSerie(); j++) {
						points[i][j].setText(Integer.parseInt(oldPoints[i][j].getText())+Integer.parseInt(pointsCum2V[i][j].getText())+"");
					}
				}
			}
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void stateChanged(ChangeEvent e) {
		if((Integer)cible.getValue() > index) {
			index = (Integer)cible.getValue() - 1;
			action = NEXT_TARGET;
		} else {
			index = (Integer)cible.getValue() + 1;
			action = PREVIOUS_TARGET;
		}
		
		setVisible(false);
	}
	public void focusGained(FocusEvent e) {
		((JTextField)e.getSource()).setSelectionStart(0);
		((JTextField)e.getSource()).setSelectionEnd(((JTextField)e.getSource()).getText().length());
	}
	public void focusLost(FocusEvent e) {
		
	}

	//////////////////////////////////////
	// methodes de sortie d'information //
	//////////////////////////////////////
    /**
     * sort l'ensemble des points saisie pour la cible
     * 
     * @return int[][] - les points de la cible
     */
	public int[][] getPoints() {
		int[][] iPoints = new int[ficheConcours.parametre.getNbTireur()][ficheConcours.parametre.getNbSerie()];
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
			for(int j = 0; j < ficheConcours.parametre.getNbSerie(); j++) {
				iPoints[i][j] = Integer.parseInt(points[i][j].getText());
			}
		}
		return iPoints;
	}
	
	public int[][] getSuplements() {
		int[][] iSupl = new int[ficheConcours.parametre.getNbTireur()][3];
		for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
				iSupl[i] = new int[] {Integer.parseInt(dix[i].getText()), Integer.parseInt(neuf[i].getText()), Integer.parseInt(manque[i].getText())};
		}
		return iSupl;
	}
	
	public int getAction() {
		return action;
	}
	/**
	 * @return Renvoie index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index index à définir.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	public class ResultatDialogFocusTraversalPolicy extends FocusTraversalPolicy {
	    @Override
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			Component nextComp = null;
			for(int i = 0; i < ficheConcours.parametre.getNbTireur(); i++) {
				for(int j = 0; j < ficheConcours.parametre.getNbSerie(); j++) {
					if(aComponent == oldPoints[i][j]) {
						if(i+1 < ficheConcours.parametre.getNbTireur() && oldPoints[i+1][j].isEnabled())
							nextComp = oldPoints[i+1][j];
						else if(j+1 < ficheConcours.parametre.getNbSerie())
							nextComp = oldPoints[0][j+1];
						else if(dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = oldPoints[0][0];
						break;
					} else if(aComponent == pointsCum2V[i][j]) {
						if(i+1 < ficheConcours.parametre.getNbTireur() && pointsCum2V[i+1][j].isEnabled())
							nextComp = pointsCum2V[i+1][j];
						else if(j+1 < ficheConcours.parametre.getNbSerie())
							nextComp = pointsCum2V[0][j+1];
						else if(dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = pointsCum2V[0][0];
						break;
					} else if(aComponent == points[i][j]) {
						if(i+1 < ficheConcours.parametre.getNbTireur() && points[i+1][j].isEnabled())
							nextComp = points[i+1][j];
						else if(j+1 < ficheConcours.parametre.getNbSerie())
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
						if(i+1 < ficheConcours.parametre.getNbTireur() && dix[i+1].isEnabled())
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
			int nbConc = ficheConcours.archerlist.list(index).length ;
			Component nextComp = null;
			for(int i = nbConc - 1; i >= 0; i--) {
				for(int j = ficheConcours.parametre.getNbSerie() - 1; j >= 0; j--) {
					if(aComponent == oldPoints[i][j]) {
						if(i-1 >= 0)
							nextComp = oldPoints[i - 1][j];
						else if(j-1 >= 0)
							nextComp = oldPoints[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = oldPoints[nbConc - 1][ficheConcours.parametre.getNbSerie() - 1];
						break;
					} else if(aComponent == pointsCum2V[i][j]) {
						if(i-1 >= 0)
							nextComp = pointsCum2V[i - 1][j];
						else if(j-1 >= 0)
							nextComp = pointsCum2V[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = pointsCum2V[nbConc - 1][ficheConcours.parametre.getNbSerie() - 1];
						break;
					} else if(aComponent == points[i][j]) {
						if(i-1 >= 0)
							nextComp = points[i - 1][j];
						else if(j-1 >= 0)
							nextComp = points[nbConc - 1][j - 1];
						else if(manque[nbConc - 1].isEnabled())
							nextComp = manque[nbConc - 1];
						else
							nextComp = points[nbConc - 1][ficheConcours.parametre.getNbSerie() - 1];
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
							nextComp = oldPoints[nbConc - 1][ficheConcours.parametre.getNbSerie() - 1];
						else
							nextComp = points[nbConc - 1][ficheConcours.parametre.getNbSerie() - 1];
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