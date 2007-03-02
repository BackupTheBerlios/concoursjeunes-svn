package ajinteractive.concours;

import java.io.*;

import ajinteractive.standard.java2.AJToolKit;

/**
 * Crée et donne le chemin des ressources utilisateur pour le programme
 * 
 * @author Aurélien Jeoffray
 *
 */
public class UserRessources {
    public String userPath;
    
    /**
     * Construit le répertoire utilisateur selon le systeme
     */
    public UserRessources() {
        if(System.getProperty("os.name").startsWith("Windows")) {
            userPath = System.getenv("APPDATA") + File.separator 
                    + "ConcoursJeunes";
        } else {
            userPath = System.getProperty("user.home") + File.separator 
            + ".ConcoursJeunes";
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
        String profilePath = userPath + File.separator + "Profile" + 
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
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
        	//si on cherche à copier un fichier de configuration
        	// et que celui ci est un profil officiel, alors on met
        	// à jour le fichier sur la reference
        	if(srcPath.getName().startsWith("configuration_") && srcPath.getName().endsWith(".xml")) {
	        	Configuration configuration = (Configuration)AJToolKit.loadXMLStructure(new File(destPath + File.separator + 
	        			srcPath.getName()), false);
	        	if(configuration.isOfficialProfile()) {
	        		Configuration configurationOfficial = (Configuration)AJToolKit.loadXMLStructure(srcPath, false);
	        		
	        		configuration.resetOfficialInfo(configurationOfficial);
	        		
	        		AJToolKit.saveXMLStructure(new File(destPath + File.separator +
	        				srcPath.getName()), configuration, false);
	        	}
        	}
        }
    }
    
    /**
     * Copie les fichiers de configuration du repertoire de base vers le repertoire utilisateur
     *
     */
    private void copyDefaultConfigForUser() {
        File[] fileForCopy = new File("config").listFiles(new java.io.FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml");
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
        File[] fileForCopy = new File("base").listFiles(new java.io.FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".xml.gz");
            }
        });
        
        createPathIfNotExist(userPath + File.separator + "base");
        
        for(int i = 0; i < fileForCopy.length; i++) {
            copyFile(fileForCopy[i], userPath + File.separator + "base");
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
        
        return userPath + File.separator + "base";
    }
    
    public String getConcoursPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "concours";
        
        createPathIfNotExist(concoursPath);
        System.out.println(concoursPath);
        return concoursPath;
    }
    
    public String getLogPathForProfile(String profile) {
        String concoursPath = getProfilePath(profile) + File.separator + "log";
        
        createPathIfNotExist(concoursPath);
        
        return concoursPath;
    }

    /**
     * @return Renvoie userPath.
     */
    public String getUserPath() {
        return userPath;
    }

    /**
     * @param userPath userPath à définir.
     */
    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
}
