package org.concoursjeunes;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.concoursjeunes.event.ProfileEvent;
import org.concoursjeunes.event.ProfileListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class ProfileTest extends TestCase {
	
	FicheConcours ficheConcours;

	@Before
	@Override
	public void setUp() throws Exception {
		ApplicationCore.initializeApplication();
	}
	
	@Test
	public void testProfile() {
		Profile profile = new Profile();
		
		assertNotNull(profile.getConfiguration());
	}

	@Test
	public void testProfileString() {
		String profileName = "defaut";
		Profile profile = new Profile(profileName);
		
		assertNotNull(profile.getConfiguration());
		assertEquals(profile.getConfiguration().getCurProfil(),profileName);
	}

	@Test
	public void testProfileFile() {
		File profileFile = new File(ApplicationCore.userRessources.getUserPath(), "configuration_defaut.xml");
		Profile profile = new Profile(profileFile);
		
		assertNotNull(profile.getConfiguration());
	}

	@Test
	public void testCreateFicheConcours() {
		Profile profile = new Profile();
		profile.addProfileListener(new ProfileListener() {
			@Override
			public void configurationChanged(ProfileEvent concoursJeunesEvent) {				
			}

			@Override
			public void ficheConcoursClosed(ProfileEvent concoursJeunesEvent) {
			}

			@Override
			public void ficheConcoursCreated(ProfileEvent concoursJeunesEvent) {
				ficheConcours = concoursJeunesEvent.getFicheConcours();
			}

			@Override
			public void ficheConcoursDeleted(ProfileEvent concoursJeunesEvent) {
			}

			@Override
			public void ficheConcoursRestored(ProfileEvent concoursJeunesEvent) {
			}
			
		});
		try {
			profile.createFicheConcours();
		} catch (NullConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		assertTrue(profile.getFichesConcours().size()==1);
		assertNotNull(ficheConcours);
		assertEquals(profile.getFichesConcours().get(0), ficheConcours);
		assertTrue(profile.getConfiguration().getMetaDataFichesConcours().contains(ficheConcours.getMetaDataFicheConcours()));
		assertTrue(new File(ApplicationCore.userRessources.getConcoursPathForProfile(profile), ficheConcours.getMetaDataFicheConcours().getFilenameConcours()).exists());
		
		try {
			profile.deleteFicheConcours(ficheConcours.getMetaDataFicheConcours());
		} catch (NullConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		assertFalse(new File(ApplicationCore.userRessources.getConcoursPathForProfile(profile), ficheConcours.getMetaDataFicheConcours().getFilenameConcours()).exists());
	}

	@Test
	public void testDeleteFicheConcours() {
		Profile profile = new Profile();
		profile.addProfileListener(new ProfileListener() {
			@Override
			public void configurationChanged(ProfileEvent concoursJeunesEvent) {				
			}

			@Override
			public void ficheConcoursClosed(ProfileEvent concoursJeunesEvent) {
			}

			@Override
			public void ficheConcoursCreated(ProfileEvent concoursJeunesEvent) {
				ficheConcours = concoursJeunesEvent.getFicheConcours();
			}

			@Override
			public void ficheConcoursDeleted(ProfileEvent concoursJeunesEvent) {
				ficheConcours = null;
			}

			@Override
			public void ficheConcoursRestored(ProfileEvent concoursJeunesEvent) {
			}
			
		});
		try {
			profile.createFicheConcours();
		} catch (NullConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		FicheConcours tmp = ficheConcours;
			
		try {
			profile.deleteFicheConcours(ficheConcours.getMetaDataFicheConcours());
		} catch (NullConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		assertNull(ficheConcours);
		assertFalse(profile.getConfiguration().getMetaDataFichesConcours().contains(tmp.getMetaDataFicheConcours()));
		assertFalse(new File(ApplicationCore.userRessources.getConcoursPathForProfile(profile), tmp.getMetaDataFicheConcours().getFilenameConcours()).exists());
	}

	@Test
	public void testCloseFicheConcours() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloseAllFichesConcours() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestoreFicheConcours() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveAllFichesConcours() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsOpenFicheConcours() {
		fail("Not yet implemented");
	}

}
