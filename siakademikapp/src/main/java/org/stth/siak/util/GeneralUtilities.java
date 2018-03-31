package org.stth.siak.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.stth.jee.persistence.HibernateUtil;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.Semester;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

public class GeneralUtilities {
	public static final Locale LOCALE = new Locale("id");
	public static final String[] AGAMA = new String[] {"ISLAM","HINDU","BUDDHA","KATOLIK","PROTESTAN"};
	/**
	 * 
	 * 
	 * public method to get the database Date and Time
	 */

	public static LocalDateTime getCurrentDBTime() {
		Query<?> sqlQuery = null;
		String queryString = null;
		Timestamp dbTimeStamp = null;
		queryString = "SELECT now()";
		//SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
		Session session = HibernateUtil.getSession();
		try {
			session.beginTransaction();
			sqlQuery = session.createNativeQuery(queryString);
//			sqlQuery.setParameter("param",
//					org.hibernate.type.TimestampType.INSTANCE);
			dbTimeStamp = (Timestamp) sqlQuery.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(dbTimeStamp.toInstant(), ZoneId.systemDefault());
		return ldt;
	}
	public static Date getCurrentDBDate() {
		Query<?> sqlQuery = null;
		String queryString = null;
		Date dbDate = null;
		queryString = "SELECT curdate()";
		Session session = HibernateUtil.getSession();
		try {
			sqlQuery = session.createNativeQuery(queryString);
//			sqlQuery.setParameter("param",
//					org.hibernate.type.DateType.INSTANCE);
			dbDate = (Date) sqlQuery.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return dbDate;
	}
	
	public static List<String> getListTA(Mahasiswa m){
		int awalTa=2013;
		KonfigurasiPersistence kp = new KonfigurasiPersistence();
		String curTA=kp.getCurrentTa();
		List<String> ls = new ArrayList<>();
		int curTAint = Integer.valueOf(curTA.substring(5, curTA.length()));
		if (m!=null) {
			awalTa=m.getAngkatan();
			if (m.getTahunLulus()>2016) {
				curTAint=m.getTahunLulus();
			}
		}
		for (int i = awalTa; i < curTAint; i++) {
			String s = String.valueOf(i)+"-"+String.valueOf(i+1);
			ls.add(s);
		}
		return ls;
	}
	
	public static String getCurrentDBDateStr(){
		//GregorianCalendar c = new GregorianCalendar();
		//c.setTime(getCurrentDBDate());
		DateFormat f = new SimpleDateFormat("dd MMM YYYY", LOCALE);
		String s = f.format(getCurrentDBDate());
		return s;
	}
	public static String getCurrentDBTimeStamp(){
		//GregorianCalendar c = new GregorianCalendar();
		//c.setTime(getCurrentDBDate());
		DateFormat f = new SimpleDateFormat("dd MMMM YYYY HH:mm", LOCALE);
		String s = f.format(getCurrentDBTime());
		return s;
	}
	public static int getCurrentYear(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int thn = c.get(java.util.Calendar.YEAR);
		return thn;
	}
	public static List<Integer> getListAngkatan(){
		List<Integer> li = new ArrayList<>();
		for (int i = 2013; i <= getCurrentYear(); i++) {
			li.add(i);
		}
		return li;
	}
	public static int getCurrentYearLocal(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
		int thn = c.get(java.util.Calendar.YEAR);
		return thn;
	}
	public static int getCurrentMonth(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int mth = c.get(java.util.Calendar.MONTH);
		return mth;
	}
	public static String getLongFormattedDate(LocalDate dt){
		if (dt!=null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM YYYY", LOCALE);
			return dtf.format(dt);
		}
		return "-";
	}
	public static String getLongFormattedDate2(LocalDate dt){
		if (dt!=null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy", LOCALE);
			return dtf.format(dt);
		}
		return "-";
	}
	public static String getMediumFormattedDate(LocalDate dt){
		DateTimeFormatter f =  DateTimeFormatter.ofPattern("dd MMM YYYY", LOCALE);
		return f.format(dt);
	}
	public static String getDay(LocalDate dt){
		DateTimeFormatter f =  DateTimeFormatter.ofPattern("EEEE", LOCALE);
		return f.format(dt);
	}
	public static String getDateHrMi(LocalDate dt){
		DateTimeFormatter f =DateTimeFormatter.ofPattern("dd MMM YYYY HH:mm", LOCALE);
		return f.format(dt);
	}
	public static String getHrMi(LocalDate dt){
		DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm", LOCALE);
		return f.format(dt);
	}
	public static String getCurrentTA(){
		Calendar c  = Calendar.getInstance();
		c.setTime(getCurrentDBDate());
		int thn = c.get(java.util.Calendar.YEAR);
		String thns = String.valueOf(thn)+"-"+(String.valueOf(thn+1));
		return thns;
	}
	public static String getCurTA(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
		int thn = c.get(java.util.Calendar.YEAR);
		String thns;
		if (c.get(java.util.Calendar.MONTH)<7) {
			thns = String.valueOf(thn-1)+"-"+(String.valueOf(thn));
		}else{
			thns = String.valueOf(thn)+"-"+(String.valueOf(thn+1));
		}
		
		return thns;
	}
	public static Semester getCurSMS(){
		Calendar c  = Calendar.getInstance();
		c.setTime(new Date());
		int bulan = c.get(java.util.Calendar.MONTH);
		System.out.println(bulan);
		if (bulan<7) {
			return Semester.GENAP;
		}
		return Semester.GANJIL;
	}
	
	
	public static Semester genapGanjilEnumFromInt(int i){
		if ((i % 2) == 0){
			return Semester.GENAP;
		}
		return Semester.GANJIL;
	}
	public static String genapGanjilFromInt(int i){
		if ((i % 2) == 0){
			return "GENAP";
		}
		return "GANJIL";
	}
	public static int genapGanjil12FromInt(int i){
		if ((i % 2) == 0){
			return 2;
		}
		return 1;
	}
	public static int genapGanjilToInt(String s){
		if (s == "GENAP"){
			return 2;
		}
		if (s == "GANJIL"){
			return 1;
		}
		return 0;
	}
	public boolean isGenap(int i){
		if ((i % 2) == 0){
			return true;
		}
		return false;
	}

	public static List<Date> getDates(Date tglMulai, Date tglAkhir){
		List<Date> dates = new ArrayList<>();
		Calendar c,e;
		c = Calendar.getInstance();
		c.setTime(tglMulai);
		e = Calendar.getInstance();
		e.setTime(tglAkhir);
		e.add(Calendar.DATE, 1);
		while (c.before(e)){
			dates.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		return dates;
	}
	
	public static Date truncateDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	public static Date truncateNextDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	public static int semesterMahasiswa(Mahasiswa m,Semester s, String ta){
		int semester=1;
		int angkatan = m.getAngkatan();
		int curTa=Integer.parseInt(ta.substring(0, 4));
		semester+=(curTa-angkatan)*2;
		if (s.equals(Semester.GENAP)) {
			semester++;
		}
		return semester;
		
	}
	public static int getBit(int n, int pos) {
		return (n >> pos) & 1;
	}
	public static String formatDecimal(double d){
		return formatDecimal(d, 2);
	}
	public static String formatDecimal(double d, int point){
		String form = ".#";
		if (point>1) {
			for (int i = 1; i < point; i++) {
				form+="#";
			}
		}
		DecimalFormat df= new DecimalFormat(form);
		return df.format(d);
	}
	/*public static Container createContainerFromEnumClass(Class<? extends Enum<?>> enumClass) {
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		for (Object enumConstant : enumClass.getEnumConstants()) {
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString());
		}

		return createContainerFromMap(enumMap);
	}
	public static String CAPTION_PROPERTY_NAME = "caption";

	public static Container createContainerFromMap(Map<?, String> hashMap) {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(CAPTION_PROPERTY_NAME, String.class, "");

		Iterator<?> iter = hashMap.keySet().iterator();
		while(iter.hasNext()) {
			Object itemId = iter.next();
			container.addItem(itemId);
			container.getItem(itemId).getItemProperty(CAPTION_PROPERTY_NAME).setValue(hashMap.get(itemId));
		}

		return container;
	}*/
	
	

}
