import React from "react";
import { useLocation } from "react-router-dom";

const Header = () => {
    const location = useLocation();
    console.log(location.pathname);

    return (
        <div style={{ color: "red", border: "solid 1px", background: "blue" }}>
            <h1>Header</h1>
        </div>
    );
};

export default Header;
