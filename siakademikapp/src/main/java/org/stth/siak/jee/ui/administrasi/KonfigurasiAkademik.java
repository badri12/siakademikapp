package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.Konfigurasi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class KonfigurasiAkademik extends VerticalLayout implements View{
	private static final long serialVersionUID = 415002847405284653L;
	
	public KonfigurasiAkademik() {
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		
		TextField tfMaxSKS = new TextField("KRS Maksimal SKS");
		tfMaxSKS.setValue(String.valueOf(k.getKRSMaxSKS()));
		
//		TextField tfKuota = new TextField("Kuota Kelas");
//		tfKuota.setValue(String.valueOf(k.get));
		
		TextField tfKRSTA = new TextField("Tahun Akademik KRS");
		tfKRSTA.setValue(k.getKRSTa());
		
		TextField tfTA = new TextField("Tahun Akademik Berjalan");
		tfTA.setValue(k.getCurrentTa());
		
		CheckBox cek = new CheckBox("KRS Open");
		cek.setValue(k.isKRSOpen());
		
		ComboBox<Semester> cbKRS = new ComboBox<>("KRS Semester", Arrays.asList(Semester.values()));
		cbKRS.setValue(k.getKRSSemester());
		
		ComboBox<Semester> cbTA = new ComboBox<>("Semester Berjalan", Arrays.asList(Semester.values()));
		cbTA.setValue(k.getCurrentSemester());
		
		FormLayout fl = new FormLayout();
		addComponent(ViewFactory.header("Administrasi Konfigurasi Akademik"));
		fl.addComponents(tfMaxSKS, tfTA,cbTA, tfKRSTA, cbKRS, cek);
		Button simpan = new Button("Simpan");
		List<Konfigurasi> lk = new ArrayList<>();
		simpan.setIcon(VaadinIcons.FILE);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(e->{
			
			if (!tfMaxSKS.getValue().isEmpty() || !tfTA.getValue().isEmpty() || !tfKRSTA.getValue().isEmpty()
					|| cbTA.getValue()!=null || cbKRS.getValue()!=null) {
				Konfigurasi kon = new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.SKS_MAX);
				kon.setNilaiKonfigurasi(tfMaxSKS.getValue());
				lk.add(kon);
				
				kon=new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.CUR_TA);
				kon.setNilaiKonfigurasi(tfTA.getValue());
				lk.add(kon);
				
				kon=new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.CUR_SEM);
				kon.setNilaiKonfigurasi(String.valueOf(cbTA.getValue()));
				lk.add(kon);
				
				kon=new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.KRS_TA);
				kon.setNilaiKonfigurasi(tfKRSTA.getValue());
				lk.add(kon);
				
				kon=new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.KRS_SEM);
				kon.setNilaiKonfigurasi(String.valueOf(cbKRS.getValue()));
				lk.add(kon);
				
				kon=new Konfigurasi();
				kon.setNamaKonfigurasi(Konfigurasi.KRS_OPEN);
				kon.setNilaiKonfigurasi(String.valueOf(cek.getValue()));
				lk.add(kon);
				
				for (Konfigurasi konfigurasi : lk) {
					k.setKonfigurasi(konfigurasi);
				}
				Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);
			}else Notification.show("Semua data harus diisi", Notification.Type.ERROR_MESSAGE);;
			
		});
		fl.addComponent(simpan);
		addComponent(fl);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
