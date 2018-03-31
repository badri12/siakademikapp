package org.stth.siak.jee.ui.administrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MataKuliahPersistence;
import org.stth.jee.persistence.MatakuliahPrasyaratPersistence;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahPrasyarat;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

@SuppressWarnings("serial")
public class AdministrasiEditorPrasyarat extends Window {
	private MataKuliah mkul;
	private Grid<MataKuliahPrasyarat> g = new Grid<>();
	private VerticalLayout vl = new VerticalLayout();
	public AdministrasiEditorPrasyarat(MataKuliah mkul) {
		this.mkul=mkul;
		button();
		createGrid();
		updateList();
		center();
		setWidth("600px");
		setModal(true);
		setContent(vl);
		setCaption("Matakuliah Prasyarat "+mkul.toString());
	}

	private void button() {
		AutocompleteTextField mk = new AutocompleteTextField("Mata Kuliah");
		mk.setWidth("250px");
		mk.setPlaceholder("Inputkan kode atau nama matakuliah");
		List<MataKuliah> lkp = MataKuliahPersistence.getByProdi(mkul.getProdiPemilik());
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk2 : lkp) {
			mapmk.put(mk2.toString(), mk2);
		}
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		mk.setSuggestionProvider(acsp);
		mk.setMinChars(3);
		
		Button add= new Button();
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(e->{
			if (mk.getValue()!=null) {
				MataKuliahPrasyarat mkpr = new MataKuliahPrasyarat();
				mkpr.setMataKuliah(mkul);
				mkpr.setPrasyarat(mapmk.get(mk.getValue()));
				GenericPersistence.merge(mkpr);
				updateList();
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(mk,add);
		hl.setComponentAlignment(add, Alignment.BOTTOM_LEFT);
		vl.addComponent(hl);
	}
	
	private void updateList() {
		List<MataKuliahPrasyarat> lmk = MatakuliahPrasyaratPersistence.getPrasyarat(mkul);
		g.setItems(lmk);
	}

	private void createGrid() {
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addComponentColumn(mkpr->{
			return new Label(mkpr.getPrasyarat().getKode());
		}).setCaption("Kode");
		g.addComponentColumn(mkpr->{
			return new Label(mkpr.getPrasyarat().getNama());
		}).setCaption("Matakuliah");
		g.addComponentColumn(mkpr->{
			Button b = new Button();
			b.setIcon(VaadinIcons.TRASH);
			b.setStyleName(ValoTheme.BUTTON_DANGER);
			b.addClickListener(e->{
				GenericPersistence.delete(mkpr);
				updateList();
			});
			return b;
		}).setCaption("Hapus");
		vl.addComponent(g);
	}
}
