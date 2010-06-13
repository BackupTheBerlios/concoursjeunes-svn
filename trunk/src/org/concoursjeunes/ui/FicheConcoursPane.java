/*
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
package org.concoursjeunes.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizableString;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.state.State;
import org.concoursjeunes.state.StateManager;
import org.concoursjeunes.state.StateProcessor;
import org.concoursjeunes.state.StateSelector;
import org.concoursjeunes.state.Categories.Category;
import org.concoursjeunes.ui.dialog.ConcurrentDialog;
import org.concoursjeunes.ui.dialog.ParametreDialog;
import org.concoursjeunes.ui.dialog.ResultatDialog;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.error.ErrorInfo;

import com.lowagie.text.DocumentException;

/**
 * fiche concours. cette fiche correspond à la table d'inscrit et de résultats
 * @author  Aurélien Jeoffray
 * @version  1.0
 * 
 * TODO passage de niveau
 */
public class FicheConcoursPane extends JPanel implements ActionListener, ChangeListener, HyperlinkListener, MouseListener {

	private ConcoursJeunesFrame parentframe;
	private AjResourcesReader localisation;

	@Localizable("onglet.ficheconcours")
	private JTabbedPane tabbedpane		= new JTabbedPane();
	@Localizable("onglet.classements")
	private JTabbedPane jtbClassement	= new JTabbedPane();
	
	@Localizable("onglet.ficheconcours.0")
	private final JLabel jlGestionArcher = new JLabel();
	@Localizable("onglet.gestionarcher.depart")
	private final LocalizableString lsDepart = new LocalizableString();

	private JPanel fichesDepart			= new JPanel();
	private CardLayout cl				= new CardLayout();
	private ArrayList<FicheConcoursDepartPane> departPanels = new ArrayList<FicheConcoursDepartPane>();

	//panneau de classements
	private JEditorPane jepClassIndiv		= new JEditorPane();
	private JEditorPane jepClassTeam		= new JEditorPane();
	private JEditorPane jepClassClub		= new JEditorPane();
	
	//panneau de classement
	//bouton d'enregistrement
	@Localizable("bouton.saisieresultats")
	private JButton jbResultat				= new JButton();
	@Localizable("interface.critereclassement")
	private JLabel jlCritClassement			= new JLabel();
	//critere individuel
	private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();

	@StateSelector(name="100-classement")
	@Localizable("bouton.impressionresultats")
	private JButton printClassementIndiv	= new JButton();
	@StateSelector(name="200-teamclassement")
	@Localizable("bouton.impressionresultats")
	private JButton printClassementEquipe	= new JButton();
	@StateSelector(name="300-clubclassement")
	@Localizable("bouton.impressionresultats")
	private JButton printClassementClub		= new JButton();
	
	//panneau d'édition
	@Localizable("state.help")
	private JLabel jlAide = new JLabel();
	@Localizable("state.choosestate")
	private JLabel jlCurrentStateName = new JLabel();
	@Localizable("state.start")
	private JLabel jlDepart = new JLabel();
	@Localizable("state.serie")
	private JLabel jlSerie = new JLabel();
	private JComboBox jcbDeparts = new JComboBox();
	private JComboBox jcbSeries = new JComboBox();
	@Localizable("state.save")
	private JCheckBox jcbSave = new JCheckBox();
	@Localizable("state.print")
	private JButton jbPrint = new JButton();
	private JXBusyLabel jxbPrint = new JXBusyLabel();
	@Localizable(value="",tooltip="state.opendocument")
	private JButton jbOpenDocument = new JButton();
	@Localizable(value="",tooltip="state.deletedocument")
	private JButton jbDeleteDocument = new JButton();
	private AJList<File> ajlDocuments = new AJList<File>();

	public ParametreDialog paramDialog;
	public ConcurrentDialog concDialog;
	public int index					= 1;
	
	private FicheConcours ficheConcours;
	private State currentState = null;
	private StateManager stateManager;

	/**
	 * Création d'une fiche concours
	 *  
	 * 
	 * @param parentframe
	 * @param ficheConcours
	 */
	public FicheConcoursPane(ConcoursJeunesFrame parentframe, FicheConcours ficheConcours) {
		this.parentframe = parentframe;
		this.ficheConcours = ficheConcours;
		this.localisation = parentframe.profile.getLocalisation();
		
		paramDialog = new ParametreDialog(parentframe, parentframe.profile, ficheConcours);
		
		if(!ficheConcours.getParametre().isReglementLock()) {
			paramDialog.showParametreDialog(ficheConcours.getParametre());
		}
		
		init();
		completeListDocuments();
		concDialog = new ConcurrentDialog(parentframe, parentframe.profile, ficheConcours);
	}

	/**
	 * initialise les parametres de la fiche.
	 * 
	 */
	private void init() {
		JPanel northpane = new JPanel();
		JPanel northpaneEqu = new JPanel();
		JPanel northpaneClub = new JPanel();
		//JPanel statusbar = new JPanel();

		//met la fiche de classement au format html
		jepClassIndiv.setEditorKit(new HTMLEditorKit());
		jepClassIndiv.setEditable(false);
		jepClassIndiv.addHyperlinkListener(this);
		//empeche le retour automatique au début de la page à chaque update
		((DefaultCaret)jepClassIndiv.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		jepClassTeam.setEditorKit(new HTMLEditorKit());
		jepClassTeam.setEditable(false);
		//empeche le retour automatique au début de la page à chaque update
		((DefaultCaret)jepClassTeam.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		jepClassClub.setEditorKit(new HTMLEditorKit());
		jepClassClub.setEditable(false);
		//empeche le retour automatique au début de la page à chaque update
		((DefaultCaret)jepClassClub.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		jbResultat.addActionListener(this);

		printClassementIndiv.addActionListener(this);
		printClassementIndiv.setName("printClassementIndiv"); //$NON-NLS-1$
		printClassementEquipe.addActionListener(this);
		printClassementEquipe.setName("printClassementEquipe"); //$NON-NLS-1$
		printClassementClub.addActionListener(this);
		printClassementClub.setName("printClassementClub"); //$NON-NLS-1$
		printClassementIndiv.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementEquipe.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementClub.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.print") //$NON-NLS-1$
		));

		//classement individuel
		GridbagComposer composer = new GridbagComposer();
		GridBagConstraints c = new GridBagConstraints();
		
		composer.setParentPanel(northpane);
		c.gridy = 0; c.insets = new Insets(5,5,5,5);
		composer.addComponentIntoGrid(jbResultat, c);

		c.insets = new Insets(5,0,5,0);
		composer.addComponentIntoGrid(jlCritClassement, c);
		for(Criterion criteria : ficheConcours.getParametre().getReglement().getListCriteria()) {
			JCheckBox checkBox = new JCheckBox(criteria.getLibelle(), criteria.isClassement());

			checkBox.addActionListener(this);
			if(ficheConcours.getParametre().getReglement().isOfficialReglement())
				checkBox.setEnabled(false);
			else if(criteria.isPlacement())
				checkBox.setEnabled(false);
			composer.addComponentIntoGrid(checkBox, c);
			
			classmentCriteriaCB.put(criteria.getCode(), checkBox);
		}
		c.weightx = 1.0; c.insets = new Insets(5,5,5,5);
		composer.addComponentIntoGrid(Box.createHorizontalGlue(),c);
		c.weightx = 0.0;
		composer.addComponentIntoGrid(printClassementIndiv, c);

		//classement par equipe
		northpaneEqu.setLayout(new FlowLayout(FlowLayout.RIGHT));
		northpaneEqu.add(printClassementEquipe);
		
		//classement par equipe
		northpaneClub.setLayout(new FlowLayout(FlowLayout.RIGHT));
		northpaneClub.add(printClassementClub);

		fichesDepart.setLayout(cl);
		
		for(int i = 0; i < ficheConcours.getParametre().getNbDepart(); i++) {
			FicheConcoursDepartPane ficheConcoursDepartPane = new FicheConcoursDepartPane(this, i);
			departPanels.add(ficheConcoursDepartPane);
			fichesDepart.add(ficheConcoursDepartPane, "depart." + i); //$NON-NLS-1$
		}
		cl.first(fichesDepart);

		//paneau du classement individuel
		JPanel ficheI = new JPanel();
		ficheI.setLayout(new BorderLayout());
		ficheI.add(northpane,BorderLayout.NORTH);
		ficheI.add(new JScrollPane(jepClassIndiv),BorderLayout.CENTER);

		//paneau du classement par equipe
		JPanel ficheE = new JPanel();
		ficheE.setLayout(new BorderLayout());
		ficheE.add(northpaneEqu,BorderLayout.NORTH);
		ficheE.add(new JScrollPane(jepClassTeam),BorderLayout.CENTER);
		
		//paneau du classement par club
		JPanel ficheC = new JPanel();
		ficheC.setLayout(new BorderLayout());
		ficheC.add(northpaneClub, BorderLayout.NORTH);
		ficheC.add(new JScrollPane(jepClassClub),BorderLayout.CENTER);
		
		jtbClassement.addChangeListener(this);
		jtbClassement.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		jtbClassement.setTabPlacement(JTabbedPane.LEFT);
		jtbClassement.addTab("onglet.classementindividuel", new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.archer")), //$NON-NLS-1$
				ficheI);
		jtbClassement.addTab("onglet.classementequipe",new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheE);
		jtbClassement.addTab("onglet.classementclub",new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheC);
		
		JPanel jpEdition = initEditions();
		ajlDocuments.setCellRenderer(new ListCellRenderer() {
			protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
			/* (non-Javadoc)
			 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
				        isSelected, cellHasFocus);
				Icon fileIcon = FileSystemView.getFileSystemView().getSystemIcon((File)value);
				renderer.setText(((File)value).getName());
				renderer.setIcon(fileIcon);

				return renderer;
			}
		});

		//panneau global
		tabbedpane.addChangeListener(this);
		tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedpane.addTab("onglet.gestionarcher", null, fichesDepart); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.addTab("onglet.pointage.greffe", new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.desktop")), //$NON-NLS-1$
				new GreffePane(this));
		tabbedpane.addTab("onglet.phasesfinal",new FicheConcoursFinalPane(this)); //$NON-NLS-1$
		tabbedpane.addTab("onglet.classement", //$NON-NLS-1$
				new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
						File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.team")), //$NON-NLS-1$
						jtbClassement);
		tabbedpane.addTab("onglet.edition", new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.print")), //$NON-NLS-1$
				jpEdition);
		
		//if(!ficheConcours.getParametre().isDuel())
		//	tabbedpane.setEnabledAt(2,false);

		//integration
		setLayout(new BorderLayout());
		add(tabbedpane, BorderLayout.CENTER);
		//add(statusbar, BorderLayout.SOUTH);

		affectLibelle();
	}
	
	private JPanel initEditions() {
		JPanel panel = new JPanel();
		
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		taskPaneContainer.setOpaque(false);
		
		try {
			//charge le gestionnaire d'etat
			stateManager = new StateManager();
			
			//charge les etats trouvé
			for(Category categorie : stateManager.getCategories().getCategorie()) {
				java.util.List<State> states = stateManager.getStates(categorie.getName());
				if(states.size() > 0) {
					JXTaskPane taskPane = new JXTaskPane();
					taskPane.setTitle(categorie.getLocalizedLibelle());
					taskPane.getContentPane().setBackground(Color.WHITE);
					taskPane.setOpaque(true);
					
					for(State state : states)
						taskPane.add(printGenAction(state));
					
					taskPaneContainer.add(taskPane);
				}
			}
		} catch (ScriptException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(parentframe.profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		
		panel.setLayout(new BorderLayout());
		
		panel.add(taskPaneContainer, BorderLayout.WEST);
		panel.add(initOptions(), BorderLayout.CENTER);
		//panel.add(printFeuilleMarque);
		
		return panel;
	}
	
	private JPanel initOptions() {
		JPanel panel = new JPanel();
		
		JXPanel jpInformations = new JXPanel();
		jpInformations.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpInformations.setBackground(new Color(255, 255, 225));
		jpInformations.setOpaque(true);
		
		JPanel jpOptions = new JPanel();
		jpOptions.setBorder(new TitledBorder(parentframe.profile.getLocalisation().getResourceString("state.options"))); //$NON-NLS-1$
		
		GridbagComposer composer = new GridbagComposer();
		GridBagConstraints c = new GridBagConstraints();
		
		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++) {
			final int dp = i;
			jcbDeparts.addItem(new Object() {
				@Override
				public String toString() {
					return lsDepart.toString() + dp;
				}
			}); 
		}
		
		jbPrint.addActionListener(this);
		jbPrint.setEnabled(false);
		ajlDocuments.addMouseListener(this);
		jbOpenDocument.addActionListener(this);
		jbOpenDocument.setMargin(new Insets(0,0,0,0));
		jbOpenDocument.setIcon(new ImageIcon(
				ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator +
				ApplicationCore.staticParameters.getResourceString("file.icon.opendocument"))); //$NON-NLS-1$
		jbDeleteDocument.addActionListener(this);
		jbDeleteDocument.setMargin(new Insets(0,0,0,0));
		jbDeleteDocument.setIcon(new ImageIcon(
				ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator +
				ApplicationCore.staticParameters.getResourceString("file.icon.removeelement"))); //$NON-NLS-1$
		
		jpInformations.add(jlAide);

		composer.setParentPanel(jpOptions);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		composer.addComponentIntoGrid(jlDepart, c);
		c.weightx = 1.0;
		composer.addComponentIntoGrid(jcbDeparts, c);
		c.gridwidth = 2;
		composer.addComponentIntoGrid(jlCurrentStateName, c);
		c.gridy++;
		c.weightx = 0;
		c.gridwidth = 1;
		composer.addComponentIntoGrid(jlSerie, c);
		c.weightx = 1.0;
		composer.addComponentIntoGrid(jcbSeries, c);
		c.gridy++;
		c.weightx = 0; c.gridwidth = 2;
		composer.addComponentIntoGrid(jcbSave, c);
		c.weightx = 0.0; c.gridwidth = 1; c.insets = new Insets(0,0,0,10);
		composer.addComponentIntoGrid(jbPrint, c);
		c.weightx = 1.0;
		composer.addComponentIntoGrid(jxbPrint, c);
		
		JPanel docActions = new JPanel();
		docActions.setLayout(new FlowLayout(FlowLayout.LEFT));
		docActions.add(jbOpenDocument);
		docActions.add(jbDeleteDocument);
		
		JPanel jpDocuments = new JPanel();
		jpDocuments.setBorder(new TitledBorder(parentframe.profile.getLocalisation().getResourceString("state.generareddoc"))); //$NON-NLS-1$
		jpDocuments.setLayout(new BorderLayout());
		jpDocuments.add(docActions, BorderLayout.NORTH);
		jpDocuments.add(new JScrollPane(ajlDocuments), BorderLayout.CENTER);
		
		JPanel jpOptInf = new JPanel();
		jpOptInf.setLayout(new BorderLayout());
		jpOptInf.add(jpInformations, BorderLayout.NORTH);
		jpOptInf.add(jpOptions, BorderLayout.CENTER);
		
		panel.setLayout(new BorderLayout());
		panel.add(jpOptInf, BorderLayout.NORTH);
		panel.add(jpDocuments, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel getGestArchersTabComponent() {
		JPanel panel = new JPanel();
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(this);

		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++) {
			final int dp = i;
			comboBox.addItem(new Object() {
				@Override
				public String toString() {
					return lsDepart.toString() + dp;
				}
			});
		}
		//comboBox.addItem("---"); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(jlGestionArcher, BorderLayout.CENTER);
		panel.add(comboBox, BorderLayout.EAST);
		return panel;
	}
	
	private void completeListDocuments() {
		String concoursFileName = ficheConcours.getParametre().getSaveName();
		String concoursDirectory = concoursFileName.substring(0, concoursFileName.length() - 5);
		
		File docsPathFile = new File(
				ApplicationCore.userRessources.getConcoursPathForProfile(parentframe.profile), 
				concoursDirectory);
		if(docsPathFile.exists()) {
			File[] files = docsPathFile.listFiles();
			FileUtils.orderByDate(files, true);
			if(files != null && files.length > 0)
				ajlDocuments.setListData(files);
		}
	}

	/**
	 * affecte les libelle localisé à l'interface
	 *
	 */
	private void affectLibelle() {
		Localizator.localize(this, parentframe.profile.getLocalisation());
		
		jcbSeries.removeAllItems();
		for(int i = 1; i <= ficheConcours.getParametre().getReglement().getNbSerie(); i++)
			jcbSeries.addItem(parentframe.profile.getLocalisation().getResourceString("state.numserie", i)); //$NON-NLS-1$
	}

	/**
	 * Affiche la boite de dialogue des parametre du concours
	 */
	public void openParametreDialog() {
		paramDialog.showParametreDialog(ficheConcours.getParametre());
	}

	/**
	 * Affiche la boite de dialogue de gestion des concurrents
	 * 
	 * @param editedConcurrent - le concurrent à editer
	 * @param concurrents - la liste des concurrents entourant le concurrent à éditer nécessaire pour les
	 * fonctions précédent/suivant. null si non utilisé
	 */
	public void openConcurrentDialog(Concurrent editedConcurrent, List<Concurrent> concurrents) {
		if(editedConcurrent != null) {
			boolean hasPrevious = false;
			boolean hasNext = false;
			if(concurrents != null) {
				int index = concurrents.indexOf(editedConcurrent);
				if(index > 0)
					hasPrevious = true;
				if(index+1 < concurrents.size())
					hasNext = true;
			}
			int codeRetour = concDialog.showConcurrentDialog(editedConcurrent, hasPrevious, hasNext);
			if(concurrents != null && (codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT 
					|| codeRetour == ConcurrentDialog.CONFIRM_AND_PREVIOUS)) {
				
				int index = concurrents.indexOf(editedConcurrent);
				if(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT) {
					if(index > -1 && index + 1 < concurrents.size())
						openConcurrentDialog(concurrents.get(index + 1), concurrents);
				} else {
					if(index > 0)
						openConcurrentDialog(concurrents.get(index - 1), concurrents);
				}
			}
		}
	}

	/**
	 * Affiche la boite de dialogue de saisie des résultats
	 * 
	 */
	public void openResultatDialog() {
		List<Concurrent> concurrents = ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart());
		if(concurrents.size() == 0)
			return;
		ResultatDialog resultat = new ResultatDialog(parentframe, localisation, parentframe.profile,
				ficheConcours.getParametre(), concurrents);

		//si annulation ne pas continuer
		int returnVal = resultat.showResultatDialog();
		if(returnVal == ResultatDialog.CANCEL)
			return;

		if(returnVal == ResultatDialog.NEXT_TARGET) {
			do {
				index++;
			} while(ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).size() == 0 && index <= ficheConcours.getParametre().getNbCible());
		} else if(returnVal == ResultatDialog.PREVIOUS_TARGET) {
			do {
				index--;
			} while(ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).size() == 0 && index > 0);
		}

		if(returnVal != ResultatDialog.SAVE_AND_QUIT && (index > 0 && index <= ficheConcours.getParametre().getNbCible())) {
			openResultatDialog();
		} else {
			index = 1;
		}
	}
	
	private Action printGenAction(State state) {
		final State actionState = state;
		
        Action action = new AbstractAction(state.getLocalizedDisplayName()) {
            public void actionPerformed(ActionEvent e) {
            	prepareState(actionState);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(
               ApplicationCore.staticParameters.getResourceString("path.ressources") + "/document-print.png"));  //$NON-NLS-1$//$NON-NLS-2$
        //action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        
        return action;
    }
	
	public void switchToEditPane() {
		tabbedpane.setSelectedIndex(4);
	}
	
	public void prepareState(State state) {
		jcbDeparts.setEnabled(state.isStart());
    	jcbSeries.setEnabled(state.isSerie());
    	jcbSave.setSelected(state.isSave());
    	jlCurrentStateName.setText("<html><font color=\"blue\" size=\"+1\">" + state.getLocalizedDisplayName() + "</font></html>"); //$NON-NLS-1$ //$NON-NLS-2$
    	currentState = state;
    	jbPrint.setEnabled(true);
	}
	
	private void printState() {
		if(currentState != null) {
			try {
				StateProcessor sp = new StateProcessor(currentState, parentframe.profile, ficheConcours);
				sp.process(jcbDeparts.getSelectedIndex(), jcbSeries.getSelectedIndex(), jcbSave.isSelected());
				completeListDocuments();
				jxbPrint.setBusy(false);
				jxbPrint.setText(""); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			} catch (IOException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			} catch (ScriptException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			} catch (DocumentException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			}
		}
	}
	
	private void openPdf(File file) {
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				assert parentframe.profile.getConfiguration() != null;
				
				String NAV = ApplicationCore.getAppConfiguration().getPdfReaderPath();

				System.out.println(NAV + " " + file.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
				Runtime.getRuntime().exec(NAV + " " + file.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (IOException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return  parentframe
	 */
	public ConcoursJeunesFrame getParentframe() {
		return parentframe;
	}

	/**
	 * @param parentframe  parentframe à définir
	 */
	public void setParentframe(ConcoursJeunesFrame parentframe) {
		this.parentframe = parentframe;
	}

	/**
	 * @return ficheConcours
	 */
	public FicheConcours getFicheConcours() {
		return ficheConcours;
	}

	/**
	 * @param ficheConcours ficheConcours à définir
	 */
	public void setFicheConcours(FicheConcours ficheConcours) {
		this.ficheConcours = ficheConcours;
	}

	/**
	 * @return localisation
	 */
	protected AjResourcesReader getLocalisation() {
		return localisation;
	}

	/**
	 * @param localisation localisation à définir
	 */
	protected void setLocalisation(AjResourcesReader localisation) {
		this.localisation = localisation;
	}

	///////////////////////////////////////
	// Auditeur d'événement
	//////////////////////////////////////
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();

		if(source == jbResultat) {
			index = 1;
			while(index < ficheConcours.getParametre().getNbCible() 
					&& ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).size() == 0)
				index++;
			if(index < ficheConcours.getParametre().getNbCible()) {
				openResultatDialog();
				jepClassIndiv.setText(ficheConcours.getClassement());
			} else {
				JOptionPane.showMessageDialog(this, parentframe.profile.getLocalisation().getResourceString("ficheconcours.target.empty")); //$NON-NLS-1$
			}
		} else if(source instanceof JCheckBox) {
			for(Criterion criterion : ficheConcours.getParametre().getReglement().getListCriteria()) {
				criterion.setClassement(classmentCriteriaCB.get(criterion.getCode()).isSelected());
			}

			jepClassIndiv.setText(ficheConcours.getClassement());
		} else if(source instanceof JComboBox) {
			ficheConcours.setCurrentDepart(((JComboBox)source).getSelectedIndex());
			cl.show(fichesDepart, "depart." + ficheConcours.getCurrentDepart()); //$NON-NLS-1$
		} else if(source == jbPrint) {
			jxbPrint.setBusy(true);
			jxbPrint.setText(parentframe.profile.getLocalisation().getResourceString("state.generate")); //$NON-NLS-1$
			Thread t = new Thread() {
				@Override
				public void run() {
					printState();
				}
			};
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
			
		} else if(source == jbOpenDocument) {
			if(!ajlDocuments.isSelectionEmpty())
				openPdf((File)ajlDocuments.getSelectedValue());
		} else if(source == jbDeleteDocument) {
			if (JOptionPane.showConfirmDialog(this, parentframe.profile.getLocalisation().getResourceString("state.confirmation.suppression"), //$NON-NLS-1$
					parentframe.profile.getLocalisation().getResourceString("state.confirmation.suppression.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {//$NON-NLS-1$
				Object[] files = ajlDocuments.getSelectedValues();
				for(Object f : files) {
					((File)f).delete();
					ajlDocuments.remove((File)f);
				}
			}
		} else if(source == printClassementIndiv || source == printClassementEquipe ||  source == printClassementClub) {
			switchToEditPane();
			if(stateManager != null) {
				try {
					String stateName = this.getClass().getDeclaredField(((JButton)source).getName()).getAnnotation(StateSelector.class).name();
					State state = stateManager.getState(stateName);
					if(state != null)
						prepareState(state);
				} catch (SecurityException e) {
					DisplayableErrorHelper.displayException(e);
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					DisplayableErrorHelper.displayException(e);
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Gestion des changements d'onglet
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if(i == 2) {
				List<Concurrent> listConcurrents = ficheConcours.getConcurrentList().list(ficheConcours.getCurrentDepart());
				for(Concurrent concurrent : listConcurrents) {
					if(concurrent.getCible() == 0) {
						JOptionPane.showMessageDialog(this,
								parentframe.profile.getLocalisation().getResourceString("erreur.nocible"), //$NON-NLS-1$ 
								parentframe.profile.getLocalisation().getResourceString("erreur.nocible.titre"),JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
						break;
					}
				}
				int index = jtbClassement.getSelectedIndex();
				switch(index) {
					case 0:
						jepClassIndiv.setText(ficheConcours.getClassement());
						break;
					case 1:
						jepClassTeam.setText(ficheConcours.getClassementEquipe());
						break;
					case 2:
						jepClassClub.setText(ficheConcours.getClassementClub());
				}
			}
		} else if(e.getSource() == jtbClassement) {
			List<Concurrent> listConcurrents = ficheConcours.getConcurrentList().list(ficheConcours.getCurrentDepart());
			for(Concurrent concurrent : listConcurrents) {
				if(concurrent.getCible() == 0) {
					JOptionPane.showMessageDialog(this,
							parentframe.profile.getLocalisation().getResourceString("erreur.nocible"), //$NON-NLS-1$ 
							parentframe.profile.getLocalisation().getResourceString("erreur.nocible.titre"),JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
					break;
				}
			}
			switch(jtbClassement.getSelectedIndex()) {
				case 0:
					jepClassIndiv.setText(ficheConcours.getClassement());
					break;
				case 1:
					jepClassTeam.setText(ficheConcours.getClassementEquipe());
					break;
				case 2:
					jepClassClub.setText(ficheConcours.getClassementClub());
			}
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e) { 
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
			if (e instanceof HTMLFrameHyperlinkEvent) { 
				((HTMLDocument)jepClassIndiv.getDocument()).processHTMLFrameHyperlinkEvent( 
						(HTMLFrameHyperlinkEvent)e); 
			} else {
				openConcurrentDialog(ficheConcours.getConcurrentList().getConcurrentAt(
						Integer.parseInt(e.getURL().getRef().substring(0,1)),
						Integer.parseInt(e.getURL().getRef().substring(2)),
						Integer.parseInt(e.getURL().getRef().substring(1,2))), null);

				jepClassIndiv.setText(ficheConcours.getClassement());
			} 
		} 
	}
	
	public void dispose() {
		paramDialog.dispose();
		paramDialog = null;
		concDialog.dispose();
		concDialog = null;
		
		tabbedpane.removeChangeListener(this);
		jepClassIndiv.removeHyperlinkListener(this);
		jbResultat.removeActionListener(this);
		printClassementIndiv.removeActionListener(this);
		printClassementEquipe.removeActionListener(this);
		printClassementClub.removeActionListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == ajlDocuments && e.getClickCount() == 2) {
			if(!ajlDocuments.isSelectionEmpty()) {
				File openFile = (File)ajlDocuments.getSelectedValue();
				openPdf(openFile);
			}
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
}