package org.concoursjeunes.plugins.FFTAImport;

import java.io.*;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JDialog;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.SecureStringsStore;
import org.ajdeveloppement.commons.io.zip.EncryptedZipInputStream;
import org.ajdeveloppement.commons.sql.SqlParser;
import org.concoursjeunes.ApplicationCore;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Plugin d'import d'une base WinFFTA 2 (Format Windev HF) vers ConcoursJeunes (Format Java XML)
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
		this.setName("FFTAImportThread"); //$NON-NLS-1$
		this.localisation = localisation;
	}

	public void addFFTAImportThreadListener(FFTAImportThreadListener listener) {
		listeners.add(FFTAImportThreadListener.class, listener);
	}

	public void removeFFTAImportThreadListener(FFTAImportThreadListener listener) {
		listeners.remove(FFTAImportThreadListener.class, listener);
	}

	/**
	 * Definit l'objet parent auquel est rattaché le plugin
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
		
		if(pluginRessources.getResourceString("plugin.mode").equals("FTP"))
			fftaFTPLoader();
		else
			fftaLoader();
		
		fireImportFinished();
	}

	/**
	 * Chargement à partir des exports FFTA
	 * 
	 */
	private void fftaLoader() {
		try {
			if (System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
				fireProgressionInfo(pluginLocalisation.getResourceString("progress.export")); //$NON-NLS-1$
				Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir") //$NON-NLS-1$
						+ File.separator + pluginRessources.getResourceString("winffta.export.cmd", fftalogpath, System.getProperty("java.io.tmpdir"))); //$NON-NLS-1$ //$NON-NLS-2$
				proc.waitFor();
			}

			fireProgressionInfo(pluginLocalisation.getResourceString("progress.clean")); //$NON-NLS-1$
			// HACK problème retour caractère \r dans la table hyperfile FICLUB.FIC
			// charge tous le fichier en mémoire
			FileReader frficlub = new FileReader(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
					+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
			StringBuffer sbuffer = new StringBuffer();
			char[] buffer = new char[128];
			int dataSize = 0;
			while ((dataSize = frficlub.read(buffer)) > -1) {
				sbuffer.append(buffer, 0, dataSize);
			}
			frficlub.close();
			buffer = null;

			String sFiclub = sbuffer.toString();
			sbuffer = null;

			// supprime le caractère foireux
			sFiclub = sFiclub.replaceAll("\\r;", ";"); //$NON-NLS-1$ //$NON-NLS-2$

			// réimprime le fichier
			PrintStream psficlub = new PrintStream(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
					+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
			psficlub.print(sFiclub);
			psficlub.close();
			sFiclub = null;
			// FIN HACK

			Statement stmt = ApplicationCore.dbConnection.createStatement();

			Hashtable<String, String> ht = new Hashtable<String, String>();

			ht.put("temp", System.getProperty("java.io.tmpdir").replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

			SqlParser.createBatch(new File(pluginRessources.getResourceString("sql.importffta")), stmt, ht); //$NON-NLS-1$

			fireProgressionInfo(pluginLocalisation.getResourceString("progress.integration")); //$NON-NLS-1$
			stmt.executeBatch();

			stmt.close();

		} catch (InterruptedException e1) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e1, Level.SEVERE, null));
			e1.printStackTrace();

		} catch (IOException io) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), io.getLocalizedMessage(), //$NON-NLS-1$
					null, null, io, Level.SEVERE, null));
			io.printStackTrace();

		} catch (NullPointerException npe) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), npe.getLocalizedMessage(), //$NON-NLS-1$
					null, null, npe, Level.SEVERE, null));
			npe.printStackTrace();
		} catch (OutOfMemoryError oome) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), oome.getLocalizedMessage(), //$NON-NLS-1$
					null, null, oome, Level.SEVERE, null));
			oome.printStackTrace();
		} catch (SQLException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		}
	}
	
	private void fftaFTPLoader() {
		try {
			SecureStringsStore secureStringsStore = new SecureStringsStore();
			secureStringsStore.loadKey(
					new File(ApplicationCore.staticParameters.getResourceString("path.ressources"), "security/keys/default.key"));
			secureStringsStore.load(
					new FileReader(new File(ApplicationCore.staticParameters.getResourceString("path.config"), "ffta.properties")));
			URL ftpFFTA = new URL("file:///d:/result_data.zip"/*secureStringsStore.get("ffta.ftp.url")*/);
			EncryptedZipInputStream ezis = new EncryptedZipInputStream(ftpFFTA.openStream());
			ezis.setEncryptedPassword(secureStringsStore.get("ffta.zip.password=").getBytes());
			
			byte[] buffer = new byte[2048];
			ZipEntry entry;
            while((entry = ezis.getNextEntry())!=null) {
            	 String outpath = System.getProperty("java.io.tmpdir") + "/" + entry.getName();
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
			
			new File(System.getProperty("java.io.tmpdir"), "result_club.txt").delete();
			new File(System.getProperty("java.io.tmpdir"), "result_licence.txt").delete();

			stmt.close();
		} catch (NoSuchAlgorithmException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} /*catch (InvalidKeyException e) {
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
		}*/ catch (IOException e) {
			JXErrorPane.showDialog(parentframe, new ErrorInfo(localisation.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
					null, null, e, Level.SEVERE, null));
			e.printStackTrace();
		} catch (SQLException e) {
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
