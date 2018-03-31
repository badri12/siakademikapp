package org.stth.siak.jee.ui.generalview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.ui.util.FileUploader;
import org.stth.siak.util.ExportToExcel;
import org.stth.siak.util.GeneralUtilities;
import org.stth.siak.util.ImportFromExcel;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class CekJudul extends VerticalLayout implements View{
	private static final long serialVersionUID = -2050138030435880678L;
	private Panel pCurTopik;
	private Panel pHasil;
	private List<Mahasiswa> lMHS = GenericPersistence.findList(Mahasiswa.class);
	private Button bDownload;
	private TextField tfPercent;
	private Map<Mahasiswa, List<Mahasiswa>> mapSim;
	private List<Mahasiswa> l;

	public CekJudul() {
		pHasil = new Panel("JUDUL YANG MIRIP");
		pCurTopik = new Panel("DAFTAR TOPIK");
		pCurTopik.setVisible(false);
		TextArea ta = new TextArea();
		ta.setWidth("400px");
		ta.setPlaceholder("Masukkan judul yang diajukan");
		Button bupload = new Button("Upload Topik");
		bupload.setIcon(VaadinIcons.UPLOAD);
		bupload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		String[] typeFile = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		"application/wps-office.xlsx"};
		bupload.addClickListener(e->{
			FileUploader fu = new FileUploader(null, 
					typeFile, 300000, "xlsx");
			getUI().addWindow(fu);
			fu.addCloseListener(close->{
				if (fu.getFile()!=null) {
					l=ImportFromExcel.setTopik(fu.getFile());
					cekExcel(l);
					gridCurTopik(l);
				}
			});
		});
		Button bCek = new Button("Cek");
		bCek.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bCek.setIcon(VaadinIcons.SEARCH);
		bCek.addClickListener(e->{
			String s= ta.getValue();
			if (!s.isEmpty()) {
				List<Mahasiswa> lSimiliar = simil(s, lMHS);
				gridSimiliar(lSimiliar);
				pCurTopik.setVisible(false);
				bDownload.setEnabled(false);
			}
		});
		bDownload = new Button("Download Hasil Cek Topik");
		bDownload.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bDownload.setIcon(VaadinIcons.DOWNLOAD);
		bDownload.setEnabled(false);

		AutocompleteTextField mhs = new AutocompleteTextField();
		mhs.setWidth("400px");
		mhs.setPlaceholder("Inputkan nim atau nama mahasiswa");

		Map<String, Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lMHS) {
			if (m.getStatus()==StatusMahasiswa.AKTIF ) {
				mapMHS.put(m.toString(), m);
			}
		}
		AutocompleteSuggestionProvider acspm = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		mhs.setSuggestionProvider(acspm);
		mhs.setMinChars(3);

		Button bInputJudul = new Button("Simpan judul");
		bInputJudul.setIcon(VaadinIcons.SIGN_IN);
		bInputJudul.setStyleName(ValoTheme.BUTTON_PRIMARY);
		bInputJudul.addClickListener(e->{
			String s= ta.getValue();
			Mahasiswa m = mapMHS.get(mhs.getValue());
			if (m!=null && !s.isEmpty()) {
				m.setJudulSkripsi(s);
				GenericPersistence.merge(m);
				Notification.show("Judul berhasil disimpan");
			}else{
				Notification.show("Isikan judul dan mahasiswa", Notification.Type.ERROR_MESSAGE);
			}
		});

		HorizontalLayout hlJudul = new HorizontalLayout();
		addComponent(ViewFactory.header("Lihat Judul Skripsi Mahasiswa"));
		hlJudul.setSpacing(true);
		hlJudul.addComponents(ta, bCek);
		hlJudul.setComponentAlignment(bCek, Alignment.BOTTOM_LEFT);
		HorizontalLayout hlMhs = new HorizontalLayout();
		hlMhs.addComponents(mhs, bInputJudul);
		hlMhs.setComponentAlignment(bInputJudul, Alignment.BOTTOM_LEFT);
		hlMhs.setSpacing(true);
		HorizontalLayout hlIO = new HorizontalLayout();
		hlIO.addComponents(bupload, bDownload);
		
		tfPercent = new TextField();
		tfPercent.setWidth("400px");
		tfPercent.setPlaceholder("persentase kemiripan dalam angka");
		tfPercent.addValueChangeListener(e->{
			String s=ta.getValue();
			if (l!=null) {
				cekExcel(l);
				gridCurTopik(l);
			}else if (!s.isEmpty()) {
				List<Mahasiswa> lSimiliar = simil(s, lMHS);
				gridSimiliar(lSimiliar);
				pCurTopik.setVisible(false);
				bDownload.setEnabled(false);
			}
		});
		
		addComponents(tfPercent, hlJudul,hlMhs,hlIO, pCurTopik, pHasil);
		setMargin(true);
		setSpacing(true);
	}


	private void cekExcel(List<Mahasiswa> l) {
		mapSim = new HashMap<>();
		Mahasiswa mEmpty = new Mahasiswa();
		mEmpty.setNpm(" ");
		mEmpty.setNama(" ");
		mEmpty.setJudulSkripsi("" );
		System.out.println(l.size());
		for (Mahasiswa m : l) {
			List<Mahasiswa> lSimil = simil(m.getJudulSkripsi(), lMHS);
			List<Mahasiswa> lTopik = new ArrayList<>();
			lTopik.addAll(l);
			lTopik.remove(m);
			List<Mahasiswa> lSimil2 = simil(m.getJudulSkripsi(), lTopik);
			lSimil.addAll(lSimil2);
			mapSim.put(m, lSimil);
		}
		List<Mahasiswa> lToExcel = new ArrayList<>();
		for (Mahasiswa m : mapSim.keySet()) {
			m.setJudulSkripsi(m.getJudulSkripsi()+" (TOPIK)");
			lToExcel.add(m);
			for (Mahasiswa mhs : mapSim.get(m)) {
				if (mhs.getId()==0) {
					mhs.setJudulSkripsi(mhs.getJudulSkripsi()+" (TOPIK)");
				}
				lToExcel.add(mhs);
			}
			lToExcel.add(mEmpty);
		}
		try {
			FileDownloader fd = new FileDownloader(ExportToExcel.createFileExcel(ExportToExcel.getTopik(lToExcel),"Similiar Judul"));
			fd.extend(bDownload);
			bDownload.setEnabled(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void gridCurTopik(List<Mahasiswa> lTopik){	
		Grid<Mahasiswa> g = createGrid(lTopik);
		g.addItemClickListener(e->{
			gridSimiliar(mapSim.get(e.getItem()),"250px");
		});
		g.setHeight("250px");
		pCurTopik.setCaption("DAFTAR TOPIK ("+l.size()+")");
		pCurTopik.setContent(g);
		pCurTopik.setVisible(true);
	}

	
	private Grid<Mahasiswa> createGrid(List<Mahasiswa> lTopik) {
		Grid<Mahasiswa> g = new Grid<>("", lTopik);
		g.setSizeFull();
		g.addColumn(Mahasiswa::getNpm).setCaption("NIM");
		g.addColumn(Mahasiswa::getNama).setCaption("NAMA");
		g.addColumn(Mahasiswa::getJudulSkripsi).setCaption("JUDUL SKRIPSI");
		
		return g;
	}
	private void gridSimiliar(List<Mahasiswa> lSimiliar){
		gridSimiliar(lSimiliar, null);
	}
	private void gridSimiliar(List<Mahasiswa> lSimiliar,String height){
		Grid<Mahasiswa> g = createGrid(lSimiliar);
		if (height!=null) {
			g.setHeight(height);
		}
		pHasil.setContent(g);
	}


	private List<Mahasiswa> simil(String s, List<Mahasiswa> l) {
		Map<String, String> except = new HashMap<>();
		String[] exc = {"sistem", "informasi", "berbasis", "menggunakan", "pada","ud", "desa","analisis", "dan",
				"di","perancangan","kantor", "kecamatan", "kabupaten", "aplikasi","toko","&", "negeri"
				,"data", "rancangan","rancang", "dengan"};
		for (String string : exc) {
			except.put(string, string);
		}
		String topik = s;
		String[] kataTopik = topik.toLowerCase().split(" ");
		Map<String, String> topikC = new HashMap<>();
		for (String string : kataTopik) {
			string = cek(except, topikC, string);
		}
		double percent = 50;
		if (!tfPercent.getValue().isEmpty()) {
			percent = Double.valueOf(tfPercent.getValue());
		}
		List<Mahasiswa> lSimiliar = new ArrayList<Mahasiswa>();
		for (Mahasiswa mahasiswa : l) {
			if (mahasiswa.getJudulSkripsi()!=null&& !mahasiswa.getJudulSkripsi().isEmpty())  {
				//Double same = 0.0;
				String[] kataJudul = mahasiswa.getJudulSkripsi().toLowerCase().split(" ");
				Map<String, String> judul = new HashMap<>();
				for (String string : kataJudul) {
					string = cek(except, judul, string);
				}
				String a = "";
				for (String string : judul.keySet()) {
					a+=string+" ";
				}
				String b = "";
				for (String string : topikC.keySet()) {
					b+=string+" ";
				}
//				for (String kata : topikC.keySet()) {
//					kata = equal(kata);
//					for (String string : judul.keySet()) {
//						string = equal(string);
//						if (kata.equals(string)) {
//							same+=1;
//						}
//					}
//				}
				//Double d = (same*2)/(topikC.size()+judul.size());
				double d = similarText(a, b);
				if (d>percent) {
					Mahasiswa m = new Mahasiswa();
					m.setId(mahasiswa.getId());
					m.setNpm(mahasiswa.getNpm());
					m.setNama(mahasiswa.getNama());
					m.setJudulSkripsi(mahasiswa.getJudulSkripsi()+" ("+GeneralUtilities.formatDecimal(d)+"%)");
					lSimiliar.add(m);
				}
				//System.out.println(d+" : "+a+" "+mahasiswa.getNama());
			}			
		}
		//System.out.println(lMHS.size());
		return lSimiliar;
	}


	private String equal(String string) {
		if (string.equals("website")) {
			string="web";
		}
		return string;
	}


	private String cek(Map<String, String> except, Map<String, String> topikC, String string) {
		string  =equal(string);
		if (string.endsWith(".")) {
			string=string.substring(0, string.length()-1);
		}
		if (!except.containsKey(string) && !string.startsWith("(") ) {
			topikC.put(string, string);
		}
		return string;
	}
	
	private static double similarText(String first, String second)   {
	    first = first.toLowerCase();
	    second = second.toLowerCase();
	    return (double)(similar(first, second)*200)/(first.length()+second.length());
	}

	private static int similar(String first, String second)  { 
	    int p, q, l, sum;
	    int pos1=0;
	    int pos2=0;
	    int max=0;
	    char[] arr1 = first.toCharArray();
	    char[] arr2 = second.toCharArray();
	    int firstLength = arr1.length;
	    int secondLength = arr2.length;

	    for (p = 0; p < firstLength; p++) {
	        for (q = 0; q < secondLength; q++) {
	            for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p+l] == arr2[q+l]); l++);            
	            if (l > max) {
	                max = l;
	                pos1 = p;
	                pos2 = q;
	            }

	        }
	    }
	    sum = max;
	    if (sum > 0) {
	        if (pos1 > 0 && pos2 > 0) {
	            sum += similar(first.substring(0, pos1>firstLength ? firstLength : pos1), second.substring(0, pos2>secondLength ? secondLength : pos2));
	        }

	        if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
	            sum += similar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
	        }
	    }       
	    return sum;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
