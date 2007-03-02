/**
 * 
 */
package org.concoursjeunes.debug;

import java.util.*;

import org.concoursjeunes.*;
import org.concoursjeunes.ui.FicheConcoursPane;


/**
 * @author Aurelien
 *
 */
public class Debug {
    public static void generateConcurrent(FicheConcoursPane ficheConcoursFrame, int depart) {
       /* for(int i = 0; i < ficheConcoursFrame.ficheConcours.getParametre().getNbCible()*ficheConcoursFrame.ficheConcours.getParametre().getNbTireur()-9; i++) {
            ArrayList<Concurrent> listeTireur = new ArrayList<Concurrent>();
            //FIXME
            //listeTireur.addAll(ConcoursJeunes.listeTireur.values());
            
            Concurrent toinsert;
            boolean isactive;
            do {
                toinsert = listeTireur.get((int)Math.round(Math.random()*(listeTireur.size()-1)));
                toinsert.setInscription(Concurrent.RESERVEE);
                
                isactive = true;
                Enumeration<Criterion> criteriaEnum = ConcoursJeunes.configuration.getCriteriaPopulation().keys();
                while(criteriaEnum.hasMoreElements()) {
                    Criterion criterion = criteriaEnum.nextElement();
                    ArrayList<CriterionElement> cp = ConcoursJeunes.configuration.getCriteriaPopulation().get(criterion);
                    //int criterionIndex = toinsert.getDifferentiationCriteria().getCriterion(criterion);
                    //isactive = isactive && cp.size() > criterionIndex && cp.get(criterionIndex).isActive();
                }

            } while(!isactive || ficheConcoursFrame.ficheConcours.getArcherlist().contains(toinsert));

            ficheConcoursFrame.ficheConcours.addConcurrent(toinsert);
        }
        
        attributeLevel(ficheConcoursFrame.ficheConcours.getArcherlist(), depart);
        */
        //TODO ficheConcoursFrame.initContent();
    }
    
    public static void attributePoints(ArcherList archerList, int depart) {
        /*for(Concurrent concurrent : archerList.list(depart)) {
            int[] points = new int[ConcoursJeunes.configuration.getNbSerie()];
            for(int i = 0; i < points.length; i++) {
                points[i] = (int)(100 + Math.random() * 80);
            }
            concurrent.setScore(points);
        }*/
    }
    
    public static void resetPoints(ArcherList archerList, int depart) {
        /*for(Concurrent concurrent : archerList.list(depart)) {
            int[] points = new int[ConcoursJeunes.configuration.getNbSerie()];
            for(int i = 0; i < points.length; i++) {
                points[i] = 0;
            }
            concurrent.setScore(points);
            concurrent.setDix(0);
            concurrent.setNeuf(0);
            concurrent.setManque(0);
        }*/
    }
    
    public static void attributeLevel(ArcherList archerList, int depart) {
        for(Concurrent concurrent : archerList.list(depart)) {
        	
            //concurrent.getDifferentiationCriteria().setCriterion(new Criterion("niveau"), (int)Math.round(Math.random() * 3)); //$NON-NLS-1$
        }
    }
}
