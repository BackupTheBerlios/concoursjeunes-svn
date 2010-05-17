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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.StringUtils;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Archer;
import org.concoursjeunes.AutoCompleteDocument;
import org.concoursjeunes.AutoCompleteDocumentContext;
import org.concoursjeunes.Blason;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Entite;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.TargetPosition;
import org.concoursjeunes.TargetsOccupation;
import org.concoursjeunes.event.AutoCompleteDocumentEvent;
import org.concoursjeunes.event.AutoCompleteDocumentListener;
import org.concoursjeunes.localisable.CriteriaSetLibelle;
import org.concoursjeunes.ui.ConcoursJeunesFrame;

/**
 * Boite de dialogue de gestion d'un concurrent
 * 
 * @author Aurélien Jeoffray
 * @version 5.0
 */
@Localizable(textMethod="setTitle",value="concurrent.titre.edition")
public class ConcurrentDialog extends JDialog implements ActionListener, FocusListener, AutoCompleteDocumentListener, ItemListener {

	public static final int CONFIRM_AND_CLOSE = 1;
	public static final int CONFIRM_AND_NEXT = 2;
	public static final int CONFIRM_AND_PREVIOUS = 3;
	public static final int CANCEL = 4;

	private AjResourcesReader localisation;
	private Profile profile;
	private FicheConcours ficheConcours;
	private Concurrent concurrent;
	private Entite entiteConcurrent;
	private Archer filter = null;
	
	private static Reglement lastActiveReglement;
	private static Future<ConcurrentListDialog> concurrentListDialog;

	@Localizable("concurrent.description")
	private JLabel jlDescription = new JLabel(); // Description
	@Localizable("concurrent.identite")
	private JLabel jlNom = new JLabel(); // Nom et prénom du Tireur
	@Localizable("concurrent.numlicence")
	private JLabel jlLicence = new JLabel(); // N° de Licence
	@Localizable("concurrent.nomclub")
	private JLabel jlClub = new JLabel(); // nom du club
	@Localizable("concurrent.agrementclub")
	private JLabel jlAgrement = new JLabel(); // n°agrement du club
	@Localizable("concurrent.cible")
	private JLabel jlCible = new JLabel(); // cible attribué
	private JLabel jlDepartages = new JLabel(); // Nb de 10/9

	// Tireur
	private JPanel jpConcurrent = new JPanel();
	private JTextField jtfNom = new JTextField(8); // Nom du tireur
	private JTextField jtfPrenom = new JTextField(8); // Prenom du tireur
	@Localizable(value="",tooltip="bouton.selectionarcher")
	private JButton jbSelectionArcher = new JButton();
	@Localizable(value="",tooltip="bouton.editer")
	private JButton jbEditerArcher = new JButton();
	private JTextField jtfLicence = new JTextField(16);// Numero de
	@Localizable("concurrent.handicap")
	private JCheckBox jcbHandicape = new JCheckBox();
	@Localizable("concurrent.surclassement")
	private JCheckBox jcbSurclassement = new JCheckBox();
	private Map<Criterion, JLabel> jlCategrieTable = new HashMap<Criterion, JLabel>();
	private Map<Criterion, JComboBox> jcbCategorieTable = new HashMap<Criterion, JComboBox>();
	@Localizable("concurrent.blason")
	private JLabel jlBlason = new JLabel();
	private JComboBox jcbBlason = new JComboBox();

	// Club du tireur
	private JPanel jpClub = new JPanel();
	private JTextField jtfClub = new JTextField(16);// Intitulé du club
	private JTextField jtfAgrement = new JTextField(16);// Numero d'Agrément
	private JButton jbDetailClub = new JButton();
	private JButton jbListeClub = new JButton();
	private JPanel jpCible = new JPanel();

	// Point du tireur
	private JLabel jlValCible = new JLabel();
	@Localizable("concurrent.points")
	private JLabel jlPoints = new JLabel();
	private JTextField[] tfpd;
	private JTextField[] tfDepartages;

	// inscription
	private final JPanel jpInscription = new JPanel();
	private final JComboBox jcbInscription = new JComboBox();

	// place libre
	private final JPanel jpPlaceLibre = new JPanel();
	private final JLabel jlPlaceLibre = new JLabel("<html></html>"); //$NON-NLS-1$

	private final JPanel jpActionPane = new JPanel();
	@Localizable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localizable("bouton.precedent")
	private final JButton jbPrecedent = new JButton();
	@Localizable("bouton.suivant")
	private final JButton jbSuivant = new JButton();
	@Localizable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();

	private int selectField = 0;

	private int returnVal = CANCEL;
	
	private boolean unlock = false;

	/**
	 * Création de la boite de dialogue de gestion de concurrent
	 * 
	 * @param concoursJeunesFrame la fenêtre parentes dont dépend la boite de dialogue 
	 * @param ficheConcours la fiche concours à laquelle est/doit être rattaché le concurrent
	 */
	public ConcurrentDialog(ConcoursJeunesFrame concoursJeunesFrame, Profile profile, FicheConcours ficheConcours) {
		super(concoursJeunesFrame, "", true); //$NON-NLS-1$

		this.ficheConcours = ficheConcours;
		this.localisation = profile.getLocalisation();
		this.profile = profile;
		
		init();
		affectLibelle();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (selectField >= 0) {
					tfpd[selectField].requestFocus(true);
					tfpd[selectField].moveCaretPosition(0);
				} else if(tfDepartages != null && tfDepartages.length > 0){
					tfDepartages[0].requestFocus(true);
					tfDepartages[0].moveCaretPosition(0);
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
		
		JPanel jpPoints = new JPanel();
		JPanel jpDepartages = new JPanel();

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.put(key, new JLabel());
			JComboBox jcbCriterion = new JComboBox();
			jcbCriterion.setEditable(false);
			jcbCriterion.setActionCommand("criterion_change_" + key.getCode()); //$NON-NLS-1$
			//jcbCriterion.addActionListener(this);
			jcbCriterion.addItemListener(this);
			jcbCriterion.setRenderer(new DefaultListCellRenderer() {
				/* (non-Javadoc)
				 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
				 */
				@Override
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					if(value instanceof CriterionElement)
						value = ((CriterionElement)value).getLibelle();
					return super.getListCellRendererComponent(list, value, index, isSelected,
							cellHasFocus);
				}
			});
			jcbCategorieTable.put(key, jcbCriterion);
		}

		jbSelectionArcher.addActionListener(this);
		jbSelectionArcher.setMargin(new Insets(0, 0, 0, 0));
		jbSelectionArcher.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.select"))); //$NON-NLS-1$
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
		jbListeClub.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.select"))); //$NON-NLS-1$
		jtfNom.addFocusListener(this);
		jtfPrenom.addFocusListener(this);
		
		jcbBlason.setEnabled(false);

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
		jpPlaceLibre.setPreferredSize(new Dimension(300, 100));
		jpPlaceLibre.setBorder(new TitledBorder("")); //$NON-NLS-1$
		JScrollPane spPlaceLibre = new JScrollPane(jlPlaceLibre);
		spPlaceLibre.getVerticalScrollBar().setUnitIncrement(20);
		jpPlaceLibre.add(spPlaceLibre, BorderLayout.CENTER);

		jpActionPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		tfpd = new JTextField[ficheConcours.getParametre().getReglement().getNbSerie()];
		for (int i = 0; i < tfpd.length; i++) {
			tfpd[i] = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
			tfpd[i].addFocusListener(this);
			jpPoints.add(tfpd[i]);
		}

		String labelDep = ""; //$NON-NLS-1$
		for(String dep : ficheConcours.getParametre().getReglement().getTie())
			labelDep += dep + "/"; //$NON-NLS-1$
		if(!labelDep.isEmpty())
			labelDep = labelDep.substring(0, labelDep.length() - 1);
		jlDepartages.setText(labelDep + ":"); //$NON-NLS-1$
		jpDepartages.add(jlDepartages);
		tfDepartages = new JTextField[ficheConcours.getParametre().getReglement().getTie().size()];
		for(int i = 0; i < tfDepartages.length; i++) {
			tfDepartages[i] = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
			tfDepartages[i].addFocusListener(this);
			jpDepartages.add(tfDepartages[i]);
		}

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
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jcbSurclassement, c);
		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			c.gridy++;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 1;
			gridbagComposer.addComponentIntoGrid(jlCategrieTable.get(key), c);
			c.gridwidth = 4;
			gridbagComposer.addComponentIntoGrid(jcbCategorieTable.get(key), c);
		}
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlBlason, c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jcbBlason, c);

		// panneau club
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
		c.weightx = 1.0;
		gridbagComposer.addComponentIntoGrid(jlCible, c);
		gridbagComposer.addComponentIntoGrid(Box.createHorizontalGlue(), c);
		gridbagComposer.addComponentIntoGrid(jlValCible, c);
		gridbagComposer.addComponentIntoGrid(jpPoints, c);
		c.gridy++;
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jpDepartages, c);

		// panneau action
		jpActionPane.add(jbValider);
		jpActionPane.add(jbPrecedent);
		jpActionPane.add(jbSuivant);
		jpActionPane.add(jbAnnuler);

		// panneau global
		c= new GridBagConstraints();
		getContentPane().setLayout(gridbag);
		c.gridy = 0; // Défaut,Haut
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
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
		((TitledBorder) jpConcurrent.getBorder()).setTitle(localisation.getResourceString("concurrent.panel.tireur")); //$NON-NLS-1$
		((TitledBorder) jpClub.getBorder()).setTitle(localisation.getResourceString("concurrent.panel.club")); //$NON-NLS-1$
		((TitledBorder) jpCible.getBorder()).setTitle(localisation.getResourceString("concurrent.panel.cible")); //$NON-NLS-1$
		((TitledBorder) jpInscription.getBorder()).setTitle(localisation.getResourceString("concurrent.inscription.titre")); //$NON-NLS-1$
		((TitledBorder) jpPlaceLibre.getBorder()).setTitle(localisation.getResourceString("concurrent.placelibre.titre")); //$NON-NLS-1$
		
		Localizator.localize(this, localisation);


		jlDescription.setBackground(new Color(255, 255, 225));

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.get(key).setText(key.getLibelle());
			jcbCategorieTable.get(key).removeAllItems();
			jcbCategorieTable.get(key).removeItemListener(this);
			for (CriterionElement element : key.getCriterionElements()) {
				if (element.isActive())
					jcbCategorieTable.get(key).addItem(element);
			}
			jcbCategorieTable.get(key).addItemListener(this);
		}

		String[] lInscription = StringUtils.tokenize(localisation.getResourceString("concurrent.inscription"), ","); //$NON-NLS-1$ //$NON-NLS-2$
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
			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.lock") //$NON-NLS-1$
			));
		} else {
			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.open") //$NON-NLS-1$
			));
		}

		jtfNom.setEditable(!isinit);
		jtfPrenom.setEditable(!isinit);
		jtfLicence.setEditable(!isinit);
		jcbHandicape.setEnabled(!isinit);
		jcbSurclassement.setEnabled(!isinit);

		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jcbCategorieTable.get(key).setEnabled(!isinit);
		}

		jtfClub.setEditable(!isinit);
		jtfAgrement.setEditable(!isinit);

		jbPrecedent.setEnabled(isinit);
		jbSuivant.setEnabled(!isinit);
		/*jbSuivant.setText(isinit ? ConcoursJeunes.ajrLibelle.getResourceString("bouton.validersuivant") //$NON-NLS-1$
				: ConcoursJeunes.ajrLibelle.getResourceString("bouton.validernouveau")); */

		jlPlaceLibre.setText(showPlacesLibre());

		if (jtfNom.getDocument() instanceof AutoCompleteDocument) {
			((AutoCompleteDocument) jtfNom.getDocument()).setText(concurrent.getName());
			((AutoCompleteDocument) jtfPrenom.getDocument()).setText(concurrent.getFirstName());
			((AutoCompleteDocument) jtfLicence.getDocument()).setText(concurrent.getNumLicenceArcher());

		} else {
			jtfNom.setText(concurrent.getName());
			jtfPrenom.setText(concurrent.getFirstName());
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
		jcbSurclassement.setSelected(concurrent.isSurclassement());
		
		jcbBlason.removeAllItems();
		CriteriaSet cs = readCriteriaSet();
		List<DistancesEtBlason> tmpDB = ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(
				cs.getFilteredCriteriaSet(
						ficheConcours.getParametre().getReglement().getPlacementFilter()));
		for(DistancesEtBlason db : tmpDB) {
			jcbBlason.addItem(db.getTargetFace());
		}
		jcbBlason.setEnabled(!isinit && jcbBlason.getItemCount() > 1);
		if(concurrent.getCriteriaSet() != null)
			jcbBlason.setSelectedItem(DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), concurrent).getTargetFace());

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


		for(int i = 0; i < tfDepartages.length; i++) {
			if(i < concurrent.getDepartages().length)
				tfDepartages[i].setText("" + concurrent.getDepartages()[i]); //$NON-NLS-1$
			else
				tfDepartages[i].setText("0"); //$NON-NLS-1$
		}


		if (concurrent.getInscription() == Concurrent.UNINIT)
			this.jcbInscription.setSelectedIndex(0);
		else
			this.jcbInscription.setSelectedIndex(concurrent.getInscription());
	}
	
	private void initConcurrentListDialog() {
		if(concurrentListDialog == null || !ficheConcours.getParametre().getReglement().equals(lastActiveReglement)) {
			if(concurrentListDialog != null) {
				try {
		            if(concurrentListDialog.isDone())
		            	concurrentListDialog.get().dispose();
		            else
		            	concurrentListDialog.cancel(true);
		        } catch (InterruptedException e) {
		        } catch (ExecutionException e) {
		        }
			}
	        lastActiveReglement = ficheConcours.getParametre().getReglement();
	        
			ExecutorService executorService = Executors.newSingleThreadExecutor(new LowFactory());
			concurrentListDialog = executorService.submit(new Callable<ConcurrentListDialog>() {
				public ConcurrentListDialog call() {
					return new ConcurrentListDialog(ConcurrentDialog.this, profile,
							lastActiveReglement, null);
				}
			});
		}
	}

	/**
	 * Affiche la boite de dialogue de création d'un concurrent
	 * 
	 * @param depart le depart affecté au concurrent
	 */
	public int showNewConcurrentDialog(int depart) {
		
		initConcurrentListDialog();

		Concurrent concurrent = new Concurrent();
		concurrent.setDepart(depart);
		
		AutoCompleteDocumentContext context = new AutoCompleteDocumentContext(ficheConcours.getParametre().getReglement());

		AutoCompleteDocument acdNom = new AutoCompleteDocument(jtfNom, AutoCompleteDocument.SearchType.NAME_SEARCH, context);
		acdNom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdPrenom = new AutoCompleteDocument(jtfPrenom, AutoCompleteDocument.SearchType.FIRSTNAME_SEARCH, context);
		acdPrenom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdLicence = new AutoCompleteDocument(jtfLicence, AutoCompleteDocument.SearchType.NUMLICENCE_SEARCH, context);
		acdLicence.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.SearchType.CLUB_SEARCH, context);
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.SearchType.AGREMENT_SEARCH, context);
		acdAgrement.addAutoCompleteDocumentListener(this);

		jtfNom.setDocument(acdNom);
		jtfLicence.setDocument(acdLicence);
		jtfPrenom.setDocument(acdPrenom);
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);

		setConcurrent(concurrent);
		
		jlDescription.setText(localisation.getResourceString("concurrent.description")); //$NON-NLS-1$
		jlDescription.setBackground(new Color(255, 255, 225));
		
		returnVal = CANCEL;

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		return returnVal;
	}

	/**
	 * Affiche la boite de dialogue de gestion du concurrent donné en paramètre
	 * 
	 * @param concurrent la concurrent à afficher
	 * @return le code de retour de la boite de dialogue
	 */
	public int showConcurrentDialog(Concurrent concurrent, boolean hasPrevious, boolean hasNext) {
		jlDescription.setText(localisation.getResourceString("concurrent.description")); //$NON-NLS-1$
		jlDescription.setBackground(new Color(255, 255, 225));

		AutoCompleteDocumentContext context = new AutoCompleteDocumentContext(ficheConcours.getParametre().getReglement());
		
		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.SearchType.CLUB_SEARCH, context);
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.SearchType.AGREMENT_SEARCH, context);
		acdAgrement.addAutoCompleteDocumentListener(this);

		jtfNom.setDocument(new PlainDocument());
		jtfLicence.setDocument(new PlainDocument());
		jtfPrenom.setDocument(new PlainDocument());
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);
		
		setConcurrent(concurrent);
		
		jbPrecedent.setEnabled(hasPrevious);
		jbSuivant.setEnabled(hasNext);

		returnVal = CANCEL;

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		return returnVal;
	}

	/**
	 * Affichage des info d'un concurrent en mode edition
	 * 
	 * @param concurrent le concurrent à éditer
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
		this.entiteConcurrent = concurrent.getEntite();

		completeConcurrentDialog();
	}

	/**
	 * Retourne le concurrent de la boite de dialogue
	 * 
	 * @return le concurrent courant
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * formate l'affichage des places libre en fonction des catégorie de classement
	 * 
	 * @return String l'affichage écran des places libres
	 */
	private String showPlacesLibre() {
		String strPlaceLibre = "<html>"; //$NON-NLS-1$

		// affiche le nombre de concurrent total sur le pas de tir
		strPlaceLibre += localisation.getResourceString("concurrent.placelibre.nbarcher") + //$NON-NLS-1$
				": " + ficheConcours.getConcurrentList().countArcher() + "<br><br>"; //$NON-NLS-1$ //$NON-NLS-2$

		// récupère la table d'occupation des cibles
		Hashtable<DistancesEtBlason, TargetsOccupation> occupationCibles = ficheConcours.getPasDeTir(concurrent.getDepart()).getTargetsOccupation(ficheConcours.getParametre().getNbTireur());

		List<DistancesEtBlason> tableCorresp = ficheConcours.getParametre().getReglement().getListDistancesEtBlason();

		// en extrait les jeux de critères de placement
		List<CriteriaSet> criteriaSetPlacement = new ArrayList<CriteriaSet>();
		for (int i = 0; i < tableCorresp.size(); i++) {
			if(!criteriaSetPlacement.contains(tableCorresp.get(i).getCriteriaSet()))
				criteriaSetPlacement.add(tableCorresp.get(i).getCriteriaSet());
		}

		// ordonne ces critères selon l'ordre définit dans la configuration
		CriteriaSet.sortCriteriaSet(criteriaSetPlacement, ficheConcours.getParametre().getReglement().getListCriteria());

		// boucle sur chacun des jeux de placement
		for (CriteriaSet differentiationCriteria : criteriaSetPlacement) {
			// établit la correspondance entre un jeux de placement et son d/b
			List<DistancesEtBlason> ldistAndBlas = ficheConcours.getParametre().getReglement().getDistancesEtBlasonFor(differentiationCriteria);

			for(DistancesEtBlason distAndBlas : ldistAndBlas) {
				// génère le libellé complet du jeux de critère
				CriteriaSetLibelle libelle = new CriteriaSetLibelle(differentiationCriteria,localisation);
				String strCategoriePlacement = libelle.toString();
	
				strPlaceLibre += "<i>" + //$NON-NLS-1$
						strCategoriePlacement + "(" + //$NON-NLS-1$
						(!distAndBlas.isDefaultTargetFace() ? "alt., " : "") +  //$NON-NLS-1$ //$NON-NLS-2$
						distAndBlas.getDistance()[0] + "m/" + //$NON-NLS-1$
						distAndBlas.getTargetFace().getName() + ")</i><br>\n"; //$NON-NLS-1$
				strPlaceLibre += "&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">" + //$NON-NLS-1$
						localisation.getResourceString("concurrent.placelibre.occupee") + //$NON-NLS-1$
						" " + occupationCibles.get(distAndBlas).getPlaceOccupe() + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
				strPlaceLibre += ", <font color=\"green\">" + //$NON-NLS-1$
						localisation.getResourceString("concurrent.placelibre.libre") + //$NON-NLS-1$
						" " + occupationCibles.get(distAndBlas).getPlaceLibre() + "</font><br>"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return strPlaceLibre;
	}

	private ArrayList<Integer> readScores() throws NumberFormatException {
		ArrayList<Integer> points = new ArrayList<Integer>();
		try {
			for (int i = 0; i < tfpd.length; i++) {
				points.add(Integer.parseInt(tfpd[i].getText()));
			}
		} catch(NumberFormatException e) {
			throw e;
		}

		return points;
	}

	private CriteriaSet readCriteriaSet() {
		CriteriaSet differentiationCriteria = new CriteriaSet();
		differentiationCriteria.setReglement(ficheConcours.getParametre().getReglement());
		for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			CriterionElement criterionElement = (CriterionElement)jcbCategorieTable.get(key).getSelectedItem();
			differentiationCriteria.addCriterionElement(criterionElement);
		}

		return differentiationCriteria;
	}
	
	private boolean verifyCriteriaSet() {
		Reglement reglement = ficheConcours.getParametre().getReglement();
		
		CriteriaSet currentCS = readCriteriaSet();
		CriteriaSet classementCS = currentCS.getFilteredCriteriaSet(reglement.getClassementFilter());
		List<CriteriaSet> validClassementCS = reglement.getValidClassementCriteriaSet();
		
		return validClassementCS.contains(classementCS);
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
			jlDescription.setText(localisation.getResourceString("concurrent.homonyme")); //$NON-NLS-1$
			jlDescription.setBackground(Color.ORANGE);
		} else if(concurrent.isSurclassement()) {
			jlDescription.setText(localisation.getResourceString("concurrent.mustbeoverclassified")); //$NON-NLS-1$
			jlDescription.setBackground(new Color(155, 155, 255));
		} else {
			jlDescription.setText(localisation.getResourceString("concurrent.description")); //$NON-NLS-1$
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
			newConcurrent.setName(jtfNom.getText());
		} else if (e.getSource() == jtfPrenom) {
			newConcurrent.setName(jtfNom.getText());
			newConcurrent.setFirstName(jtfPrenom.getText());
		} else if (e.getSource() == jtfLicence) {
			newConcurrent.setName(jtfNom.getText());
			newConcurrent.setFirstName(jtfPrenom.getText());
			newConcurrent.setNumLicenceArcher(jtfLicence.getText());
		}

		filter = null;

		setConcurrent(newConcurrent);

		jlDescription.setText(localisation.getResourceString("concurrent.noconcurrent")); //$NON-NLS-1$
		jlDescription.setBackground(Color.ORANGE);
	}

	public void entiteFinded(AutoCompleteDocumentEvent e) {
		Entite findEntite = e.getEntite();
		if (!findEntite.equals(concurrent.getEntite())) {
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
			
			//évite de modifier l'objet concurrent avant d'avoir
			//validé les paramètres
			Concurrent tempConcurrent = concurrent.clone();
			DistancesEtBlason db1 = null;
			if(tempConcurrent.getCriteriaSet() != null)
				db1 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), tempConcurrent);
			
			// fixe le jeux de critères définissant le concurrent
			tempConcurrent.setCriteriaSet(readCriteriaSet());
			//vérifie la validité du jeux
			if(!verifyCriteriaSet()) {
				JOptionPane.showMessageDialog(this, 
						localisation.getResourceString("concurrent.invalidcriteriaset"), //$NON-NLS-1$
						localisation.getResourceString("concurrent.invalidcriteriaset.title"), //$NON-NLS-1$
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			DistancesEtBlason db2 = DistancesEtBlason.getDistancesEtBlasonForConcurrent(ficheConcours.getParametre().getReglement(), tempConcurrent);
			
			tempConcurrent.setHandicape(jcbHandicape.isSelected());
			tempConcurrent.setSurclassement(jcbSurclassement.isSelected());

			if (tempConcurrent.getInscription() == Concurrent.UNINIT) {
				// si il n'y a plus de place alors retourner une erreur
				if (!ficheConcours.getPasDeTir(tempConcurrent.getDepart()).havePlaceForConcurrent(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, localisation.getResourceString("erreur.maxcible"), //$NON-NLS-1$
							localisation.getResourceString("erreur.maxcible.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
					// si le concurrent existe déjà alors retourner une
					// erreur
				} else if (ficheConcours.getConcurrentList().contains(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, localisation.getResourceString("erreur.alreadyexist"), //$NON-NLS-1$
							localisation.getResourceString("erreur.alreadyexist.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			} else {
				if (!ficheConcours.getPasDeTir(tempConcurrent.getDepart()).havePlaceForConcurrent(tempConcurrent)) {
					JOptionPane.showMessageDialog(this, localisation.getResourceString("erreur.maxcible"), //$NON-NLS-1$
							localisation.getResourceString("erreur.maxcible.titre"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			}
			
			try {
				// verification du score
				if (!ficheConcours.getParametre().getReglement().isValidScore(readScores())) {
					JOptionPane.showMessageDialog(new JDialog(), localisation.getResourceString("erreur.impscore"), //$NON-NLS-1$
							localisation.getResourceString("erreur"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
			
				concurrent.setScore(readScores());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						localisation.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						localisation.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(db1 != null && !db1.equals(db2)) {
				ficheConcours.getPasDeTir(tempConcurrent.getDepart()).retraitConcurrent(concurrent);
			}
			
			concurrent.setCriteriaSet(tempConcurrent.getCriteriaSet());
			concurrent.setHandicape(tempConcurrent.isHandicape());
			concurrent.setSurclassement(tempConcurrent.isSurclassement());
			try {
				int[] departages = new int[tfDepartages.length];
				for(int i = 0; i < tfDepartages.length; i++) {
					departages[i] = Integer.parseInt(tfDepartages[i].getText());
				}
				concurrent.setDepartages(departages);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						localisation.getResourceString("erreur.erreursaisie"), //$NON-NLS-1$
						localisation.getResourceString("erreur.erreursaisie.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			concurrent.setName(jtfNom.getText());
			concurrent.setFirstName(jtfPrenom.getText());
			concurrent.setNumLicenceArcher(jtfLicence.getText());
			concurrent.setEntite(entiteConcurrent);
			concurrent.getEntite().setVille(jtfClub.getText());
			concurrent.getEntite().setAgrement(jtfAgrement.getText());
			concurrent.setInscription(jcbInscription.getSelectedIndex());
			
			if(jcbBlason.getSelectedIndex() > 0)
				concurrent.setAlternativeTargetFace((Blason)jcbBlason.getSelectedItem());
			else
				concurrent.setAlternativeTargetFace(null);

			try {
				concurrent.saveCriteriaSet();
			} catch (ObjectPersistenceException e) {
				e.printStackTrace();
			}

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
			//Le chargement de la liste des concurrents étant asynchrone, on doit attendre que celle ci
			// soit chargé avant de l'afficher. On place un timeout de 30s pour ne pas bloqué définitivement
			// l'interface en cas d'echec de chargement ou avoir un délai d'attente trop long sur certain système
            try {
            	ConcurrentListDialog cld = concurrentListDialog.get(30, TimeUnit.SECONDS);
	            
	            cld.setFilter(filter);
				cld.setVisible(true);
				if (cld.isValider()) {
					concurrent = cld.getSelectedConcurrent();
					setConcurrent(concurrent);
				}
            } catch (InterruptedException e) {
            	DisplayableErrorHelper.displayException(e);
	            e.printStackTrace();
            } catch (ExecutionException e) {
            	DisplayableErrorHelper.displayException(e);
	            e.printStackTrace();
            } catch (TimeoutException e) {
            	JOptionPane.showMessageDialog(this, localisation.getResourceString("concurrent.info.listing.wait")); //$NON-NLS-1$
            }
		} else if (ae.getSource() == jbDetailClub) {
			if (!jtfAgrement.getText().equals("")) { //$NON-NLS-1$
				EntiteDialog ed = new EntiteDialog(this, profile);
				ed.setEntite(concurrent.getEntite());
				ed.showEntiteDialog(false);
			}
		} else if (ae.getSource() == jbListeClub) {
			EntiteListDialog entiteListDialog = new EntiteListDialog(null, profile, true);
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
			jcbSurclassement.setEnabled(true);

			for (Criterion key : ficheConcours.getParametre().getReglement().getListCriteria())
				jcbCategorieTable.get(key).setEnabled(true);

			jcbBlason.setEnabled(jcbBlason.getItemCount() > 1);
			jtfClub.setEditable(true);
			jtfAgrement.setEditable(true);

			jbEditerArcher.setEnabled(false);

			jbEditerArcher.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.open") //$NON-NLS-1$
			));

		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() instanceof JComboBox) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				Reglement reglement = ficheConcours.getParametre().getReglement();
				
				CriteriaSet currentCS = readCriteriaSet();
				CriteriaSet classementCS = currentCS.getFilteredCriteriaSet(reglement.getClassementFilter());
				if(!verifyCriteriaSet()) {
					CriteriaSet surclassement = reglement.getSurclassement().get(classementCS);
					if(surclassement == null) {
						jlDescription.setText(localisation.getResourceString("concurrent.invalidcriteriaset")); //$NON-NLS-1$
						jlDescription.setBackground(Color.ORANGE);
					} else {
						for (Criterion key : reglement.getListCriteria()) {
							CriterionElement element = surclassement.getCriterionElement(key);
							if(element != null)
								jcbCategorieTable.get(key).setSelectedItem(element);
							else
								jcbCategorieTable.get(key).setSelectedIndex(0);
						}
						jcbSurclassement.setSelected(true);
					}
				} else {
					jlDescription.setText(localisation.getResourceString("concurrent.description")); //$NON-NLS-1$
					jlDescription.setBackground(new Color(255, 255, 225));
				}
				
				jcbBlason.removeAllItems();

				List<DistancesEtBlason> tmpDB = reglement.getDistancesEtBlasonFor(
						currentCS.getFilteredCriteriaSet(reglement.getPlacementFilter()));
				for(DistancesEtBlason db : tmpDB) {
					jcbBlason.addItem(db.getTargetFace());
				}
				jcbBlason.setEnabled(jcbBlason.getItemCount() > 1);
				if(concurrent != null && concurrent.getCriteriaSet() != null)
					jcbBlason.setSelectedItem(DistancesEtBlason.getDistancesEtBlasonForConcurrent(
							reglement, concurrent).getTargetFace());

			}
		}
	}

	/**
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent fe) {
		selectField = -1;
		for (int i = 0; i < tfpd.length; i++) {
			if (fe.getSource() == tfpd[i]) {
				selectField = i;
				break;
			}
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
		if(concurrentListDialog != null) {
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
		lastActiveReglement = null;

		super.dispose();
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	private abstract static class Factory implements ThreadFactory {
		protected final ThreadGroup group;

		Factory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, getThreadName(), 0);

			if (t.isDaemon()) {
				t.setDaemon(false);
			}

			return t;
		}

		protected abstract String getThreadName();
	}
	
	private static class LowFactory extends Factory {
		private final AtomicInteger lowThreadNumber = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = super.newThread(r);

			if (t.getPriority() != Thread.MIN_PRIORITY) {
				t.setPriority(Thread.MIN_PRIORITY);
			}

			return t;
		}

		@Override
		protected String getThreadName() {
			return "low-thread-" + lowThreadNumber.getAndIncrement(); //$NON-NLS-1$
		}
	}

}


