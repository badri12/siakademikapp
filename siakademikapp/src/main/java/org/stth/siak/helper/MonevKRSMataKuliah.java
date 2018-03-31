package org.stth.siak.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;

public class MonevKRSMataKuliah {
	private List<RencanaStudiPilihanMataKuliah> lrspmkRaw = new ArrayList<>();
	private List<RencanaStudiPilihanMataKuliah> lrspmk = new ArrayList<>();
	private Semester semester;
	private String tahunAjaran;
	private int angkatan=0;
	private ProgramStudi prodi = null;
	//private Map<Integer, ProgramStudi> mapProdi;
	//private Map<Integer, DosenKaryawan> mapDosenPa;
	private Map<String, RekapPengambilanMataKuliah> mapRekapMatkul = new HashMap<>();
	
	public MonevKRSMataKuliah(Semester sem, String tahunAjaran){
		this.semester = sem;
		this.tahunAjaran = tahunAjaran;
		angkatan = 0;
		prodi = null;
		perform();
	}
	
	public MonevKRSMataKuliah(Semester sem, String tahunAjaran, int angkatan, ProgramStudi prodi){
		this.semester = sem;
		this.tahunAjaran = tahunAjaran;
		this.angkatan = angkatan;
		this.prodi = prodi;
		perform();
	}
	
	void perform(){
		loadDataMataKuliahTerpilih();
		filter();
		calculate();
		
	}

	private void filter() {
		List<RencanaStudiPilihanMataKuliah> lrspmktemp= new ArrayList<>();
		List<RencanaStudiPilihanMataKuliah> lrspmktemp2= new ArrayList<>();
		//filter by prodi
		if (prodi!=null){
			for (RencanaStudiPilihanMataKuliah r : lrspmkRaw){
				Mahasiswa m = r.getRencanaStudi().getMahasiswa();
				ProgramStudi p = m.getProdi();
//				int angkatan = m.getAngkatan();
				if (p.getId()==prodi.getId()){
					lrspmktemp.add(r);
				}
			}
		} else {
			lrspmktemp.addAll(lrspmkRaw);
		}
		//remove by angkatan
		if (angkatan>0){
			for (RencanaStudiPilihanMataKuliah r : lrspmktemp){
				Mahasiswa m = r.getRencanaStudi().getMahasiswa();
//				ProgramStudi p = m.getProdi();
				int a = m.getAngkatan();
				if (angkatan==a){
					lrspmktemp2.add(r);
				}
			}
		} else {
			lrspmktemp2.addAll(lrspmktemp);
		}
		lrspmk.addAll(lrspmktemp2);
	}

	private void calculate() {
		for (RencanaStudiPilihanMataKuliah r : lrspmk){
			String key = getElementPengambilanKey(r);
			RencanaStudiMahasiswa rsm = r.getRencanaStudi();
			Mahasiswa m = rsm.getMahasiswa();
			RekapPengambilanMataKuliah rpmk;
			if (mapRekapMatkul.containsKey(key)){
				rpmk = mapRekapMatkul.get(key);
				rpmk.ambil++;
				rpmk.addPengambil(m);
			} else {
				rpmk = new RekapPengambilanMataKuliah(r);
				rpmk.addPengambil(m);
				mapRekapMatkul.put(key, rpmk);
			}
		}
	}
	private void loadDataMataKuliahTerpilih() {
		//load data matakuliah terpilih berdasarkan semester dan angkatan
		lrspmkRaw = RencanaStudiPilihanMataKuliahPersistence.getValidBySemesterTahunAjaran(semester, tahunAjaran,null);
		//load mahasiswa aktif berdasarkan kriteria
	
	}
	
	public List<RekapPengambilanMataKuliah> getRekap(){
		List<RekapPengambilanMataKuliah> lrkp = new ArrayList<>(mapRekapMatkul.values());
		Collections.sort(lrkp);
		return lrkp;
	}

	
	
	public class RekapPengambilanMataKuliah implements Comparable<RekapPengambilanMataKuliah>, Comparator<RekapPengambilanMataKuliah>{
		String kodeMataKuliah;
		String namaMataKuliah;
		int sks;
		String prodi;
		int angkatan;
		int ambil;
		List<Mahasiswa> pengambil = new ArrayList<>();
		public RekapPengambilanMataKuliah(RencanaStudiPilihanMataKuliah rspmk) {
			MataKuliah mk = rspmk.getMataKuliah();
			Mahasiswa m = rspmk.getRencanaStudi().getMahasiswa();
			ProgramStudi p = m.getProdi();
			this.kodeMataKuliah = mk.getKode();
			this.namaMataKuliah = mk.getNama();
			this.sks = mk.getSks();
			this.prodi = p.getNama();
			this.angkatan = m.getAngkatan();
			this.ambil = 1;
		}
		public String getKodeMataKuliah() {
			return kodeMataKuliah;
		}
		public void setKodeMataKuliah(String kodeMataKuliah) {
			this.kodeMataKuliah = kodeMataKuliah;
		}
		public String getNamaMataKuliah() {
			return namaMataKuliah;
		}
		public void setNamaMataKuliah(String namaMataKuliah) {
			this.namaMataKuliah = namaMataKuliah;
		}
		public int getSks() {
			return sks;
		}
		public void setSks(int sks) {
			this.sks = sks;
		}
		public String getProdi() {
			return prodi;
		}
		public void setProdi(String prodi) {
			this.prodi = prodi;
		}
		public int getAngkatan() {
			return angkatan;
		}
		public void setAngkatan(int angkatan) {
			this.angkatan = angkatan;
		}
		public int getAmbil() {
			return ambil;
		}
		public void setAmbil(int ambil) {
			this.ambil = ambil;
		}
		public void addPengambil(Mahasiswa m){
			pengambil.add(m);
		}
		public List<Mahasiswa> getPengambil(){
			return pengambil;
		}
		@Override
		public int compare(RekapPengambilanMataKuliah o1,
				RekapPengambilanMataKuliah o2) {
			return o1.compareTo(o2);
		}
		@Override
		public int compareTo(RekapPengambilanMataKuliah o) {
			if (!prodi.equals(o.prodi)){
				return prodi.compareTo(o.prodi);
			}
			if (!kodeMataKuliah.equals(o.kodeMataKuliah)){
				return kodeMataKuliah.compareTo(o.kodeMataKuliah);
			}
			if (angkatan!=o.angkatan){
				return angkatan-o.angkatan;
			}
			return 0;
		}
				
	}
	
	private String getElementPengambilanKey(RencanaStudiPilihanMataKuliah rspmk){
		String s = "";
		if (rspmk!=null){
			Mahasiswa m = rspmk.getRencanaStudi().getMahasiswa();
			ProgramStudi p = m.getProdi();
			int angkatan = m.getAngkatan();
			String matkul = rspmk.getMataKuliah().toString();
			return p+"a"+angkatan+"m"+matkul;
		}
		return s;
	}

}
