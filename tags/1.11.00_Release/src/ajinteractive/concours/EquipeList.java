/**
 * 
 */
package ajinteractive.concours;

import java.util.ArrayList;

/**
 * @author Aurelien Jeoffray
 *
 */
public class EquipeList {
   
    private ArrayList<Equipe> equipeList  = new ArrayList<Equipe>();
    
    private FicheConcours ficheConcours;
    
    public EquipeList() { 
    }
    
    public EquipeList(FicheConcours ficheConcours) { 
        this.ficheConcours = ficheConcours;
    }
    
    /**
     * 
     * @param newEquipe
     */
    public void add(Equipe newEquipe) {
        equipeList.add(newEquipe);
    }
    
    /**
     * Donne l'équipe à l'index donné
     * 
     * @param index - l'index de l'équipe
     * @return Equipe l'équipe à l'index donné
     */
    public Equipe get(int index) {
        return equipeList.get(index);
    }
    
    /**
     * Retourne l'équipe contenant le concourrent donné ou null si inexistant
     * 
     * @param concurrent - le concurrent à rechercher
     * @return l'Equipe à renvoyer
     */
    public Equipe containsConcurrent(Concurrent concurrent) {
        Equipe goodTeam = null;
        for(Equipe equipe : equipeList) {
            if(equipe.contains(concurrent)) {
                goodTeam = equipe;
                break;
            }
        }
        return goodTeam;
    }
    
    /**
     * Retire un concurrent du classement par équipe
     * 
     * @param concurrent - le concurrent à retirer
     * @return boolean true si retiré et false si non trouvé
     */
    public boolean removeConcurrent(Concurrent concurrent) {
        boolean remove = false;
        for(Equipe equipe : equipeList) {
            if(equipe.contains(concurrent)) {
                equipe.removeConcurrent(concurrent);
                
                if(equipe.getMembresEquipe().size() < ficheConcours.parametre.getNbMembresRetenu())
                    remove(equipe);
                remove = true;
                break;
            }
        }
        return remove;
    }
    
    /**
     * retire une équipe complete
     * 
     * @param equipe
     */
    public void remove(Equipe equipe) {
        equipeList.remove(equipe);
    }
    
    /**
     * retire toutes les équipes
     *
     */
    public void removeAll() {
        equipeList.clear();
    }
    
    /**
     * Retourne la liste complete des équipes
     * 
     * @return Equipe[]
     */
    public Equipe[] list() {
        Equipe[] equipes = new Equipe[equipeList.size()];
        equipeList.toArray(equipes);
        
        return equipes;
    }
    
    /**
     * Retourne la liste des équipes correspondant au critère donné
     * 
     * @param scna - le filtre de tri des équipes
     * @return Equipe[]
     */
    public Equipe[] list(DifferentiationCriteria scna) {
        ArrayList<Equipe> sel = new ArrayList<Equipe>();
        
        for(int i=0; i < this.equipeList.size(); i++) {
            if(this.equipeList.get(i).getDifferentiationCriteria().equals(scna))
                sel.add(this.equipeList.get(i));
        }

        return sel.toArray(new Equipe[sel.size()]);
    }
    
    /**
     * Tri les équipe sur la base de leurs points
     * 
     * @param no_sort_list
     * @return Equipe[] - la liste des équipes passé en parametre, mais trié
     */
    public Equipe[] sort(Equipe[] no_sort_list) {
        if(no_sort_list != null && no_sort_list.length > 0) {
            for(int i = 0; i < no_sort_list.length - 1; i++) {
                for(int j = i+1; j < no_sort_list.length; j++) {
                    if(no_sort_list[i].getTotalScore() < no_sort_list[j].getTotalScore()) {
                        Equipe tempEquipe = no_sort_list[i];
                        no_sort_list[i] = no_sort_list[j];
                        no_sort_list[j] = tempEquipe;
                    }
                }
            }
        }
        
        return no_sort_list;
    }
    
    /**
     * Donne le nombre d'équipe enregistré
     * 
     * @return int le nombre d'équipe
     */
    public int countEquipes() {
        return equipeList.size();
    }

    /**
     * Pour la sérialisation, la table des équipes
     * 
     * @return Renvoie equipeList.
     */
    public ArrayList<Equipe> getEquipeList() {
        return equipeList;
    }

    /**
     * Pour la sérialisation, la table des équipes
     * 
     * @param equipeList equipeList à définir.
     */
    public void setEquipeList(ArrayList<Equipe> equipeList) {
        this.equipeList = equipeList;
    }

    /**
     * la fiche concours associé à la liste
     * 
     * @return Renvoie ficheConcours.
     */
    public FicheConcours getFicheConcours() {
        return ficheConcours;
    }

    /**
     * la fiche concours associé à la liste
     * 
     * @param ficheConcours ficheConcours à définir.
     */
    public void setFicheConcours(FicheConcours ficheConcours) {
        this.ficheConcours = ficheConcours;
    }
}
