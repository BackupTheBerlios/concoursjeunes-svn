/*
 * Créé le 29 déc. 2010 à 15:06:47 pour ConcoursJeunes / ArcCompétition
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
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
package org.ajdeveloppement.concours.db;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import org.h2.api.DatabaseEventListener;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class UpgradeDatabaseEventListener implements org.h2.upgrade.v1_1.api.DatabaseEventListener,DatabaseEventListener {

	private static ProgressMonitor monitor = new ProgressMonitor(null, "Chargement/Migration de la base\nL'opération peut durer plusieurs minutes\n ", "", 0, 1); //$NON-NLS-1$ //$NON-NLS-2$
	//private static boolean enabled = true;
	
	private String cuurrentTaskName;
	private int currentProgress = 0;
	
	private static SplashScreen splash = null;
	
	public static void forceCloseMonitor() {
		monitor.close();
	}
	
	public static void setMonitorEnabled(boolean enabled) {
		//UpgradeDatabaseEventListener.enabled = enabled;
	}
	
	public static void setSplashScreen(SplashScreen splash) {
		UpgradeDatabaseEventListener.splash = splash;
	}
	
	
	/* (non-Javadoc)
	 * @see org.h2.api.DatabaseEventListener#closingDatabase()
	 */
	@Override
	public void closingDatabase() {
		// TODO Raccord de méthode auto-généré

	}

	/* (non-Javadoc)
	 * @see org.h2.api.DatabaseEventListener#exceptionThrown(java.sql.SQLException, java.lang.String)
	 */
	@Override
	public void exceptionThrown(SQLException arg0, String arg1) {
		// TODO Raccord de méthode auto-généré

	}

	/* (non-Javadoc)
	 * @see org.h2.api.DatabaseEventListener#init(java.lang.String)
	 */
	@Override
	public void init(String arg0) {
		// TODO Raccord de méthode auto-généré

	}

	/* (non-Javadoc)
	 * @see org.h2.api.DatabaseEventListener#opened()
	 */
	@Override
	public void opened() {
		// TODO Raccord de méthode auto-généré

	}

	/* (non-Javadoc)
	 * @see org.h2.api.DatabaseEventListener#setProgress(int, java.lang.String, int, int)
	 */
	@Override
	public void setProgress(int state, String name, int x, int max) {
		/*if(enabled) {
			monitor.setMaximum(max);
			monitor.setNote("Traitement de " + name); //$NON-NLS-1$
			monitor.setProgress((x < max) ? x+1 : x);
		}*/
		final int percent = (int)Math.round(((double)x / (double)max) * 100.0);
		final String taskName = name;
		if(percent != currentProgress || !name.equals(cuurrentTaskName)) {
			currentProgress = percent;
			cuurrentTaskName = name;
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(splash != null) {
						try {
							Graphics2D g2d = splash.createGraphics();
				
							g2d.setColor(Color.WHITE);
							g2d.fillRect(10, 440, 480, 20);
							g2d.setColor(Color.BLACK);
							g2d.drawRect(10, 440, 480, 20);
							
							GradientPaint gp = new GradientPaint(0, 0, new Color(200,200,255, 200), (int)((480.0 / 100.0) * percent) - 11, 0, new Color(100,100,255, 200), true);
							g2d.setPaint(gp);
							g2d.fillRect(11, 441, (int)((480.0 / 100.0) * percent), 19); 
							
							g2d.setColor(Color.BLACK);
							g2d.drawString("Opération en base (peut durer plusieurs minutes): " + taskName, 15, 455); //$NON-NLS-1$
							
							try {
								splash.update();
							} catch (IllegalStateException e) {
							}
						} catch (Exception e) {
						}
					}
				}
			});
			
		}
	}

	@Override
	public void diskSpaceIsLow(long arg0) throws SQLException {
		// TODO Raccord de méthode auto-généré
		
	}

	@Override
	public void diskSpaceIsLow() {
		// TODO Raccord de méthode auto-généré
		
	}

}
