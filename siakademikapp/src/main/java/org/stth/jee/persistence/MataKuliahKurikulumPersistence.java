package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;

public class MataKuliahKurikulumPersistence {
	
	
	public static List<MataKuliahKurikulum> getByKurikulum(Kurikulum kr){
		Map<String, Object> lc = new HashMap<>();
		lc.put("kurikulum", kr);
		List<MataKuliahKurikulum> rslt = GenericPersistence.findList(MataKuliahKurikulum.class,lc);
		return rslt;
	}
	public static List<MataKuliahKurikulum> get(MataKuliahKurikulum mk){
		Map<String, Object> c = new HashMap<>();
		Map<String, String> like = new HashMap<>();
		if (mk.getKurikulum()!=null) {
			c.put("kurikulum", mk.getKurikulum());
		}
		if (mk.getMataKuliah()!=null) {
			if (mk.getMataKuliah().getKode()!=null) {
				like.put("mataKuliah.kode", mk.getMataKuliah().getKode());
			}
			if (mk.getMataKuliah().getNama()!=null) {
				like.put("mataKuliah.nama", mk.getMataKuliah().getNama());
			}
			
		}
		List<MataKuliahKurikulum> l = GenericPersistence.findList(MataKuliahKurikulum.class, c, like);
		return l;
	}
	

}
