package org.stth.siak.entity;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.stth.siak.enumtype.Semester;

@Entity
@Subselect("select @rownum:=@rownum+1 rownum,mahasiswa_id,jumlahMataKuliah, semester,tahunAjaran " +
		"from vKelasPerkuliahMahasiswaPerSemester v, (SELECT @rownum:=0) r ")
@Synchronize({"vKelasPerkuliahMahasiswaPerSemester"})
public class KelasPerkuliahanMahasiswaPerSemester implements Comparable<KelasPerkuliahanMahasiswaPerSemester>, Comparator<KelasPerkuliahanMahasiswaPerSemester>{
	@Id
	private int rownum;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	private int jumlahMataKuliah;
	@Enumerated(EnumType.STRING)
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
	public int getJumlahMataKuliah() {
		return jumlahMataKuliah;
	}
	public void setJumlahMataKuliah(int jumlahMataKuliah) {
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
	public int compare(KelasPerkuliahanMahasiswaPerSemester arg0,
			KelasPerkuliahanMahasiswaPerSemester arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(KelasPerkuliahanMahasiswaPerSemester arg0) {
		if (this.mahasiswa.getId()!=arg0.mahasiswa.getId()){
			return this.mahasiswa.compareTo(arg0.mahasiswa);
		} else if (this.tahunAjaran!=arg0.tahunAjaran) {
			return this.tahunAjaran.compareTo(tahunAjaran);
		} else {
			return this.semester.compareTo(arg0.semester);
		}
	}

	
	
}
