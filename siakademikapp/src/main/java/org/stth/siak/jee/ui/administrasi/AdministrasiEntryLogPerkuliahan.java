package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.Semester;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiEntryLogPerkuliahan extends CustomComponent {
	private static final long serialVersionUID = -7922111458645927405L;
	private Semester semester;
	private String ta;
	private VerticalLayout root = new VerticalLayout();
	private ComboBox<DosenKaryawan> cbDosen = new ComboBox<>("Dosen Pengampu");
	private ComboBox<KelasPerkuliahan> cbMataKuliah = new ComboBox<>("Mata Kuliah");
	private ComboBox<DosenKaryawan> cbDosenPengganti = new ComboBox<>("Dosen Pengganti");
	private DosenKaryawan dosen;
	private KelasPerkuliahan kelasPerkuliahan;
	private Binder<LogPerkuliahan> binder;
	private TextField textProdi= new TextField("Program Studi");
	private TextField kodeKelas= new TextField("Kelas");
	private Window parent;
	private DosenKaryawan user;

	public AdministrasiEntryLogPerkuliahan() {
		LogPerkuliahan log = new LogPerkuliahan();
		prepare(log);

	}
	public AdministrasiEntryLogPerkuliahan( LogPerkuliahan log) {
		prepare(log);
	}
	public void setParent(final Window parent){
		this.parent = parent;
	}

	private void prepare(LogPerkuliahan log){
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		binder=new Binder<>(LogPerkuliahan.class);
		binder.setBean(log);
		if (log.getId()>0) {
			kelasPerkuliahan=log.getKelasPerkuliahan();
			cbDosen.setValue(log.getDiisiOleh());
			textProdi.setValue(kelasPerkuliahan.getProdi().toString());
			textProdi.setEnabled(false);
			kodeKelas.setValue(kelasPerkuliahan.getKodeKelas());
			kodeKelas.setEnabled(false);
		}
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
		ok.setIcon(VaadinIcons.FILE_TEXT);
		ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(e->{
			if (binder.validate().isOk()) {
				LogPerkuliahan log = binder.getBean();
				saveLog(log);
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(ok);
		root.addComponent(hl);		
	}

	private void siapkanDaftarDosen(){
		cbDosen.setItems(DosenKaryawanPersistence.getDosen());
		cbDosenPengganti.setItems(DosenKaryawanPersistence.getDosen());
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
			kelasPerkuliahan =e.getValue();
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
		DateTimeField waktu = new DateTimeField("Waktu Pertemuan");
		TextField lokasi = new TextField("Ruangan");
		lokasi.setWidth(dw);
		CheckBox isOlehDosen = new CheckBox("Diisi oleh dosen bersangkutan");
		isOlehDosen.setValue(true);
		isOlehDosen.addValueChangeListener(e->{
			if (!e.getValue()) {
				siapkanPilihanDosenPengganti();
			}else{
				cbDosenPengganti.setEnabled(false);
			}
		});
		cbDosenPengganti.setEnabled(false);
		cbDosenPengganti.setWidth(dw);
		TextArea textMateri = new TextArea("Materi");
		textMateri.setWidth(dw);

		binder.forField(cbMataKuliah).asRequired("Pilih matakuliah").bind("kelasPerkuliahan");
		binder.forField(waktu).asRequired("Waktu tidak boleh kosong").bind("tanggalPertemuan");
		binder.forField(lokasi).asRequired("Isi dengan ruangan/kelas").bind("ruangPertemuan");
		binder.bind(isOlehDosen, "olehDosen");
		binder.bind(cbDosenPengganti, "diisiOleh");
		binder.forField(textMateri).asRequired("Isi materi kuliah").bind("materiPertemuan");

		fl.addComponent(cbDosen);
		fl.addComponent(cbMataKuliah);
		fl.addComponent(textProdi);
		fl.addComponent(kodeKelas);
		fl.addComponent(waktu);
		fl.addComponent(lokasi);
		fl.addComponent(isOlehDosen);
		fl.addComponent(cbDosenPengganti);
		fl.addComponent(textMateri);

		root.setMargin(true);
		root.setSizeUndefined();
		root.addComponent(fl);
		setCompositionRoot(root);

	}
	protected void siapkanPilihanDosenPengganti() {
		cbDosenPengganti.setEnabled(true);


	}
	private void saveLog(LogPerkuliahan log) {
		List<LogPerkuliahan> existedLogs = LogPerkuliahanPersistence.getLogSimilarOnDate(log);
		if (existedLogs.size()==0){
			log.setEntryOleh(user);
			GenericPersistence.merge(log);
			parent.close();
		} else {
			if (existedLogs.get(0).getId()==log.getId()) {
				log.setEntryOleh(user);
				GenericPersistence.merge(log);
				parent.close();
			}else{
				Notification.show("Data perkuliahan di hari yang sama untuk kelas bersangkutan sudah ada", Notification.Type.ERROR_MESSAGE);
			}
		}

	}

}
