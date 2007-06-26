/**
 * 
 */
package org.concoursjeunes.test;

import static org.junit.Assert.*;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ConcoursJeunesEvent;
import org.concoursjeunes.ConcoursJeunesListener;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.FicheConcours;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Aurélien JEOFFRAY
 * TODO revoir les test avec creation de concours
 */
public class FicheConcoursTest {
	
	private ConcoursJeunes concoursJeunes;
	private FicheConcours ficheConcours;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		concoursJeunes = ConcoursJeunes.getInstance();
		
		concoursJeunes.addConcoursJeunesListener(new ConcoursJeunesListener() {

			public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent) {}
			/* (non-Javadoc)
			 * @see org.concoursjeunes.ConcoursJeunesListener#ficheConcoursCreated(org.concoursjeunes.ConcoursJeunesEvent)
			 */
			public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent) {
				ficheConcours = concoursJeunesEvent.getFicheConcours();
			}
			public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent) {}
			public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent) {}
			
		});
		
		concoursJeunes.createFicheConcours();
	}
	
	@Test
	public void testFicheConcours() {
		
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#addConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testAddConcurrent() {
		Concurrent concurrent = new Concurrent();
		//concurrent.setDifferentiationCriteria();
		assertTrue(ficheConcours.addConcurrent(concurrent, 0));
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#removeConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrent() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#excludeConcurrentOfTeam(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrentInTeam() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#placementConcurrents(int)}.
	 */
	@Test
	public void testPlacementConcurrents() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#placementConcurrent(org.concoursjeunes.Concurrent, org.concoursjeunes.Cible, int)}.
	 */
	@Test
	public void testPlacementConcurrent() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#nextConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testNextConcurrent() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#previousConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testPreviousConcurrent() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getNbCiblesLibre(int)}.
	 */
	@Test
	public void testGetNbCiblesLibre() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getOccupationCibles(int)}.
	 */
	@Test
	public void testGetOccupationCibles() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getPasDeTir(int)}.
	 */
	@Test
	public void testGetPasDeTir() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#save()}.
	 */
	@Test
	public void testSilentSave() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getClassement(int, int)}.
	 */
	@Test
	public void testGetClassement() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getClassementEquipe(int)}.
	 */
	@Test
	public void testGetClassementEquipe() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	@After
	public void tearDown() {
		//concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName());
	}
}
