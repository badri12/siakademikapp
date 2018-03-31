package org.stth.siak.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import org.stth.siak.enumtype.JenisKelamin;


@Entity
public class DosenKaryawan implements Comparable<DosenKaryawan>, Comparator<DosenKaryawan> {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nama;
	private String alias;
	private String nidn;
	private String nis;
	private String tempatLahir;
	@Enumerated(EnumType.STRING)
	private JenisKelamin jenisKelamin;
	private String agama;
	private LocalDate tanggalLahir;
	private String email;
	private byte[] password;
	private byte[] salt;
	private String alamatRumah;
	private String nomorTelepon;
	private String nomorKtp;
	private String status;
	private String golongan;
	private int thnMasuk; 
	private String jenjangPendTerakhir;
	private String prodiPendTerakhir;
	private String institusiPendTerakhir;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan updateOleh;
	private LocalDateTime lastSuccessfulLogin;
	private boolean dosen;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private ProgramStudi prodi;
	@ManyToOne(cascade = CascadeType.REFRESH)
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getNidn() {
		return nidn;
	}
	public void setNidn(String nidn) {
		this.nidn = nidn;
	}
	public String getNis() {
		return nis;
	}
	public void setNis(String nis) {
		this.nis = nis;
	}
	public String getTempatLahir() {
		return tempatLahir;
	}
	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
	public LocalDate getTanggalLahir() {
		return tanggalLahir;
	}
	public void setTanggalLahir(LocalDate tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAlamatRumah() {
		return alamatRumah;
	}
	public void setAlamatRumah(String alamatRumah) {
		this.alamatRumah = alamatRumah;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	public String getNomorTelepon() {
		return nomorTelepon;
	}
	public void setNomorTelepon(String nomorTelepon) {
		this.nomorTelepon = nomorTelepon;
	}
	public String getNomorKtp() {
		return nomorKtp;
	}
	public void setNomorKtp(String nomorKtp) {
		this.nomorKtp = nomorKtp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGolongan() {
		return golongan;
	}
	public void setGolongan(String golongan) {
		this.golongan = golongan;
	}
	public int getThnMasuk() {
		return thnMasuk;
	}
	public void setThnMasuk(int thnMasuk) {
		this.thnMasuk = thnMasuk;
	}
	public String getJenjangPendTerakhir() {
		return jenjangPendTerakhir;
	}
	public void setJenjangPendTerakhir(String jenjangPendTerakhir) {
		this.jenjangPendTerakhir = jenjangPendTerakhir;
	}
	public String getProdiPendTerakhir() {
		return prodiPendTerakhir;
	}
	public void setProdiPendTerakhir(String prodiPendTerakhir) {
		this.prodiPendTerakhir = prodiPendTerakhir;
	}
	public String getInstitusiPendTerakhir() {
		return institusiPendTerakhir;
	}
	public void setInstitusiPendTerakhir(String institusiPendTerakhir) {
		this.institusiPendTerakhir = institusiPendTerakhir;
	}
	public DosenKaryawan getUpdateOleh() {
		return updateOleh;
	}
	public void setUpdateOleh(DosenKaryawan updateOleh) {
		this.updateOleh = updateOleh;
	}
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public JenisKelamin getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(JenisKelamin jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	public LocalDateTime getLastSuccessfulLogin() {
		return lastSuccessfulLogin;
	}
	public void setLastSuccessfulLogin(LocalDateTime lastSuccessfulLogin) {
		this.lastSuccessfulLogin = lastSuccessfulLogin;
	}
	public String getPendidikanTerakhir(){
		return jenjangPendTerakhir+" "+prodiPendTerakhir+" "+institusiPendTerakhir;
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
	@Override
	public String toString(){
		return nama;
	}
	public boolean isDosen() {
		return dosen;
	}
	public void setDosen(boolean dosen) {
		this.dosen = dosen;
	}
	@Override
	public int compare(DosenKaryawan arg0, DosenKaryawan arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(DosenKaryawan o) {
		return nama.compareTo(o.nama);
	}
	

}
