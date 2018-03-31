package org.stth.siak.util;

public class CaseUtil {
	public static String upperfirsLetter(String s){
		String judul="";
		String[] arrayS = s.split(" ");
		String[] kataSambung = {"dan", "di", "and", "for", "in", "the"};
		for (String string : arrayS) {
			string = string.trim();
			if (string.length()>2) {
				string = string.substring(0, 1).toUpperCase()+string.substring(1).toLowerCase();
			}
			
			if (string.startsWith("(")) {
				string=string.toUpperCase();
			}
			for(String kata : kataSambung){
				if(string.toLowerCase().equals(kata)){
					string=kata;
				}
			}
			judul += string +" ";
		}
		judul = judul.substring(0, judul.length()-1);
		return judul;
	}

}
