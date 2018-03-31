package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.ui.util.ConfirmDialog;
import org.stth.siak.ui.util.FileUploader;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ImportFromExcel;
import org.stth.siak.util.StringToDouble;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
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

public class AdministrasiEditorKelasPerkuliahan extends Window{
	private static final long serialVersionUID = 1L;
	private List<PesertaKuliah> lPK;
	private KelasPerkuliahan kp;
	private Binder<KelasPerkuliahan> binder;
	private Panel panelPeserta = new Panel("Peserta Kuliah");
	private ComboBox<Semester> cbSemester;
	private TextField tahunAjaran;
	private KonfigurasiPersistence k;
	private ComboBox<MataKuliah> cbMk;
	private ComboBox<ProgramStudi> cbProdi;
	private Map<Mahasiswa, PesertaKuliah> mapPK;

	public AdministrasiEditorKelasPerkuliahan(KelasPerkuliahan kp) {
		setCaption("Edit Kelas Perkuliahan");
		k = new KonfigurasiPersistence();
		if(kp==null) {
			kp =new KelasPerkuliahan();
			kp.setSemester(k.getCurrentSemester());
			kp.setTahunAjaran(k.getCurrentTa());
		}

		this.kp=kp;
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(buildContentIdentitas());
		panelPeserta.setContent(buildContentPesertaKuliah());
		vl.addComponent(panelPeserta);
		Panel root = new Panel();
		root.setContent(vl);
		setContent(root);
		setModal(true);
		center();
		setHeight(95.0f ,Unit.PERCENTAGE);
		setWidth("700px");
	}

	@SuppressWarnings("serial")
	private Component buildContentIdentitas(){	
		binder=new Binder<>(KelasPerkuliahan.class);
		binder.setBean(kp);
		Panel panelidentitas = new Panel("Identitas Kelas");
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();

		cbMk = new ComboBox<>("Mata Kuliah");
		cbMk.setPlaceholder("Inputkan kode atau nama matakuliah");
		cbMk.setWidth("200px");

		List<DosenKaryawan> lDosen= DosenKaryawanPersistence.getDosen(); 
		Map<String, DosenKaryawan> mapDosen= new HashMap<>();
		for (DosenKaryawan d : lDosen) {
			mapDosen.put(d.toString(), d);
		}
		AutocompleteSuggestionProvider acspDosen = new CollectionSuggestionProvider(mapDosen.keySet(),MatchMode.CONTAINS, true);
		AutocompleteTextField tfDosen = new AutocompleteTextField("Dosen");
		tfDosen.setPlaceholder("Inputkan nama dosen");
		tfDosen.setWidth("200px");

		tfDosen.setSuggestionProvider(acspDosen);
		tfDosen.setMinChars(3);

		TextField kodeKelas = new TextField("Kode Kelas");
		kodeKelas.setWidth("200px");

		cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setWidth("200px");

		tahunAjaran = new TextField("Tahun Ajaran");
		tahunAjaran.setWidth("200px");

		cbProdi = new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setWidth("200px");
		cbProdi.addValueChangeListener(e->{
			List<MataKuliah> lMatKul = MataKuliahPersistence.getByProdi(cbProdi.getValue());
			cbMk.setItems(lMatKul);
		});

		
		binder.forField(tfDosen).withConverter(new Converter<String, DosenKaryawan>() {

			@Override
			public Result<DosenKaryawan> convertToModel(String value, ValueContext context) {
				if (!value.isEmpty()) {
					return Result.ok(mapDosen.get(value));
				}
				return Result.ok(null);
			}

			@Override
			public String convertToPresentation(DosenKaryawan value, ValueContext context) {
				if (value!=null) {
					return value.toString();
				}
				return "";
			}
		}).bind("dosenPengampu");
		binder.bind(kodeKelas, "kodeKelas");
		binder.forField(cbSemester).asRequired("Pilih semester").bind("semester");
		binder.forField(tahunAjaran).asRequired("Masukkan tahun ajaran").bind("tahunAjaran");
		binder.forField(cbProdi).asRequired("pilih program studi").bind("prodi");
		binder.forField(cbMk).asRequired("Matakuliah harus diisi").bind("mataKuliah");
		
		Button simpanKelas = new Button("Simpan Kelas");
		simpanKelas.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpanKelas.setIcon(VaadinIcons.FILE_TEXT);
		simpanKelas.addClickListener(e->{
			simpanKelas();
		});

		flKiri.addComponents( cbProdi,cbMk, tfDosen);
		flKanan.addComponents(cbSemester, tahunAjaran, kodeKelas, simpanKelas);

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flKiri, flKanan);
		hl.setSpacing(true);
		hl.setMargin(true);

		panelidentitas.setContent(hl);
		return panelidentitas;
	}

	private void simpanKelas() {
		if (binder.validate().isOk()) {
			kp = binder.getBean();
			if (kp.getId()>0) {
				GenericPersistence.merge(kp);
			}else{
				GenericPersistence.saveAndFlush(kp);;
			}

			Notification.show("Kelas Berhasil disimpan", Type.HUMANIZED_MESSAGE);
		}

	}

	private Component buildContentPesertaKuliah(){
		Grid<PesertaKuliah> g = new Grid<>(PesertaKuliah.class);
		mapPK = new HashMap<>();
		if (kp.getId()>0) {
			lPK = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
			panelPeserta.setCaption("Peserta Kuliah ("+lPK.size()+")");
			g.setItems(lPK);
			if (lPK.size()>0) {
				for (PesertaKuliah pesertaKuliah : lPK) {
					mapPK.put(pesertaKuliah.getMahasiswa(), pesertaKuliah);
				}
			}
		}
		g.setSizeFull();
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(pk->{
			return pk.getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(pk->{
			return pk.getMahasiswa().getNama();
		}).setCaption("NAMA");
		//tfNilai.setEnabled(false);
		TextField tfNilaiAngka = new TextField("");
		//		tfNilaiAngka.addValueChangeListener(v->{
		//			String nilai=tfNilaiAngka.getValue();
		//			String nilaiHuruf ="";
		//			if(nilai!=null){
		//
		//				if (!nilai.isEmpty()) {
		//					Double n = Double.parseDouble(nilai);
		//					if(n>=85){
		//						nilaiHuruf="A";
		//					}else if(n>=70){
		//						nilaiHuruf="B";
		//					}else if(n>=55){
		//						nilaiHuruf="C";
		//					}else if(n>=40){
		//						nilaiHuruf="D";
		//					}else{ 
		//						nilaiHuruf="E";
		//					}
		//				}
		//				tfNilai.setValue(nilaiHuruf);
		//			}
		//		});
		Binder<PesertaKuliah> binder =g.getEditor().getBinder();
		Binding<PesertaKuliah, Double> bindAngka = 
				binder.forField(tfNilaiAngka).withConverter(new StringToDouble()).bind("nilaiAngka");
		g.addColumn(PesertaKuliah::getNilaiAngka).setEditorBinding(bindAngka).setCaption("Angka");
		//g.addColumn(PesertaKuliah::getNilai).setEditorComponent(tfNilai, PesertaKuliah::setNilai).setEditable(false)
		//.setCaption("Huruf");
		g.addColumn(PesertaKuliah::getNilai).setCaption("Huruf");

		g.addComponentColumn(pk->{
			Button delete = new Button();
			delete.setIcon(VaadinIcons.TRASH);
			delete.addStyleName(ValoTheme.BUTTON_DANGER);
			delete.addClickListener(e->{
				ConfirmDialog.show(getUI(), "", "Hapus Peserta \n"+pk.getMahasiswa()+" ?", "Ya", "Tidak", c -> {
					if (c.isConfirmed()) {
						GenericPersistence.delete(pk);
						panelPeserta.setContent(buildContentPesertaKuliah());
					}
				});
			});
			return delete;
		}).setCaption("");
		g.getEditor().setEnabled(true).setBuffered(false);

		Mahasiswa example = new Mahasiswa();
		List<Mahasiswa> lm = MahasiswaPersistence.getListByExample(example);
		Map<String , Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}

		AutocompleteTextField tfPeserta = new AutocompleteTextField();
		tfPeserta.setWidth("300px");
		AutocompleteSuggestionProvider acspMHS = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		tfPeserta.setPlaceholder("input nama atau nim mahasiswa");
		tfPeserta.setSuggestionProvider(acspMHS);
		tfPeserta.setMinChars(3);

		HorizontalLayout hlAddPeserta = new HorizontalLayout();
		Button addPeserta = new Button("");
		addPeserta.setIcon(VaadinIcons.PLUS);
		addPeserta.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		hlAddPeserta.setSpacing(true);
		hlAddPeserta.addComponents(tfPeserta,addPeserta);
		addPeserta.addClickListener(e->{
			if (kp.getId()>0) {
				simpanPeserta(mapPK, mapMHS, tfPeserta);
			}else{
				Notification.show("Simpan kelas terlebih dahulu", Type.ERROR_MESSAGE);
			}

		});
		Button addPesertaKRS = new Button("Tambah peserta kolektif");
		addPesertaKRS.setIcon(VaadinIcons.LIST);
		addPesertaKRS.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		addPesertaKRS.addClickListener(e->{

			if (kp.getId()>0) {
				if (kp.getMataKuliah()!=null) {
					DaftarPesertaPerMatkulKRS pmKRS = new DaftarPesertaPerMatkulKRS(kp);
					Window w =GeneralPopups.showGenericWindowReturn(pmKRS, "Daftar pengambilan matakuliah "
							+" "+k.getKRSTa()+"/"+k.getKRSSemester() +" yang disetujui");
					w.addCloseListener(c->{
						panelPeserta.setContent(buildContentPesertaKuliah());
					});

				}else{
					Notification.show("Matakuliah wajib diisi", Type.ERROR_MESSAGE);
				}

			}else Notification.show("Simpan kelas terlebih dahulu", Type.ERROR_MESSAGE);
		});
		hlAddPeserta.addComponent(addPesertaKRS);
		Button imNilai = new Button("Import Nilai");
		imNilai.setIcon(VaadinIcons.INSERT);
		imNilai.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		String[] typeFile = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		"application/wps-office.xlsx"};
		imNilai.addClickListener(e->{
			FileUploader fu = new FileUploader(null, typeFile, 300000, "xlsx");
			getUI().addWindow(fu);
			fu.addCloseListener(close->{
				if (fu.getFile()!=null) {
					List<Mahasiswa> list =ImportFromExcel.nilaiPerkuliahan(fu.getFile(), lPK);
					if (list.size()>0) {
						String s = "";
						for (Mahasiswa m : list) {
							s+=m.getNpm()+" "+m.getNama()+"\n";
						}
						Notification.show("mahasiswa tidak ditemukan di kelas ini",s, Type.WARNING_MESSAGE);
					}
					panelPeserta.setContent(buildContentPesertaKuliah());
				}

			});
		});
		hlAddPeserta.addComponent(imNilai);
		HorizontalLayout hlButton = new HorizontalLayout();
		Button simpanPeserta = new Button("Simpan Nilai");
		simpanPeserta.setIcon(VaadinIcons.FILE_TEXT);
		simpanPeserta.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpanPeserta.addClickListener(e->{
			for(PesertaKuliah peserta  : lPK){
				GenericPersistence.merge(peserta);
				Notification.show("Nilai Berhasil Disimpan", Notification.Type.HUMANIZED_MESSAGE);
				System.out.println(peserta.getMahasiswa().getNama()+" "+peserta.getNilai());
			}
		});

		hlButton.addComponent(simpanPeserta);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.addComponents(hlAddPeserta, g, hlButton);
		return vl;
	}

	private void simpanPeserta(Map<Mahasiswa, PesertaKuliah> mapPK, Map<String, Mahasiswa> mapMHS,
			AutocompleteTextField tfPeserta) {
		if (!tfPeserta.getValue().isEmpty()) {
			Mahasiswa mahasiswaTerpilih= mapMHS.get(tfPeserta.getValue());
			PesertaKuliah pkTerpilih = mapPK.get(mahasiswaTerpilih);
			if(pkTerpilih!=null){
				Notification.show("Mahasiswa ini sudah ada", Notification.Type.HUMANIZED_MESSAGE);
			}else{
				PesertaKuliah pk = new PesertaKuliah(mahasiswaTerpilih, kp);
				GenericPersistence.merge(pk);
				panelPeserta.setContent(buildContentPesertaKuliah());
			}

		}else{
			Notification.show("Inputkan nim atau nama mahasiswa pada field yang tersedia", Notification.Type.HUMANIZED_MESSAGE);
		}
	}

}
