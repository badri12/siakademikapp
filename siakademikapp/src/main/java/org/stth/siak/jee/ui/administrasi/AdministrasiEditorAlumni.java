package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RiwayatPekerjaan;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.util.CaseUtil;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiEditorAlumni extends Window{
	private static final long serialVersionUID = 1L;
	private Mahasiswa m;
	private RiwayatPekerjaan rp;
	private Binder<Mahasiswa> bindMahasiswa ;
	private Binder<RiwayatPekerjaan> bindRiwayat;
	private Panel pRiwayat;
	private ComboBox<StatusMahasiswa> status;
	private DosenKaryawan user;
	private TextArea judulSkripsi;
	private List<RiwayatPekerjaan> lRP;
	private AbstractField<Boolean> cekBekerja;


	public AdministrasiEditorAlumni(Mahasiswa m ) {
		setCaption("Data Alumni");
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		if (m!=null) {
			this.m=m;
		}
		setModal(true);
		setWidth("600px");
		setCaption("Edit Data Alumni");
		Panel p = new Panel();
		p.setContent(buildLayout());
		setContent(p);
		center();
	}
	private Component buildLayout(){
		bindMahasiswa = new Binder<>(Mahasiswa.class);
		bindMahasiswa.setBean(m);

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		Panel p = new Panel("Mahasiswa");
		FormLayout flkiri = new FormLayout();
		FormLayout flKanan= new FormLayout();
		TextField nama = new TextField("Nama");
		TextField nim = new TextField("NIM");
		TextField prodi = new TextField("prodi");
		prodi.setValue(m.getProdi().toString());
		prodi.setReadOnly(true);
		TextArea alamat = new TextArea("Alamat Rumah");
		TextField noHP= new TextField("No Handphone");
		TextField email = new TextField("Email");
		TextField ttl = new TextField("TTL sesuai Ijazah");
		DateField tanggalLulus = new DateField("Lulus");
		status = new ComboBox<>("Status",  Arrays.asList(StatusMahasiswa.values()));
		status.setValue(m.getStatus());
		TextArea noSeriIjazah = new TextArea("No Seri Ijazah");

		bindMahasiswa.bind(status, "status");

		bindMahasiswa.bind(nama, "nama");
		bindMahasiswa.bind(nim, "npm");
		bindMahasiswa.bind(alamat, "alamat");
		bindMahasiswa.bind(noHP, "nomorHP");
		bindMahasiswa.bind(email, "email");
		bindMahasiswa.bind(ttl, "ttldiIjazah");
		bindMahasiswa.bind(tanggalLulus, "tanggalLulus");
		bindMahasiswa.bind(noSeriIjazah, "noSeriIjazah");

		flkiri.addComponents(nama, nim, prodi,  status, alamat);
		flKanan.addComponents(noHP, email, ttl,   tanggalLulus, noSeriIjazah);
		judulSkripsi = new TextArea("Judul Skripsi");
		Button toUpperCase = new Button("To UpperCase First Letter");
		toUpperCase.addClickListener(e-> {
			judulSkripsi.setValue(CaseUtil.upperfirsLetter(judulSkripsi.getValue()));
		});
		bindMahasiswa.bind(judulSkripsi, "judulSkripsi");

		judulSkripsi.setWidth("500px");

		VerticalLayout vlJudul = new VerticalLayout();
		vlJudul.addComponents(judulSkripsi, toUpperCase);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponents(flkiri, flKanan);
		VerticalLayout vlMahasiswa = new VerticalLayout();
		vlMahasiswa.addComponents(hl, vlJudul);
		vlMahasiswa.setMargin(true);
		p.setContent(vlMahasiswa);

		vl.addComponent(p);
		HorizontalLayout hlCek = new HorizontalLayout();
		hlCek.setMargin(true);
		cekBekerja = new CheckBox("Sudah Bekerja");
		cekBekerja.addValueChangeListener(v->{
			if(cekBekerja.getValue()){
				pRiwayat.setEnabled(true);
			}else{
				pRiwayat.setEnabled(false);
			}

		});
		vl.addComponent(cekBekerja);
		vl.addComponent(buildLayoutPekerjaan());
		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(klik->{
			m.setUpdateOleh(user);
			GenericPersistence.merge(m);
			if (cekBekerja.getValue()) {
				GenericPersistence.merge(rp);
				System.out.println(cekBekerja.getValue());
			}else{
				if(lRP.size()>0){
					GenericPersistence.delete(rp);
				}	
			}
			Notification.show("Data berhasil disimapn", Notification.Type.HUMANIZED_MESSAGE);

		});
		vl.addComponent(simpan);
		return vl;

	}
	private Component buildLayoutPekerjaan(){
		Map<String, Object> c = new HashMap<>();
		c.put(new String("mahasiswa"), m);
		pRiwayat = new Panel("Riwayat Pekerjaan");
		lRP = GenericPersistence.findList(RiwayatPekerjaan.class, c);
		if(lRP.size()>0){
			rp = lRP.get(0);
			pRiwayat.setEnabled(true);
			cekBekerja.setValue(true);
		}else{
			pRiwayat.setEnabled(false);
			rp=new RiwayatPekerjaan();

		}
		rp.setMahasiswa(m);
		
		bindRiwayat= new Binder<>(RiwayatPekerjaan.class);
		bindRiwayat.setBean(rp);


		TextField instansi = new TextField("Instansi");
		TextArea alamatInstansi = new TextArea("Alamat Instansi");
		ComboBox<String> cbLamaMenunggu = new ComboBox<>("Lama Menunggu Pekerjaan");
		cbLamaMenunggu.setItems("< 6 Bulan", "6-12 bulan", ">12 bulan");
		ComboBox<String> sumberInformasi = new ComboBox<>("Sumber Informasi Pekerjaan");

		List<String> itemIds = new ArrayList<>();
		itemIds.add("Media Cetak");
		itemIds.add("Teman");
		itemIds.add("Media Elektronik");
		itemIds.add("Almamater/ Fakultas");
		itemIds.add("Orang tua/ Saudara");
		itemIds.add("Lainnya");
		sumberInformasi.setItems(itemIds);

		bindRiwayat.bind(instansi, "instansi");
		bindRiwayat.bind(alamatInstansi, "alamatInstansi");
		bindRiwayat.bind(cbLamaMenunggu, "lamaMenungguKerja");
		bindRiwayat.bind(sumberInformasi, "sumberPekerjaan");
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		flKiri.addComponents(instansi, alamatInstansi);
		flKanan.addComponents(cbLamaMenunggu, sumberInformasi);
		HorizontalLayout hl =new HorizontalLayout();
		hl.addComponents(flKiri, flKanan);
		pRiwayat.setContent(hl);

		return pRiwayat;
	}
}
