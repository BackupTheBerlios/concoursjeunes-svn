/*
 * Créé le 10 mai 08 à 13:24:05 pour ConcoursJeunes
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
package org.concoursjeunes.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.URISyntaxException;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.html.HTMLEditorKit;

import org.ajdeveloppement.apps.localisation.Localisable;
import org.ajdeveloppement.apps.localisation.Localisator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.concoursjeunes.AppInfos;
import org.concoursjeunes.ApplicationCore;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Boite de dialogue "à propos" de l'application
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@Localisable(textMethod="setTitle",value="apropos.titre")
public class AboutDialog extends JDialog implements ActionListener, HyperlinkListener {
	private AjResourcesReader localisation;
	
	@Localisable("bouton.fermer")
	private JButton jbFermer = new JButton();
	private JEditorPane jlAbout = new JEditorPane();

	MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
	/**
	 * 
	 */
	public AboutDialog(JFrame parentframe, AjResourcesReader localisation) {
		super(parentframe, true);
		this.localisation = localisation;
		init();
		affectLibelle();
	}
	
	private void init() {
		JPanel jpAction = new JPanel();
		
		jbFermer.addActionListener(this);
		
		jlAbout.setEditable(false);
		jlAbout.setOpaque(false);
		jlAbout.setEditorKit(new HTMLEditorKit());
		jlAbout.setFont(getFont());
		jlAbout.addHyperlinkListener(this);
		
		jpAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpAction.add(jbFermer);
		
		setLayout(new BorderLayout());
		add(jlAbout, BorderLayout.CENTER);
		add(jpAction, BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 */
	private void affectLibelle() {
		Localisator.localize(this, localisation);
		
		String iconURL = ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.about"); //$NON-NLS-1$
		
		jlAbout.setText("<html><table style=\"font-family: " + getFont().getName() + "; font-size:" + getFont().getSize() //$NON-NLS-1$ //$NON-NLS-2$
				+ "pt; font-weight:normal;\">" + //$NON-NLS-1$
				"<tr><td><img src=\"file:"	+ iconURL + "\"></td><td><b>"  //$NON-NLS-1$ //$NON-NLS-2$
				+ AppInfos.NOM + "<br>" + //$NON-NLS-1$ 
				localisation.getResourceString("apropos.description") + "<br><br>" + //$NON-NLS-1$ //$NON-NLS-2$
				localisation.getResourceString("apropos.version") + "<br>" +  //$NON-NLS-1$ //$NON-NLS-2$
				AppInfos.VERSION + "<br>" + //$NON-NLS-1$
				localisation.getResourceString("apropos.codename") + " " + AppInfos.CODENAME + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				AppInfos.COPYR + " " + AppInfos.AUTEURS + "<br>" + //$NON-NLS-1$ //$NON-NLS-2$
				"version base: " + ApplicationCore.dbVersion + "<br><br>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "mémoire utilisé: " + ((memoryBean.getHeapMemoryUsage().getUsed() + memoryBean.getNonHeapMemoryUsage().getUsed()) / 1024 / 1024) + "Mo<br>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "mémoire réservé: " + ((memoryBean.getHeapMemoryUsage().getCommitted() + memoryBean.getNonHeapMemoryUsage().getCommitted()) / 1024 / 1024) + "Mo<br><br>" //$NON-NLS-1$ //$NON-NLS-2$
				+ localisation.getResourceString("apropos.liens") + "</b></td></tr></table></html>"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void showAboutDialog() {
 		setSize(new Dimension(427, 376));
		//pack();
		setResizable(false);
		setLocationRelativeTo(null);
		//System.out.println(getSize());
		setVisible(true);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbFermer) {
			setVisible(false);
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == EventType.ACTIVATED) {
			if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(e.getURL().toURI());
				} catch (IOException e1) {
					JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					JXErrorPane.showDialog(this, new ErrorInfo(localisation.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
							null, null, e1, Level.SEVERE, null));
					e1.printStackTrace();
				}
			}
		}
	}
}
