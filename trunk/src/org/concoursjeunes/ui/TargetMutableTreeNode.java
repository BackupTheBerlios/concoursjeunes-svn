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
package org.concoursjeunes.ui;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.concoursjeunes.Concurrent;
import org.concoursjeunes.Target;
import org.concoursjeunes.TargetPosition;
import org.concoursjeunes.event.TargetEvent;
import org.concoursjeunes.event.TargetListener;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class TargetMutableTreeNode implements MutableTreeNode, TargetListener {
	
	private Target cible;
	private MutableTreeNode parent;
	private Vector<TreeNode> childrens = new Vector<TreeNode>();

	public TargetMutableTreeNode(Target cible) {
		this.cible = cible;
		
		if(cible != null)
			cible.addTargetListener(this);
		
		createChildren();
	}
	
	/**
	 * @return cible
	 */
	public Target getCible() {
		return cible;
	}

	/**
	 * @param cible cible à définir
	 */
	public void setCible(Target cible) {
		if(cible != null) {
			cible.removeTargetListener(this);
			childrens.removeAllElements();
			
			cible.addTargetListener(this);
		}
		this.cible = cible;
		
		createChildren();
	}
	
	private void createChildren() {
		if(cible != null) {
			for(int i = 0; i < cible.getNbMaxArchers(); i++) {
				Concurrent concurrent = cible.getConcurrentAt(i);
				if(concurrent == null)
					insert(new DefaultMutableTreeNode(new TargetPosition(cible.getNumCible(), i)), i);
				else
					insert(new DefaultMutableTreeNode(concurrent), i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	public void insert(MutableTreeNode child, int index) {
		child.setParent(this);
		childrens.add(index, child);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#remove(int)
	 */
	public void remove(int index) {
		childrens.remove(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#remove(javax.swing.tree.MutableTreeNode)
	 */
	public void remove(MutableTreeNode node) {
		childrens.remove(node);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#removeFromParent()
	 */
	public void removeFromParent() {
		parent.remove(this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#setParent(javax.swing.tree.MutableTreeNode)
	 */
	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.MutableTreeNode#setUserObject(java.lang.Object)
	 */
	public void setUserObject(Object object) {
		if(object instanceof Target) {
			setCible((Target)object);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	public Enumeration<TreeNode> children() {
		return childrens.elements();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	public TreeNode getChildAt(int childIndex) {
		return childrens.get(childIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		return childrens.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndex(TreeNode node) {
		return childrens.indexOf(node);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	public TreeNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return childrens.size() == 0;
	}

	/* (non-Javadoc)
	 * @see org.concoursjeunes.CibleListener#concurrentJoined(org.concoursjeunes.CibleEvent)
	 */
	public void concurrentJoined(TargetEvent e) {
		DefaultMutableTreeNode concurrentNode = (DefaultMutableTreeNode)childrens.get(e.getConcurrent().getPosition());
		concurrentNode.setUserObject(e.getConcurrent());
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.CibleListener#concurrentQuit(org.concoursjeunes.CibleEvent)
	 */
	public void concurrentQuit(TargetEvent e) {
		DefaultMutableTreeNode concurrentNode = (DefaultMutableTreeNode)childrens.get(e.getConcurrent().getPosition());
		concurrentNode.setUserObject(new TargetPosition(cible.getNumCible(), e.getConcurrent().getPosition()));
	}
	
	@Override
	public String toString() {
		if(cible != null)
			return cible.toString();
		return ""; //$NON-NLS-1$
	}
}
