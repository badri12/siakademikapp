package org.stth.siak.rpt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.jee.persistence.MataKuliahKonversiPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKonversi;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.IndeksPrestasiHelper;

import com.vaadin.server.StreamResource;

public class ReportResourceGenerator {
	public static StreamResource cetakTranskripMahasiswa(Collection<Mahasiswa> l) throws JRException{
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		List<Kurikulum> lkur = KurikulumPersistence.getListByProdi(l.iterator().next().getProdi());
		for (Kurikulum	k : lkur) {
			List<Mahasiswa> lm=new ArrayList<>();
			for (Mahasiswa mahasiswa : l) {
				if (mahasiswa.getK().getId()==k.getId()) {
					lm.add(mahasiswa);
				}
			}
			if (lm.size()>0) {
				List<MataKuliahKurikulum> lmkk = MataKuliahKurikulumPersistence.getByKurikulum(k);
				Map<MataKuliah, List<MataKuliah>> mapKonversi=new HashMap<>();
				for (MataKuliahKurikulum mkk : lmkk) {
					List<MataKuliahKonversi> lmkonv = MataKuliahKonversiPersistence.getByMatKul(mkk.getMataKuliah());
					List<MataKuliah> lmkul =new ArrayList<>();
					for (MataKuliahKonversi mkkonv : lmkonv) {
						lmkul.add(mkkonv.getMataKuliah());
					}
					mapKonversi.put(mkk.getMataKuliah(), lmkul);
				}
				for (Mahasiswa m : lm) {
					IndeksPrestasiHelper iph = new IndeksPrestasiHelper(m, lmkk, mapKonversi);
					ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripMahasiswa(iph);
					rrms.add(rrm);
				}
			}
		}
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	public static StreamResource printTranskrip(IndeksPrestasiHelper iph) throws JRException {
		ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripMahasiswa(iph);
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	
	public static StreamResource cetakTranskripWisudaMahasiswa(Set<Mahasiswa> l,String[] lTanggal) throws JRException{
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		Kurikulum k=l.iterator().next().getK();
		List<MataKuliahKurikulum> lmkk = MataKuliahKurikulumPersistence.getByKurikulum(k);
		Map<MataKuliah, List<MataKuliah>> mapKonversi=new HashMap<>();
		for (MataKuliahKurikulum mkk : lmkk) {
			List<MataKuliahKonversi> lmkonv = MataKuliahKonversiPersistence.getByMatKul(mkk.getMataKuliah());
			List<MataKuliah> lmkul =new ArrayList<>();
			for (MataKuliahKonversi mkkonv : lmkonv) {
				lmkul.add(mkkonv.getMataKuliah());
			}
			mapKonversi.put(mkk.getMataKuliah(), lmkul);
		}
		for (Mahasiswa m : l) {
			IndeksPrestasiHelper iph = new IndeksPrestasiHelper(m, lmkk, mapKonversi);
			ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripWisudaMahasiswa(iph,lTanggal[0],lTanggal[1]);
			rrms.add(rrm);
		}
		
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Akademik Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	
	public static StreamResource cetakKHS(List<KelasPerkuliahanMahasiswaPerSemester2> lkpmp) throws JRException{
		List<ReportRawMaterials> rrms  = ReportContentFactory.siapkanKartuHasilStdui(lkpmp);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Kartu Hasil Studi"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	public static StreamResource cetakHasilStudi(Mahasiswa m, Semester sms, String ta, IndeksPrestasiHelper iph) throws JRException{
		ReportRawMaterials rrm = ReportContentFactory.cetakHasilStudi(m, sms, ta, iph);
		List<ReportRawMaterials> rrms = new ArrayList<>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Kartu Hasil Studi"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	public static StreamResource cetakKartuUjian(List<KelasPerkuliahanMahasiswaPerSemester2> selected, JenisUjian tipe) throws JRException{
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportKartuUjian(selected, tipe);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+tipe.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	public static StreamResource cetakSampulUjian(List<KelasPerkuliahan> l, JenisUjian jenis) throws JRException{
		ReportRawMaterials rrm = ReportContentFactory.siapkanReportSampulUjian(l, jenis);
		List<ReportRawMaterials> rrms = new ArrayList<>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+jenis.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	public static StreamResource cetakAbsensiUjian(List<KelasPerkuliahan> l, JenisUjian jenis) throws JRException{
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportAbsenPerkuliahan(l, jenis);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+jenis.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	public static StreamResource cetakAbsenKuliah(List<KelasPerkuliahan> l) throws JRException{
		List<ReportRawMaterials> rrsm = ReportContentFactory.siapkanReportAbsenPerkuliahan(l);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrsm, "Absensi Perkuliahan");
		StreamResource s = rog.exportToPdf();
		return s;
	}
	public static StreamResource cetakAbsenKosong(List<KelasPerkuliahan> l) throws JRException{
		List<ReportRawMaterials> rrsm = ReportContentFactory.siapkanReportAbsenKosong(l);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrsm, "Absensi Perkuliahan");
		StreamResource s = rog.exportToPdf();
		return s;
	}
}
