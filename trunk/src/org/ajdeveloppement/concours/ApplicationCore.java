/*
 * Créé le 17 déc. 2004 pour ArcCompetition
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
package org.ajdeveloppement.concours;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.security.SecurityImporter;
import org.ajdeveloppement.commons.sql.SqlManager;
import org.ajdeveloppement.concours.db.UpgradeDatabaseEventListener;
import org.ajdeveloppement.concours.event.ApplicationCoreEvent;
import org.ajdeveloppement.concours.event.ApplicationCoreListener;
import org.ajdeveloppement.concours.managers.ConfigurationManager;
/**
 * Class principal de l'application, gère l'ensemble des ressources commune tel que
 * <ul>
 * <li>Le chargement du fichier de configuration</li>
 * <li>L'accès aux fichiers de paramétrage et libellés</li>
 * <li>L'accès aux ressources utilisateurs</li>
 * <li>La connexion à la base de données</li>
 * </ul>
 * 
 * En outre la class ApplicationCore conserve la liste des profile actuellement chargé par l'application
 * 
 * @author Aurélien JEOFFRAY
 * @version 2.0
 */
public class ApplicationCore {
	/**
	 * Numéro de version de la base de donnée nécessaire au fonctionnement du programme
	 */
	public static final int DB_RELEASE_REQUIRED = 31;

	/**
	 * Chargement des paramétrages statiques
	 */
	public static AjResourcesReader staticParameters = new AjResourcesReader("parametre");  //$NON-NLS-1$

	/**
	 * ressources utilisateurs
	 */
	public static AppRessources userRessources = new AppRessources(AppInfos.NOM);
	
	/**
	 * Connection à la base de données du logiciel
	 */
	public static Connection dbConnection;
	
	/**
	 * version de la base de donnée
	 */
	public static int dbVersion = 0;
	
	/**
	 * Identifiant unique de la base de données. Permet d'identifier
	 * si les fiches concours sont sérialisé avec des données en provenance
	 * de la base courante ou d'une autre base.
	 */
	public static UUID dbUUID = null;
	
	private static ApplicationCore instance;

	private static AppConfiguration _appConfiguration = new AppConfiguration();
	
	private List<Profile> profiles = new ArrayList<Profile>();
	private EventListenerList listeners = new EventListenerList();

	/**
	 * constructeur, création de la fenêtre principale
	 */
	private ApplicationCore() throws SQLException {
		SqlStoreHandler.setDatabaseEngine("h2"); //$NON-NLS-1$
		
		debugLogger();
		openDatabase();
		checkUpdateDatabase();
		UpgradeDatabaseEventListener.forceCloseMonitor();
		UpgradeDatabaseEventListener.setMonitorEnabled(false);
		loadAppConfiguration();
	}

	/**
	 * Initialise l'application
	 */
	public synchronized static void initializeApplication()  throws SQLException {
		if (null == instance) { // Premier appel
			instance = new ApplicationCore();
		}
	}
	/**
	 * Ajoute l'auditeur fournit en paramètre aux auditeurs de la class
	 * 
	 * @param listener
	 */
	public void addApplicationCoreListener(ApplicationCoreListener listener) {
		listeners.add(ApplicationCoreListener.class, listener);
	}
	
	/**
	 * Retire l'auditeur fournit en paramètre aux auditeurs de la class
	 * 
	 * @param listener
	 */
	public void removeApplicationCoreListener(ApplicationCoreListener listener) {
		listeners.remove(ApplicationCoreListener.class, listener);
	}
	
	/**
	 * Retourne l'instance unique du moteur du logiciel ou renvoie une RuntimeException si
	 * non initialisé.<br>
	 * <i>Pour initialisé le moteur, lancer la méthode static {@link ApplicationCore#initializeApplication()}</i>
	 * 
	 * @return l'instance de ApplicationCore
	 */
	public static ApplicationCore getInstance() {
		if(instance == null)
			throw new RuntimeException("ApplicationCore is not initialized. Invoke initializeApplication() before"); //$NON-NLS-1$
		return instance;
	}
	
	@SuppressWarnings("nls")
	private void loadAppConfiguration() {
		setAppConfiguration(ConfigurationManager.loadAppConfiguration());
		
		try {
			if(!userRessources.getAppKeyStoreFile().exists()) {
				File defaultSecurity = new File(staticParameters.getResourceString("path.ressources"), "security");
				for(File f : FileUtils.listAllFiles(defaultSecurity, ".*", false)) {
					String relativePath = f.getPath().substring(defaultSecurity.getParent().length() + 1);
					FileUtils.copyFile(f, new File(userRessources.getUpdatePath(),relativePath));
				}
			}
			SecurityImporter.importCerts(userRessources.getAppKeyStore(), new File(userRessources.getUpdatePath(), "security/certs")); //$NON-NLS-1$
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void debugLogger() {
		if (System.getProperty("debug.mode") == null) {   //$NON-NLS-1$
			try {
				System.setErr(new PrintStream(new File(userRessources.getAllusersDataPath(), staticParameters.getResourceString("log.error")))); //$NON-NLS-1$
				System.setOut(new PrintStream(new File(userRessources.getAllusersDataPath(), staticParameters.getResourceString("log.exec"))));   //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Pour le debugage donne le système de l'utilisateur
		System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Architecture: " + System.getProperty("os.arch"));   //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Version: " + System.getProperty("os.version"));    //$NON-NLS-1$//$NON-NLS-2$
		System.out.println("Repertoire utilisateur: " + System.getProperty("user.home"));   //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Java version:" + System.getProperty("java.version"));   //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Ouvre la base de donné de l'application
	 */
	private void openDatabase() throws SQLException {
		dbConnection = DriverManager.getConnection(staticParameters.getResourceString("database.url", userRessources.getBasePath()), //$NON-NLS-1$
				staticParameters.getResourceString("database.user"),   //$NON-NLS-1$
				staticParameters.getResourceString("database.password"));   //$NON-NLS-1$
	}
	
	private void checkUpdateDatabase() throws SQLException {
		Statement stmt = null;
		try {
			File updatePath = new File(staticParameters.getResourceString("path.ressources"), "update"); //$NON-NLS-1$ //$NON-NLS-2$
			stmt = dbConnection.createStatement();
			SqlManager sqlManager = new SqlManager(dbConnection, updatePath);

			// test si la base existe déjà et retourne sa révision si c'est le cas
			ResultSet rs = dbConnection.getMetaData().getTables(null, "PUBLIC", "PARAM", new String[] {"TABLE"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (rs.first()) {
				dbVersion = getDBVersion();
			}

			if (dbVersion > DB_RELEASE_REQUIRED) {
				throw new RuntimeException("Bad database version");   //$NON-NLS-1$
			}
			
			ScriptEngine scriptEngine = null;
			ScriptEngineManager se = new ScriptEngineManager();
			scriptEngine = se.getEngineByExtension("js");   //$NON-NLS-1$
			if(scriptEngine != null) {
				scriptEngine.put("dbVersion", dbVersion); //$NON-NLS-1$
				scriptEngine.put("sql", sqlManager);   //$NON-NLS-1$
				
				List<File> scripts = FileUtils.listAllFiles(updatePath, ".*\\.js");   //$NON-NLS-1$
				Collections.sort(scripts, new Comparator<File>() {
					@Override
					public int compare(File f1, File f2) {
						return f2.getName().compareTo(f1.getName());
					}
				});
				boolean updateError = false;
				for(File script : scripts) {
					try {		
						FileReader scriptReader = new FileReader(script);
						try {
							scriptEngine.eval(scriptReader);
						} finally {
							scriptReader.close();
						}
					} catch(IOException e) {
						e.printStackTrace();
						updateError = true;
					} catch (ScriptException e) {
						e.printStackTrace();
						updateError = true;
					}
				}
				
				if(updateError)
					throw new RuntimeException("Une erreur est survenue durant la mise à jour de la base de données.\n" //$NON-NLS-1$
							+ "Il est possible que l'application ne fonctionne pas correctement\n" //$NON-NLS-1$
							+ "Si tel est le cas il sera necessaire de réinitialiser la base");  //$NON-NLS-1$
			} else
				throw new RuntimeException("Votre machine virtuel java ne supporte pas javascript,\nl'application risque de ne pas fonctionner correctement");  //$NON-NLS-1$
		} finally {
			try { if(stmt != null) stmt.close(); } catch (SQLException e) { }
			dbVersion = getDBVersion();
			dbUUID = getDBUUID();
		}
	}
	
	/**
	 * Retourne la configuration de l'application
	 * 
	 * @return la configuration de l'application
	 */
	public static AppConfiguration getAppConfiguration() {
		return _appConfiguration;
	}
	
	/**
	 * Définit la configuration de l'application
	 * 
	 * @param appConfiguration la configuration de l'application
	 */
	public static void setAppConfiguration(AppConfiguration appConfiguration) {
		_appConfiguration = appConfiguration;
	}

	private int getDBVersion() {
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT DBVERSION FROM PARAM"); //$NON-NLS-1$
			try {
				if(rs.first())
					return rs.getInt("DBVERSION");   //$NON-NLS-1$
			} finally {
				if(rs != null)
					rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}

		return 0;
	}
	
	private UUID getDBUUID() {
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT DBUUID FROM PARAM"); //$NON-NLS-1$
			try {
				if(rs.first())
					return (UUID)rs.getObject("DBUUID");   //$NON-NLS-1$
			} finally {
				if(rs != null)
					rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
		
		return null;
	}

	/**
	 * Retourne la liste des profils actuellement chargé par l'application
	 * 
	 * @return profiles la liste des profils actuellement chargé par l'application
	 */
	public List<Profile> getProfiles() {
		return profiles;
	}
	
	/**
	 * Associe un profil à l'application.
	 * 
	 * @param profile le profil à associé à l'application
	 */
	public void addProfile(Profile profile) {
		profiles.add(profile);
		fireProfileAdded(profile);
	}
	
	/**
	 * Désolidarise un profil de l'application
	 * 
	 * @param profile le profil à désolidarisé de l'application
	 */
	public void removeProfile(Profile profile) {
		profiles.remove(profile);
		fireProfileRemoved(profile);
	}
	
	private void fireProfileAdded(Profile profile) {
		for (ApplicationCoreListener applicationCoreListener : listeners.getListeners(ApplicationCoreListener.class)) {
			applicationCoreListener.profileAdded(new ApplicationCoreEvent(profile));
		}
	}
	
	private void fireProfileRemoved(Profile profile) {
		for (ApplicationCoreListener applicationCoreListener : listeners.getListeners(ApplicationCoreListener.class)) {
			applicationCoreListener.profileRemoved(new ApplicationCoreEvent(profile));
		}
	}
}