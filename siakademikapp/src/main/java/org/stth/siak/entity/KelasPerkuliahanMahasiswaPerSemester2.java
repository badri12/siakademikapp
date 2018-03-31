package org.stth.siak.entity;

import java.util.Comparator;


import org.stth.siak.enumtype.Semester;


public class KelasPerkuliahanMahasiswaPerSemester2 implements Comparable<KelasPerkuliahanMahasiswaPerSemester2>, Comparator<KelasPerkuliahanMahasiswaPerSemester2>{
	
	private int rownum;
	private Mahasiswa mahasiswa;
	private Long jumlahMataKuliah;
	private Semester semester;
	private String tahunAjaran;
	
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	public Long getJumlahMataKuliah() {
		return jumlahMataKuliah;
	}
	public void setJumlahMataKuliah(Long jumlahMataKuliah) {
		this.jumlahMataKuliah = jumlahMataKuliah;
	}
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	public String getTahunAjaran() {
		return tahunAjaran;
	}
	public void setTahunAjaran(String tahunAjaran) {
		this.tahunAjaran = tahunAjaran;
	}
	@Override
	public int compare(KelasPerkuliahanMahasiswaPerSemester2 arg0,
			KelasPerkuliahanMahasiswaPerSemester2 arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(KelasPerkuliahanMahasiswaPerSemester2 arg0) {
		if (this.mahasiswa.getId()!=arg0.mahasiswa.getId()){
			return this.mahasiswa.compareTo(arg0.mahasiswa);
		} else if (this.tahunAjaran!=arg0.tahunAjaran) {
			return this.tahunAjaran.compareTo(tahunAjaran);
		} else {
			return this.semester.compareTo(arg0.semester);
		}
	}

	
	
}
