package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.MataKuliahRencanaStudi;

public class MataKuliahRencanaStudiPersistence {
	public static List<MataKuliahRencanaStudi> bySample(MataKuliahRencanaStudi mkrs){
		Map<String, Object> lc = new HashMap<>();
		if (mkrs.getProdi()!=null) {
			lc.put("prodi", mkrs.getProdi());
		}
		if (mkrs.getSemester()!=null) {
			lc.put("semester", mkrs.getSemester());
		}
		if (!mkrs.getTahunAjaran().isEmpty()) {
			lc.put("tahunAjaran", mkrs.getTahunAjaran());
		}
		List<MataKuliahRencanaStudi> l = GenericPersistence.findList(MataKuliahRencanaStudi.class, lc);
		return l;
	}

}
