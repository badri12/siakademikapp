package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class AdministrasiProgramStudi extends VerticalLayout implements View{
	private static final long serialVersionUID = -5501412860746634050L;

	public AdministrasiProgramStudi() {
		addComponent(ViewFactory.header("Administrasi Program Studi"));
		Button add = new Button("Tambah");
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.setIcon(VaadinIcons.PLUS);
		add.addClickListener(klik->{
			AdministrasiEditorProdi aep = new AdministrasiEditorProdi(null);
			GeneralPopups.showGenericWindow(aep, "Edit Program Studi");
		});
		addComponent(add);
		Panel p = new Panel("Daftar Program Studi");
		p.setContent(buildContent());
		addComponent(p);
	}

	private Component buildContent() {

		List<ProgramStudi> l = GenericPersistence.findList(ProgramStudi.class);

		Grid<ProgramStudi> g = new Grid<>("Ditemukan "+l.size()+" Program Studi", l);
		g.removeAllColumns();
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(ProgramStudi::getJenjang).setCaption("JENJANG");
		g.addColumn(ProgramStudi::getKode).setCaption("KODE");
		g.addColumn(ProgramStudi::getNama).setCaption("NAMA");
		g.addColumn(ProgramStudi::getKaprodi).setCaption("KAPRODI");
		g.addComponentColumn(prodi->{
			Button button = new Button("");
			button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			button.setIcon(VaadinIcons.EDIT);
			button.addClickListener(click ->{
				AdministrasiEditorProdi aep = new AdministrasiEditorProdi(prodi);
				GeneralPopups.showGenericWindow(aep, "Edit Program Studi");
			});
			return button;
		});
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
