package org.stth.siak.rpt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.jee.persistence.PersistenceQuery;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.util.GeneralUtilities;

import com.ibm.icu.text.DecimalFormat;

public class ReportContentFactory {
	public enum tipeAbsen{
		KEHADIRAN,UTS,UAS
	}
	public static ReportRawMaterials siapkanReportTranskripWisudaMahasiswa(IndeksPrestasiHelper iph,String tanggalLulus, String tanggalTranskrip ) {
		Map<String,Object> parameters = new HashMap<>();
		Mahasiswa curMhs = iph.getMhs();
		List<?> l = iph.getTranskripWisudaReportElements();
		String rptFile = "TranskripWisuda.jrxml";
		parameters.put("nim", curMhs.getNpm());
		parameters.put("nama", curMhs.getNama());
		parameters.put("prodi", curMhs.getProdi().getNama());
		parameters.put("ttl",curMhs.getTtldiIjazah());
		parameters.put("totsks", iph.getTotsks());
		DecimalFormat df = new DecimalFormat("#.00");
		String ipk = df.format(iph.getIpk());
		parameters.put("ipk", ipk);
		int kum = (int) iph.getTotnilai();
		parameters.put("tanggalLulus", tanggalLulus);
		parameters.put("tanggalTranskrip", tanggalTranskrip);
		parameters.put("noSeriIjazah", curMhs.getNoSeriIjazah());
		parameters.put("printJudul", curMhs.getJudulSkripsi());
		parameters.put("jumnilai", kum);
		parameters.put("batasNo", (iph.getTranskripReportElements().size()/2)+2);
		parameters.put("timestamp",GeneralUtilities.getLongFormattedDate(LocalDate.now()));
		parameters.put("dekanname", "H. Muh. Djamaluddin, BE. M.Kom");
		parameters.put("dekannis", "830785720105003");
		parameters.put("predikat", iph.getPredikat());
		
		String title = "RPT: Transkrip Mahasiswa";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, l, title);
		return rrm;
	}
	
	public static ReportRawMaterials siapkanReportTranskripMahasiswa(IndeksPrestasiHelper iph) {
		Map<String,Object> parameters = new HashMap<>();
		Mahasiswa curMhs = iph.getMhs();
		List<?> l = iph.getTranskripByKurikulum();
		String rptFile = "Transkrip.jrxml";
		parameters.put("nim", curMhs.getNpm());
		parameters.put("nama", curMhs.getNama());
		parameters.put("prodi", curMhs.getProdi().getNama());
		parameters.put("ttl",curMhs.getTempatLahir()+"/"+GeneralUtilities.getLongFormattedDate2(curMhs.getTanggalLahir()));
		parameters.put("totsks", iph.getTotsks());
		DecimalFormat df = new DecimalFormat("#.00");
		String ipk = df.format(iph.getIpk());
		parameters.put("ipk", ipk);
		int kum = (int) iph.getTotnilai();
		parameters.put("jumnilai", kum);
		parameters.put("timestamp",new Date());
		parameters.put("puket1name", "Hariman Bahtiar, M.Kom.");
		parameters.put("puket1nidn", "0826118001");
		parameters.put("predikat", iph.getPredikat());
		
		String title = "RPT: Transkrip Mahasiswa";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, l, title);
		return rrm;
	}	
	public static List<ReportRawMaterials> siapkanReportTranskrip(List<IndeksPrestasiHelper> iphs) {
		List<ReportRawMaterials> rrms = new ArrayList<>();
		for (IndeksPrestasiHelper iph : iphs) {
			ReportRawMaterials rrm = siapkanReportTranskripMahasiswa(iph);
			rrms.add(rrm);
		}
		return rrms;
	}
	public static List<ReportRawMaterials> siapkanReportRencanaStudi(List<RencanaStudiMahasiswa> rss) {
		List<ReportRawMaterials> rrms = new ArrayList<>();
		for (RencanaStudiMahasiswa rencanaStudiMahasiswa : rss) {
			ReportRawMaterials rrm = siapkanReportRencanaStudi(rencanaStudiMahasiswa);
			rrms.add(rrm);
		}
		return rrms;
	}
	private static ReportRawMaterials siapkanReportRencanaStudi(
			RencanaStudiMahasiswa rs) {
		List<?> lrspm = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rs);
		List<RencanaStudiReportElement> lrsre = new ArrayList<>();
		int totSks=0;
		for (Object object : lrspm) {
			RencanaStudiPilihanMataKuliah rsmpm = (RencanaStudiPilihanMataKuliah) object;
			RencanaStudiReportElement rspe = new RencanaStudiReportElement(rsmpm);
			lrsre.add(rspe);
			totSks += rsmpm.getMataKuliah().getSks();
		}
		Map<String,Object> parameters = new HashMap<>();
		Mahasiswa curMhs = rs.getMahasiswa();
		String rptFile = "RencanaStudi.jrxml";
		parameters.put("nim", curMhs.getNpm());
		parameters.put("nama", curMhs.getNama());
		parameters.put("prodi", curMhs.getProdi().getNama());
		parameters.put("totsks", totSks);
		parameters.put("semester", rs.getSemester().toString());
		parameters.put("keterangan", rs.getRemarks());
		parameters.put("tahunajaran", rs.getTahunAjaran());
		parameters.put("statusrencanastudi", rs.getStatus().toString());
		DecimalFormat df = new DecimalFormat("#.00");
		String ipk = df.format(rs.getIpk());
		parameters.put("ipk", ipk);
		parameters.put("timestamp",new Date());
		if (rs.getPembimbingAkademik()!=null) {
			parameters.put("pembimbingakademik", rs.getPembimbingAkademik());
			if (rs.getPembimbingAkademik().getNidn()!=null) {
				parameters.put("pembimbingnidn", rs.getPembimbingAkademik().getNidn());
			}else
			parameters.put("pembimbingnidn", "-");
		}
		String title = "RPT: Rencana Studi";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, lrsre, title);
		return rrm;
	}
	public static List<ReportRawMaterials> siapkanReportAbsenPerkuliahan(List<KelasPerkuliahan> rss) {
		return siapkanReportAbsenPerkuliahan(rss, null, false);
	}
	public static List<ReportRawMaterials> siapkanReportAbsenKosong(List<KelasPerkuliahan> rss) {
		return siapkanReportAbsenPerkuliahan(rss, null, true);
	}
	public static List<ReportRawMaterials> siapkanReportAbsenPerkuliahan(List<KelasPerkuliahan> rss, JenisUjian tipe) {
		return siapkanReportAbsenPerkuliahan(rss, null, false);
	}
	public static List<ReportRawMaterials> siapkanReportAbsenPerkuliahan(List<KelasPerkuliahan> rss, JenisUjian tipe, boolean kosong) {
		List<ReportRawMaterials> rrms = new ArrayList<>();
		for (KelasPerkuliahan kp : rss) {
			List<?> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
			List<PesertaKuliahReportElement> lpkre = new ArrayList<>();
			for (Object object : lpk) {
				PesertaKuliah pk = (PesertaKuliah) object;
				List<KehadiranPesertareportElement> lkpre ;
				if (kosong) {
					lkpre =siapkanRekapAbsensiKosong(pk);
				}else{
					lkpre =siapkanRekapAbsensi(pk, kp);
				}
				
				PesertaKuliahReportElement pkre = new PesertaKuliahReportElement(pk);
				pkre.setLkpre(lkpre);
				lpkre.add(pkre);
			}
			ReportRawMaterials rrm;
			if (tipe!=null) {
				switch (tipe) {
				case UTS:
					rrm = siapkanReportAbsensiUjian(JenisUjian.UTS, kp, lpkre);
					break;
				case UAS:
					rrm = siapkanReportAbsensiUjian(JenisUjian.UAS, kp, lpkre);
					break;
				default:
					rrm = siapkanReportAbsensiMahasiswa(kp, lpkre);
				}
			} else {
				rrm = siapkanReportAbsensiMahasiswa(kp, lpkre);
			}
			rrms.add(rrm);
		}
		return rrms;
	}
	
	public static ReportRawMaterials siapkanReportAbsensiMahasiswa(KelasPerkuliahan curObject, List<PesertaKuliahReportElement> pesertaKuliah) {
		Map<String,Object> parameters = new HashMap<>();
		String rptFile = "KelasPerkuliahanAbsensi.jrxml";
		parameters.put("title", "RPT: Absensi Mahasiswa");
		parameters.put("matakuliah", curObject.getMataKuliah().toString());
		if (curObject.getDosenPengampu()!=null){
			parameters.put("dosen", curObject.getDosenPengampu().getNama());
		} else {
			parameters.put("dosen", "-");
		}
		parameters.put("kodekelas", curObject.getKodeKelas());
		parameters.put("prodi", curObject.getProdi().toString());
		parameters.put("semta", curObject.getSemester().toString()+"/"+curObject.getTahunAjaran());
		parameters.put("timestamp",new Date());
		String title = "RPT: Absensi Mahasiswa";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, pesertaKuliah, title);
		return rrm;		
	}
	public static List<ReportRawMaterials> siapkanReportKartuUjian(List<KelasPerkuliahanMahasiswaPerSemester2> curSelection,JenisUjian tipe) {
		List<ReportRawMaterials> rrms = new ArrayList<>();
		for (KelasPerkuliahanMahasiswaPerSemester2 o : curSelection) {
			ReportRawMaterials rrm;
			rrm = siapkanReportKartuUjian(o, tipe);
			rrms.add(rrm);
		}
		return rrms;
	}
	public static ReportRawMaterials siapkanReportKartuUjian(KelasPerkuliahanMahasiswaPerSemester2 o,JenisUjian jenisUjian) {
		List<?> lpk = PesertaKuliahPersistence.getByMhsSmsTa(o.getMahasiswa(), o.getSemester(), o.getTahunAjaran());
		List<KartuUjianReportElement> lpkre = new ArrayList<>();
		int totSks = 0;
		for (Object object : lpk) {
			PesertaKuliah pk = (PesertaKuliah) object;
			lpkre.add(new KartuUjianReportElement(pk));
			totSks+=pk.getCopiedSKSMatkul();
		}
		Mahasiswa curMhs = o.getMahasiswa();
		Map<String,Object> parameters = new HashMap<>();
		String rptFile = "KartuUjian.jrxml";
		parameters.put("jenisujian", jenisUjian.toString());
		parameters.put("nim", curMhs.getNpm());
		parameters.put("nama", curMhs.getNama());
		parameters.put("prodi", curMhs.getProdi().getNama());
		parameters.put("semester", o.getSemester().toString());
		parameters.put("tahunajaran", o.getTahunAjaran());
		parameters.put("totsks", totSks);
		parameters.put("timestamp",new Date());
		if (curMhs.getPembimbingAkademik()!=null) {
			parameters.put("pembimbingakademik", curMhs.getPembimbingAkademik());
		}
		if (curMhs.getProdi().getKaprodi()!=null) {
			parameters.put("kaprodi", curMhs.getProdi().getKaprodi().getNama());
			if (curMhs.getProdi().getKaprodi().getNidn()!=null) {
				parameters.put("kaprodinidn", curMhs.getProdi().getKaprodi().getNidn());
			}else parameters.put("kaprodinidn", "-");
			
		}
		String title = "RPT: Rencana Studi";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, lpkre, title);
		return rrm;		
	}
	public static ReportRawMaterials siapkanReportDaftarMahasiswaPerKelas(KelasPerkuliahan curObject, ArrayList<PesertaKuliahReportElement> pesertaKuliah) {
		Map<String,Object> parameters = new HashMap<>();
		String rptFile = "KelasPerkuliahanDetail.jrxml";
		parameters.put("title", "Daftar Mahasiswa per Kelas");
		parameters.put("matakuliah", curObject.getMataKuliah().toString());
		if (curObject.getDosenPengampu()!=null){
			parameters.put("dosen", curObject.getDosenPengampu().getNama());
		} else {
			parameters.put("dosen", "-");
		}
		parameters.put("prodi", curObject.getProdi().toString());
		parameters.put("semta", curObject.getSemester().toString()+"/"+curObject.getTahunAjaran());
		parameters.put("timestamp", new Date());
		parameters.put("kodekelas", curObject.getKodeKelas());
		String title = "RPT: Daftar Peserta Kuliah";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, pesertaKuliah, title);
		return rrm;
	}
	public static ReportRawMaterials siapkanReportSampulUjian(List<KelasPerkuliahan> kps, JenisUjian jenis) {
		
		ArrayList<SampulUjianReportElement> smpls = new ArrayList<>();
		for (KelasPerkuliahan kelasPerkuliahan : kps) {
			SampulUjianReportElement smpl = new SampulUjianReportElement(kelasPerkuliahan);
			smpls.add(smpl);
		}
		Map<String,Object> parameters = new HashMap<>();
		if (jenis == JenisUjian.UTS){
			parameters.put("title", "UJIAN TENGAH SEMESTER (UTS)");
		}
		if (jenis == JenisUjian.UAS){
			parameters.put("title", "UJIAN AKHIR SEMESTER (UAS)");
		}
		String rptFile = "SampulUjian.jrxml";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, smpls, "Sampul berkas ujian");
		return rrm;
	}
	public static ReportRawMaterials siapkanReportAbsensiUjian(JenisUjian tipe, KelasPerkuliahan curObject, List<PesertaKuliahReportElement> lpkre) {
		Map<String,Object> parameters = new HashMap<>();
		String rptFile = "KelasPerkuliahanAbsensiUjian.jrxml";
		if (tipe == JenisUjian.UTS){
			parameters.put("title", "CATATAN KEHADIRAN UJIAN TENGAH SEMESTER");
		} else if (tipe == JenisUjian.UAS){
			parameters.put("title", "CATATAN KEHADIRAN UJIAN AKHIR SEMESTER");
		} else {
			parameters.put("title", "CATATAN KEHADIRAN UJIAN");
		}
		parameters.put("matakuliah", curObject.getMataKuliah().toString());
		parameters.put("kodekelas", curObject.getKodeKelas());
		if (curObject.getDosenPengampu()!=null){
			parameters.put("dosen", curObject.getDosenPengampu().getNama());
			parameters.put("nidndosen", curObject.getDosenPengampu().getNidn());
		} else {
			parameters.put("dosen", "-");
			parameters.put("nidndosen", "-");
		}
		parameters.put("semta", curObject.getSemester().toString()+"/"+curObject.getTahunAjaran());
		parameters.put("prodi", curObject.getProdi().toString());
		parameters.put("timestamp", new Date());
		String title = "RPT: Daftar Peserta Ujian";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, lpkre, title);
		return rrm;
	}
	
	public static List<ReportRawMaterials> siapkanReportAbsensiHarian(List<KelasPerkuliahan> lkp) {
		List<ReportRawMaterials> rrms = new ArrayList<>();
		for (KelasPerkuliahan kp : lkp) {
			List<PesertaKuliahReportElement> lpkre = new ArrayList<>();
			List<PesertaKuliah> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
			Collections.sort(lpk);
			for (PesertaKuliah pesertaKuliah : lpk) {
				PesertaKuliahReportElement pkre = new PesertaKuliahReportElement(pesertaKuliah);
				
				
				lpkre.add(pkre);
			}
			ReportRawMaterials rrm = siapkanReportAbsensiHarian(kp, lpkre);
			rrms.add(rrm);
		}
		return rrms;
	}
	
	private static ReportRawMaterials siapkanReportAbsensiHarian(KelasPerkuliahan curObject, List<PesertaKuliahReportElement> lpkre) {
		Map<String,Object> parameters = new HashMap<>();
		String rptFile = "KelasPerkuliahanAbsensiHarian.jrxml";
		parameters.put("matakuliah", curObject.getMataKuliah().toString());
		parameters.put("kodekelas", curObject.getKodeKelas());
		if (curObject.getDosenPengampu()!=null){
			parameters.put("dosen", curObject.getDosenPengampu().getNama());
			if (curObject.getDosenPengampu().getNidn()!=null){
				parameters.put("nidndosen", curObject.getDosenPengampu().getNidn());
			} else {
				parameters.put("nidndosen", "-");
			}
		} else {
			parameters.put("dosen", "-");
		}
		parameters.put("semta", curObject.getSemester().toString()+"/"+curObject.getTahunAjaran());
		parameters.put("prodi", curObject.getProdi().toString());
		parameters.put("timestamp", new Date());
		String title = "RPT: Berita Acara Perkuliahan";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, lpkre, title);
		return rrm;
	}
	
	public static List<ReportRawMaterials> siapkanKHS(){
		
		return null;
	}
	private static ReportRawMaterials siapkanKartuHasilStdui(KelasPerkuliahanMahasiswaPerSemester2 kpmp){
		Mahasiswa m = kpmp.getMahasiswa();
		Semester sms =kpmp.getSemester();
		String ta =kpmp.getTahunAjaran();
		List<?> lpk = PesertaKuliahPersistence.getByMhsSmsTa(kpmp.getMahasiswa(), kpmp.getSemester(), kpmp.getTahunAjaran());
		IndeksPrestasiHelper iph = new IndeksPrestasiHelper(lpk, kpmp.getMahasiswa());
		
		return cetakHasilStudi(m, sms, ta, iph);
	}

	public static ReportRawMaterials cetakHasilStudi(Mahasiswa m, Semester sms, String ta, IndeksPrestasiHelper iph) {
		List<TranskripReportElement> l = iph.getKHS();
		/*List<KHSReportElement> lkhs = new ArrayList<>();
		for (PesertaKuliah pk: lpk) {
			lkhs.add(new KHSReportElement(pk));
			totSks+=pk.getCopiedSKSMatkul();
		}
		*/
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("semester", sms);
		parameters.put("tahunajaran",ta );
		parameters.put("nama", m.getNama());
		parameters.put("nim", m.getNpm());
		parameters.put("prodi", m.getProdi().getNama());
		parameters.put("pembimbingakademik", m.getPembimbingAkademik());
		parameters.put("totsks", iph.getTotsks());
		parameters.put("totkn", iph.getTotnilai());
		DecimalFormat df = new DecimalFormat("#.00");
		String ipk = df.format(iph.getIpk());
		parameters.put("ipk", ipk);
		parameters.put("kaprodi",m.getProdi().getKaprodi().getNama());
		if (m.getProdi().getKaprodi().getNidn()!=null) {
			parameters.put("kaprodinidn", m.getProdi().getKaprodi().getNidn());
		}else parameters.put("kaprodinidn", "-");
		
		parameters.put("timestamp",(new Date()));
		
		String rptFile= "KartuHasilStudi.jrxml";
		String title = "Kartu Hasil Studi";
		ReportRawMaterials rrm = new ReportRawMaterials(rptFile, parameters, l, title);
		return rrm;
	}
	public static List<ReportRawMaterials> siapkanKartuHasilStdui(List<KelasPerkuliahanMahasiswaPerSemester2> lkpmp){
		List<ReportRawMaterials> rrms= new ArrayList<>();
		for (KelasPerkuliahanMahasiswaPerSemester2 km : lkpmp) {
			rrms.add(siapkanKartuHasilStdui(km));
		}
		return rrms;
	}
	public static List<KehadiranPesertareportElement> siapkanRekapAbsensi(PesertaKuliah pk, KelasPerkuliahan kp){
		List<LogPerkuliahan> llp = LogPerkuliahanPersistence.getByKelas(kp);
		List<KehadiranPesertareportElement> lkpre = new ArrayList<>();
		List<LogKehadiranPesertaKuliah> llkpk = PersistenceQuery.kehadiran(pk.getMahasiswa(), kp);
		for (LogPerkuliahan lp : llp) {
			KehadiranPesertareportElement kpre = new KehadiranPesertareportElement();
			kpre.setisHadir(false);
			for (LogKehadiranPesertaKuliah lkpk : llkpk) {
				if (lp.equals(lkpk.getLogPerkuliahan())) {
					kpre.setisHadir(lkpk.isHadir());
				}
			}
			lkpre.add(kpre);
			
		}
		
		int size =16;
		for (int i = 1; i <= size; i++) {
			if (lkpre.size()<size) {
				KehadiranPesertareportElement kpre = new KehadiranPesertareportElement();
				lkpre.add(kpre);
			}
			if (lkpre.size()>size) {
				lkpre.remove(lkpre.size()-1);
			}
		}
		return lkpre;
	}
	public static List<KehadiranPesertareportElement> siapkanRekapAbsensiKosong(PesertaKuliah pk){
		List<KehadiranPesertareportElement> lkpre = new ArrayList<>();
		int size =16;
		for (int i = 1; i <= size; i++) {
			KehadiranPesertareportElement kpre = new KehadiranPesertareportElement();
			lkpre.add(kpre);
		}
		return lkpre;
	}
}
