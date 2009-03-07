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

import static org.concoursjeunes.ApplicationCore.ajrParametreAppli;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.concoursjeunes.builders.ReglementBuilder;

/**
 * Parametre d'un concours
 * 
 * @author Aurelien Jeoffray
 * @version 3.1
 */
public class Parametre extends DefaultParameters {
	
	private String lieuConcours		= ""; //$NON-NLS-1$
	private String niveauChampionnat = ""; //$NON-NLS-1$
	private Date dateDebutConcours	= new Date();
	private Date dateFinConcours	= new Date();
	private boolean open			= true;
	private boolean duel			= false;
	private String typeEquipe		= ""; //$NON-NLS-1$
	private List<String> arbitres	= new ArrayList<String>();
	private Reglement reglement		= new Reglement();

	private String saveName         = System.currentTimeMillis()
			+ ajrParametreAppli.getResourceString("extention.concours"); //$NON-NLS-1$
	
	private boolean reglementLock = false;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );

	/**
	 * 
	 *
	 */
	public Parametre() {
		
	}
	
	/**
	 * Construit un nouvel objet parametre en initialisant ses valeurs par defaut
	 * à partir d'une configuration.
	 * 
	 * @param configuration la configuration servant de référence pour l'attribution des
	 * valeurs de l'objet
	 */
	public Parametre(Configuration configuration) {
		setClub(configuration.getClub());
		setIntituleConcours(configuration.getIntituleConcours());
		setLieuConcours(configuration.getClub().getVille());
		setNbCible(configuration.getNbCible());
		setNbTireur(configuration.getNbTireur());
		setNbDepart(configuration.getNbDepart());
		setReglement(ReglementBuilder.getReglement(configuration.getReglementName()));
	}
	
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.removePropertyChangeListener(propertyChangeListener);
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
	 * @return dateFinConcours
	 */
	public Date getDateFinConcours() {
		return dateFinConcours;
	}

	/**
	 * @param dateFinConcours dateFinConcours à définir
	 */
	public void setDateFinConcours(Date dateFinConcours) {
		Date oldValue = this.dateFinConcours;
		this.dateFinConcours = dateFinConcours;
		pcs.firePropertyChange("dateFinConcours", oldValue, dateFinConcours); //$NON-NLS-1$
	}

	/**
	 * Donne la date de début du concours
	 * <i>Remplacé par {@link Parametre#getDateDebutConcours()}</i>
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
	 * specifie la date du concours
	 * <i>Remplacé par {@link Parametre#setDateDebutConcours(Date)}</i>
	 * 
	 * @param dDateConcours la date du concours
	 */
	@Deprecated
	public void setDate(Date dDateConcours) {
		setDateDebutConcours(dDateConcours);
	}
	/**
	 * specifie la date de début du concours
	 * 
	 * @param dDateConcours la date du concours
	 */
	public void setDateDebutConcours(Date dDateConcours) {
		Date oldValue = this.dateDebutConcours;
		this.dateDebutConcours = dDateConcours;
		pcs.firePropertyChange("dateDebutConcours", oldValue, dDateConcours); //$NON-NLS-1$
	}
	
	/**
	 * @return niveauChampionnat
	 */
	public String getNiveauChampionnat() {
		return niveauChampionnat;
	}

	/**
	 * @param niveauChampionnat niveauChampionnat à définir
	 */
	public void setNiveauChampionnat(String niveauChampionnat) {
		String oldValue = this.niveauChampionnat;
		this.niveauChampionnat = niveauChampionnat;
		pcs.firePropertyChange("niveauChampionnat", oldValue, niveauChampionnat); //$NON-NLS-1$
	}

	/**
	 * @return open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open open à définir
	 */
	public void setOpen(boolean open) {
		boolean oldValue = this.open;
		this.open = open;
		pcs.firePropertyChange("open", oldValue, open); //$NON-NLS-1$
	}

	/**
	 * @return duel
	 */
	public boolean isDuel() {
		return duel;
	}

	/**
	 * @param duel duel à définir
	 */
	public void setDuel(boolean duel) {
		boolean oldValue = this.duel;
		this.duel = duel;
		pcs.firePropertyChange("duel", oldValue, duel); //$NON-NLS-1$
	}

	/**
	 * @return typeEquipe
	 */
	public String getTypeEquipe() {
		return typeEquipe;
	}

	/**
	 * @param typeEquipe typeEquipe à définir
	 */
	public void setTypeEquipe(String typeEquipe) {
		String oldValue = this.typeEquipe;
		this.typeEquipe = typeEquipe;
		pcs.firePropertyChange("typeEquipe", oldValue, typeEquipe); //$NON-NLS-1$
	}

	/**
	 * Donne la liste des arbitres
	 * 
	 * @return la liste des arbitres
	 */
	public List<String> getArbitres() {
		return arbitres;
	}

	/**
	 * specifie la liste des arbitres
	 * 
	 * @param vArbitres
	 */
	public void setArbitres(List<String> vArbitres) {
		List<String> oldValue = this.arbitres;
		this.arbitres = vArbitres;
		pcs.firePropertyChange("arbitres", oldValue, dateDebutConcours); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.DefaultParameters#setIntituleConcours(java.lang.String)
	 */
	@Override
	public void setIntituleConcours(String intituleConcours) {
		String oldValue = getIntituleConcours();
		super.setIntituleConcours(intituleConcours);
		pcs.firePropertyChange("intituleConcours", oldValue, intituleConcours); //$NON-NLS-1$
	}
	
	/**
	 * Donne le chemin du fichier de sauvegarde du concours
	 * 
	 * @return  String
	 */
	public String getSaveName() {
		return saveName;
	}

	/**
	 * specifie le nom de sauvegarde des infos du concours
	 * 
	 * @param  saveName
	 */
	public void setSaveName(String saveName) {
		String oldValue = this.saveName; 
		this.saveName = saveName;
		pcs.firePropertyChange("saveName", oldValue, saveName); //$NON-NLS-1$
	}
	
	/**
	 * Retourne le reglement appliqué au concours
	 * 
	 * @return le reglement appliqué 
	 */
	public Reglement getReglement() {
		return reglement;
	}

	/**
	 * Définit le réglement à appliqué au concours
	 * 
	 * @param reglement le réglement à appliquer
	 */
	public void setReglement(Reglement reglement) {
		Reglement oldValue = this.reglement;
		this.reglement = reglement;
		pcs.firePropertyChange("reglement", oldValue, reglement); //$NON-NLS-1$
	}

	/**
	 * Indique si le reglement du concours peut ou non être changé
	 * 
	 * @return true si le reglement peut être choisi, false sinon
	 */
	public boolean isReglementLock() {
		return reglementLock;
	}

	/**
	 * Definit le statut de changement du reglement
	 * 
	 * @param reglementLock true le reglement peut être changé, false il ne peut pas
	 */
	public void setReglementLock(boolean reglementLock) {
		boolean oldValue = this.reglementLock;
		this.reglementLock = reglementLock;
		pcs.firePropertyChange("reglementLock", oldValue, reglementLock); //$NON-NLS-1$
	}
}