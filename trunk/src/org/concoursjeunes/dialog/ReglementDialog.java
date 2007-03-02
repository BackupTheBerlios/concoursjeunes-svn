/**
 * 
 */
package org.concoursjeunes.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.CriteriaSetLibelle;
import org.concoursjeunes.Reglement;

import ajinteractive.standard.java2.GridbagComposer;
import ajinteractive.standard.java2.NumberDocument;

/**
 * @author aurelien
 *
 */
public class ReglementDialog extends JDialog {
	
	private Reglement reglement;
	
	private JLabel jlReglementName = new JLabel();
	private JComboBox jcbReglementName = new JComboBox();
	
	private JLabel jlNbSerie            = new JLabel();
	private JLabel jlNbVoleeParSerie    = new JLabel();
	private JLabel jlNbFlecheParVolee   = new JLabel();
	private JLabel jlNbMembresEquipe    = new JLabel();
	private JLabel jlNbMembresRetenu    = new JLabel();
	
	private JLabel jlNbDB               = new JLabel();
	private JButton jbAddCriteria       = new JButton();
	private JButton jbAddCriteriaMember = new JButton();
	private JButton jbUpElement         = new JButton();
	private JButton jbDownElement       = new JButton();
	private JButton jbRemoveElement     = new JButton();
	private DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("criteres"); //$NON-NLS-1$
	private DefaultTreeModel treeModel  = new DefaultTreeModel(treeRoot);
	private JTree treeCriteria       = new JTree(treeModel);
	
	private JTextField jtfNbSerie       = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbVoleeParSerie = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbFlecheParVolee = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbMembresEquipe = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$
	private JTextField jtfNbMembresRetenu = new JTextField(new NumberDocument(false, false), "", 3); //$NON-NLS-1$

	private JTable jtDistanceBlason         = new JTable();
	private JScrollPane jspDistanceBlason   = new JScrollPane();
	
	private CriteriaSet[] differentiationCriteria;
	
	public ReglementDialog(JFrame parentframe) {
		super(parentframe, true);
		
		init();
		
		pack();
		setVisible(true);
	}
	
	private void init() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("reglement.general.title", initGeneral());
		tabbedPane.addTab("reglement.criteres.title", initCriteria());
		tabbedPane.addTab("reglement.categories.title", initCategories());
	}
	
	private JPanel initGeneral() {
		GridBagConstraints c    = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel panel = new JPanel();
		
		gridbagComposer.setParentPanel(panel);
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlReglementName, c);
		gridbagComposer.addComponentIntoGrid(jcbReglementName, c);
		c.gridy++; c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(new JSeparator(), c);
		c.gridy++; c.fill = GridBagConstraints.NONE;
		gridbagComposer.addComponentIntoGrid(jlNbSerie, c);
		gridbagComposer.addComponentIntoGrid(jtfNbSerie, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbVoleeParSerie, c);
		gridbagComposer.addComponentIntoGrid(jtfNbVoleeParSerie, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbFlecheParVolee, c);
		gridbagComposer.addComponentIntoGrid(jtfNbFlecheParVolee, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbMembresEquipe, c);
		gridbagComposer.addComponentIntoGrid(jtfNbMembresEquipe, c);
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlNbMembresRetenu, c);
		gridbagComposer.addComponentIntoGrid(jtfNbMembresRetenu, c);
		
		return panel;
	}
	
	private JPanel initCriteria() {
		JPanel jpDifCriteria = new JPanel();
		
		JPanel jpOperations = new JPanel();
		JScrollPane jspCriteres = new JScrollPane();
		
		jpOperations.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpOperations.add(jbAddCriteria);
		jpOperations.add(jbAddCriteriaMember);
		jpOperations.add(jbUpElement);
		jpOperations.add(jbDownElement);
		jpOperations.add(jbRemoveElement);
		jspCriteres.setViewportView(treeCriteria);
		jspCriteres.setPreferredSize(new Dimension(300, 270));
		jpDifCriteria.add(jpOperations, BorderLayout.NORTH);
		jpDifCriteria.add(jspCriteres, BorderLayout.CENTER);
		
		return jpDifCriteria;
	}
	
	private JPanel initCategories() {
		GridBagConstraints c    = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel jpConcours = new JPanel();
		
		jspDistanceBlason.setPreferredSize(new Dimension(300, 250));
		jtDistanceBlason.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jtDistanceBlason.setPreferredScrollableViewportSize(new Dimension(350, 200));
		
		gridbagComposer.setParentPanel(jpConcours);
		c.gridy = 0; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNbDB, c);
		c.gridy++; c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(jspDistanceBlason, c);
		
		return jpConcours;
	}
	
	private void completeCategories() {
		jtDistanceBlason.setModel(createTableModel());
	}
	
	private DefaultTableModel createTableModel() {
		DefaultTableModel dtm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				if(col == 0 || reglement.isOfficialReglement())
					return false;
				return true;
			}
		};
		dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.scna")); //$NON-NLS-1$
		for(int i = 0; i < reglement.getNbSerie(); i++)
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + (i + 1)); //$NON-NLS-1$ //$NON-NLS-2$
		dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason")); //$NON-NLS-1$

		differentiationCriteria = CriteriaSet.listeDifferentiationCriteria(reglement, reglement.getPlacementFilter());

		for(int i = 0; i < differentiationCriteria.length; i++) {
			Object[] row = new String[reglement.getNbSerie() + 2];
			CriteriaSetLibelle scnaLib = new CriteriaSetLibelle(differentiationCriteria[i]);
			row[0] = scnaLib.toString();

			for(int j = 1; j < reglement.getNbSerie() + 1; j++) {
				if(reglement.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]) != null)
					row[j] = "" + reglement.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]).getDistance()[j-1]; //$NON-NLS-1$
				else
					row[j] = "0"; //$NON-NLS-1$
			}
			if(reglement.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]) != null)
				row[reglement.getNbSerie() + 1] = "" + reglement.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]).getBlason(); //$NON-NLS-1$
			else
				row[reglement.getNbSerie() + 1] = "0"; //$NON-NLS-1$
			dtm.addRow(row);
		}

		return dtm;
	}
	
	/**
	 * generere les ligne de distance et blason par code scna
	 *
	 */
	private void generateSCNA_DBRow(Reglement configuration) {
		DefaultTableModel dtm = (DefaultTableModel)this.jtDistanceBlason.getModel();

		//supprime toutes les lignes
		while(dtm.getRowCount() > 0)
			dtm.removeRow(0);

		if(dtm.getColumnCount() < configuration.getNbSerie() + 2) {
			jtDistanceBlason.removeColumn(jtDistanceBlason.getColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason"))); //$NON-NLS-1$
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + configuration.getNbSerie()); //$NON-NLS-1$ //$NON-NLS-2$
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason")); //$NON-NLS-1$
		}

		differentiationCriteria = CriteriaSet.listeDifferentiationCriteria(reglement, reglement.getPlacementFilter());

		for(int i = 0; i < differentiationCriteria.length; i++) {
			Object[] row = new String[configuration.getNbSerie() + 2];
			CriteriaSetLibelle libelle = new CriteriaSetLibelle(differentiationCriteria[i]);
			row[0] = libelle.toString();
			for(int j = 1; j < configuration.getNbSerie() + 1; j++) {
				if(configuration.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]) != null)
					row[j] = "" + configuration.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]).getDistance()[j-1]; //$NON-NLS-1$
				else
					row[j] = "0"; //$NON-NLS-1$
			}
			if(configuration.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]) != null)
				row[configuration.getNbSerie() + 1] = "" + configuration.getCorrespondanceCriteriaSet_DB(differentiationCriteria[i]).getBlason(); //$NON-NLS-1$
			else
				row[configuration.getNbSerie() + 1] = "0"; //$NON-NLS-1$
			dtm.addRow(row);
		}
	}

	/**
	 * @return reglement
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * @param reglement reglement à définir
	 */
	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}
	
	
}