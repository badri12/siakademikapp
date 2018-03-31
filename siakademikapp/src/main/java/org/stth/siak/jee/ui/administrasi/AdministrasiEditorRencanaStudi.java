package org.stth.siak.jee.ui.administrasi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.MataKuliahRencanaStudiPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.RencanaStudiCreationMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulAdditionMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulKeterangan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.helper.RencanaStudiManualHelper;
import org.stth.siak.util.StringToDouble;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiEditorRencanaStudi extends Window{
	private static final long serialVersionUID = 1L;
	private Panel identitas;
	private Panel daftarMataKuliah;
	private RencanaStudiMahasiswa rsmh;
	private Binder<RencanaStudiMahasiswa> binderRSM;
	private int totSKS ;
	private DosenKaryawan user;
	private KonfigurasiPersistence k;
	private TextField totalSKS;
	private Mahasiswa mhs;
	private RencanaStudiManualHelper rsmhelp;
	private TextField ipk;
	private ComboBox<DosenKaryawan> dosenPA;
	private DateField disusun;
	private ComboBox<StatusRencanaStudi> status;

	public AdministrasiEditorRencanaStudi(RencanaStudiMahasiswa rsm) {
		VerticalLayout vl = new VerticalLayout();
		k = new KonfigurasiPersistence();
		rsmh=rsm;
		if (rsmh==null) {
			rsmh=new RencanaStudiMahasiswa();
			mhs=new Mahasiswa();
			
			rsmh.setMahasiswa(mhs);
			rsmh.setSemester(k.getCurrentSemester());
			rsmh.setTahunAjaran(k.getCurrentTa());
			rsmh.setCreated(LocalDate.now());
			rsmh.setCreationMethod(RencanaStudiCreationMethod.MANUAL_ADMIN);
			
		}

		user=VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);

		binderRSM = new Binder<>(RencanaStudiMahasiswa.class);
		binderRSM.setBean(rsmh);

		identitas= new Panel("Rencana Studi");
		daftarMataKuliah=new Panel("Daftar Pilihan MataKuliah");
		buildIdentitas();
		daftarMataKuliah.setContent(buildRencanaStudiMatkul());
		vl.addComponents(identitas, daftarMataKuliah);
		vl.setMargin(true);
		setContent(vl);
		center();
		setWidth("700px");
		setModal(true);

	}

	@SuppressWarnings("serial")
	private void buildIdentitas() {
		FormLayout flkiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		
		Mahasiswa example = new Mahasiswa();
		List<Mahasiswa> lm = MahasiswaPersistence.getListByExample(example);
		Map<String , Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}
		AutocompleteTextField atfMHs = new AutocompleteTextField("");
		atfMHs.setPlaceholder("Inputkan nim atau nama mahasiswa");
		atfMHs.setWidth("300px");
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		atfMHs.setSuggestionProvider(acsp);
		atfMHs.setMinChars(3);
		atfMHs.addValueChangeListener(c->{
			mhs = mapMHS.get(atfMHs.getValue());
			if (mhs!=null) {
				rsmhelp = new RencanaStudiManualHelper(mhs, k.getCurrentSemester(), k.getCurrentTa(), k.getKRSMaxSKS());
				rsmh=rsmhelp.getRencanaStudi();
				if (rsmh.getId()==0) {
					rsmh.setSubmitted(LocalDate.now());
					rsmh.setApproved(LocalDate.now());
					rsmh.setStatus(StatusRencanaStudi.DIAJUKAN);
					rsmh.setCreated(LocalDate.now());
					rsmh.setCreationMethod(RencanaStudiCreationMethod.MANUAL_ADMIN);
				}
				binderRSM = new Binder<>(RencanaStudiMahasiswa.class);
				binderRSM.setBean(rsmh);
				daftarMataKuliah.setContent(buildRencanaStudiMatkul());
			}
			
		});
		
		
		
		TextField tahunAjaran = new TextField("Tahun Ajaran");
		ComboBox<Semester> semester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		dosenPA= new ComboBox<>("Dosen PA", DosenKaryawanPersistence.getDosen());
		disusun = new DateField("disusun");
		
		ComboBox<RencanaStudiCreationMethod> metode = new ComboBox<RencanaStudiCreationMethod>("Metode"
				, Arrays.asList(RencanaStudiCreationMethod.values()));
		metode.setReadOnly(true);
		status = new ComboBox<>("Status", Arrays.asList(StatusRencanaStudi.DIAJUKAN));
		ipk = new TextField("IPK");
		ipk.setReadOnly(true);
		totalSKS = new TextField("Total SKS");

		totalSKS.setValue(String.valueOf(totSKS));
		binderRSM.forField(atfMHs).asRequired("Inputkan mahasiswa")
		.withConverter(new Converter<String, Mahasiswa>() {
			@Override
			public Result<Mahasiswa> convertToModel(String value, ValueContext context) {
				if (!value.isEmpty()) {
					return Result.ok(mapMHS.get(value));
				}
				return null;
			}

			@Override
			public String convertToPresentation(Mahasiswa value, ValueContext context) {
				if (value!=null) {
					return value.toString();
				}
				return "";
			}
		});
		binderRSM.bind(dosenPA, "pembimbingAkademik");
		binderRSM.bind(disusun, "created");
		binderRSM.bind(metode, "creationMethod");
		binderRSM.bind(status, "status");
		binderRSM.forField(ipk).withConverter(new StringToDouble()).bind("ipk");
		binderRSM.forField(semester).asRequired("Pilih semester").bind("semester");
		binderRSM.forField(tahunAjaran).asRequired("Masukkan tahun ajaran").bind("tahunAjaran");

		flkiri.addComponents(semester, totalSKS, disusun, status);
		flKanan.addComponents(tahunAjaran, ipk, dosenPA, metode);
		if (rsmh.getId()<1) {
			Button simpan = new Button("Simpan");
			simpan.setIcon(VaadinIcons.FILE_TEXT);
			simpan.addStyleName(ValoTheme.BUTTON_PRIMARY);
			simpan.addClickListener(e->{
				if (binderRSM.validate().isOk()) {
					RencanaStudiMahasiswa rsm = binderRSM.getBean();
					GenericPersistence.merge(rsm);
					Notification.show("", "Data berhasil disimpan", Type.HUMANIZED_MESSAGE);
				}
			});
			flKanan.addComponent(simpan);
		}
		VerticalLayout vl = new VerticalLayout();
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flkiri, flKanan);
		hl.setSpacing(true);
		//FormLayout fl = new FormLayout();
		//fl.addComponent(atfMHs);
		vl.addComponents( hl);
		identitas.setContent(vl);
	}

	private Component buildRencanaStudiMatkul() {
		List<RencanaStudiPilihanMataKuliah> l = new ArrayList<>();
		Map<String, MataKuliah> mapRencanaStudi= new HashMap<>();
		if (rsmh.getId()>0) {
			totSKS =0;
			l = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rsmh);
			System.out.println(l);
			if (l!=null) {
				for (RencanaStudiPilihanMataKuliah rspm : l) {
					totSKS+=rspm.getMataKuliah().getSks();
					mapRencanaStudi.put(rspm.getMataKuliah().getNama(), rspm.getMataKuliah());
				}
			}else l = new ArrayList<>();
		}
		totalSKS.setValue(String.valueOf(totSKS));
		Grid<RencanaStudiPilihanMataKuliah> g = new Grid<>();
		g.setItems(l);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(rspm->{
			return rspm.getMataKuliah();
		}).setCaption("MATAKULIAH");
		g.addColumn(RencanaStudiPilihanMataKuliah::getKeterangan).setCaption("KETERANGAN");
		g.addComponentColumn(rspm->{
			Button b = new Button();
			b.setIcon(VaadinIcons.TRASH);
			b.setStyleName(ValoTheme.BUTTON_DANGER);
			b.addClickListener(e->{
				GenericPersistence.delete(rspm);
				daftarMataKuliah.setContent(buildRencanaStudiMatkul());
			});
			return b;
		});
		MataKuliahRencanaStudi mkrs = new MataKuliahRencanaStudi();
		mkrs.setSemester(k.getKRSSemester());
		mkrs.setTahunAjaran(k.getKRSTa());
		List<MataKuliah> lmk = new ArrayList<>();
		List<MataKuliahRencanaStudi> lmkrs = MataKuliahRencanaStudiPersistence.bySample(mkrs);
		for (MataKuliahRencanaStudi mkres : lmkrs) {
			lmk.add(mkres.getMataKuliah());
		}
		
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk : lmk) {
			mapmk.put(mk.toString(), mk);
		}
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		AutocompleteTextField atfmk = new AutocompleteTextField();
		atfmk.setPlaceholder("Inputkan kode atau nama mata kuliah");
		atfmk.setSuggestionProvider(acsp);
		atfmk.setMinChars(3);
		atfmk.setWidth("250px");

		Button add = new Button();
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(klik->{
			if (!atfmk.getValue().isEmpty()) {
				MataKuliah mkpilih= mapmk.get(atfmk.getValue());
				MataKuliah mkSudahDiambil = mapRencanaStudi.get(mkpilih.getNama());
				if(totSKS+mkpilih.getSks()<=k.getKRSMaxSKS()){
					if (mkSudahDiambil==null) {
						RencanaStudiPilihanMataKuliah rspm = new RencanaStudiPilihanMataKuliah(rsmh, mkpilih);
						rspm.setSubmittedBy(user.getNama());
						rspm.setAddMethod(RencanaStudiMatkulAdditionMethod.MANUAL_ADMIN);
						List<PesertaKuliah> lpk=PesertaKuliahPersistence.getByMhsMatkul(mhs, mkpilih.getNama());
						if(lpk.size()>0){
							rspm.setKeterangan(RencanaStudiMatkulKeterangan.MENGULANG);
						}else rspm.setKeterangan(RencanaStudiMatkulKeterangan.REGULER);
						GenericPersistence.merge(rspm);
						daftarMataKuliah.setContent(buildRencanaStudiMatkul());
					}else{
						Notification.show("Matakuliah ini sudah ada dalam daftar rencana studi", Notification.Type.ERROR_MESSAGE);
					}
				}else{
					Notification.show("Total SKS sudah melebihi ketentuan", Notification.Type.ERROR_MESSAGE);
				}
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(false);
		hl.addComponents(atfmk, add);

		HorizontalLayout hlButtonAksi = new HorizontalLayout();
		if (rsmh.getId()>0) {
			Button ulang = new Button("Kembalikan ke draft");
			ulang.setIcon(VaadinIcons.BAN);
			ulang.setStyleName(ValoTheme.BUTTON_DANGER);
			ulang.addClickListener(e->{
				rsmh.setStatus(StatusRencanaStudi.DITOLAK);
				GenericPersistence.merge(rsmh);
				daftarMataKuliah.setContent(buildRencanaStudiMatkul());
				Notification.show("Rencana studi dikembalikan ke draft", Type.HUMANIZED_MESSAGE);
			});
			hlButtonAksi.addComponent(ulang);
		}

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(false);
		vl.setSpacing(false);
		vl.addComponents(hl, g, hlButtonAksi);
		return vl;

	}

}
