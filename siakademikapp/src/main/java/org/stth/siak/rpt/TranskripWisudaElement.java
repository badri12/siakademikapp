package org.stth.siak.rpt;

import java.util.ArrayList;
import java.util.List;

public class TranskripWisudaElement {
	private List<TranskripReportElement> listTransKiri = new ArrayList<>();
	private List<TranskripReportElement> listTransKanan = new ArrayList<>();
	
	public List<TranskripReportElement> getListTransKiri() {
		return listTransKiri;
	}
	public void setListTransKiri(List<TranskripReportElement> listTransKiri) {
		this.listTransKiri = listTransKiri;
	}
	public List<TranskripReportElement> getListTransKanan() {
		return listTransKanan;
	}
	public void setListTransKanan(List<TranskripReportElement> listTransKanan) {
		this.listTransKanan = listTransKanan;
	}
	
		

}
