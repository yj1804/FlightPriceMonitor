package com.yj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {
	@Test
	public void testExtractFrom() {
		Parser parser = new Parser();
		String from = parser.extractFrom("https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-14&y_s");
		assertEquals("shanghai", from);
	}
	
	@Test
	public void testExtractTo() {
		Parser parser = new Parser();
		String to = parser.extractTo("https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-14&y_s");
		assertEquals("oslo", to);
	}
	
	@Test
	public void testExtractDepartureDate() {
		Parser parser = new Parser();
		String date = parser.extractDepartureDate("https://flights.ctrip.com/international/shanghai-oslo-sha-osl?2018-06-14&y_s");
		assertEquals("2018-06-14", date);
	}
}
