package org.stth.siak.jee.ui.mahasiswa;

import java.time.LocalDateTime;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.util.UserAuthenticationService;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    private TextField username = new TextField("NIM");
	private PasswordField password = new PasswordField("Password");

	public LoginView() {
        setSizeFull();
        //username.setValue("02.03.15.0008");
        //password.setValue("0008");
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(
                "Selamat datang pada aplikasi "
                + "data akademik untuk mahasiswa");
        notification
                .setDescription("<span>Lihat profil anda, "
                		+ "perkembangan prestasi akademik, "
                		+ "dan berbagai fitur lain sebagai mahasiswa di Fakultas Teknik Universitas Hamzanwadi.</span> <span>Masukkan NIM dan password anda"
                		+ " kemudian klik tombol <b>Masuk</b> untuk melanjutkan.</span>"
                		+ "Hubungi bagian akademik bila anda belum mendapatkan password");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox("Remember me", true));
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Masuk");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                authenticate();
            }
        });
        return fields;
    }

    protected void authenticate() {
		String nim = username.getValue();
		String password = this.password.getValue();
		
		Mahasiswa d = UserAuthenticationService.getValidMahasiswa(nim, password);
		if (d!=null) {
			d.setLastSuccessfulLogin(LocalDateTime.now());
		    GenericPersistence.merge(d);
			//List<Konfigurasi> konfs = GenericPersistence.findList(Konfigurasi.class);
			getSession().setAttribute("user", d.getNama());
			getSession().setAttribute("curUser", d);
			//getSession().setAttribute("konfigurasi", konfs);
			VaadinSession.getCurrent().setAttribute(Mahasiswa.class, d);
			MahasiswaUI ui = (MahasiswaUI) getUI();//
			ui.updateContent();
		} else {
			//this.password.setValue(null);
			this.password.focus();
			Notification notification = new Notification(
	                "Login / Password salah! ");
	        notification
	                .setDescription("Login dan Password yang anda berikan tidak sesuai. "
	                		+ "Jika anda merasa terjadi kesalahan, "
	                		+ "minta bantuan bagian akademik untuk mereset password anda");
	        notification.setHtmlContentAllowed(true);
	        notification.setStyleName("tray red closable login-help");
	        notification.setPosition(Position.MIDDLE_CENTER);
	        notification.show(Page.getCurrent());
		}
	}

	private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Selamat Datang");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Dosen STTH");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        //labels.addComponent(title);
        return labels;
    }

 

}
