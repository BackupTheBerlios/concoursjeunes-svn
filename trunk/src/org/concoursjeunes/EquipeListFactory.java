/**
 * 
 */
package org.concoursjeunes;

/**
 * @author Aurélien JEOFFRAY - Fiducial Informatique
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
