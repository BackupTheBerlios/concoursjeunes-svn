/*
 * Created on 7 janv. 2005
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

/**
 * Reprensentation de l'entite 'Cible' avec ses parametres de distances, de
 * blason appliqué et les concurrents qui y sont associés
 * 
 * @author Aurelien Jeoffray
 * @version 2.2
 */
public class Cible {

	private int numCible = 0; // le numero de la cible
	private FicheConcours concours;
	private final Concurrent[] concurrents; // le liste des concurrents présents
	// sur la cible
	private int nbArcher = 0; // le nombre d'archer sur la cible
	private int nbHandicap = 0;

	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Initialise une cible avec son numero et son nombre max d'archer<br>
	 * la numerotation est de type 1 à n et non de 0 à n-1
	 * 
	 * @param numCible -
	 *            le numero de la cible
	 * @param ficheConcours -
	 *            La fiche concours associé à la cible
	 */
	public Cible(int iNumCible, FicheConcours concours) {
		this.numCible = iNumCible;
		this.concours = concours;
		this.concurrents = new Concurrent[concours.getParametre().getNbTireur()];
	}

	/**
	 * Ajoute un auditeur à la cible
	 * 
	 * @param cibleListener -
	 *            l'auditeur devant être mis au courrant des évenements de la
	 *            cible
	 */
	public void addCibleListener(CibleListener cibleListener) {
		listeners.add(CibleListener.class, cibleListener);
	}

	/**
	 * Désabonne un auditeur de la cible
	 * 
	 * @param cibleListener -
	 *            l'auditeur devant être supprimé de la liste de notification
	 */
	public void removeCibleListener(CibleListener cibleListener) {
		listeners.remove(CibleListener.class, cibleListener);
	}

	/**
	 * Retourne le numero de la cible
	 * 
	 * @return int - le numero de la cible
	 */
	public int getNumCible() {
		return numCible;
	}

	/**
	 * Retourne le nombre d'archer present sur la cible
	 * 
	 * @return int - le nombre d'archer sur la cible
	 */
	public int getNbArcher() {
		return nbArcher;
	}

	/**
	 * @return nbHandicap
	 */
	public int getNbHandicap() {
		return nbHandicap;
	}

	/**
	 * Retourne le nombre maximum d'archers pouvant être accepté sur la cible
	 * 
	 * @return le nombre maximum d'archer sur la cible
	 */
	public int getNbMaxArchers() {
		return concurrents.length;
	}

	/**
	 * Retourne la Fiche Concours associé à la cible
	 * 
	 * @return la Fiche Concours associé à la cible
	 */
	public FicheConcours getFicheConcours() {
		return concours;
	}

	/**
	 * Associe la cible à un concours donné
	 * 
	 * @param concours le concours associé à la cible
	 */
	public void setFicheConcours(FicheConcours concours) {
		this.concours = concours;
	}

	/**
	 * insere un concurrent à la premiere position libre et retourne cette
	 * position ou -1 si echec
	 * 
	 * @param concurrent le concurrent à inserer
	 * 
	 * @return la position de concurrent ou -1 si echec
	 */
	public int insertConcurrent(Concurrent concurrent) {
		int position = -1;

		if (concurrent != null) {
			//verifie qu'il reste des places disponible
			if(nbArcher < concours.getParametre().getNbTireur() - nbHandicap) {
				//on valide l'insertion si il n'y a aucun archer sur la cible
				//OU si les autre archers sont à la même distance
				if ((nbArcher > 0 && DistancesEtBlason.getDistancesEtBlasonForConcurrent(concours.getParametre().getReglement(), concurrent).equals(
						getDistancesEtBlason()))
						|| nbArcher == 0) {
					//si l'archer est handicapé, vérifié que les condition spécifique sont remplis
					if(concurrent.isHandicape()) {
						if(nbArcher < concours.getParametre().getNbTireur() - (nbHandicap+1)) {
							//dans le cas d'un archer handicapé, on boucle sur les emplacements 2 à 2
							for (int i = 0; i < concurrents.length; i+=2) {
								if (concurrents[i] == null && concurrents[i+1] == null) {
									concurrent.setCible(numCible);
									concurrent.setPosition(i);
									
									concurrents[i] = concurrent;
									nbArcher++;
									nbHandicap++;
									
									fireConcurrentJoined(concurrent);
									
									position = i;
									
									break;
								}
							}
						}
					} else {
						//on boucle sur les emplacements et on remplit le premier qui est libre
						for (int i = 0; i < concurrents.length; i++) {
							if (concurrents[i] == null) {
								if(i > 0 && concurrents[i-1].isHandicape())
									continue;
								concurrent.setCible(numCible);
								concurrent.setPosition(i);
		
								concurrents[i] = concurrent;
								nbArcher++;
		
								fireConcurrentJoined(concurrent);
		
								position = i;
		
								break;
							}
						}
					}
				}
			}
		}

		return position;
	}

	/**
	 * Donne le concurrent à la postition x
	 * 
	 * @param position
	 * @return Concurrent - le concurrent à la position donnée ou <i>null</i>
	 *         si aucun concurrent trouvé
	 */
	public Concurrent getConcurrentAt(int position) {
		if (position < concours.getParametre().getNbTireur())
			return concurrents[position];
		return null;
	}

	/**
	 * Retourne l'ensemble des concurrents présent sur la cible
	 * 
	 * @return la liste des concurrents présent sur la cible
	 */
	public ArrayList<Concurrent> getAllConcurrents() {
		ArrayList<Concurrent> lstConcurrent = new ArrayList<Concurrent>();
		for (Concurrent concurrent : concurrents) {
			if (concurrent != null)
				lstConcurrent.add(concurrent);
		}
		return lstConcurrent;
	}

	/**
	 * Retourne la position du concurrent donné sur la cible
	 * 
	 * @param concurrent le concurrent à rechercher sur la cible
	 * @return l'indice du concurrent sur la cible ou -1 si non trouvé
	 */
	public int indexOf(Concurrent concurrent) {
		for (int i = 0; i < concours.getParametre().getNbTireur(); i++)
			if (concurrents[i] != null && concurrents[i].equals(concurrent))
				return i;
		return -1;
	}

	/**
	 * Test si le concurrent transmis en parametre est affecté ou non à la cible
	 * 
	 * @param concurrent le concurrent à tester
	 * @return true si le concurrent est présent sur la cible, false sinon
	 */
	public boolean contains(Concurrent concurrent) {
		return indexOf(concurrent) != -1;
	}

	/**
	 * Place un concurrent sur la cible à la position donné
	 * 
	 * @param concurrent le concurrent à placer
	 * @param position la positionn de ce concurrent
	 * 
	 * @return true si affectation avec succès, false sinon
	 */
	public boolean setConcurrentAt(Concurrent concurrent, int position) {
		if (concurrent != null) {
			if (position == -1) {
				return insertConcurrent(concurrent) > -1;
			} else if (position >= 0 && position < concours.getParametre().getNbTireur()) {
				if ((nbArcher > 0 && DistancesEtBlason.getDistancesEtBlasonForConcurrent(
						concours.getParametre().getReglement(), concurrent).equals(getDistancesEtBlason()))
						|| nbArcher == 0) {
					if(concurrent.isHandicape() && position % 2 != 0)
						return false;
					concurrent.setCible(numCible);
					concurrent.setPosition(position);
					if (concurrents[position] != null)
						removeConcurrentAt(position);
					//FIXME possible depacement de capacité si nb de place impair
					if (concurrent.isHandicape() && concurrents[position+1] != null)
						removeConcurrentAt(position);
					nbArcher++;
					if (concurrent.isHandicape())
						nbHandicap++;
					concurrents[position] = concurrent;
					fireConcurrentJoined(concurrent);

					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Supprime un concurrent à la position donnée
	 * 
	 * @param position -
	 *            la position du concurrent à supprimer
	 */
	public void removeConcurrentAt(int position) {
		if (position < concours.getParametre().getNbTireur() && concurrents[position] != null) {
			nbArcher--;

			Concurrent removedConcurrent = concurrents[position];
			removedConcurrent.setCible(0);
			if(removedConcurrent.isHandicape())
				nbHandicap--;

			concurrents[position] = null;

			fireConcurrentQuit(removedConcurrent);
		}
	}

	/**
	 * Supprime un concurrent donné
	 * 
	 * @param concurrent -
	 *            le concurrent à supprimer
	 */
	public void removeConcurrent(Concurrent concurrent) {
		for (int i = 0; i < concurrents.length; i++) {
			if (concurrents[i] == concurrent) {
				removeConcurrentAt(i);
				break;
			}
		}
	}

	/**
	 * Retire tous les concurrents de la cible
	 * 
	 */
	public void removeAll() {
		for (int i = 0; i < concurrents.length; i++)
			removeConcurrentAt(i);
	}

	/**
	 * Donne la disposition de la cible
	 * 
	 * @return DistancesEtBlason - la disposition de la cible
	 */
	public DistancesEtBlason getDistancesEtBlason() {
		DistancesEtBlason db = null;

		if (nbArcher > 0) {
			Concurrent firstConcurrent = null;
			for (Concurrent concurrent : concurrents) {
				if (concurrent != null) {
					firstConcurrent = concurrent;
					break;
				}
			}

			db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(concours.getParametre().getReglement(), firstConcurrent);
		}

		return db;
	}

	/**
	 * Renvoie le Libelle qualifié de la cible
	 * 
	 */
	@Override
	public String toString() {
		String strCouleur = "<font color=\"#00AA00\">"; //$NON-NLS-1$
		if (concours.getParametre().getNbTireur() == nbArcher)
			strCouleur = "<font color=\"#0000FF\">"; //$NON-NLS-1$
		String strCibleLibelle = "<html>" + strCouleur + "<b>" + ConcoursJeunes.ajrLibelle.getResourceString("treenode.cible") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ((this.numCible < 10) ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
				+ this.numCible + "</b> ("; //$NON-NLS-1$

		DistancesEtBlason db = getDistancesEtBlason();
		if (db != null) {
			for (int i = 0; i < db.getDistance().length; i++) {
				if (i == 0 || (i > 0 && db.getDistance()[i] != db.getDistance()[i - 1])) {
					if (i > 0)
						strCibleLibelle += "/"; //$NON-NLS-1$
					strCibleLibelle += db.getDistance()[i] + "m"; //$NON-NLS-1$
				}
			}
			strCibleLibelle += ", " + db.getBlason() + "cm"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		strCibleLibelle += ") (" + this.nbArcher + "/" + concours.getParametre().getNbTireur() + ")</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Hashtable<Entite, Integer> nbArcherByClub = new Hashtable<Entite, Integer>();
		for (Concurrent concurrent : concurrents) {
			if(concurrent != null) {
				if(!nbArcherByClub.containsKey(concurrent.getClub()))
					nbArcherByClub.put(concurrent.getClub(), 0);
				nbArcherByClub.put(concurrent.getClub(), nbArcherByClub.get(concurrent.getClub())+1);
			}
		}
		
		if (nbArcherByClub.size() == 1 && getNbArcher() > 1)
			strCibleLibelle += ConcoursJeunes.ajrLibelle.getResourceString("target.sameclub"); //$NON-NLS-1$
		else if (getNbArcher() == 1)
			strCibleLibelle += ConcoursJeunes.ajrLibelle.getResourceString("target.onlyone"); //$NON-NLS-1$
		else {
			for(Entry<Entite, Integer> nbarch : nbArcherByClub.entrySet()) {
				if(nbarch.getValue() > 2) {
					strCibleLibelle += ConcoursJeunes.ajrLibelle.getResourceString("target.morethan2sameclub"); //$NON-NLS-1$
					break;
				}
			}
		}
		strCibleLibelle += "</html>"; //$NON-NLS-1$

		return strCibleLibelle;
	}

	/**
	 * Donne un libelle textuel pour une position
	 * 
	 * @param cible -
	 *            la cible de la position
	 * @param position -
	 *            l'index de la position
	 * @return String - le libelle de la position
	 */
	public static String getCibleLibelle(int cible, int position) {
		return ((cible < 10) ? "0" : "") + cible + (char) ('A' + position); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void fireConcurrentJoined(Concurrent concurrent) {
		for (CibleListener cibleListener : listeners.getListeners(CibleListener.class)) {
			cibleListener.concurrentJoined(new CibleEvent(concurrent, this));
		}
	}

	private void fireConcurrentQuit(Concurrent concurrent) {
		for (CibleListener cibleListener : listeners.getListeners(CibleListener.class)) {
			cibleListener.concurrentQuit(new CibleEvent(concurrent, this));
		}
	}
}