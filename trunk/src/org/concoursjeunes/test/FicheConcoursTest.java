/**
 * 
 */
package org.concoursjeunes.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Aurélien JEOFFRAY
 * TODO revoir les test avec creation de concours
 */
public class FicheConcoursTest extends TestCase {
	
	private ConcoursJeunes concoursJeunes;
	private FicheConcours ficheConcours;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	@Override
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
			public void configurationChanged(ConcoursJeunesEvent concoursJeunesEvent) {}
			
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
		try {
			assertTrue(ficheConcours.addConcurrent(concurrent, 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#removeConcurrent(org.concoursjeunes.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrent() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	/**
	 * Méthode de test pour {@link org.concoursjeunes.FicheConcours#getPasDeTir(int)}.
	 */
	@Test
	public void testGetPasDeTir() {
		fail("Non implémenté actuellement"); //$NON-NLS-1$
	}

	@After
	@Override
	public void tearDown() {
		//concoursJeunes.deleteFicheConcours(ficheConcours.getParametre().getSaveName());
	}
}
