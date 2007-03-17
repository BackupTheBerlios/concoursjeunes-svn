package org.concoursjeunes.ui;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import org.concoursjeunes.*;
import org.concoursjeunes.dialog.*;

import ajinteractive.standard.java2.*;

/**
 * fiche concours. cette fiche correspond à la table d'inscrit et de résultats
 * @author  Aurélien Jeoffray
 * @version  1.0  TODO passage de niveau
 */
public class FicheConcoursPane extends JPanel implements ActionListener, ChangeListener, HyperlinkListener {

	private ConcoursJeunesFrame parentframe;

	//public
	public FicheConcours ficheConcours = new FicheConcours();

	//private
	private JTabbedPane tabbedpane     = new JTabbedPane();

	private JPanel fichesDepart = new JPanel();
	private CardLayout cl = new CardLayout();
	private ArrayList<FicheConcoursDepartPane> departPanels = new ArrayList<FicheConcoursDepartPane>();

	private JEditorPane jepClassIndiv  = new JEditorPane();					//panneau de classement
	private JEditorPane jepClassTeam   = new JEditorPane();
	//bouton d'enregistrement
	private JButton jbResultat         = new JButton();
	private JLabel jlCritClassement    = new JLabel();
	//critere individuel
	private Hashtable<String, JCheckBox> classmentCriteriaCB = new Hashtable<String, JCheckBox>();

	private JButton printClassementIndiv = new JButton();
	//critere par equipe
	//private JCheckBox sexeCaseEqu;
	//private JCheckBox armeCaseEqu;

	private JButton printClassementEquipe = new JButton();

	public ParametreDialog paramDialog;
	public ConcurrentDialog concDialog;
	public int index = 1;

	/**
	 * Création d'une fiche concours
	 *  
	 * 
	 * @param parentframe
	 * @param ficheConcours
	 */
	public FicheConcoursPane(ConcoursJeunesFrame parentframe, FicheConcours ficheConcours) {

		this.parentframe = parentframe;
		this.ficheConcours = ficheConcours;
		
		paramDialog = new ParametreDialog(this);

		init();

		concDialog = new ConcurrentDialog(parentframe, ficheConcours);
	}

	/**
	 * initialise les parametres de la fiche.
	 * 
	 */
	private void init() {
		JPanel northpane = new JPanel();
		JPanel northpaneEqu = new JPanel();

		//met la fiche de classement au format html
		jepClassIndiv.setEditorKit(new HTMLEditorKit());
		jepClassIndiv.setEditable(false);
		jepClassIndiv.addHyperlinkListener(this);
		//empeche le retour automatique au début de la page à chaque update
		((DefaultCaret)jepClassIndiv.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		jepClassTeam.setEditorKit(new HTMLEditorKit());
		jepClassTeam.setEditable(false);
		//empeche le retour automatique au début de la page à chaque update
		((DefaultCaret)jepClassTeam.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		jbResultat.addActionListener(this);

		printClassementIndiv.addActionListener(this);
		printClassementEquipe.addActionListener(this);

		//classement individuel
		northpane.add(jbResultat);

		northpane.add(jlCritClassement);
		for(Criterion criteria : ficheConcours.getParametre().getReglement().getListCriteria()) {
			JCheckBox checkBox = new JCheckBox(criteria.getLibelle(), criteria.isClassement());

			checkBox.addActionListener(this);
			if(ficheConcours.getParametre().getReglement().isOfficialReglement())
				checkBox.setEnabled(false);
			northpane.add(checkBox);
			
			classmentCriteriaCB.put(criteria.getCode(), checkBox);
		}
		northpane.add(printClassementIndiv);

		//classement par equipe
		northpaneEqu.add(printClassementEquipe);

		fichesDepart.setLayout(cl);
		
		for(int i = 0; i < ficheConcours.getParametre().getNbDepart(); i++) {
			FicheConcoursDepartPane ficheConcoursDepartPane = new FicheConcoursDepartPane(this, i);
			departPanels.add(ficheConcoursDepartPane);
			fichesDepart.add(ficheConcoursDepartPane, "depart." + i);
		}
		cl.first(fichesDepart);

		//paneau du classement individuel
		JPanel ficheI = new JPanel();
		ficheI.setLayout(new BorderLayout());
		ficheI.add(northpane,BorderLayout.NORTH);
		ficheI.add(new JScrollPane(jepClassIndiv),BorderLayout.CENTER);

		//paneau du classement par equipe
		JPanel ficheE = new JPanel();
		ficheE.setLayout(new BorderLayout());
		ficheE.add(northpaneEqu,BorderLayout.NORTH);
		ficheE.add(new JScrollPane(jepClassTeam),BorderLayout.CENTER);

		//panneau global
		tabbedpane.addChangeListener(this);
		tabbedpane.addTab("onglet.gestionarcher", null, fichesDepart); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.addTab("onglet.classementindividuel", //$NON-NLS-1$
				new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$
						File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer")), //$NON-NLS-1$
						ficheI);
		tabbedpane.addTab("onglet.classementequipe",new ImageIcon(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + //$NON-NLS-1$ //$NON-NLS-2$ 
				File.separator + ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.team")), //$NON-NLS-1$
				ficheE); 

		//integration
		setLayout(new BorderLayout());
		add(tabbedpane, BorderLayout.CENTER);

		affectLibelle();
	}

	private JPanel getGestArchersTabComponent() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(tabbedpane.getTitleAt(0));
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(this);

		for(int i = 1; i <= ficheConcours.getParametre().getNbDepart(); i++)
			comboBox.addItem(
					ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher.depart") + i);

		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(label, BorderLayout.CENTER);
		panel.add(comboBox, BorderLayout.EAST);
		return panel;
	}

	/**
	 * affecte les libelle localisé à l'interface
	 *
	 */
	private void affectLibelle() {
		jbResultat.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.saisieresultats")); //$NON-NLS-1$
		printClassementIndiv.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		printClassementEquipe.setText(ConcoursJeunes.ajrLibelle.getResourceString("bouton.impressionresultats")); //$NON-NLS-1$
		jlCritClassement.setText(ConcoursJeunes.ajrLibelle.getResourceString("interface.critereclassement")); //$NON-NLS-1$
		tabbedpane.setTitleAt(0, ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher")); //$NON-NLS-1$
		tabbedpane.setTabComponentAt(0, getGestArchersTabComponent());
		tabbedpane.setTitleAt(1, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementindividuel")); //$NON-NLS-1$
		tabbedpane.setTitleAt(2, ConcoursJeunes.ajrLibelle.getResourceString("onglet.classementequipe")); //$NON-NLS-1$
	}

	/**
	 * Affiche la boite de dialogue des parametre du concours
	 */
	public void openParametreDialog() {
		//l'ouvre si elle existe et la cré dans le cas contraire
		if(paramDialog != null) {
			paramDialog.setVisible(true);   
		} else
			paramDialog = new ParametreDialog(this);

	}

	/**
	 * Affiche la boite de dialogue de gestion des concurrents
	 * 
	 * @param concurrent - le concurrent à editer
	 */
	public void openConcurrentDialog(Concurrent concurrent) {
		if(concurrent != null) {
			int codeRetour = concDialog.showConcurrentDialog(concurrent);

			if(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT 
					|| codeRetour == ConcurrentDialog.CONFIRM_AND_PREVIOUS) {
				
				if(codeRetour == ConcurrentDialog.CONFIRM_AND_NEXT) {
					openConcurrentDialog(ficheConcours.nextConcurrent(concurrent));
				} else {
					assert codeRetour == ConcurrentDialog.CONFIRM_AND_PREVIOUS;
					
					openConcurrentDialog(ficheConcours.previousConcurrent(concurrent));
				}
			}
		}
	}

	/**
	 * Affiche la boite de dialogue de saisie des résultats
	 * 
	 * TODO Reprendre la saisi des scores
	 */
	public void openResultatDialog() {
		ResultatDialog resultat = new ResultatDialog(parentframe, 
				ficheConcours.getArcherlist().list(index, ficheConcours.getCurrentDepart()),
				ficheConcours.getParametre());

		//si annulation ne pas continuer
		int returnVal = resultat.showResultatDialog();
		if(returnVal == ResultatDialog.CANCEL)
			return;

		if(returnVal == ResultatDialog.NEXT_TARGET) {
			do {
				index++;
			} while(ficheConcours.getArcherlist().list(index).length == 0 && index <= ficheConcours.getParametre().getNbCible());
		} else if(returnVal == ResultatDialog.PREVIOUS_TARGET) {
			do {
				index--;
			} while(ficheConcours.getArcherlist().list(index).length == 0 && index > 0);
		}

		if(returnVal != ResultatDialog.SAVE_AND_QUIT && (index > 0 && index <= ficheConcours.getParametre().getNbCible())) {
			openResultatDialog();
		} else {
			index = 1;
		}
	}

	/**
	 * Exporte les résultats d'un concours pour un traitement ultérieur
	 * sur le site internet (mise en commun et diffusion)
	 * 
	 * @param export - la chaine XML des réultats à exporter
	 * 
	 * TODO Reprendre l'export
	 */
	public void exportConcours(String export) {
		ExportDialog exportDialog = new ExportDialog(parentframe);
		try {
			File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "concours" + ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(exportDialog.exportType == ExportDialog.EXPORT_FILE) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new AJFileFilter(ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export").substring(1), ConcoursJeunes.ajrLibelle.getResourceString("message.export.fichier"))); //$NON-NLS-1$ //$NON-NLS-2$
				int returnVal = chooser.showSaveDialog(this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					if(chooser.getSelectedFile().getAbsolutePath().endsWith(ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export"))) { //$NON-NLS-1$
						f = chooser.getSelectedFile();
					} else {
						f = new File(chooser.getSelectedFile().getAbsolutePath() + ConcoursJeunes.ajrParametreAppli.getResourceString("extention.export")); //$NON-NLS-1$
					}
					FileWriter fw = new FileWriter(f);
					fw.write(export, 0, export.length());
					fw.close();

					JOptionPane.showMessageDialog(this,
							ConcoursJeunes.ajrLibelle.getResourceString("message.export.fin"), //$NON-NLS-1$
							ConcoursJeunes.ajrLibelle.getResourceString("message.export"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				}
			} else if(exportDialog.exportType == ExportDialog.EXPORT_INTERNET) {
				OutputStreamWriter writer = null;
				BufferedReader reader = null;

				//création de la connection
				URL url = new URL(ConcoursJeunes.configuration.getExportURL());
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);

				writer = new OutputStreamWriter(conn.getOutputStream());
				writer.write(URLEncoder.encode("concours", "UTF-8")+ //$NON-NLS-1$ //$NON-NLS-2$
						"="+URLEncoder.encode(export, "UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
				writer.flush();
				writer.close();

				//lecture de la réponse
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String ligne;
				while ((ligne = reader.readLine()) != null) {
					if(ligne.equals("OK")) //$NON-NLS-1$
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("message.export.fin"), //$NON-NLS-1$
								ConcoursJeunes.ajrLibelle.getResourceString("message.export"),JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				}


			}
		} catch (UnknownHostException uhe) {
			JOptionPane.showMessageDialog(this,
					"<html>" + ConcoursJeunes.ajrLibelle.getResourceString("erreur.connection") + " " + uhe.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur.unknownhost"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			uhe.printStackTrace();
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this,
					"<html>" + io.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
					"IOException",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			io.printStackTrace();
		} catch(NullPointerException npe) {
			JOptionPane.showMessageDialog(this,
					ConcoursJeunes.ajrLibelle.getResourceString("erreur.save.export"), //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			npe.printStackTrace();
		}
	}

	/**
	 * @return  parentframe
	 * @uml.property  name="parentframe"
	 */
	public ConcoursJeunesFrame getParentframe() {
		return parentframe;
	}

	/**
	 * @param parentframe  parentframe à définir
	 * @uml.property  name="parentframe"
	 */
	public void setParentframe(ConcoursJeunesFrame parentframe) {
		this.parentframe = parentframe;
	}

	///////////////////////////////////////
	// Auditeur d'événement
	//////////////////////////////////////
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();

		if(source == jbResultat) {
			index = 1;
			openResultatDialog();
			jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML, ficheConcours.getCurrentDepart()));
		} else if(source == printClassementIndiv) {
			ficheConcours.printClassement();
		} else if(source == printClassementEquipe) {
			ficheConcours.printClassementEquipe();
		} else if(source instanceof JCheckBox) {
			for(Criterion criterion : ficheConcours.getParametre().getReglement().getListCriteria()) {
				criterion.setClassement(classmentCriteriaCB.get(criterion.getCode()).isSelected());
			}

			//ficheConcours.getParametre().setListCriteria()
			jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML, ficheConcours.getCurrentDepart()));
		} else if(source instanceof JComboBox) {
			ficheConcours.setCurrentDepart(((JComboBox)source).getSelectedIndex());
			cl.show(fichesDepart, "depart." + ficheConcours.getCurrentDepart());
		}
	}
	/**
	 * Gestion des changements d'onglet
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if(i > 0) {
				Concurrent[] listConcurrents = ficheConcours.getArcherlist().list(ficheConcours.getCurrentDepart());
				for(Concurrent concurrent : listConcurrents) {
					if(concurrent.getCible() == 0) {
						JOptionPane.showMessageDialog(this,
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible"), //$NON-NLS-1$ 
								ConcoursJeunes.ajrLibelle.getResourceString("erreur.nocible.titre"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						break;
					}
				}
				if(i == 1)
					jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML, ficheConcours.getCurrentDepart()));
				else if(i == 2)
					jepClassTeam.setText(ficheConcours.getClassementEquipe(FicheConcours.OUT_HTML));
			}
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e) { 
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { 
			if (e instanceof HTMLFrameHyperlinkEvent) { 
				((HTMLDocument)jepClassIndiv.getDocument()).processHTMLFrameHyperlinkEvent( 
						(HTMLFrameHyperlinkEvent)e); 
			} else {
				openConcurrentDialog(ficheConcours.getArcherlist().getConcurrentAt(ficheConcours.getCurrentDepart(), Integer.parseInt(e.getURL().getRef().substring(1)), 
						Integer.parseInt(e.getURL().getRef().substring(0,1))));

				jepClassIndiv.setText(ficheConcours.getClassement(FicheConcours.OUT_HTML, ficheConcours.getCurrentDepart()));
			} 
		} 
	}
}