package org.stth.siak.helper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MatakuliahPrasyaratPersistence;
import org.stth.jee.persistence.RencanaStudiPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.HakAksesRencanaStudiOnline;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahPrasyarat;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.RencanaStudiCreationMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulAdditionMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulKeterangan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.util.GeneralUtilities;


public class RencanaStudiManualHelper {
	//private Kurikulum kur;
	private List<MataKuliahRencanaStudi> daftarMatkulTersedia;
	private Map<String,MataKuliahRencanaStudi> daftarMatkulTersediaMap;
	private Semester semester;
	private IndeksPrestasiHelper iph;
	private Mahasiswa mahasiswa;
	private Map<String,PesertaKuliah> daftarMatkulSudahDitempuh;
	private List<MataKuliahRencanaStudi> daftarMatkulBelumDiambil;
	private List<MataKuliahRencanaStudi>  daftarMatkulMengulang;
	private List<MataKuliahPerluDiambil> daftarMatkulPerluDiambil;
	private List<RencanaStudiPilihanMataKuliah> daftarMatkulRencanaStudi;
	//private RencanaStudiMahasiswa rencanaStudi;
	private int maksSks;
	private String tahunAjaran;
	private LocalDate timestamp = LocalDate.now();
	private RencanaStudiMahasiswa rs;
	private ProgramStudi ps;


	public RencanaStudiManualHelper(ProgramStudi ps, Semester sem, int maksSks, String tahunAjaran, Mahasiswa m) {
		super();
		//this.kur = kur;
		mahasiswa=m;
		this.ps=ps;
		this.semester = sem;
		this.maksSks = maksSks;
		this.maksSks=maksSks;
		this.tahunAjaran = tahunAjaran;
		siapkanDaftarMatkulTersedia();
	}


	public RencanaStudiManualHelper(Mahasiswa mahasiswa,Semester sem,  String tahunAjaran, int maksSks) {
		super();
		this.semester = sem;
		this.mahasiswa = mahasiswa;
		this.maksSks = maksSks;
		this.tahunAjaran = tahunAjaran;
		
		siapkanDaftarMatkulTersedia();
		siapkanDaftarMatkulSudahDiambil();
		siapkanDaftarMatkulBelumDiambil();
		rs = getExistingRencanaStudi();
		if (rs==null){
			siapkanRencanaStudiKosong();
		}

	}

	public void setMahasiswa(Mahasiswa m){
		this.mahasiswa = m;
	}

	private void siapkanDaftarMatkulTersedia() {
		daftarMatkulTersedia = new ArrayList<>();
		daftarMatkulTersediaMap = new HashMap<>();
		Map<String, Object> lc = new HashMap<>();
		lc.put(new String("semester"), semester);
		if (mahasiswa!=null) {
			lc.put(new String("prodi"), mahasiswa.getProdi());
		}else{
			lc.put(new String("prodi"), ps);
		}

		lc.put(new String("tahunAjaran"), tahunAjaran);
		List<MataKuliahRencanaStudi> ls = GenericPersistence.findList(MataKuliahRencanaStudi.class, lc);
		for (MataKuliahRencanaStudi mkrs : ls) {
			//int i = mkrs.getSemesterBuka();
			if (mahasiswa.getAngkatan()>2015) {
				int smsMhs =GeneralUtilities.semesterMahasiswa(mahasiswa, semester, tahunAjaran);
				if (smsMhs>=mkrs.getSemesterBuka()) {
					daftarMatkulTersedia.add(mkrs);
					daftarMatkulTersediaMap.put(mkrs.getMataKuliah().getNama(), mkrs);
				}
			}else{
				daftarMatkulTersedia.add(mkrs);
				daftarMatkulTersediaMap.put(mkrs.getMataKuliah().getNama(), mkrs);
			}
		}
		Collections.sort(daftarMatkulTersedia);
	}


	private void siapkanDaftarMatkulSudahDiambil() {
		daftarMatkulSudahDitempuh = new HashMap<>();
		daftarMatkulMengulang = new ArrayList<>();
		List<?> nilaiDirty;
		Map<String, Object> critList = new HashMap<>();
		critList.put(new String("mahasiswa"), mahasiswa);
		nilaiDirty = GenericPersistence.findList(PesertaKuliah.class,critList);
		iph = new IndeksPrestasiHelper(nilaiDirty, mahasiswa);
		ArrayList<PesertaKuliah> nc = iph.getNilaiClean();
		for (PesertaKuliah pesertaKuliah : nc) {
			daftarMatkulSudahDitempuh.put(pesertaKuliah.getKelasPerkuliahan().getMataKuliah().getNama(), pesertaKuliah);
			if (!pesertaKuliah.isTuntas()){
				MataKuliahRencanaStudi mkk = daftarMatkulTersediaMap
						.get(pesertaKuliah.getKelasPerkuliahan().getMataKuliah().getNama());
				if (mkk!=null) {
					daftarMatkulMengulang.add(mkk);
				}
			}
		}
	}

	private void siapkanDaftarMatkulBelumDiambil(){
		daftarMatkulBelumDiambil = new ArrayList<>();
		for (MataKuliahRencanaStudi mk : daftarMatkulTersedia){
			if (!daftarMatkulSudahDitempuh.containsKey(mk.getMataKuliah().getNama())){
				daftarMatkulBelumDiambil.add(mk);
			}
		}
	}
	private void siapkanMatkulPerluDiambil(){
		daftarMatkulPerluDiambil = new ArrayList<>();
		for (MataKuliahRencanaStudi mkk : daftarMatkulBelumDiambil){
			MataKuliahPerluDiambil mkpd = new MataKuliahPerluDiambil();
			mkpd.mkrs = mkk;
			mkpd.keterangan = RencanaStudiMatkulKeterangan.REGULER;
			daftarMatkulPerluDiambil.add(mkpd);
		}
		for (MataKuliahRencanaStudi mkk : daftarMatkulMengulang){
			MataKuliahPerluDiambil mkpd = new MataKuliahPerluDiambil();
			mkpd.mkrs = mkk;
			mkpd.keterangan = RencanaStudiMatkulKeterangan.MENGULANG;
			daftarMatkulPerluDiambil.add(mkpd);
		}
		Collections.sort(daftarMatkulPerluDiambil);
	}

	public void ambilMataKuliahOtomatis() {
		ambilMatkulOtomatis(true);
	}


	private void ambilMatkulOtomatis(boolean saveToDatabase) {
		siapkanDaftarMatkulSudahDiambil();
		siapkanDaftarMatkulBelumDiambil();
		siapkanMatkulPerluDiambil();
		System.out.println(rs);
		deleteMatkulRencanaStudiAktif();
		daftarMatkulRencanaStudi = new ArrayList<>();
		int i = 0;
		for (MataKuliahPerluDiambil ambil : daftarMatkulPerluDiambil){
			int tobeSKS = i+ambil.mkrs.getMataKuliah().getSks();
			if (tobeSKS<=maksSks){
				System.out.println(i+" "+ambil);
				RencanaStudiPilihanMataKuliah rsmk = new RencanaStudiPilihanMataKuliah();
				rsmk.setRencanaStudi(rs);
				rsmk.setMataKuliah(ambil.mkrs.getMataKuliah());
				rsmk.setAddMethod(RencanaStudiMatkulAdditionMethod.AUTO_SYSTEM);
				rsmk.setSubmittedBy("SYSTEM");
				rsmk.setKeterangan(ambil.keterangan);
				daftarMatkulRencanaStudi.add(rsmk);
				i=i+ambil.mkrs.getMataKuliah().getSks();
				if (saveToDatabase) {
					GenericPersistence.merge(rsmk);
				}

			}

		}
	}
	public void siapkanRencanaStudiKosong(){
		rencanaStudiMahasiswa();
		rs.setCreationMethod(RencanaStudiCreationMethod.MANUAL_MAHASISWA);
		GenericPersistence.saveAndFlush(rs);
	}


	private void rencanaStudiMahasiswa() {
		siapkanDaftarMatkulSudahDiambil();
		rs = new RencanaStudiMahasiswa();
		rs.setMahasiswa(mahasiswa);
		rs.setPembimbingAkademik(mahasiswa.getPembimbingAkademik());
		rs.setSemester(semester);
		rs.setTahunAjaran(tahunAjaran);
		rs.setStatus(StatusRencanaStudi.DRAFT);
		rs.setCreated(timestamp);
		rs.setIpk(iph.getIpk());
		if (!iph.getNilaiD().isEmpty()){
			rs.setRemarks("Jumlah nilai D :" + iph.getNilaiD().size());
		} else {
			rs.setRemarks("-");
		}
	}

	public void siapkanRencanaStudiOtomatis(boolean saveToDatabase){
		if (saveToDatabase) {
			deleteRencanaStudi();	
		}
		rencanaStudiMahasiswa();
		rs.setCreationMethod(RencanaStudiCreationMethod.AUTO_SYSTEM);
		rs.setStatus(StatusRencanaStudi.DIAJUKAN);
		if (saveToDatabase) {
			GenericPersistence.saveAndFlush(rs);
		}
		ambilMatkulOtomatis(saveToDatabase);
	}

	private void deleteRencanaStudi() {
		List<RencanaStudiMahasiswa> l = RencanaStudiPersistence.getByMhsSmsTa(mahasiswa, semester, tahunAjaran);
		if (l.size()>0){
			for (RencanaStudiMahasiswa rencanaStudiMahasiswa : l) {
				rs=rencanaStudiMahasiswa;
				deleteMatkulRencanaStudiAktif();
				GenericPersistence.delete(rencanaStudiMahasiswa);
			}
		}

	}


	private RencanaStudiMahasiswa getExistingRencanaStudi() {
		RencanaStudiMahasiswa l = RencanaStudiPersistence.getByMhsSemTa(mahasiswa, semester, tahunAjaran);
		return l;
	}
	//persistent manager
	private void deleteMatkulRencanaStudiAktif() {
		if (rs.getId()>0) {
			List<Criterion> cl = new ArrayList<>();
			cl.add(Restrictions.eq("rencanaStudi",rs));
			List<?> lchild = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rs);
			if (lchild!=null) {
				for (Object object2 : lchild) {
					GenericPersistence.delete(object2);
				}
			}
			
		}

	}
	/*private void cekRencanaStudiYangAda(List<?> l) throws RencanaStudiExistException {
		for (Object object : l) {
			RencanaStudiMahasiswa rs = (RencanaStudiMahasiswa) object;
			if (rs.getStatus() == StatusRencanaStudi.FINAL){
				throw new RencanaStudiExistException("Rencana studi untuk mahasiswa bersangkutan sudah ada dan berstatus final");
			}
		}

	}*/
	//getters
	public List<RencanaStudiPilihanMataKuliah> getMatkulRencanaStudi(){
		return daftarMatkulRencanaStudi;
	}

	public List<MataKuliahRencanaStudi> getMatkulAvailable() {
		return daftarMatkulTersedia;
	}

	public IndeksPrestasiHelper getIph() {
		return iph;
	}

	public Map<String, PesertaKuliah> getMatkulTuntas() {
		return daftarMatkulSudahDitempuh;
	}

	public List<MataKuliahRencanaStudi> getMatkulBelumTuntas() {
		return daftarMatkulBelumDiambil;
	}
	public RencanaStudiMahasiswa getRencanaStudi(){
		return rs;
	}
	public String getNilaiLamaBilaAda(String kodeMataKuliah){
		if (daftarMatkulSudahDitempuh.containsKey(kodeMataKuliah)){
			PesertaKuliah o = daftarMatkulSudahDitempuh.get(kodeMataKuliah);
			return o.getNilai();
		}
		return null;
	}
	public List<PesertaKuliah> isLulusPrasyarat(MataKuliah mk){
		List<PesertaKuliah> lpk =new ArrayList<>();
		List<MataKuliahPrasyarat> lmk = MatakuliahPrasyaratPersistence.getPrasyarat(mk);
		for (MataKuliahPrasyarat mkpra : lmk) {
			String nilai = getNilaiLamaBilaAda(mkpra.getPrasyarat().getNama());
			PesertaKuliah pk = new PesertaKuliah();
			pk.setMahasiswa(mahasiswa);
			pk.setCopiedNamaMatkul(mkpra.getPrasyarat().getNama());
			if (nilai!=null) {
				if (nilai.equals("E")||nilai.equals("D")) {
					pk.setNilai(nilai);
					lpk.add(pk);
				}
			}else {
				pk.setNilai("NA");
				lpk.add(pk);
			};
		}
		return lpk;
	}
	
	private class MataKuliahPerluDiambil implements Comparable<MataKuliahPerluDiambil>{
		MataKuliahRencanaStudi mkrs;
		RencanaStudiMatkulKeterangan keterangan;
		@Override
		public int compareTo(MataKuliahPerluDiambil arg0) {
			return this.mkrs.compareTo(arg0.mkrs);
		}
	}
	public boolean isEligibleForEntry(){
		Map<String, Object> cl = new HashMap<>();
		cl.put(new String("mahasiswa"),mahasiswa);
		cl.put(new String("semester"), semester);
		cl.put(new String("tahunAjaran"), tahunAjaran);
		List<HakAksesRencanaStudiOnline> l = GenericPersistence.findList(HakAksesRencanaStudiOnline.class, cl);
		if (l.size()>0){
			return true;
		}
		return false;
	}

}
