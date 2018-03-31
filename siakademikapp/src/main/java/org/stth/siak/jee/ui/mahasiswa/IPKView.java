package org.stth.siak.jee.ui.mahasiswa;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.grid.cellrenderers.view.RowIndexRenderer;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.jarektoro.responsivelayout.ResponsiveRow.SpacingSize;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.sf.jasperreports.engine.JRException;

@SuppressWarnings("serial")
public class IPKView extends VerticalLayout implements View {
	private Grid<PesertaKuliah> g = new Grid<>();
	private Mahasiswa mhs;
	private IndeksPrestasiHelper iph;
	private IndeksPrestasiHelper iphSemester;
	private ComboBox<Semester> cbSMS = new ComboBox<>();
	private ComboBox<String> cbTA = new ComboBox<>();

	public IPKView(Mahasiswa mhs) {
		this.mhs = mhs;
		prepareView();
	}

	public IPKView(){
		mhs = VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
		prepareView();
	}

	private void prepareView() {
		iph = new IndeksPrestasiHelper(mhs);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Indeks Prestasi Mahasiswa"));
		cbTA.setItems(GeneralUtilities.getListTA(mhs));
		prepareViewKHS();
		addComponent(ViewFactory.footer());
	}
	private void prepareViewKHS(){
		Button btTranskrip = new Button("Transkrip");
		btTranskrip.setIcon(VaadinIcons.PRINT);
		btTranskrip.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btTranskrip.addClickListener(e->{
			StreamResource resource;
			try {
				resource = ReportResourceGenerator.printTranskrip(iph);
				EnhancedBrowserWindowOpener.extendOnce(btTranskrip).open((resource));
				//getUI().getPage().open(resource, "_blank", false);
			} catch (JRException ex) {
				ex.printStackTrace();
			}
		});
		Button btKHS = new Button("Kartu Hasil Studi");
		btKHS.setIcon(VaadinIcons.PRINT);
		btKHS.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btKHS.addClickListener(e->{
			if (cbSMS.getValue()!=null&&cbTA.getValue()!=null) {
				khs(btKHS);
			}

		});
		cbSMS.setPlaceholder("Semester");
		cbTA.setPlaceholder("Tahun akademik");
		cbSMS.setItems(Arrays.asList(Semester.values()));
//		hl.addComponents(cbSMS, cbTA, btKHS, btTranskrip);
//		hl.setComponentAlignment(btKHS, Alignment.BOTTOM_LEFT);
//		hl.setComponentAlignment(btTranskrip, Alignment.BOTTOM_LEFT);
		
		ResponsiveLayout rl = new ResponsiveLayout();
		ResponsiveRow row = rl.addRow();
		
		row.withComponents(cbSMS, cbTA, btKHS, btTranskrip).withSpacing(SpacingSize.SMALL, true);
		//addComponent(ViewFactory.header("IP " +sms.toString()+" T.A "+ta));
		buildContent2();
		addComponent(rl);
		addComponent(g);
		Label ipk = new Label("Indeks Prestasi Kumulatif : "+iph.getIpk());
		Label totSks = new Label("Jumlah SKS yang sudah ditempuh : "+iph.getSKStotal());
		Label nilaiD = new Label("Jumlah SKS nilai D : "+ iph.getSKSD());
		VerticalLayout vl = new VerticalLayout();
		vl.addComponents(ipk,totSks,nilaiD);
		addComponent(vl);
	}

	private void khs(Button bt) {
		StreamResource resource;
		try {
			List<PesertaKuliah> lpk = PesertaKuliahPersistence.getByMhsSmsTa(mhs, cbSMS.getValue(), cbTA.getValue());
			iphSemester = new IndeksPrestasiHelper(lpk, mhs);
			resource = ReportResourceGenerator.cetakHasilStudi(mhs, cbSMS.getValue(), cbTA.getValue(), iphSemester);
			EnhancedBrowserWindowOpener.extendOnce(bt).open((resource));
			//getUI().getPage().open(resource, "_blank", false);
		} catch (JRException ex) {
			ex.printStackTrace();
		}
	}

	private void buildContent2() {
		List<PesertaKuliah> ls = iph.getNilaiClean();
		g.setCaption("DAFTAR MATAKULIAH YANG SUDAH DITEMPUH ");
		g.setSizeFull();
		g.setItems(ls);
		RowIndexRenderer<Object, Object> row = new RowIndexRenderer<>();
		row.setOffset(1);
		g.addColumn(value -> "", row).setCaption("NO");
		g.addColumn(pk->{
			return pk.getKelasPerkuliahan().getMataKuliah().getKode();
		}).setCaption("KODE");
		g.addColumn(pk->{
			return pk.getKelasPerkuliahan().getMataKuliah().getNama();
		}).setCaption("MATAKULIAH");
		g.addColumn(pk->{
			return pk.getKelasPerkuliahan().getMataKuliah().getSks();
		}).setCaption("SKS");
		g.addColumn(PesertaKuliah::getNilai).setCaption("NILAI");
		g.addColumn(pk->{
			return pk.getKelasPerkuliahan().getSemester();
		}).setCaption("SEMESTER");
		g.addColumn(pk->{
			return pk.getKelasPerkuliahan().getTahunAjaran();
		}).setCaption("T.A");

	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
