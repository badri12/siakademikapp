package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.List;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.UserAccessRightsAdministrasi;

import com.github.appreciated.app.layout.builder.NavigatorAppLayoutBuilder;
import com.github.appreciated.app.layout.builder.elements.builders.SubmenuBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;

public class AdministrasiMenuComponent {
	private NavigatorAppLayoutBuilder drawer;
	private List<UserAccessRightsAdministrasi> lacl;
	public AdministrasiMenuComponent(NavigatorAppLayoutBuilder drawer) {
		this.drawer=drawer;
		lacl=new ArrayList<>();
		List<?> lo = (List<?>) VaadinSession.getCurrent().getAttribute("admrights");
		for (Object o : lo) {
			lacl.add((UserAccessRightsAdministrasi) o);
		}
		mahasiswa();
		dosen();
		perkuliahan();
		akademik();
	}

	private void mahasiswa() {
		AdministrasiControlledAccessMenuItems[] items = new AdministrasiControlledAccessMenuItems[]{
				AdministrasiControlledAccessMenuItems.DATA_MAHASISWA,
				AdministrasiControlledAccessMenuItems.MAHASISWA_TRANSFER,
				AdministrasiControlledAccessMenuItems.DATA_ALUMNI,
				AdministrasiControlledAccessMenuItems.JUDUL_SKRIPSI
		};
		SubmenuBuilder submenu = SubmenuBuilder.get("MAHASISWA", VaadinIcons.FILE_TREE);
		int menu=0;
		for (AdministrasiControlledAccessMenuItems item : items) {
			if(ACLAdministrasiEnum.isEligibleTo(lacl, item.getAccessControlList())){
				menu++;
				submenu.add(item.getViewName(), item.name() ,item.getIcon(), item.getViewClass());
			}
		}
		if (menu>0) {
			drawer.add(submenu.build());
		}

	}
	private void dosen() {
		AdministrasiControlledAccessMenuItems[] items = new AdministrasiControlledAccessMenuItems[]{
				AdministrasiControlledAccessMenuItems.DATA_DOSEN,
				AdministrasiControlledAccessMenuItems.REKAP_HADIR_DOSEN,
				
		};
		int menu=0;
		SubmenuBuilder submenu = SubmenuBuilder.get("DOSEN", VaadinIcons.FILE_TREE);
		for (AdministrasiControlledAccessMenuItems item : items) {
			if(ACLAdministrasiEnum.isEligibleTo(lacl, item.getAccessControlList())){
				menu++;
				submenu.add(item.getViewName(), item.name(), item.getIcon(), item.getViewClass());
			}
		}
		
		if (menu>0) {
			drawer.add(submenu.build());
		}

	}
	private void perkuliahan() {
		AdministrasiControlledAccessMenuItems[] items = new AdministrasiControlledAccessMenuItems[]{
				AdministrasiControlledAccessMenuItems.KELAS_PERKULIAHAN,
				AdministrasiControlledAccessMenuItems.RENCANA_STUDI,
				AdministrasiControlledAccessMenuItems.LOG_PERKULIAHAN,
				AdministrasiControlledAccessMenuItems.CETAK_ABSENSI,
				AdministrasiControlledAccessMenuItems.MAHASISWA_AKTIF
				
		};
		int menu=0;
		SubmenuBuilder submenu = SubmenuBuilder.get("PERKULIAHAN", VaadinIcons.FILE_TREE);
		for (AdministrasiControlledAccessMenuItems item : items) {
			if(ACLAdministrasiEnum.isEligibleTo(lacl, item.getAccessControlList())){
				menu++;
				submenu.add(item.getViewName(), item.name(), item.getIcon(), item.getViewClass());
			}
		}
		if (menu>0) {
			drawer.add(submenu.build());
		}

	}
	private void akademik() {
		AdministrasiControlledAccessMenuItems[] items = new AdministrasiControlledAccessMenuItems[]{
				AdministrasiControlledAccessMenuItems.PRODI,
				AdministrasiControlledAccessMenuItems.KURIKULUM,
				AdministrasiControlledAccessMenuItems.MATAKULIAH,
				AdministrasiControlledAccessMenuItems.MATAKULIAH_RENCANASTUDI,
				AdministrasiControlledAccessMenuItems.HAK_AKSES_KRS,
				AdministrasiControlledAccessMenuItems.KONFIGURASI
				
		};
		int menu=0;
		SubmenuBuilder submenu = SubmenuBuilder.get("AKADEMIK", VaadinIcons.FILE_TREE);
		for (AdministrasiControlledAccessMenuItems item : items) {
			if(ACLAdministrasiEnum.isEligibleTo(lacl, item.getAccessControlList())){
				menu++;
				submenu.add(item.getViewName(), item.name(), item.getIcon(), item.getViewClass());
			}
		}
		if (menu>0) {
			drawer.add(submenu.build());
		}
	}


}
