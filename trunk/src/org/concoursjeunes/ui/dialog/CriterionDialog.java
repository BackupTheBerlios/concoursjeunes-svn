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

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.LocalizableString;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.Reglement;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

/**
 * Boite de dialogue de gestion des critère de distinction des archers
 * @author Aurélien JEOFFRAY
 */
@Localizable(textMethod="setTitle",value="criterion.titre")
public class CriterionDialog extends JDialog implements ActionListener, ChangeListener {
    
	private AjResourcesReader localisation;
	
    private Criterion criterion = new Criterion();
    
    private BindingGroup criterionBinding = new BindingGroup();
    
    private JLabel jlIndex = new JLabel();
    @Localizable("criterion.code")
    private JLabel jlCode = new JLabel();
    @Localizable("criterion.libelle")
    private JLabel jlLibelle = new JLabel();
    @Localizable("criterion.ordretri")
    private JLabel jlSortOrder = new JLabel();
    @Localizable("criterion.winfftacode.libelle")
    private JLabel jlWinFFTACode = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JComboBox jcbSortOrder = new JComboBox();
    @Localizable("criterion.placement")
    private JCheckBox jcbPlacementCriterion = new JCheckBox();
    @Localizable("criterion.classement")
    private JCheckBox jcbClassementCriterion = new JCheckBox();
    @Localizable("criterion.classementequipe")
    private JCheckBox jcbClassementEquipeCriterion = new JCheckBox();
    private JComboBox jcbWinFFTACode = new JComboBox();
    
    @Localizable("criterion.ordretri.asc")
    private final LocalizableString lsSortOrderAsc = new LocalizableString();
    @Localizable("criterion.ordretri.desc")
    private final LocalizableString lsSortOrderDesc = new LocalizableString();
    
    @Localizable("bouton.valider")
    private JButton jbValider = new JButton();
    @Localizable("bouton.annuler")
    private JButton jbAnnuler = new JButton();
    
    private boolean creationMode = false;
    private boolean editable = false;
    
    private DefaultDialogReturn returnAction = DefaultDialogReturn.CANCEL;
    
    /**
     * Intialise une boite de dialogue de paramétrage de critère de réglement
     * 
     * @param parent la boite de dialogue parente
     * @param reglement le réglement auquel doit être associé le critère
     * @param localisation le fichier de localisation de l'interface
     */
    public CriterionDialog(JDialog parent, Reglement reglement, AjResourcesReader localisation) {
        this(parent, reglement, null, localisation);
    }
    
    /**
     * Intialise une boite de dialogue de paramétrage de critère de réglement avec un critère fournit en paramétre.<br>
     * Le critère associé doit être associé à un réglement
     * 
     * @param parent la boite de dialogue parente
     * @param criterion le critère à afficher/modifier
     * @param localisation le fichier de localisation de l'interface
     */
    public CriterionDialog(JDialog parent, Criterion criterion, AjResourcesReader localisation) {
    	this(parent, null, criterion, localisation);
    }
    
    /**
     * Intialise une boite de dialogue de paramétrage de critère de réglement avec un critère fournit en paramêtre.<br>
     * Si le critère passé en paramétre est différent de null, alors le réglement est ignoré
     * 
     * @param parent la boite de dialogue parente
     * @param reglement le réglement auquel doit être associé le critère
     * @param criterion le critère à afficher/modifier
     * @param localisation le fichier de localisation de l'interface
     */
    private CriterionDialog(JDialog parent, Reglement reglement, Criterion criterion, AjResourcesReader localisation) {
        super(parent, "", true); //$NON-NLS-1$
        
        this.localisation = localisation;
        if(reglement != null)
        	this.criterion.setReglement(reglement);
        
        if(criterion != null)
        	this.criterion = criterion;
        else
        	creationMode = true;
        
        
        init();
        affectLibelle();
    }
    
    /**
     * 
     *
     */
    private void init() {
        //Layout Manager
        GridBagConstraints c = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel jpGeneral = new JPanel();
        JPanel jpOperation = new JPanel();
        
        jpOperation.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jcbSortOrder.addItem(lsSortOrderAsc);
        jcbSortOrder.addItem(lsSortOrderDesc);
        
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
        Localizator.localize(this, localisation);
    }
    
    /**
     * 
     *
     */
    private void completePanel() {
    	//On annule le précédent binding
    	if(criterionBinding != null)
    		criterionBinding.unbind();
    	
    	criterionBinding = new BindingGroup();

    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("code"), jtfCode, BeanProperty.create("text")));  //$NON-NLS-1$//$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("libelle"), jtfLibelle, BeanProperty.create("text")));  //$NON-NLS-1$//$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("classement"), jcbClassementCriterion, BeanProperty.create("selected")));  //$NON-NLS-1$//$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("placement"), jcbPlacementCriterion, BeanProperty.create("selected")));  //$NON-NLS-1$//$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("classementEquipe"), jcbClassementEquipeCriterion, BeanProperty.create("selected")));  //$NON-NLS-1$//$NON-NLS-2$
    	//criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("reglement.officialReglement"), jcbWinFFTACode, BeanProperty.create("enabled")));  //$NON-NLS-1$//$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.create("champsTableArchers"), jcbWinFFTACode, BeanProperty.create("selectedItem"))); //$NON-NLS-1$ //$NON-NLS-2$
    	Binding<Criterion, Integer, JComboBox, Integer> sortBinding = Bindings.<Criterion, Integer, JComboBox, Integer>createAutoBinding(UpdateStrategy.READ, criterion, BeanProperty.<Criterion, Integer>create("sortOrder"), jcbSortOrder, BeanProperty.<JComboBox, Integer>create("selectedIndex"));  //$NON-NLS-1$//$NON-NLS-2$
    	sortBinding.setConverter(new Converter<Integer, Integer>() {

			@Override
			public Integer convertForward(Integer source) {
				return source == Criterion.SORT_ASC ? 0 : 1;
			}

			@Override
			public Integer convertReverse(Integer target) {
				return target == 0 ? Criterion.SORT_ASC : Criterion.SORT_DESC;
			}
    		
		});
    	criterionBinding.addBinding(sortBinding);
    	
		jcbPlacementCriterion.setEnabled(criterion.isClassement() && editable);
    	jcbClassementCriterion.setEnabled(editable);
    	jcbClassementEquipeCriterion.setEnabled(editable);
    	jcbWinFFTACode.setEnabled(editable);
    	jcbSortOrder.setEnabled(editable);
    	
    	criterionBinding.bind();
    	
        jtfCode.setEditable(creationMode);
    }
    
    /**
     * Affiche la boite de dialogue d'édition de critère
     * 
     * @return le code de retour de l'édition
     */
    public DefaultDialogReturn showCriterionDialog() {
    	completePanel();
    	
    	pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    	
    	return returnAction;
    }

    /**
     * Retourne le critère associé à la boite de dialogue
     * 
	 * @return le critère associé
	 */
    public Criterion getCriterion() {
        return criterion;
    }

    /**
     * Définit le critère associé à la boite de dialogue
     * 
	 * @param criterion le critère associé
	 */
    public void setCriterion(Criterion criterion) {
    	if(criterion != null)
    		this.criterion = criterion;
    }
    
    /**
     * Indique si la boite de dialogue est vérouillé en édition
     * 
	 * @return lock true si la boite de dialogue est vérouillé
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Définit si la boite de dialogue est vérouillé en édition
	 * 
	 * @param editable true si la boite de dialogue est vérouillé
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
        	if(criterionBinding != null) {
            	for(Binding<Criterion, ?, ?, ?> binding : criterionBinding.getBindings()) { 
            		binding.save();
            	}
        	}
            //criterion.setSortOrder((jcbSortOrder.getSelectedIndex() == 1) ? Criterion.SORT_DESC : Criterion.SORT_ASC);
            //criterion.setLibelle(jtfLibelle.getText());
            //criterion.setPlacement(jcbPlacementCriterion.isSelected());
            //criterion.setClassement(jcbClassementCriterion.isSelected());
            //criterion.setClassementEquipe(jcbClassementEquipeCriterion.isSelected());
            //criterion.setChampsTableArchers((String)jcbWinFFTACode.getSelectedItem());
            
            returnAction = DefaultDialogReturn.OK;
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
        	returnAction = DefaultDialogReturn.CANCEL;
        	
            setVisible(false);
        }
    }

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if(editable) {
			if(e.getSource() == jcbClassementCriterion) {
				jcbPlacementCriterion.setEnabled(jcbClassementCriterion.isSelected());
			} else if(e.getSource() == jcbPlacementCriterion) {
				jcbClassementCriterion.setEnabled(!jcbPlacementCriterion.isSelected());
			}
		}
	}
}
