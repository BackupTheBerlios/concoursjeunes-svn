/*
 * Created on 7 janv. 2005
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

import javax.swing.event.EventListenerList;

/**
 * Reprensentation de l'entite 'Cible' avec ses parametres de distances, de blason appliqué et les concurrents qui y sont associés
 * @author  Aurelien Jeoffray
 * @version  2.1
 */
public class Cible {

	private int numCible				= 0;	//le numero de la cible
	private FicheConcours concours;
	/**
	 * @uml.property  name="concurrents"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Concurrent[] concurrents;			//le liste des concurrents présents sur la cible
	private int nbArcher				= 0;	//le nombre d'archer sur la cible
	
	private EventListenerList listeners = new EventListenerList();

	/**
	 * Initialise une cible avec son numero et son nombre max d'archer<br>
	 * la numerotation est de type 1 à n et non de 0 à n-1
	 * 
	 * @param numCible - le numero de la cible
	 * @param ficheConcours - La fiche concours associé à la cible
	 */
	public Cible(int iNumCible, FicheConcours concours) {
		this.numCible = iNumCible;
		this.concours = concours;
		this.concurrents = new Concurrent[concours.getParametre().getNbTireur()];
	}
	
	public void addCibleListener(CibleListener cibleListener) {
		listeners.add(CibleListener.class, cibleListener);
	}
	
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
	
	public int getNbMaxArchers() {
		return concurrents.length;
	}

	/**
	 * @return reglement
	 */
	public FicheConcours getFicheConcours() {
		return concours;
	}

	/**
	 * @param reglement reglement à définir
	 */
	public void setFicheConcours(FicheConcours concours) {
		this.concours = concours;
	}

	/**
	 * insere un concurrent à la premiere position libre et retourne
	 * cette position ou -1 si ecehc
	 * 
	 * @param concurrent - le concurrent à inserer
	 * @return int - la position de concurrent ou -1 si echec
	 */
	public int insertConcurrent(Concurrent concurrent) {
		int position = -1;

		if(concurrent != null && nbArcher < concours.getParametre().getNbTireur()) {
			if((nbArcher > 0
					&& DistancesEtBlason.getDistancesEtBlasonForConcurrent(concours.getParametre().getReglement(), concurrent).equals(getDistancesEtBlason()))
					|| nbArcher == 0) {
				for(int i = 0; i < concurrents.length; i++) {
					if(concurrents[i] == null) {
						
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
		
		return position;
	}
	
	/**
	 * Donne le concurrent à la postition x
	 * 
	 * @param position
	 * @return Concurrent - le concurrent à la position donnée ou <i>null</i> si aucun concurrent trouvé
	 */
	public Concurrent getConcurrentAt(int position) {
		if(position < concours.getParametre().getNbTireur())
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
		for(Concurrent concurrent : concurrents) {
			if(concurrent != null)
				lstConcurrent.add(concurrent);
		}
		return lstConcurrent;
	}
	
	public int indexOf(Concurrent concurrent) {
		for(int i = 0; i < concours.getParametre().getNbTireur(); i++)
			if(concurrents[i] != null && concurrents[i].equals(concurrent))
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
	 * @param concurrent - le concurrent à placer
	 * @param position - la positionn de ce concurrent
	 * 
	 * @return true si affectation avec succès, false sinon
	 */
	public boolean setConcurrentAt(Concurrent concurrent, int position) {
		if(concurrent != null) {
			if(position == -1) {
				return insertConcurrent(concurrent) > -1;
			} else if(position >= 0 && position < concours.getParametre().getNbTireur()) {
				if((nbArcher > 0
						&& DistancesEtBlason.getDistancesEtBlasonForConcurrent(concours.getParametre().getReglement(), concurrent).equals(getDistancesEtBlason()))
						|| nbArcher == 0) {
					concurrent.setCible(numCible);
					concurrent.setPosition(position);
					if(concurrents[position] != null)
						removeConcurrentAt(position);
					
					nbArcher++;
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
	 * @param position - la position du concurrent à supprimer
	 */
	public void removeConcurrentAt(int position) {
		if(position < concours.getParametre().getNbTireur() && concurrents[position] != null) {
			nbArcher--;

			Concurrent removedConcurrent = concurrents[position]; 
			removedConcurrent.setCible(0);
			
			concurrents[position] = null;
			
			fireConcurrentQuit(removedConcurrent);
		}
	}

	/**
	 * Supprime un concurrent donné
	 * 
	 * @param concurrent - le concurrent à supprimer
	 */
	public void removeConcurrent(Concurrent concurrent) {
		for(int i = 0; i < concurrents.length; i++) {
			if(concurrents[i] == concurrent) {
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
		for(int i = 0; i < concurrents.length; i++)
			removeConcurrentAt(i);
	}

	/**
	 * Donne la disposition de la cible
	 * 
	 * @return DistancesEtBlason - la disposition de la cible
	 */
	public DistancesEtBlason getDistancesEtBlason() {
		DistancesEtBlason db = null;

		if(nbArcher > 0) {
			Concurrent firstConcurrent = null;
			for(Concurrent concurrent : concurrents) {
				if(concurrent != null) {
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
		if(concours.getParametre().getNbTireur() == nbArcher)
			strCouleur = "<font color=\"#0000FF\">"; //$NON-NLS-1$
		String strCibleLibelle = "<html>" + strCouleur + "<b>" + ConcoursJeunes.ajrLibelle.getResourceString("treenode.cible") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ((this.numCible < 10) ? "0" : "") //$NON-NLS-1$ //$NON-NLS-2$
				+ this.numCible + "</b> ("; //$NON-NLS-1$
		DistancesEtBlason db = getDistancesEtBlason();
		if(db != null) {
			for(int i = 0; i < db.getDistance().length; i++) {
				if(i == 0 || (i > 0 && db.getDistance()[i] != db.getDistance()[i-1])) {
					if(i > 0) 
						strCibleLibelle += "/"; //$NON-NLS-1$
					strCibleLibelle += db.getDistance()[i] + "m"; //$NON-NLS-1$
				}
			}
			strCibleLibelle += ", " + db.getBlason() + "cm"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		strCibleLibelle += ") (" + this.nbArcher + "/" + concours.getParametre().getNbTireur() + ")</font>"; //$NON-NLS-1$ //$NON-NLS-2$
		
		Concurrent precConcurrent = null;
		boolean valid = false;
		for(Concurrent concurrent : concurrents) {
			if(precConcurrent != null && concurrent != null && !precConcurrent.getClub().equals(concurrent.getClub())) {
				valid = true;
				break;
			}
			precConcurrent = concurrent;
		}
		if(!valid && getNbArcher() > 1)
			strCibleLibelle += "<br><font color=\"orange\">Attention tous les archers de cette cible sont du même club!</font>";
		
		strCibleLibelle += "</html>";
		
		return strCibleLibelle;
	}

	/**
	 * Donne un libelle textuel pour une position
	 * 
	 * @param cible - la cible de la position
	 * @param position - l'index de la position
	 * @return String - le libelle de la position
	 */
	public static String getCibleLibelle(int cible, int position) {
		return ((cible < 10) ? "0" : "") + cible + (char)('A' + position); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private void fireConcurrentJoined(Concurrent concurrent) {
		for(CibleListener cibleListener : listeners.getListeners(CibleListener.class)) {
			cibleListener.concurrentJoined(new CibleEvent(concurrent, this));
		}
	}
	
	private void fireConcurrentQuit(Concurrent concurrent) {
		for(CibleListener cibleListener : listeners.getListeners(CibleListener.class)) {
			cibleListener.concurrentQuit(new CibleEvent(concurrent, this));
		}
	}
}