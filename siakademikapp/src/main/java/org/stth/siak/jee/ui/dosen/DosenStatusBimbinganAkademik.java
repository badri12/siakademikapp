package org.stth.siak.jee.ui.dosen;


import java.io.IOException;
import java.util.List;

import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ExportToExcel;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenStatusBimbinganAkademik extends VerticalLayout implements View{

	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosen;
	private Panel p = new Panel("DAFTAR MAHASISWA BIMBINGAN AKADEMIK");
	private HorizontalLayout hl = new HorizontalLayout();

	public DosenStatusBimbinganAkademik() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Bimbingan Akademik"));
		p.setContent(getTable());
		addComponent(p);
		addComponent(hl);
		addComponent(ViewFactory.footer());
	}

	private Component getTable(){
		List<Mahasiswa> lm = MahasiswaPersistence.getListByPembimbingAkademik(dosen);
		//dashboardPanels = new VerticalLayout();
		//dashboardPanels.addStyleName("dashboard-panels");
		//Responsive.makeResponsive(dashboardPanels);
		Grid<Mahasiswa> g = new Grid<>("", lm);
		p.setCaption("DAFTAR MAHASISWA BIMBINGAN AKADEMIK ("+lm.size()+")");
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(mhs->{
			return mhs.getNpm();
		}).setCaption("NIM");
		g.addColumn(mhs->{
			return mhs.getNama();
		}).setCaption("NAMA");
		g.addColumn(mhs->{
			return mhs.getProdi().getNama();
		}).setCaption("PRODI");
//		g.addColumn(mhs->{
//			IndeksPrestasiHelper iph = new IndeksPrestasiHelper(mhs);
//			DecimalFormat df = new DecimalFormat("#.00");
//			return df.format(iph.getIpk());
//		}).setCaption("IPK");
		g.addComponentColumn(mhs->{
			HorizontalLayout hl = new HorizontalLayout();
			Button bProfil = new Button("Profil");
			bProfil.setIcon(VaadinIcons.EYE);
			bProfil.addStyleName(ValoTheme.BUTTON_PRIMARY);
			bProfil.addClickListener(e->{
				GeneralPopups.showProfilMahasiswa(mhs);
			});
			Button bTranskrip = new Button("Transkrip");
			bTranskrip.setIcon(VaadinIcons.EYE);
			bTranskrip.addStyleName(ValoTheme.BUTTON_PRIMARY);
			bTranskrip.addClickListener(e->{
				GeneralPopups.showIpkMahasiswa(mhs);
			});
			hl.addComponents(bProfil, bTranskrip);
			return hl;
		});

		Button buttonTranskrip = new Button("Export ");
		buttonTranskrip.setIcon(VaadinIcons.DOWNLOAD);
		buttonTranskrip.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonTranskrip.addClickListener(e->{
			try {
				StreamResource source = ExportToExcel.createFileExcel(ExportToExcel.dataMahasiswaByPA(dosen) , "Daftar Mahasiswa Bimbingan");
				ResourceReference ref = new ResourceReference(source, this, "mhsPA"+source.hashCode());
				this.setResource("mhsPA"+source.hashCode(), source); // now it's available for download
				getUI().getPage().open(ref.getURL(), null);
				//EnhancedBrowserWindowOpener.extendOnce(exportNilai).open(source);
				//getUI().getPage().open(source, "_blank", false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});
		hl.removeAllComponents();
		hl.addComponent(buttonTranskrip);
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {


	}


}
