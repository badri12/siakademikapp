package org.stth.siak.entity;

public class DosenMataKuliah {
	private int id;
	private DosenKaryawan dosen;
	private MataKuliah mataKuliah;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DosenKaryawan getDosen() {
		return dosen;
	}
	public void setDosen(DosenKaryawan dosen) {
		this.dosen = dosen;
	}
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	

}
