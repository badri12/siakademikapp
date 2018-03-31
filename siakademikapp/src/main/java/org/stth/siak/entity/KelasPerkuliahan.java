package org.stth.siak.entity;

import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.stth.siak.enumtype.Semester;

@Entity
public class KelasPerkuliahan implements Comparable<KelasPerkuliahan>, Comparator<KelasPerkuliahan>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private MataKuliah mataKuliah;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan dosenPengampu;
	//private int semester;
	@Enumerated(EnumType.STRING)
	private Semester semester;
	private String tahunAjaran;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private ProgramStudi prodi;
	private String kodeKelas;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public DosenKaryawan getDosenPengampu() {
		return dosenPengampu;
	}
	public void setDosenPengampu(DosenKaryawan dosenPengampu) {
		this.dosenPengampu = dosenPengampu;
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
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public String getKodeKelas() {
		return kodeKelas;
	}
	public void setKodeKelas(String kodeKelas) {
		this.kodeKelas = kodeKelas;
	}
	
	@Override
	public String toString(){
		return mataKuliah+" kelas "+kodeKelas+" "+semester+" "+tahunAjaran;
	}
	@Override
	public int compare(KelasPerkuliahan arg0, KelasPerkuliahan arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(KelasPerkuliahan arg0) {
		if (tahunAjaran!=null) {
			if (!tahunAjaran.equals(arg0.getTahunAjaran())){
				return this.tahunAjaran.compareTo(arg0.getTahunAjaran());
			}
		}
		if (tahunAjaran!=null) {
			if (semester!=arg0.getSemester()){
				return this.semester.compareTo(arg0.getSemester());
			}
		}
		
		if (mataKuliah.getId()!=arg0.getMataKuliah().getId()){
			return this.mataKuliah.compareTo(arg0.getMataKuliah());
		}
		return kodeKelas.compareTo(arg0.kodeKelas);
		
	}
	
}
