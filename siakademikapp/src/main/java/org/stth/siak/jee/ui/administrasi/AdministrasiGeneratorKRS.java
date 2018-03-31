package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.MataKuliahRencanaStudiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.RencanaStudiManualHelper;
import org.stth.siak.ui.util.ConfirmDialog;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdministrasiGeneratorKRS extends CustomComponent {
	private static final long serialVersionUID = 4910117823880984225L;
	private KonfigurasiPersistence konf = new KonfigurasiPersistence();
	private TextArea taInfo;
	private String sysOut=new String();
	private ProgramStudi prodi;
	private TextField tfAngkatan;
	private List<Mahasiswa> lMhs;
	private Panel pDaftarMahasiswa;
	private HorizontalLayout hlHasil;
	private TextField taMaxSKS;
	private TextField taTA;
	private Semester s;
	private ComboBox<Semester> cbSemester;
	private ComboBox<ProgramStudi> cbProdi;

	public AdministrasiGeneratorKRS() {
		pDaftarMahasiswa = new Panel("Daftar Mahasiswa");
		pDaftarMahasiswa.setWidth("400px");
		VerticalLayout vl = new VerticalLayout();
		hlHasil = new HorizontalLayout();
		hlHasil.addComponent(pDaftarMahasiswa);
		vl.addComponents(createFilter(), createButton(), hlHasil);
		Panel p = new Panel();
		p.setContent(vl);
		vl.setSpacing(true);
		setCompositionRoot(p);
	}

	private Component createButton() {
		HorizontalLayout hl=new HorizontalLayout();
		Button bSimulasi = new Button("Simulasi");
		bSimulasi.setIcon(VaadinIcons.CALC);
		bSimulasi.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bSimulasi.addClickListener(click->{
			generate(false);
		});
		Button bGenerateKRS = new Button("Generate KRS");
		bGenerateKRS.setIcon(VaadinIcons.CALC_BOOK);
		bGenerateKRS.addStyleName(ValoTheme.BUTTON_PRIMARY);
		bGenerateKRS.addClickListener(gen->{
			ConfirmDialog.show(getUI(), "Konfirmasi", "Anda akan menyimpan rencana studi yang dihasilkan system ?"
					, "Ya", "Tidak", e->{
						if (e.isConfirmed()) {
							generate(true);
						}
					});
		});
		hl.addComponents(bSimulasi, bGenerateKRS);
		return hl;
	}

	private Component createTableMahasiswa() {
		
		Grid<Mahasiswa> gMahasiswa = new Grid<>(lMhs.size()+" Mahasiswa", lMhs);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		gMahasiswa.removeAllColumns();
		gMahasiswa.setSizeFull();
		gMahasiswa.addColumn(value -> "", row).setCaption("NO");
		gMahasiswa.addColumn(Mahasiswa::getNpm).setCaption("NIM");
		gMahasiswa.addColumn(Mahasiswa::getNama).setCaption("NAMA");
		gMahasiswa.addComponentColumn(mhs->{
			Button edit = new Button();
			edit.setStyleName(ValoTheme.BUTTON_DANGER);
			edit.setIcon(VaadinIcons.TRASH);
			edit.addClickListener(klik->{
				lMhs.remove(mhs);
				pDaftarMahasiswa.setContent(createTableMahasiswa());
			});
			return edit;
		});
		return gMahasiswa;
	}


	private Component createFilter(){
		Panel pMahasiswa = new Panel("Kriteria Mahasiswa");
		Panel pSysOut = new Panel("Pesan Sistem");

		ComboBox<DosenKaryawan> cbPA = new ComboBox<>("P.A", DosenKaryawanPersistence.getDosen());
		cbProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));

		tfAngkatan = new TextField("Angkatan");
		tfAngkatan.setValue(String.valueOf(GeneralUtilities.getCurrentYearLocal()));

		Button bLoadMahasiswa = new Button("Load Mahasiswa");
		bLoadMahasiswa.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bLoadMahasiswa.setIcon(VaadinIcons.FOLDER_SEARCH);
		taInfo = new TextArea();
		taInfo.setHeight("400px");
		taInfo.setWidth("350px");
		taInfo.setWordWrap(false);
		bLoadMahasiswa.addClickListener(e->{
			int angkatan = Integer.parseInt(tfAngkatan.getValue());
			DosenKaryawan pa = cbPA.getValue();
			prodi = cbProdi.getValue();
			if (prodi!=null&&angkatan>0) {
				Mahasiswa example = new Mahasiswa();
				example.setAngkatan(angkatan);
				example.setProdi(prodi);
				if (pa!=null) {
					example.setPembimbingAkademik(pa);
				}
				lMhs = MahasiswaPersistence.getListByExample(example);
				Collections.sort(lMhs);
				//				mapMhs = new HashMap<>();
				//				for (Mahasiswa mhs : lMhs) {
				//					mapMhs.put(mhs, mhs);
				//				}
				pDaftarMahasiswa.setContent(createTableMahasiswa());
			}else{
				Notification.show("Angkatan dan Program Studi tidak boleh kosong", Type.ERROR_MESSAGE);
			}

		});

		cbSemester = new ComboBox<>("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(konf.getKRSSemester());

		/*Button bLoadKurikulum = new Button("Load Kurikulum");
		bLoadKurikulum.addClickListener(e->{
			k = (Kurikulum) cbKurikulum.getValue();
			s= (Semester) cbSemester.getValue();
			if (k!=null&&s!=null) {
				List<MataKuliahKurikulum> lMK = MataKuliahKurikulumPersistence.getByKurikulum(k);
				Collections.sort(lMK);
				lMatkul = new ArrayList<>();
				sysOut+="Matakuliah yang tersedia \n";
				sysOut+="======================== \n";
				for (MataKuliahKurikulum mkKurikulum : lMK) {
					if (mkKurikulum.getSemesterTipe()==s) {
						lMatkul.add(mkKurikulum.getMataKuliah());
						sysOut+=" Semester "+String.valueOf(mkKurikulum.getSemesterBuka())+" "
								+mkKurikulum.getMataKuliah() + "\n";
						taInfo.setValue(sysOut);
						taInfo.setCursorPosition(taInfo.getMaxLength());
					}
				}
				sysOut+="Total : "+lMatkul.size()+" Matakuliah \n";
				taInfo.setValue(sysOut);
			}else{
				Notification.show("Kurikukulum dan semester tidak boleh kosong", Type.ERROR_MESSAGE);
			}

		});
		 */

		taMaxSKS = new TextField("Maks SKS");
		taMaxSKS.setValue(String.valueOf(konf.getKRSMaxSKS()));
		taTA = new TextField("T.A");
		taTA.setValue(konf.getKRSTa());


		//flMhs.addComponents(cbProdi, tfAngkatan, cbPA, bLoadMahasiswa);
		//flKur.addComponents(cbKurikulum, cbSemester, bLoadKurikulum);

		//pMahasiswa.setContent(flMhs);
		//pKurikulum.setContent(flKur);
		
		Button bLoadMatkul = new Button("Load Matakuliah");
		bLoadMatkul.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bLoadMatkul.setIcon(VaadinIcons.FOLDER_SEARCH);
		bLoadMatkul.addClickListener(e->{
			isiMatkulTersedia();
		});
		pSysOut.setContent(taInfo);
		hlHasil.addComponent(pSysOut);

		//HorizontalLayout hlKRS = new HorizontalLayout();

		Panel pOther = new Panel("Rencana Studi");
		HorizontalLayout hlOther = new HorizontalLayout();
		FormLayout flKiri= new FormLayout();
		
		FormLayout flMid= new FormLayout();
		flKiri.addComponents(cbSemester, taMaxSKS);
		flMid.addComponents(taTA, bLoadMatkul);
		
		
		hlOther.addComponents(flKiri, flMid);
		//FormLayout flKRS = new FormLayout();
		//flKRS.addComponents(bSimulasi, bGenerateKRS);
		//hlKRS.addComponents(flOther, flKRS);
		pOther.setContent(hlOther);


		//		gl.addComponent(new Button("0,0"), 0 ,0);
		//		gl.addComponent(new Button("0,1"), 0 ,1);
		//		Button b= new Button("0,0,1,1");
		//		b.setSizeFull();
		//		gl.addComponent(b, 1 ,0 ,1 ,1);



		HorizontalLayout hlFilter = new HorizontalLayout();
		FormLayout flMhsKiri = new FormLayout();
		FormLayout flMhsKanan = new FormLayout();
		flMhsKiri.addComponents(cbProdi, tfAngkatan);
		flMhsKanan.addComponents(cbPA, bLoadMahasiswa);
		hlFilter.addComponents(flMhsKiri, flMhsKanan);
		pMahasiswa.setContent(hlFilter);

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(pMahasiswa, pOther);
		return hl;
	}


	private void isiMatkulTersedia() {
		String ta = (taTA.getValue());
		s= cbSemester.getValue();
		prodi = cbProdi.getValue();
		if (!ta.isEmpty()&&s!=null&&prodi!=null) {
			MataKuliahRencanaStudi mkrs = new MataKuliahRencanaStudi();
			mkrs.setProdi(prodi);
			mkrs.setSemester(s);
			mkrs.setTahunAjaran(ta);
			List<MataKuliahRencanaStudi> l = MataKuliahRencanaStudiPersistence.bySample(mkrs );
			Collections.sort(l);
			sysOut="Matakuliah Rencana Studi Tersedia \n";
			taInfo.setValue(sysOut);
			for (MataKuliahRencanaStudi mkr : l) {
				sysOut+=" Semester "+String.valueOf(mkr.getSemesterBuka())+" "
						+mkr.getMataKuliah() + "\n";
				taInfo.setValue(sysOut);
				taInfo.setCursorPosition(taInfo.getMaxLength());
			}
			sysOut+="Total : "+l.size()+" Matakuliah \n";
			taInfo.setValue(sysOut);
		}else{
			Notification.show("", Type.ERROR_MESSAGE);
		}
		
		
	}

	private void generate(boolean saveToDatabase) {
		int maksks = Integer.parseInt(taMaxSKS.getValue());
		String ta = (taTA.getValue());
		s= cbSemester.getValue();
		prodi = cbProdi.getValue();
		if (maksks>0&&!ta.isEmpty()&&s!=null&&prodi!=null) {
			
			sysOut="MULAI \n";
			taInfo.setValue(sysOut);
			int jmlMhs= 1;
			for (Mahasiswa m : lMhs) {
				RencanaStudiManualHelper rsh = new RencanaStudiManualHelper(prodi, s, maksks,ta, m);
				sysOut+=String.valueOf(jmlMhs)+". Rencana Studi untuk "+ m+"\n";
				taInfo.setValue(sysOut);
				rsh.setMahasiswa(m);
				rsh.siapkanRencanaStudiOtomatis(saveToDatabase);
				List<RencanaStudiPilihanMataKuliah> lrs = rsh.getMatkulRencanaStudi();
				int totSKS = 0;
				for (RencanaStudiPilihanMataKuliah rspm : lrs) {
					sysOut+=rspm.getMataKuliah()+" "+rspm.getKeterangan()+"\n";
					totSKS+=rspm.getMataKuliah().getSks();
					taInfo.setValue(sysOut);
					taInfo.setCursorPosition(taInfo.getMaxLength());
				}
				sysOut+="Total SKS : "+String.valueOf(totSKS)+"\n";
				sysOut+="IPK : "+rsh.getIph().getIpk()+"\n";
				sysOut+="Keterangan : "+rsh.getRencanaStudi().getRemarks()+"\n";
				sysOut+="=============================== \n";
				taInfo.setValue(sysOut);
				taInfo.setCursorPosition(taInfo.getMaxLength());
				jmlMhs++;
			}
			sysOut+=String.valueOf(jmlMhs-1)+" Mahasiswa \n";
			sysOut+="SELESAI";
			taInfo.setValue(sysOut);
		}else{
			Notification.show("", Type.ERROR_MESSAGE);
		}
		

	}


}
