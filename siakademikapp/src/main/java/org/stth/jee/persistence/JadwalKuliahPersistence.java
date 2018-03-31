package org.stth.jee.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;

public class JadwalKuliahPersistence {
	public static List<JadwalKuliah> getJadwalByDosenProdi(DosenKaryawan dosen, ProgramStudi prodi, Semester semester, String tahunAjaran){
		Map<String, Object> lc = new HashMap<>();
		if (dosen!=null){
			lc.put("kelasPerkuliahan.dosenPengampu", dosen);
		}
		if (prodi!=null){
			lc.put("kelasPerkuliahan.prodi", prodi);
		}
		lc.put("kelasPerkuliahan.semester", semester);
		lc.put("kelasPerkuliahan.tahunAjaran", tahunAjaran);
		return GenericPersistence.findList(JadwalKuliah.class, lc);
		
	}
	public static List<JadwalKuliah> getJadwalByKelasPerkuliahan(KelasPerkuliahan kp){
		Map<String, Object> lc = new HashMap<>();
		lc.put("kelasPerkuliahan", kp);
		return GenericPersistence.findList(JadwalKuliah.class,lc);
		
	}

	public static List<JadwalKuliah> getLogSimilarOnDate(JadwalKuliah log) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}
}
