/*
 * Créé le 28 juil. 2010 à 11:43:28 pour ConcoursJeunes / ArcCompétition
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizationHandler;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.Duel;
import org.jdesktop.swingx.JXHeader;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Localizable(value="duel.title",textMethod="setTitle")
public class DuelDialog extends JDialog implements ActionListener {
	
	private AjResourcesReader localisation;
	private Duel duel;
	
	@Localizable("duel.header")
	private JXHeader jxhHeaderDuel = new JXHeader();
	
	private JLabel jlConcurrent1 = new JLabel();
	@Localizable("duel.score")
	private JLabel jlScore1 = new JLabel();
	private JTextField jtfScore1 = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	private JLabel jlConcurrent2 = new JLabel();
	@Localizable("duel.score")
	private JLabel jlScore2 = new JLabel();
	private JTextField jtfScore2 = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	
	@Localizable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localizable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	private DefaultDialogReturn returnAction = DefaultDialogReturn.CANCEL;

	public DuelDialog(JFrame parent, AjResourcesReader localisation) {
		super(parent, true);
		this.localisation = localisation;
		
		init();
		affectLabels();
	}
	
	private void init() {
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel jpGeneral = new JPanel();
		gridbagComposer.setParentPanel(jpGeneral);
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.8;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlConcurrent1, c);
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlScore1, c);
		c.weightx = 0.2;
		gridbagComposer.addComponentIntoGrid(jtfScore1, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlConcurrent2, c);
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlScore2, c);
		gridbagComposer.addComponentIntoGrid(jtfScore2, c);
		c.gridy++;
		c.weighty = 1.0;
		c.fill =GridBagConstraints.VERTICAL;
		gridbagComposer.addComponentIntoGrid(Box.createVerticalGlue(), c);
		
		JPanel jpAction = new JPanel();
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH,jxhHeaderDuel);
		getContentPane().add(BorderLayout.CENTER,jpGeneral);
		getContentPane().add(BorderLayout.SOUTH,jpAction);
	}
	
	private void affectLabels() {
		Localizator.localize(this, localisation, Collections.<Class<?>, LocalizationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));
	}
	
	private void completePanel() {
		jxhHeaderDuel.setDescription(localisation.getResourceString("duel.header.description", //$NON-NLS-1$
				localisation.getResourceString("duel.phase." + (duel.getPhase()-1)), //$NON-NLS-1$
				duel.getNumDuel()));
		jlConcurrent1.setText("<html>" //$NON-NLS-1$
				 + duel.getConcurrent1().getFullName()
				 + "<br><span style=\"font-style:italic;color:#888888;font-size:90%;\">" //$NON-NLS-1$
				 + duel.getConcurrent1().getEntite().getNom()
				 + "</span></html>"); //$NON-NLS-1$
		jlConcurrent2.setText("<html>" //$NON-NLS-1$
				 + duel.getConcurrent2().getFullName()
				 + "<br><span style=\"font-style:italic;color:#888888;font-size:90%;\">" //$NON-NLS-1$
				 + duel.getConcurrent2().getEntite().getNom()
				 + "</span></html>"); //$NON-NLS-1$
		jtfScore1.setText(String.valueOf(duel.getConcurrent1().getScorePhasefinal(duel.getPhase())));
		jtfScore2.setText(String.valueOf(duel.getConcurrent2().getScorePhasefinal(duel.getPhase())));
	}
	
	public DefaultDialogReturn showDuelDialog(Duel duel) {
		returnAction = DefaultDialogReturn.CANCEL;
		this.duel = duel;
		
		completePanel();
		
		setSize(430, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		
		return returnAction;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			duel.getConcurrent1().setScorePhasefinal(duel.getPhase(), Integer.valueOf("0"+jtfScore1.getText())); //$NON-NLS-1$
			duel.getConcurrent2().setScorePhasefinal(duel.getPhase(), Integer.valueOf("0"+jtfScore2.getText())); //$NON-NLS-1$
			
			returnAction = DefaultDialogReturn.OK;
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}

}
