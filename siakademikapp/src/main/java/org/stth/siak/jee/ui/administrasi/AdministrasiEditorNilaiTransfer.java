package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.NilaiTransferPersistence;
import org.stth.siak.entity.FileTransferMahasiswa;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.NilaiTransferMataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.ui.util.ConfirmDialog;
import org.stth.siak.ui.util.FileUploader;
import org.stth.siak.ui.util.GeneralPopups;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiEditorNilaiTransfer extends CustomComponent {
	private static final long serialVersionUID = 1385838612627132941L;
	private Panel daftarNilai = new Panel();
	private Mahasiswa m;
	private FileTransferMahasiswa ftm;
	private Button bAddFile;
	public AdministrasiEditorNilaiTransfer(Mahasiswa m) {
		this.m=m;
		bAddFile = new Button("Lihat file");
		bAddFile.setIcon(VaadinIcons.EYE);
		bAddFile.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		ftm=MahasiswaPersistence.getBerkas(m);
		if (ftm==null) {
			ftm=new FileTransferMahasiswa();
			ftm.setMahasiswa(m);
			bAddFile.setCaption("Tambah file");
			bAddFile.setIcon(VaadinIcons.PLUS);
		}

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		daftarNilai.setContent(daftarNilaiTransfer());
		vl.addComponent(identitas());
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(createbutton());
		vl.addComponent(daftarNilai);
		setCompositionRoot(vl);
	}
	private Component createbutton() {
		HorizontalLayout hlButton = new HorizontalLayout();
		Button bAdd = new Button("Nilai Transfer");
		bAdd.setIcon(VaadinIcons.PLUS);
		bAdd.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		bAdd.addClickListener(add->{
			NilaiTransferMataKuliah ntmk = new NilaiTransferMataKuliah();
			ntmk.setMatKulAsal("");
			ntmk.setKodeMKAsal("");
			ntmk.setNilaiHurufAsal("");
			PesertaKuliah pk = new PesertaKuliah();
			pk.setMahasiswa(m);
			ntmk.setNilaiDiakui(pk);
			AdministrasiEditorNilaiTransferMatkul aenTMK = new AdministrasiEditorNilaiTransferMatkul(ntmk);
			Window w = GeneralPopups.showGenericWindowReturn(aenTMK, "");
			w.addCloseListener(close->{
				daftarNilai.setContent(daftarNilaiTransfer());
			});

		});

		bAddFile.addClickListener(e->{
			FileUploader fu = new FileUploader(ftm.getFile(), new String[]{"application/pdf"}, 300000, "pdf");
			getUI().addWindow(fu);
			fu.addCloseListener(el->{
				if (fu.getFile()!=null&&fu.getData()!=null) {
					ftm.setFile(fu.getByte());
					GenericPersistence.merge(ftm);
					bAddFile.setCaption("Lihat file");
					bAddFile.setIcon(VaadinIcons.EYE);
				}
				fu.hapusFile();
			});
		});
		hlButton.addComponents(bAdd, bAddFile);
		return hlButton;
	}
	private Component identitas(){
		HorizontalLayout hl = new HorizontalLayout();
		TextField tfNim = new TextField("NIM");
		tfNim.setValue(m.getNpm());
		tfNim.setWidth("200px");
		tfNim.setReadOnly(true);

		TextField tfNama = new TextField("Nama");
		tfNama.setValue(m.getNama());
		tfNama.setReadOnly(true);
		tfNama.setWidth("200px");
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();

		TextField tfProdi = new TextField("Prodi");
		tfProdi.setValue(m.getProdi().toString());
		tfProdi.setReadOnly(true);
		tfProdi.setWidth("200px");

		TextField tfStatus = new TextField("Status");
		tfStatus.setValue(m.getStatusMasuk().toString());
		tfStatus.setWidth("200px");
		tfStatus.setReadOnly(true);

		flKiri.addComponents(tfNim, tfProdi);
		flKanan.addComponents(tfNama, tfStatus);

		hl.addComponents(flKiri, flKanan);
		return hl;
	}
	private Component daftarNilaiTransfer(){
		List<NilaiTransferMataKuliah> l = NilaiTransferPersistence.getbyMahasiswa(m);

		Grid<NilaiTransferMataKuliah> g = new Grid<>("", l);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(ntfmk->{
			return ntfmk.getKodeMKAsal()+" "+ntfmk.getMatKulAsal();
		}).setCaption("MATAKULIAH ASAL");
		g.addColumn(NilaiTransferMataKuliah::getSksAsal).setCaption("SKS ASAL");
		g.addColumn(NilaiTransferMataKuliah::getNilaiHurufAsal).setCaption("NILAI ASAL");
		g.addColumn(ntfmk->{
			return ntfmk.getNilaiDiakui().getKelasPerkuliahan().getMataKuliah();
		}).setCaption("MATAKULIAH");
		g.addColumn(ntfmk->{
			return ntfmk.getNilaiDiakui().getNilai();
		}).setCaption("NILAI");
		g.addComponentColumn(ntm->{
			HorizontalLayout hl = new HorizontalLayout();
			Button edit = new Button();
			edit.setIcon(VaadinIcons.EDIT);
			edit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			edit.addClickListener(e->{
				AdministrasiEditorNilaiTransferMatkul aenTMK = new AdministrasiEditorNilaiTransferMatkul(ntm);
				Window w = GeneralPopups.showGenericWindowReturn(aenTMK, "");
				w.addCloseListener(close->{
					daftarNilai.setContent(daftarNilaiTransfer());
				});
			});
			Button hapus = new Button();
			hapus.setIcon(VaadinIcons.TRASH);
			hapus.setStyleName(ValoTheme.BUTTON_DANGER);
			hapus.addClickListener(e->{
				ConfirmDialog.show(getUI(), "", "Anda akan menghapus nilai ", "Ya", "Tidak", conf->{
					if (conf.isConfirmed()) {
						PesertaKuliah pk = ntm.getNilaiDiakui();
						GenericPersistence.delete(ntm);
						GenericPersistence.delete(pk);
					}
				});
			});
			hl.addComponents(edit,hapus);
			return hl;
		});

		return g;
	}
}
