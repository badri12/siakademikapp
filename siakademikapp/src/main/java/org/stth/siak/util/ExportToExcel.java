package org.stth.siak.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.MataKuliahKonversiPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.jee.persistence.NilaiTransferPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKonversi;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.entity.NilaiTransferMataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.helper.MonevKehadiranDosen;
import org.stth.siak.rpt.KehadiranPesertareportElement;
import org.stth.siak.rpt.PesertaKuliahReportElement;
import org.stth.siak.rpt.ReportContentFactory;

import com.vaadin.server.StreamResource;

public class ExportToExcel {
	public static Workbook rekapitulasiNilai(Collection<Mahasiswa> cm){
		Workbook wb = new XSSFWorkbook();
		List<Kurikulum> lkur = KurikulumPersistence.getListByProdi(cm.iterator().next().getProdi());
		for (Kurikulum	k : lkur) {
			List<Mahasiswa> lm=new ArrayList<>();
			for (Mahasiswa mahasiswa : cm) {
				if (mahasiswa.getK().getId()==k.getId()) {
					lm.add(mahasiswa);
				}
			}
			if (lm.size()>0) {
				rekapitulasiNilai(wb, lm, k);
			}
		}
		return wb;
	}
	public static Workbook rekapitulasiNilai(Workbook wb,Collection<Mahasiswa> cm, Kurikulum k){
		CellStyle cs = wb.createCellStyle();
		cs.setFillForegroundColor(IndexedColors.RED1.getIndex());
		cs.setFillBackgroundColor(IndexedColors.RED.getIndex());
		cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		CellStyle cs1 = wb.createCellStyle();
		cs1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cs1.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
		cs1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		CellStyle cs2 = wb.createCellStyle();
		cs2.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
		cs2.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
		cs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Sheet s = wb.createSheet(k.getNama());
		Row rSKS =s.createRow(0);
		Row rKODE =s.createRow(1);
		Row rMK =s.createRow(2);
		Row rNama= s.createRow(3);
		rSKS.createCell(0).setCellValue("SKS");
		rKODE.createCell(0).setCellValue("KODE");
		rMK.createCell(0).setCellValue("MATAKULIAH");
		s.addMergedRegion(new CellRangeAddress(rSKS.getRowNum(), rSKS.getRowNum(), 0, 1));
		s.addMergedRegion(new CellRangeAddress(rKODE.getRowNum(), rKODE.getRowNum(), 0, 1));
		s.addMergedRegion(new CellRangeAddress(rMK.getRowNum(), rMK.getRowNum(), 0, 1));
		
		rNama.createCell(0).setCellValue("NIM");
		rNama.createCell(1).setCellValue("NAMA");
		List<MataKuliahKurikulum> lmkk = MataKuliahKurikulumPersistence.getByKurikulum(k);
		Collections.sort(lmkk);
		Map<MataKuliah, List<MataKuliah>> mapKonversi=new HashMap<>();
		for (MataKuliahKurikulum mkk : lmkk) {
			MataKuliah mk = mkk.getMataKuliah();
			List<MataKuliahKonversi> lmkonv = MataKuliahKonversiPersistence.getByMatKul(mk);
			List<MataKuliah> lmkul =new ArrayList<>();
			for (MataKuliahKonversi mkkonv : lmkonv) {
				lmkul.add(mkkonv.getMataKuliah());
			}

			mapKonversi.put(mk, lmkul);
		}
		int ro=4;
		Map<Mahasiswa, IndeksPrestasiHelper> miph = new HashMap<>();
		for (Mahasiswa mhs : cm) {
			Row r= s.createRow(ro);
			r.createCell(0).setCellValue(mhs.getNpm());
			r.createCell(1).setCellValue(mhs.getNama());
			IndeksPrestasiHelper iph= new IndeksPrestasiHelper(mhs, lmkk, mapKonversi);
			iph.getTranskripByKurikulum();
			miph.put(mhs, iph);
			ro++;
		}
		int col=2;char[] huruf= {'A','B','C','D'};
		for (MataKuliahKurikulum mkk : lmkk) {
			boolean next=false;
			MataKuliah mk = mkk.getMataKuliah();
			rSKS.createCell(col).setCellValue(mk.getSks());
			rKODE.createCell(col).setCellValue(mk.getKode());
			rMK.createCell(col).setCellValue(mk.getNama());
			rNama.createCell(col).setCellValue("N");
			rNama.createCell(1+col).setCellValue("KN");
			
			ro=4;
			for (Mahasiswa mhs : cm) {
				Row r= s.getRow(ro);
				PesertaKuliah pk=miph.get(mhs).getNilaiCleanMapped().get(mk);
				
				String form="";
				for (int i = 0; i < huruf.length; i++) {
					form+="IF("+CellReference.convertNumToColString(col)+(1+r.getRowNum())+"=\""+huruf[i]+"\","+(huruf.length-i)+",";
				}
				form+="0))))"+"*$"+CellReference.convertNumToColString(col)+"$1";
				r.createCell(1+col).setCellFormula(form);
				
				if (pk!=null) {
					next=true;
					String nilai = pk.getNilai();
					r.createCell(col).setCellValue(nilai);
					//r.createCell(1+col).setCellValue(convertNilai(nilai)*mk.getSks());
					if (nilai.equals("D")) {
						r.getCell(col).setCellValue("C");//for report to dikti
						r.getCell(col).setCellStyle(cs1);
						r.getCell(1+col).setCellStyle(cs1);
					}else if (nilai.equals("E")) {
						r.getCell(col).setCellValue("C");//for report to dikti
						r.getCell(col).setCellStyle(cs2);
						r.getCell(1+col).setCellStyle(cs2);
					}
				}else {
					r.createCell(col).setCellValue("C");//for report to dikti
					r.getCell(col).setCellStyle(cs);
					r.getCell(1+col).setCellStyle(cs);
				}
				ro++;
			}
			if (next) {
				s.addMergedRegion(new CellRangeAddress(rSKS.getRowNum(), rSKS.getRowNum(), col, 1+col));
				s.addMergedRegion(new CellRangeAddress(rKODE.getRowNum(), rKODE.getRowNum(), col, 1+col));
				s.addMergedRegion(new CellRangeAddress(rMK.getRowNum(), rMK.getRowNum(), col, 1+col));
				//System.out.println(new CellRangeAddress(rSKS.getRowNum(), rSKS.getRowNum(), col, 1+col)+" "+col);
				col+=2;
			}
		}

		rKODE.createCell(col).setCellValue("");
		rMK.createCell(col).setCellValue("");
		rSKS.createCell(col).setCellFormula("SUM("+CellReference.convertNumToColString(2)+"1:"
				+CellReference.convertNumToColString(col-1) +"1)");
		rKODE.createCell(col).setCellValue("TOTAL NILAI");
		rKODE.createCell(1+col).setCellValue("IPK");
		s.addMergedRegion(new CellRangeAddress(rKODE.getRowNum(), rNama.getRowNum(), col, col));
		s.addMergedRegion(new CellRangeAddress(rKODE.getRowNum(), rNama.getRowNum(), 1+col, 1+col));
		
		//DecimalFormat df = new DecimalFormat("#.00");
		for (int noindex=4;noindex<=s.getLastRowNum() ; noindex++) {
			Row r= s.getRow(noindex);
			r.createCell(col).setCellFormula("SUM("+CellReference.convertNumToColString(2)+(1+r.getRowNum())+":"
					+CellReference.convertNumToColString(col-1)+(1+r.getRowNum())+")");
			//r.createCell(1+col).setCellValue(df.format(miph.get(mhs).getIpk()));
			r.createCell(1+col).setCellFormula(CellReference.convertNumToColString(col)+(1+r.getRowNum())+"/$"
					+CellReference.convertNumToColString(col)+"$1");
			
		}
		return wb;
	}

	public static Workbook nilaiTransfer(Collection<Mahasiswa> cm){
		Workbook wb = new XSSFWorkbook();
		for (Mahasiswa mhs : cm) {
			Sheet s= wb.createSheet(mhs.getNpm()+" "+mhs.getNama());
			s.createRow(0).createCell(0).setCellValue(mhs.getNpm()+" "+mhs.getNama());
			s.addMergedRegion(CellRangeAddress.valueOf("A1:H1"));
			s.createRow(1);
			Row row =s.createRow(2);
			row.createCell(0).setCellValue("KODE MK ASAL");
			row.createCell(1).setCellValue("MATA KULIAH ASAL");
			row.createCell(2).setCellValue("SKS ASAL");
			row.createCell(3).setCellValue("NILAI ASAL");

			row.createCell(4).setCellValue("KODE MK DIAKUI");
			row.createCell(5).setCellValue("MATA KULIAH ASAL");
			row.createCell(6).setCellValue("SKS DIAKUI");
			row.createCell(7).setCellValue("NILAI DIAKUI");
			List<NilaiTransferMataKuliah> lntmk = NilaiTransferPersistence.getbyMahasiswa(mhs);
			int i=3;
			for (NilaiTransferMataKuliah ntmk : lntmk) {
				Row r =s.createRow(i++);
				r.createCell(0).setCellValue(ntmk.getKodeMKAsal());
				r.createCell(1).setCellValue(ntmk.getMatKulAsal());
				r.createCell(2).setCellValue(ntmk.getSksAsal());
				r.createCell(3).setCellValue(ntmk.getNilaiHurufAsal());
				PesertaKuliah pk =ntmk.getNilaiDiakui(); 
				MataKuliah mk =pk.getKelasPerkuliahan().getMataKuliah();
				r.createCell(4).setCellValue(mk.getKode());
				r.createCell(5).setCellValue(mk.getNama());
				r.createCell(6).setCellValue(mk.getSks());
				r.createCell(7).setCellValue(pk.getNilai());
			}
		}
		return wb;
	}

	public static Workbook dataMahasiswaByPA(DosenKaryawan dk){
		Workbook wb = new XSSFWorkbook();
		int curYear = GeneralUtilities.getCurrentYear();
		List<ProgramStudi> lps = GenericPersistence.findList(ProgramStudi.class);
		for (ProgramStudi prodi : lps) {
			for (int i = curYear; i >=2013; i--) {
				List<Mahasiswa> lm = MahasiswaPersistence.getByPaProdiAngkatan(dk, prodi, i);
				if (lm.size()>0) {
					dataMahasiswa(wb, prodi.getNama()+" "+i, lm);
				}
			}	
		}
		return wb;
	}
	public static Workbook dataMahasiswa(Workbook wb, String header,List<Mahasiswa> lm){
		Sheet s = wb.createSheet(header);
		s.createRow(0).createCell(0).setCellValue(header);
		s.createRow(1);
		Row row =s.createRow(2);
		//row.createCell(0).setCellValue("NO");
		row.createCell(0).setCellValue("NIM");
		row.createCell(1).setCellValue("NAMA");
		row.createCell(2).setCellValue("PRODI");
		row.createCell(3).setCellValue("ANGKATAN");
		int i = 3;
		for (Mahasiswa m : lm) {
			Row r =s.createRow(i++);
			//r.createCell(0).setCellValue(no++);
			r.createCell(0).setCellValue(m.getNpm());
			r.createCell(1).setCellValue(m.getNama());
			r.createCell(2).setCellValue(m.getProdi().getNama());
			r.createCell(3).setCellValue(m.getAngkatan());
		}
		return wb;
	}
	public static Workbook kehadiranPeserta(Collection<KelasPerkuliahan> lkp){
		Workbook wb = new XSSFWorkbook();
		for (KelasPerkuliahan kp : lkp) {
			List<?> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
			List<PesertaKuliahReportElement> lpkre = new ArrayList<>();
			for (Object object : lpk) {
				PesertaKuliah pk = (PesertaKuliah) object;
				List<KehadiranPesertareportElement> lkpre = ReportContentFactory.siapkanRekapAbsensi(pk, kp);
				PesertaKuliahReportElement pkre = new PesertaKuliahReportElement(pk);
				pkre.setLkpre(lkpre);
				lpkre.add(pkre);
			}
			kehadiranPeserta(wb, kp.getMataKuliah().getNama()+" "+kp.getKodeKelas()+" "+kp.getTahunAjaran()+" "+kp.getSemester()
			, lpkre);
		}
		return wb;
	}
	public static Workbook kehadiranPeserta(Workbook wb, String header, Collection<PesertaKuliahReportElement> lpk){
		Sheet s = wb.createSheet(header);
		s.createRow(0).createCell(0).setCellValue(header);
		s.createRow(1);
		Row row =s.createRow(2);
		//row.createCell(0).setCellValue("NO");
		row.createCell(0).setCellValue("NIM");
		row.createCell(1).setCellValue("MAHASISWA");
		row.createCell(2).setCellValue("TOTAL HADIR");
		row.createCell(3).setCellValue("TIDAK HADIR");
		int i = 3;
		for (PesertaKuliahReportElement kpre : lpk) {
			Row r =s.createRow(i++);
			//r.createCell(0).setCellValue(no++);
			r.createCell(0).setCellValue(kpre.getNim());
			r.createCell(1).setCellValue(kpre.getNama());
			r.createCell(2).setCellValue(kpre.getHadir());
			r.createCell(3).setCellValue(kpre.getAbsen());
		}
		return wb;
	}
	public static Workbook kehadiranDosen(String header, List<MonevKehadiranDosen.RekapKehadiranDosen> lRK){
		Workbook wb = new XSSFWorkbook();
		Sheet s = wb.createSheet(header);
		s.createRow(0).createCell(0).setCellValue(header);
		s.createRow(1);
		Row row =s.createRow(2);
		//row.createCell(0).setCellValue("NO");
		row.createCell(0).setCellValue("DOSEN");
		row.createCell(1).setCellValue("TOTAL SKS");
		row.createCell(2).setCellValue("TOTAL HADIR");
		int i = 3;
		//int no = 1;
		for (MonevKehadiranDosen.RekapKehadiranDosen rk : lRK){
			Row r =s.createRow(i++);
			//r.createCell(0).setCellValue(no++);
			r.createCell(0).setCellValue(rk.getDosen().getNama());
			r.createCell(1).setCellValue(rk.getTotSKS());
			r.createCell(2).setCellValue(rk.getLogHadir().size());
		}
		return wb;
	}
	public static Workbook kehadiranDosenperKelas(String header, List<LogPerkuliahan> lLp){
		Workbook wb = new XSSFWorkbook();
		Sheet s = wb.createSheet(header);
		s.createRow(0);
		s.createRow(1);
		Row row =s.createRow(2);
		row.createCell(0).setCellValue("NO");
		row.createCell(1).setCellValue("DOSEN");
		row.createCell(2).setCellValue("KODE");
		row.createCell(3).setCellValue("MATAKULIAH");
		row.createCell(4).setCellValue("PRODI");
		row.createCell(5).setCellValue("KELAS");
		row.createCell(6).setCellValue("PERKULIAHAN");

		Map<KelasPerkuliahan, Integer> mapLp = new HashMap<>();
		Integer jumlah = 1;
		for (LogPerkuliahan lp : lLp) {
			if (mapLp.containsKey(lp.getKelasPerkuliahan())) {
				jumlah++;
			}else{
				jumlah=1;
			}
			mapLp.put(lp.getKelasPerkuliahan(), jumlah);
		}
		int i = 3;
		for (KelasPerkuliahan kp : mapLp.keySet()) {
			Row r =s.createRow(i++);
			r.createCell(0).setCellValue(i++-2);
			r.createCell(1).setCellValue(kp.getDosenPengampu().getNama());
			r.createCell(2).setCellValue(kp.getMataKuliah().getKode());
			r.createCell(3).setCellValue(kp.getMataKuliah().getNama());
			r.createCell(4).setCellValue(kp.getProdi().getNama());
			r.createCell(5).setCellValue(kp.getKodeKelas());
			r.createCell(6).setCellValue(mapLp.get(kp));
		}
		return wb;
	}
	public static Workbook getTopik(List<Mahasiswa> l){
		Workbook wb = new XSSFWorkbook();
		Sheet s = wb.createSheet("Judul");
		Row row =s.createRow(0);
		row.createCell(0).setCellValue("NIM");
		row.createCell(1).setCellValue("NAMA");
		row.createCell(2).setCellValue("JUDUL SKRIPSI");
		s.createRow(1);
		int i = 2;
		for (Mahasiswa m : l) {
			Row r =s.createRow(i++);
			r.createCell(0).setCellValue(m.getNpm());
			r.createCell(1).setCellValue(m.getNama());
			r.createCell(2).setCellValue(m.getJudulSkripsi());
		}
		return wb;

	}
	public static Workbook nilai(String tabName,List<PesertaKuliah> lpk){
		Workbook wb = new XSSFWorkbook();
		Sheet s = wb.createSheet(tabName);
		Row row = s.createRow(0);
		row.createCell(0).setCellValue("NIM");
		row.createCell(1).setCellValue("NAMA");
		row.createCell(2).setCellValue("KODE MATAKULIAH");
		row.createCell(3).setCellValue("MATAKULIAH");
		row.createCell(4).setCellValue("KELAS");
		row.createCell(5).setCellValue("T.A/ SEMESTER");
		row.createCell(6).setCellValue("NILAI");
		s.createRow(1);
		int i = 2;
		for (PesertaKuliah pk : lpk) {
			Row r =s.createRow(i++);
			r.createCell(0).setCellValue(pk.getMahasiswa().getNpm());
			r.createCell(1).setCellValue(pk.getMahasiswa().getNama());
			r.createCell(2).setCellValue(pk.getKelasPerkuliahan().getMataKuliah().getKode());
			r.createCell(3).setCellValue(pk.getKelasPerkuliahan().getMataKuliah().getNama());
			r.createCell(4).setCellValue(pk.getKelasPerkuliahan().getKodeKelas());
			r.createCell(5).setCellValue(pk.getKelasPerkuliahan().getTahunAjaran()+"/"+pk.getKelasPerkuliahan().getSemester());
			r.createCell(6).setCellValue(pk.getNilai()); 
		}
		return wb;

	}
	public static void writeSheet(Workbook wb, String tabName, String[][] data) 
	{
		Sheet sheet = wb.createSheet(tabName);
		Row[] row = new Row[data.length];
		Cell[][] cell = new Cell[row.length][];

		for(int i = 0; i < row.length; i ++)
		{
			row[i] = sheet.createRow(i);
			cell[i] = new Cell[data[i].length];
			for(int j = 0; j < cell[i].length; j ++)
			{
				cell[i][j] = row[i].createCell(j);
				cell[i][j].setCellValue(data[i][j]);
			}
		}
	}
	public static StreamResource createFileExcel(Workbook wb, String fileName) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wb.write(baos);
		wb.close();
		baos.close();
		return new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = 5596334119958874063L;
			@Override
			public InputStream getStream() {
				InputStream is = new ByteArrayInputStream(baos.toByteArray());
				return is;
			}
		}, fileName+".xlsx");

	}
}
