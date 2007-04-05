package org.concoursjeunes.plugins;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import org.concoursjeunes.ConcoursJeunes;

import ajinteractive.standard.java2.*;
/**
 * Plugin d'import d'une base WinFFTA 2 (Format Windev HF) vers ConcoursJeunes (Format Java XML) 
 * @author  aurelien
 */
public class FFTAImportPlugin extends Thread implements ImportPlugin {

    private boolean state = false;
    
    private JFrame parentframe;
    
    private AjResourcesReader pluginRessources = new AjResourcesReader("FFTAImportPlugin");
    private AjResourcesReader pluginLocalisation = new AjResourcesReader("FFTAImportPlugin_libelle");
    
    /**
     * 
     *
     */
    public FFTAImportPlugin() {
    	
    }
    
    /**
     * Definit l'objet parent auquel est rattaché le plugin
     * 
     * @param concoursJeunes - l'objet concoursJeunes parent à associer au plugin
     */
    public void setParentFrame(JFrame parentframe) {
    	this.parentframe = parentframe;
    }
    
    @Override
    public void run() {
        fftaLoader();
    }
    
    /**
     * Indique si le chargement s'est terminé avec succés
     * 
     * @return boolean - l'etat de fin de chargement
     */
    public boolean isSuccess() {
        return state;
    }
    
    /**
     * Chargement à partir des exports FFTA
     * 
     * @return boolean - true si la requette à été executé avec succé, false sinon
     */
    private boolean fftaLoader() {
    	if(parentframe == null)
    		return false;

        try {
        	if(System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
				Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir") //$NON-NLS-1$
				        + File.separator + pluginRessources.getResourceString("winffta.export.cmd")); //$NON-NLS-1$
				proc.waitFor();
        	}

        	try {
        		//HACK problème retour caractère \r dans la table hyperfile FICLUB.FIC
                //charge tous le fichier en mémoire
                FileReader frficlub = new FileReader("base" + File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier"));
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
                PrintStream psficlub = new PrintStream("base" + File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier"));
                psficlub.print(sFiclub);
                psficlub.close();
                //FIN HACK
                
            	ConcoursJeunes.dbConnection.setAutoCommit(true);
            	
				Statement stmt = ConcoursJeunes.dbConnection.createStatement();

				//monte les fichiers FFTA pour recuperation des données
				stmt.executeUpdate("SET TABLE EntiteFFTA SOURCE \"FICLUB.TXT;fs=\\semi\"");
				stmt.executeUpdate("SET TABLE ArchersFFTA SOURCE \"Licence.TXT;fs=\\semi\"");
				//supprime le contenu de la table et le remplace par le contenu du fichier FFTA
				stmt.executeUpdate("DELETE FROM Entite");
				stmt.executeUpdate("INSERT INTO Entite (AGREMENTENTITE, NOMENTITE, VILLEENTITE) " +
						"SELECT AGREMENTENTITE, NOMENTITE, VILLEENTITE FROM EntiteFFTA where NomEntite <> 'DEPARTEMENT FEDERATION'");
				
				stmt.executeUpdate("DELETE FROM Archers");
				stmt.executeUpdate("INSERT INTO Archers (NUMLICENCEARCHER, NOMARCHER, " +
						"PRENOMARCHER, CERTIFMEDICAL, AGREMENTENTITE, GENREFFTA, CATEGORIEFFTA, NIVEAUFFTA," +
						"ARCFFTA) SELECT NUMLICENCEARCHER, NOMARCHER, " +
						"PRENOMARCHER, CERTIFMEDICAL, AGREMENTCLUBARCHER, GENREARCHER-1, CATEGORIEARCHER-1," +
						"NIVEAUARCHER-1, ARCARCHER-1 FROM ArchersFFTA");
				
				//demonte les fichiers FFTA (reduit le temps de lancement de la base)
				stmt.executeUpdate("SET TABLE EntiteFFTA SOURCE \"\"");
				stmt.executeUpdate("SET TABLE ArchersFFTA SOURCE \"\"");
				
				stmt.executeUpdate("COMMIT");
				
				stmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(parentframe, pluginLocalisation.getResourceString("message.import.fin"), //$NON-NLS-1$
                    pluginLocalisation.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			return true;
        } catch (InterruptedException e1) {
        	JOptionPane.showMessageDialog(parentframe,
                    "<html>" + e1.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
                    "InterruptedException", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e1.printStackTrace();
			return false;
		} catch (IOException io) {
            JOptionPane.showMessageDialog(parentframe,
                    "<html>" + io.getLocalizedMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
                    "IOException", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            io.printStackTrace();
            return false;
        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(parentframe,
                    "<html>" + pluginLocalisation.getResourceString("erreur.import") + "</html>", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    pluginLocalisation.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            npe.printStackTrace();
            return false;
        } catch(OutOfMemoryError oome) {
           // listeTireur = null;
            JOptionPane.showMessageDialog(parentframe,
                    "<html>" + pluginLocalisation.getResourceString("erreur.import.outofmemory") + "</html>", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    pluginLocalisation.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            return false;
        }
    }
}
