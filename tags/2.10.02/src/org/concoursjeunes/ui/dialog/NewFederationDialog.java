/*
 * Créer le 17 aoû. 08 à 12:54:22 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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

import static org.concoursjeunes.ApplicationCore.ajrLibelle;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.*;

import org.concoursjeunes.Federation;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.ui.GridbagComposer;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class NewFederationDialog extends JDialog implements ActionListener {
	
	private JLabel jlFederationSigle = new JLabel();
	private JLabel jlFederationName = new JLabel();
	private JTextField jtfFederatonSigle = new JTextField(10);
	private JTextField jtfFederatonName = new JTextField(50);
	
	private JButton jbValider = new JButton();
	private JButton jbAnnuler = new JButton();
	
	//private JFrame parentframe;
	
	private Federation federation = null;
	
	public NewFederationDialog(JFrame parentframe) {
		super(parentframe, true);
		
		//this.parentframe = parentframe;
		
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpPrincipal = new JPanel();
		JPanel jpAction = new JPanel();
		
		GridbagComposer gbComposer = new GridbagComposer();
		GridBagConstraints c = new GridBagConstraints();
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		gbComposer.setParentPanel(jpPrincipal);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gbComposer.addComponentIntoGrid(jlFederationSigle, c);
		gbComposer.addComponentIntoGrid(jtfFederatonSigle, c);
		c.gridy++;
		gbComposer.addComponentIntoGrid(jlFederationName, c);
		gbComposer.addComponentIntoGrid(jtfFederatonName, c);
		
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpPrincipal, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		setTitle(ajrLibelle.getResourceString("newfederation.title")); //$NON-NLS-1$
		
		jlFederationSigle.setText(ajrLibelle.getResourceString("newfederation.sigle")); //$NON-NLS-1$
		jlFederationName.setText(ajrLibelle.getResourceString("newfederation.name")); //$NON-NLS-1$
		
		jbValider.setText(ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	private void completePanel() {
		
	}
	
	/**
	 * Affiche la boite de dialogue de création de dédération
	 * 
	 * @return la federation créer
	 */
	public Federation showNewFederationDialog() {
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		return federation;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			federation = new Federation(jtfFederatonName.getText(), jtfFederatonSigle.getText());
			try {
				federation.save();
				
			} catch (SQLException e1) {
				federation = null;
				JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			}
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}
	
	
}
