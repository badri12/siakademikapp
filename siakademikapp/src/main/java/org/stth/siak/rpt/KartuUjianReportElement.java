package org.stth.siak.rpt;

import org.stth.siak.entity.PesertaKuliah;

public class KartuUjianReportElement {
	private String mataKuliah;
	private String kode;
	private String dosen;
	private int sks;
	
	public KartuUjianReportElement(){
		
	}
	public KartuUjianReportElement(PesertaKuliah pk){
		this.mataKuliah = pk.getCopiedNamaMatkul();
		this.kode = pk.getCopiedKodeMatkul();
		this.sks = pk.getCopiedSKSMatkul();
		if (pk.getKelasPerkuliahan().getDosenPengampu()!=null){
			this.dosen = pk.getKelasPerkuliahan().getDosenPengampu().getNama();
		} else {
			this.dosen=" ";
		}
		
	}
	
	public String getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(String mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public int getSks() {
		return sks;
	}
	public void setSks(int sks) {
		this.sks = sks;
	}
	public String getDosen() {
		return dosen;
	}
	public void setDosen(String dosen) {
		this.dosen = dosen;
	}
	
	

}
