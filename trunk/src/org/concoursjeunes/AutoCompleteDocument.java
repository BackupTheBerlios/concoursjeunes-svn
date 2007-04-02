/**
 * 
 */
package org.concoursjeunes;

import javax.swing.JTextField;
import javax.swing.event.EventListenerList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Réalise la saisi semi automatique d'un concurrent en fonction de la chaine saisi manuellement et des informations présente en base de données
 * @author  Aurélien JEOFFRAY
 */
public class AutoCompleteDocument extends PlainDocument {
	
	/**
	 * Type de recherche : recherche par le nom
	 */
	public static final int NAME_SEARCH = 1;
	/**
	 * Type de recherche : recherche par le numero de licence
	 */
	public static final int NUMLICENCE_SEARCH = 2;
	/**
	 * Type de recherche : recherche par le prenom
	 */
	public static final int FIRSTNAME_SEARCH = 3;
	/**
	 * Type de recherche : recherche par le nom du club
	 */
	public static final int CLUB_SEARCH = 4;
	/**
	 * Type de recherche : recherche par le numero d'agrement du club
	 */
	public static final int AGREMENT_SEARCH = 5;
	
	private static Concurrent concurrent;
	private Reglement reglement;
	private static Entite entite;
	private static boolean autocompleteNom = true;
	private static boolean autocompleteLicence = true;
	private static boolean autocompleteClub = true;
	private static boolean autocompleteAgrement = true;
	
	private JTextField textField;
	private EventListenerList listeners = new EventListenerList();

	private int typeSearch = NAME_SEARCH;
	
	/**
	 * Cré un document de saisi semi automatique de concurrent
	 * 
	 * @param textField - le champs de reference pour la saisi semi automatique
	 * @param typeSearch - le type de recherche à effectuer (represente la nature de la saisi)
	 */
	public AutoCompleteDocument(JTextField textField, int typeSearch, Reglement reglement) {
		this.textField = textField;
		this.typeSearch = typeSearch;
		this.reglement = reglement;
	}
	
	/**
	 * Ajoute un auditeur aux evenments d'autocomplement
	 * 
	 * @param autoCompleteDocumentListener - l'objet auditeur
	 */
	public void addAutoCompleteDocumentListener(AutoCompleteDocumentListener autoCompleteDocumentListener) {
		listeners.add(AutoCompleteDocumentListener.class, autoCompleteDocumentListener);
	}
	
	/**
	 * supprime un auditeur aux evenments d'autocomplement
	 * 
	 * @param autoCompleteDocumentListener - l'objet auditeur
	 */
	public void removeAutoCompleteDocumentListener(AutoCompleteDocumentListener autoCompleteDocumentListener) {
		listeners.remove(AutoCompleteDocumentListener.class, autoCompleteDocumentListener);
	}
	
	/**
	 * Invoqué a l'insertion du chaine dans le champs de saisi
	 * 
	 * @param offs - position du début de l'insertion
	 * @param str - la chaine à inserer
	 * @param a - les attributs d'insertions
	 * 
	 * @throws BadLocationException
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offs, str, a);
		//this.

		switch(typeSearch) {
			case NAME_SEARCH:
				insertConcurrentWithName(offs + str.length(), false);
				break;
			case NUMLICENCE_SEARCH:
				insertConcurrentWithNumLicence(offs + str.length(), false);
				break;
			case FIRSTNAME_SEARCH:
				insertConcurrentWithFirstName(offs + str.length(), false);
				break;
			case CLUB_SEARCH:
				insertEntiteWithClubName(offs + str.length(), false);
				break;
			case AGREMENT_SEARCH:
				insertEntiteWithAgrement(offs + str.length(), false);
				break;
		}
		
	}
	
	/**
	 * Invoqué à la suppression d'une chaine dans le champs de saisi
	 * 
	 * @param offs - la position du debut de la zone à supprimer
	 * @param len - la taille de la chaine à supprimer
	 * 
	 * @throws BadLocationException
	 */
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		
		int caretpos = offs - 1;
		if(caretpos < 0) caretpos = 0;
		
		switch(typeSearch) {
			case NAME_SEARCH:
				insertConcurrentWithName(offs, true);
				break;
			case NUMLICENCE_SEARCH:
				insertConcurrentWithNumLicence(caretpos, false);
				break;
			case FIRSTNAME_SEARCH:
				insertConcurrentWithFirstName(offs, true);
				break;
			case CLUB_SEARCH:
				insertEntiteWithClubName(offs, true);
				break;
			case AGREMENT_SEARCH:
				insertEntiteWithAgrement(caretpos, false);
				break;
		}
	}
	
	/**
	 * Invoqué au remplacement d'une portion de la chaine du champs de saisi
	 * 
	 * @param offset - la position du debut de la chaine à remplacer
	 * @param length - la taille de la chaine à remplacer
	 * @param text - la chaine de remplacement
	 * @param attrs - les attributs de remplacement
	 * 
	 * @throws BadLocationException
	 */
	@Override
	public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		if(getText(offset, length).equals(text))
			return;
		
		super.remove(offset, length);
		insertString(offset, text, attrs);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {
		try {
			if(getText(0, getLength()).equals(text))
				return;
			
			super.remove(0, getLength());
			super.insertString(0, text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recherche le concurrent à afficher en fonction de la chaine actuellement
	 * dans le modéle et des informations en base
	 * 
	 * @param caretpos
	 * @param strict
	 * @throws BadLocationException
	 */
	private void insertConcurrentWithName(int caretpos, boolean strict) throws BadLocationException {
		if(!autocompleteNom)
			return;
		
		String searchString = getText(0, getLength());
		Archer searchArcher = new Archer();
		searchArcher.setNomArcher(searchString.toUpperCase() + "%");
		
		if(getLength() > 0) {
			concurrent = Concurrent.getArchersInDatabase(searchArcher, reglement, "NOMARCHER").get(0);
			autocompleteLicence = false;
		} else {
			concurrent = null;
			autocompleteLicence = true;
		}

		//aucun concurrent trouvé ou concurrent trouvé ne correspondant pas à la recherche
		if(concurrent == null || (strict && !concurrent.getNomArcher().equals(searchString))) {
			if(strict && concurrent != null)
				concurrent = null;

			fireConcurrentNotFound();

		} else {
			super.remove(0, getLength());
			super.insertString(0, concurrent.getNomArcher(), null);
			textField.setCaretPosition(concurrent.getNomArcher().length());
			textField.moveCaretPosition(caretpos);
			
			fireConcurrendFinded(concurrent, searchArcher);
		}
	}
	
	private void insertConcurrentWithFirstName(int caretpos, boolean strict) throws BadLocationException {
		if(!autocompleteNom)
			return;
		
		String searchString = getText(0, getLength());
		Archer searchArcher = new Archer();
		Concurrent tempConcurrent = null;
		if(getLength() > 0 && concurrent != null) {
			searchArcher.setNomArcher(concurrent.getNomArcher());
			searchArcher.setPrenomArcher(searchString + "%");
			
			tempConcurrent = Concurrent.getArchersInDatabase(searchArcher, reglement, "PRENOMARCHER").get(0);
			
			autocompleteLicence = false;
		} else {
			autocompleteLicence = (concurrent != null);
		}

		if(tempConcurrent == null || (strict && !tempConcurrent.getPrenomArcher().equals(getText(0, getLength())))) {
			if(concurrent != null)
				concurrent.setPrenomArcher(getText(0, getLength()));
			fireConcurrentNotFound();

		} else {
			concurrent = tempConcurrent;
			
			super.remove(0, getLength());
			super.insertString(0, concurrent.getPrenomArcher(), null);
			textField.setCaretPosition(concurrent.getPrenomArcher().length());
			textField.moveCaretPosition(caretpos);
			
			fireConcurrendFinded(concurrent, searchArcher);
		}
	}
	
	private void insertConcurrentWithNumLicence(int caretpos, boolean strict) throws BadLocationException {
		if(!strict)
			strict = !autocompleteLicence;
		
		String searchString = getText(0, getLength());
		Archer searchArcher = new Archer();
		searchArcher.setNumLicenceArcher(searchString + "%");
		if(getLength() > 0) {
			concurrent = Concurrent.getArchersInDatabase(searchArcher, reglement, "NUMLICENCEARCHER").get(0);
		} else {
			concurrent = null;
		}
		
		if(concurrent == null || (strict && !concurrent.getNumLicenceArcher().equals(getText(0, getLength())))) {
			if(strict && concurrent != null)
				concurrent = null;
			
			autocompleteNom = (getLength() == 0);			
			
			fireConcurrentNotFound();

		} else {
			autocompleteNom = true;
			autocompleteLicence = true;
			
			super.remove(0, getLength());
			super.insertString(0, concurrent.getNumLicenceArcher(), null);
			textField.setCaretPosition(concurrent.getNumLicenceArcher().length());
			textField.moveCaretPosition(caretpos);
			
			fireConcurrendFinded(concurrent, searchArcher);
		}
	}
	
	private void insertEntiteWithClubName(int caretpos, boolean strict) throws BadLocationException {
		if(!autocompleteClub)
			return;
		
		String searchString = getText(0, getLength());
		Entite searchEntite = new Entite();
		searchEntite.setVille(searchString.toUpperCase() + "%");
		if(getLength() > 0) {
			entite = Entite.getEntitesInDatabase(searchEntite, "VILLEENTITE").get(0);
			autocompleteAgrement = false;
		} else {
			entite = null;
			autocompleteAgrement = true;
		}

		//aucun concurrent trouvé ou concurrent trouvé ne correspondant pas à la recherche
		if(entite == null || (strict && !entite.getVille().equals(searchString))) {
			if(strict && entite != null)
				entite = null;

			fireEntiteNotFound();

		} else {
			super.remove(0, getLength());
			super.insertString(0, entite.getVille(), null);
			textField.setCaretPosition(entite.getVille().length());
			textField.moveCaretPosition(caretpos);
			
			fireEntiteFinded(entite, searchEntite);
		}
	}
	
	private void insertEntiteWithAgrement(int caretpos, boolean strict) throws BadLocationException {
		if(!strict)
			strict = !autocompleteAgrement;
		
		String searchString = getText(0, getLength());
		Entite searchEntite = new Entite();
		searchEntite.setAgrement(searchString.toUpperCase() + "%");
		if(getLength() > 0) {
			entite = Entite.getEntitesInDatabase(searchEntite, "AGREMENTENTITE").get(0);
		} else {
			entite = null;
		}
		
		if(entite == null || (strict && !entite.getAgrement().equals(getText(0, getLength())))) {
			if(strict && entite != null)
				entite = null;
			
			autocompleteClub = (getLength() == 0);			
			
			fireEntiteNotFound();

		} else {
			autocompleteClub = true;
			autocompleteAgrement = true;
			
			super.remove(0, getLength());
			super.insertString(0, entite.getAgrement(), null);
			textField.setCaretPosition(entite.getAgrement().length());
			textField.moveCaretPosition(caretpos);
			
			fireEntiteFinded(entite, searchEntite);
		}
	}

	private void fireConcurrendFinded(Concurrent concurrent, Archer searchArcher) {
		for(AutoCompleteDocumentListener acdl : listeners.getListeners(AutoCompleteDocumentListener.class)) {
			AutoCompleteDocumentEvent autoCompleteDocumentEvent = new AutoCompleteDocumentEvent(textField, concurrent);
			autoCompleteDocumentEvent.setGenericArcher(searchArcher);
			acdl.concurrentFinded(autoCompleteDocumentEvent);
		}
	}
	
	private void fireConcurrentNotFound() {
		for(AutoCompleteDocumentListener acdl : listeners.getListeners(AutoCompleteDocumentListener.class)) {
			acdl.concurrentNotFound(new AutoCompleteDocumentEvent(textField, (Concurrent)null));
		}
	}
	
	private void fireEntiteFinded(Entite entite, Entite searchEntite) {
		for(AutoCompleteDocumentListener acdl : listeners.getListeners(AutoCompleteDocumentListener.class)) {
			AutoCompleteDocumentEvent autoCompleteDocumentEvent = new AutoCompleteDocumentEvent(textField, entite);
			autoCompleteDocumentEvent.setGenericEntite(searchEntite);
			acdl.entiteFinded(autoCompleteDocumentEvent);
		}
	}
	
	private void fireEntiteNotFound() {
		for(AutoCompleteDocumentListener acdl : listeners.getListeners(AutoCompleteDocumentListener.class)) {
			acdl.entiteNotFound(new AutoCompleteDocumentEvent(textField, (Entite)null));
		}
	}
}
