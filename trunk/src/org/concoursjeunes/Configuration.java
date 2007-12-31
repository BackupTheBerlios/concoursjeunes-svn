/*
 * Créer le 18/01/2005 pour ConcoursJeunes
 *
 * Copyright 2002-2007 - Aurélien JEOFFRAY
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
package org.concoursjeunes;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.utilities.net.Proxy;

/**
 * parametre de configuration de l'application
 * 
 * @author  Aurelien Jeoffray
 * @version  3.0
 */
@XmlRootElement
public class Configuration extends DefaultParameters implements Cloneable {
	
	public static String CONFIG_PROFILE = "configuration_"; //$NON-NLS-1$
	public static String EXT_XML = ".xml"; //$NON-NLS-1$

	private String langue           = "";               //$NON-NLS-1$
	private String logoPath         = "ressources/logo_ffta.gif";   //$NON-NLS-1$
	
	private String reglementName	= ""; //$NON-NLS-1$

	private String pdfReaderPath    = "";               //$NON-NLS-1$

	private String formatPapier     = "A4";             //$NON-NLS-1$
	private String orientation      = "portrait";       //$NON-NLS-1$
	private int[] colonneAndLigne   = new int[] {9, 3}; 
	private Marges marges           = new Marges(0, 0, 0, 0);
	private double[] espacements    = new double[] {0.5, 0.5};

	private boolean interfaceResultatCumul = false;     //noreboot
	private boolean interfaceResultatSupl = true;       //noreboot
	private boolean interfaceAffResultatExEquo = true;  //noreboot
	
	private boolean useProxy = false;
	private Proxy proxy;

	//propriete caché
	private MetaDataFichesConcours metaDataFichesConcours = new MetaDataFichesConcours();
	private boolean firstboot       = false;            //noreboot
	private String curProfil        = "";               //noreboot //$NON-NLS-1$

	public Configuration() {

	}
	
	/**
	 * Retourne la langue courante de l'IHM
	 * @return  String - le code langue
	 */
	public String getLangue() {
		return this.langue;
	}

	/**
	 * Retourne l'adresse du lecteur pdf
	 * @return  String - l'adresse du lecteur pdf
	 */
	@XmlElement(required=false)
	public String getPdfReaderPath() {
		return this.pdfReaderPath;
	}

	/**
	 * defini la langue de l'IHM
	 * @param langue  - la langue de l'application
	 */
	public void setLangue(String langue) {
		this.langue = langue;
	}

	/**
	 * @return the reglementName
	 */
	public String getReglementName() {
		return reglementName;
	}

	/**
	 * @param reglementName the reglementName to set
	 */
	public void setReglementName(String reglementName) {
		this.reglementName = reglementName;
	}

	/**
	 * defini l'adresse du lecteur pdf
	 * @param pdfReaderPath  - l'adresse du lecteur pdf
	 */
	public void setPdfReaderPath(String pdfReaderPath) {
		this.pdfReaderPath = pdfReaderPath;
	}

	/**
	 * nombre de colonne et de ligne sur une page d'étiquettes
	 * @return  Returns the colonneAndLigne.
	 */
	public int[] getColonneAndLigne() {
		return this.colonneAndLigne;
	}

	/**
	 * Espacements entre 2 cellules d'étiquettes
	 * @return  Returns the espacements.
	 */
	public double[] getEspacements() {
		return this.espacements;
	}


	/**
	 * Format du papier étiquettes
	 * @return  Returns the formatPapier.
	 */
	public String getFormatPapier() {
		return this.formatPapier;
	}

	/**
	 * Orientation du papier étiquettes
	 * @return  Returns the orientation.
	 */
	public String getOrientation() {
		return this.orientation;
	}

	/**
	 * Marge d'impression des étiquettes
	 * @return  Returns the marges.
	 */
	public Marges getMarges() {
		return this.marges;
	}

	/**
	 * Défini le nombre de colonne et ligne d'étiquettes
	 * @param colonneAndLigne  The colonneAndLigne to set.
	 */
	public void setColonneAndLigne(int[] colonneAndLigne) {
		this.colonneAndLigne = colonneAndLigne;
	}

	/**
	 * Définit les espacementes entres cellules d'étiquettes
	 * @param espacements  The espacements to set.
	 */
	public void setEspacements(double[] espacements) {
		this.espacements = espacements;
	}

	/**
	 * Définit le format du papier étiquettes utilisé
	 * @param formatPapier  The formatPapier to set.
	 */
	public void setFormatPapier(String formatPapier) {
		this.formatPapier = formatPapier;
	}

	/**
	 * Définit l'orientation de la feuille d'étiquettes
	 * @param orientation  The orientation to set.
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	/**
	 * Définit les marges d'impression du papier étiquettes
	 * @param marges  The marges to set.
	 */
	public void setMarges(Marges marges) {
		this.marges = marges;
	}

	/**
	 * Donne le nom du profil courrant
	 * @return  Returns the curProfil.
	 */
	public String getCurProfil() {
		return curProfil;
	}


	/**
	 * Définit le nom du profil courrant
	 * @param curProfil  The curProfil to set.
	 */
	public void setCurProfil(String curProfil) {
		this.curProfil = curProfil;
	}

	/**
	 * Détéremine si c'est le premier lancement de l'application
	 * @return  Returns the firstboot.
	 */
	public boolean isFirstboot() {
		return firstboot;
	}

	/**
	 * Place l'application sur premier lancement
	 * @param firstboot  The firstboot to set.
	 */
	public void setFirstboot(boolean firstboot) {
		this.firstboot = firstboot;
	}

	/**
	 * Est ce que les champs de cumul doivent être affiché?
	 * @return  Renvoie interfaceResultatCumul.
	 */
	public boolean isInterfaceResultatCumul() {
		return interfaceResultatCumul;
	}

	/**
	 * Définit si les champs de cumul doivent être affiché
	 * @param interfaceResultatCumul  interfaceResultatCumul à définir.
	 */
	public void setInterfaceResultatCumul(boolean interfaceResultatCumul) {
		this.interfaceResultatCumul = interfaceResultatCumul;
	}

	/**
	 * Est ce qu'on affiche la saisie des 10/9/M
	 * @return  Renvoie interfaceResultatSupl.
	 */
	public boolean isInterfaceResultatSupl() {
		return interfaceResultatSupl;
	}

	/**
	 * Définit si l'on affiche la saisie des 10/9/M
	 * @param interfaceResultatSupl  interfaceResultatSupl à définir.
	 */
	public void setInterfaceResultatSupl(boolean interfaceResultatSupl) {
		this.interfaceResultatSupl = interfaceResultatSupl;
	}

	/**
	 * Est ce qu'on met en surbriance les ex-aequo?
	 * @return  Renvoie interfaceAffResultatExEquo.
	 */
	public boolean isInterfaceAffResultatExEquo() {
		return interfaceAffResultatExEquo;
	}

	/**
	 * Définit si l'on met en surbriance les ex-aequo
	 * @param interfaceAffResultatExEquo  interfaceAffResultatExEquo à définir.
	 */
	public void setInterfaceAffResultatExEquo(boolean interfaceAffResultatExEquo) {
		this.interfaceAffResultatExEquo = interfaceAffResultatExEquo;
	}

	/**
	 * Donne le chemin du logo du club
	 * @return  Renvoie logoPath.
	 */
	public String getLogoPath() {
		return logoPath;
	}

	/**
	 * Définit le chemin du logo du club
	 * @param logoPath  logoPath à définir.
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	
	/**
	 * @return the metaDataFichesConcours
	 */
	public MetaDataFichesConcours getMetaDataFichesConcours() {
		return metaDataFichesConcours;
	}

	/**
	 * @param metaDataFichesConcours the metaDataFichesConcours to set
	 */
	public void setMetaDataFichesConcours(
			MetaDataFichesConcours metaDataFichesConcours) {
		this.metaDataFichesConcours = metaDataFichesConcours;
	}

	/**
	 * @return useProxy
	 */
	public boolean isUseProxy() {
		return useProxy;
	}

	/**
	 * @param useProxy useProxy à définir
	 */
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	/**
	 * @return proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy proxy à définir
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}


	/**
	 * sauvegarde la configuration général du programme
	 *
	 */
	public void save() {
		File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + 
				File.separator + 
				CONFIG_PROFILE + curProfil + EXT_XML);
		try {
			AJToolKit.saveMarshallStructure(f, this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAsDefault() {
		try {
			File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + 
					File.separator + 
					ConcoursJeunes.ajrParametreAppli.getResourceString("file.configuration")); //$NON-NLS-1$
			try {
				AJToolKit.saveMarshallStructure(f, this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} catch(NullPointerException npe) {
			npe.printStackTrace();
		}
	}
	
	@Override
	public Configuration clone() {
		try {
			return (Configuration)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this;
		}
	}
}