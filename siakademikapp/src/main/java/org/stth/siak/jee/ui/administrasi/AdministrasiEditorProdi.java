package org.stth.siak.jee.ui.administrasi;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.ProgramStudi;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextField;

public class AdministrasiEditorProdi extends CustomComponent{
	private static final long serialVersionUID = -4050145678449510155L;
	private Binder<ProgramStudi> binder;

	public AdministrasiEditorProdi(ProgramStudi ps) {
		VerticalLayout vl = new VerticalLayout();
		FormLayout fl = new FormLayout();
		if (ps==null) {
			ps=new ProgramStudi();
		}
		binder = new Binder<>(ProgramStudi.class);
		binder.setBean(ps);
		
		TextField tfKode = new TextField("Kode");
		TextField tfNama = new TextField("Nama");
		TextField tfJenjang = new TextField("Jenjang");
		ComboBox<DosenKaryawan> cbKaprodi = new ComboBox<>("Kaprodi", DosenKaryawanPersistence.getDosen());
		//fg.bind(tfJenjang, "jenjang");
		
		binder.forField(tfKode).asRequired("Masukkan kode program studi").bind("kode");
		binder.forField(tfNama).asRequired("Masukkan nama program studi").bind("nama");
		binder.forField(tfJenjang).asRequired("Masukkan jenjang program studi").bind("jenjang");
		binder.forField(cbKaprodi).asRequired("Pilih kaprodi").bind("kaprodi");
		
		fl.addComponent(tfKode);
		fl.addComponent(tfNama);
		fl.addComponent(tfJenjang);
		fl.addComponent(cbKaprodi);
		
		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.addStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(e->{
		if (binder.validate().isOk()) {
			ProgramStudi prodi =binder.getBean();
			GenericPersistence.merge(prodi);
			Notification.show("Data berhasil disimpan", Type.HUMANIZED_MESSAGE);
		}
			
		});
		fl.addComponent(simpan);
		vl.addComponent(fl);
		setCompositionRoot(vl);
	}
}
