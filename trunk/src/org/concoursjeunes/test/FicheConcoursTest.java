/**
 * 
 */
package org.concoursjeunes.test;

import static org.junit.Assert.*;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.FicheConcours;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author aurelien
 *
 */
public class FicheConcoursTest {
	
	ConcoursJeunes concoursJeunes;
	FicheConcours ficheConcours;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		concoursJeunes = new ConcoursJeunes();
		ficheConcours = concoursJeunes.createFicheConcours();
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#havePlaceForConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testHavePlaceForConcurrent() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#addConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testAddConcurrent() {
		Concurrent concurrent = new Concurrent();
		concurrent.setDepart(0);
		//concurrent.setDifferentiationCriteria();
		assertTrue(ficheConcours.addConcurrent(concurrent));
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#removeConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrent() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#removeConcurrentInTeam(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrentInTeam() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#placementConcurrents(int)}.
	 */
	@Test
	public void testPlacementConcurrents() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#placementConcurrent(org.concoursjeunes.Concurrent, org.concoursjeunes.Cible, int)}.
	 */
	@Test
	public void testPlacementConcurrent() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#nextConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testNextConcurrent() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#previousConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testPreviousConcurrent() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getNbCiblesLibre(int)}.
	 */
	@Test
	public void testGetNbCiblesLibre() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getOccupationCibles(int)}.
	 */
	@Test
	public void testGetOccupationCibles() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getPasDeTir(int)}.
	 */
	@Test
	public void testGetPasDeTir() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#silentSave()}.
	 */
	@Test
	public void testSilentSave() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getClassement(int, int)}.
	 */
	@Test
	public void testGetClassement() {
		fail("Non implémenté actuellement");
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getClassementEquipe(int)}.
	 */
	@Test
	public void testGetClassementEquipe() {
		fail("Non implémenté actuellement");
	}

	@After
	public void tearDown() {
		concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName());
	}
}
