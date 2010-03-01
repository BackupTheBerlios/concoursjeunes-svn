/*
 * Created on 1 janv. 2005
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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.concoursjeunes.CompetitionLevel;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.Judge;
import org.concoursjeunes.Parametre;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.jdesktop.swingx.JXDatePicker;

import com.lowagie.text.Font;

/**
 * Boite de dialogue de gestion des parametre du concours
 * 
 * @author Aurélien Jeoffray
 * @version 2.0
 */
@Localisable(textMethod="setTitle",value="parametre.titre")
public class ParametreDialog extends JDialog implements ActionListener, ListSelectionListener {

	private AjResourcesReader localisation;
	private Profile profile;
	private Parametre parametre;
	private Reglement tempReglement;

	private final FicheConcours ficheConcours;
	private JFrame parentframe;

	@Localisable(value="parametre.infos",textMethod="setTitle")
	private TitledBorder infosBorder = new TitledBorder(""); //$NON-NLS-1$
	private JTextField jtfIntituleConcours = new JTextField(20);
	private JTextField jtfLieuConcours = new JTextField(20);
	private JXDatePicker jtfDateDebutConcours = new JXDatePicker();
	private JXDatePicker jtfDateFinConcours = new JXDatePicker();
	@Localisable(value="parametre.typecompetition",textMethod="setTitle")
	private TitledBorder typeCompetitionBorder = new TitledBorder(""); //$NON-NLS-1$
	private JLabel jlSelectedReglement = new JLabel();
	@Localisable("parametre.choice_reglement")
	private JButton jbSelectReglement = new JButton();
	@Localisable("parametre.detail_customize")
	private JButton jbDetail = new JButton();
	private JComboBox jcbNiveauChampionnat = new JComboBox();
	@Localisable("parametre.openclose")
	private JCheckBox jcbCloseCompetition = new JCheckBox();
	@Localisable(value="parametre.options",textMethod="setTitle")
	private TitledBorder optionsBorder = new TitledBorder(""); //$NON-NLS-1$
	private JTextField jtfNombreCible = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JComboBox jcbNombreTireurParCible = new JComboBox();
	private JTextField jtfNombreDepart = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	@Localisable(value="parametre.arbitresborder",textMethod="setTitle")
	private TitledBorder arbitresBorder = new TitledBorder(""); //$NON-NLS-1$
	@Localisable("bouton.ajouter")
	private JButton jbAjouterArbitre = new JButton();
	@Localisable("bouton.supprimer")
	private JButton jbSupprimerArbitre = new JButton();
	@Localisable("bouton.editer")
	private JButton jbEditerArbitre = new JButton();
	private AJList<Judge> jlArbitres = new AJList<Judge>();

	@Localisable("parametre.intituleconcours")
	private JLabel jlIntituleConcours = new JLabel();
	@Localisable("parametre.lieuconcours")
	private JLabel jlLieuConcours = new JLabel();
	@Localisable("parametre.datedebutconcours")
	private JLabel jlDateDebutConcours = new JLabel();
	@Localisable("parametre.datefinconcours")
	private JLabel jlDateFinConcours = new JLabel();
	@Localisable("parametre.reglement")
	private JLabel jlReglement = new JLabel();
	@Localisable("parametre.niveauchampionnat")
	private JLabel jlNiveauChampionnat = new JLabel();
	@Localisable("parametre.nombrecible")
	private JLabel jlNombreCible = new JLabel();
	@Localisable("parametre.nombretireurparcible")
	private JLabel jlNombreTireurParCible = new JLabel();
	@Localisable("parametre.nombredepart")
	private JLabel jlNombreDepart = new JLabel();
	@Localisable("parametre.arbitres")
	private JLabel jlbArbitres = new JLabel();

	@Localisable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	private ReglementDialog reglementDialog;

	public ParametreDialog(JFrame parentframe, Profile profile, FicheConcours ficheConcours) {
		super(parentframe);

		this.parentframe = parentframe;
		this.profile = profile;
		this.ficheConcours = ficheConcours;
		this.localisation = profile.getLocalisation();

		reglementDialog = new ReglementDialog(parentframe, null, localisation);

		init();
		affectLibelle();

		getRootPane().setDefaultButton(jbValider);
		setModal(true);
	}

	/**
	 * This method initializes this
	 */
	private void init() {
		JPanel jpParametre = new JPanel();
		JPanel jpValidation = new JPanel();
		JPanel jpReglement	= new JPanel();
		
		JPanel jpInfos = new JPanel();
		JPanel jpTypeCompetition = new JPanel();
		JPanel jpOptions = new JPanel();
		JPanel jpArbitres = new JPanel();
		
		jpInfos.setBorder(infosBorder);
		jpTypeCompetition.setBorder(typeCompetitionBorder);
		jpOptions.setBorder(optionsBorder);
		jpArbitres.setBorder(arbitresBorder);

		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();

		jbDetail.addActionListener(this);
		jtfDateDebutConcours.setFormats(new DateFormat[] { DateFormat.getDateInstance(DateFormat.SHORT) });
		jtfDateFinConcours.setFormats(new DateFormat[] { DateFormat.getDateInstance(DateFormat.SHORT) });
		jlSelectedReglement.setFont(jlSelectedReglement.getFont().deriveFont(Font.ITALIC));
		jbSelectReglement.addActionListener(this);
		jcbNiveauChampionnat.setRenderer(new DefaultListCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof CompetitionLevel)
					value = ((CompetitionLevel)value).getLibelle();
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});

		jlArbitres.setCellRenderer(new DefaultListCellRenderer() {

			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof Judge) {
					Judge j = (Judge)value;
					value = (j.isResponsable() ? "*" : "") + j.getID(); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
			
		});
		jlArbitres.addListSelectionListener(this);
		jbAjouterArbitre.addActionListener(this);
		jbSupprimerArbitre.addActionListener(this);
		jbSupprimerArbitre.setEnabled(false);
		jbEditerArbitre.addActionListener(this);
		jbEditerArbitre.setEnabled(false);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		jpReglement.add(jbSelectReglement);
		jpReglement.add(jbDetail);

		jpValidation.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpValidation.add(jbValider);
		jpValidation.add(jbAnnuler);
		
		gridbagComposer.setParentPanel(jpInfos);
		c.gridy = 0;
		c.weightx = 1.0;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jtfIntituleConcours, c);

		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlLieuConcours, c);
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jtfLieuConcours, c);
		
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlDateDebutConcours, c);
		gridbagComposer.addComponentIntoGrid(jtfDateDebutConcours, c);		
		gridbagComposer.addComponentIntoGrid(jlDateFinConcours, c);
		gridbagComposer.addComponentIntoGrid(jtfDateFinConcours, c);
		
		gridbagComposer.setParentPanel(jpTypeCompetition);
		c.gridy = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlReglement, c);
		gridbagComposer.addComponentIntoGrid(jlSelectedReglement, c);
		
		c.gridy++;
		c.gridx = 1;
		gridbagComposer.addComponentIntoGrid(jpReglement, c);

		c.gridy++;
		c.gridwidth = 1;
		c.gridx = GridBagConstraints.RELATIVE;
		gridbagComposer.addComponentIntoGrid(jlNiveauChampionnat, c);
		gridbagComposer.addComponentIntoGrid(jcbNiveauChampionnat, c);
		
		c.gridy++;
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbCloseCompetition, c);
		
		gridbagComposer.setParentPanel(jpOptions);
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlNombreCible, c);
		gridbagComposer.addComponentIntoGrid(jtfNombreCible, c);
		gridbagComposer.addComponentIntoGrid(jlNombreDepart, c);
		gridbagComposer.addComponentIntoGrid(jtfNombreDepart, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreTireurParCible, c);
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jcbNombreTireurParCible, c);
		
		gridbagComposer.setParentPanel(jpArbitres);
		c.gridy = 0;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlbArbitres, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 3;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jlArbitres), c);
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		gridbagComposer.addComponentIntoGrid(jbAjouterArbitre, c);

		c.gridy++;
		c.gridx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jbSupprimerArbitre, c);
		
		c.gridy++;
		c.gridx = 2;
		c.anchor = GridBagConstraints.NORTH;
		gridbagComposer.addComponentIntoGrid(jbEditerArbitre, c);

		gridbagComposer.setParentPanel(jpParametre);
		c.gridy = 0;
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(jpInfos, c);
		
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jpTypeCompetition, c);
		
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jpOptions, c);
		
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jpArbitres, c);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpParametre, BorderLayout.CENTER);
		getContentPane().add(jpValidation, BorderLayout.SOUTH);
	}

	public void showParametreDialog(Parametre parametre) {
		this.parametre = parametre;
		completePanel();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void affectLibelle() {
		Localisator.localize(this, localisation);
	}

	private void completePanel() {
		tempReglement = parametre.getReglement();
		
		jtfIntituleConcours.setText(parametre.getIntituleConcours());
		jtfLieuConcours.setText(parametre.getLieuConcours());
		jtfDateDebutConcours.setDate(parametre.getDateDebutConcours());
		jtfDateFinConcours.setDate(parametre.getDateFinConcours());
		jlSelectedReglement.setText(parametre.getReglement().getDisplayName());
		jbSelectReglement.setEnabled(!parametre.isReglementLock());
		jcbNiveauChampionnat.removeAllItems();
		for(CompetitionLevel cl : parametre.getReglement().getFederation().getCompetitionLevels(profile.getConfiguration().getLangue()))
			jcbNiveauChampionnat.addItem(cl);
		jcbNiveauChampionnat.setSelectedItem(parametre.getNiveauChampionnat());
		jcbCloseCompetition.setSelected(!parametre.isOpen());
		jtfNombreCible.setText("" + parametre.getNbCible()); //$NON-NLS-1$
		
		jcbNombreTireurParCible.removeAllItems();
		jcbNombreTireurParCible.addItem(new RhytmeTir("AB", 2));  //$NON-NLS-1$
		jcbNombreTireurParCible.addItem(new RhytmeTir("AB/CD", 4));  //$NON-NLS-1$
		if(parametre.getNbTireur() > 4) {
			RhytmeTir rythmePers = new RhytmeTir("Personnalisé (" + parametre.getNbTireur() + " archers par cible)", parametre.getNbTireur()); //$NON-NLS-1$ //$NON-NLS-2$
			jcbNombreTireurParCible.addItem(rythmePers);
		}
		jcbNombreTireurParCible.setSelectedItem(new RhytmeTir(null, parametre.getNbTireur()));
		
		jtfNombreDepart.setText("" + parametre.getNbDepart()); //$NON-NLS-1$
		jlArbitres.setListData(parametre.getJudges().toArray());
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == jbValider) {

			if(ficheConcours != null) {
				//verifie que la place libre restante est suffisante avec le nouveau nombre de cible
				int placelibre = parametre.getNbCible();
				for (int i = 0; i < parametre.getNbDepart(); i++) {
					// ficheConcours.getPasDeTir(i).getOccupationCibles();
					int placelibre_tmp = ficheConcours.getPasDeTir(i).getNbFreeTargets(((RhytmeTir)jcbNombreTireurParCible.getSelectedItem()).getNbConcurrent());
					if (placelibre_tmp < placelibre)
						placelibre = placelibre_tmp;
				}
	
				if (placelibre < 0 || parametre.getNbCible() - placelibre > Integer.parseInt(jtfNombreCible.getText())) {
					JOptionPane.showMessageDialog(this, localisation.getResourceString("parametre.toomany"), localisation.getResourceString("parametre.toomany.title"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
					return;
				}
	
				//verifie que la réduction du nombre de départ est possible (aucun archer sur les départs supprimé)
				if (Integer.parseInt(jtfNombreDepart.getText()) < parametre.getNbDepart()) {
					for (int i = Integer.parseInt(jtfNombreDepart.getText()); i < parametre.getNbDepart(); i++) {
						if (ficheConcours.getConcurrentList().countArcher(i) > 0) {
							JOptionPane.showMessageDialog(this, localisation.getResourceString("parametre.enablereducestart"), localisation //$NON-NLS-1$
									.getResourceString("parametre.enablereducestart.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
							return;
						}
					}
				}
			}
			
			//limite le nombre de départ possible
//			if(Integer.parseInt(jtfNombreDepart.getText()) > 9) {
//				JOptionPane.showMessageDialog(this, localisation.getResourceString("parametre.toomanystart"), localisation //$NON-NLS-1$
//						.getResourceString("parametre.toomanystart.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
//				return;
//			}

			/*
			 * for(int i = 0; i < Integer.parseInt(jtfNombreDepart.getText()); i++) { ArrayList<DistancesEtBlason> lDB = ficheConcours.getConcurrentList().listDistancesEtBlason(
			 * parametre.getReglement(), true, i); if(ficheConcours.getPasDeTir(i).getOptimalRythme(lDB) > (Integer)jcbNombreTireurParCible.getSelectedItem()) { JOptionPane.showMessageDialog(this, "Il
			 * y a trop d'archers pour pouvoir réduire le nombre de départ", //$NON-NLS-1$ "Rythme de tir impossible",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ return; } }
			 */

			parametre.setIntituleConcours(jtfIntituleConcours.getText());
			parametre.setLieuConcours(jtfLieuConcours.getText());
			parametre.setDateDebutConcours(jtfDateDebutConcours.getDate());
			parametre.setDateFinConcours(jtfDateFinConcours.getDate());
			parametre.setNiveauChampionnat((CompetitionLevel)jcbNiveauChampionnat.getSelectedItem());
			parametre.getJudges().clear();
			for (Object arbitre : jlArbitres.getAllElements()) {
				parametre.getJudges().add((Judge)arbitre);
			}

			parametre.setNbCible(Integer.parseInt(jtfNombreCible.getText()));
			parametre.setNbTireur(((RhytmeTir)jcbNombreTireurParCible.getSelectedItem()).getNbConcurrent());
			parametre.setNbDepart(Integer.parseInt(jtfNombreDepart.getText()));
			parametre.setReglement(tempReglement);
			parametre.setReglementLock(true);

			setVisible(false);
		} else if (ae.getSource() == jbAnnuler) {
			setVisible(false);
		} else if (ae.getSource() == jbAjouterArbitre) {
			ArbitreDialog ad = new ArbitreDialog(parentframe, profile);
			boolean mustberesponsable = true; 
			for(Object o : jlArbitres.getAllElements())
				if(((Judge)o).isResponsable())
					mustberesponsable = false;
			if(ad.showJudgeDialog(null, mustberesponsable) == ArbitreDialog.CONFIRM) {
				if(!ad.getJudge().getNomArcher().trim().isEmpty())
					jlArbitres.add(ad.getJudge());
			}
		} else if (ae.getSource() == jbSupprimerArbitre) {
			if(jlArbitres.getSelectedIndex() > -1)
				jlArbitres.remove(jlArbitres.getSelectedIndex());
		} else if(ae.getSource() == jbEditerArbitre) {
			Judge selectedJudge = (Judge)jlArbitres.getSelectedValue();
			ArbitreDialog ad = new ArbitreDialog(parentframe, profile);
			boolean mustberesponsable = true; 
			for(Object o : jlArbitres.getAllElements())
				if(((Judge)o).isResponsable() && o != selectedJudge)
					mustberesponsable = false;
			if(ad.showJudgeDialog(selectedJudge, mustberesponsable) == ArbitreDialog.CONFIRM) {
				jlArbitres.repaint();
			}
		} else if (ae.getSource() == jbDetail) {
			reglementDialog.setReglement(tempReglement);
			reglementDialog.setEditable(true);
			if(reglementDialog.showReglementDialog() == DefaultDialogReturn.OK)
				tempReglement = reglementDialog.getReglement();
			
		} else if (ae.getSource() == jbSelectReglement) {
			ReglementManagerDialog reglementManagerDialog = new ReglementManagerDialog(parentframe, profile);
			Reglement reglement = reglementManagerDialog.showReglementManagerDialog(true);
			if(reglement != null && (tempReglement == null || !tempReglement.equals(reglement))) {
				tempReglement = reglement;
				jlSelectedReglement.setText(reglement.getDisplayName());
				jcbNiveauChampionnat.removeAllItems();
				for(CompetitionLevel cl : tempReglement.getFederation().getCompetitionLevels(profile.getConfiguration().getLangue()))
					jcbNiveauChampionnat.addItem(cl);
				jcbNiveauChampionnat.setSelectedItem(parametre.getNiveauChampionnat());
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == jlArbitres) {
			jbSupprimerArbitre.setEnabled(jlArbitres.getSelectedIndex() > -1);
			jbEditerArbitre.setEnabled(jlArbitres.getSelectedIndex() > -1);
		}
	}
	
	private static class RhytmeTir {
		private String libelle = ""; //$NON-NLS-1$
		private int nbConcurrent;
		/**
		 * @param libelle
		 * @param nbConcurrent
		 */
		public RhytmeTir(String libelle, int nbConcurrent) {
			this.libelle = libelle;
			this.nbConcurrent = nbConcurrent;
		}
		/**
		 * @return libelle
		 */
		//public String getLibelle() {
		//	return libelle;
		//}
		/**
		 * @param libelle libelle à définir
		 */
		//public void setLibelle(String libelle) {
		//	this.libelle = libelle;
		//}
		/**
		 * @return nbConcurrent
		 */
		public int getNbConcurrent() {
			return nbConcurrent;
		}
		/**
		 * @param nbConcurrent nbConcurrent à définir
		 */
		//public void setNbConcurrent(int nbConcurrent) {
		//	this.nbConcurrent = nbConcurrent;
		//}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return libelle;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + nbConcurrent;
			return result;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RhytmeTir other = (RhytmeTir) obj;
			if (nbConcurrent != other.nbConcurrent)
				return false;
			return true;
		}
		
	}
}