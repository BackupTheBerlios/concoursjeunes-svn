/*
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.ajdeveloppement.concours.ui;

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.xml.bind.JAXBException;

import org.ajdeveloppement.commons.AJTemplate;
import org.ajdeveloppement.commons.ui.AJTabbedPane;
import org.ajdeveloppement.commons.ui.AJTabbedPaneListener;
import org.ajdeveloppement.commons.ui.GhostGlassPane;
import org.ajdeveloppement.commons.ui.MenuBarTools;
import org.ajdeveloppement.concours.exceptions.NullConfigurationException;
import org.ajdeveloppement.concours.ui.dialog.AboutDialog;
import org.ajdeveloppement.concours.ui.dialog.ConfigurationDialog;
import org.ajdeveloppement.concours.ui.dialog.DisablePluginDialog;
import org.ajdeveloppement.concours.ui.dialog.EntiteListDialog;
import org.ajdeveloppement.concours.ui.dialog.InstallPluginDialog;
import org.ajdeveloppement.concours.ui.dialog.ParametreDialog;
import org.ajdeveloppement.concours.ui.dialog.ReglementManagerDialog;
import org.ajdeveloppement.concours.ui.dialog.TextDialog;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.ajdeveloppement.xmlui.FrameCreator;
import org.concoursjeunes.AppInfos;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.Parametre;
import org.concoursjeunes.Profile;
import org.concoursjeunes.event.ProfileEvent;
import org.concoursjeunes.event.ProfileListener;
import org.concoursjeunes.plugins.Plugin.Type;
import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.h2.tools.Server;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * TODO Afficher status bar avec nb archers enregistre, place restante
 * 
 * @author Aurélien JEOFFRAY
 */
public class ConcoursJeunesFrame extends JFrame implements ActionListener, HyperlinkListener, 
		ProfileListener, AJTabbedPaneListener, ChangeListener, PropertyChangeListener {
	private JMenuItem jmiParametres;
	private AJTabbedPane tabbedpane;
	private JEditorPane jepHome;

	public Profile profile;

	private final AJTemplate ajtHome = new AJTemplate();

	/**
	 * Construction de l'interface graphique
	 * 
	 * @param profile le profile associé à l'interface
	 */
	public ConcoursJeunesFrame(Profile profile) {
		this.profile = profile;

		if (this.profile != null) {
			this.profile.addProfileListener(this);

			// affiche la boite de dialogue si le fichier de configuration
			// n'existe pas ou si il est
			// configurer pour un affichage
			if (ApplicationCore.getAppConfiguration().isFirstboot()) {
				showConfigurationDialog();
			}
			init();
			//setMinimumSize(new Dimension(800, 600));

			displayHome();
		} else {
			DisplayableErrorHelper.displayErrorInfo(this, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), "Moteur non initialiser", //$NON-NLS-1$ //$NON-NLS-2$
					"concoursJeunes pointe sur une référence null. Elle devrait pointer sur l'instance de l'objet ConcoursJeunes", //$NON-NLS-1$
					null, null, Level.SEVERE, null));
			System.exit(1);
		}
	}

	/**
	 * 
	 * 
	 */
	private void init() {
		FrameCreator frameCreator = new FrameCreator(this);

		frameCreator.formatTitle(AppInfos.NOM, AppInfos.VERSION + " - " + AppInfos.VERSION_DATE); //$NON-NLS-1$
		frameCreator.setL10N(profile.getLocalisation());
		frameCreator.addActionListener(this);
		frameCreator.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApp();
			}
		});

		frameCreator.createFrame(new File(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
				+ "/gui/ConcoursJeunes.xml")); //$NON-NLS-1$

		try {
			ajtHome.setLocalisationReader(profile.getLocalisation());
			ajtHome.loadTemplate(staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
					+ File.separator + staticParameters.getResourceString("template.accueil.html")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		}

		if(System.getProperty("noplugin") == null) { //$NON-NLS-1$
			fillOnDemandPlugin();
		}
		
		jmiParametres = (JMenuItem) frameCreator.getNamedComponent("mi.parametres"); //$NON-NLS-1$

		if (System.getProperty("debug.mode") != null) { //$NON-NLS-1$
			((JMenu) frameCreator.getNamedComponent("mi.debug")).setVisible(true); //$NON-NLS-1$
		}


		jepHome = (JEditorPane)((JScrollPane)frameCreator.getNamedComponent("jepHome")).getViewport().getView(); //$NON-NLS-1$
		jepHome.addHyperlinkListener(this);
		((DefaultCaret) jepHome.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		tabbedpane = (AJTabbedPane) frameCreator.getNamedComponent("tabbedpane"); //$NON-NLS-1$
		tabbedpane.addAJTabbedPaneListener(this);
		tabbedpane.addChangeListener(this);

		GhostGlassPane glassPane = new GhostGlassPane(0.5f);
		setGlassPane(glassPane);
	}

	private void fillOnDemandPlugin() {
		PluginLoader pl = new PluginLoader();
		List<PluginMetadata> plugins = pl.getPlugins(Type.ON_DEMAND);

		//if (plugins.size() > 0) {
		//	importMenu.setVisible(true);
		//}

		for (PluginMetadata pm : plugins) {
			JMenuItem mi = new JMenuItem(pm.getLocalizedOptionLabel());
			MenuBarTools.addItem(mi, getJMenuBar(), pm.getMenuPath());

			final Class<?> pluginClass = pm.getPluginClass();
			//mi.setActionCommand(pm.getClassName());
			mi.addActionListener(new ActionListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Constructor<?> c = pluginClass.getConstructor(JFrame.class, Profile.class);
						Object plugin = c.newInstance(ConcoursJeunesFrame.this, profile);
						for (Method m : pluginClass.getMethods()) {
							if (m.isAnnotationPresent(PluginEntry.class)) {
								m.invoke(plugin);
								break;
							}
						}
					} catch (InstantiationException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}  catch (SecurityException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
				}

			});

		}
	}

	private void showConfigurationDialog() {	
		ConfigurationDialog configurationDialog = new ConfigurationDialog(this, profile);
		Configuration configuration = configurationDialog.showConfigurationDialog(ApplicationCore.getAppConfiguration().clone(), profile.getConfiguration().clone());

		if (configuration != null) {
			//si le nom du profile à changer tous fermer
			if (!configuration.getCurProfil().equals(profile.getConfiguration().getCurProfil()) && !configurationDialog.isRenamedProfile()) {
				try {
					profile.closeAllFichesConcours();
				} catch (NullConfigurationException e) {
					DisplayableErrorHelper.displayException(e);
					e.printStackTrace();
				} catch (IOException e) {
					DisplayableErrorHelper.displayException(e);
					e.printStackTrace();
				} catch (JAXBException e) {
					DisplayableErrorHelper.displayException(e);
					e.printStackTrace();
				}
			}

			profile.setConfiguration(configuration);
			ApplicationCore.setAppConfiguration(configurationDialog.getWorkAppConfiguration());
			
			try {
				configuration.save();
				configurationDialog.getWorkAppConfiguration().save();
			} catch (JAXBException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			} catch (IOException e) {
				DisplayableErrorHelper.displayException(e);
				e.printStackTrace();
			}
			
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
			ajtHome.parse("LOGO_CLUB_URI", profile.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			ajtHome.parse("INTITULE_CLUB", profile.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			ajtHome.parse("PROFILE_NAME", profile.getConfiguration().getCurProfil()); //$NON-NLS-1$

			MetaDataFichesConcours metaDataFichesConcours = profile.getConfiguration().getMetaDataFichesConcours();
			if (metaDataFichesConcours.getFiches().size() > 0) {
				int i = 0;
				for (MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
					DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
					if(profile.isOpenFicheConcours(metaDataFicheConcours)) {
						ajtHome.parse("listconcours.STATE", ""); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						ajtHome.parse("listconcours.STATE", "./ressources/gestion.png"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					ajtHome.parse("listconcours.ID_CONCOURS", i + ""); //$NON-NLS-1$ //$NON-NLS-2$
					ajtHome.parse("listconcours.LIBELLE_CONCOURS", df.format(metaDataFicheConcours.getDateConcours()) //$NON-NLS-1$
							+ " - " + metaDataFicheConcours.getIntituleConcours()); //$NON-NLS-1$
					ajtHome.loopBloc("listconcours"); //$NON-NLS-1$
					i++;
				}
			} else {
				ajtHome.parseBloc("listconcours", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			Runnable homeUpdateThread = new Runnable() {
				@Override
				public void run() {
					jepHome.setText(ajtHome.output());
				}
			};
			
			if(SwingUtilities.isEventDispatchThread()) {
				homeUpdateThread.run();
			} else {
				SwingUtilities.invokeLater(homeUpdateThread);
			}
		}
	}

	/**
	 * 
	 * @param ficheConcours
	 */
	private void addFicheConcours(final FicheConcours ficheConcours) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FicheConcoursPane jif = new FicheConcoursPane(ConcoursJeunesFrame.this, ficheConcours);
		
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
				// ajoute la fiche à l'espace de travail et l'affiche
				tabbedpane.addTab(ficheConcours.getParametre().getIntituleConcours() + " - " + df.format(ficheConcours.getParametre().getDateDebutConcours()), jif); //$NON-NLS-1$
				tabbedpane.setSelectedComponent(jif);
				
				jif = null;
		
				displayHome();
			}
		});
	}

	/**
	 * 
	 * @param tabComponent
	 */
	private void removeFicheConcours(Component tabComponent) {
		FicheConcoursPane jif = (FicheConcoursPane) tabComponent;
		try {
			profile.closeFicheConcours(jif.getFicheConcours());
			jif.dispose();
		} catch (NullConfigurationException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		} catch (IOException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		} catch (JAXBException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
		jif = null; 
	}

	private void closeApp() {
		try {
			profile.saveAllFichesConcours();
			
			ApplicationCore.dbConnection.close();
		} catch (NullConfigurationException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
			System.exit(1);
		} catch (JAXBException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
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
			new EntiteListDialog(this, profile, false);

			// affiche la boite de dialogue des parametres
		} else if (cmd.equals("menubar.edition.parametre")) { //$NON-NLS-1$
			if (jif != null)
				jif.openParametreDialog();

		} else if (cmd.equals("menubar.edition.reglement")) { //$NON-NLS-1$
			ReglementManagerDialog reglementManagerDialog = new ReglementManagerDialog(this, profile);
			reglementManagerDialog.showReglementManagerDialog(false);
		} else if (cmd.equals("menubar.tools.disableplugins")) { //$NON-NLS-1$
			DisablePluginDialog disablePluginDialog = new DisablePluginDialog(this, profile.getLocalisation());
			disablePluginDialog.showDisablePluginDialog();
		} else if (cmd.equals("menubar.tools.installplugins")) { //$NON-NLS-1$
			InstallPluginDialog installPluginDialog = new InstallPluginDialog(this, profile);
			installPluginDialog.showInstallPluginDialog();
		} else if (cmd.equals("menubar.tools.configuration")) { //$NON-NLS-1$
			showConfigurationDialog();
			// affiche la boite de dialogie "A propos"
		} else if (cmd.equals("menubar.aide.apropos")) { //$NON-NLS-1$
			AboutDialog aboutDialog = new AboutDialog(this, profile.getLocalisation());
			aboutDialog.showAboutDialog();
		} else if (cmd.equals("menubar.aide.aide")) { //$NON-NLS-1$
			// affiche le pdf avec le reader pdf standard du systeme
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(new File(staticParameters.getResourceString("path.documentation"))); //$NON-NLS-1$
				} else {
					String NAV = ApplicationCore.getAppConfiguration().getPdfReaderPath();

					Runtime.getRuntime().exec(NAV, new String[] { staticParameters.getResourceString("path.documentation") }); //$NON-NLS-1$ 
				}
			} catch (IOException e1) {
				DisplayableErrorHelper.displayException(e1);
				e1.printStackTrace();
			}
		} else if (cmd.equals("menubar.aide.versionnote")) { //$NON-NLS-1$
			TextDialog textDialog = new TextDialog(this, profile.getLocalisation());
			textDialog.showTextDialog(profile.getLocalisation().getResourceString("changelog.title"), new File("changelog.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (cmd.equals("menubar.aide.licence")) { //$NON-NLS-1$
			TextDialog textDialog = new TextDialog(this, profile.getLocalisation());
			textDialog.showTextDialog(profile.getLocalisation().getResourceString("licence.title"), new File("Licence.txt")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
			if (jif != null) {
				org.concoursjeunes.debug.Debug.attributePoints(jif.getFicheConcours().getConcurrentList());
			}

			// debugage -> RAZ des points
		} else if (cmd.equals("menubar.debug.resetpoints")) { //$NON-NLS-1$
			if (jif != null)
				org.concoursjeunes.debug.Debug.resetPoints(jif.getFicheConcours());
		} else if (cmd.equals("menubar.debug.launchsqlconsole")) { //$NON-NLS-1$
			Thread sqlConsole = new Thread() {
				@Override
				public void run() {
					try {
						//System.setProperty("java.net.useSystemProxies","false");  //$NON-NLS-1$//$NON-NLS-2$
						Server.startWebServer(ApplicationCore.dbConnection);
						//System.setProperty("java.net.useSystemProxies","true"); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (SQLException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
				}
			};
			sqlConsole.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == tabbedpane) {
			int i = tabbedpane.getSelectedIndex();
			if (i > 0) {
				jmiParametres.setEnabled(true);
			} else {
				jmiParametres.setEnabled(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursCreated(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	@Override
	public void ficheConcoursCreated(ProfileEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addPropertyChangeListener(this);
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursDeleted(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	@Override
	public void ficheConcoursDeleted(ProfileEvent concoursJeunesEvent) {
		displayHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursClosed(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	@Override
	public void ficheConcoursClosed(ProfileEvent concoursJeunesEvent) {
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
	@Override
	public void ficheConcoursRestored(ProfileEvent concoursJeunesEvent) {
		addFicheConcours(concoursJeunesEvent.getFicheConcours());
		concoursJeunesEvent.getFicheConcours().getParametre().addPropertyChangeListener(this);

		displayHome();

		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(!ConcoursJeunesFrame.this.jepHome.isEnabled()) {
					ConcoursJeunesFrame.this.setCursor(Cursor.getDefaultCursor());
					ConcoursJeunesFrame.this.jepHome.setEnabled(true);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#configurationChanged(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	@Override
	public void configurationChanged(ProfileEvent concoursJeunesEvent) {
		displayHome();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof Parametre) {
			if(evt.getPropertyName().equals("intituleConcours") //$NON-NLS-1$
					|| evt.getPropertyName().equals("date")) { //$NON-NLS-1$
				displayHome();
			}
		}
	}

	@Override
	public void tabAdded(Component tabComponent) {

	}

	@Override
	public void tabClosed(Component tabComponent) {
		removeFicheConcours(tabComponent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (e instanceof HTMLFrameHyperlinkEvent) {
				((HTMLDocument) jepHome.getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) e);
			} else {
				if (e.getURL().getHost().equals("open_concours")) { //$NON-NLS-1$
					final String concref = e.getURL().getRef();
					
					final MetaDataFicheConcours metaDataFicheConcours = profile.getConfiguration().getMetaDataFichesConcours().get(Integer.parseInt(concref));
					if(metaDataFicheConcours.getFilenameConcours().endsWith(".cta")) { //$NON-NLS-1$
						JOptionPane.showMessageDialog(this, 
								profile.getLocalisation().getResourceString("home.warning.oldformat"), //$NON-NLS-1$
								profile.getLocalisation().getResourceString("home.warning.oldformat.title"), //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					} else {
						if(!profile.isOpenFicheConcours(metaDataFicheConcours)) {
							Thread launchFiche = new Thread() {
								@Override
								public void run() {
									
									try {
										profile.restoreFicheConcours(metaDataFicheConcours);
									} catch (NumberFormatException e) {
										DisplayableErrorHelper.displayException(e);
										e.printStackTrace();
									} catch (NullConfigurationException e) {
										DisplayableErrorHelper.displayException(e);
										e.printStackTrace();
									} catch (IOException e) {
										DisplayableErrorHelper.displayException(e);
										e.printStackTrace();
									} catch (NullPointerException e) {
										DisplayableErrorHelper.displayException(e);
										e.printStackTrace();
									} catch (JAXBException e) {
										DisplayableErrorHelper.displayException(e);
										e.printStackTrace();
									}
								}
							};
							this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							this.jepHome.setEnabled(false);
							Thread.yield();
							launchFiche.start();
						} else {
							ConcoursJeunesFrame.this.setCursor(Cursor.getDefaultCursor());
							for (int i = 1; i < tabbedpane.getTabCount(); i++) {
								FicheConcoursPane jif = (FicheConcoursPane) tabbedpane.getComponentAt(i);
								if (jif.getFicheConcours().getMetaDataFicheConcours().equals(metaDataFicheConcours)) {
									tabbedpane.setSelectedIndex(i);
									break;
								}
							}
							/*JOptionPane.showMessageDialog(this, 
									profile.getLocalisation().getResourceString("home.warning.alreadyopen"), //$NON-NLS-1$
									profile.getLocalisation().getResourceString("home.warning.alreadyopen.title"), //$NON-NLS-1$
									JOptionPane.WARNING_MESSAGE);*/
						}
					}
				} else if (e.getURL().getHost().equals("delete_concours")) { //$NON-NLS-1$
					if (JOptionPane.showConfirmDialog(this, profile.getLocalisation().getResourceString("confirmation.suppression.concours"), //$NON-NLS-1$
							profile.getLocalisation().getResourceString("confirmation.suppression.concours.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
						try {
							profile.deleteFicheConcours(profile.getConfiguration().getMetaDataFichesConcours().get(Integer.parseInt(e.getURL().getRef())));
						} catch (NumberFormatException e1) {
							DisplayableErrorHelper.displayException(e1);
							e1.printStackTrace();
						} catch (NullConfigurationException e1) {
							DisplayableErrorHelper.displayException(e1);
							e1.printStackTrace();
						} catch (JAXBException e1) {
							DisplayableErrorHelper.displayException(e1);
							e1.printStackTrace();
						} catch (IOException e1) {
							DisplayableErrorHelper.displayException(e1);
							e1.printStackTrace();
						}
						displayHome();
					}
				} else if (e.getURL().getHost().equals("new_concours")) { //$NON-NLS-1$
					final Parametre parametre = new Parametre(profile.getConfiguration());
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					this.jepHome.setEnabled(false);
					Thread.yield();
					
					ParametreDialog parametreDialog = new ParametreDialog(this, profile, null);
					parametreDialog.showParametreDialog(parametre);
					if(parametre.isReglementLock()) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								try {
									profile.createFicheConcours(parametre);
								} catch (NullConfigurationException e1) {
									DisplayableErrorHelper.displayException(e1);
									e1.printStackTrace();
								} catch (IOException e1) {
									DisplayableErrorHelper.displayException(e1);
									e1.printStackTrace();
								} catch (JAXBException e1) {
									DisplayableErrorHelper.displayException(e1);
									e1.printStackTrace();
								}
								ConcoursJeunesFrame.this.jepHome.setEnabled(true);
								ConcoursJeunesFrame.this.setCursor(Cursor.getDefaultCursor());
							}
						});
					} else {
						this.jepHome.setEnabled(true);
					}
				} else if (e.getURL().getHost().equals("change_profile")) { //$NON-NLS-1$
					showConfigurationDialog();
				} else if(e.getURL().getProtocol().equals("http")) { //$NON-NLS-1$
					try {
						if(Desktop.isDesktopSupported()) {
							Desktop.getDesktop().browse(e.getURL().toURI());
						}
					} catch (IOException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						DisplayableErrorHelper.displayException(e1);
						e1.printStackTrace();
					}
				}
			}
		}
	}
}