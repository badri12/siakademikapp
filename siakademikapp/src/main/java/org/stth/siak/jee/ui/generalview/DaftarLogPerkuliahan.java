package org.stth.siak.jee.ui.generalview;

import java.util.List;

import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.jee.ui.dosen.DosenDaftarHadirMahasiswa;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarLogPerkuliahan extends VerticalLayout{

	private static final long serialVersionUID = 7819101337352503246L;
	protected DosenKaryawan dosen;
	protected KelasPerkuliahan kelasPerkuliahan;
	private Panel content = new Panel();

	public DaftarLogPerkuliahan(KelasPerkuliahan kp) {
		this.kelasPerkuliahan = kp;
		Responsive.makeResponsive(this);
		saringData();
		addComponent(content);
	}
	
	
	protected void saringData() {
		List<LogPerkuliahan> l;
		l = LogPerkuliahanPersistence.getByKelas(kelasPerkuliahan);
		Grid<LogPerkuliahan> g = new Grid<>();
		g.setItems(l);
		g.addComponentColumn(log->{
			Button daftarHadir = new Button("Daftar Hadir");
			daftarHadir.setIcon(VaadinIcons.LIST);
			daftarHadir.setStyleName(ValoTheme.BUTTON_PRIMARY);
			daftarHadir.addClickListener(e->{
				siapkanDaftarHadir(log);
			});
			return daftarHadir;
		}).setCaption("AKSI");
		g.addColumn(LogPerkuliahan::getDiisiOleh).setCaption("DOSEN");
		g.addColumn(LogPerkuliahan::getKelasPerkuliahan).setCaption("KELAS");
		g.addColumn(LogPerkuliahan::getTanggalPertemuan).setCaption("WAKTU");
		g.addColumn(LogPerkuliahan::getRuangPertemuan).setCaption("RUANGAN");
		g.addColumn(LogPerkuliahan::getMateriPertemuan).setCaption("MATERI");
		g.addColumn(LogPerkuliahan::getEntryOleh).setCaption("PETUGAS ENTRY");
		g.setSizeFull();
		content.setContent(g);
	}
	
	private void siapkanDaftarHadir(LogPerkuliahan log){
		DosenDaftarHadirMahasiswa dhm = new DosenDaftarHadirMahasiswa(log);
		GeneralPopups.showGenericWindow(dhm,"Daftar Hadir Mahasiswa");
	}
	
	
	

}
