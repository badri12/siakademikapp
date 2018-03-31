package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.JadwalKuliahPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Hari;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.enumtype.Semester;
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdministrasiEntryJadwalKuliah extends CustomComponent {
	private static final long serialVersionUID = -8062480039899293414L;
	private Semester semester;
	private String ta;
	private VerticalLayout root = new VerticalLayout();
	private ComboBox<DosenKaryawan> cbDosen = new ComboBox<>("Dosen Pengampu");
	private ComboBox<KelasPerkuliahan> cbMataKuliah = new ComboBox<>("Mata Kuliah");
	private ComboBox<DosenKaryawan> cbDosenPengganti = new ComboBox<>("Dosen Pengganti");
	private DosenKaryawan dosen;
	private KelasPerkuliahan kelasPerkuliahan;
	private Binder<JadwalKuliah> binder ;
	private TextField textProdi= new TextField("Program Studi");
	private TextField kodeKelas= new TextField("Kelas");
	private Window parent;
	private DosenKaryawan user;
	private TextField tfMulai;
	private TextField tfBerakhir;


	public AdministrasiEntryJadwalKuliah() {
		JadwalKuliah log = new JadwalKuliah();
		prepare(log);

	}
	public AdministrasiEntryJadwalKuliah( JadwalKuliah jk) {
		prepare(jk);
	}
	public void setParent(final Window parent){
		this.parent = parent;
	}

	private void prepare(JadwalKuliah log){

		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		binder = new Binder<>(JadwalKuliah.class);
		cbDosenPengganti.setEnabled(false);
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		//cbDosenPengganti.set
		siapkanDaftarDosen();
		siapkanFormIsian();
		siapkanTombolAksi();
	}

	private void siapkanTombolAksi(){
		Button ok;
		ok = new Button("OK");
		ok.addClickListener(e->{
			JadwalKuliah jadwal = binder.getBean();
			saveLog(jadwal);
		});

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(ok);
		root.addComponent(hl);		
	}

	private void siapkanDaftarDosen(){
		List<DosenKaryawan> ldk =DosenKaryawanPersistence.getDosen();
		cbDosen.setItems(ldk);
		cbDosenPengganti.setItems(ldk);
		cbDosen.addValueChangeListener(e->{
			dosen = e.getValue();
			cbDosenPengganti.setSelectedItem(dosen);
			siapkanDaftarKelas();
		});
	}

	private void siapkanDaftarKelas() {
		
		List<KelasPerkuliahan> lKelas = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		
		cbMataKuliah.setItems(lKelas);		
		cbMataKuliah.addValueChangeListener(e->{
			kelasPerkuliahan = e.getValue();
			textProdi.setValue(kelasPerkuliahan.getProdi().toString());
			textProdi.setEnabled(false);
			kodeKelas.setValue(kelasPerkuliahan.getKodeKelas());
			kodeKelas.setEnabled(false);
		});
	}

	private void siapkanFormIsian(){
		FormLayout fl = new FormLayout();
		String dw = "300px";

		cbDosen.setWidth(dw);
		cbMataKuliah.setWidth(dw);
		textProdi.setWidth(dw);
		kodeKelas.setWidth(dw);

		binder.bind(cbMataKuliah, "kelasPerkuliahan");
		fl.addComponent(cbDosen);
		fl.addComponent(cbMataKuliah);
		fl.addComponent(textProdi);
		fl.addComponent(kodeKelas);

		
		ComboBox<Hari> cbHari = new ComboBox<>("Hari", Arrays.asList(Hari.values()));
		
		fl.addComponent(cbHari);
		binder.bind(cbHari, "hari");

//		Property<Integer> integerProperty = (Property<Integer>) item
//				.getItemProperty("waktuMulai");
		tfMulai = new TextField("Mulai");
		//tfMulai.addValidator(new NullValidator("Waktu mulai tidak boleh kosong", false));
		fl.addComponent(tfMulai);
		binder.bind(tfMulai, "waktuMulai");

//		Property<Integer> integerProperty2 = (Property<Integer>) item
//				.getItemProperty("waktuBerakhir");
		tfBerakhir = new TextField("Berakhir");
		//tfBerakhir.addValidator(new NullValidator("Waktu berakhir tidak boleh kosong", false));
		fl.addComponent(tfBerakhir);
		binder.bind(tfBerakhir, "waktuBerakhir");



		TextField lokasi = new TextField("Ruangan");
		//lokasi.addValidator(new NullValidator("Ruangan tidak boleh kosong", false));
		
		binder.bind(lokasi, "ruangan");
		lokasi.setWidth(dw);
		//lokasi.setNullRepresentation("Isi dengan ruangan/kelas");
		fl.addComponent(lokasi);


		root.setMargin(true);
		root.setSizeUndefined();
		root.addComponent(fl);
		setCompositionRoot(root);

	}
	protected void siapkanPilihanDosenPengganti() {
		cbDosenPengganti.setEnabled(true);


	}
	private void saveLog(JadwalKuliah jadwalKuliah) {
		List<JadwalKuliah> existedLogs = JadwalKuliahPersistence.getLogSimilarOnDate(jadwalKuliah);
		if (existedLogs.size()==0){
			jadwalKuliah.setEntryOleh(user);
			GenericPersistence.merge(jadwalKuliah);
			parent.close();
		} else {
			Notification.show("Data perkuliahan di hari yang sama untuk kelas bersangkutan sudah ada", Notification.Type.ERROR_MESSAGE);
		}

	}

}
