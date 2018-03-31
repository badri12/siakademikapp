package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.jee.persistence.MataKuliahRencanaStudiPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.ui.util.ConfirmDialog;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiPublishkurikulum extends Window {
	private static final long serialVersionUID = 5983679842185934140L;
	private Kurikulum kur;
	private ComboBox<Kurikulum> cbKurikulum;
	private Semester semester;
	private String ta;
	private KonfigurasiPersistence k;
	public AdministrasiPublishkurikulum() {
		k = new KonfigurasiPersistence();
		center();
		setWidth("450px");
		setContent(CreateContent());
		setCaption("Publish Kurikulum");
		
	}
	private Component CreateContent() {
		String width = "200px";
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		cbKurikulum = new ComboBox<>("Kurikulum");
		cbProdi.addValueChangeListener(e->{
			if (cbProdi.getValue()!=null) {
				List<Kurikulum> k = KurikulumPersistence.getListByProdi(cbProdi.getValue());
				cbKurikulum.setItems(k);
			}
		});
		ComboBox<Semester> cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(k.getKRSSemester());
		TextField tfTA = new TextField("Tahun AKademik");
		tfTA.setValue(k.getKRSTa());
		Button ok = new Button("Publish");
		ok.setIcon(VaadinIcons.SHARE_SQUARE);
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(klik->{
			kur=cbKurikulum.getValue();
			ta=tfTA.getValue();
			semester=cbSemester.getValue();
			if (kur!=null && ta!=null && semester!=null) {
				ConfirmDialog.show(getUI(), "","Anda akan publish matakuliah kurikulum"+kur.getNama()+" ?","Ya","Tidak", c->{
					if (c.isConfirmed()) {
						publishMKRS();
					}
				});
			}
		});
		FormLayout fl = new FormLayout();
		cbProdi.setWidth(width);
		cbKurikulum.setWidth(width);
		cbSemester.setWidth(width);
		tfTA.setWidth(width);
		
		fl.addComponents(cbProdi, cbKurikulum, cbSemester, tfTA, ok);
		vl.addComponent(fl);
		return vl;
	}
	
	
	private void publishMKRS() {
		clearCurrentMatkulPublished();
		List<MataKuliahKurikulum> l = MataKuliahKurikulumPersistence.getByKurikulum(kur);
		for (MataKuliahKurikulum mkKur : l) {
			if (mkKur.getSemesterTipe().equals(semester)) {
				MataKuliahRencanaStudi mkrs = new MataKuliahRencanaStudi(mkKur, semester, ta);
				GenericPersistence.merge(mkrs);
			}
		}
		
	}
	private void clearCurrentMatkulPublished() {
		MataKuliahRencanaStudi mkrs = new MataKuliahRencanaStudi();
		mkrs.setSemester(semester);
		mkrs.setTahunAjaran(ta);
		mkrs.setProdi(kur.getProdi());
		List<MataKuliahRencanaStudi> l= MataKuliahRencanaStudiPersistence.bySample(mkrs);
		for (MataKuliahRencanaStudi mataKuliahRencanaStudi : l) {
			GenericPersistence.delete(mataKuliahRencanaStudi);
		}
	}

}
