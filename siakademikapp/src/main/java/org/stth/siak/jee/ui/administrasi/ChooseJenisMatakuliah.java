package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;

import org.stth.siak.entity.MataKuliahKurikulum;
import org.stth.siak.enumtype.JenisMataKuliah;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ChooseJenisMatakuliah extends Window {
	private static final long serialVersionUID = 1604278070958062081L;
	public ChooseJenisMatakuliah(MataKuliahKurikulum mkk) {
		center();
		setWidth("400px");
		FormLayout fl = new FormLayout();
		TextField tfsemester = new TextField("Semester");
		ComboBox<JenisMataKuliah> cbSifat = new ComboBox<>("Sifat MataKuliah", Arrays.asList(JenisMataKuliah.values()));
		
		Button ok = new Button("OK");
		ok.setIcon(VaadinIcons.CHECK);
		ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(e->{
			if (!cbSifat.isEmpty()) {
				mkk.setJenis(cbSifat.getValue());
				mkk.setSemesterBuka(Integer.valueOf(tfsemester.getValue()));
				setData(mkk);
				close();
			}else Notification.show("", "Pilih Sifat MataKuliah", Notification.Type.WARNING_MESSAGE);
			
		});
		fl.addComponents(tfsemester, cbSifat, ok);
		setContent(fl);
	}


}
