/*
 * Créé le 24 oct. 2009 à 11:53:38 pour ConcoursJeunes
 *
 * Copyright 2002-2009 - Aurélien JEOFFRAY
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
package org.ajdeveloppement.concours.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.ajdeveloppement.apps.localisation.Localizator;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.Duel;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.PhaseFinal;
import org.concoursjeunes.localisable.CriteriaSetLibelle;
import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class FicheConcoursFinalPane extends JPanel implements ActionListener {
	
	private mxGraph graph = new mxGraph();
	
	//paneau parent
	private FicheConcoursPane ficheConcoursPane = null;
	
	//modele
	private FicheConcours ficheConcours;

	public FicheConcoursFinalPane(FicheConcoursPane ficheConcoursPane) {
		this.ficheConcoursPane = ficheConcoursPane;
		this.ficheConcours = ficheConcoursPane.getFicheConcours();
		
		init();
		affectLibelle();
		completePanel();
	}
	
	private void init() {
		graph.setCellsCloneable(false);
		graph.setCellsEditable(false);
		graph.setCellsDisconnectable(false);
		graph.setCellsMovable(false);
		graph.setCellsBendable(false);
		
		// Loads the defalt stylesheet from an external file
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(getClass().getResource("default-style.xml").toString()); //$NON-NLS-1$
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		graphComponent.setWheelScrollingEnabled(true);
		graphComponent.setSwimlaneSelectionEnabled(false);
		graphComponent.setPageBreaksVisible(true);
		graphComponent.setDragEnabled(false);
		graphComponent.setBackground(Color.WHITE);
		graphComponent.setOpaque(false);
		graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
		graphComponent.setFoldingEnabled(false);
		
		setLayout(new BorderLayout());
		add(graphComponent, BorderLayout.CENTER);

	}
	
	private void affectLibelle() {
		Localizator.localize(this, ficheConcoursPane.getLocalisation());
	}
	
	private void completePanel() {
		Object parent = graph.getDefaultParent();
		
		PhaseFinal phaseFinal = new PhaseFinal(ficheConcours);

		graph.getModel().beginUpdate();
		try
		{
			int startHeightCriteriaSet = 20;
			for(CriteriaSet criteriaSet : ficheConcours.getConcurrentList().listCriteriaSet()) {
				int nombrePhaseCategorie = phaseFinal.getNombrePhase(criteriaSet);
				
				int startHeight = 5;
				int elementHeight = 120;
				int spacingHeight = 20;
				int decalage = 0;
				int padding = elementHeight + spacingHeight;
				
				int duelHeight = elementHeight * 2 + spacingHeight;
				
				Object categoryGraphElement = graph.insertVertex(parent, null, 
						new CriteriaSetLibelle(criteriaSet, ficheConcoursPane.getLocalisation()), 
						0, startHeightCriteriaSet - 20, 1, 1, "swimlane;horizontal=0"); //$NON-NLS-1$
				
				Map<Integer, List<Object>> objectsPhase = new HashMap<Integer,  List<Object>>();
				
				for(int i = 0; i < nombrePhaseCategorie; i++) {
					if(i > 0) {
						decalage += (duelHeight / 2) - (elementHeight / 2);
						startHeight = 5 + decalage;
						padding = elementHeight + spacingHeight + decalage * 2;
						duelHeight = padding + elementHeight;
					}
					
					
					List<Duel> duels = phaseFinal.getDuelsPhase(criteriaSet, nombrePhaseCategorie-i);
					for(Duel duel : duels) {
						if(duel != null && duel.getConcurrent1() != null && duel.getConcurrent2() != null) {
							if(!objectsPhase.containsKey(i))
								objectsPhase.put(i, new ArrayList<Object>());
							
							Object duelGraphElement = graph.insertVertex(categoryGraphElement, null, "Contre", 30 + 380 * i, startHeight + objectsPhase.get(i).size()*padding, 260, 130); //$NON-NLS-1$ //$NON-NLS-2$
							
							objectsPhase.get(i).add(duelGraphElement);
							
							graph.insertVertex(duelGraphElement, null, duel.getConcurrent1().getFullName(), 5, 5, 250, 50,
									duel.getWinner() == duel.getConcurrent1() ? "fillColor=green" : "fillColor=gray"); //$NON-NLS-1$ //$NON-NLS-2$
							graph.insertVertex(duelGraphElement, null, duel.getConcurrent2().getFullName(), 5, 75, 250, 50, 
									duel.getWinner() == duel.getConcurrent2() ? "fillColor=green" : "fillColor=gray"); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					
					if(i > 0) {
						for(int j = 0; j < objectsPhase.get(i-1).size(); j++) {
							int l2i = (int)Math.floor(j / 2.0);
							graph.insertEdge(parent, null, "", objectsPhase.get(i-1).get(j), objectsPhase.get(i).get(l2i)); //$NON-NLS-1$
						}
					}
				}
				
				Object vainqueur = graph.insertVertex(categoryGraphElement, null, "", 20 + 380 * nombrePhaseCategorie, startHeight, 250, 50); //$NON-NLS-1$
				
				if(objectsPhase.size() > 0) {
					for(int j = 0; j < objectsPhase.get(nombrePhaseCategorie-1).size(); j++) {
						graph.insertEdge(parent, null, "", objectsPhase.get(nombrePhaseCategorie-1).get(j), vainqueur); //$NON-NLS-1$
					}
				}
				
				graph.resizeCell(categoryGraphElement, new mxRectangle(0.0, startHeightCriteriaSet - 20, 290 + 380 * nombrePhaseCategorie, decalage * 2 + elementHeight + spacingHeight));
				
				startHeightCriteriaSet += decalage * 2 + elementHeight + spacingHeight * 2;
			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		graph.setCellsResizable(false);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Raccord de méthode auto-généré

	}
}
