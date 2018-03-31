package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisKelamin;
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

public class AdministrasiEditorDataDosen extends CustomComponent{
	private static final long serialVersionUID = -5134947009911467672L;
	private DosenKaryawan dosen;
	private TextArea alamatRumah;
	private ComboBox<String> agama;
	private ComboBox<String> jenjangPendTerakhir;
	private ComboBox<String> status;
	private ComboBox<JenisKelamin> jenisKelamin;
	private Binder<DosenKaryawan> bind;
	private DosenKaryawan user;
	private ComboBox<ProgramStudi> prodi = new ComboBox<>("Pilih prodi");
	private TextField nama;
	private TextField alias;
	private TextField nidn;
	private TextField nis;
	private DateField tanggalLahir;
	private TextField tempatLahir;
	private TextField thnMasuk;
	private TextField nomorTelepon;
	private TextField email;
	private TextField nomorKtp;
	private TextField prodiPendTerakhir;
	private TextField institusiPendTerakhir;
	/**
	 * Open editor for Mahasiswa instance
	 * @param dosen if creating new record, use new Mahasiswa()
	 */

	public AdministrasiEditorDataDosen(DosenKaryawan dosen) {
		VerticalLayout vl = new VerticalLayout();
		if (dosen==null) {
			dosen = new DosenKaryawan();
		}
		this.dosen = dosen;
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setCompositionRoot(vl);
		vl.setMargin(true);
		Responsive.makeResponsive(this);
		siapkanPilihanProdi();
		vl.addComponent(buildForm());
	}

	private void siapkanPilihanProdi(){
		prodi.setItems(GenericPersistence.findList(ProgramStudi.class)); 
	}

	private Component buildForm() {
		//VerticalLayout root = new VerticalLayout();
		Panel pnl = new Panel("Data Dosen");
		Panel pnlW = new Panel("Data Pendidikan Terakhir");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(false);

		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hlW = new HorizontalLayout();
		hlW.setSpacing(true);
		hlW.setMargin(true);
		hl.setSpacing(true);
		FormLayout fl1 = new FormLayout(),fl2 = new FormLayout();
		hl.addComponents(fl1,fl2);
		vl.addComponent(hl);
		vl.addComponent(pnlW);
		pnl.setContent(vl);
		pnlW.setContent(hlW);

		bind = new Binder<>(DosenKaryawan.class);
		bind.setBean(dosen);

		nama = new TextField("Nama");
		//tfNama.addValidator(new StringLengthValidator("Minimal 3 karakter", 3, null, false));
		alias = new TextField("Alias");
		nidn = new TextField("NIDN");
		nis = new TextField("NIS");
		tanggalLahir = new DateField("Tanggal Lahir");
		tempatLahir = new TextField("Tempat Lahir");
		jenisKelamin = new ComboBox<>("Jenis Kelamin", Arrays.asList(JenisKelamin.values()));
		agama = new ComboBox<>("Agama", Arrays.asList(GeneralUtilities.AGAMA));
		nomorTelepon = new TextField("Telepon");
		email = new TextField("Email");
		thnMasuk = new TextField("Tahun Masuk");
		nomorKtp = new TextField("Nomor KTP");
		status = new ComboBox<>("Status Dosen",Arrays.asList(new String[] {"TETAP","TIDAK TETAP"}));
		alamatRumah = new TextArea("Alamat");
		//bind.bind(tfNama, "nama");

		fl1.addComponent(nama);
		fl1.addComponent(nidn);
		fl1.addComponent(tanggalLahir);
		fl1.addComponent(jenisKelamin);
		fl1.addComponent(prodi);
		fl1.addComponent(nomorTelepon);
		fl1.addComponent(nomorKtp);
		fl1.addComponent(status);

		fl2.addComponent(alias);
		fl2.addComponent(nis);
		fl2.addComponent(tempatLahir);
		fl2.addComponent(agama);
		fl2.addComponent(email);
		fl2.addComponent(thnMasuk);
		fl2.addComponent(alamatRumah);

		jenjangPendTerakhir = new ComboBox<>("Jenjang", Arrays.asList(new String[]{"S1","S2","S3"}));
		prodiPendTerakhir = new TextField("Program Studi");
		institusiPendTerakhir = new TextField("Program Studi");
		
		
		hlW.addComponents(jenjangPendTerakhir, prodiPendTerakhir, institusiPendTerakhir);
		
		bind.forMemberField(nama).asRequired("Masukkan nama");
		bind.forMemberField(thnMasuk).withConverter(new StringToIntegerConverter("Masukkan angka"));
		
		bind.bindInstanceFields(this);

		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener( e->{
			save();
		});
		vl.addComponent(simpan);
		return pnl;
	}
	private void save() {
		if (bind.validate().isOk()) {
			DosenKaryawan d =  bind.getBean();
			d.setUpdateOleh(user);
			d.setDosen(true);
			GenericPersistence.merge(d);
			Notification.show("Perubahan data berhasil dilakukan", Notification.Type.HUMANIZED_MESSAGE);
		}

	}


}
