/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
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
package org.concoursjeunes.plugins.FFTAImport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.SecretKeyEntry;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.apps.AppUtilities;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.security.SecureProperties;
import org.ajdeveloppement.commons.sql.SqlParser;
import org.ajdeveloppement.io.zip.EncryptedZipInputStream;
import org.concoursjeunes.ApplicationCore;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Plugin d'import d'une base Result'Arc (Format Windev HF) vers ConcoursJeunes
 * 
 * @author Aurélien JEOFFRAY
 */

public class FFTAImportThread extends Thread {

	private JDialog parentframe;
	private AjResourcesReader localisation;

	private final AjResourcesReader pluginRessources = new AjResourcesReader("properties.FFTAImportPlugin"); //$NON-NLS-1$
	private final AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.FFTAImport.FFTAImportPlugin_libelle", FFTAImportThread.class.getClassLoader()); //$NON-NLS-1$
	private final EventListenerList listeners = new EventListenerList();

	private String fftalogpath = ""; //$NON-NLS-1$

	/**
	 * 
	 * 
	 */
	public FFTAImportThread(AjResourcesReader localisation) {
		this.setName("ResultArcImportThread"); //$NON-NLS-1$
		this.localisation = localisation;
	}

	public void addFFTAImportThreadListener(FFTAImportThreadListener listener) {
		listeners.add(FFTAImportThreadListener.class, listener);
	}

	public void removeFFTAImportThreadListener(FFTAImportThreadListener listener) {
		listeners.remove(FFTAImportThreadListener.class, listener);
	}

	/**
	 * Définit l'objet parent auquel est rattaché le plugin
	 * 
	 * @param parentframe la boite de dialogue rattaché au thread
	 */
	public void setParentFrame(JDialog parentframe) {
		this.parentframe = parentframe;
	}

	/**
	 * @return fftalogpath
	 */
	public String getFftalogpath() {
		return fftalogpath;
	}

	/**
	 * @param fftalogpath
	 *            fftalogpath à définir
	 */
	public void setFftalogpath(String fftalogpath) {
		this.fftalogpath = fftalogpath;
	}

	@Override
	public void run() {
		if (parentframe == null)
			return;
		
		fftaFTPLoader();

		fireImportFinished();
	}
	
	private void fftaFTPLoader() {
		try {
			SecretKeyEntry keyEntry = (SecretKeyEntry)ApplicationCore.userRessources.getAppKeyStore().getEntry("ffta",  //$NON-NLS-1$
					new KeyStore.PasswordProtection(AppUtilities.getAppUID(ApplicationCore.userRessources).toCharArray()));
			SecretKey key = null;
			if(keyEntry != null)
				key = keyEntry.getSecretKey();
			else {
				JOptionPane.showMessageDialog(parentframe, pluginLocalisation.getResourceString("erreur.nokey"), "", JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			
			SecureProperties secureProperties = new SecureProperties(key);

			secureProperties.load(
					new FileReader(new File(ApplicationCore.staticParameters.getResourceString("path.config"), "ffta.properties"))); //$NON-NLS-1$ //$NON-NLS-2$
			URL ftpFFTA;
			if(!fftalogpath.isEmpty())
				ftpFFTA = new File(fftalogpath, "result_data.zip").toURI().toURL();
			else
				ftpFFTA = new URL(secureProperties.get("ffta.ftp.url")); //$NON-NLS-1$
			EncryptedZipInputStream ezis = new EncryptedZipInputStream(ftpFFTA.openStream());
			ezis.setEncryptedPassword(secureProperties.get("ffta.zip.password").getBytes()); //$NON-NLS-1$
			
			byte[] buffer = new byte[2048];
			ZipEntry entry;
            while((entry = ezis.getNextEntry())!=null) {
            	String temppath = System.getProperty("java.io.tmpdir");
            	if(!temppath.endsWith("\\") && !temppath.endsWith("/"))
            		temppath += File.separator;
            	String outpath = temppath + entry.getName(); //$NON-NLS-1$ //$NON-NLS-2$
                FileOutputStream output = null;
                 
                output = new FileOutputStream(outpath);
                int len = 0;
                while ((len = ezis.read(buffer)) > 0) {
                    output.write(buffer, 0, len);
                }
            }
            
            Statement stmt = ApplicationCore.dbConnection.createStatement();

			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("temp", System.getProperty("java.io.tmpdir").replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

			SqlParser.createBatch(new File(pluginRessources.getResourceString("sql.importftpffta")), stmt, ht); //$NON-NLS-1$

			fireProgressionInfo(pluginLocalisation.getResourceString("progress.integration")); //$NON-NLS-1$
			stmt.executeBatch();
			
			new File(System.getProperty("java.io.tmpdir"), "result_club.txt").delete(); //$NON-NLS-1$ //$NON-NLS-2$
			new File(System.getProperty("java.io.tmpdir"), "result_licence.txt").delete(); //$NON-NLS-1$ //$NON-NLS-2$

			stmt.close();
		} catch (NoSuchAlgorithmException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IOException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (SQLException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (KeyStoreException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (BadPaddingException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
	}

	private void fireProgressionInfo(String info) {
		for (FFTAImportThreadListener listener : listeners.getListeners(FFTAImportThreadListener.class)) {
			listener.progressionInfo(info);
		}
	}

	private void fireImportFinished() {
		for (FFTAImportThreadListener listener : listeners.getListeners(FFTAImportThreadListener.class)) {
			listener.importFinished();
		}
	}
}
