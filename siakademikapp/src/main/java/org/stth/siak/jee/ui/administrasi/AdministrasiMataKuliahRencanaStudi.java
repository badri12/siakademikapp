package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MataKuliahRencanaStudiPersistence;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
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

public class AdministrasiMataKuliahRencanaStudi extends VerticalLayout implements View {
	private static final long serialVersionUID = 5063834038791851580L;
	private Panel filter;
	private Panel daftarMatKul;
	private MataKuliahRencanaStudi mkrs;
	private KonfigurasiPersistence k;

	public AdministrasiMataKuliahRencanaStudi() {
		k = new KonfigurasiPersistence();
		setSpacing(true);
		filter = new Panel();
		filter.setContent(buildFilter());
		daftarMatKul = new Panel();
		addComponent(ViewFactory.header("Administrasi Mata Kuliah Rencana Studi"));
		addComponent(createbutton());
		addComponents(filter, daftarMatKul);
	}

	private Component createbutton() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Button publishKur= new Button("Publish Kurikulum");
		publishKur.setIcon(VaadinIcons.PLUS_CIRCLE);
		publishKur.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		publishKur.addClickListener(e->{
			AdministrasiPublishkurikulum apk = new AdministrasiPublishkurikulum();
			getUI().addWindow(apk);
		});
		Button add= new Button("Tambah");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(e->{
			AdministrasiEditorMataKuliahRencanaStudi amr = new AdministrasiEditorMataKuliahRencanaStudi(null);
			GeneralPopups.showGenericWindow(amr, "MataKuliah Rencana Studi");
		});
		hl.addComponents(add, publishKur);
		return hl;
	}

	private Component buildFilter() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		TextField tfTA= new TextField("Tahun Ajaran");
		tfTA.setValue(k.getKRSTa());
		ComboBox<Semester> cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(k.getKRSSemester());
		ComboBox<ProgramStudi> prodi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		Button cari = new Button("Cari");
		cari.setIcon(VaadinIcons.SEARCH);
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.addClickListener(c->{
			mkrs=new MataKuliahRencanaStudi();
			mkrs.setTahunAjaran(tfTA.getValue());
			mkrs.setProdi(prodi.getValue());
			mkrs.setSemester(cbSemester.getValue());
			daftarMatKul.setContent(buildDaftarMatkul());
		});
		flKiri.addComponents(prodi, cbSemester);
		flKanan.addComponents(tfTA, cari);
		hl.addComponents(flKiri, flKanan);
		return hl;
	}
	private Component buildDaftarMatkul() {
		List<MataKuliahRencanaStudi> lmkrs = MataKuliahRencanaStudiPersistence.bySample(mkrs);
		

		Grid<MataKuliahRencanaStudi> g = new Grid<>("Ditemukan "+lmkrs.size()+" data", lmkrs);
		g.setSizeFull();
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(MataKuliahRencanaStudi::getTahunAjaran).setCaption("TAHUN AJARAN");
		g.addColumn(MataKuliahRencanaStudi::getSemesterBuka).setCaption("SEMESTER");
		g.addColumn(MataKuliahRencanaStudi::getProdi).setCaption("PRODI");
		g.addColumn(mkrs->{
			return mkrs.getMataKuliah();
		});
		
		g.addComponentColumn(mkrs->{
			Button edit = new Button();
			edit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			edit.setIcon(VaadinIcons.EDIT);
			edit.addClickListener(klik->{
				AdministrasiEditorMataKuliahRencanaStudi amr = new AdministrasiEditorMataKuliahRencanaStudi(mkrs);
				GeneralPopups.showGenericWindow(amr, "MataKuliah Rencana Studi");
			});
			return edit;
		});

		

		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
