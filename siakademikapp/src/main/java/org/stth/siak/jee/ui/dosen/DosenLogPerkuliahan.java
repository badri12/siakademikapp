package org.stth.siak.jee.ui.dosen;

import java.util.List;

import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenLogPerkuliahan extends VerticalLayout implements View{

	private static final long serialVersionUID = 2842950566161361848L;
	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private KelasPerkuliahan kelas;
	private ComboBox<KelasPerkuliahan> cbKelas = new ComboBox<>("Pilih kelas");
	private Panel p = new Panel("Log Perkuliahan");

	public DosenLogPerkuliahan() {
		setMargin(true);
		setSpacing(true);
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		Responsive.makeResponsive(this);
		siapkanPilihanKelas();
		addComponent(ViewFactory.header("Log Perkuliahan Semester "+semester+" t.a "+ta ));
		addComponent(createFilterComponent());
		addComponent(p);
		addComponent(ViewFactory.footer());
	}
	
	
	private Component createFilterComponent() {
		Panel pnl = new Panel("Pilih kelas");
		HorizontalLayout hl = new HorizontalLayout();
		pnl.setContent(hl);
		hl.setMargin(true);
		hl.addComponent(cbKelas);
		cbKelas.setWidth("300px");
		Button saring = new Button("Saring");
		saring.addStyleName(ValoTheme.BUTTON_PRIMARY);
		saring.setIcon(VaadinIcons.SEARCH);
		saring.addClickListener(e->{
			prepareContent();
		});
		hl.addComponent(saring);
		hl.setComponentAlignment(saring, Alignment.BOTTOM_LEFT);
		return pnl;
	}
	
	protected void prepareContent() {
		if (kelas==null){
			Notification.show("Pilih kelas terlebih dahulu", Notification.Type.ERROR_MESSAGE);
			return;
		}
	
		List<LogPerkuliahan> l = LogPerkuliahanPersistence.getByKelas(kelas);
		
		//String[] visIds = {"tanggalPertemuan","ruangPertemuan","materiPertemuan","daftarHadir"};
		
		Grid<LogPerkuliahan> g = new Grid<>("", l);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(LogPerkuliahan::getTanggalPertemuan).setCaption("TANGGAL");
		g.addColumn(LogPerkuliahan::getRuangPertemuan).setCaption("RUANG");
		g.addColumn(LogPerkuliahan::getMateriPertemuan).setCaption("MATERI");
		g.addComponentColumn(log->{
			Button b =  new Button();
			b.setIcon(VaadinIcons.EYE);
			b.setStyleName(ValoTheme.BUTTON_PRIMARY);
			b.addClickListener(e->{
				siapkanDaftarHadir(log);
			});
			return b;
		}).setCaption("DAFTAR HADIR");
		p.setContent(g);
	}


	private void siapkanPilihanKelas(){
		List<KelasPerkuliahan> l = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		cbKelas.setItems(l);
		cbKelas.addValueChangeListener(e->{
			kelas = e.getValue();
		});
	}
	
	private void siapkanDaftarHadir(LogPerkuliahan log){
		DosenDaftarHadirMahasiswa dhm = new DosenDaftarHadirMahasiswa(log);
		GeneralPopups.showGenericWindow(dhm, "Daftar Hadir Mahasiswa");
	}


	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

}
