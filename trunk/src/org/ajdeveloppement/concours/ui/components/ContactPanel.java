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
package org.ajdeveloppement.concours.ui.components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLEditorKit;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizationHandler;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.ui.AJSuggestTextField;
import org.ajdeveloppement.commons.ui.GenericListModel;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.concours.CategoryContact;
import org.ajdeveloppement.concours.Civility;
import org.ajdeveloppement.concours.Contact;
import org.ajdeveloppement.concours.Coordinate;
import org.ajdeveloppement.concours.Coordinate.Type;
import org.ajdeveloppement.concours.managers.CategoryContactManager;
import org.ajdeveloppement.concours.managers.CivilityManager;
import org.ajdeveloppement.concours.ui.components.CountryComboBox.Country;
import org.ajdeveloppement.concours.ui.components.ZipCodeCitySuggestModel.SuggestType;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Entite;
import org.concoursjeunes.Profile;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTitledSeparator;

import com.lowagie.text.Font;

/**
 * Panneau d'information sur un contact
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class ContactPanel extends JPanel implements ActionListener, MouseListener, ListSelectionListener {
	private Profile profile;
	private Contact contact;
	private Entite parentEntite = null;
	private boolean create = true;
	
	private boolean saveOnlyInMemoryBean = false;
	
	private BindingGroup contactBinding = null;
	
	private List<CategoryContact> categoriesContact = new ArrayList<CategoryContact>();
	
	private GenericListModel<Coordinate> coordinatesModel = new GenericListModel<Coordinate>();
	
	private CardLayout cardLayout = new CardLayout();
	private Coordinate editedCoordinate = null;
	
	private JPopupMenu popup = null;
	@Localizable("entite.sendmail")
	private JMenuItem miMail = new JMenuItem();
	
	@Localizable("entite.civility")
	private JLabel jlCivility = new JLabel();
	private JComboBox jcbCivility = new JComboBox();
	@Localizable("entite.newcivility")
	private JXHyperlink jxhNewCivility = new JXHyperlink();
	@Localizable("entite.namefirstname")
	private JLabel jlNameFirstName = new JLabel();
	private JTextField jtfName = new JTextField("", 15); //$NON-NLS-1$
	private JTextField jtfFirstName = new JTextField("", 15); //$NON-NLS-1$
	private JPanel jpCategories = new JPanel();
	private JEditorPane jlCategories = new JEditorPane();
	@Localizable("entite.addcategories")
	private JXHyperlink jxhCategories = new JXHyperlink();
	@Localizable("entite.adresse")
	private JLabel jlAdressContact = new JLabel();
	private JTextArea jtaAddressContact = new JTextArea(4, 30);
	@Localizable("entite.codepostal")
	private JLabel jlZipCodeContact = new JLabel();
	private AJSuggestTextField jtfZipCodeContact = new AJSuggestTextField("", 10); //$NON-NLS-1$
	@Localizable("entite.ville")
	private JLabel jlCityContact = new JLabel();
	private AJSuggestTextField jtfCityContact = new AJSuggestTextField("", 10); //$NON-NLS-1$
	@Localizable("entite.pays")
	private JLabel jlPays = new JLabel();
	private CountryComboBox ccbPays = new CountryComboBox();
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
	private JLabel jlSateSaveContact = new JLabel();
	
	private JPopupMenu dropDownMenu = new JPopupMenu();
	@Localizable(value="entite.removecategory",textMethod="setTitle")
	private JXTitledSeparator dropDownRemoveSeparator = new JXTitledSeparator();
	@Localizable(value="entite.addcategory",textMethod="setTitle")
	private JXTitledSeparator dropDownAddSeparator = new JXTitledSeparator();
	
	//Panneau d'édition de coordonnée
	@Localizable("entite.coordinateheader")
	private JXHeader jxhCoordinate = new JXHeader();
	@Localizable("entite.typecoordinate")
	private JLabel jlTypeCoordinate = new JLabel();
	private JComboBox jcbTypeCoordinate = new JComboBox();
	@Localizable("entite.valuecoordinate")
	private JLabel jlValueCoordinate = new JLabel();
	private JTextField jtfValueCoordinate = new JTextField();
	@Localizable("entite.savecoordinate")
	private JXHyperlink jxhSaveCoordinate = new JXHyperlink();
	@Localizable("entite.cancelcoordinate")
	private JXHyperlink jxhCancelCoordinate = new JXHyperlink();
	
	private EventListenerList listeners = new EventListenerList();
	
	public ContactPanel(Profile profile) {
		this.profile = profile;
		
		init();
		affectLabels();
		completePanel();
	}
	
	private void init() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel jpContact = new JPanel();
		
		JPanel jpNameFirstName = new JPanel();
		
		JPanel jpCoordinatesAction = new JPanel();
		JPanel jpContactAction = new JPanel();
		
		
		jpCategories.setMinimumSize(new Dimension(100, 30));
		jpCategories.setSize(new Dimension(100, 30));
		
		jcbCivility.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				if(value == null)
					value = "<html>&nbsp;</html>"; //$NON-NLS-1$
				else if(value instanceof Civility)
					value = ((Civility)value).getLibelle();
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		jcbCivility.setEnabled(false);
		jxhNewCivility.setFont(jxhNewCivility.getFont().deriveFont(Font.NORMAL|Font.ITALIC));
		jxhNewCivility.setEnabled(false);
		
		jtfFirstName.setEnabled(false);
		jtfName.setEnabled(false);
		
		jlCategories.setEditorKit(new HTMLEditorKit());
		jlCategories.setOpaque(false);
		//jlCategories.setMinimumSize(new Dimension(100, 55));
		jxhCategories.setFont(jxhCategories.getFont().deriveFont(Font.NORMAL|Font.ITALIC));
		jxhCategories.addActionListener(this);
		jxhCategories.setEnabled(false);
		dropDownRemoveSeparator.setForeground(new Color(150,150,150));
		dropDownAddSeparator.setForeground(new Color(150,150,150));
		
		jtaAddressContact.setEnabled(false);
		
		JScrollPane jspAdressContact = new JScrollPane(jtaAddressContact);
		jspAdressContact.setMinimumSize(new Dimension(300, 70));
		try {
			jtfZipCodeContact.setModel(new ZipCodeCitySuggestModel(SuggestType.ZIP_CODE, jtfCityContact));
			jtfCityContact.setModel(new ZipCodeCitySuggestModel(SuggestType.CITY, jtfZipCodeContact));
		} catch (SQLException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
		jtfZipCodeContact.setEnabled(false);
		jtfCityContact.setEnabled(false);
		ccbPays.setEnabled(false);

		jlstCoordinates.setVisibleRowCount(5);
		jlstCoordinates.setModel(coordinatesModel);
		jlstCoordinates.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				
				if(value instanceof Coordinate) {
					Coordinate coordinate = (Coordinate)value;
					if(coordinate.getValue() != null) {
						value = "<html><span style=\"color: #888888;\">" +  //$NON-NLS-1$
							profile.getLocalisation().getResourceString("entite.coordinate.type."  //$NON-NLS-1$
								+ coordinate.getCoordinateType().getValue().toLowerCase()) + " - </span>" +  coordinate.getValue() + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
					} else
						value = "<html></html>"; //$NON-NLS-1$
				} else {
					value = "<html></html>"; //$NON-NLS-1$
				}
				
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		jlstCoordinates.addMouseListener(this);
		JScrollPane jspCoordinates = new JScrollPane(jlstCoordinates);
		jspCoordinates.setMinimumSize(new Dimension(150, 70));
		
		jpCoordinatesAction.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		jbAddCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add_active", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.add_disable", 16, 16)); //$NON-NLS-1$
		jbAddCoordinate.setBorderPainted(false);
		jbAddCoordinate.setFocusPainted(false);
		jbAddCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbAddCoordinate.setContentAreaFilled(false);
		jbAddCoordinate.addActionListener(this);
		jbAddCoordinate.setEnabled(false);
		jbDelCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del_active", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.del_disable", 16, 16)); //$NON-NLS-1$
		jbDelCoordinate.setBorderPainted(false);
		jbDelCoordinate.setFocusPainted(false);
		jbDelCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbDelCoordinate.setContentAreaFilled(false);
		jbDelCoordinate.addActionListener(this);
		jbDelCoordinate.setEnabled(false);
		jbEditCoordinate.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setPressedIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit_active", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setDisabledIcon(ApplicationCore.userRessources.getImageIcon("file.icon.edit_disable", 16, 16)); //$NON-NLS-1$
		jbEditCoordinate.setBorderPainted(false);
		jbEditCoordinate.setFocusPainted(false);
		jbEditCoordinate.setMargin(new Insets(0, 0, 0, 0));
		jbEditCoordinate.setContentAreaFilled(false);
		jbEditCoordinate.addActionListener(this);
		jbEditCoordinate.setEnabled(false);
		
		JScrollPane jspNoteContact = new JScrollPane(jtaNoteContact);
		jspNoteContact.setMinimumSize(new Dimension(150, 70));
		jtaNoteContact.setEnabled(false);
		
		jxhNewContact.addActionListener(this);
		jxhSaveContact.addActionListener(this);
		
		gridbagComposer.setParentPanel(jpNameFirstName);
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jtfName, c);
		gridbagComposer.addComponentIntoGrid(jtfFirstName, c);
		
//		jpCategories.setLayout(new FlowLayout(FlowLayout.LEFT));
//		jpCategories.add(jlCategories);
//		jpCategories.add(jxhCategories);
		
		jpCoordinatesAction.add(jbAddCoordinate);
		jpCoordinatesAction.add(jbDelCoordinate);
		jpCoordinatesAction.add(jbEditCoordinate);
		
		jpContactAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpContactAction.add(jxhNewContact);
		jpContactAction.add(new JLabel("-")); //$NON-NLS-1$
		jpContactAction.add(jxhSaveContact);
		jpContactAction.add(jlSateSaveContact);
		
		gridbagComposer.setParentPanel(jpContact);
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.anchor= GridBagConstraints.NORTHWEST;
		gridbagComposer.addComponentIntoGrid(jlCivility, c);
		gridbagComposer.addComponentIntoGrid(jcbCivility, c);
		c.weightx = 0.9;
		c.gridwidth = 2;
		c.anchor= GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jxhNewCivility, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.anchor= GridBagConstraints.NORTHWEST;
		gridbagComposer.addComponentIntoGrid(jlNameFirstName, c);
		c.weightx = 0.9;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jpNameFirstName, c);
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jxhCategories, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlCategories, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.gridx = GridBagConstraints.RELATIVE;
		gridbagComposer.addComponentIntoGrid(jlAdressContact, c);
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jspAdressContact, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlZipCodeContact, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jtfZipCodeContact, c);
		gridbagComposer.addComponentIntoGrid(jlCityContact, c);
		c.weightx = 0.9;
		gridbagComposer.addComponentIntoGrid(jtfCityContact, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlPays, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.weightx = 0.9;
		gridbagComposer.addComponentIntoGrid(ccbPays, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlCoordinates, c);
		c.weightx = 0.9;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jspCoordinates, c);
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
		c.weightx = 0.9;		
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(jspNoteContact, c);
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jpContactAction, c);
		
		setLayout(cardLayout);
		add("contact",jpContact); //$NON-NLS-1$
		add("coordinate", initCoordinatePanel()); //$NON-NLS-1$
		
		popup();
	}
	
	private JPanel initCoordinatePanel() {
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		jcbTypeCoordinate.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				
				if(value instanceof Coordinate.Type)
					value = profile.getLocalisation().getResourceString("entite.coordinate.type." + ((Coordinate.Type)value).getValue().toLowerCase()); //$NON-NLS-1$
				
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});
		
		for(Coordinate.Type type : Coordinate.Type.values()) {
			jcbTypeCoordinate.addItem(type);
		}
		
		jlstCoordinates.addListSelectionListener(this);
		jbEditCoordinate.setEnabled(false);
		jbDelCoordinate.setEnabled(false);
		
		jxhSaveCoordinate.addActionListener(this);
		jxhCancelCoordinate.addActionListener(this);
		
		JPanel jpAction = new JPanel();
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jxhSaveCoordinate);
		jpAction.add(new JLabel(" - ")); //$NON-NLS-1$
		jpAction.add(jxhCancelCoordinate);
		
		JPanel jpCoordinate = new JPanel();
		
		gridbagComposer.setParentPanel(jpCoordinate);
		
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jxhCoordinate, c);
		c.gridy++;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jlTypeCoordinate, c);
		gridbagComposer.addComponentIntoGrid(jcbTypeCoordinate, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlValueCoordinate, c);
		gridbagComposer.addComponentIntoGrid(jtfValueCoordinate, c);
		c.gridy++;
		c.gridwidth = 2;
		c.weightx = 0.0;
		gridbagComposer.addComponentIntoGrid(jpAction, c);
		c.gridy++;
		c.weighty = 1.0;
		gridbagComposer.addComponentIntoGrid(Box.createGlue(), c);
		
		return jpCoordinate;
	}
	
	private void popup() {
		popup = new JPopupMenu("Action"); //$NON-NLS-1$
		
		miMail.addActionListener(this);
		popup.add(miMail);
	}
	
	private void affectLabels() {
		Localizator.localize(this, profile.getLocalisation(), Collections.<Class<?>, LocalizationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));
	}
	
	private void completePanel() {
		if(contactBinding != null)
			contactBinding.unbind();
		
		contactBinding = new BindingGroup();
		
		jcbCivility.removeAllItems();
		try {
			jcbCivility.addItem(null);
			for(Civility civility : CivilityManager.getAllCivilities()) {
				jcbCivility.addItem(civility);
			}
		} catch (ObjectPersistenceException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
		
		
		
		if(contact != null) {
			jcbCivility.setEnabled(true);
			jtfFirstName.setEnabled(true);
			jtfName.setEnabled(true);
			jxhCategories.setEnabled(true);
			jtaAddressContact.setEnabled(true);
			jtfZipCodeContact.setEnabled(true);
			jtfCityContact.setEnabled(true);
			ccbPays.setEnabled(true);
			jbAddCoordinate.setEnabled(true);
			jbDelCoordinate.setEnabled(true);
			jbEditCoordinate.setEnabled(true);
			jtaNoteContact.setEnabled(true);
			
			jtfCityContact.beginInit();
			jtfZipCodeContact.beginInit();
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("civility"), jcbCivility, BeanProperty.create("selectedItem")));  //$NON-NLS-1$//$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("name"), jtfName, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("firstName"), jtfFirstName, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("adress"), jtaAddressContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("zipCode"), jtfZipCodeContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("city"), jtfCityContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			contactBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, contact, BeanProperty.create("note"), jtaNoteContact, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
			
			contactBinding.bind();
			jtfCityContact.endInit();
			jtfZipCodeContact.endInit();
			
			categoriesContact = contact.getCategories();
			
			if(contact.getCoordinates() != null && contact.getCoordinates().size() > 0)
				coordinatesModel.setElements(new ArrayList<Coordinate>(contact.getCoordinates()));
			else
				coordinatesModel.setElements(new ArrayList<Coordinate>(Collections.singletonList(new Coordinate())));
			
			jlSateSaveContact.setEnabled(true);
			jxhSaveContact.setEnabled(true);
			
			if(contact.getCountryCode() == null)
				ccbPays.setSelectedCountry(contact.getEntite().getPays());
			else
				ccbPays.setSelectedCountry(contact.getCountryCode());
		} else {
			jlSateSaveContact.setEnabled(false);
			jxhSaveContact.setEnabled(false);
		}
		
		populateCategoriesPanel();
		
		jxhNewContact.setEnabled(true);
		jlSateSaveContact.setIcon(null);
	}
	
	private void populateCategoriesPanel() {
		List<CategoryContact> allCategories = null;
		
		try {
			allCategories = CategoryContactManager.getAllCategoryContact();
		} catch (ObjectPersistenceException e) {
			DisplayableErrorHelper.displayException(e);
		}
		
		if(allCategories == null)
			allCategories = new ArrayList<CategoryContact>();
		
		dropDownMenu.removeAll();
		
		//jlCategories.setSize(getWidth(), jlCategories.getSize().height);
		
		if(contact != null) {
			String categories = ""; //$NON-NLS-1$
			dropDownMenu.add(dropDownRemoveSeparator);
			
			Collections.sort(categoriesContact, new Comparator<CategoryContact>() {

				@Override
				public int compare(CategoryContact o1, CategoryContact o2) {
					return o1.getLibelle(profile.getConfiguration().getLangue()).compareToIgnoreCase(o2.getLibelle(profile.getConfiguration().getLangue()));
				}
			});		
			
			for(final CategoryContact category : categoriesContact) {
				if(!categories.isEmpty())
					categories += ", "; //$NON-NLS-1$
				categories += category.getLibelle(profile.getConfiguration().getLangue());
				
				JMenuItem menuItem = new JMenuItem(category.getLibelle(profile.getConfiguration().getLangue()));
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						categoriesContact.remove(category);
						
						populateCategoriesPanel();
					}
				});
				
				dropDownMenu.add(menuItem);
				
				allCategories.remove(category);
			}
			jlCategories.setText("<html><span style=\"font-family: " + getFont().getName() + "; font-size:" + getFont().getSize() //$NON-NLS-1$ //$NON-NLS-2$
				+ "pt; font-weight:bold;\">" + categories + "</span><html>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		dropDownMenu.add(dropDownAddSeparator);
		for (final CategoryContact categoryContact : allCategories) {
			JMenuItem menuItem = new JMenuItem(categoryContact.getLibelle(profile.getConfiguration().getLangue()));
			menuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					categoriesContact.add(categoryContact);
					
					populateCategoriesPanel();
				}
			});
			
			dropDownMenu.add(menuItem);
		}
	}
	
	public void addContactPanelListener(ContactPanelListener listener) {
		listeners.add(ContactPanelListener.class, listener);
	}
	
	public void removeContactPanelListener(ContactPanelListener listener) {
		listeners.remove(ContactPanelListener.class, listener);
	}
	
	/**
	 * Définit le contact à afficher / modifier
	 * 
	 * @param contact le contact à afficher / modifier
	 */
	public void setContact(Contact contact) {
		setContact(contact, false);
	}
	
	/**
	 * Définit le contact à afficher / modifier
	 * 
	 * @param contact le contact à afficher / modifier
	 */
	public void setContact(Contact contact, boolean create) {
		this.contact = contact;
		this.create = create;
		
		completePanel();
	}
	
	/**
	 * @return parentEntite
	 */
	public Entite getParentEntite() {
		return parentEntite;
	}

	/**
	 * @param parentEntite parentEntite à définir
	 */
	public void setParentEntite(Entite parentEntite) {
		this.parentEntite = parentEntite;
	}

	public void setEnabledCreateContact(boolean enabledCreateContact) {
		jxhNewContact.setEnabled(enabledCreateContact);
	}
	

	/**
	 * @return enabledCreateContact
	 */
	public boolean isEnabledCreateContact() {
		return jxhNewContact.isEnabled();
	}

	/**
	 * @param saveOnlyInMemoryBean saveOnlyInMemoryBean à définir
	 */
	public void setSaveOnlyInMemoryBean(boolean saveOnlyInMemoryBean) {
		this.saveOnlyInMemoryBean = saveOnlyInMemoryBean;
	}

	/**
	 * @return saveOnlyInMemoryBean
	 */
	public boolean isSaveOnlyInMemoryBean() {
		return saveOnlyInMemoryBean;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jxhNewContact) {
			Contact contact = new Contact();
			if(parentEntite != null) {
				contact.setEntite(parentEntite);
				contact.setCountryCode(parentEntite.getPays());
			}
			setContact(contact);
			
			create = true;
			jxhNewContact.setEnabled(false);
		} else if(e.getSource() == jxhSaveContact) {
			if(contact != null) {
				if(contactBinding != null) {
	            	for(Binding<Contact, ?, ?, ?> binding : contactBinding.getBindings()) {
	            		binding.save();
	            	}
	        	}
				contact.setCategories(categoriesContact);
				contact.setCoordinates(new ArrayList<Coordinate>(coordinatesModel.getElements()));
				contact.setCountryCode(((Country)ccbPays.getSelectedItem()).getCode());
				
				try {
					if(!saveOnlyInMemoryBean)
						contact.save();
					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							jlSateSaveContact.setEnabled(true);
							jlSateSaveContact.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.success", 16, 16)); //$NON-NLS-1$
						}
					});
					
					if(!create)
						fireContactEdited(contact);
					else
						fireContactAdded(contact);
				} catch (ObjectPersistenceException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
					
					jlSateSaveContact.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.failed", 16, 16)); //$NON-NLS-1$
				}
			}
		} else if(e.getSource() == jxhCategories) {
			dropDownMenu.show(jxhCategories, 0, jxhCategories.getHeight());
		} else if(e.getSource() == jbAddCoordinate || e.getSource() == jbEditCoordinate) {
			if(e.getSource() == jbAddCoordinate)
				editedCoordinate = new Coordinate();
			else
				editedCoordinate = (Coordinate)jlstCoordinates.getSelectedValue();
			
			if(editedCoordinate != null) {
				jcbTypeCoordinate.setSelectedItem(editedCoordinate.getCoordinateType());
				jtfValueCoordinate.setText(editedCoordinate.getValue());
				
				cardLayout.show(this, "coordinate"); //$NON-NLS-1$
			}
		} else if(e.getSource() == jbDelCoordinate) {
			Coordinate deletedCoordinate = (Coordinate)jlstCoordinates.getSelectedValue();
			if(deletedCoordinate != null && deletedCoordinate.getValue() != null)
				coordinatesModel.remove(deletedCoordinate);
		} else if(e.getSource() == jxhSaveCoordinate || e.getSource() == jxhCancelCoordinate) {
			if(e.getSource() == jxhSaveCoordinate) {
				editedCoordinate.setCoordinateType((Coordinate.Type)jcbTypeCoordinate.getSelectedItem());
				editedCoordinate.setValue(jtfValueCoordinate.getText());
				if(!coordinatesModel.getElements().contains(editedCoordinate)) {
					if(coordinatesModel.getSize() == 1 && ((Coordinate)coordinatesModel.getElementAt(0)).getValue() == null)
						coordinatesModel.remove((Coordinate)coordinatesModel.getElementAt(0));
					coordinatesModel.add(editedCoordinate);
				}
			}
			cardLayout.show(this, "contact"); //$NON-NLS-1$
		} else if(e.getSource() == miMail) {
			if(Desktop.isDesktopSupported()) {
				if(Desktop.getDesktop().isSupported(Action.MAIL)) {
					URI uri;
					try {
						uri = new URI(
								String.format("mailto:%s?body=%s", //$NON-NLS-1$
										((Coordinate)jlstCoordinates.getSelectedValue()).getValue(),
										("Bonjour " + contact.getFullNameWithCivility() + ",").replace(" ", "%20"))  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
								);
						Desktop.getDesktop().mail(uri);
					} catch (UnsupportedEncodingException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (IOException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
					
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
			int elemIndex = jlstCoordinates.locationToIndex(e.getPoint());

			if(elemIndex > -1)
				jlstCoordinates.setSelectedIndex(elemIndex);
				
			if(jlstCoordinates.getSelectedValue() != null && ((Coordinate)jlstCoordinates.getSelectedValue()).getCoordinateType() == Type.MAIL)
				popup.show(jlstCoordinates, e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == jlstCoordinates) {
			if(jlstCoordinates.getSelectedValue() != null && ((Coordinate)jlstCoordinates.getSelectedValue()).getValue() != null) {
				jbEditCoordinate.setEnabled(true);
				jbDelCoordinate.setEnabled(true);
			} else {
				jbEditCoordinate.setEnabled(false);
				jbDelCoordinate.setEnabled(false);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	private void fireContactEdited(Contact contact) {
		for(ContactPanelListener l : listeners.getListeners(ContactPanelListener.class)) {
			l.contactEdited(contact);
		}
	}
	
	private void fireContactAdded(Contact contact) {
		for(ContactPanelListener l : listeners.getListeners(ContactPanelListener.class)) {
			l.contactAdded(contact);
		}
	}
	
	public static interface ContactPanelListener extends EventListener {
		public void contactEdited(Contact contact);
		
		public void contactAdded(Contact contact);
	}
}
