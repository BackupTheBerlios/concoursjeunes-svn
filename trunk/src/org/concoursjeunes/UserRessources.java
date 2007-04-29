/*
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
package org.concoursjeunes;

import java.io.*;

/**
 * Crée et donne le chemin des ressources utilisateur pour le programme
 * @author  Aurélien Jeoffray
 */
public class UserRessources {
	private static String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static String EXT_XML = ".xml"; //$NON-NLS-1$
	
    public String userPath;
    
    /**
     * Construit le répertoire utilisateur selon le systeme
     * 
     * @param progname nom du programme
     */
    public UserRessources(String progname) {
        if(System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
            userPath = System.getenv("APPDATA") + File.separator //$NON-NLS-1$
                    + progname; //$NON-NLS-1$
            //TODO pour vista copier les données multi utilisateur dans ALLUSERSPROFILE
        } else {
            userPath = System.getProperty("user.home") + File.separator //$NON-NLS-1$ 
            + "." + progname; //$NON-NLS-1$
        }
        
        createPathIfNotExist(userPath);
    }
    
    /**
     * Retourne le chemin absolu du profile
     * 
     * @param profile
     * @return String - le chemin absolu du profile
     */
    private String getProfilePath(String profile) {
        String profilePath = userPath + File.separator + "Profile" + //$NON-NLS-1$
                File.separator + profile;
        
        return profilePath;
    }
    
    /**
     * Crée l'arborescence inexistante
     * 
     * @param path - l'arborescence à tester et cré si nécessaire
     */
    private void createPathIfNotExist(String path) {
        File directory = new File(path);
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Copie le fichier srcPath dans le repertoire desPath
     * 
     * @param srcPath - le fichier à copier
     * @param destPath - l'emplacement de destination
     */
    private void copyFile(File srcPath, String destPath) {
        if(!(new File(destPath + File.separator + srcPath.getName()).exists())) {
            System.out.println(srcPath.getAbsolutePath());
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(srcPath);
                
                fos = new FileOutputStream(
                        new File(destPath + File.separator + 
                                srcPath.getName()));
                
                byte[] buffer = new byte[512*1024];
                int nbLecture;
                while((nbLecture = fis.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, nbLecture);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                	if(fis != null) fis.close();
                    if(fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Copie les fichiers de configuration du repertoire de base vers le repertoire utilisateur
     *
     */
    private void copyDefaultConfigForUser() {
        File[] fileForCopy = new File("config").listFiles(new java.io.FileFilter() { //$NON-NLS-1$
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml"); //$NON-NLS-1$
            }
        });
        
        for(int i = 0; i < fileForCopy.length; i++) {
            copyFile(fileForCopy[i], userPath);
        }
    }
    
    /**
     * Donne le chemin des fichiers de configuration
     * 
     * @return String - le chemin des fichiers de configuration
     */
    public String getConfigPathForUser() {
        copyDefaultConfigForUser();
        
        return userPath;
    }
    
    public String getBasePathForUser() {
    	 createPathIfNotExist(userPath + File.separator + "base"); //$NON-NLS-1$
        
        return userPath + File.separator + "base"; //$NON-NLS-1$
    }
    
    public String getConcoursPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "concours"; //$NON-NLS-1$
        
        createPathIfNotExist(concoursPath);
        
        return concoursPath;
    }
    
    /**
     * Retourne le chemin des logs en fonction du profil
     * 
     * @param profile - le profil pour lequelle renvouyé le chemin des logs
     * @return le chemin des logs du profil
     */
    public String getLogPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "log"; //$NON-NLS-1$
        
        createPathIfNotExist(concoursPath);
        
        return concoursPath;
    }
    
    /**
     * Retourne la liste des configuration disponaible
     * 
     * @return la liste des configurations disponibles
     */
    public String[] listAvailableConfigurations() {
    	String[] strConfig = new File(getConfigPathForUser()).list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.startsWith(CONFIG_PROFILE) && name.endsWith(EXT_XML))
					return true;
				return false;
			}
		});

		for(int i = 0; i < strConfig.length; i++)
			strConfig[i] = strConfig[i].substring(CONFIG_PROFILE.length(), strConfig[i].length() - EXT_XML.length());
		return strConfig;
    }

    /**
	 * @return  Renvoie userPath.
	 * @uml.property  name="userPath"
	 */
    public String getUserPath() {
        return userPath;
    }

    /**
	 * @param userPath  userPath à définir.
	 * @uml.property  name="userPath"
	 */
    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
}
