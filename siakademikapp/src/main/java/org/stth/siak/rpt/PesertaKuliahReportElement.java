package org.stth.siak.rpt;

import java.util.List;

import org.stth.siak.entity.PesertaKuliah;

public class PesertaKuliahReportElement {
	private String nim;
	private String nama;
	private String nilai;
	private List<KehadiranPesertareportElement> lkpre;
	private int hadir;
	private int absen;
	public PesertaKuliahReportElement(String nim, String nama, String nilai) {
		super();
		this.nim = nim;
		this.nama = nama;
		this.nilai = nilai;
	}
	public PesertaKuliahReportElement(PesertaKuliah p){
		this.nim = p.getMahasiswa().getNpm();
		this.nama = p.getMahasiswa().getNama();
		this.nilai = p.getNilai();
	}
	public String getNim() {
		return nim;
	}
	public void setNim(String nim) {
		this.nim = nim;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNilai() {
		return nilai;
	}
	public void setNilai(String nilai) {
		this.nilai = nilai;
	}
	public List<KehadiranPesertareportElement> getLkpre() {
		return lkpre;
	}
	public void setLkpre(List<KehadiranPesertareportElement> lkpre) {
		this.lkpre = lkpre;
		setHadir(0);
		setAbsen(0);
		for (KehadiranPesertareportElement kpre : lkpre) {
			if (kpre.isHadir()) {
				setHadir(getHadir() + 1);
			}else if (kpre.getKehadiran().equals("\u2715")) {
				setAbsen(getAbsen() + 1);
			}
		}
	}
	public int getHadir() {
		return hadir;
	}
	public void setHadir(int hadir) {
		this.hadir = hadir;
	}
	public int getAbsen() {
		return absen;
	}
	public void setAbsen(int absen) {
		this.absen = absen;
	}

}
