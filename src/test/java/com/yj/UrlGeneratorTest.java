package com.yj;

import java.text.ParseException;

import org.junit.Test;

public class UrlGeneratorTest {
	@Test
	public void testGenerate() throws ParseException {
		System.out.println(UrlGenerator.generate("https://flights.ctrip.com/international/shanghai-oslo-sha-osl?", "2018-06-10", "2018-07-10"));
	}
}
