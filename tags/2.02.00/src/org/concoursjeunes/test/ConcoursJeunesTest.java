package org.concoursjeunes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.naming.ConfigurationException;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ConcoursJeunesEvent;
import org.concoursjeunes.ConcoursJeunesListener;
import org.concoursjeunes.FicheConcours;
import org.junit.Before;
import org.junit.Test;

/**
 * @author  Aurélien JEOFFRAY
 */
public class ConcoursJeunesTest {
	
	ConcoursJeunes concoursJeunes;
	
	private boolean eventReceived = false;

	@Before
	public void setUp() throws Exception {
		concoursJeunes = ConcoursJeunes.getInstance();
	}

	@Test
	public void testConcoursJeunes() {
		//test que ConcoursJeunes accédent correctement aux fichiers
		//ressources
		assertNotNull(ConcoursJeunes.ajrLibelle);
		assertEquals(ConcoursJeunes.ajrLibelle.getResourceString("onglet.gestionarcher"), "Gestion des archers"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(ConcoursJeunes.ajrParametreAppli);
		assertEquals(ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources"), "./ressources"); //$NON-NLS-1$ //$NON-NLS-2$
		
		//test l'accès au fichier de config
		assertNotNull(ConcoursJeunes.configuration);
		//test l'accès aux ressources utilisateur
		assertNotNull(ConcoursJeunes.userRessources);
		if(System.getProperty("os.name").startsWith("Windows")) //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(ConcoursJeunes.userRessources.getUserPath(), System.getenv("APPDATA") + File.separator + ConcoursJeunes.NOM); //$NON-NLS-1$
		else
			assertEquals(ConcoursJeunes.userRessources.getUserPath(), System.getProperty("user.home") + File.separator + "." + ConcoursJeunes.NOM); //$NON-NLS-1$ //$NON-NLS-2$
		
		//test la base de donnée
		assertNotNull(ConcoursJeunes.dbConnection);
		try {
			assertFalse(ConcoursJeunes.dbConnection.isClosed());
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
				
				String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
				
				assertTrue(new File(concourspath + File.separator 
						+ e.getFicheConcours().getParametre().getSaveName()).exists());
				
				try {
					concoursJeunes.closeFicheConcours(e.getFicheConcours());
					concoursJeunes.deleteFicheConcours(e.getFicheConcours().getMetaDataFicheConcours());
				} catch (ConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) { }
		};
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (ConfigurationException e1) {
			fail(e1.getLocalizedMessage());
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
				} catch (ConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) { }
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) {
				eventReceived = true;

				String concourspath = ConcoursJeunes.userRessources.getConcoursPathForProfile(ConcoursJeunes.configuration.getCurProfil());
				
				assertFalse("Le fichier du concours ne devrait plus exister",  //$NON-NLS-1$
						new File(concourspath + File.separator + testConcours.getParametre().getSaveName()).exists());
			}
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (ConfigurationException e1) {
			fail(e1.getLocalizedMessage());
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
				} catch (ConfigurationException e1) {
					fail(e1.getLocalizedMessage());
					e1.printStackTrace();
				}
			}
			public void ficheConcoursClosed(ConcoursJeunesEvent e) {
				eventReceived = true;
			}
			public void ficheConcoursRestored(ConcoursJeunesEvent e) { }
			public void ficheConcoursDeleted(ConcoursJeunesEvent e) { }
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (ConfigurationException e1) {
			fail(e1.getLocalizedMessage());
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
				} catch (ConfigurationException e1) {
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
		};
		
		concoursJeunes.addConcoursJeunesListener(auditeur);
		
		try {
			concoursJeunes.createFicheConcours();
		} catch (ConfigurationException e1) {
			fail(e1.getLocalizedMessage());
			e1.printStackTrace();
		}
		
		assertTrue("Aucun evenements de restauration reçue", eventReceived); //$NON-NLS-1$
		
		concoursJeunes.removeConcoursJeunesListener(auditeur);
		
		concoursJeunes = null;
	}
}