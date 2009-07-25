/*
 * Créé le 31 déc. 07 à 16:35:22 pour ConcoursJeunes
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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
package org.concoursjeunes.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.security.JarVerifier;
import org.ajdeveloppement.commons.ui.AJList;
import org.ajdeveloppement.macosx.PrivilegedRuntime;
import org.ajdeveloppement.updater.AjUpdater;
import org.ajdeveloppement.updater.AjUpdaterEvent;
import org.ajdeveloppement.updater.AjUpdaterListener;
import org.ajdeveloppement.updater.Repository;
import org.ajdeveloppement.updater.UpdateException;
import org.ajdeveloppement.updater.ui.AjUpdaterFrame;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Profile;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.concoursjeunes.plugins.AvailablePluginsManager;
import org.concoursjeunes.plugins.PluginDescription;
import org.concoursjeunes.ui.GlassPanePanel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.util.OS;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class InstallPluginDialog extends JDialog implements ActionListener, CaretListener, ListSelectionListener, AjUpdaterListener {
	
	private Profile profile;
	private AjResourcesReader localisation;
	
	@Localisable("installplugindialog.category")
	private final JLabel jllCategorie = new JLabel();
	private final AJList<String> jlCategorie = new AJList<String>();
	
	@Localisable("installplugindialog.plugins")
	private final JLabel jlPlugins = new JLabel();
	@Localisable("installplugindialog.search")
	private final JLabel jlSearch = new JLabel();
	private final JTextField jtfSearch = new JTextField();
	private final JTable jtPlugins = new JTable() {
	//  Returning the Class of each column will allow different
		//  renderers to be used based on Class
		@Override
        public Class<?> getColumnClass(int column)	{
			return getValueAt(0, column).getClass();
		}
	};
	private PluginDescriptionTableModel pdtm = new PluginDescriptionTableModel();
	private TableRowSorter<PluginDescriptionTableModel> sorter = new TableRowSorter<PluginDescriptionTableModel>(pdtm);
	private final JTextPane jtpDescription = new JTextPane();
	
	@Localisable("installplugindialog.addurl")
	private JButton jbAddURL = new JButton();
	@Localisable("bouton.valider")
	private final JButton jbValider = new JButton();
	@Localisable("bouton.annuler")
	private final JButton jbAnnuler = new JButton();
	
	private AjUpdater ajUpdater;
	
	public InstallPluginDialog(JFrame parentframe, Profile profile) {
		super(parentframe, true);
		
		this.profile = profile;
		this.localisation = profile.getLocalisation();
		
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpPrincipal = new JPanel();
		JPanel jpAction = new JPanel();
		JPanel jpCategorie = new JPanel();
		JPanel jpSearch = new JPanel();
		JPanel jpSelection = new JPanel();
		JPanel jpPlugins = new JPanel();
		
		/*GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();*/
		
		jbAddURL.addActionListener(this);
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		
		JScrollPane jspTablePlugins = new JScrollPane(jtPlugins);
		jspTablePlugins.setPreferredSize(new Dimension(450,200));
		jtpDescription.setPreferredSize(new Dimension(450,200));
		jtpDescription.setEditable(false);
		
		jlCategorie.addListSelectionListener(this);
		jlCategorie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlCategorie.setPreferredSize(new Dimension(200, 400));
		jtfSearch.addCaretListener(this);
		jtPlugins.setModel(pdtm);
		jtPlugins.setRowSorter(sorter);
		jtPlugins.getSelectionModel().addListSelectionListener(this);
		
		jtPlugins.getColumnModel().getColumn(0).setMaxWidth(20);
		jtPlugins.getColumnModel().getColumn(1).setPreferredWidth(100);
		jtPlugins.getColumnModel().getColumn(2).setMaxWidth(70);
		jtPlugins.getColumnModel().getColumn(3).setPreferredWidth(200);
		jtPlugins.getColumnModel().removeColumn(jtPlugins.getColumnModel().getColumn(4));
		
		/*c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;*/
		jpCategorie.setLayout(new BorderLayout());
		jpCategorie.add(jllCategorie, BorderLayout.NORTH);
		jpCategorie.add(new JScrollPane(jlCategorie), BorderLayout.CENTER);
		
		jpSearch.setLayout(new BorderLayout());
		jpSearch.add(jlSearch, BorderLayout.WEST);
		jpSearch.add(jtfSearch, BorderLayout.CENTER);
		
		jpSelection.setLayout(new BorderLayout());
		jpSelection.add(jpSearch, BorderLayout.NORTH);
		jpSelection.add(jspTablePlugins, BorderLayout.CENTER);
		
		jpPlugins.setLayout(new BorderLayout());
		jpPlugins.add(jlPlugins, BorderLayout.NORTH);
		jpPlugins.add(jpSelection, BorderLayout.CENTER);
		jpPlugins.add(new JScrollPane(jtpDescription), BorderLayout.SOUTH);
		
		jpPrincipal.setLayout(new BorderLayout());
		jpPrincipal.add(jpCategorie, BorderLayout.WEST);
		jpPrincipal.add(jpPlugins, BorderLayout.CENTER);
		
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbAddURL);
		jpAction.add(Box.createHorizontalStrut(30));
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpPrincipal, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		Localisator.localize(this, localisation);
	}
	
	private void completePanel() {
		if(ApplicationCore.getAppConfiguration().isUseProxy()) {
			ApplicationCore.getAppConfiguration().getProxy().activateProxyConfiguration();
		}
		
		boolean disable = false;
		File tmJar = null;
		try {
			tmJar = File.createTempFile("csws", ".jar"); //$NON-NLS-1$ //$NON-NLS-2$
			URL webservicesImplURL = new URL(ApplicationCore.staticParameters.getResourceString("url.webservices") + "/ConcoursJeunes-webservices.jar"); //$NON-NLS-1$ //$NON-NLS-2$
			//charge l'implémentation dans un fichier temporaire
			FileUtils.dumpStreamToFile(webservicesImplURL.openStream(), tmJar);
			
			//On vérifie la signature de l'archive
			JarVerifier verifier = new JarVerifier(ApplicationCore.userRessources.getAppKeyStore());
			verifier.verifyJar(tmJar);
			
			//on s'assure que l'archive a bien été signé
			if(verifier.isSignedJar()) {
				Class<?> clazz = Class.forName(
						"org.concoursjeunes.webservices.AvailablePluginsManagerImpl", //$NON-NLS-1$
						true,
						new URLClassLoader(new URL[] { tmJar.toURI().toURL()  }));
				
				Constructor<?> constr = clazz.getConstructor(Profile.class);
				AvailablePluginsManager apm = (AvailablePluginsManager)constr.newInstance(profile);
				List<PluginDescription> pluginsDetail = apm.getPluginsDetail();
				
				pdtm.clearPluginDescriptions();
				for(PluginDescription pluginDescription : pluginsDetail) {
					pdtm.addPluginDescription(pluginDescription);
				}
				
				jlCategorie.add(localisation.getResourceString("installplugindialog.category.all")); //$NON-NLS-1$
				jlCategorie.add(localisation.getResourceString("installplugindialog.category.manual")); //$NON-NLS-1$
				for(String category : apm.getCategories().values()) {
					jlCategorie.add(category);
				}
				jlCategorie.setSelectedIndex(0);
			} else
				disable = true;
		} catch(WebServiceException e1) {
			e1.printStackTrace();
			disable = true;
		} catch(ClassNotFoundException e1) {
			e1.printStackTrace();
			disable = true;
		} catch(IllegalAccessException e1) {
			e1.printStackTrace();
			disable = true;
		} catch(InstantiationException e1) {
			e1.printStackTrace();
			disable = true;
		} catch(MalformedURLException e1) {
			e1.printStackTrace();
			disable = true;
		} catch (SecurityException e) {
			e.printStackTrace();
			disable = true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			disable = true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			disable = true;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			disable = true;
		} catch (IOException e) {
			e.printStackTrace();
			disable = true;
		} catch (CertificateException e) {
			e.printStackTrace();
			disable = true;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			disable = true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			disable = true;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			disable = true;
		} catch (CertPathValidatorException e) {
			e.printStackTrace();
			disable = true;
		} finally {
			if(tmJar != null)
				tmJar.delete();
		}
		
		if(disable) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GlassPanePanel panel = new GlassPanePanel();
					
					panel.setMessage(localisation.getResourceString("installplugindialog.temporary.disable")); //$NON-NLS-1$
					setGlassPane(panel);
					panel.setVisible(true);
				}
			});
		}
	}
	
	/**
	 * Affiche la boite de dialogue d'intallation de plugin
	 */
	public void showInstallPluginDialog() {
		final GlassPanePanel panel = new GlassPanePanel();
		panel.setMessage(localisation.getResourceString("installplugindialog.loading")); //$NON-NLS-1$
		setGlassPane(panel);
		panel.setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				completePanel();
				panel.setVisible(false);
			}
		});
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbValider) {
			List<Repository> pluginsRepos = new ArrayList<Repository>();
			for(int i = 0; i < pdtm.getRowCount(); i++) {
				if(pdtm.isSelected(i)) {
					pluginsRepos.add(new Repository(
							pdtm.getPluginDescriptionAt(i).getLogicalName(),
							new String[] { pdtm.getPluginDescriptionAt(i).getReposURL()},
							"0.00.00")); //$NON-NLS-1$ //la version est toujours à 0 car l'extention n'est pas installé
				}
			}
			
			if(pluginsRepos.size() > 0) {
				ajUpdater = new AjUpdater(ApplicationCore.userRessources.getAllusersDataPath() + File.separator + "update", //$NON-NLS-1$
						"."); //$NON-NLS-1$
				ajUpdater.addAjUpdaterListener(this);
				ajUpdater.setAppKeyStore(ApplicationCore.userRessources.getAppKeyStore());
				ajUpdater.setSecretKeyPassword(AppUtilities.getAppUID(ApplicationCore.userRessources).toCharArray());
				
				for(Repository repos : pluginsRepos) {
					ajUpdater.addRepository(repos);
				}
				try {
					GlassPanePanel panel = new GlassPanePanel();
					panel.setMessage(localisation.getResourceString("installplugindialog.loading")); //$NON-NLS-1$
					setGlassPane(panel);
					panel.setVisible(true);
					ajUpdater.checkUpdate();
					
					for(Repository repos : ajUpdater.getRepositories()) {
						if(repos.getReposStatus() != Repository.Status.REVISION_NOSIGN_FOUND
								&& repos.getReposStatus() != Repository.Status.REVISION_SIGN_VERIFY_WITH_EXPIRED_CERT
								&& repos.getReposStatus() != Repository.Status.REVISION_SIGN_VERIFY_WITH_SELFSIGNED_CERT
								&& repos.getReposStatus() != Repository.Status.REVISION_SIGN_VERIFY_WITH_TRUSTED_CERT) {
							JOptionPane.showMessageDialog(this, localisation.getResourceString("installplugindialog.installerror", repos.getReposName()), "", //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (UpdateException e1) {
					JXErrorPane.showDialog(this,
							new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
									e1.toString(),
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				}
			}
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		} else if(e.getSource() == jbAddURL) {
			String prompturl = JOptionPane.showInputDialog(this, 
					localisation.getResourceString("installplugindialog.promptaddurl"), "http://"); //$NON-NLS-1$ //$NON-NLS-2$
			if(prompturl == null)
				return;
			
			try {
				if(!prompturl.endsWith("/")) //$NON-NLS-1$
					prompturl += "/"; //$NON-NLS-1$
				URL url = new URL(prompturl);
				
				PluginDescription pd = XMLSerializer.loadMarshallStructure(new URL(url, "plugindescription.xml"), PluginDescription.class); //$NON-NLS-1$
				pdtm.addPluginDescription(pd);
				pdtm.setSelected(pdtm.getRowCount()-1, true);
			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(this, 
						localisation.getResourceString("installplugindialog.invalidurl"), //$NON-NLS-1$
						localisation.getResourceString("installplugindialog.invalidurl.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			} catch (JAXBException e1) {
				JXErrorPane.showDialog(this,
						new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
								e1.toString(),
						null, null, e1, Level.WARNING, null));
				e1.printStackTrace();
			} catch (IOException e1) {
				JXErrorPane.showDialog(this,
						new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
								e1.toString(),
						null, null, e1, Level.WARNING, null));
				e1.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		if(jtPlugins.getModel().getRowCount() > 0) {
			List<RowFilter<PluginDescriptionTableModel, Integer>> filters = new ArrayList<RowFilter<PluginDescriptionTableModel, Integer>>();
			filters.add(RowFilter.<PluginDescriptionTableModel, Integer>regexFilter("(?i)" + jtfSearch.getText())); //$NON-NLS-1$
			if(jlCategorie.getSelectedIndex() > 0)
				filters.add(RowFilter.<PluginDescriptionTableModel, Integer>regexFilter((String)jlCategorie.getSelectedValue()));
			
			sorter.setRowFilter(RowFilter.<PluginDescriptionTableModel, Integer>andFilter((Iterable<RowFilter<PluginDescriptionTableModel, Integer>>)filters));
			jtPlugins.setRowSorter(sorter);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() instanceof DefaultListSelectionModel) {
			if(((DefaultListSelectionModel)e.getSource()).getMinSelectionIndex() > -1)
				jtpDescription.setText(pdtm.getPluginDescriptionAt(((DefaultListSelectionModel)e.getSource()).getMinSelectionIndex()).getLongDescription());
		} else if(e.getSource() == jlCategorie) {
			List<RowFilter<PluginDescriptionTableModel, Integer>> filters = new ArrayList<RowFilter<PluginDescriptionTableModel, Integer>>();
			filters.add(RowFilter.<PluginDescriptionTableModel, Integer>regexFilter("(?i)" + jtfSearch.getText())); //$NON-NLS-1$
			if(jlCategorie.getSelectedIndex() > 0)
				filters.add(RowFilter.<PluginDescriptionTableModel, Integer>regexFilter((String)jlCategorie.getSelectedValue()));
			sorter.setRowFilter(RowFilter.<PluginDescriptionTableModel, Integer>andFilter((Iterable<RowFilter<PluginDescriptionTableModel, Integer>>)filters));
			jtPlugins.setRowSorter(sorter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ajinteractive.standard.utilities.updater.AjUpdaterListener#updaterStatusChanged(ajinteractive.standard.utilities.updater.AjUpdaterEvent)
	 */
	@Override
	public void updaterStatusChanged(AjUpdaterEvent event) {
		GlassPanePanel panel;
		switch (event.getStatus()) {
			case UPDATE_AVAILABLE:
				AjUpdaterFrame ajUpdaterFrame = new AjUpdaterFrame(ajUpdater);
				
				if(ajUpdaterFrame.showAjUpdaterFrame() == AjUpdaterFrame.ReturnCode.OK) {
					ajUpdater.downloadFiles(event.getUpdateFiles());
				}
	
				break;
			case CONNECTION_INTERRUPTED:
				panel = new GlassPanePanel();
				panel.setMessage(localisation.getResourceString("installplugindialog.temporary.disable")); //$NON-NLS-1$
				setGlassPane(panel);
				panel.setVisible(true);
				break;
			case FILE_ERROR:
				panel = new GlassPanePanel();
				panel.setMessage(localisation.getResourceString("installplugindialog.temporary.disable")); //$NON-NLS-1$
				setGlassPane(panel);
				panel.setVisible(true);
				break;
			case FILES_DOWNLOADED:
				if (JOptionPane.showConfirmDialog(null, localisation.getResourceString("update.confirminstall"), localisation.getResourceString("update.confirminstall.title"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						try {
							for(Profile profile : ApplicationCore.getInstance().getProfiles())
								profile.saveAllFichesConcours();
							
							ApplicationCore.dbConnection.close();
						} catch (NullConfigurationException e) {
							e.printStackTrace();
							JXErrorPane.showDialog(null, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
									e.toString(), null, null, e, Level.SEVERE, null));
						} catch (IOException e) {
							e.printStackTrace();
							JXErrorPane.showDialog(null, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
									e.toString(), null, null, e, Level.SEVERE, null));
						} catch (JAXBException e) {
							e.printStackTrace();
							JXErrorPane.showDialog(null, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
									e.toString(), null, null, e, Level.SEVERE, null));
						} catch (SQLException e) {
							e.printStackTrace();
							JXErrorPane.showDialog(null, new ErrorInfo(localisation.getResourceString("erreur"), //$NON-NLS-1$
									e.toString(), null, null, e, Level.SEVERE, null));
						}
						
						Process process = null;
						String[] command = new String[] { "concoursjeunes-applyupdate", //$NON-NLS-1$
								ApplicationCore.userRessources.getUpdatePath().getPath(),
								System.getProperty("user.dir") }; //$NON-NLS-1$
						if(OS.isMacOSX()) {
							//Sous Mac OS X, l'elevation de privilege est effectué en java
							//à l'aide d'une librairie jni
							process = PrivilegedRuntime.getRuntime().exec(command);
							
						} else {
							//sur les systèmes Windows et Linux, invoque le programme "concoursjeunes-applyupdate"
							//qui s'occupe d'élever les privilèges utilisateur si nécessaire.				
							process = Runtime.getRuntime().exec(command); 
						}
						if(process != null)
							process.waitFor();
	
						System.exit(3);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				break;
			case NO_UPDATE_AVAILABLE:
		}
	}
	
	private class PluginDescriptionTableModel extends AbstractTableModel {
		
		private List<PluginDescription> descriptions = new ArrayList<PluginDescription>();
		private Map<Integer,  Boolean> selected = new HashMap<Integer, Boolean>();
		
		public void addPluginDescription(PluginDescription desc) {
			descriptions.add(desc);
			
			fireTableRowsInserted(descriptions.size()-1, descriptions.size()-1);
		}
		
		/*public void removePluginDescription(PluginDescription desc) {
			int index = descriptions.indexOf(desc);
			if(index != -1) {
				descriptions.remove(desc);
				
				fireTableRowsDeleted(index, index);
			}
		}*/
		
		public PluginDescription getPluginDescriptionAt(int row) {
			return descriptions.get(row);
		}
		
		public boolean isSelected(int row) {
			return selected.get(row) != null ? selected.get(row) : false;
		}
		
		public void setSelected(int row, boolean selected) {
			this.selected.put(row, selected);
			
			fireTableRowsUpdated(row, row);
		}
		
		public void clearPluginDescriptions() {
			descriptions.clear();
			
			fireTableDataChanged();
		}
		
		
		@Override
		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return true;
			return false;
		}

		@Override
		public int getColumnCount() {
			return 5;
		}
		
		@SuppressWarnings("nls")
		@Override
		public String getColumnName(int column) {
			switch (column) {
				case 1:
					return localisation.getResourceString("installplugindialog.plugins.name");
				case 2:
					return localisation.getResourceString("installplugindialog.plugins.version");
				case 3:
					return localisation.getResourceString("installplugindialog.plugins.description");
			}
			return "";
		}

		@Override
		public int getRowCount() {
			return descriptions.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			PluginDescription currentPluginDescription = descriptions.get(rowIndex);
			switch(columnIndex) {
				case 0:
					return selected.get(rowIndex) != null ? selected.get(rowIndex) : false;
				case 1:
					return currentPluginDescription.getDisplayName();
				case 2:
					return currentPluginDescription.getVersion();
				case 3:
					return currentPluginDescription.getShortDescription();
				case 4:
					return currentPluginDescription.getCategory();
			}
			return null;
		}
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			selected.put(rowIndex, (Boolean)value);
			
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}
}
