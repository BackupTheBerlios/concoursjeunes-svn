/**
 * 
 */
package org.concoursjeunes.debug;

//import java.util.*;

import java.util.ArrayList;
import java.util.List;

import org.concoursjeunes.*;


/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Debug {
    
    public static void attributePoints(ConcurrentList archerList, int depart) {
    	
        for(Concurrent concurrent : archerList.list(-1)) {
        	List<Integer> points = new ArrayList<Integer>();
            for(int i = 0; i < archerList.getParametre().getReglement().getNbSerie(); i++) {
            	points.add((int)(100 + Math.random() * 80));
            }
            concurrent.setScore(points);
        }
    }
    
    public static void resetPoints(FicheConcours ficheConcours) {
        for(Concurrent concurrent : ficheConcours.getConcurrentList().list(-1)) {
            for(int i = 0; i < concurrent.getScore().size(); i++) {
            	concurrent.getScore().set(i, 0);
            }
            concurrent.setDix(0);
            concurrent.setNeuf(0);
            concurrent.setManque(0);
        }
    }
}
