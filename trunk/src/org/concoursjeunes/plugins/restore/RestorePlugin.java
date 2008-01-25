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
package org.concoursjeunes.plugins.restore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;
import java.util.zip.ZipEntry;

import javax.naming.ConfigurationException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.ConfigurationManager;
import org.concoursjeunes.MetaDataFicheConcours;
import org.concoursjeunes.plugins.Plugin;
import org.concoursjeunes.plugins.PluginEntry;
import org.jdesktop.swingx.JXErrorDialog;

import ajinteractive.standard.common.AjResourcesReader;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Plugin(type = Plugin.Type.ON_DEMAND)
public class RestorePlugin {
	
	private AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.restore.RestorePlugin_libelle"); //$NON-NLS-1$
	
	private JFrame parentframe;
	
	public RestorePlugin(JFrame parentframe) {
		this.parentframe = parentframe;
	}
	
	@PluginEntry
	public void showRestoreDialog() {
		Configuration configuration = ConcoursJeunes.getConfiguration();
		
		String concoursPath = ConcoursJeunes.userRessources.getConcoursPathForProfile(configuration.getCurProfil());
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    		pluginLocalisation.getResourceString("description.type.file"), "backup"); //$NON-NLS-1$ //$NON-NLS-2$
	    chooser.setFileFilter(filter);
	    //chooser.setSelectedFile(new File(configuration.getCurProfil() + ".backup"));
	    //chooser.setFileView(FileSystemView.getFileSystemView().);
	    int returnVal = chooser.showOpenDialog(parentframe);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {       

			try {
				Unpacker unpack = Pack200.newUnpacker();

				File tempJar = File.createTempFile("profilecj_", ".jar"); //$NON-NLS-1$ //$NON-NLS-2$
				
				JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempJar));
				
				unpack.unpack(chooser.getSelectedFile(), jos);
				jos.close();
				
				File configFile = null;
				
				JarInputStream jis = new JarInputStream(new FileInputStream(tempJar));
				ZipEntry ze;
				FileOutputStream fos;
				while((ze = jis.getNextEntry()) != null) {
					File destination;
					if(ze.getName().startsWith("configuration")) { //$NON-NLS-1$
						destination = new File(System.getProperty("java.io.tmpdir"), ze.getName()); //$NON-NLS-1$
						configFile = destination;
					} else {
						destination = new File(concoursPath, ze.getName());
					}
					fos = new FileOutputStream(destination);
					
					byte[] buffer = new byte[512*1024];
		            int nbLecture;
		            while((nbLecture = jis.read(buffer)) != -1 ) {
		                fos.write(buffer, 0, nbLecture);
		            }

					fos.close();
				}
				jis.close();

				tempJar.delete();
				
				if(configFile != null) {
					Configuration restoredConfiguration = ConfigurationManager.loadConfiguration(configFile);
					if(restoredConfiguration != null) {
						if(restoredConfiguration.getCurProfil().equals(configuration.getCurProfil())) {
							try {
								ConcoursJeunes.getInstance().closeAllFichesConcours();
								
								//ConcoursJeunes.configuration.save();
							} catch (ConfigurationException e1) {
								JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
										e1);
								e1.printStackTrace();
							}
							
							for(MetaDataFicheConcours metaDataFicheConcours : configuration.getMetaDataFichesConcours().getFiches()) {
								if(!restoredConfiguration.getMetaDataFichesConcours().contains(metaDataFicheConcours))
									restoredConfiguration.getMetaDataFichesConcours().add(metaDataFicheConcours);
							}
							
							ConcoursJeunes.setConfiguration(restoredConfiguration);
							restoredConfiguration.saveAsDefault();
						}
						
						restoredConfiguration.save();
					}
				}
			} catch (IOException e) {
				JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.toString(), //$NON-NLS-1$
						e);
				e.printStackTrace();
			}
	    }
	}
}
