package org.stth.siak.jee.ui.dosen;

import com.github.appreciated.app.layout.builder.NavigatorAppLayoutBuilder;


public class DosenMenuComponent  {
	private NavigatorAppLayoutBuilder drawer;
	public DosenMenuComponent(NavigatorAppLayoutBuilder drawer) {
		this.drawer=drawer;
		createMenu();
	}
	
	private void createMenu(){
		for(DosenMenuItems item : DosenMenuItems.values()){
			drawer.add(item.getViewName(), item.name() ,item.getIcon(), item.getViewClass());
		}
	}

}
