/*
 * Créé le 13 févr. 2011 à 16:06:46 pour ConcoursJeunes / ArcCompétition
 *
 * Copyright 2002-2011 - Aurélien JEOFFRAY
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
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizationHandler;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.swingxext.localisation.JXHeaderLocalisationHandler;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.Profile;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.painter.GlossPainter;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Localizable(value="competitionselection.title",textMethod="setTitle")
public class CompetitionSelectionDialog extends JDialog implements ActionListener {
	
	private AjResourcesReader localisation;
	private Profile profile;
	
	@Localizable("competitionselection.header")
	private JXHeader jxhCompetitionSelection = new JXHeader();
	
	private JList jlCompetitions = new JList();
	private DefaultListModel competitionListModel = new DefaultListModel();
	
	@Localizable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localizable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	private MetaDataFicheConcours selectedCompetition;
	private DefaultDialogReturn returnAction = DefaultDialogReturn.CANCEL;
	
	public CompetitionSelectionDialog(JFrame parentframe, Profile profile, AjResourcesReader localisation) {
		super(parentframe, ModalityType.TOOLKIT_MODAL);
		
		this.localisation = localisation;
		this.profile = profile;
		
		init();
		affectLabels();
	}
	
	/**
	 * initialise la boite de dialogue
	 */
	private void init() {
		GlossPainter gloss = new GlossPainter();
		jxhCompetitionSelection.setBackground(new Color(200,200,255));
		jxhCompetitionSelection.setBackgroundPainter(gloss);
		
		jlCompetitions.setCellRenderer(new DefaultListCellRenderer() {
			private DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(value instanceof MetaDataFicheConcours) {
					MetaDataFicheConcours metaDataFicheConcours = (MetaDataFicheConcours)value;
					value = df.format(metaDataFicheConcours.getDateConcours()) 
							+ " - " + metaDataFicheConcours.getIntituleConcours(); //$NON-NLS-1$
				}
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		jlCompetitions.setModel(competitionListModel);
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		JPanel jpAction = new JPanel();
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, jxhCompetitionSelection);
		getContentPane().add(BorderLayout.CENTER, new JScrollPane(jlCompetitions));
		getContentPane().add(BorderLayout.SOUTH, jpAction);
		getRootPane().setDefaultButton(jbValider);
	}
	
	private void completePanel() {
		MetaDataFichesConcours metaDataFichesConcours = profile.getConfiguration().getMetaDataFichesConcours();
		List<MetaDataFicheConcours> fiches = metaDataFichesConcours.getFiches();
		if (fiches.size() > 0) {
			Collections.sort(fiches, new Comparator<MetaDataFicheConcours>() {
				@Override
				public int compare(MetaDataFicheConcours o1, MetaDataFicheConcours o2) {
					return o2.getDateConcours().compareTo(o1.getDateConcours());
				}
			});
			competitionListModel.clear();
			for (MetaDataFicheConcours metaDataFicheConcours : fiches) {
				competitionListModel.addElement(metaDataFicheConcours);
			}
		}
	}
	
	private void affectLabels() {
		Localizator.localize(this, localisation, Collections.<Class<?>, LocalizationHandler>singletonMap(JXHeader.class, new JXHeaderLocalisationHandler()));	
	}
	
	public DefaultDialogReturn showCompetitionSelectionDialog() {
		
		completePanel();
		
		setSize(350, 450);
		setLocationRelativeTo(null);
		setVisible(true);
		
		return returnAction;
	}

	/**
	 * @return selectedCompetition
	 */
	public MetaDataFicheConcours getSelectedCompetition() {
		return selectedCompetition;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			selectedCompetition = (MetaDataFicheConcours)jlCompetitions.getSelectedValue();
			
			returnAction = DefaultDialogReturn.OK;
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}
}
