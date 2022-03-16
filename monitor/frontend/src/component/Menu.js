import React from "react";
import { Link } from "react-router-dom";

const Menu = () => {
    return (
        <div style={{ border: "solid 1px", background: "red" }}>
            <h1>Menu</h1>
            <ul>
                <li>
                    <Link to={"/dashboard"}>Dashboard</Link>
                </li>
                <li>
                    <Link to={"/dashboard/detail/all"}>Dashboard Detail</Link>
                </li>
            </ul>
        </div>
    );
};

export default Menu;
