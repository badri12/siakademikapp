package org.stth.siak.jee.ui.dosen;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarLogPerkuliahan;
import org.stth.siak.jee.ui.generalview.PesertaKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ExportToExcel;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;

public class DosenKelasDiampu extends VerticalLayout implements View{
	private static final long serialVersionUID = 1653775031486924211L;
	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private Panel filter;
	private Panel daftarTugasMengajar;
	private List<KelasPerkuliahan> lm;
	private Grid<KelasPerkuliahan> g;

	public DosenKelasDiampu() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Tugas Mengajar Semester "));
		filter = new Panel("Filter");
		filter.setContent(buildFilter());
		daftarTugasMengajar = new Panel("Daftar Tugas Mengajar");
		daftarTugasMengajar.setContent(getTable());

		addComponents(filter, daftarTugasMengajar);
		addComponent(buttonComponent());
		addComponent(ViewFactory.footer());

	}
	private Component buttonComponent(){
		HorizontalLayout hl = new HorizontalLayout();
		Button bCetakAbsensi = new Button("Rekap Kehadiran");
		bCetakAbsensi.setIcon(VaadinIcons.PRINT);
		bCetakAbsensi.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bCetakAbsensi.addClickListener(cetak->{
			if (g.getSelectedItems().size()>0) {
				List<KelasPerkuliahan> lkp = new ArrayList<>();
				lkp.addAll(g.getSelectedItems());
				try {
					StreamResource source = ReportResourceGenerator.cetakAbsenKuliah(lkp);
					EnhancedBrowserWindowOpener.extendOnce(bCetakAbsensi).open((source));
					//getUI().getPage().open(source, "_blank", false);
					//GeneralPopups.windowPrint(source);
				} catch (JRException e1) {
					e1.printStackTrace();
				}
			}else Notification.show("Anda belum memilih kelas", Type.WARNING_MESSAGE);

		});
		Button bExcel = new Button("Total Kehadiran");
		bExcel.setIcon(VaadinIcons.DOWNLOAD);
		bExcel.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bExcel.addClickListener(e->{
			if (g.getSelectedItems().size()>0) {
				try {
					StreamResource source = ExportToExcel.createFileExcel(ExportToExcel.kehadiranPeserta(g.getSelectedItems()) , "Kehadiran Mahasiswa");
					ResourceReference ref = new ResourceReference(source, this, "get"+source.hashCode());
				    this.setResource("get"+source.hashCode(), source); // now it's available for download
				    getUI().getPage().open(ref.getURL(), null);
					//EnhancedBrowserWindowOpener.extendOnce(exportNilai).open(source);
					//getUI().getPage().open(source, "_blank", false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else Notification.show("Anda belum memilih kelas", Type.WARNING_MESSAGE);
		});
		hl.addComponents(bCetakAbsensi, bExcel);
		return hl;
	}
	
	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flMid = new FormLayout();
		FormLayout flKanan = new FormLayout();
		TextField tfta = new TextField("T.A");
		tfta.setValue(ta);
		flKiri.addComponent(tfta);
		ComboBox<Semester> cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(semester);
		flMid.addComponent(cbSemester);
		Button cari = new Button("Cari");
		cari.addClickListener(e->{
			ta=tfta.getValue();
			semester=cbSemester.getValue();
			daftarTugasMengajar.setContent(getTable());
		});
		flKanan.addComponent(cari);
		hl.addComponents(flKiri, flMid, flKanan);
		return hl;
	}

	private Component getTable(){
		lm = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		g = new Grid<>("",lm);
		g.setSizeFull();
		g.setSelectionMode(SelectionMode.MULTI);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		Responsive.makeResponsive(this);
		g.addColumn(kp->{
			return kp.getMataKuliah().getSks();
		}).setCaption("SKS");
		g.addColumn(kp->{
			return kp.getMataKuliah();
		}).setCaption("MATAKULIAH");
		
		g.addColumn(kp->{
			return kp.getProdi().getNama();
		}).setCaption("PRODI");
		g.addColumn(kp->{
			return kp.getKodeKelas();
		}).setCaption("KELAS");
		g.addComponentColumn(kp->{
			HorizontalLayout hl = new HorizontalLayout();
			Button button = new Button("Peserta Kuliah");
			button.setIcon(VaadinIcons.LIST);
			button.setStyleName(ValoTheme.BUTTON_PRIMARY);
			button.addClickListener(e->{
				showPesertaKuliah(kp);
			});
			Button buttonLog = new Button("Log Mengajar");
			buttonLog.setIcon(VaadinIcons.LIST);
			buttonLog.setStyleName(ValoTheme.BUTTON_PRIMARY);
			buttonLog.addClickListener(e->{
				showLogPerkuliahan(kp);
			});
			hl.addComponents(button,buttonLog);
			return hl;
		}).setCaption("AKSI");
		
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private void showPesertaKuliah(KelasPerkuliahan o) {
		PesertaKelasPerkuliahanView c = new PesertaKelasPerkuliahanView(o);
		c.setRekapKehadiranVisible();
		c.refreshContent();
		GeneralPopups.showGenericWindow(c,"Daftar Peserta Kuliah");
	}


	private void showLogPerkuliahan(KelasPerkuliahan kp) {
		DaftarLogPerkuliahan dlp = new DaftarLogPerkuliahan(kp);
		GeneralPopups.showGenericWindow(dlp, "Log Perkuliahan");
	}


}
