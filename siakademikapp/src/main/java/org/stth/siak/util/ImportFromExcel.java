package org.stth.siak.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;

public class ImportFromExcel {
	private static String getCellValue(Cell cell){
		String s="";
		if (cell!=null) {
			switch (cell.getCellTypeEnum()) {
			case STRING:
				s=cell.getStringCellValue();
				break;
			case NUMERIC:
				s=String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue());
				break;
			case BLANK:
				break;
			case _NONE:
				break;
			case FORMULA:
				break;
			case ERROR:
				break;
			case BOOLEAN:
				break;
			}

		}
		return s;
	}
	public static List<Mahasiswa> setTopik(File file){
		List<Mahasiswa> l = new ArrayList<>();
		try {
			Workbook wb = new XSSFWorkbook(file);
			Sheet sheet = wb.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
			for (Row row : sheet) {
				Mahasiswa m = new Mahasiswa();
				m.setNpm(getCellValue(row.getCell(0)));
				m.setNama(getCellValue(row.getCell(1)));
				m.setJudulSkripsi(getCellValue(row.getCell(2)));
				if (!m.getJudulSkripsi().isEmpty()) {
					l.add(m);
				}
			}
			wb.close();
			return l;
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<Mahasiswa> nilaiPerkuliahan(File f, Collection<PesertaKuliah> colPeserta){
		Map<String , PesertaKuliah> peserta = new HashMap<>();
		for (PesertaKuliah pesertaKuliah : colPeserta) {
			peserta.put(pesertaKuliah.getMahasiswa().getNpm(), pesertaKuliah);
		}
		List<Mahasiswa> l = new ArrayList<>();
		Workbook wb = readExcel(f);
		Sheet sheet = wb.getSheetAt(0);
		sheet.removeRow(sheet.getRow(0));
		sheet.removeRow(sheet.getRow(1));
		for(Row row : sheet){
			System.out.println(getCellValue(row.getCell(1))+" "+getCellValue(row.getCell(3)));
			PesertaKuliah pk = peserta.get(getCellValue(row.getCell(1)));
			if (pk!=null) {
				pk.setNilaiAngka(Double.valueOf(getCellValue(row.getCell(3))));
				GenericPersistence.merge(pk);
			}else{
				Mahasiswa m = new Mahasiswa();
				m.setNpm(getCellValue(row.getCell(1)));
				m.setNama(getCellValue(row.getCell(2)));
				l.add(m);
			}
		}
		return l;
	}
	
	public static List<Mahasiswa> dataMahasiswa(File f){
		List<Mahasiswa> l = new ArrayList<>();
		Workbook wb = readExcel(f);
		Sheet sheet = wb.getSheetAt(0);
		sheet.removeRow(sheet.getRow(0));
		for (Row row : sheet) {
			Mahasiswa m = new Mahasiswa();
			m.setNpm(row.getCell(0).getStringCellValue());
			m.setNama(row.getCell(1).getStringCellValue());	
			l.add(m);
		}
		return l;
	}
	
	private static Workbook readExcel(File f) {
		try {
			Workbook wb = new XSSFWorkbook(f);
			return wb;
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
