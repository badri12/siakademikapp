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
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.FileTransferMahasiswa;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;

public class MahasiswaPersistence {
	private static Session s;
	private static CriteriaBuilder cb;
	private static CriteriaQuery<Mahasiswa> cq;
	private static Root<Mahasiswa> r;
	
	private static void instan() {
		s = HibernateUtil.getSession();
		cb = s.getCriteriaBuilder();
		cq = cb.createQuery(Mahasiswa.class);
		r = cq.from(Mahasiswa.class);
	}
	public static List<Mahasiswa> getMHSJudulIsExist(){
		instan();
		List<Predicate> lp = new ArrayList<>();
		lp.add(cb.notEqual(r.get("judulSkripsi"), ""));
		lp.add(cb.isNotNull(r.get("judulSkripsi")));
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where(cb.or(p));
		List<Mahasiswa>l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<Mahasiswa> getListByPembimbingAkademik(DosenKaryawan d){
		return getByPaProdiAngkatan(d, null, null);
	}
	public static List<Mahasiswa> getByPaProdiAngkatan(DosenKaryawan d,  ProgramStudi prodi, Integer angkatan){
		instan();
		List<Predicate> lp = new ArrayList<>();
		if (d!=null) {
			lp.add(cb.equal(r.get("pembimbingAkademik"), d));
		}
		lp.add(cb.equal(r.get("status"), StatusMahasiswa.AKTIF));
		if (prodi!=null){
			lp.add(cb.equal(r.get("prodi"), prodi));
		}
		if (angkatan!=null) {
			lp.add(cb.equal(r.get("angkatan"), angkatan));
		}
		return returnListMhs(lp);
	}

	public static List<Mahasiswa> getByNim(String nim){
		Mahasiswa example = new Mahasiswa();
		example.setNpm(nim);
		return getListByExample(example);
	}
	public static List<Mahasiswa> getListByExample(Mahasiswa example){
		instan();
		List<Predicate> lp = new ArrayList<>();
		if (example.getPembimbingAkademik()!=null){
			lp.add(cb.equal(r.get("pembimbingAkademik"), example.getPembimbingAkademik()));
		}
		if (example.getNama()!=null){
			if (!example.getNama().isEmpty()){
				lp.add(cb.like(r.get("nama"), "%"+example.getNama()+"%"));
			}
		}
		if (example.getNpm()!=null){
			if (!example.getNpm().isEmpty()) {
				lp.add(cb.like(r.get("npm"), "%"+example.getNpm()+"%"));
			}
		}
		if (example.getAngkatan()>0){
			lp.add(cb.equal(r.get("angkatan"), example.getAngkatan()));
		}
		if (example.getProdi()!=null){
			lp.add(cb.equal(r.get("prodi"), example.getProdi()));
		}

		if (example.getStatus()!=null) {
			lp.add(cb.equal(r.get("status"), example.getStatus()));
		}else{
			lp.add(cb.equal(r.get("status"), StatusMahasiswa.AKTIF));
		}
		if (example.getStatusMasuk()!=null) {
			lp.add(cb.equal(r.get("statusMasuk"), example.getStatusMasuk()));
		}
		return returnListMhs(lp);
	}
	private static List<Mahasiswa> returnListMhs(List<Predicate> lp) {
		Predicate[] p =new Predicate[lp.size()];
		for (Predicate predicate : lp) {
			p[lp.indexOf(predicate)] = predicate;
		}
		cq.select(r).where((p));
		List<Mahasiswa>l = s.createQuery(cq).getResultList();
		s.close();
		return l;
	}
	public static List<Mahasiswa> getTransfer(Mahasiswa example){
		instan();
		List<Predicate> lp = new ArrayList<>();
		if (example.getNama()!=null){
			if (!example.getNama().isEmpty()){
				lp.add(cb.like(r.get("nama"), "%"+example.getNama()+"%"));
			}
		}
		if (example.getStatusMasuk()!=null) {
			lp.add(cb.equal(r.get("statusMasuk"), example.getStatusMasuk()));
		}else{
			Predicate[] p = new Predicate[2];
			p[0] =cb.equal(r.get("statusMasuk"), StatusMasuk.TRANSFER);
			p[1] =cb.equal(r.get("statusMasuk"), StatusMasuk.PINDAHAN);
			lp.add(cb.or(p));
		}
		if (example.getProdi()!=null){
			lp.add(cb.equal(r.get("prodi"), example.getProdi()));
		}
		return returnListMhs(lp);
	}
	public static FileTransferMahasiswa getBerkas(Mahasiswa m){
		Map<String, Object> lc = new HashMap<>();
		lc.put("mahasiswa", m);
		List<FileTransferMahasiswa> lftm = GenericPersistence.findList(FileTransferMahasiswa.class,lc);
		if (lftm.size()>0) {
			return lftm.get(0);
		}
		return null;
	}

	public static Long getJumlahMHSperProdi(ProgramStudi ps){
		Map<String, Object> lc = new HashMap<>();
		lc.put("prodi", ps);
		Long l = GenericPersistence.getCount(Mahasiswa.class, lc);
		return l;
	}

}
