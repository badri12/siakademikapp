package org.stth.siak.jee.ui.mahasiswa;

import java.util.List;

import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarLogPerkuliahan;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MahasiswaKelasDiikuti extends VerticalLayout implements View{
	private static final long serialVersionUID = 1L;
	private Mahasiswa mhs;
	private Semester semester;
	private String ta;
	private List<KelasPerkuliahan> lkp;
	
	public MahasiswaKelasDiikuti(){
		mhs = VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getCurrentSemester();
		ta = k.getCurrentTa();
    	setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("Kelas Perkuliahan Anda Semester "+semester+ " T.A "+ta));
        Component content = buildContent2();
        addComponent(content);
        addComponent(ViewFactory.footer());
		
	}
	
	private Component buildContent2(){
		Panel pnl = new Panel("Daftar Mata Kuliah");
		
		lkp = PesertaKuliahPersistence.getKelasPerkuliahanMahasiswaSemester(mhs, semester, ta);
		Grid<KelasPerkuliahan> g = new Grid<>("", lkp);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(KelasPerkuliahan::getMataKuliah).setCaption("MATAKULIAH");
		g.addColumn(kp->{
			return kp.getDosenPengampu();
		}).setCaption("DOSEN");
		g.addComponentColumn(kp->{
			HorizontalLayout hl = new HorizontalLayout();
			PesertaKuliah pk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahanMahasiswa(kp, mhs);
			int pertemuan = LogPerkuliahanPersistence.getByKelas(kp).size();
			int kehadiran = LogKehadiranPerkuliahanPersistence.getByPesertaKuliah(pk).size();
			String ratio = kehadiran +" dari " +pertemuan;
			Button log = new Button("");
			log.setIcon(VaadinIcons.EYE);
			log.addStyleName(ValoTheme.BUTTON_PRIMARY);
			log.addClickListener(e->{
				showLogPerkuliahan(kp);
			});
			hl.addComponents( log,new Label(ratio));
			return hl;
		}).setCaption("Log");
		g.setSizeFull();
		
		pnl.setContent(g);
		//tabel.setVisibleColumns("matakuliah","sks","dosenPengampu","jadwal","kehadiran","log");
		return pnl;
	}
	
	private void showLogPerkuliahan(KelasPerkuliahan kp) {
		DaftarLogPerkuliahan dlp = new DaftarLogPerkuliahan(kp);
		GeneralPopups.showGenericWindow(dlp, "Log Perkuliahan");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}
	
	
}
