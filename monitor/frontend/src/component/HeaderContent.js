import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useInterval } from '../utils/customHooks'
import { getCurrentDateTime } from "../utils/utils";

import * as Common from '../commons/common'
import { Divider, MediaQuery } from "@mantine/core";

import styles from './HeaderContent.module.css'
import smartCityLogo from "../assets/images/header/logo_smartcitychallenge.png"
import daeguLogo from '../assets/images/header/logo_daegu.png'

const HeaderContent = () => {
    const [time, setTime] = useState();
    const [date, setDate] = useState();
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("currentUser");
        navigate("/login");
    };

    useInterval(() => {
        const t = getCurrentDateTime()
        setTime(t.time)
        setDate(t.date)
    }, 1000)
    

    return (
        <header className={styles.header}>
            <Link to={Common.PAGE_DASHBOARD} 
                className={styles.logoSection}
            >
                <img
                    className={styles.challengeLogo} 
                    src={smartCityLogo} alt="logo for smart city"/>
                <MediaQuery smallerThan="xs" styles={{ width: 0, opacity: 0 }}>
                    <div className={styles.appTitle}>
                        <img className={styles.daeguLogo}   src={daeguLogo} alt="logo for smart city"/>
                        <div><span className={styles.blue}>AI</span>스마트교통관제플랫폼</div>
                    </div>
                </MediaQuery>
            </Link>
        
            <div className={styles.rightSection}>
                <div className={styles.timeBox}>
                    <div className={styles.time}>{date}</div>
                    <Divider orientation="vertical" mx="sm" size="xs" variant="solid" style={{ height: "24px" }}/>
                    <div className={styles.date}>{time}</div>
                </div>

                <div className={styles.userBox}>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </header>
    );
};

export default HeaderContent;
