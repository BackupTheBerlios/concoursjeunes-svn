/*
 * Created on 7 janv. 2005
 *
 */
package ajinteractive.concours;

/**
 * Reprensentation de l'entite 'Cible' avec ses parametres de
 * distances, de blason appliqué et les concurrents qui y sont associés
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */
public class Cible {

	private int iNumCible				= 0;	//le numero de la cible
	private Concurrent[] concurrents;			//le liste des concurrents présents sur la cible
	private int iNbArcher				= 0;	//le nombre d'archer sur la cible
	//private DistancesEtBlason distAndBlas;		//la distance en m de la cible aux pas de tir
    
    private FicheConcours ficheConcours;
	
	/**
	 * Initialise une cible avec son numero et son nombre max d'archer
	 * 
	 * @param iNumCible - le numero de la cible
	 * @param ficheConcours - la fiche concours associé à la cible
	 */
	public Cible(int iNumCible, FicheConcours ficheConcours) {
		this.iNumCible = iNumCible;
		this.ficheConcours = ficheConcours;
        this.concurrents = new Concurrent[ficheConcours.parametre.getNbTireur()];
	}
	
	/**
	 * Donne le concurrent à la postition x
	 * 
	 * @param position
	 * @return Concurrent - le concurrent à la position donnée ou <i>null</i> si aucun concurrent trouvé
	 */
	public Concurrent getConcurrentAt(int position) {
		return this.concurrents[position];
	}
	
	/**
	 * Retourne le numero de la cible
	 * 
	 * @return int - le numero de la cible
	 */
	public int getNumCible() {
		return this.iNumCible;
	}
	
	/**
	 * Retourne le nombre d'archer present sur la cible
	 * 
	 * @return int - le nombre d'archer sur la cible
	 */
	public int getNbArcher() {
		return this.iNbArcher;
	}
	
	/**
	 * insere un concurrent à la premiere position libre et retourne
	 * cette position ou -1 si ecehc
	 * 
	 * @param concurrent - le concurrent à inserer
	 * @return int - la position de concurrent
	 */
	public int insertConcurrent(Concurrent concurrent) {
		int position = -1;
		
		if(concurrent != null) {
			for(int i = 0; i < this.concurrents.length; i++) {
				if(this.concurrents[i] == null) {
					concurrent.setCible(this.iNumCible);
					concurrent.setPosition(i);
                    this.concurrents[i] = concurrent;
					
                    this.iNbArcher++;
					
					position = i;
					
					break;
				}
			}
		}
		return position;
	}
	
	/**
	 * Supprime un concurrent à la position donnée
	 * 
	 * @param position - la position du concurrent à supprimer
	 */
	public void removeConcurrentAt(int position) {
		if(this.concurrents[position] != null)
            this.iNbArcher--;
        this.concurrents[position] = null;
	}
	
	/**
	 * Supprime un concurrent donné
	 * 
	 * @param concurrent - le concurrent à supprimer
	 */
	public void removeConcurrent(Concurrent concurrent) {
		for(int i = 0; i < this.concurrents.length; i++) {
			if(this.concurrents[i] == concurrent) {
                this.concurrents[i] = null;
                this.iNbArcher--;
                break;
			}
		}
	}
    
    /**
     * Retire tous les concurrents de la cible
     *
     */
    public void removeAll() {
        for(int i = 0; i < concurrents.length; i++)
            concurrents[i] = null;
        iNbArcher = 0;
    }
	
	/**
	 * Place un concurrent sur la cible à la position donné
	 * 
	 * @param concurrent - le concurrent à placer
	 * @param position - la positionn de ce concurrent
	 */
	public void setConcurrentAt(Concurrent concurrent, int position) {
		if(concurrent != null) {
			concurrent.setCible(this.iNumCible);
			concurrent.setPosition(position);
			if(this.concurrents[position] == null)
                this.iNbArcher++;
		} else {
			if(this.concurrents[position] != null)
                this.iNbArcher--;
		}
		
        this.concurrents[position] = concurrent;
	}
	
	/**
	 * Place un concurrent sur la cible
	 * 
	 * @param concurrent - le concurrent à placer
	 */
	public void addConcurrent(Concurrent concurrent) {
		if(concurrent != null) {
			concurrent.setCible(iNumCible);
			
			if(concurrents[concurrent.getPosition()] == null)
                iNbArcher++;
            concurrents[concurrent.getPosition()] = concurrent;
		}
	}
    
    /**
     * Donne la disposition de la cible
     * 
     * @return DistancesEtBlason - la disposition de la cible
     */
    public DistancesEtBlason getDistancesEtBlason() {
        DistancesEtBlason db = null;
        
        if(iNbArcher > 0) {
            Concurrent firstConcurrent = null;
            for(Concurrent concurrent : concurrents) {
                if(concurrent != null) {
                    firstConcurrent = concurrent;
                    break;
                }
            }
            
            DifferentiationCriteria dti = firstConcurrent.getDifferentiationCriteria();
            dti.setFiltreCriteria(ficheConcours.parametre.getPlacementFilter());
            db = ficheConcours.parametre.getCorrespondanceDifferentiationCriteria_DB(dti);
        }
        
        return db;
    }
	
    /**
     * Renvoie le Libelle qualifié de la cible
     * 
     */
    @Override
	public String toString() {
        String strCouleur = "<font color=\"#00AA00\">";
        if(ficheConcours.parametre.getNbTireur() == this.iNbArcher)
            strCouleur = "<font color=\"#0000FF\">";
		String strCibleLibelle = "<html>" + strCouleur + "<b>" + ConcoursJeunes.ajrLibelle.getResourceString("treenode.cible")
            + ((this.iNumCible < 10) ? "0" : "")
			+ this.iNumCible + "</b> (";
        DistancesEtBlason db = getDistancesEtBlason();
		if(db != null) {
			for(int i = 0; i < db.getDistance().length; i++) {
				if(i == 0 || (i > 0 && db.getDistance()[i] != db.getDistance()[i-1])) {
					if(i > 0) 
						strCibleLibelle += "/";
					strCibleLibelle += db.getDistance()[i] + "m";
				}
			}
            strCibleLibelle += ", " + db.getBlason() + "cm";
		}
		
		strCibleLibelle += ") (" + this.iNbArcher + ")</font></html>";
		
		return strCibleLibelle;
	}
}