package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete information about vehicles, as well as
 * gather related location and price data when desired.
 */
@Service
public class CarService {
  private final CarRepository repository;
  private final MapsClient mapsClient;
  private final PriceClient priceClient;

  public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
    this.repository = repository;
    this.mapsClient = mapsClient;
    this.priceClient = priceClient;
  }

  /**
   * Gathers a list of all vehicles
   * 
   * @return a list of all vehicles in the CarRepository
   */
  public List<Car> list() {
    return repository.findAll().stream().map(car -> {
      addPriceFor(car);
      addLocationFor(car);
      return car;
    }).collect(Collectors.toList());
  }

  /**
   * Gets car information by ID (or throws exception if non-existent)
   * 
   * @param id the ID number of the car to gather information on
   * @return the requested car's information, including location and price
   */
  public Car findById(Long id) {
    Car car = repository.findById(id).orElseThrow(() -> new CarNotFoundException("No car found with an ID of " + id));
    addPriceFor(car);
    addLocationFor(car);
    return car;
  }

  /**
   * Either creates or updates a vehicle, based on prior existence of car
   * 
   * @param car A car object, which can be either new or existing
   * @return the new/updated car is stored in the repository
   */
  public Car save(Car car) {
    Car toSave = car;

    // if ID passed in, try to find the car to be updated
    if (car.getId() != null) {
      toSave = repository.findById(car.getId()).orElseThrow(() -> new CarNotFoundException("No car found with an ID of " + car.getId()));

      toSave.setDetails(car.getDetails());
      toSave.setLocation(car.getLocation());
    }

    // this will set the ID if it is a new car being added
    toSave = repository.save(toSave);

    addPriceFor(toSave);
    addLocationFor(toSave);
    return toSave;
  }

  /**
   * Deletes a given car by ID
   * 
   * @param id the ID number of the car to delete
   */
  public void delete(Long id) {
    Car car = repository.findById(id).orElseThrow(() -> new CarNotFoundException("No car found with an ID of " + id));
    repository.delete(car);
  }
  
  private void addPriceFor(Car car) {
    car.setPrice(priceClient.getPrice(car.getId()));
  }
  
  private void addLocationFor(Car car) {
    car.setLocation(mapsClient.getAddress(car.getLocation()));
  }
}
