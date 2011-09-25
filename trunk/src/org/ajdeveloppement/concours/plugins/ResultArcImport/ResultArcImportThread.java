/*
 * Copyright 2002-2009 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
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
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
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
 *  any later version.
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
package org.ajdeveloppement.concours.plugins.ResultArcImport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.event.EventListenerList;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.sql.SqlParser;
import org.ajdeveloppement.concours.ApplicationCore;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;

/**
 * Plugin d'import d'une base Result'Arc (Format Windev HF) vers ArcCompetition
 * 
 * @author Aurélien JEOFFRAY
 */

public class ResultArcImportThread extends Thread {

	private JDialog parentframe;

	private final AjResourcesReader pluginRessources = new AjResourcesReader("properties.ResultArcImportPlugin"); //$NON-NLS-1$
	private final AjResourcesReader pluginLocalisation = new AjResourcesReader("org.ajdeveloppement.concours.plugins.ResultArcImport.ResultArcImportPlugin_libelle", ResultArcImportThread.class.getClassLoader()); //$NON-NLS-1$
	private final EventListenerList listeners = new EventListenerList();

	private String fftalogpath = ""; //$NON-NLS-1$


	public ResultArcImportThread() {
		this.setName("ResultArcImportThread"); //$NON-NLS-1$
	}

	public void addResultArcImportThreadListener(ResultArcImportThreadListener listener) {
		listeners.add(ResultArcImportThreadListener.class, listener);
	}

	public void removeResultArcImportThreadListener(ResultArcImportThreadListener listener) {
		listeners.remove(ResultArcImportThreadListener.class, listener);
	}

	/**
	 * Définit l'objet parent auquel est rattaché le plugin
	 * 
	 * @param parentframe la boite de dialogue rattaché au thread
	 */
	public void setParentFrame(JDialog parentframe) {
		this.parentframe = parentframe;
	}

	/**
	 * @return fftalogpath
	 */
	public String getFftalogpath() {
		return fftalogpath;
	}

	/**
	 * @param fftalogpath
	 *            fftalogpath à définir
	 */
	public void setFftalogpath(String fftalogpath) {
		this.fftalogpath = fftalogpath;
	}

	@Override
	public void run() {
		if (parentframe == null)
			return;
		
		fftaLoader();
		
		fireImportFinished();
	}

	/**
	 * Chargement à partir des exports FFTA
	 * 
	 */
	private void fftaLoader() {
		try {
			if (System.getProperty("os.name").startsWith("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
				fireProgressionInfo(pluginLocalisation.getResourceString("progress.export")); //$NON-NLS-1$
				Process proc = Runtime.getRuntime().exec(System.getProperty("user.dir") //$NON-NLS-1$
						+ File.separator + pluginRessources.getResourceString("winffta.export.cmd", fftalogpath, System.getProperty("java.io.tmpdir"))); //$NON-NLS-1$ //$NON-NLS-2$
				proc.waitFor();
			}

			fireProgressionInfo(pluginLocalisation.getResourceString("progress.clean")); //$NON-NLS-1$
			// HACK problème retour caractère \r dans la table hyperfile FICLUB.FIC
			// charge tous le fichier en mémoire
			FileReader frficlub = new FileReader(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
					+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
			StringBuffer sbuffer = new StringBuffer();
			char[] buffer = new char[128];
			int dataSize = 0;
			while ((dataSize = frficlub.read(buffer)) > -1) {
				sbuffer.append(buffer, 0, dataSize);
			}
			frficlub.close();
			buffer = null;

			String sFiclub = sbuffer.toString();
			sbuffer = null;

			// supprime le caractère foireux
			sFiclub = sFiclub.replaceAll("\\r;", ";"); //$NON-NLS-1$ //$NON-NLS-2$

			// réimprime le fichier
			PrintStream psficlub = new PrintStream(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
					+ File.separator + pluginRessources.getResourceString("winffta.ficlub.fichier")); //$NON-NLS-1$
			psficlub.print(sFiclub);
			psficlub.close();
			sFiclub = null;
			// FIN HACK

			Statement stmt = ApplicationCore.dbConnection.createStatement();

			Hashtable<String, String> ht = new Hashtable<String, String>();

			ht.put("temp", System.getProperty("java.io.tmpdir").replaceAll("\\\\", "\\\\\\\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

			SqlParser.createBatch(new File(pluginRessources.getResourceString("sql.importffta")), stmt, ht); //$NON-NLS-1$

			fireProgressionInfo(pluginLocalisation.getResourceString("progress.integration")); //$NON-NLS-1$
			stmt.executeBatch();

			stmt.close();

		} catch (InterruptedException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();

		} catch (IOException io) {
			DisplayableErrorHelper.displayException(io);
			io.printStackTrace();

		} catch (NullPointerException npe) {
			DisplayableErrorHelper.displayException(npe);
			npe.printStackTrace();
		} catch (OutOfMemoryError oome) {
			DisplayableErrorHelper.displayException(oome);
			oome.printStackTrace();
		} catch (SQLException e) {
			DisplayableErrorHelper.displayException(e);
			e.printStackTrace();
		}
	}

	private void fireProgressionInfo(String info) {
		for (ResultArcImportThreadListener listener : listeners.getListeners(ResultArcImportThreadListener.class)) {
			listener.progressionInfo(info);
		}
	}

	private void fireImportFinished() {
		for (ResultArcImportThreadListener listener : listeners.getListeners(ResultArcImportThreadListener.class)) {
			listener.importFinished();
		}
	}
}
