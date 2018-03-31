package org.stth.siak.rpt;

import java.util.Comparator;

import org.stth.siak.entity.PesertaKuliah;

public class TranskripReportElement implements Comparable<TranskripReportElement>, Comparator<TranskripReportElement>{
	private String kodemk;
	private String namamk;
	private String nilaihuruf;
	private int nilaiangka;
	private int sks;
	private int kn;
	private Integer semester;
	public TranskripReportElement(PesertaKuliah p, int sem){
		this.kodemk = p.getKelasPerkuliahan().getMataKuliah().getKode();
		this.namamk = p.getKelasPerkuliahan().getMataKuliah().getNama();
		this.nilaihuruf = p.getNilai();
		this.nilaiangka = convertNilai(nilaihuruf);
		this.sks = p.getKelasPerkuliahan().getMataKuliah().getSks();
		this.kn = sks * nilaiangka;
		//String ta = p.getKelasPerkuliahan().getTahunAjaran();
		//int thn = Integer.parseInt(ta.substring(0,4)); 
		//int selisih = thn - p.getMahasiswa().getAngkatan();
		//int sem = selisih * 2;
		//if (p.getKelasPerkuliahan().getSemester()==Semester.GANJIL) sem ++;
		this.semester = sem;
	}



	public String getKodemk() {
		return kodemk;
	}
	public void setKodemk(String kodemk) {
		this.kodemk = kodemk;
	}
	public String getNamamk() {
		return namamk;
	}
	public void setNamamk(String namamk) {
		this.namamk = namamk;
	}
	public String getNilaihuruf() {
		return nilaihuruf;
	}
	public void setNilaihuruf(String nilaihuruf) {
		this.nilaihuruf = nilaihuruf;
	}
	public int getNilaiangka() {
		return nilaiangka;
	}
	public void setNilaiangka(int nilaiangka) {
		this.nilaiangka = nilaiangka;
	}
	public int getSks() {
		return sks;
	}
	public void setSks(int sks) {
		this.sks = sks;
	}
	public int getKn() {
		return kn;
	}
	public void setKn(int kn) {
		this.kn = kn;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	private int convertNilai(String nl){
		int n=0;
		switch (nl) {
		case "A":
			n=4;
			break;
		case "B":
			n=3;
			break;
		case "C":
			n=2;
			break;
		case "D":
			n=1;
			break;
		default:
			break;
		}
		return n;
	}


	@Override
	public int compare(TranskripReportElement o1, TranskripReportElement o2) {
		return o1.compareTo(o2);
	}


	@Override
	public int compareTo(TranskripReportElement o) {
		//		if (semester!=o.semester){
		//		return semester-o.semester;
		//	}
		//	return this.semester.compareTo(o.semester);
		String angkaKodemkthis = kodemk.substring(kodemk.length()-3, kodemk.length()-1);
		String angkaKodemk = o.getKodemk().substring(o.getKodemk().length()-3, o.getKodemk().length()-1);
		int rslt = semester.compareTo(o.getSemester());
		if (rslt==0) {
			rslt = angkaKodemkthis.compareTo(angkaKodemk);
		}
		if (rslt==0) {
			return kodemk.compareTo(o.getKodemk());
		}
		return rslt;
	}

}
