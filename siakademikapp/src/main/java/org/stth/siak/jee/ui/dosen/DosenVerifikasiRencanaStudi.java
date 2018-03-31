package org.stth.siak.jee.ui.dosen;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.RencanaStudiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.jee.ui.generalview.MahasiswaProfilView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.jee.ui.mahasiswa.IPKView;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

public class DosenVerifikasiRencanaStudi extends VerticalLayout implements View{

	private static final long serialVersionUID = -5660284900239030833L;
	private Grid<Mahasiswa> g;
	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private boolean isKRSOpen;
	private Panel p ;
	private ComboBox<StatusRencanaStudi> cbStatus;
	//private int limitPengambilanSKS;

	public DosenVerifikasiRencanaStudi() {
		setMargin(true);
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		isKRSOpen = k.isKRSOpen();
		//limitPengambilanSKS = k.getKRSMaxSKS();
		Responsive.makeResponsive(this);
		p=new Panel("DAFTAR STATUS PENGAJUAN RENCANA STUDI OLEH MAHASISWA BIMBINGAN AKADEMIK");
		addComponent(ViewFactory.header("Verifikasi Rencana Studi Semester "+semester+" t.a "+ta ));
		if (isKRSOpen) {
			addComponent(filter());
			p.setContent(getTable());
			addComponent(p);
		} else {
			addComponent(new Label("Masa pengambilan mata kuliah telah ditutup"));
		}
		addComponent(ViewFactory.footer());

	}

	private Component filter(){
		FormLayout fl = new FormLayout();
		cbStatus = new ComboBox<>("Status", Arrays.asList(StatusRencanaStudi.values()));
		cbStatus.setValue(StatusRencanaStudi.DIAJUKAN);
		cbStatus.addValueChangeListener(e->{
			p.setContent(getTable());
		});
		fl.addComponent(cbStatus);
		return fl;
	}

	private Component getTable(){
		List<Mahasiswa> lm = new ArrayList<>();
		StatusRencanaStudi sRS =cbStatus.getValue();
		List<Mahasiswa> lmhs = MahasiswaPersistence.getListByPembimbingAkademik(dosen);
		if (sRS!=null) {
//			if (sRS.equals(StatusRencanaStudi.BELUM_KRS)) {
//				for (Mahasiswa mahasiswa : lmhs) {
//					RencanaStudiMahasiswa rsmcur = RencanaStudiPersistence.getByMhsSemTa(mahasiswa, semester, ta);
//					if (rsmcur==null) {
//						lm.add(mahasiswa);
//					}
//				}
//			}else{
				RencanaStudiMahasiswa rsm = new RencanaStudiMahasiswa();
				Mahasiswa m = new Mahasiswa();
				m.setPembimbingAkademik(dosen);
				rsm.setMahasiswa(m);
				rsm.setStatus(sRS);
				rsm.setSemester(semester);
				rsm.setTahunAjaran(ta);
				List<RencanaStudiMahasiswa> lrs = RencanaStudiPersistence.getList(rsm );
				for (RencanaStudiMahasiswa rs : lrs) {
					if (dosen.getId()==rs.getMahasiswa().getPembimbingAkademik().getId()) {
						lm.add(rs.getMahasiswa());
					}
				}
//			}

		}else{
			lm.addAll(lmhs);
		}

		//dashboardPanels = new VerticalLayout();
		//dashboardPanels.addStyleName("dashboard-panels");
		//Responsive.makeResponsive(dashboardPanels);
		g = new Grid<>("",lm);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(Mahasiswa::getNpm).setCaption("NIM");
		g.addColumn(Mahasiswa::getNama).setCaption("NAMA");
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
			RencanaStudiMahasiswa rsm = RencanaStudiPersistence.getByMhsSemTa(mhs, semester, ta);
			Button bVerif = new Button("Belum Mengisi KRS");
			bVerif.setEnabled(false);
			if (rsm!=null) {
				bVerif.setIcon(VaadinIcons.EYE);
				bVerif.setCaption(rsm.getStatus()+" | LIHAT");
				if (rsm.getStatus().equals(StatusRencanaStudi.DISETUJUI)) {
					bVerif.setStyleName(ValoTheme.BUTTON_PRIMARY);

				}else{ 
					bVerif.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				}
				bVerif.setEnabled(true);
				bVerif.addClickListener(e->{
					showRencanaStudiMahasiswa(rsm);
				});
			}

			hl.addComponents(bVerif);
			return hl;

		}).setCaption("RENCANA STUDI");
		g.addComponentColumn(mhs->{
			HorizontalLayout hl = new HorizontalLayout();
			Button bProf = new Button("Profil");
			bProf.setIcon(VaadinIcons.EYE);
			bProf.setStyleName(ValoTheme.BUTTON_PRIMARY);
			bProf.addClickListener(e->{
				showProfilMahasiswa(mhs);
			});
			Button bTranskrip = new Button("Transkrip");
			bTranskrip.setIcon(VaadinIcons.EYE);
			bTranskrip.setStyleName(ValoTheme.BUTTON_PRIMARY);
			bTranskrip.addClickListener(e->{
				showIpkMahasiswa(mhs);
			});
			hl.addComponents(bProf, bTranskrip);
			return hl;
		});


		//dashboardPanels.addComponent(g);
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {


	}

	private void showProfilMahasiswa(Mahasiswa m){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new MahasiswaProfilView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	private void showIpkMahasiswa(Mahasiswa m){
		final Window win = new Window("Transkrip Nilai Mahasiswa");
		Component c = new IPKView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}


	private void showRencanaStudiMahasiswa(RencanaStudiMahasiswa rsm) {
		final Window win = new Window("Pengajuan Rencana Studi Mahasiswa");
		final RencanaStudiDiajukanView c = new RencanaStudiDiajukanView(rsm, win);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
		win.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				if (c.needRefresh){
					UI.getCurrent().getPage().reload();
				}
			}
		});
	}

}
