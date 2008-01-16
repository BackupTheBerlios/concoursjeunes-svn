/**
 * 
 */
package org.concoursjeunes.ui;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.concoursjeunes.Cible;
import org.concoursjeunes.CibleEvent;
import org.concoursjeunes.CibleListener;
import org.concoursjeunes.Concurrent;
import org.concoursjeunes.TargetPosition;

/**
 * @author Aurélien JEOFFRAY
 * 
 */
public class TargetTreeModel implements TreeModel, CibleListener {

	private final EventListenerList listeners = new EventListenerList();

	private String rootLabel = ""; //$NON-NLS-1$
	private ArrayList<Cible> targetChilds = new ArrayList<Cible>();

	public TargetTreeModel() {

	}

	public TargetTreeModel(String rootLabel, ArrayList<Cible> targetChilds) {
		this.rootLabel = rootLabel;
		this.targetChilds = targetChilds;
	}

	/**
	 * @return rootLabel
	 */
	public String getRootLabel() {
		return rootLabel;
	}

	/**
	 * @param rootLabel
	 *            rootLabel à définir
	 */
	public void setRootLabel(String rootLabel) {
		this.rootLabel = rootLabel;

		fireTreeNodesChanged(new TreePath(rootLabel));
	}

	/**
	 * @return targetChild
	 */
	public ArrayList<Cible> getTargetChilds() {
		return targetChilds;
	}

	/**
	 * @param targetChilds
	 *            targetChild à définir
	 */
	public void setTargetChilds(ArrayList<Cible> targetChilds) {
		if (this.targetChilds != null)
			for (Cible cible : this.targetChilds) {
				cible.removeCibleListener(this);
			}

		this.targetChilds = targetChilds;

		for (Cible cible : targetChilds) {
			cible.addCibleListener(this);
		}

		fireTreeStructureChanged(new TreePath(new Object[] { rootLabel }));
	}

	public void addTargetChild(Cible cible) {
		targetChilds.add(cible);

		cible.addCibleListener(this);

		fireTreeNodesInserted(cible);
	}

	public void removeTargetChild(Cible cible) {
		targetChilds.remove(cible);

		cible.removeCibleListener(this);

		fireTreeNodesRemoved(cible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(TreeModelListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		if (parent == rootLabel)
			return targetChilds.get(index);
		else if (parent instanceof Cible) {
			Cible cibleParent = (Cible) parent;
			Concurrent concurrent = cibleParent.getConcurrentAt(index);
			if (concurrent == null)
				return new TargetPosition(cibleParent.getNumCible(), index);
			return concurrent;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		if (parent == rootLabel)
			return targetChilds.size();
		else if (parent instanceof Cible) {
			Cible cibleParent = (Cible) parent;
			return cibleParent.getNbMaxArchers();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == rootLabel)
			return targetChilds.indexOf(child);
		else if (parent instanceof Cible) {
			Cible cibleParent = (Cible) parent;
			if (child instanceof Concurrent) {
				return cibleParent.indexOf((Concurrent) child);
			}
			TargetPosition targetPosition = (TargetPosition) child;
			return targetPosition.getPosition();
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return rootLabel;
	}

	public TreePath getTreePathForNode(Object node) {
		if (node == rootLabel)
			return new TreePath(rootLabel);
		else if (node instanceof Cible)
			return new TreePath(new Object[] { rootLabel, node });
		else if (node instanceof Concurrent) {
			Concurrent concurrent = (Concurrent) node;
			if (concurrent.getCible() > 0) {
				Cible cible = targetChilds.get(concurrent.getCible() - 1);

				return new TreePath(new Object[] { rootLabel, cible, concurrent });
			}
		} else {
			assert node instanceof String : "A ce niveau un noeud doit toujours être une chaine"; //$NON-NLS-1$

			String posLabel = (String) node;

			Cible cible = targetChilds.get(Integer.parseInt(posLabel.substring(0, posLabel.length() - 1)));

			return new TreePath(new Object[] { rootLabel, cible, posLabel });
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		if (node instanceof TargetPosition || node instanceof Concurrent)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(TreeModelListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.CibleListener#concurrentJoined(org.concoursjeunes.CibleEvent)
	 */
	public void concurrentJoined(CibleEvent e) {
		fireTreeNodesChanged(getTreePathForNode(e.getCible()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concoursjeunes.CibleListener#concurrentQuit(org.concoursjeunes.CibleEvent)
	 */
	public void concurrentQuit(CibleEvent e) {
		fireTreeNodesChanged(getTreePathForNode(e.getCible()));
		// fireTreeNodesChanged(getTreePathForNode(Cible.getCibleLibelle(
		// e.getConcurrent().getCible()-1, e.getConcurrent().getPosition())));
	}

	private void fireTreeNodesChanged(TreePath treePath) {
		Object[] childs = new Object[getChildCount(treePath.getLastPathComponent())];
		int[] indices = new int[childs.length];
		for (int i = 0; i < childs.length; i++) {
			childs[i] = getChild(treePath.getLastPathComponent(), i);
			indices[i] = i;
		}
		TreeModelEvent treeModelEvent;
		if (childs.length > 0) {
			treeModelEvent = new TreeModelEvent(this, treePath, indices, childs);
		} else
			treeModelEvent = new TreeModelEvent(this, treePath);

		for (TreeModelListener tml : listeners.getListeners(TreeModelListener.class)) {
			tml.treeNodesChanged(treeModelEvent);
		}
	}

	private void fireTreeNodesInserted(Cible cible) {
		TreeModelEvent treeModelEvent = new TreeModelEvent(this, new Object[] { rootLabel, cible });
		for (TreeModelListener tml : listeners.getListeners(TreeModelListener.class)) {
			tml.treeNodesInserted(treeModelEvent);
		}
	}

	private void fireTreeNodesRemoved(Cible cible) {
		TreeModelEvent treeModelEvent = new TreeModelEvent(this, new Object[] { rootLabel, cible });
		for (TreeModelListener tml : listeners.getListeners(TreeModelListener.class)) {
			tml.treeNodesRemoved(treeModelEvent);
		}
	}

	private void fireTreeStructureChanged(TreePath treePath) {
		TreePath parentPath = treePath.getParentPath();
		TreeModelEvent treeModelEvent;
		if (parentPath != null) {
			treeModelEvent = new TreeModelEvent(this, parentPath, new int[] { getIndexOfChild(parentPath.getLastPathComponent(), treePath.getLastPathComponent()) }, new Object[] { treePath
					.getLastPathComponent() });
		} else
			treeModelEvent = new TreeModelEvent(this, treePath);
		for (TreeModelListener tml : listeners.getListeners(TreeModelListener.class)) {
			tml.treeStructureChanged(treeModelEvent);
		}
	}
}
