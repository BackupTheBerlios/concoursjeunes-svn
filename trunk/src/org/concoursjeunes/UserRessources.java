package org.concoursjeunes;

import java.io.*;

/**
 * Crée et donne le chemin des ressources utilisateur pour le programme
 * @author  Aurélien Jeoffray
 */
public class UserRessources {
	private static String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	private static String REGLEMENT_PROFILE = "reglement_"; //$NON-NLS-1$
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
     * Copie la base des archers du repertoire de base vers le repertoire utilisateur
     *
     */
    private void copyDefaultBaseForUser() {
        File[] fileForCopy = new File("base").listFiles(new java.io.FileFilter() { //$NON-NLS-1$
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml.gz"); //$NON-NLS-1$
            }
        });
        
        createPathIfNotExist(userPath + File.separator + "base"); //$NON-NLS-1$
        
        for(int i = 0; i < fileForCopy.length; i++) {
            copyFile(fileForCopy[i], userPath + File.separator + "base"); //$NON-NLS-1$
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
        copyDefaultBaseForUser();
        
        return userPath + File.separator + "base"; //$NON-NLS-1$
    }
    
    public String getConcoursPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "concours"; //$NON-NLS-1$
        
        createPathIfNotExist(concoursPath);
        
        return concoursPath;
    }
    
    /**
     * 
     * 
     * @param profile
     * @return
     */
    public String getLogPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "log"; //$NON-NLS-1$
        
        createPathIfNotExist(concoursPath);
        
        return concoursPath;
    }
    
    /**
     * 
     * 
     * @return
     */
    public String getReglementPathForUser() {
    	String reglementPath = userPath + File.separator + "reglements"; //$NON-NLS-1$
        
        createPathIfNotExist(reglementPath);
    	
    	return reglementPath;
    }
    
    /**
     * 
     * @return
     */
    public String[] listAvailableReglements() {
    	String[] strReglement = new File(getReglementPathForUser()).list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.startsWith(REGLEMENT_PROFILE) && name.endsWith(EXT_XML))
					return true;
				return false;
			}
		});
    	if(strReglement != null) {
			for(int i = 0; i < strReglement.length; i++)
				strReglement[i] = strReglement[i].substring(REGLEMENT_PROFILE.length(), strReglement[i].length() - EXT_XML.length());
    	}
		return strReglement;
    }
    
    /**
     * 
     * 
     * @return
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
