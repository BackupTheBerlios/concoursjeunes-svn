package org.concoursjeunes.plugins;

import javax.swing.JFrame;

public interface ImportPlugin {
	public void setParentFrame(JFrame parentframe);
	public void start();
	public boolean isSuccess();
}
