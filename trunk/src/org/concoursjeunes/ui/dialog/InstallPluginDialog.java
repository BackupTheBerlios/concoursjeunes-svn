/*
 * Créer le 31 déc. 07 à 16:35:22 pour ConcoursJeunes
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;

import org.ajdeveloppement.commons.AjResourcesReader;
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
	
	private final JLabel jllCategorie = new JLabel();
	private final AJList<String> jlCategorie = new AJList<String>();
	
	private final JLabel jlPlugins = new JLabel();
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
	private TableRowSorter<DefaultTableModel> sorter;
	private final JTextPane jtpDescription = new JTextPane();
	
	private final JButton jbValider = new JButton();
	private final JButton jbAnnuler = new JButton();
	
	private List<PluginDescription> pluginsDetail;
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
		jtPlugins.setModel(createTableModel());
		jtPlugins.getSelectionModel().addListSelectionListener(this);
		
		jtPlugins.getColumnModel().getColumn(0).setMaxWidth(20);
		jtPlugins.getColumnModel().getColumn(1).setPreferredWidth(100);
		jtPlugins.getColumnModel().getColumn(2).setMaxWidth(70);
		jtPlugins.getColumnModel().getColumn(4).setPreferredWidth(200);
		jtPlugins.getColumnModel().removeColumn(jtPlugins.getColumnModel().getColumn(3));
		
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
		jpAction.add(jbValider);
		jpAction.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpPrincipal, BorderLayout.CENTER);
		getContentPane().add(jpAction, BorderLayout.SOUTH);
	}
	
	private void affectLibelle() {
		jllCategorie.setText(localisation.getResourceString("installplugindialog.category")); //$NON-NLS-1$
		jlPlugins.setText(localisation.getResourceString("installplugindialog.plugins")); //$NON-NLS-1$
		jlSearch.setText(localisation.getResourceString("installplugindialog.search")); //$NON-NLS-1$
		
		jbValider.setText(localisation.getResourceString("bouton.valider")); //$NON-NLS-1$
		jbAnnuler.setText(localisation.getResourceString("bouton.annuler")); //$NON-NLS-1$
	}
	
	private void completePanel() {
		DefaultTableModel dtm = createTableModel();
		
		if(ApplicationCore.getAppConfiguration().isUseProxy()) {
			ApplicationCore.getAppConfiguration().getProxy().activateProxyConfiguration();
		}
		
		boolean disable = false;
		try {
			URL webservicesImplURL = new URL(ApplicationCore.staticParameters.getResourceString("url.webservices") + "/ConcoursJeunes-webservices.jar"); //$NON-NLS-1$ //$NON-NLS-2$
			Class<?> clazz = Class.forName(
					"org.concoursjeunes.webservices.AvailablePluginsManagerImpl", //$NON-NLS-1$
					true,
					new URLClassLoader(new URL[] { webservicesImplURL  })); 
			
			Constructor<?> constr = clazz.getConstructor(Profile.class);
			AvailablePluginsManager apm = (AvailablePluginsManager)constr.newInstance(profile);
			pluginsDetail = apm.getPluginsDetail();
			
			for(PluginDescription pluginDescription : pluginsDetail) {
				Object[] row = new Object[] {
						new Boolean(false),
						pluginDescription.getDisplayName(),
						pluginDescription.getVersion(),
						apm.getCategories().get(pluginDescription.getCategory()),
						pluginDescription.getShortDescription()
				};
				
				dtm.addRow(row);
			}
			
			sorter = new TableRowSorter<DefaultTableModel>(dtm);
			jtPlugins.setModel(dtm);
			jtPlugins.getColumnModel().getColumn(0).setMaxWidth(20);
			jtPlugins.getColumnModel().getColumn(1).setPreferredWidth(100);
			jtPlugins.getColumnModel().getColumn(2).setMaxWidth(70);
			jtPlugins.getColumnModel().getColumn(3).setMaxWidth(0);
			jtPlugins.getColumnModel().getColumn(4).setPreferredWidth(200);
			jtPlugins.getColumnModel().removeColumn(jtPlugins.getColumnModel().getColumn(3));
			
			jlCategorie.add(localisation.getResourceString("installplugindialog.category.all")); //$NON-NLS-1$
			for(String category : apm.getCategories().values()) {
				jlCategorie.add(category);
			}
			jlCategorie.setSelectedIndex(0);
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
	 * 
	 */
	private DefaultTableModel createTableModel() {
		DefaultTableModel dtm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				if(col == 0)
					return true;
				return false;
			}
		};
		
		dtm.addColumn(""); //$NON-NLS-1$
		dtm.addColumn(localisation.getResourceString("installplugindialog.plugins.name")); //$NON-NLS-1$
		dtm.addColumn(localisation.getResourceString("installplugindialog.plugins.version")); //$NON-NLS-1$
		dtm.addColumn("category_hide"); //$NON-NLS-1$
		dtm.addColumn(localisation.getResourceString("installplugindialog.plugins.description")); //$NON-NLS-1$
		
		return dtm;
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
			DefaultTableModel dtm = (DefaultTableModel)jtPlugins.getModel();
			List<Repository> pluginsRepos = new ArrayList<Repository>();
			for(int i = 0; i < dtm.getRowCount(); i++) {
				if((Boolean)dtm.getValueAt(i, 0) == true) {
					pluginsRepos.add(new Repository(
							pluginsDetail.get(i).getLogicalName(),
							new String[] { pluginsDetail.get(i).getReposURL()},
							"0.00.00")); //$NON-NLS-1$ //la version est toujours à 0 car l'extention n'est pas installé
				}
			}
			
			if(pluginsRepos.size() > 0) {
				ajUpdater = new AjUpdater(ApplicationCore.userRessources.getAllusersDataPath() + File.separator + "update", //$NON-NLS-1$
						"."); //$NON-NLS-1$
				ajUpdater.addAjUpdaterListener(this);
				
				for(Repository repos : pluginsRepos) {
					ajUpdater.addRepository(repos);
				}
				try {
					GlassPanePanel panel = new GlassPanePanel();
					panel.setMessage(localisation.getResourceString("installplugindialog.loading")); //$NON-NLS-1$
					setGlassPane(panel);
					panel.setVisible(true);
					ajUpdater.checkUpdate();
				} catch (UpdateException e1) {
					System.out.println("Mise à jour impossible"); //$NON-NLS-1$
					e1.printStackTrace();
				}
			}
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			setVisible(false);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		if(jtPlugins.getModel().getRowCount() > 0) {
			List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<RowFilter<DefaultTableModel, Integer>>();
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("(?i)" + jtfSearch.getText())); //$NON-NLS-1$
			if(jlCategorie.getSelectedIndex() > 0)
				filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter((String)jlCategorie.getSelectedValue()));
			
			sorter.setRowFilter(RowFilter.<DefaultTableModel, Integer>andFilter((Iterable<RowFilter<DefaultTableModel, Integer>>)filters));
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
				jtpDescription.setText(pluginsDetail.get(((DefaultListSelectionModel)e.getSource()).getMinSelectionIndex()).getLongDescription());
		} else if(e.getSource() == jlCategorie) {
			List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<RowFilter<DefaultTableModel, Integer>>();
			filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter("(?i)" + jtfSearch.getText())); //$NON-NLS-1$
			if(jlCategorie.getSelectedIndex() > 0)
				filters.add(RowFilter.<DefaultTableModel, Integer>regexFilter((String)jlCategorie.getSelectedValue()));
			sorter.setRowFilter(RowFilter.<DefaultTableModel, Integer>andFilter((Iterable<RowFilter<DefaultTableModel, Integer>>)filters));
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
			/*case CONNECTED:
				break;*/
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
						} catch (XMLStreamException e) {
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
							//qui s'occupe d'élever les priviléges utilisateur si nécessaire.				
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
}
