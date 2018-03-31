package org.stth.siak.jee.ui.mahasiswa;

import java.util.List;

import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class KelasPerkuliahanView extends VerticalLayout implements View {


    private VerticalLayout vl;
	private Mahasiswa mhs;
	//private BeanContainer<Integer, PesertaKuliah> beansPK = new BeanContainer<Integer, PesertaKuliah>(PesertaKuliah.class);
	private List<KelasPerkuliahan> lkp;
	private Semester semester;
	private String ta;

    public KelasPerkuliahanView() {
    	mhs = VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getCurrentSemester();
		ta = k.getCurrentTa();
    	setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("Kelas Perkuliahan Semester "+semester+ " T.A "+ta));
        Component content = buildContent2();
        addComponent(content);
        addComponent(ViewFactory.footer());

    }
    private Component buildContent2() {
        vl = new VerticalLayout();
        lkp = PesertaKuliahPersistence.getKelasPerkuliahanMahasiswaSemester(mhs, semester, ta);
		vl.addComponent(new DaftarKelasPerkuliahanView(lkp));
        return vl;
    }

    
        @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
