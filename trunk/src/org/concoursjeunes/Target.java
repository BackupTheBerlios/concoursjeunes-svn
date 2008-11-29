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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

import org.concoursjeunes.event.TargetEvent;
import org.concoursjeunes.event.TargetListener;
import org.concoursjeunes.exceptions.PlacementException;

/**
 * Reprensentation de l'entite 'Cible' avec ses parametres de distances, de
 * blason appliqué et les concurrents qui y sont associés
 * 
 * @author Aurelien Jeoffray
 * @version 2.2
 */
public class Target implements PropertyChangeListener {
	
	public enum Repartition {
		ABCD,
		ACBD
	}

	private int numCible = 0; // le numero de la cible
	private final Concurrent[] concurrents; // le liste des concurrents présents
	// sur la cible
	private int nbArcher = 0; // le nombre d'archer sur la cible
	private int nbHandicap = 0;
	
	//private int tailleCible = 0;
	private Reglement reglement;

	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Initialise une cible avec son numero et son nombre max d'archer<br>
	 * la numerotation est de type 1 à n et non de 0 à n-1
	 * 
	 * @param numCible le numero de la cible
	 * @param reglement le reglement régissant la cible
	 * @param nbTireurMaxparCible le nombre de tireur maximum pouvant être présent sur la cible
	 */
	public Target(int numCible, Reglement reglement, int nbTireurMaxparCible) {
		this.numCible = numCible;
		this.reglement = reglement;
		
		this.concurrents = new Concurrent[nbTireurMaxparCible];
	}

	/**
	 * Ajoute un auditeur à la cible
	 * 
	 * @param cibleListener -
	 *            l'auditeur devant être mis au courrant des évenements de la
	 *            cible
	 */
	public void addTargetListener(TargetListener cibleListener) {
		listeners.add(TargetListener.class, cibleListener);
	}

	/**
	 * Désabonne un auditeur de la cible
	 * 
	 * @param cibleListener -
	 *            l'auditeur devant être supprimé de la liste de notification
	 */
	public void removeTargetListener(TargetListener cibleListener) {
		listeners.remove(TargetListener.class, cibleListener);
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
	 * Retourne le nombre d'archer present sur la cible pour un DistancesEtBlason donnée
	 * 
	 * @param db le DistancesEtBlason pour lequelle retourner le nombre d'archers
	 * @return le nombre d'archer sur le DistancesEtBlason présent sur la cible
	 */
	public int getNbArcherFor(DistancesEtBlason db) {
		int nbArcher = 0;
		for(Concurrent concurrent : concurrents) {
			if(concurrent != null) {
				DistancesEtBlason concDb = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent);
				if(db.equals(concDb)) {
					nbArcher++;
				}
			}
		}
		return nbArcher;
	}

	/**
	 * Retourne le nombre d'archer handicapé rattaché à la cible
	 * 
	 * @return nbHandicap le nombre d'archer handicapé rattaché à la cible
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
	 * Retourne le nombre de position disponible pour l'objet DistancesEtBlason
	 * fournit en parametre
	 * 
	 * @param distancesEtBlason l'objet DistancesEtBlason permettant de déterminer
	 * le nombre de position disponible
	 * 
	 * @return le nombre de slot disponible pour le DistancesEtBlason
	 */
	public int getNbAvailableSlotsFor(DistancesEtBlason distancesEtBlason) {
		if(nbArcher > 0) {
			if(Arrays.equals(getDistancesEtBlason().get(0).getDistance(), distancesEtBlason.getDistance())) {
				int availableSlots = 0;
				for(int i = 0; i < concurrents.length; i++) {
					if(concurrents[i] == null && !isReservedPosition(i) && isSlotAvailable(distancesEtBlason.getTargetFace(), i)) {
						availableSlots++;
					}
				}
				return availableSlots;
			}
			return 0;
		}
		return concurrents.length;
	}
	
	/**
	 * Determine si une position est reservé pour un archer handicape
	 * ou non
	 * 
	 * @param position la position à tester
	 * @return true si la position est reservé, false sinon
	 */
	public boolean isReservedPosition(int position) {
		if(position % 2 == 1 && concurrents[position-1] != null && concurrents[position-1].isHandicape())
			return true;
		return false;
	}

	/**
	 * insere un concurrent à la premiere position libre et retourne cette
	 * position ou produit une exception PlacementException en cas d'echec
	 * 
	 * @param concurrent le concurrent à inserer
	 * @param repartition le mode de distribution des archers sur la cible
	 * 
	 * @return la position de concurrent
	 * @throws PlacementException si l'insertion du concurrent est impossible
	 */
	protected int insertConcurrent(Concurrent concurrent, Repartition repartition) 
			throws PlacementException {
		return insertConcurrent(concurrent, repartition, false);
	}
	
	/**
	 * <p>
	 * insere un concurrent à la premiere position libre et retourne cette
	 * position ou produit une exception PlacementException en cas d'echec
	 * </p>
	 * <p>
	 * L'insertion peut se dérouler en mode <i>réel</i> auquel cas, les informations
	 * de placement seront ajouté au concurrent ou en mode </i>simulation</i> si l'on
	 * souhaite simplement voir si l'on peut placer les archers sur la cible sans
	 * affecter les valeurs de placement au concurrent.
	 * </p>
	 * 
	 * @param concurrent le concurrent à inserer
	 * @param repartition le mode de distribution des archers sur la cible
	 * @param simulationMode <i>true</i> si l'on souhaite une insertion en mode simulation,
	 * 	<i>false</i> sinon
	 * 
	 * @return la position de concurrent
	 * @throws PlacementException si l'insertion du concurrent est impossible
	 */
	protected int insertConcurrent(Concurrent concurrent, Repartition repartition, boolean simulationMode)
			throws PlacementException {
		int position = -1;

		if (concurrent != null) {
			//verifie qu'il reste des places disponible
			if(nbArcher < concurrents.length - nbHandicap) {
				DistancesEtBlason concurrentDb = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent);
				List<DistancesEtBlason> targetDbs = getDistancesEtBlason();
				
				//verifie que la distance est bonne
				if(targetDbs.size() > 0 && !Arrays.equals(concurrentDb.getDistance(), targetDbs.get(0).getDistance()))
					throw new PlacementException(PlacementException.Nature.BAD_DISTANCES);
				
				//si l'archer est handicapé, vérifié que les condition spécifique sont remplis
				if(concurrent.isHandicape()) {
					if(nbArcher < concurrents.length - (nbHandicap+1)) {
						//dans le cas d'un archer handicapé, on boucle sur les emplacements 2 à 2
						for (int i = 0; i < concurrents.length; i+=2) {
							if (isSlotAvailable(concurrentDb.getTargetFace(), i) && concurrents[i+1] == null) {
								placeConcurrent(concurrent, i, simulationMode);
								nbHandicap++;
								
								position = i;
								
								break;
							}
						}
						if(position == -1)
							throw new PlacementException(PlacementException.Nature.BAD_TARGETFACE);
					} else {
						throw new PlacementException(PlacementException.Nature.POSITION_AVAILABLE_FOR_VALID_CONCURRENT);
					}
				} else {
					if(repartition == Repartition.ABCD) {
						//on boucle sur les emplacements et on remplit le premier qui est libre
						for (int i = 0; i < concurrents.length; i++) {
							if (isSlotAvailable(concurrentDb.getTargetFace(), i)) {
								if(isReservedPosition(i)) //la position est libre mais reservé pour un archer handicapé
									continue;
								placeConcurrent(concurrent, i, simulationMode);
		
								position = i;
		
								break;
							}
						}
					} else {
						//tente le placement en AC
						for (int i = 0; i < concurrents.length; i+=2) {
							if (isSlotAvailable(concurrentDb.getTargetFace(), i)) {
								placeConcurrent(concurrent, i, simulationMode);
		
								position = i;
		
								break;
							}
						}
						//si AC en echec tente en BD
						if(position == -1) {
							for (int i = 1; i < concurrents.length; i+=2) {
								if (isSlotAvailable(concurrentDb.getTargetFace(), i)) {
									if(isReservedPosition(i)) //la position est libre mais reservé pour un archer handicapé
										continue;
									placeConcurrent(concurrent, i, simulationMode);
			
									position = i;
			
									break;
								}
							}
						}
					}
					if(position == -1)
						throw new PlacementException(PlacementException.Nature.BAD_TARGETFACE);
				}
			} else {
				if(nbHandicap > 0)
					throw new PlacementException(PlacementException.Nature.POSITION_RESERVED_FOR_HANDICAP);
				throw new PlacementException(PlacementException.Nature.ANY_AVAILABLE_POSITION);
			}
		} else {
			throw new PlacementException(PlacementException.Nature.NULL_CONCURRENT);
		}

		return position;
	}
	
	/**
	 * Détermine si une position est ou non disponible en fonction d'un
	 * blason donné
	 * 
	 * @param blason le blason que l'on cherche à placer sur la position
	 * @param position la position ou placer ce blason
	 * @return <i>true</i> si l'on arrive à placer le blason sur la position, <i>false</i> sinon
	 */
	public boolean isSlotAvailable(Blason blason, int position) {
		boolean placable = true;
		
		if(concurrents[position] != null)
			return false;
		
		//Ancrage ancrage = blason.getAncrage(position);
		for(int i = 0; i < concurrents.length; i++) {
			if(concurrents[i] != null) {
				//int otherPosition = concurrent.getPosition();
				DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrents[i]);
				Blason otherBlason = db.getTargetFace();
				
				if(blason.getNbArcher() > 2 || otherBlason.getNbArcher() > 2) {
					if(!otherBlason.equals(blason)) {
						placable = false;
						break;
					}
				} else if(blason.getNbArcher() > 1) {
					if(!((!otherBlason.equals(blason) && !blason.isOver(position, otherBlason, i))
							|| otherBlason.equals(blason))) {
						placable = false;
						break;
					}
				} else {
					if(blason.isOver(position, otherBlason, i)) {
						placable = false;
						break;
					}
				}
			}
		}
		
		return placable;
	}
	
	private void placeConcurrent(Concurrent concurrent, int pos, boolean simulationMode) {
		if(!simulationMode) {
			concurrent.setCible(numCible);
			concurrent.setPosition(pos);
		}
		concurrent.addPropertyChangeListener(this);
		
		concurrents[pos] = concurrent;
		nbArcher++;
		
		fireConcurrentJoined(concurrent);
	}

	/**
	 * Donne le concurrent à la postition x
	 * 
	 * @param position
	 * @return Concurrent - le concurrent à la position donnée ou <i>null</i>
	 *         si aucun concurrent trouvé
	 */
	public Concurrent getConcurrentAt(int position) {
		if (position > -1 && position < concurrents.length)
			return concurrents[position];
		return null;
	}

	/**
	 * Retourne l'ensemble des concurrents présent sur la cible
	 * 
	 * @return la liste des concurrents présent sur la cible
	 */
	public List<Concurrent> getAllConcurrents() {
		List<Concurrent> lstConcurrent = new ArrayList<Concurrent>();
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
		for (int i = 0; i < concurrents.length; i++)
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
	 * @throws PlacementException invoqué en cas d'echec
	 * d'insertion du concurrent
	 */
	protected void setConcurrentAt(Concurrent concurrent, int position) 
			throws PlacementException {
		if (concurrent != null) {
			if (position == -1) {
				Repartition repartition = Repartition.ACBD;
				if(concurrents.length == 2)
					repartition = Repartition.ABCD;
				insertConcurrent(concurrent, repartition);
				
				return;
			} else if (position >= 0 && position < concurrents.length && !isReservedPosition(position)) {
				DistancesEtBlason dbConcurrent = DistancesEtBlason.getDistancesEtBlasonForConcurrent(
						reglement, concurrent);
				
				assert dbConcurrent != null : "un concururrent doit toujours avoir un DistancesEtBlason associé"; //$NON-NLS-1$
				
				if(getDistancesEtBlason().size() > 0 && !Arrays.equals(dbConcurrent.getDistance(), getDistancesEtBlason().get(0).getDistance()))
					throw new PlacementException(PlacementException.Nature.BAD_DISTANCES);
				
				if (isSlotAvailable(dbConcurrent.getTargetFace(), position)) {
					if(concurrent.isHandicape() && position % 2 != 0)
						throw new PlacementException(PlacementException.Nature.POSITION_AVAILABLE_FOR_VALID_CONCURRENT);
					concurrent.setCible(numCible);
					concurrent.setPosition(position);
					if (concurrents[position] != null)
						removeConcurrentAt(position);
					
					if (concurrent.isHandicape())
						removeConcurrentAt(position+1);
						
					nbArcher++;
					if (concurrent.isHandicape())
						nbHandicap++;
					
					concurrent.addPropertyChangeListener(this);
					
					concurrents[position] = concurrent;
					fireConcurrentJoined(concurrent);
					
					return;
				}
				throw new PlacementException(PlacementException.Nature.BAD_TARGETFACE);
			} else {
				if(isReservedPosition(position))
					throw new PlacementException(PlacementException.Nature.POSITION_RESERVED_FOR_HANDICAP);
				throw new PlacementException(PlacementException.Nature.ANY_AVAILABLE_POSITION);
			}
		}
		
		throw new PlacementException(PlacementException.Nature.NULL_CONCURRENT);
	}

	/**
	 * Supprime un concurrent à la position donnée
	 * 
	 * @param position -
	 *            la position du concurrent à supprimer
	 */
	protected void removeConcurrentAt(int position) {
		removeConcurrentAt(position, false);
	}
	
	/**
	 * Supprime un concurrent à la position donnée
	 * 
	 * @param position -
	 *            la position du concurrent à supprimer
	 * @param simulationMode ne fait pas rééllement la suppression en mode simulation
	 */
	private void removeConcurrentAt(int position, boolean simulationMode) {
		if (position < concurrents.length && concurrents[position] != null) {
			nbArcher--;

			Concurrent removedConcurrent = concurrents[position];
			if(!simulationMode)
				removedConcurrent.setCible(0);
			if(removedConcurrent.isHandicape())
				nbHandicap--;

			removedConcurrent.removePropertyChangeListener(this);
			
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
	protected void removeConcurrent(Concurrent concurrent) {
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
	protected void removeAll() {
		removeAll(false);
	}
	
	/**
	 * Retire tous les concurrents de la cible en mode simulation
	 * 
	 * @param simulationMode passe en mode de simulation de suppression
	 */
	protected void removeAll(boolean simulationMode) {
		for (int i = 0; i < concurrents.length; i++)
			removeConcurrentAt(i, simulationMode);
	}

	/**
	 * Donne la disposition de la cible
	 * 
	 * @return DistancesEtBlason - la disposition de la cible
	 */
	public List<DistancesEtBlason> getDistancesEtBlason() {
		//DistancesEtBlason db = null;
		List<DistancesEtBlason> dbs = new ArrayList<DistancesEtBlason>();

		if (nbArcher > 0) {
			//Concurrent firstConcurrent = null;
			for (Concurrent concurrent : concurrents) {
				if (concurrent != null) {
					DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, concurrent);
					if(!dbs.contains(db))
						dbs.add(db);
					//firstConcurrent = concurrent;
					//break;
				}
			}

			//db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(reglement, firstConcurrent);
		}

		return dbs;
	}

	/**
	 * Renvoie le Libelle qualifié de la cible
	 * 
	 */
	@Override
	public String toString() {
		String strCouleur = "<font color=\"#00AA00\">"; //$NON-NLS-1$
		if (concurrents.length == nbArcher + nbHandicap)
			strCouleur = "<font color=\"#0000FF\">"; //$NON-NLS-1$
		String strCibleLibelle = "<html>" + strCouleur + "<b>" + ApplicationCore.ajrLibelle.getResourceString("treenode.cible") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ((this.numCible < 10) ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
				+ this.numCible + "</b> ("; //$NON-NLS-1$
		if(getDistancesEtBlason().size() > 0) {
			List<DistancesEtBlason> dbs = getDistancesEtBlason();
			if (dbs != null && dbs.size() > 0) {
				//Sur une cible, les distances des differents objets sont réputées être identique
				for (int i = 0; i < dbs.get(0).getDistance().length; i++) {
					if (i == 0 || (i > 0 && dbs.get(0).getDistance()[i] != dbs.get(0).getDistance()[i - 1])) {
						if (i > 0)
							strCibleLibelle += "/"; //$NON-NLS-1$
						strCibleLibelle += dbs.get(0).getDistance()[i] + "m"; //$NON-NLS-1$
					}
				}
				strCibleLibelle += ", "; //$NON-NLS-1$
				
				//Les blasons sont eux toujours différent
				for (int i = 0; i < dbs.size(); i++) {
					if (i == 0 || (i > 0 && !dbs.get(i).getTargetFace().equals(dbs.get(i - 1).getTargetFace()))) {
						if (i > 0)
							strCibleLibelle += "/"; //$NON-NLS-1$
						strCibleLibelle += dbs.get(i).getTargetFace().getName();
					}
				}
			}
		}

		strCibleLibelle += ") (" + this.nbArcher + "/" + (concurrents.length - nbHandicap) + ")</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Hashtable<Entite, Integer> nbArcherByClub = new Hashtable<Entite, Integer>();
		for (Concurrent concurrent : concurrents) {
			if(concurrent != null) {
				if(!nbArcherByClub.containsKey(concurrent.getClub()))
					nbArcherByClub.put(concurrent.getClub(), 0);
				nbArcherByClub.put(concurrent.getClub(), nbArcherByClub.get(concurrent.getClub())+1);
			}
		}
		
		if (nbArcherByClub.size() == 1 && getNbArcher() > 1)
			strCibleLibelle += ApplicationCore.ajrLibelle.getResourceString("target.sameclub"); //$NON-NLS-1$
		else if (getNbArcher() == 1)
			strCibleLibelle += ApplicationCore.ajrLibelle.getResourceString("target.onlyone"); //$NON-NLS-1$
		else {
			for(Entry<Entite, Integer> nbarch : nbArcherByClub.entrySet()) {
				if(nbarch.getValue() > 2) {
					strCibleLibelle += ApplicationCore.ajrLibelle.getResourceString("target.morethan2sameclub"); //$NON-NLS-1$
					break;
				}
			}
		}
		strCibleLibelle += "</html>"; //$NON-NLS-1$

		return strCibleLibelle;
	}

	private void fireConcurrentJoined(Concurrent concurrent) {
		for (TargetListener cibleListener : listeners.getListeners(TargetListener.class)) {
			cibleListener.concurrentJoined(new TargetEvent(concurrent, this));
		}
	}

	private void fireConcurrentQuit(Concurrent concurrent) {
		for (TargetListener cibleListener : listeners.getListeners(TargetListener.class)) {
			cibleListener.concurrentQuit(new TargetEvent(concurrent, this));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("handicape")) { //$NON-NLS-1$
			boolean oldValue = (Boolean)evt.getOldValue();
			boolean newValue = (Boolean)evt.getNewValue();
			if(oldValue != newValue) {
				if(newValue)
					nbHandicap++;
				else
					nbHandicap--;
			}
		}
	}
}