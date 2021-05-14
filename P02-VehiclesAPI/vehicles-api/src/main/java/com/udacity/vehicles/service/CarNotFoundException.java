package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Car not found")
public class CarNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -1041220383152339297L;

  public CarNotFoundException() {}

  public CarNotFoundException(String message) {
    super(message);
  }
}
