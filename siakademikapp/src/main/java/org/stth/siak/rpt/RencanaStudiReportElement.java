package org.stth.siak.rpt;

import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;

public class RencanaStudiReportElement {
	private String mataKuliah;
	private String kodeMataKuliah;
	private int sks;
	private String keterangan;
	public RencanaStudiReportElement(RencanaStudiPilihanMataKuliah pil){
		mataKuliah = pil.getMataKuliah().getNama();
		kodeMataKuliah = pil.getMataKuliah().getKode();
		sks = pil.getMataKuliah().getSks();
		keterangan = pil.getKeterangan().toString();		
	}
	public String getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(String mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public String getKodeMataKuliah() {
		return kodeMataKuliah;
	}
	public void setKodeMataKuliah(String kodeMataKuliah) {
		this.kodeMataKuliah = kodeMataKuliah;
	}
	public int getSks() {
		return sks;
	}
	public void setSks(int sks) {
		this.sks = sks;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
	

}
