/**
 * 
 */
package org.concoursjeunes;

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
