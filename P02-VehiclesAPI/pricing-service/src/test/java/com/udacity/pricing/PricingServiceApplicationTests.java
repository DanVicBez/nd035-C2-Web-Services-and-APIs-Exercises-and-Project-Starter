package com.udacity.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.udacity.pricing.domain.price.Price;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "eureka.client.enabled:false")
public class PricingServiceApplicationTests {
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testGetPrice() throws Exception {
		Price price = restTemplate.getForObject("http://localhost:" + port + "/prices/1", Price.class);
		assertEquals("USD", price.getCurrency());
		assertEquals(BigDecimal.valueOf(5794.49), price.getPrice());
	}
}
