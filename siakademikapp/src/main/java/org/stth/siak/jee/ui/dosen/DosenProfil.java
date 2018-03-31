package org.stth.siak.jee.ui.dosen;


import java.util.Arrays;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.enumtype.StatusDosen;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenProfil extends VerticalLayout implements View{
	private static final long serialVersionUID = -8265667895917038873L;
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private ComboBox<StatusDosen> status;
	private Binder<DosenKaryawan> bind;
	private Button simpan = new Button("Simpan");

	public DosenProfil() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		DosenKaryawan d = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		bind=new Binder<>(DosenKaryawan.class);
		bind.setBean(d);
		addComponent(ViewFactory.header("Profil Anda"));
		addComponent(buildForm());
		addComponent(ViewFactory.footer());
	}
	private Component buildForm() {
		VerticalLayout pnl = new VerticalLayout();
		FormLayout flkiri = new FormLayout();
		FormLayout flKanan = new FormLayout();

		TextField tfNama = new TextField("Nama");
		TextField tfAlias = new TextField("Alias");
		DateField tfTglLahir = new DateField("Tanggal Lahir");
		TextField tfTmptLahir = new TextField("Tempat Lahir");
		TextField tfKTP = new TextField("No KTP");
		TextField tfTlp = new TextField("telepon");
		TextField tfemail = new TextField("e-mail");
		TextField tfAlamat = new TextField("Alamat");
		TextField tfThnMAsuk = new TextField("Tahun Masuk");
		TextField tfNIS = new TextField("nis");
		TextField tfNIDN = new TextField("nidn");
		TextField tfInstitusi = new TextField("Institusi");
		TextField tfProdiAkhir = new TextField("Program Studi");
		TextField tfPendAkhir = new TextField("Pendidikan Terakhir");
		status = new ComboBox<>("Status", Arrays.asList(StatusDosen.values()));

		bind.forField(tfNama).asRequired("Masukkan nama").bind("nama");
		bind.forField(tfAlias).asRequired("masukkan alias").bind("alias");
		bind.bind(tfTglLahir, "tanggalLahir");
		bind.bind(tfTmptLahir, "tempatLahir");
		bind.bind(tfKTP, "nomorKtp");
		bind.bind(tfTlp, "nomorTelepon");
		bind.bind(tfemail, "email");
		bind.bind(tfAlamat, "alamatRumah");
		bind.forField(tfThnMAsuk)
		.withConverter(new StringToIntegerConverter("Masukkan angka")).bind( "thnMasuk");
		bind.bind(status, "status");
		bind.bind(tfNIS, "nis");
		bind.bind(tfNIDN, "nidn");
		bind.bind(tfPendAkhir, "jenjangPendTerakhir");
		bind.bind(tfProdiAkhir, "prodiPendTerakhir");
		bind.bind(tfInstitusi, "institusiPendTerakhir");

		flkiri.addComponents(tfNama, tfAlias, tfNIS, tfNIDN, tfKTP, tfTglLahir, tfTmptLahir);
		flKanan.addComponents(tfTlp, tfemail, tfThnMAsuk, tfPendAkhir, tfProdiAkhir, tfInstitusi, status);


		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponents(flkiri, flKanan);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.addClickListener(e->{
			simpanDK(bind);
		});
		pnl.addComponents(hl, simpan);
		return pnl;
	}

	private void simpanDK(Binder<DosenKaryawan> bind) {
		if (bind.validate().isOk()) {
			DosenKaryawan dk= bind.getBean();
			dk.setUpdateOleh(dk);
			GenericPersistence.merge(dk);
			Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);
		}
		
	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
