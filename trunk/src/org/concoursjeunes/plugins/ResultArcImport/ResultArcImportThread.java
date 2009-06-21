package org.concoursjeunes.plugins.ResultArcImport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.sql.SqlParser;
import org.concoursjeunes.ApplicationCore;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Plugin d'import d'une base Result'Arc (Format Windev HF) vers ConcoursJeunes
 * 
 * @author Aurélien JEOFFRAY
 */

public class ResultArcImportThread extends Thread {

	private JDialog parentframe;
	private AjResourcesReader localisation;

	private final AjResourcesReader pluginRessources = new AjResourcesReader("properties.FFTAImportPlugin"); //$NON-NLS-1$
	private final AjResourcesReader pluginLocalisation = new AjResourcesReader("org.concoursjeunes.plugins.ResultArcImport.ResultArcImportPlugin_libelle", ResultArcImportThread.class.getClassLoader()); //$NON-NLS-1$
	private final EventListenerList listeners = new EventListenerList();

	private String fftalogpath = ""; //$NON-NLS-1$

	/**
	 * 
	 * 
	 */
	public ResultArcImportThread(AjResourcesReader localisation) {
		this.setName("ResultArcImportThread"); //$NON-NLS-1$
		this.localisation = localisation;
	}

	public void addFFTAImportThreadListener(ResultArcImportThreadListener listener) {
		listeners.add(ResultArcImportThreadListener.class, listener);
	}

	public void removeFFTAImportThreadListener(ResultArcImportThreadListener listener) {
		listeners.remove(ResultArcImportThreadListener.class, listener);
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

	private void fireProgressionInfo(String info) {
		for (ResultArcImportThreadListener listener : listeners.getListeners(ResultArcImportThreadListener.class)) {
			listener.progressionInfo(info);
		}
	}

	private void fireImportFinished() {
		for (ResultArcImportThreadListener listener : listeners.getListeners(ResultArcImportThreadListener.class)) {
			listener.importFinished();
		}
	}
}
