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
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.StringUtils;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.concoursjeunes.AppConfiguration;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.AutoCompleteDocument;
import org.concoursjeunes.AutoCompleteDocumentContext;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.Entite;
import org.concoursjeunes.Federation;
import org.concoursjeunes.Margin;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.builders.ReglementBuilder;
import org.concoursjeunes.event.AutoCompleteDocumentEvent;
import org.concoursjeunes.event.AutoCompleteDocumentListener;
import org.concoursjeunes.manager.ConfigurationManager;
import org.concoursjeunes.manager.FederationManager;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * Ecran de configuration de ConcoursJeunes
 * 
 * @author Aurélien Jeoffray
 * @version 2.2
 */
@Localisable(textMethod="setTitle",value="configuration.title")
public class ConfigurationDialog extends JDialog implements ActionListener, AutoCompleteDocumentListener {

	private JFrame parentframe;
	private AjResourcesReader localisation;
	private Profile profile;
	private Configuration workConfiguration;
	private AppConfiguration workAppConfiguration;
	
	@Localisable("configuration.onglet")
	private JTabbedPane tabbedpane = new JTabbedPane();
	@Localisable(value="configuration.ecran.general.titre0",textMethod="setTitle")
	private TitledBorder tbProfil = new TitledBorder(""); //$NON-NLS-1$
	@Localisable("configuration.ecran.general.profil")
	private JLabel jlNomProfil = new JLabel();
	private JComboBox jcbProfil = new JComboBox();
	@Localisable("configuration.ecran.general.renameprofile")
	private JButton jbRenameProfile = new JButton();

	// Ecran general personnalisation
	@Localisable(value="configuration.ecran.general.titre1",textMethod="setTitle")
	private TitledBorder tbParamGeneral = new TitledBorder(""); //$NON-NLS-1$
	@Localisable("configuration.ecran.general.federation")
	private JLabel jlFederation = new JLabel();
	@Localisable("configuration.ecran.general.nom")
	private JLabel jlNomClub = new JLabel();
	@Localisable("configuration.ecran.general.agrement")
	private JLabel jlAgremClub = new JLabel();
	@Localisable("configuration.ecran.general.intituleconcours")
	private JLabel jlIntituleConcours = new JLabel();
	@Localisable("configuration.ecran.general.langue")
	private JLabel jlLangue = new JLabel();
	@Localisable("configuration.ecran.general.pdf")
	private JLabel jlPathPdf = new JLabel();
	@Localisable("configuration.ecran.general.logo")
	private JLabel jlLogoPath = new JLabel();
	private JComboBox jcbFederation = new JComboBox();
	private JTextField jtfNomClub = new JTextField(20);
	private JTextField jtfAgrClub = new JTextField(new NumberDocument(false, false), "", 7); //$NON-NLS-1$
	@Localisable(value="configuration.ecran.general.choiceclub",tooltip="configuration.ecran.general.browseclub")
	private JButton jbParcourir = new JButton();
	@Localisable("bouton.detail")
	private JButton jbDetail = new JButton();
	private JTextField jtfIntConc = new JTextField(20);
	private JComboBox jcbLangue = new JComboBox();
	private JComboBox jcbPathPdf = new JComboBox();
	private JButton jbParcourirPdf = new JButton("..."); //$NON-NLS-1$
	private JButton jbLogoPath = new JButton();

	// Ecran concours/pas de tir
	@Localisable("configuration.ecran.concours.reglement")
	private JLabel jlReglement = new JLabel();
	@Localisable("configuration.ecran.concours.cible")
	private JLabel jlNbCible = new JLabel();
	@Localisable("configuration.ecran.concours.tireur")
	private JLabel jlNbTireur = new JLabel();
	@Localisable("configuration.ecran.concours.depart")
	private JLabel jlNbDepart = new JLabel();
	private JLabel jlSelectedReglement = new JLabel();
	@Localisable("configuration.ecran.concours.change_reglement")
	private JButton jbSelectReglement = new JButton();
	private JTextField jtfNbCible = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JComboBox jcbNbTireur = new JComboBox();
	private JTextField jtfNbDepart = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$

	// Ecran etiquette
	@Localisable("configuration.ecran.etiquettes.formatpapier")
	private JLabel jlFormatPapier = new JLabel();
	@Localisable("configuration.ecran.etiquettes.nbetiquettes")
	private JLabel jlNbEtiquettes = new JLabel();
	private JLabel jlColonnes = new JLabel("x"); //$NON-NLS-1$
	@Localisable("configuration.ecran.etiquettes.marges")
	private JLabel jlMarges = new JLabel();
	@Localisable("configuration.ecran.etiquettes.haut")
	private JLabel jlMargesH = new JLabel();
	@Localisable("configuration.ecran.etiquettes.bas")
	private JLabel jlMargesB = new JLabel();
	@Localisable("configuration.ecran.etiquettes.gauche")
	private JLabel jlMargesG = new JLabel();
	@Localisable("configuration.ecran.etiquettes.droite")
	private JLabel jlMargesD = new JLabel();
	@Localisable("configuration.ecran.etiquettes.espacement")
	private JLabel jlEspacements = new JLabel();
	@Localisable("configuration.ecran.etiquettes.horizontal")
	private JLabel jlEspacementsH = new JLabel();
	@Localisable("configuration.ecran.etiquettes.vertical")
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
	@Localisable("configuration.ecran.interface.resultat")
	private JLabel jlResultats = new JLabel();
	@Localisable("configuration.ecran.interface.affresultat")
	private JLabel jlAffResultats = new JLabel();

	// private TitledBorder tbPath = new TitledBorder(""); //$NON-NLS-1$
	@Localisable("configuration.ecran.interface.resultatcumul")
	private JCheckBox jcbAvanceResultatCumul = new JCheckBox();
	@Localisable("configuration.ecran.interface.resultataffexequo")
	private JCheckBox jcbAvanceAffResultatExEquo = new JCheckBox();
	@Localisable(value="configuration.ecran.avance.configurationproxy",textMethod="setTitle")
	private TitledBorder tbProxy = new TitledBorder(""); //$NON-NLS-1$
	@Localisable("configuration.ecran.avance.adresseproxy")
	private JLabel jlAdresseProxy = new JLabel();
	@Localisable("configuration.ecran.avance.portproxy")
	private JLabel jlPortProxy = new JLabel();
	
	@Localisable("configuration.ecran.avance.utilisationproxysystem")
	private JRadioButton jrbUseSystemConfig = new JRadioButton();
	@Localisable("configuration.ecran.avance.utilisationproxycustom")
	private JRadioButton jrbUseSpecificConfig = new JRadioButton();
	private final JTextField jtfAdresseProxy = new JTextField(20);
	private final JTextField jtfPortProxy = new JTextField(new NumberDocument(false, false), "", 5); //$NON-NLS-1$	

	// Ecran avancé option debug
	@Localisable("configuration.ecran.interface.firstboot")
	private final JCheckBox jcbFirstBoot = new JCheckBox();
	@Localisable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();
	//private String[] strLstLangue;
	private boolean renameProfile = false;
	private boolean renamedProfile = false;

	public ConfigurationDialog(JFrame parentframe, Profile profile) {
		super(parentframe, true);
		
		this.parentframe = parentframe;
		this.localisation = profile.getLocalisation();
		this.profile = profile;
		
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
		gridbagComposer.addComponentIntoGrid(jlFederation, c);
		gridbagComposer.addComponentIntoGrid(jcbFederation, c);
		c.gridy++;
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
		jpEcranEtiquette.setBorder(new TitledBorder(localisation.getResourceString("configuration.ecran.etiquettes.bordertitle"))); //$NON-NLS-1$

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
		jlAdresseProxy.setEnabled(false);
		jlPortProxy.setEnabled(false);

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

		gridbagComposer.setParentPanel(jpEcranInterface);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jlResultats, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbAvanceResultatCumul, c);
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
		Localisator.localize(this, localisation);
	}

	private void completePanel() {
		completeGeneralPanel(workConfiguration);
		completeConcoursPanel(workConfiguration);
		completeEtiquettePanel(workConfiguration);
		completeAdvancedPanel(workConfiguration);
		jbAnnuler.setEnabled(!workAppConfiguration.isFirstboot());
	}

	private void completeGeneralPanel(Configuration configuration) {
		String[] libelleLangues = Configuration.getAvailableLanguages();

		jtfNomClub.setText(configuration.getClub().getNom());
		((AutoCompleteDocument) jtfAgrClub.getDocument()).setText(configuration.getClub().getAgrement());
		jtfIntConc.setText(configuration.getIntituleConcours());

		jcbProfil.removeActionListener(this);
		jcbProfil.removeAllItems();
		for (String profile : ApplicationCore.userRessources.listAvailableConfigurations())
			jcbProfil.addItem(profile);
		jcbProfil.addItem("---"); //$NON-NLS-1$
		jcbProfil.addItem(localisation.getResourceString("configuration.ecran.general.addprofile")); //$NON-NLS-1$

		jcbProfil.setSelectedItem(configuration.getCurProfil());
		jcbProfil.addActionListener(this);
		
		if(configuration.getCurProfil().equals("defaut") /*|| !configuration.getCurProfil().equals(ConcoursJeunes.configuration.getCurProfil())*/) //$NON-NLS-1$
			jbRenameProfile.setEnabled(false);
		else
			jbRenameProfile.setEnabled(true);
		
		jcbFederation.removeAllItems();
		for(Federation federation : FederationManager.getAvailableFederations())
			jcbFederation.addItem(federation);
		jcbFederation.setSelectedItem(configuration.getFederation());

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
		Reglement reglement = ReglementBuilder.getReglement(configuration.getReglementName());
		jlSelectedReglement.setText(reglement.getDisplayName());
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
		jcbAvanceAffResultatExEquo.setSelected(configuration.isInterfaceAffResultatExEquo());

		jrbUseSpecificConfig.setSelected(workAppConfiguration.isUseProxy());
		jlAdresseProxy.setEnabled(workAppConfiguration.isUseProxy());
		jtfAdresseProxy.setEnabled(workAppConfiguration.isUseProxy());
		jlPortProxy.setEnabled(workAppConfiguration.isUseProxy());
		jtfPortProxy.setEnabled(workAppConfiguration.isUseProxy());
		if (workAppConfiguration.getProxy() != null) {
			jtfAdresseProxy.setText(workAppConfiguration.getProxy().getProxyServerAddress());
			jtfPortProxy.setText(workAppConfiguration.getProxy().getProxyServerPort() + ""); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param configuration
	 * @return les chemins d'accès à l'executable PDF
	 */
	private ArrayList<String> getPdfPath(Configuration configuration) {
		ArrayList<String> pdfPath = new ArrayList<String>();

		if (!workAppConfiguration.getPdfReaderPath().equals("")) {  //$NON-NLS-1$
			pdfPath.add(workAppConfiguration.getPdfReaderPath());
		}
		// Recherche d'un lecteur pdf en fonction du syteme
		if (System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$ 

			String base_pdfPath = ApplicationCore.staticParameters.getResourceString("path.windows.acrobat");  //$NON-NLS-1$
			// tente l'ouverture de acrobat reader
			File f = new File(base_pdfPath);

			String[] fList = f.list();
			if (fList != null) {
				String reader = ""; //$NON-NLS-1$
				for (int i = 0; i < fList.length; i++) {
					if (fList[i].startsWith("Acrobat")) { //$NON-NLS-1$
						reader = base_pdfPath + File.separator + fList[i] + "\\Reader\\AcroRd32.exe";  //$NON-NLS-1$
						pdfPath.add(reader);
					}
				}
			}

		} else if (System.getProperty("os.name").contains("Linux")) { //$NON-NLS-1$ //$NON-NLS-2$

			String[] pdfReader = StringUtils.tokenize(ApplicationCore.staticParameters.getResourceString("path.unix.pdf"), ",");  //$NON-NLS-1$ //$NON-NLS-2$
			for (String reader : pdfReader)
				pdfPath.add(reader);
		}

		return pdfPath;
	}

	/**
	 * Recherche du logo club
	 * 
	 */
	private void changeLogoPath() {
		File f;
		JFileChooser fileDialog = new JFileChooser(workConfiguration.getLogoPath());
		FileNameExtensionFilter filtreimg = new FileNameExtensionFilter(localisation.getResourceString("filter.gifjpeg"), "jpg", "gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		//jbLogoPath.setText("<html><img src=\"file:" + f.getPath() + "\" width=90 height=100></html>"); 
		workConfiguration.setLogoPath(f.getPath());
	}

	public Configuration showConfigurationDialog(AppConfiguration appConfiguration, Configuration configuration) {
		try {
			configuration.save();
			appConfiguration.save();
		} catch (JAXBException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		
		this.workConfiguration = configuration;
		this.workAppConfiguration = appConfiguration;

		completePanel();
		pack();
		setVisible(true);

		return workConfiguration;
	}

	/**
	 * Retourne la configuration du profil actuellement manipulé par la boite
	 * de dialogue
	 * 
	 * @return Renvoie la configuration du profil
	 */
	public Configuration getWorkConfiguration() {
		return workConfiguration;
	}
	
	/**
	 * Retourne la configuration de l'application actuellement manipulé par
	 * la boite de dialogue
	 * 
	 * @return la configuration de l'application en cours de manipulation
	 */
	public AppConfiguration getWorkAppConfiguration() {
		return workAppConfiguration;
	}

	/**
	 * @param workConfiguration
	 *            workConfiguration à définir.
	 */
	public void setWorkConfiguration(Configuration workConfiguration) {
		this.workConfiguration = workConfiguration;
	}

	private void loadProfile() {
		renamedProfile = false;
		workConfiguration = ConfigurationManager.loadConfiguration((String) jcbProfil.getSelectedItem());
		completePanel();

		workConfiguration.setCurProfil((String) jcbProfil.getSelectedItem());
	}

	/**
	 * Détermine si le profil à été ou non renommé
	 * 
	 * @return <code>true</true> si le profil à été renomé, <code>false</code> sinon
	 */
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
						localisation.getResourceString("configuration.ecran.etiquettes.errordimension"), //$NON-NLS-1$
						localisation.getResourceString("configuration.ecran.etiquettes.errordimension.title"),  //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (SecurityException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
					e.toString(), null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		
		if(jtfNomClub.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, 
					localisation.getResourceString("configuration.ecran.general.errornameclub"), //$NON-NLS-1$
					localisation.getResourceString("configuration.ecran.general.errornameclub.title"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		workConfiguration.setFederation((Federation)jcbFederation.getSelectedItem());
		workConfiguration.getClub().setNom(jtfNomClub.getText());
		workConfiguration.getClub().setAgrement(jtfAgrClub.getText());
		workConfiguration.setIntituleConcours(jtfIntConc.getText());
		workConfiguration.setLangue(Configuration.listLangue()[jcbLangue.getSelectedIndex()]);
		workAppConfiguration.setPdfReaderPath(jcbPathPdf.getSelectedItem().toString());

		workConfiguration.setNbCible(Integer.parseInt(jtfNbCible.getText()));
		workConfiguration.setNbTireur((jcbNbTireur.getSelectedIndex() == 0) ? 2 : 4);
		workConfiguration.setNbDepart(Integer.parseInt(jtfNbDepart.getText()));
		workAppConfiguration.setFirstboot(jcbFirstBoot.isSelected());

		workConfiguration.setFormatPapier((String) this.jcbFormatPapier.getSelectedItem());
		workConfiguration.setOrientation((String) this.jcbOrientation.getSelectedItem());
		workConfiguration.setColonneAndLigne(new int[] { nbLigne, nbColonne });
		workConfiguration.setMarges(new Margin(margeHaut, margeBas, margeGauche, margeDroite));
		workConfiguration.setEspacements(new double[] { espacementHorizontal, espacementVertical });
		workConfiguration.setInterfaceResultatCumul(jcbAvanceResultatCumul.isSelected());
		workConfiguration.setInterfaceAffResultatExEquo(jcbAvanceAffResultatExEquo.isSelected());

		workAppConfiguration.setUseProxy(jrbUseSpecificConfig.isSelected());
		workAppConfiguration.getProxy().setProxyServerAddress(jtfAdresseProxy.getText());
		workAppConfiguration.getProxy().setProxyServerPort(Integer.parseInt("0" + jtfPortProxy.getText()));  //$NON-NLS-1$
		
		return true;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.jbValider) {
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
				System.err.println("IOException: " + io.getMessage());  //$NON-NLS-1$
			} catch (NullPointerException npe) {
				System.err.println("Aucune sauvegarde possible. Action annulé");  //$NON-NLS-1$
			}
		} else if (source == jbParcourir) {
			EntiteListDialog eld = new EntiteListDialog(null, localisation);
			if (eld.getAction() == EntiteListDialog.VALIDER)
					jtfAgrClub.setText(eld.getSelectedEntite().getAgrement());
		} else if (source == this.jbDetail) {
			EntiteDialog ed = new EntiteDialog(this, localisation);
			ed.setEntite(workConfiguration.getClub());
			ed.showEntiteDialog();

			jtfNomClub.setText(workConfiguration.getClub().getNom());
		} else if (source == jcbProfil) {
			if (!renameProfile) {
				if (jcbProfil.getSelectedIndex() > -1 && jcbProfil.getSelectedIndex() < jcbProfil.getItemCount() - 2) {
					loadProfile();
				} else if (jcbProfil.getSelectedIndex() == jcbProfil.getItemCount() - 1) {
					String strP = JOptionPane.showInputDialog(this, localisation.getResourceString("configuration.ecran.general.newprofile")); //$NON-NLS-1$
					if (strP != null && !strP.equals("")) {  //$NON-NLS-1$

						workConfiguration.setCurProfil(strP);
						workConfiguration.getMetaDataFichesConcours().removeAll();

						completePanel();
						jcbProfil.insertItemAt(strP, 0);
						jcbProfil.setSelectedItem(strP);
					}
				}
			}
		} else if (source == jbRenameProfile) {
			String strP = JOptionPane.showInputDialog(this, localisation.getResourceString("configuration.ecran.general.newprofile"), //$NON-NLS-1$ 
					workConfiguration.getCurProfil());
			if (strP != null && !strP.isEmpty()) {
				renameProfile = true;

				int insIndex = jcbProfil.getSelectedIndex();
				
				renamedProfile = profile.renameProfile(strP);
				
				if(renamedProfile) {
					jcbProfil.removeItem(workConfiguration.getCurProfil());
	
					workConfiguration.setCurProfil(strP);

					jcbProfil.insertItemAt(strP, insIndex);
					jcbProfil.setSelectedIndex(insIndex);
					
					renamedProfile = true;
				} else {
					JOptionPane.showMessageDialog(this, 
							localisation.getResourceString("erreur.renameprofile"), //$NON-NLS-1$
							localisation.getResourceString("erreur.renameprofile.title"),  //$NON-NLS-1$
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
		} else if (source == jbSelectReglement) {
			ReglementManagerDialog reglementManagerDialog = new ReglementManagerDialog(parentframe, profile);
			Reglement reglement = reglementManagerDialog.showReglementManagerDialog(true);
			if(reglement != null) {
				workConfiguration.setReglementName(reglement.getName());
				jlSelectedReglement.setText(ReglementBuilder.getReglement(reglement.getName()).getDisplayName());
			}
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