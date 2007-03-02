/**
 * 
 */
package ajinteractive.concours.dialog;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ajinteractive.concours.*;
import ajinteractive.standard.java2.AJToolKit;

/**
 * @author Aurelien
 *
 */
public class CriterionDialog extends JDialog implements ActionListener {
    
    private ConfigurationDialog parent;
    
    private Criterion criterion;
    
    private JLabel jlIndex = new JLabel();
    private JLabel jlCode = new JLabel();
    private JLabel jlLibelle = new JLabel();
    private JLabel jlSortOrder = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JComboBox jcbSortOrder = new JComboBox();
    private JCheckBox jcbPlacementCriterion = new JCheckBox();
    private JCheckBox jcbClassementCriterion = new JCheckBox();
    
    private JButton jbValider = new JButton();
    private JButton jbAnnuler = new JButton();
    
    public CriterionDialog(ConfigurationDialog parent) {
        this(parent, null);
    }
    
    public CriterionDialog(ConfigurationDialog parent, Criterion criterion) {
        super(parent, "", true);
        
        this.parent = parent;
        this.criterion = criterion;
        
        init();
        affectLibelle();
        completePanel();
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void init() {
        //Layout Manager
        GridBagLayout gridbag   = new GridBagLayout();
        GridBagConstraints c    = new GridBagConstraints();
        
        JPanel jpGeneral = new JPanel();
        JPanel jpOperation = new JPanel();
        
        jpGeneral.setLayout(gridbag);
        jpOperation.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;     //Défaut,Haut
        AJToolKit.addComponentIntoGrid(jpGeneral, jlIndex, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jlCode, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpGeneral, jtfCode, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jlLibelle, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpGeneral, jtfLibelle, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jlSortOrder, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpGeneral, jcbSortOrder, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jcbPlacementCriterion, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jcbClassementCriterion, gridbag, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    private void affectLibelle() {
        setTitle(ConcoursJeunes.ajrLibelle.getResourceString("criterion.titre"));
        
        jlCode.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.code"));
        jlLibelle.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.libelle"));
        jlSortOrder.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri"));
        jcbSortOrder.removeAllItems();
        jcbSortOrder.addItem(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri.asc"));
        jcbSortOrder.addItem(ConcoursJeunes.ajrLibelle.getResourceString("criterion.ordretri.desc"));
        jcbPlacementCriterion.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.placement"));
        jcbClassementCriterion.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.classement"));
        
        jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
        jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
    }
    
    private void completePanel() {
        if(criterion != null) {
            jtfCode.setText(criterion.getCode());
            jtfCode.setEditable(false);
            jtfLibelle.setText(criterion.getLibelle());
            
            jcbSortOrder.setSelectedIndex((criterion.getSortOrder() > 0) ? 0 : 1);
            
            jcbPlacementCriterion.setSelected(criterion.isPlacement());
            jcbClassementCriterion.setSelected(criterion.isClassement());
            
            jcbSortOrder.setEnabled(!parent.getWorkConfiguration().isOfficialProfile());
            jcbPlacementCriterion.setEnabled(!parent.getWorkConfiguration().isOfficialProfile());
            jcbClassementCriterion.setEnabled(!parent.getWorkConfiguration().isOfficialProfile());
        }
    }

    /**
     * @return Renvoie criterion.
     */
    public Criterion getCriterion() {
        return criterion;
    }

    /**
     * @param criterion criterion à définir.
     */
    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
            if(criterion == null) {
                criterion = new Criterion(jtfCode.getText());

                parent.getWorkConfiguration().getCriteriaPopulation().put(criterion, new ArrayList<CriterionElement>());
                parent.getWorkConfiguration().getListCriteria().add(criterion);
            }
            criterion.setSortOrder((jcbSortOrder.getSelectedIndex() == 1) ? Criterion.SORT_DESC : Criterion.SORT_ASC);
            criterion.setLibelle(jtfLibelle.getText());
            criterion.setPlacement(jcbPlacementCriterion.isSelected());
            criterion.setClassement(jcbClassementCriterion.isSelected());
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
            setVisible(false);
        }
    }

}
