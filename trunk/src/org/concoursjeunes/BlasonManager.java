package org.concoursjeunes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.concoursjeunes.builders.BlasonBuilder;

public class BlasonManager {
	
	/**
	 * Retourne le blason associé à une ligne distance/blason d'un réglement donnée
	 * 
	 * @param distancesEtBlason l'objet distanceEtBlason dont le blason fait partie
	 * @return le blason associé à la ligne d/b du réglement donnée
	 */
	public static Blason findBlasonAssociateToDistancesEtBlason(DistancesEtBlason distancesEtBlason) {
		return findBlasonAssociateToDistancesEtBlason(distancesEtBlason, distancesEtBlason.getReglement().hashCode());
	}
	
	/**
	 * Retourne le blason associé à une ligne distance/blason d'un réglement donnée
	 * 
	 * @param distancesEtBlason l'objet distanceEtBlason dont le blason fait partie
	 * @param numreglement le numrero de reglement
	 * @return le blason associé à la ligne d/b du réglement donnée
	 */
	public static Blason findBlasonAssociateToDistancesEtBlason(DistancesEtBlason distancesEtBlason, int numreglement) {
		try {
			String sql = "select BLASONS.* from DISTANCESBLASONS,BLASONS " //$NON-NLS-1$
				+ "where DISTANCESBLASONS.NUMBLASON=BLASONS.NUMBLASON AND NUMDISTANCESBLASONS=? and NUMREGLEMENT=? order by NUMORDRE DESC"; //$NON-NLS-1$
			
			PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
			
			pstmt.setInt(1, distancesEtBlason.getNumdistancesblason());
			pstmt.setInt(2, numreglement);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.first()) {		
				return BlasonBuilder.getBlason(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Recherche dans la base le blason correspondant au nom donnée en parametre
	 * 
	 * @param name le nom du blason à trouver
	 * 
	 * @return l'objet Blason trouvé ou null si inexistant
	 * @throws SQLException
	 */
	public static Blason findBlasonByName(String name) throws SQLException {
		Blason blason = null;
		
		String sql = "select * from BLASONS where NOMBLASON=?"; //$NON-NLS-1$
		PreparedStatement pstmt = ConcoursJeunes.dbConnection.prepareStatement(sql);
		
		pstmt.setString(1, name);
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.first()) {
			blason = BlasonBuilder.getBlason(rs);
		}
		
		return blason;
	}
}
