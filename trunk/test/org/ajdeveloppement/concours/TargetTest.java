/*
 * Créer le 20 mars 08 à 18:57:01 pour ArcCompetition
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
 *  any later version.
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
package org.ajdeveloppement.concours;

import junit.framework.TestCase;

import org.ajdeveloppement.concours.ApplicationCore;
import org.ajdeveloppement.concours.Concurrent;
import org.ajdeveloppement.concours.Reglement;
import org.ajdeveloppement.concours.Target;
import org.ajdeveloppement.concours.Target.Repartition;
import org.ajdeveloppement.concours.builders.ConcurrentBuilder;
import org.ajdeveloppement.concours.exceptions.PlacementException;
import org.ajdeveloppement.concours.managers.ReglementManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@SuppressWarnings("nls")
public class TargetTest extends TestCase {

	Reglement reglement;
	Target targetVide;
	Target target1Archer;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	@Override
	public void setUp() throws Exception {
		ApplicationCore.initializeApplication(); // initialise le noyau
		
		ReglementManager reglementManager = ReglementManager.getInstance();
		
		
		reglement = reglementManager.getReglementByName("Savoie"); //charge le réglement savoie //$NON-NLS-1$
		Concurrent concurrent = ConcurrentBuilder.getConcurrent(reglement);
		
		targetVide = new Target(1, reglement, 4);
		target1Archer = new Target(1, reglement, 4);
		target1Archer.setConcurrentAt(concurrent, 0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	@Override
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getNbArcher()}.
	 */
	@Test
	public void testGetNbArcher() {
		assertTrue("Il devrait y avoir un archer", target1Archer.getNbArcher() == 1); //$NON-NLS-1$
		try {
			Concurrent concurrent = ConcurrentBuilder.getConcurrent(reglement);
			target1Archer.insertConcurrent(concurrent, Repartition.ABCD);
			assertTrue("Il devrait y avoir 2 archer", target1Archer.getNbArcher() == 2); //$NON-NLS-1$
			target1Archer.removeConcurrent(concurrent);
			assertTrue("Il devrait y avoir un archer", target1Archer.getNbArcher() == 1); //$NON-NLS-1$
			target1Archer.removeAll();
			assertTrue("Il devrait y avoir un archer", target1Archer.getNbArcher() == 0); //$NON-NLS-1$
		} catch (PlacementException e) {
			e.printStackTrace();
			fail("Une erreur de placement est survenue"); //$NON-NLS-1$
		}
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getNbArcherFor(org.ajdeveloppement.concours.DistancesEtBlason)}.
	 */
	@Test
	public void testGetNbArcherFor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getNbHandicap()}.
	 */
	@Test
	public void testGetNbHandicap() {
		assertTrue("Il devrait y avoir aucun archer handicape", target1Archer.getNbHandicap() == 0); //$NON-NLS-1$
		try {
			Concurrent concurrent = ConcurrentBuilder.getConcurrent(reglement);
			concurrent.setHandicape(true);
			target1Archer.insertConcurrent(concurrent, Repartition.ABCD);
			assertTrue("Il devrait y avoir 1 archer handicape", target1Archer.getNbHandicap() == 1); //$NON-NLS-1$
			target1Archer.removeConcurrent(concurrent);
			assertTrue("Il devrait y avoir aucun archer handicape", target1Archer.getNbHandicap() == 0); //$NON-NLS-1$$
		} catch (PlacementException e) {
			e.printStackTrace();
			fail("Une erreur de placement est survenue"); //$NON-NLS-1$
		}
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getNbAvailableSlotsFor(org.ajdeveloppement.concours.DistancesEtBlason)}.
	 */
	@Test
	public void testGetNbAvailableSlotsFor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#isReservedPosition(int)}.
	 */
	@Test
	public void testIsReservedPosition() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#insertConcurrent(org.ajdeveloppement.concours.Concurrent, org.ajdeveloppement.concours.Target.Repartition)}.
	 */
	@Test
	public void testInsertConcurrentConcurrentRepartition() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#insertConcurrent(org.ajdeveloppement.concours.Concurrent, org.ajdeveloppement.concours.Target.Repartition, boolean)}.
	 */
	@Test
	public void testInsertConcurrentConcurrentRepartitionBoolean() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#isSlotAvailable(org.ajdeveloppement.concours.Blason, int)}.
	 */
	@Test
	public void testIsSlotAvailable() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getConcurrentAt(int)}.
	 */
	@Test
	public void testGetConcurrentAt() {
		Concurrent concurrent = ConcurrentBuilder.getConcurrent(reglement);
		concurrent.setName("Archers"); //$NON-NLS-1$
		try {
			target1Archer.setConcurrentAt(concurrent, 3);
		} catch (PlacementException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertTrue(target1Archer.getConcurrentAt(3) == concurrent);
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#indexOf(org.ajdeveloppement.concours.Concurrent)}.
	 */
	@Test
	public void testIndexOf() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		
		assertTrue(target1Archer.indexOf(concurrent) == 0);
		
		Concurrent concurrent2 = ConcurrentBuilder.getConcurrent(reglement);
		concurrent2.setName("Archers 2"); //$NON-NLS-1$
		try {
			target1Archer.setConcurrentAt(concurrent2, 3);
		} catch (PlacementException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(target1Archer.indexOf(concurrent2) == 3);
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#contains(org.ajdeveloppement.concours.Concurrent)}.
	 */
	@Test
	public void testContains() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		
		assertTrue(target1Archer.contains(concurrent));
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#setConcurrentAt(org.ajdeveloppement.concours.Concurrent, int)}.
	 */
	@Test
	public void testSetConcurrentAt() {
		Concurrent concurrent = ConcurrentBuilder.getConcurrent(reglement);
		concurrent.setName("Archers 2"); //$NON-NLS-1$
		try {
			target1Archer.setConcurrentAt(concurrent, 3);
		} catch (PlacementException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertTrue(target1Archer.getConcurrentAt(3) == concurrent);
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#removeConcurrentAt(int)}.
	 */
	@Test
	public void testRemoveConcurrentAt() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		target1Archer.removeConcurrentAt(0);
		assertTrue("Il devrait y avoir 0 archer", target1Archer.getNbArcher() == 0); //$NON-NLS-1$
		assertTrue("Il devrait y avoir 0 archer", target1Archer.getNbHandicap() == 0); //$NON-NLS-1$
		assertTrue("Il ne doit y avoir aucun distance et blason associé", target1Archer.getDistancesEtBlason().size() == 0); //$NON-NLS-1$
		assertTrue("Le concurrent ne dvrait plus être associé à la cible", concurrent.getCible() == 0); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#removeConcurrent(org.ajdeveloppement.concours.Concurrent)}.
	 */
	@Test
	public void testRemoveConcurrent() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		target1Archer.removeConcurrent(concurrent);
		assertTrue("Le concurrent ne devrait plus être associé à la cible", concurrent.getCible() == 0); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#removeAll()}.
	 */
	@Test
	public void testRemoveAll() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		
		target1Archer.removeAll();
		assertTrue("Il devrait y avoir 0 archer", target1Archer.getNbArcher() == 0); //$NON-NLS-1$
		assertTrue("Il devrait y avoir 0 archer", target1Archer.getNbHandicap() == 0); //$NON-NLS-1$
		assertTrue("Il ne doit y avoir aucun distance et blason associé", target1Archer.getDistancesEtBlason().size() == 0); //$NON-NLS-1$
		assertTrue("Le concurrent ne devrait plus être associé à la cible", concurrent.getCible() == 0); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#removeAll(boolean)}.
	 */
	@Test
	public void testRemoveAllBoolean() {
		Concurrent concurrent = target1Archer.getConcurrentAt(0);
		assertTrue("Le concurrent doit avoir une cible affecté", concurrent.getCible() != 0); //$NON-NLS-1$
		target1Archer.removeAll(true);
		assertTrue("En mode simulation le retrait n'est pas vraiment effectif, le concurrent doit garder sa cible", concurrent.getCible() != 0); //$NON-NLS-1$
		try {
			target1Archer.insertConcurrent(concurrent, Repartition.ABCD);
			target1Archer.removeAll(false);
			assertTrue("le concurrent doit perdre sa cible", concurrent.getCible() == 0); //$NON-NLS-1$
		} catch (PlacementException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test method for {@link org.ajdeveloppement.concours.Target#getDistancesEtBlason()}.
	 */
	@Test
	public void testGetDistancesEtBlason() {
		fail("Not yet implemented");
	}
}
