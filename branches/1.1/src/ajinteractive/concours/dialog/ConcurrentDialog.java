/*
 * Created on 20 déc. 2004
 * 
 * 
 */
package ajinteractive.concours.dialog;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import ajinteractive.standard.java2.*;
import ajinteractive.concours.*;

/**
 * Boite de dialogue de gestion d'un concurrent
 * 
 * @author Aurelien Jeoffray
 * @version 4.2
 *
 */
public class ConcurrentDialog extends JDialog implements ActionListener, FocusListener, WindowListener {
	private static final int LIBRE = 0;
	private static final int OCCUPEE = 1;
	
	//private:
	private FicheConcours ficheConcours;
	private Concurrent concurrent;
	private int Volee = 0;
	
    //panel
    private JButton jbSelectionArcher;
    private JButton jbEditerArcher;

	//Tireur
	private JTextField tfnom = new JTextField(8); //Nom du tireur
	private JTextField tfpre = new JTextField(8); //Prenom du tireur
	private JTextField tflic = new JTextField(16);//Numero de licence
	
    private Hashtable<String, JComboBox> jcbCategorieTable = new Hashtable<String, JComboBox>();

	//Club du tireur
	private JTextField tfclu = new JTextField(16);//Intitulé du club
	private JTextField tfagr = new JTextField(16);//Numero d'Agrément
	
	//Point du tireur
	private JLabel tfcib;
	private JTextField tfpd[];
	private JTextField tfpd10;
	private JTextField tfpdNeuf;
	private JTextField tfpdM;
	private JTextField tfv = new JTextField("0",4); //$NON-NLS-1$
	
	//inscription
	private JComboBox jcbInscription;
	
	//place libre
	private JLabel jlPlaceLibre = new JLabel("<html></html>"); //$NON-NLS-1$
    //private JTextArea jlPlaceLibre = new JTextArea("<html></html>", 20, 30); //$NON-NLS-1$
    
    JButton prec;
    JButton suiv;
    JButton quit;
    
    private static ConcurrentListDialog concurrentListDialog;
	
    private int selectField = 0;
    
    private ArrayList<DifferentiationCriteria> categoriesPlacement = new ArrayList<DifferentiationCriteria>();
    private Hashtable<DistancesEtBlason, int[]> placeSurDistance = new Hashtable<DistancesEtBlason, int[]>();
	
    private int cibleLibre;
//public:
	public boolean valid = false;
	public boolean suite = false;
    public boolean precedent = false;

	/**
	 * Création de la boite de dialogue de gestion de concurrent
	 * 
	 * @param ficheConcoursFrame - la fiche maître dont depend la boite de dialogue
	 */
	public ConcurrentDialog(FicheConcoursFrame ficheConcoursFrame) {
		super(ficheConcoursFrame.concoursJeunes);
		this.ficheConcours = ficheConcoursFrame.ficheConcours;
		
		initConcurrentDialog();
		
		this.setModal(true);
        this.addWindowListener(this);
	}
    
    /**
     * initialise la boite de dialogue concurrent
     * pour un nouveau concurrent ou en édition
     * 
     * @param nouveau - true si nouveau, false sinon
     */
	private void initConcurrentDialog() {
		
		//Layout Manager
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		//Elements textuelle
		JLabel desc = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.description"));		//Description
		JLabel nom = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.identite"));			//Nom et prénom du Tireur
		JLabel lic = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.numlicence"));			//N° de Licence
		JLabel clu = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nomclub"));			//nom du club
		JLabel agr = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.agrementclub"));		//n°agrement du club
		JLabel cib = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.cible"));				//cible attribué
		JLabel dix = new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.dix"));				//Nb de 10 et 10+
		
        //
        for(Criterion key : ficheConcours.parametre.getListCriteria()) {
            jcbCategorieTable.put(key.getCode(), new JComboBox(ficheConcours.parametre.getCriteriaPopulation().get(key).toArray()));
            jcbCategorieTable.get(key.getCode()).setEditable(false);
        }
		
		jbSelectionArcher = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.selectionarcher"));
        jbEditerArcher = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.editer"));
		prec = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.precedent"));
		suiv = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.suivant"));
		quit = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.quitter"));
		JButton jbDetailClub = new JButton(ConcoursJeunes.ajrLibelle.getResourceString("bouton.detail"));
		JButton bprec = new JButton("<");//$NON-NLS-1$
		JButton bsuiv = new JButton(">");//$NON-NLS-1$
		
        this.tfcib = new JLabel();
        this.tfpd = new JTextField[ficheConcours.parametre.getNbSerie()];
		
        this.tfpd10 = new JTextField(new NumberDocument(false, false), "0",4);//$NON-NLS-1$
        this.tfpdNeuf = new JTextField(new NumberDocument(false, false), "0",4);//$NON-NLS-1$
        this.tfpdM = new JTextField(new NumberDocument(false, false), "0",4);//$NON-NLS-1$
		
        this.jcbInscription = new JComboBox(AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.inscription"), ","));
		
        this.tflic.addActionListener(this);
		
		jbSelectionArcher.setActionCommand("bouton.selectionarcher");
		jbSelectionArcher.addActionListener(this);
        jbEditerArcher.setActionCommand("bouton.editer");
        jbEditerArcher.addActionListener(this);
		prec.setActionCommand("bouton.precedent");
		prec.addActionListener(this);
		suiv.setActionCommand("bouton.suivant");
		suiv.addActionListener(this);
		quit.setActionCommand("bouton.quitter");
		quit.addActionListener(this);
		jbDetailClub.setActionCommand("bouton.detail");
		jbDetailClub.addActionListener(this);
		
		//Panneau de champs
        JPanel jpSelectionArcher = new JPanel();
		jpSelectionArcher.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.selectionarcher")));
		
		JPanel tireur = new JPanel();
		tireur.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.tireur")));
		tireur.setLayout(gridbag);
		
		JPanel clubpane = new JPanel();
		clubpane.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.club")));
		clubpane.setLayout(gridbag);
		
		JPanel ciblepane = new JPanel();
		ciblepane.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.cible")));
		ciblepane.setLayout(gridbag);

		JPanel voleepane = new JPanel();
        this.tfv.setEditable(false);
		bprec.addActionListener(this);
		bsuiv.addActionListener(this);
		voleepane.add(new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.volee")));
		voleepane.add(this.tfv);
		voleepane.add(bprec);
		voleepane.add(bsuiv);
		
		JPanel pointpane = new JPanel();
		pointpane.add(new JLabel(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.points")));
		
		//panneau validation inscription
		JPanel jpInscription = new JPanel();
		jpInscription.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.inscription.titre")));
		jpInscription.add(this.jcbInscription);
		
		JPanel jpPlaceLibre = new JPanel();
        jpPlaceLibre.setLayout(new BorderLayout());
        jpPlaceLibre.setPreferredSize(new Dimension(200,100));
		jpPlaceLibre.setBorder(new TitledBorder(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.titre")));
        
		jpPlaceLibre.add(new JScrollPane(jlPlaceLibre), BorderLayout.CENTER);
        
        JPanel jpActionPane = new JPanel();
        jpActionPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		for(int i = 0; i < this.ficheConcours.parametre.getNbSerie(); i++) {
            this.tfpd[i] = new JTextField(new NumberDocument(false, false), "0", 4);
            this.tfpd[i].addFocusListener(this);
			pointpane.add(this.tfpd[i]);
		}
		this.tfpd10.addFocusListener(this);
		this.tfpdNeuf.addFocusListener(this);
		this.tfpdM.addFocusListener(this);
		
		getContentPane().setLayout(gridbag);
		
		c.anchor = GridBagConstraints.WEST;
		
		//panneau selection Archer
		jpSelectionArcher.add(jbSelectionArcher);
        jpSelectionArcher.add(jbEditerArcher);
		
		//panneau tireur
		c.gridy = 0;						//Défaut,Haut
		AJToolKit.addComponentIntoGrid(tireur, nom, gridbag, c);
		c.gridwidth = 2;
		AJToolKit.addComponentIntoGrid(tireur, this.tfnom, gridbag, c);
		AJToolKit.addComponentIntoGrid(tireur, this.tfpre, gridbag, c);
		c.gridy++;							//Défaut,Ligne 2
		c.gridwidth = 1;
		AJToolKit.addComponentIntoGrid(tireur, lic, gridbag, c);
		c.gridwidth = 4;
		AJToolKit.addComponentIntoGrid(tireur, this.tflic, gridbag, c);
        for(Criterion key : ficheConcours.parametre.getListCriteria()) {
            c.gridy++; c.fill = GridBagConstraints.HORIZONTAL; c.gridwidth = 1;
            AJToolKit.addComponentIntoGrid(tireur, new JLabel(key.getLibelle()), gridbag, c);
            c.gridwidth = 4;
            AJToolKit.addComponentIntoGrid(tireur, jcbCategorieTable.get(key.getCode()), gridbag, c);
        }
		
		//paneau club
		c.gridy = 0;						//Défaut,Haut
		AJToolKit.addComponentIntoGrid(clubpane, clu, gridbag, c);
		AJToolKit.addComponentIntoGrid(clubpane, this.tfclu, gridbag, c);
		AJToolKit.addComponentIntoGrid(clubpane, jbDetailClub, gridbag, c);
		c.gridy++;							//Défaut,Ligne 2
		AJToolKit.addComponentIntoGrid(clubpane, agr, gridbag, c);
		AJToolKit.addComponentIntoGrid(clubpane, this.tfagr, gridbag, c);		

		//panneau cible
		c.gridy = 0;
		c.gridwidth = 1;						//Défaut,Haut
		AJToolKit.addComponentIntoGrid(ciblepane, cib, gridbag, c);
		AJToolKit.addComponentIntoGrid(ciblepane, this.tfcib, gridbag, c);
		c.gridwidth = 2;
		AJToolKit.addComponentIntoGrid(ciblepane, pointpane, gridbag, c);
		c.gridy++;
		c.gridwidth = 1;
		AJToolKit.addComponentIntoGrid(ciblepane, dix, gridbag, c);
		AJToolKit.addComponentIntoGrid(ciblepane, this.tfpd10, gridbag, c);
		AJToolKit.addComponentIntoGrid(ciblepane, this.tfpdNeuf, gridbag, c);
		AJToolKit.addComponentIntoGrid(ciblepane, this.tfpdM, gridbag, c);
        
        //panneau action
        jpActionPane.add(prec);
        jpActionPane.add(suiv);
        jpActionPane.add(quit);

		//panneau global
		c.gridy = 0;						//Défaut,Haut
		c.gridwidth = 2;
		gridbag.setConstraints(desc, c);
		getContentPane().add(desc);
		c.gridy++;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		gridbag.setConstraints(jpSelectionArcher, c);
		getContentPane().add(jpSelectionArcher, c);
		c.gridy += c.gridheight;
		gridbag.setConstraints(tireur, c);
		getContentPane().add(tireur,c);
		c.gridy += c.gridheight;
		c.gridheight = 1;
		gridbag.setConstraints(clubpane, c);
		getContentPane().add(clubpane,c);
		c.gridy++;
		gridbag.setConstraints(ciblepane, c);
		getContentPane().add(ciblepane,c);
        c.gridy++; c.gridwidth = 2;
        gridbag.setConstraints(jpActionPane, c);
        getContentPane().add(jpActionPane,c);
		
		c.gridy = 1; c.gridwidth = 1;
		gridbag.setConstraints(jpInscription, c);
		getContentPane().add(jpInscription);
		c.gridy++; c.gridheight = 5; c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(jpPlaceLibre, c);
		getContentPane().add(jpPlaceLibre);
		
        getRootPane().setDefaultButton(suiv);
	}
	
	/**
	 * Affichage des info d'un concurrent en mode edition
	 * 
	 * @param concurrent - le concurrent à editer
	 */
	public void editConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
		setTitle(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.titre.edition"));
		
        if(concurrent.getInscription() != Concurrent.UNINIT) {
            jbSelectionArcher.setEnabled(false);
            jbEditerArcher.setEnabled(true);
            
            tfnom.setEditable(false);
            tfpre.setEditable(false);
            tflic.setEditable(false);
            
            for(Criterion key : ficheConcours.parametre.getListCriteria())
                jcbCategorieTable.get(key.getCode()).setEnabled(false);
            
            tfclu.setEditable(false);
            tfagr.setEditable(false);
            
            prec.setEnabled(true);
            suiv.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.validersuivant"));

        } else {
            jbSelectionArcher.setEnabled(true);
            jbEditerArcher.setEnabled(false);
            
            tfnom.setEditable(true);
            tfpre.setEditable(true);
            tflic.setEditable(true);
            
            for(Criterion key : ficheConcours.parametre.getListCriteria())
                jcbCategorieTable.get(key.getCode()).setEnabled(true);
            
            tfclu.setEditable(true);
            tfagr.setEditable(true);
            
            prec.setEnabled(false);
            suiv.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.validernouveau"));
            
        }

        jlPlaceLibre.setText(showPlacesLibre());
		
        tfnom.setText(concurrent.getNom());
        tfpre.setText(concurrent.getPrenom());
        tflic.setText(concurrent.getLicence());
        tfclu.setText(concurrent.getClub());
        tfagr.setText(concurrent.getAgrement());
		
        if(concurrent.getDifferentiationCriteria() != null) {
            for(Criterion key : ficheConcours.parametre.getListCriteria())
                jcbCategorieTable.get(key.getCode()).setSelectedIndex(concurrent.getDifferentiationCriteria().getCriterion(key.getCode()));
        }
        
        this.tfcib.setText(concurrent.getCible() + "" + (char)('A' + concurrent.getPosition()));
		
		int[] score = concurrent.getScore();
		if(score != null) {
			for(int i = 0; i < score.length; i++) {
                tfpd[i].setText(score[i] + "");
			}
		} else {
		    for(int i = 0; i < tfpd.length; i++) {
                tfpd[i].setText("0");
            }
        }
		
        this.tfpd10.setText("" + concurrent.getDix());
        this.tfpdNeuf.setText("" + concurrent.getNeuf());
        this.tfpdM.setText("" + concurrent.getManque());
		
        if(concurrent.getInscription() == Concurrent.UNINIT)
            this.jcbInscription.setSelectedIndex(0);
        else
            this.jcbInscription.setSelectedIndex(concurrent.getInscription());
        
        this.pack();
        //this.setResizable(false);
        this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * formate l'affichage des places libre en fonction des catégorie de classement
	 * 
	 * @return String l'affichage ecran des places libres
	 */
	private String showPlacesLibre() {
		String strPlaceLibre = "<html>";
		
        DifferentiationCriteria[] scnaUse = new DifferentiationCriteria[categoriesPlacement.size()];
        categoriesPlacement.toArray(scnaUse);

        DifferentiationCriteria.sortDifferentiationCriteria(scnaUse, ficheConcours.parametre.getListCriteria());

        strPlaceLibre += ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.nbarcher") +
            ": " + ficheConcours.archerlist.countArcher() + "<br><br>";
		
		//categoriePlacement
		for(DifferentiationCriteria differentiationCriteria : scnaUse) {
			DistancesEtBlason distAndBlas = ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(differentiationCriteria);
            
            DifferentiationCriteriaLibelle libelle = new DifferentiationCriteriaLibelle(differentiationCriteria);
            
			String strCategoriePlacement = libelle.toString();
			
			strPlaceLibre += "<i>" +
				 strCategoriePlacement + "(" +
				 distAndBlas.getDistance()[0] + "m/" +
				 distAndBlas.getBlason() + "cm)</i><br>\n";
			strPlaceLibre += "&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">" +
				 ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.occupee") +
				 " " + this.placeSurDistance.get(distAndBlas)[LIBRE] + "</font>";
			strPlaceLibre += ", <font color=\"green\">" +
				 ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.libre") +
				 " " + this.placeSurDistance.get(distAndBlas)[OCCUPEE] + "</font><br>";
		}

		strPlaceLibre += "<br>" + ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.ciblelibre") +
			 " " + this.cibleLibre + "</html>";
		return strPlaceLibre;
	}
    
	public void addCategoriePlacement(DifferentiationCriteria categoriePlacement) {
		boolean add = true;
		for(DifferentiationCriteria scnatmp : this.categoriesPlacement) {
			if(scnatmp.equals(categoriePlacement)) {
				add = false;
				break;
			}
		}
		if(add) {
			categoriesPlacement.add(categoriePlacement);
            placeSurDistance.put(ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(categoriePlacement),
            		new int[] {0, 4});
		}
	}
    
    /**
     * defini la place libre restante pour un d&b donné
     * 
     * @param distEtblas - le d&b de filtre
     * @param places - le  b de place libre restant
     */
	public void setPlaceLibre(DistancesEtBlason distEtblas, int[] places) {
		this.placeSurDistance.put(distEtblas, places);
	}
    
    /**
     * defini le nb de cible vide restant
     * 
     * @param cibleVide - le nb de cible vide restant
     */
	public void setCibleVide(int cibleVide) {
        this.cibleLibre = cibleVide;
	}
	
    
	public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("bouton.suivant") //$NON-NLS-1$
                || cmd.equals("bouton.precedent") //$NON-NLS-1$
                || cmd.equals("bouton.quitter")) { //$NON-NLS-1$
            
            //Verifie si des modifications ont été effectué
            boolean modif = false;
            if(concurrent.getScore() != null && !concurrent.getNom().equals("")) {
                for(int i = 0; i < concurrent.getScore().length; i++) {
                    if(concurrent.getScore()[i] != Integer.parseInt(this.tfpd[i].getText())) {
                        modif = true;
                    }
                }
                if(Integer.parseInt(tfpd10.getText()) != concurrent.getDix() 
                        || Integer.parseInt(tfpdNeuf.getText()) != concurrent.getNeuf()
                        || Integer.parseInt(tfpdM.getText()) != concurrent.getManque()
                        || (tfnom.isEditable() && !tfnom.getText().equals("")) //$NON-NLS-1$
                        || (jcbInscription.getSelectedIndex() != concurrent.getInscription() && !tfnom.getText().equals(""))) { //$NON-NLS-1$
                    modif = true;
                }
            } else {
                modif = true;
            }

            //si il n'y a pas de modification ou si les modifications sont refuse alors quitter sans sauvegarder
            if(!cmd.equals("bouton.suivant") //$NON-NLS-1$
                    && (!modif || JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("concurrent.quitter.enregistrement"), //$NON-NLS-1$
                    ConcoursJeunes.ajrLibelle.getResourceString("concurrent.quitter.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)) { //$NON-NLS-1$
                
                suite = false;
                if(cmd.equals("bouton.precedent")) //$NON-NLS-1$
                    precedent = true;
                else
                    precedent = false;
                valid = false;
                
            // sinon enregistrer le parametrage
            } else {
                	
            	//verification du score
                int[] points = new int[ficheConcours.parametre.getNbSerie()];
                for(int i = 0; i < points.length; i++) {
                    points[i] = Integer.parseInt(this.tfpd[i].getText());
                    if(points[i] > (ficheConcours.parametre.getNbVoleeParSerie()
                            * ficheConcours.parametre.getNbFlecheParVolee() * 10)) {
                        JOptionPane.showMessageDialog(new JDialog(),
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur.impscore"), //$NON-NLS-1$
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
                        return;
                    }
                }
                
                DifferentiationCriteria differentiationCriteria = new DifferentiationCriteria();
                for(Criterion key : ficheConcours.parametre.getListCriteria())
                    differentiationCriteria.setCriterion(key.getCode(), jcbCategorieTable.get(key.getCode()).getSelectedIndex());
                concurrent.setDifferentiationCriteria(differentiationCriteria);
                
                if(concurrent.getInscription() == Concurrent.UNINIT) {
                    DifferentiationCriteria dti = concurrent.getDifferentiationCriteria();
                    dti.setFiltreCriteria(ficheConcours.parametre.getPlacementFilter());
                    DistancesEtBlason db = ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(dti);
                    
                    int[] place = this.placeSurDistance.get(db);
                    
                    if(place[1] == 0 && this.cibleLibre == 0) {
                        JOptionPane.showMessageDialog(this,
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible"), //$NON-NLS-1$
                                ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
                        return;
                    }
                }
                
                
                concurrent.setScore(points);
                concurrent.setDix(Integer.parseInt(tfpd10.getText()));
                concurrent.setNeuf(Integer.parseInt(tfpdNeuf.getText()));
                concurrent.setManque(Integer.parseInt(tfpdM.getText()));
                concurrent.setNom(tfnom.getText());
                concurrent.setPrenom(tfpre.getText());
                concurrent.setLicence(tflic.getText());
                concurrent.setClub(tfclu.getText());
                concurrent.setAgrement(tfagr.getText());
                //concurrent.setInscription(Concurrent.RESERVEE);
                concurrent.setInscription(jcbInscription.getSelectedIndex());

                if(cmd.equals("bouton.quitter")) { //$NON-NLS-1$
                    if(!this.tfnom.getText().equals("")) { //$NON-NLS-1$
                        this.suite = false;
                        this.precedent = false;
                        this.valid = true;
                    }
                } else if(cmd.equals("bouton.suivant")) { //$NON-NLS-1$
                    this.suite = true;
                    this.precedent = false;
                } else if(cmd.equals("bouton.precedent")) { //$NON-NLS-1$
                    this.suite = false;
                    this.precedent = true;
                }
            }
            
            setVisible(false);
        } else if (cmd.equals("bouton.selectionarcher")) { //$NON-NLS-1$
            concurrentListDialog.setVisible(true);
		    if(concurrentListDialog.isValider()) {
                concurrentListDialog.initConcurrent(concurrent);
		    	editConcurrent(concurrent);
		    }
		} else if (cmd.equals("<")) { //$NON-NLS-1$
            this.Volee = this.Volee - 2;
            this.tfv.setText(this.Volee + ""); //$NON-NLS-1$
		} else if (cmd.equals(">")) { //$NON-NLS-1$
            this.Volee = this.Volee + 2;
            this.tfv.setText(this.Volee + ""); //$NON-NLS-1$
		} else if (cmd.equals("bouton.detail")) { //$NON-NLS-1$
			EntiteDialog ed = new EntiteDialog(this);
        	ed.showEntite(ConcoursJeunes.listeEntite.get(tfagr.getText()));
		} else if (cmd.equals("bouton.editer")) { //$NON-NLS-1$
            tfnom.setEditable(true);
            tfpre.setEditable(true);
            tflic.setEditable(true);
            
            for(Criterion key : ficheConcours.parametre.getListCriteria())
                jcbCategorieTable.get(key.getCode()).setEnabled(true);
            
            tfclu.setEditable(true);
            tfagr.setEditable(true);
            
            jbEditerArcher.setEnabled(false);
        }
	}

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent fe) {
        for(int i = 0; i < tfpd.length; i++) {
            if(fe.getSource() == tfpd[i]) {
                selectField = i;
                break;
            }
        }
        if(fe.getSource() == tfpd10 || fe.getSource() == tfpdNeuf || fe.getSource() == tfpdM) {
        	selectField = -1;
        }
        if(fe.getSource() instanceof JTextField) {
	        ((JTextField)fe.getSource()).setSelectionStart(0);
			((JTextField)fe.getSource()).setSelectionEnd(((JTextField)fe.getSource()).getText().length());
        }
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent fe) {
    }

    /**
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent arg0) {
    	if(selectField >= 0) {
    		tfpd[selectField].requestFocus(true);
    		tfpd[selectField].moveCaretPosition(0);
    	} else {
    		tfpd10.requestFocus(true);
    		tfpd10.moveCaretPosition(0);
    	}
    }

    /**
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent arg0) {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent arg0) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent arg0) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent arg0) {  
    }

    /**
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent arg0) {
    }

    /**
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent arg0) {
    }

    /**
     * @return Renvoie concurrentListDialog.
     */
    public static ConcurrentListDialog getConcurrentListDialog() {
        return concurrentListDialog;
    }

    /**
     * @param concurrentListDialog concurrentListDialog à définir.
     */
    public static void setConcurrentListDialog(
            ConcurrentListDialog concurrentListDialog) {
        ConcurrentDialog.concurrentListDialog = concurrentListDialog;
    }
}