package org.stth.siak.jee.ui.administrasi;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ExportToExcel;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiCetakTranskripMahasiswa extends VerticalLayout implements View{

	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosenPA;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private ComboBox<DosenKaryawan> cbPA = new ComboBox<>("Pilih Dosen P.A");
	private ComboBox<StatusMahasiswa> cbStatus = new ComboBox<>("Status", Arrays.asList(StatusMahasiswa.values()));
	protected ProgramStudi prodi;
	private List<DosenKaryawan> lpa;
	private VerticalLayout content = new VerticalLayout();
	private StatusMahasiswa statusM;
	private TextField tfNama;
	private TextField tfNimStart;
	private ComboBox<Integer> tfAngkatan;
	private List<Mahasiswa> kr;
	private ComboBox<StatusMasuk> cbStatusMasuk;
	private HorizontalLayout hlAksi;
	private Button rekap;
	private DaftarMahasiswaView dafv;

	public AdministrasiCetakTranskripMahasiswa() {
		Responsive.makeResponsive(this);
		//addComponent(ViewFactory.header("Cetak Transkrip Mahasiswa"));
		addComponent(ViewFactory.header("Administrasi Data Mahasiswa"));
		createButtonAksi();
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		siapkanPilihanPA();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	private void createButtonAksi() {
		hlAksi = new HorizontalLayout();
		Button add = new Button("");
		add.setDescription("Tambah data mahasiswa");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(klik->{
			AdministrasiEditorDataMahasiswa ae = new AdministrasiEditorDataMahasiswa(null);
			GeneralPopups.showGenericWindow(ae, "Edit Data Mahasiswa");
		});
		hlAksi.addComponent(add);
		rekap = new Button("Rekapitulasi Nilai");
		rekap.setIcon(VaadinIcons.CLIPBOARD_TEXT);
		rekap.setStyleName(ValoTheme.BUTTON_PRIMARY);
		rekap.addClickListener(e->{
			if (dafv.getMhs().size()>0) {
				try {
					StreamResource source =ExportToExcel.createFileExcel(ExportToExcel.rekapitulasiNilai(dafv.getMhs()), 
							cbProdi.getValue()+" "+tfAngkatan.getValue());
					ResourceReference ref = new ResourceReference(source, this, "rekap"+source.hashCode());
				    this.setResource("rekap"+source.hashCode(), source);
				    getUI().getPage().open(ref.getURL(), null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else Notification.show("silahkan pilih mahasiswa");
			
			
		});
		addComponent(hlAksi);
	}
	private void siapkanPilihanProdi(){
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class)); 
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
		GridLayout gl = new GridLayout(4, 2);
		gl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flMid = new FormLayout();
		FormLayout flKanan = new FormLayout();
		tfNama = new TextField("Nama");
		tfNimStart = new TextField("Nim mulai");
		tfAngkatan = new ComboBox<>("Angkatan");

		tfAngkatan.setItems(GeneralUtilities.getListAngkatan());
		cbStatus.addValueChangeListener(e->{
			statusM = e.getValue();
		});
		flKiri.setSpacing(true);
		flKanan.setSpacing(true);

		Button buttonFilter = new Button("Cari");
		buttonFilter.setIcon(VaadinIcons.SEARCH);
		buttonFilter.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonFilter.addClickListener(e->{
			hlAksi.removeComponent(rekap);
			if (tfAngkatan.getValue()!=null&&cbProdi.getValue()!=null) {
				hlAksi.addComponent(rekap);
			}
			filter();
		});

		cbStatusMasuk = new ComboBox<>("Status Masuk", Arrays.asList(StatusMasuk.values()));
		flKiri.addComponents(tfNama,tfNimStart);
		flMid.addComponents(cbProdi, tfAngkatan);
		flKanan.addComponents(cbPA, cbStatus);
		FormLayout flButton  = new FormLayout();
		flButton.addComponents(cbStatusMasuk, buttonFilter);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flMid, 1, 0);
		gl.addComponent(flKanan, 2, 0);
		gl.addComponent(flButton, 3,0);
		gl.setSpacing(true);
		pnl.setContent(gl);
		return pnl;
	}


	protected void filter() {
		Mahasiswa example = new Mahasiswa();
		example.setNpm(tfNimStart.getValue());
		example.setNama(tfNama.getValue());
		if (tfAngkatan.getValue()!=null) {
			example.setAngkatan(tfAngkatan.getValue());
		}
		example.setProdi(prodi);
		example.setPembimbingAkademik(dosenPA);
		example.setStatus(statusM);
		example.setStatusMasuk(cbStatusMasuk.getValue());
		content.removeAllComponents();
		kr = MahasiswaPersistence.getListByExample(example);
		int visColumns = DaftarMahasiswaView.NIM 
				| DaftarMahasiswaView.NAMA 
				| DaftarMahasiswaView.PRODI 
				//| DaftarMahasiswaView.TGL_LAHIR
				//| DaftarMahasiswaView.TEMPAT_LAHIR
				| DaftarMahasiswaView.DOSEN_PA
				;
		int allowedActions = DaftarMahasiswaView.LIHAT_PROFIL | DaftarMahasiswaView.EDIT
				| DaftarMahasiswaView.CETAK_TRANSKRIP;
		dafv = new DaftarMahasiswaView(kr, visColumns, allowedActions, true);
		//		Panel panel = new Panel("Hasil Pencarian");
		//		panel.setContent(dafv);
		content.setMargin(false);
		content.addComponent(dafv);
	}
	@Override
	public void enter(ViewChangeEvent event) {

	}


}
