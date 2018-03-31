package org.stth.siak.jee.ui.administrasi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.ui.util.ConfirmDialog;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiEditorKurikulum extends Window{
	private static final long serialVersionUID = 8079523232250649033L;
	private Panel detailKur;
	private Panel daftarMK;
	private Binder<Kurikulum> binder;
	private MataKuliahKurikulum mkk;
	private Kurikulum kur;
	private MataKuliahKurikulum mkkNew;
	private ComboBox<ProgramStudi> cbProdi;
	private List<ProgramStudi> lProdi = GenericPersistence.findList(ProgramStudi.class);

	public AdministrasiEditorKurikulum(Kurikulum kurikulum) {
		setCaption("Detail Kurikulum");
		if (kurikulum==null) {
			kurikulum=new Kurikulum();
		}
		this.kur=kurikulum;
		mkk=new MataKuliahKurikulum();
		mkk.setKurikulum(kur);
		binder=new Binder<>(Kurikulum.class);
		binder.setBean(kur);
		VerticalLayout vl = new VerticalLayout();
		detailKur = new Panel("Detail Kurikulum");
		daftarMK=new Panel("Daftar MataKuliah");
		detailKur.setContent(detailKurikulum());
		vl.addComponents(detailKur, daftarMK);
		vl.setMargin(true);
		setContent(vl);
		center();
		setWidth("600px");
		setModal(true);
	}

	private Component detailKurikulum() {
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		TextField nama= new TextField("Judul");

		cbProdi = new ComboBox<>("Prodi",lProdi );

		cbProdi.addValueChangeListener(e->{
			if (cbProdi.getValue()!=null) {
				daftarMK.setContent(buildDaftarMatkul());
			}
		});
		if (kur.getId()==0) {
			cbProdi.setValue(lProdi.get(0));
		}
		TextField sksLulus = new TextField("SKS Lulus");
		TextField thnMulai = new TextField("Tahun Mulai");
		CheckBox cbAktif = new CheckBox("Aktif");

		binder.forField(nama).asRequired("Masukkan nama kurikulum").bind("nama");
		binder.forField(cbProdi).asRequired("Pilih program studi").bind("prodi");
		binder.forField(thnMulai).asRequired("Masukkan tahun")
		.withConverter(new StringToIntegerConverter("Masukkan angka")).bind("tahunMulai");
		binder.forField(sksLulus).withConverter(new StringToIntegerConverter("Masukkan angka")).bind("sksLulus");
		binder.bind(cbAktif, "aktif");

		Button simpan = new Button("Simpan");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(e->{
			simpanKurikulum();
		});
		flKiri.addComponents(nama, cbProdi, cbAktif);
		flKanan.addComponents(sksLulus, thnMulai, simpan);
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flKiri, flKanan);
		return hl;
	}

	private void simpanKurikulum() {
		if (binder.validate().isOk()) {
			kur = binder.getBean();
			if (kur.getId()>0) {
				GenericPersistence.merge(kur);
			}else{
				GenericPersistence.saveAndFlush(kur);
			}

			Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);
		}

	}
	private Component buildDaftarMatkul(){
		ProgramStudi p = cbProdi.getValue();
		List<MataKuliah> lMatkul = MataKuliahPersistence.getByProdi(p);
		Map<String, MataKuliah> mapMatkul = new HashMap<>();

		for (MataKuliah mataKul : lMatkul) {
			mapMatkul.put(mataKul.toString(), mataKul);
		}
		AutocompleteSuggestionProvider aspMatkul = new CollectionSuggestionProvider(mapMatkul.keySet(), MatchMode.CONTAINS, true);
		AutocompleteTextField atfMatkul = new AutocompleteTextField();
		atfMatkul.setMinChars(3);
		atfMatkul.setSuggestionProvider(aspMatkul);
		atfMatkul.setPlaceholder("Inputkan kode atau nama matakuliah");
		atfMatkul.setWidth("250px");

		Map<MataKuliah, MataKuliahKurikulum> mapMkKur = new HashMap<>();
		List<MataKuliahKurikulum> lMKK = new ArrayList<>();
		if (mkk.getKurikulum().getId()>0) {
			lMKK = MataKuliahKurikulumPersistence.get(mkk);
			for (MataKuliahKurikulum mkKur : lMKK) {
				mapMkKur.put(mkKur.getMataKuliah(), mkKur);
			}

		}

		Button add = new Button();
		add.setIcon(VaadinIcons.PLUS);
		add.addClickListener(e->{
			if (!atfMatkul.getValue().isEmpty() && atfMatkul.getValue().length()>3) {
				MataKuliah mkGet = mapMatkul.get(atfMatkul.getValue());
				MataKuliahKurikulum mkselected = mapMkKur.get(mkGet);
				if (mkselected!=null) {
					ConfirmDialog.show(getUI(), "", "Matakuliah ini sudah ada di kurikulum,"
							+ "\n tetap ingin menambahkan ?", "Ya", "Tidak", c->{
								if (c.isConfirmed()) {
									addMatakuliahKurikulum(mkGet);
								}
							});
				}else{
					addMatakuliahKurikulum(mkGet);
				}
			}

		});

		//List<MataKuliah> lMK = new ArrayList<>();
		//for (MataKuliahKurikulum mkk : lMKK) {
		//	lMK.add(mkk.getMataKuliah());
		//}

		Grid<MataKuliahKurikulum> g = new Grid<>();
		g.setItems(lMKK);
		daftarMK.setCaption("Daftar MataKuliah ("+ lMKK.size()+")");
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(mkk->{
			return mkk.getMataKuliah().getKode();
		}).setCaption("KODE");
		g.addColumn(mkk->{
			return mkk.getMataKuliah().getNama();
		}).setCaption("NAMA");
		g.addColumn(MataKuliahKurikulum::getSemesterBuka).setCaption("SEMESTER");
		g.addColumn(MataKuliahKurikulum::getJenis).setCaption("JENIS");
		g.addComponentColumn(mkk->{
			Button hapus = new Button();
			hapus.setIcon(VaadinIcons.TRASH);
			hapus.setStyleName(ValoTheme.BUTTON_DANGER);
			hapus.addClickListener(e->{
				ConfirmDialog.show(getUI(), "", "Anda Akan Menghapus Matakuliah \n"
						+ mkk.toString() +" ?", "Ya", "Tidak", conf->{
							if (conf.isConfirmed()) {
								GenericPersistence.delete(mkk);
								daftarMK.setContent(buildDaftarMatkul());
							}
						});
			});
			return hapus;
		}).setCaption("AKSI");;


		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(atfMatkul, add);
		vl.addComponent(hl);
		vl.addComponent(g);
		return vl;
	}

	private void addMatakuliahKurikulum(MataKuliah mkGet) {
		mkkNew = new MataKuliahKurikulum();
		mkkNew.setMataKuliah(mkGet);
		Window cjm = new ChooseJenisMatakuliah(mkkNew);
		cjm.addCloseListener(close->{
			mkkNew = (MataKuliahKurikulum) close.getWindow().getData();

			if (mkkNew!=null) {
				simpanKurikulum();

				mkkNew.setKurikulum(kur);
				GenericPersistence.merge(mkkNew);
				daftarMK.setContent(buildDaftarMatkul());
			}
		});
		getUI().addWindow(cjm);
	}


}
