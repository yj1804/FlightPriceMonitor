package com.yj;

import static net.logstash.logback.marker.Markers.appendEntries;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {
	private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);
	private static final Logger REPORTER = LoggerFactory.getLogger("REPORTER");
	WebDriver driver;

	public void parse(List<String> urls) {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		 options.addArguments("--headless");
		driver = new ChromeDriver(options);
		try {
			List<String> remain = new LinkedList<String>();
			for (String baseUrl : urls) {
				try {
					parse(baseUrl);					
				} catch (Exception e) {
					e.printStackTrace();
					remain.add(baseUrl);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!remain.isEmpty()) {
				LOGGER.warn("retry urls: {}", remain);
				try {
					driver.close();
				} catch (Exception e) {

				}
				parse(remain);
			}
		} finally {
			try {
				driver.close();
			} catch (Exception e) {
			}
		}
	}

	String extractFrom(String url) {
		int index = url.lastIndexOf("/");
		String tmp = url.substring(index + 1);
		index = tmp.indexOf("?");
		tmp = tmp.substring(0, index);
		index = tmp.indexOf('-');
		return tmp.substring(0, index);
	}

	String extractTo(String url) {
		int index = url.lastIndexOf("/");
		String tmp = url.substring(index + 1);
		index = tmp.indexOf("?");
		tmp = tmp.substring(0, index);
		index = tmp.indexOf('-');
		tmp = tmp.substring(index + 1);
		index = tmp.indexOf('-');
		return tmp.substring(0, index);
	}

	String extractDepartureDate(String url) {
		int startIndex = url.indexOf("?");
		int endIndex = url.indexOf("&");

		return url.substring(startIndex + 1, endIndex);
	}

	public void parse(String baseUrl) throws Exception {
		LOGGER.info("parse url {}", baseUrl);
		driver.get(baseUrl);
		Thread.sleep(30000);
		WebElement flightsList = driver.findElement(By.className("flights-list"));
		List<WebElement> flightItems = flightsList.findElements(By.className("flight-item"));
		List<Ticket> tickets = new LinkedList<Ticket>();
		for (WebElement flightItem : flightItems) {
			Ticket ticket = parseFlightItem(flightItem);
			ticket.setCaptureDate(getCurDate());
			ticket.setFrom(extractFrom(baseUrl));
			ticket.setTo(extractTo(baseUrl));
			ticket.setDepartureDate(extractDepartureDate(baseUrl));
			// LOGGER.info(ticket.toString());
			// LOGGER.info("ticket {} {} {} {} {} {}", value("price",
			// ticket.getTotalPrice()),
			// value("airlineName", ticket.getAirlineName()),
			// value("productshowname", ticket.getProductshowname()),
			// value("flightNo", ticket.getFlightNo()), value("departureTime",
			// ticket.getDepartureTime()),
			// value("landingTime", ticket.getLandingTime()));
			Map<String, Object> ticketMap = new HashMap<String, Object>();
			ticketMap.put("from", ticket.getFrom());
			ticketMap.put("to", ticket.getTo());
			ticketMap.put("price", ticket.getTotalPrice());
			ticketMap.put("airlineName", ticket.getAirlineName());
			ticketMap.put("productshowname", ticket.getProductshowname());
			ticketMap.put("flightNo", ticket.getFlightNo().toString());
			ticketMap.put("departureTime", ticket.getDepartureTime());
			ticketMap.put("landingTime", ticket.getLandingTime());
			ticketMap.put("departureDate", ticket.getDepartureDate());
			ticketMap.put("captureDate", ticket.getCaptureDate());
			// LOGGER.info(append("price", ticket.getTotalPrice()), "ticket");
			REPORTER.info(appendEntries(ticketMap), "ticket");
			tickets.add(ticket);
		}
		Collections.sort(tickets);
		LOGGER.info("all tickets: {}" + tickets.toString());

	}

	private String getCurDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	private Ticket parseFlightItem(WebElement flightItem) {
		WebElement flightRow = flightItem.findElement(By.className("flight-row"));
		Ticket ticket = parseFlightRow(flightRow);

		WebElement seatsElement = flightItem.findElement(By.className("seats-list"));
		List<WebElement> seats = seatsElement.findElements(By.className("seat-row"));
		for (WebElement seat : seats) {
			String productshowname = seat.getAttribute("productshowname");
			if ("¡Ù—ß…˙".equals(productshowname)) {
				continue;
			}
			double totalPrice = Double.parseDouble(seat.getAttribute("totalprice"));
			if (totalPrice < ticket.getTotalPrice()) {
				ticket.setTotalPrice(totalPrice);
				ticket.setProductshowname(productshowname);
			}
		}

		List<WebElement> flightNos = flightItem.findElements(By.className("flight-detail-section"));
		for (WebElement flightNo : flightNos) {
			ticket.getFlightNo().add(flightNo.getAttribute("flightno"));
		}
		return ticket;
	}

	private Ticket parseFlightRow(WebElement flightRow) {
		Ticket ticket = new Ticket();
		String airlineName = flightRow.findElement(By.className("airline-name")).getText();
		String arrStop = null;
		try {
			arrStop = flightRow.findElement(By.className("arr-stop")).getText();
		} catch (NoSuchElementException e) {
			try {
				arrStop = flightRow.findElement(By.className("arr-nostop")).getText();
			} catch (NoSuchElementException eœ») {

			}
		}
		String flightTotalTime = flightRow.findElement(By.className("flight-total-time")).getText();
		List<WebElement> times = flightRow.findElements(By.className("flight-detail-time"));

		ticket.setAirlineName(airlineName);
		ticket.setArrStop(arrStop);
		ticket.setFlightTotalTime(flightTotalTime);
		ticket.setDepartureTime(times.get(0).getText());
		ticket.setLandingTime(times.get(1).getText());
		return ticket;
	}

	private static class Ticket implements Comparable {
		private String from;
		private String to;
		private String airlineName;
		private String arrStop;
		private String flightTotalTime;
		private String productshowname;
		private String departureTime;
		private String departureDate;
		private String landingTime;
		private String captureDate;

		private List<String> flightNo = new LinkedList<String>();
		private double totalPrice = Integer.MAX_VALUE;

		public String getCaptureDate() {
			return captureDate;
		}

		public void setCaptureDate(String captureDate) {
			this.captureDate = captureDate;
		}

		public String getDepartureDate() {
			return departureDate;
		}

		public void setDepartureDate(String departureDate) {
			this.departureDate = departureDate;
		}

		public List<String> getFlightNo() {
			return flightNo;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public void setFlightNo(List<String> flightNo) {
			this.flightNo = flightNo;
		}

		public String getDepartureTime() {
			return departureTime;
		}

		public void setDepartureTime(String departureTime) {
			this.departureTime = departureTime;
		}

		public String getLandingTime() {
			return landingTime;
		}

		public void setLandingTime(String landingTime) {
			this.landingTime = landingTime;
		}

		public String getAirlineName() {
			return airlineName;
		}

		public void setAirlineName(String airlineName) {
			this.airlineName = airlineName;
		}

		public String getArrStop() {
			return arrStop;
		}

		public void setArrStop(String arrStop) {
			this.arrStop = arrStop;
		}

		public String getFlightTotalTime() {
			return flightTotalTime;
		}

		public void setFlightTotalTime(String flightTotalTime) {
			this.flightTotalTime = flightTotalTime;
		}

		public String getProductshowname() {
			return productshowname;
		}

		public void setProductshowname(String productshowname) {
			this.productshowname = productshowname;
		}

		public double getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("price: ").append(totalPrice).append(" airlineName: ").append(airlineName).append(" arrStop: ")
					.append(arrStop).append(" productshowname:").append(productshowname).append(" ")
					.append(departureTime).append("-").append(landingTime).append(" flightNo:").append(flightNo)
					.append(" departureDate:").append(departureDate).append(" captureDate:").append(captureDate)
					.append('\n');
			return sb.toString();
		}

		public int compareTo(Object o) {
			return Double.compare(this.totalPrice, ((Ticket) o).totalPrice);
		}
	}
}
