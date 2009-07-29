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
 *  any later version.
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.ConcurrentList;
import org.concoursjeunes.Parametre;
import org.concoursjeunes.Profile;
import org.concoursjeunes.ConcurrentList.SortCriteria;

/**
 * Boite de dialogue de saisie des résultats pour une cible
 * @author  Aurélien Jeoffray
 * @version  3.0
 */
@Localisable(textMethod="setTitle",value="resultats.titre")
public class ResultatDialog extends JDialog implements ActionListener, KeyListener, FocusListener {
	//static
	public static final int PREVIOUS_TARGET = 0;
	public static final int NEXT_TARGET = 1;
	public static final int SAVE_AND_QUIT = 2;
	public static final int CANCEL = 3;

	private AjResourcesReader localisation;
	private final Profile profile;
	private Parametre parametres;
	private List<Concurrent> concurrents;

	private JLabel jlCible = new JLabel();
	@Localisable("resultats.distances")
	private JLabel jlDistance = new JLabel();
	private JLabel[] jlDepartages;
	private JLabel[] jlDistances;

	private JLabel[] lPoints; //label cumuls des points de la série
	private JTextField[][] oldPoints; //ancien cumuls
	private JTextField[][] pointsCum2V; //cumuls sur 2 volées
	private JTextField[][] points; //cumuls des points de la série
	private JTextField[][] departages;
	
	@Localisable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localisable("bouton.suivant")
	private final JButton jbSuivant = new JButton();
	@Localisable("bouton.precedent")
	private final JButton jbPrecedent = new JButton();
	@Localisable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();

	private int returnVal = CANCEL;

	/**
	 * Ouverture de la boite de dialogue de saisie des résultats.
	 * 
	 * @param parentframe la fenetre parentes dont dépent la boite de dialogue
	 * @param concurrents les concurrents à editer
	 * @param parametres les parametre specifique du concours regissant la saisie
	 */
	public ResultatDialog(
			JFrame parentframe, AjResourcesReader localisation, Profile profile, Parametre parametres, List<Concurrent> concurrents) {
		super(parentframe, "", true); //$NON-NLS-1$
		
		this.localisation = localisation;
		this.profile = profile;
		this.parametres = parametres;
		this.concurrents = concurrents;
		ConcurrentList.sort(this.concurrents, SortCriteria.SORT_BY_TARGETS);

		init();
		completePanel();
		affectLibelle();

		//gestion du focus
		setFocusTraversalPolicy(new ResultatDialogFocusTraversalPolicy());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (ResultatDialog.this.profile.getConfiguration().isInterfaceResultatCumul()) {
					oldPoints[0][0].requestFocus(true);
				} else {
					points[0][0].requestFocus(true);
				}
			}
		});
	}
	
	private void init() {
		int nbSerie = parametres.getReglement().getNbSerie();
		int nbDepartages = parametres.getReglement().getTie().size();

		//initialise les champs
		oldPoints = new JTextField[parametres.getNbTireur()][nbSerie];
		pointsCum2V = new JTextField[parametres.getNbTireur()][nbSerie];
		points = new JTextField[parametres.getNbTireur()][nbSerie];
		departages = new JTextField[parametres.getNbTireur()][nbDepartages];

		for(int i = 0; i < parametres.getNbTireur(); i++) {
			for(int j = 0; j < nbSerie; j++) {
				//ancien score
				oldPoints[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				oldPoints[i][j].addKeyListener(this);
				oldPoints[i][j].addFocusListener(this);
				oldPoints[i][j].setEnabled(false);
				oldPoints[i][j].setName("oldpoints." + i + "." + j);  //$NON-NLS-1$//$NON-NLS-2$

				//points cumulé sur 2 volées
				pointsCum2V[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				pointsCum2V[i][j].addKeyListener(this);
				pointsCum2V[i][j].addFocusListener(this);
				pointsCum2V[i][j].setEnabled(false);
				pointsCum2V[i][j].setName("pointscum2v." + i + "." + j);  //$NON-NLS-1$//$NON-NLS-2$

				points[i][j] = new JTextField(new NumberDocument(false, false), "0",3); //$NON-NLS-1$
				if(profile.getConfiguration().isInterfaceResultatCumul()) {
					points[i][j].setEditable(false);
					points[i][j].setFocusable(false);
				} else {
					points[i][j].addKeyListener(this);
					points[i][j].addFocusListener(this);
				}
				points[i][j].setEnabled(false);
				points[i][j].setName("points." + i + "." + j);  //$NON-NLS-1$//$NON-NLS-2$

			}
			for(int j = 0; j < departages[i].length; j++) {
				departages[i][j] = new JTextField(new NumberDocument(false, false),"0",3); //$NON-NLS-1$
				departages[i][j].addKeyListener(this);
				departages[i][j].addFocusListener(this);
				departages[i][j].setEnabled(false);
				departages[i][j].setName("departages." + i + "." + j); //$NON-NLS-1$ //$NON-NLS-2$
			}
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
		jlDepartages = new JLabel[nbDepartages];
		for(int i = 0; i < nbDepartages; i++) {
			jlDepartages[i] = new JLabel();
		}

		lPoints = new JLabel[parametres.getNbTireur()];
		for(int i = 0; i < parametres.getNbTireur(); i++) {
			char pos = (char)(('A' + i));

			String libelle = pos + ":"; //$NON-NLS-1$
			
			for(int j = 0; j < concurrents.size(); j++) {
				if(concurrents.get(j).getPosition() == i) {
					libelle += concurrents.get(j).getID();
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
		c.gridy = 0; c.gridwidth = 4 + (profile.getConfiguration().isInterfaceResultatCumul() ? nbSerie*3 : nbSerie);
		gridbagComposer.addComponentIntoGrid(jlCible, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDistance, c);
		for(int i = 0; i < nbSerie; i++) {
			c.gridx = i+1;
			gridbagComposer.addComponentIntoGrid(jlDistances[i], c);
		}
		for(int i = 0; i < nbDepartages; i++) {
			c.gridx++;
			gridbagComposer.addComponentIntoGrid(jlDepartages[i], c);
		}

		c.gridx = GridBagConstraints.RELATIVE;
		JPanel[][] ppoints = new JPanel[parametres.getNbTireur()][nbSerie];
		for(int i = 0; i < parametres.getNbTireur(); i++) {
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(lPoints[i], c);

			for(int j = 0; j < nbSerie; j++) {
				ppoints[i][j] = new JPanel();
				if(profile.getConfiguration().isInterfaceResultatCumul()) {
					ppoints[i][j].add(oldPoints[i][j]);
					ppoints[i][j].add(new JLabel("+")); //$NON-NLS-1$
					ppoints[i][j].add(pointsCum2V[i][j]);
					ppoints[i][j].add(new JLabel("=")); //$NON-NLS-1$
				}
				ppoints[i][j].add(points[i][j]);
				gridbagComposer.addComponentIntoGrid(ppoints[i][j], c);
			}
			for(int j = 0; j < departages[i].length; j++)
				gridbagComposer.addComponentIntoGrid(departages[i][j], c);

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
			}
			for(int j = 0; j < departages[concurrent.getPosition()].length; j++) {
				if(concurrent.getDepartages().length > j)
					departages[concurrent.getPosition()][j].setText(String.valueOf(concurrent.getDepartages()[j]));
				else
					departages[concurrent.getPosition()][j].setText(String.valueOf(0));
				departages[concurrent.getPosition()][j].setEnabled(true);
			}
		}
	}
	
	private void affectLibelle() {
		Localisator.localize(this, localisation);
		
		jlCible.setText(localisation.getResourceString("resultats.cible", concurrents.get(0).getCible())); //$NON-NLS-1$
		for(int i = 0; i < jlDistances.length; i++) {
			jlDistances[i].setText((i==0) ?
					localisation.getResourceString("resultats.distance1") + " " //$NON-NLS-1$ //$NON-NLS-2$
					: (i+1) + localisation.getResourceString("resultats.distancen") + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		for(int i = 0 ; i < jlDepartages.length; i++)
			jlDepartages[i].setText(parametres.getReglement().getTie().get(i));
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
						if(profile.getConfiguration().isInterfaceResultatCumul())
							points[concurrent.getPosition()][i].setText(
									Integer.parseInt(oldPoints[concurrent.getPosition()][i].getText())
									+ Integer.parseInt(pointsCum2V[concurrent.getPosition()][i].getText())
									+ ""); //$NON-NLS-1$
						
						if(concPoints.size() > i)
							concPoints.set(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
						else
							concPoints.add(i, Integer.parseInt(points[concurrent.getPosition()][i].getText()));
					}
					//récupére les départages
					int[] concDepartages = new int[departages[concurrent.getPosition()].length];
					for(int i = 0; i < departages[concurrent.getPosition()].length; i++) {
						concDepartages[i] = Integer.parseInt(departages[concurrent.getPosition()][i].getText());
					}
					
					//vérifie que le score soit valide et affiche un message d'erreur dans le cas contraire 
					if(!parametres.getReglement().isValidScore(concPoints)) {
						JOptionPane.showMessageDialog(new JDialog(),
								localisation.getResourceString("erreur.impscore") + "<br>" + concurrent.getNomArcher(), //$NON-NLS-1$ //$NON-NLS-2$
								localisation.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					}
					
					//si c'est bon affecte le score à l'archer
					concurrent.setScore(concPoints);
					//intégre les 10/9/M si nécessaire
					concurrent.setDepartages(concDepartages);

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
						localisation.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						localisation.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			}
		} else if(source == jbAnnuler) {
			setVisible(false);
		}
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
		if(profile.getConfiguration().isInterfaceResultatCumul()) {
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
		private int getIndexConcurrentAtPosition(int position) {
			for(int i = 0; i < concurrents.size(); i++)
				if(concurrents.get(i).getPosition() == position)
					return i;
			return -1;
		}
		
		@Override
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			if(aComponent.getName() != null) {
				String prefix =  "oldpoints."; //$NON-NLS-1$
				int rowIndex = Integer.parseInt(aComponent.getName().substring(aComponent.getName().indexOf('.')+1, aComponent.getName().lastIndexOf('.')));
				int colIndex = Integer.parseInt(aComponent.getName().substring(aComponent.getName().lastIndexOf('.')+1));
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(concurrents.size()-1).getPosition()) { // si on est à la dernière position
						if(colIndex < parametres.getReglement().getNbSerie() - 1) //si on est pas sur la dernière série
							return oldPoints[concurrents.get(0).getPosition()][colIndex+1];
						else if(departages != null && departages.length > 0)
							return departages[concurrents.get(0).getPosition()][0];
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return oldPoints[concurrents.get(indexConcurrent+1).getPosition()][colIndex];
					}
				}
				prefix = "pointscum2v."; //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(concurrents.size()-1).getPosition()) { // si on est à la dernière position
						if(colIndex < parametres.getReglement().getNbSerie() - 1) { //si on est pas sur la dernière série
							return pointsCum2V[concurrents.get(0).getPosition()][colIndex+1];
						} else if(departages != null && departages.length > 0) {
							return departages[concurrents.get(0).getPosition()][0];
						}
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return pointsCum2V[concurrents.get(indexConcurrent+1).getPosition()][colIndex];
					}
				}
				prefix = "points."; //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(concurrents.size()-1).getPosition()) { // si on est à la dernière position
						if(colIndex < parametres.getReglement().getNbSerie() - 1) { //si on est pas sur la dernière série
							return points[concurrents.get(0).getPosition()][colIndex+1];
						} else if(departages != null && departages.length > 0) {
							return departages[concurrents.get(0).getPosition()][0];
						}
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return points[concurrents.get(indexConcurrent+1).getPosition()][colIndex];
					}
				}
				prefix = "departages.";  //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(colIndex < parametres.getReglement().getTie().size() -1) {
						return departages[rowIndex][colIndex+1];
					} else {
						if(rowIndex == concurrents.get(concurrents.size()-1).getPosition()) { // si on est à la dernière position
							return jbSuivant;
						} else {
							int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
							
							assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
							
							return departages[concurrents.get(indexConcurrent+1).getPosition()][0];
						}
					}
				}
			} else if (aComponent == jbSuivant) {
				return jbAnnuler;
			} else if (aComponent == jbAnnuler) {
				return jbValider;
			} else if (aComponent == jbValider) {
				return jbPrecedent;
			} else if (aComponent == jbPrecedent) {
				int firstPosition = concurrents.get(0).getPosition();
				
				if(points[firstPosition][0].isEditable())
					return points[firstPosition][0];
				else
					return pointsCum2V[firstPosition][0];
			}

			return null;
		}

		@Override
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			
			if(aComponent.getName() != null) {
				String prefix =  "oldpoints."; //$NON-NLS-1$
				int rowIndex = Integer.parseInt(aComponent.getName().substring(aComponent.getName().indexOf('.')+1, aComponent.getName().lastIndexOf('.')));
				int colIndex = Integer.parseInt(aComponent.getName().substring(aComponent.getName().lastIndexOf('.')+1));
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(0).getPosition()) { // si on est à la première position
						if(colIndex > 0) //si on est pas sur la première série
							return oldPoints[concurrents.get(concurrents.size()-1).getPosition()][colIndex-1];
						else
							return jbPrecedent;
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return oldPoints[concurrents.get(indexConcurrent-1).getPosition()][colIndex];
					}
				}
				prefix = "pointscum2v."; //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(0).getPosition()) { // si on est à la première position
						if(colIndex > 0) { //si on est pas sur la première série
							return pointsCum2V[concurrents.get(concurrents.size()-1).getPosition()][colIndex-1];
						} else {
							return jbPrecedent;
						}
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return pointsCum2V[concurrents.get(indexConcurrent-1).getPosition()][colIndex];
					}
				}
				prefix = "points."; //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(rowIndex == concurrents.get(0).getPosition()) { // si on est à la première position
						if(colIndex > 0) { //si on est pas sur la première série
							return points[concurrents.get(concurrents.size()-1).getPosition()][colIndex-1];
						} else {
							return jbPrecedent;
						}
					} else {
						int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
						
						assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
						
						return points[concurrents.get(indexConcurrent-1).getPosition()][colIndex];
					}
				}
				prefix = "departages.";  //$NON-NLS-1$
				if(aComponent.getName().startsWith(prefix)) {
					if(colIndex > 0) {
						return departages[rowIndex][colIndex-1];
					} else {
						if(rowIndex == concurrents.get(0).getPosition()) { // si on est à la première position
							int lastPosition = concurrents.get(concurrents.size()-1).getPosition();
							if(points[lastPosition][0].isEditable())
								return points[lastPosition][parametres.getReglement().getNbSerie()-1];
							else
								return pointsCum2V[lastPosition][parametres.getReglement().getNbSerie()-1];
						} else {
							int indexConcurrent = getIndexConcurrentAtPosition(rowIndex);
							
							assert indexConcurrent != -1 : "La position courante devrait toujours être occupé"; //$NON-NLS-1$
							
							return departages[concurrents.get(indexConcurrent-1).getPosition()][parametres.getReglement().getTie().size()-1];
						}
					}
				}
			} else if (aComponent == jbSuivant) {
				return departages[concurrents.get(concurrents.size()-1).getPosition()][parametres.getReglement().getTie().size()-1];
			} else if (aComponent == jbAnnuler) {
				return jbSuivant;
			} else if (aComponent == jbValider) {
				return jbAnnuler;
			} else if (aComponent == jbPrecedent) {
				return jbValider;
			}
			
			return null;
		}

		@Override
		public Component getDefaultComponent(Container focusCycleRoot) {
			if(points[concurrents.get(0).getPosition()][0].isEditable())
				return pointsCum2V[concurrents.get(0).getPosition()][0];
			return points[concurrents.get(0).getPosition()][0];
		}

		@Override
		public Component getLastComponent(Container focusCycleRoot) {
			return departages[concurrents.get(concurrents.size()-1).getPosition()][parametres.getReglement().getTie().size()-1];
		}

		@Override
		public Component getFirstComponent(Container focusCycleRoot) {
			if(points[concurrents.get(0).getPosition()][0].isEditable())
				return pointsCum2V[concurrents.get(0).getPosition()][0];
			return points[concurrents.get(0).getPosition()][0];
		}
	}
}