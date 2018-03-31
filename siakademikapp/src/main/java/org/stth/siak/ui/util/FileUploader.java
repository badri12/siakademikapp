package org.stth.siak.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.stth.jee.persistence.KonfigurasiPersistence;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class FileUploader extends Window implements Receiver, SucceededListener{
	private static final long serialVersionUID = -4804984301371728963L;
	private Map<String, String> typeFile = new HashMap<>();
	private BrowserFrame embed;
	private File file;
	private String uploadDirectory="";
	private Panel p;
	private Button ok;
	private byte[] byt;
	private int filesize;
	private String extensi;
	public FileUploader(byte[] byt, String[] typeFile, int filesize, String extensi) {
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		uploadDirectory = k.getUploadDirectory();
		this.byt=byt;
			
		setCaption("Document File");
		//typeFile="application/pdf";
		for (String string : typeFile) {
			this.typeFile.put(string, string);
		}
		this.extensi=extensi;
		this.filesize=filesize;
		createComponent();
		setFile();
		center();
		setModal(true);
	}

	private void setFile() {
		if (byt!=null) {
			file = new File(uploadDirectory+"berkasTransfer."+extensi);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				fos.write(byt);
				setContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private void createComponent() {
		p= new Panel();
		ok = new Button("OK");
		ok.setEnabled(false);
		Upload upload = new Upload("Upload file", this);
		upload.addSucceededListener(this);
		upload.setButtonCaption("Mulai upload");
		upload.setIcon(VaadinIcons.UPLOAD);
		upload.addStartedListener(e->{
			long l = e.getContentLength();			
			if (l > filesize){
				GeneralNotificartion.warning("File document maksimal "+filesize/1000+" kB").show(Page.getCurrent());
				upload.interruptUpload();
			}else{
				if (typeFile.containsKey(e.getMIMEType())) {
					p.setCaption("File terupload | ukuran file : "+l/1000+" kB");
				}else{
					System.out.println(e.getMIMEType());
					GeneralNotificartion.warning("Gunakan file dengan format " 
							+extensi).show(Page.getCurrent());
					upload.interruptUpload();
				}
			}
		});
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		//embed.setSizeFull();
		HorizontalLayout hl = new HorizontalLayout();

		ok.addClickListener(klik->{
			getByte();
			setData(true);
			close();
		});

		Button cancel = new Button("Cancel");
		cancel.addClickListener(e->{
			hapusFile();
			close();
		});
		//p.setContent(embed);
		hl.setSpacing(true);
		hl.addComponents(upload, ok, cancel);	
		hl.setComponentAlignment(ok, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(cancel, Alignment.BOTTOM_LEFT);
		vl.addComponent(hl);
		vl.addComponent(p);
		setContent(vl);
	}

	public void hapusFile() {
		if (file!=null) {
			if (file.exists()) {
				file.delete();
			}
		}
	}
	public File getFile(){
		return file;
	}
	public byte[] getByte() {
		if (file!=null) {
			try {
				InputStream is = new FileInputStream(file);
				byte[] bi = IOUtils.toByteArray(is);
				setData(bi);
				return bi;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	private void setContent(){
		if (extensi.toLowerCase().equals("xlsx")) {
			
		}else{
			embed =new BrowserFrame();
			embed.setSource(new FileResource(file));
			embed.setSizeFull();
			p.setContent(embed);
			p.setHeight("600px");
			setHeight(90.0f, Unit.PERCENTAGE);
			setWidth(90.0f, Unit.PERCENTAGE);
			center();
		}
		
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		setContent();
		ok.setEnabled(true);
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		try {
			hapusFile();
			file= new File(uploadDirectory+filename);
			fos=new FileOutputStream(file);
		} catch (IOException e) {
			System.out.println(e.getMessage()+"aaaaaaa");
			e.printStackTrace();
			return null;
		}
		return fos;
	}


}
