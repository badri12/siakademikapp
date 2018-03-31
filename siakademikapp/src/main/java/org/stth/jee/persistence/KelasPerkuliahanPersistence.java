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
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;

public class KelasPerkuliahanPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<KelasPerkuliahan> cq;
	private static Root<KelasPerkuliahan> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(KelasPerkuliahan.class);
		r = cq.from(KelasPerkuliahan.class);
	}
	
	public static List<KelasPerkuliahan> getKelasPerkuliahanByExample(KelasPerkuliahan kelasExample){
		Map<String, Object> lc = new HashMap<>();
		Map<String, Object> lcNotEq = new HashMap<>();
		if (kelasExample.getDosenPengampu()!=null) {
			lc.put("dosenPengampu", kelasExample.getDosenPengampu());
		}
		if (kelasExample.getProdi()!=null) {
			lc.put("prodi", kelasExample.getProdi());
		}
		if (kelasExample.getSemester()!=null) {
			lc.put("semester", kelasExample.getSemester());
		}
		if (kelasExample.getTahunAjaran()!=null) {
			if (!kelasExample.getTahunAjaran().isEmpty()) {
				lc.put("tahunAjaran", kelasExample.getTahunAjaran());
			}
		}
		
		if (kelasExample.getMataKuliah()!=null) {
			lc.put("mataKuliah", kelasExample.getMataKuliah());
		}
		if (kelasExample.getKodeKelas()!=null) {
			lc.put("kodeKelas", kelasExample.getKodeKelas());
		}else{
			lcNotEq.put("kodeKelas", "Transfer");
		}
		List<KelasPerkuliahan> l = GenericPersistence.findListNotequal(KelasPerkuliahan.class, lc, lcNotEq);
		return l;
	}
	
	public static List<KelasPerkuliahan> getKelasPerkuliahanByDosen(DosenKaryawan d){
		Map<String, Object> lc = new HashMap<>();
		lc.put("dosenPengampu", d);
		List<KelasPerkuliahan> l = GenericPersistence.findList(KelasPerkuliahan.class, lc);
		return l;
	}
	public static List<KelasPerkuliahan> getKelasPerkuliahanByProdiSemester(DosenKaryawan d,ProgramStudi prodi, Semester semester, String ta){
		Map<String, Object> lc = new HashMap<>();
		List<String> notNull = new ArrayList<>();
		if (prodi!=null) {
			lc.put("prodi", prodi);
		}
		lc.put("semester", semester);
		lc.put("tahunAjaran", ta);
		if (d!=null) {
			lc.put("dosenPengampu", d);
		}
		notNull.add("dosenPengampu");
		List<KelasPerkuliahan> l = GenericPersistence.findListNotnull(KelasPerkuliahan.class, lc, notNull);
		return l;
	}
	
	public static List<KelasPerkuliahan> getKelasPerkuliahanByDosenSemesterTa(DosenKaryawan d, Semester semester, String ta){
		Map<String, Object> lc = new HashMap<>();
		lc.put("dosenPengampu", d);
		lc.put("semester", semester);
		lc.put("tahunAjaran", ta);
		List<KelasPerkuliahan> l = GenericPersistence.findList(KelasPerkuliahan.class, lc);
		return l;
	}
	
	public static List<KelasPerkuliahan> getKelasPerkuliahanMahasiswaSemester(Mahasiswa mhs, Semester sem, String ta){
		instan();
		List<Predicate> lp = new ArrayList<>();
		
		Join<KelasPerkuliahan, PesertaKuliah> rPeserta = r.join("peserta");
		lp.add(cb.equal(r, rPeserta.get("kelasPerkuliahan")));
		lp.add(cb.equal(rPeserta.get("mahasiswa"), mhs));
		lp.add(cb.equal(r.get("semester"), sem));
		lp.add(cb.equal(r.get("tahunAjaran"), ta));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<KelasPerkuliahan> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}

}
