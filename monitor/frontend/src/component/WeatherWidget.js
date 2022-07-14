import React from 'react'

import styles from './WeatherWidget.module.css'
import axios from 'axios';

function WeatherWidget() {
    const api = {
      key: "6ee1b6ba59abf58f6806b15abc0815b1",
      base: "https://api.openweathermap.org/data/2.5/",
    };

    const city = "Daegu";
    const url = `${api.base}weather?q=${city}&appid=${api.key}`;

    const getWeather = () => {
        axios.get(url).then((responseData) => {
            const data = responseData.data;
            return({
              id: data.weather[0].id,
              temperature: data.main.temp,
              main: data.weather[0].main,
              loading: false,
            });
          });
    }

    return (
    <div className={styles.wrapper}>
        <h1>{getWeather.temperature}</h1>
    </div>	
    )
}

export default WeatherWidget

