package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MataKuliahKonversiPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.jee.persistence.MatakuliahPrasyaratPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiMataKuliah extends VerticalLayout implements View {
	private static final long serialVersionUID = -6000146506900354121L;
	private Panel filter;
	private Panel daftarMatkul;
	private MataKuliahKurikulum mkk;

	public AdministrasiMataKuliah() {
		setSpacing(true);
		addComponent(ViewFactory.header("Administrasi MataKuliah"));
		Button add =new Button("Tambah");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(ev->{
			GeneralPopups.showGenericWindow(new AdministrasiEditorMataKuliah(null), "Mata Kuliah");
		});
		addComponent(add);
		filter = new Panel("Filter");
		filter.setContent(buildFilter());
		daftarMatkul = new Panel("Daftar Matakuliah");
		//daftarMatkul.setContent(buildDaftarMatkul());
		addComponents(filter, daftarMatkul);	
	}

	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		TextField kode = new TextField("Kode");
		TextField nama = new TextField("Nama");
		List<Kurikulum> lKur = GenericPersistence.findList(Kurikulum.class);
		ComboBox<Kurikulum> kurikulum = new ComboBox<>("Kurikulum", lKur);
		ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		kurikulum.setWidth("300px");
		cbProdi.setWidth("300px");
		Button cari = new Button("Cari");
		cari.setIcon(VaadinIcons.SEARCH);
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.addClickListener(e->{
			Kurikulum kur = kurikulum.getValue();
			MataKuliah mk =new MataKuliah();
			mkk = new MataKuliahKurikulum();
			mkk.setKurikulum(kur);
			if (!kode.getValue().isEmpty()) {
				mk.setKode(kode.getValue());
			}
			if (!nama.getValue().isEmpty()) {
				mk.setNama(nama.getValue());
			}
			if (!cbProdi.isEmpty()) {
				mk.setProdiPemilik(cbProdi.getValue());
			}
			mkk.setMataKuliah(mk);
			daftarMatkul.setContent(buildDaftarMatkul());
		});
		flKiri.addComponents(kode, nama);
		flKanan.addComponents(cbProdi, kurikulum);
		FormLayout flb = new FormLayout();
		flb.addComponent(cari);
		hl.addComponents(flKiri, flKanan,flb);
		return hl;
	}

	private Component buildDaftarMatkul(){
		List<MataKuliah> lMK = new ArrayList<>();
		if (mkk.getKurikulum()!=null) {
			List<MataKuliahKurikulum> lMKK = MataKuliahKurikulumPersistence.get(mkk);
			for (MataKuliahKurikulum mkk : lMKK) {
				lMK.add(mkk.getMataKuliah());
			}
		}else
		lMK=MataKuliahPersistence.get(mkk.getMataKuliah());
		Grid<MataKuliah> g = new Grid<>();
		g.setItems(lMK);
		daftarMatkul.setCaption("Daftar Matakuliah ("+lMK.size()+")");
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.setSizeFull();
		g.addColumn(MataKuliah::getKode).setCaption("KODE");
		g.addColumn(MataKuliah::getNama).setCaption("NAMA");
		g.addColumn(MataKuliah::getSks).setCaption("SKS");
		g.addColumn(MataKuliah::getProdiPemilik).setCaption("PRODI");
		g.addComponentColumn(mk->{
			Button b = new Button();
			b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.setDescription("Edit Matakuliah");
			b.setIcon(VaadinIcons.EDIT);
			b.addClickListener(e->{
				AdministrasiEditorMataKuliah aemk = new AdministrasiEditorMataKuliah(mk);
				GeneralPopups.showGenericWindow(aemk, "Edit Matakuliah");
			});
			Long l=MatakuliahPrasyaratPersistence.getCount(mk);
			Button prasy = new Button(""+l);
			prasy.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			prasy.setIcon(VaadinIcons.FOLDER);
			prasy.setDescription("Prasyarat");
			prasy.addClickListener(e->{
				AdministrasiEditorPrasyarat aep = new AdministrasiEditorPrasyarat(mk);
				getUI().addWindow(aep);
			});
			Long jml = MataKuliahKonversiPersistence.getCount(mk);
			Button konv=new Button(""+jml);
			konv.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			konv.setIcon(VaadinIcons.EXCHANGE);
			konv.setDescription("MataKuliah Konversi");
			konv.addClickListener(e->{
				AdministrasiEditorMataKuliahKonversi aemkv = new AdministrasiEditorMataKuliahKonversi(mk);
				getUI().addWindow(aemkv);
			});
			HorizontalLayout hl= new HorizontalLayout();
			hl.addComponents(b, prasy, konv);
			return hl;
		});	
		return g;
	}
	@Override
	public void enter(ViewChangeEvent event) {


	}

}
