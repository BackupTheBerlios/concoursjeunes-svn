/*
 * Créer le 13 juin 08 à 10:57:18 pour ConcoursJeunes
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
package org.concoursjeunes.state;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.concoursjeunes.ApplicationCore;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@XmlRootElement(name="state")
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
	
	/**
	 * 
	 */
	public State() {
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name name à définir
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category category à définir
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	public String getLocalizedDisplayName() {
		String actionName = displayName;
		String statePath = ApplicationCore.ajrParametreAppli.getResourceString("path.ressources") //$NON-NLS-1$
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
	 * @param displayName displayName à définir
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return serie
	 */
	public boolean isSerie() {
		return serie;
	}

	/**
	 * @param serie serie à définir
	 */
	public void setSerie(boolean serie) {
		this.serie = serie;
	}

	/**
	 * @return start
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * @param start start à définir
	 */
	public void setStart(boolean start) {
		this.start = start;
	}

	/**
	 * @return save
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * @param save save à définir
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
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template template à définir
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script script à définir
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return isZipped
	 */
	public boolean isZipped() {
		return isZipped;
	}

	/**
	 * @param isZipped isZipped à définir
	 */
	public void setZipped(boolean isZipped) {
		this.isZipped = isZipped;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(State o) {
		return name.compareTo(o.getName());
	}
}
