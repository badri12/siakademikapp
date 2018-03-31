package org.stth.siak.jee.ui.administrasi;

import java.util.Set;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.util.GeneralUtilities;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;

public class AdministrasiPopupTanggalLulus extends Window {
	private static final long serialVersionUID = 2319062893834904563L;
	private String[] l ;
	private DateField tanggalLulus = new DateField("Tanggal Lulus");
	private DateField tanggalTranskrip = new DateField("Tanggal Transkrip");

	public AdministrasiPopupTanggalLulus(Set<Mahasiswa> o) {
		FormLayout fl= new FormLayout();
		Button ok = new Button("OK");
		ok.setIcon(VaadinIcons.CHECK);
		ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(e-> {
			String lulus =null;
			String trans =null;
			if (tanggalLulus.getValue()!=null && tanggalTranskrip.getValue()!=null) {
				lulus=GeneralUtilities.getLongFormattedDate2(tanggalLulus.getValue());
				trans=GeneralUtilities.getLongFormattedDate2(tanggalTranskrip.getValue());
				l = new String[2];
				l[0]=(lulus);
				l[1]=(trans);
				if (l!=null) {
					StreamResource source;
					try {
						source = ReportResourceGenerator.cetakTranskripWisudaMahasiswa(o,l);
						EnhancedBrowserWindowOpener.extendOnce(ok).open((source));
//						ResourceReference ref = new ResourceReference(source, this, "transkripakademik"+source.hashCode());
//					    this.setResource("transkripakademik"+source.hashCode(), source); // now it's available for download
//					    getUI().getPage().open(ref.getURL(), "_blank", false);
					} catch (JRException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		});
		fl.addComponents(tanggalLulus, tanggalTranskrip, ok);
		fl.setMargin(true);
		setWidth("480px");
		setCaption("Tanggal Lulus dan Tanggal Transkrip");
		center();
		setContent(fl);

	}
}
