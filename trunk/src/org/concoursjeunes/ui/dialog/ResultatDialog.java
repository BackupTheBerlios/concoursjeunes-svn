/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.concoursjeunes.ui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Parametre;

import ajinteractive.standard.ui.GridbagComposer;
import ajinteractive.standard.ui.NumberDocument;

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

	private final Concurrent[] concurrents;
	private final Parametre parametres;

	private final JLabel jlCible = new JLabel();
	private final JLabel jlDistance = new JLabel();
	private JLabel[] jlDistances;

	private JLabel[] lPoints;
	private JTextField[][] oldPoints;
	private JTextField[][] pointsCum2V;
	private JTextField[][] points;
	private JTextField[] dix;
	private JTextField[] neuf;
	//private JTextField[] manque;
	
	private final JButton jbValider = new JButton();
	private final JButton jbSuivant = new JButton();
	private final JButton jbPrecedent = new JButton();
	private final JButton jbAnnuler = new JButton();

	private int returnVal = CANCEL;

	/**
	 * Ouverture de la boite de dialogue de saisie des résultats.
	 * 
	 * @param parentframe la fenetre parentes dont dépent la boite de dialogue
	 * @param concurrents les concurrents à editer
	 * @param parametres les parametre specifique du concours regissant la saisie
	 */
	public ResultatDialog(JFrame parentframe, Concurrent[] concurrents, Parametre parametres) {
		super(parentframe, "", true); //$NON-NLS-1$

		this.concurrents = concurrents;
		this.parametres = parametres;

		init();
		completePanel();
		affectLibelle();

		setFocusTraversalPolicy(new ResultatDialogFocusTraversalPolicy());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (ApplicationCore.getConfiguration().isInterfaceResultatCumul()) {
					oldPoints[0][0].requestFocus(true);
				} else {
					points[0][0].requestFocus(true);
				}
			}
		});
	}
	
	private void init() {
		int nbSerie = parametres.getReglement().getNbSerie();

		//initialise les champs
		oldPoints = new JTextField[parametres.getNbTireur()][nbSerie];
		pointsCum2V = new JTextField[parametres.getNbTireur()][nbSerie];
		points = new JTextField[parametres.getNbTireur()][nbSerie];
		dix = new JTextField[parametres.getNbTireur()];
		neuf = new JTextField[parametres.getNbTireur()];
		//manque = new JTextField[parametres.getNbTireur()];

		for(int i = 0; i < parametres.getNbTireur(); i++) {
			for(int j = 0; j < nbSerie; j++) {
				oldPoints[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				oldPoints[i][j].addKeyListener(this);
				oldPoints[i][j].addFocusListener(this);
				oldPoints[i][j].setEnabled(false);

				pointsCum2V[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				pointsCum2V[i][j].addKeyListener(this);
				pointsCum2V[i][j].addFocusListener(this);
				pointsCum2V[i][j].setEnabled(false);

				points[i][j] = new JTextField(new NumberDocument(false, false), "0",3); //$NON-NLS-1$
				if(ApplicationCore.getConfiguration().isInterfaceResultatCumul()) {
					points[i][j].setEditable(false);
					points[i][j].setFocusable(false);
				} else {
					points[i][j].addKeyListener(this);
					points[i][j].addFocusListener(this);
				}
				points[i][j].setEnabled(false);

			}
			dix[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			dix[i].addKeyListener(this);
			dix[i].addFocusListener(this);
			dix[i].setEnabled(false);
			neuf[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			neuf[i].addKeyListener(this);
			neuf[i].addFocusListener(this);
			neuf[i].setEnabled(false);
			/*manque[i] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
			manque[i].addKeyListener(this);
			manque[i].addFocusListener(this);
			manque[i].setEnabled(false);*/
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
		//JLabel lmanque = new JLabel("M"); //$NON-NLS-1$

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
		jbPrecedent.addActionListener(this);
		jbAnnuler.addActionListener(this);

		gridbagComposer.setParentPanel(pane1);
		c.anchor = GridBagConstraints.WEST; c.weightx = 1.0;
		c.gridy = 0; c.gridwidth = 4 + (ApplicationCore.getConfiguration().isInterfaceResultatCumul() ? nbSerie*3 : nbSerie);
		gridbagComposer.addComponentIntoGrid(jlCible, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDistance, c);
		for(int i = 0; i < nbSerie; i++) {
			c.gridx = i+1;
			gridbagComposer.addComponentIntoGrid(jlDistances[i], c);
		}
		if(ApplicationCore.getConfiguration().isInterfaceResultatSupl()) {
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(ldix, c);
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(lneuf, c);
		/*	c.gridx++;
			gridbagComposer.addComponentIntoGrid(lmanque, c);*/
		}

		c.gridx = GridBagConstraints.RELATIVE;
		JPanel[][] ppoints = new JPanel[parametres.getNbTireur()][nbSerie];
		for(int i = 0; i < parametres.getNbTireur(); i++) {
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(lPoints[i], c);

			for(int j = 0; j < nbSerie; j++) {
				ppoints[i][j] = new JPanel();
				if(ApplicationCore.getConfiguration().isInterfaceResultatCumul()) {
					ppoints[i][j].add(oldPoints[i][j]);
					ppoints[i][j].add(new JLabel("+")); //$NON-NLS-1$
					ppoints[i][j].add(pointsCum2V[i][j]);
					ppoints[i][j].add(new JLabel("=")); //$NON-NLS-1$
				}
				ppoints[i][j].add(points[i][j]);
				gridbagComposer.addComponentIntoGrid(ppoints[i][j], c);
			}
			if(ApplicationCore.getConfiguration().isInterfaceResultatSupl()) {
				gridbagComposer.addComponentIntoGrid(dix[i], c);
				gridbagComposer.addComponentIntoGrid(neuf[i], c);
				//gridbagComposer.addComponentIntoGrid(manque[i], c);
			}
		}

		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbPrecedent);
		jpAction.add(jbSuivant);
		jpAction.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pane1, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(jbSuivant);
	}
	
	private void completePanel() {
		//attribue l'ancienne valeur au champ distances
		for(Concurrent concurrent : concurrents) {
			List<Integer> p = concurrent.getScore();
			while(p.size() < parametres.getReglement().getNbSerie())
				p.add(0);
			
			for(int j = 0; j < parametres.getReglement().getNbSerie(); j++) {
				oldPoints[concurrent.getPosition()][j].setText(p.get(j)+""); //$NON-NLS-1$
				oldPoints[concurrent.getPosition()][j].setEnabled(true);
				pointsCum2V[concurrent.getPosition()][j].setEnabled(true);

				points[concurrent.getPosition()][j].setText(p.get(j)+""); //$NON-NLS-1$
				points[concurrent.getPosition()][j].setEnabled(true);
				lPoints[concurrent.getPosition()].setEnabled(true);
				
				if(ApplicationCore.getConfiguration().isInterfaceResultatSupl()) {
					dix[concurrent.getPosition()].setText(concurrent.getDix()+""); //$NON-NLS-1$
					dix[concurrent.getPosition()].setEnabled(true);
					neuf[concurrent.getPosition()].setText(concurrent.getNeuf()+""); //$NON-NLS-1$
					neuf[concurrent.getPosition()].setEnabled(true);
					//manque[concurrent.getPosition()].setText(concurrent.getManque()+""); //$NON-NLS-1$
					//manque[concurrent.getPosition()].setEnabled(true);
				}
			}
		}
	}
	
	private void affectLibelle() {
		setTitle(ApplicationCore.ajrLibelle.getResourceString("resultats.titre")); //$NON-NLS-1$
		
		jlCible.setText("<html><font size=\"+1\">" + ApplicationCore.ajrLibelle.getResourceString("resultats.cible") + " " + concurrents[0].getCible() + "</font></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		jlDistance.setText(ApplicationCore.ajrLibelle.getResourceString("resultats.distances")); //$NON-NLS-1$
		for(int i = 0; i < jlDistances.length; i++) {
			jlDistances[i].setText((i==0) ?
					ApplicationCore.ajrLibelle.getResourceString("resultats.distance1") + " " //$NON-NLS-1$ //$NON-NLS-2$
					: (i+1) + ApplicationCore.ajrLibelle.getResourceString("resultats.distancen") + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		jbValider.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbSuivant.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.suivant")); //$NON-NLS-1$
		jbPrecedent.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.precedent")); //$NON-NLS-1$
		jbAnnuler.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	public int showResultatDialog() {
		// affichage de la boite de dialogue
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		return returnVal;
	}

	//auditeur d'événement
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();

		if(source == jbValider || source == jbSuivant || source == jbPrecedent) {
			try {
				//boucle sur les concurrents de la cible
				for(Concurrent concurrent : concurrents) {
					ArrayList<Integer> concPoints = new ArrayList<Integer>();
					//récupere les points du concurrent
					for(int i = 0; i < parametres.getReglement().getNbSerie(); i++) {
						if(ApplicationCore.getConfiguration().isInterfaceResultatCumul())
							points[concurrent.getPosition()][i].setText(
									Integer.parseInt(oldPoints[concurrent.getPosition()][i].getText())
									+ Integer.parseInt(pointsCum2V[concurrent.getPosition()][i].getText())
									+ ""); //$NON-NLS-1$
						
						if(concPoints.size() > i)
							concPoints.set(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
						else
							concPoints.add(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
					}
					
					//vérifie que le score soit valide et affiche un message d'erreur dans le cas contraire 
					if(!parametres.getReglement().isValidScore(concPoints)) {
						JOptionPane.showMessageDialog(new JDialog(),
								ApplicationCore.ajrLibelle.getResourceString("erreur.impscore") + "<br>" + concurrent.getNomArcher(), //$NON-NLS-1$ //$NON-NLS-2$
								ApplicationCore.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					}
					
					//si c'est bon affecte le score à l'archer
					concurrent.setScore(concPoints);
					//intégre les 10/9/M si nécessaire
					if(ApplicationCore.getConfiguration().isInterfaceResultatSupl()) {
						concurrent.setDix(Integer.parseInt(dix[concurrent.getPosition()].getText()));
						concurrent.setNeuf(Integer.parseInt(neuf[concurrent.getPosition()].getText()));
						//concurrent.setManque(Integer.parseInt(manque[concurrent.getPosition()].getText()));
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
				//Passe au concurrent precedent
				else if(source == jbPrecedent) {
					returnVal = PREVIOUS_TARGET;
					setVisible(false);
				}
				setVisible(false);
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			}
		} else if(source == jbAnnuler) {
			setVisible(false);
		}
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
		if(ApplicationCore.getConfiguration().isInterfaceResultatCumul()) {
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
		if (e.getSource() instanceof JTextField) {
			((JTextField)e.getSource()).setSelectionStart(0);
			((JTextField)e.getSource()).setSelectionEnd(((JTextField)e.getSource()).getText().length());
		}
	}
	
	public void focusLost(FocusEvent e) {

	}

	public class ResultatDialogFocusTraversalPolicy extends FocusTraversalPolicy {
		@Override
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			Component nextComp = null;
			for (int i = 0; i < parametres.getNbTireur(); i++) {
				for (int j = 0; j < parametres.getReglement().getNbSerie(); j++) {
					if (aComponent == oldPoints[i][j]) {
						if (i + 1 < parametres.getNbTireur() && oldPoints[i + 1][j].isEnabled())
							nextComp = oldPoints[i + 1][j];
						else if (j + 1 < parametres.getReglement().getNbSerie())
							nextComp = oldPoints[0][j + 1];
						else if (dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = oldPoints[0][0];
						break;
					} else if (aComponent == pointsCum2V[i][j]) {
						if (i + 1 < parametres.getNbTireur() && pointsCum2V[i + 1][j].isEnabled())
							nextComp = pointsCum2V[i + 1][j];
						else if (j + 1 < parametres.getReglement().getNbSerie())
							nextComp = pointsCum2V[0][j + 1];
						else if (dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = pointsCum2V[0][0];
						break;
					} else if (aComponent == points[i][j]) {
						if (i + 1 < parametres.getNbTireur() && points[i + 1][j].isEnabled())
							nextComp = points[i + 1][j];
						else if (j + 1 < parametres.getReglement().getNbSerie())
							nextComp = points[0][j + 1];
						else if (dix[0].isEnabled())
							nextComp = dix[0];
						else
							nextComp = points[0][0];
						break;
					} else if (aComponent == dix[i]) {
						nextComp = neuf[i];
						break;
					} else if (aComponent == neuf[i]) {
						if (i + 1 < parametres.getNbTireur() && dix[i + 1].isEnabled())
							nextComp = dix[i + 1];
						else if (ApplicationCore.getConfiguration().isInterfaceResultatCumul())
							nextComp = oldPoints[0][0];
						else
							nextComp = jbSuivant;
						break;
					} else if (aComponent == jbSuivant) {
						nextComp = jbAnnuler;
						break;
					} else if (aComponent == jbAnnuler) {
						nextComp = jbValider;
						break;
					} else if (aComponent == jbValider) {
						nextComp = jbPrecedent;
						break;
					} else if (aComponent == jbPrecedent) {
						nextComp = points[0][0];
						break;
					}
				}
				if (nextComp != null)
					break;
			}
			return nextComp;
		}

		@Override
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			int nbConc = concurrents.length;
			Component nextComp = null;
			for (int i = nbConc - 1; i >= 0; i--) {
				for (int j = parametres.getReglement().getNbSerie() - 1; j >= 0; j--) {
					if (aComponent == oldPoints[i][j]) {
						if (i - 1 >= 0)
							nextComp = oldPoints[i - 1][j];
						else if (j - 1 >= 0)
							nextComp = oldPoints[nbConc - 1][j - 1];
						else if (neuf[nbConc - 1].isEnabled())
							nextComp = jbPrecedent;
						else
							nextComp = oldPoints[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if (aComponent == pointsCum2V[i][j]) {
						if (i - 1 >= 0)
							nextComp = pointsCum2V[i - 1][j];
						else if (j - 1 >= 0)
							nextComp = pointsCum2V[nbConc - 1][j - 1];
						else if (neuf[nbConc - 1].isEnabled())
							nextComp = jbPrecedent;
						else
							nextComp = pointsCum2V[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if (aComponent == points[i][j]) {
						if (i - 1 >= 0)
							nextComp = points[i - 1][j];
						else if (j - 1 >= 0)
							nextComp = points[nbConc - 1][j - 1];
						else if (neuf[nbConc - 1].isEnabled())
							nextComp = jbPrecedent;
						else
							nextComp = points[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if (aComponent == neuf[i]) {
						nextComp = dix[i];
						break;
					} else if (aComponent == dix[i]) {
						if (i - 1 >= 0)
							nextComp = neuf[i - 1];
						else if (ApplicationCore.getConfiguration().isInterfaceResultatCumul())
							nextComp = oldPoints[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						else
							nextComp = points[nbConc - 1][parametres.getReglement().getNbSerie() - 1];
						break;
					} else if (aComponent == jbSuivant) {
						nextComp = neuf[nbConc - 1];
						break;
					} else if (aComponent == jbAnnuler) {
						nextComp = jbSuivant;
						break;
					} else if (aComponent == jbValider) {
						nextComp = jbAnnuler;
						break;
					} else if (aComponent == jbPrecedent) {
						nextComp = jbValider;
						break;
					}
				}
				if (nextComp != null)
					break;
			}
			return nextComp;
		}

		@Override
		public Component getDefaultComponent(Container focusCycleRoot) {
			if(oldPoints[0][0].isEditable())
				return oldPoints[0][0];
			return points[0][0];
		}

		@Override
		public Component getLastComponent(Container focusCycleRoot) {
			return neuf[3];
		}

		@Override
		public Component getFirstComponent(Container focusCycleRoot) {
			if(oldPoints[0][0].isEditable())
				return oldPoints[0][0];
			return points[0][0];
		}
	}
}