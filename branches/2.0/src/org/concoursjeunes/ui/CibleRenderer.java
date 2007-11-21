package org.concoursjeunes.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.concoursjeunes.Cible;
import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.PasDeTir;
import org.concoursjeunes.TargetPosition;

/**
 * Affecte les icone à l'arbre des cibles
 * 
 * @author Aurélien Jeoffray
 * @version 2.0
 *
 */

public class CibleRenderer extends DefaultTreeCellRenderer {
	private final ImageIcon archerIcon;
	private final ImageIcon archerHandicapIcon;
	private final ImageIcon cibleIcon;
	private final ImageIcon disableIcon;
	
	private final PasDeTir pasDeTir;

	/**
	 * Constructeur, initialise les icone à afficher
	 * 
	 * @param archerIcon - Icone de representation des archers
	 * @param cibleIcon - Icone de representation des cibles
	 */
	public CibleRenderer(PasDeTir pasDeTir) {
		this.pasDeTir = pasDeTir;
		
		archerIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")); //$NON-NLS-1$
		archerHandicapIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.handicap")); //$NON-NLS-1$
		cibleIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.target")); //$NON-NLS-1$
		disableIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.disable")); //$NON-NLS-1$
	}

	/**
	 * affecte l'icone en fonction du type de noeud
	 * 
	 * @param tree - l'arbre affecté par le rendu
	 * @param value - l'objet à prendre en compte pour le rendu
	 * @param sel - Le noeud est il seléctionné ?
	 * @param expanded - Le noeud est il étendu ?
	 * @param leaf - Le noeud est il une feuille ?
	 * @param hasFocus - Le noeud a t'il le focus ?
	 * 
	 * @return Component - Retourne le renderer
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (isCible(value)) {
			setIcon(cibleIcon);
		} else if(value instanceof Concurrent) {
			Concurrent concurrent = (Concurrent) value;
			if(concurrent.isHandicape())
				setIcon(archerHandicapIcon);
			else
				setIcon(archerIcon);
		} else if (value instanceof TargetPosition) {
			TargetPosition targetPosition = (TargetPosition) value;
			if(pasDeTir.getTargets().get(targetPosition.getTarget() - 1).isReservedPosition(targetPosition.getPosition()))
				setIcon(disableIcon);
			else
				setIcon(archerIcon);
		}
		return this;
	}

	/**
	 * teste si un noeud est une cible
	 * 
	 * @param value - le contenue du noeud à tester
	 * 
	 * @return boolean - true si c'est une cible, false dans le cas contraire
	 */
	protected boolean isCible(Object value) {
		if(value instanceof Cible) {
			return true;
		} 
		return false;
	}
}