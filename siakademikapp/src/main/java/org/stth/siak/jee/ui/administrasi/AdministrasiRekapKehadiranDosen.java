// 
// Decompiled by Procyon v0.5.30
// 

package org.stth.siak.jee.ui.administrasi;

import com.vaadin.navigator.ViewChangeListener;

import org.stth.siak.helper.MonevKehadiranDosen;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.util.ExportToExcel;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;
import org.apache.poi.ss.usermodel.Workbook;
import org.stth.jee.persistence.KonfigurasiPersistence;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import java.util.List;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.DosenKaryawan;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import org.stth.siak.enumtype.Semester;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiRekapKehadiranDosen extends VerticalLayout implements View
{

	private static final long serialVersionUID = -3132557413309643943L;
	private Semester semester;
	private String ta;
	private DateField periodStart;
	private DateField periodEnd;
	private Panel pnl = new Panel("Daftar Hasil Penyaringan");
	protected DosenKaryawan dosen;
	private Button export = new Button("File Excel");
	protected KelasPerkuliahan kelasPerkuliahan;
	//private List<UserAccessRightsAdministrasi> lacl;

	public AdministrasiRekapKehadiranDosen() {
		this.periodStart = new DateField("Periode Mulai");
		this.periodEnd = new DateField("Periode End");
		this.setMargin(true);
		this.setSpacing(true);
		Responsive.makeResponsive(new Component[] { this });
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		this.semester = k.getKRSSemester();
		this.ta = k.getKRSTa();
		//this.lacl = (List)VaadinSession.getCurrent().getAttribute("admrights");
		this.addComponent(ViewFactory.header("Catatan Perkuliahan Semester " + this.semester + " t.a " + this.ta));
		this.addComponent(this.createFilterComponent());
		this.addComponent(pnl);
		
		export.setIcon(VaadinIcons.DOWNLOAD);
		export.setStyleName(ValoTheme.BUTTON_PRIMARY);
		export.setVisible(false);
		addComponent(export);
		this.addComponent(ViewFactory.footer());
	}

	private Component createFilterComponent() {
		Panel pnl = new Panel("Filter Data");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		LocalDate ld = LocalDate.now();
		this.periodEnd.setValue(ld);
		//this.periodStart.setValue((Object)c.getTime());
		ld=ld.minusMonths(1);
		this.periodStart.setValue(ld);
		Button saring = new Button("Saring");
		saring.setIcon(VaadinIcons.SEARCH);
		saring.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saring.addClickListener(e->{
			saringData();
		});
		hl.addComponents(periodStart, periodEnd, saring);
		hl.setComponentAlignment(saring, Alignment.BOTTOM_LEFT);
		pnl.setContent(hl);
		return pnl;
	}


	protected void saringData() {
		LocalDateTime dt1 = LocalDateTime.of(periodStart.getValue(), LocalTime.MIN);
		LocalDateTime dt2 = LocalDateTime.of(periodEnd.getValue(), LocalTime.MAX);
		MonevKehadiranDosen mnv = new MonevKehadiranDosen(dt1, dt2);
		List<MonevKehadiranDosen.RekapKehadiranDosen> l = mnv.getRekap();
		Grid<MonevKehadiranDosen.RekapKehadiranDosen> g = new Grid<>();

		g.setItems(l);
		//		g.addComponentColumn(rk-> {
		//			Label lab = new Label(String.valueOf(l.indexOf(rk)+1));
		//			return lab; 
		//		});
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(MonevKehadiranDosen.RekapKehadiranDosen::getDosen).setCaption("DOSEN");
		g.addColumn(MonevKehadiranDosen.RekapKehadiranDosen::getTotSKS).setCaption("TOTAL SKS");
		g.addColumn(rk->{
			return rk.getLogHadir().size();
		}).setCaption("TOTAL HADIR");
		pnl.setContent(g);
		if (l.size()>0) {
			exportExcel(l);
		}
		
	}
	public void exportExcel(List<MonevKehadiranDosen.RekapKehadiranDosen> l){
		try {
			Workbook wb = ExportToExcel.kehadiranDosen("REKAP "+periodStart.getValue().toString()+" - "
		+periodEnd.getValue().toString(), l);
			Resource rs = ExportToExcel.createFileExcel(wb , "rekap dosen");
			FileDownloader fd = new FileDownloader(rs);
			fd.extend(export);
			export.setVisible(true);
		} catch (IOException e1) {	
			e1.printStackTrace();
		}
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}
}
