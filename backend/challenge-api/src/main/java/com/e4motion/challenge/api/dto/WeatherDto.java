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

    private Coord coord;

    private List<Weather> weather;

    private String base;

    private Main main;

    private int visibility;

    private Wind wind;

    private Clouds clouds;

    private long dt;

    private Sys sys;

    private int timezone;

    private long id;

    private String name;

    private int cod;

    @Data
    public static class Coord {
        private float lon;
        private float lat;
    }
    @Data
    public static class Weather {

        private int id;

        private String main;

        private String description;

        private String icon;
    }

    @Data
    public static class Main {

        private float temp;

        private float feels_like;

        private float temp_min;

        private float temp_max;

        private int pressure;

        private float humidity;
    }

    @Data
    public static class Wind {

        private float speed;

        private int deg;
    }

    @Data
    public static class Clouds {

        private int all;
    }

    @Data
    public static class Sys {

        private int type;

        private int id;

        private String country;

        private long sunrise;

        private long sunset;
    }
}
