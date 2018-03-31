package org.stth.siak.ui.util;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;


public class GeneralNotificartion {
	public static Notification warning(String message){
		Notification n = new Notification(message);
		n.setDelayMsec(2500);
		n.setStyleName(ValoTheme.NOTIFICATION_WARNING);
		n.setPosition(Position.MIDDLE_CENTER);
		return n;
	}
	public static Notification error(String message){
		Notification n = new Notification(message);
		n.setDelayMsec(2500);
		n.setStyleName(ValoTheme.NOTIFICATION_ERROR );
		n.setPosition(Position.MIDDLE_CENTER);
		return n;
	}
	public static Notification sukses(String message){
		Notification n = new Notification(message);
		n.setDelayMsec(2500);
		n.setStyleName("dark");
		n.setPosition(Position.MIDDLE_CENTER);
		return n;
	}
}
