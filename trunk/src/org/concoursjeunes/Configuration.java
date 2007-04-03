/*
 * Created on 18 janv. 2005
 *
 */
package org.concoursjeunes;

import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import ajinteractive.standard.java2.AJToolKit;

/**
 * parametre de configuration de l'application
 * 
 * @author  Aurelien Jeoffray
 * @version  3.0
 */
@XmlRootElement
public class Configuration extends DefaultParameters {

	private String langue           = "";               //$NON-NLS-1$
	private String logoPath         = "ressources/logo_ffta.gif";   //$NON-NLS-1$

	private String pdfReaderPath    = "";               //$NON-NLS-1$
	private String importURL        = "http://";        //$NON-NLS-1$
	private String exportURL        = "http://";        //$NON-NLS-1$

	private String formatPapier     = "A4";             //$NON-NLS-1$
	private String orientation      = "portrait";       //$NON-NLS-1$
	private int[] colonneAndLigne   = new int[] {9, 3}; 
	private Marges marges           = new Marges(0, 0, 0, 0);
	private double[] espacements    = new double[] {0.5, 0.5};

	private boolean interfaceResultatCumul = false;     //noreboot
	private boolean interfaceResultatSupl = true;       //noreboot
	private boolean interfaceAffResultatExEquo = true;  //noreboot

	//propriete caché
	private boolean firstboot       = false;            //noreboot
	private String curProfil        = "";               //noreboot //$NON-NLS-1$

	public Configuration() {

	}
	
	/**
	 * Retourne la langue courante de l'IHM
	 * @return  String - le code langue
	 * @uml.property  name="langue"
	 */
	public String getLangue() {
		return this.langue;
	}

	/**
	 * Retourne l'adresse du lecteur pdf
	 * @return  String - l'adresse du lecteur pdf
	 * @uml.property  name="pdfReaderPath"
	 */
	public String getPdfReaderPath() {
		return this.pdfReaderPath;
	}

	/**
	 * defini la langue de l'IHM
	 * @param langue  - la langue de l'application
	 * @uml.property  name="langue"
	 */
	public void setLangue(String langue) {
		this.langue = langue;
	}

	/**
	 * defini l'adresse du lecteur pdf
	 * @param pdfReaderPath  - l'adresse du lecteur pdf
	 * @uml.property  name="pdfReaderPath"
	 */
	public void setPdfReaderPath(String pdfReaderPath) {
		this.pdfReaderPath = pdfReaderPath;
	}

	/**
	 * nombre de colonne et de ligne sur une page d'étiquettes
	 * @return  Returns the colonneAndLigne.
	 * @uml.property  name="colonneAndLigne"
	 */
	public int[] getColonneAndLigne() {
		return this.colonneAndLigne;
	}

	/**
	 * Espacements entre 2 cellules d'étiquettes
	 * @return  Returns the espacements.
	 * @uml.property  name="espacements"
	 */
	public double[] getEspacements() {
		return this.espacements;
	}

	/**
	 * URL pour l'exportation des données
	 * @return  Returns the exportURL.
	 * @uml.property  name="exportURL"
	 */
	public String getExportURL() {
		return this.exportURL;
	}

	/**
	 * Format du papier étiquettes
	 * @return  Returns the formatPapier.
	 * @uml.property  name="formatPapier"
	 */
	public String getFormatPapier() {
		return this.formatPapier;
	}

	/**
	 * Orientation du papier étiquettes
	 * @return  Returns the orientation.
	 * @uml.property  name="orientation"
	 */
	public String getOrientation() {
		return this.orientation;
	}

	/**
	 * URL d'importation des données
	 * @return  Returns the importURL.
	 * @uml.property  name="importURL"
	 */
	public String getImportURL() {
		return this.importURL;
	}

	/**
	 * Marge d'impression des étiquettes
	 * @return  Returns the marges.
	 * @uml.property  name="marges"
	 */
	public Marges getMarges() {
		return this.marges;
	}

	/**
	 * Défini le nombre de colonne et ligne d'étiquettes
	 * @param colonneAndLigne  The colonneAndLigne to set.
	 * @uml.property  name="colonneAndLigne"
	 */
	public void setColonneAndLigne(int[] colonneAndLigne) {
		this.colonneAndLigne = colonneAndLigne;
	}

	/**
	 * Définit les espacementes entres cellules d'étiquettes
	 * @param espacements  The espacements to set.
	 * @uml.property  name="espacements"
	 */
	public void setEspacements(double[] espacements) {
		this.espacements = espacements;
	}

	/**
	 * Définit l'URL d'export de donnée
	 * @param exportURL  The exportURL to set.
	 * @uml.property  name="exportURL"
	 */
	public void setExportURL(String exportURL) {
		this.exportURL = exportURL;
	}

	/**
	 * Définit le format du papier étiquettes utilisé
	 * @param formatPapier  The formatPapier to set.
	 * @uml.property  name="formatPapier"
	 */
	public void setFormatPapier(String formatPapier) {
		this.formatPapier = formatPapier;
	}

	/**
	 * Définit l'orientation de la feuille d'étiquettes
	 * @param orientation  The orientation to set.
	 * @uml.property  name="orientation"
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	/**
	 * Définit l'URL d'import des données de concours
	 * @param importURL  The importURL to set.
	 * @uml.property  name="importURL"
	 */
	public void setImportURL(String importURL) {
		this.importURL = importURL;
	}

	/**
	 * Définit les marges d'impression du papier étiquettes
	 * @param marges  The marges to set.
	 * @uml.property  name="marges"
	 */
	public void setMarges(Marges marges) {
		this.marges = marges;
	}

	/**
	 * Donne le nom du profil courrant
	 * @return  Returns the curProfil.
	 * @uml.property  name="curProfil"
	 */
	public String getCurProfil() {
		return curProfil;
	}


	/**
	 * Définit le nom du profil courrant
	 * @param curProfil  The curProfil to set.
	 * @uml.property  name="curProfil"
	 */
	public void setCurProfil(String curProfil) {
		this.curProfil = curProfil;
	}

	/**
	 * Détéremine si c'est le premier lancement de l'application
	 * @return  Returns the firstboot.
	 * @uml.property  name="firstboot"
	 */
	public boolean isFirstboot() {
		return firstboot;
	}

	/**
	 * Place l'application sur premier lancement
	 * @param firstboot  The firstboot to set.
	 * @uml.property  name="firstboot"
	 */
	public void setFirstboot(boolean firstboot) {
		this.firstboot = firstboot;
	}

	/**
	 * Est ce que les champs de cumul doivent être affiché?
	 * @return  Renvoie interfaceResultatCumul.
	 * @uml.property  name="interfaceResultatCumul"
	 */
	public boolean isInterfaceResultatCumul() {
		return interfaceResultatCumul;
	}

	/**
	 * Définit si les champs de cumul doivent être affiché
	 * @param interfaceResultatCumul  interfaceResultatCumul à définir.
	 * @uml.property  name="interfaceResultatCumul"
	 */
	public void setInterfaceResultatCumul(boolean interfaceResultatCumul) {
		this.interfaceResultatCumul = interfaceResultatCumul;
	}

	/**
	 * Est ce qu'on affiche la saisie des 10/9/M
	 * @return  Renvoie interfaceResultatSupl.
	 * @uml.property  name="interfaceResultatSupl"
	 */
	public boolean isInterfaceResultatSupl() {
		return interfaceResultatSupl;
	}

	/**
	 * Définit si l'on affiche la saisie des 10/9/M
	 * @param interfaceResultatSupl  interfaceResultatSupl à définir.
	 * @uml.property  name="interfaceResultatSupl"
	 */
	public void setInterfaceResultatSupl(boolean interfaceResultatSupl) {
		this.interfaceResultatSupl = interfaceResultatSupl;
	}

	/**
	 * Est ce qu'on met en surbriance les ex-aequo?
	 * @return  Renvoie interfaceAffResultatExEquo.
	 * @uml.property  name="interfaceAffResultatExEquo"
	 */
	public boolean isInterfaceAffResultatExEquo() {
		return interfaceAffResultatExEquo;
	}

	/**
	 * Définit si l'on met en surbriance les ex-aequo
	 * @param interfaceAffResultatExEquo  interfaceAffResultatExEquo à définir.
	 * @uml.property  name="interfaceAffResultatExEquo"
	 */
	public void setInterfaceAffResultatExEquo(boolean interfaceAffResultatExEquo) {
		this.interfaceAffResultatExEquo = interfaceAffResultatExEquo;
	}

	/**
	 * Donne le chemin du logo du club
	 * @return  Renvoie logoPath.
	 * @uml.property  name="logoPath"
	 */
	public String getLogoPath() {
		return logoPath;
	}

	/**
	 * Définit le chemin du logo du club
	 * @param logoPath  logoPath à définir.
	 * @uml.property  name="logoPath"
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	
	/**
	 * sauvegarde la configuration général du programme
	 *
	 */
	public void saveConfig() {
		/*try {
			File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + 
					File.separator + "test_" + 
					ConcoursJeunes.ajrParametreAppli.getResourceString("file.configuration")); //$NON-NLS-1$
			
			//on crée un contexte JAXB pour la classe Person
			JAXBContext context = JAXBContext.newInstance(Configuration.class);

			//on crée un marshaller à partir du contexte
			Marshaller m = context.createMarshaller();

			//on veut un affichage formatté
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// on demande au marshaller de générer le XML de la personne "serge"
			// et de l'afficher dans la console
			
			m.marshal(this, new FileOutputStream(f));

		} catch (JAXBException ex) {
			ex.printStackTrace();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}*/
		try {
			File f = new File(ConcoursJeunes.userRessources.getConfigPathForUser() + 
					File.separator + 
					ConcoursJeunes.ajrParametreAppli.getResourceString("file.configuration")); //$NON-NLS-1$
			AJToolKit.saveXMLStructure(f, ConcoursJeunes.configuration, false);
		} catch(NullPointerException npe) {
			npe.printStackTrace();
		}
	}
}