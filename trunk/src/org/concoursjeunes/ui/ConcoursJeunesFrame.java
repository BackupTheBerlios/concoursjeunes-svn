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
 *  (at your option) any later version.
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
package org.concoursjeunes.ui;

import static org.concoursjeunes.ApplicationCore.ajrParametreAppli;

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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.MetaDataFichesConcours;
import org.concoursjeunes.Parametre;
import org.concoursjeunes.Reglement;
import org.concoursjeunes.builders.ReglementBuilder;
import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.concoursjeunes.event.ParametreEvent;
import org.concoursjeunes.event.ParametreListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.concoursjeunes.plugins.Plugin.Type;
import org.concoursjeunes.ui.dialog.*;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import ajinteractive.standard.common.AJTemplate;
import ajinteractive.standard.ui.AJTabbedPane;
import ajinteractive.standard.ui.AJTabbedPaneListener;
import ajinteractive.standard.ui.FrameCreator;
import ajinteractive.standard.ui.GhostGlassPane;
import ajinteractive.standard.ui.MenuBarTools;

/**
 * TODO Afficher status bar avec nb archers enregistre, place restante
 * 
 * @author Aurélien JEOFFRAY
 */
public class ConcoursJeunesFrame extends JFrame implements ActionListener, HyperlinkListener, ConcoursJeunesListener, ParametreListener, AJTabbedPaneListener, ChangeListener {
	private JMenuItem jmiParametres;
	private JMenu jmReglements;
	private JMenu jmImpression;
	private AJTabbedPane tabbedpane;
	private JEditorPane jepHome;

	private ApplicationCore concoursJeunes;

	private final AJTemplate ajtHome = new AJTemplate();

	/**
	 * Construction de l'interface graphique
	 * 
	 * @param concoursJeunes
	 */
	public ConcoursJeunesFrame(ApplicationCore concoursJeunes) {
		this.concoursJeunes = concoursJeunes;

		if (this.concoursJeunes != null) {
			this.concoursJeunes.addConcoursJeunesListener(this);

			// affiche la boite de dialogue si le fichier de configuration
			// n'existe pas ou si il est
			// configurer pour un affichage
			if (ApplicationCore.getConfiguration().isFirstboot()) {
				showConfigurationDialog();
			}
			init();
			setMinimumSize(new Dimension(750, 580));

			displayHome();
		} else {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), "Moteur non initialiser", //$NON-NLS-1$ //$NON-NLS-2$
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

		frameCreator.formatTitle(ApplicationCore.NOM, ApplicationCore.VERSION);
		frameCreator.setLibelleAjResourcesReader(ApplicationCore.ajrLibelle);
		frameCreator.addActionListener(this);
		frameCreator.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApp();
			}
		});

		frameCreator.createFrame(new File(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ "/gui/ConcoursJeunes.xml")); //$NON-NLS-1$

		ajtHome.loadTemplate(ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + ajrParametreAppli.getResourceString("template.accueil.html")); //$NON-NLS-1$

		if(System.getProperty("noplugin") == null) { //$NON-NLS-1$
			fillOnDemandPlugin((JMenu) frameCreator.getNamedComponent("mi.import")); //$NON-NLS-1$
		}

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
		List<PluginMetadata> plugins = pl.getPlugins(Type.ON_DEMAND);

		if (plugins.size() > 0) {
			importMenu.setVisible(true);
		}

		for (PluginMetadata pm : plugins) {
			JMenuItem mi = new JMenuItem(pm.getOptionLabel());
			MenuBarTools.addItem(mi, getJMenuBar(), pm.getMenuPath());

			final Class<?> pluginClass = pm.getPluginClass();
			//mi.setActionCommand(pm.getClassName());
			mi.addActionListener(new ActionListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					try {
						Constructor<?> c = pluginClass.getConstructor(JFrame.class);
						Object plugin = c.newInstance(ConcoursJeunesFrame.this);
						for (Method m : pluginClass.getMethods()) {
							if (m.isAnnotationPresent(PluginEntry.class)) {
								m.invoke(plugin, (Object[]) null);
								break;
							}
						}
					} catch (InstantiationException e1) {
						JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					}  catch (SecurityException e1) {
						JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
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
							try {
								reglement.save();
							} catch(SQLException e1) {
								JXErrorPane.showDialog(null, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
										null, null, e1, Level.SEVERE, null));
								e1.printStackTrace();
							}
						}
					}
				});
				reglementMenu.add(jmiReglement);
			}
			reglementMenu.addSeparator();
		}
		JMenuItem jmiNewReglement = new JMenuItem(ApplicationCore.ajrLibelle.getResourceString("menubar.edition.reglement.new")); //$NON-NLS-1$
		jmiNewReglement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reglementName = JOptionPane.showInputDialog(ApplicationCore.ajrLibelle.getResourceString("reglement.general.addreglement")); //$NON-NLS-1$
				if (reglementName != null) {
					//Reglement reglement = new Reglement(reglementName);
					Reglement reglement = ReglementBuilder.createReglement();
					reglement.setName(reglementName);

					ReglementDialog reglementDialog = new ReglementDialog(ConcoursJeunesFrame.this, reglement);
					reglement = reglementDialog.showReglementDialog();
					if (reglement != null) {
						try {
							reglement.save();
						} catch(SQLException e1) {
							JXErrorPane.showDialog(null, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
									null, null, e1, Level.SEVERE, null));
							e1.printStackTrace();
						}

						fillReglementItem(jmReglements);
					}
				}
			}
		});
		reglementMenu.add(jmiNewReglement);
	}

	private void showConfigurationDialog() {	
		ConfigurationDialog configurationDialog = new ConfigurationDialog(this);
		Configuration configuration = configurationDialog.showConfigurationDialog(ApplicationCore.getConfiguration().clone());

		if (configuration != null) {
			//si le nom du profile à changer tous fermer
			if (!configuration.getCurProfil().equals(ApplicationCore.getConfiguration().getCurProfil()) && !configurationDialog.isRenamedProfile()) {
				try {
					concoursJeunes.closeAllFichesConcours();
				} catch (NullConfigurationException e) {
					JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
							null, null, e, Level.SEVERE, null));
					e.printStackTrace();
				} catch (IOException e) {
					JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
							null, null, e, Level.SEVERE, null));
					e.printStackTrace();
				}
			}

			ApplicationCore.setConfiguration(configuration);
			
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
			ajtHome.parse("lib_active_profile", ApplicationCore.ajrLibelle.getResourceString("home.activeprofile")); //$NON-NLS-1$ //$NON-NLS-2$
			ajtHome.parse("lib_gest_competion", ApplicationCore.ajrLibelle.getResourceString("home.gestcompet")); //$NON-NLS-1$ //$NON-NLS-2$
			ajtHome.parse("lib_new", ApplicationCore.ajrLibelle.getResourceString("home.new")); //$NON-NLS-1$ //$NON-NLS-2$
			ajtHome.parse("lib_delete", ApplicationCore.ajrLibelle.getResourceString("home.delete")); //$NON-NLS-1$ //$NON-NLS-2$
			ajtHome.parse("lib_info", ApplicationCore.ajrLibelle.getResourceString("home.info")); //$NON-NLS-1$ //$NON-NLS-2$
			ajtHome.parse("LOGO_CLUB_URI", ApplicationCore.getConfiguration().getLogoPath().replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			ajtHome.parse("INTITULE_CLUB", ApplicationCore.getConfiguration().getClub().getNom()); //$NON-NLS-1$
			ajtHome.parse("PROFILE_NAME", ApplicationCore.getConfiguration().getCurProfil()); //$NON-NLS-1$

			MetaDataFichesConcours metaDataFichesConcours = ApplicationCore.getConfiguration().getMetaDataFichesConcours();
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
		} catch (NullConfigurationException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		jif = null; 
	}

	private void closeApp() {
		try {
			concoursJeunes.saveAllFichesConcours();
			
			ApplicationCore.dbConnection.close();
		} catch (NullConfigurationException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * @return concoursJeunes
	 */
	public ApplicationCore getConcoursJeunes() {
		return concoursJeunes;
	}

	/**
	 * @param concoursJeunes
	 *            concoursJeunes à définir
	 */
	public void setConcoursJeunes(ApplicationCore concoursJeunes) {
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

		} else if (cmd.equals("menubar.impression.listeconcurrent.ordrealpha")) { //$NON-NLS-1$
			if (jif != null) {
				if(!jif.getFicheConcours().printArcherList(FicheConcours.ALPHA)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}
			}

			// imprime la liste des concurrents par ordre alphabetique avec
			// information greffe
		} else if (cmd.equals("menubar.impression.listeconcurrent.greffe")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printArcherList(FicheConcours.GREFFE)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}

			// imprime la liste des concurrents par ordre sur le pas de tir
		} else if (cmd.equals("menubar.impression.listeconcurrent.bytarget")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printArcherList(FicheConcours.TARGET)) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}
			
			// imprime les etiquettes concurrent
		} else if (cmd.equals("menubar.impression.listeconcurrent.etiquette")) { //$NON-NLS-1$

			if (jif != null)
				if(!jif.getFicheConcours().printEtiquettes()) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}

			// imprime la vu du pas de tir
		} else if (cmd.equals("menubar.impression.pasdetir")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printPasDeTir()) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}

			// imprime le classement individuel
		} else if (cmd.equals("menubar.impression.classement.individuel")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassement()) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}

			// imprime le classement par equipe
		} else if (cmd.equals("menubar.impression.classement.equipe")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassementEquipe()) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}

			// aimprime le classement par club
		} else if (cmd.equals("menubar.impression.classement.club")) { //$NON-NLS-1$
			if (jif != null)
				if(!jif.getFicheConcours().printClassementClub()) {
					JOptionPane.showMessageDialog(this, ApplicationCore.ajrLibelle.getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
				}
		} else if (cmd.equals("menubar.tools.disableplugins")) { //$NON-NLS-1$
			DisablePluginDialog disablePluginDialog = new DisablePluginDialog(this);
			disablePluginDialog.showDisablePluginDialog();
		} else if (cmd.equals("menubar.tools.installplugins")) { //$NON-NLS-1$
			InstallPluginDialog installPluginDialog = new InstallPluginDialog(this);
			installPluginDialog.showInstallPluginDialog();
		} else if (cmd.equals("menubar.tools.configuration")) { //$NON-NLS-1$
			showConfigurationDialog();
			// affiche la boite de dialogie "A propos"
		} else if (cmd.equals("menubar.aide.apropos")) { //$NON-NLS-1$
			AboutDialog aboutDialog = new AboutDialog(this);
			aboutDialog.showAboutDialog();
		} else if (cmd.equals("menubar.aide.aide")) { //$NON-NLS-1$
			// affiche le pdf avec le reader pdf standard du systeme
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(new File(ajrParametreAppli.getResourceString("path.documentation"))); //$NON-NLS-1$
				} else {
					String NAV = ApplicationCore.getConfiguration().getPdfReaderPath();

					Runtime.getRuntime().exec(NAV, new String[] { ajrParametreAppli.getResourceString("path.documentation") }); //$NON-NLS-1$ 
				}
			} catch (IOException e1) {
				JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			}
		} else if (cmd.equals("menubar.aide.versionnote")) { //$NON-NLS-1$
			ChangeLogDialog changeLogDialog = new ChangeLogDialog(this);
			changeLogDialog.showChangeLogDialog();
		} else if (cmd.equals("menubar.debug.addpoints")) { //$NON-NLS-1$
			if (jif != null) {
				org.concoursjeunes.debug.Debug.attributePoints(jif.getFicheConcours().getConcurrentList());
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

	/* (non-Javadoc)
	 * @see org.concoursjeunes.ConcoursJeunesListener#configurationChanged(org.concoursjeunes.ConcoursJeunesEvent)
	 */
	@Override
	public void configurationChanged(ConcoursJeunesEvent concoursJeunesEvent) {
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
					
					final MetaDataFicheConcours metaDataFicheConcours = ApplicationCore.getConfiguration().getMetaDataFichesConcours().get(Integer.parseInt(concref));
					if(!concoursJeunes.isOpenFicheConcours(metaDataFicheConcours)) {
						Thread launchFiche = new Thread() {
							@Override
							public void run() {
								
								try {
									concoursJeunes.restoreFicheConcours(metaDataFicheConcours);
								} catch (NumberFormatException e) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
											null, null, e, Level.SEVERE, null));
									e.printStackTrace();
								} catch (NullConfigurationException e) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
											null, null, e, Level.SEVERE, null));
									e.printStackTrace();
								} catch (IOException e) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
											null, null, e, Level.SEVERE, null));
									e.printStackTrace();
								} catch (NullPointerException e) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
											null, null, e, Level.SEVERE, null));
									e.printStackTrace();
								}
								ConcoursJeunesFrame.this.setCursor(Cursor.getDefaultCursor());
								//ConcoursJeunesFrame.this.jepHome.setCursor(Cursor.getDefaultCursor());
								ConcoursJeunesFrame.this.jepHome.setEnabled(true);
							}
						};
						this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						//this.jepHome.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						this.jepHome.setEnabled(false);
						Thread.yield();
						launchFiche.start();
					} else {
						JOptionPane.showMessageDialog(this, 
								ApplicationCore.ajrLibelle.getResourceString("home.warning.alreadyopen"), //$NON-NLS-1$
								ApplicationCore.ajrLibelle.getResourceString("home.warning.alreadyopen.title"), //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					}
				} else if (e.getURL().getHost().equals("delete_concours")) { //$NON-NLS-1$
					if (JOptionPane.showConfirmDialog(this, ApplicationCore.ajrLibelle.getResourceString("confirmation.suppression.concours"), //$NON-NLS-1$
							ApplicationCore.ajrLibelle.getResourceString("confirmation.suppression.concours.titre"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
						try {
							concoursJeunes.deleteFicheConcours(ApplicationCore.getConfiguration().getMetaDataFichesConcours().get(Integer.parseInt(e.getURL().getRef())));
						} catch (NumberFormatException e1) {
							JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
									null, null, e1, Level.SEVERE, null));
							e1.printStackTrace();
						} catch (NullConfigurationException e1) {
							JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
									null, null, e1, Level.SEVERE, null));
							e1.printStackTrace();
						}
						displayHome();
					}
				} else if (e.getURL().getHost().equals("new_concours")) { //$NON-NLS-1$
					final Parametre parametre = new Parametre(ApplicationCore.getConfiguration());
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					this.jepHome.setEnabled(false);
					Thread.yield();
					
					ParametreDialog parametreDialog = new ParametreDialog(this, null);
					parametreDialog.showParametreDialog(parametre);
					if(parametre.isReglementLock()) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								try {
									concoursJeunes.createFicheConcours(parametre);
								} catch (NullConfigurationException e1) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
											null, null, e1, Level.SEVERE, null));
									e1.printStackTrace();
								} catch (IOException e1) {
									JXErrorPane.showDialog(ConcoursJeunesFrame.this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
											null, null, e1, Level.SEVERE, null));
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
						JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						JXErrorPane.showDialog(this, new ErrorInfo(ApplicationCore.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
								null, null, e1, Level.SEVERE, null));
						e1.printStackTrace();
					}
				}
			}
		}
	}
}