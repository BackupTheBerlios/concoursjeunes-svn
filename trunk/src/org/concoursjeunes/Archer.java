/**
 * 
 */
package org.concoursjeunes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class représentant un archer independament d'un concours
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public class Archer {

	private String nomArcher        = ""; //$NON-NLS-1$
	private String prenomArcher     = ""; //$NON-NLS-1$
	private String numLicenceArcher = ""; //$NON-NLS-1$
	private Entite club             = new Entite();
	private boolean certificat      = false;

	private CriteriaSet criteriaSet;

	public Archer() { }

	/**
	 * Retourne le numero de licence de l'archer
	 * 
	 * @return le numero de licence (pour la ffta, 6 chiffres + 1 lettre)
	 */
	public String getNumLicenceArcher() {
		return numLicenceArcher;
	}

	/**
	 * Définit le numero de licence de l'archer
	 * 
	 * @param numLicenceArcher le numero de licence de l'archer
	 */
	public void setNumLicenceArcher(String numLicenceArcher) {
		this.numLicenceArcher = numLicenceArcher;
	}

	/**
	 * Retourne le nom de l'archer
	 * 
	 * @return le nom de l'archer
	 */
	public String getNomArcher() {
		return nomArcher;
	}

	/**
	 * Définit le nom de l'archer
	 * 
	 * @param nomArcher le nom de l'archer
	 */
	public void setNomArcher(String nomArcher) {
		this.nomArcher = nomArcher;
	}

	/**
	 * Retourne le prénom de l'archer
	 * 
	 * @return le prenom de l'archer
	 */
	public String getPrenomArcher() {
		return prenomArcher;
	}

	/**
	 * Définit le prénom de l'archer
	 * 
	 * @param prenomArcher le prenom de l'archer
	 */
	public void setPrenomArcher(String prenomArcher) {
		this.prenomArcher = prenomArcher;
	}

	/**
	 * Retourne le club (Compagnie) auquel appartient l'archer
	 * 
	 * @return club le club de l'archer
	 */
	public Entite getClub() {
		return club;
	}

	/**
	 * Définit le club (Entite legal) de l'archer
	 * 
	 * @param club l'objet Entite representant le club de l'archer
	 */
	public void setClub(Entite club) {
		this.club = club;
	}

	/**
	 * Retourne les critères distinguant l'archer
	 * 
	 * @return criteriaSet le jeux de critères distinguant l'archer
	 */
	public CriteriaSet getCriteriaSet() {
		return criteriaSet;
	}

	/**
	 * Définit le jeux de critère d'istinguant l'archer
	 * 
	 * @param criteriaSet le jeux de critères de distinction
	 */
	public void setCriteriaSet(CriteriaSet criteriaSet) {
		this.criteriaSet = criteriaSet;
	}

	/**
	 * Indique si l'archer possede ou non un certificat medical de non contre indiquation
	 * à la pratique du tir à l'arc en competition
	 * 
	 * @return true si l'archer possede un certificat, false sinon
	 */
	public boolean isCertificat() {
		return certificat;
	}

	/**
	 * Définit si l'archer possede (true) ou non (false) un certificat
	 * 
	 * @param certificat true si l'archer possede un certificat, false sinon
	 */
	public void setCertificat(boolean certificat) {
		this.certificat = certificat;
	}

	/**
	 * Retourne l'identifiant de l'archer (Concatenation du nom et du prenom)
	 * 
	 * @return l'identifiant de l'archer
	 */
	public String getID() {
		return nomArcher + " " + prenomArcher;
	}

	/**
	 * Test si l'archer possede dans la base des homonymes (même nom et prenom)
	 * 
	 * @return true su l'archer possede des homonyme, false sinon
	 */
	public boolean haveHomonyme() {
		Archer aComparant = new Archer();
		aComparant.setNomArcher(this.nomArcher);
		aComparant.setPrenomArcher(this.prenomArcher);

		ArrayList<Archer> homonyme = getArchersInDatabase(aComparant, null, "");

		return (homonyme.size() > 1);
	}

	/**
	 * Retourne une liste d'archer en provenance de la base de données en fonction
	 * des critères de recherche fournit en parametres
	 * 
	 * @param aGeneric objet Archer generique servant de filtre de recherche (la recherche se
	 * fait sur les champs renseigné, le caractères genérique (%, ?) sont accepté
	 * 
	 * @param reglement le reglement à appliqué aux objets archers retourné
	 * 
	 * @param orderfield l'ordre de trie des objets retourné. Doivent être listé dans 
	 * l'ordre les champs de la base de données (table ARCHERS) servant au trie.
	 * 
	 * @return la liste des archers correspondant aux critères de recherche
	 */
	public static ArrayList<Archer> getArchersInDatabase(Archer aGeneric, Reglement reglement, String orderfield) {
		ArrayList<Archer> archers = new ArrayList<Archer>();
		Statement stmt = null;
		try {
			stmt = ConcoursJeunes.dbConnection.createStatement();

			String sql = "select * from archers ";
			if(aGeneric != null) {
				sql += "where ";
				ArrayList<String> filters = new ArrayList<String>();
				if(aGeneric.getNumLicenceArcher().length() > 0) {
					filters.add("NUMLICENCEARCHER like \"" + aGeneric.getNumLicenceArcher() + "\"");
				}
				if(aGeneric.getNomArcher().length() > 0) {
					filters.add("NOMARCHER like \"" + aGeneric.getNomArcher() + "\"");
				}
				if(aGeneric.getPrenomArcher().length() > 0) {
					filters.add("UPPER(PRENOMARCHER) like \"" + aGeneric.getPrenomArcher().toUpperCase() + "\"");
				}
				if(aGeneric.getClub().getAgrement().length() > 0) {
					filters.add("AGREMENTENTITE like \"" + aGeneric.getClub().getAgrement() + "\"");
				}

				for(String filter : filters) {
					sql += " and " + filter;
				}
			}
			sql = sql.replaceFirst(" and ", "");
			if(orderfield.length() > 0)
				sql += "order by " + orderfield;

			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				Archer archer = ArcherFactory.getArcher(rs, reglement);

				archers.add(archer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(Exception e) { }
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((nomArcher == null) ? 0 : nomArcher.hashCode());
		result = PRIME * result + ((numLicenceArcher == null) ? 0 : numLicenceArcher.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Archer other = (Archer) obj;
		if (nomArcher == null) {
			if (other.getNomArcher() != null)
				return false;
		} else if (!nomArcher.equals(other.getNomArcher()))
			return false;
		if (numLicenceArcher == null) {
			if (other.getNumLicenceArcher() != null)
				return false;
		} else if (!numLicenceArcher.equals(other.getNumLicenceArcher()))
			return false;
		return true;
	}
}
