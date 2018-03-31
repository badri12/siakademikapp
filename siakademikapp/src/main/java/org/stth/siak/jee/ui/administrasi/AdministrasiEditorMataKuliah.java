package org.stth.siak.jee.ui.administrasi;


import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


public class AdministrasiEditorMataKuliah extends CustomComponent{
	private static final long serialVersionUID = -4451460212517987981L;
	private Binder<MataKuliah> binder;
	private TextField tfKode;
	private MataKuliah mk;
	private TextField tfNama;
	private TextField tfSKS;
	private TextField tfemester;
	public AdministrasiEditorMataKuliah(MataKuliah mk) {
		if (mk==null) {
			mk=new MataKuliah();
		}
		this.mk=mk;
		binder=new Binder<>(MataKuliah.class);
		binder.setBean(this.mk);
		setCompositionRoot(buildContent());

	}
	private FormLayout buildContent() {
		FormLayout fl = new FormLayout();
		tfKode = new TextField("Kode");
		tfNama = new TextField("Nama");
		tfSKS = new TextField("SKS");
		tfemester = new TextField("Semester");
		ComboBox<ProgramStudi> cbProdi=new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		CheckBox cbStatus = new CheckBox("Aktif");
		
		binder.forField(tfKode).asRequired("Masukkan kode matakuliah").bind("kode");
		binder.forField(tfNama).asRequired("Masukkan nama matakuliah").bind("nama");
		binder.forField(tfSKS).asRequired("Masukkan SKS").withConverter(new StringToIntegerConverter("Masukkan angka")).bind("sks");
		binder.forField(cbProdi).asRequired("Pilih program studi").bind("prodiPemilik");
		binder.bind(cbStatus, "aktif");
		binder.forField(tfemester).asRequired("")
		.withConverter(new StringToIntegerConverter("masukkan angka")).bind("semesterBuka");
		
		fl.addComponent(tfKode);
		fl.addComponent(tfNama);
		fl.addComponent(tfSKS);
		fl.addComponent(tfemester);
		fl.addComponent(cbProdi);
		fl.addComponent(cbStatus);

		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(ok->{
			simpan();

		});
		fl.addComponent(simpan);
		return fl;
	}
	private void simpan() {
		String kode = tfKode.getValue();
		List<MataKuliah> l = MataKuliahPersistence.getByKode(kode);
		if (l.size()>1) {
			batalSimpan();
		}else if (l.size()>0) {
			if (mk.getId()>0) {
				if (mk.getId()==l.get(0).getId()) {
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
	private void save() {
		if (binder.validate().isOk()) {
			MataKuliah mkbind=binder.getBean();
			GenericPersistence.merge(mkbind);
			Notification.show("Data berhasil Simpan", Notification.Type.HUMANIZED_MESSAGE);
		}

	}
	private void batalSimpan() {
		Notification.show("Gunakan kode yang lain \n"
				+ "Kode ini sudah dipakai", Notification.Type.ERROR_MESSAGE);
	}

}
