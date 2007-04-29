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
