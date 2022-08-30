package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.controller.OpenWeatherController;

import java.util.concurrent.ConcurrentHashMap;

public interface OpenWeatherService {

    ConcurrentHashMap get(OpenWeatherController.Location location) throws Exception;
}
