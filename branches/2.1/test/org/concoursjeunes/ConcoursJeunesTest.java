/*
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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
import java.sql.SQLException;

import junit.framework.TestCase;

import org.concoursjeunes.event.ConcoursJeunesEvent;
import org.concoursjeunes.event.ConcoursJeunesListener;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author  Aurélien JEOFFRAY
 */
public class ConcoursJeunesTest extends TestCase {
	
	ApplicationCore concoursJeunes;
	
	private boolean eventReceived = false;

	@Before
	@Override
	public void setUp() throws Exception {
		concoursJeunes = ApplicationCore.getInstance();
	}

	@Test
	public void testConcoursJeunes() {
		//test que ConcoursJeunes accédent correctement aux fichiers
		//ressources
		assertNotNull(ApplicationCore.ajrLibelle);
		assertEquals(ApplicationCore.ajrLibelle.getResourceString("onglet.gestionarcher"), "Gestion des archers"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(ApplicationCore.ajrParametreAppli);
		assertEquals(ApplicationCore.ajrParametreAppli.getResourceString("path.ressources"), "ressources"); //$NON-NLS-1$ //$NON-NLS-2$
		
		//test l'accès au fichier de config
		assertNotNull(ApplicationCore.getConfiguration());
		//test l'accès aux ressources utilisateur
		assertNotNull(ApplicationCore.userRessources);
		if(System.getProperty("os.name").startsWith("Windows")) //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ApplicationCore.userRessources.getUserPath(), System.getenv("APPDATA") + File.separator + ApplicationCore.NOM); //$NON-NLS-1$
		else
			assertEquals(ApplicationCore.userRessources.getUserPath(), System.getProperty("user.home") + File.separator + "." + ApplicationCore.NOM); //$NON-NLS-1$ //$NON-NLS-2$
		
		//test la base de donnée
		assertNotNull(ApplicationCore.dbConnection);
		try {
			assertFalse(ApplicationCore.dbConnection.isClosed());
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateFicheConcours() {
		eventReceived = false;

		ConcoursJeunesListener auditeur = new ConcoursJeunesListener() {
			public void ficheConcoursCreated(ConcoursJeunesEvent e) {
				
				eventReceived = true;
				
				assertNotNull(e.getFicheConcours());
				
				File concourspath = ApplicationCore.userRessources.getConcoursPathForProfile(ApplicationCore.getConfiguration().getCurProfil());
				
				assertTrue(new File(concourspath + File.separator 
						+ e.getFicheConcours().getParametre().getSaveName()).exists());
				
				try {
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.deleteFicheConcours(e.getFicheConcours().getMetaDataFicheConcours());
				} catch (NullConfigurationException e1) {
					fail(e1.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					fail(e1.toString());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) { }
			public void configurationChanged(ConcoursJeunesEvent e) { }
		};
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (NullConfigurationException e1) {
			fail(e1.getLocalizedMessage());
			e1.printStackTrace();
		} catch (IOException e1) {
			fail(e1.toString());
			e1.printStackTrace();
		}
		//marche à condition que la gestion evenementiel soit synchrone
		assertTrue("Aucun evenements de création reçue", eventReceived); //$NON-NLS-1$
		concoursJeunes.removeConcoursJeunesListener(auditeur);
		
		concoursJeunes = null;
	}

	@Test
	public void testDeleteFicheConcours() {
		
		eventReceived = false;
		
		ConcoursJeunesListener auditeur = new ConcoursJeunesListener() {
			FicheConcours testConcours; 
			
			public void ficheConcoursCreated(ConcoursJeunesEvent e) {
				testConcours = e.getFicheConcours();
				try {
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.deleteFicheConcours(
							e.getFicheConcours().getMetaDataFicheConcours());
				} catch (NullConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				} catch (IOException e1) {
					fail(e1.toString());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) {
				eventReceived = true;

				File concourspath = ApplicationCore.userRessources.getConcoursPathForProfile(ApplicationCore.getConfiguration().getCurProfil());
				
				assertFalse("Le fichier du concours ne devrait plus exister",  //$NON-NLS-1$
						new File(concourspath, testConcours.getParametre().getSaveName()).exists());
			}
			public void configurationChanged(ConcoursJeunesEvent e) { }
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (NullConfigurationException e1) {
			fail(e1.getLocalizedMessage());
			e1.printStackTrace();
		} catch (IOException e1) {
			fail(e1.toString());
			e1.printStackTrace();
		}
		
		assertTrue("Aucun evenements de suppression reçue", eventReceived); //$NON-NLS-1$
		
		concoursJeunes.removeConcoursJeunesListener(auditeur);
		
		concoursJeunes = null;
	}

	@Test
	public void testCloseFicheConcours() {
		eventReceived = false;
		
		ConcoursJeunesListener auditeur = new ConcoursJeunesListener() {
			public void ficheConcoursCreated(ConcoursJeunesEvent e) {
				try {
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.deleteFicheConcours(
							e.getFicheConcours().getMetaDataFicheConcours());
				} catch (NullConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				} catch (IOException e1) {
					fail(e1.toString());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) {
				eventReceived = true;
			}
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) { }
			public void configurationChanged(ConcoursJeunesEvent e) { }
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (NullConfigurationException e1) {
			fail(e1.getLocalizedMessage());
			e1.printStackTrace();
		} catch (IOException e1) {
			fail(e1.toString());
			e1.printStackTrace();
		}
		
		assertTrue("Aucun evenements de fermeture reçue", eventReceived); //$NON-NLS-1$
		
		concoursJeunes.removeConcoursJeunesListener(auditeur);
		
		concoursJeunes = null;
	}
	
	@Test
	public void testRestoreFicheConcours() {
		eventReceived = false;
		
		ConcoursJeunesListener auditeur = new ConcoursJeunesListener() {
			public void ficheConcoursCreated(ConcoursJeunesEvent e) {
				try {
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.restoreFicheConcours(e.getFicheConcours().getMetaDataFicheConcours());
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.deleteFicheConcours(
							e.getFicheConcours().getMetaDataFicheConcours());
				} catch (NullConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				} catch (IOException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent e) {
				eventReceived = true;
				
				assertNotNull("la fiche concours n'est pas chargé correctement", e.getFicheConcours()); //$NON-NLS-1$
			}
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) { }
			public void configurationChanged(ConcoursJeunesEvent e) { }
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (NullConfigurationException e1) {
			fail(e1.getLocalizedMessage());
			e1.printStackTrace();
		} catch (IOException e1) {
			fail(e1.toString());
			e1.printStackTrace();
		}
		
		assertTrue("Aucun evenements de restauration reçue", eventReceived); //$NON-NLS-1$
		
		concoursJeunes.removeConcoursJeunesListener(auditeur);
		
		concoursJeunes = null;
	}
}