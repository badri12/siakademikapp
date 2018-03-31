package org.siakad.siakademikapp;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("demo")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	VerticalLayout vl = new VerticalLayout();
        final GridLayout gl = new GridLayout(2,2);
        Button mhs = new Button("MAHASISWA", VaadinIcons.GROUP);
        mhs.setHeight("100px");
        mhs.setWidth("100px");
        mhs.addClickListener(e->{
        	getUI().getPage().setLocation("/mahasiswa");
        });
        
        
        Button dosen = new Button("DOSEN", VaadinIcons.USERS);
        dosen.setHeight("100px");
        dosen.setWidth("100px");
        dosen.addClickListener(e->{
        	getUI().getPage().setLocation("/dosen");
        });
        
        Button administrasi = new Button("ADMINISTRASI", VaadinIcons.USER_STAR);
        administrasi.setHeight("100px");
        administrasi.setWidth("200px");
        administrasi.addClickListener(e->{
        	getUI().getPage().setLocation("/administrasi");
        });

        gl.addComponent(mhs,0,0);
        gl.addComponent(dosen,1,0);
        gl.addComponent(administrasi,0,1,1,1);
        gl.setComponentAlignment(administrasi, Alignment.MIDDLE_CENTER);
        
        vl.setSizeFull();
        vl.addComponent(gl);
        vl.setComponentAlignment(gl, Alignment.MIDDLE_CENTER);
        setContent(vl);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
