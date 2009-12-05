/*
 * Créé le 17 aoû. 08 à 12:54:22 pour ConcoursJeunes
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
package org.concoursjeunes.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.LocalisationHandler;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.StringUtils;
import org.ajdeveloppement.commons.sql.SqlPersistanceException;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.CompetitionLevel;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.Federation;
import org.concoursjeunes.Profile;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.painter.GlossPainter;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Localisable(textMethod="setTitle",value="federation.title")
public class FederationDialog extends JDialog implements ActionListener {
	
	private Profile profile;
	private AjResourcesReader localisation;
	
	@Localisable("federation.header")
	private JXHeader jxhFederation = new JXHeader();
	
	@Localisable("federation.sigle")
	private JLabel jlFederationSigle = new JLabel();
	@Localisable("federation.name")
	private JLabel jlFederationName = new JLabel();
	@Localisable("federation.level")
	private JLabel jlFederationNiveau = new JLabel();
	private JLabel jlFederationLocaleNiveau = new JLabel();
	@Localisable("federation.levelinfo")
	private JLabel jlFederationNiveauInfo = new JLabel();
	@Localisable("federation.addlocale")
	private JLabel jlAddLocale = new JLabel();
	@Localisable("federation.localeinfo")
	private JLabel jlAddLocaleInfo = new JLabel();
	private JTextField jtfFederatonSigle = new JTextField(10);
	private JTextField jtfFederatonName = new JTextField(40);
	private JTextField jtfFederationNiveau = new JTextField(40);
	private JComboBox jcbAvailableLocale = new JComboBox();
	@Localisable("bouton.ajouter")
	private JButton jbAddLocale = new JButton();
	
	private GridbagComposer gbcTraduction = new GridbagComposer();
	private GridBagConstraints cTraduction = new GridBagConstraints();
	private JPanel jpTraduction = new JPanel();
	private Map<String, JTextField> mTraduction = new HashMap<String, JTextField>();
	
	@Localisable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	//private JFrame parentframe;
	
	private Federation federation = null;
	
	public FederationDialog(Window parentframe, Profile profile) {
		super(parentframe, ModalityType.TOOLKIT_MODAL);
		
		this.profile = profile;
		this.localisation = profile.getLocalisation();
		
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpPrincipal = new JPanel();
		JPanel jpAction = new JPanel();
		
		GridbagComposer gbComposer = new GridbagComposer();
		GridBagConstraints c = new GridBagConstraints();
		
		GlossPainter gloss = new GlossPainter();
		jxhFederation.setBackground(new Color(200,200,255));
		jxhFederation.setBackgroundPainter(gloss);
		jxhFederation.setTitleFont(jxhFederation.getTitleFont().deriveFont(16.0f));
		
		jlFederationLocaleNiveau.setOpaque(true);
		jlFederationLocaleNiveau.setBackground(Color.WHITE);
		
		gbcTraduction.setParentPanel(jpTraduction);
		cTraduction.gridy = -1;
		cTraduction.anchor = GridBagConstraints.WEST;
		cTraduction.fill = GridBagConstraints.HORIZONTAL;
		
		String[] libelleLangues = Configuration.listLangue();
		
		jcbAvailableLocale.setRenderer(new DefaultListCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				
				if(value != null)
					value = ((Locale)value).getDisplayLanguage((Locale)value);
				else
					value = ""; //$NON-NLS-1$
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
			
		});
		for (int i = 0; i < libelleLangues.length; i++) {
			if(!new Locale(libelleLangues[i]).equals(new Locale(profile.getConfiguration().getLangue())))
				jcbAvailableLocale.addItem(new Locale(libelleLangues[i]));
		}
		if (libelleLangues.length < 2) {
			jcbAvailableLocale.setEnabled(false);
		}
		
		jlAddLocaleInfo.setVisible(false);
		jbAddLocale.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		gbComposer.setParentPanel(jpPrincipal);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gbComposer.addComponentIntoGrid(jlFederationSigle, c);
		c.gridwidth = 2;
		gbComposer.addComponentIntoGrid(jtfFederatonSigle, c);
		c.gridy++;
		c.gridwidth = 1;
		gbComposer.addComponentIntoGrid(jlFederationName, c);
		c.gridwidth = 2;
		gbComposer.addComponentIntoGrid(jtfFederatonName, c);
		c.gridy++;
		c.gridwidth = 1;
		gbComposer.addComponentIntoGrid(jlFederationNiveau, c);
		c.gridwidth = 2;
		gbComposer.addComponentIntoGrid(jtfFederationNiveau, c);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gbComposer.addComponentIntoGrid(jlFederationLocaleNiveau, c);
		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.NONE;
		gbComposer.addComponentIntoGrid(jlFederationNiveauInfo, c);
		c.gridy++;
		gbComposer.addComponentIntoGrid(jpTraduction, c);
		c.gridy++;
		gbComposer.addComponentIntoGrid(jlAddLocaleInfo, c);
		c.gridy++;
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridwidth = 1;
		gbComposer.addComponentIntoGrid(jlAddLocale, c);
		gbComposer.addComponentIntoGrid(jcbAvailableLocale, c);
		gbComposer.addComponentIntoGrid(jbAddLocale, c);
		

		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jxhFederation, BorderLayout.NORTH);
		getContentPane().add(jpPrincipal, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		Localisator.localize(this, localisation, Collections.<Class<?>, LocalisationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));
	}
	
	private void completePanel() {
		jlFederationLocaleNiveau.setText(profile.getConfiguration().getLangue());
		
		if(federation == null)
			return;
		
		jtfFederatonSigle.setText(federation.getSigleFederation());
		jtfFederatonName.setText(federation.getNomFederation());
		

		for(CompetitionLevel cl : federation.getCompetitionLevels()) {
			if(cl.getLang().equals(profile.getConfiguration().getLangue())) {
				String tmp = ""; //$NON-NLS-1$
				if(!jtfFederationNiveau.getText().isEmpty())
					tmp = jtfFederationNiveau.getText() + ","; //$NON-NLS-1$
				tmp += cl.getLibelle();
				jtfFederationNiveau.setText(tmp);
			} else {
				if(!mTraduction.containsKey(cl.getLang())) {
					addLocaleLevelField(cl.getLang());
					jcbAvailableLocale.removeItem(new Locale(cl.getLang()));
					if(jcbAvailableLocale.getItemCount() == 0) {
						jcbAvailableLocale.setEnabled(false);
						jbAddLocale.setEnabled(false);
					}
					jlAddLocaleInfo.setVisible(true);
				}
				String tmp = ""; //$NON-NLS-1$
				JTextField tmpTF = mTraduction.get(cl.getLang());
				if(!tmpTF.getText().isEmpty())
					tmp = tmpTF.getText() + ","; //$NON-NLS-1$
				tmp += cl.getLibelle();
				tmpTF.setText(tmp);
			}
		}
	}
	
	private void addLocaleLevelField(String locale) {
		cTraduction.gridy++;
		JTextField jtfFederationNiveauLocalise = new JTextField(40);
		gbcTraduction.addComponentIntoGrid(jtfFederationNiveauLocalise, cTraduction);
		JLabel label = new JLabel(locale);
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		gbcTraduction.addComponentIntoGrid(label, cTraduction);
		
		mTraduction.put(locale, jtfFederationNiveauLocalise);
	}
	
	private void redimDialog() {
		pack();
		setSize(getSize().width, getSize().height + 15);
	}
	
	/**
	 * Affiche la boite de dialogue de création de dédération
	 * 
	 * @return la federation créer
	 */
	public Federation showFederationDialog(Federation federation) {
		this.federation = federation;
		completePanel();
		
		redimDialog();
		setLocationRelativeTo(null);
		setVisible(true);
		
		return this.federation;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			federation = new Federation(jtfFederatonName.getText(), jtfFederatonSigle.getText());
			boolean first = true;
			for(String level : StringUtils.tokenize(jtfFederationNiveau.getText(), ",")) { //$NON-NLS-1$
				CompetitionLevel cl = new CompetitionLevel();
				cl.setDefaut(first);
				first = false;
				cl.setLang(profile.getConfiguration().getLangue());
				cl.setLibelle(level);
				federation.addCompetitionLevel(cl);
			}
			for(Entry<String, JTextField> le : mTraduction.entrySet()) {
				first = true;
				for(String level : StringUtils.tokenize(le.getValue().getText(), ",")) { //$NON-NLS-1$
					CompetitionLevel cl = new CompetitionLevel();
					cl.setDefaut(first);
					first = false;
					cl.setLang(le.getKey());
					cl.setLibelle(level);
					federation.addCompetitionLevel(cl);
				}
			}
			try {
				federation.save();
			} catch (SqlPersistanceException e1) {
				federation = null;
				JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			}
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		} else if(e.getSource() == jbAddLocale) {
			Locale loc = (Locale)jcbAvailableLocale.getSelectedItem();
			addLocaleLevelField(loc.toString());
			mTraduction.get(loc.toString()).setText(jtfFederationNiveau.getText());
			jcbAvailableLocale.removeItem(loc);
			if(jcbAvailableLocale.getItemCount() == 0) {
				jcbAvailableLocale.setEnabled(false);
				jbAddLocale.setEnabled(false);
			}
			jlAddLocaleInfo.setVisible(true);
			redimDialog();
		}
	}
	
	
}
