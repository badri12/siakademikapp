package org.stth.siak.jee.ui.administrasi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.ConfirmDialog;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdministrasiDaftarLogPerkuliahan extends VerticalLayout implements View {

	private static final long serialVersionUID = -8249051415291086482L;
	private Semester semester;
	private String ta;
	private ComboBox<DosenKaryawan> cbDosen = new ComboBox<>("Dosen Pengampu");
	private ComboBox<KelasPerkuliahan> cbMataKuliah = new ComboBox<>("Mata Kuliah");
	private DateField periodStart = new DateField("Periode Mulai");
	private DateField periodEnd = new DateField("Periode End");
	
	private Panel pnl = new Panel("Daftar Hasil Penyaringan");
	protected DosenKaryawan dosen;
	protected KelasPerkuliahan kelasPerkuliahan;
	//private List<UserAccessRightsAdministrasi> lacl;
	private List<LogPerkuliahan> l;

	public AdministrasiDaftarLogPerkuliahan() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		//lacl = (List<UserAccessRightsAdministrasi>) VaadinSession.getCurrent().getAttribute("admrights");
		
		addComponent(ViewFactory.header("Catatan Perkuliahan Semester "+semester+" t.a "+ ta));
		addComponent(createActionButton());
		addComponent(createFilterComponent());
		addComponent(pnl);
		addComponent(ViewFactory.footer());
	}
	
	private void siapkanDaftarDosen(){
		cbDosen.setItems(DosenKaryawanPersistence.getDosen());
		cbDosen.addValueChangeListener(e->{
			dosen = e.getValue();
			siapkanDaftarKelas();
		});
	}
	
	private void siapkanDaftarKelas() {
		List<KelasPerkuliahan> lKelas = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		cbMataKuliah.setItems(lKelas);	
		cbMataKuliah.addValueChangeListener(e->{
			kelasPerkuliahan = e.getValue();
		});
	}
	
	private Component createActionButton() {
		HorizontalLayout hl = new HorizontalLayout();
		Button tambah;
		tambah = new Button("Tambah");
		tambah.setIcon(VaadinIcons.PLUS);
		tambah.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		tambah.addClickListener(e->{
			AdministrasiEntryLogPerkuliahan entryLog = new AdministrasiEntryLogPerkuliahan();
			siapkanLogPerkuliahan(entryLog);
		});
		
		hl.addComponents(tambah);
		
		
		hl.setSpacing(true);
		return hl;
	}
	
	private Component createFilterComponent(){
		Panel pnl = new Panel("Filter Data");
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		LocalDate ld = LocalDate.now();
		cbDosen.setWidth("200px");
		cbMataKuliah.setWidth("300px");
		periodEnd.setValue(ld);
		ld= ld.minusMonths(1);
		periodStart.setValue(ld);
		Button saring = new Button("Saring");
		saring.setIcon(VaadinIcons.SEARCH);
		saring.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saring.addClickListener(e->{
			saringData();
		});
		hl.addComponents(cbDosen,cbMataKuliah,periodStart,periodEnd, saring);
		hl.setComponentAlignment(saring, Alignment.BOTTOM_LEFT);
		vl.addComponents(hl);
		vl.setSpacing(true);
		vl.setMargin(true);
		siapkanDaftarDosen();
		pnl.setContent(vl);
		return pnl;
	}
	
	protected void saringData() {
		LocalDateTime dt1 = LocalDateTime.of(periodStart.getValue(), LocalTime.MIN);
		LocalDateTime dt2 = LocalDateTime.of(periodEnd.getValue(), LocalTime.MAX);
		
		if (kelasPerkuliahan!=null){
			l = LogPerkuliahanPersistence.getByKelasOnPeriod(kelasPerkuliahan, dt1, dt2);
		} else if (dosen!=null){
			l = LogPerkuliahanPersistence.getByDosenOnPeriod(dosen,  dt1, dt2);
		} else {
			l = LogPerkuliahanPersistence.getLogOnPeriod( dt1, dt2);
		}
		//Collections.sort(l);
		Grid<LogPerkuliahan> g = new Grid<>();
		g.setStyleName(ValoTheme.TABLE_COMPACT);
		g.setStyleName(ValoTheme.TABLE_SMALL, true);
		g.setItems(l);
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addComponentColumn(log->{
			HorizontalLayout hl = new HorizontalLayout();
			Button daftarHadir = new Button("Daftar Hadir");
			daftarHadir.setIcon(VaadinIcons.TABLE);
			daftarHadir.setStyleName(ValoTheme.BUTTON_PRIMARY);
			daftarHadir.addClickListener(e->{
				siapkanIsianDaftarHadir(log);
			});
			Button edit = new Button();
			edit.setIcon(VaadinIcons.EDIT);
			edit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			edit.addClickListener(e->{
				AdministrasiEntryLogPerkuliahan entryLog = new AdministrasiEntryLogPerkuliahan(log);
				siapkanLogPerkuliahan(entryLog);
			});
			Button hapus = new Button();
			hapus.setIcon(VaadinIcons.TRASH);
			hapus.setStyleName(ValoTheme.BUTTON_DANGER);
			hapus.addClickListener(e->{
				ConfirmDialog.show(UI.getCurrent(), "", "Anda akan menghapus log perkuliahan", "Ya", "Tidak", c->{
					if (c.isConfirmed()) {
						deleteLog(log);
						Notification.show("Data berhasil dihapus");
						saringData();
					}
				});
				
			});
			hl.addComponents(daftarHadir, edit, hapus);
			return hl;
		}).setCaption("AKSI");
		g.addColumn(LogPerkuliahan::getDiisiOleh).setCaption("DOSEN");
		g.addColumn(LogPerkuliahan::getKelasPerkuliahan).setCaption("KELAS");
		g.addColumn(LogPerkuliahan::getTanggalPertemuan).setCaption("WAKTU");
		g.addColumn(LogPerkuliahan::getRuangPertemuan).setCaption("RUANGAN");
		g.addColumn(LogPerkuliahan::getMateriPertemuan).setCaption("MATERI");
		g.addColumn(LogPerkuliahan::getEntryOleh).setCaption("PETUGAS ENTRY");
		g.setSizeFull();
		pnl.setContent(g);
		
	}
	
	private void siapkanIsianDaftarHadir(LogPerkuliahan log) {
		final Window win = new Window("Daftar Hadir Mahasiswa");
		AdministrasiIsianDaftarHadirMahasiswa dhm = new AdministrasiIsianDaftarHadirMahasiswa(log, win);
		win.setContent(dhm);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	
	private void siapkanLogPerkuliahan(AdministrasiEntryLogPerkuliahan entryLog){
		final Window win = new Window("Entry Log Perkuliahan");
		entryLog.setParent(win);
		win.addCloseListener(e->{
			saringData();
		});
		win.setContent(entryLog);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	

	private void deleteLog(LogPerkuliahan log) {
		List<LogKehadiranPesertaKuliah> l = LogKehadiranPerkuliahanPersistence.getByLogPerkuliahan(log);
		for (LogKehadiranPesertaKuliah logKehadiranPesertaKuliah : l) {
			GenericPersistence.delete(logKehadiranPesertaKuliah);
		}
		GenericPersistence.delete(log);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
