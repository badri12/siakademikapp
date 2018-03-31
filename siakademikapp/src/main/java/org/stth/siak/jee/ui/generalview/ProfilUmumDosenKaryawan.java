package org.stth.siak.jee.ui.generalview;


import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ProfilUmumDosenKaryawan extends VerticalLayout implements View{
	
	private static final long serialVersionUID = -8265667895917038873L;
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private Binder<DosenKaryawan> bind;
	private DosenKaryawan dk;
	private VerticalLayout pnl;
	private Button simpan = new Button("Simpan");
	private TextField tfNama = new TextField("Nama");
	private TextField tfAlias = new TextField("Alias");
	private DateField dfTglLahir = new DateField("Tanggal Lahir");
	private TextField tfTmptLahir = new TextField("Tempat Lahir");
	private TextField tfKTP = new TextField("No KTP");
	private TextField tfTlp = new TextField("telepon");
	private TextField tfemail = new TextField("e-mail");
	private TextField tfAlamat = new TextField("Alamat");
	private TextField tfThnMAsuk = new TextField("Tahun Masuk");

	public ProfilUmumDosenKaryawan() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dk = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		bind = new Binder<>(DosenKaryawan.class);
		bind.setBean(dk);
		addComponent(ViewFactory.header("Profil Anda"));
		addComponent(buildForm());
		addComponent(ViewFactory.footer());
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.setIcon(VaadinIcons.FILE_TEXT);
	}
	private Component buildForm() {
		pnl = new VerticalLayout();
		if (dk.isDosen()){
			buildForDosen();
		} else {
			buildForKaryawan();
		}
		return pnl;
	}
	
	private void buildForKaryawan() {
		HorizontalLayout hl = new HorizontalLayout();
		FormLayout fly = new FormLayout();
		FormLayout flr = new FormLayout();
		bind.bind(tfNama, "nama");
		bind.bind(tfAlias, "alias");
		bind.bind(dfTglLahir, "tanggalLahir");
		bind.bind(tfTmptLahir, "tempatLahir");
		bind.bind(tfKTP, "nomorKtp");
		bind.bind(tfTlp, "nomorTelepon");
		bind.bind(tfemail, "email");
		bind.bind(tfAlamat, "alamatRumah");
		bind.forField(tfThnMAsuk).withConverter(new StringToIntegerConverter("Mohon masukkan angka")).bind("thnMasuk");

		fly.addComponents(tfNama, tfAlias, dfTglLahir, tfTmptLahir, tfKTP);
		flr.addComponents(tfTlp, tfemail, tfAlamat, tfThnMAsuk);
		fly.addComponent(simpan);
		simpan.addClickListener(e->{
			simpanDK(bind);
		});
		hl.addComponents(fly, flr);
		pnl.addComponent(hl);

	}
	private void simpanDK(Binder<DosenKaryawan> bind) {
		DosenKaryawan dk= bind.getBean();
		dk.setUpdateOleh(dk);
		GenericPersistence.merge(dk);
		Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);

	}
	private void buildForDosen() {
		FormLayout fly = new FormLayout();

		//TextField tfNIS = new TextField("nis");
		//TextField tfNIDN = new TextField("nidn");
		// tfPendAkhir = new TextField("jenjangPendTerakhir");


		bind.bind(tfNama, "nama");
		bind.bind(tfAlias, "alias");
		bind.bind(dfTglLahir, "tanggalLahir");
		bind.bind(tfTmptLahir, "tempatLahir");
		bind.bind(tfKTP, "nomorKtp");
		bind.bind(tfTlp, "nomorTelepon");
		bind.bind(tfemail, "email");
		bind.bind(tfAlamat, "alamatRumah");
		bind.forField(tfThnMAsuk).withConverter(new StringToIntegerConverter("Mohon masukkan angka")).bind("thnMasuk");

		fly.addComponents(tfNama, tfAlias, dfTglLahir, tfTmptLahir, tfKTP, tfTlp, tfemail, tfAlamat, tfThnMAsuk);


		/*
		fly.addComponent(binder.buildAndBind("Program Studi", "prodiPendTerakhir"));
		fly.addComponent(binder.buildAndBind("Institusi", "institusiPendTerakhir"));
		fly.addComponent(binder.buildAndBind("Status", "status"));
		fly.addComponent(simpan);
		 */
		simpan.addClickListener(e->{
			simpanDK(bind);
		});
		pnl.addComponent(fly);
	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
