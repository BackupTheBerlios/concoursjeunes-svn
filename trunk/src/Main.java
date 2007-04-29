import java.sql.SQLException;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.ui.ConcoursJeunesFrame;

/**
 * 
 */

/**
 * @author aurelien
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcoursJeunes concoursJeunes = ConcoursJeunes.getInstance();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					//permet de s'assurer que la base de données est correctement fermé
					ConcoursJeunes.dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		new ConcoursJeunesFrame(concoursJeunes);
	}

}
