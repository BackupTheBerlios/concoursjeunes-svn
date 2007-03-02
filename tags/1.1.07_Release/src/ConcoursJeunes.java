/**
 * Main Class du projet ConcoursJeunes
 * 
 * @author Aurelien Jeoffray
 * @version 1.1 - 3.0
 */
import java.io.*;

public class ConcoursJeunes implements ajinteractive.concours.Luncher {
    private static ajinteractive.concours.ConcoursJeunes cj;
    private String[] args;
    
    private ConcoursJeunes(String[] args) {
        this.args = args;
        start();
    }
    
    /**
     * demarrage d'une nouvelle instance de ConcoursJeunes
     *
     */
    private void start() {
        cj = new ajinteractive.concours.ConcoursJeunes();
        cj.setLuncher(this);
        if(args.length > 0)
            cj.restoreConcours(new File(args[0]));
    }
    
    /**
     * redemarrage de ConcoursJeunes
     */
    public void reboot() {
        cj = null;
        System.gc();
        start();
    }
    
    public static void main(String[] args) {
        new ConcoursJeunes(args);
    }
}
