/*
 * Créer le 29 déc. 07 à 16:58:55 pour ConcoursJeunes
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
package org.concoursjeunes.plugins.restore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.Profile;
import org.concoursjeunes.exceptions.NullConfigurationException;
import org.concoursjeunes.plugins.Plugin;
import org.concoursjeunes.plugins.PluginEntry;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Plugin(type = Plugin.Type.ON_DEMAND)
public class RestorePlugin {
	
	private AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.restore.RestorePlugin_libelle", RestorePlugin.class.getClassLoader()); //$NON-NLS-1$
	
	private JFrame parentframe;
	private Profile profile;
	
	public RestorePlugin(JFrame parentframe, Profile profile) {
		this.parentframe = parentframe;
		this.profile = profile;
	}
	
	@PluginEntry
	public void showRestoreDialog() {
		Configuration configuration = profile.getConfiguration();
		
		File tempPath;
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    		pluginLocalisation.getResourceString("description.type.file"), "backup"); //$NON-NLS-1$ //$NON-NLS-2$
	    chooser.setFileFilter(filter);

	    int returnVal = chooser.showOpenDialog(parentframe);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {       

			try {
				//On transforme le pack200 en jar
				//On décompresse le contenue du jar dans un répertoire temporaire
				tempPath = extractJarContent(unpack(chooser.getSelectedFile()));
				
				File configFile = new File(tempPath, "configuration.xml"); //$NON-NLS-1$
				
				if(configFile.exists()) {
					Profile restoredProfile = new Profile(configFile);
					//Configuration restoredConfiguration = ConfigurationManager.loadConfiguration(configFile);

					//si le profil à chargé est le profil courant, alors fermé et sauvegardé tous les concours ouvert
					//puis ajouté le contenue à la configuration courante
					if(restoredProfile.getConfiguration().getCurProfil().equals(configuration.getCurProfil())) {
						try {
							profile.closeAllFichesConcours();
						} catch (NullConfigurationException e) {
							JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
									null, null, e, Level.SEVERE, null));
							e.printStackTrace();
						} catch (XMLStreamException e) {
							JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
									null, null, e, Level.SEVERE, null));
							e.printStackTrace();
						}
						
						for(MetaDataFicheConcours metaDataFicheConcours : configuration.getMetaDataFichesConcours().getFiches()) {
							if(!restoredProfile.getConfiguration().getMetaDataFichesConcours().contains(metaDataFicheConcours))
								restoredProfile.getConfiguration().getMetaDataFichesConcours().add(metaDataFicheConcours);
						}
						
						profile.setConfiguration(restoredProfile.getConfiguration());
					}
					
					File concoursPath = ApplicationCore.userRessources.getConcoursPathForProfile(restoredProfile);
					for(File f : FileUtils.listAllFiles(tempPath, ".*")) { //$NON-NLS-1$
						if(!f.getName().equals("configuration.xml")) //$NON-NLS-1$
							FileUtils.copyFile(f, concoursPath, true);
						f.delete();
					}
					tempPath.delete();
					
					restoredProfile.getConfiguration().save();
					
					JOptionPane.showMessageDialog(parentframe, pluginLocalisation.getResourceString("restoredialog.success")); //$NON-NLS-1$
				}
			} catch (IOException e) {
				JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
						null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			} catch (JAXBException e) {
				JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
						null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			} catch (XMLStreamException e) {
				JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
						null, null, e, Level.SEVERE, null));
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * Convertie un fichier pack200 en fichier jar temporaire
	 * 
	 * @param packedFile le fichier pack200 à convertir
	 * @return le fichier jar temporaire
	 */
	private File unpack(File packedFile) {
		Unpacker unpack;
		File tempJar = null;
		InputStream is = null;
		JarOutputStream jos = null;
		
		try {
			unpack = Pack200.newUnpacker();
			tempJar = File.createTempFile("profilecj_", ".jar"); //$NON-NLS-1$ //$NON-NLS-2$
			jos = new JarOutputStream(new FileOutputStream(tempJar));
			is = new FileInputStream(packedFile);
			try {
				if (is.markSupported()) {
                    is.mark(2); //set marker for reset
                }
				is = new GZIPInputStream(is);
			} catch(IOException ioe) {
				if(is.markSupported())
					is.reset(); 
			}
			
			unpack.unpack(is, jos);
		} catch (FileNotFoundException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} finally {
			if(jos != null)
				try { jos.close(); } catch(IOException e) {};
			if(is != null)
				try { is.close(); } catch(IOException e) {};
			unpack = null;
			jos = null;
		}

		return tempJar;
	}
	
	/**
	 * Extrait les fichiers du jar dans un répertoire temporaire
	 * 
	 * @param jarFile le fichier jar à extraire
	 * @return le chemin contenant les fichiers extrait
	 */
	private File extractJarContent(File jarFile) {
		File extractedPath = new File(System.getProperty("java.io.tmpdir"), "profilecj_" + System.currentTimeMillis());  //$NON-NLS-1$//$NON-NLS-2$
		JarInputStream jis = null;
		ZipEntry ze;
		FileOutputStream fos = null;
		
		extractedPath.mkdirs();
		
		try {
			jis = new JarInputStream(new FileInputStream(jarFile));
			
			while((ze = jis.getNextEntry()) != null) {
				File destination = new File(extractedPath, ze.getName());
				try {
					fos = new FileOutputStream(destination);
					
					byte[] buffer = new byte[512*1024]; //bloc de 512Ko
				    int nbLecture;
				    while((nbLecture = jis.read(buffer)) != -1 ) {
				        fos.write(buffer, 0, nbLecture);
				    }
				} finally {
					try { fos.close(); } catch (IOException e) { }
				}
			}
		} catch (FileNotFoundException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), e.toString(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} finally {
			try { jis.close(); } catch (IOException e) { }
		}

		jarFile.delete();
		
		return extractedPath;
	}
}
