package org.stth.siak.jee.ui.dosen;

import java.util.List;

import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.PesertaKuliah;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class DosenDaftarHadirMahasiswa extends VerticalLayout{
	
	private static final long serialVersionUID = -3814612184691959776L;
	private KelasPerkuliahan kelasPerkuliahan;
	private LogPerkuliahan logPerkuliahan;
	private List<LogKehadiranPesertaKuliah> l;
	

	public DosenDaftarHadirMahasiswa(LogPerkuliahan log) {
		this.logPerkuliahan = log;
		this.kelasPerkuliahan = log.getKelasPerkuliahan();
		loadDaftarHadir();
		siapkanForm();
	}

	private void siapkanForm() {
		Label identitas = new Label();
		identitas.setValue(logPerkuliahan.getLongDesc());
		Panel pnl = new Panel("Daftar Hadir");
		pnl.setContent(getTable());
		addComponent(pnl);
		
	}

	private Component getTable() {
		Grid<LogKehadiranPesertaKuliah> g = new Grid<>("",l);
		g.addColumn(log->{
			return log.getMahasiswa().getNpm();
		}).setCaption("NIM");
		g.addColumn(log->{
			return log.getMahasiswa().getNama();
		}).setCaption("NAMA");
		g.addColumn(log->{
			if (log.isHadir()) {
				return "Hadir";
			}
			return "";
		}).setCaption("HADIR");
		g.setSizeFull();
		return g;
	}

	private void loadDaftarHadir() {
		l = LogKehadiranPerkuliahanPersistence.getByLogPerkuliahan(logPerkuliahan);
		if (l.size()==0){
			List<PesertaKuliah> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
			for (PesertaKuliah pesertaKuliah : lpk) {
				LogKehadiranPesertaKuliah lkpk = new LogKehadiranPesertaKuliah(logPerkuliahan, pesertaKuliah);
				l.add(lkpk);
			}
		}

	}
}
