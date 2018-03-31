package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportContentFactory;
import org.stth.siak.rpt.ReportOutputGenerator;
import org.stth.siak.rpt.ReportRawMaterials;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Panel;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiCetakAbsensi extends VerticalLayout implements View{
	private static final long serialVersionUID = -1425418749387686688L;
	private Panel content = new Panel("Daftar Kelas");
	private ComboBox<ProgramStudi> comboProdi;
	private ComboBox<DosenKaryawan> comboDosen=new ComboBox<>("Dosen Pengampu");
	private Semester semester;
	private String ta;
	private ProgramStudi prodi;
	
	public AdministrasiCetakAbsensi(){
		setMargin(true);
		setSpacing(true);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		addComponent(ViewFactory.header("Daftar Kelas Perkuliahan Semester "+semester+" t.a "+ ta));
		addComponent(createFilterComponent());
		addComponent(content);
		addComponent(ViewFactory.footer());
		setMargin(true);
	}

	private Component createFilterComponent() {
		Panel pnl = new Panel("Filter");
		HorizontalLayout vl = new HorizontalLayout();
		comboProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		comboProdi.addValueChangeListener(e->{
			prodi = e.getValue();
		});
		comboDosen.setItems(DosenKaryawanPersistence.getDosen());
		String width="250px";
		comboDosen.setWidth(width);
		comboProdi.setWidth(width);
		Button saring = new Button("Saring");
		saring.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saring.setIcon(VaadinIcons.SEARCH);
		saring.addClickListener(e->{
			saringData();
		});
		vl.addComponents(comboProdi,comboDosen,saring);
		vl.setComponentAlignment(saring, Alignment.BOTTOM_LEFT);
		pnl.setContent(vl);
		return pnl;
	}

	protected void saringData() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(false);
		vl.removeAllComponents();
		List<KelasPerkuliahan> l = 
				KelasPerkuliahanPersistence.getKelasPerkuliahanByProdiSemester(comboDosen.getValue(), prodi, semester, ta);
		Grid<KelasPerkuliahan> g = new Grid<>();
		g.setItems(l);
		g.setSizeFull();
		g.setSelectionMode(SelectionMode.MULTI);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(KelasPerkuliahan::getProdi).setCaption("PROGRAM STUDI");
		g.addColumn(KelasPerkuliahan::getDosenPengampu).setCaption("DOSEN");
		g.addColumn(KelasPerkuliahan::getMataKuliah).setCaption("MATAKULIAH");
		g.addColumn(KelasPerkuliahan::getKodeKelas).setCaption("KELAS");
		

		vl.addComponent(g);
		//Label label = new Label("Pilih kelas sebelum mencetak");
		Button cetak = new Button("Absensi");
		cetak.setIcon(VaadinIcons.PRINT);
		cetak.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		cetak.addClickListener(e->{
			if (g.getSelectedItems().size()>0) {
				try {
					List<KelasPerkuliahan> lkp = new ArrayList<>();
					lkp.addAll(g.getSelectedItems());
					 EnhancedBrowserWindowOpener.extendOnce(cetak)
				        .open((cetakAbsensi(lkp)));
					;
				} catch (JRException e1) {
					e1.printStackTrace();
				}
			}
		});
		Button presensi = new Button("Presensi");
		presensi.setIcon(VaadinIcons.PRINT);
		presensi.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		presensi.addClickListener(e->{
			if (g.getSelectedItems().size()>0) {
				try {
					List<KelasPerkuliahan> lkp = new ArrayList<>();
					lkp.addAll(g.getSelectedItems());
					 EnhancedBrowserWindowOpener.extendOnce(cetak)
				        .open((cetakAbsensiKosong(lkp)));
					;
				} catch (JRException e1) {
					e1.printStackTrace();
				}
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(cetak,presensi);
		vl.addComponents(hl);
		content.setContent(vl);
	}

	private StreamResource cetakAbsensiKosong(List<KelasPerkuliahan> lkp) throws JRException {
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportAbsenKosong(lkp);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Presensi"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}

	protected StreamResource cetakAbsensi(List<KelasPerkuliahan> lkp) throws JRException {
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportAbsensiHarian(lkp);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Berita Acara Perkuliahan"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
		//getUI().getPage().open(resource, "_blank", false);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
