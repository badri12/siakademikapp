package org.stth.siak.rpt;

import org.stth.siak.entity.PesertaKuliah;

public class KHSReportElement {
	
	private String kodeMatKul;
	private String namaMatKul;
	private int sks;
	private String nilai;
	private int kn;
	
	public KHSReportElement(PesertaKuliah pk) {
		kodeMatKul=pk.getCopiedKodeMatkul();
		namaMatKul=pk.getCopiedNamaMatkul();
		sks=pk.getCopiedSKSMatkul();
		nilai=pk.getNilai();
		int nilaiAngka = convertNilai(nilai);
		kn = sks*nilaiAngka;
	}
	
	
	public String getKodeMatKul() {
		return kodeMatKul;
	}


	public void setKodeMatKul(String kodeMatKul) {
		this.kodeMatKul = kodeMatKul;
	}


	public String getNamaMatKul() {
		return namaMatKul;
	}


	public void setNamaMatKul(String namaMatKul) {
		this.namaMatKul = namaMatKul;
	}


	public int getSks() {
		return sks;
	}


	public void setSks(int sks) {
		this.sks = sks;
	}


	public String getNilai() {
		return nilai;
	}


	public void setNilai(String nilai) {
		this.nilai = nilai;
	}


	public int getKn() {
		return kn;
	}


	public void setKn(int kn) {
		this.kn = kn;
	}


	private int convertNilai(String nl){
		int n=0;
		if (nl!=null) {
			switch (nl) {
			case "A":
				n = 4;
				break;
			case "B":
				n = 3;
				break;
			case "C":
				n = 2;
				break;
			case "D":
				n = 1;
				break;
			default:
				break;
			}
		}
		return n;
	}
	

}
