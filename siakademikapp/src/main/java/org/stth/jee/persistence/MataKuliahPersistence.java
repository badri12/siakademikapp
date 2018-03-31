package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;

public class MataKuliahPersistence {
	public static List<MataKuliah> get(MataKuliah mk){
		Map<String, Object> lc = new HashMap<>();
		Map<String, String> like = new HashMap<>();
		if (mk.getProdiPemilik()!=null) {
			lc.put("prodiPemilik", mk.getProdiPemilik());
		}
		if (mk.getKode()!=null) {
			like.put("kode", mk.getKode());
		}
		if (mk.getNama()!=null) {
			like.put("nama", mk.getNama());
		}
		List<MataKuliah> l = GenericPersistence.findList(MataKuliah.class, lc, like);
		return l;
	}
	public static List<MataKuliah> getByProdi(ProgramStudi ps){
		MataKuliah mk = new MataKuliah();
		mk.setProdiPemilik(ps);
		return get(mk);
	}
	public static List<MataKuliah> getByKode(String kode){
		MataKuliah mk = new MataKuliah();
		mk.setKode(kode);
		return get(mk);
	}
}
