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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.naming.ConfigurationException;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.concoursjeunes.builders.FicheConcoursBuilder;
import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.xml.sax.InputSource;

import ajinteractive.standard.common.AjResourcesReader;
import ajinteractive.standard.common.PluginClassLoader;
import ajinteractive.standard.utilities.io.FileUtil;
import ajinteractive.standard.utilities.sql.SqlManager;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Class principal de ConcoursJeunes, gére l'ensemble des ressources commune de l'application tel que
 * <ul>
 * <li>Le chargement du fichier de configuration</li>
 * <li>L'accès aux fichiers de parametrage et libellés</li>
 * <li>L'accès aux ressources utilisateurs</li>
 * <li>La connexion à la base de données</li>
 * </ul>
 * 
 * En outre la class ConcoursJeunes gére l'ensemble des fiches concours du logiciel (création, ouverture, fermeture, suppression)
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 */
public class ConcoursJeunes {

	// UID: 1.Major(2).Minor(2).Correctif(2).Build(3).Type(1,Alpha,Beta,RC(1->6),Release)
	public static final long serialVersionUID = 10205000011l;

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
	public static final int DB_RELEASE_REQUIRED = 10;
	
	// Chaine de ressources
	private static final String RES_LIBELLE = "libelle"; //$NON-NLS-1$
	private static final String RES_PARAMETRE = "parametre"; //$NON-NLS-1$

	/**
	 * Chargement des Libelle de l'application
	 */
	public static AjResourcesReader ajrLibelle = new AjResourcesReader(RES_LIBELLE);

	/**
	 * Chargement des parametrages statiques
	 */
	public static AjResourcesReader ajrParametreAppli = new AjResourcesReader(RES_PARAMETRE);

	/**
	 * ressources utilisateurs
	 */
	public static CJAppRessources userRessources = new CJAppRessources(NOM);
	/**
	 * version de la base de donnée
	 */
	public static int dbVersion = 0;

	/**
	 * Connection à la base de données du logiciel
	 */
	public static Connection dbConnection;

	private static Configuration configuration = new Configuration();
	private final ArrayList<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();
	private final EventListenerList listeners = new EventListenerList();
	private static ConcoursJeunes instance;

	/**
	 * constructeur, création de la fenetre principale
	 */
	private ConcoursJeunes() {
		// tente de recuperer la configuration générale du programme
		try {
			configuration = ConfigurationManager.loadCurrentConfiguration();
		} catch (IOException e2) {
			JXErrorPane.showDialog(null, 
					new ErrorInfo(ajrLibelle.getResourceString("erreur.concoursjeunes.loadconfig.title"), //$NON-NLS-1$
					ajrLibelle.getResourceString("erreur.concoursjeunes.loadconfig"),  //$NON-NLS-1$
					null, null, e2, Level.SEVERE, null));
			e2.printStackTrace();
			System.exit(1);
		}
		
		Locale.setDefault(new Locale(configuration.getLangue()));
		reloadLibelle();
		try {
			AjResourcesReader.setClassLoader(new PluginClassLoader(findParentClassLoader(), new File("plugins"))); //$NON-NLS-1$
		} catch (MalformedURLException e1) {
			JXErrorPane.showDialog(null, new ErrorInfo(ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
					e1.toString(), null, null, e1, Level.SEVERE, null));
			e1.printStackTrace();
		}

		if (System.getProperty("debug.mode") == null) { //$NON-NLS-1$
			try {
				System.setErr(new PrintStream(userRessources.getLogPathForProfile(configuration.getCurProfil()) + File.separator + ajrParametreAppli.getResourceString("log.error"))); //$NON-NLS-1$
				System.setOut(new PrintStream(userRessources.getLogPathForProfile(configuration.getCurProfil()) + File.separator + ajrParametreAppli.getResourceString("log.exec"))); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				JXErrorPane.showDialog(null, new ErrorInfo(ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
						e.toString(), null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			}
		}
		
		// Pour le debugage donne le systeme de l'utilisateur
		System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Java version:" + System.getProperty("java.version")); //$NON-NLS-1$ //$NON-NLS-2$

		boolean erasedb = false;
		do {
			try {
				dbConnection = DriverManager.getConnection(ajrParametreAppli.getResourceString("database.url", userRessources.getBasePath()), //$NON-NLS-1$
						ajrParametreAppli.getResourceString("database.user"), //$NON-NLS-1$
						ajrParametreAppli.getResourceString("database.password")); //$NON-NLS-1$
			} catch (SQLException e) {
				e.printStackTrace();
				JXErrorPane.showDialog(null,new ErrorInfo( "SQL Error", e.toString(), //$NON-NLS-1$
						null, null, e, Level.SEVERE, null));
				
				//Si ce n'est pas un message db bloqué par un autre processus
				if(!e.getMessage().contains("[90020")) { //$NON-NLS-1$
					if(JOptionPane.showConfirmDialog(null, ajrLibelle.getResourceString("erreur.breakdb")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
						erasedb = true;
						for(File deletefile : new File(userRessources.getBasePath()).listFiles()) {
							deletefile.delete();
						}
					} else {
						System.exit(1);
					}
				} else {
					System.exit(1);
				}
			}
		} while(erasedb);
		
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();

			// test si la base existe déjà et retourne sa révision si c'est le cas
			ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='PARAM'"); //$NON-NLS-1$
			if (rs.first()) {
				dbVersion = getDBVersion();
			}

			// si la version de la base est différente de la version requise par le programme
			// copie les fichiers de mise à jour par défaut
			if (dbVersion < DB_RELEASE_REQUIRED) {
				userRessources.copyDefaultUpdateFile();
			} else if (dbVersion > DB_RELEASE_REQUIRED) {
				JOptionPane.showMessageDialog(null, ajrLibelle.getResourceString("erreur.dbrelease"), //$NON-NLS-1$
						ajrLibelle.getResourceString("erreur.dbrelease.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				System.exit(1);
			}
			if (dbVersion != DB_RELEASE_REQUIRED) {
				File updatePath = new File(userRessources.getAllusersDataPath() + File.separator + "update"); //$NON-NLS-1$
				
				ScriptEngineManager se = new ScriptEngineManager();
				ScriptEngine scriptEngine = se.getEngineByName("JavaScript"); //$NON-NLS-1$
				scriptEngine.setBindings(new SimpleBindings(Collections.synchronizedMap(new HashMap<String, Object>())), ScriptContext.ENGINE_SCOPE);
				try {
					scriptEngine.put("dbVersion", dbVersion); //$NON-NLS-1$
					scriptEngine.put("sql", new SqlManager(stmt, updatePath)); //$NON-NLS-1$
					
					List<File> scripts = FileUtil.listAllFiles(updatePath, ".*\\.js"); //$NON-NLS-1$
					for(File script : scripts)
						scriptEngine.eval(new FileReader(script));
				} catch (ScriptException e1) {
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} finally {
					//Supprime les fichiers du repertoire update après une mise à jour
					for(File file : FileUtil.listAllFiles(updatePath, ".*")) { //$NON-NLS-1$
						boolean success = file.delete();
						System.out.println("delete: " + file.getName() + ": " //$NON-NLS-1$ //$NON-NLS-2$
								+ success);
						if(!success)
							file.deleteOnExit();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JXErrorPane.showDialog(null, new ErrorInfo("SQL Error", e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
		} finally {
			try { if(stmt != null) stmt.close(); } catch (SQLException e) { }
			dbVersion = getDBVersion();
		}
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
	 * Recharge le fichier de libelle en fonction de la localite definit par defaut
	 */
	public static void reloadLibelle() {
		AjResourcesReader.setLocale(Locale.getDefault());
		ajrLibelle = new AjResourcesReader(RES_LIBELLE);
	}
	
	/**
	 * genere le pdf à partir des parametres document et du contenu xml
	 * 
	 * @param document -
	 *            parametre du doc
	 * @param xmlcontent -
	 *            le contenu formater en xml iText
	 * 
	 * @return true si le pdf à correctement été généré, false si une erreur est survenue
	 */
	public static boolean printDocument(Document document, String xmlcontent) {
		boolean printOK = true;
		
		if(xmlcontent.isEmpty())
			return false;
		
		try {
			// cré un document pdf temporaire
			File tmpFile = File.createTempFile("cta", ajrParametreAppli.getResourceString("extention.pdf")); //$NON-NLS-1$ //$NON-NLS-2$
			String filePath = tmpFile.getCanonicalPath();
			tmpFile.deleteOnExit();

			
			//HeaderFooter footer = new HeaderFooter(new Phrase("page "), new Phrase(".")); document.setFooter(footer);
			 

			// genere le pdf
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			InputSource is = new InputSource(new StringReader(xmlcontent));
			parser.parse(is, new com.lowagie.text.xml.SAXiTextHandler(document));

			// affiche le pdf avec le reader pdf standard du systeme
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(tmpFile.getAbsolutePath()));
			} else {
				if (configuration != null) {
					String NAV = configuration.getPdfReaderPath();

					System.out.println(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
					Runtime.getRuntime().exec(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
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
	public void createFicheConcours() throws ConfigurationException, IOException {
		createFicheConcours(null);
	}
	
	/**
	 * Création d'une nouvelle fiche concours ayant les parametres fournit
	 * 
	 * @param parametre les parametres du concours
	 * @throws ConfigurationException
	 */
	public void createFicheConcours(Parametre parametre) throws ConfigurationException, IOException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

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
	public void deleteFicheConcours(MetaDataFicheConcours metaDataFicheConcours) throws ConfigurationException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

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
	public void closeFicheConcours(FicheConcours ficheConcours) throws ConfigurationException, IOException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

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
	public void closeAllFichesConcours() throws ConfigurationException, IOException {
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
			throws ConfigurationException, IOException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = FicheConcoursBuilder.getFicheConcours(metaDataFicheConcours);

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
	public void saveAllFichesConcours() throws ConfigurationException, IOException {
		if (configuration == null)
			throw new ConfigurationException("la configuration est null"); //$NON-NLS-1$

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