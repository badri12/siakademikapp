package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.stth.siak.entity.HakAksesRencanaStudiOnline;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.Semester;


public class HakAksesKRSPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<HakAksesRencanaStudiOnline> cq;
	private static Root<HakAksesRencanaStudiOnline> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(HakAksesRencanaStudiOnline.class);
		r = cq.from(HakAksesRencanaStudiOnline.class);
	}
	public static List<HakAksesRencanaStudiOnline> getDaftarMHS(HakAksesRencanaStudiOnline sample){
		instan();
		List<Predicate> lp = new ArrayList<>();
		if (sample.getMahasiswa()!=null) {
			lp.add(cb.equal(r.get("mahasiswa"), sample.getMahasiswa()));
		}
		lp.add(cb.equal(r.get("semester"), sample.getSemester()));
		lp.add(cb.equal(r.get("tahunAjaran"), sample.getTahunAjaran()));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where(p);
		List<HakAksesRencanaStudiOnline> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<HakAksesRencanaStudiOnline> getBySemTa(String ta, Semester s){
		HakAksesRencanaStudiOnline hak = new HakAksesRencanaStudiOnline();
		hak.setSemester(s);
		hak.setTahunAjaran(ta);
		return getDaftarMHS(hak);
	}
	public static HakAksesRencanaStudiOnline getByMhs(Mahasiswa m,String ta, Semester s){
		HakAksesRencanaStudiOnline hak = new HakAksesRencanaStudiOnline();
		hak.setSemester(s);
		hak.setTahunAjaran(ta);
		hak.setMahasiswa(m);
		List<HakAksesRencanaStudiOnline> l =getDaftarMHS(hak);
		if (l.size()>0) {
			return l.get(0);
		}
		return null;
	}
}
