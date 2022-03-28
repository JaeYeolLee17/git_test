import React from "react";
import { Link } from "react-router-dom";

import * as Common from "../commons/common";

const Menu = () => {
    return (
        <div >
            <ul>
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
                    <Link to={"/dashboard/test"}> TEST </Link>
                </li>

            </ul>
        </div>
    );
};

export default Menu;
