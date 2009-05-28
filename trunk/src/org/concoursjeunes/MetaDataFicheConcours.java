/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * @author Aurélien JEOFFRAY
 */
public class MetaDataFicheConcours implements PropertyChangeListener {
	private Date dateConcours;
	private String intituleConcours;
	private String filenameConcours;
	
	public MetaDataFicheConcours() { }
	
	/**
	 * @param dateConcours
	 * @param intituleConcours
	 */
	public MetaDataFicheConcours(Date dateConcours, String intituleConcours, String filenameConcours) {
		this.dateConcours = dateConcours;
		this.intituleConcours = intituleConcours;
		this.filenameConcours = filenameConcours;
	}

	/**
	 * @return  dateConcours
	 */
	public Date getDateConcours() {
		return dateConcours;
	}

	/**
	 * @param dateConcours  dateConcours à définir
	 */
	public void setDateConcours(Date dateConcours) {
		this.dateConcours = dateConcours;
	}

	/**
	 * @return  intituleConcours
	 */
	public String getIntituleConcours() {
		return intituleConcours;
	}

	/**
	 * @param intituleConcours  intituleConcours à définir
	 */
	public void setIntituleConcours(String intituleConcours) {
		this.intituleConcours = intituleConcours;
	}

	/**
	 * @return  filenameConcours
	 */
	public String getFilenameConcours() {
		return filenameConcours;
	}

	/**
	 * @param filenameConcours  filenameConcours à définir
	 */
	public void setFilenameConcours(String filenameConcours) {
		this.filenameConcours = filenameConcours;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof Parametre) {
			Parametre param = (Parametre)evt.getSource();
			if(evt.getPropertyName().equals("date")) { //$NON-NLS-1$
				setDateConcours(param.getDateDebutConcours());
			} else if(evt.getPropertyName().equals("intituleConcours")) { //$NON-NLS-1$
				setIntituleConcours(param.getIntituleConcours());
			} else if(evt.getPropertyName().equals("saveName")) { //$NON-NLS-1$
				setFilenameConcours(param.getSaveName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dateConcours == null) ? 0 : dateConcours.hashCode());
		result = PRIME * result + ((filenameConcours == null) ? 0 : filenameConcours.hashCode());
		result = PRIME * result + ((intituleConcours == null) ? 0 : intituleConcours.hashCode());
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
		final MetaDataFicheConcours other = (MetaDataFicheConcours) obj;
		if (dateConcours == null) {
			if (other.dateConcours != null)
				return false;
		} else if (!dateConcours.equals(other.dateConcours))
			return false;
		if (filenameConcours == null) {
			if (other.filenameConcours != null)
				return false;
		} else if (!filenameConcours.equals(other.filenameConcours))
			return false;
		if (intituleConcours == null) {
			if (other.intituleConcours != null)
				return false;
		} else if (!intituleConcours.equals(other.intituleConcours))
			return false;
		return true;
	}
}