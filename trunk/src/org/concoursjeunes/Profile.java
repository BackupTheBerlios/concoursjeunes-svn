package org.concoursjeunes;

import static org.concoursjeunes.ApplicationCore.userRessources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.ConfigurationException;
import javax.swing.event.EventListenerList;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.builders.FicheConcoursBuilder;
import org.concoursjeunes.event.ProfileEvent;
import org.concoursjeunes.event.ProfileListener;
import org.concoursjeunes.exceptions.NullConfigurationException;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class Profile {
	
	private String name = "defaut"; //$NON-NLS-1$
	
	private AjResourcesReader localisation = new AjResourcesReader("libelle"); //$NON-NLS-1$
	private Configuration configuration = new Configuration();
	private List<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();
	private EventListenerList listeners = new EventListenerList();
	
	public Profile() {
		this((String)null);
	}
	
	public Profile(String name){
		this.name = name;
		loadConfiguration();
	}
	
	public Profile(File profilepath) {
		loadConfiguration(profilepath);
	}
	
	/**
	 * tente de recuperer la configuration générale du programme
	 */
	private void loadConfiguration() {
		if(this.name == null) {
			configuration = ConfigurationManager.loadCurrentConfiguration();
			this.name = configuration.getCurProfil();
		} else {
			configuration = ConfigurationManager.loadConfiguration(name);
		}
		localisation.setLocale(new Locale(configuration.getLangue()));
	}
	
	private void loadConfiguration(File profilepath) {
		configuration = ConfigurationManager.loadConfiguration(profilepath);
		this.name = configuration.getCurProfil();
		localisation.setLocale(new Locale(configuration.getLangue()));
	}
	
	/**
	 * Ajoute un auditeur aux evenements du Singleton ConcoursJeunes
	 * 
	 * @param profileListener l'auditeur qui s'enregistre à la class
	 */
	public void addProfileListener(ProfileListener profileListener) {
		listeners.add(ProfileListener.class, profileListener);
	}

	/**
	 * Retire un auditeur aux evenements du Singleton ConcoursJeunes
	 * @param profileListener l'auditeur qui résilie à la class
	 */
	public void removeProfileListener(ProfileListener profileListener) {
		listeners.remove(ProfileListener.class, profileListener);
	}
	
	/**
	 * @return localisation
	 */
	public AjResourcesReader getLocalisation() {
		return localisation;
	}

	/**
	 * Retourne la configuration courante de l'application
	 * 
	 * @return la configuration de l'application
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * Définit la configuration de l'application
	 * 
	 * @param _configuration la configuration de l'application
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Création d'une nouvelle fiche concours
	 * 
	 * @throws ConfigurationException
	 */
	public void createFicheConcours()
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
		createFicheConcours(null);
	}
	
	/**
	 * Création d'une nouvelle fiche concours ayant les parametres fournit
	 * 
	 * @param parametre les parametres du concours
	 * @throws ConfigurationException
	 */
	public void createFicheConcours(Parametre parametre)
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		FicheConcours ficheConcours = new FicheConcours(this, parametre);
		fichesConcours.add(ficheConcours);
		configuration.getMetaDataFichesConcours().add(ficheConcours.getMetaDataFicheConcours());

		configuration.save();
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
	public void deleteFicheConcours(MetaDataFicheConcours metaDataFicheConcours)
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		configuration.getMetaDataFichesConcours().remove(metaDataFicheConcours);

		if (new File(userRessources.getConcoursPathForProfile(this) + File.separator + metaDataFicheConcours.getFilenameConcours()).delete()) {
			configuration.save();

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
	public void closeFicheConcours(FicheConcours ficheConcours)
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		ficheConcours.save();
		configuration.save();
		if (fichesConcours.remove(ficheConcours)) {
			fireFicheConcoursClosed(ficheConcours);
		}
	}

	/**
	 * Décharge de la mémoire l'ensemble des fiches ouvertes
	 * 
	 * @throws ConfigurationException
	 */
	public void closeAllFichesConcours()
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
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

		FicheConcours ficheConcours = FicheConcoursBuilder.getFicheConcours(metaDataFicheConcours, this);

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
	public void saveAllFichesConcours()
			throws NullConfigurationException, IOException, JAXBException, XMLStreamException {
		if (configuration == null)
			throw new NullConfigurationException("la configuration est null"); //$NON-NLS-1$

		for (FicheConcours fiche : fichesConcours) {
			fiche.save();
		}
		configuration.save();
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
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursCreated(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.CREATE_CONCOURS));
		}
	}

	private void fireFicheConcoursDeleted(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursDeleted(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.DELETE_CONCOURS));
		}
	}

	private void fireFicheConcoursClosed(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursClosed(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.CLOSE_CONCOURS));
		}
	}

	private void fireFicheConcoursRestored(FicheConcours ficheConcours) {
		for (ProfileListener concoursJeunesListener : listeners.getListeners(ProfileListener.class)) {
			concoursJeunesListener.ficheConcoursRestored(new ProfileEvent(ficheConcours, this, ProfileEvent.Type.OPEN_CONCOURS));
		}
	}
}
