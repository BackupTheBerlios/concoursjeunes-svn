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
package org.concoursjeunes;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.XMLSerializer;
import org.ajdeveloppement.commons.security.SecureSiteAuthenticationStore;
import org.ajdeveloppement.commons.ui.SwingURLAuthenticator;
import org.concoursjeunes.exceptions.ExceptionHandlingEventQueue;
import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.concoursjeunes.plugins.Plugin.Type;
import org.concoursjeunes.ui.ConcoursJeunesFrame;
import org.h2.tools.DeleteDbFiles;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Class initial de l'application.
 * 
 * @author Aurélien JEOFFRAY
 * @version 2.0
 * 
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationCore core;
		
		System.setProperty("java.net.useSystemProxies","true"); //$NON-NLS-1$ //$NON-NLS-2$ 
		System.setProperty("java.net.preferIPv6Addresses", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		
		AjResourcesReader localisation = new AjResourcesReader("libelle");  //$NON-NLS-1$
		
		Thread.UncaughtExceptionHandler handlerException = new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, final Throwable e) {
				EventQueue.invokeLater(new Runnable() {
			         public void run() {
						JXErrorPane.showDialog(null, new ErrorInfo("Application uncaught Exception!", //$NON-NLS-1$
								e.toString(),
								null, null, e, Level.SEVERE, null));
						e.printStackTrace();
			         }
				});
			}
		};
		
		Thread.setDefaultUncaughtExceptionHandler(handlerException);
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ExceptionHandlingEventQueue());
		
		final ProxySelector systemProxySelector = ProxySelector.getDefault();
		ProxySelector.setDefault(new ProxySelector() {

			@Override
			public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
				systemProxySelector.connectFailed(uri, sa, ioe);
			}

			@Override
			public List<Proxy> select(URI uri) {
				try {
					InetAddress address = InetAddress.getByName(uri.getHost());
					if(address.isLoopbackAddress() || address.isSiteLocalAddress())
						return Collections.singletonList(Proxy.NO_PROXY);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				return systemProxySelector.select(uri);
			}
			
		});
	
		boolean retry = false;
		do {
			try {
				ApplicationCore.initializeApplication();
			} catch (SQLException e1) {
				JXErrorPane.showDialog(null,new ErrorInfo( "SQL Error", e1.toString(), //$NON-NLS-1$
						null, null, e1, Level.SEVERE, null));
				
				if(JOptionPane.showConfirmDialog(null, localisation.getResourceString("erreur.breakdb")) == JOptionPane.YES_OPTION) {  //$NON-NLS-1$
					retry = true;
					try {
						if(ApplicationCore.dbConnection != null) ApplicationCore.dbConnection.close();
						DeleteDbFiles.execute(ApplicationCore.userRessources.getBasePath().getPath(), null, true);
					} catch (SQLException e2) {	e2.printStackTrace(); }
				} else {
					System.exit(1);
				}
			}
		} while(retry);
		
		core = ApplicationCore.getInstance();
		
		initSecureContext();
		
		if(System.getProperty("noplugin") == null) { //$NON-NLS-1$
			loadStartupPlugin();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					Thread.setDefaultUncaughtExceptionHandler(null);

					// rend l'ensemble des fichier de la base accessible en lecture/ecriture pour permettre
					// le multiutilisateur
					File[] dbfiles = new File(ApplicationCore.userRessources.getAllusersDataPath(), "base").listFiles(); //$NON-NLS-1$
					for (File dbfile : dbfiles) {
						if (dbfile.isFile()) {
							dbfile.setWritable(true, false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("core loaded");  //$NON-NLS-1$
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Profile profile = new Profile();
				core.addProfile(profile);
				new ConcoursJeunesFrame(profile);
			}
		});

	}
	
	private static void initSecureContext() {
		String urlAuthStoreAlias = "urlauthstore"; //$NON-NLS-1$
		char[] defaultUrlAuthStorePassword = AppUtilities.getAppUID(ApplicationCore.userRessources).toCharArray();
		
		try {
			SecretKey urlAuthStoreKey = (SecretKey)ApplicationCore.userRessources.getAppKeyStore().getKey(
					urlAuthStoreAlias, defaultUrlAuthStorePassword); 
			
			SecureSiteAuthenticationStore urlAuthStore = new SecureSiteAuthenticationStore(urlAuthStoreKey);
			if(urlAuthStoreKey == null) {
				 KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(urlAuthStore.getSecretKey());
				 ApplicationCore.userRessources.getAppKeyStore().setEntry(
						 urlAuthStoreAlias,
						 skEntry, new KeyStore.PasswordProtection(defaultUrlAuthStorePassword)); 
			}
			
			try {
				urlAuthStore.setStoreFile(new File(ApplicationCore.userRessources.getAllusersDataPath(), ApplicationCore.staticParameters.getResourceString("file.urlauthstore"))); //$NON-NLS-1$
				urlAuthStore.load(); 
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
			}
			
			SwingURLAuthenticator urlauth = new SwingURLAuthenticator();
			urlauth.setUrlAuthenticationStore(urlAuthStore);
			Authenticator.setDefault(urlauth);
			
			ApplicationCore.userRessources.storeAppKeyStore();
		} catch (UnrecoverableKeyException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (KeyStoreException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (CertificateException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(null, new ErrorInfo(e.getLocalizedMessage(),
					e.toString(),
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
		
		//Remplace le vérifieur de nom d'hôte par un autre moins restrictif
	    HostnameVerifier hostnameVerifier= new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
	    };
	    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	private static void loadStartupPlugin() {
		PluginLoader pl = new PluginLoader();
		
		List<String> disablePlugin = null;
		try {
			disablePlugin = XMLSerializer.loadXMLStructure(
					new File(ApplicationCore.userRessources.getConfigPathForUser(), "disable_plugins.xml"), false);  //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (PluginMetadata pm : pl.getPlugins(Type.STARTUP)) {
			if(disablePlugin != null && disablePlugin.contains(pm.getName()))
				continue;
			try {
				Class<?> cla = pm.getPluginClass();
				
				assert cla != null : "le loader devrait toujours retourner une class";  //$NON-NLS-1$
				
				Object plugin = cla.newInstance();
				for (Method m : cla.getMethods()) {
					if (m.isAnnotationPresent(PluginEntry.class)) {
						m.invoke(plugin, (Object[]) null);
						break;
					}
				}
			} catch (InstantiationException e1) {
				JXErrorPane.showDialog(null, new ErrorInfo(e1.getLocalizedMessage(), e1.toString(),
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				JXErrorPane.showDialog(null, new ErrorInfo(e1.getLocalizedMessage(), e1.toString(),
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			} catch (SecurityException e1) {
				JXErrorPane.showDialog(null, new ErrorInfo(e1.getLocalizedMessage(), e1.toString(),
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				JXErrorPane.showDialog(null, new ErrorInfo(e1.getLocalizedMessage(), e1.toString(),
						null, null, e1, Level.SEVERE, null));
				e1.printStackTrace();
			}
		}
	}
}
