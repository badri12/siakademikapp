package org.stth.siak.jee.ui.dosen;


import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.jee.ui.generalview.PesertaKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DosenRiwayatMengajar extends VerticalLayout implements View{

	private static final long serialVersionUID = 1653775031486924211L;
	private DosenKaryawan dosen;

	public DosenRiwayatMengajar() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Riwayat Mengajar Dosen"));
		Panel p = new Panel("RIWAYAT MENGAJAR");
		p.setContent(getTable());
		addComponent(p);
		addComponent(ViewFactory.footer());

	}

	private Component getTable(){
		List<KelasPerkuliahan> lm = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosen(dosen);
		Collections.sort(lm, Collections.reverseOrder());
		Responsive.makeResponsive(this);
		Grid<KelasPerkuliahan> g = new Grid<>("",lm);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(kp->{
			return kp.getMataKuliah();
		}).setCaption("MATAKULIAH");
		g.addColumn(kp->{
			return kp.getProdi().getNama();
		}).setCaption("PRODI");
		g.addColumn(KelasPerkuliahan::getSemester).setCaption("SEMESTER");
		g.addColumn(KelasPerkuliahan::getTahunAjaran).setCaption("T.A");
		g.addComponentColumn(kp->{
			HorizontalLayout hl = new HorizontalLayout();
			Button button = new Button();
			button.setIcon(VaadinIcons.EYE);
			button.setStyleName(ValoTheme.BUTTON_PRIMARY);
			button.addClickListener(e->{
				showPesertaKuliah(kp);
			});
			hl.addComponent(button);
			return hl;
		}).setCaption("PESERTA");
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private void showPesertaKuliah(KelasPerkuliahan o) {
		final Window win = new Window("Daftar Peserta Kuliah");
		Component c = new PesertaKelasPerkuliahanView(o);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);

	}


}
