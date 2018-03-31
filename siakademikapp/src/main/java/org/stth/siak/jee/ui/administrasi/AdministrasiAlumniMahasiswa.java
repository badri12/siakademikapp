package org.stth.siak.jee.ui.administrasi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;


public class AdministrasiAlumniMahasiswa extends VerticalLayout implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel content = new Panel("Daftar Alumni Mahasiswa");
	private ComboBox<ProgramStudi> cbProdi ;
	protected ProgramStudi prodi;
	private TextField nama = new TextField("Nama");
	private TextField nim = new TextField("NIM");
	private TextField angkatan = new TextField("Angkatan");
	private Button btnCari = new Button("Cari");
	private AutocompleteTextField atf;
	private List<Mahasiswa> l;
	private Map<String, Mahasiswa> map ;
	private List<String> col;

	public AdministrasiAlumniMahasiswa() {
		setMargin(true);
		setSpacing(true);
		HorizontalLayout hl = tambahAlumni();
		addComponent(ViewFactory.header("Administrasi Data Alumni"));
		addComponent(hl);
		addComponent(filterLulusan());
		addComponent(content);
		btnCari.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCari.setIcon(VaadinIcons.SEARCH);
		btnCari.addClickListener(click->cari());

	}
	private HorizontalLayout tambahAlumni() {
		atf = new AutocompleteTextField();
		atf.setWidth("400px");
		atf.setPlaceholder("inputkan nim atau nama mahasiswa yang masih aktif");
		addSuggesttoTextField();
		Button b = new Button("Tambah");
		b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		b.setIcon(VaadinIcons.PLUS);
		b.addClickListener(klik->{
			Mahasiswa mSelected = map.get(atf.getValue());
			if (mSelected!=null) {
				AdministrasiEditorAlumni aa = new AdministrasiEditorAlumni(mSelected);
				getUI().addWindow(aa);
				aa.addCloseListener(close->{
					atf.clear();
					addSuggesttoTextField();
				});
			}else{
				Notification.show("inputkan mahasiswa atau nim di textfield", Notification.Type.WARNING_MESSAGE);
			}
			
		});
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(atf, b);
		return hl;
	}
	private void addSuggesttoTextField() {

		Mahasiswa example = new Mahasiswa();
		example.setStatus(StatusMahasiswa.AKTIF);
		l = MahasiswaPersistence.getListByExample(example);
		map = new HashMap<>();
		col = new ArrayList<>();
		for (Mahasiswa m : l) {
			col.add(m.toString());
			map.put(m.toString(), m);
		}
		System.out.println(l.size()+" "+col.size()+" "+map.size());
		AutocompleteSuggestionProvider sP = new CollectionSuggestionProvider(map.keySet(), MatchMode.CONTAINS, true);
		atf.setDelay(500);
		atf.setMinChars(3);
		atf.setSuggestionProvider(sP);

	}
	private Component filterLulusan() {
		Panel pnl = new Panel("Filter");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		FormLayout flButton = new FormLayout();

		
		cbProdi = new ComboBox<>("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		cbProdi.addValueChangeListener(e->{
			prodi = e.getValue();
		});
		flButton.addComponent(btnCari);
		flKiri.addComponents(nama, nim);
		flKanan.addComponents(angkatan, cbProdi);
		hl.addComponents(flKiri, flKanan, flButton);
		pnl.setContent(hl);
		return pnl;
	}
	protected void cari() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		Mahasiswa example = new Mahasiswa();
		example.setNama(nama.getValue());
		example.setProdi(prodi);
		example.setNpm(nim.getValue());
		example.setStatus(StatusMahasiswa.LULUS);
		if (!angkatan.getValue().isEmpty()) {
			example.setAngkatan(Integer.parseInt(angkatan.getValue()));
		}
		List<Mahasiswa> l = MahasiswaPersistence.getListByExample(example);
		
		Grid<Mahasiswa> g = new Grid<>(Mahasiswa.class);
		g.setItems(l);
		g.setSizeFull();
		g.setCaption("Ditemukan "+ l.size()+ " mahasiswa");
		g.removeAllColumns();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(Mahasiswa::getNama).setCaption("NAMA");
		g.addColumn(Mahasiswa::getProdi).setCaption("PRODI");
		g.addColumn(Mahasiswa::getTempatLahir).setCaption("TEMPAT LAHIR");
		g.addColumn(Mahasiswa::getTanggalLahir).setCaption("TGL LAHIR");
		//g.addColumn("alamat").setHeaderCaption("ALAMAT");
		//g.addColumn("Bekerja").setCaption("BEKERJA");
		g.addComponentColumn(mhs->{
			Button b= new Button();
			b.setIcon(VaadinIcons.EDIT);
			b.addClickListener(e->{
				AdministrasiEditorAlumni aa = new AdministrasiEditorAlumni(mhs);
				getUI().addWindow(aa);
			});
			return b;
		});
		vl.setMargin(false);
		vl.addComponent(g);
		content.setContent(vl);

	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
