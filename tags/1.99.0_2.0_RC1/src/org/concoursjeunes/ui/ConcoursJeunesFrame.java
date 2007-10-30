package org.concoursjeunes.ui;

import static org.concoursjeunes.ConcoursJeunes.ajrParametreAppli;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.naming.ConfigurationException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ConcoursJeunesEvent;
import org.concoursjeunes.ConcoursJeunesListener;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.ParametreEvent;
import org.concoursjeunes.ParametreListener;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.ReglementBuilder;
import org.concoursjeunes.dialog.ConfigurationDialog;
import org.concoursjeunes.dialog.EntiteListDialog;
import org.concoursjeunes.dialog.ReglementDialog;
import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.jdesktop.swingx.JXErrorDialog;

import ajinteractive.standard.common.AJTemplate;
import ajinteractive.standard.common.PluginClassLoader;
import ajinteractive.standard.ui.AJTabbedPane;
import ajinteractive.standard.ui.AJTabbedPaneListener;
import ajinteractive.standard.ui.FrameCreator;
import ajinteractive.standard.ui.GhostGlassPane;
import ajinteractive.standard.ui.MenuBarTools;
import ajinteractive.standard.utilities.io.FileUtil;

/**
 * @author Aurélien JEOFFRAY
 */
public class ConcoursJeunesFrame extends JFrame implements ActionListener, HyperlinkListener, ConcoursJeunesListener, ParametreListener, AJTabbedPaneListener, ChangeListener {
	
	private static final String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static final String EXT_XML = ".xml"; //$NON-NLS-1$

	private JMenuItem jmiParametres;
	private JMenu jmReglements;
	private JMenu jmImpression;
	private AJTabbedPane tabbedpane;
	private JEditorPane jepHome;

	private ConcoursJeunes concoursJeunes;

	private final AJTemplate ajtHome = new AJTemplate();

	/**
	 * Construction de l'interface graphique
	 * 
	 * @param concoursJeunes
	 */
	public ConcoursJeunesFrame(ConcoursJeunes concoursJeunes) {
		this.concoursJeunes = concoursJeunes;

		if (this.concoursJeunes != null) {
			this.concoursJeunes.addConcoursJeunesListener(this);

			// affiche la boite de dialogue si le fichier de configuration
			// n'existe pas ou si il est
			// configurer pour un affichage
			if (ConcoursJeunes.configuration.isFirstboot()) {
				showConfigurationDialog();
			}
			init();
			setMinimumSize(new Dimension(750, 580));

			displayHome();
		} else {
			JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), "Moteur non initialiser", //$NON-NLS-1$ //$NON-NLS-2$
					"concoursJeunes pointe sur une référence null. Elle devrait pointer sur l'instance de l'objet ConcoursJeunes"); //$NON-NLS-1$
			System.exit(1);
		}
	}

	/**
	 * 
	 * 
	 */
	private void init() {
		FrameCreator frameCreator = new FrameCreator(this);

		frameCreator.formatTitle(ConcoursJeunes.NOM, ConcoursJeunes.VERSION);
		frameCreator.setLibelleAjResourcesReader(ConcoursJeunes.ajrLibelle);
		frameCreator.addActionListener(this);
		frameCreator.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApp();
			}
		});

		frameCreator.createFrame(new File(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ "/gui/ConcoursJeunes.xml")); //$NON-NLS-1$

		ajtHome.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.accueil.html")); //$NON-NLS-1$

		fillOnDemandPlugin((JMenu) frameCreator.getNamedComponent("mi.import")); //$NON-NLS-1$

		jmReglements = (JMenu) frameCreator.getNamedComponent("mi.reglements"); //$NON-NLS-1$
		fillReglementItem(jmReglements);

		jmiParametres = (JMenuItem) frameCreator.getNamedComponent("mi.parametres"); //$NON-NLS-1$

		jmImpression = (JMenu) frameCreator.getNamedComponent("mi.print"); //$NON-NLS-1$

		if (System.getProperty("debug.mode") != null) { //$NON-NLS-1$
			((JMenu) frameCreator.getNamedComponent("mi.debug")).setVisible(true); //$NON-NLS-1$
		}

		jepHome = (JEditorPane) frameCreator.getNamedComponent("jepHome"); //$NON-NLS-1$
		jepHome.addHyperlinkListener(this);
		((DefaultCaret) jepHome.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		tabbedpane = (AJTabbedPane) frameCreator.getNamedComponent("tabbedpane"); //$NON-NLS-1$
		tabbedpane.addAJTabbedPaneListener(this);
		tabbedpane.addChangeListener(this);

		GhostGlassPane glassPane = new GhostGlassPane();
		setGlassPane(glassPane);
	}

	private void fillOnDemandPlugin(JMenu importMenu) {
		PluginLoader pl = new PluginLoader();
		ArrayList<PluginMetadata> plugins = pl.getPlugins(PluginMetadata.ONDEMAND_PLUGIN);

		if (plugins.size() > 0) {
			importMenu.setVisible(true);
		}

		for (PluginMetadata pm : plugins) {
			JMenuItem mi = new JMenuItem(pm.getOptionLabel());
			MenuBarTools.addItem(mi, getJMenuBar(), pm.getMenuPath());

			mi.setActionCommand(pm.getClassName());
			mi.addActionListener(new ActionListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					try {
						Class<?> cla = null;
						String importClass = e.getActionCommand();
						if (importClass != null) {
							cla = Class.forName(importClass, false, new PluginClassLoader(findParentClassLoader(), new File("plugins"))); //$NON-NLS-1$
						}

						if (cla != null) {
							Constructor<?> c = cla.getConstructor(JFrame.class);
							Object plugin = c.newInstance(ConcoursJeunesFrame.this);
							for (Method m : cla.getMethods()) {
								if (m.isAnnotationPresent(PluginEntry.class)) {
									m.invoke(plugin, (Object[]) null);
									break;
								}
							}
						}
					} catch (InstantiationException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (SecurityException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					} catch (MalformedURLException e1) {
						JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					}
				}

			});

		}
	}

	private void fillReglementItem(JMenu reglementMenu) {

		reglementMenu.removeAll();

		String[] availableReglements = Reglement.listAvailableReglements();
		if (availableReglements != null) {
			for (String reglementName : availableReglements) {
				JMenuItem jmiReglement = new JMenuItem(reglementName);
				jmiReglement.setActionCommand(reglementName);
				jmiReglement.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ReglementDialog reglementDialog = new ReglementDialog(ConcoursJeunesFrame.this, ReglementBuilder.createReglement(e.getActionCommand()));
						Reglement reglement = reglementDialog.showReglementDialog();
						if (reglement != null) {
							reglement.save();
						}
					}
				});
				reglementMenu.add(jmiReglement);
			}
			reglementMenu.addSeparator();
		}
		JMenuItem jmiNewReglement = new JMenuItem(ConcoursJeunes.ajrLibelle.getResourceString("menubar.edition.reglement.new")); //$NON-NLS-1$
		jmiNewReglement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reglementName = JOptionPane.showInputDialog(ConcoursJeunes.ajrLibelle.getResourceString("reglement.general.addreglement")); //$NON-NLS-1$
				if (reglementName != null) {
					Reglement reglement = new Reglement(reglementName);

					ReglementDialog reglementDialog = new ReglementDialog(ConcoursJeunesFrame.this, reglement);
					reglement = reglementDialog.showReglementDialog();
					if (reglement != null) {
						reglement.save();

						fillReglementItem(jmReglements);
					}
				}
			}
		});
		reglementMenu.add(jmiNewReglement);
	}

	/**
	 * Locates the best class loader based on context (see class description).
	 * 
	 * @return The best parent classloader to use
	 */
	private ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = this.getClass().getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return parent;
	}

	private void showConfigurationDialog() {	
		ConfigurationDialog configurationDialog = new ConfigurationDialog(this);
		Configuration configuration = configurationDialog.showConfigurationDialog(ConcoursJeunes.configuration.clone());

		if (configuration != null) {
			//si le nom du profile à changer tous fermer
			if (!configuration.getCurProfil().equals(ConcoursJeunes.configuration.getCurProfil())) {
				try {
					concoursJeunes.closeAllFichesConcours();
				} catch (ConfigurationException e1) {
					JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
							e1.fillInStackTrace());
					e1.printStackTrace();
				}
				//si le profil à simplement été renomé
				if(configurationDialog.isRenamedProfile()) {
					boolean success = false;
					
					//renome le fichier de configuration
					File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + ConcoursJeunes.configuration.getCurProfil() + EXT_XML);
					File fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + configuration.getCurProfil() + EXT_XML);
					success = f.renameTo(fNew);

					//renome le dossier du profil
					f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + "Profile" + File.separator + ConcoursJeunes.configuration.getCurProfil()); //$NON-NLS-1$
					fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + "Profile" + File.separator + configuration.getCurProfil()); //$NON-NLS-1$
					
					if(success && !f.renameTo(fNew)) {
						try {
							FileUtil.deleteFilesPath(fNew);
							fNew.delete();
							success = f.renameTo(fNew);
						} catch (IOException e1) {
							success = false;
							e1.printStackTrace();
						}
						
						if(!success) {
							//si le renomage du dossier echoue (pouvant avoir pour seul cause
							//une erreur système) revenir en arrière
							f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + ConcoursJeunes.configuration.getCurProfil() + EXT_XML);
							fNew = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + File.separator + CONFIG_PROFILE + configuration.getCurProfil() + EXT_XML);
							fNew.renameTo(f);
						}
					}
					
					if(!success) {
						configuration.setCurProfil(ConcoursJeunes.configuration.getCurProfil());
						
						JOptionPane.showMessageDialog(this, "<html>La tentative de renomage du profil à échoué,<br>L'ancien nom a été conservé", "Erreur renomage", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

			ConcoursJeunes.configuration = configuration;
			
			configuration.save();
			configuration.saveAsDefault();
			
			displayHome();
		}
	}

	/**
	 * Affiche une boite de dialogue avec la liste des concours existant pour le profil propose de créer une nouvelle fiche, d'ouvrir une fiche existante, de supprimer une fiche
	 * 
	 */
	private void displayHome() {
		if (jepHome != null) {
			ajtHome.reset();
			ajtHome.parse("lib_active_profile", ConcoursJeunes.ajrLibelle.getResourceString("home.activeprofile"));
			ajtHome.parse("lib_gest_competion", ConcoursJeunes.ajrLibelle.getResourceString("home.gestcompet"));
			ajtHome.parse("lib_new", ConcoursJeunes.ajrLibelle.getResourceString("home.new"));
			ajtHome.parse("lib_delete", ConcoursJeunes.ajrLibelle.getResourceString("home.delete"));
			ajtHome.parse("lib_info", ConcoursJeunes.ajrLibelle.getResourceString("home.info"));
			ajtHome.parse("LOGO_CLUB_URI", ConcoursJeunes.configuration.getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			ajtHome.parse("INTITULE_CLUB", ConcoursJeunes.configuration.getClub().getNom()); //$NON-NLS-1$
			ajtHome.parse("PROFILE_NAME", ConcoursJeunes.configuration.getCurProfil()); //$NON-NLS-1$

			MetaDataFichesConcours metaDataFichesConcours = ConcoursJeunes.configuration.getMetaDataFichesConcours();
			if (metaDataFichesConcours.getFiches().size() > 0) {
				int i = 0;
				for (MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
					DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
					ajtHome.parse("listconcours.STATE", ""); //$NON-NLS-1$ //$NON-NLS-2$
					ajtHome.parse("listconcours.ID_CONCOURS", i + ""); //$NON-NLS-1$ //$NON-NLS-2$
					ajtHome.parse("listconcours.LIBELLE_CONCOURS", df.format(metaDataFicheConcours.getDateConcours()) //$NON-NLS-1$
							+ " - " + metaDataFicheConcours.getIntituleConcours()); //$NON-NLS-1$

					ajtHome.loopBloc("listconcours"); //$NON-NLS-1$
					i++;
				}
			} else {
				ajtHome.parseBloc("listconcours", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			jepHome.setText(ajtHome.output());
		}
	}

	/**
	 * 
	 * @param ficheConcours
	 */
	private void addFicheConcours(FicheConcours ficheConcours) {
		FicheConcoursPane jif = new FicheConcoursPane(this, ficheConcours);

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		// ajoute la fiche à l'espace de travail et l'affiche
		tabbedpane.addTab(ficheConcours.getParametre().getIntituleConcours() + " - " + df.format(ficheConcours.getParametre().getDate()), jif); //$NON-NLS-1$
		tabbedpane.setSelectedComponent(jif);
		
		jif = null;

		displayHome();
	}

	/**
	 * 
	 * @param tabComponent
	 */
	private void removeFicheConcours(Component tabComponent) {
		FicheConcoursPane jif = (FicheConcoursPane) tabComponent;
		try {
			concoursJeunes.closeFicheConcours(jif.getFicheConcours());
			jif.dispose();
		} catch (ConfigurationException e) {
			JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					e.fillInStackTrace());
			e.printStackTrace();
		}
		jif = null; 
	}

	private void closeApp() {
		try {
			concoursJeunes.saveAllFichesConcours();
		} catch (ConfigurationException e) {
			JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					e.fillInStackTrace());
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * @return concoursJeunes
	 */
	public ConcoursJeunes getConcoursJeunes() {
		return concoursJeunes;
	}

	/**
	 * @param concoursJeunes
	 *            concoursJeunes à définir
	 */
	public void setConcoursJeunes(ConcoursJeunes concoursJeunes) {
		this.concoursJeunes = concoursJeunes;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		FicheConcoursPane jif = null;

		// determine la fenetre active
		if (tabbedpane.getSelectedIndex() > 0)
			jif = (FicheConcoursPane) tabbedpane.getSelectedComponent();

		// enregistre l'ensemble des fiche et quitte l'application
		if (cmd.equals("menubar.fichier.quitter")) { //$NON-NLS-1$
			closeApp();

			// affiche la liste des entites (Fédération, CD, Club)
		} else if (cmd.equals("menubar.edition.entite")) { //$NON-NLS-1$
			new EntiteListDialog(this);

			// affiche la boite de dialogue des parametres
		} else if (cmd.equals("menubar.edition.parametre")) { //$NON-NLS-1$
			if (jif != null)
				jif.openParametreDialog();

			// affiche la boite de dialogue de configuartion
		} else if (cmd.equals("menubar.edition.configuration")) { //$NON-NLS-1$
			showConfigurationDialog();
			
			// imprime la liste des concurrents par ordre alphabetique
		} else if (cmd.equals("menubar.impression.listeconcurrent.ordrealpha")) { //$NON-NLS-1$
			if (jif != null) {
				if(!jif.getFicheConcours().printArcherList(FicheConcours.ALPHA)) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}
			}

			// imprime la liste des concurrents par ordre alphabetique avec
			// information greffe
		} else if (cmd.equals("menubar.impression.listeconcurrent.greffe")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printArcherList(FicheConcours.GREFFE)) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// imprime la liste des concurrents par ordre sur le pas de tir
		} else if (cmd.equals("menubar.impression.listeconcurrent.bytarget")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printArcherList(FicheConcours.TARGET)) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}
			
			// imprime les etiquettes concurrent
		} else if (cmd.equals("menubar.impression.listeconcurrent.etiquette")) { //$NON-NLS-1$

			if (jif != null)
				if(!jif.getFicheConcours().printEtiquettes()) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// imprime la vu du pas de tir
		} else if (cmd.equals("menubar.impression.pasdetir")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printPasDeTir()) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// imprime le classement individuel
		} else if (cmd.equals("menubar.impression.classement.individuel")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassement()) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// imprime le classement par equipe
		} else if (cmd.equals("menubar.impression.classement.equipe")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassementEquipe()) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// aimprime le classement par club
		} else if (cmd.equals("menubar.impression.classement.club")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassementClub()) {
					JOptionPane.showMessageDialog(this, "Rien à imprimer");
				}

			// affiche la boite de dialogie "A propos"
		} else if (cmd.equals("menubar.aide.apropos")) { //$NON-NLS-1$
			
			MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
			JOptionPane.showMessageDialog(this, "<html>ConcoursJeunes<br>" + //$NON-NLS-1$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.description") + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.version") + "<br>" + ConcoursJeunes.VERSION + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					ConcoursJeunes.COPYR + " " + ConcoursJeunes.AUTEURS + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
					"version base: " + ConcoursJeunes.dbVersion + "<br><br>" //$NON-NLS-1$ //$NON-NLS-2$
					+ "mémoire utilisé: " + ((memoryBean.getHeapMemoryUsage().getUsed() + memoryBean.getNonHeapMemoryUsage().getUsed()) / 1024 / 1024) + "Mo<br>" //$NON-NLS-1$ //$NON-NLS-2$
					+ "mémoire réservé: " + ((memoryBean.getHeapMemoryUsage().getCommitted() + memoryBean.getNonHeapMemoryUsage().getCommitted()) / 1024 / 1024) + "Mo<br><br>" //$NON-NLS-1$ //$NON-NLS-2$
					+ ConcoursJeunes.ajrLibelle.getResourceString("apropos.liens") + "<br></html>", //$NON-NLS-1$ //$NON-NLS-2$
					ConcoursJeunes.ajrLibelle.getResourceString("apropos.titre"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		} else if (cmd.equals("menubar.aide.aide")) { //$NON-NLS-1$
			// affiche le pdf avec le reader pdf standard du systeme
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(new File("documentation/Documentation ConcoursJeunes.pdf")); //$NON-NLS-1$
				} else {
					String NAV = ConcoursJeunes.configuration.getPdfReaderPath();

					Runtime.getRuntime().exec(NAV, new String[] { "documentation/Documentation ConcoursJeunes.pdf" }); //$NON-NLS-1$ 
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
			if (jif != null) {
				org.concoursjeunes.debug.Debug.attributePoints(jif.getFicheConcours().getConcurrentList(), 0);
			}

			// debugage -> RAZ des points
		} else if (cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
			if (jif != null)
				org.concoursjeunes.debug.Debug.resetPoints(jif.getFicheConcours());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if (i > 0) {
				jmiParametres.setEnabled(true);
				jmImpression.setVisible(true);
			} else {
				jmiParametres.setEnabled(false);
				jmImpression.setVisible(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursCreated(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addParametreListener(this);
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursDeleted(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent) {
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursClosed(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent) {
		//
		for (int i = 1; i < tabbedpane.getTabCount(); i++) {
			FicheConcoursPane jif = (FicheConcoursPane) tabbedpane.getComponentAt(i);
			if (jif.getFicheConcours() == concoursJeunesEvent.getFicheConcours()) {
				tabbedpane.removeTabAt(i);

				break;
			}
		}
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursRestored(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addParametreListener(this);
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ParametreListener#metaDataChanged(org.concoursjeunes.ParametreEvent)
	 */
	public void metaDataChanged(ParametreEvent parametreEvent) {
		displayHome();
	}

	public void parametreChanged(ParametreEvent parametreEvent) {

	}

	public void tabAdded(Component tabComponent) {

	}

	public void tabClosed(Component tabComponent) {
		removeFicheConcours(tabComponent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (e instanceof HTMLFrameHyperlinkEvent) {
				((HTMLDocument) jepHome.getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) e);
			} else {
				if (e.getURL().getHost().equals("open_concours")) { //$NON-NLS-1$
					final String concref = e.getURL().getRef();
					Thread launchFiche = new Thread() {
						@Override
						public void run() {
							
							try {
								concoursJeunes.restoreFicheConcours(ConcoursJeunes.configuration.getMetaDataFichesConcours().get(Integer.parseInt(concref)));
							} catch (NumberFormatException e) {
								JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
										e.fillInStackTrace());
								e.printStackTrace();
							} catch (ConfigurationException e) {
								JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
										e.fillInStackTrace());
								e.printStackTrace();
							} catch (IOException e) {
								JXErrorDialog.showDialog(ConcoursJeunesFrame.this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
										e.fillInStackTrace());
								e.printStackTrace();
							}
							ConcoursJeunesFrame.this.setCursor(Cursor.getDefaultCursor());
							ConcoursJeunesFrame.this.jepHome.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							ConcoursJeunesFrame.this.jepHome.setEnabled(true);
						}
					};
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					this.jepHome.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					this.jepHome.setEnabled(false);
					Thread.yield();
					launchFiche.start();
				} else if (e.getURL().getHost().equals("delete_concours")) { //$NON-NLS-1$
					if (JOptionPane.showConfirmDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.concours"), //$NON-NLS-1$
							ConcoursJeunes.ajrLibelle.getResourceString("confirmation.suppression.concours.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
						try {
							concoursJeunes.deleteFicheConcours(ConcoursJeunes.configuration.getMetaDataFichesConcours().get(Integer.parseInt(e.getURL().getRef())));
						} catch (NumberFormatException e1) {
							JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
									e1.fillInStackTrace());
							e1.printStackTrace();
						} catch (ConfigurationException e1) {
							JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
									e1.fillInStackTrace());
							e1.printStackTrace();
						}
					}
				} else if (e.getURL().getHost().equals("new_concours")) { //$NON-NLS-1$
					try {
						this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						
						concoursJeunes.createFicheConcours();
						this.setCursor(Cursor.getDefaultCursor());
					} catch (ConfigurationException e1) {
						JXErrorDialog.showDialog(this, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
								e1.fillInStackTrace());
						e1.printStackTrace();
					}
				} else if (e.getURL().getHost().equals("change_profile")) { //$NON-NLS-1$
					showConfigurationDialog();
				}
			}
		}
	}
}