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
package org.concoursjeunes.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
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

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.state.State;
import org.concoursjeunes.state.StateManager;
import org.concoursjeunes.state.StateProcessor;
import org.concoursjeunes.state.Categories.Category;
import org.concoursjeunes.ui.dialog.ConcurrentDialog;
import org.concoursjeunes.ui.dialog.ParametreDialog;
import org.concoursjeunes.ui.dialog.ResultatDialog;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import ajinteractive.standard.java2.GridbagComposer;
import ajinteractive.standard.ui.AJList;

/**
 * fiche concours. cette fiche correspond à la table d'inscrit et de résultats
 * @author  Aurélien Jeoffray
 * @version  1.0
 * 
 * TODO passage de niveau
 */
public class FicheConcoursPane extends JPanel implements ActionListener, ChangeListener, HyperlinkListener {

	private ConcoursJeunesFrame parentframe;

	private JTabbedPane tabbedpane		= new JTabbedPane();
	private JTabbedPane jtbClassement	= new JTabbedPane();

	private JPanel fichesDepart			= new JPanel();
	private CardLayout cl				= new CardLayout();
	private ArrayList<FicheConcoursDepartPane> departPanels = new ArrayList<FicheConcoursDepartPane>();

	//panneau de classements
	private JEditorPane jepClassIndiv		= new JEditorPane();
	private JEditorPane jepClassTeam		= new JEditorPane();
	private JEditorPane jepClassClub		= new JEditorPane();
	
	//panneau de classement
	//bouton d'enregistrement
	private JButton jbResultat				= new JButton();
	private JLabel jlCritClassement			= new JLabel();
	//critere individuel
	private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();

	private JButton printClassementIndiv	= new JButton();
	private JButton printClassementEquipe	= new JButton();
	private JButton printClassementClub		= new JButton();
	
	//panneau d'édition
	private JLabel jlDepart = new JLabel();
	private JLabel jlSerie = new JLabel();
	private JComboBox jcbDeparts = new JComboBox();
	private JComboBox jcbSeries = new JComboBox();
	private JButton jbPrint = new JButton();
	private AJList ajlDocuments = new AJList();

	public ParametreDialog paramDialog;
	public ConcurrentDialog concDialog;
	public int index					= 1;
	
	private FicheConcours ficheConcours;

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
		
		paramDialog = new ParametreDialog(parentframe, ficheConcours);
		
		if(!ficheConcours.getParametre().isReglementLock()) {
			paramDialog.showParametreDialog(ficheConcours.getParametre());
		}
		
		init();
		completeListDocuments();
		concDialog = new ConcurrentDialog(parentframe, ficheConcours);
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
		printClassementEquipe.addActionListener(this);
		printClassementClub.addActionListener(this);
		printClassementIndiv.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementEquipe.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementClub.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));

		//classement individuel
		northpane.setLayout(new FlowLayout());
		northpane.add(jbResultat);

		northpane.add(jlCritClassement);
		for(Criterion criteria : ficheConcours.getParametre().getReglement().getListCriteria()) {
			JCheckBox checkBox = new JCheckBox(criteria.getLibelle(), criteria.isClassement());

			checkBox.addActionListener(this);
			if(ficheConcours.getParametre().getReglement().isOfficialReglement())
				checkBox.setEnabled(false);
			else if(criteria.isPlacement())
				checkBox.setEnabled(false);
			northpane.add(checkBox);
			
			classmentCriteriaCB.put(criteria.getCode(), checkBox);
		}
		northpane.add(Box.createHorizontalGlue());
		northpane.add(printClassementIndiv);

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
		jtbClassement.addTab("onglet.classementindividuel", new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.archer")), //$NON-NLS-1$
				ficheI);
		jtbClassement.addTab("onglet.classementequipe",new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheE);
		jtbClassement.addTab("onglet.classementclub",new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
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
		tabbedpane.addTab("onglet.pointage.greffe", new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.desktop")), //$NON-NLS-1$
				new GreffePane(this));
		tabbedpane.addTab("onglet.classement", //$NON-NLS-1$
				new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
						File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
						jtbClassement);
		tabbedpane.addTab("onglet.edition", new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.print")), //$NON-NLS-1$
				jpEdition);

		//integration
		setLayout(new BorderLayout());
		add(tabbedpane, BorderLayout.CENTER);
		//add(statusbar, BorderLayout.SOUTH);

		affectLibelle();
	}
	
	private JPanel initEditions() {
		JPanel panel = new JPanel();
		
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		
		StateManager sm = new StateManager();
		
		for(Category categorie : sm.getCategories().getCategorie()) {
			JXTaskPane taskPane = new JXTaskPane();
			taskPane.setTitle(categorie.getLocalizedLibelle());
			
			for(State state : sm.getStates(categorie.getName()))
				taskPane.add(printGenAction(state));
			
			taskPaneContainer.add(taskPane);
		}
		
		panel.setLayout(new BorderLayout());
		
		panel.add(taskPaneContainer, BorderLayout.WEST);
		panel.add(initOptions(), BorderLayout.CENTER);
		//panel.add(printFeuilleMarque);
		
		return panel;
	}
	
	private JPanel initOptions() {
		JPanel panel = new JPanel();
		
		JPanel jpOptions = new JPanel();
		jpOptions.setBorder(new TitledBorder("Option d'impressions"));
		
		GridbagComposer composer = new GridbagComposer();
		GridBagConstraints c = new GridBagConstraints();
		
		jlDepart.setText("Départ:");
		jlSerie.setText("Série:");
		jbPrint.setText("Imprimer");
		
		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++)
			jcbDeparts.addItem(
					ApplicationCore.ajrLibelle.getResourceString("onglet.gestionarcher.depart") + i); //$NON-NLS-1$
		for(int i = 1; i <= ficheConcours.getParametre().getReglement().getNbSerie(); i++)
			jcbSeries.addItem("Série n°" + i);
		
		
		composer.setParentPanel(jpOptions);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		composer.addComponentIntoGrid(jlDepart, c);
		c.weightx = 1.0;
		composer.addComponentIntoGrid(jcbDeparts, c);
		c.gridy++;
		c.weightx = 0;
		composer.addComponentIntoGrid(jlSerie, c);
		c.weightx = 1.0;
		composer.addComponentIntoGrid(jcbSeries, c);
		composer.addComponentIntoGrid(jbPrint, c);
		
		JPanel jpDocuments = new JPanel();
		jpDocuments.setBorder(new TitledBorder("Documents généré"));
		jpDocuments.setLayout(new BorderLayout());
		jpDocuments.add(new JScrollPane(ajlDocuments), BorderLayout.CENTER);
		
		panel.setLayout(new BorderLayout());
		panel.add(jpOptions, BorderLayout.NORTH);
		panel.add(jpDocuments, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel getGestArchersTabComponent() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(tabbedpane.getTitleAt(0));
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(this);

		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++)
			comboBox.addItem(
					ApplicationCore.ajrLibelle.getResourceString("onglet.gestionarcher.depart") + i); //$NON-NLS-1$
		//comboBox.addItem("---"); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(label, BorderLayout.CENTER);
		panel.add(comboBox, BorderLayout.EAST);
		return panel;
	}
	
	private void completeListDocuments() {
		String concoursFileName = ficheConcours.getParametre().getSaveName();
		String concoursDirectory = concoursFileName.substring(0, concoursFileName.length() - 4);
		
		File docsPathFile = new File(
				ApplicationCore.userRessources.getConcoursPathForProfile(ApplicationCore.getConfiguration().getCurProfil()), 
				concoursDirectory);
		if(docsPathFile.exists()) {
			File[] files = docsPathFile.listFiles();
			//FileUtils.sortFilesListByDate(files, 1);
			if(files != null && files.length > 0)
				ajlDocuments.setListData(files);
		}
	}

	/**
	 * affecte les libelle localisé à l'interface
	 *
	 */
	private void affectLibelle() {
		jbResultat.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.saisieresultats")); //$NON-NLS-1$
		printClassementIndiv.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		printClassementEquipe.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		printClassementClub.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		jlCritClassement.setText(ApplicationCore.ajrLibelle.getResourceString("interface.critereclassement")); //$NON-NLS-1$
		tabbedpane.setTitleAt(0, ApplicationCore.ajrLibelle.getResourceString("onglet.gestionarcher")); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.setTitleAt(1, ApplicationCore.ajrLibelle.getResourceString("onglet.pointage.greffe")); //$NON-NLS-1$
		tabbedpane.setTitleAt(2, ApplicationCore.ajrLibelle.getResourceString("onglet.classement")); //$NON-NLS-1$
		tabbedpane.setTitleAt(3, ApplicationCore.ajrLibelle.getResourceString("onglet.edition")); //$NON-NLS-1$
		jtbClassement.setTitleAt(0, ApplicationCore.ajrLibelle.getResourceString("onglet.classementindividuel")); //$NON-NLS-1$
		jtbClassement.setTitleAt(1, ApplicationCore.ajrLibelle.getResourceString("onglet.classementequipe")); //$NON-NLS-1$
		jtbClassement.setTitleAt(2, ApplicationCore.ajrLibelle.getResourceString("onglet.classementclub")); //$NON-NLS-1$
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
	 * @param concurrent - le concurrent à editer
	 */
	public void openConcurrentDialog(Concurrent concurrent) {
		if(concurrent != null) {
			int codeRetour = concDialog.showConcurrentDialog(concurrent);
			if(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT 
					|| codeRetour == ConcurrentDialog.CONFIRM_AND_PREVIOUS) {
				
				if(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT) {
					openConcurrentDialog(ficheConcours.getConcurrentList().nextConcurrent(concurrent));
				} else {
					assert codeRetour == ConcurrentDialog.CONFIRM_AND_PREVIOUS;
					
					openConcurrentDialog(ficheConcours.getConcurrentList().previousConcurrent(concurrent));
				}
			}
		}
	}

	/**
	 * Affiche la boite de dialogue de saisie des résultats
	 * 
	 */
	public void openResultatDialog() {
		Concurrent[] concurrents = ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart());
		if(concurrents.length == 0)
			return;
		ResultatDialog resultat = new ResultatDialog(parentframe, 
				concurrents,
				ficheConcours.getParametre());

		//si annulation ne pas continuer
		int returnVal = resultat.showResultatDialog();
		if(returnVal == ResultatDialog.CANCEL)
			return;

		if(returnVal == ResultatDialog.NEXT_TARGET) {
			do {
				index++;
			} while(ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).length == 0 && index <= ficheConcours.getParametre().getNbCible());
		} else if(returnVal == ResultatDialog.PREVIOUS_TARGET) {
			do {
				index--;
			} while(ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).length == 0 && index > 0);
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
            	StateProcessor sp = new StateProcessor(actionState, ficheConcours);
            	sp.process();
            	completeListDocuments();
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(
               ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + "/document-print.png"));  //$NON-NLS-1$//$NON-NLS-2$
        //action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        
        return action;
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

	///////////////////////////////////////
	// Auditeur d'événement
	//////////////////////////////////////
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();

		if(source == jbResultat) {
			index = 1;
			while(index < ficheConcours.getParametre().getNbCible() 
					&& ficheConcours.getConcurrentList().list(index, ficheConcours.getCurrentDepart()).length == 0)
				index++;
			if(index < ficheConcours.getParametre().getNbCible()) {
				openResultatDialog();
				jepClassIndiv.setText(ficheConcours.getClassement());
			} else {
				JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.target.empty")); //$NON-NLS-1$
			}
		} else if(source instanceof JCheckBox) {
			for(Criterion criterion : ficheConcours.getParametre().getReglement().getListCriteria()) {
				criterion.setClassement(classmentCriteriaCB.get(criterion.getCode()).isSelected());
			}

			jepClassIndiv.setText(ficheConcours.getClassement());
		} else if(source instanceof JComboBox) {
			ficheConcours.setCurrentDepart(((JComboBox)source).getSelectedIndex());
			cl.show(fichesDepart, "depart." + ficheConcours.getCurrentDepart()); //$NON-NLS-1$
		}
	}
	/**
	 * Gestion des changements d'onglet
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if(i == 2) {
				Concurrent[] listConcurrents = ficheConcours.getConcurrentList().list(ficheConcours.getCurrentDepart());
				for(Concurrent concurrent : listConcurrents) {
					if(concurrent.getCible() == 0) {
						JOptionPane.showMessageDialog(this,
								ApplicationCore.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
								ApplicationCore.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
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
			Concurrent[] listConcurrents = ficheConcours.getConcurrentList().list(ficheConcours.getCurrentDepart());
			for(Concurrent concurrent : listConcurrents) {
				if(concurrent.getCible() == 0) {
					JOptionPane.showMessageDialog(this,
							ApplicationCore.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
							ApplicationCore.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
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
				openConcurrentDialog(ficheConcours.getConcurrentList().getConcurrentAt(ficheConcours.getCurrentDepart(), Integer.parseInt(e.getURL().getRef().substring(1)), 
						Integer.parseInt(e.getURL().getRef().substring(0,1))));

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
}