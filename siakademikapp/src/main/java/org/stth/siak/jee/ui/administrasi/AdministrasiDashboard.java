package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.jee.ui.generalview.ChartMahasiswa;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class AdministrasiDashboard extends VerticalLayout implements View{
	private static final long serialVersionUID = 59817202994172308L;

	public AdministrasiDashboard() {
		ChartMahasiswa cm = new ChartMahasiswa();
		addComponent(cm);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
