/*
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
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;

import org.concoursjeunes.*;
import org.concoursjeunes.event.AutoCompleteDocumentEvent;
import org.concoursjeunes.event.AutoCompleteDocumentListener;
import org.concoursjeunes.ui.ConcoursJeunesFrame;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.common.ArraysUtils;
import ajinteractive.standard.java2.GridbagComposer;
import ajinteractive.standard.java2.NumberDocument;

/**
 * Boite de dialogue de gestion d'un concurrent
 * 
 * @author Aurelien Jeoffray
 * @version 5.0
 */
public class ConcurrentDialog extends JDialog implements ActionListener, FocusListener, AutoCompleteDocumentListener {

	public static final int CONFIRM_AND_CLOSE = 1;
	public static final int CONFIRM_AND_NEXT = 2;
	public static final int CONFIRM_AND_PREVIOUS = 3;
	public static final int CANCEL = 4;

	private final FicheConcours ficheConcours;
	private Concurrent concurrent;
	private Entite entiteConcurrent;
	private Archer filter = null;
	
	private static volatile int nbInstance = 0;
	private static Future<ConcurrentListDialog> concurrentListDialog;

	private JLabel jlDescription = new JLabel(); // Description
	private JLabel jlNom = new JLabel(); // Nom et prénom du Tireur
	private JLabel jlLicence = new JLabel(); // N° de Licence
	private JLabel jlSurclassment = new JLabel("<html>&nbsp;</html>"); //Archer surclassé? //$NON-NLS-1$
	private JLabel jlClub = new JLabel(); // nom du club
	private JLabel jlAgrement = new JLabel(); // n°agrement du club
	private JLabel jlCible = new JLabel(); // cible attribué
	private JLabel jlDixNeufM = new JLabel(); // Nb de 10/9/M
	

	// Tireur
	private JPanel jpConcurrent = new JPanel();
	private JTextField jtfNom = new JTextField(8); // Nom du tireur
	private JTextField jtfPrenom = new JTextField(8); // Prenom du tireur
	private JButton jbSelectionArcher = new JButton();
	private JButton jbEditerArcher = new JButton();
	private JTextField jtfLicence = new JTextField(16);// Numero de
	private JCheckBox jcbHandicape = new JCheckBox();
	// licence
	private Hashtable<Criterion, JLabel> jlCategrieTable = new Hashtable<Criterion, JLabel>();
	private Hashtable<Criterion, JComboBox> jcbCategorieTable = new Hashtable<Criterion, JComboBox>();

	// Club du tireur
	private final JPanel jpClub = new JPanel();
	private final JTextField jtfClub = new JTextField(16);// Intitulé du club
	private final JTextField jtfAgrement = new JTextField(16);// Numero
	// d'Agrément
	private final JButton jbDetailClub = new JButton();
	private final JButton jbListeClub = new JButton();
	private final JPanel jpCible = new JPanel();

	// Point du tireur
	private final JPanel jpPoints = new JPanel();
	private final JLabel jlValCible = new JLabel();
	private final JLabel jlPoints = new JLabel();
	private JTextField tfpd[];
	private final JTextField tfpd10 = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	private final JTextField tfpdNeuf = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	private final JTextField tfpdM = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$

	// inscription
	private final JPanel jpInscription = new JPanel();
	private final JComboBox jcbInscription = new JComboBox();

	// place libre
	private final JPanel jpPlaceLibre = new JPanel();
	private final JLabel jlPlaceLibre = new JLabel("<html></html>"); //$NON-NLS-1$

	private final JPanel jpActionPane = new JPanel();
	private final JButton jbValider = new JButton();
	private final JButton jbPrecedent = new JButton();
	private final JButton jbSuivant = new JButton();
	private final JButton jbAnnuler = new JButton();

	private int selectField = 0;

	private int returnVal = CANCEL;
	
	private boolean unlock = false;

	/**
	 * Création de la boite de dialogue de gestion de concurrent
	 * 
	 * @param concoursJeunesFrame -
	 *            la fenetre parentes dont dépend la boite de dialogue 
	 * @param ficheConcours la fiche concours à laquelle est/doit être rattaché le concurrent
	 */
	public ConcurrentDialog(ConcoursJeunesFrame concoursJeunesFrame, FicheConcours ficheConcours) {
		super(concoursJeunesFrame, "", true); //$NON-NLS-1$

		this.ficheConcours = ficheConcours;
		nbInstance++;
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		concurrentListDialog = executorService.submit(new Callable<ConcurrentListDialog>() {
			public ConcurrentListDialog call() {
				return new ConcurrentListDialog(ConcurrentDialog.this, ConcurrentDialog.this.ficheConcours.getParametre().getReglement(), null);
			}
		});
		init();
		affectLibelle();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (selectField >= 0) {
					tfpd[selectField].requestFocus(true);
					tfpd[selectField].moveCaretPosition(0);
				} else {
					tfpd10.requestFocus(true);
					tfpd10.moveCaretPosition(0);
				}
			}
		});
	}

	/**
	 * initialise la boite de dialogue concurrent pour un nouveau concurrent ou en édition
	 */
	private void init() {
		// Layout Manager
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.put(key, new JLabel());
			JComboBox jcbCriterion = new JComboBox();
			jcbCriterion.setEditable(false);
			jcbCriterion.setActionCommand("criterion_change_" + key.getCode()); //$NON-NLS-1$
			jcbCriterion.addActionListener(this);
			jcbCategorieTable.put(key, jcbCriterion);
		}

		jbSelectionArcher.addActionListener(this);
		jbSelectionArcher.setMargin(new Insets(0, 0, 0, 0));
		jbEditerArcher.addActionListener(this);
		jbEditerArcher.setMargin(new Insets(0, 0, 0, 0));
		jbValider.addActionListener(this);
		jbPrecedent.addActionListener(this);
		jbSuivant.addActionListener(this);
		jbAnnuler.addActionListener(this);
		jbDetailClub.addActionListener(this);
		jbDetailClub.setMargin(new Insets(0, 0, 0, 0));
		jbDetailClub.setText("+"); //$NON-NLS-1$
		jbListeClub.addActionListener(this);
		jbListeClub.setMargin(new Insets(0, 0, 0, 0));
		jbListeClub.setText("..."); //$NON-NLS-1$
		jtfNom.addFocusListener(this);
		jtfPrenom.addFocusListener(this);

		// Panneau de champs
		jlDescription.setOpaque(true);
		jlDescription.setPreferredSize(new Dimension(250, 70));
		jpConcurrent.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpClub.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpCible.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpPoints.add(jlPoints);

		// panneau validation inscription
		jpInscription.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpInscription.add(jcbInscription);

		jpPlaceLibre.setLayout(new BorderLayout());
		jpPlaceLibre.setPreferredSize(new Dimension(200, 100));
		jpPlaceLibre.setBorder(new TitledBorder("")); //$NON-NLS-1$

		jpPlaceLibre.add(new JScrollPane(jlPlaceLibre), BorderLayout.CENTER);

		jpActionPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		tfpd = new JTextField[ficheConcours.getParametre().getReglement().getNbSerie()];
		for (int i = 0; i < ficheConcours.getParametre().getReglement().getNbSerie(); i++) {
			this.tfpd[i] = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
			this.tfpd[i].addFocusListener(this);
			jpPoints.add(this.tfpd[i]);
		}

		this.tfpd10.addFocusListener(this);
		this.tfpdNeuf.addFocusListener(this);
		this.tfpdM.addFocusListener(this);

		// panneau tireur
		gridbagComposer.setParentPanel(jpConcurrent);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlNom, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNom, c);
		gridbagComposer.addComponentIntoGrid(jtfPrenom, c);
		gridbagComposer.addComponentIntoGrid(jbSelectionArcher, c);
		gridbagComposer.addComponentIntoGrid(jbEditerArcher, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlLicence, c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jtfLicence, c);
		c.gridy++;
		c.gridwidth = 5;
		gridbagComposer.addComponentIntoGrid(jcbHandicape, c);
		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			c.gridy++;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 1;
			gridbagComposer.addComponentIntoGrid(jlCategrieTable.get(key), c);
			c.gridwidth = 4;
			gridbagComposer.addComponentIntoGrid(jcbCategorieTable.get(key), c);
		}
		c.gridy++;
		c.gridwidth = 5;
		gridbagComposer.addComponentIntoGrid(jlSurclassment, c);

		// paneau club
		gridbagComposer.setParentPanel(jpClub);
		c.gridy = 0; // Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlClub, c);
		gridbagComposer.addComponentIntoGrid(jtfClub, c);
		gridbagComposer.addComponentIntoGrid(jbDetailClub, c);
		gridbagComposer.addComponentIntoGrid(jbListeClub, c);
		c.gridy++; // Défaut,Ligne 2
		gridbagComposer.addComponentIntoGrid(jlAgrement, c);
		gridbagComposer.addComponentIntoGrid(jtfAgrement, c);

		// panneau cible
		gridbagComposer.setParentPanel(jpCible);
		c.gridy = 0;
		c.gridwidth = 1; // Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlCible, c);
		gridbagComposer.addComponentIntoGrid(jlValCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jpPoints, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDixNeufM, c);
		gridbagComposer.addComponentIntoGrid(tfpd10, c);
		gridbagComposer.addComponentIntoGrid(tfpdNeuf, c);
		gridbagComposer.addComponentIntoGrid(tfpdM, c);

		// panneau action
		jpActionPane.add(jbValider);
		jpActionPane.add(jbPrecedent);
		jpActionPane.add(jbSuivant);
		jpActionPane.add(jbAnnuler);

		// panneau global
		getContentPane().setLayout(gridbag);
		c.gridy = 0; // Défaut,Haut
		c.gridwidth = 2;
		gridbag.setConstraints(jlDescription, c);
		getContentPane().add(jlDescription);
		c.gridy++;
		c.gridwidth = 1;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		gridbag.setConstraints(jpConcurrent, c);
		getContentPane().add(jpConcurrent, c);
		c.gridy += c.gridheight;
		c.gridheight = 1;
		gridbag.setConstraints(jpClub, c);
		getContentPane().add(jpClub, c);
		c.gridy++;
		gridbag.setConstraints(jpCible, c);
		getContentPane().add(jpCible, c);
		c.gridy++;
		c.gridwidth = 2;
		gridbag.setConstraints(jpActionPane, c);
		getContentPane().add(jpActionPane, c);

		c.gridy = 1;
		c.gridwidth = 1;
		gridbag.setConstraints(jpInscription, c);
		getContentPane().add(jpInscription);
		c.gridy++;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(jpPlaceLibre, c);
		getContentPane().add(jpPlaceLibre);

		getRootPane().setDefaultButton(jbSuivant);
	}

	/**
	 * affecte les libellé localisé à l'interface
	 */
	private void affectLibelle() {
		setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.titre.edition")); //$NON-NLS-1$

		((TitledBorder) jpConcurrent.getBorder()).setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.panel.tireur")); //$NON-NLS-1$
		((TitledBorder) jpClub.getBorder()).setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.panel.club")); //$NON-NLS-1$
		((TitledBorder) jpCible.getBorder()).setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.panel.cible")); //$NON-NLS-1$
		((TitledBorder) jpInscription.getBorder()).setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.inscription.titre")); //$NON-NLS-1$
		((TitledBorder) jpPlaceLibre.getBorder()).setTitle(ApplicationCore.ajrLibelle.getResourceString("concurrent.placelibre.titre")); //$NON-NLS-1$

		jlDescription.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.description")); // Description //$NON-NLS-1$
		jlDescription.setBackground(new Color(255, 255, 225));
		jlNom.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.identite")); // Nom et prénom du Tireur //$NON-NLS-1$
		jlLicence.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.numlicence")); // N° de Licence //$NON-NLS-1$
		jcbHandicape.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.handicap")); // Archer handicapé? //$NON-NLS-1$
		jlClub.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.nomclub")); // nom du club //$NON-NLS-1$
		jlAgrement.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.agrementclub")); // n°agrement du club //$NON-NLS-1$
		jlCible.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.cible")); // cible attribué //$NON-NLS-1$
		jlPoints.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.points")); //$NON-NLS-1$
		jlDixNeufM.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.dix")); //$NON-NLS-1$

		jbSelectionArcher.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.selectionarcher")); //$NON-NLS-1$
		jbEditerArcher.setToolTipText(ApplicationCore.ajrLibelle.getResourceString("bouton.editer")); //$NON-NLS-1$
		jbValider.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbPrecedent.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.precedent")); //$NON-NLS-1$
		jbSuivant.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.suivant")); //$NON-NLS-1$
		jbAnnuler.setText(ApplicationCore.ajrLibelle.getResourceString("bouton.annuler")); //$NON-NLS-1$

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.get(key).setText(key.getLibelle());
			jcbCategorieTable.get(key).removeAllItems();
			for (CriterionElement element : key.getCriterionElements()) {
				if (element.isActive())
					jcbCategorieTable.get(key).addItem(element);
			}
		}

		String[] lInscription = AJToolKit.tokenize(ApplicationCore.ajrLibelle.getResourceString("concurrent.inscription"), ","); //$NON-NLS-1$ //$NON-NLS-2$
		jcbInscription.removeAllItems();
		for (String status : lInscription) {
			jcbInscription.addItem(status);
		}
	}

	/**
	 * remplit les champs de la boite de dialogue avec le modèle sous jacent
	 */
	private void completeConcurrentDialog() {
		boolean isinit = concurrent.getInscription() != Concurrent.UNINIT && !unlock;

		jbSelectionArcher.setEnabled(!isinit);
		jbEditerArcher.setEnabled(isinit);
		if (isinit) {
			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.lock") //$NON-NLS-1$
			));
		} else {
			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.open") //$NON-NLS-1$
			));
		}

		jtfNom.setEditable(!isinit);
		jtfPrenom.setEditable(!isinit);
		jtfLicence.setEditable(!isinit);
		jcbHandicape.setEnabled(!isinit);

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jcbCategorieTable.get(key).setEnabled(!isinit);
		}

		jtfClub.setEditable(!isinit);
		jtfAgrement.setEditable(!isinit);

		jbPrecedent.setEnabled(isinit);
		/*jbSuivant.setText(isinit ? ConcoursJeunes.ajrLibelle.getResourceString("bouton.validersuivant") //$NON-NLS-1$
				: ConcoursJeunes.ajrLibelle.getResourceString("bouton.validernouveau")); */

		jlPlaceLibre.setText(showPlacesLibre());

		if (jtfNom.getDocument() instanceof AutoCompleteDocument) {
			((AutoCompleteDocument) jtfNom.getDocument()).setText(concurrent.getNomArcher());
			((AutoCompleteDocument) jtfPrenom.getDocument()).setText(concurrent.getPrenomArcher());
			((AutoCompleteDocument) jtfLicence.getDocument()).setText(concurrent.getNumLicenceArcher());

		} else {
			jtfNom.setText(concurrent.getNomArcher());
			jtfPrenom.setText(concurrent.getPrenomArcher());
			jtfLicence.setText(concurrent.getNumLicenceArcher());
		}
		jcbHandicape.setSelected(concurrent.isHandicape());
		if (jtfClub.getDocument() instanceof AutoCompleteDocument) {
			((AutoCompleteDocument) jtfClub.getDocument()).setText(entiteConcurrent.getVille());
			((AutoCompleteDocument) jtfAgrement.getDocument()).setText(entiteConcurrent.getAgrement());
		} else {
			jtfClub.setText(entiteConcurrent.getVille());
			jtfAgrement.setText(entiteConcurrent.getAgrement());
		}
		if (concurrent.getCriteriaSet() != null) {
			for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
				CriterionElement element = concurrent.getCriteriaSet().getCriterionElement(key);
				if(element != null)
					jcbCategorieTable.get(key).setSelectedItem(element);
				else
					jcbCategorieTable.get(key).setSelectedIndex(0);
			}
		}
		if(concurrent.isSurclassement())
			jlSurclassment.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.surclassement")); //$NON-NLS-1$
		else
			jlSurclassment.setText("<html>&nbsp;</html>"); //$NON-NLS-1$

		jlValCible.setText(new TargetPosition(concurrent.getCible(), concurrent.getPosition()).toString());

		List<Integer> score = concurrent.getScore();
		if (score.size() > 0) {
			for (int i = 0; i < score.size(); i++) {
				tfpd[i].setText(score.get(i) + ""); //$NON-NLS-1$
			}
		} else {
			for (int i = 0; i < tfpd.length; i++) {
				tfpd[i].setText("0"); //$NON-NLS-1$
			}
		}

		tfpd10.setText("" + concurrent.getDix()); //$NON-NLS-1$
		tfpdNeuf.setText("" + concurrent.getNeuf()); //$NON-NLS-1$
		tfpdM.setText("" + concurrent.getManque()); //$NON-NLS-1$

		if (concurrent.getInscription() == Concurrent.UNINIT)
			this.jcbInscription.setSelectedIndex(0);
		else
			this.jcbInscription.setSelectedIndex(concurrent.getInscription());
	}

	/**
	 * Affiche la boite de dialogue de création d'un concurrent
	 * 
	 * @param depart -
	 *            le depart affecté au concurrent
	 */
	public int showNewConcurrentDialog(int depart) {

		Concurrent concurrent = new Concurrent();
		concurrent.setDepart(depart);

		AutoCompleteDocument acdNom = new AutoCompleteDocument(jtfNom, AutoCompleteDocument.SearchType.NAME_SEARCH, ficheConcours.getParametre().getReglement());
		acdNom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdPrenom = new AutoCompleteDocument(jtfPrenom, AutoCompleteDocument.SearchType.FIRSTNAME_SEARCH, ficheConcours.getParametre().getReglement());
		acdPrenom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdLicence = new AutoCompleteDocument(jtfLicence, AutoCompleteDocument.SearchType.NUMLICENCE_SEARCH, ficheConcours.getParametre().getReglement());
		acdLicence.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.SearchType.CLUB_SEARCH, ficheConcours.getParametre().getReglement());
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.SearchType.AGREMENT_SEARCH, ficheConcours.getParametre().getReglement());
		acdAgrement.addAutoCompleteDocumentListener(this);

		jtfNom.setDocument(acdNom);
		jtfLicence.setDocument(acdLicence);
		jtfPrenom.setDocument(acdPrenom);
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);

		setConcurrent(concurrent);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		return returnVal;
	}

	/**
	 * Affiche la boite de dialogue de gestion du concurrent donné en parametre
	 * 
	 * @param concurrent -
	 *            la concurrent à afficher
	 * @return le code de retour de la boite de dialogue
	 */
	public int showConcurrentDialog(Concurrent concurrent) {

		jlDescription.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.description")); //$NON-NLS-1$
		jlDescription.setBackground(new Color(255, 255, 225));

		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.SearchType.CLUB_SEARCH, ficheConcours.getParametre().getReglement());
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.SearchType.AGREMENT_SEARCH, ficheConcours.getParametre().getReglement());
		acdAgrement.addAutoCompleteDocumentListener(this);

		jtfNom.setDocument(new PlainDocument());
		jtfLicence.setDocument(new PlainDocument());
		jtfPrenom.setDocument(new PlainDocument());
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);

		setConcurrent(concurrent);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		return returnVal;
	}

	/**
	 * Affichage des info d'un concurrent en mode edition
	 * 
	 * @param concurrent -
	 *            le concurrent à editer
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
		this.entiteConcurrent = concurrent.getClub();

		completeConcurrentDialog();
	}

	/**
	 * Retourne le concurrent de la boite de dialogue
	 * 
	 * @return le concurrent courrant
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * formate l'affichage des places libre en fonction des catégorie de classement
	 * 
	 * @return String l'affichage ecran des places libres
	 */
	private String showPlacesLibre() {
		String strPlaceLibre = "<html>"; //$NON-NLS-1$

		// affiche le nombre de concurrent total sur le pas de tir
		strPlaceLibre += ApplicationCore.ajrLibelle.getResourceString("concurrent.placelibre.nbarcher") + //$NON-NLS-1$
				": " + ficheConcours.getConcurrentList().countArcher() + "<br><br>"; //$NON-NLS-1$ //$NON-NLS-2$

		// recupere la table d'occupation des cibles
		Hashtable<DistancesEtBlason, TargetsOccupation> occupationCibles = ficheConcours.getPasDeTir(concurrent.getDepart()).getTargetsOccupation(ficheConcours.getParametre().getNbTireur());

		List<DistancesEtBlason> tableCorresp = ficheConcours.getParametre().getReglement().getListDistancesEtBlason();

		// en extrait les jeux de critères de placement
		CriteriaSet[] criteriaSetPlacement = new CriteriaSet[tableCorresp.size()];
		for (int i = 0; i < tableCorresp.size(); i++) {
			criteriaSetPlacement[i] = tableCorresp.get(i).getCriteriaSet();
		}

		// ordonne ces critères selon l'ordre définit dans la configuration
		CriteriaSet.sortCriteriaSet(criteriaSetPlacement, ficheConcours.getParametre().getReglement().getListCriteria());

		// boucle sur chacun des jeux de placement
		for (CriteriaSet differentiationCriteria : criteriaSetPlacement) {
			// etablit la correspondance entre un jeux de placement et son d/b
			DistancesEtBlason distAndBlas = ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(differentiationCriteria);

			// genere le libellé complet du jeux de critère
			CriteriaSetLibelle libelle = new CriteriaSetLibelle(differentiationCriteria);
			String strCategoriePlacement = libelle.toString();

			strPlaceLibre += "<i>" + //$NON-NLS-1$
					strCategoriePlacement + "(" + //$NON-NLS-1$
					distAndBlas.getDistance()[0] + "m/" + //$NON-NLS-1$
					distAndBlas.getTargetFace().getName() + ")</i><br>\n"; //$NON-NLS-1$
			strPlaceLibre += "&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">" + //$NON-NLS-1$
					ApplicationCore.ajrLibelle.getResourceString("concurrent.placelibre.occupee") + //$NON-NLS-1$
					" " + occupationCibles.get(distAndBlas).getPlaceOccupe() + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
			strPlaceLibre += ", <font color=\"green\">" + //$NON-NLS-1$
					ApplicationCore.ajrLibelle.getResourceString("concurrent.placelibre.libre") + //$NON-NLS-1$
					" " + occupationCibles.get(distAndBlas).getPlaceLibre() + "</font><br>"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return strPlaceLibre;
	}

	private ArrayList<Integer> readScores() throws NumberFormatException {
		ArrayList<Integer> points = new ArrayList<Integer>();
		try {
			for (int i = 0; i < tfpd.length; i++) {
				if (points.size() > i)
					points.set(i, Integer.parseInt(tfpd[i].getText()));
				else
					points.add(Integer.parseInt(tfpd[i].getText()));
			}
		} catch(NumberFormatException e) {
			throw e;
		}

		return points;
	}

	private CriteriaSet readCriteriaSet() {
		CriteriaSet differentiationCriteria = new CriteriaSet();
		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			CriterionElement criterionElement = (CriterionElement)jcbCategorieTable.get(key).getSelectedItem();
			differentiationCriteria.setCriterionElement(key, criterionElement);
		}

		return differentiationCriteria;
	}

	/**
	 * @see org.concoursjeunes.event.AutoCompleteDocumentListener#concurrentFinded(org.concoursjeunes.event.AutoCompleteDocumentEvent)
	 */
	public void concurrentFinded(AutoCompleteDocumentEvent e) {
		Concurrent findConcurrent = e.getConcurrent();
		if (!findConcurrent.equals(concurrent)) {
			findConcurrent.setDepart(ficheConcours.getCurrentDepart());
			setConcurrent(findConcurrent);
		}
		
		if (concurrent.haveHomonyme()) {
			jlDescription.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.homonyme")); //$NON-NLS-1$
			jlDescription.setBackground(Color.ORANGE);
		} else {
			jlDescription.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.description")); //$NON-NLS-1$
			jlDescription.setBackground(new Color(255, 255, 225));
		}

		filter = e.getGenericArcher();
	}

	/**
	 * @see org.concoursjeunes.event.AutoCompleteDocumentListener#concurrentNotFound(org.concoursjeunes.event.AutoCompleteDocumentEvent)
	 */
	public void concurrentNotFound(AutoCompleteDocumentEvent e) {
		Concurrent newConcurrent = new Concurrent();
		newConcurrent.setDepart(ficheConcours.getCurrentDepart());
		if (e.getSource() == jtfNom) {
			newConcurrent.setNomArcher(jtfNom.getText());
		} else if (e.getSource() == jtfPrenom) {
			newConcurrent.setNomArcher(jtfNom.getText());
			newConcurrent.setPrenomArcher(jtfPrenom.getText());
		} else if (e.getSource() == jtfLicence) {
			newConcurrent.setNomArcher(jtfNom.getText());
			newConcurrent.setPrenomArcher(jtfPrenom.getText());
			newConcurrent.setNumLicenceArcher(jtfLicence.getText());
		}

		filter = null;

		setConcurrent(newConcurrent);

		jlDescription.setText(ApplicationCore.ajrLibelle.getResourceString("concurrent.noconcurrent")); //$NON-NLS-1$
		jlDescription.setBackground(Color.ORANGE);
	}

	public void entiteFinded(AutoCompleteDocumentEvent e) {
		Entite findEntite = e.getEntite();
		if (!findEntite.equals(concurrent.getClub())) {
			//concurrent.setClub(findEntite);
			//setConcurrent(concurrent);
			entiteConcurrent = findEntite;
			completeConcurrentDialog();
		}
	}

	public void entiteNotFound(AutoCompleteDocumentEvent e) {
		Entite newEntite = new Entite();
		if (e.getSource() == jtfClub) {
			newEntite.setVille(jtfClub.getText());
		} else if (e.getSource() == jtfAgrement) {
			newEntite.setVille(jtfClub.getText());
			newEntite.setAgrement(jtfAgrement.getText());
		}

		//concurrent.setClub(newEntite);
		//setConcurrent(concurrent);
		entiteConcurrent = newEntite;
		completeConcurrentDialog();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == jbSuivant || ae.getSource() == jbPrecedent || ae.getSource() == jbValider) {

			filter = null;
			
			//evite de modifier l'objet concurrent avant d'avoir
			//validé les paramêtres
			Concurrent tempConcurrent = concurrent.clone();
			DistancesEtBlason db1 = null;
			if(tempConcurrent.getCriteriaSet() != null)
				db1 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), tempConcurrent);
			
			// fixe le jeux de critères definissant le concurrent
			tempConcurrent.setCriteriaSet(readCriteriaSet());
			DistancesEtBlason db2 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), tempConcurrent);
			
			tempConcurrent.setHandicape(jcbHandicape.isSelected());

			if (tempConcurrent.getInscription() == Concurrent.UNINIT) {
				// si il n'y a plus de place alors retourner une erreur
				if (!ficheConcours.getPasDeTir(tempConcurrent.getDepart()).havePlaceForConcurrent(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("erreur.maxcible"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("erreur.maxcible.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
					// si le concurrent existe déjà alors retourner une
					// erreur
				} else if (ficheConcours.getConcurrentList().contains(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("erreur.alreadyexist"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("erreur.alreadyexist.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			} else {
				if (!ficheConcours.getPasDeTir(tempConcurrent.getDepart()).havePlaceForConcurrent(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("erreur.maxcible"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("erreur.maxcible.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			}
			
			try {
				// verification du score
				if (!ficheConcours.getParametre().getReglement().isValidScore(readScores())) {
					JOptionPane.showMessageDialog(new JDialog(), ApplicationCore.ajrLibelle.getResourceString("erreur.impscore"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("erreur"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			
				concurrent.setScore(readScores());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(db1 != null && !db1.equals(db2)) {
				ficheConcours.getPasDeTir(tempConcurrent.getDepart()).retraitConcurrent(concurrent);
			}
			
			concurrent.setCriteriaSet(tempConcurrent.getCriteriaSet());
			concurrent.setHandicape(tempConcurrent.isHandicape());
			try {
				concurrent.setDix(Integer.parseInt(tfpd10.getText()));
				concurrent.setNeuf(Integer.parseInt(tfpdNeuf.getText()));
				concurrent.setManque(Integer.parseInt(tfpdM.getText()));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						ApplicationCore.ajrLibelle.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			concurrent.setNomArcher(jtfNom.getText());
			concurrent.setPrenomArcher(jtfPrenom.getText());
			concurrent.setNumLicenceArcher(jtfLicence.getText());
			concurrent.setClub(entiteConcurrent);
			concurrent.getClub().setVille(jtfClub.getText());
			concurrent.getClub().setAgrement(jtfAgrement.getText());
			concurrent.setInscription(jcbInscription.getSelectedIndex());

			concurrent.saveCriteriaSet(ficheConcours.getParametre().getReglement());

			if (ae.getSource() == jbValider) {
				if (!this.jtfNom.getText().equals("")) { //$NON-NLS-1$
					returnVal = CONFIRM_AND_CLOSE;
				}
			} else if (ae.getSource() == jbSuivant) {
				returnVal = CONFIRM_AND_NEXT;
			} else if (ae.getSource() == jbPrecedent) {
				returnVal = CONFIRM_AND_PREVIOUS;
			}
			unlock = false;
			setVisible(false);
		} else if (ae.getSource() == jbAnnuler) {
			returnVal = CANCEL;
			unlock = false;
			setVisible(false);
		} else if (ae.getSource() == jbSelectionArcher) {
			//Le chargement de la liste des concurrents etant asynchrone, on doit attendre que celle ci
			// soit chargé avant de l'afficher. On place un timeout de 30s pour ne pas bloqué définitivement
			// l'interface en cas d'echec de chargement ou avoir un delai d'attente trop long sur certain système
            try {
            	ConcurrentListDialog cld = concurrentListDialog.get(30, TimeUnit.SECONDS);
	            
	            cld.setFilter(filter);
				cld.setVisible(true);
				if (cld.isValider()) {
					cld.initConcurrent(concurrent);
					setConcurrent(concurrent);
				}
            } catch (InterruptedException e) {
            	JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
            			null, null, e, Level.SEVERE, null));
	            e.printStackTrace();
            } catch (ExecutionException e) {
            	JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
            			null, null, e, Level.SEVERE, null));
	            e.printStackTrace();
            } catch (TimeoutException e) {
            	JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("concurrent.info.listing.wait")); //$NON-NLS-1$
            }
		} else if (ae.getSource() == jbDetailClub) {
			if (!jtfAgrement.getText().equals("")) { //$NON-NLS-1$
				EntiteDialog ed = new EntiteDialog(this);
				ed.showEntite(concurrent.getClub());
			}
		} else if (ae.getSource() == jbListeClub) {
			EntiteListDialog entiteListDialog = new EntiteListDialog(null);
			if (entiteListDialog.getAction() == EntiteListDialog.VALIDER) {
				entiteListDialog.setAction(EntiteListDialog.ANNULER);
				jtfClub.setText(entiteListDialog.getSelectedEntite().getVille());
				jtfAgrement.setText(entiteListDialog.getSelectedEntite().getAgrement());
			}
		} else if (ae.getSource() == jbEditerArcher) {
			unlock = true;
			
			jtfNom.setEditable(true);
			jtfPrenom.setEditable(true);
			jtfLicence.setEditable(true);
			jcbHandicape.setEnabled(true);

			for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria())
				jcbCategorieTable.get(key).setEnabled(true);

			jtfClub.setEditable(true);
			jtfAgrement.setEditable(true);

			jbEditerArcher.setEnabled(false);

			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.ajrParametreAppli.getResourceString("file.icon.open") //$NON-NLS-1$
			));

		} else if (ae.getSource() instanceof JComboBox && ae.getActionCommand().startsWith("criterion_change")) { //$NON-NLS-1$
			if(!ArraysUtils.contains(ficheConcours.getParametre().getReglement().getValidClassementCriteriaSet(), readCriteriaSet())) {
				JOptionPane.showMessageDialog(this, 
						ApplicationCore.ajrLibelle.getResourceString("concurrent.invalidcriteriaset"), //$NON-NLS-1$
						ApplicationCore.ajrLibelle.getResourceString("concurrent.invalidcriteriaset.title"), //$NON-NLS-1$
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent fe) {
		for (int i = 0; i < tfpd.length; i++) {
			if (fe.getSource() == tfpd[i]) {
				selectField = i;
				break;
			}
		}
		if (fe.getSource() == tfpd10 || fe.getSource() == tfpdNeuf || fe.getSource() == tfpdM) {
			selectField = -1;
		}
		if (fe.getSource() instanceof JTextField) {
			((JTextField) fe.getSource()).setSelectionStart(0);
			((JTextField) fe.getSource()).setSelectionEnd(((JTextField) fe.getSource()).getText().length());
		}
	}

	/**
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent fe) {
	}
	
	@Override
	public void dispose() {
		nbInstance--;
		if(nbInstance == 0) {
			
			try {
	            if(concurrentListDialog.isDone())
	            	concurrentListDialog.get().dispose();
	            else
	            	concurrentListDialog.cancel(true);
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
			concurrentListDialog = null;
		}
		super.dispose();
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
}