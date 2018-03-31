package org.stth.siak.entity;

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
@Subselect("select @rownum:=@rownum+1 rownum," +
		"mahasiswa_id,mataKuliah_id, kelasPerkuliahan_id" +
		"semester,tahunAjaran " +
		"from vkpojrs v, (SELECT @rownum:=0) r " +
		"where rencanaStudi_id is null")
@Synchronize({"vkpojrs"})
public class RencanaStudiKelasPerkuliahan {
	@Id
	private int rownum;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private MataKuliah mataKuliah;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private KelasPerkuliahan kelasPerkuliahan;
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
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public KelasPerkuliahan getKelasPerkuliahan() {
		return kelasPerkuliahan;
	}
	public void setKelasPerkuliahan(KelasPerkuliahan kelasPerkuliahan) {
		this.kelasPerkuliahan = kelasPerkuliahan;
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
	
}
