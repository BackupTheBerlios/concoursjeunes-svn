/*
 * Créer le 20 déc. 06 - 17:16:36
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.concoursjeunes;

import java.util.EventListener;

/**
 * 
 * 
 * @author Aurélien JEOFFRAY
 * @version 1.0
 */
public interface AutoCompleteDocumentListener extends EventListener {
	public void concurrentFinded(AutoCompleteDocumentEvent e);
	public void concurrentNotFound(AutoCompleteDocumentEvent e);
	public void entiteFinded(AutoCompleteDocumentEvent e);
	public void entiteNotFound(AutoCompleteDocumentEvent e);
}
