/*
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
package org.concoursjeunes.plugins.update;

import static org.concoursjeunes.ApplicationCore.ajrParametreAppli;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.concoursjeunes.plugins.Plugin;
import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.concoursjeunes.plugins.Plugin.Type;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.util.OS;

import ajinteractive.macosx.auth.PrivilegedRuntime;
import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.common.AjResourcesReader;
import ajinteractive.standard.utilities.app.AppSerializer;
import ajinteractive.standard.utilities.net.SimpleAuthenticator;
import ajinteractive.standard.utilities.updater.*;

@Plugin(type = Plugin.Type.STARTUP)
public class ConcoursJeunesUpdate extends Thread implements AjUpdaterListener, MouseListener {
	// private String baseURL;
	private SystemTray tray;
	private TrayIcon trayIcon;
	private AjUpdater ajUpdater;
	private Hashtable<String, ArrayList<FileMetaData>> updateFiles = new Hashtable<String, ArrayList<FileMetaData>>();
	private final AjResourcesReader pluginRessources = new AjResourcesReader("properties.ConcoursJeunesUpdate"); //$NON-NLS-1$
	private final AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.update.ConcoursJeunesUpdate_libelle", ConcoursJeunesUpdate.class.getClassLoader()); //$NON-NLS-1$
	private enum Status {
		NONE,
		AVAILABLE,
		DOWNLODED
	}
	private Status currentStatus = Status.NONE;

	public ConcoursJeunesUpdate() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@PluginEntry
	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public void run() {
		boolean proxyAuthRequired = false;
		PluginLoader pl = new PluginLoader();
		

		ajUpdater = new AjUpdater(ApplicationCore.userRessources.getUpdatePath().getPath(),
				"."); //$NON-NLS-1$
		ajUpdater.addAjUpdaterListener(this);
		
		try {
			AppSerializer appSerializer = new AppSerializer(ApplicationCore.userRessources);
			ajUpdater.setUserAgent(ApplicationCore.NOM + " " + ApplicationCore.VERSION //$NON-NLS-1$
					+ " (" + appSerializer.getSerial() + ";" + ApplicationCore.getConfiguration().getClub().getAgrement() //$NON-NLS-1$ //$NON-NLS-2$ 
					+ " " + ApplicationCore.getConfiguration().getClub().getNom() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		ajUpdater.addRepositoryURL(pluginRessources.getResourceString("url.reference")); //$NON-NLS-1$
		for (PluginMetadata pm : pl.getPlugins(Type.ALL)) {
			ajUpdater.addRepositoryURL(pm.getReposURL());
		}
		if (ApplicationCore.getConfiguration().isUseProxy()) {
			System.setProperty("http.proxyHost", ApplicationCore.getConfiguration().getProxy().getProxyServerAddress()); //$NON-NLS-1$
			System.setProperty("http.proxyPort",Integer.toString(ApplicationCore.getConfiguration().getProxy().getProxyServerPort())); //$NON-NLS-1$
			if(ApplicationCore.getConfiguration().getProxy().isUseProxyAuthentification()) {
				Authenticator.setDefault(new SimpleAuthenticator(
						ApplicationCore.getConfiguration().getProxy().getProxyAuthLogin(),
						ApplicationCore.getConfiguration().getProxy().getProxyAuthPassword()));
			}
		}
		
		boolean loop;
		do {
			loop = false;
			proxyAuthRequired = false;
			try {
				updateFiles = ajUpdater.checkUpdate();
			} catch (UpdateException e) {
				if(e.getMessage().equals("Proxy Authentification required")) { //$NON-NLS-1$
					proxyAuthRequired = true;
				} else {
					System.out.println("update enable"); //$NON-NLS-1$
					e.printStackTrace();
					
					if (trayIcon != null) {
						tray.remove(trayIcon);
					}
				}
			}
			if(proxyAuthRequired) {
				LoginService loginService = new LoginService() {
					@Override
					public boolean authenticate(String user , char[] password, String server) {
						Authenticator.setDefault(new SimpleAuthenticator(
								user,
								new String(password)));
						return true;
					}
				};
				JXLoginPane loginPanel = new JXLoginPane(loginService);
				loginPanel.setBannerText(pluginLocalisation.getResourceString("update.proxy.auth.title")); //$NON-NLS-1$
				loginPanel.setMessage(pluginLocalisation.getResourceString("update.proxy.auth.message")); //$NON-NLS-1$
				//loginPanel.setP
				org.jdesktop.swingx.JXLoginPane.Status status = JXLoginPane.showLoginDialog(null, loginPanel);
				if(status == JXLoginPane.Status.SUCCEEDED)
					loop = true;
			}
		} while(loop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ajinteractive.standard.utilities.updater.AjUpdaterListener#updaterStatusChanged(ajinteractive.standard.utilities.updater.AjUpdaterEvent)
	 */
	@Override
	public void updaterStatusChanged(AjUpdaterEvent event) {
		switch (event.getStatus()) {
		case CONNECTED:
			if (SystemTray.isSupported() && tray == null) {
				tray = SystemTray.getSystemTray();
				// load an image
				Dimension dimension = tray.getTrayIconSize();
				Image image = Toolkit.getDefaultToolkit().getImage(
						ajrParametreAppli.getResourceString("path.ressources") + File.separator + ajrParametreAppli.getResourceString("file.icon.application")).getScaledInstance(dimension.width, //$NON-NLS-1$ //$NON-NLS-2$
						dimension.height, Image.SCALE_SMOOTH);

				// create a popup menu
				trayIcon = new TrayIcon(image, pluginLocalisation.getResourceString("tray.name")); //$NON-NLS-1$
				trayIcon.addMouseListener(this);

				try {
					tray.add(trayIcon);
				} catch (AWTException e) {
					System.err.println(e);
				}
			}
			break;
		case UPDATE_AVAILABLE:

			String strSize = AJToolKit.formatFileSize(ajUpdater.getDownloadSize());

			updateFiles = event.getUpdateFiles();

			if (trayIcon != null) {
				trayIcon.displayMessage(pluginLocalisation.getResourceString("update.available.title"), pluginLocalisation.getResourceString("update.available", strSize), TrayIcon.MessageType.INFO); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				AjUpdaterFrame ajUpdaterFrame = new AjUpdaterFrame(ajUpdater, ApplicationCore.VERSION);
				
				if(ajUpdaterFrame.showAjUpdaterFrame() == AjUpdaterFrame.ReturnCode.OK) {
					ajUpdater.downloadFiles(updateFiles);
				}
			}
			currentStatus = Status.AVAILABLE;
			break;
		case CONNECTION_INTERRUPTED:
			if (trayIcon != null) {
				trayIcon.displayMessage(pluginLocalisation.getResourceString("update.interrupt.title"), pluginLocalisation.getResourceString("update.interrupt"), TrayIcon.MessageType.ERROR); //$NON-NLS-1$ //$NON-NLS-2$
			}
			break;
		case FILE_ERROR:
			if (trayIcon != null) {
				trayIcon.displayMessage(pluginLocalisation.getResourceString("update.downloaderror.title"), pluginLocalisation.getResourceString("update.downloaderror"), //$NON-NLS-1$ //$NON-NLS-2$
						TrayIcon.MessageType.ERROR);
			}
			break;
		case FILES_DOWNLOADED:
			if (JOptionPane.showConfirmDialog(null, pluginLocalisation.getResourceString("update.confirminstall"), pluginLocalisation.getResourceString("update.confirminstall.title"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				try {
					Process process = null;
					String[] command = new String[] { "concoursjeunes-applyupdate", //$NON-NLS-1$
							ApplicationCore.userRessources.getAllusersDataPath() + File.separator + "update", //$NON-NLS-1$
							System.getProperty("user.dir") }; //$NON-NLS-1$
					if(!OS.isMacOSX()) { 
						//sur les systèmes Windows et Linux, invoque le programme "concoursjeunes-applyupdate"
						//qui s'occupe d'élever les priviléges utilisateur si nécessaire.
						process = Runtime.getRuntime().exec(command); 
					} else {
						//Sous Mac OS X, l'elevation de privilege est effectué en java
						//à l'aide d'une librairie jni
						process = PrivilegedRuntime.getRuntime().exec(command);
					}
					if(process != null)
						process.waitFor();
					
					try {
						ApplicationCore.getInstance().saveAllFichesConcours();
						
						ApplicationCore.dbConnection.close();
					} catch (NullConfigurationException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}

					System.exit(3);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NO_UPDATE_AVAILABLE:
			if (trayIcon != null) {
				tray.remove(trayIcon);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == trayIcon) {
			if (currentStatus == Status.AVAILABLE) {
				AjUpdaterFrame ajUpdaterFrame = new AjUpdaterFrame(ajUpdater, ApplicationCore.VERSION);
				
				if(ajUpdaterFrame.showAjUpdaterFrame() == AjUpdaterFrame.ReturnCode.OK) {
					ajUpdater.downloadFiles(updateFiles);
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
