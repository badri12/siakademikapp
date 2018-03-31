package org.stth.siak.jee.ui.dosen;

import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.util.GeneralUtilities;
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

    private TextField username = new TextField("Alias");
	private PasswordField password = new PasswordField("Password");

	public LoginView() {
		//username.setValue("MHZ");
		//password.setValue("mhz");
        setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(
                "Selamat datang pada aplikasi data akademik untuk dosen");
        notification
                .setDescription("<span>Anda dapat melihat data kelas yang anda ampu, status bimbingan akademik, dan berbagai fitur lain sebagai dosen di Fakultas Teknik Universitas Hamzanwadi.</span> <span>Masukkan email dan password anda kemudian klik tombol <b>Masuk</b> untuk melanjutkan.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
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
        //loginPanel.addComponent(new CheckBox("Remember me", true));
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
		String alias = username.getValue();
		String password = this.password.getValue();
		alias = alias.toUpperCase();
		DosenKaryawan d = UserAuthenticationService.getValidDosen(alias, password);
	
		if (d!=null) {
			d.setLastSuccessfulLogin(GeneralUtilities.getCurrentDBTime());
			//GenericPersistence.merge(d);
			getSession().setAttribute("user", d.getNama());
			getSession().setAttribute("curUser", d);
			VaadinSession.getCurrent().setAttribute(DosenKaryawan.class, d);
			DosenUI ui = (DosenUI) getUI();//
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
	        notification.setDelayMsec(2000);
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
