package org.stth.siak.jee.ui.generalview;

import java.util.List;

import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class KurikulumView extends CustomComponent{
	
	private static final long serialVersionUID = 4001325208244016531L;
	private Kurikulum kr;

	public KurikulumView(Kurikulum kr){
		this.kr = kr;
		VerticalLayout layout = new VerticalLayout();
		//layout.setMargin(true);
		layout.setMargin(false);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	public Component getTable(){
		List<MataKuliahKurikulum> lm = MataKuliahKurikulumPersistence.getByKurikulum(kr);
		Grid<MataKuliahKurikulum> g = new Grid<>("Daftar Mata Kuliah "+ kr.getNama(), lm);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(mkk->{
			return mkk.getMataKuliah().getKode();
		}).setCaption("KODE");
		g.addColumn(mkk->{
			return mkk.getMataKuliah().getNama();
		}).setCaption("NAMA");
		g.addColumn(mkk->{
			return mkk.getMataKuliah().getSks();
		}).setCaption("SKS");
		g.addColumn(MataKuliahKurikulum::getSemesterBuka).setCaption("SEMESTER");
		g.addColumn(MataKuliahKurikulum::getJenis).setCaption("JENIS");
		
		return g;
	}

}
