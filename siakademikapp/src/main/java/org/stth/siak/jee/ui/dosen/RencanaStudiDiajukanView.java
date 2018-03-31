package org.stth.siak.jee.ui.dosen;

import java.util.ArrayList;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.StatusRencanaStudi;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RencanaStudiDiajukanView extends Panel implements View {

	private Label titleLabel;
	private VerticalLayout dashboardPanels;
	private VerticalLayout root;
	private RencanaStudiMahasiswa rsm;
	private Window window;
	public boolean needRefresh=false;


	public RencanaStudiDiajukanView(RencanaStudiMahasiswa rsm, Window win) {
		this.rsm = rsm;
		this.window = win;
		prepareView();
	}

	private void prepareView() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);

		root.addComponent(buildHeader());
		Component content = populateMatkul();
		root.addComponent(content);
		Component aksi = createActionButton();
		root.addComponent(aksi);
		root.setExpandRatio(content, 1);
	}


	private Component createActionButton() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(new Label("Status Rencana Studi: "+rsm.getStatus().toString()));
		if (rsm.getStatus().equals(StatusRencanaStudi.DIAJUKAN)){
			Button buttonSetujui = new Button("SETUJUI");
			buttonSetujui.setIcon(VaadinIcons.CHECK);
			buttonSetujui.setStyleName(ValoTheme.BUTTON_PRIMARY);
			buttonSetujui.addClickListener(e->{
				rsm.setStatus(StatusRencanaStudi.DISETUJUI);
				GenericPersistence.merge(rsm);
				needRefresh = true;
				window.close();
			});
			Button buttonTolak = new Button("TOLAK");
			buttonTolak.setIcon(VaadinIcons.ROTATE_RIGHT);
			buttonTolak.setStyleName(ValoTheme.BUTTON_DANGER);
			buttonTolak.addClickListener(e->{
				rsm.setStatus(StatusRencanaStudi.DITOLAK);
				GenericPersistence.merge(rsm);
				needRefresh = true;
				window.close();
			});
			hl.addComponents(buttonSetujui,buttonTolak);
		}
		return hl;
	}

	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);

		titleLabel = new Label("Verifikasi Rencana Studi Oleh Wali");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(titleLabel);

		return header;
	}

	private Component populateMatkul() {
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		int totSks=0;
		List<MataKuliah> ls = new ArrayList<>();
		List<RencanaStudiPilihanMataKuliah> lsR = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rsm);
		
		if (lsR!=null) {
			for (RencanaStudiPilihanMataKuliah rencanaStudiPilihanMataKuliah : lsR) {
				MataKuliah mk = rencanaStudiPilihanMataKuliah.getMataKuliah();
				ls.add(mk);
				totSks += mk.getSks();
			}
		}else lsR=new ArrayList<>();
		
		Grid<RencanaStudiPilihanMataKuliah> g = new Grid<>("", lsR);
		g.addColumn(rspm->{
			return rspm.getMataKuliah();
		});
		g.addColumn(rspm->{
			return rspm.getMataKuliah().getSks();
		});
		g.addColumn(RencanaStudiPilihanMataKuliah::getKeterangan);
		dashboardPanels.addComponent(g);
		Label ipk = new Label("Indeks Prestasi Kumulatif : "+rsm.getIpk());
		Label totSksL = new Label("Jumlah SKS yang diambil : "+totSks);
		Label nilaiD = new Label("Keterangan : "+ rsm.getRemarks());
		dashboardPanels.addComponents(ipk,totSksL,nilaiD);
		return dashboardPanels;
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}

}
