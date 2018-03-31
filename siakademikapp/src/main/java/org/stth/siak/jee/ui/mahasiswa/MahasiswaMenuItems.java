package org.stth.siak.jee.ui.mahasiswa;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum MahasiswaMenuItems {
	DASHBOARD("PROFIL", MahasiswaProfil.class, VaadinIcons.USER, true),
	RENCANASTUDI("RENCANA STUDI", RencanaStudiView.class, VaadinIcons.USER_CARD, false),
	KULIAH_SEMESTER("KELAS DIIKUTI", MahasiswaKelasDiikuti.class, VaadinIcons.CALENDAR_USER, false),
	PERKULIAHAN("RIWAYAT KULIAH", KelasPerkuliahanView.class, VaadinIcons.CALENDAR, false),
	INDEKSPRESTASI("INDEKS PRESTASI", IPKView.class,VaadinIcons.USER_CARD, true),
	KURIKULUM("KURIKULUM", MahasiswaDetailKurikulum.class, VaadinIcons.INSTITUTION, true);

	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private MahasiswaMenuItems(String viewName, Class<? extends View> viewClass,
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
