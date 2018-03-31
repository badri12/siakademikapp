package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.siak.entity.Konfigurasi;
import org.stth.siak.enumtype.Semester;

public class KonfigurasiPersistence {
	private String getValue(String konfName){
		List<Konfigurasi> konfs = GenericPersistence.findList(Konfigurasi.class);
		for (Konfigurasi konfigurasi : konfs) {
			if (konfigurasi.getNamaKonfigurasi().equals(konfName)){
				return konfigurasi.getNilaiKonfigurasi();
			}
		}
		return null;
	}
	public void setKonfigurasi(Konfigurasi k){
		Map<String, Object> lc = new HashMap<>();
		lc.put("namaKonfigurasi", k.getNamaKonfigurasi());
		List<Konfigurasi> l = GenericPersistence.findList(Konfigurasi.class, lc);
		Konfigurasi konf= l.get(0);
		System.out.println(l.size());
		konf.setNilaiKonfigurasi(k.getNilaiKonfigurasi());
		GenericPersistence.merge(konf);
	}
	public String getKRSTa(){
		String s = getValue(Konfigurasi.KRS_TA);
		if (s!=null){
			return s;
		}
		return "";
	}
	public int getKRSMaxSKS(){
		String s = getValue(Konfigurasi.SKS_MAX);
		if (s!=null){
			return Integer.parseInt(s);
		}
		return 0;
	}
	public Semester getKRSSemester(){
		String s = getValue(Konfigurasi.KRS_SEM);
		if (s!=null){
			Semester sem = Semester.valueOf(s);
			return sem;
		}
		return null;
	}
	public boolean isKRSOpen(){
		String s = getValue(Konfigurasi.KRS_OPEN);
		if (s!=null){
			boolean b = Boolean.parseBoolean(s);
			return b;
		}
		return false;
	}
	public Semester getCurrentSemester(){
		String s = getValue(Konfigurasi.CUR_SEM);
		if (s!=null){
			Semester sem = Semester.valueOf(s);
			return sem;
		}
		return null;
	}
	public String getCurrentTa(){
		String s = getValue(Konfigurasi.CUR_TA);
		if (s!=null){
			return s;
		}
		return "";
	}
	public String getUploadDirectory(){
		String s = getValue(Konfigurasi.UPLOAD_DIR);
		if (s!=null){
			return s;
		}
		return "";
	}
}
