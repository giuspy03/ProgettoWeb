package com.AutolavaggioDomicilio.demo.Controller;

public class SlotNotAvailableException extends RuntimeException {
  public SlotNotAvailableException(String message) {
    super(message);
  }
}