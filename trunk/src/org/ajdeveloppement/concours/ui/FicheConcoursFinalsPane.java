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
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.Converters;
import org.ajdeveloppement.concours.Duel;
import org.ajdeveloppement.concours.PhasesFinales;
import org.ajdeveloppement.concours.ui.components.ConcurentMxCell;
import org.ajdeveloppement.concours.ui.components.DuelMxCell;
import org.ajdeveloppement.concours.ui.dialog.DuelDialog;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.CriteriaSet;
import org.concoursjeunes.FicheConcours;
import org.concoursjeunes.localisable.CriteriaSetLibelle;
import org.concoursjeunes.state.PageFooter;
import org.concoursjeunes.state.State;
import org.concoursjeunes.state.StateSelector;
import org.jdesktop.swingx.JXBusyLabel;
import org.w3c.dom.Document;

import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphComponent.mxGraphControl;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class FicheConcoursFinalsPane extends JPanel implements ActionListener, MouseListener {
	
	private mxGraph graph = new mxGraph();
	
	//paneau parent
	private FicheConcoursPane ficheConcoursPane = null;
	
	//modele
	private FicheConcours ficheConcours;
	private PhasesFinales phaseFinal;
	
	@Localizable(value="",tooltip="finals.help")
	private JLabel jlHelp = new JLabel();
	private JXBusyLabel jxbPrint = new JXBusyLabel();
	@Localizable("finals.printgraph")
	private JButton jbPrintGraph = new JButton();
	@StateSelector(name="400-classementfinals")
	@Localizable("finals.printclassement")
	private JButton jbPrintClassement = new JButton();

	public FicheConcoursFinalsPane(FicheConcoursPane ficheConcoursPane) {
		this.ficheConcoursPane = ficheConcoursPane;
		this.ficheConcours = ficheConcoursPane.getFicheConcours();
		
		this.phaseFinal = new PhasesFinales(ficheConcours);
		
		init();
		affectLabels();
		completePanel();
	}
	
	private void init() {
		graph.setCellsCloneable(false);
		graph.setCellsEditable(false);
		graph.setCellsDisconnectable(false);
		graph.setCellsMovable(false);
		graph.setCellsBendable(false);
		graph.setSwimlaneNesting(true);
		graph.setHtmlLabels(true);
		
		jlHelp.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.help", 24, 24)); //$NON-NLS-1$
		jbPrintGraph.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.print")); //$NON-NLS-1$
		jbPrintGraph.addActionListener(this);
		jbPrintClassement.setIcon(ApplicationCore.userRessources.getImageIcon("file.icon.print")); //$NON-NLS-1$
		jbPrintClassement.setName("jbPrintClassement"); //$NON-NLS-1$
		jbPrintClassement.addActionListener(this);

		// Loads the default stylesheet from an external file
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(getClass().getResource("default-style.xml").toString()); //$NON-NLS-1$
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		
		JPanel northPane = new JPanel();
		northPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		northPane.add(jlHelp);
		northPane.add(jbPrintGraph);
		northPane.add(jbPrintClassement);
		northPane.add(jxbPrint);
		
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		graphComponent.setWheelScrollingEnabled(true);
		graphComponent.setSwimlaneSelectionEnabled(true);
		graphComponent.setPageBreaksVisible(true);
		graphComponent.setDragEnabled(false);
		graphComponent.setBackground(Color.WHITE);
		graphComponent.getViewport().setOpaque(false);
		graphComponent.setOpaque(true);
		graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
		graphComponent.setAntiAlias(true);
		graphComponent.setFoldingEnabled(false);
		graphComponent.getGraphControl().addMouseListener(this);
		graphComponent.getVerticalScrollBar().setUnitIncrement(20);
		graphComponent.setDoubleBuffered(true);
		//Gestion du zoom
		graphComponent.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline
						|| e.isControlDown())
				{
					if (e.getWheelRotation() < 0)
					{
						if(graph.getView().getScale() < 5.0)
							graphComponent.zoomIn();
					}
					else
					{
						if(graph.getView().getScale() > 0.15)
							graphComponent.zoomOut();
					}
				}
			}
		});
		graphComponent.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyChar() == '1')
					graphComponent.zoomActual();
				else if(e.getKeyChar() == '+' && graph.getView().getScale() < 5.0)
					graphComponent.zoomIn();
				else if(e.getKeyChar() == '-' && graph.getView().getScale() > 0.15)
					graphComponent.zoomOut();
				else if(e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyChar() == '') {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							printGraph();
						}
					});
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		setLayout(new BorderLayout());
		add(northPane, BorderLayout.NORTH);
		add(graphComponent, BorderLayout.CENTER);
	}
	
	private void affectLabels() {
		Localizator.localize(this, ficheConcoursPane.getLocalisation());
	}
	
	private void completePanel() {
		//graph.removeCells();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		int nbChild = ((mxCell)parent).getChildCount();
		for(int i = 0; i < nbChild; i++) {
			graph.getModel().remove(((mxCell)parent).getChildAt(0));
		}
		try {
			int startHeightCriteriaSet = 20;
			
			List<CriteriaSet> lstCriteriaSet = ficheConcours.getConcurrentList().listCriteriaSet();
			
			CriteriaSet.sortCriteriaSet(lstCriteriaSet, ficheConcours.getParametre().getReglement().getListCriteria());
			
			for(CriteriaSet criteriaSet : lstCriteriaSet) {
				int nombrePhaseCategorie = phaseFinal.getNombrePhase(criteriaSet);
				
				int startHeight = 30;
				int elementHeight = 120;
				int spacingHeight = 20;
				int decalage = 0;
				int padding = elementHeight + spacingHeight;
				
				int duelHeight = elementHeight * 2 + spacingHeight;
				
				Object categoryGraphElement = graph.insertVertex(parent, null, 
						new CriteriaSetLibelle(criteriaSet, ficheConcoursPane.getLocalisation()), 
						5, 5, 1, 1, "swimlane;labelPosition=middle;align=left;verticalLabelPosition=middle;verticalAlign=middle"); //$NON-NLS-1$
				
				Map<Integer, List<Object>> objectsPhase = new HashMap<Integer, List<Object>>();

				for(int i = 0; i < nombrePhaseCategorie; i++) {
					if(i > 0) {
						decalage += (duelHeight / 2) - (elementHeight / 2);
						startHeight = 30 + decalage;
						padding = elementHeight + spacingHeight + decalage * 2;
						duelHeight = padding + elementHeight;
					}

					List<Duel> duels = phaseFinal.getDuelsPhase(criteriaSet, nombrePhaseCategorie-i-1);
					for(Duel duel : duels) {
						if(duel != null && duel.getConcurrent1() != null && duel.getConcurrent2() != null) {
							Object duelGraphElement = null;
							if(!objectsPhase.containsKey(i))
								objectsPhase.put(i, new ArrayList<Object>());
							
							if(duel.getPhase() != 0 || duel.getNumDuel() == 1) { //exclu la petite finale
								duelGraphElement = drawDuel(graph, 5 + 380 * i, startHeight + objectsPhase.get(i).size()*padding,
										duel, categoryGraphElement, nombrePhaseCategorie-i-1);
							} else { //affichage de la petite finale en fonction du nombre totale de phases
								if(nombrePhaseCategorie > 3) {
									duelGraphElement = drawDuel(graph, 5 + 380 * (i - 1), startHeight,
											duel, categoryGraphElement, nombrePhaseCategorie-i-1);

									graph.insertEdge(categoryGraphElement, null, "", objectsPhase.get(i-1).get(0), duelGraphElement); //$NON-NLS-1$
									graph.insertEdge(categoryGraphElement, null, "", objectsPhase.get(i-1).get(1), duelGraphElement); //$NON-NLS-1$
								} else {
									duelGraphElement = drawDuel(graph, 5 + 380 * i, startHeight + 160,
											duel, categoryGraphElement, nombrePhaseCategorie-i-1);
								}
							}
							objectsPhase.get(i).add(duelGraphElement);
						}
					}
					
					if(i > 0 && objectsPhase.get(i-1) != null && objectsPhase.get(i) != null) {
						for(int j = 0; j < objectsPhase.get(i-1).size(); j++) {
							int l2i = (int)Math.floor(j / 2.0);
							if(objectsPhase.get(i).size() > l2i) {
								graph.insertEdge(categoryGraphElement, null, "", objectsPhase.get(i-1).get(j), objectsPhase.get(i).get(l2i)); //$NON-NLS-1$
							}
						}
					}
				}
				
				// Affichage du podium
				if(objectsPhase.get(nombrePhaseCategorie-1) != null) {
					List<Duel> duels = phaseFinal.getDuelsPhase(criteriaSet, 0);
					String nomVainqueur = ""; //$NON-NLS-1$
					String nomPerdant = ""; //$NON-NLS-1$
					if(duels.get(0).getWinner() != null)
						nomVainqueur = "1. " + duels.get(0).getWinner().getFullName(); //$NON-NLS-1$
					graph.insertVertex(categoryGraphElement, null, nomVainqueur, 5 + 380 * (nombrePhaseCategorie + ((nombrePhaseCategorie > 3) ? -1 : 0)), 150, 250, 50);
					
					if(duels.get(0).getLooser() != null)
						nomPerdant = "2. " + duels.get(0).getLooser().getFullName(); //$NON-NLS-1$
					graph.insertVertex(categoryGraphElement, null, nomPerdant, 5 + 380 * (nombrePhaseCategorie + ((nombrePhaseCategorie > 3) ? -1 : 0)), 220, 250, 50); 
					
					if(duels.size() > 1) {
						nomVainqueur = ""; //$NON-NLS-1$
						nomPerdant = ""; //$NON-NLS-1$
						if(duels.get(1).getWinner() != null)
							nomVainqueur = "3. " + duels.get(1).getWinner().getFullName(); //$NON-NLS-1$
						graph.insertVertex(categoryGraphElement, null, nomVainqueur, 5 + 380 * (nombrePhaseCategorie + ((nombrePhaseCategorie > 3) ? -1 : 0)), 290, 250, 50);
						
						if(duels.get(1).getLooser() != null)
							nomPerdant = "4. " + duels.get(1).getLooser().getFullName(); //$NON-NLS-1$
						graph.insertVertex(categoryGraphElement, null, nomPerdant, 5 + 380 * (nombrePhaseCategorie + ((nombrePhaseCategorie > 3) ? -1 : 0)), 360, 250, 50);
					}
				}
				
				graph.resizeCell(categoryGraphElement, new mxRectangle(5, startHeightCriteriaSet - 15, 290 + 380 * 5, 30 + decalage * 2 + elementHeight + spacingHeight));

				startHeightCriteriaSet += 30 + decalage * 2 + elementHeight + spacingHeight * 2;
			}
		} finally {
			graph.getModel().endUpdate();
		}
		graph.setCellsResizable(false);
		graph.refresh();
	}
	
	private Object drawDuel(mxGraph graph, int x, int y, Duel duel, Object categoryGraphElement, int phase) {
		Object duelGraphElement = graph.addCell(
				new DuelMxCell(new mxGeometry(x, y, 260, 130),
				duel, ficheConcours.getProfile().getLocalisation(), ""), categoryGraphElement); //$NON-NLS-1$

		graph.addCell(
				new ConcurentMxCell(
						new mxGeometry(5, 5, 250, 50),
						duel.getConcurrent1(), phase,
						duel.getWinner() == duel.getConcurrent1() ? "fillColor=green" :  //$NON-NLS-1$
							duel.getWinner() == null ? "fillColor=#FFFFFF" :"fillColor=gray"),//$NON-NLS-1$ //$NON-NLS-2$
				duelGraphElement); 
		graph.addCell(
				new ConcurentMxCell(
						new mxGeometry(5, 75, 250, 50),
						duel.getConcurrent2(), phase,
						duel.getWinner() == duel.getConcurrent2() ? "fillColor=green" : //$NON-NLS-1$
								duel.getWinner() == null ? "fillColor=#FFFFFF" :"fillColor=gray"),//$NON-NLS-1$ //$NON-NLS-2$
				duelGraphElement);
		
		return duelGraphElement;
	}
	
	private int getNbPrintablePage(CriteriaSet criteriaSet) {
		int nombrePhaseCategorie = phaseFinal.getNombrePhase(criteriaSet);
		if(nombrePhaseCategorie == 5)
			return 3;
		else if(nombrePhaseCategorie == 6)
			return 5;
		return 1;
	}
	
	/**
	 * 
	 * @param criteriaSet
	 * @param page
	 * @return
	 */
	private BufferedImage drawPrintableGraph(CriteriaSet criteriaSet, int page) {
		mxGraph printableGraph = new mxGraph();
		printableGraph.setSwimlaneNesting(true);
		printableGraph.setHtmlLabels(true);
		
		// Loads the default stylesheet from an external file
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(getClass().getResource("default-style.xml").toString()); //$NON-NLS-1$
		codec.decode(doc.getDocumentElement(), printableGraph.getStylesheet());
		
		int nombrePhaseCategorie = phaseFinal.getNombrePhase(criteriaSet);
		
		int startHeight = 5; //Hauteur de départ
		int startWidth = 0;	//Largeur de départ
		int elementHeight = 120; //Hauteur d'un duel
		int elementWidth = 380;
		int decalage = 0;
		int spacingHeight = 20; //espace initial entre 2 duel
		int padding = elementHeight + spacingHeight; //interval (vertical) entre le début de deux duel
		
		int duelHeight = elementHeight * 2 + spacingHeight;
		
		Object parent = printableGraph.getDefaultParent();

		printableGraph.getModel().beginUpdate();
		try {
			
			int premierePhaseAffiche = nombrePhaseCategorie - 1;
			int dernierePhaseAffiche = 0;
			if(nombrePhaseCategorie == 5) {
				if(page < 2)
					dernierePhaseAffiche = 1;
				else
					premierePhaseAffiche = 1;
			} else if(nombrePhaseCategorie == 6) {
				if(page < 4)
					dernierePhaseAffiche = 2;
				else
					premierePhaseAffiche = 2;
			}
			
			Map<Integer, List<Object>> objectsPhase = new HashMap<Integer, List<Object>>();
			
			for(int i = (nombrePhaseCategorie - 1) - premierePhaseAffiche; i < nombrePhaseCategorie - dernierePhaseAffiche; i++) {
				if(i > (nombrePhaseCategorie - 1) - premierePhaseAffiche || premierePhaseAffiche < 3) {
					decalage += (duelHeight / 2) - (elementHeight / 2);
					startHeight = 5 + decalage;
					padding = elementHeight + spacingHeight + decalage * 2;
					duelHeight = padding + elementHeight;
					if(premierePhaseAffiche == 1 && i == (nombrePhaseCategorie - 1) - premierePhaseAffiche) { // si on démarre au demi-finale au faitdouble décalage
						decalage += (duelHeight / 2) - (elementHeight / 2);
						startHeight = 5 + decalage;
						padding = elementHeight + spacingHeight + decalage * 2;
						duelHeight = padding + elementHeight;
					}
				}
				
				List<Duel> duels = phaseFinal.getDuelsPhase(criteriaSet, nombrePhaseCategorie-i-1);
				int nbDuelAAfficher = duels.size();
				int premierDuel = 0;
				if(nombrePhaseCategorie == 5 && page < 2) {
					nbDuelAAfficher = nbDuelAAfficher / 2;
					premierDuel = nbDuelAAfficher * page;

				} else if(nombrePhaseCategorie == 6 && page < 4) {
					nbDuelAAfficher = nbDuelAAfficher / 4;
					premierDuel = nbDuelAAfficher * page;
				}
				int dernierDuel = premierDuel + nbDuelAAfficher -1;
				
				for(int j = premierDuel; j <= dernierDuel; j++) {
					Duel duel = duels.get(j);
					if(duel != null && duel.getConcurrent1() != null && duel.getConcurrent2() != null) {
						Object duelGraphElement = null;
						if(!objectsPhase.containsKey(i))
							objectsPhase.put(i, new ArrayList<Object>());
						
						if(duel.getPhase() != 0 || duel.getNumDuel() == 1) { //exclu la petite finale
							duelGraphElement = drawDuel(printableGraph, startWidth + elementWidth * i + ((duel.getPhase() == 0) ? 100 : 0), startHeight + objectsPhase.get(i).size()*padding,
									duel, parent, nombrePhaseCategorie-i-1);
						} else { //affichage de la petite finale en fonction du nombre totale de phases
							if(nombrePhaseCategorie > 2) {
								duelGraphElement = drawDuel(printableGraph, startWidth + elementWidth * (i - 1) + 100, startHeight,
										duel, parent, nombrePhaseCategorie-i-1);

								printableGraph.insertEdge(parent, null, "", objectsPhase.get(i-1).get(0), duelGraphElement); //$NON-NLS-1$
								printableGraph.insertEdge(parent, null, "", objectsPhase.get(i-1).get(1), duelGraphElement); //$NON-NLS-1$
							} else {
								duelGraphElement = drawDuel(printableGraph, startWidth + elementWidth * i + 100, startHeight + 160,
										duel, parent, nombrePhaseCategorie-i-1);
							}
						}
						
						objectsPhase.get(i).add(duelGraphElement);
					}
				}
				
				if(i > 0 && objectsPhase.get(i-1) != null && objectsPhase.get(i) != null) {
					for(int j = 0; j < objectsPhase.get(i-1).size(); j++) {
						int l2i = (int)Math.floor(j / 2.0);
						if(objectsPhase.get(i).size() > l2i) {
							printableGraph.insertEdge(parent, null, "", objectsPhase.get(i-1).get(j), objectsPhase.get(i).get(l2i)); //$NON-NLS-1$
						}
					}
				}
			}
		} finally {
			printableGraph.getModel().endUpdate();
		}

		BufferedImage image = mxCellRenderer.createBufferedImage(printableGraph, null, 2, Color.white, true, null);
		
		return image;
	}
	
	private void printGraph() {
		try {
			jxbPrint.setBusy(true);
			File tempPdf= File.createTempFile("acpf", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
			com.lowagie.text.Document document = new com.lowagie.text.Document();
			document.setPageSize(PageSize.A4.rotate());
			document.addCreationDate();
			
			PageFormat pageFormat = new PageFormat();
			Paper paper = new Paper();
			float width = document.getPageSize().getWidth();
			float height = document.getPageSize().getHeight();
			double margin = Converters.centimeterToDpi(0.5);
			paper.setSize(width, height);
			paper.setImageableArea(margin, margin, width-(margin*2), height-(margin*2));
			pageFormat.setPaper(paper);
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tempPdf));
			writer.setFullCompression();
			writer.setPageEvent(new PageFooter());
			
			document.open();
			
			for(CriteriaSet criteriaSet : phaseFinal.getCriteriaSetPhasesFinal()) {
				int nbPage = getNbPrintablePage(criteriaSet);
				for(int i = 0; i < nbPage; i++) {
					PdfContentByte cb = writer.getDirectContent();
					
					BufferedImage image = drawPrintableGraph(criteriaSet, i);

					if(image == null)
						continue;
					float srcWidth = image.getWidth();
					float srcHeight = image.getHeight();
					
					float destHeight = (float)(height-(margin*2));
					float destWidth = srcWidth * destHeight / srcHeight;
					if(destWidth > width-(margin*2)) {
						destWidth = (float)(width-(margin*2));
						destHeight = srcHeight * destWidth / srcWidth;
					}
					
					float x = (float)margin;
					float y = (float)margin;
					
					if(destHeight < (float)(height-(margin*2))) {
						y = ((height - destHeight) / 2);
					}
					
					cb.addImage(com.lowagie.text.Image.getInstance(cb, image, 1), destWidth, 0, 0, destHeight, x, y);
					
					Graphics2D g2d = cb.createGraphics((float)paper.getWidth(), (float)paper.getHeight());
					String poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.finals"); //$NON-NLS-1$
					if(nbPage == 3) {
						switch (i) {
							case 0:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init16a"); //$NON-NLS-1$
								break;
							case 1:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init16b"); //$NON-NLS-1$
								break;
						}
					} else if(nbPage == 5) {
						switch (i) {
							case 0:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init32a"); //$NON-NLS-1$
								break;
							case 1:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init32b"); //$NON-NLS-1$
								break;
							case 2:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init32c"); //$NON-NLS-1$
								break;
							case 3:
								poules = ficheConcours.getProfile().getLocalisation().getResourceString("finals.init32d"); //$NON-NLS-1$
								break;
						}
					}
						
					g2d.drawString(
							CriteriaSetLibelle.getLibelle(criteriaSet, ficheConcours.getProfile().getLocalisation()),
							550, 50);
					g2d.drawString(
							poules,
							550, 65);
					
					g2d.dispose();

					document.newPage();
					writer.newPage();
				}
			}
		    
			document.close();
			
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(tempPdf);
			} else {
				String NAV = ApplicationCore.getAppConfiguration().getPdfReaderPath();

				Runtime.getRuntime().exec(NAV + " " + tempPdf.getAbsolutePath() + ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (FileNotFoundException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		} catch (DocumentException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			DisplayableErrorHelper.displayException(e1);
			e1.printStackTrace();
		} finally {
			jxbPrint.setBusy(false);
		}
	}
	
	public void updateGraph() {
		completePanel();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbPrintGraph) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					printGraph();
				}
			});
			t.start();
		} else if(e.getSource() == jbPrintClassement) {
			ficheConcoursPane.switchToEditPane();
			//ficheConcoursPane.
			if(ficheConcoursPane.getStateManager() != null) {
				try {
					String stateName = this.getClass().getDeclaredField(((JButton)e.getSource()).getName()).getAnnotation(StateSelector.class).name();
					State state = ficheConcoursPane.getStateManager().getState(stateName);
					if(state != null)
						ficheConcoursPane.prepareState(state);
				} catch (SecurityException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					DisplayableErrorHelper.displayException(e1);
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			if(e.getSource() instanceof mxGraphControl) {
				mxGraphComponent graphComponent = ((mxGraphControl)e.getSource()).getGraphContainer();
				final Object cell = graphComponent.getCellAt(e.getX(), e.getY());
				if(cell instanceof ConcurentMxCell) {
					JPopupMenu popup = new JPopupMenu("Edit"); //$NON-NLS-1$
					
					JMenuItem mi = new JMenuItem(ficheConcours.getProfile().getLocalisation().getResourceString("finals.editresults")); //$NON-NLS-1$
					mi.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DuelDialog duelDialog = new DuelDialog(ficheConcoursPane, ficheConcours.getProfile().getLocalisation());
							duelDialog.showDuelDialog(((DuelMxCell)((ConcurentMxCell)cell).getParent()).getDuel());
							
							completePanel();
						}
					});
					popup.add(mi);
					mi = new JMenuItem(ficheConcours.getProfile().getLocalisation().getResourceString("finals.seeconcurrent")); //$NON-NLS-1$
					mi.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ficheConcoursPane.openConcurrentDialog(((ConcurentMxCell)cell).getConcurrent(), null);
							
							completePanel();
						}
					});
					popup.add(mi);
					popup.show(graphComponent, e.getX(), e.getY());

				} else if(cell instanceof DuelMxCell) {
					JPopupMenu popup = new JPopupMenu("Edit"); //$NON-NLS-1$
					
					JMenuItem mi = new JMenuItem(ficheConcours.getProfile().getLocalisation().getResourceString("finals.editresults")); //$NON-NLS-1$
					mi.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DuelDialog duelDialog = new DuelDialog(ficheConcoursPane, ficheConcours.getProfile().getLocalisation());
							duelDialog.showDuelDialog(((DuelMxCell)cell).getDuel());
							
							completePanel();
						}
					});
					popup.add(mi);
					popup.show(graphComponent, e.getX(), e.getY());
				}
			}
		} else if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			mxGraphComponent graphComponent = ((mxGraphControl)e.getSource()).getGraphContainer();
			Object cell = graphComponent.getCellAt(e.getX(), e.getY());
			if(cell instanceof ConcurentMxCell) {
				DuelDialog duelDialog = new DuelDialog(ficheConcoursPane, ficheConcours.getProfile().getLocalisation());
				duelDialog.showDuelDialog(((DuelMxCell)((ConcurentMxCell)cell).getParent()).getDuel());
				
				completePanel();
			} else if(cell instanceof DuelMxCell) {
				DuelDialog duelDialog = new DuelDialog(ficheConcoursPane, ficheConcours.getProfile().getLocalisation());
				duelDialog.showDuelDialog(((DuelMxCell)cell).getDuel());
				
				completePanel();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
