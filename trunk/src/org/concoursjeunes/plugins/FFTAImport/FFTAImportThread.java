package org.concoursjeunes.plugins.FFTAImport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.event.EventListenerList;

import org.concoursjeunes.ConcoursJeunes;
import org.jdesktop.swingx.JXErrorDialog;

import ajinteractive.standard.java2.AjResourcesReader;
import ajinteractive.standard.utilities.sql.SqlParser;
/**
 * Plugin d'import d'une base WinFFTA 2 (Format Windev HF)
 * vers ConcoursJeunes (Format Java XML)
 *  
 * @author Aurélien JEOFFRAY
 */

public class FFTAImportThread extends Thread {
    
    private JDialog parentframe;
    
    private AjResourcesReader pluginRessources = new AjResourcesReader("properties.FFTAImportPlugin"); //$NON-NLS-1$
    private EventListenerList listeners = new EventListenerList();
    
    private String fftalogpath = ""; //$NON-NLS-1$
    
    /**
     * 
     *
     */
    public FFTAImportThread() {
    	this.setName("FFTAImportThread");
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
     * @param concoursJeunes - l'objet concoursJeunes parent à associer au plugin
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
	 * @param fftalogpath fftalogpath à définir
	 */
	public void setFftalogpath(String fftalogpath) {
		this.fftalogpath = fftalogpath;
	}

	@Override
    public void run() {
    	if(parentframe == null)
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
        	if(System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
        		fireProgressionInfo("Exportation de Result'Arc...");
				Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir") //$NON-NLS-1$
				        + File.separator
				        + pluginRessources.getResourceString("winffta.export.cmd", fftalogpath, System.getProperty("java.io.tmpdir"))); //$NON-NLS-1$ //$NON-NLS-2$
				proc.waitFor();
        	}

    		fireProgressionInfo("Nettoyage du fichier...");
    		//HACK problème retour caractère \r dans la table hyperfile FICLUB.FIC
            //charge tous le fichier en mémoire
            FileReader frficlub = new FileReader(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
            		+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
            StringBuffer sbuffer = new StringBuffer();
            char[] buffer = new char[128];
            int dataSize = 0;
            while((dataSize = frficlub.read(buffer)) > -1) {
            	sbuffer.append(buffer, 0, dataSize);
            }
            frficlub.close();
            buffer = null;
            
            String sFiclub = sbuffer.toString();
            sbuffer = null;
            
            //supprime le caractère foireux
            sFiclub = sFiclub.replaceAll("\\r;", ";"); //$NON-NLS-1$ //$NON-NLS-2$
            
            //réimprime le fichier
            PrintStream psficlub = new PrintStream(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
            		+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
            psficlub.print(sFiclub);
            psficlub.close();
            sFiclub = null;
            //FIN HACK
            
        	//ConcoursJeunes.dbConnection.setAutoCommit(true);
        	
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			stmt.executeUpdate("SET LOG 0");
			
			Hashtable<String, String> ht = new Hashtable<String, String>();
			
			ht.put("temp", System.getProperty("java.io.tmpdir").replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$
			
			SqlParser.createBatch(
					new File(pluginRessources.getResourceString("sql.importffta")), stmt, ht); //$NON-NLS-1$
			
			fireProgressionInfo("Integration à la base...");
			stmt.executeBatch();
			
			stmt.close();

        } catch (InterruptedException e1) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(), //$NON-NLS-1$
					e1.fillInStackTrace());
			e1.printStackTrace();

		} catch (IOException io) {
			JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), io.getLocalizedMessage(), //$NON-NLS-1$
					io.fillInStackTrace());
            io.printStackTrace();

        } catch(NullPointerException npe) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), npe.getLocalizedMessage(), //$NON-NLS-1$
					npe.fillInStackTrace());
            npe.printStackTrace();
        } catch(OutOfMemoryError oome) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), oome.getLocalizedMessage(), //$NON-NLS-1$
        			oome.fillInStackTrace());
        	oome.printStackTrace();
        } catch (SQLException e) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.getLocalizedMessage(), //$NON-NLS-1$
        			e.fillInStackTrace());
			e.printStackTrace();
		}
    }
    
    private void fireProgressionInfo(String info) {
    	for(FFTAImportThreadListener listener : listeners.getListeners(FFTAImportThreadListener.class)) {
    		listener.progressionInfo(info);
    	}
    }
    
    private void fireImportFinished() {
    	for(FFTAImportThreadListener listener : listeners.getListeners(FFTAImportThreadListener.class)) {
    		listener.importFinished();
    	}
    }
}
