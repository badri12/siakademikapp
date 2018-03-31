package org.stth.siak.jee.ui.administrasi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.util.ExportToExcel;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import net.sf.jasperreports.engine.JRException;

public class AdministrasiKelasPerkuliahan extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private Panel content = new Panel("Daftar Kelas Perkuliahan");
	private List<KelasPerkuliahan> lKelasPerkuliahan;
	private KelasPerkuliahan kelasExample;
	private Button tambah;
	private List<KelasPerkuliahan> l;
	private Mahasiswa m;
	private KonfigurasiPersistence k;
	private Grid<KelasPerkuliahan> g;
	
	public AdministrasiKelasPerkuliahan() {
		k=new KonfigurasiPersistence();
		addComponent(ViewFactory.header("Administrasi Kelas Perkuliahan"));
		tambah = new Button("Tambah");
		tambah.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		tambah.setIcon(VaadinIcons.PLUS);
		tambah.addClickListener(klik->{
			Window w = new AdministrasiEditorKelasPerkuliahan(null);
			getUI().addWindow(w);
		});
		addComponent(tambah);
		addComponent(buildFilter());
		addComponent(content);
	}
	
	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flLeft = new FormLayout();
		FormLayout flMiddle = new FormLayout();
		FormLayout flRight = new FormLayout();
		ComboBox<String> ta = new ComboBox<>("T.A");
		ta.setItems(GeneralUtilities.getListTA(null));
		ta.setValue(k.getCurrentTa());
		
		AutocompleteTextField	mk = new AutocompleteTextField("Mata Kuliah");
		mk.setWidth("250px");
		mk.setPlaceholder("Inputkan kode atau nama matakuliah");
		List<MataKuliah> lkp = GenericPersistence.findList(MataKuliah.class);
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk2 : lkp) {
			mapmk.put(mk2.toString(), mk2);
		}
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		mk.setSuggestionProvider(acsp);
		mk.setMinChars(3);
		ComboBox<Semester> semester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		semester.setValue(k.getCurrentSemester());
		
		ComboBox<ProgramStudi> prodi = new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		ComboBox<DosenKaryawan> dosen = new ComboBox<>("Dosen",DosenKaryawanPersistence.getDosen());
		
		AutocompleteTextField mhs = new AutocompleteTextField("Mahasiswa");
		mhs.setWidth("250px");
		mhs.setPlaceholder("Inputkan nim atau nama mahasiswa");
		List<Mahasiswa> lm= MahasiswaPersistence.getListByExample(new Mahasiswa());
		System.out.println(lm.size());
		Map<String, Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}
		AutocompleteSuggestionProvider acspm = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		mhs.setSuggestionProvider(acspm);
		mhs.setMinChars(3);
		
		Button cari = new Button("Cari");
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.setIcon(VaadinIcons.SEARCH);
		cari.addClickListener(klik->{
			m=mapMHS.get(mhs.getValue());
			MataKuliah matKul = mapmk.get(mk.getValue());
			kelasExample = new KelasPerkuliahan();
			kelasExample.setTahunAjaran(ta.getValue());
			kelasExample.setProdi(prodi.getValue());
			kelasExample.setSemester(semester.getValue());
			kelasExample.setDosenPengampu(dosen.getValue());
			kelasExample.setMataKuliah(matKul);
			if (m!=null) {
				lKelasPerkuliahan=new ArrayList<>();
				PesertaKuliah peserta= new PesertaKuliah();
				peserta.setMahasiswa(m);
				peserta.setKelasPerkuliahan(kelasExample);
				List<PesertaKuliah> l =PesertaKuliahPersistence.getPesertaKuliahByExample(peserta);
				for (PesertaKuliah pk : l) {
					if (matKul!=null) {
						if (pk.getKelasPerkuliahan().getMataKuliah().getId()==matKul.getId()) {
							lKelasPerkuliahan.add(pk.getKelasPerkuliahan());
						}
					}else lKelasPerkuliahan.add(pk.getKelasPerkuliahan());
					
				}
			}else{
				lKelasPerkuliahan=KelasPerkuliahanPersistence.getKelasPerkuliahanByExample(kelasExample);
			}
			buildContent();
			m=null;
		});
		flLeft.addComponents(dosen, prodi);
		flMiddle.addComponents(semester, ta);
		flRight.addComponents(mk, mhs);
		FormLayout flB = new FormLayout();
		flB.addComponent(cari);
		hl.addComponents(flLeft, flMiddle, flRight, flB);
		Panel panelFilter = new Panel("Filter");
		panelFilter.setContent(hl);
		return panelFilter;
	}
	
	private void buildContent(){
		VerticalLayout vl = new VerticalLayout();
		g = new  Grid<>();
		g.setItems(lKelasPerkuliahan);
		g.removeAllColumns();
		g.setSelectionMode(SelectionMode.MULTI);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(KelasPerkuliahan::getDosenPengampu).setCaption("DOSEN");
		g.addColumn(KelasPerkuliahan::getKodeKelas).setCaption("KODE");
		g.addColumn(KelasPerkuliahan::getMataKuliah).setCaption("MATAKULIAH");
		g.addColumn(KelasPerkuliahan::getSemester).setCaption("SEMESTER");
		g.addColumn(KelasPerkuliahan::getTahunAjaran).setCaption("T.A");
		g.addColumn(KelasPerkuliahan::getProdi).setCaption("PRODI");
		g.addComponentColumn(kp->{
			Button b = new Button();
			b.setIcon(VaadinIcons.EDIT);
			b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.addClickListener(e->{
				AdministrasiEditorKelasPerkuliahan aekp = new AdministrasiEditorKelasPerkuliahan(kp);
				getUI().addWindow(aekp);
			});
			return b;
		});
		
		HorizontalLayout hlButton = new HorizontalLayout();
		Button cetakSampulUAS = new Button("Sampul Soal UAS");
		cetakSampulUAS.setIcon(VaadinIcons.PRINT);
		cetakSampulUAS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakSampulUAS.addClickListener(klik->{
			cetakSampulSoal(JenisUjian.UAS, cetakSampulUAS);
		});
		Button cetakSampulUTS = new Button("Sampul Soal UTS");
		cetakSampulUTS.setIcon(VaadinIcons.PRINT);
		cetakSampulUTS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakSampulUTS.addClickListener(klik->{
			cetakSampulSoal(JenisUjian.UTS, cetakSampulUTS);
		});
		Button cetakAbsenUTS = new Button("Absen UTS");
		cetakAbsenUTS.setIcon(VaadinIcons.PRINT);
		cetakAbsenUTS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakAbsenUTS.addClickListener(klik->{
			cetakAbsenUjian(JenisUjian.UTS, cetakAbsenUTS);
		});
		
		Button cetakAbsenUAS = new Button("Absen UAS");
		cetakAbsenUAS.setIcon(VaadinIcons.PRINT);
		cetakAbsenUAS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakAbsenUAS.addClickListener(klik->{
			 cetakAbsenUjian(JenisUjian.UAS, cetakAbsenUAS);
		});
		Button bCetakKehadiranKuliah = new Button("Catatan Kehadiran kuliah");
		bCetakKehadiranKuliah.setIcon(VaadinIcons.PRINT);
		bCetakKehadiranKuliah.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bCetakKehadiranKuliah.addClickListener(e->{
			if (g.getSelectedItems().size()==0) {
				Notification.show("pilih kelas perkuliahan", Type.ERROR_MESSAGE);
				return;
			}
			l=new ArrayList<>();
			l.addAll(g.getSelectedItems());
			try {
				StreamResource source = ReportResourceGenerator.cetakAbsenKuliah(l);
				EnhancedBrowserWindowOpener.extendOnce(bCetakKehadiranKuliah).open(source);
				//getUI().getPage().open(source, "_blank", false);
			} catch (JRException e1) {
				e1.printStackTrace();
			}
		});
		Button exportNilai = new Button("Export Nilai");
		exportNilai.setIcon(VaadinIcons.DOWNLOAD);
		exportNilai.setStyleName(ValoTheme.BUTTON_PRIMARY);
		exportNilai.addClickListener(e->{
			if (g.getSelectedItems().size()==0) {
				Notification.show("pilih kelas perkuliahan", Type.ERROR_MESSAGE);
				return;
			}
			l=new ArrayList<>();
			l.addAll(g.getSelectedItems());
			List<PesertaKuliah> lpk = new ArrayList<>();
			for (KelasPerkuliahan kp : l) {
				lpk.addAll(PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp));
			}
			try {
				StreamResource source = ExportToExcel.createFileExcel(ExportToExcel.nilai( "Nilai", lpk) , "Nilai");
				ResourceReference ref = new ResourceReference(source, this, "nilai"+source.hashCode());
			    this.setResource("nilai"+source.hashCode(), source); // now it's available for download
			    getUI().getPage().open(ref.getURL(), null);
				//EnhancedBrowserWindowOpener.extendOnce(exportNilai).open(source);
				//getUI().getPage().open(source, "_blank", false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		hlButton.setSpacing(true);
		hlButton.addComponents(cetakSampulUTS ,cetakSampulUAS, cetakAbsenUTS, cetakAbsenUAS, bCetakKehadiranKuliah, exportNilai);
		
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponents(g, hlButton);
		content.setCaption("Daftar Kelas Perkuliahan ("+lKelasPerkuliahan.size()+")");
		content.setContent(vl);
	}


	private void cetakAbsenUjian(JenisUjian jenis, Button bt) {
		if (g.getSelectedItems().size()==0) {
			Notification.show("pilih kelas perkuliahan", Type.ERROR_MESSAGE);
			return;
		}
		l=new ArrayList<>();
		l.addAll(g.getSelectedItems());
		try {
			StreamResource source = ReportResourceGenerator.cetakAbsensiUjian(l, jenis);
			EnhancedBrowserWindowOpener.extendOnce(bt).open(source);
			//getUI().getPage().open(source, "_blank", false);
		} catch (JRException e1) {
			e1.printStackTrace();
		}
	}

	private void cetakSampulSoal(JenisUjian jenis, Button bt) {
		if (g.getSelectedItems().size()==0) {
			Notification.show("pilih kelas perkuliahan", Type.ERROR_MESSAGE);
			return;
		}
		l=new ArrayList<>();
		l.addAll(g.getSelectedItems());
		StreamResource resource;
		try {
			resource = ReportResourceGenerator.cetakSampulUjian(l, jenis);
			EnhancedBrowserWindowOpener.extendOnce(bt).open(resource);
			//getUI().getPage().open(resource, "_blank", false);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
