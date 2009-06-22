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
				JOptionPane.showMessageDialog(parentframe, localisation.getResourceString("erreur.nokey"), "", JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			
			SecureProperties secureProperties = new SecureProperties(key);

			secureProperties.load(
					new FileReader(new File(ApplicationCore.staticParameters.getResourceString("path.config"), "ffta.properties"))); //$NON-NLS-1$ //$NON-NLS-2$
			URL ftpFFTA;
			if(fftalogpath.isEmpty())
				ftpFFTA = new File(fftalogpath).toURI().toURL();
			else
				ftpFFTA = new URL(secureProperties.get("ffta.ftp.url")); //$NON-NLS-1$
			EncryptedZipInputStream ezis = new EncryptedZipInputStream(ftpFFTA.openStream());
			ezis.setEncryptedPassword(secureProperties.get("ffta.zip.password").getBytes()); //$NON-NLS-1$
			
			byte[] buffer = new byte[2048];
			ZipEntry entry;
            while((entry = ezis.getNextEntry())!=null) {
            	 String outpath = System.getProperty("java.io.tmpdir") + "/" + entry.getName(); //$NON-NLS-1$ //$NON-NLS-2$
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
