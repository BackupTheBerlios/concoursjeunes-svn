/**
 * 
 */
package ajinteractive.concours.dialog;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ajinteractive.concours.*;
/**
 * @author Aurelien Jeoffray
 *
 */
public class LigueLoaderDialog extends JDialog implements ActionListener{
    
    private ArrayList<JCheckBox> jcbLigues = new ArrayList<JCheckBox>();
    private JButton jbValider;
    private JButton jbAnnuler;
    private JRadioButton jcbTout;
    private JRadioButton jcbAucun;
    
    private Hashtable<String, Entite> vLigues = new Hashtable<String, Entite>();
    private Hashtable<String, Entite> vLiguesSelectionne = new Hashtable<String, Entite>();
    
    public LigueLoaderDialog(ConcoursJeunes concoursJeunes) {
        super(concoursJeunes, ConcoursJeunes.ajrLibelle.getResourceString("listeligue.titre"), true);
        
        JLabel jlSelectionLigue = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("listeligue.description"));
        JPanel listeLigues = new JPanel() { 
            Insets insets = new Insets(0, 4, 0, 0); 
            @Override
            public Insets getInsets() { 
                return insets; 
            } 
        };
        JPanel jpAction = new JPanel();

        listeLigues.setBackground(Color.WHITE);
        listeLigues.setLayout(new BoxLayout(listeLigues, BoxLayout.Y_AXIS));
        for(Entite entite : ConcoursJeunes.listeEntite.values()) {
            if(entite.getType() == Entite.LIGUE) {
                
                vLigues.put(entite.getAgrement(), entite);
                vLiguesSelectionne.put(entite.getAgrement(), entite);
                
                JCheckBox cb = new JCheckBox(entite.getNom());
                cb.setBackground(Color.WHITE);
                cb.setSelected(true);
                cb.addActionListener(this);
                cb.setActionCommand(entite.getAgrement());
                
                jcbLigues.add(cb);
                listeLigues.add(cb);
            }
        }
        Entite autre = new Entite();
        autre.setNom("AUTRE");
        autre.setAgrement("null");
        vLigues.put(autre.getAgrement(), autre);
        vLiguesSelectionne.put(autre.getAgrement(), autre);
        JCheckBox cb = new JCheckBox("AUTRE");
        cb.setBackground(Color.WHITE);
        cb.setSelected(true);
        cb.addActionListener(this);
        cb.setActionCommand("null");
        
        jcbLigues.add(cb);
        listeLigues.add(cb);
        
        ButtonGroup bg = new ButtonGroup();
        jcbTout = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("listeligue.tout"), true);
        jcbTout.addActionListener(this);
        jcbAucun = new JRadioButton(ConcoursJeunes.ajrLibelle.getResourceString("listeligue.aucun"));
        jcbAucun.addActionListener(this);
        bg.add(jcbTout);
        bg.add(jcbAucun);
        jbValider = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("listeligue.telecharger"));
        jbValider.addActionListener(this);
        jbAnnuler = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
        jbAnnuler.addActionListener(this);
        
        jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpAction.add(jcbTout);
        jpAction.add(jcbAucun);
        jpAction.add(jbValider);
        jpAction.add(jbAnnuler);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jlSelectionLigue, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(listeLigues), BorderLayout.CENTER);
        getContentPane().add(jpAction, BorderLayout.SOUTH);
        //setMaximumSize(new Dimension(640, 480));
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public Hashtable<String, Entite> getLiguesSelectionne() {
        return vLiguesSelectionne;
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == jcbTout) {
            for(int i = 0; i < jcbLigues.size(); i++) {
                jcbLigues.get(i).setSelected(true);
                if(vLiguesSelectionne.get(jcbLigues.get(i).getActionCommand()) == null)
                    vLiguesSelectionne.put(jcbLigues.get(i).getActionCommand(), vLigues.get(jcbLigues.get(i).getActionCommand()));
            }
        } else if(ae.getSource() == jcbAucun) {
            for(int i = 0; i < jcbLigues.size(); i++) {
                jcbLigues.get(i).setSelected(false);
                vLiguesSelectionne.remove(jcbLigues.get(i).getActionCommand());
            }
        } else if(ae.getSource() instanceof JCheckBox) {
            JCheckBox jcb = (JCheckBox)ae.getSource();
            if(jcb.isSelected()) {
                if(vLiguesSelectionne.get(ae.getActionCommand()) == null)
                    vLiguesSelectionne.put(ae.getActionCommand(), vLigues.get(ae.getActionCommand()));
            } else {
                vLiguesSelectionne.remove(ae.getActionCommand());
            }
        } else if(ae.getSource() == jbValider){
            setVisible(false);
        } else {
            vLiguesSelectionne.clear();
            setVisible(false);
        }
    }
    
    public Vector<Integer> getAutre() {
        Vector<Integer> numLigues = new Vector<Integer>();
        for(Entite entite : vLigues.values()) {
            if(!entite.getAgrement().equals("") && !entite.getAgrement().equals("null"))
                numLigues.add(Integer.parseInt(entite.getAgrement().substring(0,2)));
        }
        Vector<Integer> key = new Vector<Integer>();
        for(Entite tmpent : ConcoursJeunes.listeEntite.values()) {
            if(!tmpent.getAgrement().equals("")) {
                int n = Integer.parseInt(tmpent.getAgrement().substring(0,2));
                
                if(!numLigues.contains(n) && !key.contains(n)) {
                    key.add(n);
                }
            }
        }
        return key;
    }
}
