package org.stth.jee.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericPersistence {
	public static <T> List<T> findList(Class<T> arg0) {
		return findList(arg0, null, null, null,null,null, null,null);
	}
	public static <T> List<T> findList(Class<T> arg0,Map<String, Object> equal) {
		return findList(arg0, equal, null, null, null,null, null,null);
	}
	public static <T> List<T> findListNotequal(Class<T> arg0,Map<String, Object> equal, Map<String, Object> notEqual) {
		return findList(arg0, equal, null, null, null,null, notEqual,null);
	}
	public static <T> List<T> findListNotnull(Class<T> arg0,Map<String, Object> equal,List<String> notNull) {
		return findList(arg0, equal, null, null, null,notNull, null,null);
	}
	public static <T> List<T> findList(Class<T> arg0,Map<String, Object> equal,
			Map<String, String> like, Map<String, Object[]> disjungsi) {
		return findList(arg0, equal, like, null, disjungsi,null, null,null);
	}
	public static <T> List<T> findListLike(Class<T> arg0, Map<String, String> like) {
		return findList(arg0, null, like, null, null,null, null,null);
	}
	public static <T> List<T> findListBetween(Class<T> arg0,Map<String, LocalDateTime[]> between) {
		return findList(arg0, null, null, between, null,null,null,null);
	}
	public static <T> List<T> findList(Class<T> arg0,Map<String, Object> equal,Map<String, String> like) {
		return findList(arg0, equal, like, null, null,null,null,null);
	}
	public static <T> List<T> findListBetween(Class<T> arg0,Map<String, Object> equal,Map<String, LocalDateTime[]> betweenDate) {
		return findList(arg0, equal, null, betweenDate,null, null,null,null);
	}
	public static <T> List<T> findList(Class<T> arg0, Map<String, Object> equal ,Map<String, String> like, 
			Map<String, LocalDateTime[]> betweenDate, Map<String, Object[]> disjungsi, 
			List<String> notNull, Map<String, Object> notEqual, String[] orderAsc) {
		Session session = HibernateUtil.getSession();
		List<T> l = null;
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				CriteriaBuilder cb = session.getCriteriaBuilder();
				CriteriaQuery<T> cq = cb.createQuery(arg0);
				Root<T> r = cq.from(arg0);
				List<Predicate> lp = new ArrayList<>();
				if (equal!=null) {
					for(String s : equal.keySet()){
						lp.add(cb.equal(r.get(s), equal.get(s)));
					}
				}
				if (notEqual!=null) {
					for(String s : notEqual.keySet()){
						lp.add(cb.notEqual(r.get(s), notEqual.get(s)));
					}
				}
				if (notNull!=null) {
					for(String s : notNull){
						lp.add(cb.isNotNull(r.get(s)));
					}
				}
				if (like!=null) {
					for (String key : like.keySet() ) {
						lp.add(cb.like(r.get(key), "%"+like.get(key)+"%"));
					}
				}
				if (betweenDate!=null) {
					for (String key : betweenDate.keySet() ) {
						LocalDateTime[] o = betweenDate.get(key);
						lp.add(cb.between(r.get(key), o[0], o[1]));
					}
				}
				if (disjungsi!=null) {
					int i=0;
					for(String s : disjungsi.keySet()){
						Object[] o = disjungsi.get(s);
						Predicate[] or = new Predicate[o.length];
						for (Object object : o) {
							or[i] = cb.equal(r.get(s), object);
							i++;
						}
						lp.add(cb.or(or));
					}

				}
				Predicate[] p =new Predicate[lp.size()];
				for (Predicate predicate : lp) {
					p[lp.indexOf(predicate)] = predicate;
				}

				cq.select(r).where(p);
				List<Order> lo= new ArrayList<>();
				if (orderAsc!=null) {
					for (String s: orderAsc) {
						lo.add(cb.asc(r.get(s)));
					}
				}
				cq.select(r).where(p).orderBy(lo);
				l=session.createQuery(cq).getResultList();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				throw e;
			}
		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
		return l;
	}
	public static <T> Long getCount(Class<T> arg0, Map<String, Object> equal){
		Session session = HibernateUtil.getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> r = cq.from(arg0);
		Predicate[] p =new Predicate[equal.size()];
		int i=0;
		for(String s : equal.keySet()){
			p[i++]=cb.equal(r.get(s), equal.get(s));
		}
		cq.select(cb.count(r)).where(p);
		Long jml = session.createQuery(cq).getSingleResult();
		return jml;
	}

	public static void saveAndFlush(Object a) {
		Session session = HibernateUtil.getSession();
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				session.save(a);
				session.flush();
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				throw e;
			}
		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
	}

	public static Object merge(Object a) {
		Session session = HibernateUtil.getSession();
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				session.merge(a);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				//e.printStackTrace();
				return e;
			} 
		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
		return null;
	}

	public static void delete(Object a) {
		Session session = HibernateUtil.getSession();
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				session.delete(a);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				throw e;
			}
		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
	}
	public static void refresh(Object a){
		Session session = HibernateUtil.getSession();
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				session.refresh(a);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				throw e;
			}
		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
	}

	public static void closeSession() {
		Session session = HibernateUtil.getSession();
		session.flush();
		session.close();
	}
}
