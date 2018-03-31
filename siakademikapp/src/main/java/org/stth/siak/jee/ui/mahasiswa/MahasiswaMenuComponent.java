package org.stth.siak.jee.ui.mahasiswa;

import com.github.appreciated.app.layout.builder.NavigatorAppLayoutBuilder;


public class MahasiswaMenuComponent  {
	private NavigatorAppLayoutBuilder drawer;
	public MahasiswaMenuComponent(NavigatorAppLayoutBuilder drawer) {
		this.drawer=drawer;
		createMenu();
	}
	private void createMenu(){
		for(MahasiswaMenuItems item : MahasiswaMenuItems.values()){
			drawer.add(item.getViewName(), item.name() ,item.getIcon(), item.getViewClass());
		}
	}
}
