package org.concoursjeunes.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.concoursjeunes.Cible;

/**
 * Affecte les icone à l'arbre des cibles
 * 
 * @author Aurélien Jeoffray
 * @version 2.0
 *
 */

public class CibleRenderer extends DefaultTreeCellRenderer {
	private ImageIcon archerIcon;
	private ImageIcon cibleIcon;

	/**
	 * Constructeur, initialise les icone à afficher
	 * 
	 * @param archerIcon - Icone de representation des archers
	 * @param cibleIcon - Icone de representation des cibles
	 */
	public CibleRenderer(ImageIcon archerIcon, ImageIcon cibleIcon) {
		this.archerIcon = archerIcon;
		this.cibleIcon = cibleIcon;
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
			setIcon(this.cibleIcon);
		} else {
			setIcon(this.archerIcon);
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