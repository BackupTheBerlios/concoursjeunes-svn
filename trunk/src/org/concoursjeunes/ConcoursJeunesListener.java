/*
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

import java.util.EventListener;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public interface ConcoursJeunesListener extends EventListener {
	
	/**
	 * Emis lorsqu'une fiche concours est créer
	 * 
	 * @param concoursJeunesEvent l'évenement associé à la création de la fiche
	 */
	public void ficheConcoursCreated(ConcoursJeunesEvent concoursJeunesEvent);
	
	/**
	 * Emis lorsqu'une fiche concours est supprimé
	 * 
	 * @param concoursJeunesEvent l'évenement associé à la suppression de la fiche
	 */
	public void ficheConcoursDeleted(ConcoursJeunesEvent concoursJeunesEvent);
	
	/**
	 * Emis lorsqu'une fiche concours est fermé
	 * 
	 * @param concoursJeunesEvent l'évenement associé à la fermeture de la fiche
	 */
	public void ficheConcoursClosed(ConcoursJeunesEvent concoursJeunesEvent);
	
	/**
	 * Emis lorsqu'une fiche concours est restauré
	 * 
	 * @param concoursJeunesEvent l'évenement associé à la restauration de la fiche
	 */
	public void ficheConcoursRestored(ConcoursJeunesEvent concoursJeunesEvent);
	
	/**
	 * Emis lorsque la configuration du programme à changé
	 * 
	 * @param concoursJeunesEvent l'evenements associé au changement de configguration
	 */
	public void configurationChanged(ConcoursJeunesEvent concoursJeunesEvent);
}
