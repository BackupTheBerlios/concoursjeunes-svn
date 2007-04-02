/**
 * Created on 17 déc. 2004
 */
package org.concoursjeunes;

import java.awt.Desktop;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import ajinteractive.standard.java2.*;

/**
 * Class principal de ConcoursJeunes, gére l'ensemble des ressources commune de l'application
 * @author  Aurelien Jeoffray
 * @version  @version.numero@ - @version.date@
 */
public class ConcoursJeunes {

	//UID: 1.Major(2).Minor(2).Correctif(2).Build(3).Type(1,Alpha,Beta,RC(1->6),Release)
	public static final long serialVersionUID          = 10120000011l;

	/**
	 * Chaines de version de ConcoursJeunes
	 */
	public static final String NOM                     = "@version.name@";      //$NON-NLS-1$
	public static final String VERSION                 = "@version.numero@ - @version.date@";//$NON-NLS-1$
	public static final String CODENAME                = "@version.codename@";  //$NON-NLS-1$
	public static final String AUTEURS                 = "@version.author@";    //$NON-NLS-1$
	public static final String COPYR                   = "@version.copyr@";     //$NON-NLS-1$

	/**
	 * Chaine de ressources
	 * 
	 */
	private static final String RES_LIBELLE         = "libelle";                //$NON-NLS-1$
	private static final String RES_PARAMETRE       = "parametre";              //$NON-NLS-1$

//	variable de récupération des ressources
	/**
	 * Chargement des Libelle de l'application
	 */
	public static AjResourcesReader ajrLibelle         = new AjResourcesReader(RES_LIBELLE);

	/**
	 * Chargement des parametrages statiques
	 */
	public static AjResourcesReader ajrParametreAppli  = new AjResourcesReader(RES_PARAMETRE);

	/**
	 * Gestion de la configuration
	 */
	public static Configuration configuration          = new Configuration();

	/**
	 * ressources utilisateurs
	 */
	public static UserRessources userRessources        = new UserRessources(NOM);
	
	public static Connection dbConnection;

	private MetaDataFichesConcours metaDataFichesConcours;
	private ArrayList<FicheConcours> fichesConcours    = new ArrayList<FicheConcours>();
	
	private EventListenerList listeners = new EventListenerList();
	
	/**	
	 * constructeur, création de la fenetre principale
	 */
	public ConcoursJeunes() {
		//tente de recuperer la configuration générale du programme
		restoreConfig();

		//fige la langue de ressource de l'appli sur la locale du fichier de config
		AjResourcesReader.setLocale(new Locale(configuration.getLangue()));

		//affecte la langue de l'interface
		ajrLibelle = new AjResourcesReader(RES_LIBELLE);

		//en debug_mode=0, log la sortie systeme
		if(ajrParametreAppli.getResourceInteger("debug.mode") == 0) { //$NON-NLS-1$
			try {
				System.setErr(new PrintStream(
						userRessources.getLogPathForProfile(configuration.getCurProfil()) +
						File.separator + ajrParametreAppli.getResourceString("log.error"))); //$NON-NLS-1$
				System.setOut(new PrintStream(
						userRessources.getLogPathForProfile(configuration.getCurProfil()) +
						File.separator + ajrParametreAppli.getResourceString("log.exec"))); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		//Pour le debugage donne le systeme de l'utilisateur
		System.out.println("OS: " + System.getProperty("os.name")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Architecture: " + System.getProperty("os.arch")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Version: " + System.getProperty("os.version")); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("Repertoire utilisateur: " + System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
		
		try {
			//chargement du driver
			Class.forName(ajrParametreAppli.getResourceString("database.driver")).newInstance();

			dbConnection = DriverManager.getConnection(
					ajrParametreAppli.getResourceString("database.url"),
					ajrParametreAppli.getResourceString("database.user"),
					ajrParametreAppli.getResourceString("database.password"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}        
	}
	
	public void addConcoursJeunesListener(ConcoursJeunesListener concoursJeunesListener) {
		listeners.add(ConcoursJeunesListener.class, concoursJeunesListener);
	}
	
	public void removeConcoursJeunesListener(ConcoursJeunesListener concoursJeunesListener) {
		listeners.remove(ConcoursJeunesListener.class, concoursJeunesListener);
	}

	/**
	 * restaure la configuration général du programme
	 *
	 */
	private void restoreConfig() {
		System.out.println("chargement de la configuration");

		configuration = (Configuration)AJToolKit.loadXMLStructure(new File(userRessources.getConfigPathForUser() + File.separator + 
				ajrParametreAppli.getResourceString("file.configuration")), false); //$NON-NLS-1$

		if(configuration == null) {
			configuration = new Configuration();
		}

		System.out.println("fin chargement de la configuration");
	}
	
	private void saveMetaDataFichesConcours() {
		if(configuration != null) {
			if(metaDataFichesConcours == null)
				loadMetaDataFichesConcours();
	
			AJToolKit.saveXMLStructure(
					new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + 
							ajrParametreAppli.getResourceString("file.metadatafichesconcours")), metaDataFichesConcours, false);
		}
	}
	
	private void loadMetaDataFichesConcours() {
		if(configuration != null) {
			metaDataFichesConcours = (MetaDataFichesConcours)AJToolKit.loadXMLStructure(
					new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + 
							ajrParametreAppli.getResourceString("file.metadatafichesconcours")), false);
			
			if(metaDataFichesConcours == null)
				metaDataFichesConcours = new MetaDataFichesConcours();
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="metaDataFichesConcours"
	 */
	public MetaDataFichesConcours getMetaDataFichesConcours() {
		if(metaDataFichesConcours == null)
			loadMetaDataFichesConcours();
		return metaDataFichesConcours;
	}

	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 * @return FicheConcours la fiche du concours créé
	 */
	public void createFicheConcours() {
		if(configuration != null) {
			FicheConcours ficheConcours = new FicheConcours();
			fichesConcours.add(ficheConcours);
			ficheConcours.silentSave();
			
			if(metaDataFichesConcours == null)
				loadMetaDataFichesConcours();
			
			MetaDataFicheConcours metaDataFicheConcours 
					= new MetaDataFicheConcours(
							ficheConcours.getParametre().getDate(), 
							ficheConcours.getParametre().getIntituleConcours(),
							ficheConcours.getParametre().getSaveName());
			metaDataFichesConcours.addMetaDataFicheConcours(metaDataFicheConcours);
			
			saveMetaDataFichesConcours();
			
			fireFicheConcoursCreated(ficheConcours);
		}
	}
	
	/**
	 * 
	 * @param ficheConcours
	 */
	public void deleteFicheConcours(String filename) {

		if(configuration != null) {
			for(MetaDataFicheConcours metaDataFicheConcours : metaDataFichesConcours.getFiches()) {
				String filenameConcours = metaDataFicheConcours.getFilenameConcours();
				if(filenameConcours.equals(filename)) {
					metaDataFichesConcours.removeMetaDataFicheConcours(metaDataFicheConcours);
					break;
				}
			}
			
			if(new File(userRessources.getConcoursPathForProfile(configuration.getCurProfil()) + File.separator + 
					filename).delete()) {
				fireFicheConcoursDeleted(null);
				saveMetaDataFichesConcours();
			} else {
				//TODO gerer l'exception
				//throw
			}
		}

	}

	/**
	 * Referme une fiche de concours
	 * 
	 * @param ficheConcours
	 * @return true si la fiche à été fermé et false sinon
	 */
	public void closeFicheConcours(FicheConcours ficheConcours) {
		ficheConcours.silentSave();
		if(fichesConcours.remove(ficheConcours)) {
			fireFicheConcoursClosed(ficheConcours);
		}
	}

	/**
	 * Restaure le coucours fournit en parametre
	 * 
	 * @param concoursFile - le chemin du concours à restaurer
	 * 
	 * @return FicheConcours la fiche du concours restauré
	 */
	public void restoreFicheConcours(File concoursFile) {
		System.out.println("chargement d'un concours");
		FicheConcours ficheConcours = null;
		
		Object[] savedStructure = (Object[])AJToolKit.loadXMLStructure(concoursFile, true);
		
		if(savedStructure != null) {
			//lecture du fichier
			ficheConcours = new FicheConcours();
			ficheConcours.setFiche((Object[])AJToolKit.loadXMLStructure(concoursFile, true));

			fichesConcours.add(ficheConcours);
		}
		
		fireFicheConcoursRestored(ficheConcours);
		

		System.out.println("Fin chargement d'un concours");
	}

	/**
	 * Sauvegarde l'ensemble des fiches de concours actuellement ouverte
	 *
	 */
	public void saveAllFichesConcours() {
		for(FicheConcours fiche : fichesConcours) {
			fiche.silentSave();
		}
	}

	/**
	 * genere le pdf à partir des parametres document et du contenu xml
	 * 
	 * @param document - parametre du doc
	 * @param xmlcontent - le contenu formater en xml iText
	 * 
	 * @return true si le pdf à correctement été généré, false si une erreur est survenue
	 */
	public static boolean printDocument(Document document, String xmlcontent) {
		boolean printOK = true;
		try {
			//cré un document pdf temporaire
			File tmpFile = File.createTempFile("cjta", ajrParametreAppli.getResourceString("extention.pdf")); //$NON-NLS-1$ //$NON-NLS-2$
			String filePath = tmpFile.getCanonicalPath();
			tmpFile.deleteOnExit();

			/*HeaderFooter footer = new HeaderFooter(new Phrase("page "), new Phrase("."));
            document.setFooter(footer);*/

			//genere le pdf
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			InputSource is = new InputSource(new StringReader(xmlcontent));
			parser.parse(is, new com.lowagie.text.xml.SAXiTextHandler(document));

			//affiche le pdf avec le reader pdf standard du systeme
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(tmpFile.getAbsolutePath()));
			} else {
				if(configuration != null) {
					String NAV =  configuration.getPdfReaderPath();
	
					System.out.println(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
					Runtime.getRuntime().exec(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			printOK = false;
		} finally {
			document.close();
		}

		return printOK;
	}
	
	private void fireFicheConcoursCreated(FicheConcours ficheConcours) {
		for(ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursCreated(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.CREATE_CONCOURS));
		}
	}
	
	private void fireFicheConcoursDeleted(FicheConcours ficheConcours) {
		for(ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursDeleted(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.DELETE_CONCOURS));
		}
	}
	
	private void fireFicheConcoursClosed(FicheConcours ficheConcours) {
		for(ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursClosed(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.CLOSE_CONCOURS));
		}
	}
	
	private void fireFicheConcoursRestored(FicheConcours ficheConcours) {
		for(ConcoursJeunesListener concoursJeunesListener : listeners.getListeners(ConcoursJeunesListener.class)) {
			concoursJeunesListener.ficheConcoursRestored(new ConcoursJeunesEvent(ficheConcours, ConcoursJeunesEvent.OPEN_CONCOURS));
		}
	}
}