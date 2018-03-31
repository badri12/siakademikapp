package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiEditorDataMahasiswa extends CustomComponent{


	private static final long serialVersionUID = 1744423645758750586L;
	private VerticalLayout vl = new VerticalLayout();
	private TextField tfNim,tfAngkatan, nama ;
	private TextArea taAlamat,taAlamatWali;
	private ComboBox<DosenKaryawan> cbDosenPa;
	private ComboBox<StatusMahasiswa> cbStatus;
	private ComboBox<ProgramStudi> cbProdi;
	private ComboBox<JenisKelamin> cbJenisKelamin;
	private ComboBox<String> cbAgama;
	private Binder<Mahasiswa> bind;
	private DosenKaryawan user;
	private Mahasiswa m;
	private List<Kurikulum> lk;
	private ComboBox<Kurikulum> cbKur;
	/**
	 * Open editor for Mahasiswa instance
	 * @param mahasiswa if creating new record, use new Mahasiswa()
	 */

	public AdministrasiEditorDataMahasiswa(Mahasiswa mahasiswa) {
		if (mahasiswa==null) {
			mahasiswa=new Mahasiswa();
			mahasiswa.setStatus(StatusMahasiswa.AKTIF);
			mahasiswa.setAngkatan(GeneralUtilities.getCurrentYearLocal());
		}
		m=mahasiswa;
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setCompositionRoot(vl);
		vl.setMargin(true);
		vl.setSpacing(true);
		Responsive.makeResponsive(this);
		bind= new Binder<>(Mahasiswa.class);
		bind.setBean(m);
		vl.addComponent(buildForm());
	}

	private Component buildForm() {
		FormLayout fl1 = new FormLayout();
		FormLayout fl2 = new FormLayout();

		FormLayout fl3 = new FormLayout();
		FormLayout fl4 = new FormLayout();

		tfNim = new TextField("Nomor Induk");
		//tfNim.setReadOnly(true);
		//tfNim.addValidator(new StringLengthValidator("minimal 7 karakter", 7, null, false));
		DateField df = new DateField("Tanggal Lahir");
		cbJenisKelamin = new ComboBox<>("Jenis Kelamin", Arrays.asList(JenisKelamin.values()));
		cbProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		cbProdi.addValueChangeListener(e->{
			lk=KurikulumPersistence.getListByProdi(cbProdi.getValue());
			cbKur.setItems(lk);
		});
		cbStatus = new ComboBox<>("Status", Arrays.asList(StatusMahasiswa.values()));
		TextField tfAsalSekolah = new TextField("Asal Sekolah");
		taAlamat = new TextArea("Alamat");
		nama= new TextField("Nama");
		TextField tmptLahir= new TextField("Tempat Lahir");
		cbAgama = new ComboBox<>("Agama", Arrays.asList(GeneralUtilities.AGAMA));
		tfAngkatan = new TextField("Angkatan");
		cbDosenPa = new ComboBox<>("Pembimbing Akademik", DosenKaryawanPersistence.getDosen());
		TextField tfTelpon = new TextField("Nomor Telepon");
		ComboBox<StatusMasuk> cbStatusMasuk = new ComboBox<>("Status Masuk", Arrays.asList(StatusMasuk.values()));
		TextField tfEmail = new TextField("e-mail");
		cbKur = new ComboBox<>("Kurikulum");
		
		
		bind.forField(tfNim).asRequired("Masukkan nim").bind("npm");
		bind.bind(df, "tanggalLahir");
		bind.forField(cbJenisKelamin).asRequired("Pilih jenis kelamin").bind("jenisKelamin");
		bind.forField(cbProdi).asRequired("Pilih prodi").bind("prodi");
		bind.forField(cbStatus).asRequired("pilih status").bind("status");
		bind.bind(tfAsalSekolah, "asalSekolah");
		bind.bind(taAlamat, "alamat");
		bind.forField(nama).asRequired("Masukkan nama").bind("nama");
		bind.bind(tmptLahir, "tempatLahir");
		bind.bind(cbAgama, "agama");
		bind.forField(tfAngkatan).asRequired("Masukkan angkatan").withConverter(new StringToIntegerConverter("Mohon masukkan angka")).bind("angkatan");
		bind.forField(cbDosenPa).asRequired("pilih dosen P.A").bind("pembimbingAkademik");
		bind.bind(tfTelpon, "nomorHP");
		bind.forField(cbStatusMasuk).asRequired("Pilih status masuk").bind("statusMasuk");
		bind.bind(tfEmail, "email");
		bind.forField(cbKur).asRequired("Pilih kurikulum").bind("k");

		fl1.addComponent(tfNim);
		fl1.addComponent(df);
		fl1.addComponent(cbJenisKelamin);
		fl1.addComponent(cbProdi);
		fl1.addComponent(cbStatus);
		fl1.addComponent(tfAsalSekolah);
		fl1.addComponent(taAlamat);

		fl2.addComponent(nama);
		fl2.addComponent(tmptLahir);
		fl2.addComponent(cbAgama);
		fl2.addComponent(tfAngkatan);
		fl2.addComponent(cbDosenPa);
		fl2.addComponent(tfTelpon);
		fl2.addComponent(cbStatusMasuk);
		fl2.addComponent(tfEmail);
		fl2.addComponent(cbKur);

		TextField tfNamaWali = new TextField("Nama Wali");
		TextField tfNoWali = new TextField("Nomor telepon Wali");
		taAlamatWali = new TextArea("Alamat Wali");

		bind.bind(tfNamaWali, "wali");
		bind.bind(tfNoWali, "nomorHPWali");
		bind.bind(taAlamatWali, "alamatWali");

		fl3.addComponent(tfNamaWali);
		fl3.addComponent(tfNoWali);
		fl4.addComponent(taAlamatWali);

		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(e-> {
			simpan();

		});

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(fl1,fl2);

		HorizontalLayout hlW = new HorizontalLayout();
		hlW.addComponents(fl3, fl4);

		Panel pnl = new Panel("Data Mahasiswa");
		Panel pnlW = new Panel("Data Wali");

		pnl.setContent(hl);
		pnlW.setContent(hlW);

		VerticalLayout vl = new VerticalLayout();

		vl.addComponents(pnl, pnlW, simpan);
		return vl;
	}

	private void simpan() {
		String nim = tfNim.getValue();
		List<Mahasiswa> l = MahasiswaPersistence.getByNim(nim);
		System.out.println(l.size());
		if (l.size()>1) {
			batalSimpan();
		}else if (l.size()>0) {
			if (m.getId()>0) {
				if (m.getId()==l.get(0).getId()) {
					save();
				}else{
					batalSimpan();
				}
			}else{
				batalSimpan();
			}

		}else{
			save();
		}

	}

	private void batalSimpan() {
		Notification.show("Gunakan NIM yang lain \n"
				+ "NIM ini sudah dipakai", Notification.Type.ERROR_MESSAGE);
	}

	private void save() {
		if (bind.validate().isOk()) {
			Mahasiswa m =  bind.getBean();
			m.setUpdateOleh(user);
			GenericPersistence.merge(m);
			Notification.show("Data berhasil Simpan", Notification.Type.HUMANIZED_MESSAGE);
		}

	}

}
