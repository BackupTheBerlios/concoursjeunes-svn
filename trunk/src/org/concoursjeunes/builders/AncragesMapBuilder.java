package org.concoursjeunes.builders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.concoursjeunes.Ancrage;
import org.concoursjeunes.Blason;
import org.concoursjeunes.ApplicationCore;

/**
 * Construit la table des ancrages possible d'un blason sur une cible 
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 *
 */
public class AncragesMapBuilder {
	/**
	 * Construit la tables des ancrages à partir de la refernce du blason en base
	 * 
	 * @param blason la reference du blason pour récuperer les informations en base
	 * @return la tables des ancrages ou null si non trouvé en base
	 * @throws SQLException
	 */
	public static ConcurrentMap<Integer, Ancrage> getAncragesMap(Blason blason) throws SQLException {
		ConcurrentMap<Integer, Ancrage> ancrages = new ConcurrentHashMap<Integer, Ancrage>();
		
		String sql = "select * from ANCRAGES_BLASONS where NUMBLASON=?"; //$NON-NLS-1$
		
		PreparedStatement pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
		
		pstmt.setInt(1, blason.getNumblason());
		
		ResultSet rs2 = pstmt.executeQuery();
		while(rs2.next()) {
			ancrages.put(
				rs2.getInt("EMPLACEMENT") , //$NON-NLS-1$
				new Ancrage(
					rs2.getDouble("ANCRAGEX"), //$NON-NLS-1$
					rs2.getDouble("ANCRAGEY") //$NON-NLS-1$
				)
			);
		}
		
		if(ancrages.size() == 0)
			ancrages = null;
		
		return ancrages;
	}
	
	/**
	 * Construit arbitrairement une table d'ancrage en fonction d'un nombre d'archer devant être présent sur le blason
	 * 
	 * @param nbArcher le nombre d'archer devant être présent
	 * @return la tables des ancrages
	 */
	public static ConcurrentMap<Integer, Ancrage> getAncragesMap(int nbArcher) {
		ConcurrentMap<Integer, Ancrage> ancrages = new ConcurrentHashMap<Integer, Ancrage>();
		if(nbArcher > 2) {
			ancrages.put(Ancrage.POSITION_ABCD, new Ancrage(0, 0));
		} else if(nbArcher > 1) {
			ancrages.put(Ancrage.POSITION_AC, new Ancrage(0, 0));
			ancrages.put(Ancrage.POSITION_BD, new Ancrage(0, 0.5));
		} else {
			ancrages.put(Ancrage.POSITION_A, new Ancrage(0, 0));
			ancrages.put(Ancrage.POSITION_B, new Ancrage(0, 0.5));
			ancrages.put(Ancrage.POSITION_C, new Ancrage(0.5, 0));
			ancrages.put(Ancrage.POSITION_D, new Ancrage(0.5, 0.5));
		}
		
		return ancrages;
	}
}
