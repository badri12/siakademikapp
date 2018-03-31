package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import javax.persistence.PersistenceException;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MataKuliahKonversiPersistence;
import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.MataKuliahKonversi;
import org.stth.siak.entity.MataKuliahKurikulum;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AdministrasiEditorMataKuliahKonversi extends Window {
	private MataKuliah mkul;
	private VerticalLayout vl = new VerticalLayout();
	private Grid<MataKuliahKonversi> g = new Grid<>();
	private Integer grup;
	public AdministrasiEditorMataKuliahKonversi(MataKuliah mkul) {
		this.mkul=mkul;
		button();
		createGrid();
		updateList();
		center();
		setWidth("600px");
		setContent(vl);
		setModal(true);
		setCaption("Matakuliah Konversi");
	}

	private void button() {
		String width="250px";
		HorizontalLayout hl = new HorizontalLayout();
		List<Kurikulum> lkur = GenericPersistence.findList(Kurikulum.class);
		ComboBox<Kurikulum> cbKur = new ComboBox<>();
		ComboBox<MataKuliahKurikulum> cbMatKul = new ComboBox<>();
		cbKur.setWidth(width);cbMatKul.setWidth(width);
		cbKur.setItems(lkur);
		cbKur.addValueChangeListener(e->{
			cbMatKul.setItems();
			List<MataKuliahKurikulum> lmkk = MataKuliahKurikulumPersistence.getByKurikulum(cbKur.getValue());
			cbMatKul.setItems(lmkk);
			
		});
		
		Button add= new Button();
		add.setIcon(VaadinIcons.PLUS);
		add.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(e->{
			if (cbMatKul.getValue()!=null) {
				MataKuliahKonversi mkv = new MataKuliahKonversi();
				mkv.setMataKuliah(cbMatKul.getValue().getMataKuliah());
				mkv.setGrup(grup);
				Object o = GenericPersistence.merge(mkv);
				if (o!=null) {
					if (o instanceof PersistenceException) {
						Notification.show("MataKuliah sudah ada dalam matakuliah konversi", Type.ERROR_MESSAGE);
						System.out.println("belebelele");
					}
				}else	updateList();
			}else Notification.show("Perhatian", "Silahkan pilih matakuliah", Type.ERROR_MESSAGE);
		}); 
		hl.addComponents(cbKur, cbMatKul, add);
		vl.addComponent(hl);
	}

	private void updateList() {
		List<MataKuliahKonversi> lmkk = MataKuliahKonversiPersistence.getByMatKul(mkul);
		if (lmkk.size()>0) {
			grup=lmkk.get(0).getGrup();
		}else{
			grup=MataKuliahKonversiPersistence.getGrup();
		}
		g.setItems(lmkk);
	}

	private void createGrid() {
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addComponentColumn(mkk->{
			return new Label(mkk.getMataKuliah().getKode());
		}).setCaption("KODE");
		g.addComponentColumn(mkk->{
			return new Label(mkk.getMataKuliah().getNama());
		}).setCaption("MATAKULIAH");
		g.addComponentColumn(mkk->{
			Button b = new Button();
			b.setIcon(VaadinIcons.TRASH);
			b.setStyleName(ValoTheme.BUTTON_DANGER);
			b.addClickListener(e->{
				GenericPersistence.delete(mkk);
				updateList();
			});
			return b;
		}).setCaption("AKSI");
		vl.addComponent(g);
	}
	
}
