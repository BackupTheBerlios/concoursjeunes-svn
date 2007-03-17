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
public class ArcherFactory {

	/**
	 * Construit un archer à partir d'un jeux de resultat sgbd et un reglement donnée
	 * 
	 * @param rs - le jeux de resultat sgbd
	 * @param reglement - le reglement appliqué
	 * @return l'archer construit
	 */
	public static Archer getArcher(ResultSet rs, Reglement reglement) {
		Archer archer = new Archer();

		try {
			archer.setCertificat(rs.getBoolean("CERTIFMEDICAL"));
			archer.setClub(EntiteFactory.getEntite(rs.getString("AGREMENTENTITE")));

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

				archer.setCriteriaSet(differentiationCriteria);
			}
			archer.setNumLicenceArcher(rs.getString("NUMLICENCEARCHER"));
			archer.setNomArcher(rs.getString("NOMARCHER"));
			archer.setPrenomArcher(rs.getString("PRENOMARCHER"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return archer;
	}
}
