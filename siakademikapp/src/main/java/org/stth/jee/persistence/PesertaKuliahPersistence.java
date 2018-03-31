package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;

public class PesertaKuliahPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<PesertaKuliah> cq;
	private static Root<PesertaKuliah> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(PesertaKuliah.class);
		r = cq.from(PesertaKuliah.class);
	}
	
	public static List<PesertaKuliah> getPesertaKuliahByKelasPerkuliahan(KelasPerkuliahan kp){
		return getPesertaKuliahByKelasPerkuliahanMhs(kp, null);
	}
	public static List<PesertaKuliah> getPesertaKuliahByMahasiswa(Mahasiswa mhs){
		return getPesertaKuliahByKelasPerkuliahanMhs(null, mhs);
	}
	public static List<PesertaKuliah> getPesertaKuliahByKelasPerkuliahanMhs(KelasPerkuliahan kp, Mahasiswa mhs){
		Map<String, Object> lc = new HashMap<>();
		if (kp!=null) {
			lc.put("kelasPerkuliahan", kp);
		}
		if (mhs!=null) {
			lc.put("mahasiswa", mhs);
		}
		List<PesertaKuliah> l = GenericPersistence.findList(PesertaKuliah.class, lc);
		return l;
	}
	public static PesertaKuliah getPesertaKuliahByKelasPerkuliahanMahasiswa(KelasPerkuliahan kp, Mahasiswa mhs){
		Map<String, Object> lc = new HashMap<>();
		lc.put("kelasPerkuliahan", kp);
		lc.put("mahasiswa", mhs);
		List<PesertaKuliah> l = GenericPersistence.findList(PesertaKuliah.class, lc);
		if (l.size()>0) return l.get(0);
		return null;
	}
	public static List<PesertaKuliah> getPesertaKuliahByExample(PesertaKuliah pk){
		instan();
		List<Predicate> lp = new ArrayList<>();
		
		if(pk.getMahasiswa()!=null){
			lp.add(cb.equal(r.get("mahasiswa"), pk.getMahasiswa()));
		}
		if (pk.getKelasPerkuliahan()!=null) {
			Join<PesertaKuliah, KelasPerkuliahan> rKP = r.join("kelasPerkuliahan");
			if (pk.getKelasPerkuliahan().getDosenPengampu() != null) {
				lp.add(cb.equal(rKP.get("dosenPengampu"), pk.getKelasPerkuliahan().getDosenPengampu()));
			}
			if (pk.getKelasPerkuliahan().getSemester() != null) {
				lp.add(cb.equal(rKP.get("semester"), pk.getKelasPerkuliahan().getSemester()));
			}
			if (pk.getKelasPerkuliahan().getProdi() != null) {
				lp.add(cb.equal(rKP.get("prodi"), pk.getKelasPerkuliahan().getProdi()));
			}
			if (pk.getKelasPerkuliahan().getTahunAjaran()!=null) {
				if (!pk.getKelasPerkuliahan().getTahunAjaran().isEmpty()) {
					lp.add(cb.equal(rKP.get("tahunAjaran"), pk.getKelasPerkuliahan().getTahunAjaran()));
				} 
			}
			if (pk.getKelasPerkuliahan().getMataKuliah() != null) {
				lp.add(cb.equal(rKP.get("mataKuliah"), pk.getKelasPerkuliahan().getMataKuliah()));
			}
		}
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<PesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<PesertaKuliah> getByMhsSmsTa(Mahasiswa mhs, Semester sms , String ta){
		instan();
		List<Predicate> lp = new ArrayList<>();
		Join<PesertaKuliah, KelasPerkuliahan> rKP = r.join("kelasPerkuliahan");
		lp.add(cb.equal(r.get("mahasiswa"), mhs));
		lp.add(cb.equal(rKP.get("semester"), sms));
		lp.add(cb.equal(rKP.get("tahunAjaran"), ta));
		lp.add(cb.isNotNull(rKP.get("dosenPengampu")));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<PesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<KelasPerkuliahan> getKelasPerkuliahanMahasiswaSemester(Mahasiswa mhs, Semester sem, String ta){
		instan();
		List<Predicate> lp = new ArrayList<>();
		
		Join<PesertaKuliah, KelasPerkuliahan> rKP = r.join("kelasPerkuliahan");;
		
		lp.add(cb.equal(r.get("mahasiswa"), mhs));
		lp.add(cb.equal(rKP.get("semester"), sem));
		lp.add(cb.equal(rKP.get("tahunAjaran"), ta));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<PesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		List<KelasPerkuliahan> lkp = new ArrayList<>();
		if (l.size()>0) {
			for (PesertaKuliah pk : l) {
				lkp.add(pk.getKelasPerkuliahan());
			}
		}
		return lkp;
	}
	public static List<PesertaKuliah> getByMhsMatkul(Mahasiswa m, String namaMatkul){
		instan();
		Join<PesertaKuliah, KelasPerkuliahan> rKP = r.join("kelasPerkuliahan");
		Join<KelasPerkuliahan, MataKuliah> mk= rKP.join("mataKuliah");
		Predicate[] p = new Predicate[2];
		p[0]=cb.equal(r.get("mahasiswa"), m);
		p[1]=cb.equal(mk.get("nama"), namaMatkul);
		cq.select(r).where((p));
		List<PesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
}
