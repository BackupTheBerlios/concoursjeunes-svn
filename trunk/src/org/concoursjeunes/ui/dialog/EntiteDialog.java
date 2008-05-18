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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Entite;

import ajinteractive.standard.java2.GridbagComposer;

/**
 * @author Aurélien JEOFFRAY
 */
public class EntiteDialog extends JDialog implements ActionListener {
	// private ConcoursJeunes concoursJeunes;
	private Entite entite;

	private JTextField jtfNom;
	private JTextField jftfAgrement;
	private JTextField jtfAdresse;
	private JFormattedTextField jftfCodePostal;
	private JTextField jtfVille;
	private JComboBox jcbType;
	private JTextArea jtaNote;

	private JButton jbValider;
	private JButton jbAnnuler;

	public EntiteDialog(JFrame parent) {
		super(parent, "", true); //$NON-NLS-1$

		initialize();
	}

	public EntiteDialog(JDialog parent) {
		super(parent, "", true); //$NON-NLS-1$

		initialize();
	}

	public void initialize() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel entitePane = new JPanel();
		JPanel buttonPane = new JPanel();

		JLabel jlNom = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.nom")); //$NON-NLS-1$
		JLabel jlAgrement = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.agrement")); //$NON-NLS-1$
		JLabel jlAdresse = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.adresse")); //$NON-NLS-1$
		JLabel jlCodePostal = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.codepostal")); //$NON-NLS-1$
		JLabel jlVille = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.ville")); //$NON-NLS-1$
		JLabel jlType = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.type")); //$NON-NLS-1$
		JLabel jlNote = new JLabel(ApplicationCore.ajrLibelle.getResourceString("entite.note")); //$NON-NLS-1$

		jtfNom = new JTextField("", 30); //$NON-NLS-1$
		jtfNom.setEditable(false);
		jftfAgrement = new JTextField("", 6); //$NON-NLS-1$
		jftfAgrement.setEditable(false);
		jtfAdresse = new JTextField("", 30); //$NON-NLS-1$
		jtfVille = new JTextField("", 10); //$NON-NLS-1$
		jtfVille.setEditable(false);
		jcbType = new JComboBox(new String[] { "Fédération", "Ligue", "Comité Départemental", "Compagnie" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		jtaNote = new JTextArea(5, 30);

		MaskFormatter formatter;
		try {
			formatter = new MaskFormatter("#####"); //$NON-NLS-1$
			formatter.setPlaceholderCharacter('_');
			jftfCodePostal = new JFormattedTextField(formatter);
			jftfCodePostal.setColumns(4);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		gridbagComposer.setParentPanel(entitePane);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlNom, c);
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jtfNom, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlAgrement, c);
		gridbagComposer.addComponentIntoGrid(jftfAgrement, c);
		gridbagComposer.addComponentIntoGrid(jlType, c);
		gridbagComposer.addComponentIntoGrid(jcbType, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlAdresse, c);
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jtfAdresse, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlCodePostal, c);
		gridbagComposer.addComponentIntoGrid(jftfCodePostal, c);
		gridbagComposer.addComponentIntoGrid(jlVille, c);
		gridbagComposer.addComponentIntoGrid(jtfVille, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNote, c);
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jtaNote), c);

		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		jbValider = new JButton(ApplicationCore.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbValider.addActionListener(this);
		jbAnnuler = new JButton(ApplicationCore.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
		jbAnnuler.addActionListener(this);

		buttonPane.add(jbValider);
		buttonPane.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(entitePane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	public void showEntite(Entite curEntite) {
		if (curEntite != null) {
			this.entite = curEntite;

			jtfNom.setText(curEntite.getNom());
			jtfAdresse.setText(curEntite.getAdresse());
			jtfVille.setText(curEntite.getVille());
			jcbType.setSelectedIndex(curEntite.getType());
			jtaNote.setText(curEntite.getNote());
			jftfAgrement.setText(curEntite.getAgrement());
			jftfCodePostal.setValue(curEntite.getCodePostal());
			setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == jbAnnuler) {
			setVisible(false);
		} else {
			entite.setNom(jtfNom.getText());
			entite.setAdresse(jtfAdresse.getText());
			entite.setVille(jtfVille.getText());
			entite.setType(jcbType.getSelectedIndex());
			entite.setNote(jtaNote.getText());
			entite.setAgrement(jftfAgrement.getText());
			entite.setCodePostal((String) jftfCodePostal.getValue());

			entite.save();

			setVisible(false);
		}
	}
}
