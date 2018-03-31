package org.stth.siak.rpt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;


public class ReportOutputGenerator {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	private String reportFile;
	private Map<String,Object> reportParameters;
	private List<?> reportObjects;
	private boolean isMultiReport;
	private List<ReportRawMaterials> reportRawMaterials;
	private String reportTitle;
	private JasperPrint jasperPrint;
	public static String baseReportsPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()+"/WEB-INF/rpt/";


	public ReportOutputGenerator(String reportFile, Map<String,Object>  reportParameters, List<?> objects) {
		this.reportFile = reportFile;
		this.reportParameters = reportParameters;
		this.reportObjects = objects;
		prepareContents();

	}
	public ReportOutputGenerator(ReportRawMaterials rrm) {
		reportFile = rrm.getReportFile();
		reportParameters = rrm.getReportParameters();
		reportObjects = rrm.getObjects();
		this.reportTitle=rrm.getTitle();
		prepareContents();
	}
	public ReportOutputGenerator(List<ReportRawMaterials> rrms, String title) {
		this.reportRawMaterials = rrms;
		this.reportTitle=title;
		isMultiReport = true;
		prepareContents();
	}

	private void prepareContents() {

		try {
			if (isMultiReport){
				runMultipleReport();
			}else{
				runSingleReport();
			}

		} catch (JRException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void runSingleReport() throws JRException, FileNotFoundException{
		FileInputStream fis = new FileInputStream(baseReportsPath+reportFile);
		JasperReport jasperReport = JasperCompileManager.compileReport(fis);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportObjects);
		reportParameters.put("basepath", baseReportsPath);
		jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, ds);
		//exportToPdf(jasperPrint);

	}
	private void runMultipleReport() throws JRException, FileNotFoundException{
		
		List<JasperPrint> jpl = new ArrayList<>();
		for (ReportRawMaterials rrm : reportRawMaterials){
			Map<String, Object> rP = rrm.getReportParameters();
			rP.put("basepath", baseReportsPath);
			JasperReport jasperReport;
			try {
				FileInputStream fis = new FileInputStream(baseReportsPath+rrm.getCompiledReportFile());
				jasperReport = (JasperReport)JRLoader.loadObject(fis);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("compiled report not found, compile first");
				FileInputStream fis = new FileInputStream(baseReportsPath+rrm.getReportFile());
				jasperReport = JasperCompileManager.compileReport(fis);
			}
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(rrm.getObjects());
			JasperPrint jprint = JasperFillManager.fillReport(jasperReport, 
					rrm.getReportParameters(), ds);
			jpl.add(jprint);
		}
		jasperPrint = jpl.get(0);
		if (jpl.size()>1){
			for (int i = 1; i < jpl.size(); i++) {
				List<JRPrintPage> pages = jpl.get(i).getPages();
				for (JRPrintPage jrPrintPage : pages) {
					jasperPrint.addPage(jrPrintPage);
				}
			}
		}
		//exportToPdf(jasperPrint);
		//JRViewer jasperviewer = new JRViewer(jp);

	}
	public String getReportTitle() {
		return reportTitle;
	}

	public StreamResource exportToPdf() throws JRException{
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		exporter.exportReport();
		return new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = 5273295999602621690L;

			@Override
			public InputStream getStream() {
				InputStream is = new ByteArrayInputStream(
						outputStream.toByteArray());
				return is;

			}
		}, reportTitle+".pdf");

	}
	public StreamResource exportToExcel() throws JRException{
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JRXlsExporter exportExcel = new JRXlsExporter();
		exportExcel.setExporterInput(new SimpleExporterInput(jasperPrint));
		exportExcel.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		exportExcel.exportReport();
		return new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = -8046568610681881912L;

			@Override
			public InputStream getStream() {
				InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
				return is;
			}
		}, reportTitle+".xls");
	}

}
