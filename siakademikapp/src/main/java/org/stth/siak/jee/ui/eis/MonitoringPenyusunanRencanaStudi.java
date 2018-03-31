package org.stth.siak.jee.ui.eis;


import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.MonevKRS;
import org.stth.siak.helper.MonevKRS.RekapMonevKRS;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
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

public class MonitoringPenyusunanRencanaStudi extends VerticalLayout implements View{
	
	private static final long serialVersionUID = -5660284900239030833L;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private Panel p = new Panel("Rekapitulasi Status Penyusunan Rencana Studi");
	private Semester semester;
	private String ta;
	protected ProgramStudi prodi;
	private List<RekapMonevKRS> rekap;
	private TextField tfa;
	
	public MonitoringPenyusunanRencanaStudi() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		addComponent(ViewFactory.header("Monitoring Penyusunan Rencana Studi semester "+semester+" t.a "+ ta));
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		addComponent(p);
		addComponent(ViewFactory.footer());

	}
	
	private void siapkanPilihanProdi(){
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class));
		//cbProdi.setNullSelectionAllowed(false);
		//cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(e->{
			prodi=e.getValue();
		});
	}
	
	private Component createFilterComponent(){
		Panel pnl = new Panel("Pilih program studi dan angkatan");
		HorizontalLayout hl = new HorizontalLayout();
		FormLayout fl1 = new FormLayout();
		FormLayout fl2 = new FormLayout();
		fl1.addComponent(cbProdi);
		tfa = new TextField("angkatan");
		fl2.addComponent(tfa);
		Button buttonProses = new Button("Kalkulasi");
		buttonProses.setIcon(VaadinIcons.CALC);
		buttonProses.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonProses.addClickListener(e->{
			prepareContent();
		});
		hl.addComponent(fl1);
		hl.addComponent(fl2);
		hl.addComponent(buttonProses);
		hl.setComponentAlignment(buttonProses, Alignment.MIDDLE_LEFT);
		hl.setSpacing(true);
		hl.setMargin(true);
		pnl.setContent(hl);
		return pnl;
		
	}
	
	public void prepareContent(){
		int angkatan=0;
		try {
			angkatan = Integer.valueOf(tfa.getValue());
		} catch (Exception e) {
			
		}
		MonevKRS m = new MonevKRS(semester,ta,angkatan,prodi);
		rekap = m.getRekap();
		
		Grid<RekapMonevKRS> g = new Grid<>("", rekap);
		g.setSizeFull();
		g.addColumn(r->{
			return r.getProdi().getNama();
		}).setCaption("PRODI");
		g.addColumn(RekapMonevKRS::getBelumSusun).setCaption("BELUM SUSUN");
		g.addColumn(RekapMonevKRS::getDraft).setCaption("DRAFT");
		g.addColumn(RekapMonevKRS::getDiajukan).setCaption("DIAJUKAN");
		g.addColumn(RekapMonevKRS::getDisetujui).setCaption("DISETUJUI");
		g.addColumn(RekapMonevKRS::getDitolak).setCaption("DITOLAK");
		g.addColumn(RekapMonevKRS::getDosenPa).setCaption("DOSEN P.A");
		
		p.setContent(g);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	
	

}
