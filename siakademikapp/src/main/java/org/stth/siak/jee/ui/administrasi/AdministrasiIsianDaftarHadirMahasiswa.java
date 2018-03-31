package org.stth.siak.jee.ui.administrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.ui.util.ConfirmDialog;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiIsianDaftarHadirMahasiswa extends CustomComponent{

	private static final long serialVersionUID = -6821145374499236035L;
	private KelasPerkuliahan kelasPerkuliahan;
	private LogPerkuliahan logPerkuliahan;
	private List<LogKehadiranPesertaKuliah> l;
	//private Window parent;
	private List<PesertaKuliah> lpk;
	private Map<Mahasiswa, Mahasiswa> mapPesertaAbsen;
	private Panel pnl;
	private Grid<LogKehadiranPesertaKuliah> g;

	public AdministrasiIsianDaftarHadirMahasiswa(LogPerkuliahan log, Window parent) {
		//this.parent = parent;
		this.logPerkuliahan = log;
		this.kelasPerkuliahan = log.getKelasPerkuliahan();
		lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
		siapkanForm();
		loadDaftarHadir();
	}

	private void siapkanForm() {
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		pnl = new Panel("Daftar Hadir");
		Label identitas = new Label();
		identitas.setValue(logPerkuliahan.getLongDesc());
		vl.addComponent(identitas);
		Map<String, PesertaKuliah> MapMhs = new HashMap<>();
		for (PesertaKuliah pk : lpk) {
			MapMhs.put(pk.getMahasiswa().toString(), pk);
		}

		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(MapMhs.keySet(), MatchMode.CONTAINS, true);
		HorizontalLayout hlAdd=new HorizontalLayout();
		AutocompleteTextField atfMhsKelas = new AutocompleteTextField();
		atfMhsKelas.setSuggestionProvider(acsp);
		atfMhsKelas.setPlaceholder("Inputkan nama atau nim Mahasiswa");
		atfMhsKelas.setWidth("250px");
		atfMhsKelas.setMinChars(3);
		Button bAdd = new Button("Tambah");
		bAdd.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		bAdd.setIcon(VaadinIcons.PLUS);
		bAdd.addClickListener(e->{
			PesertaKuliah ps = MapMhs.get(atfMhsKelas.getValue());
			if (ps!=null) {
				Mahasiswa mPeserta = ps.getMahasiswa();
				Mahasiswa mExist = mapPesertaAbsen.get(mPeserta);
				if (mExist!=null) {
					Notification.show("mahasiswa ini sudah ada", Type.ERROR_MESSAGE);
				}else{
					LogKehadiranPesertaKuliah lhpk = new LogKehadiranPesertaKuliah(logPerkuliahan, ps);
					l.add(lhpk);
					listMhsAbsen();
					//loadDaftarHadir();
					pnl.setContent(getTable());
				}
			}

		});
		hlAdd.setSpacing(true);
		hlAdd.addComponents(atfMhsKelas, bAdd);

		pnl.setContent(getTable());
		vl.addComponent(hlAdd);
		vl.addComponent(pnl);
		Button simpan = new Button("Simpan");
		simpan.setStyleName(ValoTheme.BUTTON_PRIMARY);
		simpan.setIcon(VaadinIcons.FILE_TEXT);
		simpan.addClickListener(e->{
			for(LogKehadiranPesertaKuliah lkpk : l){
				GenericPersistence.merge(lkpk);
			}
			Notification.show("Data berhasil disimpan");
			loadDaftarHadir();
		});
		vl.addComponent(simpan);
		setCompositionRoot(vl);
	}

	private Component getTable() {
		g = new Grid<>();
		g.removeAllColumns();
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(logMHS->{
			return logMHS.getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(logMHS->{
			return logMHS.getMahasiswa().getNama();
		}).setCaption("NAMA");
		g.addComponentColumn(logMHS->{
			CheckBox cb = new CheckBox();
			cb.setValue(logMHS.isHadir());
			cb.addValueChangeListener(e->{
				logMHS.setHadir(e.getValue());
			});
			return cb;
		}).setCaption("HADIR");
		g.addComponentColumn(logMHS->{
			Button hapus = new Button();
			hapus.setStyleName(ValoTheme.BUTTON_DANGER);
			hapus.setIcon(VaadinIcons.TRASH);
			hapus.addClickListener(e->{
				ConfirmDialog.show(this.getUI(), "", "Anda akan menghapus "+logMHS.getMahasiswa().toString()
						+ " dari absensi kehadiran ?"
						, "Ya", "Tidak", conf->{
							if (conf.isConfirmed()) {
								if (logMHS.getId()>0) {
									l.remove(logMHS);
									listMhsAbsen();
									GenericPersistence.delete(logMHS);
								}else{
									l.remove(logMHS);
									listMhsAbsen();
								}
								pnl.setContent(getTable());
							}
						});
			});
			return hapus;
		});

		return g;
	}

	private void loadDaftarHadir() {
		l = LogKehadiranPerkuliahanPersistence.getByLogPerkuliahan(logPerkuliahan);
		System.out.println(l.size());
		if (l.size()==0){
			//List<PesertaKuliah> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
			int i=1;
			for (PesertaKuliah pesertaKuliah : lpk) {
				System.out.println(i);
				LogKehadiranPesertaKuliah lkpk = new LogKehadiranPesertaKuliah(logPerkuliahan, pesertaKuliah);
				l.add(lkpk);i++;
			}
			System.out.println(lpk.size());
		}
		listMhsAbsen();
		g.setItems(l);
	}

	private void listMhsAbsen() {
		mapPesertaAbsen = new HashMap<>();
		for (LogKehadiranPesertaKuliah lhpk : l) {
			mapPesertaAbsen.put(lhpk.getMahasiswa(), lhpk.getMahasiswa());
		}
	}
}
