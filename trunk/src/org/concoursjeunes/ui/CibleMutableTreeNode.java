/**
 * 
 */
package org.concoursjeunes.ui;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.concoursjeunes.Cible;
import org.concoursjeunes.CibleEvent;
import org.concoursjeunes.CibleListener;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.TargetPosition;

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class CibleMutableTreeNode implements MutableTreeNode, CibleListener {
	
	private Cible cible;
	private MutableTreeNode parent;
	private Vector<TreeNode> childrens = new Vector<TreeNode>();

	public CibleMutableTreeNode(Cible cible) {
		this.cible = cible;
		
		if(cible != null)
			cible.addCibleListener(this);
		
		createChildren();
	}
	
	/**
	 * @return cible
	 */
	public Cible getCible() {
		return cible;
	}

	/**
	 * @param cible cible à définir
	 */
	public void setCible(Cible cible) {
		if(cible != null) {
			cible.removeCibleListener(this);
			childrens.removeAllElements();
			
			cible.addCibleListener(this);
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
		if(object instanceof Cible) {
			setCible((Cible)object);
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
	public void concurrentJoined(CibleEvent e) {
		DefaultMutableTreeNode concurrentNode = (DefaultMutableTreeNode)childrens.get(e.getConcurrent().getPosition());
		concurrentNode.setUserObject(e.getConcurrent());
	}
	
	/* (non-Javadoc)
	 * @see org.concoursjeunes.CibleListener#concurrentQuit(org.concoursjeunes.CibleEvent)
	 */
	public void concurrentQuit(CibleEvent e) {
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
