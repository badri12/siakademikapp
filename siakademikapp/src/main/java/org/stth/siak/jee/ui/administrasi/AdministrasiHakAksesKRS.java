package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.HakAksesKRSPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.HakAksesRencanaStudiOnline;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.ui.util.ConfirmDialog;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

@SuppressWarnings("serial")
public class AdministrasiHakAksesKRS extends VerticalLayout implements View {
	private Panel pDaftar = new Panel("");
	private Mahasiswa m;
	private KonfigurasiPersistence kf = new KonfigurasiPersistence();
	private Grid<HakAksesRencanaStudiOnline> g = new Grid<>();
	private List<HakAksesRencanaStudiOnline> l;
	private List<HakAksesRencanaStudiOnline> lKRS;
	private String ta;
	private Semester semester;
	private ComboBox<ProgramStudi> cbProdi;
	private ProgramStudi prodi;
	private Map<String, Mahasiswa> mapMHS;
	List<Mahasiswa> lm= MahasiswaPersistence.getListByExample(new Mahasiswa());
	private AutocompleteTextField mhs;

	public AdministrasiHakAksesKRS() {
		ta=kf.getKRSTa();
		semester=kf.getKRSSemester();
		addComponent(tambahHakAksesMhs());
		pDaftar.setContent(daftarHakKRS());
		addComponent(pDaftar);
	}
	private HorizontalLayout tambahHakAksesMhs() {
		cbProdi = new ComboBox<>();
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setPlaceholder("filter by prodi");
		cbProdi.setWidth("200px");
		
		cbProdi.addValueChangeListener(e->{
			lKRS=new ArrayList<>();
			prodi=cbProdi.getValue();
			if (prodi!=null) {
				for (HakAksesRencanaStudiOnline hak : l) {
					if (prodi.getId()==hak.getMahasiswa().getProdi().getId()) {
						lKRS.add(hak);
					}
				}
			}else lKRS.addAll(l);
			updateGrid();
		});

		mhs = new AutocompleteTextField();
		mhs.setWidth("300px");
		mhs.setPlaceholder("Inputkan nim atau nama mahasiswa");
		mhs.addValueChangeListener(e->{
			lKRS=new ArrayList<>();	
			if (!mhs.getValue().isEmpty()) {
				m=mapMHS.get(mhs.getValue());
				if (m!=null) {
					for (HakAksesRencanaStudiOnline hak : l) {
						if (m.getId()==hak.getMahasiswa().getId()) {
							lKRS.add(hak);
						}
					}
				};
			}else{
				lKRS.addAll(l);
			}
			updateGrid();
		});
		mhs.setValueChangeMode(ValueChangeMode.LAZY);
		System.out.println(lm.size());
		mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}
		updatemapMHs();

		mhs.setMinChars(3);
		Button b = new Button("");
		b.setIcon(VaadinIcons.PLUS);
		b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		b.setDescription("Tambah hak akses KRS");
		b.addClickListener(e->{
			m=mapMHS.get(mhs.getValue());
			if (m!=null) {
				HakAksesRencanaStudiOnline hakExist = HakAksesKRSPersistence.getByMhs(m, ta, semester);
				if (hakExist==null) {
					ConfirmDialog.show(getUI(), "", "Berikan hak akses KRS \n untuk "+m.toString()+"?", "Ya", "Tidak", cf->{
						if (cf.isConfirmed()) {
							HakAksesRencanaStudiOnline hak = new HakAksesRencanaStudiOnline();
							hak.setMahasiswa(m);
							hak.setSemester(semester);
							hak.setTahunAjaran(ta);
							GenericPersistence.merge(hak);
							updateList();
						}
					});
				}else Notification.show("Mahasiswa ini sudah memiliki hak akses KRS", Type.ERROR_MESSAGE);
			}else Notification.show("Inputkan nim atau nama mahasiswa pada field yang tersedia", Type.ERROR_MESSAGE);
		});
		HorizontalLayout hl =new HorizontalLayout();
		hl.addComponents(mhs,b, cbProdi);

		return hl;
	}
	private void updatemapMHs() {

		AutocompleteSuggestionProvider acspm = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		mhs.setSuggestionProvider(acspm);

	}
	private Component daftarHakKRS(){
		updateList();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.setSizeFull();
		g.addColumn(value -> "", row).setCaption("NO");
		g.addComponentColumn(hkrs->{
			return new Label(hkrs.getMahasiswa().getNpm());
		}).setCaption("NIM");
		g.addComponentColumn(hkrs->{
			return new Label(hkrs.getMahasiswa().getNama());
		}).setCaption("NAMA");
		g.addColumn(HakAksesRencanaStudiOnline::getSemester).setCaption("SEMESTER");
		g.addColumn(HakAksesRencanaStudiOnline::getTahunAjaran).setCaption("T.A");

		g.addComponentColumn(hkrs->{
			Button b = new Button("");
			b.setIcon(VaadinIcons.TRASH);
			b.setStyleName(ValoTheme.BUTTON_DANGER);
			b.setDescription("Hapus Hak Akses KRS");
			b.addClickListener(e->{
				ConfirmDialog.show(getUI(), "", "Hapus hak akses KRS \n untuk "
						+hkrs.getMahasiswa().toString()+"?", "Ya", "Tidak", cf->{
							if (cf.isConfirmed()) {
								GenericPersistence.delete(hkrs);
								updateList();
							}
						});
			});
			return b;
		}).setCaption("HAPUS");
		return g;
	}
	private void updateList(){
		l = HakAksesKRSPersistence.getBySemTa(ta, semester);
		lKRS=new ArrayList<>();
		lKRS.addAll(l);
		updateGrid();
		
	}
	private void updateGrid() {
		g.setItems(lKRS);
		pDaftar.setCaption("Daftar Hak Akses KRS ("+lKRS.size()+")");
	}
}
