/*
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

package org.concoursjeunes;

import static org.concoursjeunes.ApplicationCore.staticParameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.concoursjeunes.manager.ReglementManager;

/**
 * Paramètre d'un concours
 * 
 * @author Aurélien Jeoffray
 * @version 3.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Parametre extends DefaultParameters {
	
	private String lieuConcours		= ""; //$NON-NLS-1$
	private CompetitionLevel niveauChampionnat = new CompetitionLevel();
	private Date dateDebutConcours	= new Date();
	private Date dateFinConcours	= new Date();
	private boolean open			= true;
	private boolean duel			= false;
	@XmlElementWrapper(name="arbitres",required=false)
	@XmlElement(name="arbitre")
	private List<String> arbitres;
	@XmlElementWrapper(name="judges",required=false)
	@XmlElement(name="judge")
	private List<Judge> judges		= new ArrayList<Judge>();
	private Reglement reglement		= new Reglement();

	private String saveName         = UUID.randomUUID().toString()
			+ staticParameters.getResourceString("extention.concours"); //$NON-NLS-1$
	
	private boolean reglementLock = false;

	/**
	 * 
	 *
	 */
	public Parametre() {
		
	}
	
	/**
	 * Construit un nouvel objet paramètre en initialisant ses valeurs par défaut
	 * à partir d'une configuration.
	 * 
	 * @param configuration la configuration servant de référence pour l'attribution des
	 * valeurs de l'objet
	 */
	public Parametre(Configuration configuration) {
		ReglementManager reglementManager = new ReglementManager();
		
		setClub(configuration.getClub());
		setIntituleConcours(configuration.getIntituleConcours());
		setLieuConcours(configuration.getClub().getVille());
		setNbCible(configuration.getNbCible());
		setNbTireur(configuration.getNbTireur());
		setNbDepart(configuration.getNbDepart());
		
		setReglement(reglementManager.getReglementByName(configuration.getReglementName()));
	}
	
	/**
	 * Donne le lieu d'organisation du concours
	 * 
	 * @return le lieu d'organisation du concours
	 */
	public String getLieuConcours() {
		return lieuConcours;
	}

	/**
	 * Définit le lieu d'organisation du concours
	 * 
	 * @param lieuConcours le lieu d'organisation du concours
	 */
	public void setLieuConcours(String lieuConcours) {
		String oldValue = this.lieuConcours;
		
		this.lieuConcours = lieuConcours;
		
		pcs.firePropertyChange("lieuConcours", oldValue, lieuConcours); //$NON-NLS-1$
	}

	/**
	 * Retourne la date de fin du concours
	 * 
	 * @return dateFinConcours la date de fin du concours
	 */
	public Date getDateFinConcours() {
		return dateFinConcours;
	}

	/**
	 * Définit la date de fin du concours
	 * 
	 * @param dateFinConcours la date de fin du concours
	 */
	public void setDateFinConcours(Date dateFinConcours) {
		Date oldValue = this.dateFinConcours;
		
		this.dateFinConcours = dateFinConcours;
		
		pcs.firePropertyChange("dateFinConcours", oldValue, dateFinConcours); //$NON-NLS-1$
	}

	/**
	 * Donne la date de début du concours
	 * 
	 * <i>Remplacé par {@link #getDateDebutConcours()}</i>
	 * 
	 * @return Date - la date du concours;
	 */
	@Deprecated
	public Date getDate() {
		return getDateDebutConcours();
	}
	
	/**
	 * Donne la date de début du concours
	 * 
	 * @return Date - la date du concours;
	 */
	public Date getDateDebutConcours() {
		return dateDebutConcours;
	}

	/**
	 * Spécifie la date du concours
	 * <i>Remplacé par {@link #setDateDebutConcours(Date)}</i>
	 * 
	 * @param dDateConcours la date du concours
	 */
	@Deprecated
	public void setDate(Date dDateConcours) {
		setDateDebutConcours(dDateConcours);
	}
	
	/**
	 * Spécifie la date de début du concours
	 * 
	 * @param dDateConcours la date du concours
	 */
	public void setDateDebutConcours(Date dDateConcours) {
		Date oldValue = this.dateDebutConcours;
		
		this.dateDebutConcours = dDateConcours;
		
		pcs.firePropertyChange("dateDebutConcours", oldValue, dDateConcours); //$NON-NLS-1$
	}
	
	/**
	 * Retourne le niveau de la compétition
	 * 
	 * @return niveauChampionnat le niveau de la compétition
	 */
	public CompetitionLevel getNiveauChampionnat() {
		return niveauChampionnat;
	}

	/**
	 * Définit le niveau de la compétition
	 * 
	 * @param niveauChampionnat le niveau de la compétition
	 */
	public void setNiveauChampionnat(CompetitionLevel niveauChampionnat) {
		CompetitionLevel oldValue = this.niveauChampionnat;
		
		this.niveauChampionnat = niveauChampionnat;
		
		pcs.firePropertyChange("niveauChampionnat", oldValue, niveauChampionnat); //$NON-NLS-1$
	}

	/**
	 * Indique si c'est un concours ouvert ou fermé.
	 * 
	 * @return open true si le concours est ouvert
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Définit si c'est un concours ouvert ou fermé.
	 * 
	 * @param open true si le concours est ouvert
	 */
	public void setOpen(boolean open) {
		boolean oldValue = this.open;
		
		this.open = open;
		
		pcs.firePropertyChange("open", oldValue, open); //$NON-NLS-1$
	}

	/**
	 * Indique si on a une gestion des duels sur le concours ou non
	 * 
	 * @return true si on a une gestion des duels
	 */
	public boolean isDuel() {
		return duel;
	}

	/**
	 * Définit si on a une gestion des duels sur le concours ou non
	 * 
	 * @param duel true si on a une gestion des duels
	 */
	public void setDuel(boolean duel) {
		boolean oldValue = this.duel;
		
		this.duel = duel;
		
		pcs.firePropertyChange("duel", oldValue, duel); //$NON-NLS-1$
	}

	/**
	 * Donne la liste des arbitres
	 * 
	 * @deprecated replacé par {@link #getJudges()}
	 * 
	 * @return la liste des arbitres
	 */
	@Deprecated
	public List<String> getArbitres() {
		if(arbitres == null)
			return new ArrayList<String>();
		return arbitres;
	}

	/**
	 * Spécifie la liste des arbitres
	 * 
	 * @deprecated remplacé par [{@link #setJudges(List)}
	 * 
	 * @param arbitres
	 */
	@Deprecated
	public void setArbitres(List<String> arbitres) {
		this.arbitres = arbitres;
	}

	/**
	 * Donne la liste des arbitres
	 * 
	 * @return judges la liste des arbitres
	 */
	public List<Judge> getJudges() {
		return judges;
	}

	/**
	 * Définit la liste des arbitres
	 * 
	 * @param judges la liste des arbitres du concours
	 */
	public void setJudges(List<Judge> judges) {
		List<Judge> oldValue = this.judges;
		
		this.judges = judges;
		
		pcs.firePropertyChange("arbitres", oldValue, judges); //$NON-NLS-1$
	}

	/**
	 * Donne le nom du fichier de sauvegarde du concours
	 * 
	 * @return le nom du fichier de sauvegarde du concours
	 */
	public String getSaveName() {
		return saveName;
	}

	/**
	 * définit le nom de sauvegarde des infos du concours
	 * 
	 * @param saveName le nom de sauvegarde des infos du concours
	 */
	public void setSaveName(String saveName) {
		String oldValue = this.saveName; 
		
		this.saveName = saveName;
		
		pcs.firePropertyChange("saveName", oldValue, saveName); //$NON-NLS-1$
	}
	
	/**
	 * Retourne le règlement appliqué au concours
	 * 
	 * @return le règlement appliqué 
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * Définit le règlement à appliqué au concours
	 * 
	 * @param reglement le règlement à appliquer
	 */
	public void setReglement(Reglement reglement) {
		Reglement oldValue = this.reglement;
		
		this.reglement = reglement;
		
		pcs.firePropertyChange("reglement", oldValue, reglement); //$NON-NLS-1$
	}

	/**
	 * Indique si le règlement du concours peut ou non être changé
	 * 
	 * @return true si le règlement peut être choisi, false sinon
	 */
	public boolean isReglementLock() {
		return reglementLock;
	}

	/**
	 * Définit le statut de changement du règlement
	 * 
	 * @param reglementLock true le règlement peut être changé, false il ne peut pas
	 */
	public void setReglementLock(boolean reglementLock) {
		boolean oldValue = this.reglementLock;
		
		this.reglementLock = reglementLock;
		
		pcs.firePropertyChange("reglementLock", oldValue, reglementLock); //$NON-NLS-1$
	}
}