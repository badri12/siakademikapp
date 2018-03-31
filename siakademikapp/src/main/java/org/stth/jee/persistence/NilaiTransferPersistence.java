package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.NilaiTransferMataKuliah;
import org.stth.siak.entity.PesertaKuliah;

public class NilaiTransferPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<NilaiTransferMataKuliah> cq;
	private static Root<NilaiTransferMataKuliah> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(NilaiTransferMataKuliah.class);
		r = cq.from(NilaiTransferMataKuliah.class);
	}
	public static List<NilaiTransferMataKuliah> getbyMahasiswa(Mahasiswa m){
		instan();
		List<Predicate> lp = new ArrayList<>();
		Join<NilaiTransferMataKuliah, PesertaKuliah> pk = r.join("nilaiDiakui");
		lp.add(cb.equal(pk.get("mahasiswa"), m));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<NilaiTransferMataKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
		
	}
}
