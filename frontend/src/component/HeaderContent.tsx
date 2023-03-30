import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useInterval } from "../utils/customHooks";
import { getCurrentDateTime } from "../utils/utils";
import { useAuthDispatch } from "../provider/AuthProvider";

import * as Common from "../commons/common";
import * as Utils from "../utils/utils";

import styles from "./HeaderContent.module.css";
import smartCityLogo from "../assets/images/logo_smartcitychallenge.png";
import daeguLogo from "../assets/images/logo_daegu.png";

const HeaderContent = () => {
    const [time, setTime] = useState<string>();
    const [date, setDate] = useState<string>();
    const navigate = useNavigate();
    const dispatch = useAuthDispatch();

    const handleLogout = () => {
        if (dispatch !== null) dispatch({ type: "LOGOUT" });

        navigate("/login");
    };

    useInterval(() => {
        const t = getCurrentDateTime();
        setTime(t.time);
        setDate(t.date);
    }, 1000);

    return (
        <header className={styles.header}>
            <Link to={Common.PAGE_DASHBOARD} className={styles.logoSection}>
                <img className={styles.challengeLogo} src={smartCityLogo} alt="logo for smart city" />
                <div className={styles.appTitle}>
                    <img className={styles.daeguLogo} src={daeguLogo} alt="logo for smart city" />
                    <div>
                        <span className={styles.blue}>AI</span>
                        스마트교통관제플랫폼
                    </div>
                </div>
                {/* <MediaQuery smallerThan='xs' styles={{ width: 0, opacity: 0 }}>
                    <div className={styles.appTitle}>
                        <img
                            className={styles.daeguLogo}
                            src={daeguLogo}
                            alt='logo for smart city'
                        />
                        <div>
                            <span className={styles.blue}>AI</span>
                            스마트교통관제플랫폼
                        </div>
                    </div>
                </MediaQuery> */}
            </Link>

            <div className={styles.rightSection}>
                <div className={styles.timeBox}>
                    <div>{date + " " + time}</div>
                </div>
                {/* <MediaQuery smallerThan='sm' styles={{ width: 0, opacity: 0 }}>
                    <div className={styles.timeBox}>
                        <div className={styles.time}>{date}</div>
                        <Divider
                            orientation='vertical'
                            mx='sm'
                            size='xs'
                            variant='solid'
                            className={styles.divider}
                        />
                        <div className={styles.date}>{time}</div>
                    </div>
                </MediaQuery> */}
                <div className={styles.userBox}>
                    <div>안녕하세요 {JSON.parse(Utils.getLocalStorage(Common.CONTEXT_AUTH) || "").user.userId}님</div>
                    <button className={styles.logoutBtn} onClick={handleLogout}>
                        Logout
                    </button>
                </div>
            </div>
        </header>
    );
};

export default HeaderContent;
