package org.stth.siak.jee.ui.generalview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.PesertaKuliah;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class PesertaKelasPerkuliahanView extends CustomComponent{
	
	private static final long serialVersionUID = 4001325208244016531L;
	private KelasPerkuliahan kp;
	private VerticalLayout content = new VerticalLayout();
	private boolean showRekapKehadiran=false;
	private boolean showNilai=false;

	public PesertaKelasPerkuliahanView(KelasPerkuliahan kp){
		this.kp = kp;
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(content);
		content.setSizeUndefined();
		content.addComponent(getTable());
		DosenKaryawan d = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		if (d!=null){
			layout.addComponent(getExcelExporter());
		}
		setCompositionRoot(layout);
	}
	
	private Component getExcelExporter() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Button buttonExport = new Button("Export ke Excel");
		buttonExport.addClickListener(e->{

		});
		//hl.addComponent(buttonExport);
		return hl;
	}
	
	public Component getTable(){

		List<PesertaKuliah> lm = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
		Grid<PesertaKuliah> g = new Grid<>();
		g.setItems(lm);
		g.addColumn(peserta->{
			return peserta.getMahasiswa().getNpm();
		}).setCaption("NIM").setId("nim");
		g.addColumn(peserta->{
			return peserta.getMahasiswa().getNama();
		}).setCaption("NAMA").setId("nama");
		g.addColumn(PesertaKuliah::getNilai).setCaption("NILAI").setId("nilai");
		g.addColumn(peserta->{
			List<LogKehadiranPesertaKuliah> l = LogKehadiranPerkuliahanPersistence.getByPesertaKuliah(peserta);
			return l.size();
		}).setCaption("TOTAL HADIR").setId("kehadiran");
		g.addColumn(peserta->{
			return peserta.getMahasiswa().getPembimbingAkademik().getNama();
		}).setCaption("DOSEN P.A").setId("pa");
		
		String[] s = {"nim","nama","pa"};
		List<String> visColumns= new ArrayList<>(Arrays.asList(s));
		
		if (showNilai){
			visColumns.add("nilai");
		}
		if (showRekapKehadiran){
			visColumns.add("kehadiran");
		}
		g.setSizeFull();
		//table.setVisibleColumns(visColumns.toArray());
		return g;
	}

	public void refreshContent(){
		content.removeAllComponents();
		content.addComponent(getTable());
	}

	public void setNilaiVisible(){
		showNilai = true;
	}
	public void setRekapKehadiranVisible(){
		showRekapKehadiran = true;
	}

}
