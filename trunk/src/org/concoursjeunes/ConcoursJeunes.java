/*
 * Créer le 17 déc. 2004 pour ConcoursJeunes
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

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.naming.ConfigurationException;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.jdesktop.swingx.JXErrorDialog;
import org.xml.sax.InputSource;

import ajinteractive.standard.common.PluginClassLoader;
import ajinteractive.standard.java2.AjResourcesReader;
import ajinteractive.standard.utilities.sql.SqlParser;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Class principal de ConcoursJeunes, gére l'ensemble des ressources commune de
 * l'application tel que
 * <ul>
 * <li>Le chargement du fichier de configuration</li>
 * <li>L'accès aux fichiers de parametrage et libellés</li>
 * <li>L'accès aux ressources utilisateurs</li>
 * <li>La connexion à la base de données</li>
 * </ul>
 * 
 * En outre la class ConcoursJeunes gére l'ensemble des fiches concours du
 * logiciel (création, ouverture, fermeture, suppression)
 * 
 * @author Aurelien Jeoffray
 * @version
 * @version.numero@ -
 * @version.date@
 */
public class ConcoursJeunes {

	// UID:
	// 1.Major(2).Minor(2).Correctif(2).Build(3).Type(1,Alpha,Beta,RC(1->6),Release)
	public static final long serialVersionUID = 10190000011l;

	/**
	 * Chaines de version de ConcoursJeunes
	 */
	public static final String NOM = "@version.name@"; //$NON-NLS-1$

	public static final String VERSION = "@version.numero@ - @version.date@";//$NON-NLS-1$

	public static final String CODENAME = "@version.codename@"; //$NON-NLS-1$

	public static final String AUTEURS = "@version.author@"; //$NON-NLS-1$

	public static final String COPYR = "@version.copyr@"; //$NON-NLS-1$

	public static final int DB_RELEASE_REQUIRED = 1;

	// Chaine de ressources
	public static final String RES_LIBELLE = "libelle"; //$NON-NLS-1$

	public static final String RES_PARAMETRE = "parametre"; //$NON-NLS-1$

	/**
	 * Chargement des Libelle de l'application
	 */
	public static AjResourcesReader ajrLibelle = new AjResourcesReader(
			RES_LIBELLE);

	/**
	 * Chargement des parametrages statiques
	 */
	public static AjResourcesReader ajrParametreAppli = new AjResourcesReader(
			RES_PARAMETRE);

	/**
	 * Gestion de la configuration
	 */
	public static Configuration configuration = new Configuration();

	/**
	 * ressources utilisateurs
	 */
	public static CJAppRessources userRessources = new CJAppRessources(NOM);

	public static int dbVersion = 0;

	/**
	 * Connection à la base de données du logiciel
	 */
	public static Connection dbConnection;

	private final ArrayList<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();

	private final EventListenerList listeners = new EventListenerList();

	private static ConcoursJeunes instance;

	/**
	 * constructeur, création de la fenetre principale
	 */
	private ConcoursJeunes() {
		// tente de recuperer la configuration générale du programme
		configuration = ConfigurationBuilder.getCurrentConfiguration();

		reloadLibelle(new Locale(configuration.getLangue()));
		try {
			AjResourcesReader.setClassLoader(new PluginClassLoader(
					findParentClassLoader(), new File("plugins"))); //$NON-NLS-1$
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		// en debug_mode=0, log la sortie systeme
		if (ajrParametreAppli.getResourceInteger("debug.mode") == 0) { //$NON-NLS-1$
			try {
				System.setErr(new PrintStream(userRessources
						.getLogPathForProfile(configuration.getCurProfil())
						+ File.separator
						+ ajrParametreAppli.getResourceString("log.error"))); //$NON-NLS-1$
				System.setOut(new PrintStream(userRessources
						.getLogPathForProfile(configuration.getCurProfil())
						+ File.separator
						+ ajrParametreAppli.getResourceString("log.exec"))); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		// Pour le debugage donne le systeme de l'utilisateur
		System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out
				.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out
				.println("Java version:" + System.getProperty("java.version")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			dbConnection = DriverManager.getConnection(ajrParametreAppli
					.getResourceString(
							"database.url", userRessources.getBasePath()), //$NON-NLS-1$
					ajrParametreAppli.getResourceString("database.user"), //$NON-NLS-1$
					ajrParametreAppli.getResourceString("database.password")); //$NON-NLS-1$

			Statement stmt = dbConnection.createStatement();

			// test si la base existe déjà et retourne sa révision si c'est le
			// cas
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='PARAM'"); //$NON-NLS-1$
			if (rs.first()) {
				dbVersion = getDBVersion();
			}

			// si la version de la base est différente de la version requise par
			// le programme
			// copie les fichiers de mise à jour par défaut
			if (dbVersion < DB_RELEASE_REQUIRED) {
				userRessources.copyDefaultUpdateFile();
			} else if (dbVersion > DB_RELEASE_REQUIRED) {
				JOptionPane.showMessageDialog(null, ajrLibelle
						.getResourceString("erreur.dbrelease"), ajrLibelle //$NON-NLS-1$
						.getResourceString("erreur.dbrelease.title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

			String[] updateScripts = new File(userRessources
					.getAllusersDataPath()
					+ File.separator + "update").list(new FilenameFilter() { //$NON-NLS-1$
						public boolean accept(File dir, String name) {
							if (name.endsWith(".sql")) { //$NON-NLS-1$
								return true;
							}
							return false;
						}
					});
			if (updateScripts != null && updateScripts.length > 0) {
				Arrays.sort(updateScripts);

				int scriptRelease = dbVersion;
				for (String scriptPath : updateScripts) {
					scriptRelease = SqlParser
							.createBatch(
									new File(
											userRessources
													.getAllusersDataPath()
													+ File.separator
													+ "update" + File.separator + scriptPath), stmt, null, scriptRelease); //$NON-NLS-1$
					System.out
							.println("delete: " //$NON-NLS-1$
									+ new File(
											userRessources
													.getAllusersDataPath()
													+ File.separator
													+ "update" + File.separator + scriptPath).delete()); //$NON-NLS-1$
				}
				stmt.executeBatch();
				stmt.close();

				dbVersion = getDBVersion();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JXErrorDialog.showDialog(null,
					"SQL Error", e.getLocalizedMessage(), //$NON-NLS-1$
					e.fillInStackTrace());
			System.exit(1);
		}

		loadStartupPlugin();
	}

	/**
	 * Retourne l'instance unique du moteur du logiciel
	 * 
	 * @return l'instance de ConcoursJeunes
	 */
	public synchronized static ConcoursJeunes getInstance() {
		if (null == instance) { // Premier appel
			instance = new ConcoursJeunes();
		}
		return instance;
	}

	/**
	 * Recharge le fichier de libelle en fonction de la localité en parametre
	 * 
	 * @param locale -
	 *            la localité utilisé pour les libellés
	 */
	public static void reloadLibelle(Locale locale) {
		AjResourcesReader.setLocale(locale);
		ajrLibelle = new AjResourcesReader(RES_LIBELLE);
	}

	/**
	 * Ajoute un auditeur sur les evenements de gestion de concours de
	 * l'application
	 * 
	 * @param concoursJeunesListener
	 *            l'auditeur qui souhaite s'abonner
	 */
	public void addConcoursJeunesListener(
			ConcoursJeunesListener concoursJeunesListener) {
		listeners.add(ConcoursJeunesListener.class, concoursJeunesListener);
	}

	/**
	 * 
	 * @param concoursJeunesListener
	 */
	public void removeConcoursJeunesListener(
			ConcoursJeunesListener concoursJeunesListener) {
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
	 * Charge les plugins devant être initialisé au démmarage de l'application
	 */
	private void loadStartupPlugin() {
		PluginLoader pl = new PluginLoader();

		for (PluginMetadata pm : pl.getPlugins(PluginMetadata.STARTUP_PLUGIN)) {

			try {
				Class<?> cla = null;
				String importClass = pm.getClassName();
				if (importClass != null) {
					System.out.println("load startup plugin: " + pm.getInfo() //$NON-NLS-1$
							+ "(" + importClass + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					cla = Class.forName(importClass, false,
							new PluginClassLoader(findParentClassLoader(),
									new File("plugins"))); //$NON-NLS-1$
				}

				if (cla != null) {
					Object plugin = cla.newInstance();
					for (Method m : cla.getMethods()) {
						if (m.isAnnotationPresent(PluginEntry.class)) {
							m.invoke(plugin, (Object[]) null);
							break;
						}
					}
				}
			} catch (InstantiationException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						e1.fillInStackTrace());
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						e1.fillInStackTrace());
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						e1.fillInStackTrace());
				e1.printStackTrace();
			} catch (SecurityException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						e1.fillInStackTrace());
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
						e1.fillInStackTrace());
				e1.printStackTrace();
			} catch (MalformedURLException e) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle
						.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
						e.fillInStackTrace());
				e.printStackTrace();
			}
		}
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

	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 * @throws ConfigurationException
	 */
	public void createFicheConcours() throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = new FicheConcours();
		fichesConcours.add(ficheConcours);
		configuration.getMetaDataFichesConcours().add(
				ficheConcours.getMetaDataFicheConcours());

		configuration.saveAsDefault();
		ficheConcours.save();

		fireFicheConcoursCreated(ficheConcours);
	}

	/**
	 * 
	 * @param ficheConcours
	 * 
	 * @throws ConfigurationException
	 */
	public void deleteFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		configuration.getMetaDataFichesConcours().remove(metaDataFicheConcours);

		if (new File(userRessources.getConcoursPathForProfile(configuration
				.getCurProfil())
				+ File.separator + metaDataFicheConcours.getFilenameConcours())
				.delete()) {
			configuration.saveAsDefault();

			fireFicheConcoursDeleted(null);
		}
	}

	/**
	 * Referme une fiche de concours
	 * 
	 * @param ficheConcours
	 * 
	 * @throws ConfigurationException
	 */
	public void closeFicheConcours(FicheConcours ficheConcours)
			throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		ficheConcours.save();
		configuration.saveAsDefault();
		if (fichesConcours.remove(ficheConcours)) {
			fireFicheConcoursClosed(ficheConcours);
		}
	}

	/**
	 * 
	 * @throws ConfigurationException
	 */
	public void closeAllFichesConcours() throws ConfigurationException {
		saveAllFichesConcours();

		ArrayList<FicheConcours> tmpList = new ArrayList<FicheConcours>();
		tmpList.addAll(fichesConcours);
		fichesConcours.clear();
		for (FicheConcours fiche : tmpList) {
			fireFicheConcoursClosed(fiche);
		}
		tmpList.clear();
	}

	/**
	 * Restaure le coucours fournit en parametre
	 * 
	 * @param concoursFile -
	 *            le chemin du concours à restaurer
	 * @throws ConfigurationException
	 */
	public void restoreFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = FicheConcoursBuilder
				.getFicheConcours(metaDataFicheConcours);

		if (ficheConcours != null) {
			fichesConcours.add(ficheConcours);
			fireFicheConcoursRestored(ficheConcours);
		}
	}

	/**
	 * Sauvegarde l'ensemble des fiches de concours actuellement ouverte
	 * 
	 * @exception ConfigurationException
	 */
	public void saveAllFichesConcours() throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		for (FicheConcours fiche : fichesConcours) {
			fiche.save();
		}
		configuration.saveAsDefault();
	}

	/**
	 * genere le pdf à partir des parametres document et du contenu xml
	 * 
	 * @param document -
	 *            parametre du doc
	 * @param xmlcontent -
	 *            le contenu formater en xml iText
	 * 
	 * @return true si le pdf à correctement été généré, false si une erreur est
	 *         survenue
	 */
	public static boolean printDocument(Document document, String xmlcontent) {
		boolean printOK = true;
		try {
			// cré un document pdf temporaire
			File tmpFile = File
					.createTempFile(
							"cta", ajrParametreAppli.getResourceString("extention.pdf")); //$NON-NLS-1$ //$NON-NLS-2$
			String filePath = tmpFile.getCanonicalPath();
			tmpFile.deleteOnExit();

			/*
			 * HeaderFooter footer = new HeaderFooter(new Phrase("page "), new
			 * Phrase(".")); document.setFooter(footer);
			 */

			// genere le pdf
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			InputSource is = new InputSource(new StringReader(xmlcontent));
			parser
					.parse(is, new com.lowagie.text.xml.SAXiTextHandler(
							document));

			// affiche le pdf avec le reader pdf standard du systeme
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(tmpFile.getAbsolutePath()));
			} else {
				if (configuration != null) {
					String NAV = configuration.getPdfReaderPath();

					System.out.println(NAV
							+ " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
					Runtime.getRuntime().exec(
							NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			printOK = false;
		} finally {
			document.close();
		}

		return printOK;
	}

	private void fireFicheConcoursCreated(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners
				.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener
					.ficheConcoursCreated(new ConcoursJeunesEvent(
							ficheConcours, ConcoursJeunesEvent.CREATE_CONCOURS));
		}
	}

	private void fireFicheConcoursDeleted(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners
				.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener
					.ficheConcoursDeleted(new ConcoursJeunesEvent(
							ficheConcours, ConcoursJeunesEvent.DELETE_CONCOURS));
		}
	}

	private void fireFicheConcoursClosed(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners
				.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursClosed(new ConcoursJeunesEvent(
					ficheConcours, ConcoursJeunesEvent.CLOSE_CONCOURS));
		}
	}

	private void fireFicheConcoursRestored(FicheConcours ficheConcours) {
		for (ConcoursJeunesListener concoursJeunesListener : listeners
				.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener
					.ficheConcoursRestored(new ConcoursJeunesEvent(
							ficheConcours, ConcoursJeunesEvent.OPEN_CONCOURS));
		}
	}
}