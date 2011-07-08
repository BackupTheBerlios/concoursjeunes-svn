/*
 * Créé le 13 juin 08 à 10:57:18 pour ConcoursJeunes
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.FicheConcours;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Représente un état d'édition
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@XmlRootElement(name="state")
@XmlAccessorType(XmlAccessType.FIELD)
public class State implements Comparable<State> {
	private String name = ""; //$NON-NLS-1$
	private String category = ""; //$NON-NLS-1$
	private String displayName = ""; //$NON-NLS-1$
	private boolean serie = false;
	private boolean start = false;
	private boolean save = false;
	private String type = "XML"; //$NON-NLS-1$
	private String template = ""; //$NON-NLS-1$
	private String script = ""; //$NON-NLS-1$
	@XmlTransient
	private boolean isZipped = false;
	@XmlTransient
	private StateScriptInterface stateScript;
	
	/**
	 * Initialise un nouvel état
	 */
	public State() {
	}

	/**
	 * Retourne le nom de l'état
	 * 
	 * @return le nom de l'état
	 */
	public String getName() {
		return name;
	}

	/**
	 * Définit le nom de l'état
	 * 
	 * @param name le nom de l'état
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retourne le nom de la catégorie rattaché à l'état
	 * 
	 * @return le nom de la catégorie rattaché à l'état
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Définit le nom de la catégorie rattaché à l'état
	 * 
	 * @param category le nom de la catégorie rattaché à l'état
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Retourne le nom à afficher pour l'état (non localisé)
	 * 
	 * @return le nom à afficher pour l'état (non localisé)
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Retourne le nom à afficher pour l'état dans sa forme localisé
	 * 
	 * @return le nom à afficher pour l'état dans sa forme localisé
	 */
	public String getLocalizedDisplayName() {
		String actionName = displayName;
		String statePath = ApplicationCore.staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + "states" + File.separator + name + ((isZipped) ? ".zip" : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle(
					"lang", //$NON-NLS-1$
					Locale.getDefault(),
					new URLClassLoader(new URL[] {new File(statePath).toURI().toURL() })); 
			try {
				actionName = rb.getString(displayName);
				actionName = new String(actionName.getBytes("ISO-8859-1"), "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MissingResourceException e) {
				actionName = displayName;
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		
		return actionName;
	}
	
	/**
	 * Définit le nom à afficher pour l'état (non localisé)
	 * 
	 * @param displayName le nom à afficher pour l'état (non localisé)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Indique si l'utilisateur doit choisir la série pour cette état
	 * 
	 * @return true si l'utilisateur doit choisir la série pour cette état
	 */
	public boolean isSerie() {
		return serie;
	}

	/**
	 * Définit si l'utilisateur doit choisir la série pour cette état
	 * 
	 * @param serie true si l'utilisateur doit choisir la série pour cette état
	 */
	public void setSerie(boolean serie) {
		this.serie = serie;
	}

	/**
	 * Indique si l'utilisateur doit choisir le départ pour cette état
	 * 
	 * @return true si l'utilisateur doit choisir le départ pour cette état
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * Définit si l'utilisateur doit choisir le départ pour cette état
	 * 
	 * @param start true si l'utilisateur doit choisir le départ pour cette état
	 */
	public void setStart(boolean start) {
		this.start = start;
	}

	/**
	 * Indique si, par défaut, l'édition doit ou non être sauvegardé
	 * 
	 * @return true si l'édition doit être sauvegardé par défaut
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * Définit si, par défaut, l'édition doit ou non être sauvegardé
	 * 
	 * @param save true si l'édition doit être sauvegardé par défaut
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type type à définir
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Retourne le template XML de l'édition
	 * 
	 * @return template le template XML de l'édition
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Définit le template XML de l'édition
	 * 
	 * @param template le template XML de l'édition
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Retourne le script de génération de l'édition
	 * 
	 * @return script le script de génération de l'édition
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Définit le script de génération de l'édition
	 * 
	 * @param script le script de génération de l'édition
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * Indique si les fichiers de l'état sont stocké dans une archive zip ou
	 * dans un dossier 
	 * 
	 * @return isZipped true si les fichiers de l'état sont dans une archive zip
	 */
	public boolean isZipped() {
		return isZipped;
	}

	/**
	 * Définit si les fichiers de l'état sont stocké dans une archive zip ou
	 * dans un dossier
	 * 
	 * @param isZipped true si les fichiers de l'état sont dans une archive zip
	 */
	public void setZipped(boolean isZipped) {
		this.isZipped = isZipped;
	}
	
	/**
	 * Compile le script javascript de l'état pour une exécution plus rapide
	 * 
	 * @throws IOException
	 * @throws ScriptException
	 */
	protected void compileScript() throws IOException, ScriptException {
		ScriptEngineManager se = new ScriptEngineManager();
		ScriptEngine scriptEngine = se.getEngineByName("JavaScript"); //$NON-NLS-1$
		
		Reader reader = new BufferedReader(new InputStreamReader(
				new URL(((isZipped) ? "jar:" : "") + getStateURL().toString() + ((isZipped) ? "!" : "") + "/" + script).openStream())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		scriptEngine.eval(reader);
		reader.close();
		
		stateScript = ((Invocable)scriptEngine).getInterface(StateScriptInterface.class);
	}
	
	/**
	 * Retourne l'url du fichier ou répertoire de l'état
	 * 
	 * @return stateURL
	 * @throws MalformedURLException 
	 */
	protected URL getStateURL() throws MalformedURLException {
		String statePath = ApplicationCore.staticParameters.getResourceString("path.ressources") //$NON-NLS-1$
				+ File.separator + "states" + File.separator + name; //$NON-NLS-1$

		//test si l'état est dans une archive compressé
		if(isZipped) {
			statePath += ".zip"; //$NON-NLS-1$
		}
		return new File(statePath).toURI().toURL();
	}

	/**
	 * Test si l'état est générable pour la fiche et les options fournit en paramètre
	 * 
	 * @param ficheConcours la fiche avec laquelle tester l'état
	 * @param options les options d'édition
	 * @return true si l'état est générable
	 * @throws ScriptException
	 */
	public boolean checkPrintable(FicheConcours ficheConcours, StateOptions options) throws ScriptException {
		if(stateScript == null) {
			try {
				compileScript();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(stateScript == null)
			return false;
				
		return stateScript.checkPrintable(ficheConcours, options);
	}
	
	/**
	 * Génère l'état
	 * 
	 * @param ficheConcours la fiche concours pour laquelle générer l'état
	 * @param document la représentation objet du pdf de l'état
	 * @param options les options d'éditions
	 * @param pdfPath le chemin du pdf à générer
	 * @return true si la génération a put être réalisé
	 * 
	 * @throws ScriptException
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	@SuppressWarnings("nls")
	public boolean printState(FicheConcours ficheConcours, Document document, StateOptions options, File pdfPath) throws ScriptException, FileNotFoundException, DocumentException, MalformedURLException {
		if(!checkPrintable(ficheConcours, options))
			return false;
		
		pdfPath.getParentFile().mkdirs();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
		writer.setFullCompression();
		
		stateScript.printState(ficheConcours, new URL(((isZipped) ? "jar:" : "") + getStateURL().toString() + ((isZipped) ? "!" : "") + "/" + template), document, writer, options);
		
		//writer.flush();
		//writer.close();

		return true;
	}

	/**
	 * Permet de trier les états par ordre alphabétique sur leurs nom
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(State o) {
		return name.compareTo(o.getName());
	}
}
