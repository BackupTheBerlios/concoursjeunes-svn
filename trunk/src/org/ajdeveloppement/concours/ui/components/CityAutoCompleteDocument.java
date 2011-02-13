/*
 * Créé le 31 déc. 2010 à 13:44:07 pour ConcoursJeunes / ArcCompétition
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
 *
 * http://www.concoursjeunes.org
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.concoursjeunes.ApplicationCore;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class CityAutoCompleteDocument extends PlainDocument {
	
	private PreparedStatement pstmt;
	private JTextField textField;
	
	public CityAutoCompleteDocument(JTextField textField) throws SQLException {
		this.textField = textField;
		String sql = "select NOM from VILLE where upper(NOM) like concat(?, '%') order by NOM limit 1"; //$NON-NLS-1$
		
		pstmt = ApplicationCore.dbConnection.prepareStatement(sql);
	}
	
	/**
	 * Invoqué a l'insertion du chaîne dans le champs de saisi
	 * 
	 * @param offs - position du début de l'insertion
	 * @param str - la chaîne à insérer
	 * @param a - les attributs d'insertions
	 * 
	 * @throws BadLocationException
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if(str == null)
			return;
		super.insertString(offs, str, a);
		
		autoComplete(offs + str.length());
	}
	
	/**
	 * Invoqué à la suppression d'une chaîne dans le champs de saisi
	 * 
	 * @param offs - la position du début de la zone à supprimer
	 * @param len - la taille de la chaîne à supprimer
	 * 
	 * @throws BadLocationException
	 */
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		
		int caretpos = offs - 1;
		if(caretpos < 0) caretpos = 0;
		
		autoComplete(caretpos);
	}
	
	/**
	 * Invoqué au remplacement d'une portion de la chaîne du champs de saisi
	 * 
	 * @param offset - la position du début de la chaîne à remplacer
	 * @param length - la taille de la chaîne à remplacer
	 * @param text - la chaîne de remplacement
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
	 * @param offs
	 * @param strLength
	 * @throws BadLocationException
	 */
	private void autoComplete(int offs) throws BadLocationException {
		String searchString = getText(0, getLength()).toUpperCase().replace(' ', '-');
		
		try {
			pstmt.setString(1, searchString);

			ResultSet rs = pstmt.executeQuery();
			try {
				if(rs.first()) {
					String cityResult = rs.getString("NOM"); //$NON-NLS-1$
					
					super.remove(0, getLength());
					super.insertString(0, cityResult, null);
					textField.setCaretPosition(cityResult.length());
					textField.moveCaretPosition(offs);
				}
			} finally {
				rs.close();
			}
		} catch(SQLException e) {
			//pas d'auto complément sur erreur. On se contente de logger sur la console
			e.printStackTrace();
		}
	}
	
	
}
