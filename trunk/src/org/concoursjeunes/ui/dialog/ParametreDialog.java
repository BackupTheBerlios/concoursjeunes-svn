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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.Localisable;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.commons.ui.NumberDocument;
import org.concoursjeunes.CompetitionLevel;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.Parametre;
import org.concoursjeunes.Profile;
import org.concoursjeunes.Reglement;
import org.jdesktop.swingx.JXDatePicker;

import com.lowagie.text.Font;

/**
 * Boite de dialogue de gestion des parametre du concours
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 */
public class ParametreDialog extends JDialog implements ActionListener {

	private AjResourcesReader localisation;
	private Profile profile;
	private Parametre parametre;
	private Reglement tempReglement;

	private final FicheConcours ficheConcours;
	private JFrame parentframe;

	private final JTextField jtfIntituleConcours = new JTextField(20);
	private final JTextField jtfLieuConcours = new JTextField(20);
	private final JXDatePicker jtfDateDebutConcours = new JXDatePicker();
	private final JXDatePicker jtfDateFinConcours = new JXDatePicker();
	private final JLabel jlSelectedReglement = new JLabel();
	@Localisable("parametre.choice_reglement")
	private final JButton jbSelectReglement = new JButton();
	@Localisable("parametre.detail_customize")
	private final JButton jbDetail = new JButton();
	private JComboBox jcbNiveauChampionnat = new JComboBox();
	private JCheckBox jcbCloseCompetition = new JCheckBox();
	private final JTextField jtfNombreCible = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JComboBox jcbNombreTireurParCible = new JComboBox();
	private final JTextField jtfNombreDepart = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private final JTextField jtfArbitres = new JTextField(20);
	@Localisable("bouton.ajouter")
	private final JButton jbAjouterArbitre = new JButton();
	@Localisable("bouton.supprimer")
	private final JButton jbSupprimerArbitre = new JButton();
	@Localisable("bouton.arbitreresponsable")
	private final JButton jbArbitreResponsable = new JButton();
	private final AJList jlArbitres = new AJList();

	@Localisable("parametre.intituleconcours")
	private final JLabel jlIntituleConcours = new JLabel();
	@Localisable("parametre.lieuconcours")
	private final JLabel jlLieuConcours = new JLabel();
	@Localisable("parametre.datedebutconcours")
	private final JLabel jlDateDebutConcours = new JLabel();
	@Localisable("parametre.datefinconcours")
	private final JLabel jlDateFinConcours = new JLabel();
	@Localisable("parametre.reglement")
	private final JLabel jlReglement = new JLabel();
	@Localisable("parametre.niveauchampionnat")
	private JLabel jlNiveauChampionnat = new JLabel();
	@Localisable("parametre.openclose")
	private JLabel jlConcoursOuvert = new JLabel();
	@Localisable("parametre.nombrecible")
	private final JLabel jlNombreCible = new JLabel();
	@Localisable("parametre.nombretireurparcible")
	private final JLabel jlNombreTireurParCible = new JLabel();
	@Localisable("parametre.nombredepart")
	private final JLabel jlNombreDepart = new JLabel();
	@Localisable("parametre.arbitres")
	private final JLabel jlbArbitres = new JLabel();

	@Localisable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();
	
	private ReglementDialog reglementDialog;

	public ParametreDialog(JFrame parentframe, Profile profile, FicheConcours ficheConcours, AjResourcesReader localisation) {
		super(parentframe);

		this.parentframe = parentframe;
		this.profile = profile;
		this.ficheConcours = ficheConcours;
		this.localisation = localisation;

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

		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();

		jbDetail.addActionListener(this);
		jtfDateDebutConcours.setFormats(new DateFormat[] { DateFormat.getDateInstance(DateFormat.SHORT) });
		jtfDateFinConcours.setFormats(new DateFormat[] { DateFormat.getDateInstance(DateFormat.SHORT) });
		jlSelectedReglement.setFont(jlSelectedReglement.getFont().deriveFont(Font.ITALIC));
		jbSelectReglement.addActionListener(this);
		jcbNombreTireurParCible.addItem("AB"); //$NON-NLS-1$
		jcbNombreTireurParCible.addItem("AB/CD"); //$NON-NLS-1$
		jtfArbitres.addActionListener(this);
		jbAjouterArbitre.addActionListener(this);
		jbSupprimerArbitre.addActionListener(this);
		jbArbitreResponsable.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		jpReglement.add(jlSelectedReglement);
		jpReglement.add(jbSelectReglement);
		jpReglement.add(jbDetail);

		jpValidation.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpValidation.add(jbValider);
		jpValidation.add(jbAnnuler);

		gridbagComposer.setParentPanel(jpParametre);
		c.gridy = 0;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.WEST;
		c.ipadx = 2;
		gridbagComposer.addComponentIntoGrid(jlIntituleConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfIntituleConcours, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlLieuConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfLieuConcours, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDateDebutConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfDateDebutConcours, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDateFinConcours, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfDateFinConcours, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlReglement, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jpReglement, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNiveauChampionnat, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbNiveauChampionnat, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlConcoursOuvert, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbCloseCompetition, c);
		
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNombreCible, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreTireurParCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jcbNombreTireurParCible, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNombreDepart, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNombreDepart, c);

		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlbArbitres, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfArbitres, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jbAjouterArbitre, c);

		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(new JScrollPane(jlArbitres), c);
		c.gridx = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jbSupprimerArbitre, c);

		c.gridy++;
		c.anchor = GridBagConstraints.NORTH;
		gridbagComposer.addComponentIntoGrid(jbArbitreResponsable, c);

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
		setTitle(localisation.getResourceString("parametre.titre")); //$NON-NLS-1$
		
		AppUtilities.localize(this, localisation);
	}

	private void completePanel() {
		tempReglement = parametre.getReglement();
		
		jtfIntituleConcours.setText(parametre.getIntituleConcours());
		jtfLieuConcours.setText(parametre.getLieuConcours());
		jtfDateDebutConcours.setDate(parametre.getDateDebutConcours());
		jtfDateFinConcours.setDate(parametre.getDateFinConcours());
		jlSelectedReglement.setText(parametre.getReglement().getName());
		jbSelectReglement.setEnabled(!parametre.isReglementLock());
		jcbNiveauChampionnat.removeAllItems();
		for(CompetitionLevel cl : parametre.getReglement().getFederation().getCompetitionLevels(profile.getConfiguration().getLangue()))
			jcbNiveauChampionnat.addItem(cl);
		jcbNiveauChampionnat.setSelectedItem(parametre.getNiveauChampionnat());
		jcbCloseCompetition.setSelected(!parametre.isOpen());
		jtfNombreCible.setText("" + parametre.getNbCible()); //$NON-NLS-1$
		jcbNombreTireurParCible.setSelectedIndex((parametre.getNbTireur() / 2) - 1);
		jtfNombreDepart.setText("" + parametre.getNbDepart()); //$NON-NLS-1$
		jlArbitres.setListData(parametre.getArbitres().toArray());
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == jbValider) {

			if(ficheConcours != null) {
				//verifie que la place libre restante est suffisante avec le nouveau nombre de cible
				int placelibre = parametre.getNbCible();
				for (int i = 0; i < parametre.getNbDepart(); i++) {
					// ficheConcours.getPasDeTir(i).getOccupationCibles();
					int placelibre_tmp = ficheConcours.getPasDeTir(i).getNbFreeTargets(jcbNombreTireurParCible.getSelectedIndex() == 0 ? 2 : 4);
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
			if(Integer.parseInt(jtfNombreDepart.getText()) > 9) {
				JOptionPane.showMessageDialog(this, localisation.getResourceString("parametre.toomanystart"), localisation //$NON-NLS-1$
						.getResourceString("parametre.toomanystart.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}

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
			parametre.getArbitres().clear();
			for (Object arbitre : jlArbitres.getAllElements()) {
				parametre.getArbitres().add((String) arbitre);
			}

			parametre.setNbCible(Integer.parseInt(jtfNombreCible.getText()));
			parametre.setNbTireur(jcbNombreTireurParCible.getSelectedIndex() == 0 ? 2 : 4);
			parametre.setNbDepart(Integer.parseInt(jtfNombreDepart.getText()));
			parametre.setReglement(tempReglement);
			parametre.setReglementLock(true);

			setVisible(false);
		} else if (ae.getSource() == jbAnnuler) {
			setVisible(false);
		} else if (ae.getSource() == jbAjouterArbitre || ae.getSource() == jtfArbitres) {
			if(!jtfArbitres.getText().isEmpty()) {
				jlArbitres.add(jtfArbitres.getText());
				jtfArbitres.setText(""); //$NON-NLS-1$
			}
		} else if (ae.getSource() == jbSupprimerArbitre) {
			if(jlArbitres.getSelectedIndex() > -1)
				jlArbitres.remove(jlArbitres.getSelectedIndex());
		} else if (ae.getSource() == jbArbitreResponsable && jlArbitres.getSelectedIndex() > -1) {
			if(jlArbitres.getSelectedIndex() > -1) {
				// cherche si il existe un arbitre responsable
				List<Object> lstArbitres = jlArbitres.getAllElements();
				for (Object arbitre : lstArbitres) {
					if(((String) arbitre).startsWith("*")) { //$NON-NLS-1$
						// si il en existe 1 et qu'il est different de celui que l'on veut
						if(!((String) arbitre).equals(jlArbitres.getSelectedValue())) {
							
							// affecter à ce statut alors retirer l'* de la selection precedente
							lstArbitres.set(lstArbitres.indexOf(arbitre), ((String) arbitre).substring(1));
						}
						
						break;
					}
				}
				// et l'ajouter sur la nouvelle selection
				lstArbitres.set(jlArbitres.getSelectedIndex(), "*" + jlArbitres.getSelectedValue()); //$NON-NLS-1$
				
				jlArbitres.setListData(lstArbitres.toArray());
			}
		} else if (ae.getSource() == jbDetail) {
			reglementDialog.setReglement(tempReglement);
			if (parametre.isReglementLock())
				reglementDialog.setVerrou(ReglementDialog.LOCK_CHANGE_L1);
			else
				reglementDialog.setVerrou(ReglementDialog.NO_LOCK);
			Reglement reglement = reglementDialog.showReglementDialog();
			if (reglement != null)
				tempReglement = reglement;
		} else if (ae.getSource() == jbSelectReglement) {
			ReglementManagerDialog reglementManagerDialog = new ReglementManagerDialog(parentframe, localisation);
			Reglement reglement = reglementManagerDialog.showReglementManagerDialog(true);
			if(reglement != null && (tempReglement == null || !tempReglement.equals(reglement))) {
				tempReglement = reglement;
				jlSelectedReglement.setText(reglement.getName());
				jcbNiveauChampionnat.removeAllItems();
				for(CompetitionLevel cl : parametre.getReglement().getFederation().getCompetitionLevels(profile.getConfiguration().getLangue()))
					jcbNiveauChampionnat.addItem(cl);
				jcbNiveauChampionnat.setSelectedItem(parametre.getNiveauChampionnat());
			}
		}
	}
}