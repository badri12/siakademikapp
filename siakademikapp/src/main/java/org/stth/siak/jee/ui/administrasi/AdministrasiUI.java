package org.stth.siak.jee.ui.administrasi;


import static com.github.appreciated.app.layout.builder.AppLayoutConfiguration.Position.HEADER;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.util.PasswordEncryptionService;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.generalview.ProfilUmumDosenKaryawan;
import org.stth.siak.ui.util.BehaviourSelector;

import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayout;
import com.github.appreciated.app.layout.builder.NavigatorAppLayoutBuilder;
import com.github.appreciated.app.layout.builder.design.AppBarDesign;
import com.github.appreciated.app.layout.builder.entities.DefaultBadgeHolder;
import com.github.appreciated.app.layout.interceptor.DefaultViewNameInterceptor;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.MenuBar.MenuItem;

@Title("IOSYS:SIAKAD-Administrasi")
@SuppressWarnings("serial")
@PushStateNavigation
@Viewport("initial-scale=1, maximum-scale=1")
@Theme("demo")
@Push()
public class AdministrasiUI extends UI {
	DefaultBadgeHolder badge = new DefaultBadgeHolder();
	private VerticalLayout holder;
	private DosenKaryawan user;
	private Behaviour var;

	@Override
	protected void init(VaadinRequest request) {
		//getSession().setConverterFactory(new MyConverterFactory());
		setLocale(Locale.UK);
		updateContent();
	}
	public void updateContent() {
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		if (user == null) {
			// Authenticated user
			setContent(new LoginView());
		}else{
			holder = new VerticalLayout();
			holder.setMargin(false);
			setDrawerVariant(Behaviour.LEFT_RESPONSIVE);
			setContent(holder);
			holder.setSizeFull();
		}	
	}

	private void setDrawerVariant(Behaviour variant) {
		var = variant;
		ThemeResource tr = new ThemeResource("images/logo-hamzanwadi.png");
		Image image = new Image("",tr);
		image.setWidth("150px");
		image.setHeight("150px");
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(image);
		hl.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		hl.setWidth("200px");
		holder.removeAllComponents();
		NavigatorAppLayoutBuilder drawer = AppLayout.getDefaultBuilder(variant)
				.withTitle("UNIVERSITAS HAMZANWADI")
				.addToAppBar(buildUserMenu())
				.withViewNameInterceptor(new DefaultViewNameInterceptor())
				.withDefaultNavigationView(ProfilUmumDosenKaryawan.class)
				.withDesign(AppBarDesign.MATERIAL)
				//.withNavigatorConsumer(navigator -> {/* Do someting with it */})
				.add(hl, HEADER)
				.add("PROFIL", VaadinIcons.USER, badge, new ProfilUmumDosenKaryawan())
				//.add(buildUserMenu(), FOOTER)
				//.addClickable("", VaadinIcons.COG, clickEvent -> buildUserMenu(), FOOTER)
				;
		
		new AdministrasiMenuComponent(drawer);
		AppLayoutComponent alc = drawer.build();
		holder.addComponent(alc);
		getNavigator().setErrorView(ProfilUmumDosenKaryawan.class);
		if (getNavigator().getState()!= null) {
			getNavigator().navigateTo(getNavigator().getState());
		}

	}

	private Component buildUserMenu() {
		MenuBar settings = new MenuBar();
		settings.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		settings.addStyleName("white");
		
		MenuItem settingsItem = settings.addItem(user.getAlias(), null);
		settingsItem.setStyleName("white");
		settingsItem.setIcon(VaadinIcons.COG);
		settingsItem.addItem("Ganti Password", c->{
			spawnPasswordResetDialog();
		});

		settingsItem.addSeparator();
		settingsItem.addItem("Sign Out", e->{
			VaadinSession.getCurrent().setAttribute(DosenKaryawan.class, null);
			VaadinSession.getCurrent().setAttribute("admrights", null);
			updateContent();
		});
		settingsItem.addItem("Mode", e->{
			 UI.getCurrent().addWindow(new BehaviourSelector(var, variant1 -> setDrawerVariant(variant1)));
		});
		return settings;
	}
	private void spawnPasswordResetDialog() {
		final Window gantiPassword = new Window("Ganti password");
		FormLayout fly = new FormLayout();
		final PasswordField tpass1 = new PasswordField("Password baru");
		final PasswordField tpass2 = new PasswordField("Ketik ulang password");
		Button b = new Button("Simpan", c->{
			if (tpass1.getValue().equals(tpass2.getValue())){
				try {
					byte[] salt = PasswordEncryptionService.generateSalt();
					user.setSalt(salt);
					user.setPassword(PasswordEncryptionService.getEncryptedPassword(tpass1.getValue(), salt));
					GenericPersistence.merge(user);
					Notification.show("Penggantian password berhasil",Notification.Type.HUMANIZED_MESSAGE);
					gantiPassword.close();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
				}
			} else {
				Notification.show("Password tidak sesuai",Notification.Type.ERROR_MESSAGE);
			}
		});
		gantiPassword.setModal(true);
		gantiPassword.setWidth("375px");
		fly.setMargin(true);
		gantiPassword.setContent(fly);
		fly.addComponents(tpass1,tpass2,b);
		gantiPassword.center();
		UI.getCurrent().addWindow(gantiPassword);
	}
	
	@WebServlet(value = "/administrasi/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = AdministrasiUI.class)
	public static class Servlet extends VaadinServlet {
	}

}
