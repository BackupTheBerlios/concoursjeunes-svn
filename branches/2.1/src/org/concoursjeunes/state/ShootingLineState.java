/*
 * Créé le 15/03/2008 à 13:08 pour ConcoursJeunes
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

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.concoursjeunes.*;

import ajinteractive.standard.common.AJToolKit;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class ShootingLineState {
	
	private static final double topMargin = 0.5;
	private static final double bottomMargin = 0.5;
	private static final double rightMargin = 0.5;
	private static final double leftMargin = 0.5;
	
	private PasDeTir pasDeTir;
	
	private Document document;
	private PdfWriter writer;
	
	private PdfContentByte cb;
	private float pageWidth = 0;
	private float pageHeight = 0;
	private int maxDistance = 0;
	
	public ShootingLineState(PasDeTir pasDeTir, Document document, PdfWriter writer) {
		this.pasDeTir = pasDeTir;
		this.maxDistance = getMaxDistance(0);
		this.document = document;
		this.writer = writer;
		
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(
			AJToolKit.centimeterToDpi(leftMargin),
			AJToolKit.centimeterToDpi(rightMargin),
			AJToolKit.centimeterToDpi(topMargin),
			AJToolKit.centimeterToDpi(bottomMargin));
		
		pageWidth = document.getPageSize().getWidth();
		pageHeight = document.getPageSize().getHeight();
		
		paint();
	}
	
	/*public Document getDocument() {
		return document;
	}*/
	
	public void paint() {
		int page = 1;
		try {
			//String concoursFileName = pasDeTir.getFicheConcours().getParametre().getSaveName();
			//String concoursDirectory = concoursFileName.substring(0, concoursFileName.length() - 4);
			
			/*File pdfFile = new File(
					ApplicationCore.userRessources.getConcoursPathForProfile(ApplicationCore.getConfiguration().getCurProfil()), 
					concoursDirectory + File.separator + ajrLibelle.getResourceString("state.pasdetir.title")  //$NON-NLS-1$
					+ " - " + DateFormat.getDateInstance().format(new Date()) + " " + DateFormat.getTimeInstance().format(new Date()) + ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			pdfFile.getParentFile().mkdirs();*/
			//String filePath = pdfFile.getCanonicalPath();
			
			/*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
			writer.setFullCompression();*/

			document.addCreationDate();
			document.addAuthor(ApplicationCore.getConfiguration().getClub().getNom());
			document.addProducer();
			document.addCreator(ApplicationCore.getConfiguration().getClub().getNom());
			document.addTitle(ajrLibelle.getResourceString("state.pasdetir.title")); //$NON-NLS-1$
			document.open();
			
			cb = writer.getDirectContent();
			Graphics2D g2 = cb.createGraphics(pageWidth, pageHeight);
			
			boolean multiserie = false;
			for(Target target : pasDeTir.getTargets()) {
				if(target.getNbArcher() > 0) {
					int[] distances = target.getDistancesEtBlason().get(0).getDistance();
					int d1 = distances[0];
					for(int i = 1; i < distances.length; i++) {
						if(distances[i] != d1) {
							multiserie = true;
							break;
						}
					}
					
					if(multiserie)
						break;
				}
			}

			// Ligne de Tir
			printLigneDeTir(g2, page++, multiserie ? 0 : -1, 1);
			
			for(int i = 0; (!multiserie && i < 1) || (multiserie && i < pasDeTir.getFicheConcours().getParametre().getReglement().getNbSerie()); i++) {
				for(Target target : pasDeTir.getTargets()) {
					if(target.getNumCible() > 1 && (target.getNumCible() - 1) % 10 == 0) {
						g2.dispose();
						
						document.newPage();
						g2 = cb.createGraphicsShapes(pageWidth, pageHeight);
						printLigneDeTir(g2, page++, multiserie ? i : -1, target.getNumCible());
					}
					
					if(target.getDistancesEtBlason().size() > 0) {
						paintTarget(g2, target, i);
					}
				}
			}
			
			g2.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void printLigneDeTir(Graphics2D g2, int page, int serie, int firsttarget) {
		String strSerie = ajrLibelle.getResourceString("state.pasdetir.allseries"); //$NON-NLS-1$
		if(serie != -1)
			strSerie = ajrLibelle.getResourceString("state.pasdetir.serie", serie); //$NON-NLS-1$
		String strPasDeTir = ajrLibelle.getResourceString("state.pasdetir.footer", (pasDeTir.getDepart() + 1), strSerie, page, firsttarget, firsttarget + 9); //$NON-NLS-1$
		double pasDeTirPos = 20;
		int sizeStrPasDeTir = g2.getFontMetrics().stringWidth(strPasDeTir);
		double colSize = (29.7 - (leftMargin + rightMargin)) / 10;
		
		g2.drawLine(
				AJToolKit.centimeterToDpi(rightMargin),
				AJToolKit.centimeterToDpi(pasDeTirPos), 
				AJToolKit.centimeterToDpi(29.2),
				AJToolKit.centimeterToDpi(pasDeTirPos));
		g2.drawString(strPasDeTir,
				pageWidth / 2f - sizeStrPasDeTir / 2f,
				AJToolKit.centimeterToDpi(pasDeTirPos + 0.5));
		for(int i = 0; i < 10; i++) {
			g2.drawString((firsttarget + i) + "", //$NON-NLS-1$
					AJToolKit.centimeterToDpi(leftMargin + colSize * i + 0.5),
					AJToolKit.centimeterToDpi(pasDeTirPos - 0.5));
			g2.drawLine(
					AJToolKit.centimeterToDpi(leftMargin + colSize / 2 + colSize * i), 
					AJToolKit.centimeterToDpi(pasDeTirPos - 0.1),
					AJToolKit.centimeterToDpi(leftMargin + colSize / 2 + colSize * i),
					AJToolKit.centimeterToDpi(pasDeTirPos + 0.1));
		}
	}
	
	private void paintTarget(Graphics2D g2, Target target, int serie)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		double colSize = (29.7 - (leftMargin + rightMargin)) / 10;
		double pasDeTirPos = 20;
		double targetSize = colSize - 0.27;
		double targetPos = (pasDeTirPos - topMargin - targetSize) * (1 - (double)target.getDistancesEtBlason().get(0).getDistance()[serie] / (double)maxDistance); //variation possible entre 0 et 19.5 - colSize
		
		String distance = target.getDistancesEtBlason().get(0).getDistance()[serie] + "m"; //$NON-NLS-1$
		
		double startCol = rightMargin + colSize * ((target.getNumCible() - 1) % 10);
		double distanceCible = topMargin + targetPos + targetSize;
		
		g2.drawLine(
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(distanceCible), 
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(pasDeTirPos));
		g2.drawLine(
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(distanceCible), 
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0 - 0.235),
				AJToolKit.centimeterToDpi(distanceCible + 0.25));
		g2.drawLine(
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(distanceCible), 
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0 + 0.235),
				AJToolKit.centimeterToDpi(distanceCible + 0.25));
		g2.drawLine(
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(pasDeTirPos), 
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0 - 0.235),
				AJToolKit.centimeterToDpi(pasDeTirPos - 0.25));
		g2.drawLine(
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0),
				AJToolKit.centimeterToDpi(pasDeTirPos), 
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0 + 0.235),
				AJToolKit.centimeterToDpi(pasDeTirPos - 0.25));
		g2.drawString(target.getNumCible() + "", //$NON-NLS-1$
				AJToolKit.centimeterToDpi(startCol + 0.5),
				AJToolKit.centimeterToDpi(distanceCible + 0.5));
		g2.drawString(distance,
				AJToolKit.centimeterToDpi(startCol + colSize / 2.0 + 0.065),
				AJToolKit.centimeterToDpi(distanceCible + (pasDeTirPos - distanceCible) / 2.0));
		
		//TEST cadre cible
		g2.drawRect(
				AJToolKit.centimeterToDpi(startCol + 0.135),
				AJToolKit.centimeterToDpi(topMargin + targetPos),
				AJToolKit.centimeterToDpi(targetSize),
				AJToolKit.centimeterToDpi(targetSize));
		
		for(Concurrent concurrent : target.getAllConcurrents()) {
			DistancesEtBlason db = DistancesEtBlason.getDistancesEtBlasonForConcurrent(
					pasDeTir.getFicheConcours().getParametre().getReglement(),
					concurrent);
			Ancrage ancrage = db.getTargetFace().getAncrage(concurrent.getPosition());
			double hr = db.getTargetFace().getHorizontalRatio();
			double vr = db.getTargetFace().getVerticalRatio();
			
			Image image = Image.getInstance(
					ApplicationCore.ajrParametreAppli.getResourceString("path.ressources")  //$NON-NLS-1$
					+ File.separator + db.getTargetFace().getTargetFaceImage());
			
			image.setAbsolutePosition(AJToolKit.centimeterToDpi(startCol + 0.135 + ancrage.getX() * 2.6),
					pageHeight - AJToolKit.centimeterToDpi(2.6 * vr) - AJToolKit.centimeterToDpi(topMargin + targetPos + ancrage.getY() * 2.6));
			image.scaleAbsolute(AJToolKit.centimeterToDpi(2.6 * hr), AJToolKit.centimeterToDpi(2.6 * vr));
			cb.addImage(image);
		}
	}
	
	private int getMaxDistance(int serie) {
		int max = 0;
		for(Target target : pasDeTir.getTargets()) {
			if(target.getDistancesEtBlason().size() > 0) {
				int targetDistance = target.getDistancesEtBlason().get(0).getDistance()[serie];
				if(targetDistance > max) {
					max = targetDistance;
				}
			}
		}
		
		return max;
	}
}
