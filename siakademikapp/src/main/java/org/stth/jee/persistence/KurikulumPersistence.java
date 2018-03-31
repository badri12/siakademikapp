package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.ProgramStudi;

public class KurikulumPersistence {

	public static List<Kurikulum> getListByProdi(ProgramStudi prodi){
		Map<String, Object> lc = new HashMap<>();
		lc.put("prodi", prodi);
		List<Kurikulum> rslt = GenericPersistence.findList(Kurikulum.class,lc);
		return rslt;
	}

}
