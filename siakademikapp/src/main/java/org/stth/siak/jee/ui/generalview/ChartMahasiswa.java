package org.stth.siak.jee.ui.generalview;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.ProgramStudi;
import org.vaadin.addon.JFreeChartWrapper;
import com.vaadin.ui.VerticalLayout;

public class ChartMahasiswa extends VerticalLayout{
	private static final long serialVersionUID = -4143893163295849932L;

	public ChartMahasiswa() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		List<ProgramStudi> l = GenericPersistence.findList(ProgramStudi.class);
		int jmlah = 0;
		for (ProgramStudi ps : l) {
			Long jml = MahasiswaPersistence.getJumlahMHSperProdi(ps);
			dataset.setValue(ps.getNama(), jml);
			jmlah+=jml;
		}
		JFreeChart chart = ChartFactory.createPieChart("Data Mahasiswa ("+jmlah+")", dataset);
		PiePlot pPlot = (PiePlot) chart.getPlot();
		pPlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} : {2}"));
		JFreeChartWrapper jcw = new JFreeChartWrapper(chart );
		addComponent(jcw);
	}

}
