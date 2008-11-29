/*
 * Created on 20 janv. 2005
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.concoursjeunes.*;
import org.concoursjeunes.event.AutoCompleteDocumentEvent;
import org.concoursjeunes.event.AutoCompleteDocumentListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.common.StringUtils;
import ajinteractive.standard.ui.GridbagComposer;
import ajinteractive.standard.ui.NumberDocument;
import ajinteractive.standard.utilities.io.AJFileFilter;
import ajinteractive.standard.utilities.net.Proxy;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * Ecran de configuration de ConcoursJeunes
 * 
 * @author Aurelien Jeoffray
 * @version 2.2
 */
public class ConfigurationDialog extends JDialog implements ActionListener, AutoCompleteDocumentListener {
	// private boolean runInitDialog = true;

	private Configuration workConfiguration;
	private JTabbedPane tabbedpane = new JTabbedPane();
	private TitledBorder tbProfil = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlNomProfil = new JLabel();
	private JComboBox jcbProfil = new JComboBox();
	private JButton jbRenameProfile = new JButton();

	// Ecran general personnalisation
	private TitledBorder tbParamGeneral = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlNomClub = new JLabel();
	private JLabel jlAgremClub = new JLabel();
	private JLabel jlIntituleConcours = new JLabel();
	private JLabel jlLangue = new JLabel();
	private JLabel jlPathPdf = new JLabel();
	private JLabel jlLogoPath = new JLabel();
	private JTextField jtfNomClub = new JTextField(20);
	private JTextField jtfAgrClub = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JButton jbParcourir = new JButton();
	private JButton jbDetail = new JButton();
	private JTextField jtfIntConc = new JTextField(20);
	private JComboBox jcbLangue = new JComboBox();
	private JComboBox jcbPathPdf = new JComboBox();
	private JButton jbParcourirPdf = new JButton("..."); //$NON-NLS-1$
	private JButton jbLogoPath = new JButton();

	// Ecran concours/pas de tir
	private JLabel jlReglement = new JLabel();
	private JLabel jlNbCible = new JLabel();
	private JLabel jlNbTireur = new JLabel();
	private JLabel jlNbDepart = new JLabel();
	private JLabel jlSelectedReglement = new JLabel();
	private JButton jbSelectReglement = new JButton();
	private JTextField jtfNbCible = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JComboBox jcbNbTireur = new JComboBox();
	private JTextField jtfNbDepart = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$

	// Ecran etiquette
	private JLabel jlFormatPapier = new JLabel();
	private JLabel jlNbEtiquettes = new JLabel();
	private JLabel jlColonnes = new JLabel("x"); //$NON-NLS-1$
	private JLabel jlMarges = new JLabel();
	private JLabel jlMargesH = new JLabel();
	private JLabel jlMargesB = new JLabel();
	private JLabel jlMargesG = new JLabel();
	private JLabel jlMargesD = new JLabel();
	private JLabel jlEspacements = new JLabel();
	private JLabel jlEspacementsH = new JLabel();
	private JLabel jlEspacementsV = new JLabel();
	private JComboBox jcbFormatPapier = new JComboBox();
	private JComboBox jcbOrientation = new JComboBox();
	private JTextField jtfLignes = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JTextField jtfColonnes = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesH = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesB = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesG = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfMargesD = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfEspacementsH = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$
	private JTextField jtfEspacementsV = new JTextField(new NumberDocument(true, false), "", 5); //$NON-NLS-1$

	// Ecran avancé
	private JLabel jlURLImport = new JLabel();
	private JLabel jlURLExport = new JLabel();
	private JLabel jlResultats = new JLabel();
	private JLabel jlAffResultats = new JLabel();

	// private TitledBorder tbPath = new TitledBorder(""); //$NON-NLS-1$
	private JCheckBox jcbAvanceResultatCumul = new JCheckBox();
	private JCheckBox jcbAvanceResultatSupl = new JCheckBox();
	private JCheckBox jcbAvanceAffResultatExEquo = new JCheckBox();
	private TitledBorder tbProxy = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlAdresseProxy = new JLabel();
	private JLabel jlPortProxy = new JLabel();
	private JLabel jlUserProxy = new JLabel();
	private JLabel jlPasswordProxy = new JLabel();
	//private JCheckBox jcbUseProxy = new JCheckBox();
	private JRadioButton jrbUseSystemConfig = new JRadioButton();
	private JRadioButton jrbUseSpecificConfig = new JRadioButton();
	private final JTextField jtfAdresseProxy = new JTextField(20);
	private final JTextField jtfPortProxy = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$
	private final JCheckBox jcbAuthentificationProxy = new JCheckBox();
	private final JTextField jtfUserProxy = new JTextField(20);
	private final JPasswordField jpfPasswordProxy = new JPasswordField(20);

	// Ecran avancé option debug
	private final JCheckBox jcbFirstBoot = new JCheckBox();
	private final JButton jbValider = new JButton();
	private final JButton jbAnnuler = new JButton();
	private String[] strLstLangue;
	private boolean renameProfile = false;
	private boolean renamedProfile = false;
	
	private JFrame parentframe;

	public ConfigurationDialog(JFrame parentframe) {
		super(parentframe, true);
		this.parentframe = parentframe;
		
		JPanel jpBouton = new JPanel();

		jpBouton.setLayout(new FlowLayout(FlowLayout.RIGHT));

		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);

		jpBouton.add(jbValider);
		jpBouton.add(jbAnnuler);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpBouton, BorderLayout.SOUTH);

		tabbedpane.addTab("configuration.onglet.genral", null, initEcranGeneral()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.concours", null, initEcranConcours()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.etiquettes", null, initEcranEtiquette()); //$NON-NLS-1$
		tabbedpane.addTab("configuration.onglet.interface", null, initEcranAdvanced()); //$NON-NLS-1$

		affectLibelle();

		getContentPane().add(tabbedpane, BorderLayout.CENTER);
		getRootPane().setDefaultButton(this.jbValider);

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Genere l'onglet general
	 * 
	 * @return JPanel - le panneau de l'onglet général
	 */
	private JPanel initEcranGeneral() {
		// Layout Manager
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel jpEcranGeneral = new JPanel();
		JPanel jpProfil = new JPanel();
		JPanel jpParamGeneral = new JPanel();
		JPanel jpClub = new JPanel();

		jbRenameProfile.addActionListener(this);
		jcbProfil.addActionListener(this);

		jbParcourir.addActionListener(this);
		jbParcourir.setMargin(new Insets(0,2,0,2));
		jbDetail.addActionListener(this);
		jbDetail.setMargin(new Insets(0,2,0,2));

		AutoCompleteDocumentContext context = new AutoCompleteDocumentContext(null);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrClub, AutoCompleteDocument.SearchType.AGREMENT_SEARCH, context);
		acdAgrement.addAutoCompleteDocumentListener(this);
		jtfAgrClub.setDocument(acdAgrement);

		jcbLangue.addActionListener(this);

		jcbPathPdf.setEditable(true);
		jcbPathPdf.setPrototypeDisplayValue("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ "); //$NON-NLS-1$

		jbParcourirPdf.setMargin(new Insets(0, 3, 0, 3));
		jbParcourirPdf.addActionListener(this);

		jbLogoPath.setPreferredSize(new java.awt.Dimension(100, 100));
		jbLogoPath.setMargin(new Insets(0, 0, 0, 0));
		jbLogoPath.addActionListener(this);

		jpProfil.setBorder(tbProfil);
		jpParamGeneral.setBorder(tbParamGeneral);

		jpProfil.add(jlNomProfil);
		jpProfil.add(jcbProfil);
		jpProfil.add(jbRenameProfile);
		
		jpClub.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpClub.add(jtfAgrClub);
		jpClub.add(jbParcourir);
		jpClub.add(jbDetail);

		gridbagComposer.setParentPanel(jpParamGeneral);
		c.weightx = 1.0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST; // Défaut,Haut
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jlAgremClub, c);
		gridbagComposer.addComponentIntoGrid(jpClub, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlNomClub, c);
		gridbagComposer.addComponentIntoGrid(jtfNomClub, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
		gridbagComposer.addComponentIntoGrid(jtfIntConc, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlLangue, c);
		gridbagComposer.addComponentIntoGrid(jcbLangue, c);
		if (!Desktop.isDesktopSupported()) {
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(jlPathPdf, c);
			gridbagComposer.addComponentIntoGrid(jcbPathPdf, c);
			gridbagComposer.addComponentIntoGrid(jbParcourirPdf, c);
		}
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlLogoPath, c);
		gridbagComposer.addComponentIntoGrid(jbLogoPath, c);

		gridbagComposer.setParentPanel(jpEcranGeneral);
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jpProfil, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jpParamGeneral, c);

		return jpEcranGeneral;
	}

	/**
	 * Genere l'onglet concours
	 * 
	 * @return JPanel - le panneau de l'ecran concours
	 */
	private JPanel initEcranConcours() {
		// Layout Manager
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel jpEcranConcours = new JPanel();
		JPanel jpPasDeTir = new JPanel();
		JPanel jpDifCriteria = new JPanel();
		
		jbSelectReglement.addActionListener(this);
		jlSelectedReglement.setFont(jlSelectedReglement.getFont().deriveFont(Font.ITALIC));
		
		jcbNbTireur.addItem("AB"); //$NON-NLS-1$
		jcbNbTireur.addItem("AB/CD"); //$NON-NLS-1$

		jpEcranConcours.setLayout(new BorderLayout());
		jpDifCriteria.setLayout(new BorderLayout());

		gridbagComposer.setParentPanel(jpPasDeTir);
		c.weightx = 1.0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST; // Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlReglement, c);
		gridbagComposer.addComponentIntoGrid(jlSelectedReglement, c);
		gridbagComposer.addComponentIntoGrid(jbSelectReglement, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbCible, c);
		gridbagComposer.addComponentIntoGrid(jtfNbCible, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbDepart, c);
		gridbagComposer.addComponentIntoGrid(jtfNbDepart, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(new JPanel(), c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbTireur, c);
		gridbagComposer.addComponentIntoGrid(jcbNbTireur, c);
		c.gridy++;
		c.weighty = 1.0;
		gridbagComposer.addComponentIntoGrid(Box.createGlue(), c);

		jpEcranConcours.add(jpPasDeTir, BorderLayout.CENTER);

		return jpEcranConcours;
	}

	/**
	 * Genere l'onglet des etiquettes
	 * 
	 * @return JPanel - ecran etiquette
	 */
	private JPanel initEcranEtiquette() {
		JPanel jpEcranEtiquette = new JPanel();
		jpEcranEtiquette.setBorder(new TitledBorder(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.bordertitle"))); //$NON-NLS-1$

		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel margeHB = new JPanel();
		JPanel margeGD = new JPanel();
		JPanel espacement = new JPanel();

		margeHB.setLayout(new GridLayout(1, 4));
		margeHB.add(jlMargesH);
		margeHB.add(jtfMargesH);
		margeHB.add(jlMargesB);
		margeHB.add(jtfMargesB);

		margeGD.setLayout(new GridLayout(1, 4));
		margeGD.add(jlMargesD);
		margeGD.add(jtfMargesD);
		margeGD.add(jlMargesG);
		margeGD.add(jtfMargesG);

		espacement.setLayout(new GridLayout(1, 4));
		espacement.add(jlEspacementsH);
		espacement.add(jtfEspacementsH);
		espacement.add(jlEspacementsV);
		espacement.add(jtfEspacementsV);

		gridbagComposer.setParentPanel(jpEcranEtiquette);
		c.weightx = 1.0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlFormatPapier, c);
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(this.jcbFormatPapier, c);
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(this.jcbOrientation, c);
		c.gridy++;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jlNbEtiquettes, c);
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jtfLignes, c);
		gridbagComposer.addComponentIntoGrid(jlColonnes, c);
		gridbagComposer.addComponentIntoGrid(jtfColonnes, c);
		c.gridy++;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jlMarges, c);
		c.gridy++;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.CENTER;
		gridbagComposer.addComponentIntoGrid(margeHB, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(margeGD, c);
		c.gridy++;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlEspacements, c);
		c.gridy++;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.CENTER;
		gridbagComposer.addComponentIntoGrid(espacement, c);
		c.gridy++;
		c.weighty = 1.0;
		gridbagComposer.addComponentIntoGrid(Box.createGlue(), c);

		return jpEcranEtiquette;
	}

	private JPanel initEcranAdvanced() {
		JPanel jpEcranInterface = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		JPanel panelProxy = new JPanel();
		panelProxy.setBorder(tbProxy);

		ButtonGroup bg = new ButtonGroup();
		bg.add(jrbUseSystemConfig);
		bg.add(jrbUseSpecificConfig);
		jrbUseSystemConfig.setSelected(true);
		jrbUseSystemConfig.addActionListener(this);
		jrbUseSpecificConfig.addActionListener(this);
		jcbAuthentificationProxy.addActionListener(this);
		jlAdresseProxy.setEnabled(false);
		jlPortProxy.setEnabled(false);
		jlUserProxy.setEnabled(false);
		jlPasswordProxy.setEnabled(false);

		gridbagComposer.setParentPanel(panelProxy);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jrbUseSystemConfig, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jrbUseSpecificConfig, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlAdresseProxy, c);
		gridbagComposer.addComponentIntoGrid(jtfAdresseProxy, c);
		gridbagComposer.addComponentIntoGrid(jlPortProxy, c);
		gridbagComposer.addComponentIntoGrid(jtfPortProxy, c);
		c.gridy++;
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jcbAuthentificationProxy, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlUserProxy, c);
		gridbagComposer.addComponentIntoGrid(jtfUserProxy, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlPasswordProxy, c);
		gridbagComposer.addComponentIntoGrid(jpfPasswordProxy, c);

		gridbagComposer.setParentPanel(jpEcranInterface);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jlResultats, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceResultatCumul, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceResultatSupl, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlAffResultats, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceAffResultatExEquo, c);
		if (System.getProperty("debug.mode") != null) { //$NON-NLS-1$
			c.gridy++;
			gridbagComposer.addComponentIntoGrid(jcbFirstBoot, c);
		}
		c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(panelProxy, c);

		JPanel jpEcranInterface2 = new JPanel();
		jpEcranInterface2.setLayout(new BorderLayout());
		jpEcranInterface2.add(jpEcranInterface, BorderLayout.NORTH);

		return jpEcranInterface2;
	}

	private void affectLibelle() {
		setTitle(ApplicationCore.ajrLibelle.getResourceString("configuration.title")); //$NON-NLS-1$
		jbValider.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$

		if (tabbedpane.getTabCount() > 0) {
			tabbedpane.setTitleAt(0, ApplicationCore.ajrLibelle.getResourceString("configuration.onglet.genral")); //$NON-NLS-1$
			tabbedpane.setTitleAt(1, ApplicationCore.ajrLibelle.getResourceString("configuration.onglet.concours")); //$NON-NLS-1$
			tabbedpane.setTitleAt(2, ApplicationCore.ajrLibelle.getResourceString("configuration.onglet.etiquettes")); //$NON-NLS-1$
			tabbedpane.setTitleAt(3, ApplicationCore.ajrLibelle.getResourceString("configuration.onglet.avance")); //$NON-NLS-1$
		}

		affectLibelleGeneral();
		affectLibelleConcours();
		affectLibelleEtiquette();
		affectLibelleAdvanced();
	}

	private void affectLibelleGeneral() {
		jlNomProfil.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.profil")); //$NON-NLS-1$
		jlNomClub.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.nom")); //$NON-NLS-1$
		jlAgremClub.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.agrement")); //$NON-NLS-1$
		jlIntituleConcours.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.intituleconcours")); //$NON-NLS-1$
		jlLangue.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.langue")); //$NON-NLS-1$
		jlPathPdf.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.pdf")); //$NON-NLS-1$
		jlLogoPath.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.logo")); //$NON-NLS-1$

		tbProfil.setTitle(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.titre0")); //$NON-NLS-1$
		tbParamGeneral.setTitle(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.titre1")); //$NON-NLS-1$

		jbRenameProfile.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.renameprofile")); //$NON-NLS-1$
		jbParcourir.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.choiceclub")); //$NON-NLS-1$
		jbParcourir.setToolTipText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.browseclub")); //$NON-NLS-1$
		jbDetail.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.detail")); //$NON-NLS-1$
		/*if (jbLogoPath.getText().equals("")) //$NON-NLS-1$
			jbLogoPath.setText(ApplicationCore.ajrLibelle.getResourceString("parametre.logo")); //$NON-NLS-1$*/
	}

	private void affectLibelleConcours() {
		jlReglement.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.concours.reglement")); //$NON-NLS-1$
		jbSelectReglement.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.concours.change_reglement")); //$NON-NLS-1$
		jlNbCible.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.concours.cible")); //$NON-NLS-1$
		jlNbTireur.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.concours.tireur")); //$NON-NLS-1$
		jlNbDepart.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.concours.depart")); //$NON-NLS-1$
	}

	private void affectLibelleEtiquette() {
		jlFormatPapier.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.formatpapier")); //$NON-NLS-1$
		jlNbEtiquettes.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.nbetiquettes")); //$NON-NLS-1$
		jlMarges.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.marges")); //$NON-NLS-1$
		jlMargesH.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.haut")); //$NON-NLS-1$
		jlMargesB.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.bas")); //$NON-NLS-1$
		jlMargesG.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.gauche")); //$NON-NLS-1$
		jlMargesD.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.droite")); //$NON-NLS-1$
		jlEspacements.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.espacement")); //$NON-NLS-1$
		jlEspacementsH.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.horizontal")); //$NON-NLS-1$
		jlEspacementsV.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.vertical")); //$NON-NLS-1$
	}

	private void affectLibelleAdvanced() {
		jlResultats.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.resultat")); //$NON-NLS-1$
		jlAffResultats.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.affresultat")); //$NON-NLS-1$
		jlURLImport.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.urlimport")); //$NON-NLS-1$
		jlURLExport.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.urlexport")); //$NON-NLS-1$
		jcbFirstBoot.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.firstboot")); //$NON-NLS-1$

		jcbAvanceResultatCumul.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.resultatcumul")); //$NON-NLS-1$
		jcbAvanceResultatSupl.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.resultatdnm")); //$NON-NLS-1$
		jcbAvanceAffResultatExEquo.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.interface.resultataffexequo")); //$NON-NLS-1$

		jrbUseSystemConfig.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.utilisationproxysystem")); //$NON-NLS-1$
		jrbUseSpecificConfig.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.utilisationproxycustom")); //$NON-NLS-1$
		tbProxy.setTitle(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.configurationproxy")); //$NON-NLS-1$
		jlAdresseProxy.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.adresseproxy")); //$NON-NLS-1$
		jlPortProxy.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.portproxy")); //$NON-NLS-1$
		jcbAuthentificationProxy.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.authentificationproxy")); //$NON-NLS-1$
		jlUserProxy.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.userproxy")); //$NON-NLS-1$
		jlPasswordProxy.setText(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.avance.passwordproxy")); //$NON-NLS-1$
	}

	private void completePanel(Configuration configuration) {
		completeGeneralPanel(configuration);
		completeConcoursPanel(configuration);
		completeEtiquettePanel(configuration);
		completeAdvancedPanel(configuration);
	}

	private void completeGeneralPanel(Configuration configuration) {
		String[] libelleLangues = getAvailableLanguages();

		jtfNomClub.setText(configuration.getClub().getNom());
		((AutoCompleteDocument) jtfAgrClub.getDocument()).setText(configuration.getClub().getAgrement());
		jtfIntConc.setText(configuration.getIntituleConcours());

		jcbProfil.removeActionListener(this);
		jcbProfil.removeAllItems();
		for (String profile : ApplicationCore.userRessources.listAvailableConfigurations())
			jcbProfil.addItem(profile);
		jcbProfil.addItem("---"); //$NON-NLS-1$
		jcbProfil.addItem(ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.addprofile")); //$NON-NLS-1$

		jcbProfil.setSelectedItem(configuration.getCurProfil());
		jcbProfil.addActionListener(this);
		
		if(configuration.getCurProfil().equals("defaut") /*|| !configuration.getCurProfil().equals(ConcoursJeunes.configuration.getCurProfil())*/) //$NON-NLS-1$
			jbRenameProfile.setEnabled(false);
		else
			jbRenameProfile.setEnabled(true);

		jcbLangue.removeAllItems();
		for (int i = 0; i < libelleLangues.length; i++) {
			jcbLangue.addItem(libelleLangues[i]);
		}
		jcbLangue.setSelectedItem(new Locale(configuration.getLangue()).getDisplayLanguage(new Locale(configuration.getLangue())));
		if (libelleLangues.length < 2) {
			jcbLangue.setEnabled(false);
		}

		jcbPathPdf.removeAllItems();
		for (String pdfpath : getPdfPath(configuration)) {
			jcbPathPdf.addItem(pdfpath);
		}

		ImageIcon logo = new ImageIcon(configuration.getLogoPath());
		logo = new ImageIcon(logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		jbLogoPath.setIcon(logo);
		//jbLogoPath.setText("<html><img src=\"file:" + configuration.getLogoPath() + "\" width=90 height=100></html>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void completeConcoursPanel(Configuration configuration) {
		jlSelectedReglement.setText(configuration.getReglementName());
		jtfNbCible.setText("" + configuration.getNbCible()); //$NON-NLS-1$
		if(configuration.getNbTireur() == 2)
			jcbNbTireur.setSelectedIndex(0);
		else
			jcbNbTireur.setSelectedIndex(1);
		//jtfNbTireur.setText("" + configuration.getNbTireur()); //$NON-NLS-1$
		jtfNbDepart.setText("" + configuration.getNbDepart()); //$NON-NLS-1$
	}

	private void completeEtiquettePanel(Configuration configuration) {
		jcbFormatPapier.removeAllItems();
		for (Field champs : PageSize.class.getFields()) {
			jcbFormatPapier.addItem(champs.getName());
		}
		jcbFormatPapier.setSelectedItem(configuration.getFormatPapier());
		for (String orientation : new String[] { "portrait", "landscape" }) { //$NON-NLS-1$ //$NON-NLS-2$
			jcbOrientation.addItem(orientation);
		}
		jcbOrientation.setSelectedItem(configuration.getOrientation());

		jtfLignes.setText("" + configuration.getColonneAndLigne()[0]); //$NON-NLS-1$
		jtfColonnes.setText("" + configuration.getColonneAndLigne()[1]); //$NON-NLS-1$
		jtfMargesH.setText("" + configuration.getMarges().getTop()); //$NON-NLS-1$
		jtfMargesB.setText("" + configuration.getMarges().getBottom()); //$NON-NLS-1$
		jtfMargesG.setText("" + configuration.getMarges().getLeft()); //$NON-NLS-1$
		jtfMargesD.setText("" + configuration.getMarges().getRight()); //$NON-NLS-1$
		jtfEspacementsH.setText("" + configuration.getEspacements()[0]); //$NON-NLS-1$
		jtfEspacementsV.setText("" + configuration.getEspacements()[1]); //$NON-NLS-1$
	}

	private void completeAdvancedPanel(Configuration configuration) {
		jcbAvanceResultatCumul.setSelected(configuration.isInterfaceResultatCumul());
		jcbAvanceResultatSupl.setSelected(configuration.isInterfaceResultatSupl());
		jcbAvanceAffResultatExEquo.setSelected(configuration.isInterfaceAffResultatExEquo());

		jrbUseSpecificConfig.setSelected(configuration.isUseProxy());
		jlAdresseProxy.setEnabled(configuration.isUseProxy());
		jtfAdresseProxy.setEnabled(configuration.isUseProxy());
		jlPortProxy.setEnabled(configuration.isUseProxy());
		jtfPortProxy.setEnabled(configuration.isUseProxy());
		if (configuration.getProxy() != null) {
			jtfAdresseProxy.setText(configuration.getProxy().getProxyServerAddress());
			jtfPortProxy.setText(configuration.getProxy().getProxyServerPort() + ""); //$NON-NLS-1$
			jcbAuthentificationProxy.setSelected(configuration.getProxy().isUseProxyAuthentification());
			jlUserProxy.setEnabled(configuration.getProxy().isUseProxyAuthentification());
			jtfUserProxy.setText(configuration.getProxy().getProxyAuthLogin());
			jtfUserProxy.setEnabled(configuration.getProxy().isUseProxyAuthentification());
			jlPasswordProxy.setEnabled(configuration.getProxy().isUseProxyAuthentification());
			jpfPasswordProxy.setText(configuration.getProxy().getProxyAuthPassword());
			jpfPasswordProxy.setEnabled(configuration.getProxy().isUseProxyAuthentification());
		}
	}

	/**
	 * 
	 * @param configuration
	 * @return les chemins d'accès à l'executable PDF
	 */
	private ArrayList<String> getPdfPath(Configuration configuration) {
		ArrayList<String> pdfPath = new ArrayList<String>();

		if (!configuration.getPdfReaderPath().equals("")) { //$NON-NLS-1$
			pdfPath.add(configuration.getPdfReaderPath());
		}
		// Recherche d'un lecteur pdf en fonction du syteme
		if (System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$

			String base_pdfPath = ApplicationCore.ajrParametreAppli.getResourceString("path.windows.acrobat"); //$NON-NLS-1$
			// tente l'ouverture de acrobat reader
			File f = new File(base_pdfPath);

			String[] fList = f.list();
			if (fList != null) {
				String reader = ""; //$NON-NLS-1$
				for (int i = 0; i < fList.length; i++) {
					if (fList[i].startsWith("Acrobat")) { //$NON-NLS-1$
						reader = base_pdfPath + File.separator + fList[i] + "\\Reader\\AcroRd32.exe"; //$NON-NLS-1$
						pdfPath.add(reader);
					}
				}
			}

		} else if (System.getProperty("os.name").contains("Linux")) { //$NON-NLS-1$ //$NON-NLS-2$

			String[] pdfReader = StringUtils.tokenize(ApplicationCore.ajrParametreAppli.getResourceString("path.unix.pdf"), ","); //$NON-NLS-1$ //$NON-NLS-2$
			for (String reader : pdfReader)
				pdfPath.add(reader);
		}

		return pdfPath;
	}

	private String[] getAvailableLanguages() {
		// liste les langues disponible
		String[] langues = listLangue();
		Locale[] locales = new Locale[langues.length];
		String[] libelleLangues = new String[langues.length];
		for (int i = 0; i < langues.length; i++) {
			locales[i] = new Locale(langues[i]);
			libelleLangues[i] = locales[i].getDisplayLanguage(locales[i]);
		}

		return libelleLangues;
	}

	/**
	 * Recherche du logo club
	 * 
	 */
	private void changeLogoPath() {
		File f;
		JFileChooser fileDialog = new JFileChooser(workConfiguration.getLogoPath());
		AJFileFilter filtreimg = new AJFileFilter(new String[] { "jpg", "gif" }, ApplicationCore.ajrLibelle.getResourceString("filter.gifjpeg")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		fileDialog.addChoosableFileFilter(filtreimg);
		fileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
		fileDialog.showOpenDialog(this);
		f = fileDialog.getSelectedFile();
		if (f == null) {
			return;
		}
		ImageIcon logo = new ImageIcon(f.getPath());
		logo = new ImageIcon(logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		jbLogoPath.setIcon(logo);
		//jbLogoPath.setText("<html><img src=\"file:" + f.getPath() + "\" width=90 height=100></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		workConfiguration.setLogoPath(f.getPath());
	}

	public Configuration showConfigurationDialog(Configuration configuration) {
		configuration.save();
		
		this.workConfiguration = configuration;

		completePanel(workConfiguration);
		pack();
		setVisible(true);

		return workConfiguration;
	}

	/**
	 * @return Renvoie workConfiguration.
	 */
	public Configuration getWorkConfiguration() {
		return workConfiguration;
	}

	/**
	 * @param workConfiguration
	 *            workConfiguration à définir.
	 */
	public void setWorkConfiguration(Configuration workConfiguration) {
		this.workConfiguration = workConfiguration;
	}

	/**
	 * Renvoie la liste des langues disponibles
	 * 
	 * @return String[] - retourne la liste des langues disponible
	 */
	private String[] listLangue() {
		if (strLstLangue == null) {
			String[] strLng = new File("lang").list(new FilenameFilter() { //$NON-NLS-1$
						public boolean accept(File dir, String name) {
							if (name.startsWith("libelle_") && name.endsWith(".properties")) //$NON-NLS-1$ //$NON-NLS-2$
								return true;
							return false;
						}
					});

			for (int i = 0; i < strLng.length; i++)
				strLng[i] = strLng[i].substring(8, strLng[i].length() - 11);
			strLstLangue = strLng;
		}

		return strLstLangue;
	}

	private void loadProfile() throws IOException {
		renamedProfile = false;
		workConfiguration = ConfigurationManager.loadConfiguration((String) jcbProfil.getSelectedItem());
		completePanel(workConfiguration);

		workConfiguration.setCurProfil((String) jcbProfil.getSelectedItem());
	}
	
	public boolean isRenamedProfile() {
		return renamedProfile;
	}

	private boolean registerConfig() {
		double margeDroite = Double.parseDouble(this.jtfMargesD.getText());
		double margeGauche = Double.parseDouble(this.jtfMargesG.getText());
		double margeHaut = Double.parseDouble(this.jtfMargesH.getText());
		double margeBas = Double.parseDouble(this.jtfMargesB.getText());
		double espacementHorizontal = Double.parseDouble(this.jtfEspacementsH.getText());
		double espacementVertical = Double.parseDouble(this.jtfEspacementsV.getText());
		int nbColonne = Integer.parseInt(this.jtfColonnes.getText());
		int nbLigne = Integer.parseInt(this.jtfLignes.getText());
		
		try {
			Field formatPapier = PageSize.class.getField((String) this.jcbFormatPapier.getSelectedItem()); 
			
			Rectangle pageDimension = (Rectangle)formatPapier.get(null);
			
			if(((margeGauche + (espacementHorizontal*nbColonne-1) + margeDroite) / 2.54 * 72 > pageDimension.getWidth())
					|| ((margeHaut + (espacementVertical*nbLigne-1) + margeBas) / 2.54 * 72 > pageDimension.getHeight())) {
				JOptionPane.showMessageDialog(this, 
						ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.errordimension"), //$NON-NLS-1$
						ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.etiquettes.errordimension.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (SecurityException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		
		if(jtfNomClub.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, 
					ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.errornameclub"), //$NON-NLS-1$
					ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.errornameclub.title"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		workConfiguration.getClub().setNom(jtfNomClub.getText());
		workConfiguration.getClub().setAgrement(jtfAgrClub.getText());
		workConfiguration.setIntituleConcours(jtfIntConc.getText());
		workConfiguration.setLangue(listLangue()[jcbLangue.getSelectedIndex()]);
		workConfiguration.setPdfReaderPath(jcbPathPdf.getSelectedItem().toString());

		workConfiguration.setReglementName(jlSelectedReglement.getText());
		workConfiguration.setNbCible(Integer.parseInt(jtfNbCible.getText()));
		workConfiguration.setNbTireur((jcbNbTireur.getSelectedIndex() == 0) ? 2 : 4);
		workConfiguration.setNbDepart(Integer.parseInt(jtfNbDepart.getText()));
		workConfiguration.setFirstboot(jcbFirstBoot.isSelected());

		workConfiguration.setFormatPapier((String) this.jcbFormatPapier.getSelectedItem());
		workConfiguration.setOrientation((String) this.jcbOrientation.getSelectedItem());
		workConfiguration.setColonneAndLigne(new int[] { nbLigne, nbColonne });
		workConfiguration.setMarges(new Margin(margeHaut, margeBas, margeGauche, margeDroite));
		workConfiguration.setEspacements(new double[] { espacementHorizontal, espacementVertical });
		workConfiguration.setInterfaceResultatCumul(jcbAvanceResultatCumul.isSelected());
		workConfiguration.setInterfaceResultatSupl(jcbAvanceResultatSupl.isSelected());
		workConfiguration.setInterfaceAffResultatExEquo(jcbAvanceAffResultatExEquo.isSelected());

		workConfiguration.setUseProxy(jrbUseSpecificConfig.isSelected());
		Proxy proxy = new Proxy(jtfAdresseProxy.getText(), Integer.parseInt("0" + jtfPortProxy.getText()), jtfUserProxy.getText(), new String(jpfPasswordProxy.getPassword())); //$NON-NLS-1$
		proxy.setUseProxyAuthentification(jcbAuthentificationProxy.isSelected());
		workConfiguration.setProxy(proxy);
		
		return true;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.jbValider) {
			if(ApplicationCore.getInstance().getFichesConcours().size() > 0 
					&& !workConfiguration.getCurProfil().equals(ApplicationCore.getConfiguration().getCurProfil())
					&& JOptionPane.showConfirmDialog(this, 
					ApplicationCore.ajrLibelle.getResourceString("configuration.fermeture.confirmation"), "", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
				return;

			if(registerConfig())
				setVisible(false);
		} else if (source == jbAnnuler) {
			workConfiguration = null;

			setVisible(false);
		} else if (source == jbLogoPath) {
			changeLogoPath();
		} else if (source == this.jbParcourirPdf) {
			try {
				JFileChooser fileDialog = new JFileChooser();
				fileDialog.setCurrentDirectory(new File((String) this.jcbPathPdf.getSelectedItem()));

				if (fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					this.jcbPathPdf.addItem(fileDialog.getSelectedFile().getCanonicalPath());
					this.jcbPathPdf.setSelectedItem(fileDialog.getSelectedFile().getCanonicalPath());
				}
			} catch (IOException io) {
				System.err.println("IOException: " + io.getMessage()); //$NON-NLS-1$
			} catch (NullPointerException npe) {
				System.err.println("Aucune sauvegarde possible. Action annulé"); //$NON-NLS-1$
			}
		} else if (source == jbParcourir) {
			EntiteListDialog eld = new EntiteListDialog(null);
			if (eld.getAction() == EntiteListDialog.VALIDER)
					jtfAgrClub.setText(eld.getSelectedEntite().getAgrement());
		} else if (source == this.jbDetail) {
			EntiteDialog ed = new EntiteDialog(this);
			ed.showEntite(workConfiguration.getClub());

			jtfNomClub.setText(workConfiguration.getClub().getNom());
		} else if (source == jcbProfil) {
			if (!renameProfile) {
				if (jcbProfil.getSelectedIndex() > -1 && jcbProfil.getSelectedIndex() < jcbProfil.getItemCount() - 2) {
					try {
						loadProfile();
					} catch (IOException e1) {
						JXErrorPane.showDialog(null, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
								e1.toString(), null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					}
				} else if (jcbProfil.getSelectedIndex() == jcbProfil.getItemCount() - 1) {
					String strP = JOptionPane.showInputDialog(this, ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.newprofile")); //$NON-NLS-1$
					if (strP != null && !strP.equals("")) { //$NON-NLS-1$

						workConfiguration.setCurProfil(strP);
						workConfiguration.getMetaDataFichesConcours().removeAll();

						completePanel(workConfiguration);
						jcbProfil.insertItemAt(strP, 0);
						jcbProfil.setSelectedItem(strP);
					}
				}
			}
		} else if (source == jbRenameProfile) {
			String strP = JOptionPane.showInputDialog(this, ApplicationCore.ajrLibelle.getResourceString("configuration.ecran.general.newprofile"), //$NON-NLS-1$ 
					workConfiguration.getCurProfil());
			if (strP != null && !strP.isEmpty()) {
				renameProfile = true;

				int insIndex = jcbProfil.getSelectedIndex();
				
				try {
					renamedProfile = ConfigurationManager.renameConfiguration(workConfiguration.getCurProfil(), strP);
				} catch (NullConfigurationException e1) {
					renamedProfile = false;
					e1.printStackTrace();
				} catch (IOException e1) {
					renamedProfile = false;
					e1.printStackTrace();
				}
				if(renamedProfile) {
					jcbProfil.removeItem(workConfiguration.getCurProfil());
	
					workConfiguration.setCurProfil(strP);
					//workConfiguration.save();

					jcbProfil.insertItemAt(strP, insIndex);
					jcbProfil.setSelectedIndex(insIndex);
					
					renamedProfile = true;
				} else {
					JOptionPane.showMessageDialog(this, 
							ApplicationCore.ajrLibelle.getResourceString("erreur.renameprofile"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("erreur.renameprofile.title"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
				}
				/*} else {
					JOptionPane.showMessageDialog(this, "Il existe déjà un profil portant ce nom", "Renomage impossible", JOptionPane.ERROR_MESSAGE);
				}*/

				renameProfile = false;
			}
		} else if (source == jrbUseSpecificConfig || source == jrbUseSystemConfig) {
			jlAdresseProxy.setEnabled(jrbUseSpecificConfig.isSelected());
			jlPortProxy.setEnabled(jrbUseSpecificConfig.isSelected());
			jtfAdresseProxy.setEnabled(jrbUseSpecificConfig.isSelected());
			jtfPortProxy.setEnabled(jrbUseSpecificConfig.isSelected());
		} else if (source == jcbAuthentificationProxy) {
			jlUserProxy.setEnabled(jcbAuthentificationProxy.isSelected());
			jlPasswordProxy.setEnabled(jcbAuthentificationProxy.isSelected());
			jtfUserProxy.setEnabled(jcbAuthentificationProxy.isSelected());
			jpfPasswordProxy.setEnabled(jcbAuthentificationProxy.isSelected());
		} else if (source == jbSelectReglement) {
			ReglementManagerDialog reglementManagerDialog = new ReglementManagerDialog(parentframe);
			Reglement reglement = reglementManagerDialog.showReglementManagerDialog(true);
			if(reglement != null)
				jlSelectedReglement.setText(reglement.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#concurrentFinded(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void concurrentFinded(AutoCompleteDocumentEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#concurrentNotFound(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void concurrentNotFound(AutoCompleteDocumentEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#entiteFinded(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void entiteFinded(AutoCompleteDocumentEvent e) {
		Entite findEntite = e.getEntite();
		if (!findEntite.equals(workConfiguration.getClub())) {
			workConfiguration.setClub(findEntite);
			completeGeneralPanel(workConfiguration);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#entiteNotFound(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void entiteNotFound(AutoCompleteDocumentEvent e) {
		Entite newEntite = new Entite();
		if (e.getSource() == jtfAgrClub) {
			newEntite.setVille(jtfNomClub.getText());
			newEntite.setAgrement(jtfAgrClub.getText());

			jtfNomClub.setEditable(true);
		}

		workConfiguration.setClub(newEntite);
		completeGeneralPanel(workConfiguration);
	}
}