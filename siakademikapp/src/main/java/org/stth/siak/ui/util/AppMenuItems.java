package org.stth.siak.ui.util;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public interface AppMenuItems {
	public String getViewName(); 
	public Class<? extends View> getViewClass(); 
	public Resource getIcon(); 
	public boolean isStateful(); 

}
