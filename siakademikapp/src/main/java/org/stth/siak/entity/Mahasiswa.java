package org.stth.siak.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;


@Entity
public class Mahasiswa implements Comparable<Mahasiswa>, Comparator<Mahasiswa>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nama;
	@Column(unique=true)
	private String npm;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private ProgramStudi prodi;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private MasterKelas kelas;
	private int angkatan;
	@Enumerated(EnumType.STRING)
	private JenisKelamin jenisKelamin;
	private String agama;
	private String alamat;
	private String email;
	private String nomorHP;
	private LocalDate tanggalLahir;
	private String tempatLahir;
	private String asalSekolah;
	private int tahunLulus;
	private String wali;
	private String hubDenganWali;
	private String alamatWali;
	private String nomorHPWali;
	@Enumerated(EnumType.STRING)
	private StatusMahasiswa status;
	private byte[] password;
	private byte[] salt;
	private LocalDateTime lastSuccessfulLogin;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private DosenKaryawan pembimbingAkademik;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan entryOleh;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan updateOleh;
	private String judulSkripsi;
	private String noSeriIjazah;
	private String ttldiIjazah;
	private LocalDate tanggalLulus;
	private String facebook;
	@Column(length=50)
	private String angkatanLulus;
	@Enumerated(EnumType.STRING)
	private StatusMasuk statusMasuk;
	@ManyToOne  @PrimaryKeyJoinColumn
	private Kurikulum k;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNpm() {
		return npm;
	}
	public void setNpm(String npm) {
		this.npm = npm;
	}
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public MasterKelas getKelas() {
		return kelas;
	}
	public void setKelas(MasterKelas kelas) {
		this.kelas = kelas;
	}
	public int getAngkatan() {
		return angkatan;
	}
	public void setAngkatan(int angkatan) {
		this.angkatan = angkatan;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	public int getTahunLulus() {
		return tahunLulus;
	}
	public void setTahunLulus(int tahunLulus) {
		this.tahunLulus = tahunLulus;
	}
	public JenisKelamin getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(JenisKelamin jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNomorHP() {
		return nomorHP;
	}
	public void setNomorHP(String nomorHP) {
		this.nomorHP = nomorHP;
	}
	public LocalDate getTanggalLahir() {
		return tanggalLahir;
	}
	public void setTanggalLahir(LocalDate tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
	public String getTempatLahir() {
		return tempatLahir;
	}
	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
	public String getAsalSekolah() {
		return asalSekolah;
	}
	public void setAsalSekolah(String asalSekolah) {
		this.asalSekolah = asalSekolah;
	}
	public String getWali() {
		return wali;
	}
	public void setWali(String wali) {
		this.wali = wali;
	}
	public String getHubDenganWali() {
		return hubDenganWali;
	}
	public void setHubDenganWali(String hubDenganWali) {
		this.hubDenganWali = hubDenganWali;
	}
	public String getAlamatWali() {
		return alamatWali;
	}
	public void setAlamatWali(String alamatWali) {
		this.alamatWali = alamatWali;
	}
	public String getNomorHPWali() {
		return nomorHPWali;
	}
	public void setNomorHPWali(String nomorHPWali) {
		this.nomorHPWali = nomorHPWali;
	}
	public StatusMahasiswa getStatus() {
		return status;
	}
	public void setStatus(StatusMahasiswa status) {
		this.status = status;
	}
	public DosenKaryawan getPembimbingAkademik() {
		return pembimbingAkademik;
	}
	public void setPembimbingAkademik(DosenKaryawan pembimbingAkademik) {
		this.pembimbingAkademik = pembimbingAkademik;
	}
	public LocalDateTime getLastSuccessfulLogin() {
		return this.lastSuccessfulLogin;
	}

	public void setLastSuccessfulLogin(LocalDateTime lastSuccessfulLogin) { 
		this.lastSuccessfulLogin = lastSuccessfulLogin; 
	}

	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public byte[] getSalt() {
		return salt;
	}
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	public DosenKaryawan getEntryOleh() {
		return entryOleh;
	}
	public void setEntryOleh(DosenKaryawan entryOleh) {
		this.entryOleh = entryOleh;
	}
	public DosenKaryawan getUpdateOleh() {
		return updateOleh;
	}
	public void setUpdateOleh(DosenKaryawan updateOleh) {
		this.updateOleh = updateOleh;
	}
	public String getJudulSkripsi() {
		return judulSkripsi;
	}
	public void setJudulSkripsi(String judulSkripsi) {
		this.judulSkripsi =(judulSkripsi);
	}
	public String getNoSeriIjazah() {
		return noSeriIjazah;
	}
	public void setNoSeriIjazah(String noSeriIjazah) {
		this.noSeriIjazah = noSeriIjazah;
	}
	public String getTtldiIjazah() {
		return ttldiIjazah;
	}
	public void setTtldiIjazah(String ttldiIjazah) {
		this.ttldiIjazah = ttldiIjazah;
	}
	public LocalDate getTanggalLulus() {
		return tanggalLulus;
	}
	public void setTanggalLulus(LocalDate tanggalLulus) {
		this.tanggalLulus = tanggalLulus;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getAngkatanLulus() {
		return angkatanLulus;
	}
	public void setAngkatanLulus(String angkatanLulus) {
		this.angkatanLulus = angkatanLulus;
	}
	public StatusMasuk getStatusMasuk() {
		return statusMasuk;
	}
	public void setStatusMasuk(StatusMasuk statusMasuk) {
		this.statusMasuk = statusMasuk;
	}
	public Kurikulum getK() {
		return k;
	}
	public void setK(Kurikulum k) {
		this.k = k;
	}
	@Override
	public String toString(){
		return npm+" "+nama+" "+angkatan;
	}
	@Override
	public int compare(Mahasiswa arg0, Mahasiswa arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(Mahasiswa arg0) {
		return npm.compareTo(arg0.npm);
	}



}
