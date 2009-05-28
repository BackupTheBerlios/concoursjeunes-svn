/*
 * Créer le 30 déc. 07 à 15:02:57 pour ConcoursJeunes
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
package org.concoursjeunes.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.Localisable;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.concoursjeunes.plugins.Plugin.Type;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class DisablePluginDialog extends JDialog implements ActionListener {
	
	private AjResourcesReader localisation;
	
	@Localisable("disableplugin.columnname")
	private JLabel jlColumnName = new JLabel();
	@Localisable("disableplugin.columnstate")
	private JLabel jlColumnState = new JLabel();
	
	private Hashtable<String, JLabel> jlNomsPlugins = new Hashtable<String, JLabel>();
	private Hashtable<String, JCheckBox> jcbStatePlugins = new Hashtable<String, JCheckBox>();
	
	@Localisable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	private List<PluginMetadata> plugins;
	
	public DisablePluginDialog(JFrame parentframe, AjResourcesReader localisation) {
		super(parentframe, true);		
		
		this.localisation = localisation;
		
		PluginLoader pl = new PluginLoader();
		plugins = pl.getPlugins(Type.STARTUP);
		
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpPrincipal = new JPanel();
		JPanel jpAction = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();
		
		jbAnnuler.addActionListener(this);
		jbValider.addActionListener(this);

		gridbagComposer.setParentPanel(jpPrincipal);
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER;
		gridbagComposer.addComponentIntoGrid(jlColumnName, c);
		gridbagComposer.addComponentIntoGrid(jlColumnState, c);
		for (PluginMetadata pm : plugins) {
			JLabel label = new JLabel(pm.getOptionLabel());
			label.setToolTipText(pm.getInfo());
			jlNomsPlugins.put(pm.getName(), label);
			jcbStatePlugins.put(pm.getName(), new JCheckBox("", true));  //$NON-NLS-1$
			
			c.gridy++;
			c.anchor = GridBagConstraints.WEST;
			gridbagComposer.addComponentIntoGrid(jlNomsPlugins.get(pm.getName()), c);
			c.anchor = GridBagConstraints.CENTER;
			gridbagComposer.addComponentIntoGrid(jcbStatePlugins.get(pm.getName()), c);
		}
		c.gridy++;
		c.gridwidth = 2;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(Box.createGlue(), c);
		
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(jpPrincipal), BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		AppUtilities.localize(this, localisation);
	}
	
	@SuppressWarnings("unchecked")
	private void completePanel() {
		List<String> disablePlugin = null;
		try {
			disablePlugin = (List<String>)XMLSerializer.loadXMLStructure(
					new File(ApplicationCore.userRessources.getConfigPathForUser(), "disable_plugins.xml"), false);  //$NON-NLS-1$
		} catch (IOException e) {
			JXErrorPane.showDialog(this, 
					new ErrorInfo(localisation.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
							null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		if(disablePlugin != null) {
			for(String name : disablePlugin) {
				jcbStatePlugins.get(name).setSelected(false);
			}
		}
	}
	
	public void showDisablePluginDialog() {
		completePanel();
		setSize(new Dimension(400,300));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			List<String> disablePlugin = new ArrayList<String>();
			for(PluginMetadata pm : plugins) {
				if(!jcbStatePlugins.get(pm.getName()).isSelected()) {
					disablePlugin.add(pm.getName());
				}
			}

			try {
				XMLSerializer.saveXMLStructure(
						new File(ApplicationCore.userRessources.getConfigPathForUser(), "disable_plugins.xml"), disablePlugin, false);  //$NON-NLS-1$
				setVisible(false);
			} catch (IOException e1) {
				JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			}
			
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}
}
