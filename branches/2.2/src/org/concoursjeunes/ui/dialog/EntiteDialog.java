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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;

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

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.concoursjeunes.Entite;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * @author Aurélien JEOFFRAY
 */
public class EntiteDialog extends JDialog implements ActionListener {
	private AjResourcesReader localisation;
	private Entite entite;

	@Localisable("entite.nom")
	private JLabel jlNom = new JLabel();
	@Localisable("entite.agrement")
	private JLabel jlAgrement = new JLabel();
	@Localisable("entite.adresse")
	private JLabel jlAdresse = new JLabel();
	@Localisable("entite.codepostal")
	private JLabel jlCodePostal = new JLabel();
	@Localisable("entite.ville")
	private JLabel jlVille = new JLabel();
	@Localisable("entite.type")
	private JLabel jlType = new JLabel();
	@Localisable("entite.note")
	private JLabel jlNote = new JLabel();
	private JTextField jtfNom;
	private JTextField jftfAgrement;
	private JTextField jtfAdresse;
	private JFormattedTextField jftfCodePostal;
	private JTextField jtfVille;
	private JComboBox jcbType;
	private JTextArea jtaNote;

	@Localisable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();

	public EntiteDialog(JFrame parent, AjResourcesReader localisation) {
		super(parent, "", true); //$NON-NLS-1$

		this.localisation = localisation;
		
		init();
		affectLibelle();
		completePanel();
	}

	public EntiteDialog(JDialog parent, AjResourcesReader localisation) {
		super(parent, "", true); //$NON-NLS-1$
		
		this.localisation = localisation;

		init();
		affectLibelle();
		completePanel();
	}

	private void init() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel entitePane = new JPanel();
		JPanel buttonPane = new JPanel();

		jtfNom = new JTextField("", 30); //$NON-NLS-1$
		jtfNom.setEditable(false);
		jftfAgrement = new JTextField("", 6); //$NON-NLS-1$
		jftfAgrement.setEditable(false);
		jtfAdresse = new JTextField("", 30); //$NON-NLS-1$
		jtfVille = new JTextField("", 10); //$NON-NLS-1$
		jtfVille.setEditable(false);
		jcbType = new JComboBox(new String[] { "Fédération", "Ligue", "Comité Départemental", "Compagnie" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		jtaNote = new JTextArea(5, 30);
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);

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

		buttonPane.add(jbValider);
		buttonPane.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(entitePane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void affectLibelle() {
		Localisator.localize(this, localisation);
	}
	
	private void completePanel() {
		if(entite != null) {
			jtfNom.setText(entite.getNom());
			jtfAdresse.setText(entite.getAdresse());
			jtfVille.setText(entite.getVille());
			jcbType.setSelectedIndex(entite.getType());
			jtaNote.setText(entite.getNote());
			jftfAgrement.setText(entite.getAgrement());
			jftfCodePostal.setValue(entite.getCodePostal());
		}
	}

	public void showEntiteDialog() {
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * @return entite
	 */
	public Entite getEntite() {
		return entite;
	}

	/**
	 * @param entite entite à définir
	 */
	public void setEntite(Entite entite) {
		this.entite = entite;
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
			
			try {
				entite.save();
			} catch (SqlPersistanceException e) {
				JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
            			null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			}

			setVisible(false);
		}
	}
}
