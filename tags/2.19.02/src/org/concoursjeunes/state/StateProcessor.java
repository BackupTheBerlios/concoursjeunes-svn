/*
 * Créé le 13 juin 08 à 11:29:05 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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
package org.concoursjeunes.state;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.script.ScriptException;
import javax.swing.JOptionPane;

import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.Profile;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Permet la compilation et l'affichage des éditions. Voir la documentation de 
 * conception des états de concours pour plus d'information.
 * 
 * @author Aurélien JEOFFRAY
 *
 */
public class StateProcessor {
	private State state;
	private Profile profile;
	private FicheConcours ficheConcours;
	
	/**
	 * 
	 */
	public StateProcessor(State state, Profile profile, FicheConcours ficheConcours) {
		this.state = state;
		this.profile = profile;
		this.ficheConcours = ficheConcours;
	}
	
	/**
	 * @return state
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * @param state state à définir
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	public void process(int depart, int serie, boolean save)
			throws IOException, ScriptException, FileNotFoundException, DocumentException {
		Document document = new Document();
		String filePath;
		
		if(!save) {
			File tmpFile = File.createTempFile("cta", ApplicationCore.staticParameters.getResourceString("extention.pdf")); //$NON-NLS-1$ //$NON-NLS-2$
			filePath = tmpFile.getCanonicalPath();
			tmpFile.deleteOnExit();
		} else {
			String concoursName = ficheConcours.getParametre().getSaveName();
			concoursName = concoursName.substring(0, concoursName.length() - 4);
			filePath = ApplicationCore.userRessources.getConcoursPathForProfile(profile).getPath() + File.separator
					+ concoursName + File.separator + state.getLocalizedDisplayName()
					+ " - " + DateFormat.getDateInstance().format(new Date()) + " " + new SimpleDateFormat("HH.mm.ss").format(new Date()) + ".pdf";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
		}
		
		AjResourcesReader langReader = new AjResourcesReader("lang", new URLClassLoader(new URL[] { state.getStateURL() })); //$NON-NLS-1$
		
		StateOptions options = new StateOptions(depart, serie, langReader, profile);
		
		boolean isprintable = state.printState(ficheConcours, document, options, new File(filePath));
		
		if(!isprintable) {
			JOptionPane.showMessageDialog(null, profile.getLocalisation().getResourceString("ficheconcours.print.nothing")); //$NON-NLS-1$
		}
		
		document.close();
		
		if (isprintable) {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(filePath));
			} else {
				assert profile.getConfiguration() != null;
				
				String NAV = ApplicationCore.getAppConfiguration().getPdfReaderPath();

				Runtime.getRuntime().exec(NAV + " " + new File(filePath).getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
}
