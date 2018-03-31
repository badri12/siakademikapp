package org.stth.siak.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.stth.siak.entity.Mahasiswa;

import com.vaadin.server.StreamResource;

public class ExportData {
	
	public static StreamResource mahasiswa(List<Mahasiswa> l, String title){
		Workbook wb = new XSSFWorkbook();
		String[][] data = new String[l.size()+1][10];
		data[0][0] = "NIM";
		data[0][1] = "NAMA";
		data[0][2] = "ANGKATAN";
		data[0][3] = "PRODI";
		data[0][4] = "TEMPAT LAHIR";
		data[0][5] = "TANGGAL LAHIR";
		data[0][6] = "STATUS";
		int r = 1;
		
		for (Mahasiswa m : l) {
			data[r][0] = m.getNpm();
			data[r][1] = m.getNama();
			data[r][2] = String.valueOf( m.getAngkatan());
			data[r][3] = m.getProdi().getNama();
			data[r][4] = m.getTempatLahir();
			data[r][5] = GeneralUtilities.getLongFormattedDate2(m.getTanggalLahir());
			data[r][6] = m.getStatus().toString();
			r++;
		}
		ExportToExcel.writeSheet(wb, "data", data);
		try {
			return ExportToExcel.createFileExcel(wb, title);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static StreamResource nilaiMHS(){
		
		
		return null;
	}
}
