/*
 * Créé le 10 mai 07 à 10:04:53 pour ConcoursJeunes
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
package org.concoursjeunes.plugins.ResultArcImport;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.concoursjeunes.Profile;
import org.concoursjeunes.plugins.Plugin;
import org.concoursjeunes.plugins.PluginEntry;

/**
 * @author Aurélien JEOFFRAY
 * 
 */
@Plugin(type = Plugin.Type.ON_DEMAND)
public class ResultArcImportDialog extends JDialog implements ActionListener, ResultArcImportThreadListener {

	private Profile profile;
	
	@Localisable("emplacement.lffta")
	private JLabel jlEmplacementLFFTA = new JLabel();
	private JTextField jtfEmplacementLFFTA = new JTextField("C:\\ResultArc", 30); //$NON-NLS-1$
	@Localisable("button.parcourir")
	private JButton jbParcourir = new JButton();
	@Localisable("button.start")
	private JButton jbSart = new JButton();
	private JProgressBar jpbProgression = new JProgressBar();

	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();

	private AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.ResultArcImport.ResultArcImportPlugin_libelle", ResultArcImportDialog.class.getClassLoader());  //$NON-NLS-1$

	public ResultArcImportDialog(JFrame parentframe, Profile profile) {
		super(parentframe);
		
		this.profile = profile;
		pluginLocalisation.setLocale(new Locale(profile.getConfiguration().getLangue()));

		init();
		affectLibelle();
	}

	private void init() {
		JPanel jpAnnulation = new JPanel();
		JPanel jpGeneral = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();

		jbAnnuler.addActionListener(this);
		jbParcourir.addActionListener(this);
		jbParcourir.setMargin(new Insets(0, 0, 0, 0));
		jbSart.addActionListener(this);

		jpbProgression.setStringPainted(true);

		gridbagComposer.setParentPanel(jpGeneral);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlEmplacementLFFTA, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jtfEmplacementLFFTA, c);
		gridbagComposer.addComponentIntoGrid(jbParcourir, c);
		c.gridy++;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		gridbagComposer.addComponentIntoGrid(jbSart, c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jpbProgression, c);

		jpAnnulation.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAnnulation.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpGeneral, BorderLayout.CENTER);
		getContentPane().add(jpAnnulation, BorderLayout.SOUTH);
	}

	private void affectLibelle() {
		Localisator.localize(this, pluginLocalisation);
	}

	private void completePane() {

	}

	@PluginEntry
	public void showFFTAImportDialog() {
		completePane();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbParcourir) {
			JFileChooser jfc = new JFileChooser(new File(jtfEmplacementLFFTA.getText()));
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				jtfEmplacementLFFTA.setText(jfc.getSelectedFile().getPath());
			}
		} else if (e.getSource() == jbSart) {
			ResultArcImportThread fftaIT = new ResultArcImportThread(profile.getLocalisation());
			fftaIT.setParentFrame(this);
			fftaIT.setFftalogpath(jtfEmplacementLFFTA.getText());
			fftaIT.addResultArcImportThreadListener(this);
			jpbProgression.setIndeterminate(true);
			fftaIT.start();
		} else if (e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.plugins.ResultArcImport.ResultArcImportThreadListener#importFinished()
	 */
	public void importFinished() {
		JOptionPane.showMessageDialog(this, pluginLocalisation.getResourceString("message.import.fin"), //$NON-NLS-1$
				pluginLocalisation.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE);  //$NON-NLS-1$
		setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.plugins.ResultArcImport.ResultArcImportThreadListener#progressionInfo(java.lang.String)
	 */
	public void progressionInfo(String info) {
		jpbProgression.setString(info);
		// jlProgression.repaint();
	}
}
