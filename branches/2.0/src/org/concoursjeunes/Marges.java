/*
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

/**
 * @author Aurélien JEOFFRAY
 *
 */
public class Marges {
	double top = 0.0;
	double bottom = 0.0;
	double right = 0.0;
	double left = 0.0;
	
	public Marges() {
		
	}
	/**
	 * @param top
	 * @param bottom
	 * @param right
	 * @param left
	 */
	public Marges(double top, double bottom, double right, double left) {
		this.top = top;
		this.bottom = bottom;
		this.right = right;
		this.left = left;
	}
	
	/**
	 * @return bottom
	 */
	public double getBottom() {
		return bottom;
	}
	
	/**
	 * @param bottom bottom à définir
	 */
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}
	
	/**
	 * @return left
	 */
	public double getLeft() {
		return left;
	}
	
	/**
	 * @param left left à définir
	 */
	public void setLeft(double left) {
		this.left = left;
	}
	
	/**
	 * @return right
	 */
	public double getRight() {
		return right;
	}
	
	/**
	 * @param right right à définir
	 */
	public void setRight(double right) {
		this.right = right;
	}
	
	/**
	 * @return top
	 */
	public double getTop() {
		return top;
	}
	
	/**
	 * @param top top à définir
	 */
	public void setTop(double top) {
		this.top = top;
	}
}
