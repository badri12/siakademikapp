package org.stth.siak.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MultiPurposeImageUploaderWindow extends Window{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uploadDirectory="/home/tmp/foto";
	public static final long FILE_MAX_SIZE = 0;
	final Embedded image = new Embedded();
	private Panel pl;
	private boolean gambarOk=false;
	private String namaFile;
	public MultiPurposeImageUploaderWindow(Object o) {
		namaFile = "file";
		if (o instanceof DosenKaryawan || o instanceof Mahasiswa){
			namaFile = o.toString();
		}
		prepareComponents();
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		uploadDirectory = k.getUploadDirectory();
	}

	private void prepareComponents() {
		image.setVisible(false);
		ImageUploader receiver = new ImageUploader(namaFile);
		// Create the upload with a caption and set receiver later
		final Upload upload = new Upload("Upload di sini", receiver);
		upload.setButtonCaption("Mulai upload");
		upload.addSucceededListener(receiver);
		upload.addStartedListener(new StartedListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uploadStarted(StartedEvent event) {
				long l = event.getContentLength();
				pl.setCaption(pl.getCaption()+" | ukuran file : "+l/1000+" kB");
				if (l > 200000){
					upload.interruptUpload();
					Notification.show("File yang anda pilih terlalu besar, "
							+ "silahkan periksa kembali");
				}
			}
		});

		// Put the components in a panel

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		pl = new Panel("Image yang terupload");
		pl.setContent(image);
		image.setSizeUndefined();
		HorizontalLayout hlaksi = new HorizontalLayout();
		Button btnSimpan = new Button("OK");
		btnSimpan.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				gambarOk = true;
				close();

			}
		});
		Button btnBatal = new Button("Batal");
		btnBatal.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();

			}
		});
		//hlaksi.setMargin(true);
		hlaksi.setSpacing(true);
		hlaksi.addComponents(btnSimpan,btnBatal);

		vl.addComponents(upload, pl, hlaksi);
		setContent(vl);
	}


	class ImageUploader implements Receiver, SucceededListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String namaFile;
		public ImageUploader(String namaFile) {
			this.namaFile = namaFile;
		}
		public File file;

		public OutputStream receiveUpload(String filename,
				String mimeType) {
			// Create upload stream
			FileOutputStream fos = null; // Stream to write to
			if (mimeType.equalsIgnoreCase("image/jpeg")||mimeType.equalsIgnoreCase("image/png")) {
				try {
					// Open the file for writing.
					file = new File(uploadDirectory + namaFile.trim() + ".jpg");
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					new Notification("Could not open file<br/>",
							e.getMessage(), Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
					
					return null;
				}
			}
			else {
				new Notification("Gunakan file image dengan format jpeg/png");
				return null;
			}
			return fos; // Return the output stream to write to
		}

		public void uploadSucceeded(SucceededEvent event) {

			// Show the uploaded file in the image viewer
			image.setVisible(true);
			image.setSource(new FileResource(file));
		}


	};

	public  byte[] getImage() throws IOException{
		if (image!=null){
			FileResource fr = (FileResource) image.getSource();
			InputStream is = new FileInputStream(fr.getSourceFile());
			byte[] bi = IOUtils.toByteArray(is);
			return bi;
		}
		return null;
	}

	public boolean isGambarOK(){
		return gambarOk;
	}

}
