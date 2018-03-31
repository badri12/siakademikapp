package org.stth.siak.jee.ui.mahasiswa;


import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.RencanaStudiMatkulAdditionMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulKeterangan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.helper.RencanaStudiManualHelper;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportContentFactory;
import org.stth.siak.rpt.ReportOutputGenerator;
import org.stth.siak.rpt.ReportRawMaterials;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RencanaStudiView extends VerticalLayout implements View{

	private static final long serialVersionUID = 3747760953041344405L;
	private Button buttonAutoPick;
	private Semester semester = Semester.GENAP;
	private String ta = "2015-2016";
	private int limitPengambilanSKS = 22;
	private boolean isKRSOpen;
	private Grid<MataKuliahRencanaStudi> tableMkTersedia = new Grid<>();
	private Grid<RencanaStudiPilihanMataKuliah> tableMKterpilih = new Grid<>();
	private RencanaStudiManualHelper rsmh;
	private Mahasiswa mhs;
	private HorizontalLayout tableHl = new HorizontalLayout();
	private VerticalLayout rightContainer = new VerticalLayout();
	private VerticalLayout leftContainer = new VerticalLayout();
	private int sksTotal;
	private Label labelSksTotal= new Label();
	private Button buttonPrint;
	private Button buttonSubmit;
	private HorizontalLayout actionButtonContainer;
	final Logger logger = LoggerFactory.getLogger(RencanaStudiView.class);
	private List<RencanaStudiPilihanMataKuliah> lsRSPM;

	public RencanaStudiView() {
		mhs = VaadinSession.getCurrent().getAttribute(
				Mahasiswa.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		isKRSOpen = k.isKRSOpen();
		limitPengambilanSKS = k.getKRSMaxSKS();
		logger.debug("Semester {} - tahun ajaran {}", semester,ta);
		logger.debug("Limit SKS: " + limitPengambilanSKS);
		if (isKRSOpen) {
			GenericPersistence.closeSession();
			rsmh = new RencanaStudiManualHelper(mhs, semester, ta, limitPengambilanSKS);
			if (mhs.getStatus().equals(StatusMahasiswa.AKTIF)) {
				if (rsmh.isEligibleForEntry()) {
					prepareMainUI();
				} else {
					String textNoAccessRights = "<center><h3>Anda belum memiliki hak akses untuk mengisi rencana studi pada semester ini. "
							+ "<br>Mohon Lengkapi persyaratan di BAAK untuk mendapatkan hak akses."
							+ "<br>Terima Kasih</h3></center>";
					Label noAccessRights = new Label(textNoAccessRights,
							ContentMode.HTML);
					noAccessRights.setStyleName(ValoTheme.LABEL_FAILURE);
					addComponent(new Label(" "));
					addComponent(noAccessRights);
					
				}
			}else{
				String textNoAccessRights = "<center><h3>Anda tidak memiliki hak akses untuk mengisi rencana studi. "
						+ "<br>Status anda saat ini adalah "+mhs.getStatus() +".<br>"
						+ "Terima Kasih</h3></center>";
				Label noAccessRights = new Label(textNoAccessRights,
						ContentMode.HTML);
				noAccessRights.setStyleName(ValoTheme.LABEL_FAILURE);
				addComponent(new Label(" "));
				addComponent(noAccessRights);
			}
			
		} else {
			String textOutsidePeriod = "<center><h3>Masa pengisian rencana studi online telah ditutup. "
					+ "Jika anda memiliki pertanyaan silahkan menghubungi bagian akademik.<br>"
					+ "Terima Kasih</h3></center>";
			Label lblOutsidePeriod = new Label(textOutsidePeriod,
					ContentMode.HTML);
			lblOutsidePeriod.setStyleName(ValoTheme.LABEL_FAILURE);
			addComponent(new Label(" "));
			addComponent(lblOutsidePeriod);
		}
		
	}

	private void prepareMainUI() {
		removeAllComponents();
		addComponent(ViewFactory.header("Rencana Studi "+ semester+" "+ta));
		prepareActionButtons();
		populateMataKuliahTersediaTable();
		populateMataKuliahTerpilihTable();
		addComponent(actionButtonContainer);
		Label lbl = new Label("Status Pengajuan Rencana Studi: "+ rsmh.getRencanaStudi().getStatus());
		Label lblDesc = new Label("<i><small>Informasi urutan status rencana studi: Draft, Diajukan, Disetujui/Ditolak, Final</i></small><p></p>");
		lblDesc.setContentMode(ContentMode.HTML);
		addComponents(lbl);
		addComponents(lblDesc);
		tableHl.setMargin(false);
		tableHl.setSpacing(false);
		tableHl.setSizeFull();
		addComponent(tableHl);
		
		prepareTableMataKulTerpilih();
		Panel pKiri = new Panel("DAFTAR MATA KULIAH TERSEDIA");
		Panel pKanan = new Panel("DAFTAR MATA KULIAH YANG SUDAH DIPILIH");
		pKanan.setContent(tableMKterpilih);
		pKiri.setContent(tableMkTersedia);
		leftContainer.addComponent(pKiri);
		rightContainer.addComponent(pKanan);
		rightContainer.addComponent(labelSksTotal);
		leftContainer.setMargin(false);
		rightContainer.setMargin(false);
		tableHl.addComponent(leftContainer);
		tableHl.addComponent(rightContainer);
		addComponent(ViewFactory.footer());
	}


	private void prepareActionButtons(){
		actionButtonContainer = new HorizontalLayout();
		actionButtonContainer.setSpacing(true);
		
		buttonAutoPick = new Button("Pilih Mata Kuliah Secara Otomatis", klik->{
			rsmh.ambilMataKuliahOtomatis();
			populateMataKuliahTerpilihTable();
		});
		buttonAutoPick.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		buttonAutoPick.setIcon(VaadinIcons.RANDOM);
		buttonPrint = new Button("Cetak", klik->{
			try {
				printRencanaStudi();
			} catch (JRException e) {
				e.printStackTrace();
			}
		});
		buttonPrint.setIcon(VaadinIcons.PRINT);
		buttonPrint.setStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonSubmit = new Button("Ajukan Ke Wali", klik->{
			submitRencanaStudi();
			UI.getCurrent().getPage().reload();
		});
		buttonSubmit.setIcon(VaadinIcons.LEVEL_RIGHT_BOLD);
		buttonSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		actionButtonContainer.addComponents(buttonPrint,buttonSubmit);
		refreshActionButtonsState();
	}

	protected void submitRencanaStudi() {
		RencanaStudiMahasiswa rsm = rsmh.getRencanaStudi();
		rsm.setStatus(StatusRencanaStudi.DIAJUKAN);
		GenericPersistence.merge(rsm);
		rsmh = new RencanaStudiManualHelper(mhs, semester, ta, limitPengambilanSKS);
		//prepareMainUI();

	}

	private void refreshActionButtonsState(){
		StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();

		if (state.equals(StatusRencanaStudi.DRAFT)){
			buttonPrint.setEnabled(false);
			buttonSubmit.setEnabled(true);
			buttonAutoPick.setEnabled(true);
		}
		else if (state.equals(StatusRencanaStudi.DIAJUKAN)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}
		else if (state.equals(StatusRencanaStudi.DITOLAK)){
			buttonPrint.setEnabled(false);
			buttonSubmit.setEnabled(true);
			buttonAutoPick.setEnabled(true);
		}
		else if (state.equals(StatusRencanaStudi.DISETUJUI)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}
		else if (state.equals(StatusRencanaStudi.FINAL)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}

	}

	
	protected void printRencanaStudi() throws JRException {
		List<RencanaStudiMahasiswa> rss = new ArrayList<>();
		rss.add(this.rsmh.getRencanaStudi());
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportRencanaStudi(rss);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Rencana Studi Mahasiswa");
		StreamResource resource = rog.exportToPdf();
 		//getUI().getPage().open(resource, "_blank", false);
		EnhancedBrowserWindowOpener.extendOnce(buttonPrint).open((resource));
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private void populateMataKuliahTersediaTable() {
		List<MataKuliahRencanaStudi> ls = rsmh.getMatkulAvailable();
		tableMkTersedia.setItems(ls);
		tableMkTersedia.addColumn(MataKuliahRencanaStudi::getMataKuliah).setCaption("MATAKULIAH");
		tableMkTersedia.addColumn(mk->{
			return mk.getMataKuliah().getSks();
		}).setCaption("SKS");
		tableMkTersedia.addColumn(MataKuliahRencanaStudi::getSemesterBuka).setCaption("SEMESTER");
		tableMkTersedia.addColumn(mk->{
			String kodemk = mk.getMataKuliah().getNama();
			return rsmh.getNilaiLamaBilaAda(kodemk);
		}).setCaption("GRADE");
		tableMkTersedia.addComponentColumn(mk->{
			Button button = new Button("");
			button.setIcon(VaadinIcons.PLUS);
			button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			button.addClickListener(e->{
				addPilihanMataKuliahRencanaStudi(mk);
				//populateMataKuliahTerpilihTable();
			});
			StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();
			if (state.equals(StatusRencanaStudi.DRAFT)||state.equals(StatusRencanaStudi.DITOLAK)) {
				return button;
			}
			return new Label(" ");
		}).setCaption("AMBIL");
		tableMkTersedia.setSizeFull();
		//tableMkTersedia.setVisibleColumns("mataKuliah","SKS","semesterBuka","oldmark","Aksi");

	}

	private void deleteRencanaStudiPilihanMatakuliah(
			RencanaStudiPilihanMataKuliah o) {
		GenericPersistence.delete(o);
		populateMataKuliahTerpilihTable();
		Notification.show("Perhatian",
				"Mata kuliah " + o.getMataKuliah() +" sudah dihapus dari pilihan rencana studi",
				Notification.Type.HUMANIZED_MESSAGE);
	}
	private void addPilihanMataKuliahRencanaStudi(MataKuliahRencanaStudi mkrs) {
		int sksTambahan = mkrs.getMataKuliah().getSks();
		if ((sksTotal + sksTambahan)>limitPengambilanSKS){
			Notification.show("Perhatian",
					"SKS yang anda ambil melebihi ketentuan",
					Notification.Type.ERROR_MESSAGE);
			return;
		}
		if (rsmh.isLulusPrasyarat(mkrs.getMataKuliah()).size()>0) {
			String nt = "Anda belum lulus matakuliah prasyarat :";
			for(PesertaKuliah pk:rsmh.isLulusPrasyarat(mkrs.getMataKuliah())){
				nt+="\n"+pk.getCopiedNamaMatkul()+" : "+ pk.getNilai();
			}
			Notification.show("Perhatian",	nt,	Notification.Type.ERROR_MESSAGE);
			return;
		}
		String s = rsmh.getNilaiLamaBilaAda(mkrs.getMataKuliah().getNama());
		RencanaStudiMatkulKeterangan k = RencanaStudiMatkulKeterangan.REGULER;
		if (s!=null){
			if (s.equals("A")) {
				Notification.show("Perhatian",
						"Anda pernah mengambil mata kuliah ini dan mendapat nilai " + s,
						Notification.Type.ERROR_MESSAGE);
				return;
			}
			k = RencanaStudiMatkulKeterangan.MENGULANG;
			Notification.show("Perhatian",
					"Anda pernah mengambil mata kuliah ini dan mendapat nilai " + s,
					Notification.Type.WARNING_MESSAGE);
		}
		for (RencanaStudiPilihanMataKuliah rspm : lsRSPM) {
			if (rspm.getMataKuliah().getId()==mkrs.getMataKuliah().getId()) {
				Notification.show("Perhatian",
						"Matakuliah ini sudah ada dalam Rencana Studi Anda",
						Notification.Type.ERROR_MESSAGE);
				return;
			}
		}
		
		RencanaStudiPilihanMataKuliah rspmk = new RencanaStudiPilihanMataKuliah(
				rsmh.getRencanaStudi(), mkrs.getMataKuliah());
		rspmk.setAddMethod(RencanaStudiMatkulAdditionMethod.MANUAL_MAHASISWA);
		rspmk.setSubmittedBy(mhs.getNama());
		rspmk.setKeterangan(k);
		GenericPersistence.merge(rspmk);
		populateMataKuliahTerpilihTable();

	}
	private void populateMataKuliahTerpilihTable() {
		//System.out.println(rsm.getId());
		lsRSPM = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rsmh.getRencanaStudi());
		if (lsRSPM==null) {
			lsRSPM=new ArrayList<>();
		}
		System.out.println(lsRSPM.size());
		sksTotal = 0;
		for (RencanaStudiPilihanMataKuliah rencanaStudiPilihanMataKuliah : lsRSPM) {
			sksTotal = sksTotal + rencanaStudiPilihanMataKuliah.getMataKuliah().getSks();
		}
		tableMKterpilih.setItems(lsRSPM);
		labelSksTotal.setValue("Total SKS : "+sksTotal);

	}

	private void prepareTableMataKulTerpilih() {
		tableMKterpilih.setItems(lsRSPM);
		tableMKterpilih.addColumn(RencanaStudiPilihanMataKuliah::getMataKuliah).setCaption("MATAKULIAH");
		tableMKterpilih.addColumn(mk->{
			return mk.getMataKuliah().getSks();
		}).setCaption("SKS");
		tableMKterpilih.addColumn(RencanaStudiPilihanMataKuliah::getKeterangan).setCaption("KETERANGAN");
		tableMKterpilih.addComponentColumn(mk->{
			Button b = new Button();
			b.setIcon(VaadinIcons.TRASH);
			b.addStyleName(ValoTheme.BUTTON_DANGER);
			b.addClickListener(e->{
				deleteRencanaStudiPilihanMatakuliah(mk);
				//populateMataKuliahTerpilihTable();
			});
			StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();
			if (state.equals(StatusRencanaStudi.DRAFT)||state.equals(StatusRencanaStudi.DITOLAK)) {
				return b;
			}
			return new Label(" ");
		});
		tableMKterpilih.setSizeFull();
		
		//tableMKterpilih.setVisibleColumns("mataKuliah","SKS","keterangan","Aksi");
	}

}
