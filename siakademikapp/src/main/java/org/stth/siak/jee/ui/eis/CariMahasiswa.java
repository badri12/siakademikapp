package org.stth.siak.jee.ui.eis;


import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CariMahasiswa extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosenPA;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private ComboBox<DosenKaryawan> cbPA = new ComboBox<>("Pilih Dosen P.A");
	protected ProgramStudi prodi;
	private List<DosenKaryawan> lpa;
	private TextField tfNama;
	private TextField tfNimStart;
	private TextField tfAngkatan;
	private Panel panel = new Panel("Hasil Pencarian");

	public CariMahasiswa() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Pencarian Data Mahasiswa"));
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		siapkanPilihanPA();
		addComponent(panel);
		addComponent(ViewFactory.footer());

	}
	private void siapkanPilihanProdi(){
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class));; 
		//cbProdi.setNullSelectionAllowed(false);
		//cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(e->{
			prodi = e.getValue();
		});
	}
	private void siapkanPilihanPA(){
		lpa = DosenKaryawanPersistence.getDosen();
		cbPA.setItems(lpa);
		cbPA.addValueChangeListener(e->{
			dosenPA = e.getValue();
		});
	}

	
	private Component createFilterComponent() {
		Panel pnl = new Panel("Cari mahasiswa");
		GridLayout gl = new GridLayout(3, 2);
		gl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		tfNama = new TextField("Nama");
		tfNimStart = new TextField("Nim mulai");
		tfAngkatan = new TextField("Angkatan");
		flKiri.setSpacing(true);
		flKanan.setSpacing(true);
		
		Button buttonFilter = new Button("Cari");
		buttonFilter.setIcon(VaadinIcons.SEARCH);
		buttonFilter.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonFilter.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				filter();
			}
			
		});
		flKiri.addComponents(tfNama,tfNimStart,cbProdi);
		flKanan.addComponents(tfAngkatan,cbPA,buttonFilter);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flKanan, 1, 0);
		gl.setSpacing(true);
		pnl.setContent(gl);
		return pnl;
	}
	
	
	protected void filter() {
		Mahasiswa example = new Mahasiswa();
		example.setNpm(tfNimStart.getValue());
		example.setNama(tfNama.getValue());
		if (!tfAngkatan.getValue().isEmpty()) {
			example.setAngkatan(Integer.parseInt(tfAngkatan.getValue()));
		}
		example.setProdi(prodi);
		example.setPembimbingAkademik(dosenPA);
		List<Mahasiswa> kr = MahasiswaPersistence.getListByExample(example);
		DaftarMahasiswaView dafv = new DaftarMahasiswaView(kr);
		panel.setContent(dafv);
	}
	@Override
	public void enter(ViewChangeEvent event) {


	}
	
	

}
