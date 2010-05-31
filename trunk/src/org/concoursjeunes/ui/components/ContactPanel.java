/*
 * Créé le 8 mai 2010 à 18:27:57 pour ConcoursJeunes
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
package org.concoursjeunes.ui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizationHandler;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.concours.CategoryContact;
import org.ajdeveloppement.concours.Civility;
import org.ajdeveloppement.concours.Contact;
import org.ajdeveloppement.concours.managers.CivilityManager;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Profile;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;

import com.lowagie.text.Font;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ContactPanel extends JPanel implements ActionListener{
	private Profile profile;
	private Contact contact;
	
	private BindingGroup contactBinding = null;
	
	private List<CategoryContact> categoriesContact = new ArrayList<CategoryContact>();
	
	@Localizable("entite.civility")
	private JLabel jlCivility = new JLabel();
	private JComboBox jcbCivility = new JComboBox();
	@Localizable("entite.newcivility")
	private JXHyperlink jxhNewCivility = new JXHyperlink();
	@Localizable("entite.namefirstname")
	private JLabel jlNameFirstName = new JLabel();
	private JTextField jtfName = new JTextField("", 15); //$NON-NLS-1$
	private JTextField jtfFirstName = new JTextField("", 15); //$NON-NLS-1$
	private JLabel jlCategories = new JLabel();
	@Localizable("entite.addcategories")
	private JXHyperlink jxhAddCategories = new JXHyperlink();
	@Localizable("entite.adresse")
	private JLabel jlAdressContact = new JLabel();
	private JTextArea jtaAddressContact = new JTextArea(4, 30);
	@Localizable("entite.codepostal")
	private JLabel jlZipCodeContact = new JLabel();
	private JTextField jtfZipCodeContact = new JTextField("", 10); //$NON-NLS-1$
	@Localizable("entite.ville")
	private JLabel jlCityContact = new JLabel();
	private JTextField jtfCityContact = new JTextField("", 10); //$NON-NLS-1$
	@Localizable("entite.coordinates")
	private JLabel jlCoordinates = new JLabel();
	private JList jlstCoordinates = new JList();
	@Localizable(value="",tooltip="entite.addcoordinate")
	private JButton jbAddCoordinate = new JButton();
	@Localizable(value="",tooltip="entite.delcoordinate")
	private JButton jbDelCoordinate = new JButton();
	@Localizable(value="",tooltip="entite.editcoordinate")
	private JButton jbEditCoordinate = new JButton();
	@Localizable("entite.note")
	private JLabel jlNoteContact = new JLabel();
	private JTextArea jtaNoteContact = new JTextArea(4, 30);
	@Localizable("entite.newcontact")
	private JXHyperlink jxhNewContact = new JXHyperlink();
	@Localizable("entite.savecontact")
	private JXHyperlink jxhSaveContact = new JXHyperlink();
	
	public ContactPanel(Profile profile) {
		this.profile = profile;
		
		init();
		affectLibelle();
		completePanel();
	}
	
	private void init() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel jpNameFirstName = new JPanel();
		JPanel jpCategory = new JPanel();
		JPanel jpCoordinatesAction = new JPanel();
		JPanel jpContactAction = new JPanel();
		
		jpCategory.setSize(new Dimension(100, 25));
		
		jcbCivility.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				if(value instanceof Civility)
					value = ((Civility)value).getLibelle();
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		jxhNewCivility.setFont(jxhNewCivility.getFont().deriveFont(Font.NORMAL|Font.ITALIC));
		
		jlstCoordinates.setVisibleRowCount(5);
		
		jpCoordinatesAction.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		jbAddCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add_active", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add_disable", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setBorderPainted(false);
		jbAddCoordinate.setFocusPainted(false);
		jbAddCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbAddCoordinate.setContentAreaFilled(false);
		jbAddCoordinate.addActionListener(this);
		jbDelCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del_active", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del_disable", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setBorderPainted(false);
		jbDelCoordinate.setFocusPainted(false);
		jbDelCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbDelCoordinate.setContentAreaFilled(false);
		jbDelCoordinate.addActionListener(this);
		jbEditCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit_active", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit_disable", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setBorderPainted(false);
		jbEditCoordinate.setFocusPainted(false);
		jbEditCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbEditCoordinate.setContentAreaFilled(false);
		jbEditCoordinate.addActionListener(this);
		
		jxhSaveContact.addActionListener(this);
		
		gridbagComposer.setParentPanel(jpNameFirstName);
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jtfName, c);
		gridbagComposer.addComponentIntoGrid(jtfFirstName, c);
		
		jpCategory.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpCategory.add(jlCategories);
		jpCategory.add(jxhAddCategories);
		
		jpCoordinatesAction.add(jbAddCoordinate);
		jpCoordinatesAction.add(jbDelCoordinate);
		jpCoordinatesAction.add(jbEditCoordinate);
		
		jpContactAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpContactAction.add(jxhNewContact);
		jpContactAction.add(new JLabel("-")); //$NON-NLS-1$
		jpContactAction.add(jxhSaveContact);
		
		gridbagComposer.setParentPanel(this);
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.anchor= GridBagConstraints.NORTHWEST;
		gridbagComposer.addComponentIntoGrid(jlCivility, c);
		gridbagComposer.addComponentIntoGrid(jcbCivility, c);
		c.weightx = 1.0;
		c.gridwidth = 2;
		c.anchor= GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jxhNewCivility, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.anchor= GridBagConstraints.NORTHWEST;
		gridbagComposer.addComponentIntoGrid(jlNameFirstName, c);
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jpNameFirstName, c);
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jpCategory, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.gridx = GridBagConstraints.RELATIVE;
		gridbagComposer.addComponentIntoGrid(jlAdressContact, c);
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jtaAddressContact), c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlZipCodeContact, c);
		gridbagComposer.addComponentIntoGrid(jtfZipCodeContact, c);
		gridbagComposer.addComponentIntoGrid(jlCityContact, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jtfCityContact, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jlCoordinates, c);
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jlstCoordinates), c);
		c.gridy++;
		c.gridx = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jpCoordinatesAction, c);
		c.gridy++;
		c.gridx = GridBagConstraints.RELATIVE;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.weighty = 1.0;
		gridbagComposer.addComponentIntoGrid(jlNoteContact, c);
		c.gridwidth = 3;
		c.weightx = 1.0;		
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jtaNoteContact), c);
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jpContactAction, c);
	}
	
	private void affectLibelle() {
		Localizator.localize(this, profile.getLocalisation(), Collections.<Class<?>, LocalizationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));
	}
	
	private void completePanel() {
		if(contactBinding != null)
			contactBinding.unbind();
		
		contactBinding = new BindingGroup();
		
		jcbCivility.removeAllItems();
		try {
			jcbCivility.addItem("<html>&nbsp;</html>"); //$NON-NLS-1$
			for(Civility civility : CivilityManager.getAllCivilities()) {
				jcbCivility.addItem(civility);
			}
		} catch (ObjectPersistenceException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
		
		if(contact != null) {
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("name"), jtfName, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("firstName"), jtfFirstName, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("adress"), jtaAddressContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("zipCode"), jtfZipCodeContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("city"), jtfCityContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("note"), jtaNoteContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			
			contactBinding.bind();
			
			categoriesContact = contact.getCategories();
			
			String categories = ""; //$NON-NLS-1$
			for(CategoryContact category : categoriesContact) {
				if(!categories.isEmpty())
					categories += ", "; //$NON-NLS-1$
				categories += category.getLibelle(profile.getConfiguration().getLangue());
			}
			jlCategories.setText(categories);
		}
	}
	
	public JXPanel getAddCategoryPanel() {
		JXPanel panel = new JXPanel();
		
		JXPanel jpCategories = new JXPanel();
		
		jpCategories.add(new JComboBox());
		
		panel.setLayout(new BorderLayout());
		panel.add(jpCategories, BorderLayout.CENTER);
		
		return panel;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
		
		completePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jxhSaveContact) {
			if(contactBinding != null) {
            	for(Binding<Contact, ?, ?, ?> binding : contactBinding.getBindings()) { 
            		binding.save();
            	}
        	}
			
			try {
				contact.save();
			} catch (ObjectPersistenceException e1) {
				DisplayableErrorHelper.displayException(e1);
				e1.printStackTrace();
			}
		}
	}
}