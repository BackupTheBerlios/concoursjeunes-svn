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
package org.concoursjeunes.plugins.FFTAImport;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.Localisable;
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
public class FFTAImportDialog extends JDialog implements ActionListener, FFTAImportThreadListener {

	private Profile profile;
	
	@Localisable("import.download")
	private JRadioButton jrbFTPDownload = new JRadioButton();
	@Localisable("import.local")
	private JRadioButton jrbLocal = new JRadioButton();
	private JTextField jtfEmplacementLFFTA = new JTextField("", 30); //$NON-NLS-1$
	private JButton jbParcourir = new JButton();
	private JButton jbSart = new JButton();
	private JProgressBar jpbProgression = new JProgressBar();

	private JButton jbAnnuler = new JButton();

	private AjResourcesReader pluginLocalisation = new AjResourcesReader(
			"org.concoursjeunes.plugins.FFTAImport.FFTAImportPlugin_libelle", //$NON-NLS-1$
			FFTAImportDialog.class.getClassLoader());

	public FFTAImportDialog(JFrame parentframe, Profile profile) {
		super(parentframe);
		
		this.profile = profile;
		pluginLocalisation.setLocale(new Locale(profile.getConfiguration().getLangue()));

		init();
		affectLibelle();
	}

	private void init() {
		JPanel jpAction = new JPanel();
		JPanel jpGeneral = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();

		jbAnnuler.addActionListener(this);
		jbParcourir.addActionListener(this);
		jbParcourir.setMargin(new Insets(0, 0, 0, 0));
		jbParcourir.setEnabled(false);
		jbSart.addActionListener(this);
		jrbFTPDownload.setSelected(true);
		jtfEmplacementLFFTA.setEnabled(false);
		jrbFTPDownload.addActionListener(this);
		jrbLocal.addActionListener(this);

		jpbProgression.setStringPainted(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(jrbFTPDownload);
		group.add(jrbLocal);

		gridbagComposer.setParentPanel(jpGeneral);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jrbFTPDownload, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jrbLocal, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jtfEmplacementLFFTA, c);
		gridbagComposer.addComponentIntoGrid(jbParcourir, c);
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jpbProgression, c);

		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbSart);
		jpAction.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpGeneral, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}

	private void affectLibelle() {
		AppUtilities.localize(this, pluginLocalisation);
		
		jbParcourir.setText(pluginLocalisation.getResourceString("button.parcourir")); //$NON-NLS-1$
		jbSart.setText(pluginLocalisation.getResourceString("button.start")); //$NON-NLS-1$

		jbAnnuler.setText(profile.getLocalisation().getResourceString("bouton.annuler")); //$NON-NLS-1$
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
		if(e.getSource() == jrbFTPDownload) {
			jtfEmplacementLFFTA.setEnabled(false);
			jbParcourir.setEnabled(false);
		} else if (e.getSource() == jrbLocal) {
			jtfEmplacementLFFTA.setEnabled(true);
			jbParcourir.setEnabled(true);
		} else if (e.getSource() == jbParcourir) {
			JFileChooser jfc = new JFileChooser(new File(jtfEmplacementLFFTA.getText()));
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				jtfEmplacementLFFTA.setText(jfc.getSelectedFile().getPath());
			}
		} else if (e.getSource() == jbSart) {
			FFTAImportThread fftaIT = new FFTAImportThread(profile.getLocalisation());
			fftaIT.setParentFrame(this);
			if(jrbFTPDownload.isSelected())
				fftaIT.setFftalogpath(jtfEmplacementLFFTA.getText());
			fftaIT.addFFTAImportThreadListener(this);
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
				pluginLocalisation.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
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
