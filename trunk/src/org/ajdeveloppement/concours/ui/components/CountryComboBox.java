/*
 * Créé le 21 avr. 2011 à 11:53:04 pour ArcCompetition / ArcCompétition
 *
 * Copyright 2002-2011 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
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
package org.ajdeveloppement.concours.ui.components;

import java.awt.Component;
import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.ajdeveloppement.concours.ApplicationCore;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class CountryComboBox extends JComboBox {

	private List<Country> countries = new ArrayList<CountryComboBox.Country>();
	/**
	 * 
	 */
	public CountryComboBox() {
		super();

		Locale[] locales = Locale.getAvailableLocales();
		List<Country> countries = new ArrayList<Country>();
		for (Locale locale : locales) {
			String iso = ""; //$NON-NLS-1$
			try {
					iso = locale.getISO3Country();
			} catch (MissingResourceException e) {
			}
			String code = locale.getCountry();
			String name = locale.getDisplayCountry();

			Country country = new Country(iso, code, name);
			if (!iso.isEmpty() && !code.isEmpty() && !name.isEmpty() && !countries.contains(country)) {
				countries.add(country);
			}
		}

		Collections.sort(countries, new CountryComparator());
		for (Country country : countries) {
			this.countries.add(country);
			addItem(country);
		}
		
		setRenderer(new DefaultListCellRenderer() {
			/* (non-Javadoc)
			 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				
				Country country = (Country)value;
				JLabel item = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				
				item.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + File.separator +  //$NON-NLS-1$
						"countries" + File.separator + country.getCode().toLowerCase() + ".png")); //$NON-NLS-1$ //$NON-NLS-2$
				
				return item;
			}
			
		});
	}
	
	public void setSelectedCountry(String countryCode) {
		for(int i = 0; i < countries.size(); i++)
			if(countries.get(i).getCode().equalsIgnoreCase(countryCode))
				setSelectedIndex(i);
	}

	private class CountryComparator implements Comparator<Country> {
		private Comparator<Object> comparator;

		CountryComparator() {
			comparator = Collator.getInstance();
		}

		public int compare(Country o1, Country o2) {
			return comparator.compare(o1.getName(), o2.getName());
		}
	}

	public static class Country {
		private String iso;

		private String code;

		private String name;

		public Country(String iso, String code, String name) {
			this.iso = iso;
			this.code = code;
			this.name = name;
		}
		
		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return iso
		 */
		public String getIso() {
			return iso;
		}

		/**
		 * @return code
		 */
		public String getCode() {
			return code;
		}

		public String toString() {
			return getName();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((code == null) ? 0 : code.hashCode());
			result = prime * result + ((iso == null) ? 0 : iso.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Country other = (Country) obj;
			if (code == null) {
				if (other.code != null)
					return false;
			} else if (!code.equals(other.code))
				return false;
			if (iso == null) {
				if (other.iso != null)
					return false;
			} else if (!iso.equals(other.iso))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
}
