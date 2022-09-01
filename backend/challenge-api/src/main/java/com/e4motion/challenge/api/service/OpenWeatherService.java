package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.Area;
import com.e4motion.challenge.api.dto.WeatherDto;

public interface OpenWeatherService {

    WeatherDto get(Area area);
}
