package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;

public class DaftarPesertaPerMatkulKRS extends CustomComponent{
	private static final long serialVersionUID = -509856854498364123L;
	private KonfigurasiPersistence k;
	private KelasPerkuliahan kp;
	private Panel filter = new Panel("Filter");
	private Panel content = new Panel("Daftar pengambilan matkul");
	private MataKuliah matKul;
	private ComboBox<ProgramStudi> prodi;
	public DaftarPesertaPerMatkulKRS(KelasPerkuliahan kp) {
		this.kp=kp;
		matKul=kp.getMataKuliah();
		k=new KonfigurasiPersistence();

		VerticalLayout vl = new VerticalLayout();
		filter.setContent(buildFilter());
		content.setContent(buildContent());
		vl.addComponents(filter,content);
		setCompositionRoot(vl);
	}
	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		AutocompleteTextField mk = new AutocompleteTextField("Mata Kuliah");
		mk.setWidth("250px");
		mk.setPlaceholder("Inputkan kode atau nama matakuliah");
		List<MataKuliah> lkp = GenericPersistence.findList(MataKuliah.class);
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk2 : lkp) {
			mapmk.put(mk2.toString(), mk2);
		}
		mk.setValue(matKul.toString());
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		mk.setMinChars(3);
		mk.setSuggestionProvider(acsp);
		Button cari = new Button();
		cari.addClickListener(e->{
			matKul=mapmk.get(mk.getValue());
			if (matKul!=null) {
				content.setContent(buildContent());
			}else Notification.show("isikan matakuliah terlebih dahulu", Type.HUMANIZED_MESSAGE);

		});
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.setIcon(VaadinIcons.SEARCH);
		FormLayout flkiri = new FormLayout();
		FormLayout flButton = new FormLayout();
		prodi = new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		flButton.addComponent(prodi);
		flkiri.addComponent(mk);
		hl.addComponents(flkiri, flButton, cari);
		hl.setComponentAlignment(cari, Alignment.MIDDLE_LEFT);
		return hl;
	}

	private Component buildContent(){
		List<RencanaStudiPilihanMataKuliah> lrspm = RencanaStudiPilihanMataKuliahPersistence
				.getValidBySemesterTahunAjaran(k.getKRSSemester(), k.getKRSTa(), matKul);

		List<RencanaStudiPilihanMataKuliah> lrspmProdi = new ArrayList<>();
		if (lrspm!=null) {
			if (prodi.getValue()!=null) {
				for (RencanaStudiPilihanMataKuliah rspm : lrspm) {
					if (prodi.getValue()==rspm.getRencanaStudi().getMahasiswa().getProdi()) {
						lrspmProdi.add(rspm);
					}
				}
				
			}else lrspmProdi.addAll(lrspm);
		}


		VerticalLayout vl = new VerticalLayout();

		//		gpc.addGeneratedProperty("kelas", new PropertyValueGenerator<String>() {
		//			private static final long serialVersionUID = -5530260601405387447L;
		//
		//			@Override
		//			public String getValue(Item item, Object itemId, Object propertyId) {
		//				RencanaStudiPilihanMataKuliah rsp = beans.getItem(itemId).getBean();
		//				if (rsp!=null) {
		//					Mahasiswa m = rsp.getRencanaStudi().getMahasiswa();
		//					List<PesertaKuliah> lp=PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahanMhs(kp, m);
		//					System.out.println(lp);
		//				}
		//				return "";
		//			}
		//
		//			@Override
		//			public Class<String> getType() {
		//				return String.class;
		//			}
		//		});
		Grid<RencanaStudiPilihanMataKuliah> g = new Grid<>("Ditemukan "+lrspmProdi.size()+" mahasiswa", lrspmProdi);
		g.setSizeFull();
		g.setSelectionMode(SelectionMode.MULTI);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(rspm->{
			return rspm.getRencanaStudi().getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(rspm->{
			return rspm.getRencanaStudi().getMahasiswa().getNama();
		}).setCaption("NAMA");
		g.addColumn(rspm->{
			return rspm.getRencanaStudi().getMahasiswa().getProdi().getNama();
		}).setCaption("PROGRAM STUDI");
		//g.setSizeFull();
		//		g.addColumn("kelas");
		Button keKelas = new Button("Masukkan ke kelas");
		keKelas.setIcon(VaadinIcons.SIGN_IN);
		keKelas.setStyleName(ValoTheme.BUTTON_PRIMARY);
		keKelas.addClickListener(e->{
			if (g.getSelectedItems().size()>0) {
				for(RencanaStudiPilihanMataKuliah rspm : g.getSelectedItems()){
					Mahasiswa m = rspm.getRencanaStudi().getMahasiswa();
					List<PesertaKuliah> lp=PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahanMhs(kp, m);
					System.out.println(lp);
					if (lp.isEmpty()) {
						PesertaKuliah pk = new PesertaKuliah(m, kp);
						GenericPersistence.merge(pk);
					}		
				}
				Notification.show("data berhasil disimpan", Type.HUMANIZED_MESSAGE);
			}else{
				Notification.show("pilih mahasiwa terlebih dahulu", Type.HUMANIZED_MESSAGE);
			}

		});
		vl.addComponents(g, keKelas);
		return vl;
	}

}
