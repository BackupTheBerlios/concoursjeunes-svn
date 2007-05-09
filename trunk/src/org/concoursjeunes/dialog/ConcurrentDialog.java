package org.concoursjeunes.dialog;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;

import org.concoursjeunes.*;
import org.concoursjeunes.ui.ConcoursJeunesFrame;

import ajinteractive.standard.java2.*;

/**
 * Boite de dialogue de gestion d'un concurrent
 * @author  Aurelien Jeoffray
 * @version  5.0
 */
public class ConcurrentDialog extends JDialog implements ActionListener, FocusListener, 
		AutoCompleteDocumentListener {

	public static final int CONFIRM_AND_CLOSE		= 1;
	public static final int CONFIRM_AND_NEXT		= 2;
	public static final int CONFIRM_AND_PREVIOUS 	= 3;
	public static final int CANCEL					= 4;

	private FicheConcours ficheConcours;
	private Concurrent concurrent;
	private Archer filter							= null;

	private JLabel jlDescription					= new JLabel();	//Description
	private JLabel jlNom							= new JLabel();	//Nom et prénom du Tireur
	private JLabel jlLicence						= new JLabel();	//N° de Licence
	private JLabel jlClub							= new JLabel();	//nom du club
	private JLabel jlAgrement						= new JLabel();	//n°agrement du club
	private JLabel jlCible							= new JLabel();	//cible attribué
	private JLabel jlDixNeufM						= new JLabel();	//Nb de 10/9/M

	//Tireur
	private JPanel jpConcurrent						= new JPanel();
	private JTextField jtfNom						= new JTextField(8); //Nom du tireur
	private JTextField jtfPrenom					= new JTextField(8); //Prenom du tireur
	private JButton jbSelectionArcher				= new JButton();
	private JButton jbEditerArcher					= new JButton();
	private JTextField jtfLicence					= new JTextField(16);//Numero de licence
	private Hashtable<Criterion, JLabel> jlCategrieTable = new Hashtable<Criterion, JLabel>();
	private Hashtable<Criterion, JComboBox> jcbCategorieTable = new Hashtable<Criterion, JComboBox>();

	//Club du tireur
	private JPanel jpClub							= new JPanel();
	private JTextField jtfClub						= new JTextField(16);//Intitulé du club
	private JTextField jtfAgrement					= new JTextField(16);//Numero d'Agrément
	private JButton jbDetailClub					= new JButton();
	private JButton jbListeClub						= new JButton();
	private JPanel jpCible							= new JPanel();

	//Point du tireur
	private JPanel jpPoints							= new JPanel();
	private JLabel jlValCible						= new JLabel();
	private JLabel jlPoints							= new JLabel();
	private JTextField tfpd[];
	private JTextField tfpd10						= new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	private JTextField tfpdNeuf						= new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
	private JTextField tfpdM						= new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$

	//inscription
	private JPanel jpInscription					= new JPanel();
	private JComboBox jcbInscription				= new JComboBox();

	//place libre
	private JPanel jpPlaceLibre						= new JPanel();
	private JLabel jlPlaceLibre						= new JLabel("<html></html>"); //$NON-NLS-1$

	private JPanel jpActionPane						= new JPanel();
	private JButton jbPrecedent						= new JButton();
	private JButton jbSuivant						= new JButton();
	private JButton jbQuitter						= new JButton();

	private int selectField							= 0;

	private int returnVal							= CANCEL;

	/**
	 * Création de la boite de dialogue de gestion de concurrent
	 * 
	 * @param ficheConcoursFrame - la fiche maître dont depend la boite de dialogue
	 */
	public ConcurrentDialog(ConcoursJeunesFrame concoursJeunesFrame, FicheConcours ficheConcours) {
		super(concoursJeunesFrame, "", true);

		this.ficheConcours = ficheConcours;

		init();
		affectLibelle();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if(selectField >= 0) {
					tfpd[selectField].requestFocus(true);
					tfpd[selectField].moveCaretPosition(0);
				} else {
					tfpd10.requestFocus(true);
					tfpd10.moveCaretPosition(0);
				}
			}
		});
	}

	/**
	 * initialise la boite de dialogue concurrent
	 * pour un nouveau concurrent ou en édition
	 */
	private void init() {
		//Layout Manager
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		GridbagComposer gridbagComposer = new GridbagComposer();

		for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.put(key, new JLabel());
			jcbCategorieTable.put(key, new JComboBox());
			jcbCategorieTable.get(key).setEditable(false);
		}

		jbSelectionArcher.addActionListener(this);
		jbSelectionArcher.setMargin(new Insets(0, 0, 0, 0));
		jbEditerArcher.addActionListener(this);
		jbEditerArcher.setMargin(new Insets(0, 0, 0, 0));
		jbPrecedent.addActionListener(this);
		jbSuivant.addActionListener(this);
		jbQuitter.addActionListener(this);
		jbDetailClub.addActionListener(this);
		jbDetailClub.setMargin(new Insets(0, 0, 0, 0));
		jbDetailClub.setText("+"); //$NON-NLS-1$
		jbListeClub.addActionListener(this);
		jbListeClub.setMargin(new Insets(0, 0, 0, 0));
		jbListeClub.setText("..."); //$NON-NLS-1$
		jtfNom.addFocusListener(this);
		jtfPrenom.addFocusListener(this);

		//Panneau de champs
		jlDescription.setOpaque(true);
		jlDescription.setPreferredSize(new Dimension(250, 70));
		jpConcurrent.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpClub.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpCible.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpPoints.add(jlPoints);

		//panneau validation inscription
		jpInscription.setBorder(new TitledBorder("")); //$NON-NLS-1$
		jpInscription.add(jcbInscription);

		jpPlaceLibre.setLayout(new BorderLayout());
		jpPlaceLibre.setPreferredSize(new Dimension(200,100));
		jpPlaceLibre.setBorder(new TitledBorder("")); //$NON-NLS-1$

		jpPlaceLibre.add(new JScrollPane(jlPlaceLibre), BorderLayout.CENTER);

		jpActionPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		tfpd = new JTextField[ficheConcours.getParametre().getReglement().getNbSerie()];
		for(int i = 0; i < ficheConcours.getParametre().getReglement().getNbSerie(); i++) {
			this.tfpd[i] = new JTextField(new NumberDocument(false, false), "0", 4); //$NON-NLS-1$
			this.tfpd[i].addFocusListener(this);
			jpPoints.add(this.tfpd[i]);
		}
		
		this.tfpd10.addFocusListener(this);
		this.tfpdNeuf.addFocusListener(this);
		this.tfpdM.addFocusListener(this);

		//panneau tireur
		gridbagComposer.setParentPanel(jpConcurrent);
		c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlNom, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfNom, c);
		gridbagComposer.addComponentIntoGrid(jtfPrenom, c);
		gridbagComposer.addComponentIntoGrid(jbSelectionArcher, c);
		gridbagComposer.addComponentIntoGrid(jbEditerArcher, c);
		c.gridy++; c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlLicence, c);
		c.gridwidth = 4;
		gridbagComposer.addComponentIntoGrid(jtfLicence, c);
		for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			c.gridy++; c.fill = GridBagConstraints.HORIZONTAL; c.gridwidth = 1;
			gridbagComposer.addComponentIntoGrid(jlCategrieTable.get(key), c);
			c.gridwidth = 4;
			gridbagComposer.addComponentIntoGrid(jcbCategorieTable.get(key), c);
		}

		//paneau club
		gridbagComposer.setParentPanel(jpClub);
		c.gridy = 0;						//Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlClub, c);
		gridbagComposer.addComponentIntoGrid(jtfClub, c);
		gridbagComposer.addComponentIntoGrid(jbDetailClub, c);
		gridbagComposer.addComponentIntoGrid(jbListeClub, c);
		c.gridy++;							//Défaut,Ligne 2
		gridbagComposer.addComponentIntoGrid(jlAgrement, c);
		gridbagComposer.addComponentIntoGrid(jtfAgrement, c);		

		//panneau cible
		gridbagComposer.setParentPanel(jpCible);
		c.gridy = 0;
		c.gridwidth = 1;						//Défaut,Haut
		gridbagComposer.addComponentIntoGrid(jlCible, c);
		gridbagComposer.addComponentIntoGrid(jlValCible, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jpPoints, c);
		c.gridy++;
		c.gridwidth = 1;
		gridbagComposer.addComponentIntoGrid(jlDixNeufM, c);
		gridbagComposer.addComponentIntoGrid(tfpd10, c);
		gridbagComposer.addComponentIntoGrid(tfpdNeuf, c);
		gridbagComposer.addComponentIntoGrid(tfpdM, c);

		//panneau action
		jpActionPane.add(jbPrecedent);
		jpActionPane.add(jbSuivant);
		jpActionPane.add(jbQuitter);

		//panneau global
		getContentPane().setLayout(gridbag);
		c.gridy = 0;						//Défaut,Haut
		c.gridwidth = 2;
		gridbag.setConstraints(jlDescription, c);
		getContentPane().add(jlDescription);
		c.gridy++;c.gridwidth = 1;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		gridbag.setConstraints(jpConcurrent, c);
		getContentPane().add(jpConcurrent,c);
		c.gridy += c.gridheight;
		c.gridheight = 1;
		gridbag.setConstraints(jpClub, c);
		getContentPane().add(jpClub,c);
		c.gridy++;
		gridbag.setConstraints(jpCible, c);
		getContentPane().add(jpCible,c);
		c.gridy++; c.gridwidth = 2;
		gridbag.setConstraints(jpActionPane, c);
		getContentPane().add(jpActionPane,c);

		c.gridy = 1; c.gridwidth = 1;
		gridbag.setConstraints(jpInscription, c);
		getContentPane().add(jpInscription);
		c.gridy++; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(jpPlaceLibre, c);
		getContentPane().add(jpPlaceLibre);

		getRootPane().setDefaultButton(jbSuivant);
	}
	
	/**
	 * affecte les libellé localisé à l'interface
	 */
	private void affectLibelle() {
		setTitle(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.titre.edition")); //$NON-NLS-1$
		
		((TitledBorder)jpConcurrent.getBorder()).setTitle(
				ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.tireur")); //$NON-NLS-1$
		((TitledBorder)jpClub.getBorder()).setTitle(
				ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.club")); //$NON-NLS-1$
		((TitledBorder)jpCible.getBorder()).setTitle(
				ConcoursJeunes.ajrLibelle.getResourceString("concurrent.panel.cible")); //$NON-NLS-1$
		((TitledBorder)jpInscription.getBorder()).setTitle(
				ConcoursJeunes.ajrLibelle.getResourceString("concurrent.inscription.titre")); //$NON-NLS-1$
		((TitledBorder)jpPlaceLibre.getBorder()).setTitle(
				ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.titre")); //$NON-NLS-1$
		
		jlDescription.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.description"));	//Description //$NON-NLS-1$
		jlNom.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.identite"));		//Nom et prénom du Tireur //$NON-NLS-1$
		jlLicence.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.numlicence"));		//N° de Licence //$NON-NLS-1$
		jlClub.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.nomclub"));			//nom du club //$NON-NLS-1$
		jlAgrement.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.agrementclub"));	//n°agrement du club //$NON-NLS-1$
		jlCible.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.cible"));			//cible attribué //$NON-NLS-1$
		jlPoints.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.points"));		//$NON-NLS-1$
		jlDixNeufM.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.dix"));				//$NON-NLS-1$
		
		jbSelectionArcher.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.selectionarcher")); //$NON-NLS-1$
		jbEditerArcher.setToolTipText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.editer")); //$NON-NLS-1$
		jbPrecedent.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.precedent")); //$NON-NLS-1$
		jbSuivant.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.suivant")); //$NON-NLS-1$
		jbQuitter.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.quitter")); //$NON-NLS-1$
		
		for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jlCategrieTable.get(key).setText(key.getLibelle());
			jcbCategorieTable.get(key).removeAllItems();
			for(CriterionElement element : key.getCriterionElements())
				jcbCategorieTable.get(key).addItem(element.getLibelle());
		}
		
		String[] lInscription = AJToolKit.tokenize(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.inscription"), ",");
		jcbInscription.removeAllItems();
		for(String status : lInscription) {
			jcbInscription.addItem(status);
		}
	}
	
	/**
	 * remplit les champs de la boite de dialogue avec le modèle sous jacent
	 */
	private void completeConcurrentDialog() {
		boolean isinit = concurrent.getInscription() != Concurrent.UNINIT;

		jbSelectionArcher.setEnabled(!isinit);
		jbEditerArcher.setEnabled(isinit);
		if(isinit) {
			jbEditerArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.lock")
			));
		} else {
			jbEditerArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.open")
			));
		}

		jtfNom.setEditable(!isinit);
		jtfPrenom.setEditable(!isinit);
		jtfLicence.setEditable(!isinit);

		for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			jcbCategorieTable.get(key).setEnabled(!isinit);
		}

		jtfClub.setEditable(!isinit);
		jtfAgrement.setEditable(!isinit);

		jbPrecedent.setEnabled(isinit);
		jbSuivant.setText(
				isinit ? ConcoursJeunes.ajrLibelle.getResourceString("bouton.validersuivant")
						: ConcoursJeunes.ajrLibelle.getResourceString("bouton.validernouveau")); //$NON-NLS-1$


		jlPlaceLibre.setText(showPlacesLibre());

		if(jtfNom.getDocument() instanceof AutoCompleteDocument) {
			((AutoCompleteDocument)jtfNom.getDocument()).setText(concurrent.getNomArcher());
			((AutoCompleteDocument)jtfPrenom.getDocument()).setText(concurrent.getPrenomArcher());
			((AutoCompleteDocument)jtfLicence.getDocument()).setText(concurrent.getNumLicenceArcher());
			((AutoCompleteDocument)jtfClub.getDocument()).setText(concurrent.getClub().getVille());
			((AutoCompleteDocument)jtfAgrement.getDocument()).setText(concurrent.getClub().getAgrement());
		} else {
			jtfNom.setText(concurrent.getNomArcher());
			jtfPrenom.setText(concurrent.getPrenomArcher());
			jtfLicence.setText(concurrent.getNumLicenceArcher());
			jtfClub.setText(concurrent.getClub().getVille());
			jtfAgrement.setText(concurrent.getClub().getAgrement());
		}

		if(concurrent.getCriteriaSet() != null) {
			for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
				CriterionElement element = concurrent.getCriteriaSet().getCriterionElement(key);
				jcbCategorieTable.get(key).setSelectedIndex(key.getCriterionElements().indexOf(element));
			}
		}

		jlValCible.setText(concurrent.getCible() + "" + (char)('A' + concurrent.getPosition())); //$NON-NLS-1$

		ArrayList<Integer> score = concurrent.getScore();
		if(score.size() > 0) {
			for(int i = 0; i < score.size(); i++) {
				tfpd[i].setText(score.get(i) + ""); //$NON-NLS-1$
			}
		} else {
			for(int i = 0; i < tfpd.length; i++) {
				tfpd[i].setText("0"); //$NON-NLS-1$
			}
		}

		tfpd10.setText("" + concurrent.getDix()); //$NON-NLS-1$
		tfpdNeuf.setText("" + concurrent.getNeuf()); //$NON-NLS-1$
		tfpdM.setText("" + concurrent.getManque()); //$NON-NLS-1$

		if(concurrent.getInscription() == Concurrent.UNINIT)
			this.jcbInscription.setSelectedIndex(0);
		else
			this.jcbInscription.setSelectedIndex(concurrent.getInscription());
	}
	
	/**
	 * Affiche la boite de dialogue de création d'un concurrent
	 * 
	 * @param depart - le depart affecté au concurrent
	 */
	public int showNewConcurrentDialog() {
		
		Concurrent concurrent = new Concurrent();
		
		AutoCompleteDocument acdNom = new AutoCompleteDocument(jtfNom, AutoCompleteDocument.NAME_SEARCH, ficheConcours.getParametre().getReglement());
		acdNom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdPrenom = new AutoCompleteDocument(jtfPrenom, AutoCompleteDocument.FIRSTNAME_SEARCH, ficheConcours.getParametre().getReglement());
		acdPrenom.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdLicence = new AutoCompleteDocument(jtfLicence, AutoCompleteDocument.NUMLICENCE_SEARCH, ficheConcours.getParametre().getReglement());
		acdLicence.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.CLUB_SEARCH, ficheConcours.getParametre().getReglement());
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.AGREMENT_SEARCH, ficheConcours.getParametre().getReglement());
		acdAgrement.addAutoCompleteDocumentListener(this);
		
		jtfNom.setDocument(acdNom);
		jtfLicence.setDocument(acdLicence);
		jtfPrenom.setDocument(acdPrenom);
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);
		
		setConcurrent(concurrent);
				
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		return returnVal;
	}
	
	/**
	 * Affiche la boite de dialogue de gestion du concurrent donné en parametre
	 * 
	 * @param concurrent - la concurrent à afficher
	 * @return le code de retour de la boite de dialogue
	 */
	public int showConcurrentDialog(Concurrent concurrent) {
		
		jlDescription.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.description"));
		jlDescription.setBackground(null);
		
		AutoCompleteDocument acdClub = new AutoCompleteDocument(jtfClub, AutoCompleteDocument.CLUB_SEARCH, ficheConcours.getParametre().getReglement());
		acdClub.addAutoCompleteDocumentListener(this);
		AutoCompleteDocument acdAgrement = new AutoCompleteDocument(jtfAgrement, AutoCompleteDocument.AGREMENT_SEARCH, ficheConcours.getParametre().getReglement());
		acdAgrement.addAutoCompleteDocumentListener(this);
		
		jtfNom.setDocument(new PlainDocument());
		jtfLicence.setDocument(new PlainDocument());
		jtfPrenom.setDocument(new PlainDocument());
		jtfClub.setDocument(acdClub);
		jtfAgrement.setDocument(acdAgrement);
		
		setConcurrent(concurrent);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		return returnVal;
	}

	/**
	 * Affichage des info d'un concurrent en mode edition
	 * @param concurrent  - le concurrent à editer
	 * @uml.property  name="concurrent"
	 */
	public void setConcurrent(Concurrent concurrent) {
		this.concurrent = concurrent;
		
		completeConcurrentDialog();
	}
	
	/**
	 * Retourne le concurrent de la boite de dialogue
	 * @return  le concurrent courrant
	 * @uml.property  name="concurrent"
	 */
	public Concurrent getConcurrent() {
		return concurrent;
	}

	/**
	 * formate l'affichage des places libre en fonction des catégorie de classement
	 * 
	 * @return String l'affichage ecran des places libres
	 */
	private String showPlacesLibre() {
		String strPlaceLibre = "<html>"; //$NON-NLS-1$
		
		//affiche le nombre de concurrent total sur le pas de tir
		strPlaceLibre += ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.nbarcher") + //$NON-NLS-1$
		": " + ficheConcours.getConcurrentList().countArcher() + "<br><br>"; //$NON-NLS-1$ //$NON-NLS-2$

		//recupere la table d'occupation des cibles
		Hashtable<DistancesEtBlason, OccupationCibles> occupationCibles = 
			ficheConcours.getPasDeTir(concurrent.getDepart()).getOccupationCibles(ficheConcours.getParametre().getNbTireur());

		Hashtable<CriteriaSet, DistancesEtBlason> tableCorresp = 
			ficheConcours.getParametre().getReglement().getCorrespondanceCriteriaSet_DB();
		
		//en extrait les jeux de critères de placement
		CriteriaSet[] criteriaSetPlacement = new CriteriaSet[tableCorresp.size()];
		Enumeration<CriteriaSet> critEnum = tableCorresp.keys();
		for(int i = 0; critEnum.hasMoreElements(); i++) {
			criteriaSetPlacement[i] = critEnum.nextElement();
		}

		//ordonne ces critères selon l'ordre définit dans la configuration
		CriteriaSet.sortCriteriaSet(criteriaSetPlacement, ficheConcours.getParametre().getReglement().getListCriteria());

		//boucle sur chacun des jeux de placement
		for(CriteriaSet differentiationCriteria : criteriaSetPlacement) {
			//etablit la correspondance entre un jeux de placement et son d/b
			DistancesEtBlason distAndBlas = ficheConcours.getParametre().getReglement().getCorrespondanceCriteriaSet_DB(differentiationCriteria);

			//genere le libellé complet du jeux de critère
			CriteriaSetLibelle libelle = new CriteriaSetLibelle(differentiationCriteria);
			String strCategoriePlacement = libelle.toString();

			strPlaceLibre += "<i>" + //$NON-NLS-1$
					strCategoriePlacement + "(" + //$NON-NLS-1$
					distAndBlas.getDistance()[0] + "m/" + //$NON-NLS-1$
					distAndBlas.getBlason() + "cm)</i><br>\n"; //$NON-NLS-1$
			strPlaceLibre += "&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">" + //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.occupee") + //$NON-NLS-1$
					" " + occupationCibles.get(distAndBlas).getPlaceOccupe() + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
			strPlaceLibre += ", <font color=\"green\">" + //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.libre") + //$NON-NLS-1$
					" " + occupationCibles.get(distAndBlas).getPlaceLibre() + "</font><br>"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		strPlaceLibre += "<br>" + ConcoursJeunes.ajrLibelle.getResourceString("concurrent.placelibre.ciblelibre") + //$NON-NLS-1$ //$NON-NLS-2$
				" " + ficheConcours.getPasDeTir(concurrent.getDepart()).getNbCiblesLibre(ficheConcours.getParametre().getNbTireur()) + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		
		return strPlaceLibre;
	}

	private boolean isConcurrentModified() {
		boolean modif = false;
		if(concurrent.getInscription() != Concurrent.UNINIT) { //$NON-NLS-1$
			
			assert concurrent.getScore() != null : "La grille des scores ne doit pas être null";
			
			//regarde si il y a changement au niveau des scores
			for(int i = 0; i < concurrent.getScore().size(); i++) {
				if(concurrent.getScore().get(i) != Integer.parseInt(this.tfpd[i].getText())) {
					modif = true;
					
					break;
				}
			}
			
			if(!modif) {
				if(Integer.parseInt(tfpd10.getText()) != concurrent.getDix() 
						|| Integer.parseInt(tfpdNeuf.getText()) != concurrent.getNeuf()
						|| Integer.parseInt(tfpdM.getText()) != concurrent.getManque()
						|| (jtfNom.isEditable() && !jtfNom.getText().equals("")) //$NON-NLS-1$
						|| (jcbInscription.getSelectedIndex() != concurrent.getInscription() && !jtfNom.getText().equals(""))) { //$NON-NLS-1$
					modif = true;
				}
			}
		} else if(jtfNom.getText().length() > 0) {
			modif = true;
		}
		
		return modif;
	}
	
	private ArrayList<Integer> readScores() {
		ArrayList<Integer> points = new ArrayList<Integer>();
		for(int i = 0; i < tfpd.length; i++) {
			if(points.size() > i)
				points.set(i, Integer.parseInt(tfpd[i].getText()));
			else
				points.add(Integer.parseInt(tfpd[i].getText()));
		}
		
		return points;
	}
	
	private CriteriaSet readCriteriaSet() {
		CriteriaSet differentiationCriteria = new CriteriaSet();
		for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria()) {
			CriterionElement criterionElement = key.getCriterionElements().get(
					jcbCategorieTable.get(key).getSelectedIndex());
			differentiationCriteria.setCriterionElement(key, criterionElement);
		}
		
		return differentiationCriteria;
	}
	/**
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#concurrentFinded(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void concurrentFinded(AutoCompleteDocumentEvent e) {
		Concurrent findConcurrent = e.getConcurrent();
		if(!findConcurrent.equals(concurrent)) {
			findConcurrent.setDepart(ficheConcours.getCurrentDepart());
			setConcurrent(findConcurrent);
		}
		
		if(concurrent.haveHomonyme()) {
			jlDescription.setText("<html><u><b>Homonymie détécté</b></u><br>Le concurrent selectionné possède un homonyme<br>Assurez vous d'avoir choisi le bon!</html>");
			jlDescription.setBackground(Color.ORANGE);
		} else {
			jlDescription.setText(ConcoursJeunes.ajrLibelle.getResourceString("concurrent.description"));
			jlDescription.setBackground(null);
		}
		
		filter = e.getGenericArcher();
	}
	
	/**
	 * @see org.concoursjeunes.AutoCompleteDocumentListener#concurrentNotFound(org.concoursjeunes.AutoCompleteDocumentEvent)
	 */
	public void concurrentNotFound(AutoCompleteDocumentEvent e) {
		Concurrent newConcurrent = new Concurrent();
		newConcurrent.setDepart(ficheConcours.getCurrentDepart());
		if(e.getSource() == jtfNom) {
			newConcurrent.setNomArcher(jtfNom.getText());
		} else if(e.getSource() == jtfPrenom) {
			newConcurrent.setNomArcher(jtfNom.getText());
			newConcurrent.setPrenomArcher(jtfPrenom.getText());
		} else if(e.getSource() == jtfLicence) {
			newConcurrent.setNomArcher(jtfNom.getText());
			newConcurrent.setPrenomArcher(jtfPrenom.getText());
			newConcurrent.setNumLicenceArcher(jtfLicence.getText());
		}
		
		filter = null;
		
		setConcurrent(newConcurrent);
		
		jlDescription.setText("<html>Aucun concurrent n'à été trouvé<br>Assurez vous de ne pas " +
				"avoir commit de faute de frappe!</html>");
		jlDescription.setBackground(Color.ORANGE);
	}
	
	public void entiteFinded(AutoCompleteDocumentEvent e) {
		Entite findEntite = e.getEntite();
		if(!findEntite.equals(concurrent.getClub())) {
			concurrent.setClub(findEntite);
			setConcurrent(concurrent);
		}
	}
	
	public void entiteNotFound(AutoCompleteDocumentEvent e) {
		Entite newEntite = new Entite();
		if(e.getSource() == jtfClub) {
			newEntite.setVille(jtfClub.getText());
		} else if(e.getSource() == jtfAgrement) {
			newEntite.setVille(jtfClub.getText());
			newEntite.setAgrement(jtfAgrement.getText());
		}
		
		concurrent.setClub(newEntite);
		setConcurrent(concurrent);
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == jbSuivant
				|| ae.getSource() == jbPrecedent
				|| ae.getSource() == jbQuitter) {
			
			filter = null;
			
			//si il n'y a pas de modification ou si les modifications sont refuse alors quitter sans sauvegarder
			if(ae.getSource() != jbSuivant
					&& (!isConcurrentModified() || JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("concurrent.quitter.enregistrement"), //$NON-NLS-1$
							ConcoursJeunes.ajrLibelle.getResourceString("concurrent.quitter.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)) { //$NON-NLS-1$
				
				returnVal = CANCEL;
				
			} else {
				if(concurrent == null)
					concurrent = new Concurrent();
				//fixe le jeux de critères definissant le concurrent
				CriteriaSet differentiationCriteria = readCriteriaSet();
				concurrent.setCriteriaSet(differentiationCriteria);

				if(concurrent.getInscription() == Concurrent.UNINIT) {
					//si il n'y a plus de place alors retourner une erreur
					if(!ficheConcours.getPasDeTir(concurrent.getDepart()).havePlaceForConcurrent(concurrent)) {
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible"), //$NON-NLS-1$
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					//si le concurrent existe déjà alors retourner une erreur
					} else if(ficheConcours.getConcurrentList().contains(concurrent)) {
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.alreadyexist"), //$NON-NLS-1$
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.alreadyexist.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					}
				} else {
					if(!ficheConcours.getPasDeTir(concurrent.getDepart()).havePlaceForConcurrent(concurrent)) {
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible"), //$NON-NLS-1$
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.maxcible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
			
				//verification du score
				if(!ficheConcours.getParametre().getReglement().isValidScore(readScores())) {
					JOptionPane.showMessageDialog(new JDialog(),
							ConcoursJeunes.ajrLibelle.getResourceString("erreur.impscore"), //$NON-NLS-1$
							ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}
				concurrent.setScore(readScores());

				concurrent.setDix(Integer.parseInt(tfpd10.getText()));
				concurrent.setNeuf(Integer.parseInt(tfpdNeuf.getText()));
				concurrent.setManque(Integer.parseInt(tfpdM.getText()));
				concurrent.setNomArcher(jtfNom.getText());
				concurrent.setPrenomArcher(jtfPrenom.getText());
				concurrent.setNumLicenceArcher(jtfLicence.getText());
				concurrent.getClub().setNom(jtfClub.getText());
				concurrent.getClub().setAgrement(jtfAgrement.getText());
				concurrent.setInscription(jcbInscription.getSelectedIndex());
				
				
				concurrent.saveCriteriaSet(ficheConcours.getParametre().getReglement());

				if(ae.getSource() == jbQuitter) {
					if(!this.jtfNom.getText().equals("")) { //$NON-NLS-1$
						returnVal = CONFIRM_AND_CLOSE;
					}
				} else if(ae.getSource() == jbSuivant) { //$NON-NLS-1$
					returnVal = CONFIRM_AND_NEXT;
				} else if(ae.getSource() == jbPrecedent) { //$NON-NLS-1$
					returnVal = CONFIRM_AND_PREVIOUS;
				}
			}

			setVisible(false);
		} else if(ae.getSource() == jbSelectionArcher) {
			ConcurrentListDialog concurrentListDialog = new ConcurrentListDialog(this, ficheConcours.getParametre().getReglement(), filter);
			concurrentListDialog.setVisible(true);
			if(concurrentListDialog.isValider()) {
				concurrentListDialog.initConcurrent(concurrent);
				setConcurrent(concurrent);
			}
		} else if(ae.getSource() == jbDetailClub) { //$NON-NLS-1$
			if(!jtfAgrement.getText().equals("")) {
				EntiteDialog ed = new EntiteDialog(this);
				ed.showEntite(concurrent.getClub());
			}
		} else if(ae.getSource() == jbListeClub) { //$NON-NLS-1$
			EntiteListDialog entiteListDialog = new EntiteListDialog(null);
			if(entiteListDialog.getAction() == EntiteListDialog.VALIDER) {
				entiteListDialog.setAction(EntiteListDialog.ANNULER);
				jtfClub.setText(entiteListDialog.getSelectedEntite().getVille());
				jtfAgrement.setText(entiteListDialog.getSelectedEntite().getAgrement());
			}
		} else if (ae.getSource() == jbEditerArcher) { //$NON-NLS-1$
			jtfNom.setEditable(true);
			jtfPrenom.setEditable(true);
			jtfLicence.setEditable(true);

			for(Criterion key : ficheConcours.getParametre().getReglement().getListCriteria())
				jcbCategorieTable.get(key).setEnabled(true);

			jtfClub.setEditable(true);
			jtfAgrement.setEditable(true);

			jbEditerArcher.setEnabled(false);

			jbEditerArcher.setIcon(new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
					File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.open")
			));

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
}