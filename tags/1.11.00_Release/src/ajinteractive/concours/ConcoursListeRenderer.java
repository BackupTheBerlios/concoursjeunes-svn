/*
 * Created on 5 janv. 2005
 *
 */
package ajinteractive.concours;

import java.awt.*;
import javax.swing.*;

/**
 * Applique des icones à la liste des archers
 * 
 * @author Aurelien Jeoffray
 * @version 2.0
 *
 */
public class ConcoursListeRenderer extends JLabel implements ListCellRenderer {
     private ImageIcon normalIcon;
     private ImageIcon redIcon;

     /**
      * Construit le rendu des icone pour l'arbre
      * 
      * @param normalIcon - l'icone normal
      * @param redIcon - l'icone pas de cible attribué
      */
     public ConcoursListeRenderer(ImageIcon normalIcon, ImageIcon redIcon) {
         this.normalIcon = normalIcon;
         this.redIcon = redIcon;
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
         setIcon((s.startsWith("<html>")) ? this.redIcon : this.normalIcon);
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