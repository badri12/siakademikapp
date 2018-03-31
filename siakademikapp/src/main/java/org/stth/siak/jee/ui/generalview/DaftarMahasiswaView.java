package org.stth.siak.jee.ui.generalview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.jee.ui.administrasi.AdministrasiEditorDataMahasiswa;
import org.stth.siak.jee.ui.administrasi.AdministrasiPopupTanggalLulus;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;

import net.sf.jasperreports.engine.JRException;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarMahasiswaView extends VerticalLayout{

	private static final long serialVersionUID = 4001325208244016531L;
	private List<Mahasiswa> daftarMahasiswa;
	private Grid<Mahasiswa> g;
	private boolean showCetakTranskrip = false;
	public static final int NIM = 1;
	public static final int NAMA = 2;
	public static final int PRODI = 4;
	public static final int TGL_LAHIR = 8;
	public static final int TEMPAT_LAHIR = 16;
	public static final int ALAMAT = 32;
	public static final int DOSEN_PA = 64;

	public static final int LIHAT_PROFIL = 1;
	public static final int LIHAT_IPK = 2;
	public static final int CETAK_TRANSKRIP = 4;
	public static final int EDIT = 8;
	public static final int HAPUS = 16;

	private int visibleColumnsBit = 7;
	private int allowedActionsBit = 3;
	private int[] visColumnsArrays;
	private String[] columnNames;
	private List<String> visibleColumnNames = new ArrayList<String>();
	private List<?> lac= (List<?>) VaadinSession.getCurrent().getAttribute("admrights");
	private List<UserAccessRightsAdministrasi> lacl = new ArrayList<>();
	
	public DaftarMahasiswaView(List<Mahasiswa> kr){
		this.daftarMahasiswa = kr;
		prepareContent();
	}
	public DaftarMahasiswaView(List<Mahasiswa> kr,int visibleColumns, int allowedActions){
		this.daftarMahasiswa = kr;
		this.visibleColumnsBit = visibleColumns;
		this.allowedActionsBit = allowedActions;
		prepareContent();
	}
	public DaftarMahasiswaView(List<Mahasiswa> kr,int visibleColumns, int allowedActions, boolean transkrip){
		showCetakTranskrip=transkrip;
		this.daftarMahasiswa = kr;
		this.visibleColumnsBit = visibleColumns;
		this.allowedActionsBit = allowedActions;
		prepareContent();
	}

	private void prepareContent() {
		setMargin(false);
		setSizeFull();
		for (Object o : lac) {
			lacl.add((UserAccessRightsAdministrasi) o);
		}
		visColumnsArrays = new int[] {NIM,NAMA,PRODI,TGL_LAHIR,TEMPAT_LAHIR,ALAMAT,DOSEN_PA};
		columnNames = new String[] {"npm","nama","prodi","tanggalLahir","tempatLahir","alamat","pembimbingAkademik","aksi"};
		addComponent(getTable());
		if (showCetakTranskrip) {
			addComponent(createButton());
		}
	}

	private Component createButton() {
		HorizontalLayout hlb = new HorizontalLayout();
//		EnhancedBrowserWindowOpener transkrip = new EnhancedBrowserWindowOpener()
//			    .popupBlockerWorkaround(true);
		Button print = new Button("Transkrip");
		print.setDescription("Cetak transkrip sementara");
		print.setIcon(VaadinIcons.PRINT);
		print.setStyleName(ValoTheme.BUTTON_PRIMARY);
		print.addClickListener(event->{
			if(g.getSelectedItems().size()>0){
				cetakTranskrip(g.getSelectedItems(), print);
			}else Notification.show("silahkan pilih mahasiswa");
		});
//		transkrip.extend(print);
		Button printa = new Button("Transkrip Akademik");
		printa.setDescription("Cetak transkrip akademik");
		printa.setIcon(VaadinIcons.PRINT);
		printa.setStyleName(ValoTheme.BUTTON_PRIMARY);
		printa.addClickListener(event->{
			if(g.getSelectedItems().size()>0){
				cetakTranskripAkademik(g.getSelectedItems());
			}else Notification.show("silahkan pilih mahasiswa");
		});
		hlb.addComponent(print);
		if (ACLAdministrasiEnum.isEligibleTo(lacl, ACLAdministrasiEnum.TRANSKRIP_AKADEMIK)) {
			hlb.addComponent(printa);
		}
		
		Button b = new Button("Pilihkan P.A");
		b.setIcon(VaadinIcons.EXCHANGE);
		b.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		b.addClickListener(e->{
			if(g.getSelectedItems().size()>0){
				PilihDosen pd = new PilihDosen();
				pd.addCloseListener(close->{
					if (pd.getData()!=null) {
						for (Mahasiswa m : g.getSelectedItems()) {
							m.setPembimbingAkademik((DosenKaryawan) pd.getData());
							GenericPersistence.merge(m);
						}
					}
				});
				getUI().addWindow(pd);
			}else Notification.show("silahkan pilih mahasiswa");
		});
		hlb.addComponent(b);
		return hlb;
	}
	public Component getTable(){
		g = new Grid<>();
		g.setItems(daftarMahasiswa);
		g.setCaption("DAFTAR MAHASISWA ("+daftarMahasiswa.size()+")");
		g.setSizeFull();
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(Mahasiswa::getNpm).setCaption("NIM").setId("npm").setHidden(true);
		//		if(VaadinSession.getCurrent().getAttribute("linkmahasiswa")!=null){
		//			g.addColumn(mhs->{
		//				return mhs.getNama();
		//			}).setCaption("NAMA").setId("nama").setHidden(true);
		//			
		//		}else
		g.setSelectionMode(SelectionMode.MULTI);
		g.setRowHeight(30);
		g.addColumn(Mahasiswa::getNama).setCaption("NAMA").setId("nama").setHidden(true);
		g.addColumn(Mahasiswa::getProdi).setCaption("PROGRAM STUDI").setId("prodi").setHidden(true);
		g.addColumn(Mahasiswa::getTempatLahir).setCaption("TEMPAT LAHIR").setId("tempatLahir").setHidden(true);
		g.addColumn(Mahasiswa::getTanggalLahir).setCaption("TGL LAHIR").setId("tanggalLahir").setHidden(true);
		g.addColumn(Mahasiswa::getAlamat).setCaption("ALAMAT").setId("alamat").setHidden(true);
		g.addColumn(Mahasiswa::getPembimbingAkademik).setCaption("DOSEN P.A").setId("pembimbingAkademik").setHidden(true);
		g.addComponentColumn(mhs->{
			HorizontalLayout hl = new HorizontalLayout();
			Button btLihatProfil = new Button("Profil");
			btLihatProfil.setIcon(VaadinIcons.EYE);
			btLihatProfil.setStyleName(ValoTheme.BUTTON_PRIMARY);
			//btLihatProfil.addStyleName(ValoTheme.BUTTON_SMALL);
			btLihatProfil.addClickListener(e->{
				GeneralPopups.showProfilMahasiswa(mhs);
			});
			Button btIPK = new Button("Indeks Prestasi");
			btIPK.setStyleName(ValoTheme.BUTTON_PRIMARY);
			//btIPK.addStyleName(ValoTheme.BUTTON_SMALL);
			btIPK.addClickListener(e->{
				GeneralPopups.showIpkMahasiswa(mhs);
			});
			Button btTranskrip = new Button("Transkrip");
			btTranskrip.setStyleName(ValoTheme.BUTTON_PRIMARY);
			//btTranskrip.addStyleName(ValoTheme.BUTTON_SMALL);
			btTranskrip.setIcon(VaadinIcons.PRINT);
			btTranskrip.addClickListener(e->{
				//				if (mhs.getStatus().equals(StatusMahasiswa.LULUS)) {
				//					ConfirmDialog.show(getUI(), "", "Print Transkrip?", "Sementara", "Akademik", cf->{
				//						if (cf.isConfirmed()) {
				//							cetakTranskrip(mhs);
				//						}else {
				//							cetakTranskripAkademik(mhs);
				//						}		
				//					});
				//				}else{
				//					cetakTranskrip(mhs);
				//				}
			});
			Button btEdit = new Button();
			btEdit.setIcon(VaadinIcons.EDIT);
			btEdit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			//btEdit.addStyleName(ValoTheme.BUTTON_SMALL);
			btEdit.addClickListener(e->{
				AdministrasiEditorDataMahasiswa ae = new AdministrasiEditorDataMahasiswa(mhs);
				GeneralPopups.showGenericWindow(ae, "Edit Data Mahasiswa");
			});

			Button btResetPassword = new Button("Reset Password");
			btResetPassword.setStyleName(ValoTheme.BUTTON_DANGER);
			//btResetPassword.addStyleName(ValoTheme.BUTTON_SMALL);
			btResetPassword.setIcon(VaadinIcons.REFRESH);
			btResetPassword.addClickListener(e->{
				ResetPassword rp = new ResetPassword(mhs);
				GeneralPopups.showGenericWindow(rp, "Reset Password");
			});
			for (int j = 0; j < visColumnsArrays.length; j++) {
				int bitVal = GeneralUtilities.getBit(allowedActionsBit, j);
				if (bitVal==1 && j==0){
					hl.addComponent(btLihatProfil);
				}
				if (bitVal==1 && j==1){
					hl.addComponent(btIPK);
				}
				//				if (bitVal==1 && j==2){
				//					hl.addComponent(btTranskrip);
				//				}
				if (bitVal==1 && j==3){
					hl.addComponents(btEdit, btResetPassword);
				}
			}
			hl.addComponent(btLihatProfil);
			return hl;
		}).setCaption("AKSI").setId("aksi").setHidden(true);

		for (int j = 0; j < visColumnsArrays.length; j++) {
			int bitVal = GeneralUtilities.getBit(visibleColumnsBit, j);
			if (bitVal==1){
				visibleColumnNames.add(columnNames[j]);
			}
		}
		if(allowedActionsBit > 1){
			visibleColumnNames.add("aksi");
		}
		//table.setVisibleColumns(visibleColumnNames.toArray());
		for (String string :visibleColumnNames) {
			g.getColumn(string).setHidden(false);
		}
		return g;
	}
	private void cetakTranskripAkademik(Set<Mahasiswa> o) {
		Window window = new AdministrasiPopupTanggalLulus(o);
		getUI().addWindow(window);
	}

	private StreamResource cetakTranskrip(Set<Mahasiswa> o, Button bt) {
		StreamResource source=null;
		try {
			source = ReportResourceGenerator.cetakTranskripMahasiswa(o);
			EnhancedBrowserWindowOpener.extendOnce(bt).open(source);
//			ResourceReference ref = new ResourceReference(source, this, "transkrip"+source.hashCode());
//		    this.setResource("transkrip"+source.hashCode(), source); // now it's available for download
//		    getUI().getPage().open(ref.getURL(), "_blank", false);
		} catch (JRException ex) {
			ex.printStackTrace();
		}
		return source;
	}
	public void showTranskripButton(){
		showCetakTranskrip = true;
	}
	public Collection<Mahasiswa> getMhs(){
		return g.getSelectedItems();
	}
}
