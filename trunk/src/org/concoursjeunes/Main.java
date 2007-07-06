package org.concoursjeunes;
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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.concoursjeunes.ui.ConcoursJeunesFrame;

/**
 * @author aurelien
 *
 */
public class Main {
	private static boolean mustUpdate = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcoursJeunes concoursJeunes = ConcoursJeunes.getInstance();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					//permet de s'assurer que la base de données est correctement fermé
					ConcoursJeunes.dbConnection.close();
					
					if(mustUpdate && new File(System.getProperty("user.dir")).canWrite()) {
						Runtime runtime = Runtime.getRuntime();
						Process process = runtime.exec("java -cp lib/AJPackage.jar ajinteractive.standard.utilities.updater.AjUpdaterApply " +
								"\"" + ConcoursJeunes.userRessources.getAllusersDataPath() + 
								File.separator + "update" + "\" \"" + System.getProperty("user.dir") + "\"");
						process.waitFor();
						process = runtime.exec("java -jar ConcoursJeunes.jar");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		File[] updateFiles = new File(ConcoursJeunes.userRessources.getAllusersDataPath() + 
				File.separator + "update").listFiles(new FilenameFilter() {
					public boolean accept(File file, String name) {
						return !name.endsWith(".sql");
					}
				});
		if(updateFiles.length > 0 
				&& JOptionPane.showConfirmDialog(null, "Une mise à jour est disponible, voulez vous l'installer") == JOptionPane.YES_OPTION) {
			mustUpdate = true;
			System.exit(0);
		}
		
		new ConcoursJeunesFrame(concoursJeunes);
	}

}
