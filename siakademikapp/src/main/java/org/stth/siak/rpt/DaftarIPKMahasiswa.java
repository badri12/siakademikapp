package org.stth.siak.rpt;

import org.stth.siak.helper.IndeksPrestasiHelper;

public class DaftarIPKMahasiswa {

	private String nim,nama,prodi;
	private int angkatan,totsks;
	private double ipk,kumnilai;
	public DaftarIPKMahasiswa(IndeksPrestasiHelper iph){
		this.nim = iph.getMhs().getNpm();
		this.nama = iph.getMhs().getNama();
		this.prodi = iph.getMhs().getProdi().getKode();
		this.angkatan = iph.getMhs().getAngkatan();
		this.kumnilai = iph.getTotnilai();
		this.totsks = iph.getTotsks();
		this.ipk = iph.getIpk();
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
	public String getProdi() {
		return prodi;
	}
	public void setProdi(String prodi) {
		this.prodi = prodi;
	}
	public int getAngkatan() {
		return angkatan;
	}
	public void setAngkatan(int angkatan) {
		this.angkatan = angkatan;
	}
	public int getTotsks() {
		return totsks;
	}
	public void setTotsks(int totsks) {
		this.totsks = totsks;
	}
	public double getKumnilai() {
		return kumnilai;
	}
	public void setKumnilai(double kumnilai) {
		this.kumnilai = kumnilai;
	}
	public double getIpk() {
		return ipk;
	}
	public void setIpk(double ipk) {
		this.ipk = ipk;
	}

	
}
