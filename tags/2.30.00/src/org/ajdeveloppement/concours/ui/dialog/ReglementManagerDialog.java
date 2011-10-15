/*
 * Créé le 7 août 2008 à 11:35:17 pour ConcoursJeunes
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
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizableString;
import org.ajdeveloppement.apps.localisation.LocalizationHandler;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Federation;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.manager.FederationManager;
import org.concoursjeunes.manager.ReglementManager;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.painter.GlossPainter;

import com.lowagie.text.Font;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Localizable(textMethod="setTitle",value="reglementmanager.title")
public class ReglementManagerDialog extends JDialog implements ListSelectionListener, MouseListener, ActionListener {
	
	private JFrame parentframe;
	private Profile profile;
	private AjResourcesReader localisation;
	
	@Localizable("reglementmanager.header")
	private JXHeader jxhReglementManager = new JXHeader();
	
	@Localizable("reglementmanager.federations")
	private JLabel jlFederations	= new JLabel();
	@Localizable("reglementmanager.categories")
	private JLabel jlCategories		= new JLabel();
	@Localizable("reglementmanager.reglements")
	private JLabel jlReglements		= new JLabel();
	private AJList<Federation> ajlFederations	= new AJList<Federation>();
	private AJList<LocalizableString> ajlCategories	= new AJList<LocalizableString>();
	private AJList<Reglement> ajlReglements	= new AJList<Reglement>();
	
	@Localizable(value="",tooltip="reglementmanager.new")
	private JButton jbNewFederation	= new JButton();
	@Localizable(value="",tooltip="reglementmanager.edit")
	private JButton jbEditFederation = new JButton();
	@Localizable(value="",tooltip="reglementmanager.delete")
	private JButton jbDeleteFederation = new JButton();
	
	@Localizable("reglementmanager.category.all")
	private final LocalizableString lsAllCategory = new LocalizableString();
	@Localizable("reglementmanager.category.young")
	private final LocalizableString lsYoungCategory = new LocalizableString();
	@Localizable("reglementmanager.category.indoor")
	private final LocalizableString lsIndoorCategory = new LocalizableString();
	@Localizable("reglementmanager.category.outdoor")
	private final LocalizableString lsOutdoorCategory = new LocalizableString();
	@Localizable("reglementmanager.category.other")
	private final LocalizableString lsOtherCategory = new LocalizableString();
	
	@Localizable(value="",tooltip="reglementmanager.new")
	private JButton jbNew			= new JButton();
	@Localizable(value="",tooltip="reglementmanager.edit")
	private JButton jbEdit			= new JButton();
	@Localizable(value="",tooltip="reglementmanager.delete")
	private JButton jbDelete		= new JButton();
	@Localizable("reglementmanager.export")
	private JButton jbExport		= new JButton();
	@Localizable("reglementmanager.import")
	private JButton jbImport		= new JButton();
	
	@Localizable("bouton.valider")
	private JButton jbValider		= new JButton();
	@Localizable("bouton.annuler")
	private JButton jbAnnuler		= new JButton();
	@Localizable("bouton.fermer")
	private JButton jbFermer		= new JButton();
	
	private ReglementManager reglementManager;
	private Reglement selectedReglement;
	private boolean selection		= false;
	/**
	 * 
	 */
	public ReglementManagerDialog(JFrame parentframe, Profile profile) {
		super(parentframe, true);
		
		this.profile = profile;
		this.parentframe = parentframe;
		this.localisation = profile.getLocalisation();
		
		reglementManager = ReglementManager.getInstance();
		
		init();
		affectLabels();
	}
	
	/**
	 * 
	 */
	private void init() {
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gbcomposer = new GridbagComposer();
		
		GlossPainter gloss = new GlossPainter();
		jxhReglementManager.setBackground(new Color(200,200,255));
		jxhReglementManager.setBackgroundPainter(gloss);
		jxhReglementManager.setTitleFont(jxhReglementManager.getTitleFont().deriveFont(16.0f));
		
		jbNewFederation.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.addcriteria")); //$NON-NLS-1$
		jbEditFederation.setEnabled(false);
		jbEditFederation.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.opendocument")); //$NON-NLS-1$
		jbDeleteFederation.setEnabled(false);
		jbDeleteFederation.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.removeelement")); //$NON-NLS-1$
		jbNew.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.addcriteria")); //$NON-NLS-1$
		jbEdit.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.opendocument")); //$NON-NLS-1$
		jbDelete.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.removeelement")); //$NON-NLS-1$
		
		ajlFederations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlFederations.addListSelectionListener(this);
		ajlFederations.setCellRenderer(new DefaultListCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof Federation)
					value = ((Federation)value).getSigleFederation();
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		ajlCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlCategories.addListSelectionListener(this);
		ajlReglements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlReglements.addListSelectionListener(this);
		ajlReglements.addMouseListener(this);
		ajlReglements.setCellRenderer(new DefaultListCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof Reglement) {
					Reglement reglement = (Reglement)value;
					
					String description = reglement.getDescription();
					if(description == null)
						description = ""; //$NON-NLS-1$
					
					value = "<html><b>" + reglement.getDisplayName() + "</b> - " + reglement.getFederation().getSigleFederation() //$NON-NLS-1$ //$NON-NLS-2$
							+ "<p style=\"margin-left: 10px;\"><span style=\"font-style: italic; color: #777777;\">" + description.replace("\n", "<br>") + "</span></p> </html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
				
				JLabel comp = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				comp.setFont(comp.getFont().deriveFont(Font.NORMAL));
				if(index % 2 == 0 && !cellHasFocus) {
					comp.setBackground(new Color(220, 220, 220));
				}
				return comp;
			}
		});
		
		ajlCategories.add(lsAllCategory);
		ajlCategories.add(lsYoungCategory);
		ajlCategories.add(lsIndoorCategory);
		ajlCategories.add(lsOutdoorCategory);
		ajlCategories.add(lsOtherCategory);
		
		jbNewFederation.setMargin(new Insets(0,0,0,0));
		jbNewFederation.addActionListener(this);
		jbEditFederation.setMargin(new Insets(0,0,0,0));
		jbEditFederation.addActionListener(this);
		jbDeleteFederation.setMargin(new Insets(0,0,0,0));
		jbDeleteFederation.addActionListener(this);
		jbNew.setMargin(new Insets(0,0,0,0));
		jbNew.addActionListener(this);
		jbEdit.setMargin(new Insets(0,0,0,0));
		jbEdit.setEnabled(false);
		jbEdit.addActionListener(this);
		jbDelete.setMargin(new Insets(0,0,0,0));
		jbDelete.setEnabled(false);
		jbDelete.addActionListener(this);
		jbExport.setMargin(new Insets(0,0,0,0));
		jbExport.setEnabled(false);
		jbExport.addActionListener(this);
		jbImport.setMargin(new Insets(0,0,0,0));
		jbImport.addActionListener(this);
		
		ajlFederations.setFixedCellWidth(150);
		ajlReglements.setFixedCellWidth(400);
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		jbFermer.addActionListener(this);
		
		JPanel jpSelection = new JPanel();
		gbcomposer.setParentPanel(jpSelection);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		gbcomposer.addComponentIntoGrid(jlFederations, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		gbcomposer.addComponentIntoGrid(jbNewFederation, c);
		gbcomposer.addComponentIntoGrid(jbEditFederation, c);
		gbcomposer.addComponentIntoGrid(jbDeleteFederation, c);
		c.gridy++;
		c.gridwidth = 3;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gbcomposer.addComponentIntoGrid(new JScrollPane(ajlFederations), c);
		c.gridy++;
		c.weightx = 0.0;
		c.weighty = 0.0;
		gbcomposer.addComponentIntoGrid(jlCategories, c);
		c.gridy++;
		c.weighty = 1.0;
		c.weightx = 1.0;
		gbcomposer.addComponentIntoGrid(new JScrollPane(ajlCategories), c);
		
		JPanel jpReglements = new JPanel();
		gbcomposer.setParentPanel(jpReglements);
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridwidth = 5;
		gbcomposer.addComponentIntoGrid(jlReglements, c);
		c.gridy++;
		c.gridwidth = 1;
		gbcomposer.addComponentIntoGrid(jbNew, c);
		gbcomposer.addComponentIntoGrid(jbEdit, c);
		gbcomposer.addComponentIntoGrid(jbDelete, c);
		gbcomposer.addComponentIntoGrid(jbExport, c);
		gbcomposer.addComponentIntoGrid(jbImport, c);
		c.gridy++;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.weightx = 1.0;
		gbcomposer.addComponentIntoGrid(new JScrollPane(ajlReglements), c);
		
		JPanel jpAction = new JPanel();
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		jpAction.add(jbFermer);
		
		setLayout(new BorderLayout());
		
		add(jxhReglementManager, BorderLayout.NORTH);
		add(jpSelection, BorderLayout.WEST);
		add(jpReglements, BorderLayout.CENTER);
		add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLabels() {
		Localizator.localize(this, localisation, Collections.<Class<?>, LocalizationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));
	}
	
	private void completePanel() {
		ajlFederations.clear();
		ajlFederations.add(new Federation("", 0, localisation.getResourceString("reglementmanager.federation.all"))); //$NON-NLS-1$ //$NON-NLS-2$
		for(Federation federation : FederationManager.getAvailableFederations()) {
			ajlFederations.add(federation);
		}
		ajlFederations.setSelectedIndex(0);
		ajlCategories.setSelectedIndex(0);
		
		jbFermer.setVisible(!selection);
		jbValider.setVisible(selection);
		jbAnnuler.setVisible(selection);
	}
	
	/**
	 * Affiche la boite de dialogue de gestion des réglements
	 * @param selection si true, alors permet la sélection d'un réglement, sinon permet juste la sélection mais ne renvoie
	 * rien.
	 * @return si selection=true alors renvoie le réglement selectionné
	 */
	public Reglement showReglementManagerDialog(boolean selection) {
		this.selection = selection;
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		return selectedReglement;
	}
	
	private void showReglementDialog() {
		ReglementDialog reglementDialog = new ReglementDialog(this, (Reglement)ajlReglements.getSelectedValue(), localisation);
		if(reglementDialog.showReglementDialog() == DefaultDialogReturn.OK) {
			Reglement modifiedReglement = reglementDialog.getReglement();
			try {
				modifiedReglement.save();
			} catch(ObjectPersistenceException e1) {
				DisplayableErrorHelper.displayException(e1);
				e1.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == ajlFederations || e.getSource() == ajlCategories) {
			if(ajlFederations.getSelectedIndex() == 0 && ajlCategories.getSelectedIndex() == 0) {
				jbEditFederation.setEnabled(false);
				jbDeleteFederation.setEnabled(false);
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getAvailableReglements()) {
					ajlReglements.add(reglement);
				}
			} else if(ajlFederations.getSelectedIndex() == 0) {
				jbEditFederation.setEnabled(false);
				jbDeleteFederation.setEnabled(false);
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getReglementsForCategory(ajlCategories.getSelectedIndex())) {
					ajlReglements.add(reglement);
				}
			} else if(ajlCategories.getSelectedIndex() == 0) {
				jbEditFederation.setEnabled(true);
				jbDeleteFederation.setEnabled(reglementManager.getReglementsForFederation((Federation)ajlFederations.getSelectedValue()).size() == 0);
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getReglementsForFederation((Federation)ajlFederations.getSelectedValue())) {
					ajlReglements.add(reglement);
				}
			} else {
				jbEditFederation.setEnabled(true);
				jbDeleteFederation.setEnabled(reglementManager.getReglementsForFederation((Federation)ajlFederations.getSelectedValue()).size() == 0);
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getReglementsForFederationAndCategory(
						(Federation)ajlFederations.getSelectedValue(), 
						ajlCategories.getSelectedIndex())) {
					ajlReglements.add(reglement);
				}
			}
		} else if(e.getSource() == ajlReglements) {
			Reglement reglement = (Reglement)ajlReglements.getSelectedValue();
			if(reglement != null) {
				jbEdit.setEnabled(true);
				jbDelete.setEnabled(reglement.isRemovable());
				jbExport.setEnabled(true);
			} else {
				jbEdit.setEnabled(false);
				jbDelete.setEnabled(false);
				jbExport.setEnabled(false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == ajlReglements && e.getClickCount() == 2) {
			showReglementDialog();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			selectedReglement = (Reglement)ajlReglements.getSelectedValue();
			setVisible(false);
		} else if(e.getSource() == jbAnnuler || e.getSource() == jbFermer) {
			setVisible(false);
		} else if(e.getSource() == jbNew) {
			NewReglementDialog newReglementDialog = new NewReglementDialog(this, profile);
			Reglement reglement = newReglementDialog.showNewReglementDialog();
			if(reglement != null) {
				try {
					reglementManager.addReglement(reglement);
				} catch (ObjectPersistenceException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
				}
			}
			completePanel();
		} else if(e.getSource() == jbEdit) {
			if(ajlReglements.getSelectedIndex() > -1)
				showReglementDialog();
		} else if(e.getSource() == jbDelete) {
			if(JOptionPane.showConfirmDialog(this, localisation.getResourceString("reglementmanager.delete.confirm"), "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
				Reglement reglement = (Reglement)ajlReglements.getSelectedValue(); 
				try {
					reglementManager.removeReglement(reglement);
					ajlReglements.remove(reglement);
				} catch (ObjectPersistenceException e1) {
					if(e1.getMessage().equals("delete this Reglement is not authorized because there is official")) { //$NON-NLS-1$
						JOptionPane.showMessageDialog(this, localisation.getResourceString("reglementmanager.delete.unauhorized"), "", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						e1.printStackTrace();
						DisplayableErrorHelper.displayException(e1);
					}
				}
			}
		} else if(e.getSource() == jbExport) {
			Reglement reglement = (Reglement)ajlReglements.getSelectedValue();
			
			if(reglement != null) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(localisation.getResourceString("filetype.reglement"), "reglement")); //$NON-NLS-1$ //$NON-NLS-2$
				fileChooser.setSelectedFile(new File(reglement.getName() + ".reglement")); //$NON-NLS-1$
				if(fileChooser.showSaveDialog(parentframe) == JFileChooser.APPROVE_OPTION) {
					try {
						ReglementManager.exportReglement(reglement, fileChooser.getSelectedFile());
					} catch (FileNotFoundException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (IOException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
				}
			}
		} else if(e.getSource() == jbImport) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter(localisation.getResourceString("filetype.reglement"), "reglement")); //$NON-NLS-1$ //$NON-NLS-2$
			if(fileChooser.showOpenDialog(parentframe) == JFileChooser.APPROVE_OPTION) {
				try {
					reglementManager.importReglement(fileChooser.getSelectedFile());
					completePanel();
				} catch (IOException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
				} catch (ObjectPersistenceException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == jbNewFederation || e.getSource() == jbEditFederation) {
			Federation federation = null;
			if(e.getSource() == jbEditFederation && ajlFederations.getSelectedIndex() > 0)
				federation = (Federation)ajlFederations.getSelectedValue();
			
			FederationDialog newFederationDialog = new FederationDialog(parentframe, profile);
			federation = newFederationDialog.showFederationDialog(federation);
			if(federation != null) {
				if(e.getSource() == jbNewFederation)
					ajlFederations.add(federation);
				ajlFederations.repaint();
			}
		} else if(e.getSource() == jbDeleteFederation) {
			if(JOptionPane.showConfirmDialog(parentframe, localisation.getResourceString("reglementmanager.deletefederation.confirm"), "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
				Federation federation = (Federation)ajlFederations.getSelectedValue();
				
				if(reglementManager.getReglementsForFederation(federation).size() == 0) {
					try {
						reglementManager.removeFederation(federation);
						
						ajlFederations.remove(federation);
						
						ajlFederations.setSelectedIndex(0);
					} catch (ObjectPersistenceException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
