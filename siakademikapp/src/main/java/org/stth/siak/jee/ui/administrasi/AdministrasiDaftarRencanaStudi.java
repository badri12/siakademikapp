package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.RencanaStudiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportContentFactory;
import org.stth.siak.rpt.ReportOutputGenerator;
import org.stth.siak.rpt.ReportRawMaterials;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ExportData;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import net.sf.jasperreports.engine.JRException;

public class AdministrasiDaftarRencanaStudi extends VerticalLayout implements View{
	private static final long serialVersionUID = 7192154917532767043L;
	private Panel filter;
	private Panel daftarRencanaStudi;
	private KonfigurasiPersistence k;
	private List<RencanaStudiMahasiswa> lrsm;

	public AdministrasiDaftarRencanaStudi() {
		filter = new Panel("Filter");
		addComponent(ViewFactory.header("Administrasi Daftar Rencana Studi"));
		HorizontalLayout hl = new HorizontalLayout();
		Button add = new Button("Tambah");
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(e->{
			Window w = new AdministrasiEditorRencanaStudi(null);
			getUI().addWindow(w);
		});
		Button btKRSGenerate = new Button("Generate KRS otomatis");
		btKRSGenerate.setIcon(VaadinIcons.PLUS_CIRCLE);
		btKRSGenerate.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btKRSGenerate.addClickListener(gen->{
			AdministrasiGeneratorKRS krs= new AdministrasiGeneratorKRS();
			Window win =GeneralPopups.showGenericWindowReturn(krs, "Generate KRS otomatis");
			win.setWidth("800px");
		});
		hl.addComponents(btKRSGenerate);
		addComponent(hl);
		daftarRencanaStudi = new Panel("Daftar Rencana Studi");
		k = new KonfigurasiPersistence();
		buildFilter();
		setMargin(true);
		setSpacing(true);
		addComponents(filter, daftarRencanaStudi);
	}

	private void buildFilter() {	
		FormLayout flLeft = new FormLayout();
		FormLayout flMid= new FormLayout();
		FormLayout flRight=new FormLayout();

		List<Mahasiswa> lm = MahasiswaPersistence.getListByExample(new Mahasiswa());
		Map<String, Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa mahasiswa : lm) {
			mapMHS.put(mahasiswa.toString(), mahasiswa);
		}

		AutocompleteTextField atfMHS = new AutocompleteTextField("Mahasiswa");
		atfMHS.setPlaceholder("Inputkan nama atau NIM Mahasiswa");
		AutocompleteSuggestionProvider acspMHS = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true	);
		atfMHS.setMinChars(3);
		atfMHS.setSuggestionProvider(acspMHS);
		atfMHS.setWidth("200px");

		ComboBox<Semester> cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(k.getCurrentSemester());

		TextField tfTA = new TextField("Tahun Ajaran");
		tfTA.setValue(k.getCurrentTa());

		ComboBox<StatusRencanaStudi> cbStatus = new ComboBox<>("Status", Arrays.asList(StatusRencanaStudi.values()));

		cbStatus.setValue(StatusRencanaStudi.DISETUJUI);
		//cbStatus.addItem("Belum KRS");
		ComboBox<DosenKaryawan> cbPA = new ComboBox<>("Dosen PA", DosenKaryawanPersistence.getDosen());
		ComboBox<ProgramStudi> cbprodi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));

		TextField tfangkatan = new TextField("Angkatan");

		Button cari = new Button();
		cari.setIcon(VaadinIcons.SEARCH);
		cari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		cari.addClickListener(klik->{
			int angkatan =0;
			if (!tfangkatan.getValue().isEmpty()) {
				angkatan = Integer.valueOf(tfangkatan.getValue());
			}

			ProgramStudi ps =cbprodi.getValue();
			Semester sem = cbSemester.getValue();
			String tahunAjaran = tfTA.getValue();
				RencanaStudiMahasiswa rsm = new RencanaStudiMahasiswa();
				rsm.setMahasiswa(mapMHS.get(atfMHS.getValue()));
				rsm.setPembimbingAkademik(cbPA.getValue());
				rsm.setTahunAjaran(tahunAjaran);
				rsm.setSemester(sem);
				rsm.setStatus(cbStatus.getValue());
				List<RencanaStudiMahasiswa> lrsm = RencanaStudiPersistence.getList(rsm);
				List<RencanaStudiMahasiswa> lrsProdi = new ArrayList<>();

				if (ps!=null) {
					for (RencanaStudiMahasiswa rsma :lrsm) {			
						if (rsma.getMahasiswa().getProdi().getId()==ps.getId()) {
							lrsProdi.add(rsma);
						}
					}
				}else lrsProdi.addAll(lrsm);
				List<RencanaStudiMahasiswa> lrsProdiAngkatan = new ArrayList<>();

				if (angkatan>2000) {
					for (RencanaStudiMahasiswa rsmaProdi :lrsProdi) {
						if (rsmaProdi.getMahasiswa().getAngkatan()==angkatan) {
							lrsProdiAngkatan.add(rsmaProdi);
						}
					}
				}else lrsProdiAngkatan.addAll(lrsProdi);
				this.lrsm=lrsProdiAngkatan;
				buildDaftarRencana(1);
			
		});

		flLeft.addComponents(cbSemester, tfTA);
		flMid.addComponents(cbPA, cbStatus);
		flRight.addComponents(tfangkatan, cbprodi);
		FormLayout flbutton = new FormLayout();

		flbutton.addComponents(atfMHS,cari);
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flLeft, flMid, flRight, flbutton);
		hl.setSpacing(true);
		filter.setContent(hl);
	}
	private void buildDaftarRencana(int krs) {
		VerticalLayout vl = new VerticalLayout();
		Grid<RencanaStudiMahasiswa> g= new Grid<>("Ditemukan "+lrsm.size()+" Rencana Studi", lrsm);

		//g.addColumn("No");
		//g.addColumn("NIM");
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(rsm->{
			return rsm.getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(rsm->{
			return rsm.getMahasiswa().getNama();
		}).setCaption("NAMA");
		g.addColumn(RencanaStudiMahasiswa::getSemester).setCaption("SEMESTER");
		g.addColumn(RencanaStudiMahasiswa::getTahunAjaran).setCaption("T.A");
		g.addColumn(RencanaStudiMahasiswa::getStatus).setCaption("STATUS");

		if (krs>0) {
			g.addColumn(RencanaStudiMahasiswa::getCreated).setCaption("Tanggal Susun");
			g.addColumn(RencanaStudiMahasiswa::getCreationMethod).setCaption("Kode Susun");
			g.addComponentColumn(rsm->{
				Button b =  new Button();
				b.setIcon(VaadinIcons.PRINT);
				b.setStyleName(ValoTheme.BUTTON_PRIMARY);
				b.addClickListener(e->{
					try {
						printRencanaStudi(rsm);
					} catch (JRException e1) {
						e1.printStackTrace();
					}
				});
				return b;
			});
			g.addComponentColumn(rsm->{
				Button b =  new Button();
				b.setIcon(VaadinIcons.EDIT);
				b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				b.addClickListener(e->{
					Window w = new AdministrasiEditorRencanaStudi(rsm);
					getUI().addWindow(w);
				});
				return b;
			});
		}

		g.setSizeFull();
		Button bExport = new Button("Export Data Mahasiswa");
		//bExport.setIcon(VaadinIcons.xl);
		bExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		String s= "Belum KRS";
		if (krs>0) {
			s="Sudah KRS";
		}

		List<Mahasiswa> l =new ArrayList<>();
		for (RencanaStudiMahasiswa rsm : lrsm) {
			l.add(rsm.getMahasiswa());
		}
		StreamResource source = ExportData.mahasiswa(l, "Daftar Mahasiswa "+s);
		FileDownloader fd = new FileDownloader(source);
		fd.extend(bExport);
		vl.setSpacing(true);
		vl.addComponents(g, bExport);
		daftarRencanaStudi.setContent(vl);

	}
	@SuppressWarnings("deprecation")
	protected void printRencanaStudi(RencanaStudiMahasiswa rsm) throws JRException {
		List<RencanaStudiMahasiswa> rss = new ArrayList<>();
		rss.add(rsm);
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportRencanaStudi(rss);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Rencana Studi Mahasiswa");
		StreamResource resource = rog.exportToPdf();
		getUI().getPage().open(resource, "_blank", false);

	}

	@Override
	public void enter(ViewChangeEvent event) {


	}

}
