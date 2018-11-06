package com.yj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class UrlGenerator {
	public static List<String> generate(String url, String fromDate, String toDate)  {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> urls = Lists.newLinkedList();
		Date from = sdf.parse(fromDate);
		Date to = sdf.parse(toDate);
		while(from.before(to)) {
			urls.add(url + sdf.format(from) + "&y_s");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(from);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			from = calendar.getTime();
		}
		return urls;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
