package org.stth.siak.jee.ui.administrasi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.StatusMasuk;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.ExportToExcel;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdministrasiNilaiTransfer extends VerticalLayout implements View{
	private Panel p = new Panel();
	private Panel pFilter = new Panel("Filter");
	private Mahasiswa example;
	private HorizontalLayout hlbt = new HorizontalLayout();;
	private static final long serialVersionUID = -1426840100353763003L;
	private Grid<Mahasiswa> g ;
	public AdministrasiNilaiTransfer() {
		addComponent(ViewFactory.header("Daftar Mahasiswa Transfer dan Pindahan"));
		addComponent(pFilter);
		pFilter.setContent(filter());
		addComponent(p);
		
		
		
		addComponent(hlbt);
	}
	
	private Component filter() {
		HorizontalLayout hl = new HorizontalLayout();
		TextField tfNama = new TextField("Nama");
		ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Prodi", GenericPersistence.findList(ProgramStudi.class));
		
		Button bExport = new Button("Export");
		bExport.setDescription("Export data ke excel");
		bExport.setIcon(VaadinIcons.DOWNLOAD);
		bExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bExport.addClickListener(e->{
			if (g.getSelectedItems().size()>0) {
				try {
					StreamResource source =ExportToExcel.createFileExcel(ExportToExcel.nilaiTransfer(g.getSelectedItems()), "Nilai Transfer");
					ResourceReference ref = new ResourceReference(source, this, "nilaiTransfer"+source.hashCode());
				    this.setResource("nilaiTransfer"+source.hashCode(), source);
				    getUI().getPage().open(ref.getURL(), null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else Notification.show("Pilih Mahasiswa terlebih dahulu", Type.ERROR_MESSAGE);
			
		});
		
		ComboBox<StatusMasuk> cbStatusMasuk = new ComboBox<>("Status Masuk", Arrays.asList(StatusMasuk.PINDAHAN, StatusMasuk.TRANSFER));
		Button bSearch = new Button("");
		bSearch.setIcon(VaadinIcons.SEARCH);
		bSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bSearch.addClickListener(click->{
			example = new Mahasiswa();
			example.setNama(tfNama.getValue());
			example.setProdi(cbProdi.getValue());
			example.setStatusMasuk(cbStatusMasuk.getValue());
			p.setContent(daftarMhs());
			hlbt.removeAllComponents();
			hlbt.addComponent(bExport);
		});
		
		FormLayout flLeft = new FormLayout();
		FormLayout flRight = new FormLayout();
		flLeft.addComponents(tfNama, cbProdi);
		flRight.addComponents(cbStatusMasuk, bSearch);
		hl.setSpacing(true);
		hl.addComponents(flLeft, flRight);
		return hl;
	}

	private Component daftarMhs(){	
		List<Mahasiswa> l = MahasiswaPersistence.getTransfer(example);

		g =  new Grid<>("Ditemukan "+ l.size()+" mahasiswa",l);
		g.removeAllColumns();
		g.setSizeFull();
		g.setSelectionMode(SelectionMode.MULTI);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(Mahasiswa::getNpm).setCaption("NIM");
		g.addColumn(Mahasiswa::getNama).setCaption("NAMA");
		g.addColumn(Mahasiswa::getProdi).setCaption("Program Studi");
		g.addColumn(Mahasiswa::getStatusMasuk).setCaption("STATUS MASUK");
		g.addComponentColumn(mhs->{
			Button b = new Button();
			b.setIcon(VaadinIcons.EDIT);
			b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.addClickListener(e->{
				AdministrasiEditorNilaiTransfer aentf =  new AdministrasiEditorNilaiTransfer(mhs);
				GeneralPopups.showGenericWindow(aentf, "Nilai Transfer Mahasiswa");
			});
			return b;
		});
		return g;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

}
