package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiKurikulum extends VerticalLayout implements View{
	private static final long serialVersionUID = 159820765224305385L;
	private Panel daftarKurikulum;
	
	public AdministrasiKurikulum() {
		daftarKurikulum = new Panel("Daftar Kurikulum");
		daftarKurikulum.setContent(daftarKurikulum());
		Button add = new Button("Tambah");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(ev->{
			AdministrasiEditorKurikulum aek = new AdministrasiEditorKurikulum(null);
			UI.getCurrent().addWindow(aek);
			daftarKurikulum.setContent(daftarKurikulum());
		});
		addComponent(ViewFactory.header("Administrasi Kurikulum"));
		addComponent(add);
		addComponents(daftarKurikulum);
		setSpacing(true);
		setMargin(true);
	}
	private Component daftarKurikulum(){
		List<Kurikulum> lk= GenericPersistence.findList(Kurikulum.class);
		Grid<Kurikulum> g = new Grid<>();
		g.setItems(lk);
		daftarKurikulum.setCaption("Daftar Kurikulum ("+ lk.size()+")");
		g.setSizeFull();
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(Kurikulum::getNama).setCaption("NAMA");
		g.addColumn(Kurikulum::getProdi).setCaption("Program Studi");
		g.addColumn(Kurikulum::isAktif).setCaption("AKTIF");
		g.addColumn(Kurikulum::getTahunMulai).setCaption("Tahun");
		//g.addColumn("sksLulus");
		g.addComponentColumn(kur->{
			Button b = new Button();
			b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.setIcon(VaadinIcons.EDIT);
			b.addClickListener(e->{
				AdministrasiEditorKurikulum aek = new AdministrasiEditorKurikulum(kur);
				getUI().addWindow(aek);
			});
			return b;
		});
		return g;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
