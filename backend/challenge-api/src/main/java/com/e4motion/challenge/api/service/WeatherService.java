package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.constant.WeatherArea;
import com.e4motion.challenge.api.dto.WeatherDto;

public interface WeatherService {

    WeatherDto get(WeatherArea area);
}
