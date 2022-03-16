import React from "react";
import { Link } from "react-router-dom";

import * as Common from "../commons/common";

const Menu = () => {
    return (
        <div style={{ border: "solid 1px", background: "red" }}>
            <h1>Menu</h1>
            <ul>
                <li>
                    <Link to={Common.PAGE_DASHBOARD}>Dashboard</Link>
                </li>
                <li>
                    <Link to={Common.PAGE_DASHBOARD_DETAIL}>
                        Dashboard Detail
                    </Link>
                </li>
            </ul>
        </div>
    );
};

export default Menu;
