package org.stth.siak.jee.ui.generalview;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class PilihDosen extends Window{
	private static final long serialVersionUID = 7726461372059892500L;

	public PilihDosen() {
		setCaption("Pilih Dosen");
		center();
		VerticalLayout vl = new VerticalLayout();
		createContent(vl);
		setContent(vl);
	}

	private void createContent(VerticalLayout vl) {
		ComboBox<DosenKaryawan> cbDosen = new ComboBox<>("Pilih Dosen", DosenKaryawanPersistence.getDosen());
		Button bOk =  new Button("OK");
		bOk.setIcon(VaadinIcons.CHECK);
		bOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bOk.addClickListener(e->{
			setData(cbDosen.getValue());
			close();
		});
		Button bC =  new Button("CANCEL");
		bC.setIcon(VaadinIcons.CLOSE);
		bC.addClickListener(e->{
			close();
		});
		HorizontalLayout hlButton = new HorizontalLayout();
		hlButton.addComponents(bOk, bC);
		vl.addComponents(cbDosen, hlButton);
	}
}
