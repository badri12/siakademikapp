package org.stth.siak.jee.ui.mahasiswa;


import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.KurikulumView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MahasiswaDetailKurikulum extends VerticalLayout implements View{
	private static final long serialVersionUID = 1653775031486924211L;
	private Kurikulum kr;
	private ProgramStudi prodi;
	//private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private FormLayout fl;
	private VerticalLayout content = new VerticalLayout();
	//private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private ComboBox<Kurikulum> cbKur = new ComboBox<>("Pilih Kurikulum");
	private Mahasiswa mhs;

	public MahasiswaDetailKurikulum() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		mhs = VaadinSession.getCurrent().getAttribute(
				Mahasiswa.class);
		prodi = mhs.getProdi();
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Detail Kurikulum"));
		addComponent(siapkanComboKurikulum());
		content.setMargin(false);
		addComponent(content);
		addComponent(ViewFactory.footer());

	}

	


	protected Component siapkanComboKurikulum() {
		fl = new FormLayout();
		fl.addComponent(cbKur);
		cbKur.setItems(KurikulumPersistence.getListByProdi(prodi));;
		cbKur.setTextInputAllowed(false);
		cbKur.setWidth("300px");
		cbKur.addValueChangeListener(e->{
			kr = e.getValue();
			content.removeAllComponents();
			KurikulumView kv = new KurikulumView(kr);
			content.addComponent(kv);
		});
		return fl;
	}



	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	

		

}
