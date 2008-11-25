/*
 * Créer le 7 août 2008 à 11:35:17 pour ConcoursJeunes
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
import static org.concoursjeunes.ApplicationCore.ajrParametreAppli;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.JAXBException;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Federation;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.ReglementManager;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.ui.AJList;
import ajinteractive.standard.ui.GridbagComposer;
import ajinteractive.standard.utilities.io.AJFileFilter;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ReglementManagerDialog extends JDialog implements ListSelectionListener, MouseListener, ActionListener {
	
	private JFrame parentframe;
	
	private JLabel jlFederations	= new JLabel();
	private JLabel jlCategories		= new JLabel();
	private JLabel jlReglements		= new JLabel();
	private AJList ajlFederations	= new AJList();
	private AJList ajlCategories	= new AJList();
	private AJList ajlReglements	= new AJList();
	
	private JButton jbNew			= new JButton();
	private JButton jbEdit			= new JButton();
	private JButton jbDelete		= new JButton();
	private JButton jbExport		= new JButton();
	private JButton jbImport		= new JButton();
	
	private JButton jbValider		= new JButton();
	private JButton jbAnnuler		= new JButton();
	private JButton jbFermer		= new JButton();
	
	private ReglementManager reglementManager;
	private Reglement selectedReglement;
	private boolean selection		= false;
	/**
	 * 
	 */
	public ReglementManagerDialog(JFrame parentframe) {
		super(parentframe, true);
		
		this.parentframe = parentframe;
		
		reglementManager = new ReglementManager();
		
		init();
		affectLibelle();
	}
	
	/**
	 * 
	 */
	private void init() {
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gbcomposer = new GridbagComposer(); 
		
		ajlFederations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlFederations.addListSelectionListener(this);
		ajlCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlCategories.addListSelectionListener(this);
		ajlReglements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ajlReglements.addListSelectionListener(this);
		ajlReglements.addMouseListener(this);
		
		jbNew.setMargin(new Insets(0,0,0,0));
		jbNew.addActionListener(this);
		jbEdit.setMargin(new Insets(0,0,0,0));
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
		gbcomposer.addComponentIntoGrid(jlFederations, c);
		c.gridy++;
		c.weighty = 1.0;
		gbcomposer.addComponentIntoGrid(new JScrollPane(ajlFederations), c);
		c.gridy++;
		c.weighty = 0.0;
		gbcomposer.addComponentIntoGrid(jlCategories, c);
		c.gridy++;
		c.weighty = 1.0;
		gbcomposer.addComponentIntoGrid(new JScrollPane(ajlCategories), c);
		
		JPanel jpReglements = new JPanel();
		gbcomposer.setParentPanel(jpReglements);
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
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
		
		add(jpSelection, BorderLayout.WEST);
		add(jpReglements, BorderLayout.CENTER);
		add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		setTitle(ajrLibelle.getResourceString("reglementmanager.title")); //$NON-NLS-1$
		
		jlFederations.setText(ajrLibelle.getResourceString("reglementmanager.federations")); //$NON-NLS-1$
		jlCategories.setText(ajrLibelle.getResourceString("reglementmanager.categories")); //$NON-NLS-1$
		jlReglements.setText(ajrLibelle.getResourceString("reglementmanager.reglements")); //$NON-NLS-1$
		
		jbNew.setIcon(new ImageIcon(ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ajrParametreAppli.getResourceString("file.icon.addcriteria"))); //$NON-NLS-1$
		jbNew.setToolTipText(ajrLibelle.getResourceString("reglementmanager.new")); //$NON-NLS-1$
		jbEdit.setIcon(new ImageIcon(ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ajrParametreAppli.getResourceString("file.icon.opendocument"))); //$NON-NLS-1$
		jbEdit.setToolTipText(ajrLibelle.getResourceString("reglementmanager.edit")); //$NON-NLS-1$
		jbDelete.setIcon(new ImageIcon(ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ajrParametreAppli.getResourceString("file.icon.removeelement"))); //$NON-NLS-1$
		jbDelete.setToolTipText(ajrLibelle.getResourceString("reglementmanager.delete")); //$NON-NLS-1$
		jbExport.setText(ajrLibelle.getResourceString("reglementmanager.export")); //$NON-NLS-1$
		jbImport.setText(ajrLibelle.getResourceString("reglementmanager.import")); //$NON-NLS-1$
		
		ajlCategories.clear();
		ajlCategories.add(ajrLibelle.getResourceString("reglementmanager.category.all")); //$NON-NLS-1$
		ajlCategories.add(ajrLibelle.getResourceString("reglementmanager.category.young")); //$NON-NLS-1$
		ajlCategories.add(ajrLibelle.getResourceString("reglementmanager.category.indoor")); //$NON-NLS-1$
		ajlCategories.add(ajrLibelle.getResourceString("reglementmanager.category.outdoor")); //$NON-NLS-1$
		ajlCategories.add(ajrLibelle.getResourceString("reglementmanager.category.other")); //$NON-NLS-1$
		
		jbValider.setText(ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$
		jbFermer.setText(ajrLibelle.getResourceString("bouton.fermer")); //$NON-NLS-1$
	}
	
	private void completePanel() {
		ajlFederations.clear();
		ajlFederations.add(new Federation("", 0, ajrLibelle.getResourceString("reglementmanager.federation.all"))); //$NON-NLS-1$ //$NON-NLS-2$
		for(Federation federation : reglementManager.getFederations()) {
			ajlFederations.add(federation);
		}
		ajlFederations.setSelectedIndex(0);
		ajlCategories.setSelectedIndex(0);
		
		jbFermer.setVisible(!selection);
		jbValider.setVisible(selection);
		jbAnnuler.setVisible(selection);
	}
	
	public Reglement showReglementManagerDialog(boolean selection) {
		this.selection = selection;
		completePanel();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		return selectedReglement;
	}
	
	private void showReglementDialog() {
		ReglementDialog reglementDialog = new ReglementDialog(parentframe, (Reglement)ajlReglements.getSelectedValue());
		Reglement modifiedReglement = reglementDialog.showReglementDialog();
		if (modifiedReglement != null) {
			try {
				modifiedReglement.save();
			} catch(SQLException e1) {
				JXErrorPane.showDialog(parentframe, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
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
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getAvailableReglements()) {
					ajlReglements.add(reglement);
				}
			} else if(ajlFederations.getSelectedIndex() == 0) {
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getReglementsForCategory(ajlCategories.getSelectedIndex())) {
					ajlReglements.add(reglement);
				}
			} else if(ajlCategories.getSelectedIndex() == 0) {
				ajlReglements.clear();
				for(Reglement reglement : reglementManager.getReglementsForFederation((Federation)ajlFederations.getSelectedValue())) {
					ajlReglements.add(reglement);
				}
			} else {
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
				jbDelete.setEnabled(reglement.isRemovable());
				jbExport.setEnabled(true);
			} else {
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
			NewReglementDialog newReglementDialog = new NewReglementDialog(parentframe);
			Reglement reglement = newReglementDialog.showNewReglementDialog();
			if(reglement != null) {
				try {
					reglementManager.addReglement(reglement);
				} catch (SQLException e1) {
					JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				}
			}
			completePanel();
		} else if(e.getSource() == jbEdit) {
			if(ajlReglements.getSelectedIndex() > -1)
				showReglementDialog();
		} else if(e.getSource() == jbDelete) {
			if(JOptionPane.showConfirmDialog(parentframe, ajrLibelle.getResourceString("reglementmanager.delete.confirm")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
				Reglement reglement = (Reglement)ajlReglements.getSelectedValue(); 
				if(reglement.delete()) {
					ajlReglements.remove(reglement);
					reglementManager.removeReglement(reglement);
				}
			}
		} else if(e.getSource() == jbExport) {
			Reglement reglement = (Reglement)ajlReglements.getSelectedValue();
			
			if(reglement != null) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new AJFileFilter(new String[] {"reglement"}, "Fichier réglement")); //$NON-NLS-1$
				fileChooser.setSelectedFile(new File(reglement.getName() + ".reglement")); //$NON-NLS-1$
				if(fileChooser.showSaveDialog(parentframe) == JFileChooser.APPROVE_OPTION) {
					try {
						reglementManager.exportReglement(reglement, 
								fileChooser.getSelectedFile());
					} catch (FileNotFoundException e1) {
						JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					} catch (JAXBException e1) {
						JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					}
				}
			}
		} else if(e.getSource() == jbImport) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new AJFileFilter(new String[] {"reglement"}, "Fichier réglement")); //$NON-NLS-1$
			if(fileChooser.showOpenDialog(parentframe) == JFileChooser.APPROVE_OPTION) {
				try {
					reglementManager.importReglement(fileChooser.getSelectedFile());
					completePanel();
				} catch (JAXBException e1) {
					JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				} catch (SQLException e1) {
					JXErrorPane.showDialog(this, new ErrorInfo(ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				}
			}
		}
	}
}
