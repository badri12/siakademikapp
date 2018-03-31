package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahPrasyarat;

public class MatakuliahPrasyaratPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<MataKuliahPrasyarat> cq;
	private static Root<MataKuliahPrasyarat> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(MataKuliahPrasyarat.class);
		r = cq.from(MataKuliahPrasyarat.class);
	}
	public static List<MataKuliahPrasyarat> getPrasyarat(MataKuliah mk){
		instan();
		cq.select(r).where(cb.equal(r.get("mataKuliah"), mk));
		List<MataKuliahPrasyarat> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static <T> Long getCount(MataKuliah mk){
		Map<String, Object> lc = new HashMap<>();
		lc.put("mataKuliah", mk);
		Long jml = GenericPersistence.getCount(MataKuliahPrasyarat.class, lc);
		return jml;
	}
}
