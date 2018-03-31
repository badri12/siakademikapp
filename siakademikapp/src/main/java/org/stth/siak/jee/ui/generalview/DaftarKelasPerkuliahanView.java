package org.stth.siak.jee.ui.generalview;

import java.util.List;
import org.stth.siak.entity.KelasPerkuliahan;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarKelasPerkuliahanView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private List<KelasPerkuliahan> lkp;
	
	public DaftarKelasPerkuliahanView(List<KelasPerkuliahan> lkp){
		this.lkp = lkp;
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	public Component getTable(){
		Grid<KelasPerkuliahan> g = new Grid<>("Daftar Kelas Perkuliahan ", lkp);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(KelasPerkuliahan::getTahunAjaran).setCaption("T.A").setId("tahunAjaran");
		g.addColumn(KelasPerkuliahan::getSemester).setCaption("SEMESTER").setId("semester");
		g.addColumn(kp->{
			return kp.getMataKuliah();
		}).setCaption("MATAKULIAH").setId("matakuliah");
		g.addColumn(KelasPerkuliahan::getProdi).setCaption("PRODI").setId("prodi");
		g.addComponentColumn(kp->{
			HorizontalLayout hl =  new HorizontalLayout();
			Button b = new Button("Peserta");
			b.setStyleName(ValoTheme.BUTTON_PRIMARY);
			b.setIcon(VaadinIcons.LIST);
			b.addClickListener(e->{
				showPesertaKuliah(kp);
			});
			hl.addComponent(b);
			return hl;
		});
		g.setSizeFull();
		return g;
	}
	
	private void showPesertaKuliah(KelasPerkuliahan o) {
		final Window win = new Window("Daftar Peserta Kuliah");
		Component c = new PesertaKelasPerkuliahanView(o);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}

}
