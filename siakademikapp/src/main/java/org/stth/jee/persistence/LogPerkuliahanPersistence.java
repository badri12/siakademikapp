package org.stth.jee.persistence;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;

public class LogPerkuliahanPersistence {
	public static List<LogPerkuliahan> getByDosenOnPeriod(DosenKaryawan dosen, LocalDateTime start, LocalDateTime end){
		Map<String, Object> lc = new HashMap<>();
		Map<String, LocalDateTime[]> between = new HashMap<>();
		LocalDateTime[] ld = {start, end};
		lc.put("diisiOleh", dosen);
		between.put("tanggalPertemuan",ld);
		List<LogPerkuliahan> l = GenericPersistence.findListBetween(LogPerkuliahan.class, lc, between);
		return l;
	}
	public static List<LogPerkuliahan> getByKelas(KelasPerkuliahan kp){
		Map<String, Object> lc = new HashMap<>();
		lc.put("kelasPerkuliahan", kp);
		List<LogPerkuliahan> l = GenericPersistence.findList(LogPerkuliahan.class, lc);
		return l;
	}
	public static List<LogPerkuliahan> getByKelasOnPeriod(KelasPerkuliahan kp, LocalDateTime start, LocalDateTime end){
		Map<String, Object> lc = new HashMap<>();
		Map<String, LocalDateTime[]> between = new HashMap<>();
		lc.put("kelasPerkuliahan", kp);
		LocalDateTime[] ld = {start, end};
		between.put("tanggalPertemuan",ld);
		List<LogPerkuliahan> l = GenericPersistence.findListBetween(LogPerkuliahan.class, lc, between);
		return l;
	}
	public static List<LogPerkuliahan> getLogOnPeriod(LocalDateTime start, LocalDateTime end){
		Map<String, LocalDateTime[]> between = new HashMap<>();
		LocalDateTime[] ld = {start, end};
		between.put("tanggalPertemuan",ld);
		List<LogPerkuliahan> l = GenericPersistence.findListBetween(LogPerkuliahan.class, between);
		return l;
	}
	public static List<LogPerkuliahan> getLogSimilarOnDate(LogPerkuliahan log){
		Map<String, Object> lc = new HashMap<>();
		Map<String, LocalDateTime[]> between = new HashMap<>();
		LocalDateTime date = log.getTanggalPertemuan();
		LocalDateTime minDate =  LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
		LocalDateTime maxDate =  date.plusHours(23).plusMinutes(59);
		LocalDateTime[] ld = {minDate, maxDate};
		lc.put("kelasPerkuliahan", log.getKelasPerkuliahan());
		between.put("tanggalPertemuan",ld);
		List<LogPerkuliahan> l = GenericPersistence.findListBetween(LogPerkuliahan.class, lc, between);
		return l;
	}
}
