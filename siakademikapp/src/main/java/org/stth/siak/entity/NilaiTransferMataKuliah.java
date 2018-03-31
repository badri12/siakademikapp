package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class NilaiTransferMataKuliah {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=25)
	private String kodeMKAsal;
	@Column(length=75)
	private String matKulAsal;
	private int sksAsal;
	@Column(length=5)
	private String nilaiHurufAsal;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private PesertaKuliah nilaiDiakui;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKodeMKAsal() {
		return kodeMKAsal;
	}
	public void setKodeMKAsal(String kodeMKAsal) {
		this.kodeMKAsal = kodeMKAsal;
	}
	public String getMatKulAsal() {
		return matKulAsal;
	}
	public void setMatKulAsal(String matKulAsal) {
		this.matKulAsal = matKulAsal;
	}
	public int getSksAsal() {
		return sksAsal;
	}
	public void setSksAsal(int sksAsal) {
		this.sksAsal = sksAsal;
	}
	public String getNilaiHurufAsal() {
		return nilaiHurufAsal;
	}
	public void setNilaiHurufAsal(String nilaiHurufAsal) {
		this.nilaiHurufAsal = nilaiHurufAsal;
	}
	public PesertaKuliah getNilaiDiakui() {
		return nilaiDiakui;
	}
	public void setNilaiDiakui(PesertaKuliah nilaiDiakui) {
		this.nilaiDiakui = nilaiDiakui;
	}
	

}
