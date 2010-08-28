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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.concoursjeunes.Criterion;

/**
 * Boite de dialogue de gestion des critère de distinction des archers
 * @author Aurélien JEOFFRAY
 */
@Localisable(textMethod="setTitle",value="criterion.titre")
public class CriterionDialog extends JDialog implements ActionListener, ChangeListener {
    
	private AjResourcesReader localisation;
    private ReglementDialog parent;
    
    private Criterion criterion;
    
    private JLabel jlIndex = new JLabel();
    @Localisable("criterion.code")
    private JLabel jlCode = new JLabel();
    @Localisable("criterion.libelle")
    private JLabel jlLibelle = new JLabel();
    @Localisable("criterion.ordretri")
    private JLabel jlSortOrder = new JLabel();
    @Localisable("criterion.winfftacode.libelle")
    private JLabel jlWinFFTACode = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JComboBox jcbSortOrder = new JComboBox();
    @Localisable("criterion.placement")
    private JCheckBox jcbPlacementCriterion = new JCheckBox();
    @Localisable("criterion.classement")
    private JCheckBox jcbClassementCriterion = new JCheckBox();
    @Localisable("criterion.classementequipe")
    private JCheckBox jcbClassementEquipeCriterion = new JCheckBox();
    private JComboBox jcbWinFFTACode = new JComboBox();
    
    @Localisable("bouton.valider")
    private JButton jbValider = new JButton();
    @Localisable("bouton.annuler")
    private JButton jbAnnuler = new JButton();
    
    private boolean lock = false;
    
    /**
     * 
     * @param parent
     */
    public CriterionDialog(ReglementDialog parent, AjResourcesReader localisation) {
        this(parent, null, localisation);
    }
    
    /**
     * 
     * @param parent
     * @param criterion
     */
    public CriterionDialog(ReglementDialog parent, Criterion criterion, AjResourcesReader localisation) {
        super(parent, "", true); //$NON-NLS-1$
        
        this.localisation = localisation;
        this.parent = parent;
        this.criterion = criterion;
        
        init();
        affectLibelle();
        completePanel();
    }
    
    /**
     * 
     *
     */
    private void init() {
        //Layout Manager
        GridBagConstraints c    = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel jpGeneral = new JPanel();
        JPanel jpOperation = new JPanel();
        
        jpOperation.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jcbWinFFTACode.addItem(""); //$NON-NLS-1$
        for(String critere : Criterion.CRITERES_TABLE_ARCHERS) {
            jcbWinFFTACode.addItem(critere);
        }
        
        jcbPlacementCriterion.setEnabled(false);
        jcbPlacementCriterion.addChangeListener(this);
        jcbClassementCriterion.addChangeListener(this);
        
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        gridbagComposer.setParentPanel(jpGeneral);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jlIndex, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlCode, c);
        gridbagComposer.addComponentIntoGrid(jtfCode, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlLibelle, c);
        gridbagComposer.addComponentIntoGrid(jtfLibelle, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlSortOrder, c);
        gridbagComposer.addComponentIntoGrid(jcbSortOrder, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbPlacementCriterion, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbClassementCriterion, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbClassementEquipeCriterion, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlWinFFTACode, c);
        gridbagComposer.addComponentIntoGrid(jcbWinFFTACode, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    /**
     * 
     *
     */
    private void affectLibelle() {
        Localisator.localize(this, localisation);
        
        jcbSortOrder.removeAllItems();
        jcbSortOrder.addItem(localisation.getResourceString("criterion.ordretri.asc")); //$NON-NLS-1$
        jcbSortOrder.addItem(localisation.getResourceString("criterion.ordretri.desc")); //$NON-NLS-1$
    }
    
    /**
     * 
     *
     */
    private void completePanel() {
        if(criterion != null) {
            jtfCode.setText(criterion.getCode());
            jtfCode.setEditable(false);
            jtfLibelle.setText(criterion.getLibelle());
            
            jcbSortOrder.setSelectedIndex((criterion.getSortOrder() > 0) ? 0 : 1);
            
            jcbClassementCriterion.setSelected(criterion.isClassement());
            //jcbClassementCriterion.setEnabled(!criterion.isPlacement());
            jcbPlacementCriterion.setSelected(criterion.isPlacement());
            jcbClassementEquipeCriterion.setSelected(criterion.isClassementEquipe());
            jcbWinFFTACode.setSelectedItem(criterion.getChampsTableArchers());
            
            jcbPlacementCriterion.setEnabled(jcbClassementCriterion.isSelected());
			jcbClassementCriterion.setEnabled(!jcbPlacementCriterion.isSelected());
            
			if(jcbPlacementCriterion.isEnabled())
				jcbPlacementCriterion.setEnabled(!parent.getReglement().isOfficialReglement());
			if(jcbClassementCriterion.isEnabled())
				jcbClassementCriterion.setEnabled(!parent.getReglement().isOfficialReglement());
            jcbWinFFTACode.setEnabled(!parent.getReglement().isOfficialReglement());
        }
        
        if(lock) {
        	jcbPlacementCriterion.setEnabled(false);
        	jcbClassementCriterion.setEnabled(false);
        	jcbClassementEquipeCriterion.setEnabled(false);
        	jcbWinFFTACode.setEnabled(false);
        	jcbSortOrder.setEnabled(false);
        }
    }
    
    public Criterion showCriterionDialog() {
    	completePanel();
    	
    	pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    	
    	return criterion;
    }

    /**
	 * @return  Renvoie criterion.
	 */
    public Criterion getCriterion() {
        return criterion;
    }

    /**
	 * @param criterion  criterion à définir.
	 */
    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }
    
    /**
	 * @return lock
	 */
	public boolean getLock() {
		return lock;
	}

	/**
	 * @param lock lock à définir
	 */
	public void setLock(boolean lock) {
		this.lock = lock;
	}

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
            if(criterion == null) {
                criterion = new Criterion(jtfCode.getText());
                //criterion.setReglementParent(parent.getReglement());

                parent.getReglement().getListCriteria().add(criterion);
            }
            
            criterion.setReglement(parent.getReglement());
            criterion.setSortOrder((jcbSortOrder.getSelectedIndex() == 1) ? Criterion.SORT_DESC : Criterion.SORT_ASC);
            criterion.setLibelle(jtfLibelle.getText());
            criterion.setPlacement(jcbPlacementCriterion.isSelected());
            criterion.setClassement(jcbClassementCriterion.isSelected());
            criterion.setClassementEquipe(jcbClassementEquipeCriterion.isSelected());
            criterion.setChampsTableArchers((String)jcbWinFFTACode.getSelectedItem());
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
        	criterion = null;
        	
            setVisible(false);
        }
    }

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == jcbClassementCriterion) {
			jcbPlacementCriterion.setEnabled(jcbClassementCriterion.isSelected());
		} else if(e.getSource() == jcbPlacementCriterion) {
			jcbClassementCriterion.setEnabled(!jcbPlacementCriterion.isSelected());
		}
	}
}
