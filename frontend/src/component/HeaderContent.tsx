import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useInterval } from "../utils/customHooks";
import { getCurrentDateTime } from "../utils/utils";
import { useAuthDispatch } from "../provider/AuthProvider";

import * as Common from "../commons/common";

// import styles from "./HeaderContent.module.css";
// import smartCityLogo from "../assets/images/header/logo_smartcitychallenge.png";
// import daeguLogo from "../assets/images/header/logo_daegu.png";

const HeaderContent = () => {
    const [time, setTime] = useState<string>();
    const [date, setDate] = useState<string>();
    const navigate = useNavigate();
    const dispatch = useAuthDispatch();

    const handleLogout = () => {
        //localStorage.removeItem("currentUser");

        if (dispatch !== null) dispatch({ type: "LOGOUT" });

        navigate("/login");
    };

    useInterval(() => {
        const t = getCurrentDateTime();
        setTime(t.time);
        setDate(t.date);
    }, 1000);

    return (
        <header>
            <Link to={Common.PAGE_DASHBOARD}>
                <img alt='logo for smart city' />
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

            <div>
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
                <div>
                    <div>
                        안녕하세요{" "}
                        {
                            JSON.parse(
                                localStorage.getItem("currentUser") || ""
                            ).user.userId
                        }
                        님
                    </div>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </header>
    );
};

export default HeaderContent;
