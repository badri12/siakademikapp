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
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;

public class RencanaStudiPilihanMataKuliahPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<RencanaStudiPilihanMataKuliah> cq;
	private static Root<RencanaStudiPilihanMataKuliah> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(RencanaStudiPilihanMataKuliah.class);
		r = cq.from(RencanaStudiPilihanMataKuliah.class);
	}
	
	public static List<RencanaStudiPilihanMataKuliah> getByRencanaStudi(RencanaStudiMahasiswa rsm){
		Map<String, Object> lc = new HashMap<>();
		lc.put("rencanaStudi", rsm);
		List<RencanaStudiPilihanMataKuliah> rslt = GenericPersistence.findList(RencanaStudiPilihanMataKuliah.class, lc);
		if (rslt.size()>0){
			return rslt;
		}
		return null;
	}
	
	public static List<RencanaStudiPilihanMataKuliah> getValidBySemesterTahunAjaran(Semester semester, String tahunAjaran, MataKuliah mk){
		return getByMatkulProdi(semester, tahunAjaran, mk,null);
	}

	public static List<RencanaStudiPilihanMataKuliah> getByMatkulProdi(Semester semester, String tahunAjaran,
			MataKuliah mk, ProgramStudi ps) {
		instan();
		List<Predicate> lp = new ArrayList<>();
		Join<RencanaStudiPilihanMataKuliah, RencanaStudiMahasiswa> rs = r.join("rencanaStudi");
		
		if (mk!=null) {
			lp.add(cb.equal(r.get("mataKuliah"), mk));
		}
		lp.add(cb.equal(rs.get("semester"), semester));
		lp.add(cb.equal(rs.get("tahunAjaran"), tahunAjaran));
		lp.add(cb.equal(rs.get("status"), StatusRencanaStudi.DISETUJUI));
		
		if (ps!=null) {
			Join<RencanaStudiMahasiswa, Mahasiswa> mhs = rs.join("mahasiswa");
			lp.add(cb.equal(mhs.get("prodi"), ps));
		}
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<RencanaStudiPilihanMataKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		if (l.size()>0){
			return l;
		}
		return null;
	}
}
