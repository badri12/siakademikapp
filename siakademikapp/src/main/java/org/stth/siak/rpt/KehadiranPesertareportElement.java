package org.stth.siak.rpt;


public class KehadiranPesertareportElement {
	private boolean isHadir;
	private String kehadiran="";	
	
	public boolean isHadir() {
		return isHadir;
	}

	public void setisHadir(boolean isHadir) {
		this.isHadir = isHadir;
		if (isHadir) {
			setKehadiran("\u2713");
		}else setKehadiran("\u2715");
	}

	public String getKehadiran() {
		return kehadiran;
	}

	public void setKehadiran(String kehadiran) {
		this.kehadiran = kehadiran;
	}

	

}
