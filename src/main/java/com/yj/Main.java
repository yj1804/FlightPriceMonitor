package com.yj;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	public static void main(String[] args) {
		LOGGER.info("start..................");
		Parser parser = new Parser();
//		List<String> baseUrls = Arrays
//				.asList(new String[] {
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-04&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-05&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-06&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-07&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-08&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-09&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-10&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-11&y_s",
//						"https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-12&y_s",
//						
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-04&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-05&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-06&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-07&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-08&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-09&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-10&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-11&y_s",
//						"https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?2018-06-12&y_s",
//						
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-04&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-05&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-06&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-07&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-08&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-09&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-10&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-11&y_s",
//						"https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?2018-06-12&y_s",
//						
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-19&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-20&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-21&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-22&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-23&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-24&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-25&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-26&y_s",
//						"https://flights.ctrip.com/international/milan-shanghai-mil-sha?2018-06-27&y_s",
//						});
		List<String> baseUrls = Lists.newLinkedList();
		List<String> shToOslo = UrlGenerator.generate("https://flights.ctrip.com/international/shanghai-oslo-sha-osl?", "2018-06-04", "2018-07-15");
		List<String> shToBergen = UrlGenerator.generate("https://flights.ctrip.com/international/shanghai-bergen-sha-bgo?", "2018-06-04", "2018-07-15");
		List<String> shToStavanger = UrlGenerator.generate("https://flights.ctrip.com/international/shanghai-stavanger-sha-svg?", "2018-06-04", "2018-07-15");
		List<String> milanToSh = UrlGenerator.generate("https://flights.ctrip.com/international/milan-shanghai-mil-sha?", "2018-06-04", "2018-07-15");
		
		baseUrls.addAll(shToStavanger);
		baseUrls.addAll(shToOslo);
		baseUrls.addAll(shToBergen);
		baseUrls.addAll(milanToSh);
		parser.parse(baseUrls);

	}
}
