package org.stth.siak.rpt;

import org.stth.siak.entity.KelasPerkuliahan;

public class SampulUjianReportElement {
	String haritanggal;
	String prodi;
	String semester;
	String matakuliah;
	String kodematakuliah;
	String waktu;
	String dosenpengampu;
	String pengawas;
	String tahunajaran;
	String kodekelas;
	
	public SampulUjianReportElement(KelasPerkuliahan kp){
		prodi = kp.getProdi().getNama();
		semester = kp.getSemester().toString();
		matakuliah = kp.getMataKuliah().getNama();
		kodematakuliah = kp.getMataKuliah().getKode();
		dosenpengampu = kp.getDosenPengampu().getNama();
		tahunajaran = kp.getTahunAjaran();
		kodekelas = kp.getKodeKelas();
	}

	public String getHaritanggal() {
		return haritanggal;
	}

	public void setHaritanggal(String haritanggal) {
		this.haritanggal = haritanggal;
	}

	public String getProdi() {
		return prodi;
	}

	public void setProdi(String prodi) {
		this.prodi = prodi;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getMatakuliah() {
		return matakuliah;
	}

	public void setMatakuliah(String matakuliah) {
		this.matakuliah = matakuliah;
	}

	public String getKodematakuliah() {
		return kodematakuliah;
	}

	public void setKodematakuliah(String kodematakuliah) {
		this.kodematakuliah = kodematakuliah;
	}

	public String getWaktu() {
		return waktu;
	}

	public void setWaktu(String waktu) {
		this.waktu = waktu;
	}

	public String getDosenpengampu() {
		return dosenpengampu;
	}

	public void setDosenpengampu(String dosenpengampu) {
		this.dosenpengampu = dosenpengampu;
	}

	public String getPengawas() {
		return pengawas;
	}

	public void setPengawas(String pengawas) {
		this.pengawas = pengawas;
	}

	public String getTahunajaran() {
		return tahunajaran;
	}

	public void setTahunajaran(String tahunajaran) {
		this.tahunajaran = tahunajaran;
	}

	public String getKodekelas() {
		return kodekelas;
	}

	public void setKodekelas(String kodekelas) {
		this.kodekelas = kodekelas;
	}
	
	

}
