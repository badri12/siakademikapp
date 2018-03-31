package org.stth.siak.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.stth.siak.enumtype.RencanaStudiCreationMethod;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
@Entity
public class RencanaStudiMahasiswa {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	@Enumerated(EnumType.STRING)
	private Semester semester;
	private String tahunAjaran;
	private double ipk;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan pembimbingAkademik;
	private LocalDate submitted;//
	private LocalDate approved;
	private LocalDate created;
	@Enumerated(EnumType.STRING)
	private RencanaStudiCreationMethod creationMethod;
	@Enumerated(EnumType.STRING)
	private StatusRencanaStudi status;//if rejected, mahasiswa can rearrange and resubmit after needed modifications
	private String remarks;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
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
	public void setTahunAjaran(String tahun) {
		this.tahunAjaran = tahun;
	}
	public double getIpk() {
		return ipk;
	}
	public void setIpk(double ipk) {
		this.ipk = ipk;
	}
	public DosenKaryawan getPembimbingAkademik() {
		return pembimbingAkademik;
	}
	public void setPembimbingAkademik(DosenKaryawan pembimbingAkademik) {
		this.pembimbingAkademik = pembimbingAkademik;
	}
	public LocalDate getSubmitted() {
		return submitted;
	}
	public void setSubmitted(LocalDate submitted) {
		this.submitted = submitted;
	}
	public LocalDate getApproved() {
		return approved;
	}
	public void setApproved(LocalDate approved) {
		this.approved = approved;
	}
	public LocalDate getCreated() {
		return created;
	}
	public void setCreated(LocalDate created) {
		this.created = created;
	}
	public RencanaStudiCreationMethod getCreationMethod() {
		return creationMethod;
	}
	public void setCreationMethod(RencanaStudiCreationMethod creationMethod) {
		this.creationMethod = creationMethod;
	}
	public StatusRencanaStudi getStatus() {
		return status;
	}
	public void setStatus(StatusRencanaStudi status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
