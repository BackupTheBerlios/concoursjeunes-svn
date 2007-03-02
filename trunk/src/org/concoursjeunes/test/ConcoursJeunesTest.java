package org.concoursjeunes.test;

import static org.junit.Assert.*;

import java.io.File;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.FicheConcours;
import org.junit.Before;
import org.junit.Test;

/**
 * @author  aurelien
 */
public class ConcoursJeunesTest {
	
	ConcoursJeunes concoursJeunes;

	@Before
	public void setUp() throws Exception {
		concoursJeunes = new ConcoursJeunes();
	}

	@Test
	public void testConcoursJeunes() {
		assertNotNull(ConcoursJeunes.configuration);
		assertNotNull(ConcoursJeunes.ajrLibelle);
		assertNotNull(ConcoursJeunes.ajrParametreAppli);
		assertNotNull(ConcoursJeunes.userRessources);
	}

	@Test
	public void testGetMetaDataFichesConcours() {
		assertNotNull(concoursJeunes.getMetaDataFichesConcours());
	}

	@Test
	public void testCreateFicheConcours() {
		System.out.println("creation concours");
		FicheConcours ficheConcours = concoursJeunes.createFicheConcours();
		
		assertNotNull(ficheConcours);
		assertNotNull(concoursJeunes.getMetaDataFichesConcours());
		
		String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
		
		assertTrue(new File(concourspath + File.separator + ficheConcours.getParametre().getSaveName()).exists());
		
		concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName());
	}

	@Test
	public void testDeleteFicheConcours() {
		FicheConcours ficheConcours = concoursJeunes.createFicheConcours();
		
		String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
		
		assertTrue("echec de la suppression", concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName()));
		assertFalse("Le fichier du concours ne devarit plus exister", new File(concourspath + File.separator + ficheConcours.getParametre().getSaveName()).exists());
	}

	@Test
	public void testCloseFicheConcours() {
		FicheConcours ficheConcours = concoursJeunes.createFicheConcours();
		
		assertTrue("la fiche n'a pas été fermé", concoursJeunes.closeFicheConcours(ficheConcours));
		//String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
		
		concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName());
	}
	
	@Test
	public void testRestoreFicheConcours() {
		FicheConcours ficheConcours = concoursJeunes.createFicheConcours();
		concoursJeunes.closeFicheConcours(ficheConcours);
		
		String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
		
		File fConcours = new File(concourspath + File.separator + ficheConcours.getParametre().getSaveName());
		ficheConcours = concoursJeunes.restoreFicheConcours(fConcours);
		
		assertNotNull("la fiche concours n'est pas chargé correctement", ficheConcours);
		
		if(ficheConcours != null) {
			assertTrue(concoursJeunes.closeFicheConcours(ficheConcours));
			System.out.println(ficheConcours.getParametre().getSaveName());
			assertTrue(concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName()));
		}
	}
}