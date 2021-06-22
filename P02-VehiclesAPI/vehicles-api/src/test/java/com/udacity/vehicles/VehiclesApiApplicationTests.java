package com.udacity.vehicles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;

/**
 * Integration tests require all other services to be running
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VehiclesApiApplicationTests {
  @LocalServerPort
  private int port;
  
  @Autowired
  private TestRestTemplate restTemplate;
  
  @Test
  public void testAddAndDeleteCar() {
    // create
    ResponseEntity<Car> response = restTemplate.postForEntity("http://localhost:" + port + "/cars", getCar(), Car.class);
    assertEquals(1L, response.getBody().getId().longValue());
    assertNotNull(response.getBody().getLocation().getAddress());
    assertNotNull(response.getBody().getLocation().getCity());
    assertNotNull(response.getBody().getLocation().getState());
    assertNotNull(response.getBody().getLocation().getZip());
    assertTrue(response.getBody().getPrice().startsWith("USD "));
    assertEquals(4, response.getBody().getDetails().getNumberOfDoors().intValue());
    
    // update
    Car car = getCar();
    car.setCondition(Condition.NEW);
    
    restTemplate.put("http://localhost:" + port + "/cars/1", car);

    response = restTemplate.getForEntity("http://localhost:" + port + "/cars/1", Car.class);
    assertEquals(Condition.NEW, response.getBody().getCondition());
    
    // delete
    restTemplate.delete("http://localhost:" + port + "/cars/1");

    response = restTemplate.getForEntity("http://localhost:" + port + "/cars/1", Car.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  private Car getCar() {
    Car car = new Car();
    car.setLocation(new Location(40.730610, -73.935242));
    Details details = new Details();
    Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
    details.setManufacturer(manufacturer);
    details.setModel("Impala");
    details.setMileage(32280);
    details.setExternalColor("white");
    details.setBody("sedan");
    details.setEngine("3.6L V6");
    details.setFuelType("Gasoline");
    details.setModelYear(2018);
    details.setProductionYear(2018);
    details.setNumberOfDoors(4);
    car.setDetails(details);
    car.setCondition(Condition.USED);
    return car;
  }
}
