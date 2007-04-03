/**
 * 
 */
package org.concoursjeunes.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.tree.TreePath;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.CriteriaSetLibelle;
import org.concoursjeunes.Criterion;
import org.concoursjeunes.CriterionElement;
import org.concoursjeunes.DistancesEtBlason;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.ReglementFactory;

import ajinteractive.standard.java2.GridbagComposer;
import ajinteractive.standard.java2.NumberDocument;

import static org.concoursjeunes.ConcoursJeunes.userRessources;

/**
 * @author aurelien
 *
 */
public class ReglementDialog extends JDialog implements ActionListener, ItemListener {
	
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
	
	private JButton jbValider				= new JButton();
	private JButton jbAnnuler				= new JButton();
	
	private CriteriaSet[] differentiationCriteria;
	
	public ReglementDialog(JFrame parentframe) {
		super(parentframe, true);
		
		init();
		affectLibelle();
		completePanel();
		
		pack();
	}
	
	private void init() {
		JPanel panel = new JPanel();
		JPanel jpAction = new JPanel();
		
		panel.setLayout(new BorderLayout());
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.general.title"), initGeneral());
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.criteres.title"), initCriteria());
		tabbedPane.addTab(ConcoursJeunes.ajrLibelle.getResourceString("reglement.categories.title"), initCategories());
		
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		panel.add(tabbedPane, BorderLayout.CENTER);
		panel.add(jpAction, BorderLayout.SOUTH);
		
		setContentPane(panel);
	}
	
	private JPanel initGeneral() {
		GridBagConstraints c    = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();
		
		JPanel panel = new JPanel();
		
		jcbReglementName.addItemListener(this);
		
		gridbagComposer.setParentPanel(panel);
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlReglementName, c);
		gridbagComposer.addComponentIntoGrid(jcbReglementName, c);
		c.gridy++; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
		gridbagComposer.addComponentIntoGrid(new JSeparator(), c);
		c.gridy++; c.gridwidth = 1; c.fill = GridBagConstraints.NONE;
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
		
		jpDifCriteria.setLayout(new BorderLayout());
		
		jbAddCriteria.setMargin(new Insets(0, 0, 0, 0));
		jbAddCriteria.addActionListener(this);
		jbAddCriteriaMember.setMargin(new Insets(0, 0, 0, 0));
		jbAddCriteriaMember.addActionListener(this);
		jbUpElement.setMargin(new Insets(0, 0, 0, 0));
		jbUpElement.addActionListener(this);
		jbDownElement.setMargin(new Insets(0, 0, 0, 0));
		jbDownElement.addActionListener(this);
		jbRemoveElement.setMargin(new Insets(0, 0, 0, 0));
		jbRemoveElement.addActionListener(this);
		
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
		
		jspDistanceBlason.setPreferredSize(new Dimension(400, 250));
		jtDistanceBlason.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jtDistanceBlason.setPreferredScrollableViewportSize(new Dimension(450, 200));
		
		gridbagComposer.setParentPanel(jpConcours);
		c.gridy = 0; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlNbDB, c);
		c.gridy++; c.fill = GridBagConstraints.BOTH;
		gridbagComposer.addComponentIntoGrid(jspDistanceBlason, c);
		
		return jpConcours;
	}
	
	private void affectLibelle() {
		jbValider.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.valider"));
		jbAnnuler.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.annuler"));
		
		jlReglementName.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.name"));
		jlNbSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.serie"));
		jlNbVoleeParSerie.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.voleeparserie"));
		jlNbFlecheParVolee.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.flecheparvolee"));
		jlNbMembresEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.membresmax"));
		jlNbMembresRetenu.setText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.selectionmax"));
		
		jbAddCriteria.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteria"))); //$NON-NLS-1$
		jbAddCriteria.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.addcriteria")); //$NON-NLS-1$
		jbAddCriteriaMember.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.addcriteriamember"))); //$NON-NLS-1$
		jbAddCriteriaMember.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("creglement.addcriteriamember")); //$NON-NLS-1$
		jbUpElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.upelement"))); //$NON-NLS-1$
		jbUpElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.upelement")); //$NON-NLS-1$
		jbDownElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.downelement"))); //$NON-NLS-1$
		jbDownElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.downelement")); //$NON-NLS-1$
		jbRemoveElement.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.removeelement"))); //$NON-NLS-1$
		jbRemoveElement.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("reglement.removeelement")); //$NON-NLS-1$
	}
	
	private void completePanel() {
		completeGeneral();
		completeCriteria();
		completeCategories();
	}
	
	private void completeGeneral() {
		jcbReglementName.removeAllItems();
		
		String[] availableReglements = userRessources.listAvailableReglements();
		if(availableReglements != null) {
			for(String reglementName : availableReglements)
				jcbReglementName.addItem(reglementName);
		}
		jcbReglementName.addItem("---");
		jcbReglementName.addItem("Nouveau Réglement");
		
		if(reglement == null) {
			reglement = ReglementFactory.getReglement((String)jcbReglementName.getItemAt(0));
			if(reglement != null)
				completePanel();
			return;
		}
		
		//jcbReglementName.setSelectedItem(reglement.getName());

		jtfNbSerie.setText(reglement.getNbSerie() + "");
		jtfNbVoleeParSerie.setText(reglement.getNbVoleeParSerie() + "");
		jtfNbFlecheParVolee.setText(reglement.getNbFlecheParVolee() + "");
		jtfNbMembresEquipe.setText(reglement.getNbMembresEquipe() + "");
		jtfNbMembresRetenu.setText(reglement.getNbMembresRetenu() + "");
	}
	
	private void completeCriteria() {
		if(reglement == null)
			return;
		treeRoot.removeAllChildren();
		for(Criterion critere : reglement.getListCriteria()) {
			if(critere != null) {
				DefaultMutableTreeNode dmtnCriteria = new DefaultMutableTreeNode(critere);
				treeRoot.add(dmtnCriteria);

				for(CriterionElement criterionIndividu : critere.getCriterionElements()) {
					DefaultMutableTreeNode dmtnCriteriaIndiv = new DefaultMutableTreeNode(criterionIndividu);
					dmtnCriteria.add(dmtnCriteriaIndiv);
				}
			}
		}
		treeModel.reload();
		jspDistanceBlason.setViewportView(jtDistanceBlason);
	}
	
	private void completeCategories() {
		if(reglement != null)
			jtDistanceBlason.setModel(createTableModel());
	}
	
	private DefaultTableModel createTableModel() {
		assert reglement != null : "ne peut creer le tableau avec un reglement null";
		
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

		differentiationCriteria = CriteriaSet.listCriteriaSet(reglement, reglement.getPlacementFilter());

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
	private void generateSCNA_DBRow() {
		DefaultTableModel dtm = (DefaultTableModel)this.jtDistanceBlason.getModel();

		//supprime toutes les lignes
		while(dtm.getRowCount() > 0)
			dtm.removeRow(0);

		if(dtm.getColumnCount() < reglement.getNbSerie() + 2) {
			jtDistanceBlason.removeColumn(jtDistanceBlason.getColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason"))); //$NON-NLS-1$
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.distance") + " " + reglement.getNbSerie()); //$NON-NLS-1$ //$NON-NLS-2$
			dtm.addColumn(ConcoursJeunes.ajrLibelle.getResourceString("configuration.ecran.concours.blason")); //$NON-NLS-1$
		}

		differentiationCriteria = CriteriaSet.listCriteriaSet(reglement, reglement.getPlacementFilter());

		for(int i = 0; i < differentiationCriteria.length; i++) {
			Object[] row = new String[reglement.getNbSerie() + 2];
			CriteriaSetLibelle libelle = new CriteriaSetLibelle(differentiationCriteria[i]);
			row[0] = libelle.toString();
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
	
	public Reglement showReglementDialog() {
		setVisible(true);
		return reglement;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == this.jbValider) {
			if(reglement == null)
				reglement = new Reglement();
			reglement.setNbSerie(Integer.parseInt(jtfNbSerie.getText()));
			reglement.setNbVoleeParSerie(Integer.parseInt(jtfNbVoleeParSerie.getText()));
			reglement.setNbFlecheParVolee(Integer.parseInt(jtfNbFlecheParVolee.getText()));
			reglement.setNbMembresEquipe(Integer.parseInt(jtfNbMembresEquipe.getText()));
			reglement.setNbMembresRetenu(Integer.parseInt(jtfNbMembresRetenu.getText()));
			
			reglement.setCorrespondanceCriteriaSet_DB(new Hashtable<CriteriaSet, DistancesEtBlason>());
			for(int i = 0; i < jtDistanceBlason.getRowCount(); i++) {
				int[] distances = new int[reglement.getNbSerie()];
				for(int j = 0; j < distances.length; j++) {
					if(j < jtDistanceBlason.getModel().getColumnCount() - 2)
						distances[j] = Integer.parseInt((String)this.jtDistanceBlason.getModel().getValueAt(i, j+1));
					else
						distances[j] = 0;
				}
				reglement.putCorrespondanceCriteriaSet_DB(differentiationCriteria[i],
						new DistancesEtBlason(distances, 
								Integer.parseInt((String)this.jtDistanceBlason.getModel().getValueAt(i, jtDistanceBlason.getModel().getColumnCount() - 1))));
			}
			
			setVisible(false);
		} else if(source == jbAnnuler) {
			reglement = null;
			setVisible(false);
		} if(source == jbAddCriteria) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
			if(dmtn != null) {
				CriterionDialog cd = new CriterionDialog(this);

				if(cd.getCriterion() != null) {
					DefaultMutableTreeNode dmtnCrit = new DefaultMutableTreeNode(cd.getCriterion());

					dmtn.add(dmtnCrit);

					treeModel.reload();
				}
			}
		} else if(source == jbAddCriteriaMember) { 
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
			if(dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if(dmtnObj instanceof Criterion) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					CriterionElementDialog cpd = new CriterionElementDialog(this, (Criterion)dmtnObj);

					if(cpd.getCriterionIndividu() != null) {
						DefaultMutableTreeNode dmtnIndiv = new DefaultMutableTreeNode(cpd.getCriterionIndividu());

						dmtn.add(dmtnIndiv);

						treeModel.reload();
						treeCriteria.setSelectionPath(selectedPath);
						//treeCriteria.expandPath(selectedPath);

						generateSCNA_DBRow();
					}
				}
			}
		} else if(source == jbUpElement) { 
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
			if(dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if(dmtnObj instanceof Criterion) {
					int curIndex = reglement.getListCriteria().indexOf(dmtnObj);
					if(curIndex > 0) {
						reglement.getListCriteria().set(curIndex, reglement.getListCriteria().get(curIndex-1));
						reglement.getListCriteria().set(curIndex-1, (Criterion)dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
					dmtnRoot.insert(dmtn, curIndex - 1);

					treeModel.reload();

					generateSCNA_DBRow();
				} else if(dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
					selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion)dmtnParent.getUserObject();

					int curIndex = curCriterion.getCriterionElements().indexOf(dmtnObj);
					if(curIndex > 0) {
						ArrayList<CriterionElement> alce = curCriterion.getCriterionElements(); 

						CriterionElement precElement = alce.get(curIndex-1);
						CriterionElement curElement = (CriterionElement)dmtnObj;
						alce.set(curIndex, precElement);
						alce.set(curIndex-1, curElement);
					}
					treeModel.removeNodeFromParent(dmtn);

					dmtnParent.insert(dmtn, curIndex - 1);

					treeModel.reload();

					generateSCNA_DBRow();
				}
			}
		} else if(source == jbDownElement) { 
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
			if(dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if(dmtnObj instanceof Criterion) {
					int curIndex = reglement.getListCriteria().indexOf(dmtnObj);
					if(curIndex < reglement.getListCriteria().size() - 1) {
						reglement.getListCriteria().set(curIndex, reglement.getListCriteria().get(curIndex+1));
						reglement.getListCriteria().set(curIndex+1, (Criterion)dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					DefaultMutableTreeNode dmtnRoot = (DefaultMutableTreeNode)treeCriteria.getPathForRow(0).getLastPathComponent();
					dmtnRoot.insert(dmtn, curIndex + 1);

					treeModel.reload();

					generateSCNA_DBRow();
				} else if(dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
					selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion)dmtnParent.getUserObject();

					int curIndex = curCriterion.getCriterionElements().indexOf(dmtnObj);
					if(curIndex < curCriterion.getCriterionElements().size() - 1) {
						curCriterion.getCriterionElements().set(curIndex, curCriterion.getCriterionElements().get(curIndex+1));
						curCriterion.getCriterionElements().set(curIndex+1, (CriterionElement)dmtnObj);
					}
					treeModel.removeNodeFromParent(dmtn);

					dmtnParent.insert(dmtn, curIndex + 1);

					treeModel.reload();

					generateSCNA_DBRow();
				}
			}
		} else if(source == jbRemoveElement) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeCriteria.getLastSelectedPathComponent();
			if(dmtn != null) {
				Object dmtnObj = dmtn.getUserObject();
				if(dmtnObj instanceof Criterion) {
					reglement.getListCriteria().remove(dmtnObj);

					treeModel.removeNodeFromParent(dmtn);

					generateSCNA_DBRow();
				} else if(dmtnObj instanceof CriterionElement) {
					TreePath selectedPath = treeCriteria.getSelectionPath();
					DefaultMutableTreeNode dmtnParent = (DefaultMutableTreeNode)
					selectedPath.getParentPath().getLastPathComponent();

					Criterion curCriterion = (Criterion)dmtnParent.getUserObject();
					curCriterion.getCriterionElements().remove(dmtnObj);

					treeModel.removeNodeFromParent(dmtn);

					generateSCNA_DBRow();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if(jcbReglementName.getSelectedIndex() < jcbReglementName.getItemCount() - 2) {
			reglement = ReglementFactory.getReglement((String)e.getItem());
			completePanel();
		} else if(jcbReglementName.getItemCount() > 1 && jcbReglementName.getSelectedIndex() == jcbReglementName.getItemCount() - 1) {
			String reglementName = JOptionPane.showInputDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("reglement.general.addreglement")); //$NON-NLS-1$
			reglement = new Reglement(reglementName);
			reglement.save();
			completePanel();
		}
	}
}