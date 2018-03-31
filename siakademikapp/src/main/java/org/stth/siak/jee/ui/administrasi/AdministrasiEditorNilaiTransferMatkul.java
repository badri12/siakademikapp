package org.stth.siak.jee.ui.administrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.NilaiTransferMataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.ui.util.GeneralNotificartion;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextField;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiEditorNilaiTransferMatkul extends CustomComponent{
	private static final long serialVersionUID = -7388605527498298733L;
	private PesertaKuliah pk;
	private FormLayout fl;
	private NilaiTransferMataKuliah ntmk;
	private ProgramStudi ps;
	private Map<String, MataKuliah> map;
	private Binder<PesertaKuliah> binderPK;
	private Binder<NilaiTransferMataKuliah> binderntmk;
	private AutocompleteTextField atfMatkul;
	private KonfigurasiPersistence k = new KonfigurasiPersistence();
	
	public AdministrasiEditorNilaiTransferMatkul(NilaiTransferMataKuliah ntmk) {
		this.ntmk=ntmk;
		ps=ntmk.getNilaiDiakui().getMahasiswa().getProdi();;
		System.out.println(ps);
		pk=ntmk.getNilaiDiakui();
		fl = new FormLayout();
		fl.setMargin(true);
		nilaiAsal();
		matkul();
		nilaiDiakui();
		setCompositionRoot(fl);
	}
	private void nilaiAsal() {
		binderntmk = new Binder<>(NilaiTransferMataKuliah.class);
		binderntmk.setBean(ntmk);

		TextField tfKodeAsal = new TextField("Kode Matakuliah Asal");
		tfKodeAsal.setWidth("250px");
		TextField tfMatKulAsal = new TextField("Nama Matakuliah Asal");
		tfMatKulAsal.setWidth("250px");
		TextField tfSKSAsal = new TextField("SKS Asal");
		tfSKSAsal.setWidth("250px");
		TextField tfNilaiAsal = new TextField("Nilai Asal");
		tfNilaiAsal.setWidth("250px");
			
		binderntmk.forField(tfKodeAsal).asRequired("Masukkan kode matakuliah asal").bind("kodeMKAsal");
		binderntmk.forField(tfMatKulAsal).asRequired("Masukkan nama matakuliah asal").bind("matKulAsal");
		binderntmk.forField(tfSKSAsal).asRequired("Masukkan SKS matakuliah asal")
		.withConverter(new StringToIntegerConverter("masukkan angka")).bind("sksAsal");
		binderntmk.forField(tfNilaiAsal).asRequired("Masukkan nilai").bind("nilaiHurufAsal");

		fl.addComponents(tfKodeAsal, tfMatKulAsal, tfSKSAsal, tfNilaiAsal);
	}
	private void nilaiDiakui(){
		binderPK = new Binder<>(PesertaKuliah.class);
		binderPK.setBean(pk);

		TextField tfNilaiDiakui = new TextField("Nilai Diakui");
		tfNilaiDiakui.setWidth("250px");
		
		binderPK.bind(tfNilaiDiakui, "nilai");

		Button bSimpan = new Button("Simpan");
		bSimpan.setIcon(VaadinIcons.FILE_TEXT);
		bSimpan.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bSimpan.addClickListener(e->{
			save();
		});
		fl.addComponents(tfNilaiDiakui, bSimpan);
	}
	private void matkul() {
		List<MataKuliah> lMK= MataKuliahPersistence.getByProdi(ps);
		map = new HashMap<>();
		System.out.println(lMK.size());
		for (MataKuliah mataKuliah : lMK) {
			map.put(mataKuliah.toString(), mataKuliah);
		}
		CollectionSuggestionProvider acspMK = new CollectionSuggestionProvider(map.keySet(), MatchMode.CONTAINS, true);
		atfMatkul = new AutocompleteTextField("Matakuliah");
		atfMatkul.setPlaceholder("Inputkan kode atau nama matakuliah");
		atfMatkul.setSuggestionProvider(acspMK);
		atfMatkul.setMinChars(3);
		atfMatkul.setWidth("250px");
		if (pk.getId()>0) {
			atfMatkul.setValue(pk.getKelasPerkuliahan().getMataKuliah().toString());
		}
		fl.addComponent(atfMatkul);
	}
	private void save() {
		MataKuliah mataKuliah = map.get(atfMatkul.getValue());
		if (ntmk.getId()>0) {
			if (binderPK.validate().isOk() && binderntmk.validate().isOk()) {
				GenericPersistence.merge(pk);
				GenericPersistence.merge(ntmk);
				GeneralNotificartion.sukses("Nilai matakuliah berhasil disimpan").show(Page.getCurrent());
			}

			
		}else{
			KelasPerkuliahan kelasExample = new KelasPerkuliahan();
			if (mataKuliah != null) {
				kelasExample.setKodeKelas("Transfer");
				kelasExample.setMataKuliah(mataKuliah);
				kelasExample.setProdi(ps);
				List<KelasPerkuliahan> lkp = KelasPerkuliahanPersistence.getKelasPerkuliahanByExample(kelasExample);
				if (lkp.size() > 0) {
					kelasExample = lkp.get(0);
					List<PesertaKuliah> lpk = PesertaKuliahPersistence
							.getPesertaKuliahByKelasPerkuliahanMhs(kelasExample, ntmk.getNilaiDiakui().getMahasiswa());
					if (lpk.size() > 0) {
						GeneralNotificartion.error("Nilai matakuliah " + mataKuliah.toString() + " sudah ada")
								.show(Page.getCurrent());
						return;
					}
				} else {
					kelasExample.setSemester(k.getCurrentSemester());
					kelasExample.setTahunAjaran(k.getCurrentTa());
					GenericPersistence.saveAndFlush(kelasExample);
				}
				if (binderPK.validate().isOk() && binderntmk.validate().isOk()) {
					pk = binderPK.getBean();
					pk.setKelasPerkuliahan(kelasExample);
					GenericPersistence.saveAndFlush(pk);
					NilaiTransferMataKuliah ntmk = binderntmk.getBean();
					ntmk.setNilaiDiakui(pk);
					GenericPersistence.merge(ntmk);
					GeneralNotificartion.sukses("Nilai matakuliah berhasil disimpan").show(Page.getCurrent());
				}

			} else
				Notification.show("", "Anda belum memilih matakuliah", Type.ERROR_MESSAGE);
		}

	}

}
