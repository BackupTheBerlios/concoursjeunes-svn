/**
 * 
 */
package org.concoursjeunes.debug;

//import java.util.*;

import org.concoursjeunes.*;


/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Debug {
    
    public static void attributePoints(ConcurrentList archerList, int depart) {
        for(Concurrent concurrent : archerList.list(depart)) {
            for(int i = 0; i < concurrent.getScore().size(); i++) {
            	concurrent.getScore().set(i, (int)(100 + Math.random() * 80));
            }
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
