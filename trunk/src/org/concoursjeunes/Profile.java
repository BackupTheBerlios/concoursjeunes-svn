package org.concoursjeunes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.ConfigurationException;
import javax.swing.event.EventListenerList;

import org.concoursjeunes.builders.FicheConcoursBuilder;
import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.concoursjeunes.exceptions.NullConfigurationException;

import static org.concoursjeunes.ApplicationCore.userRessources;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class Profile {
	
	private String name = "defaut"; //$NON-NLS-1$
	
	private Configuration configuration = new Configuration();
	private List<FicheConcours> fichesConcours = new ArrayList<FicheConcours>();
	private EventListenerList listeners = new EventListenerList();
	
	public Profile() throws IOException {
		this("defaut"); //$NON-NLS-1$
	}
	
	public Profile(String name) throws IOException {
		this.name = name;
		loadConfiguration();
	}
	
	/**
	 * tente de recuperer la configuration générale du programme
	 */
	private void loadConfiguration() throws IOException {
		configuration = ConfigurationManager.loadConfiguration(name);
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

		if (new File(userRessources.getConcoursPathForProfile(name) + File.separator + metaDataFicheConcours.getFilenameConcours()).delete()) {
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
	 * @exception ConfigurationException
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
}
