package ajinteractive.concours.dialog;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * Boite de dialogue de gestion des équipes
 * 
 * @author Aurélien Jeoffray
 * @version 1.0
 *
 */
public class EquipeDialog extends JDialog implements ActionListener, ListSelectionListener, TreeSelectionListener, MouseListener, MouseMotionListener {
    
    private FicheConcoursFrame ficheConcours;
    
    private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();
    private DnDJTree treeEquipes;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("racine");
    
    private JCheckBox cbEquipeClub;
    private JComboBox jcbClubs;
    private JRadioButton rbNom;
    private JRadioButton rbClub;
    
    private AJList ajlConcurrents = new AJList();
    
    private PopupMenu popup;
    
    private Object[] selectionConc;
    
    private JButton jbValider = new JButton();
    private JButton jbAnnuler = new JButton();
    
    private boolean validation = false;

    //private Hashtable<String, Equipe> membresEquipes = new Hashtable<String, Equipe>();
    private Hashtable<DifferentiationCriteria, ArrayList<Concurrent>> hConcurrents = new Hashtable<DifferentiationCriteria, ArrayList<Concurrent>>();
    private Hashtable<DifferentiationCriteria, Hashtable<String, Equipe>> hEquipes = new Hashtable<DifferentiationCriteria, Hashtable<String, Equipe>>();
     
    /**
     * Generation de la boite de dialogue de gestion des équipes
     * 
     * @param ficheConcours - La fiche concours associé
     */
    public EquipeDialog(FicheConcoursFrame ficheConcours) {
        super(ficheConcours.concoursJeunes, true);
        this.ficheConcours = ficheConcours;
        
        //initialisation de l'interface
        init();
        
        //parcours toutes les équipes enregistré
        assert null != ficheConcours.ficheConcours.equipes : "La liste des équipes devrait être != null";
        for(Equipe equipe : ficheConcours.ficheConcours.equipes.list()) {
            //si la catégorie de l'équipe n'existe pas alors la créer
            if(!hEquipes.containsKey(equipe.getDifferentiationCriteria())) {
                hEquipes.put(equipe.getDifferentiationCriteria(), new Hashtable<String, Equipe>());
            }
            //injecter l'equipes dans sa catégorie de classement
            hEquipes.get(equipe.getDifferentiationCriteria()).put(equipe.getNomEquipe(), equipe);
        }

        
        categoriesEquipes();
        //listeConcurrents(new SCNA(0,0,0,0), ArcherList.SORT_BY_CLUBS);
        
        //affichage de la boite de dialogue
        getRootPane().setDefaultButton(jbValider);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * initialisation de l'interface
     *
     */
    private void init() {
        AjResourcesReader ajrLibelle = ConcoursJeunes.ajrLibelle;
        
        JPanel jpEquipes    = new JPanel();
        JPanel jpSelection  = new JPanel();
        JPanel jpValidAnnul = new JPanel();

        JPanel jpCriteres   = new JPanel();
        JPanel jpTri        = new JPanel();
        JPanel jpFiltre     = new JPanel();
        JPanel jpCriteresTri= new JPanel();
        
        JSplitPane splitpane;
        
        ButtonGroup critereTri = new ButtonGroup();
        
        for(Criterion criteria : ficheConcours.ficheConcours.parametre.getListCriteria()) {
            classmentCriteriaCB.put(criteria.getCode(), new JCheckBox(criteria.getLibelle()));
            classmentCriteriaCB.get(criteria.getCode()).addActionListener(this);
            classmentCriteriaCB.get(criteria.getCode()).setActionCommand("filter");
            jpCriteres.add(classmentCriteriaCB.get(criteria.getCode()));
        }
        
        treeModel   = new DefaultTreeModel(treeRoot);
        //treeModel.
        treeEquipes = new DnDJTree(treeModel);
        
        cbEquipeClub= new JCheckBox(ajrLibelle.getResourceString("equipe.contrainte.club"), true);
        jcbClubs    = new JComboBox();
        rbNom       = new JRadioButton(ajrLibelle.getResourceString("radiobutton.nom"));
        rbClub      = new JRadioButton(ajrLibelle.getResourceString("radiobutton.club"), true);
        
        jbValider   = new JButton(ajrLibelle.getResourceString("bouton.valider"));
        jbAnnuler   = new JButton(ajrLibelle.getResourceString("bouton.annuler"));
        
        cbEquipeClub.addActionListener(this);
        jcbClubs.addActionListener(this);
        jcbClubs.setEditable(false);
        rbNom.addActionListener(this);
        rbClub.addActionListener(this);
        jbValider.addActionListener(this);
        jbAnnuler.addActionListener(this);
        treeEquipes.setRootVisible(false);
        treeEquipes.addTreeSelectionListener(this);
        treeEquipes.addMouseListener(this);
        treeEquipes.addMouseMotionListener(this);
        //ajlConcurrents.setDragEnabled(true);
        ajlConcurrents.addMouseListener(this);
        ajlConcurrents.addMouseMotionListener(this);
        ajlConcurrents.addListSelectionListener(this);
        //ajlConcurrents.setTransferHandler(new ObjectTransferHandler());
        
        
        jpEquipes.setLayout(new BorderLayout());
        jpEquipes.add(jpCriteres, BorderLayout.NORTH);
        jpEquipes.add(new JScrollPane(treeEquipes), BorderLayout.CENTER);

        jpFiltre.setLayout(new FlowLayout(FlowLayout.LEFT));
        jpFiltre.add(cbEquipeClub);
        jpFiltre.add(jcbClubs);
        
        critereTri.add(rbNom);
        critereTri.add(rbClub);
        jpCriteresTri.add(rbNom);
        jpCriteresTri.add(rbClub);
        
        
        jpTri.setLayout(new BorderLayout());
        jpTri.add(jpFiltre, BorderLayout.NORTH);
        jpTri.add(jpCriteresTri, BorderLayout.CENTER);
        
        jpSelection.setLayout(new BorderLayout());
        jpSelection.add(jpTri, BorderLayout.NORTH);
        jpSelection.add(new JScrollPane(ajlConcurrents));
        
        jpValidAnnul.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpValidAnnul.add(jbValider);
        jpValidAnnul.add(jbAnnuler);
        
        splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jpEquipes, jpSelection);
        splitpane.setOneTouchExpandable(true);
        splitpane.setResizeWeight(0.25); 
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitpane, BorderLayout.CENTER);
        getContentPane().add(jpValidAnnul, BorderLayout.SOUTH);
        
        popup();
    }
    
    /**
     * Enumere les catégories d'équipes valide sur le concours
     * en fonction des critères de tri.
     *
     */
    private void categoriesEquipes() {
        
        DefaultMutableTreeNode[] dmtnCategorie;
        int firstIndex = -1;
        
        Hashtable<String, Boolean> criteriaFilter = new Hashtable<String, Boolean>();
        for(Map.Entry<String, JCheckBox> cb : classmentCriteriaCB.entrySet()) {
            criteriaFilter.put(cb.getKey(), cb.getValue().isSelected());
        }
        //Donne la liste des codes SCNA filtré
        DifferentiationCriteria[] catList = DifferentiationCriteria.listeDifferentiationCriteria(criteriaFilter);
        
        dmtnCategorie = new DefaultMutableTreeNode[catList.length];
        
        //upprimer tous le noeud de l'arbre
        treeRoot.removeAllChildren();
        
        //Pour chaque categorie
        for(int i = 0; i < catList.length; i++) {
            //test si il existe des archers dans la catégorie
            if(ficheConcours.ficheConcours.archerlist.list(catList[i]).length > 0) {
                if(firstIndex == -1)
                    firstIndex = i;
                //generer le noeud correspondant
                dmtnCategorie[i] = new DefaultMutableTreeNode(new DifferentiationCriteriaLibelle(catList[i]));
                
                //cré les container manquant
                if(!hEquipes.containsKey(catList[i])) {
                    hEquipes.put(catList[i], new Hashtable<String, Equipe>());
                }
                
                //liste les équipes pour chaque catégorie
                for(Equipe equipe : hEquipes.get(catList[i]).values()) {
                    DefaultMutableTreeNode dmtnEquipe = new DefaultMutableTreeNode(equipe);
                    for(Concurrent concurrent : equipe.getMembresEquipe())
                        dmtnEquipe.add(new DefaultMutableTreeNode(concurrent));
                    dmtnCategorie[i].add(dmtnEquipe);
                }
                
                //integrer le noeud à l'arbre
                treeRoot.add(dmtnCategorie[i]);  
            }
        }
        //recharger l'arbre
        treeModel.reload(treeRoot);
        
        if(firstIndex != -1) {
            TreePath treePath = new TreePath(dmtnCategorie[firstIndex].getPath());
            treeEquipes.expandPath(treePath);
        }
        
        //selectionner le 1er élément
        treeEquipes.setSelectionRow(0);
    }

    /**
     * 
     * 
     * @param scna
     * @param typeSort
     */
    private void listeConcurrents(DifferentiationCriteria scna, int typeSort) {
        listeConcurrents(null, scna, typeSort);
    }
    
    /**
     * 
     * 
     * @param forclub
     * @param scna
     * @param typeSort
     */
    private void listeConcurrents(String forclub, DifferentiationCriteria scna, int typeSort) {
        Concurrent[] unsortList;
        String[] compagnies;
        
        //determine l'étendu de la séléction
        if(forclub == null || forclub.equals("")) {
            //supprimer toutes les clubs
            jcbClubs.removeAllItems();
            jcbClubs.addItem(ConcoursJeunes.ajrLibelle.getResourceString("equipe.contrainte.club.tous"));
            
            //liste l'ensemble des compagnies présente sur le concours
            compagnies = ficheConcours.ficheConcours.archerlist.listCompagnie();
        } else {
            compagnies = new String[] { forclub };
        }
        
        //genere l'entrée dans la table de hashage si elle n'existe pas
        hConcurrents.put(scna, new ArrayList<Concurrent>());

        //parcours les compagnies
        for(String compagnie : compagnies) {
            //liste les archers pour chaque compagnie
            Concurrent[] concurrents = ficheConcours.ficheConcours.archerlist.list(compagnie, scna);
            //regarde si leurs nombre est suffisant pour créer une équipe
            if(!cbEquipeClub.isSelected() || concurrents.length >= ficheConcours.ficheConcours.parametre.getNbMembresRetenu()) {
                //ajoute le club à la liste
                if(forclub == null || forclub.equals(""))
                    jcbClubs.addItem(compagnie);                                                           
                
                //ajoute les concurrents à la table de hashage
                for(Concurrent concurrent : concurrents) {
                    boolean add = true;
                    for(Equipe equipe : hEquipes.get(scna).values()) {
                        if(equipe.contains(concurrent)) {
                            add = false;
                            
                            break;
                        }
                    }
                    if(add)
                        hConcurrents.get(scna).add(concurrent);
                }
            }
        }
        
        unsortList =  hConcurrents.get(scna).toArray(new Concurrent[hConcurrents.get(scna).size()]);

        ajlConcurrents.setListData(ArcherList.sort(unsortList, typeSort));
    }
    
    public void popup() {
        popup = new PopupMenu("Edit");

        MenuItem mi1 = new MenuItem(ConcoursJeunes.ajrLibelle.getResourceString("popup.suppression"));
        mi1.setActionCommand("popup.suppression");
        mi1.addActionListener(this);
        popup.add(mi1);
        
        add(popup);
    }
    
    /**
     * renvoie la liste des équipes
     * 
     * @return Equipe[] - la liste des équipes
     */
    public Equipe[] getEquipes() {
        ArrayList<Equipe> al = new ArrayList<Equipe>();
        
        for(Hashtable<String, Equipe> htable : hEquipes.values()) {
            for(Equipe equipe : htable.values()) {
                if(equipe.getMembresEquipe().size() < ficheConcours.ficheConcours.parametre.getNbMembresRetenu()) {
                    htable.remove(equipe.getNomEquipe());
                }
            }
        }
        
        for(Hashtable<String, Equipe> htable : hEquipes.values()) {
            for(Equipe equipe : htable.values()) {
                al.add(equipe);
            }
        }
        return al.toArray(new Equipe[al.size()]);
    }
    
    /**
     * indique si la boite à été valider ou non
     * 
     * @return boolean - l'etat de validation
     */
    public boolean isValider() {
        return validation;
    }
    
    private void createEquipe(TreePath destPath) {
        
        if(destPath == null)
            return;
    	
        DefaultMutableTreeNode equipesTreeNode;
        
        //selectionne la destination
        treeEquipes.setSelectionPath(destPath);
        
        //recupere le noeud destination
        equipesTreeNode = (DefaultMutableTreeNode)destPath.getLastPathComponent();
        
        //detecte la categorie du point d'arborescence
        DifferentiationCriteria scna = ((DifferentiationCriteriaLibelle)equipesTreeNode.getUserObject()).getDifferentiationCriteria();
        
        //si le concurrent courrant appartient bien à la categorie
        if(scna.equals(((Concurrent)selectionConc[0]).getDifferentiationCriteria())) {
            
            //donner à l'équipe le nom du club du premier concourrent
            String strEquipeName = ((Concurrent)selectionConc[0]).getClub();

            if(hEquipes.get(scna).containsKey(strEquipeName)) {
                int i = 2;
                while(hEquipes.get(scna).containsKey(strEquipeName + i)) {
                    i++;
                }
                strEquipeName += "" + i;
            }
            
            do {
                strEquipeName = JOptionPane.showInputDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("equipe.saisinom"), strEquipeName);
            } while(strEquipeName == null || strEquipeName.equals("") || hEquipes.get(scna).containsKey(strEquipeName));
            
            //on cré l'équipe
            Equipe equipe = new Equipe(strEquipeName);
    
            //on met l'équipe dans la table des équipes
            hEquipes.get(scna).put(strEquipeName, equipe);
            
            //on cré le point d'arborescence dans l'arbre
            DefaultMutableTreeNode equipeTreeNode = new DefaultMutableTreeNode(equipe);
            
            //on ajoute les memebres à l'équipe
            	addMembresToEquipe(scna, strEquipeName, equipeTreeNode);
            	
            	//on cré le point d'arborescence dans l'arbre
            	equipesTreeNode.add(equipeTreeNode);
            	
            	treeModel.reload();
            	TreePath treePath = new TreePath(equipeTreeNode.getPath());
            	treeEquipes.expandPath(treePath);
        } else {
        	    JOptionPane.showMessageDialog(this,
    				ConcoursJeunes.ajrLibelle.getResourceString("equipe.error.badcategorie"),
    				ConcoursJeunes.ajrLibelle.getResourceString("equipe.warning"),JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void addMembresToEquipe(DifferentiationCriteria scna, String nomEquipe, DefaultMutableTreeNode dmtn) {
        //test si le nombre de concurrent n'est pas trop important pour l'équipe
	    	if(hEquipes.get(scna).get(nomEquipe).getMembresEquipe().size() + selectionConc.length > ficheConcours.ficheConcours.parametre.getNbMembresEquipe()) {
	    		JOptionPane.showMessageDialog(this,
	    				ConcoursJeunes.ajrLibelle.getResourceString("equipe.taille.max"),
	    				ConcoursJeunes.ajrLibelle.getResourceString("equipe.warning"),JOptionPane.WARNING_MESSAGE);
	    	} else {
		    	for(Object concurrent : selectionConc) {
		    		if(concurrent != null) {
                    //ajoute le concurrent à l'equipe
                    hEquipes.get(scna).get(nomEquipe).addConcurrent((Concurrent)concurrent);
                    //ajoute le concurrent à l'arborescence.
		    			DefaultMutableTreeNode membreTreeNode = new DefaultMutableTreeNode(concurrent);
		    			dmtn.add(membreTreeNode);
                    //le supprime de la liste des non affecté
		    			ajlConcurrents.remove(concurrent);
		    		}
		    	}
	    	}
    }
    
    private void removeMembreForEquipe(DifferentiationCriteria scna, Equipe equipe, Concurrent concurrent) {
        
        //recupere la racine de l'arbre
        DefaultMutableTreeNode tnRoot = (DefaultMutableTreeNode)treeModel.getRoot();
        DefaultMutableTreeNode tnSCNA = null;
        DefaultMutableTreeNode tnEquipe = null;
        DefaultMutableTreeNode tnConcurrent = null;
        
        Enumeration edmtnRoot = tnRoot.children();
        while(edmtnRoot.hasMoreElements()) {
            DefaultMutableTreeNode tnStmp = (DefaultMutableTreeNode)edmtnRoot.nextElement();
            
            if(((DifferentiationCriteriaLibelle)tnStmp.getUserObject()).getDifferentiationCriteria() == scna) {
                tnSCNA = tnStmp;
                
                Enumeration edmtnSCNA = tnStmp.children();
                while(edmtnSCNA.hasMoreElements()) {
                    DefaultMutableTreeNode tnEtmp = (DefaultMutableTreeNode)edmtnSCNA.nextElement();
                    
                    if(tnEtmp.getUserObject() == equipe) {
                        tnEquipe = tnEtmp;
                        
                        Enumeration edmtnEquipe = tnEtmp.children();
                        while(edmtnEquipe.hasMoreElements()) {
                            
                            DefaultMutableTreeNode tnCtmp = (DefaultMutableTreeNode)edmtnEquipe.nextElement();
                            if(tnCtmp.getUserObject() == concurrent) {
                                tnConcurrent = tnCtmp;
                                
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        assert null != tnSCNA : "L'arbre doit contenir les catégories de classement";
        assert null != tnEquipe : "L'arbre doit contenir les équipes";
        assert null != tnConcurrent : "L'arbre doit contenir le concurrent";
        
        hEquipes.get(scna).get(equipe.getNomEquipe()).removeConcurrent(concurrent);
        treeModel.removeNodeFromParent(tnConcurrent);
        
        treeModel.reload();
        
        treeEquipes.expandPath(new TreePath(tnEquipe.getPath()));
        
        if(hEquipes.get(scna).get(equipe.getNomEquipe()).getEquipe().size() < 
                ficheConcours.ficheConcours.parametre.getNbMembresRetenu()) {
            
            hEquipes.get(scna).remove(equipe.getNomEquipe());
            treeModel.removeNodeFromParent(tnEquipe);
            
            treeModel.reload();
            
            treeEquipes.expandPath(new TreePath(tnSCNA.getPath()));
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JCheckBox) {
            if(e.getActionCommand().equals("filter"))
                categoriesEquipes();
            else if(e.getSource() == cbEquipeClub) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
                if(dmtn != null) {
                    Object dmtnObj = dmtn.getUserObject();
                    if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
                        listeConcurrents(((DifferentiationCriteriaLibelle)dmtnObj).getDifferentiationCriteria(), ArcherList.SORT_BY_CLUBS);
                    }
                }
            }
        } else if(e.getActionCommand().equals("popup.suppression")) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
            if(dmtn != null) {
                Object dmtnObj = dmtn.getUserObject();
                if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
                    
                } else if(dmtnObj instanceof Concurrent) {
                    DefaultMutableTreeNode tnEquipe = (DefaultMutableTreeNode)dmtn.getParent();
                    DefaultMutableTreeNode tnSCNA = (DefaultMutableTreeNode)tnEquipe.getParent();
                    
                    Equipe equipe = (Equipe)tnEquipe.getUserObject();
                    DifferentiationCriteria scna = ((DifferentiationCriteriaLibelle)tnSCNA.getUserObject()).getDifferentiationCriteria();
                    
                    removeMembreForEquipe(scna, equipe, (Concurrent)dmtnObj);
                }
            }
            
        } else if(e.getSource() == jbAnnuler) {
            validation = false;
            setVisible(false);
        } else if(e.getSource() == jbValider) {
            validation = true;
            setVisible(false);
        } else if(e.getSource() instanceof JRadioButton) {
			if(e.getSource() == rbNom) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
                if(dmtn != null) {
                    Object dmtnObj = dmtn.getUserObject();
                    if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
                        listeConcurrents(((DifferentiationCriteriaLibelle)dmtnObj).getDifferentiationCriteria(), ArcherList.SORT_BY_NAME);
                    }
                }
            } else if(e.getSource() == rbClub) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
                if(dmtn != null) {
                    Object dmtnObj = dmtn.getUserObject();
                    if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
                        listeConcurrents(((DifferentiationCriteriaLibelle)dmtnObj).getDifferentiationCriteria(), ArcherList.SORT_BY_CLUBS);
                    }
                }
            }
			ajlConcurrents.repaint();
		} else if(e.getSource() == jcbClubs) {
            if(jcbClubs.getSelectedItem() != null && !((String)jcbClubs.getSelectedItem()).equals("Tous")) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
                if(dmtn != null) {
                    Object dmtnObj = dmtn.getUserObject();
                    if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
                        listeConcurrents((String)jcbClubs.getSelectedItem(), ((DifferentiationCriteriaLibelle)dmtnObj).getDifferentiationCriteria(), ArcherList.SORT_BY_NAME);
                    }
                }
            }
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if(e.getSource() == ajlConcurrents) {
            AJList tmplist = (AJList)e.getSource();
            int[] selection = tmplist.getSelectedIndices();
            
            if(cbEquipeClub.isSelected()) {
                Concurrent concRef = null;
                
                for(int index : selection) {
                    Concurrent concurrent = (Concurrent)tmplist.getValueAt(index);
                    //prend le 1er élément en référence
                    if(concRef == null)
                        concRef = concurrent;
                    
                    if(!concurrent.getClub().equals(concRef.getClub())) {
                        tmplist.removeSelectionInterval(index, index);
                    }
                }
            }
            
            if(selection.length > ficheConcours.ficheConcours.parametre.getNbMembresEquipe()) {
                tmplist.removeSelectionInterval(selection[ficheConcours.ficheConcours.parametre.getNbMembresEquipe()],
                        selection[selection.length-1]);
            }
        }
    }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
        if(e.getSource() == treeEquipes) {
            if(e.getModifiers()==MouseEvent.BUTTON3_MASK) {
                Point coord = e.getPoint();
                
                TreePath destinationPath = treeEquipes.getPathForLocation(coord.x, coord.y);
                
                //recupere le noeud destination et son parent
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)destinationPath.getLastPathComponent();
                treeEquipes.setSelectionPath(destinationPath);
                
                if(node.getUserObject() instanceof Concurrent)
                    popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == ajlConcurrents) {
			selectionConc = ajlConcurrents.getSelectedValues();
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == ajlConcurrents) {
			AJList tmplist = (AJList)e.getSource();
            //coordonnée de l'objet source
			Point coordSrc = tmplist.getLocationOnScreen();
            //coordonnée de l'objet destination
			Point coordDest = treeEquipes.getLocationOnScreen();
            
            //test si le drop correspond bien à la bonne action
			if(tmplist == ajlConcurrents && (coordSrc.x + e.getX() - coordDest.x) < coordSrc.x - coordDest.x) {
				TreePath selectedPath = treeEquipes.getPathForLocation(coordSrc.x+e.getX()-coordDest.x,
                        coordSrc.y+e.getY()-coordDest.y);
				
				if(selectedPath != null) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
					
					if(selectedNode.getUserObject() instanceof DifferentiationCriteriaLibelle) {
						createEquipe(selectedPath);
					} else if(selectedNode.getUserObject() instanceof Equipe) {
						addMembresToEquipe(((DifferentiationCriteriaLibelle)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject()).getDifferentiationCriteria(), 
                                ((Equipe)selectedNode.getUserObject()).getNomEquipe(), selectedNode);
						treeModel.reload();
                        
                        TreePath treePath = new TreePath(selectedNode.getPath());
                        treeEquipes.expandPath(treePath);
					}
				}
            }
		}
		//dans tous les cas remettre le curseur par defaut à la fin du drag
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if(e.getSource() instanceof AJList) {
			Cursor cursor = java.awt.dnd.DragSource.DefaultMoveDrop;
			this.setCursor(cursor);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		
	}
	
	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		//TreePath treePath = e.getPath();
       if(e.getOldLeadSelectionPath() != e.getNewLeadSelectionPath()) {

        		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEquipes.getLastSelectedPathComponent();
        		if(dmtn != null) {
        			Object dmtnObj = dmtn.getUserObject();
        			if(dmtnObj instanceof DifferentiationCriteriaLibelle) {
        			    listeConcurrents(((DifferentiationCriteriaLibelle)dmtnObj).getDifferentiationCriteria(), ArcherList.SORT_BY_CLUBS);
        			}
        		}
        }
    }
}