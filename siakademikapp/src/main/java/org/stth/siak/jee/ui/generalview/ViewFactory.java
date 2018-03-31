package org.stth.siak.jee.ui.generalview;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewFactory {
	public static Component footer(){
		VerticalLayout footer = new VerticalLayout();
		//footer.addStyleName("viewheader");
		footer.setMargin(false);
		String s = "<br><strong>INTEGRATED ONLINE SYSTEM : SISTEM INFORMASI AKADEMIK</strong><br>"
				+ "http://iosys.stthamzanwadi.ac.id/siakademik <br> "
				+ "<b>powered by : Ubuntu, JavaEE, JBossAS (Wildfly), Vaadin, Hibernate, MySQL, JasperReport</b>";
		Label lblCredit = new Label(s, ContentMode.HTML);
		lblCredit.setStyleName(ValoTheme.LABEL_COLORED);
		footer.addComponent(lblCredit);
		return footer;
	}
	public static Component header(String label){
		HorizontalLayout header = new HorizontalLayout();
		//ooter.addStyleName("viewheader");
		Label titleLabel2;
		titleLabel2 = new Label(label.toUpperCase());
		titleLabel2.setSizeUndefined();
		titleLabel2.addStyleName(ValoTheme.LABEL_H3);
		titleLabel2.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel2);
		return header;
	}

}
