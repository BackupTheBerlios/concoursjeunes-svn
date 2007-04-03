package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Fabrique d'archer en se basant sur les données en base
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class ConcurrentFactory {

	/**
	 * Construit un archer à partir d'un jeux de resultat sgbd et un reglement donnée
	 * 
	 * @param rs - le jeux de resultat sgbd
	 * @param reglement - le reglement appliqué
	 * @return l'archer construit
	 */
	public static Concurrent getConcurrent(ResultSet rs, Reglement reglement) {
		Concurrent concurrent = new Concurrent();

		try {
			concurrent.setCertificat(rs.getBoolean("CERTIFMEDICAL"));
			concurrent.setClub(EntiteFactory.getEntite(rs.getString("AGREMENTENTITE")));

			if(reglement != null) {
				CriteriaSet differentiationCriteria = new CriteriaSet();

				for(Criterion key : reglement.getListCriteria()) {
					if(!key.getCodeffta().equals("")) {
						ArrayList<CriterionElement> arrayList = key.getCriterionElements();
						int valindex = rs.getInt(key.getCodeffta() + "FFTA");
						if(valindex >= arrayList.size())
							valindex = arrayList.size() - 1;
						if(valindex < 0)
							valindex = 0;
						differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(valindex));
					}
				}

				concurrent.setCriteriaSet(differentiationCriteria);
			}
			concurrent.setNumLicenceArcher(rs.getString("NUMLICENCEARCHER"));
			concurrent.setNomArcher(rs.getString("NOMARCHER"));
			concurrent.setPrenomArcher(rs.getString("PRENOMARCHER"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return concurrent;
	}
	
	/**
	 * Construit un nouveau concurrent à parametrer en se basant sur le reglement donnée
	 * 
	 * @param reglement
	 * @return
	 */
	public static Concurrent getConcurrent(Reglement reglement) {
		Concurrent concurrent = new Concurrent();
		
		CriteriaSet differentiationCriteria = new CriteriaSet();
		for(Criterion key : reglement.getListCriteria()) {
			differentiationCriteria.getCriteria().put(key, key.getCriterionElements().get(0));
		}
		concurrent.setCriteriaSet(differentiationCriteria);
		
		return concurrent;
	}
}
