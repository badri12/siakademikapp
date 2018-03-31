package org.stth.siak.rpt;

import java.util.List;
import java.util.Map;

public class ReportRawMaterials {
	private String reportFile; 
	private Map<String,Object>  reportParameters;
	private List<?> objects;
	private String title;
	
	public ReportRawMaterials(String reportFile,
			Map<String, Object> reportParameters, List<?> objects, String title) {
		super();
		this.reportFile = reportFile;
		this.reportParameters = reportParameters;
		this.objects = objects;
		this.setTitle(title);
	}
	public String getReportFile() {
		return reportFile;
	}
	public String getCompiledReportFile() {
		return reportFile.replaceFirst(".jrxml", ".jasper");
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public Map<String, Object> getReportParameters() {
		return reportParameters;
	}
	public void setReportParameters(Map<String, Object> reportParameters) {
		this.reportParameters = reportParameters;
	}
	public List<?> getObjects() {
		return objects;
	}
	public void setObjects(List<?> objects) {
		this.objects = objects;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
