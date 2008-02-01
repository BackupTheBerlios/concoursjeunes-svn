package org.concoursjeunes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.concoursjeunes.builders.BlasonBuilder;

public class BlasonManager {
	public static Blason findBlasonInDatabase(String name) throws SQLException {
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
