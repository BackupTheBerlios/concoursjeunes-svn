package org.concoursjeunes.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import org.concoursjeunes.ConcoursJeunes;

import ajinteractive.standard.java2.AJFileFilter;

public class FileImportPlugin extends Thread implements ImportPlugin {

    private boolean state = false;
    
    private JFrame parentframe;
    
    public FileImportPlugin() {
    }
    
    public void setParentFrame(JFrame parentframe) {
    	this.parentframe = parentframe;
    }
    
    @Override
    public void run() {
        fileLoader();
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
     * Chargement à partir d'une archive
     * 
     * @return boolean - true si le chargement est réussi, false sinon
     */
    @SuppressWarnings("unchecked")
    private boolean fileLoader() {
    	if(parentframe == null)
    		return false;
    	
        File source;
        File destination;
        
        //java.io.FileInputStream sourceFile = null;
        FileOutputStream destinationFile = null;
        
        try {
            //Boite de dialogue de selection de fichier
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new AJFileFilter("jar", "application/x-compressed")); //$NON-NLS-1$ //$NON-NLS-2$
            int returnVal = chooser.showOpenDialog(parentframe);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               source = chooser.getSelectedFile();
               
               //ouvre l'archive et recupere les entrées
               ZipFile zipRessources = new ZipFile(source);
               Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)zipRessources.entries();
               
               //barre de progression
               ProgressMonitor progressMonitor = new ProgressMonitor(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("message.telechargement"), //$NON-NLS-1$
                       "", 0, zipRessources.size()); //$NON-NLS-1$
               progressMonitor.setProgress(0);
               progressMonitor.setMillisToDecideToPopup(0);
               int iLigue = 1;
               
               //import de chaque entrée
               while(e.hasMoreElements()) {
                  ZipEntry ze = e.nextElement();
                  InputStream is = zipRessources.getInputStream(ze);
                  
                  if(!ze.getName().startsWith("META-INF")) { //$NON-NLS-1$
                      destination = new File(ConcoursJeunes.userRessources.getBasePathForUser() + File.separator + ze.getName());
                      destination.createNewFile();

                      destinationFile = new java.io.FileOutputStream(destination);
                      byte[] buffer = new byte[512*1024];
                      int nbLecture;
                      while((nbLecture = is.read(buffer)) != -1 ) {
                          destinationFile.write(buffer, 0, nbLecture);
                      }
                  }
                  progressMonitor.setNote(ze.getName());
                  progressMonitor.setProgress(iLigue++);
               }
               
               JOptionPane.showMessageDialog(parentframe, ConcoursJeunes.ajrLibelle.getResourceString("message.import.fin"), //$NON-NLS-1$
                       ConcoursJeunes.ajrLibelle.getResourceString("message.import"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
            }
            return true;
        } catch( java.io.FileNotFoundException f ) {
            JOptionPane.showMessageDialog(parentframe,
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur.filenotfound"), //$NON-NLS-1$
                    ConcoursJeunes.ajrLibelle.getResourceString("erreur"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            f.printStackTrace();
            return false;
        } catch( java.io.IOException e ) {
            JOptionPane.showMessageDialog(parentframe,
                    "<html>" + e.getMessage() + "</html>", //$NON-NLS-1$ //$NON-NLS-2$
                    "IOException",JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
            e.printStackTrace();
            return false;
        } finally {
            // Quoi qu'il arrive, on ferme les flux
            try { if(destinationFile != null) destinationFile.close(); } catch(Exception e) { }
        }
    }
}
