package org.stth.siak.jee.ui.generalview;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.util.PasswordEncryptionService;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;


public class ResetPassword extends CustomComponent {
	private static final long serialVersionUID = 1L;
	private PasswordField password;
	private PasswordField repeat;
	private Button ok;
	
	public ResetPassword(Mahasiswa ma) {
		FormLayout fl = new FormLayout();
		buildComponent(fl);
		ok.addClickListener(klik->{
			if (password.getValue().equals(repeat.getValue())) {
				try {
					byte[] salt=PasswordEncryptionService.generateSalt();
					byte[] pass = PasswordEncryptionService.getEncryptedPassword(password.getValue(), salt);
					ma.setSalt(salt);
					ma.setPassword(pass);
					GenericPersistence.merge(ma);
					Notification.show("Password berhasil di reset", Notification.Type.HUMANIZED_MESSAGE);
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Notification.show("Password tidak sama", Notification.Type.HUMANIZED_MESSAGE);
			}
		});
		setCompositionRoot(fl);
	}
	public ResetPassword(DosenKaryawan d) {
		FormLayout fl = new FormLayout();
		buildComponent(fl);
		ok.addClickListener(klik->{
			if (password.getValue().equals(repeat.getValue())) {
				try {
					byte[] salt=PasswordEncryptionService.generateSalt();
					byte[] pass = PasswordEncryptionService.getEncryptedPassword(password.getValue(), salt);
					d.setSalt(salt);
					d.setPassword(pass);
					GenericPersistence.merge(d);
					Notification.show("Password berhasil di reset", Notification.Type.HUMANIZED_MESSAGE);
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Notification.show("Password tidak sama", Notification.Type.HUMANIZED_MESSAGE);
			}
		});
		setCompositionRoot(fl);
	}

	private Component buildComponent(FormLayout fl) {
		password = new PasswordField("Password Baru");
		repeat = new PasswordField("Ulangi Password");
		ok = new Button("OK");
		ok.setStyleName(ValoTheme.BUTTON_PRIMARY);
		fl.addComponents(password, repeat, ok);
		return fl;
	}
	

}
