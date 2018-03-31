package org.stth.siak.ui.util;

import java.util.List;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
import org.stth.siak.jee.ui.generalview.MahasiswaProfilView;
import org.stth.siak.jee.ui.mahasiswa.IPKView;

import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GeneralPopups {
	public static void showProfilMahasiswa(Mahasiswa m){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new MahasiswaProfilView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void showIpkMahasiswa(Mahasiswa m){
		final Window win = new Window("Transkrip Nilai Mahasiswa");
		Component c = new IPKView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void showDaftarMahasiswa(List<Mahasiswa> lm ){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new DaftarMahasiswaView(lm);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void showGenericWindow(Component c, String title){
		generalWindow(c, title, 0);
	}
	private static Window generalWindow(Component c, String title, float size) {
		final Window win = new Window(title);
		win.setContent(c);
		win.setModal(true);
		if (size>0) {
			win.setHeight(size, Unit.PERCENTAGE);
			win.setWidth(size, Unit.PERCENTAGE);
		}else{
			win.setWidth("600px");
		}
		
		win.center();
		UI.getCurrent().addWindow(win);
		return win;
	}
	public static Window showGenericWindowReturn(Component c, String title){
		return generalWindow(c, title,0);
	}
	public static Window showGenericWindowReturn(Component c, String title, float size){
		return generalWindow(c, title,size);
	}
	
	public static <T extends Component & HasParent> void showWindowRefreshParentOnClose(T child, String title){
		final Window win = new Window(title);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(child);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void windowPrint(StreamResource source) {
		Window w = new Window();
		w.setModal(true);

		w.setResizable(false);
		//w.setClosable(false);
		w.setHeight(90.0f, Unit.PERCENTAGE);
		w.setWidth(90.0f, Unit.PERCENTAGE);

		BrowserFrame e = new BrowserFrame("Print", source);
		e.setSizeFull();

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(e);

		w.setContent(layout);
		UI.getCurrent().addWindow(w);
		w.focus();
		//return w;
	}
	

}
