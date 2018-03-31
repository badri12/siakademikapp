package org.stth.siak.jee.ui.eis;


import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.MonevKRSMataKuliah;
import org.stth.siak.helper.MonevKRSMataKuliah.RekapPengambilanMataKuliah;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MonitoringPengambilanMataKuliahRencanaStudi extends VerticalLayout implements View{
	
	private static final long serialVersionUID = -5660284900239030833L;
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private Panel p = new Panel("Rekapitulasi Pengambilan Mata Kuliah");
	private Semester semester;
	private String ta;
	protected ProgramStudi prodi;
	private List<RekapPengambilanMataKuliah> rekap;
	private TextField tfa;
	
	public MonitoringPengambilanMataKuliahRencanaStudi() {
		Responsive.makeResponsive(this);
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		addComponent(ViewFactory.header("Monitoring Pengambilan Mata Kuliah Semester "+semester+" T.A "+ ta));
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
		//MonevKRS m = new MonevKRS(semester,ta,Integer.valueOf(tfa.getValue()),prodi);
		int angkatan = 0;
		 
		try {
			angkatan = Integer.valueOf(tfa.getValue());
		} catch (Exception e) {
			//nothing todo here
		}
		MonevKRSMataKuliah m = new MonevKRSMataKuliah(semester, ta, angkatan, prodi); 
		rekap = m.getRekap();
		Grid<RekapPengambilanMataKuliah> g = new Grid<>("",rekap);
		g.setSizeFull();
		g.addColumn(RekapPengambilanMataKuliah::getProdi).setCaption("PRODI");
		g.addColumn(RekapPengambilanMataKuliah::getAngkatan).setCaption("ANGKATAN");
		g.addColumn(RekapPengambilanMataKuliah::getKodeMataKuliah).setCaption("KODE MATAKULIAH");
		g.addColumn(RekapPengambilanMataKuliah::getNamaMataKuliah).setCaption("MATAKULIAH");
		g.addColumn(RekapPengambilanMataKuliah::getSks).setCaption("SKS");
		g.addComponentColumn(rkp->{
			HorizontalLayout hl = new HorizontalLayout();
			Button b = new Button();
			b.setIcon(VaadinIcons.EYE);
			b.setStyleName(ValoTheme.BUTTON_PRIMARY);
			b.addClickListener(e->{
				showPengambilMataKuliah(rkp.getPengambil());
			});
			Label l = new Label();
			l.setCaption(rkp.getPengambil().size()+" ");
			hl.addComponents(l,b);
			return hl;
		});
		
		
		p.setContent(g);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	private void showPengambilMataKuliah(List<Mahasiswa> pengambil) {
		GeneralPopups.showDaftarMahasiswa(pengambil);
	}
	
	

}
