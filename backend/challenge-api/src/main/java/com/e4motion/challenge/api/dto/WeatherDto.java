package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {

    private String base;

    private Clouds clouds;

    private int cod;

    private Coord coord;

    private long dt;

    private int id;

    private Main main;

    private String name;

    private Sys sys;

    private int timezone;

    private int visibility;

    private List<Weather> weather;

    private Wind wind;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Coord {

        private float lat;

        private float lon;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Weather {

        private String description;

        private String icon;

        private int id;

        private String main;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Main {

        private float feels_like;

        private float humidity;

        private float temp;

        private float temp_max;

        private float temp_min;

        private int pressure;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wind {

        private int deg;

        private float speed;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Clouds {

        private int all;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sys {

        private String country;

        private int id;

        private long sunrise;

        private long sunset;

        private int type;
    }
}
