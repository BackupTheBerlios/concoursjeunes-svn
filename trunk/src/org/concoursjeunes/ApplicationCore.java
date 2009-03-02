/*
 * Créer le 17 déc. 2004 pour ConcoursJeunes
 *
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
package org.concoursjeunes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.ConfigurationException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.ajdeveloppement.commons.sql.SqlManager;
import org.concoursjeunes.builders.FicheConcoursBuilder;
import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
/**
 * Class principal de l'application, gére l'ensemble des ressources commune tel que
 * <ul>
 * <li>Le chargement du fichier de configuration</li>
 * <li>L'accès aux fichiers de parametrage et libellés</li>
 * <li>L'accès aux ressources utilisateurs</li>
 * <li>La connexion à la base de données</li>
 * </ul>
 * 
 * En outre la class ApplicationCore gére l'ensemble des fiches concours du logiciel (création, ouverture, fermeture, suppression)
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 */
public class ApplicationCore {

	/**
	 * Nom public de l'application
	 */
	public static final String NOM = "@version.name@"; //$NON-NLS-1$
	
	/**
	 * version de l'application
	 */
	public static final String VERSION = new String("@version.numero@ - @version.date@");//$NON-NLS-1$
	
	/**
	 * Nom de code de l'application
	 */
	public static final String CODENAME = "@version.codename@"; //$NON-NLS-1$
	
	/**
	 * Auteur(s) de l'application
	 */
	public static final String AUTEURS = "@version.author@"; //$NON-NLS-1$
	
	/**
	 * Copyright de l'application
	 */
	public static final String COPYR = "@version.copyr@"; //$NON-NLS-1$
	
	/**
	 * Numero de version de la base de donnée nécessaire au fonctionnement du programme
	 */
	public static final int DB_RELEASE_REQUIRED = 20;

	/**
	 * Chargement des Libelle de l'application
	 */
	public static AjResourcesReader ajrLibelle = new AjResourcesReader("libelle"); //$NON-NLS-1$

	/**
	 * Chargement des parametrages statiques
	 */
	public static AjResourcesReader ajrParametreAppli = new AjResourcesReader("parametre"); //$NON-NLS-1$

	/**
	 * ressources utilisateurs
	 */
	public static AppRessources userRessources = new AppRessources(NOM);
	
	/**
	 * Connection à la base de données du logiciel
	 */
	public static Connection dbConnection;
	
	/**
	 * version de la base de donnée
	 */
	public static int dbVersion = 0;
	
	private static boolean consoleMode = true;
	private static Configuration configuration = new Configuration();
	private static ApplicationCore instance;
	
	private final List<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * constructeur, création de la fenetre principale
	 */
	private ApplicationCore() throws SQLException {
		loadConfiguration();
		loadLibelle();
		debugLogger();
		openDatabase();
		checkUpdateDatabase();
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
	 * Retourne l'instance unique du moteur du logiciel ou null si le moteur
	 * n'est pas initialisé.
	 * Pour initialisé le moteur, lancer la métyode static initializeApplication()
	 * 
	 * @return l'instance de ConcoursJeunes
	 */
	public static ApplicationCore getInstance() {
		return instance;
	}
	
	/**
	 * tente de recuperer la configuration générale du programme
	 */
	private void loadConfiguration() {
		configuration = ConfigurationManager.loadCurrentConfiguration();
	}
	
	/**
	 * Charge les libelle de l'application en fonction de la locale
	 */
	private void loadLibelle() {
		Locale.setDefault(new Locale(configuration.getLangue()));
		reloadLibelle();
	}
	
	private void debugLogger() {
		if (System.getProperty("debug.mode") == null) { //$NON-NLS-1$
			try {
				System.setErr(new PrintStream(new File(userRessources.getAllusersDataPath(), ajrParametreAppli.getResourceString("log.error")))); //$NON-NLS-1$
				System.setOut(new PrintStream(new File(userRessources.getAllusersDataPath(), ajrParametreAppli.getResourceString("log.exec")))); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Pour le debugage donne le systeme de l'utilisateur
		System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Java version:" + System.getProperty("java.version")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Ouvre la base de donné de l'application
	 */
	private void openDatabase() throws SQLException {
		dbConnection = DriverManager.getConnection(ajrParametreAppli.getResourceString("database.url", userRessources.getBasePath()), //$NON-NLS-1$
				ajrParametreAppli.getResourceString("database.user"), //$NON-NLS-1$
				ajrParametreAppli.getResourceString("database.password")); //$NON-NLS-1$
	}
	
	private void checkUpdateDatabase() throws SQLException {
		Statement stmt = null;
		try {
			File updatePath = new File(ajrParametreAppli.getResourceString("path.ressources"), "update"); //$NON-NLS-1$ //$NON-NLS-2$
			stmt = dbConnection.createStatement();
			SqlManager sqlManager = new SqlManager(dbConnection, updatePath);

			// test si la base existe déjà et retourne sa révision si c'est le cas
			ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='PARAM'"); //$NON-NLS-1$
			if (rs.first()) {
				dbVersion = getDBVersion();
			}

			// si la version de la base est différente de la version requise par le programme
			// copie les fichiers de mise à jour par défaut
			if (dbVersion > DB_RELEASE_REQUIRED) {
				throw new RuntimeException(ajrLibelle.getResourceString("erreur.dbrelease")); //$NON-NLS-1$
			}
			
			ScriptEngine scriptEngine = null;
			ScriptEngineManager se = new ScriptEngineManager();
			scriptEngine = se.getEngineByExtension("js"); //$NON-NLS-1$
			if(scriptEngine != null) {
				scriptEngine.put("dbVersion", dbVersion); //$NON-NLS-1$
				scriptEngine.put("sql", sqlManager); //$NON-NLS-1$
				
				List<File> scripts = FileUtils.listAllFiles(updatePath, ".*\\.js"); //$NON-NLS-1$
				for(File script : scripts) {
					try {		
						FileReader scriptReader = new FileReader(script);
						scriptEngine.eval(scriptReader);
						scriptReader.close();
					} catch(IOException e) {
						e.printStackTrace();
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				}
			} else
				throw new RuntimeException("Votre machine virtuel java ne supporte pas javascript,\nl'application risque de ne pas fonctionner correctement"); //$NON-NLS-1$
		} finally {
			try { if(stmt != null) stmt.close(); } catch (SQLException e) { }
			dbVersion = getDBVersion();
		}
	}

	/**
	 * Recharge le fichier de libelle en fonction de la localite definit par defaut
	 */
	public static void reloadLibelle() {
		AjResourcesReader.setLocale(Locale.getDefault());
		ajrLibelle = new AjResourcesReader("libelle"); //$NON-NLS-1$
	}
	
	/**
	 * Retourne la configuration courante de l'application
	 * 
	 * @return la configuration de l'application
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * Définit la configuration de l'application
	 * 
	 * @param _configuration la configuration de l'application
	 */
	public static void setConfiguration(Configuration _configuration) {
		configuration = _configuration;
		if(instance != null)
			instance.fireConfigurationChanged();
	}

	/**
	 * @return interactiveMode
	 */
	public static boolean isConsoleMode() {
		return consoleMode;
	}

	/**
	 * @param interactiveMode interactiveMode à définir
	 */
	public static void setConsoleMode(boolean consoleMode) {
		ApplicationCore.consoleMode = consoleMode;
	}

	/**
	 * Ajoute un auditeur aux evenements du Singleton ConcoursJeunes
	 * 
	 * @param concoursJeunesListener l'auditeur qui s'enregistre à la class
	 */
	public void addConcoursJeunesListener(ConcoursJeunesListener concoursJeunesListener) {
		listeners.add(ConcoursJeunesListener.class, concoursJeunesListener);
	}

	/**
	 * Retire un auditeur aux evenements du Singleton ConcoursJeunes
	 * @param concoursJeunesListener l'auditeur qui résilie à la class
	 */
	public void removeConcoursJeunesListener(ConcoursJeunesListener concoursJeunesListener) {
		listeners.remove(ConcoursJeunesListener.class, concoursJeunesListener);
	}

	private int getDBVersion() {
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM PARAM"); //$NON-NLS-1$
			rs.first();
			return rs.getInt("DBVERSION"); //$NON-NLS-1$
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

	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 * @throws ConfigurationException
	 */
	public void createFicheConcours() throws NullConfigurationException, IOException {
		createFicheConcours(null);
	}
	
	/**
	 * Création d'une nouvelle fiche concours ayant les parametres fournit
	 * 
	 * @param parametre les parametres du concours
	 * @throws ConfigurationException
	 */
	public void createFicheConcours(Parametre parametre) throws NullConfigurationException, IOException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = new FicheConcours(parametre);
		fichesConcours.add(ficheConcours);
		configuration.getMetaDataFichesConcours().add(ficheConcours.getMetaDataFicheConcours());

		configuration.saveAsDefault();
		ficheConcours.save();

		fireFicheConcoursCreated(ficheConcours);
	}

	/**
	 * Supprime une fiche concours du système
	 * 
	 * @param metaDataFicheConcours le fichier de metadonné contenant les
	 * informations sur le concours à supprimer
	 * 
	 * @throws ConfigurationException
	 */
	public void deleteFicheConcours(MetaDataFicheConcours metaDataFicheConcours) throws NullConfigurationException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		configuration.getMetaDataFichesConcours().remove(metaDataFicheConcours);

		if (new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + metaDataFicheConcours.getFilenameConcours()).delete()) {
			configuration.saveAsDefault();

			fireFicheConcoursDeleted(null);
		}
	}

	/**
	 * Referme une fiche de concours
	 * 
	 * @param ficheConcours la fiche concours à décharger de la méméoire
	 * 
	 * @throws ConfigurationException
	 */
	public void closeFicheConcours(FicheConcours ficheConcours) throws NullConfigurationException, IOException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		ficheConcours.save();
		configuration.saveAsDefault();
		if (fichesConcours.remove(ficheConcours)) {
			fireFicheConcoursClosed(ficheConcours);
		}
	}

	/**
	 * Décharge de la mémoire l'ensemble des fiches ouvertes
	 * 
	 * @throws ConfigurationException
	 */
	public void closeAllFichesConcours() throws NullConfigurationException, IOException {
		saveAllFichesConcours();

		ArrayList<FicheConcours> tmpList = new ArrayList<FicheConcours>();
		tmpList.addAll(fichesConcours);
		fichesConcours.clear();
		for (FicheConcours fiche : tmpList) {
			fireFicheConcoursClosed(fiche);
		}
		tmpList.clear();
		
		configuration.save();
	}

	/**
	 * Restaure le coucours dont l'objet de metadonnée est fournit en parametre
	 * 
	 * @param metaDataFicheConcours -
	 *            l'objet metadonnée du concours à restaurer
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	public void restoreFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws NullConfigurationException, IOException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = FicheConcoursBuilder.getFicheConcours(metaDataFicheConcours);

		if (ficheConcours != null) {
			fichesConcours.add(ficheConcours);
			fireFicheConcoursRestored(ficheConcours);
		}
	}

	/**
	 * Sauvegarde l'ensemble des fiches de concours actuellement ouverte
	 * 
	 * @throws NullConfigurationException
	 * @throws IOException
	 */
	public void saveAllFichesConcours() throws NullConfigurationException, IOException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		for (FicheConcours fiche : fichesConcours) {
			fiche.save();
		}
		configuration.saveAsDefault();
	}
	
	/**
	 * Retourne la liste des fiches concours actuellement ouvertent
	 * 
	 * @return la liste des fiches concours actuellement ouvertent
	 */
	public List<FicheConcours> getFichesConcours() {
		return fichesConcours;
	}
	
	/**
	 * Test si une fiche est déjà ouverte ou non
	 * 
	 * @param metaDataFicheConcours - le fichier de metadonnées du concours à tester
	 * @return true si ouvert, false sinon
	 */
	public boolean isOpenFicheConcours(MetaDataFicheConcours metaDataFicheConcours) {
		for(FicheConcours ficheConcours : fichesConcours) {
			if(ficheConcours.getMetaDataFicheConcours().equals(metaDataFicheConcours))
				return true;
		}
		return false;
	}

	private void fireFicheConcoursCreated(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursCreated(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.Type.CREATE_CONCOURS));
		}
	}

	private void fireFicheConcoursDeleted(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursDeleted(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.Type.DELETE_CONCOURS));
		}
	}

	private void fireFicheConcoursClosed(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursClosed(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.Type.CLOSE_CONCOURS));
		}
	}

	private void fireFicheConcoursRestored(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursRestored(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.Type.OPEN_CONCOURS));
		}
	}
	
	private void fireConfigurationChanged() {
		for (ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.configurationChanged(new ConcoursJeunesEvent(null, ConcoursJeunesEvent.Type.CONFIGURATION_CHANGED));
		}
	}
}