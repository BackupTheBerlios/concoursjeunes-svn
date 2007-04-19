package org.concoursjeunes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Convertisseur {

	public static void convert(File oldconfig) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(oldconfig));
			
			String contentfile = ""; 
			
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				contentfile += line.replaceAll("ajinteractive.concours", "org.concoursjeunes") + "\n";
			}
			
			bufferedReader.close();
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(oldconfig));
			bufferedWriter.write(contentfile);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
