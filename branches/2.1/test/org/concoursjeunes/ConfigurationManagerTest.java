/*
 * Créer le 23 déc. 07 à 11:42:29 pour ConcoursJeunes
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

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.concoursjeunes.exceptions.NullConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ajinteractive.standard.utilities.io.FileUtils;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ConfigurationManagerTest extends TestCase {

	//ConcoursJeunes engine;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	@Override
	public void setUp() throws Exception {
		//engine = ConcoursJeunes.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	@Override
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.concoursjeunes.ConfigurationManager#loadCurrentConfiguration()}.
	 */
	@Test
	public void testLoadCurrentConfiguration() {
		try {
			assertNotNull(ConfigurationManager.loadCurrentConfiguration());
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.concoursjeunes.ConfigurationManager#loadConfiguration(java.lang.String)}.
	 */
	@Test
	public void testLoadConfigurationString() {
		try {
			assertNotNull(ConfigurationManager.loadConfiguration("defaut")); //$NON-NLS-1$
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.concoursjeunes.ConfigurationManager#loadConfiguration(java.io.File)}.
	 */
	@Test
	public void testLoadConfigurationFile() {
		try {
			assertNotNull(ConfigurationManager.loadConfiguration(new File(ApplicationCore.userRessources.getConfigPathForUser() 
					+ File.separator + "configuration_defaut.xml"))); //$NON-NLS-1$
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.concoursjeunes.ConfigurationManager#renameConfiguration(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRenameConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setCurProfil("test_rename_orig"); //$NON-NLS-1$
		configuration.save();
		
		ApplicationCore concoursJeunes = ApplicationCore.getInstance();
		
		String actualProfile = ApplicationCore.getConfiguration().getCurProfil();
		ApplicationCore.getConfiguration().save();
		
		configuration.saveAsDefault();
		ApplicationCore.setConfiguration(configuration);
		
		
		/*concoursJeunes.addConcoursJeunesListener(new ConcoursJeunesListener() {
			public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent) {
				concoursJeunesEvent.getFicheConcours().getMetaDataFicheConcours().
			}
			public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent) { }
			public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent) { }
		});*/
		try {
			concoursJeunes.createFicheConcours();
			concoursJeunes.saveAllFichesConcours();
			concoursJeunes.closeAllFichesConcours();
			
			assertFalse("Il est interdit de renommer le profil par defaut", ConfigurationManager.renameConfiguration("defaut", "test_rename")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			//test l'absence d'erreur dans l'etat d'origine
			assertTrue(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename_orig").exists()); //$NON-NLS-1$
			
			assertTrue(ConfigurationManager.renameConfiguration("test_rename_orig", "test_rename")); //$NON-NLS-1$ //$NON-NLS-2$
			assertTrue(new File(ApplicationCore.userRessources.getConfigPathForUser(), "configuration_test_rename.xml").exists()); //$NON-NLS-1$
			assertFalse(new File(ApplicationCore.userRessources.getConfigPathForUser(), "configuration_test_rename_orig.xml").exists()); //$NON-NLS-1$
			
			assertTrue(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename").exists()); //$NON-NLS-1$
			assertFalse(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename_orig").exists()); //$NON-NLS-1$
			assertTrue(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename").listFiles().length > 0); //$NON-NLS-1$
		} catch (NullConfigurationException e) {
			fail(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
		
		try {
			configuration = ConfigurationManager.loadConfiguration(actualProfile);
			ApplicationCore.setConfiguration(configuration);
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
		
		assertTrue(new File(ApplicationCore.userRessources.getConfigPathForUser(), "configuration_test_rename.xml").delete()); //$NON-NLS-1$
		try {
			FileUtils.deleteFilesPath(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename")); //$NON-NLS-1$
			assertFalse(new File(ApplicationCore.userRessources.getConfigPathForUser(), "Profile/test_rename").exists()); //$NON-NLS-1$
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

}
