package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.PesertaKuliah;

public class LogKehadiranPerkuliahanPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<LogKehadiranPesertaKuliah> cq;
	private static Root<LogKehadiranPesertaKuliah> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(LogKehadiranPesertaKuliah.class);
		r = cq.from(LogKehadiranPesertaKuliah.class);
	}
	public static List<LogKehadiranPesertaKuliah> getByPesertaKuliah(PesertaKuliah peserta){
		instan();
		List<Predicate> lp = new ArrayList<>();
		//String[] alias = {"logPerkuliahan"};
		Join<LogKehadiranPesertaKuliah, LogPerkuliahan> rLP = r.join("logPerkuliahan");
		lp.add(cb.equal(rLP.get("kelasPerkuliahan"), peserta.getKelasPerkuliahan()));
		lp.add(cb.equal(r.get("mahasiswa"), peserta.getMahasiswa()));
		lp.add(cb.equal(r.get("isHadir"), true));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<LogKehadiranPesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<LogKehadiranPesertaKuliah> getByLogPerkuliahan(LogPerkuliahan log) {
		instan();
		List<Predicate> lp = new ArrayList<>();
		lp.add(cb.equal(r.get("logPerkuliahan"), log));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<LogKehadiranPesertaKuliah> l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
}
