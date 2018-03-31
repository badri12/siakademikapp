package org.stth.siak.jee.ui.generalview;

import java.util.ArrayList;
import java.util.List;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.administrasi.AdministrasiEditorDataDosen;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarDosenView extends VerticalLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private List<DosenKaryawan> daftarDosen;
	public static final int NAMA = 1;
	public static final int PRODI = 2;
	public static final int TGL_LAHIR = 4;
	public static final int TEMPAT_LAHIR = 8;
	public static final int ALAMAT = 16;
	public static final int NIDN = 32;
	public static final int TELP = 64;
	public static final int EMAIL = 128;
	public static final int ALIAS = 256;
	
	public static final int EDIT = 1;
	public static final int HAPUS = 2;

	private int visibleColumnsBit = 7;
	private int allowedActionsBit = 3;
	private int[] visColumnsArrays;
	private String[] columnNames;
	private List<String> visibleColumnNames = new ArrayList<String>();

	public DaftarDosenView(List<DosenKaryawan> kr){
		this.daftarDosen = kr;
		prepareContent();
	}
	public DaftarDosenView(List<DosenKaryawan> kr,int visibleColumns, int allowedActions){
		this.daftarDosen = kr;
		this.visibleColumnsBit = visibleColumns;
		this.allowedActionsBit = allowedActions;
		prepareContent();
	}

	private void prepareContent() {
		visColumnsArrays = new int[] {NAMA,PRODI,TGL_LAHIR,TEMPAT_LAHIR,ALAMAT,NIDN,TELP,EMAIL,ALIAS};
		columnNames = new String[] {"nama","prodi","tanggalLahir","tempatLahir","alamatRumah","nidn","nomorTelepon","email","alias","aksi"};
		setSizeFull();
		addComponent(getTable());
	}
	
	public Component getTable(){
		Grid<DosenKaryawan> g = new Grid<>("Ditemukan "+ daftarDosen.size()+ " dosen", daftarDosen);
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addComponentColumn(d->{
			HorizontalLayout hl = new HorizontalLayout();
			Button btEdit = new Button("");
			btEdit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btEdit.setIcon(VaadinIcons.EDIT);
			btEdit.addClickListener(e->{
				AdministrasiEditorDataDosen ae = new AdministrasiEditorDataDosen(d);
				GeneralPopups.showGenericWindow(ae, "Edit Data Dosen");
			});
			
			Button btResetPassword = new Button("Reset Password");
			btResetPassword.setStyleName(ValoTheme.BUTTON_DANGER);
			btResetPassword.setIcon(VaadinIcons.REFRESH);
			btResetPassword.addClickListener(e->{
				ResetPassword rp = new ResetPassword(d);
				GeneralPopups.showGenericWindow(rp, "Reset Password");
			});
			for (int j = 0; j < visColumnsArrays.length; j++) {
				int bitVal = GeneralUtilities.getBit(allowedActionsBit, j);
				if (bitVal==1 && j==0){
					hl.addComponents(btEdit, btResetPassword);
				}
				
			}
			return hl;
		}).setCaption("AKSI").setId("aksi").setHidden(true);
		g.addColumn(DosenKaryawan::getAlias).setCaption("ALIAS").setId("alias").setHidden(true);
		g.addColumn(DosenKaryawan::getNama).setCaption("NAMA").setId("nama").setHidden(true);
		g.addColumn(DosenKaryawan::getTanggalLahir).setCaption("TGL LAHIR").setId("tanggalLahir").setHidden(true);
		g.addColumn(DosenKaryawan::getTempatLahir).setCaption("TEMPAT LAHIR").setId("tempatLahir").setHidden(true);
		g.addColumn(DosenKaryawan::getAlamatRumah).setCaption("ALAMAT").setId("alamatRumah").setHidden(true);
		g.addColumn(DosenKaryawan::getProdi).setCaption("PROGRAM STUDI").setId("prodi").setHidden(true);
		g.addColumn(DosenKaryawan::getNomorTelepon).setCaption("TELP").setId("nomorTelepon").setHidden(true);
		g.addColumn(DosenKaryawan::getNidn).setCaption("NIDN").setId("nidn").setHidden(true);
		
		for (int j = 0; j < visColumnsArrays.length; j++) {
			int bitVal = GeneralUtilities.getBit(visibleColumnsBit, j);
			if (bitVal==1){
				visibleColumnNames.add(columnNames[j]);
			}
		}
		if(allowedActionsBit > 1){
			visibleColumnNames.add("aksi");
		}
		
		for(String s : visibleColumnNames){
			g.getColumn(s).setHidden(false);
		}
		return g;
	}
	

}
