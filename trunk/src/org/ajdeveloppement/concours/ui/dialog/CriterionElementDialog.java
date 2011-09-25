/*
 * Copyright 2002-2007 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.DefaultDialogReturn;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.concours.Criterion;
import org.ajdeveloppement.concours.CriterionElement;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 */
public class CriterionElementDialog extends JDialog implements ActionListener {
    
	private AjResourcesReader localisation;
	
    private CriterionElement criterionElement = new CriterionElement();
    
    private BindingGroup criterionBinding = new BindingGroup();
    
    @Localizable("criterion.code")
    private JLabel jlCode = new JLabel();
    @Localizable("criterion.libelle")
    private JLabel jlLibelle = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    @Localizable("criterion.active")
    private JCheckBox jcbActive = new JCheckBox("", true); //$NON-NLS-1$
    
    @Localizable("bouton.valider")
    private JButton jbValider = new JButton();
    @Localizable("bouton.annuler")
    private JButton jbAnnuler = new JButton();
    
    private boolean editable = false;
    
    private DefaultDialogReturn returnAction = DefaultDialogReturn.CANCEL;

    /**
     * Construit la boite de dialogue d'élément de critères
     * 
     * @param parent la boite de dialogue de reglement parent
     * @param criterion le critére parent de l'element
     * @param localisation la source de localisation de la boite de dialogue
     */
    public CriterionElementDialog(JDialog parent, Criterion criterion, AjResourcesReader localisation) {
        this(parent, criterion, null, localisation);
    }
    
    /**
     * Construit la boite de dialogue d'élément de critères
     * 
     * @param parent la boite de dialogue de reglement parent
     * @param criterionElement l'élément à éditer
     * @param localisation la source de localisation de la boite de dialogue
     */
    public CriterionElementDialog(JDialog parent, CriterionElement criterionElement, AjResourcesReader localisation) {
    	 this(parent, null, criterionElement, localisation);
    }
    
    /**
     * Construit la boite de dialogue d'élément de critères
     * 
     * @param parent la boite de dialogue de reglement parent
     * @param criterion le critére parent de l'element
     * @param criterionElement l'element à éditer
     * @param localisation la source de localisation de la boite de dialogue
     */
    private CriterionElementDialog(JDialog parent, Criterion criterion, CriterionElement criterionElement, AjResourcesReader localisation) {
        super(parent, localisation.getResourceString("criterion.titre"), true); //$NON-NLS-1$
        
        this.localisation = localisation;
        if(criterion != null)
	        this.criterionElement.setCriterion(criterion);

        if(criterionElement != null)
        	this.criterionElement = criterionElement;
        
        init();
        affectLabels();
    }
    
    private void init() {
        //Layout Manager
        GridBagConstraints c    = new GridBagConstraints();
        
        GridbagComposer gridbagComposer = new GridbagComposer();
        
        JPanel jpGeneral = new JPanel();
        JPanel jpOperation = new JPanel();
        
        jpOperation.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        gridbagComposer.setParentPanel(jpGeneral);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        gridbagComposer.addComponentIntoGrid(jlCode, c);
        gridbagComposer.addComponentIntoGrid(jtfCode, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jlLibelle, c);
        gridbagComposer.addComponentIntoGrid(jtfLibelle, c);
        c.gridy++;
        gridbagComposer.addComponentIntoGrid(jcbActive, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    private void affectLabels() {
    	Localizator.localize(this, localisation);
    }
    
    private void completePanel() {
    	if(criterionBinding != null)
    		criterionBinding.unbind();
    	
    	criterionBinding = new BindingGroup();
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterionElement, BeanProperty.create("code"), jtfCode, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterionElement, BeanProperty.create("libelle"), jtfLibelle, BeanProperty.create("text"))); //$NON-NLS-1$ //$NON-NLS-2$
    	criterionBinding.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, criterionElement, BeanProperty.create("active"), jcbActive, BeanProperty.create("selected"))); //$NON-NLS-1$ //$NON-NLS-2$
    	
    	criterionBinding.bind();
        
        jtfCode.setEditable(!criterionElement.getCriterion().getReglement().isOfficialReglement()
        		&& editable);
        jcbActive.setEnabled(!criterionElement.getCriterion().getReglement().isOfficialReglement()
        		&& editable);

    }
    
    /**
     * Affiche la boite de dialogue d'édition d'élément de critère
     * 
     * @return le code de retour de la boite de dialogue
     */
    public DefaultDialogReturn showCriterionElementDialog() {
    	completePanel();
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        
        return returnAction;
    }

    /**
	 * @return  Renvoie criterionIndividu.
	 */
    public CriterionElement getCriterionElement() {
        return criterionElement;
    }

    /**
	 * @param criterionElement  criterionIndividu à définir.
	 */
    public void setCriterionElement(CriterionElement criterionElement) {
        this.criterionElement = criterionElement;
    }
    
    /**
     * Indique si la boite de dialogue est vérouillé en édition
     *  
     * @return true si vérouillé
     */
    public boolean isEditable() {
    	return editable;
    }
    
    /**
     * Définit si la boite de dialogue doit être vérouillé en édition
     * 
     * @param editable true si vérouillé
     */
    public void setEditable(boolean editable) {
    	this.editable = editable;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
        	if(criterionBinding != null) {
            	for(Binding<CriterionElement, ?, ?, ?> binding : criterionBinding.getBindings()) { 
            		binding.save();
            	}
        	}
            
            returnAction = DefaultDialogReturn.OK;
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
        	returnAction = DefaultDialogReturn.CANCEL;
        	
            setVisible(false);
        }
    }
}
