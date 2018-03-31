package org.stth.siak.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.MataKuliahKonversiPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKonversi;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.rpt.TranskripReportElement;
import org.stth.siak.rpt.TranskripWisudaElement;

public class IndeksPrestasiHelper {
	private ArrayList<PesertaKuliah> nilai;
	private Mahasiswa mhs;
	private double ipk=0.0;
	private int totsks=0;
	private int totsksD=0;
	private double kumNilai;
	private Map<String, PesertaKuliah> m;
	private Map<MataKuliah, PesertaKuliah> mclean;
	private Map<String, PesertaKuliah> nilaiD;
	private Map<String,Integer> semMatKuKur;
	private Kurikulum kur;
	private Map<MataKuliah, List<MataKuliah>> mapKonversi;
	private List<MataKuliahKurikulum> lmkk;

	public IndeksPrestasiHelper(Mahasiswa mhs){
		this.mhs = mhs;
		Kurikulum k=mhs.getK();
		lmkk = MataKuliahKurikulumPersistence.getByKurikulum(k);
		mapKonversi=new HashMap<>();
		for (MataKuliahKurikulum mkk : lmkk) {
			List<MataKuliahKonversi> lmkonv = MataKuliahKonversiPersistence.getByMatKul(mkk.getMataKuliah());
			List<MataKuliah> lmkul =new ArrayList<>();
			for (MataKuliahKonversi mkkonv : lmkonv) {
				lmkul.add(mkkonv.getMataKuliah());
			}
			mapKonversi.put(mkk.getMataKuliah(), lmkul);
		}
		
		List<?> nilaiDirty = PesertaKuliahPersistence.getPesertaKuliahByMahasiswa(mhs);
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
	}
	public IndeksPrestasiHelper(Mahasiswa mhs, List<MataKuliahKurikulum> lmkk
			, Map<MataKuliah, List<MataKuliah>> mapKonversi){
		this.mhs = mhs;
		this.lmkk=lmkk;
		this.mapKonversi=mapKonversi;
		List<?> nilaiDirty = PesertaKuliahPersistence.getPesertaKuliahByMahasiswa(mhs);
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
	}
	public IndeksPrestasiHelper(List<?> nilaiDirty, Mahasiswa mhs){
		this.mhs = mhs;
		cekMatkulDiambilUlang(nilaiDirty);
		hitungIPK();
	}
//	public IndeksPrestasiHelper(List<?> nilaiDirty, Mahasiswa mhs, Kurikulum k) {
//		this.mhs = mhs;
//		cekMatkulDiambilUlang(nilaiDirty);
//		hitungIPK();
//		setKur(k);
//	}
	public Map<String, PesertaKuliah> getNilaiMapped(){
		return m;
	}
	public Map<MataKuliah, PesertaKuliah> getNilaiCleanMapped(){
		return mclean;
	}
	
	private int convertNilai(String nl){
		int n=0;
		if (nl!=null) {
			switch (nl) {
			case "A":
				n = 4;
				break;
			case "B":
				n = 3;
				break;
			case "C":
				n = 2;
				break;
			case "D":
				n = 1;
				break;
			default:
				break;
			}
		}
		return n;
	}
	private void hitungIPK(){
		kumNilai = 0.0;
		if (nilai.size()>0){
			for (PesertaKuliah p : nilai) {
				totsks += p.getKelasPerkuliahan().getMataKuliah().getSks();
				if (p.getNilai()!=null) {
					if (p.getNilai().equals("D")) {
						nilaiD.put(p.getKelasPerkuliahan().getMataKuliah().getNama(), p);
						totsksD += p.getKelasPerkuliahan().getMataKuliah().getSks();
					}
				}
				int bobot= convertNilai(p.getNilai());
				kumNilai += (p.getKelasPerkuliahan().getMataKuliah().getSks()*bobot);
			}
			ipk= kumNilai/totsks;
		}
	}
	private void cekMatkulDiambilUlang(List<?> nilaiDirty){
		m = new HashMap<>();
		nilaiD = new HashMap<>();
		for (Object o : nilaiDirty) {
			PesertaKuliah p = (PesertaKuliah) o;
			if (p.getNilai()!=null) {
				if (!(p.getNilai().equals("") || p.getNilai().equals("0"))) {
					if (m.containsKey(p.getKelasPerkuliahan().getMataKuliah().getNama())) {
						PesertaKuliah p1 = m.get(p.getKelasPerkuliahan().getMataKuliah().getNama());
						int p1n = convertNilai(p1.getNilai());
						int pn = convertNilai(p.getNilai());
						if (pn > p1n) {
							m.remove(p1);
							m.put(p.getKelasPerkuliahan().getMataKuliah().getNama(), p);
							//m.put(p.getCopiedNamaMatkul(), p);
						}
					} else {
						m.put(p.getKelasPerkuliahan().getMataKuliah().getNama(), p);
						//m.put(p.getCopiedNamaMatkul(), p);
					}
				}
			}
		}
		nilai = new ArrayList<>(m.values());
		Collections.sort(nilai);
	}
	public Mahasiswa getMhs() {
		return mhs;
	}
	public double getIpk() {
		return ipk;
	}
	public int getTotsks() {
		return totsks;
	}
	public ArrayList<PesertaKuliah> getNilaiClean(){
		return nilai;
	}
	public Map<String, PesertaKuliah> getNilaiD() {
		return nilaiD;
	}

	public double getTotnilai() {
		return kumNilai;
	}
	public double getSKStotal(){
		return totsks;
	}
	public double getSKSD(){
		return totsksD;
	}
	public String getPredikat() {
		if (ipk>=3.5){
			return "Cum Laude";
		} else if (ipk>=3.0){
			return "Sangat Memuaskan";
		} else if (ipk>=2.5){
			return "Memuaskan";
		} else if (ipk>=2){
			return "Cukup";
		} else if (ipk>=2){
			return "Kurang Memuaskan";
		} else {
			return "Tidak Memuaskan";
		}
	}
	public Kurikulum getKur() {
		return kur;
	}
	public void setKur(Kurikulum kur) {
		this.kur = kur;
		List<?> l = MataKuliahKurikulumPersistence.getByKurikulum(kur);
		semMatKuKur = new HashMap<>();
		for (Object object : l) {
			MataKuliahKurikulum mkk = (MataKuliahKurikulum) object;
			semMatKuKur.put(mkk.getMataKuliah().getKode(), mkk.getSemesterBuka());
		}
		System.out.println(l.size());
	}
	private int getSem(String mkkode){
		if (semMatKuKur!=null) {
			Integer sem = semMatKuKur.get(mkkode);
			if (sem!=null){
				return sem;
			}
			System.out.println(mkkode +" "+ sem);
		}
		return 0;
	}
	public ArrayList<TranskripReportElement> getKHS(){
		ArrayList<TranskripReportElement> rslt = new ArrayList<>();
		for (PesertaKuliah p : nilai) {
			String kdk =p.getKelasPerkuliahan().getKodeKelas().toLowerCase();
			if (!kdk.equals("transfer")&&!kdk.equals("sp")) {
				rslt.add(new TranskripReportElement(p,getSem(p.getKelasPerkuliahan().getMataKuliah().getKode())));
			}
		}
		Collections.sort(rslt);
		TranskripReportElement treEnd=null;
		for (TranskripReportElement tre : rslt) {
			if(tre.getNamamk().equals("Tugas Akhir")||tre.getNamamk().equals("Skripsi")){
				treEnd=tre;
			}
		}

		if (treEnd!=null) {
			rslt.remove(treEnd);
			rslt.add(treEnd);
		}
		return rslt;
	}
	public ArrayList<TranskripReportElement> getTranskripReportElements(){
		ArrayList<TranskripReportElement> rslt = new ArrayList<>();
		for (PesertaKuliah p : nilai) {
			rslt.add(new TranskripReportElement(p,getSem(p.getKelasPerkuliahan().getMataKuliah().getKode())));
		}
		Collections.sort(rslt);
		TranskripReportElement treEnd=null;
		for (TranskripReportElement tre : rslt) {
			if(tre.getNamamk().equals("Tugas Akhir")||tre.getNamamk().equals("Skripsi")){
				treEnd=tre;
			}
		}
		if (treEnd!=null) {
			rslt.remove(treEnd);
			rslt.add(treEnd);
		}
		return rslt;
	}
	public ArrayList<TranskripReportElement> getTranskripByKurikulum(){
		kumNilai = 0.0;
		totsks=0;
		ipk=0;
		mclean=new HashMap<>();
		ArrayList<TranskripReportElement> rslt = new ArrayList<>();
		Collections.sort(lmkk);
		for (MataKuliahKurikulum mkk : lmkk) {
			PesertaKuliah p = m.get(mkk.getMataKuliah().getNama());
			List<MataKuliah> lmatkul = mapKonversi.get(mkk.getMataKuliah());
			if (lmatkul.size()>0) {
				for (MataKuliah matkul : lmatkul) {
					PesertaKuliah pka=m.get(matkul.getNama());
					if (p!=null&&pka!=null) {
						if (pka.getNilai()!=null) {
							if (p.getNilai()!=null) {
								int pn=convertNilai(p.getNilai());
								int pn1=convertNilai(pka.getNilai());
								if (pn1>pn) {
									p=pka;
								}
							}else p=pka;
						}
					}else if(pka!=null){
						p=pka;
					}
				}
			}
			if (p!=null) {
				totsks += mkk.getMataKuliah().getSks();
				int bobot= convertNilai(p.getNilai());
				kumNilai += mkk.getMataKuliah().getSks()*bobot;
				p.getKelasPerkuliahan().setMataKuliah(mkk.getMataKuliah());
				rslt.add(new TranskripReportElement(p, mkk.getSemesterBuka()));
				mclean.put(mkk.getMataKuliah(), p);

			}
		}
		ipk= kumNilai/totsks;
		Collections.sort(rslt);
		TranskripReportElement treEnd=null;
		for (TranskripReportElement tre : rslt) {
			if(tre.getNamamk().equals("Tugas Akhir")||tre.getNamamk().equals("Skripsi")){
				treEnd=tre;
			}
		}
		if (treEnd!=null) {
			rslt.remove(treEnd);
			rslt.add(treEnd);
		}
		return rslt;
	}
//	public ArrayList<TranskripReportElement> getTranskripByKurikulum(){
//		return getTranskripByKurikulum(lmkk);
//	}
	public ArrayList<TranskripWisudaElement> getTranskripWisudaReportElements(){
		ArrayList<TranskripWisudaElement> rslt = new ArrayList<>();
		ArrayList<TranskripReportElement> listTransKiri = new ArrayList<>();
		ArrayList<TranskripReportElement> listTransKanan= new ArrayList<>();
		int no =1;
		int batas = getTranskripByKurikulum().size()/2 +2;
		TranskripWisudaElement twe = new TranskripWisudaElement();
		for (TranskripReportElement tre : getTranskripReportElements()) {
			if (no<=batas) {
				listTransKiri.add(tre);		
			}else{
				listTransKanan.add(tre);
			}
			no++;
		}
		twe.setListTransKiri(listTransKiri);
		twe.setListTransKanan(listTransKanan);
		rslt.add(twe);
		return rslt;
	}
}
