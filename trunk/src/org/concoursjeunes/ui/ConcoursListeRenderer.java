/*
 * Created on 5 janv. 2005
 *
 */
package org.concoursjeunes.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.concoursjeunes.ConcoursJeunes;
import org.concoursjeunes.Concurrent;

/**
 * Applique des icones à la liste des archers
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */
public class ConcoursListeRenderer extends JLabel implements ListCellRenderer {
	private final ImageIcon archerIcon;
	private final ImageIcon archerHandicapIcon;
	private final ImageIcon archerRedIcon;
	private final ImageIcon archerHandicapRedIcon;

	/**
	 * Construit le rendu des icone pour l'arbre
	 */
	public ConcoursListeRenderer() {
		archerIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.normal")); //$NON-NLS-1$
		archerHandicapIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.handicap")); //$NON-NLS-1$
		archerRedIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.notarget")); //$NON-NLS-1$
		archerHandicapRedIcon = new ImageIcon(
				ConcoursJeunes.ajrParametreAppli.getResourceString("path.ressources") + File.separator + //$NON-NLS-1$
				ConcoursJeunes.ajrParametreAppli.getResourceString("file.icon.archer.handicap.notarget")); //$NON-NLS-1$
	}

	/**
	 * This is the only method defined by ListCellRenderer.
	 * We just reconfigure the JLabel each time we're called.
	 * 
	 * @param list
	 * @param value
	 * @param index
	 * @param isSelected
	 * @param cellHasFocus
	 * 
	 * @return Component
	 */
	public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
	{
		String s = value.toString();
		setText(s);
		Concurrent concurrent = (Concurrent) value;
		if(concurrent.isHandicape())
			setIcon((concurrent.getCible() == 0) ? archerHandicapRedIcon : archerHandicapIcon);
		else
			setIcon((concurrent.getCible() == 0) ? archerRedIcon : archerIcon);
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
}