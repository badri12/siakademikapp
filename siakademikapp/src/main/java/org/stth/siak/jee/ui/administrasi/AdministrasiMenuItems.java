package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.jee.ui.generalview.ProfilUmumDosenKaryawan;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum AdministrasiMenuItems implements AppMenuItems{
	//DASHBOARD("Dashboard", AdministrasiDashboard.class, VaadinIcons.DASHBOARD, true),
	DASHBOARD("Profil ", ProfilUmumDosenKaryawan.class, VaadinIcons.HOME, true);
	//,DOSEN("Misc", DosenProfil.class, FontAwesome.MALE, false);

	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private AdministrasiMenuItems(String viewName, Class<? extends View> viewClass,
			Resource icon, boolean stateful) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
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



}
