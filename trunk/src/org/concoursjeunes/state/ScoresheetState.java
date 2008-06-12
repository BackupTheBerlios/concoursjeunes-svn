/*
 * Créer le 7 juin 08 à 17:20:15 pour ConcoursJeunes
 *
 * Copyright 2002-2008 - Aurélien JEOFFRAY
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
 * pri connaissance de la licence CeCILL, et que vous en avez accepté les
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
package org.concoursjeunes.state;

import static org.concoursjeunes.ApplicationCore.ajrLibelle;
import static org.concoursjeunes.ApplicationCore.ajrParametreAppli;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Concurrent;

import ajinteractive.standard.common.AJToolKit;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class ScoresheetState {
	private static final double topMargin = 0.5;
	private static final double bottomMargin = 0.5;
	private static final double rightMargin = 0.5;
	private static final double leftMargin = 0.5;
	
	private Document document = new Document(
			PageSize.A4.rotate(),
			AJToolKit.centimeterToDpi(leftMargin),
			AJToolKit.centimeterToDpi(rightMargin),
			AJToolKit.centimeterToDpi(topMargin),
			AJToolKit.centimeterToDpi(bottomMargin));
	
	private PdfContentByte cb;
	private float pageWidth = document.getPageSize().getWidth();
	private float pageHeight = document.getPageSize().getHeight();
	
	private Concurrent concurrent;
	
	/**
	 * 
	 */
	public ScoresheetState(Concurrent concurrent) {
		this.concurrent = concurrent;
		
		paint();
	}
	
	public void paint() {
		try {
			File tmpFile = File.createTempFile("cta", ajrParametreAppli.getResourceString("extention.pdf")); //$NON-NLS-1$ //$NON-NLS-2$
			String filePath = tmpFile.getCanonicalPath();
			tmpFile.deleteOnExit();
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
			writer.setFullCompression();

			document.addCreationDate();
			document.addAuthor(ApplicationCore.getConfiguration().getClub().getNom());
			document.addProducer();
			document.addTitle(ajrLibelle.getResourceString("state.scoresheet.title")); //$NON-NLS-1$
			document.open();
			
			cb = writer.getDirectContent();
			Graphics2D g2 = cb.createGraphicsShapes(pageWidth, pageHeight);
			
			g2.drawString("<html>" + concurrent.getNomArcher() + " " + concurrent.getPrenomArcher() //$NON-NLS-1$
					+ "<br>" + concurrent.getNumLicenceArcher() + "\n"
					+ concurrent.getClub().getNom() + "(" + concurrent.getClub().getVille() + ")\n"
					+ concurrent.getCriteriaSet().toString() + "</html>", 
					AJToolKit.centimeterToDpi(leftMargin),
					AJToolKit.centimeterToDpi(topMargin));
			
			g2.dispose();
			
			document.close();
			
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(tmpFile);
			} else {
				if (ApplicationCore.getConfiguration() != null) {
					
					String NAV = ApplicationCore.getConfiguration().getPdfReaderPath();

					System.out.println(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
					Runtime.getRuntime().exec(NAV + " " + tmpFile.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
