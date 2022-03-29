import React from "react";
import { Link } from "react-router-dom";

import * as Common from "../commons/common";

import WeatherWidget from '../component/WeatherWidget'

import styles from './Menu.module.css'
import MenuGroup from "./MenuGroup";

const Menu = () => {

    // pass string array of menu items to be rendered into 
    // components
    return (
        <aside className={styles.wrapper}>
            <WeatherWidget />
            <ul >
                <li>
                    <Link to={Common.PAGE_DASHBOARD}>Dashboard</Link>
                </li>
                <li>
                    <Link to={Common.PAGE_DASHBOARD_DETAIL}>
                        Dashboard Detail
                    </Link>
                </li>
                <li>
                    <Link to={Common.PAGE_STAT_TRAFFIC}>Stat Traffic</Link>
                </li>
                
                <li>                    
                    <Link to={"/dashboard/test"}>
                        <div className={styles.groupItem}>TEST</div>
                    </Link>
                </li>

            </ul>
            
        </aside>
    );
};

export default Menu;
