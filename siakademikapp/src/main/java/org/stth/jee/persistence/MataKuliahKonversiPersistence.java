package org.stth.jee.persistence;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.Session;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKonversi;

public class MataKuliahKonversiPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<MataKuliahKonversi> cq;
	private static Root<MataKuliahKonversi> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(MataKuliahKonversi.class);
		r = cq.from(MataKuliahKonversi.class);
	}
	
	public static List<MataKuliahKonversi> getByMatKul(MataKuliah mk){
		instan();
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<MataKuliahKonversi> ri = sq.from(MataKuliahKonversi.class);
		sq.select(ri.get("grup")).where(cb.equal(ri.get("mataKuliah"), mk));
		cq.select(r).where(cb.in(r.get("grup")).value(sq));
		List<MataKuliahKonversi> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static Integer getGrup(){
		instan();
		cq.select(r).orderBy(cb.desc(r.get("grup")));
		List<MataKuliahKonversi> lmkv = s.createQuery(cq).setMaxResults(1).getResultList();
		s.close();
		if (lmkv.size()>0) {
			return lmkv.get(0).getGrup()+1;
		}
		return 0;
	}
	public static Long getCount(MataKuliah mk){
		instan();
		CriteriaQuery<Long> cqLong = cb.createQuery(Long.class);
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<MataKuliahKonversi> ri = sq.from(MataKuliahKonversi.class);
		Root<MataKuliahKonversi> rlong = cqLong.from(MataKuliahKonversi.class);
		sq.select(ri.get("grup")).where(cb.equal(ri.get("mataKuliah"), mk));
		cqLong.select(cb.count(rlong)).where(cb.in(rlong.get("grup")).value(sq));
		Long jml = s.createQuery(cqLong).getSingleResult();
		return jml;
	}
}
