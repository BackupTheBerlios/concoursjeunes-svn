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
import java.sql.SQLException;

import org.concoursjeunes.ui.ConcoursJeunesFrame;

/**
 * @author aurelien
 *
 */
public class Main {

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
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		new ConcoursJeunesFrame(concoursJeunes);
	}

}
