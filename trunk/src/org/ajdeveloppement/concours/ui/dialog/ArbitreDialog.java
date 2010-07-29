/*
 * Créé le 8 mai 2009 à 17:07:56 pour ConcoursJeunes
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
package org.ajdeveloppement.concours.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import org.ajdeveloppement.apps.localisation.Localizable;
import org.ajdeveloppement.apps.localisation.Localizator;
import org.ajdeveloppement.commons.AjResourcesReader;
import org.ajdeveloppement.commons.ui.GridbagComposer;
import org.ajdeveloppement.swingxext.error.ui.DisplayableErrorHelper;
import org.concoursjeunes.ApplicationCore;
import org.concoursjeunes.Archer;
import org.concoursjeunes.AutoCompleteDocument;
import org.concoursjeunes.AutoCompleteDocumentContext;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Judge;
import org.concoursjeunes.Profile;
import org.concoursjeunes.event.AutoCompleteDocumentEvent;
import org.concoursjeunes.event.AutoCompleteDocumentListener;

/**
 * Boite de dialogue de sélection et manipulation d'un arbitre
 * 
 * @author Aurélien JEOFFRAY
 *
 */
@Localizable(textMethod="setTitle", value="arbitredialog.title")
public class ArbitreDialog extends JDialog implements AutoCompleteDocumentListener, ActionListener, FocusListener {
	public static final int CONFIRM = 0;
	public static final int CANCEL = 1;
	
	private static Future<ConcurrentListDialog> concurrentListDialog;
	
	private AjResourcesReader localisation;
	
	private Archer filter;
	private Judge judge;
	private boolean mustberesponsable = true;
	private boolean enableautocomplement = true;
	
	@Localizable("arbitredialog.description")
	private JLabel jlDescription = new JLabel();
	@Localizable("arbitredialog.identite")
	private JLabel jlNom = new JLabel();
	private JTextField jtfNom = new JTextField(8);
	private JTextField jtfPrenom = new JTextField(8);
	@Localizable(value="",tooltip="arbitredialog.select")
	private JButton jbSelectArbitre = new JButton();
	@Localizable("arbitredialog.numlicence")
	private JLabel jlLicence = new JLabel();
	private JTextField jtfLicence = new JTextField(16);
	@Localizable("arbitredialog.responsable")
	private JCheckBox jcbResponsable = new JCheckBox();
	
	@Localizable("bouton.valider")
	private JButton jbValider = new JButton();
	@Localizable("bouton.annuler")
	private JButton jbAnnuler = new JButton();
	
	private int returnVal = CONFIRM;
	
	public ArbitreDialog(JFrame parentframe, Profile profile) {
		super(parentframe, true);
		
		this.localisation = profile.getLocalisation();
		
		ExecutorService executorService = Executors.newSingleThreadExecutor(new LowFactory());
		final Profile threadProfile = profile;
		concurrentListDialog = executorService.submit(new Callable<ConcurrentListDialog>() {
			@Override
			public ConcurrentListDialog call() {
				return new ConcurrentListDialog(ArbitreDialog.this, threadProfile,
						null, null);
			}
		});
		
		init();
		affectLabels();
		
		setSize(400, 250);
		setLocationRelativeTo(null);
	}
	
	private void init() {
		JPanel jpGeneral = new JPanel();
		JPanel jpValidation = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		GridbagComposer gridbagComposer = new GridbagComposer();

		jlDescription.setOpaque(true);
		jlDescription.setPreferredSize(new Dimension(250, 70));
		jlDescription.setBackground(new Color(255, 255, 225));
		jbValider.addActionListener(this);
		jbAnnuler.addActionListener(this);
		jbSelectArbitre.addActionListener(this);
		jbSelectArbitre.setIcon(new ImageIcon(ApplicationCore.staticParameters.getResourceString("path.ressources") + //$NON-NLS-1$
				File.separator + ApplicationCore.staticParameters.getResourceString("file.icon.select"))); //$NON-NLS-1$
		jbSelectArbitre.setMargin(new Insets(0,0,0,0));
		
		jtfNom.addFocusListener(this);
		jtfPrenom.addFocusListener(this);
		jtfLicence.addFocusListener(this);
		
		gridbagComposer.setParentPanel(jpGeneral);
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbagComposer.addComponentIntoGrid(jlNom, c);
		gridbagComposer.addComponentIntoGrid(jtfNom, c);
		gridbagComposer.addComponentIntoGrid(jtfPrenom, c);
		gridbagComposer.addComponentIntoGrid(jbSelectArbitre, c);
		
		c.gridy++;
		gridbagComposer.addComponentIntoGrid(jlLicence, c);
		c.gridwidth = 2;
		gridbagComposer.addComponentIntoGrid(jtfLicence, c);
		
		c.gridy++;
		c.gridwidth = 3;
		gridbagComposer.addComponentIntoGrid(jcbResponsable, c);
		
		jpValidation.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpValidation.add(jbValider);
		jpValidation.add(jbAnnuler);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jlDescription, BorderLayout.NORTH);
		getContentPane().add(jpGeneral, BorderLayout.CENTER);
		getContentPane().add(jpValidation, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(jbValider);
	}
	
	private void affectLabels() {
		Localizator.localize(this, localisation);
	}
	
	private void completePanel() {
		if(judge != null) {
			
			
			if(enableautocomplement) {
				((AutoCompleteDocument) jtfNom.getDocument()).setText(judge.getName());
				((AutoCompleteDocument) jtfPrenom.getDocument()).setText(judge.getFirstName());
				((AutoCompleteDocument) jtfLicence.getDocument()).setText(judge.getNumLicenceArcher());
				
				jbSelectArbitre.setEnabled(true);
			} else {
				jtfNom.setText(judge.getName());
				jtfPrenom.setText(judge.getFirstName());
				jtfLicence.setText(judge.getNumLicenceArcher());
				
				jbSelectArbitre.setEnabled(false);
			}
			
			if(!mustberesponsable) {
				jcbResponsable.setEnabled(false);
				jcbResponsable.setSelected(false);
			} else
				jcbResponsable.setSelected(judge.isResponsable());
		}
	}
	
	/**
	 * Affiche la boite de dialogue de l'arbitre
	 * 
	 * @param arbitre l'arbitre à afficher ou null si sélectionner un arbitre
	 * @param mustberesponsable indique si l'arbitre peut être ou non responsable
	 * @return CONFIRM ou CANCEL en fonction de l'action utilisateur
	 */
	public int showJudgeDialog(Judge arbitre, boolean mustberesponsable) {
		if(arbitre == null) {
			arbitre = new Judge();
			
			AutoCompleteDocumentContext context = new AutoCompleteDocumentContext(null);
			
			AutoCompleteDocument acdNom = new AutoCompleteDocument(jtfNom, AutoCompleteDocument.SearchType.NAME_SEARCH, context);
			acdNom.addAutoCompleteDocumentListener(this);
			AutoCompleteDocument acdPrenom = new AutoCompleteDocument(jtfPrenom, AutoCompleteDocument.SearchType.FIRSTNAME_SEARCH, context);
			acdPrenom.addAutoCompleteDocumentListener(this);
			AutoCompleteDocument acdLicence = new AutoCompleteDocument(jtfLicence, AutoCompleteDocument.SearchType.NUMLICENCE_SEARCH, context);
			acdLicence.addAutoCompleteDocumentListener(this);
			
			jtfNom.setDocument(acdNom);
			jtfPrenom.setDocument(acdPrenom);
			jtfLicence.setDocument(acdLicence);
		} else {
			enableautocomplement = false;
			
			jtfNom.setDocument(new PlainDocument());
			jtfPrenom.setDocument(new PlainDocument());
			jtfLicence.setDocument(new PlainDocument());
		}
		this.judge = arbitre;
		this.mustberesponsable = mustberesponsable;
		
		completePanel();
		
		setVisible(true);
		
		return returnVal;
	}
	
	/**
	 * Définit l'arbitre associé à la boite de dialogue
	 * 
	 * @param arbitre l'arbitre associé à la boite de dialogue
	 */
	public void setJudge(Judge arbitre) {
		this.judge = arbitre;
		
		completePanel();
	}
	
	/**
	 * Retourne l'arbitre associé à la boite de dialogue
	 * @return l'arbitre associé à la boite de dialogue
	 */
	public Judge getJudge() {
		return judge;
	}

	@Override
	public void concurrentFinded(AutoCompleteDocumentEvent e) {
		Archer findArbitre = e.getConcurrent();
		
		if (!findArbitre.equals(judge)) {
			setJudge(new Judge(findArbitre));
		}
		
		if (judge.haveHomonyme()) {
			jlDescription.setText(localisation.getResourceString("arbitredialog.homonyme")); //$NON-NLS-1$
			jlDescription.setBackground(Color.ORANGE);
		} else {
			jlDescription.setText(localisation.getResourceString("arbitredialog.description")); //$NON-NLS-1$
			jlDescription.setBackground(new Color(255, 255, 225));
		}
		
		filter = e.getGenericArcher();
	}

	@Override
	public void concurrentNotFound(AutoCompleteDocumentEvent e) {
		Judge newArbitre = new Judge();
		if (e.getSource() == jtfNom) {
			newArbitre.setName(jtfNom.getText());
		} else if (e.getSource() == jtfPrenom) {
			newArbitre.setName(jtfNom.getText());
			newArbitre.setFirstName(jtfPrenom.getText());
		} else if (e.getSource() == jtfLicence) {
			newArbitre.setName(jtfNom.getText());
			newArbitre.setFirstName(jtfPrenom.getText());
			newArbitre.setNumLicenceArcher(jtfLicence.getText());
		}

		filter = null;

		setJudge(newArbitre);

		jlDescription.setText(localisation.getResourceString("arbitredialog.noarbitre")); //$NON-NLS-1$
		jlDescription.setBackground(Color.ORANGE);
	}

	@Override
	public void entiteFinded(AutoCompleteDocumentEvent e) {
	}

	@Override
	public void entiteNotFound(AutoCompleteDocumentEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbSelectArbitre) {
			try {
				Concurrent tmparbitre;
	        	ConcurrentListDialog cld = concurrentListDialog.get(30, TimeUnit.SECONDS);
	            
	            cld.setFilter(filter);
				cld.setVisible(true);
				if (cld.isValider()) {
					tmparbitre = cld.getSelectedConcurrent();
					setJudge(new Judge(tmparbitre));
				}
			} catch (InterruptedException e1) {
				DisplayableErrorHelper.displayException(e1);
	            e1.printStackTrace();
            } catch (ExecutionException e1) {
            	DisplayableErrorHelper.displayException(e1);
	            e1.printStackTrace();
            } catch (TimeoutException e1) {
            	JOptionPane.showMessageDialog(this, localisation.getResourceString("concurrent.info.listing.wait")); //$NON-NLS-1$
            }
		} else if(e.getSource() == jbValider) {
			judge.setName(jtfNom.getText());
			judge.setFirstName(jtfPrenom.getText());
			judge.setNumLicenceArcher(jtfLicence.getText());
			judge.setResponsable(jcbResponsable.isSelected());
			
			returnVal = CONFIRM;
			
			setVisible(false);
		} else if(e.getSource() == jbAnnuler) {
			returnVal = CANCEL;
			
			setVisible(false);
		}
	}
	
	/**
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent fe) {
		if (fe.getSource() instanceof JTextField) {
			((JTextField) fe.getSource()).setSelectionStart(0);
			((JTextField) fe.getSource()).setSelectionEnd(((JTextField) fe.getSource()).getText().length());
		}
	}

	/**
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent fe) {
	}
	
	@Override
	public void dispose() {
		try {
            if(concurrentListDialog.isDone())
            	concurrentListDialog.get().dispose();
            else
            	concurrentListDialog.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
		concurrentListDialog = null;
		super.dispose();
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	private abstract static class Factory implements ThreadFactory {
		protected final ThreadGroup group;

		Factory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, getThreadName(), 0);

			if (t.isDaemon()) {
				t.setDaemon(false);
			}

			return t;
		}

		protected abstract String getThreadName();
	}
	
	private static class LowFactory extends Factory {
		private final AtomicInteger lowThreadNumber = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = super.newThread(r);

			if (t.getPriority() != Thread.MIN_PRIORITY) {
				t.setPriority(Thread.MIN_PRIORITY);
			}

			return t;
		}

		@Override
		protected String getThreadName() {
			return "low-thread-" + lowThreadNumber.getAndIncrement(); //$NON-NLS-1$
		}
	}
}
