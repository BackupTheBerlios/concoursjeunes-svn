/*
 * Créer le 31 déc. 07 à 16:35:22 pour ConcoursJeunes
 *
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
package org.concoursjeunes.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.concoursjeunes.ConcoursJeunes;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class InstallPluginDialog extends JDialog implements ActionListener {
	
	private final JLabel jllCategorie = new JLabel();
	private final JList jlCategorie = new JList();
	
	private final JLabel jlPlugins = new JLabel();
	private final JLabel jlSearch = new JLabel();
	private final JTextField jtfSearch = new JTextField();
	private final JTable jtPlugins = new JTable();
	private final JTextPane jtpDescription = new JTextPane();
	
	private final JButton jbValider = new JButton();
	private final JButton jbAnnuler = new JButton();
	
	public InstallPluginDialog(JFrame parentframe) {
		super(parentframe, true);
		
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpPrincipal = new JPanel();
		JPanel jpAction = new JPanel();
		JPanel jpCategorie = new JPanel();
		JPanel jpPlugins = new JPanel();
		
		/*GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();*/
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		//gridbagComposer.setParentPanel(jpPrincipal);
		
		/*c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;*/
		jpCategorie.setLayout(new BorderLayout());
		jpCategorie.add(jllCategorie, BorderLayout.NORTH);
		jpCategorie.add(new JScrollPane(jlCategorie), BorderLayout.CENTER);
		
		jpPlugins.setLayout(new BorderLayout());
		jpPlugins.add(jlPlugins, BorderLayout.NORTH);
		jpPlugins.add(new JScrollPane(jtPlugins), BorderLayout.CENTER);
		jpPlugins.add(new JScrollPane(jtpDescription), BorderLayout.SOUTH);
		
		jpPrincipal.setLayout(new BorderLayout());
		jpPrincipal.add(jpCategorie, BorderLayout.WEST);
		jpPrincipal.add(jpPlugins, BorderLayout.CENTER);
		
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpPrincipal, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		jllCategorie.setText(ConcoursJeunes.ajrLibelle.getResourceString("installplugindialog.category")); //$NON-NLS-1$
		jlPlugins.setText(ConcoursJeunes.ajrLibelle.getResourceString("installplugindialog.plugins")); //$NON-NLS-1$
		
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	private void completePanel() {
		
	}
	
	public void showInstallPluginDialog() {
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}
	
	
}
