package org.stth.siak.jee.ui.administrasi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.PersistenceQuery;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;

public class AdministrasiKartudanHasilStudi extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private List<KelasPerkuliahanMahasiswaPerSemester2> lKPMP ;
	private List<KelasPerkuliahanMahasiswaPerSemester2> lKPMPSelected ;
	private ComboBox<Semester> cbSemester;
	private TextField tfTA;
	private TextField tfAngkatan;
	private ComboBox<ProgramStudi> cbProdi;
	private TextField tfNama;
	private Panel p = new Panel("Daftar Mahasiswa");
	private KonfigurasiPersistence k;
	private ComboBox<DosenKaryawan> cbPa;

	public AdministrasiKartudanHasilStudi() {
		setMargin(true);
		setSpacing(true);
		k = new KonfigurasiPersistence();
		addComponent(ViewFactory.header("Administrasi Daftar Mahasiswa Aktif"));
		addComponent(buildFilter());
		addComponent(p);
	}
	private Component buildFilter(){
		Panel p = new Panel("Filter");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		FormLayout flLeft = new FormLayout();
		FormLayout flMiddle = new FormLayout();
		FormLayout flRight = new FormLayout();
		FormLayout flBt = new FormLayout();
		tfNama = new TextField("Nama");
		cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(k.getCurrentSemester());
		tfTA= new TextField("Tahun Ajaran");
		tfTA.setValue(k.getCurrentTa());
		cbProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		cbPa = new ComboBox<>("P.A",DosenKaryawanPersistence.getDosen());
		tfAngkatan = new TextField("Angkatan");
		Button cari = new Button("Cari");
		cari.setIcon(VaadinIcons.SEARCH);
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.addClickListener(klik->{
			buildComponent();
		});
		flLeft.addComponents(tfNama, cbProdi);
		flMiddle.addComponents(cbSemester, tfTA);
		flRight.addComponents(tfAngkatan, cbPa);
		flBt.addComponent(cari);
		hl.addComponents(flLeft, flMiddle, flRight, flBt);
		p.setContent(hl);
		

		return p;
	}
	private void buildComponent(){
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		String nama="";
		String prodi="";
		String ta="";
		int angkatan=0;
		if(!cbProdi.isEmpty()){
			prodi=((ProgramStudi)cbProdi.getValue()).getNama();
		}

		if(!tfNama.isEmpty()){
			nama=tfNama.getValue();
		}
		if(!tfAngkatan.isEmpty()){
			angkatan=Integer.parseInt(tfAngkatan.getValue());
		}
		if(!tfTA.getValue().isEmpty()){
			ta=tfTA.getValue();
		}

		lKPMP = PersistenceQuery.khs(nama, (Semester)cbSemester.getValue(), prodi, ta,angkatan,cbPa.getValue());
		
		Grid<KelasPerkuliahanMahasiswaPerSemester2> g = 
				new Grid<>("Ditemukan "+lKPMP.size()+" mahasiswa", lKPMP);
		g.removeAllColumns();
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(KelasPerkuliahanMahasiswaPerSemester2::getSemester).setCaption("SEMESTER");
		g.addColumn(KelasPerkuliahanMahasiswaPerSemester2::getTahunAjaran).setCaption("T.A");
		//g.addColumn("NIM");
		g.addColumn(kpms->{
			return kpms.getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(kpms->{
			return kpms.getMahasiswa().getNama();
		}).setCaption("NAMA");
		g.addColumn(KelasPerkuliahanMahasiswaPerSemester2::getJumlahMataKuliah).setCaption("KELAS");
		g.setSelectionMode(SelectionMode.MULTI);

		HorizontalLayout hlButton = new HorizontalLayout();
		Button cetakKHS = new Button("KHS");
		cetakKHS.setIcon(VaadinIcons.PRINT);
		cetakKHS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakKHS.addClickListener(khs->{
			lKPMPSelected = new ArrayList<>();
			for(KelasPerkuliahanMahasiswaPerSemester2 kpm: g.getSelectedItems()){
				lKPMPSelected.add(kpm);
				System.out.println(kpm.getMahasiswa().getNama());
			}
			StreamResource resource;
			try {
				resource = ReportResourceGenerator.cetakKHS(lKPMPSelected);
				 EnhancedBrowserWindowOpener.extendOnce(cetakKHS)
			        .open(resource);
				//getUI().getPage().open(resource, "_blank", false);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		Button cetakkartuUTS = new Button("Kartu UTS");
		cetakkartuUTS.setIcon(VaadinIcons.PRINT);
		cetakkartuUTS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakkartuUTS.addClickListener(uts->{
			cetakKartu(g, JenisUjian.UTS, cetakkartuUTS);
		});
		Button cetakKartuUAS = new Button("Kartu UAS");
		cetakKartuUAS.setIcon(VaadinIcons.PRINT);
		cetakKartuUAS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cetakKartuUAS.addClickListener(uas->{
			cetakKartu(g, JenisUjian.UAS, cetakKartuUAS);
		});
		hlButton.setSpacing(true);
		hlButton.addComponents(cetakkartuUTS, cetakKartuUAS, cetakKHS);
		vl.addComponents(g, hlButton);
		p.setContent(vl);

	}
	
	private void cetakKartu(Grid<KelasPerkuliahanMahasiswaPerSemester2> g, JenisUjian jenis, Button cetakBT) {
		lKPMPSelected = new ArrayList<>();
		for(KelasPerkuliahanMahasiswaPerSemester2 kpm : g.getSelectedItems()){
			lKPMPSelected.add(kpm);
			System.out.println(kpm.getMahasiswa().getNama());
		}
			StreamResource source;
			try {
				source = ReportResourceGenerator.cetakKartuUjian(lKPMPSelected, jenis);
				EnhancedBrowserWindowOpener.extendOnce(cetakBT).open(source);
				//getUI().getPage().open(source, "_blank", false);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Override
	public void enter(ViewChangeEvent event) {


	}

}
