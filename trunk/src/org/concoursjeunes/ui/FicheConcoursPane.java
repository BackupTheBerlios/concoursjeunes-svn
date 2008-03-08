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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.ui.dialog.ConcurrentDialog;
import org.concoursjeunes.ui.dialog.ParametreDialog;
import org.concoursjeunes.ui.dialog.ResultatDialog;

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

	private JEditorPane jepClassIndiv	= new JEditorPane();					//panneau de classement
	private JEditorPane jepClassTeam	= new JEditorPane();
	private JEditorPane jepClassClub	= new JEditorPane();
	//bouton d'enregistrement
	private JButton jbResultat			= new JButton();
	private JLabel jlCritClassement		= new JLabel();
	//critere individuel
	private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();

	private JButton printClassementIndiv	= new JButton();
	private JButton printClassementEquipe	= new JButton();
	private JButton printClassementClub		= new JButton();

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
		printClassementIndiv.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementEquipe.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
		));
		printClassementClub.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print") //$NON-NLS-1$
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
		jtbClassement.addTab("onglet.classementindividuel", new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer")), //$NON-NLS-1$
				ficheI);
		jtbClassement.addTab("onglet.classementequipe",new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheE);
		jtbClassement.addTab("onglet.classementclub",new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheC);

		//panneau global
		tabbedpane.addChangeListener(this);
		tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedpane.addTab("onglet.gestionarcher", null, fichesDepart); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.addTab("onglet.pointage.greffe", new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.desktop")), //$NON-NLS-1$
				new GreffePane(this));
		tabbedpane.addTab("onglet.classement", //$NON-NLS-1$
				new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
						File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
						jtbClassement);
		tabbedpane.addTab("onglet.edition", new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.print")), //$NON-NLS-1$
		new JPanel());

		//integration
		setLayout(new BorderLayout());
		add(tabbedpane, BorderLayout.CENTER);
		//add(statusbar, BorderLayout.SOUTH);

		affectLibelle();
	}

	private JPanel getGestArchersTabComponent() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(tabbedpane.getTitleAt(0));
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(this);

		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++)
			comboBox.addItem(
					ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher.depart") + i); //$NON-NLS-1$
		//comboBox.addItem("---"); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(label, BorderLayout.CENTER);
		panel.add(comboBox, BorderLayout.EAST);
		return panel;
	}

	/**
	 * affecte les libelle localisé à l'interface
	 *
	 */
	private void affectLibelle() {
		jbResultat.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.saisieresultats")); //$NON-NLS-1$
		printClassementIndiv.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		printClassementEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		printClassementClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		jlCritClassement.setText(ConcoursJeunes.ajrLibelle.getResourceString("interface.critereclassement")); //$NON-NLS-1$
		tabbedpane.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher")); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("onglet.pointage.greffe")); //$NON-NLS-1$
		tabbedpane.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classement")); //$NON-NLS-1$
		tabbedpane.setTitleAt(3, ConcoursJeunes.ajrLibelle.getResourceString("onglet.edition")); //$NON-NLS-1$
		jtbClassement.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementindividuel")); //$NON-NLS-1$
		jtbClassement.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementequipe")); //$NON-NLS-1$
		jtbClassement.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementclub")); //$NON-NLS-1$
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
				jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
			} else {
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.target.empty")); //$NON-NLS-1$
			}
		} else if(source == printClassementIndiv) {
			if(!ficheConcours.printClassement())
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
		} else if(source == printClassementEquipe) {
			if(!ficheConcours.printClassementEquipe())
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
		} else if(source == printClassementClub) {
			if(!ficheConcours.printClassementClub())
				JOptionPane.showMessageDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
		} else if(source instanceof JCheckBox) {
			for(Criterion criterion : ficheConcours.getParametre().getReglement().getListCriteria()) {
				criterion.setClassement(classmentCriteriaCB.get(criterion.getCode()).isSelected());
			}

			jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
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
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						break;
					}
				}
				int index = jtbClassement.getSelectedIndex();
				switch(index) {
					case 0:
						jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
						break;
					case 1:
						jepClassTeam.setText(ficheConcours.getClassementEquipe(FicheConcours.OUT_HTML));
						break;
					case 2:
						jepClassClub.setText(ficheConcours.getClassementClub(FicheConcours.OUT_HTML));
				}
			}
		} else if(e.getSource() == jtbClassement) {
			Concurrent[] listConcurrents = ficheConcours.getConcurrentList().list(ficheConcours.getCurrentDepart());
			for(Concurrent concurrent : listConcurrents) {
				if(concurrent.getCible() == 0) {
					JOptionPane.showMessageDialog(this,
							ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
							ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					break;
				}
			}
			switch(jtbClassement.getSelectedIndex()) {
				case 0:
					jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
					break;
				case 1:
					jepClassTeam.setText(ficheConcours.getClassementEquipe(FicheConcours.OUT_HTML));
					break;
				case 2:
					jepClassClub.setText(ficheConcours.getClassementClub(FicheConcours.OUT_HTML));
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

				jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML));
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