package org.stth.siak.jee.ui.administrasi;


import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.JadwalKuliahPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdministrasiDaftarJadwalKuliah extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosenPengampu;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private ComboBox<DosenKaryawan> cbDosenPengampu = new ComboBox<>("Pilih Dosen ");
	private ProgramStudi prodi;
	private List<DosenKaryawan> lpa;
	private VerticalLayout content = new VerticalLayout();
	private TextField tfNama;
	private TextField tfNimStart;
	private TextField tfAngkatan;
	private List<JadwalKuliah> ljk;
	private Semester semester;
	private String ta;

	public AdministrasiDaftarJadwalKuliah() {
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Administrasi Jadwal Kuliah Semester "+ semester + " T.A " + ta));
		addComponent(createActionButton());
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		siapkanPilihanDosen();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	
	private Component createActionButton() {
		HorizontalLayout hl = new HorizontalLayout();
		Button tambah;
		tambah = new Button("Tambah");
		tambah.addClickListener(e->{
			JadwalKuliah jk = new JadwalKuliah();
			siapkanEditorJadwalKuliah(jk);
		});
		Button edit= new Button("Edit"),hapus = new Button("Hapus");
		hl.addComponents(tambah, edit, hapus);
		hl.setSpacing(true);
		return hl;
	}
	
	private void siapkanPilihanProdi(){
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.addValueChangeListener(e->{
			prodi = e.getValue();
		});
	}
	
	private void siapkanPilihanDosen(){
		lpa = DosenKaryawanPersistence.getDosen();
		cbDosenPengampu.setItems(lpa); 
		cbDosenPengampu.addValueChangeListener(e->{
			dosenPengampu = e.getValue();
		});
	}

	
	private Component createFilterComponent() {
		Panel pnl = new Panel("Saring Jadwal");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		Button buttonFilter = new Button("Cari");
		buttonFilter.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				filter();
			}
		});
		hl.addComponents(cbProdi,cbDosenPengampu,buttonFilter);
		pnl.setContent(hl);
		return pnl;
	}
	
	
	protected void filter() {
		content.removeAllComponents();
		ljk = JadwalKuliahPersistence.getJadwalByDosenProdi(dosenPengampu, prodi,semester,ta);
		Panel panel = new Panel("Daftar Jadwal");
		Grid<JadwalKuliah> g = new Grid<>("", ljk);
		g.addColumn(jk->{
			return jk.getKelasPerkuliahan().getMataKuliah();
		}).setCaption("MATAKULIAH");
		g.addColumn(jk->{
			return jk.getKelasPerkuliahan().getDosenPengampu();
		}).setCaption("MATAKULIAH");
		
		
		//tabel.setVisibleColumns("hari","waktuMulai","waktuBerakhir","ruangan","matkul","dosen");
		panel.setContent(g);
		content.addComponent(panel);
	}
	
	private void siapkanEditorJadwalKuliah(JadwalKuliah jk){
		AdministrasiEntryJadwalKuliah adjk = new AdministrasiEntryJadwalKuliah(jk);
		final Window win = new Window("Entry Log Perkuliahan");
		adjk.setParent(win);
		win.addCloseListener(e->{
			filter();
		});
		win.setContent(adjk);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		

	}
	
	

}
