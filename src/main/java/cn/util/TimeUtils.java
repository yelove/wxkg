package cn.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	private final static String yMdHms= "yyyy-MM-dd HH:mm:ss";
	
	public static String date2str(Date date){
		SimpleDateFormat time = new SimpleDateFormat(yMdHms);
		return time.format(date);
	}

}
