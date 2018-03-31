package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisMataKuliah;
import org.stth.siak.enumtype.Semester;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiEditorMataKuliahRencanaStudi extends CustomComponent{
	
	private static final long serialVersionUID = -8686162647271656924L;
	private KonfigurasiPersistence k ;
	
	private Binder<MataKuliahRencanaStudi> binder;
	private Button simpan;
	private ComboBox<ProgramStudi> prodi;
	private AutocompleteTextField atfMatkul;
	
	 public AdministrasiEditorMataKuliahRencanaStudi(MataKuliahRencanaStudi mkrs) {
		k=new KonfigurasiPersistence();
		if (mkrs==null) {
			mkrs=new MataKuliahRencanaStudi();
			mkrs.setSemester(k.getKRSSemester());
			mkrs.setTahunAjaran(k.getKRSTa());
		}
		binder=new Binder<>(MataKuliahRencanaStudi.class);
		binder.setBean(mkrs);
		setCompositionRoot(createContent());
	}

	@SuppressWarnings("serial")
	private Component createContent() {
		FormLayout fl = new FormLayout();fl.setSizeFull();
		prodi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		prodi.setWidth("250px");
		ComboBox<Semester> cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setWidth("250px");
		TextField tfTA= new TextField("Tahun Ajaran");
		tfTA.setWidth("250px");
		atfMatkul = new AutocompleteTextField("Matakuliah");
		ComboBox<JenisMataKuliah> jenisMatkul = new ComboBox<>("Jenis", Arrays.asList(JenisMataKuliah.values()));
		jenisMatkul.setWidth("250px");
		List<MataKuliah> lMatKul = GenericPersistence.findList(MataKuliah.class);
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk : lMatKul) {
			mapmk.put(mk.toString(), mk);
		}
		
		AutocompleteSuggestionProvider acspMK = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		atfMatkul.setPlaceholder("Inputkan kode atau nama matakuliah");
		atfMatkul.setMinChars(3);
		atfMatkul.setSuggestionProvider(acspMK);
		atfMatkul.setWidth("250px");
		
		
		simpan = new Button("Publish");
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.addClickListener(e->{
			save();
		});
		
		binder.forField(prodi).asRequired("pilih program studi").bind("prodi");
		binder.forField(cbSemester).asRequired("pilih semester").bind("semester");
		binder.forField(tfTA).asRequired("Masukkan tahun ajaran").bind("tahunAjaran");
		binder.forField(atfMatkul).asRequired("Inputkan matakuliah")
		.withConverter(new Converter<String, MataKuliah>() {

			@Override
			public Result<MataKuliah> convertToModel(String value, ValueContext context) {
				if (!value.isEmpty()) {
					return Result.ok(mapmk.get(value));
				}
				return null;
			}

			@Override
			public String convertToPresentation(MataKuliah value, ValueContext context) {
				if (value!=null) {
					return value.toString();
				}
				return "";
			}
		}).bind("mataKuliah");
		binder.forField(jenisMatkul).asRequired("pilih jenis matakuliah").bind("jenis");
		fl.addComponents(prodi,atfMatkul, cbSemester, tfTA, jenisMatkul);
		
		TextField tfsmsperuntukan = new TextField("Semester peruntukan");
		tfsmsperuntukan.setWidth("250px");
		binder.forField(tfsmsperuntukan).asRequired("")
		.withConverter(new StringToIntegerConverter("masukkan angka")).bind("semesterBuka");
		fl.addComponent(tfsmsperuntukan);
		fl.addComponent(simpan);
//		prodi.addValueChangeListener(e->{
//			
//			
//		});
		return fl;
	}

	private void save() {
		if (binder.validate().isOk()) {
			MataKuliahRencanaStudi mkrs = binder.getBean();
			GenericPersistence.merge(mkrs);
			Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);
		}
		
	}


}
