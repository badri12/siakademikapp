package org.stth.siak.jee.ui.dosen;

import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum DosenMenuItems implements AppMenuItems{
	 PROFIL("PROFIL", DosenProfil.class, VaadinIcons.USER, true),
	 LOG_PERKULIAHAN("LOG PERKULIAHAN", DosenLogPerkuliahan.class, VaadinIcons.CALENDAR_USER, false),
	 KELAS_DIAMPU("KELAS DIAMPU", DosenKelasDiampu.class,  VaadinIcons.TABLE, false),
	 PERKULIAHAN("RIWAYAT MENGAJAR", DosenRiwayatMengajar.class, VaadinIcons.CALENDAR, false),
	 MAHASISWA("BIMBINGAN AKADEMIK", DosenStatusBimbinganAkademik.class, VaadinIcons.GROUP, true),
	 RENCANA_STUDI("VERIFIKASI KRS", DosenVerifikasiRencanaStudi.class,  VaadinIcons.CLIPBOARD_CHECK, true),
	 INFO_KURIKULUM("KURIKULUM", DosenDetailKurikulum.class, VaadinIcons.INSTITUTION, true);
	 //,DOSEN("Misc", DosenProfil.class, FontAwesome.MALE, false);
	 
	 private final String viewName;
	 private final Class<? extends View> viewClass;
	 private final Resource icon;
	 private final boolean stateful;
	 
	 private DosenMenuItems(String viewName, Class<? extends View> viewClass,
	            Resource icon, boolean stateful) {
	        this.viewName = viewName;
	        this.viewClass = viewClass;
	        this.icon = icon;
	        this.stateful = stateful;
	    }

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public boolean isStateful() {
		return stateful;
	}
	 


}
