/**
 * 
 */
package ajinteractive.concours.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ajinteractive.concours.*;
import ajinteractive.standard.java2.*;

/**
 * @author Aurelien
 *
 */
public class CriterionElementDialog extends JDialog implements ActionListener {
    
    private ConfigurationDialog parent;
    private Criterion criterion;
    private CriterionElement criterionIndividu;
    
    private JLabel jlCode = new JLabel();
    private JLabel jlLibelle = new JLabel();
    private JTextField jtfCode = new JTextField(10);
    private JTextField jtfLibelle = new JTextField(20);
    private JCheckBox jcbActive = new JCheckBox("", true);
    
    private JButton jbValider = new JButton();
    private JButton jbAnnuler = new JButton();

    public CriterionElementDialog(ConfigurationDialog parent, Criterion criterion) {
        this(parent, criterion, null);
    }
    public CriterionElementDialog(ConfigurationDialog parent, Criterion criterion, CriterionElement criterionIndividu) {
        super(parent, ConcoursJeunes.ajrLibelle.getResourceString("criterion.titre"), true);
        
        this.parent = parent;
        this.criterion = criterion;
        this.criterionIndividu = criterionIndividu;
        
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
        AJToolKit.addComponentIntoGrid(jpGeneral, jlCode, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpGeneral, jtfCode, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jlLibelle, gridbag, c);
        AJToolKit.addComponentIntoGrid(jpGeneral, jtfLibelle, gridbag, c);
        c.gridy++;
        AJToolKit.addComponentIntoGrid(jpGeneral, jcbActive, gridbag, c);
        
        jpOperation.add(jbValider);
        jpOperation.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpGeneral, BorderLayout.CENTER);
        getContentPane().add(jpOperation, BorderLayout.SOUTH);
    }
    
    private void affectLibelle() {
        jlCode.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.code"));
        jlLibelle.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.libelle"));
        jcbActive.setText(ConcoursJeunes.ajrLibelle.getResourceString("criterion.active"));
        
        jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
        jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
    }
    
    private void completePanel() {
        if(criterionIndividu != null) {
            jtfCode.setText(criterionIndividu.getCode());
            jtfLibelle.setText(criterionIndividu.getLibelle());
                       
            jcbActive.setSelected(criterionIndividu.isActive());
            
            jtfCode.setEditable(!parent.getWorkConfiguration().isOfficialProfile());
            jcbActive.setEnabled(!parent.getWorkConfiguration().isOfficialProfile());
        }
    }

    /**
     * @return Renvoie criterionIndividu.
     */
    public CriterionElement getCriterionIndividu() {
        return criterionIndividu;
    }

    /**
     * @param criterionIndividu criterionIndividu à définir.
     */
    public void setCriterionIndividu(CriterionElement criterionIndividu) {
        this.criterionIndividu = criterionIndividu;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbValider) {
            if(criterionIndividu == null) {
                criterionIndividu = new CriterionElement();
                parent.getWorkConfiguration().getCriteriaPopulation().get(criterion).add(criterionIndividu);
            }
            
            criterionIndividu.setCode(jtfCode.getText());
            criterionIndividu.setLibelle(jtfLibelle.getText());
            criterionIndividu.setActive(jcbActive.isSelected());
            
            setVisible(false);
        } else if(e.getSource() == jbAnnuler) {
            setVisible(false);
        }
    }
}
