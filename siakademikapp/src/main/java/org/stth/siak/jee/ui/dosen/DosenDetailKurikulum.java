package org.stth.siak.jee.ui.dosen;


import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.KurikulumView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenDetailKurikulum extends VerticalLayout implements View{
	
	private static final long serialVersionUID = 1653775031486924211L;
	private Kurikulum kr;
	private ProgramStudi prodi;
	private FormLayout fl;
	private VerticalLayout content = new VerticalLayout();
	private ComboBox<ProgramStudi> cbProdi = new ComboBox<>("Pilih prodi");
	private ComboBox<Kurikulum> cbKur = new ComboBox<>("Pilih Kurikulum");

	public DosenDetailKurikulum() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		//dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Detail Kurikulum"));
		addComponent(siapkanComboProdi());
		cbProdi.setWidth("300px");
		cbKur.setWidth("300px");
		addComponent(content);
		//addComponent(new KurikulumView(kr));
		addComponent(ViewFactory.footer());

	}

	

	private Component siapkanComboProdi() {
		fl = new FormLayout();
		cbProdi.setItems(GenericPersistence.findList(ProgramStudi.class)); 
		cbProdi.setEmptySelectionAllowed(false);
		cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(e->{
			prodi =e.getValue();
			siapkanComboKurikulum();
		});
		fl.addComponent(cbProdi);
		fl.addComponent(cbKur);
		return fl;
	}

	protected void siapkanComboKurikulum() {
		cbKur.setItems(KurikulumPersistence.getListByProdi(prodi));;
		cbKur.setEmptySelectionAllowed(false);
		cbKur.setTextInputAllowed(false);
		cbKur.addValueChangeListener(e->{
			kr = e.getValue();
			content.removeAllComponents();
			KurikulumView kv = new KurikulumView(kr);
			content.addComponent(kv);
		});
			
	}



	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	

		

}
