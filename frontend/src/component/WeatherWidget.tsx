import React, { useEffect, useState } from 'react'

import styles from './WeatherWidget.module.css'
import axios from 'axios';
import { useAsyncAxios } from '../utils/customHooks';

type weather =  {
  icon: string;
  temperature: string;
}

function WeatherWidget() {
    const api = {
      key: "107ae00c8ec9e7f2cb859c5e00342bc4",
      base: "https://api.openweathermap.org/data/2.5/",
    };

    const city = "Daegu";
    const url = `${api.base}weather?q=${city}&appid=${api.key}`;
    const [weather, setWeather] = useState<weather>();

    useEffect(() => {
      requestWeather();
    }, []);

    const requestAxiosWeather = async() => {
      const response = await axios.get(url);
        if(response !== null){
          response.data.result = "ok";
        }
      return response.data;
    }

    const {
      loading: loadingWeather,
      error: errorWeather,
      data: resultWeather,
      execute: requestWeather,
  } = useAsyncAxios(requestAxiosWeather);

    useEffect(() => {
      if (resultWeather === null) return;

      setWeather({
        icon: resultWeather.weather[0].icon,
        temperature: Math.floor(resultWeather.main.temp - 273.15) + " °",
      });

    }, [resultWeather]);

    useEffect(() => {
        if (errorWeather === null) return;

        console.log("errorWeather", errorWeather);
    }, [errorWeather]);

    const weatherIconMap: Map<string, string> = new Map([
      ["01d" , "ClearSky"],
      ["02d" , "FewClouds"],
      ["03d" , "ScatteredClouds"],
      ["04d" , "BrokenClouds"],
      ["09d" , "ShowerRain"],
      ["10d" , "Rain"],
      ["11d" , "ThunderStorm"],
      ["13d" , "Snow"],
      ["50d" , "Mist"]
    ])

    return (
      <div className={styles.wrapper}>
        {weather !== undefined ? (
          <>
            <img src={`${process.env.PUBLIC_URL}/images/weather/ico-weather-` + weatherIconMap.get(weather.icon) + `.svg`} className={styles.img}></img>
            <div className={styles.textBody}>
              <p className={styles.temperature}>{weather.temperature}</p>
              <p className={styles.city}>대구광역시</p>
            </div>
          </>
        ) : null}
      </div>        
    )
}

export default WeatherWidget

