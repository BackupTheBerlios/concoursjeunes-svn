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

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class EquipeListFactory {
	
	public static EquipeList getClubEquipeList(ConcurrentList concurrentList, FicheConcours ficheConcours) {
		EquipeList equipeList = new EquipeList(ficheConcours);
		
		for(Entite entite : concurrentList.listCompagnie()) {
			Concurrent[] concurrents = concurrentList.list(entite, null);
			if(concurrents.length >=
					ficheConcours.getParametre().getReglement().getNbMembresRetenu()) {
				Equipe equipe = new Equipe(entite.getNom());
				
				concurrents = ConcurrentList.sort(concurrents, ConcurrentList.SORT_BY_POINTS);
				
				int i = 0;
				for(Concurrent concurrent : concurrents) {
					if(i < ficheConcours.getParametre().getReglement().getNbMembresRetenu())
						equipe.addConcurrent(concurrent);
					else
						break;
					i++;
				}
				
				equipeList.add(equipe);
			}
		}
		
		return equipeList;
	}
}
