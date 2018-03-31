package org.stth.jee.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusMahasiswa;

public class PersistenceQuery {
	@SuppressWarnings("unchecked")
	public static List<KelasPerkuliahanMahasiswaPerSemester2> khs(String nama,Semester semester, String prodi, String ta, 
			Integer angkatan, DosenKaryawan dk) {
		String criteriaProdi = "";
		Query<?> q = null;
		if (!prodi.isEmpty()) {
			criteriaProdi=" and ps.nama = :prodi";
		}
		String criteriaAngkatan = "";
		if (angkatan>0) {
			criteriaAngkatan=" and m.angkatan = :angkatan";
		}
		String criteriaNama="";
		if(!nama.isEmpty()){
			criteriaNama=" and m.nama like :nama ";
		}
		String criteriaTA="";
		if(!ta.isEmpty()){
			criteriaTA = " and kp.tahunAjaran= (:ta) ";
		}
		String criteriaSMS ="";
		if(semester!=null){
			criteriaSMS=" and kp.semester = (:semester) ";
		}
		String criteriaPA ="";
		if(dk!=null){
			criteriaPA=" and m.pembimbingAkademik = (:pa) ";
		}
		String query = "SELECT m.id as rownum, m as mahasiswa, count(pk.mahasiswa) as jumlahMataKuliah, "
				+ "kp.semester as semester, kp.tahunAjaran as tahunAjaran "
				+ "FROM KelasPerkuliahan kp, Mahasiswa m, ProgramStudi ps, PesertaKuliah pk "
				+ "where m.id = pk.mahasiswa and pk.kelasPerkuliahan=kp.id and ps.id=m.prodi"
				+ criteriaNama
				+ criteriaProdi
				+ criteriaAngkatan
				+ criteriaTA
				+ criteriaSMS
				+ criteriaPA
				+ " and m.status = (:status)"
				+ " and kp.dosenPengampu is not null"
				+ " group by m.id ";
		System.out.println(query);
		Session session = HibernateUtil.getSession();
		List<KelasPerkuliahanMahasiswaPerSemester2> l=null;
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}

			try {
				q=session.createQuery(query);
				q.setParameter("status", StatusMahasiswa.AKTIF);
				if (!prodi.isEmpty()) {
					q.setParameter("prodi", prodi);
				}
				if (angkatan>0) {
					q.setParameter("angkatan", angkatan);
				}
				if(!nama.isEmpty()){
					q.setParameter("nama", "%"+nama+"%");
				}
				if (!ta.isEmpty()) {
					q.setParameter("ta", ta);
				}
				if (semester!=null) {
					q.setParameter("semester", semester);
				}
				if (dk!=null) {
					q.setParameter("pa", dk);
				}
				l=(List<KelasPerkuliahanMahasiswaPerSemester2>) q.setResultTransformer(
						Transformers.aliasToBean(KelasPerkuliahanMahasiswaPerSemester2.class)).list();
			} catch (HibernateException e) {
				tx.rollback();
				e.printStackTrace();
			}

		} finally {
			HibernateUtil.closeSession();
		}
		HibernateUtil.closeSession();
		return l;
	}
	@SuppressWarnings("unchecked")
	public static List<LogKehadiranPesertaKuliah> kehadiran(Mahasiswa m, KelasPerkuliahan kp){
		Query<?> q = null;
		String query = "SELECT lpk.id as id, lpk.isHadir as isHadir, m as mahasiswa, lp as logPerkuliahan "
				+"FROM LogKehadiranPesertaKuliah lpk, LogPerkuliahan lp, KelasPerkuliahan kp, Mahasiswa m "
				+"where lpk.logPerkuliahan=lp.id and lpk.mahasiswa=m.id and lp.kelasPerkuliahan=kp.id "
				+"and kp.id= :kpid "
				+"and m.id = :mid";

		Session session = HibernateUtil.getSession();
		List<LogKehadiranPesertaKuliah> l=null;
		try {
			Transaction tx;
			if ((session.getTransaction() != null)
					&& (session.getTransaction().isActive())) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			try {
				q=session.createQuery(query);
				q.setParameter("mid", m.getId());
				q.setParameter("kpid", kp.getId());
				l=(List<LogKehadiranPesertaKuliah>) q.setResultTransformer(
						Transformers.aliasToBean(LogKehadiranPesertaKuliah.class)).list();
			} catch (HibernateException e) {
				tx.rollback();
				e.printStackTrace();
			}
		}finally {
			HibernateUtil.closeSession();
		}
		return l;
	}
}
