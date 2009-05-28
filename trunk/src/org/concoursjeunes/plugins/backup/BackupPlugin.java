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
package org.concoursjeunes.plugins.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.io.FileUtils;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Configuration;
import org.concoursjeunes.Profile;
import org.concoursjeunes.plugins.Plugin;
import org.concoursjeunes.plugins.PluginEntry;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@Plugin(type = Plugin.Type.ON_DEMAND)
public class BackupPlugin {
	
	private Profile profile;
	private AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.backup.BackupPlugin_libelle", BackupPlugin.class.getClassLoader()); //$NON-NLS-1$
	
	private JFrame parentframe;
	
	public BackupPlugin(JFrame parentframe, Profile profile) {
		this.parentframe = parentframe;
		this.profile = profile;
	}
	
	@PluginEntry
	public void showBackupDialog() {
		Configuration configuration = profile.getConfiguration();
		
		File concoursPath = ApplicationCore.userRessources.getConcoursPathForProfile(profile);
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    		pluginLocalisation.getResourceString("description.type.file"), "backup"); //$NON-NLS-1$ //$NON-NLS-2$
	    chooser.setFileFilter(filter);
	    chooser.setSelectedFile(new File(configuration.getCurProfil() + ".backup")); //$NON-NLS-1$
	   // chooser.setFileView(FileSystemView.getFileSystemView().);
	    int returnVal = chooser.showSaveDialog(parentframe);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {       

			try {
				List<File> concoursFiles = FileUtils.listAllFiles(concoursPath, ".*\\.cta", false); //$NON-NLS-1$
				
				File tempJar = File.createTempFile("profilecj_", ".jar"); //$NON-NLS-1$ //$NON-NLS-2$
				
				JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempJar));
				
				for(File concoursFile : concoursFiles) {
					addEntryToJar(concoursFile, jos);
				}
				
				addEntryToJar(new File(ApplicationCore.userRessources.getConfigPathForUser(), "configuration.xml"), jos); //$NON-NLS-1$
				
				jos.close();
				
				File backupFile = chooser.getSelectedFile();
				if(!backupFile.getName().endsWith(".backup")) //$NON-NLS-1$
					backupFile = new File(backupFile.getParent(), backupFile.getName() + ".backup"); //$NON-NLS-1$
				
				Packer packer = Pack200.newPacker();
				packer.pack(new JarFile(tempJar), new GZIPOutputStream(new FileOutputStream(backupFile)));

				tempJar.delete();
				
				JOptionPane.showMessageDialog(parentframe, pluginLocalisation.getResourceString("backupdialog.success")); //$NON-NLS-1$
			} catch (IOException e) {
				e.printStackTrace();
				JXErrorPane.showDialog(parentframe, new ErrorInfo(profile.getLocalisation().getResourceString("erreur"), //$NON-NLS-1$
						e.toString(),
						null, null, e, Level.SEVERE, null));
			}
	    }
	}
	
	private void addEntryToJar(File fileEntry, JarOutputStream jos)
			throws IOException {
		ZipEntry ze = new ZipEntry(fileEntry.getName());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileEntry));
		jos.putNextEntry(ze);
		
		byte[] buffer = new byte[512 * 1024];
		int nbLecture;
		while ((nbLecture = bis.read(buffer)) != -1) {
			jos.write(buffer, 0, nbLecture);
		}
		bis.close();
		
		//jos.finish();
		jos.closeEntry();
	}
}
