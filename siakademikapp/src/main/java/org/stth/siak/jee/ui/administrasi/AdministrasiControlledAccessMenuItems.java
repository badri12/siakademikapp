package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.jee.ui.generalview.CekJudul;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum AdministrasiControlledAccessMenuItems implements AppMenuItems{
	 
	 LOG_PERKULIAHAN("LOG PERKULIAHAN", AdministrasiDaftarLogPerkuliahan.class, VaadinIcons.CALENDAR, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 REKAP_HADIR_DOSEN("KEHADIRAN DOSEN", AdministrasiRekapKehadiranDosen.class, VaadinIcons.CALENDAR_USER, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 JADWAL_PERKULIAHAN("Jadwal Perkuliahan", AdministrasiDaftarJadwalKuliah.class, VaadinIcons.TABLE, false, ACLAdministrasiEnum.JADWAL_KULIAH),
	 CETAK_ABSENSI("CETAK ABSENSI", AdministrasiCetakAbsensi.class, VaadinIcons.PRINT, false, ACLAdministrasiEnum.LOG_PERKULIAHAN),
	 DATA_MAHASISWA("DATA MAHASIWA", AdministrasiCetakTranskripMahasiswa.class, VaadinIcons.GROUP, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 //DATA_MAHASISWA("Administrasi Data Mahasiswa", AdministrasiDataMahasiswa.class, FontAwesome.USERS, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 DATA_DOSEN("DATA DOSEN", AdministrasiDataDosen.class, VaadinIcons.USERS, false, ACLAdministrasiEnum.EDIT_DOSEN),
	 PRODI("PROGRAM STUDI", AdministrasiProgramStudi.class, VaadinIcons.INSTITUTION, false, ACLAdministrasiEnum.EDIT_DOSEN),
	 DATA_ALUMNI("DATA ALUMNI", AdministrasiAlumniMahasiswa.class,VaadinIcons.ACADEMY_CAP, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 KELAS_PERKULIAHAN("KELAS PERKULIAHAN", AdministrasiKelasPerkuliahan.class,VaadinIcons.INSTITUTION, false, ACLAdministrasiEnum.ADD_KELASPERKULIAHAN),
	 MAHASISWA_AKTIF("KARTU STUDI", AdministrasiKartudanHasilStudi.class, VaadinIcons.USER_CARD, false, ACLAdministrasiEnum.EDIT_MAHASISWA),
	 RENCANA_STUDI("RENCANA STUDI", AdministrasiDaftarRencanaStudi.class, VaadinIcons.INSTITUTION, false, ACLAdministrasiEnum.TOLAK_KRS),
	 MATAKULIAH("MATAKULIAH", AdministrasiMataKuliah.class, VaadinIcons.INSTITUTION, false, ACLAdministrasiEnum.MATAKULIAH),
	 KURIKULUM("KURIKULUM", AdministrasiKurikulum.class,VaadinIcons.INSTITUTION, false, ACLAdministrasiEnum.MATAKULIAH),
	 KONFIGURASI("KONFIGURASI", KonfigurasiAkademik.class, VaadinIcons.WRENCH, false, ACLAdministrasiEnum.KONFIGURASI),
	 MATAKULIAH_RENCANASTUDI("MATAKULIAH KRS", AdministrasiMataKuliahRencanaStudi.class, VaadinIcons.TABLE, false, ACLAdministrasiEnum.TOLAK_KRS),
	 MAHASISWA_TRANSFER("MAHASISWA TRANSFER", AdministrasiNilaiTransfer.class, VaadinIcons.EXCHANGE, false, ACLAdministrasiEnum.TOLAK_KRS),
	 JUDUL_SKRIPSI("JUDUL SKRIPSI", CekJudul.class, VaadinIcons.DIPLOMA, false, ACLAdministrasiEnum.JUDUL),
	 HAK_AKSES_KRS("HAK AKSES KRS", AdministrasiHakAksesKRS.class, VaadinIcons.USER_CARD, false, ACLAdministrasiEnum.HAKKRS);
	
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 private final ACLAdministrasiEnum acl;
	 
	 private AdministrasiControlledAccessMenuItems(String viewName, Class<? extends View> viewClass,
	            Resource icon, boolean stateful, ACLAdministrasiEnum acl) {
	        this.viewName = viewName;
	        this.viewClass = viewClass;
	        this.icon = icon;
	        this.stateful = stateful;
	        this.acl = acl;
	    }

	@Override
	public String getViewName() {
		return viewName;
	}

	@Override
	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	@Override
	public Resource getIcon() {
		return icon;
	}

	@Override
	public boolean isStateful() {
		return stateful;
	}

	public ACLAdministrasiEnum getAccessControlList(){
		return acl;
	}

}
