package org.concoursjeunes.plugins.FFTAImport;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.concoursjeunes.ConcoursJeunes;
import org.jdesktop.swingx.JXErrorDialog;

import ajinteractive.standard.java2.*;
import ajinteractive.standard.utilities.sql.SqlParser;
/**
 * Plugin d'import d'une base WinFFTA 2 (Format Windev HF)
 * vers ConcoursJeunes (Format Java XML)
 *  
 * @author Aurélien JEOFFRAY
 */

public class FFTAImportThread extends Thread {
    
    private JDialog parentframe;
    
    private AjResourcesReader pluginRessources = new AjResourcesReader("FFTAImportPlugin");
    private EventListenerList listeners = new EventListenerList();
    
    private String fftalogpath = "";
    
    /**
     * 
     *
     */
    public FFTAImportThread() {
    	
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
     * @return boolean - true si la requette à été executé avec succé, false sinon
     */
    private void fftaLoader() {
        try {
        	if(System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
        		fireProgressionInfo("Exportation de Result'Arc...");
				Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir") //$NON-NLS-1$
				        + File.separator
				        + pluginRessources.getResourceString("winffta.export.cmd", fftalogpath)); //$NON-NLS-1$
				proc.waitFor();
        	}

    		fireProgressionInfo("Nettoyage du fichier...");
    		//HACK problème retour caractère \r dans la table hyperfile FICLUB.FIC
            //charge tous le fichier en mémoire
            FileReader frficlub = new FileReader("base"
            		+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier"));
            StringBuffer sbuffer = new StringBuffer();
            char[] buffer = new char[128];
            int dataSize = 0;
            while((dataSize = frficlub.read(buffer)) > -1) {
            	sbuffer.append(buffer, 0, dataSize);
            }
            frficlub.close();
            
            String sFiclub = sbuffer.toString();
            sbuffer = null;
            
            //supprime le caractère foireux
            sFiclub = sFiclub.replaceAll("\\r;", ";");
            
            //réimprime le fichier
            PrintStream psficlub = new PrintStream("base"
            		+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier"));
            psficlub.print(sFiclub);
            psficlub.close();
            //FIN HACK
            
        	ConcoursJeunes.dbConnection.setAutoCommit(true);
        	
			Statement stmt = ConcoursJeunes.dbConnection.createStatement();
			
			SqlParser.createBatch(
					new File(pluginRessources.getResourceString("sql.importffta")), stmt);
			
			fireProgressionInfo("Integration à la base...");
			stmt.executeBatch();
			
			stmt.close();

        } catch (InterruptedException e1) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.getLocalizedMessage(),
					e1.fillInStackTrace());
			e1.printStackTrace();

		} catch (IOException io) {
			JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), io.getLocalizedMessage(),
					io.fillInStackTrace());
            io.printStackTrace();

        } catch(NullPointerException npe) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), npe.getLocalizedMessage(),
					npe.fillInStackTrace());
            npe.printStackTrace();
        } catch(OutOfMemoryError oome) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), oome.getLocalizedMessage(),
        			oome.fillInStackTrace());
        	oome.printStackTrace();
        } catch (SQLException e) {
        	JXErrorDialog.showDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e.getLocalizedMessage(),
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
