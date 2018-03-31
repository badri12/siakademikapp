package org.stth.siak.jee.ui.administrasi;


import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.DaftarDosenView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiDataDosen extends VerticalLayout implements View{
	private static final long serialVersionUID = -5660284900239030833L;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	protected ProgramStudi prodi;
	private VerticalLayout content = new VerticalLayout();
	private TextField tfNama;
	

	public AdministrasiDataDosen() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Administrasi Data Dosen"));
		Button add = new Button("Tambah");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(klik->{
			AdministrasiEditorDataDosen ae = new AdministrasiEditorDataDosen(null);
			GeneralPopups.showGenericWindow(ae, "Edit Data Dosen");
		});
		addComponent(add);
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	private void siapkanPilihanProdi(){
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class)); 
		cbProdi.addValueChangeListener(e->{
			prodi = e.getValue();
		});
	}
	
	private Component createFilterComponent() {
		Panel pnl = new Panel("Cari mahasiswa");
		GridLayout gl = new GridLayout(3, 1);
		//gl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		tfNama = new TextField("Nama");
		flKiri.setSpacing(true);
		flKanan.setSpacing(true);
		
		Button buttonFilter = new Button("Cari");
		buttonFilter.setIcon(VaadinIcons.SEARCH);
		buttonFilter.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonFilter.addClickListener(e->{
			filter();
		});
		flKiri.addComponents(tfNama);
		flKanan.addComponents(cbProdi);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flKanan, 1, 0);
		gl.addComponent(buttonFilter, 2, 0);
		gl.setComponentAlignment(buttonFilter, Alignment.MIDDLE_LEFT);
		pnl.setContent(gl);
		return pnl;
	}
	
	
	protected void filter() {
		DosenKaryawan example = new DosenKaryawan();
		example.setNama(tfNama.getValue());
		example.setProdi(prodi);
		content.removeAllComponents();
		DosenKaryawanPersistence.getDosenByExample(example);
		List<DosenKaryawan> kr = DosenKaryawanPersistence.getDosenByExample(example);
		int visColumns = DaftarDosenView.NIDN
				| DaftarDosenView.NAMA 
				| DaftarDosenView.PRODI 
				| DaftarDosenView.TGL_LAHIR
				| DaftarDosenView.TEMPAT_LAHIR
				//| DaftarMahasiswaView.DOSEN_PA
				;
		int allowedActions = DaftarDosenView.HAPUS
				| DaftarDosenView.EDIT;
		DaftarDosenView dafv = new DaftarDosenView(kr,visColumns 
				,allowedActions);
		Panel panel = new Panel("Hasil Pencarian");
		panel.setContent(dafv);
		content.addComponent(panel);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		

	}
	
	

}
