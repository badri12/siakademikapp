package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;

public class RencanaStudiPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<RencanaStudiMahasiswa> cq;
	private static Root<RencanaStudiMahasiswa> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(RencanaStudiMahasiswa.class);
		r = cq.from(RencanaStudiMahasiswa.class);
	}
	
	public static RencanaStudiMahasiswa getByMhsSemTa(Mahasiswa mhs, Semester sem, String tahunAjaran){
		List<RencanaStudiMahasiswa> rslt = getByMhsSmsTa(mhs, sem, tahunAjaran);
		if (rslt.size()>0){
			RencanaStudiMahasiswa rsm = rslt.get(0);
			return rsm;
		}
		return null;
	}
	public static List<RencanaStudiMahasiswa> getByMhsSmsTa(Mahasiswa mhs, Semester sem, String tahunAjaran) {
		Map<String, Object> lc = new HashMap<>();
		lc.put("mahasiswa", mhs);
		lc.put("semester", sem);
		lc.put("tahunAjaran", tahunAjaran);
		List<RencanaStudiMahasiswa> rslt = GenericPersistence.findList(RencanaStudiMahasiswa.class, lc);
		return rslt;
	}
	public static List<RencanaStudiMahasiswa> getList(RencanaStudiMahasiswa rsm){
		instan();
		List<Predicate> lp = new ArrayList<>();
		if (rsm.getMahasiswa()!=null && rsm.getMahasiswa().getId()>0) {
			lp.add(cb.equal(r.get("mahasiswa"), rsm.getMahasiswa()));
		}
//		if (rsm.getPembimbingAkademik()!=null) {
//			lc.put(new String[]{"mahasiswa","pembimbingAkademik"}, rsm.getPembimbingAkademik());
//		}
		if (rsm.getSemester()!=null) {
			lp.add(cb.equal(r.get("semester"), rsm.getSemester()));
		}
		if (!rsm.getTahunAjaran().isEmpty()) {
			lp.add(cb.equal(r.get("tahunAjaran"), rsm.getTahunAjaran()));
		}
		if (rsm.getStatus()!=null) {
			lp.add(cb.equal(r.get("status"), rsm.getStatus()));
		}
		if (rsm.getPembimbingAkademik()!=null) {
			lp.add(cb.equal(r.get("pembimbingAkademik"), rsm.getPembimbingAkademik()));
		}
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<RencanaStudiMahasiswa> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
}
