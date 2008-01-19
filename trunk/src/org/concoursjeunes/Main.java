/*
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
package org.concoursjeunes;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;

import org.concoursjeunes.plugins.PluginEntry;
import org.concoursjeunes.plugins.PluginLoader;
import org.concoursjeunes.plugins.PluginMetadata;
import org.concoursjeunes.ui.ConcoursJeunesFrame;
import org.jdesktop.swingx.JXErrorDialog;

import ajinteractive.standard.common.AJToolKit;
import ajinteractive.standard.common.PluginClassLoader;

/**
 * Class initial de l'application.
 * 
 * @author Aurelien JEOFFRAY
 * @version 2.0
 * 
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread.UncaughtExceptionHandler handlerException = new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, final Throwable e) {
				EventQueue.invokeLater(new Runnable() {
			         public void run() {
						JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), //$NON-NLS-1$
								e.toString(),
								e);
						e.printStackTrace();
			         }
				});
			}
		};
		
		Thread.setDefaultUncaughtExceptionHandler(handlerException);
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ExceptionHandlingEventQueue());

		ConcoursJeunes concoursJeunes = ConcoursJeunes.getInstance();
		if(System.getProperty("noplugin") == null) { //$NON-NLS-1$
			loadStartupPlugin();
		}
		
		//System.setProperty("java.net.useSystemProxies","true");
		//ProxySelector.getDefault().

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					Thread.setDefaultUncaughtExceptionHandler(null);

					// rend l'ensemble des fichier de la base accessible en lecture/ecriture pour permettre
					// le multiutilisateur
					File[] dbfiles = new File(ConcoursJeunes.userRessources.getAllusersDataPath() + File.separator + "base").listFiles(); //$NON-NLS-1$
					for (File dbfile : dbfiles) {
						if (dbfile.isFile()) {
							dbfile.setWritable(true, false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("core loaded"); //$NON-NLS-1$

		new ConcoursJeunesFrame(concoursJeunes);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadStartupPlugin() {
		PluginLoader pl = new PluginLoader();
		
		List<String> disablePlugin = null;
		try {
			disablePlugin = (List<String>)AJToolKit.loadXMLStructure(
					new File(ConcoursJeunes.userRessources.getConfigPathForUser(), "disable_plugins.xml"), false); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (PluginMetadata pm : pl.getPlugins(PluginMetadata.STARTUP_PLUGIN)) {
			if(disablePlugin != null && disablePlugin.contains(pm.getName()))
				continue;
			try {
				Class<?> cla = null;
				String importClass = pm.getClassName();
				if (importClass != null) {
					cla = Class.forName(importClass, false, new PluginClassLoader(findParentClassLoader(), new File("plugins"))); //$NON-NLS-1$
				}

				if (cla != null) {
					Object plugin = cla.newInstance();
					for (Method m : cla.getMethods()) {
						if (m.isAnnotationPresent(PluginEntry.class)) {
							m.invoke(plugin, (Object[]) null);
							break;
						}
					}
				}
			} catch (InstantiationException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			} catch (SecurityException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				JXErrorDialog.showDialog(null, ConcoursJeunes.ajrLibelle.getResourceString("erreur"), e1.toString(), //$NON-NLS-1$
						e1);
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Locates the best class loader based on context (see class description).
	 * 
	 * @return The best parent classloader to use
	 */
	private static ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = Main.class.getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return parent;
	}

}
